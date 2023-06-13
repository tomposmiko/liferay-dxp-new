import * as API from 'shared/api';
import BasePage from 'settings/components/BasePage';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import URLConstants from 'shared/util/url-constants';
import withStatefulPagination from 'shared/hoc/StatefulPagination';
import {applyTimeZone} from 'shared/util/date';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withCurrentUser} from 'shared/hoc';
import {connect, ConnectedProps} from 'react-redux';
import {createOrderIOMap} from 'shared/util/pagination';
import {getDefinitions} from 'shared/util/breadcrumbs';
import {omit} from 'lodash';
import {RootState} from 'shared/store';
import {Routes, toRoute} from 'shared/util/router';
import {Sizes} from 'shared/util/constants';
import {sub} from 'shared/util/lang';
import {User} from 'shared/util/records';

const SearchableEntityTableHOC = withStatefulPagination(
	SearchableEntityTable,
	{
		initialOrderIOMap: createOrderIOMap('fieldName')
	},
	props => omit(props, 'onSearchValueChange')
);

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
	{close, open}
);

type PropsFromRedux = ConnectedProps<typeof connector>;

interface IIndividualAttributesProps
	extends PropsFromRedux,
		React.HTMLAttributes<HTMLElement> {
	currentUser: User;
	groupId: string;
}

const IndividualAttributes: React.FC<IIndividualAttributesProps> = ({
	close,
	currentUser,
	groupId,
	open,
	timeZoneId
}) => {
	const openModal = ({dataSources, fieldName}) => () => {
		open(modalTypes.INDIVIDUAL_ATTRIBUTES_MODAL, {
			dataSources,
			fieldName,
			onClose: close
		});
	};

	const authorized = currentUser.isAdmin();

	const FieldNameCell = ({data: {dataSources, fieldName}}) => (
		<td className='table-cell-expand'>
			<div className='content-container'>
				<Button
					display='unstyled'
					onClick={openModal({dataSources, fieldName})}
				>
					{fieldName}
				</Button>
			</div>
		</td>
	);

	return (
		<BasePage
			breadcrumbItems={[
				getDefinitions({groupId}),
				{
					active: true,
					label: Liferay.Language.get('individual-attributes')
				}
			]}
			className='individual-attributes-root'
			groupId={groupId}
			pageDescription={Liferay.Language.get(
				'this-is-the-data-model-of-an-individual.-analytics-cloud-will-take-and-store-the–newest-data-from-all-your-sources'
			)}
			pageTitle={Liferay.Language.get('individual-attributes')}
		>
			<Card>
				<SearchableEntityTableHOC
					columns={[
						{
							accessor: 'fieldName',
							cellRenderer: FieldNameCell,
							className: 'table-cell-expand',
							label: Liferay.Language.get('attribute')
						},
						{
							accessor: 'dataSources',
							className: 'pr-6',
							dataFormatter: dataSources =>
								dataSources.length > 1
									? sub(Liferay.Language.get('x-sources'), [
											dataSources.length
									  ])
									: dataSources[0].dataSourceName,
							label: Liferay.Language.get('sources')
						},
						{
							accessor: 'dateModified',
							className: 'pr-5',
							dataFormatter: dateModified =>
								applyTimeZone(
									dateModified,
									timeZoneId
								).fromNow(),
							label: Liferay.Language.get('last-synced'),
							sortable: false
						}
					]}
					dataSourceFn={API.definitions.searchIndividualAttributes}
					dataSourceParams={{groupId}}
					internalSort
					noResultsRenderer={() => (
						<NoResultsDisplay
							description={
								<>
									{Liferay.Language.get(
										'connect-a-data-source-with-people-data'
									)}

									<a
										className='d-block mb-3'
										href={URLConstants.DataSourceConnection}
										key='DOCUMENTATION'
										target='_blank'
									>
										{Liferay.Language.get(
											'access-our-documentation-to-learn-more'
										)}
									</a>

									{authorized && (
										<Button
											display='primary'
											href={toRoute(
												Routes.SETTINGS_ADD_DATA_SOURCE,
												{
													groupId
												}
											)}
										>
											{Liferay.Language.get(
												'connect-data-source'
											)}
										</Button>
									)}
								</>
							}
							icon={{
								border: false,
								size: Sizes.XXXLarge,
								symbol: 'ac-satellite'
							}}
							spacer
							title={Liferay.Language.get(
								'no-individuals-synced-from-data-sources'
							)}
						/>
					)}
					rowIdentifier='fieldName'
					showFilterAndOrder={false}
					showPagination={false}
				/>
			</Card>
		</BasePage>
	);
};

export default compose<any>(withCurrentUser, connector)(IndividualAttributes);
