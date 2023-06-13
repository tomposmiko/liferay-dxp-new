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
	DefaultEventHandler,
	createPortletURL,
	navigate,
	openSelectionModal,
} from 'frontend-js-web';

class ManagementToolbarDefaultEventHandler extends DefaultEventHandler {
	activateAccountUsers(itemData) {
		this._updateAccountUsers(itemData.activateAccountUsersURL);
	}

	deactivateAccountUsers(itemData) {
		if (
			confirm(
				Liferay.Language.get(
					'are-you-sure-you-want-to-deactivate-the-selected-users'
				)
			)
		) {
			this._updateAccountUsers(itemData.deactivateAccountUsersURL);
		}
	}

	deleteAccountUsers(itemData) {
		if (
			confirm(
				Liferay.Language.get(
					'are-you-sure-you-want-to-delete-the-selected-users'
				)
			)
		) {
			this._updateAccountUsers(itemData.deleteAccountUsersURL);
		}
	}

	selectAccountEntries(itemData) {
		this._openAccountEntrySelector(
			Liferay.Language.get('select'),
			this.ns('selectAccountEntries'),
			Liferay.Language.get(itemData.dialogTitle),
			itemData.accountEntriesSelectorURL,
			(selectedItems) => {
				var redirectURL = Liferay.Util.PortletURL.createPortletURL(
					itemData.redirectURL,
					{
						accountEntriesNavigation: 'accounts',
						accountEntryIds: selectedItems.value,
					}
				);

				window.location.href = redirectURL;
			}
		);
	}

	addAccountUser(itemData) {
		openSelectionModal({
			id: this.ns('addAccountUser'),
			onSelect: (selectedItem) => {
				var addAccountUserURL = createPortletURL(
					itemData.addAccountUserURL,
					{
						accountEntryId: selectedItem.accountentryid,
					}
				);

				navigate(addAccountUserURL);
			},
			selectEventName: this.ns('selectAccountEntry'),
			title: Liferay.Language.get(itemData.dialogTitle),
			url: itemData.accountEntrySelectorURL,
		});
	}

	_openAccountEntrySelector(
		dialogButtonLabel,
		dialogEventName,
		dialogTitle,
		accountEntrySelectorURL,
		callback
	) {
		openSelectionModal({
			buttonAddLabel: dialogButtonLabel,
			multiple: true,
			onSelect: (selectedItem) => {
				if (selectedItem) {
					callback(selectedItem);
				}
			},
			selectEventName: dialogEventName,
			title: dialogTitle,
			url: accountEntrySelectorURL,
		});
	}

	_updateAccountUsers(url) {
		const form = this.one('#fm');

		Liferay.Util.postForm(form, {
			data: {
				accountUserIds: Liferay.Util.listCheckedExcept(
					form,
					this.ns('allRowIds')
				),
			},
			url,
		});
	}
}

export default ManagementToolbarDefaultEventHandler;
