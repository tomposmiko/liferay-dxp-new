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

import {Parameters, parametersFormater} from '.';
import {axios} from './liferay/api';

const headlessAPI = 'o/headless-user-notification/v1.0';

export function getUserNotification(parameters: Parameters) {
	const parametersList = Object.keys(parameters);

	if (parametersList.length) {
		return axios.get(
			`${headlessAPI}/my-user-notifications/?${parametersFormater(
				parametersList,
				parameters
			)}`
		);
	}

	return axios.get(`${headlessAPI}/my-user-notifications`);
}

export function putUserNotificationRead(userNotificationId: number) {
	return axios.put(
		`${headlessAPI}/user-notifications/${userNotificationId}/read`
	);
}
