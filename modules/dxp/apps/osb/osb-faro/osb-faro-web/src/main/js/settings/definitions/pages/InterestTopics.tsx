import * as API from 'shared/api';
import BasePage from 'settings/components/BasePage';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import CrossPageSelect from 'shared/hoc/CrossPageSelect';
import Nav from 'shared/components/Nav';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
import {
	ActionTypes,
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withCurrentUser} from 'shared/hoc';
import {connect, ConnectedProps} from 'react-redux';
import {CREATE_DATE, createOrderIOMap, KEYWORD} from 'shared/util/pagination';
import {formatDateToTimeZone} from 'shared/util/date';
import {getDefinitions} from 'shared/util/breadcrumbs';
import {partition} from 'lodash';
import {RootState} from 'shared/store';
import {Routes, toRoute} from 'shared/util/router';
import {Sizes} from 'shared/util/constants';
import {sub} from 'shared/util/lang';
import {UNAUTHORIZED_ACCESS} from 'shared/util/request';
import {useQueryPagination, useRequest} from 'shared/hooks';
import {User} from 'shared/util/records';

const INITIAL_PAGE = 1;

const connector = connect(
	(store: RootState, {groupId}: {groupId: string}) => ({
		timeZoneId: store.getIn([
			'projects',
			groupId,
			'data',
			'timeZone',
			'timeZoneId'
		])
	}),
	{addAlert, close, open}
);

type PropsFromRedux = ConnectedProps<typeof connector>;

interface IInterestTopicsProps extends PropsFromRedux {
	currentUser: User;
	groupId: string;
	timeZoneId: string;
}

