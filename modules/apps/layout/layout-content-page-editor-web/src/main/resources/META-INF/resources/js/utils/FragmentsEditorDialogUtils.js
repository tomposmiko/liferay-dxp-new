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

import {UPDATE_LAST_SAVE_DATE} from '../actions/actions.es';
import CreateContentDialog from '../components/content/CreateContentDialog.es';
import {getState} from '../store/store.es';

/**
 * @private
 * @review
 * @type {null|{detach: Function}}
 */
let _widgetConfigurationChangeHandler = null;

/**
 * Possible types that can be returned by the image selector
 */
const DOWNLOAD_FILE_ENTRY_IMAGE_SELECTOR_RETURN_TYPE =
	'com.liferay.item.selector.criteria.DownloadFileEntryItemSelectorReturnType';

/**
 * @param {object} store Store
 * @return {CreateContentDialog}
 */
function openCreateContentDialog(store) {
	return new CreateContentDialog({
		store
	});
}

/**
 * @param {function} callback
 * @param {function} [destroyedCallback=null]
 */
function openImageSelector(callback, destroyedCallback = null) {
	const state = getState();

	const itemSelectorDialog = new ItemSelectorDialog({
		eventName: `${state.portletNamespace}selectImage`,
		singleSelect: true,
		title: Liferay.Language.get('select'),
		url: state.imageSelectorURL
	});

	itemSelectorDialog.on('selectedItemChange', event => {
		const selectedItem = event.selectedItem || {};

		const {returnType, value} = selectedItem;
		const selectedImage = {};

		if (returnType === 'URL') {
			selectedImage.title = value;
			selectedImage.url = value;
		}

		if (returnType === DOWNLOAD_FILE_ENTRY_IMAGE_SELECTOR_RETURN_TYPE) {
			const fileEntry = JSON.parse(value);

			selectedImage.title = fileEntry.title;
			selectedImage.url = fileEntry.url;
		}

		if (selectedImage.url) {
			callback(selectedImage);
		}
	});

	itemSelectorDialog.on('visibleChange', () => {
		if (destroyedCallback) {
			destroyedCallback();
		}
	});

	itemSelectorDialog.open();
}

/**
 * @param String infoItemSelectorURL
 * @param {function} callback
 * @param {function} [destroyedCallback=null]
 */
function openInfoItemSelector(
	infoItemSelectorURL,
	callback,
	destroyedCallback = null
) {
	const state = getState();

	const itemSelectorDialog = new ItemSelectorDialog({
		eventName: `${state.portletNamespace}selectInfoItem`,
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
		if (event.newVal === false && destroyedCallback) {
			destroyedCallback();
		}
	});

	itemSelectorDialog.open();
}

/**
 * @param {function} callback
 * @param {function} [destroyedCallback=null]
 */
function openItemSelector(callback, destroyedCallback = null) {
	const state = getState();

	openInfoItemSelector(
		state.infoItemSelectorURL,
		callback,
		destroyedCallback
	);
}

/**
 * @param {{dispatch: Function}} store
 * @review
 */
function startListeningWidgetConfigurationChange(store) {
	stopListeningWidgetConfigurationChange();

	let submitFormHandler = null;

	_widgetConfigurationChangeHandler = Liferay.after('popupReady', event => {
		if (event.win) {
			const configurationForm = event.win.document.querySelector(
				'.portlet-configuration-setup'
			);

			if (configurationForm) {
				if (submitFormHandler) {
					submitFormHandler.detach();

					submitFormHandler = null;
				}

				submitFormHandler = event.win.Liferay.on('submitForm', () => {
					store.dispatch({
						lastSaveDate: new Date(),
						type: UPDATE_LAST_SAVE_DATE
					});
				});
			}
		}
	});
}

/**
 * @review
 */
function stopListeningWidgetConfigurationChange() {
	if (_widgetConfigurationChangeHandler) {
		_widgetConfigurationChangeHandler.detach();

		_widgetConfigurationChangeHandler = null;
	}
}

export {
	openCreateContentDialog,
	openImageSelector,
	openInfoItemSelector,
	openItemSelector,
	startListeningWidgetConfigurationChange,
	stopListeningWidgetConfigurationChange
};
