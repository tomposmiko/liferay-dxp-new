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

import {axios} from './liferay/api';

const headlessAPI = 'o/headless-commerce-delivery-catalog/v1.0';

export function getProducts() {
	const result = getChannelId('Raylife AP').then((response) => {
		const {
			data: {items},
		} = response;

		const channelId = items[0].id;

		return axios.get(
			`${headlessAPI}/channels/${channelId}/products?nestedFields=skus,catalog&fields=name,externalReferenceCode&page=1&pageSize=50`
		);
	});

	return result;
}

export function getChannelId(channelName: string) {
	return axios.get(
		`${headlessAPI}/channels?filter=contains(name, '${channelName}')&fields=id`
	);
}
