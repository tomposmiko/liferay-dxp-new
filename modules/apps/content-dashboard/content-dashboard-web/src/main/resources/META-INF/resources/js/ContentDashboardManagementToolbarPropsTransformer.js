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

import {addParams, navigate, openSelectionModal} from 'frontend-js-web';

const DEFAULT_VALUES = {
	buttonAddLabel: Liferay.Language.get('select'),
	iframeBodyCssClass: '',
	modalHeight: '70vh',
	size: 'md',
};

/**
 * Returns true if the specified value is an object. Not arrays, custom events or functions.
 * @param {?} value Variable to test.
 * @return {boolean} Whether variable is an object.
 */
const _isObjectStrict = (value) =>
	typeof value === 'object' &&
	!Array.isArray(value) &&
	value !== null &&
	!Object.prototype.hasOwnProperty.call(value, 'currentTarget');

/**
 * Returns URL with proper search params.
 */
const _getRedirectURLWithParams = ({data, portletNamespace, selection}) => {
	const {itemValueKey, redirectURL, urlParamName} = data;

	return [selection]
		.reduce((acc, val) => acc.concat(val), []) // replace with flat()
		.reduce((acc, item) => {
			let paramValue;

			if (itemValueKey) {
				paramValue = item[itemValueKey];
			}
			else {
				paramValue =
					typeof item === 'string' ? item : JSON.stringify(item);
			}

			return addParams(
				`${portletNamespace}${urlParamName}=${paramValue}`,
				acc
			);
		}, redirectURL);
};

const _handleOnSelect = ({data, portletNamespace, selection}) => {
	if (_isObjectStrict(selection)) {
		selection = Object.values(selection).filter((item) => !item.unchecked);
	}

	navigate(
		_getRedirectURLWithParams({
			data,
			portletNamespace,
			selection,
		})
	);
};

export default function propsTransformer({portletNamespace, ...otherProps}) {
	const selectAuthor = (itemData) => {
		openSelectionModal({
			buttonAddLabel: Liferay.Language.get('select'),
			height: '70vh',
			multiple: true,
			onSelect: (selectedItem) => {
				if (selectedItem) {
					let redirectURL = itemData?.redirectURL;

					selectedItem.forEach((item) => {
						redirectURL = addParams(
							`${portletNamespace}authorIds=${item.id}`,
							redirectURL
						);
					});

					navigate(redirectURL);
				}
			},
			selectEventName: `${portletNamespace}selectedAuthorItem`,
			size: 'lg',
			title: itemData?.dialogTitle,
			url: itemData?.selectAuthorURL,
		});
	};

	const selectAssetCategory = (itemData) => {
		openSelectionModal({
			buttonAddLabel: Liferay.Language.get('select'),
			height: '70vh',
			iframeBodyCssClass: '',
			multiple: true,
			onSelect: (selectedItem) => {
				if (selectedItem) {
					const assetCategories = Object.keys(selectedItem).filter(
						(key) => !selectedItem[key].unchecked
					);

					let redirectURL = itemData?.redirectURL;

					assetCategories.forEach((assetCategory) => {
						redirectURL = addParams(
							`${portletNamespace}assetCategoryId=${selectedItem[assetCategory].categoryId}`,
							redirectURL
						);
					});

					navigate(redirectURL);
				}
			},
			selectEventName: `${portletNamespace}selectedAssetCategory`,
			size: 'md',
			title: itemData?.dialogTitle,
			url: itemData?.selectAssetCategoryURL,
		});
	};

	const selectAssetTag = (itemData) => {
		openSelectionModal({
			buttonAddLabel: Liferay.Language.get('select'),
			height: '70vh',
			multiple: true,
			onSelect: (selectedItem) => {
				if (selectedItem) {
					const assetTags = selectedItem.map((tag) => tag.value);

					let redirectURL = itemData?.redirectURL;

					assetTags.forEach((assetTag) => {
						redirectURL = addParams(
							`${portletNamespace}assetTagId=${assetTag}`,
							redirectURL
						);
					});

					navigate(redirectURL);
				}
			},
			selectEventName: `${portletNamespace}selectedAssetTag`,
			size: 'lg',
			title: itemData?.dialogTitle,
			url: itemData?.selectTagURL,
		});
	};

	const selectContentDashboardItemSubtype = (itemData) => {
		openSelectionModal({
			buttonAddLabel: Liferay.Language.get('select'),
			height: '70vh',
			multiple: true,
			onSelect: (selectedItems) => {
				let redirectURL = itemData?.redirectURL;

				selectedItems.forEach((item) => {
					redirectURL = addParams(
						`${portletNamespace}contentDashboardItemSubtypePayload=${JSON.stringify(
							item
						)}`,
						redirectURL
					);
				});

				navigate(redirectURL);
			},
			selectEventName: `${portletNamespace}selectedContentDashboardItemSubtype`,
			size: 'md',
			title: itemData?.dialogTitle,
			url: itemData?.selectContentDashboardItemSubtypeURL,
		});
	};

	const selectScope = (itemData) => {
		openSelectionModal({
			height: '70vh',
			id: `${portletNamespace}selectedScopeIdItem`,
			onSelect: (selectedItem) => {
				navigate(
					addParams(
						`${portletNamespace}scopeId=${selectedItem.groupid}`,
						itemData?.redirectURL
					)
				);
			},
			selectEventName: `${portletNamespace}selectedScopeIdItem`,
			size: 'lg',
			title: itemData?.dialogTitle,
			url: itemData?.selectScopeURL,
		});
	};

	return {
		...otherProps,
		onFilterDropdownItemClick(_event, {item = {}}) {
			const {data} = item;

			if (!Object.keys(data).length) {
				return;
			}

			const {
				action,
				dialogTitle,
				selectEventName,
				selectItemURL,
				multiple,
				size = DEFAULT_VALUES.size,
			} = data;

			if (action === 'selectAssetCategory') {
				selectAssetCategory(data);
			}
			else if (action === 'selectAssetTag') {
				selectAssetTag(data);
			}
			else if (action === 'selectAuthor') {
				selectAuthor(data);
			}
			else if (action === 'selectContentDashboardItemSubtype') {
				selectContentDashboardItemSubtype(data);
			}
			else if (action === 'selectScope') {
				selectScope(data);
			}
			else {
				openSelectionModal({
					buttonAddLabel: DEFAULT_VALUES.buttonAddLabel,
					height: DEFAULT_VALUES.modalHeight,
					iframeBodyCssClass: DEFAULT_VALUES.iframeBodyCssClass,
					multiple: multiple === 'true',
					onSelect: (selection) =>
						_handleOnSelect({
							data,
							portletNamespace,
							selection,
						}),
					selectEventName: portletNamespace + selectEventName,
					size,
					title: dialogTitle,
					url: selectItemURL,
				});
			}
		},
	};
}
