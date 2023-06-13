/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {Text} from '@clayui/core';
import {ClayToggle} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayLabel from '@clayui/label';
import ClayList from '@clayui/list';
import {useModal} from '@clayui/modal';
import ClayPanel from '@clayui/panel';
import {sub} from 'frontend-js-web';
import React, {useEffect, useMemo, useState} from 'react';

import {
	fetchAttributesConfiguration,
	updateAttributesConfiguration,
} from '../../utils/api';
import ModalAccountGroups from './AccountGroupsModal';
import ModalOrganizations from './OrganizationsModal';
import ModalUserGroups from './UserGroupsModal';

function getLanguageCount(count: number, allSelected: boolean): string {
	return allSelected ? Liferay.Language.get('all') : String(count);
}

export enum EPeople {
	AccountGroupIds = 'syncedAccountGroupIds',
	OrganizationIds = 'syncedOrganizationIds',
	UserGroupIds = 'syncedUserGroupIds',
}

const People: React.FC = () => {
	const [syncAll, setSyncAll] = useState(false);
	const [syncAllAccounts, setSyncAllAccounts] = useState(false);
	const [syncAllContacts, setSyncAllContacts] = useState(false);
	const [syncedIds, setSyncedIds] = useState({
		[EPeople.AccountGroupIds]: [],
		[EPeople.OrganizationIds]: [],
		[EPeople.UserGroupIds]: [],
	});

	const {
		observer: observerAccountGroups,
		onOpenChange: onOpenChangeAccount,
		open: openAccount,
	} = useModal();
	const {
		observer: observerUserGroups,
		onOpenChange: onOpenChangeUser,
		open: openUser,
	} = useModal();
	const {
		observer: observerOrganizations,
		onOpenChange: onOpenChangeOrganizations,
		open: openOrganizations,
	} = useModal();

	const userOrganizationList = useMemo(
		() => [
			{
				count: getLanguageCount(
					syncedIds.syncedUserGroupIds.length,
					syncAllContacts
				),
				icon: 'users',
				onOpenChange: () => onOpenChangeUser(true),
				title: Liferay.Language.get('user-groups'),
			},
			{
				count: getLanguageCount(
					syncedIds.syncedOrganizationIds.length,
					syncAllContacts
				),
				icon: 'organizations',
				onOpenChange: () => onOpenChangeOrganizations(true),
				title: Liferay.Language.get('organizations'),
			},
		],
		[
			onOpenChangeOrganizations,
			onOpenChangeUser,
			syncAllContacts,
			syncedIds.syncedOrganizationIds.length,
			syncedIds.syncedUserGroupIds.length,
		]
	);

	const syncData = async () => {
		const {
			syncAllAccounts,
			syncAllContacts,
			syncedAccountGroupIds,
			syncedOrganizationIds,
			syncedUserGroupIds,
		} = await fetchAttributesConfiguration();

		setSyncAll(syncAllAccounts && syncAllContacts);
		setSyncAllAccounts(syncAllAccounts);
		setSyncAllContacts(syncAllContacts);

		setSyncedIds({
			[EPeople.AccountGroupIds]: syncedAccountGroupIds,
			[EPeople.OrganizationIds]: syncedOrganizationIds,
			[EPeople.UserGroupIds]: syncedUserGroupIds,
		});
	};

	useEffect(() => {
		syncData();
	}, []);

	const handleCloseModal = (closeFn: (value: boolean) => void) => {
		syncData();
		closeFn(false);
	};

	return (
		<>
			<div className="my-5">
				<ClayToggle
					label={Liferay.Language.get(
						'sync-all-contacts-and-accounts'
					)}
					onToggle={async () => {
						let newSyncedIds = {...syncedIds};

						if (!syncAll) {
							newSyncedIds = {
								[EPeople.AccountGroupIds]: [],
								[EPeople.OrganizationIds]: [],
								[EPeople.UserGroupIds]: [],
							};
						}

						const {ok} = await updateAttributesConfiguration({
							syncAllAccounts: !syncAll,
							syncAllContacts: !syncAll,
							...newSyncedIds,
						});

						if (ok) {
							setSyncAll(!syncAll);
							setSyncAllAccounts(!syncAll);
							setSyncAllContacts(!syncAll);
							setSyncedIds(newSyncedIds);
						}
					}}
					toggled={syncAll}
				/>

				<ClayLabel className="ml-4" displayType="info">
					{Liferay.Language.get('recomended')}
				</ClayLabel>
			</div>

			<ClayPanel
				className="panel-unstyled"
				collapsable
				displayTitle={Liferay.Language.get('select-contacts')}
				displayType="secondary"
				showCollapseIcon
			>
				<ClayPanel.Body>
					<div className="mb-4 mt-3">
						<ClayToggle
							label={Liferay.Language.get('sync-all-contacts')}
							onToggle={async () => {
								let newSyncedIds = {...syncedIds};

								if (!syncAllContacts) {
									newSyncedIds = {
										...syncedIds,
										[EPeople.OrganizationIds]: [],
										[EPeople.UserGroupIds]: [],
									};
								}

								const {
									ok,
								} = await updateAttributesConfiguration({
									syncAllAccounts,
									syncAllContacts: !syncAllContacts,
									...newSyncedIds,
								});

								if (ok) {
									setSyncAll(
										!syncAllContacts && syncAllAccounts
									);
									setSyncAllContacts(!syncAllContacts);
									setSyncedIds(newSyncedIds);
								}
							}}
							toggled={syncAllContacts}
						/>
					</div>

					<Text size={3}>
						{Liferay.Language.get(
							'sync-contacts-label-description'
						)}
					</Text>

					<ClayList className="mt-3" showQuickActionsOnHover>
						{userOrganizationList.map(
							({count, icon, onOpenChange, title}) => (
								<ClayList.Item
									action
									className="align-items-center"
									disabled={syncAllContacts}
									flex
									key={title}
									onClick={() =>
										!syncAllContacts && onOpenChange()
									}
								>
									<ClayList.ItemField>
										<ClayIcon symbol={icon} />
									</ClayList.ItemField>

									<ClayList.ItemField expand>
										<ClayList.ItemTitle className="hover-title">
											{title}
										</ClayList.ItemTitle>

										<ClayList.ItemText className="text-secondary">
											{sub(
												Liferay.Language.get(
													'x-selected'
												),
												count
											)}
										</ClayList.ItemText>
									</ClayList.ItemField>
								</ClayList.Item>
							)
						)}
					</ClayList>
				</ClayPanel.Body>
			</ClayPanel>

			<ClayPanel
				className="panel-unstyled"
				collapsable
				displayTitle={Liferay.Language.get('select-accounts')}
				displayType="secondary"
				showCollapseIcon={true}
			>
				<ClayPanel.Body>
					<div className="mb-4 mt-3">
						<ClayToggle
							label={Liferay.Language.get('sync-all-accounts')}
							onToggle={async () => {
								let newSyncedIds = {...syncedIds};

								if (!syncAllAccounts) {
									newSyncedIds = {
										...syncedIds,
										[EPeople.AccountGroupIds]: [],
									};
								}

								await updateAttributesConfiguration({
									syncAllAccounts: !syncAllAccounts,
									syncAllContacts,
									...newSyncedIds,
								});

								setSyncAll(!syncAllAccounts && syncAllContacts);
								setSyncAllAccounts(!syncAllAccounts);
								setSyncedIds(newSyncedIds);
							}}
							toggled={syncAllAccounts}
						/>
					</div>

					<Text size={3}>
						{Liferay.Language.get(
							'sync-accounts-label-description'
						)}
					</Text>

					<ClayList className="mt-3" showQuickActionsOnHover>
						<ClayList.Item
							action
							className="align-items-center"
							disabled={syncAllAccounts}
							flex
							onClick={() =>
								!syncAllAccounts && onOpenChangeAccount(true)
							}
						>
							<ClayList.ItemField>
								<ClayIcon symbol="users" />
							</ClayList.ItemField>

							<ClayList.ItemField expand>
								<ClayList.ItemTitle className="hover-title">
									{Liferay.Language.get(
										'sync-by-accounts-groups'
									)}
								</ClayList.ItemTitle>

								<ClayList.ItemText className="mt-1 text-secondary">
									{sub(
										Liferay.Language.get('x-selected'),
										getLanguageCount(
											syncedIds.syncedAccountGroupIds
												.length,
											syncAllAccounts
										)
									)}
								</ClayList.ItemText>
							</ClayList.ItemField>
						</ClayList.Item>
					</ClayList>
				</ClayPanel.Body>
			</ClayPanel>

			{openAccount && (
				<ModalAccountGroups
					observer={observerAccountGroups}
					onCloseModal={() => handleCloseModal(onOpenChangeAccount)}
					syncAllAccounts={syncAllAccounts}
					syncAllContacts={syncAllContacts}
					syncedIds={syncedIds}
				/>
			)}

			{openOrganizations && (
				<ModalOrganizations
					observer={observerOrganizations}
					onCloseModal={() =>
						handleCloseModal(onOpenChangeOrganizations)
					}
					syncAllAccounts={syncAllAccounts}
					syncAllContacts={syncAllContacts}
					syncedIds={syncedIds}
				/>
			)}

			{openUser && (
				<ModalUserGroups
					observer={observerUserGroups}
					onCloseModal={() => handleCloseModal(onOpenChangeUser)}
					syncAllAccounts={syncAllAccounts}
					syncAllContacts={syncAllContacts}
					syncedIds={syncedIds}
				/>
			)}
		</>
	);
};

export default People;
