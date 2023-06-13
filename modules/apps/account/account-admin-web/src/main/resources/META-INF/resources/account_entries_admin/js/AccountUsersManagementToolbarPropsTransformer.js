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
	getCheckedCheckboxes,
	openConfirmModal,
	openModal,
	openSelectionModal,
	postForm,
	sub,
} from 'frontend-js-web';

function openSelectAccountUsersModal(
	accountEntryName,
	assignAccountUsersURL,
	selectAccountUsersURL,
	portletNamespace
) {
	openSelectionModal({
		buttonAddLabel: Liferay.Language.get('assign'),
		containerProps: {
			className: '',
		},
		iframeBodyCssClass: '',
		multiple: true,
		onSelect: (selectedItems) => {
			if (!selectedItems?.length) {
				return;
			}

			const form = document.getElementById(`${portletNamespace}fm`);

			if (form) {
				const values = selectedItems.map((item) => item.value);

				postForm(form, {
					data: {
						accountUserIds: values.join(','),
					},
					url: assignAccountUsersURL,
				});
			}
		},
		title: sub(Liferay.Language.get('assign-users-to-x'), accountEntryName),
		url: selectAccountUsersURL,
	});
}

export default function propsTransformer({
	additionalProps: {accountEntryName},
	portletNamespace,
	...otherProps
}) {
	return {
		...otherProps,
		onActionButtonClick: (event, {item}) => {
			const data = item?.data;

			const action = data?.action;

			if (action === 'removeUsers') {
				openConfirmModal({
					message: Liferay.Language.get(
						'are-you-sure-you-want-to-remove-the-selected-users'
					),
					onConfirm: (isConfirmed) => {
						if (isConfirmed) {
							const form = document.getElementById(
								`${portletNamespace}fm`
							);

							if (form) {
								postForm(form, {
									data: {
										accountUserIds: getCheckedCheckboxes(
											form,
											`${portletNamespace}allRowIds`
										),
									},
									url: data?.removeUsersURL,
								});
							}
						}
					},
				});
			}
		},
		onCreateButtonClick: (event, {item}) => {
			const data = item.data;

			if (data?.action === 'selectAccountUsers') {
				openSelectAccountUsersModal(
					accountEntryName,
					data?.assignAccountUsersURL,
					data?.selectAccountUsersURL,
					portletNamespace
				);
			}
		},
		onCreationMenuItemClick: (event, {item}) => {
			const data = item?.data;

			const action = data?.action;

			if (action === 'inviteAccountUsers') {
				openModal({
					containerProps: {
						className: 'modal-height-xl',
					},
					id: `${portletNamespace}inviteUsersDialog`,
					iframeBodyCssClass: '',
					size: 'lg',
					title: sub(
						Liferay.Language.get('invite-users-to-x'),
						accountEntryName
					),
					url: data?.requestInvitationsURL,
				});
			}
			else if (action === 'selectAccountUsers') {
				openSelectAccountUsersModal(
					accountEntryName,
					data?.assignAccountUsersURL,
					data?.selectAccountUsersURL,
					portletNamespace
				);
			}
		},
	};
}
