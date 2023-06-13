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
import {MDFClaimColumnKey} from '../../../common/enums/mdfClaimColumnKey';
import {MDFColumnKey} from '../../../common/enums/mdfColumnKey';
import {PermissionActionType} from '../../../common/enums/permissionActionType';
import {PRMPageRoute} from '../../../common/enums/prmPageRoute';
import MDFClaimDTO from '../../../common/interfaces/dto/mdfClaimDTO';
import {MDFClaimListItem} from '../../../common/interfaces/mdfClaimListItem';
import TableColumn from '../../../common/interfaces/tableColumn';
import {Liferay} from '../../../common/services/liferay';
import LiferayItems from '../../../common/services/liferay/common/interfaces/liferayItems';
import {ResourceName} from '../../../common/services/liferay/object/enum/resourceName';
import deleteMDFClaim from '../../../common/services/liferay/object/mdf-claim/deleteMDFClaim';
import {Status} from '../../../common/utils/constants/status';

export default function getMDFClaimListColumns(
	siteURL?: string,
	actions?: PermissionActionType[],
	mutated?: KeyedMutator<LiferayItems<MDFClaimDTO[]>>
): TableColumn<MDFClaimListItem>[] | undefined {
	const getDropdownOptions = (row: MDFClaimListItem) => {
		const options = actions?.reduce<DropdownOption[]>(
			(previousValue, currentValue) => {
				const currentMDFClaimHasValidStatusToEdit =
					row[MDFClaimColumnKey.STATUS] === Status.DRAFT.name ||
					row[MDFClaimColumnKey.STATUS] ===
						Status.REQUEST_MORE_INFO.name;

				const currentMDFClaimHasValidStatusToDelete =
					row[MDFClaimColumnKey.STATUS] === Status.DRAFT.name;

				if (currentValue === PermissionActionType.VIEW) {
					previousValue.push({
						icon: 'view',
						key: 'approve',
						label: 'View',
						onClick: () =>
							Liferay.Util.navigate(
								`${siteURL}/l/${
									row[MDFClaimColumnKey.CLAIM_ID]
								}`
							),
					});
				}

				if (
					currentValue === PermissionActionType.UPDATE &&
					currentMDFClaimHasValidStatusToEdit
				) {
					previousValue.push({
						icon: 'pencil',
						key: 'edit',
						label: 'Edit',
						onClick: () =>
							Liferay.Util.navigate(
								`${siteURL}/${
									PRMPageRoute.EDIT_MDF_CLAIM
								}/#/mdf-request/${
									row[MDFClaimColumnKey.REQUEST_ID]
								}/mdf-claim/${row[MDFClaimColumnKey.CLAIM_ID]}`
							),
					});
				}

				if (
					currentValue === PermissionActionType.DELETE &&
					currentMDFClaimHasValidStatusToDelete
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
											await deleteMDFClaim(
												ResourceName.MDF_CLAIM_DXP,
												Number(
													row[
														MDFClaimColumnKey
															.CLAIM_ID
													]
												)
											);

											Liferay.Util.openToast({
												message:
													'MDF Claim successfully deleted!',
												title: 'Success',
												type: 'success',
											});

											mutate(mutated);
										}
										catch (error: unknown) {
											Liferay.Util.openToast({
												message:
													'Fail to delete MDF Claim.',
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
			columnKey: MDFClaimColumnKey.CLAIM_ID,
			label: 'Claim ID',
			render: (data: string | undefined, row: MDFClaimListItem) => (
				<a
					className="link"
					onClick={() =>
						Liferay.Util.navigate(
							`${siteURL}/l/${row[MDFClaimColumnKey.CLAIM_ID]}`
						)
					}
				>{`Claim-${data}`}</a>
			),
		},
		{
			columnKey: MDFClaimColumnKey.REQUEST_ID,
			label: 'Request ID',
			render: (data: string | undefined, row: MDFClaimListItem) => (
				<a
					className="link"
					onClick={() =>
						Liferay.Util.navigate(
							`${siteURL}/l/${row[MDFClaimColumnKey.REQUEST_ID]}`
						)
					}
				>{`Request-${data}`}</a>
			),
		},
		{
			columnKey: MDFClaimColumnKey.PARTNER,
			label: 'Partner',
		},
		{
			columnKey: MDFClaimColumnKey.STATUS,
			label: 'Status',
			render: (data?: string) => <StatusBadge status={data as string} />,
		},
		{
			columnKey: MDFClaimColumnKey.TYPE,
			label: 'Type',
		},
		{
			columnKey: MDFClaimColumnKey.AMOUNT_CLAIMED,
			label: 'Amount Claimed',
		},
		{
			columnKey: MDFClaimColumnKey.PAID,
			label: 'Paid',
		},
		{
			columnKey: MDFClaimColumnKey.DATE_SUBMITTED,
			label: 'Date Submitted',
		},
		{
			columnKey: MDFColumnKey.ACTION,
			label: '',
			render: (_, row) => getDropdownOptions(row),
		},
	];
}
