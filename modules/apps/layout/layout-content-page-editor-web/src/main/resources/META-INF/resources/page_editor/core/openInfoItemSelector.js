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

import {ItemSelectorDialog} from 'frontend-js-web';

export function openInfoItemSelector(
	callback,
	config,
	destroyedCallback = null
) {
	const {infoItemSelectorURL, portletNamespace} = config;

	const itemSelectorDialog = new ItemSelectorDialog({
		eventName: `${portletNamespace}selectInfoItem`,
		singleSelect: true,
		title: Liferay.Language.get('select'),
		url: infoItemSelectorURL
	});

	itemSelectorDialog.on('selectedItemChange', event => {
		const selectedItem = event.selectedItem;

		if (selectedItem && selectedItem.value) {
			const infoItem = JSON.parse(selectedItem.value);

			callback({
				className: infoItem.className,
				classNameId: infoItem.classNameId,
				classPK: infoItem.classPK,
				title: infoItem.title
			});
		}
	});

	itemSelectorDialog.on('visibleChange', event => {
		if (
			!event.newVal &&
			destroyedCallback &&
			typeof destroyedCallback === 'function'
		) {
			destroyedCallback();
		}
	});

	itemSelectorDialog.open();
}
