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

import {
	PagesVisitor,
	generateName,
	getRepeatedIndex,
	parseName,
} from 'dynamic-data-mapping-form-renderer';

import {FIELD_TYPE_FIELDSET} from './constants.es';

export const generateId = (length, allowOnlyNumbers = false) => {
	let text = '';

	const possible = allowOnlyNumbers
		? '0123456789'
		: 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

	for (let i = 0; i < length; i++) {
		text += possible.charAt(Math.floor(Math.random() * possible.length));
	}

	return text;
};

export const generateInstanceId = (length) => {
	return generateId(length);
};

export const getDefaultFieldName = (isOptionField = false) => {
	const defaultFieldName = isOptionField
		? Liferay.Language.get('option')
		: Liferay.Language.get('field');

	return defaultFieldName + generateId(8, true);
};

export const getFieldProperties = (
	{pages},
	defaultLanguageId,
	editingLanguageId
) => {
	const properties = {};
	const visitor = new PagesVisitor(pages);

	visitor.mapFields(
		({fieldName, localizable, localizedValue = {}, type, value}) => {
			if (
				localizable &&
				localizedValue[editingLanguageId] !== undefined
			) {
				properties[fieldName] = localizedValue[editingLanguageId];
			}
			else if (localizable && localizedValue[defaultLanguageId]) {
				properties[fieldName] = localizedValue[defaultLanguageId];
			}
			else if (type == 'options') {
				if (!value[editingLanguageId] && value[defaultLanguageId]) {
					properties[fieldName] = value[defaultLanguageId];
				}
				else {
					properties[fieldName] = value[editingLanguageId];
				}
			}
			else if (type == 'validation') {
				if (!value.errorMessage[editingLanguageId]) {
					value.errorMessage[editingLanguageId] =
						value.errorMessage[defaultLanguageId];
				}

				if (!value.parameter[editingLanguageId]) {
					value.parameter[editingLanguageId] =
						value.parameter[defaultLanguageId];
				}

				properties[fieldName] = value;
			}
			else {
				properties[fieldName] = value;
			}
		}
	);

	return properties;
};

export const normalizeSettingsContextPages = (
	pages,
	defaultLanguageId,
	editingLanguageId,
	fieldType,
	generatedFieldName
) => {
	const visitor = new PagesVisitor(pages);

	return visitor.mapFields(
		(field) => {
			const {fieldName} = field;

			if (fieldName === 'fieldReference' || fieldName === 'name') {
				field = {
					...field,
					value: generatedFieldName,
				};
			}
			else if (fieldName === 'label') {
				const localizedValue = {
					...field.localizedValue,
					[editingLanguageId]: fieldType.label,
				};

				if (
					editingLanguageId !== defaultLanguageId &&
					!localizedValue[defaultLanguageId]
				) {
					localizedValue[defaultLanguageId] = fieldType.label;
				}

				field = {
					...field,
					localizedValue,
					type: 'text',
					value: fieldType.label,
				};
			}
			else if (fieldName === 'type') {
				field = {
					...field,
					value: fieldType.name,
				};
			}
			else if (fieldName === 'validation') {
				field = {
					...field,
					validation: {
						...field.validation,
						fieldName: generatedFieldName,
					},
				};
			}

			const newInstanceId = generateInstanceId(8);

			if (field.type === 'rich_text' && field.editorConfig) {
				const {editorConfig} = field;

				Object.keys(editorConfig).map((key) => {
					if (typeof editorConfig[key] === 'string') {
						const parsedName = parseName(
							decodeURIComponent(editorConfig[key])
						);

						if (parsedName.instanceId) {
							editorConfig[key] = editorConfig[key].replace(
								parsedName.instanceId,
								newInstanceId
							);
						}
					}
				});
			}

			return {
				...field,
				instanceId: newInstanceId,
				name: generateName(field.name, {
					instanceId: newInstanceId,
					repeatedIndex: getRepeatedIndex(field.name),
				}),
			};
		},
		false,
		true
	);
};

export const createField = (props, event) => {
	const {
		defaultLanguageId,
		editingLanguageId,
		fieldNameGenerator,
		spritemap,
	} = props;
	const {
		fieldType,
		skipFieldNameGeneration = false,
		useFieldName = '',
	} = event;

	let newFieldName = useFieldName;

	if (!useFieldName) {
		if (skipFieldNameGeneration) {
			const {settingsContext} = fieldType;
			const visitor = new PagesVisitor(settingsContext.pages);

			visitor.mapFields(({fieldName, value}) => {
				if (fieldName === 'name') {
					newFieldName = value;
				}
			});
		}
		else {
			newFieldName = fieldNameGenerator(getDefaultFieldName());
		}
	}

	const newField = {
		...fieldType,
		fieldName: newFieldName,
		fieldReference: newFieldName,
		name: newFieldName,
		settingsContext: {
			...fieldType.settingsContext,
			pages: normalizeSettingsContextPages(
				fieldType.settingsContext.pages,
				defaultLanguageId,
				editingLanguageId,
				fieldType,
				newFieldName
			),
			type: fieldType.name,
		},
	};

	const {fieldName, fieldReference, name, settingsContext} = newField;

	return {
		...getFieldProperties(
			settingsContext,
			defaultLanguageId,
			editingLanguageId
		),
		fieldName,
		fieldReference,
		instanceId: generateInstanceId(8),
		name,
		settingsContext,
		spritemap,
		type: fieldType.name,
	};
};

export const formatFieldName = (instanceId, languageId, value) => {
	return `ddm$$${value}$${instanceId}$0$$${languageId}`;
};

export const getField = (pages, fieldName) => {
	const visitor = new PagesVisitor(pages);

	return visitor.findField((field) => field.fieldName === fieldName);
};

export const getParentField = (pages, fieldName) => {
	let parentField = null;
	const visitor = new PagesVisitor(pages);

	visitor.visitFields((field) => {
		const nestedFieldsVisitor = new PagesVisitor(field.nestedFields || []);

		if (nestedFieldsVisitor.containsField(fieldName)) {
			parentField = field;
		}

		return false;
	});

	return parentField;
};

export const isFieldSet = (field) =>
	field.type === FIELD_TYPE_FIELDSET && field.ddmStructureId;

export const getParentFieldSet = (pages, fieldName) => {
	let parentField = getParentField(pages, fieldName);

	while (parentField) {
		if (isFieldSet(parentField)) {
			return parentField;
		}

		parentField = getParentField(pages, parentField.fieldName);
	}

	return null;
};

export const isFieldSetChild = (pages, fieldName) => {
	return !!getParentFieldSet(pages, fieldName);
};

export const localizeField = (field, defaultLanguageId, editingLanguageId) => {
	let value = field.value;

	if (field.dataType === 'json' && typeof value === 'object') {
		value = JSON.stringify(value);
	}

	if (field.localizable && field.localizedValue) {
		let localizedValue = field.localizedValue[editingLanguageId];

		if (localizedValue === undefined) {
			localizedValue = field.localizedValue[defaultLanguageId];
		}

		if (localizedValue !== undefined) {
			value = localizedValue;
		}
	}
	else if (
		field.dataType === 'ddm-options' &&
		value[editingLanguageId] === undefined
	) {
		value = {
			...value,
			[editingLanguageId]: value[defaultLanguageId],
		};
	}

	return {
		...field,
		defaultLanguageId,
		editingLanguageId,
		localizedValue: {
			...(field.localizedValue || {}),
			[editingLanguageId]: value,
		},
		value,
	};
};
