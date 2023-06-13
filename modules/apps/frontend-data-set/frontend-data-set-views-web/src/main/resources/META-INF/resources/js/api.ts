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

import {fetch, openToast} from 'frontend-js-web';

import {OBJECT_RELATIONSHIP} from './Constants';
import {FDSViewType} from './FDSViews';

interface Field {
	format: string;
	label: string;
	name: string;
	type: string;
}

export async function getFields(fdsView: FDSViewType) {
	const {restApplication, restSchema} = fdsView[
		OBJECT_RELATIONSHIP.FDS_ENTRY_FDS_VIEW
	];

	const response = await fetch(`/o${restApplication}/openapi.json`);

	if (!response.ok) {
		openToast({
			message: Liferay.Language.get('your-request-failed-to-complete'),
			type: 'danger',
		});

		return;
	}

	const responseJSON = await response.json();

	const properties =
		responseJSON?.components?.schemas[restSchema]?.properties;

	if (!properties) {
		openToast({
			message: Liferay.Language.get('your-request-failed-to-complete'),
			type: 'danger',
		});

		return;
	}

	const fieldsArray: Array<Field> = [];

	const isObjectSchema =
		responseJSON.components.schemas[restSchema].xml.name === 'ObjectEntry';

	Object.keys(properties).map((propertyKey) => {
		const propertyValue = properties[propertyKey];

		if (isObjectSchema && !propertyValue.extensions) {
			return;
		}

		if (propertyKey === 'x-class-name') {
			return;
		}

		const type = propertyValue.type;

		if (type === 'object' || type === 'array') {
			return;
		}

		fieldsArray.push({
			format: properties[propertyKey].format || type,
			label: propertyKey,
			name: propertyKey,
			type,
		});
	});

	return fieldsArray;
}