const InterestTopics: React.FC<IInterestTopicsProps> = ({
	addAlert,
	close,
	currentUser,
	groupId,
	open,
	timeZoneId
}) => {
	const {selectedItems, selectionDispatch} = useSelectionContext();

	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(KEYWORD)
	});

	const {data, error, loading, refetch} = useRequest({
		dataSourceFn: API.blockedKeywords.search,
		variables: {
			delta,
			groupId,
			orderIOMap,
			page,
			query
		}
	});

	const handleInsertModal = () => {
		open(modalTypes.INSERT_BLOCKED_KEYWORDS, {
			onClose: close,
			onSubmit: handleAddKeywords
		});
	};

	const handleAddKeywords = (keywords: string[]) => {
		API.blockedKeywords
			.insertMany({groupId, keywords})
			.then(response => {
				analytics.track('Added Interest Keywords to Block List');

				const [duplicate, nonDuplicate] = partition(
					response.items,
					({duplicate}) => duplicate
				);

				if (duplicate.length) {
					addAlert({
						alertType: Alert.Types.Default,
						message: `${duplicate
							.map(({keyword}) => keyword)
							.join(', ')} ${Liferay.Language.get(
							'already-belong-to-the-blocklist'
						)}`
					});
				}

				if (nonDuplicate.length) {
					const nonDuplicatedMessage =
						nonDuplicate.length > 1
							? Liferay.Language.get(
									'x-keywords-added-to-the-blocklist'
							  )
							: Liferay.Language.get(
									'x-keyword-added-to-the-blocklist'
							  );

					addAlert({
						alertType: Alert.Types.Success,
						message: sub(
							nonDuplicatedMessage,
							[
								<b key='nonDuplicateCount'>
									{nonDuplicate.length}
								</b>
							],
							false
						)
					});
				}

				refetch();

				close();
			})
			.catch(() => {
				addAlert({
					alertType: Alert.Types.Error,
					message: Liferay.Language.get('error')
				});
			});
	};

	const handleDeleteKeyword = (ids: string[]) => () => {
		open(modalTypes.CONFIRMATION_MODAL, {
			message: sub(
				Liferay.Language.get(
					'are-you-sure-you-want-to-delete-x-keywords'
				),
				[<b key='confirmDeleteCount'>{ids.length}</b>],
				false
			),
			modalVariant: 'modal-warning',
			onClose: close,
			onSubmit: () =>
				API.blockedKeywords
					.delete({
						groupId,
						ids
					})
					.then(() => {
						const deletedMessage =
							ids.length > 1
								? Liferay.Language.get(
										'x-keywords-have-been-deleted'
								  )
								: Liferay.Language.get(
										'x-keyword-have-been-deleted'
								  );

						addAlert({
							alertType: Alert.Types.Success,
							message: sub(
								deletedMessage,
								[<b key='deleteCount'>{ids.length}</b>],
								false
							)
						});

						selectionDispatch({type: ActionTypes.ClearAll});

						refetch();
					})
					.catch(err =>
						addAlert({
							alertType: Alert.Types.Error,
							message:
								err.message === UNAUTHORIZED_ACCESS
									? Liferay.Language.get(
											'unauthorized-access'
									  )
									: Liferay.Language.get('error'),
							timeout: false
						})
					),
			title: Liferay.Language.get('delete-keyword'),
			titleIcon: 'warning-full'
		});
	};

	const renderNav = () => {
		if (selectedItems.isEmpty()) {
			return (
				<Nav>
					<Nav.Item>
						<Button
							className='nav-btn'
							display='primary'
							onClick={handleInsertModal}
						>
							{Liferay.Language.get('add-keyword')}
						</Button>
					</Nav.Item>
				</Nav>
			);
		} else {
			return (
				<Nav>
					<Button
						borderless
						className='nav-btn'
						display='secondary'
						icon='trash'
						iconAlignment='left'
						onClick={handleDeleteKeyword(
							selectedItems.keySeq().toArray()
						)}
						outline
					/>
				</Nav>
			);
		}
	};

	const renderNoResults = () => {
		const authorized = currentUser.isAdmin();

		const connectMessage = authorized ? (
			<>
				{Liferay.Language.get('add-a-keyword-to-be-blocked')}

				<a
					className='d-block mb-3'
					href={URLConstants.InterestTopicsDocumentation}
					key='DOCUMENTATION'
					target='_blank'
				>
					{Liferay.Language.get('learn-more-about-interest-topics')}
				</a>
			</>
		) : (
			Liferay.Language.get(
				'please-contact-your-site-administrator-to-add-keywords'
			)
		);

		return query ? (
			<NoResultsDisplay
				icon={{symbol: 'star-o'}}
				title={getFormattedTitle(Liferay.Language.get('keywords'))}
			/>
		) : page > INITIAL_PAGE ? (
			<NoResultsDisplay title={Liferay.Language.get('page-not-found')}>
				<Button
					display='secondary'
					href={toRoute(Routes.SETTINGS_DEFINITIONS_INTEREST_TOPICS, {
						groupId
					})}
				>
					{Liferay.Language.get('back-to-interest-topics')}
				</Button>
			</NoResultsDisplay>
		) : (
			<NoResultsDisplay
				description={connectMessage}
				icon={{
					border: false,
					size: Sizes.XXXLarge,
					symbol: 'ac-satellite'
				}}
				primary
				title={Liferay.Language.get('no-keywords-found')}
			/>
		);
	};

	const renderInlineRowActions = ({data: {id}, itemsSelected}) => (
		<Button
			borderless
			disabled={itemsSelected}
			icon='trash'
			iconAlignment='left'
			onClick={handleDeleteKeyword([id])}
			size='sm'
		/>
	);

	const renderPageDescription = () => (
		<>
			<p>
				{Liferay.Language.get(
					'approximates-what-topics-individuals-are-interested-in-based-on-their-interactions-with-registered-touchpoints'
				)}
				<br />
				{Liferay.Language.get(
					'liferay-analytics-cloud-automatically-associates-registered-touchpoints-with-keywords-based-on-their-meta-tags-titles-and-descriptions'
				)}
			</p>

			<h4>{Liferay.Language.get('keywords-blocklist')}</h4>
			<p>
				{Liferay.Language.get(
					'keywords-can-be-excluded-by-adding-them-to-a-blocklist-manage-the-keywords-that-you-dont-want-listed-in-liferay-analytics-cloud-and-dont-want-them-to-be-used-to-generate-content-recommendation-in-liferay-dxp'
				)}
			</p>
		</>
	);

	return (
		<BasePage
			breadcrumbItems={[
				getDefinitions({groupId}),
				{
					active: true,
					label: Liferay.Language.get('interest-topics')
				}
			]}
			groupId={groupId}
			key='interestTopicsPage'
			pageDescription={renderPageDescription()}
			pageTitle={Liferay.Language.get('interest-topics')}
		>
			<Card pageDisplay>
				<CrossPageSelect
					columns={[
						{
							accessor: KEYWORD,
							className: 'table-cell-expand',
							label: Liferay.Language.get('keyword'),
							title: true
						},
						{
							accessor: CREATE_DATE,
							dataFormatter: date =>
								formatDateToTimeZone(date, 'll', timeZoneId),
							label: Liferay.Language.get('added')
						}
					]}
					delta={delta}
					entityLabel={Liferay.Language.get('keywords')}
					error={error}
					items={data?.items}
					loading={loading}
					maxLength={900}
					noResultsRenderer={renderNoResults()}
					orderIOMap={orderIOMap}
					page={page}
					query={query}
					renderInlineRowActions={
						currentUser.isAdmin() ? renderInlineRowActions : null
					}
					renderNav={currentUser.isAdmin() ? renderNav : null}
					rowIdentifier='id'
					showCheckbox={currentUser.isAdmin()}
					total={data?.total}
				/>
			</Card>
		</BasePage>
	);
};

export default compose(
	connector,
	withCurrentUser,
	withSelectionProvider
)(InterestTopics);
