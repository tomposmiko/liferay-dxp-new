/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import {ClayPaginationBarWithBasicItems} from '@clayui/pagination-bar';
import {useMemo} from 'react';
import {CSVLink} from 'react-csv';

import Table from '../../common/components/Table';
import TableHeader from '../../common/components/TableHeader';
import CheckboxFilter from '../../common/components/TableHeader/Filter/components/CheckboxFilter';
import DropDownWithDrillDown from '../../common/components/TableHeader/Filter/components/DropDownWithDrillDown';
import DateFilter from '../../common/components/TableHeader/Filter/components/filters/DateFilter';
import Search from '../../common/components/TableHeader/Search';
import {LiferayPicklistName} from '../../common/enums/liferayPicklistName';
import {MDFClaimColumnKey} from '../../common/enums/mdfClaimColumnKey';
import useLiferayNavigate from '../../common/hooks/useLiferayNavigate';
import usePagination from '../../common/hooks/usePagination';
import {MDFClaimListItem} from '../../common/interfaces/mdfClaimListItem';
import TableColumn from '../../common/interfaces/tableColumn';
import getDropDownFilterMenus from '../../common/utils/getDropDownFilterMenus';
import {isPartnerManager} from '../../common/utils/isPartnerManager';
import useDynamicFieldEntries from './hooks/useDynamicFieldEntries';
import useFilters from './hooks/useFilters';
import useGetListItemsFromMDFClaims from './hooks/useGetListItemsFromMDFClaims';
import {INITIAL_FILTER} from './utils/constants/initialFilter';
import getMDFClaimListColumns from './utils/getMDFClaimListColumns';

type MDFClaimItem = {
	[key in MDFClaimColumnKey]?: any;
};

const MDFClaimList = () => {
	const {
		accountRoleEntries,
		companiesEntries,
		fieldEntries,
		roleEntries,
	} = useDynamicFieldEntries();

	const {filters, filtersTerm, onFilter, setFilters} = useFilters();

	const pagination = usePagination();
	const {data, isValidating} = useGetListItemsFromMDFClaims(
		pagination.activePage,
		pagination.activeDelta,
		filtersTerm
	);

	const isPartnerManagerRole = useMemo(() => {
		if (companiesEntries) {
			const roles = accountRoleEntries(
				companiesEntries[0]?.value as number
			);

			return roles && isPartnerManager(roles);
		}

		return false;
	}, [accountRoleEntries, companiesEntries]);

	const siteURL = useLiferayNavigate();

	const columns = getMDFClaimListColumns(
		isPartnerManagerRole,
		siteURL,
		roleEntries
	);

	const getTable = (
		totalCount: number,
		items?: MDFClaimItem[],
		columns?: TableColumn<MDFClaimListItem>[]
	) => {
		if (items && columns) {
			if (!totalCount) {
				return (
					<div className="d-flex justify-content-center mt-4">
						<ClayAlert
							className="m-0 w-50"
							displayType="info"
							title="Info:"
						>
							No entries were found
						</ClayAlert>
					</div>
				);
			}

			return (
				<div className="mt-3">
					<Table<MDFClaimListItem> columns={columns} rows={items} />

					<ClayPaginationBarWithBasicItems
						{...pagination}
						totalItems={totalCount}
					/>
				</div>
			);
		}
	};

	return (
		<div className="border-0 my-4">
			<h1>MDF Claim</h1>

			<TableHeader>
				<div className="d-flex">
					<div>
						<Search
							onSearchSubmit={(searchTerm: string) =>
								onFilter({
									searchTerm,
								})
							}
						/>

						<div className="bd-highlight flex-shrink-2 mt-1">
							{!!filters.searchTerm &&
								!!data.items?.length &&
								!isValidating && (
									<div>
										<p className="font-weight-semi-bold m-0 ml-1 mt-3 text-paragraph-sm">
											{data.items?.length > 1
												? `${data.items?.length} results for ${filters.searchTerm}`
												: `${data.items?.length} result for ${filters.searchTerm}`}
										</p>
									</div>
								)}

							{filters.hasValue && (
								<ClayButton
									borderless
									className="link"
									onClick={() => {
										onFilter({
											...INITIAL_FILTER,
											searchTerm: filters.searchTerm,
										});
									}}
									small
								>
									<ClayIcon
										className="ml-n2 mr-1"
										symbol="times-circle"
									/>
									Clear All Filters
								</ClayButton>
							)}
						</div>
					</div>

					<DropDownWithDrillDown
						className=""
						initialActiveMenu="x0a0"
						menus={getDropDownFilterMenus([
							{
								component: (
									<DateFilter
										dateFilters={(dates: {
											endDate: string;
											startDate: string;
										}) => {
											onFilter({
												dateCreated: {
													dates,
												},
											});
										}}
										filterDescription="Claim Submitted "
									/>
								),
								name: 'Date Submitted',
							},
							{
								component: (
									<CheckboxFilter
										availableItems={fieldEntries[
											LiferayPicklistName.MDF_CLAIM_STATUS
										]?.map<string>(
											(status) => status.label as string
										)}
										clearCheckboxes={
											!filters.status.value?.length
										}
										updateFilters={(checkedItems) =>
											setFilters((previousFilters) => ({
												...previousFilters,
												status: {
													...previousFilters.status,
													value: checkedItems,
												},
											}))
										}
									/>
								),
								name: 'Status',
							},
							{
								component: (
									<CheckboxFilter
										availableItems={companiesEntries?.map<
											string
										>((company) => company.label as string)}
										clearCheckboxes={
											!filters.partner.value?.length
										}
										updateFilters={(checkedItems) =>
											setFilters((previousFilters) => ({
												...previousFilters,
												partner: {
													...previousFilters.status,
													value: checkedItems,
												},
											}))
										}
									/>
								),
								name: 'Partner',
							},
							{
								component: (
									<CheckboxFilter
										availableItems={['Full', 'Partial']}
										clearCheckboxes={
											!filters.type.value?.length
										}
										updateFilters={(checkedItems) =>
											setFilters((previousFilters) => ({
												...previousFilters,
												type: {
													...previousFilters.type,
													value: checkedItems,
												},
											}))
										}
									/>
								),
								name: 'Type',
							},
						])}
						trigger={
							<ClayButton borderless className="btn-secondary">
								<span className="inline-item inline-item-before">
									<ClayIcon symbol="filter" />
								</span>
								Filter
							</ClayButton>
						}
					/>
				</div>

				<div className="mb-2 mb-lg-0">
					{!!data.items?.length && (
						<CSVLink
							className="btn btn-secondary mr-2"
							data={data.items}
							filename="MDF Claim.csv"
						>
							Export MDF Claim
						</CSVLink>
					)}
				</div>
			</TableHeader>

			{!isValidating &&
				getTable(data.totalCount || 0, data.items, columns)}

			{isValidating && <ClayLoadingIndicator />}
		</div>
	);
};
export default MDFClaimList;
