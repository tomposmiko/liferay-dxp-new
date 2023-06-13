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

import {CACHE_KEYS, getCacheItem, getCacheKey} from './cache';

export function formIsMapped(item) {
	const {classNameId, classTypeId} = item.config;

	const formTypes = getCacheItem(getCacheKey([CACHE_KEYS.formTypes])).data;

	if (!formTypes) {
		return classNameId && classNameId !== '0' ? true : false;
	}

	const type = formTypes.find(({value}) => value === classNameId);

	if (!type) {
		return false;
	}

	const subtype = type.subtypes.find(({value}) => value === classTypeId);

	if (subtype || classTypeId === '0') {
		return true;
	}

	return false;
}
