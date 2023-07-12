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

import {openSelectionModal} from 'frontend-js-web';

const ACTIONS = {
	deleteVocabularies({
		deleteVocabulariesURL,
		portletNamespace,
		viewVocabulariesURL,
	}) {
		const vocabulariesForm = document.createElement('form');

		vocabulariesForm.setAttribute('method', 'post');

		openSelectionModal({
			buttonAddLabel: Liferay.Language.get('delete'),
			multiple: true,
			onSelect: (selectedItems) => {
				if (selectedItems) {
					if (
						confirm(
							Liferay.Language.get(
								'are-you-sure-you-want-to-delete-the-selected-entries'
							)
						)
					) {
						selectedItems.forEach((item) => {
							vocabulariesForm.appendChild(item.cloneNode(true));
						});

						submitForm(vocabulariesForm, deleteVocabulariesURL);
					}
				}
			},
			selectEventName: `${portletNamespace}selectVocabularies`,
			title: Liferay.Language.get('delete-vocabulary'),
			url: viewVocabulariesURL,
		});
	},
};

export default function propsTransformer({
	deleteVocabulariesURL,
	items,
	portletNamespace,
	viewVocabulariesURL,
	...otherProps
}) {
	return {
		...otherProps,
		items: items.map((item) => {
			return {
				...item,
				onClick(event) {
					const action = item.data?.action;

					if (action) {
						event.preventDefault();

						ACTIONS[action]({
							deleteVocabulariesURL,
							portletNamespace,
							viewVocabulariesURL,
						});
					}
				},
			};
		}),
	};
}
