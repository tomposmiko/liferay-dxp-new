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

// Deal with big changes in case of need to change to localStorage to sessionStorage
// Or Even with Cookie, we don't need to change everytime in every single file

import {Liferay} from '../../utils/liferay';

const liferayStorage = Liferay.Util.LocalStorage;

/**
 * @description Use this to get value from Storage
 * @param {*} key Storage Key
 */

export function getItem(key, consentType = liferayStorage.TYPES.NECESSARY) {
	return liferayStorage.getItem(key, consentType);
}

/**
 * @description Use this to remove values from Storage
 * @param {*} key Storage Key
 */

export function removeItem(key) {
	liferayStorage.removeItem(key);
}

/**
 * @description Use this to set values into Storage
 * @param {*} key Storage Key
 * @param {*} value Storage Value
 */

export function setItem(
	key,
	value,
	consentType = liferayStorage.TYPES.NECESSARY
) {
	liferayStorage.setItem(key, value, consentType);
}

export const Storage = {
	getItem,
	removeItem,
	setItem,
};

export const STORAGE_KEYS = {
	APPLICATION_FORM: 'raylife-application-form',
	APPLICATION_ID: 'raylife-application-id',
	BACK_TO_EDIT: 'raylife-back-to-edit',
	BASIC_STEP_CLICKED: 'basic-step-clicked',
	CONTEXTUAL_MESSAGE: 'raylife-contextual-message',
	PRODUCT: 'raylife-product',
	QUOTE_ID: 'raylife-quote-id',
	SELECTED_PRODUCT: 'raylife-selected-product',
};
