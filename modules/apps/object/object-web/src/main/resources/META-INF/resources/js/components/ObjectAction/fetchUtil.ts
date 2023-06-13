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

import {API} from '@liferay/object-js-components-web';

export type ObjectsOptionsList = {
	label: string;
	options: LabelValueObject[];
	type: string;
}[];

function fillSelect(
	label: string,
	options: LabelValueObject[],
	objectsOptionsList: ObjectsOptionsList
) {
	if (options.length) {
		objectsOptionsList.push({label, options, type: 'group'});
	}
}

export async function fetchObjectDefinitions(
	objectDefinitionsRelationshipsURL: string,
	values: Partial<ObjectAction>,
	setRelationships: (values: ObjectDefinitionsRelationship[]) => void,
	setObjectOptions: (values: ObjectsOptionsList) => void
) {
	const relationships = await API.fetchJSON<ObjectDefinitionsRelationship[]>(
		objectDefinitionsRelationshipsURL
	);

	const relatedObjects: LabelValueObject[] = [];
	const unrelatedObjects: LabelValueObject[] = [];

	relationships?.forEach((object) => {
		const {externalReferenceCode, id, label, system} = object;

		const target = object.related ? relatedObjects : unrelatedObjects;

		target.push({label, value: `${externalReferenceCode},${id},${system}`});
	});

	const objectsOptionsList: ObjectsOptionsList = [];

	fillSelect(
		Liferay.Language.get('related-objects'),
		relatedObjects,
		objectsOptionsList
	);

	fillSelect(
		Liferay.Language.get('unrelated-objects'),
		unrelatedObjects,
		objectsOptionsList
	);

	setObjectOptions(objectsOptionsList);
	setRelationships(relationships);
}

export async function fetchObjectDefinitionFields(
	objectDefinitionId: number,
	objectDefinitionExternalReferenceCode: string,
	systemObject: boolean,
	values: Partial<ObjectAction>,
	isValidField: (
		{businessType, name, objectFieldSettings, system}: ObjectField,
		isObjectActionSystem?: boolean
	) => boolean,
	setCurrentObjectDefinitionFields: (values: ObjectField[]) => void,
	setValues: (values: Partial<ObjectAction>) => void
) {
	let definitionId = objectDefinitionId;
	let externalReferenceCode = objectDefinitionExternalReferenceCode;
	const validFields: ObjectField[] = [];

	if (values.objectActionExecutorKey === 'add-object-entry') {
		definitionId = values?.parameters?.objectDefinitionId as number;
		externalReferenceCode = values.parameters
			?.objectDefinitionExternalReferenceCode as string;
	}

	if (externalReferenceCode) {
		const items = await API.getObjectFieldsByExternalReferenceCode(
			externalReferenceCode
		);

		items.forEach((field) => {
			if (isValidField(field, systemObject)) {
				validFields.push(field);
			}
		});
	}

	setCurrentObjectDefinitionFields(validFields);

	const {predefinedValues = []} = values.parameters as ObjectActionParameters;

	const predefinedValuesMap = new Map<string, PredefinedValue>();

	predefinedValues.forEach((field) => {
		predefinedValuesMap.set(field.name, field);
	});

	const newPredefinedValues: PredefinedValue[] = [];

	validFields.forEach(({label, name, required}) => {
		if (predefinedValuesMap.has(name)) {
			const field = predefinedValuesMap.get(name);

			newPredefinedValues.push(field as PredefinedValue);
		}
		else if (
			required &&
			values.objectActionExecutorKey === 'add-object-entry'
		) {
			newPredefinedValues.push({
				inputAsValue: false,
				label,
				name,
				value: '',
			});
		}
	});
	setValues({
		parameters: {
			...values.parameters,
			objectDefinitionExternalReferenceCode: externalReferenceCode,
			objectDefinitionId: definitionId,
			predefinedValues: newPredefinedValues,
		},
	});
}
