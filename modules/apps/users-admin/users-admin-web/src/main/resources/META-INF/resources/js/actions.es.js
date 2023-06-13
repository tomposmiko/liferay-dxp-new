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

import {
	createActionURL,
	createRenderURL,
	fetch,
	getCheckedCheckboxes,
	openConfirmModal,
	openModal,
	openSelectionModal,
	openToast,
	postForm,
} from 'frontend-js-web';

export const ACTIONS = {
	activateUser(itemData) {
		submitForm(document.hrefFm, itemData.activateUserURL);
	},

	activateUsers(itemData, portletNamespace) {
		updateUsers(portletNamespace, itemData?.activateUsersURL);
	},

	assignOrganizationRoles(itemData) {
		openModal({
			title: itemData.label,
			url: itemData.assignOrganizationRolesURL,
		});
	},

	assignUsers(itemData, portletNamespace) {
		this.selectUsers({
			basePortletURL: itemData.basePortletURL,
			organizationId: itemData.organizationId,
			portletNamespace,
		});
	},

	deactivateUser(itemData) {
		openConfirmModal({
			message: Liferay.Language.get(
				'are-you-sure-you-want-to-deactivate-this'
			),
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					submitForm(document.hrefFm, itemData.deactivateUserURL);
				}
			},
		});
	},

	deactivateUsers(itemData, portletNamespace) {
		openConfirmModal({
			message: Liferay.Language.get(
				'are-you-sure-you-want-to-deactivate-the-selected-users'
			),
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					updateUsers(portletNamespace, itemData?.editUsersURL);
				}
			},
		});
	},

	deleteOrganization(itemData, portletNamespace) {
		confirmDeleteOrganizations(
			itemData,
			portletNamespace,
			itemData?.organizationId
		);
	},

	deleteOrganizations(itemData, portletNamespace) {
		confirmDeleteOrganizations(
			itemData,
			portletNamespace,
			getOrganizationIds(portletNamespace)
		);
	},

	deleteOrganizationsAndUsers(itemData, portletNamespace) {
		const form = document.getElementById(`${portletNamespace}fm`);

		postForm(form, {
			data: {
				deleteOrganizationIds: getOrganizationIds(portletNamespace),
				deleteUserIds: getUserIds(portletNamespace),
			},
			url: itemData?.deleteOrganizationsAndUsersURL,
		});
	},

	deleteUser(itemData) {
		openConfirmModal({
			message: Liferay.Language.get(
				'are-you-sure-you-want-to-delete-this'
			),
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					submitForm(document.hrefFm, itemData.deleteUserURL);
				}
			},
		});
	},

	deleteUserActionContributor(itemData) {
		openConfirmModal({
			message: itemData.confirmation,
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					submitForm(
						document.hrefFm,
						itemData.deleteUserActionContributorURL
					);
				}
			},
		});
	},

	deleteUsers(itemData, portletNamespace) {
		openConfirmModal({
			message: Liferay.Language.get(
				'are-you-sure-you-want-to-permanently-delete-the-selected-users'
			),
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					updateUsers(portletNamespace, itemData?.editUsersURL);
				}
			},
		});
	},

	permissions(itemData) {
		openModal({
			title: Liferay.Language.get('permissions'),
			url: itemData.permissionsURL,
		});
	},

	removeOrganization(itemData) {
		submitForm(document.hrefFm, itemData.removeOrganizationURL);
	},

	removeOrganizationsAndUsers(itemData, portletNamespace) {
		const form = document.getElementById(`${portletNamespace}fm`);

		postForm(form, {
			data: {
				removeOrganizationIds: getOrganizationIds(portletNamespace),
				removeUserIds: getUserIds(portletNamespace),
			},
			url: itemData?.removeOrganizationsAndUsersURL,
		});
	},

	removeUser(itemData) {
		submitForm(document.hrefFm, itemData.removeUserURL);
	},

	selectUsers({basePortletURL, organizationId, portletNamespace}) {
		if (!organizationId) {
			return;
		}

		const selectUsersURL = createRenderURL(basePortletURL, {
			mvcPath: '/select_organization_users.jsp',
			organizationId,
			p_p_state: 'pop_up',
		});

		openSelectionModal({
			buttonAddLabel: Liferay.Language.get('done'),
			multiple: true,
			onSelect: (selectedItems) => {
				if (selectedItems?.length) {
					const assignmentsRedirectURL = createRenderURL(
						basePortletURL,
						{
							mvcRenderCommandName: '/users_admin/view',
							organizationId,
							toolbarItem: 'view-all-organizations',
							usersListView: 'tree',
						}
					);

					const values = selectedItems.map((item) => item.value);

					const editAssignmentURL = createActionURL(basePortletURL, {
						'addUserIds': values.join(','),
						'assignmentsRedirect': assignmentsRedirectURL.toString(),
						'javax.portlet.action':
							'/users_admin/edit_organization_assignments',
						organizationId,
					});

					const form = document.getElementById(
						`${portletNamespace}fm`
					);

					if (!form) {
						return;
					}

					submitForm(form, editAssignmentURL.toString());
				}
			},
			title: Liferay.Language.get('assign-users'),
			url: selectUsersURL.toString(),
		});
	},
};

