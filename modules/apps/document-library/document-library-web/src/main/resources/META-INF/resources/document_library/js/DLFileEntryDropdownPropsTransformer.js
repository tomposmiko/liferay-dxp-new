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
	addParams,
	navigate,
	openConfirmModal,
	openModal,
	openSelectionModal,
} from 'frontend-js-web';

const ACTIONS = {
	checkin({checkinURL}, portletNamespace) {
		Liferay.componentReady(
			`${portletNamespace}DocumentLibraryCheckinModal`
		).then((documentLibraryCheckinModal) => {
			documentLibraryCheckinModal.open((versionIncrease, changeLog) => {
				let portletURL = checkinURL;

				if (versionIncrease) {
					portletURL = addParams(
						`${portletNamespace}versionIncrease=${encodeURIComponent(
							versionIncrease
						)}`,
						portletURL
					);
				}

				if (changeLog) {
					portletURL = addParams(
						`${portletNamespace}changeLog=${encodeURIComponent(
							changeLog
						)}`,
						portletURL
					);
				}

				portletURL = addParams(
					`${portletNamespace}updateVersionDetails=true`,
					portletURL
				);

				navigate(portletURL);
			});
		});
	},

	compareTo({
		compareVersionURL,
		dialogTitle,
		jsNamespace,
		namespace,
		selectFileVersionURL,
	}) {
		openSelectionModal({
			id: `${jsNamespace}compareFileVersions`,
			onSelect(selectedItem) {
				let uri = compareVersionURL;

				uri = addParams(
					`${namespace}sourceFileVersionId=${selectedItem.sourceversion}`,
					uri
				);
				uri = addParams(
					`${namespace}targetFileVersionId=${selectedItem.targetversion}`,
					uri
				);

				navigate(uri);
			},
			selectEventName: `${namespace}selectFileVersionFm`,
			title: dialogTitle,
			url: selectFileVersionURL,
		});
	},

	delete({deleteURL}) {
		openConfirmModal({
			message: Liferay.Language.get(
				'are-you-sure-you-want-to-delete-this'
			),
			onConfirm: (isConfirmed) =>
				isConfirmed && submitForm(document.hrefFm, deleteURL),
		});
	},

	deleteVersion({deleteURL}) {
		openConfirmModal({
			message: Liferay.Language.get(
				'are-you-sure-you-want-to-delete-this'
			),
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					submitForm(document.hrefFm, deleteURL);
				}
			},
		});
	},

	editImage({fileEntryId, imageURL}, portletNamespace) {
		window[`${portletNamespace}editWithImageEditor`]({
			fileEntryId,
			imageURL,
		});
	},

	editOfficeDocument({editURL}, portletNamespace) {
		Liferay.componentReady(`${portletNamespace}DocumentLibraryOpener`).then(
			(openerOnedrive) => {
				openerOnedrive.edit({
					formSubmitURL: editURL,
				});
			}
		);
	},

	move({parameterName, parameterValue}, portletNamespace) {
		window[`${portletNamespace}move`](1, parameterName, parameterValue);
	},

	permissions({permissionsURL}) {
		openModal({
			title: Liferay.Language.get('permissions'),
			url: permissionsURL,
		});
	},

	publish({publishURL}) {
		openConfirmModal({
			message: Liferay.Language.get(
				'are-you-sure-you-want-to-publish-the-selected-document'
			),
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					location.href = publishURL;
				}
			},
		});
	},
};

export default function propsTransformer({items, portletNamespace, ...props}) {
	return {
		...props,
		items: items.map((item) =>
			item?.type === 'group'
				? {
						...item,
						items: item.items?.map((child) => ({
							...child,
							onClick(event) {
								const action = child.data?.action;

								if (action) {
									event.preventDefault();

									ACTIONS[action](
										child.data,
										portletNamespace
									);
								}
							},
						})),
				  }
				: {
						...item,
						onClick(event) {
							const action = item.data?.action;

							if (action) {
								event.preventDefault();

								ACTIONS[action](item.data, portletNamespace);
							}
						},
				  }
		),
	};
}
