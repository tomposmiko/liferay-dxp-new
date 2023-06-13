import Card from 'shared/components/Card';
import ClayButton from '@clayui/button';
import ClayLink from '@clayui/link';
import Constants, {OrderByDirections, Sizes} from 'shared/util/constants';
import CrossPageSelect from 'shared/hoc/CrossPageSelect';
import DataControlRequest from '../queries/DataControlRequestMutation';
import Label from 'shared/components/Label';
import moment from 'moment';
import Nav from 'shared/components/Nav';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import RequestListQuery from '../queries/RequestListQuery';
import URLConstants from 'shared/util/url-constants';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {
	CREATE_DATE,
	createOrderIOMap,
	getGraphQLVariablesFromPagination
} from 'shared/util/pagination';
import {FilterByType, FilterInputType} from 'shared/types';
import {formatDateToTimeZone} from 'shared/util/date';
import {
	GDPRRequestStatuses,
	GDPRRequestTypes,
	RangeKeyTimeRanges
} from 'shared/util/constants';
import {get} from 'lodash';
import {getSafeDisplayValue} from 'shared/util/util';
import {mapListResultsToProps} from 'shared/util/mappers';
import {OrderedMap, Set} from 'immutable';
import {
	PERIOD,
	Routes,
	setUriQueryValues,
	STATUSES,
	toRoute,
	TYPES
} from 'shared/util/router';
import {useMutation, useQuery} from '@apollo/react-hooks';
import {useParams} from 'react-router-dom';
import {useQueryPagination} from 'shared/hooks';
import {User} from 'shared/util/records';
import {
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {withHistory} from 'shared/hoc';

const {
	pagination: {cur: defaultPage}
} = Constants;

const DATE_FORMAT = 'MMM DD, YYYY';

export const REQUEST_TYPE_LABEL_MAP = {
	[GDPRRequestTypes.Access]: Liferay.Language.get('access'),
	[GDPRRequestTypes.Delete]: Liferay.Language.get('delete'),
	[GDPRRequestTypes.Suppress]: Liferay.Language.get('suppress'),
	[GDPRRequestTypes.Unsuppress]: Liferay.Language.get('unsuppress')
};

export const REQUEST_STATUS_LABEL_MAP = {
	[GDPRRequestStatuses.Completed]: Liferay.Language.get('done'),
	[GDPRRequestStatuses.Error]: Liferay.Language.get('error'),
	[GDPRRequestStatuses.Expired]: Liferay.Language.get('done'),
	[GDPRRequestStatuses.Pending]: Liferay.Language.get('pending'),
	[GDPRRequestStatuses.Running]: Liferay.Language.get('running')
};

export const REQUEST_STATUS_DISPLAY_MAP = {
	[GDPRRequestStatuses.Completed]: 'success',
	[GDPRRequestStatuses.Error]: 'danger',
	[GDPRRequestStatuses.Expired]: 'success',
	[GDPRRequestStatuses.Pending]: 'secondary',
	[GDPRRequestStatuses.Running]: 'info'
};

export const FILTER_BY_OPTIONS = [
	{
		key: STATUSES,
		label: Liferay.Language.get('status'),
		values: [
			{
				label: REQUEST_STATUS_LABEL_MAP[GDPRRequestStatuses.Completed],
				value: GDPRRequestStatuses.Completed
			}
		]
	},
	{
		key: TYPES,
		label: Liferay.Language.get('type'),
		values: [
			{
				label: REQUEST_TYPE_LABEL_MAP[GDPRRequestTypes.Access],
				value: GDPRRequestTypes.Access
			},
			{
				label: REQUEST_TYPE_LABEL_MAP[GDPRRequestTypes.Delete],
				value: GDPRRequestTypes.Delete
			},
			{
				label: REQUEST_TYPE_LABEL_MAP[GDPRRequestTypes.Suppress],
				value: GDPRRequestTypes.Suppress
			}
		]
	},
	{
		key: PERIOD,
		label: Liferay.Language.get('period'),
		type: 'radio' as const,
		values: [
			{
				label: Liferay.Language.get('last-seven-days'),
				value: RangeKeyTimeRanges.Last7Days
			},
			{
				label: Liferay.Language.get('last-30-days'),
				value: RangeKeyTimeRanges.Last30Days
			},
			{
				label: Liferay.Language.get('last-90-days'),
				value: RangeKeyTimeRanges.Last90Days
			}
		]
	}
];

export const getTodaysDate = () => moment().utc();

const isDisabled = ({
	completeDate,
	status
}: {
	completeDate: string;
	status: GDPRRequestStatuses;
}): boolean => !completeDate || status !== GDPRRequestStatuses.Completed;

/**
 * Function for searching and filtering requests.
 */
export const searchSelectedFn = ({
	filterBy,
	items,
	query
}: {
	filterBy: FilterByType;
	items: OrderedMap<any, any>;
	query: string;
}): OrderedMap<any, any> => {
	let result: OrderedMap<any, any>;

	const statuses = filterBy.get(STATUSES, Set()).toArray();
	const requestTypes = filterBy.get(TYPES, Set()).toArray();
	const period = filterBy.get(PERIOD, Set()).toArray();

	result = items.filter(item =>
		Object.values(item).some((value: any) =>
			String(getSafeDisplayValue(value, ''))
				.toLowerCase()
				.match(query.toLowerCase())
		)
	) as OrderedMap<any, any>;

	if (statuses.length) {
		result = result.filter(({status}) =>
			statuses.includes(status)
		) as OrderedMap<any, any>;
	}

	if (requestTypes.length) {
		result = result.filter(({type}) =>
			requestTypes.includes(type)
		) as OrderedMap<any, any>;
	}

	if (period.length) {
		const dateLimit = getTodaysDate().subtract(period[0], 'days');

		result = result.filter(({createDate}) =>
			moment(createDate).isAfter(dateLimit)
		) as OrderedMap<any, any>;
	}

	return result;
};

const getFilterOptionType = (filterKey: string): FilterInputType =>
	get(
		FILTER_BY_OPTIONS.find(({key}: {key: string}) => key === filterKey),
		'type',
		'checkbox'
	);

interface IRequestListProps {
	addAlert: Alert.AddAlert;
	close: () => void;
	currentUser: User;
	history: {
		push: (href: string) => void;
	};
	open: (modalType: string, options: object) => void;
	timeZoneId: string;
}

const RequestList: React.FC<IRequestListProps> = ({
	addAlert,
	close,
	currentUser,
	history,
	open,
	timeZoneId
}) => {
	const {delta, filterBy, orderIOMap, page, query} = useQueryPagination({
		filterFields: [STATUSES, TYPES, PERIOD],
		initialOrderIOMap: createOrderIOMap(CREATE_DATE)
	});
	const {groupId} = useParams();

	const {selectedItems} = useSelectionContext();

	const formattedFilterBy = filterBy
		.filterNot(val => val.isEmpty())
		.map((val, key) =>
			getFilterOptionType(key) === 'radio' ? parseInt(val.first()) : val
		)
		.toJS();

	const response = useQuery(RequestListQuery, {
		fetchPolicy: 'no-cache',
		variables: {
			...formattedFilterBy,
			...getGraphQLVariablesFromPagination({
				delta,
				orderIOMap,
				page,
				query
			})
		}
	});

	const {refetch} = response;

	const [addDataControlTask] = useMutation(DataControlRequest);

	const handleOpenNewRequestModal = () => {
		open(modalTypes.NEW_REQUEST_MODAL, {
			groupId,
			onClose: close,
			onSubmit: ({
				emailAddresses,
				fileName,
				types
			}: {
				emailAddresses?: string[];
				fileName?: string;
				types: string[];
			}) => {
				addDataControlTask({
					variables: {
						emailAddresses,
						fileName,
						ownerId: currentUser.id,
						types
					}
				})
					.then(() => {
						analytics.track('Created User Data Request', {
							types,
							uploadedFile: !!fileName
						});

						addAlert({
							alertType: Alert.Types.Success,
							message: Liferay.Language.get(
								'requests-have-been-successfully-submitted'
							)
						});

						history.push(
							setUriQueryValues(
								{
									field: CREATE_DATE,
									keywords: '',
									page: defaultPage,
									sortOrder: OrderByDirections.Descending
								},
								toRoute(
									Routes.SETTINGS_DATA_PRIVACY_REQUEST_LOG,
									{
										groupId
									}
								)
							)
						);

						refetch();

						close();
					})
					.catch(() =>
						addAlert({
							alertType: Alert.Types.Error,
							message: Liferay.Language.get(
								'there-was-an-error-processing-your-request.-please-try-again'
							),
							timeout: false
						})
					);
			}
		});
	};

	return (
		<Card className='request-list-root' pageDisplay>
			<CrossPageSelect
				{...mapListResultsToProps(
					response,
					({dataControlTasks: {dataControlTasks, total}}) => ({
						items: dataControlTasks,
						total
					})
				)}
				checkDisabled={isDisabled}
				columns={[
					{
						accessor: 'batchId',
						label: Liferay.Language.get('request-id'),
						title: true
					},
					{
						accessor: 'emailAddress',
						className: 'table-cell-expand',
						label: Liferay.Language.get('email')
					},
					{
						accessor: 'type',
						dataFormatter: (type: GDPRRequestTypes) =>
							REQUEST_TYPE_LABEL_MAP[type],
						label: Liferay.Language.get('request-type')
					},
					{
						accessor: CREATE_DATE,
						dataFormatter: (date: string) =>
							formatDateToTimeZone(date, DATE_FORMAT, timeZoneId),
						label: Liferay.Language.get('requested-date')
					},
					{
						accessor: 'status',
						cellRenderer: ({
							data: {status}
						}: {
							data: {status: GDPRRequestStatuses};
						}) => (
							<td>
								<Label
									className='status'
									display={REQUEST_STATUS_DISPLAY_MAP[status]}
									size='lg'
									uppercase
								>
									{REQUEST_STATUS_LABEL_MAP[status]}
								</Label>
							</td>
						),
						label: Liferay.Language.get('request-status')
					}
				]}
				delta={delta}
				entityLabel={Liferay.Language.get('requests')}
				filterBy={filterBy}
				filterByOptions={FILTER_BY_OPTIONS}
				flatFilter
				groupId={groupId}
				noResultsRenderer={
					<NoResultsDisplay
						description={
							<>
								{Liferay.Language.get(
									'create-a-request-to-get-started'
								)}

								<a
									className='d-block mb-3'
									href={URLConstants.RequestLogDocumentation}
									key='DOCUMENTATION'
									target='_blank'
								>
									{Liferay.Language.get(
										'access-our-documentation-to-learn-more'
									)}
								</a>
							</>
						}
						icon={{
							border: false,
							size: Sizes.XXXLarge,
							symbol: 'ac-satellite'
						}}
						title={Liferay.Language.get('no-requests-found')}
					/>
				}
				orderIOMap={orderIOMap}
				page={page}
				primary
				query={query}
				renderInlineRowActions={({
					data: {id, status},
					itemsSelected
				}: {
					data: {
						completeDate: string;
						id: string;
						status: GDPRRequestStatuses;
					};
					itemsSelected: boolean;
				}) => {
					if (status === GDPRRequestStatuses.Expired) {
						return (
							<b>{Liferay.Language.get('download-expired')}</b>
						);
					}

					return (
						status === GDPRRequestStatuses.Completed && (
							<ClayLink
								button
								className='button-root'
								{...(!itemsSelected && {
									href: `/o/proxy/download/data-control-tasks/${id}?projectGroupId=${groupId}`
								})}
								aria-disabled={itemsSelected}
								displayType='secondary'
								onClick={() =>
									analytics.track(
										'Downloaded User Data Request'
									)
								}
								small
							>
								{Liferay.Language.get('download')}
							</ClayLink>
						)
					);
				}}
				renderNav={() => (
					<Nav>
						<Nav.Item>
							{selectedItems.size ? (
								<ClayLink
									button
									className='button-root nav-btn'
									displayType='primary'
									href={`/o/proxy/download/data-control-tasks?projectGroupId=${groupId}&filter=(id eq ${selectedItems
										.map(({id}) => id)
										.join(' or id eq ')})`}
									onClick={() =>
										analytics.track(
											'Downloaded User Data Request'
										)
									}
								>
									{Liferay.Language.get('download-all')}
								</ClayLink>
							) : (
								<ClayButton
									className='button-root nav-btn'
									displayType='primary'
									onClick={handleOpenNewRequestModal}
								>
									{Liferay.Language.get('create-request')}
								</ClayButton>
							)}
						</Nav.Item>
					</Nav>
				)}
				searchSelectedFn={searchSelectedFn}
			/>
		</Card>
	);
};

export default compose<any>(
	withSelectionProvider,
	connect(null, {addAlert, close, open}),
	withHistory
)(RequestList);
