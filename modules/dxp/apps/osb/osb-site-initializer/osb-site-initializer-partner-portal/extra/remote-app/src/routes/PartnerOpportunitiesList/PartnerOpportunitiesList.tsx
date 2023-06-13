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
import ClayLoadingIndicator from '@clayui/loading-indicator';
import {useModal} from '@clayui/modal';
import {ClayPaginationBarWithBasicItems} from '@clayui/pagination-bar';
import {useState} from 'react';
import {CSVLink} from 'react-csv';

import Modal from '../../common/components/Modal';
import Table from '../../common/components/Table';
import TableHeader from '../../common/components/TableHeader';
import Search from '../../common/components/TableHeader/Search';
import {PartnerOpportunitiesColumnKey} from '../../common/enums/partnerOpportunitiesColumnKey';
import {PRMPageRoute} from '../../common/enums/prmPageRoute';
import useLiferayNavigate from '../../common/hooks/useLiferayNavigate';
import usePagination from '../../common/hooks/usePagination';
import DealRegistrationDTO from '../../common/interfaces/dto/dealRegistrationDTO';
import TableColumn from '../../common/interfaces/tableColumn';
import {Liferay} from '../../common/services/liferay';
import getDoubleParagraph from '../../common/utils/getDoubleParagraph';
import ModalContent from './components/ModalContent';
import useFilters from './hooks/useFilters';
import useGetListItemsFromPartnerOpportunities from './hooks/useGetListItemsFromPartnerOpportunities';
import PartnerOpportunitiesItem from './interfaces/partnerOpportunitiesItem';

interface IProps {
	columnsDates: TableColumn<PartnerOpportunitiesItem>[];
	getDates: (
		items: DealRegistrationDTO
	) => PartnerOpportunitiesItem | undefined;
	getFilteredItems: (
		items: PartnerOpportunitiesItem[]
	) => PartnerOpportunitiesItem[];
	name: string;
	sort: string;
}

const PartnerOpportunitiesList = ({
	columnsDates,
	getDates,
	getFilteredItems,
	name,
	sort,
}: IProps) => {
	const {filters, filtersTerm, onFilter} = useFilters();
	const [isVisibleModal, setIsVisibleModal] = useState(false);
	const [modalContent, setModalContent] = useState<
		PartnerOpportunitiesItem
	>();
	const {observer, onClose} = useModal({
		onClose: () => setIsVisibleModal(false),
	});

	const pagination = usePagination();
	const {data, isValidating} = useGetListItemsFromPartnerOpportunities(
		getDates,
		pagination.activePage,
		pagination.activeDelta,
		filtersTerm,
		sort
	);

	const filteredData = data.items && getFilteredItems(data.items);

	const siteURL = useLiferayNavigate();
	const columns = [
		{
			columnKey: PartnerOpportunitiesColumnKey.ACCOUNT_NAME,
			label: 'Account Name',
		},
		...columnsDates,
		{
			columnKey: PartnerOpportunitiesColumnKey.DEAL_AMOUNT,
			label: 'Deal Amount',
		},
		{
			columnKey: PartnerOpportunitiesColumnKey.PARTNER_REP_NAME,
			label: getDoubleParagraph('Partner Rep', 'Name'),
		},
		{
			columnKey: PartnerOpportunitiesColumnKey.PARTNER_REP_EMAIL,
			label: getDoubleParagraph('Partner Rep', 'Email'),
		},
		{
			columnKey: PartnerOpportunitiesColumnKey.LIFERAY_REP,
			label: 'Liferay Rep',
		},
		{
			columnKey: PartnerOpportunitiesColumnKey.STAGE,
			label: 'Stage',
		},
	];

	const handleCustomClickOnRow = (row: PartnerOpportunitiesItem) => {
		setIsVisibleModal(true);
		setModalContent(row);
	};

	const getModal = () => {
		return (
			<Modal observer={observer} size="lg">
				<ModalContent content={modalContent} onClose={onClose} />
			</Modal>
		);
	};

	const getTable = (
		totalCount: number,
		items?: PartnerOpportunitiesItem[]
	) => {
		if (items) {
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
					<Table<PartnerOpportunitiesItem>
						borderless
						columns={columns}
						customClickOnRow={handleCustomClickOnRow}
						responsive
						rows={items}
					/>

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
			<h1>{name}</h1>

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
								!!filteredData?.length &&
								!isValidating && (
									<div>
										<p className="font-weight-semi-bold m-0 ml-1 mt-3 text-paragraph-sm">
											{filteredData?.length > 1
												? `${filteredData?.length} results for ${filters.searchTerm}`
												: `${filteredData?.length} result for ${filters.searchTerm}`}
										</p>
									</div>
								)}
						</div>
					</div>
				</div>

				<div>
					{!!filteredData?.length && (
						<CSVLink
							className="btn btn-secondary mb-2 mb-lg-0 mr-2"
							data={filteredData}
							filename={`${name}.csv`}
						>
							Export {name}
						</CSVLink>
					)}

					<ClayButton
						className="mb-2 mb-lg-0 mr-2"
						onClick={() =>
							Liferay.Util.navigate(
								`${siteURL}/${PRMPageRoute.CREATE_DEAL_REGISTRATION}`
							)
						}
					>
						Register New Deal
					</ClayButton>
				</div>
			</TableHeader>

			{isVisibleModal && getModal()}

			{isValidating && <ClayLoadingIndicator />}

			{!isValidating && getTable(filteredData?.length || 0, filteredData)}
		</div>
	);
};
export default PartnerOpportunitiesList;
