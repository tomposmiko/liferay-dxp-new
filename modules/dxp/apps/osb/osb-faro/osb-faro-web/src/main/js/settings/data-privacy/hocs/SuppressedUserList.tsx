import Card from 'shared/components/Card';
import ClayButton from '@clayui/button';
import DataControlRequest from '../queries/DataControlRequestMutation';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import SuppressedUsersListQuery from '../queries/SuppressedUsersListQuery';
import URLConstants from 'shared/util/url-constants';
import {addAlert} from 'shared/actions/alerts';
import {Alert, Router} from 'shared/types';
import {
	compose,
	withBaseResults,
	withQueryPagination,
	withQueryRangeSelectors
} from 'shared/hoc';
import {connect, ConnectedProps} from 'react-redux';
import {CREATE_DATE, createOrderIOMap} from 'shared/util/pagination';
import {formatDateToTimeZone} from 'shared/util/date';
import {
	GDPRRequestStatuses,
	GDPRRequestTypes,
	Sizes
} from 'shared/util/constants';
import {graphql} from '@apollo/react-hoc';
import {sub} from 'shared/util/lang';
import {useMutation} from '@apollo/react-hooks';
import {User} from 'shared/util/records';

const DATE_FORMAT = 'MMM DD, YYYY';

const withData = () =>
	graphql(
		SuppressedUsersListQuery,
		getMetricsMapper(
			({suppressions: {suppressions, total}}) => ({
				items: suppressions,
				total
			}),
			{
				fetchPolicy: 'no-cache'
			},
			SuppressedUsersListQuery
		)
	);

const withQueryOptions = Component => ({
	addAlert,
	currentUser,
	refetch,
	...otherProps
}: Pick<ISuppressedUserListProps, 'addAlert' | 'currentUser'> & {
	refetch: () => Promise<any>;
}) => {
	const [unsuppressUser] = useMutation(DataControlRequest);

	return (
		<Component
			{...otherProps}
			renderInlineRowActions={({
				data: {dataControlTaskStatus, emailAddress}
			}) =>
				dataControlTaskStatus !== GDPRRequestStatuses.Pending && (
					<ClayButton
						className='button-root unsuppress'
						displayType='secondary'
						onClick={() => {
							unsuppressUser({
								variables: {
									emailAddresses: [emailAddress],
									ownerId: currentUser.id,
									types: [GDPRRequestTypes.Unsuppress]
								}
							})
								.then(() => {
									addAlert({
										alertType: Alert.Types.Success,
										message: sub(
											Liferay.Language.get(
												'x-has-been-successfully-unsuppressed'
											),
											[emailAddress]
										) as string
									});

									refetch();
								})
								.catch(() => {
									addAlert({
										alertType: Alert.Types.Error,
										message: sub(
											Liferay.Language.get(
												'there-was-an-error-unsuppressing-x.-please-try-again'
											),
											[emailAddress]
										) as string,
										timeout: false
									});
								});
						}}
						small
					>
						{Liferay.Language.get('unsuppress')}
					</ClayButton>
				)
			}
		/>
	);
};

const SuppressedListWithData = withBaseResults(withData, {
	getColumns: ({timeZoneId}) => [
		{
			accessor: 'emailAddress',
			className: 'table-cell-expand',
			label: Liferay.Language.get('email'),
			title: true
		},
		{
			accessor: 'dataControlTaskBatchId',
			label: Liferay.Language.get('request-id')
		},
		{
			accessor: 'dataControlTaskCreateDate',
			dataFormatter: val =>
				formatDateToTimeZone(val, DATE_FORMAT, timeZoneId),
			label: Liferay.Language.get('requested-date')
		},
		{
			accessor: 'createDate',
			dataFormatter: val =>
				formatDateToTimeZone(val, DATE_FORMAT, timeZoneId),
			label: Liferay.Language.get('suppression-date')
		}
	],
	primary: true,
	showDropdownRangeKey: false,
	withQueryOptions
});

const connector = connect(null, {addAlert});

type PropsFromRedux = ConnectedProps<typeof connector>;

interface ISuppressedUserListProps extends PropsFromRedux {
	currentUser: User;
	router: Router;
	timeZoneId: string;
}

const SuppressedUserList: React.FC<ISuppressedUserListProps> = props => (
	<Card className='suppressed-user-list-root' pageDisplay>
		<SuppressedListWithData
			{...props}
			checkDisabled={({dataControlTaskStatus}) =>
				dataControlTaskStatus === GDPRRequestStatuses.Pending
			}
			entityLabel={Liferay.Language.get('suppressed-users')}
			noResultsRenderer={
				<NoResultsDisplay
					description={
						<>
							{Liferay.Language.get(
								'to-suppress-a-user,-go-to-data-control-&-privacy-under-settings-and-create-a-new-request-on-the-request-log'
							)}

							<a
								className='d-block mb-3'
								href={URLConstants.SuppressedUsersDocumentation}
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
					title={Liferay.Language.get('no-suppressed-users-found')}
				/>
			}
		/>
	</Card>
);

export default compose<any>(
	connector,
	withQueryPagination({initialOrderIOMap: createOrderIOMap(CREATE_DATE)}),
	withQueryRangeSelectors({})
)(SuppressedUserList);
