/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import {fetch} from 'frontend-js-web';

import {HEADERS} from './constants';

const PINS_ENDPOINT = '/o/headless-commerce-admin-catalog/v1.0';

export const loadPins = (productId) => {
	const url = new URL(
		`${PINS_ENDPOINT}/products/${productId}/pins`,
		themeDisplay.getPortalURL()
	);

	url.searchParams.set('pageSize', 100);

	return fetch(url, {
		headers: HEADERS,
	})
		.then((response) => response.json())
		.then((jsonResponse) => jsonResponse.items);
};

export const deletePin = (pinId) => {
	const url = new URL(
		`${PINS_ENDPOINT}/pins/${pinId}`,
		themeDisplay.getPortalURL()
	);

	return fetch(url, {
		headers: HEADERS,
		method: 'DELETE',
	});
};

export const savePin = (
	pinId,
	mappedProduct,
	sequence,
	positionX,
	positionY,
	productId
) => {
	const baseURL = pinId
		? `${PINS_ENDPOINT}/pins/${pinId}`
		: `${PINS_ENDPOINT}/products/${productId}/pins`;

	const url = new URL(baseURL, themeDisplay.getPortalURL());

	const body = {};

	if (mappedProduct) {
		body.mappedProduct = mappedProduct;
	}

	if (positionX || positionY) {
		body.positionX = positionX;
		body.positionY = positionY;
	}

	if (sequence) {
		body.sequence = sequence;
	}

	return fetch(url, {
		body: JSON.stringify(body),
		headers: HEADERS,
		method: pinId ? 'PATCH' : 'POST',
	}).then((response) => response.json());
};

export const updateGlobalPinsRadius = (diagramId, radius, namespace) => {
	const url = new URL(
		`${PINS_ENDPOINT}/diagrams/${diagramId}`,
		themeDisplay.getPortalURL()
	);

	return fetch(url, {
		body: JSON.stringify({radius}),
		headers: HEADERS,
		method: 'PATCH',
	}).then((response) => {
		if (response.ok) {
			const radiusInput = document.getElementById(`${namespace}radius`);

			if (radiusInput) {
				radiusInput.value = radius;
			}
		}
	});
};

export const getMappedProducts = (productId, query, page, pageSize) => {
	const url = new URL(
		`${PINS_ENDPOINT}/products/${productId}/mapped-products`,
		themeDisplay.getPortalURL()
	);

	if (query) {
		url.searchParams.set('search', query);
	}

	if (page) {
		url.searchParams.set('page', page);
	}

	url.searchParams.set('pageSize', pageSize);

	return fetch(url, {
		headers: HEADERS,
	}).then((response) => response.json());
};
