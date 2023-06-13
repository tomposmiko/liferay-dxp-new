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

import {KeyedMutator, mutate} from 'swr';

import Dropdown from '../../../common/components/Dropdown';
import {DropdownOption} from '../../../common/components/Dropdown/Dropdown';
import StatusBadge from '../../../common/components/StatusBadge';
import {MDFColumnKey} from '../../../common/enums/mdfColumnKey';
import {PermissionActionType} from '../../../common/enums/permissionActionType';
import {PRMPageRoute} from '../../../common/enums/prmPageRoute';
import MDFRequestDTO from '../../../common/interfaces/dto/mdfRequestDTO';
import {MDFRequestListItem} from '../../../common/interfaces/mdfRequestListItem';
import TableColumn from '../../../common/interfaces/tableColumn';
import {Liferay} from '../../../common/services/liferay';
import LiferayItems from '../../../common/services/liferay/common/interfaces/liferayItems';
import {ResourceName} from '../../../common/services/liferay/object/enum/resourceName';
import deleteMDFRequest from '../../../common/services/liferay/object/mdf-requests/deleteMDFRequest';
import {Status} from '../../../common/utils/constants/status';

export default function getMDFListColumns(
	hasUserAccountSameAccountEntryCurrentMDFRequest: (
		index: number
	) => boolean | undefined,
	siteURL: string,
	actions?: PermissionActionType[],
	mutated?: KeyedMutator<LiferayItems<MDFRequestDTO[]>>
): TableColumn<MDFRequestListItem>[] | undefined {
	const getDropdownOptions = (row: MDFRequestListItem, index: number) => {
		const isUserAssociated = hasUserAccountSameAccountEntryCurrentMDFRequest(
			index
		);

		const options = actions?.reduce<DropdownOption[]>(
			(previousValue, currentValue) => {
				const currentMDFRequestHasValidStatusToEdit =
					row[MDFColumnKey.STATUS] === Status.DRAFT.name ||
					row[MDFColumnKey.STATUS] === Status.REQUEST_MORE_INFO.name;

				const currentMDFRequestHasValidStatusToDelete =
					row[MDFColumnKey.STATUS] === Status.DRAFT.name;

				if (currentValue === PermissionActionType.VIEW) {
					previousValue.push({
						icon: 'view',
						key: 'approve',
						label: ' View',
						onClick: () =>
							Liferay.Util.navigate(
								`${siteURL}/l/${row[MDFColumnKey.ID]}`
							),
					});
				}

				if (
					(currentValue === PermissionActionType.UPDATE &&
						isUserAssociated &&
						currentMDFRequestHasValidStatusToEdit) ||
					currentValue ===
						PermissionActionType.UPDATE_WO_CHANGE_STATUS
				) {
					previousValue.push({
						icon: 'pencil',
						key: 'edit',
						label: ' Edit',
						onClick: () =>
							Liferay.Util.navigate(
								`${siteURL}/${
									PRMPageRoute.EDIT_MDF_REQUEST
								}/#/${row[MDFColumnKey.ID]}`
							),
					});
				}

				if (
					currentValue === PermissionActionType.DELETE &&
					currentMDFRequestHasValidStatusToDelete
				) {
					previousValue.push({
						icon: 'trash',
						key: 'delete',
						label: ' Delete',
						onClick: () => {
							Liferay.Util.openConfirmModal({
								message: 'Are you sure?',
								onConfirm: async (isConfirmed: boolean) => {
									if (isConfirmed) {
										try {
											await deleteMDFRequest(
												ResourceName.MDF_REQUEST_DXP,
												Number(row[MDFColumnKey.ID])
											);

											Liferay.Util.openToast({
												message:
													'MDF Request successfully deleted!',
												title: 'Success',
												type: 'success',
											});

											mutate(mutated);
										}
										catch (error: unknown) {
											Liferay.Util.openToast({
												message:
													'Fail to delete MDF Request',
												title: 'Error',
												type: 'danger',
											});
										}
									}
								},
							});
						},
					});
				}

				return previousValue;
			},
			[]
		);

		return (
			<Dropdown closeOnClick={true} options={options || []}></Dropdown>
		);
	};

	return [
		{
			columnKey: MDFColumnKey.ID,
			label: 'Request ID',
			render: (data, row) => (
				<a
					className="link"
					onClick={() =>
						Liferay.Util.navigate(
							`${siteURL}/l/${row[MDFColumnKey.ID]}`
						)
					}
				>{`Request-${data}`}</a>
			),
		},
		{
			columnKey: MDFColumnKey.STATUS,
			label: 'Status',
			render: (data) => <StatusBadge status={data as string} />,
		},
		{
			columnKey: MDFColumnKey.ACTIVITY_PERIOD,
			label: 'Activity Period',
		},
		{
			columnKey: MDFColumnKey.PARTNER,
			label: 'Partner',
		},
		{
			columnKey: MDFColumnKey.TOTAL_COST,
			label: 'Total Cost',
		},
		{
			columnKey: MDFColumnKey.NAME,
			label: 'Name',
		},
		{
			columnKey: MDFColumnKey.REQUESTED,
			label: 'Requested',
		},
		{
			columnKey: MDFColumnKey.AMOUNT_CLAIMED,
			label: 'Amout Claimed',
		},
		{
			columnKey: MDFColumnKey.APPROVED,
			label: 'Approved',
		},
		{
			columnKey: MDFColumnKey.PAID,
			label: 'Paid',
		},
		{
			columnKey: MDFColumnKey.DATE_SUBMITTTED,
			label: 'Date Submitted',
		},
		{
			columnKey: MDFColumnKey.LAST_MODIFIED,
			label: 'Last Modified',
		},
		{
			columnKey: MDFColumnKey.ACTION,
			label: '',
			render: (_, row, index) => getDropdownOptions(row, index),
		},
	];
}