const addSearchParams = (url, portletNamespace, searchParams) => {
	const newURL = new URL(url);

	Object.keys(searchParams).forEach((paramName) => {
		newURL.searchParams.append(
			portletNamespace + paramName,
			searchParams[paramName]
		);
	});

	return newURL.toString();
};

const confirmDeleteOrganizations = (
	itemData,
	portletNamespace,
	organizationIds
) => {
	const getInactiveUsersURL = addSearchParams(
		itemData?.getInactiveUsersURL,
		portletNamespace,
		{ids: organizationIds}
	);

	getUsersCount(getInactiveUsersURL).then((responseData) => {
		let count = parseInt(responseData, 10);

		if (count > 0) {
			const getActiveUsersURL = addSearchParams(
				itemData?.getActiveUsersURL,
				portletNamespace,
				{ids: organizationIds}
			);

			getUsersCount(getActiveUsersURL).then((responseData) => {
				count = parseInt(responseData, 10);

				if (count > 0) {
					openConfirmModal({
						message: Liferay.Language.get(
							'are-you-sure-you-want-to-delete-this'
						),
						onConfirm: (isConfirmed) => {
							if (isConfirmed) {
								doDeleteOrganizations(
									itemData,
									portletNamespace,
									organizationIds
								);
							}
						},
					});
				}
				else {
					let message;

					if (
						organizationIds &&
						organizationIds.toString().split(',').length > 1
					) {
						message = Liferay.Language.get(
							'one-or-more-organizations-are-associated-with-deactivated-users.-do-you-want-to-proceed-with-deleting-the-selected-organizations-by-automatically-unassociating-the-deactivated-users'
						);
					}
					else {
						message = Liferay.Language.get(
							'the-selected-organization-is-associated-with-deactivated-users.-do-you-want-to-proceed-with-deleting-the-selected-organization-by-automatically-unassociating-the-deactivated-users'
						);
					}

					openConfirmModal({
						message,
						onConfirm: (isConfirmed) => {
							if (isConfirmed) {
								doDeleteOrganizations(
									itemData,
									portletNamespace,
									organizationIds
								);
							}
						},
					});
				}
			});
		}
		else {
			openConfirmModal({
				message: Liferay.Language.get(
					'are-you-sure-you-want-to-delete-this'
				),
				onConfirm: (isConfirmed) => {
					if (isConfirmed) {
						doDeleteOrganizations(
							itemData,
							portletNamespace,
							organizationIds
						);
					}
				},
			});
		}
	});
};

const doDeleteOrganizations = (itemData, portletNamespace, organizationIds) => {
	const form = document.getElementById(`${portletNamespace}fm`);

	postForm(form, {
		data: {
			cmd: itemData?.cmd,
			deleteOrganizationIds: organizationIds,
		},
		url: itemData?.deleteOrganizationURL,
	});
};

const getOrganizationIds = (portletNamespace) => {
	const form = document.getElementById(`${portletNamespace}fm`);

	return getCheckedCheckboxes(
		form,
		`${portletNamespace}allRowIds`,
		`${portletNamespace}rowIdsOrganization`
	);
};

const getUserIds = (portletNamespace) => {
	const form = document.getElementById(`${portletNamespace}fm`);

	return getCheckedCheckboxes(
		form,
		`${portletNamespace}allRowIds`,
		`${portletNamespace}rowIdsUser`
	);
};

const getUsersCount = (url) => {
	return fetch(url)
		.then((response) => {
			return response.text();
		})
		.catch(() => {
			openToast({
				message: Liferay.Language.get(
					'an-unexpected-system-error-occurred'
				),
				type: 'danger',
			});
		});
};

const updateUsers = (portletNamespace, url) => {
	const form = document.getElementById(`${portletNamespace}fm`);

	if (form) {
		postForm(form, {
			data: {
				deleteUserIds: getUserIds(portletNamespace),
			},
			url,
		});
	}
};
