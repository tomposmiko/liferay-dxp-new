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

import {EDITABLE_TYPES} from './editableTypes';

export const EDITABLE_TYPE_DEFAULT_OPTIONS = {
	[EDITABLE_TYPES.backgroundImage]: {},
	[EDITABLE_TYPES['date-time']]: {
		dateFormat: 'MM/dd/yy',
	},
	[EDITABLE_TYPES.html]: {},
	[EDITABLE_TYPES.image]: {
		alt: '',
		imageTitle: '',
	},
	[EDITABLE_TYPES.link]: {},
	[EDITABLE_TYPES['rich-text']]: {},
	[EDITABLE_TYPES.text]: {},
};
