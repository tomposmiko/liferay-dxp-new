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

import {getDDMFormFieldSettingsContext} from './utils/dataConverter.es';
import {getDataDefinitionField} from './utils/dataDefinition.es';

export const ADD_CUSTOM_OBJECT_FIELD = 'ADD_CUSTOM_OBJECT_FIELD';
export const ADD_DATA_LAYOUT_RULE = 'ADD_DATA_LAYOUT_RULE';
export const DELETE_DATA_DEFINITION_FIELD = 'DELETE_DATA_DEFINITION_FIELD';
export const DELETE_DATA_LAYOUT_FIELD = 'DELETE_DATA_LAYOUT_FIELD';
export const DELETE_DATA_LAYOUT_RULE = 'DELETE_DATA_LAYOUT_RULE';
export const EDIT_CUSTOM_OBJECT_FIELD = 'EDIT_CUSTOM_OBJECT_FIELD';
export const EVALUATION_ERROR = 'EVALUATION_ERROR';
export const SET_FORM_RENDERER_CUSTOM_FIELDS =
	'SET_FORM_RENDERER_CUSTOM_FIELDS';
export const SWITCH_SIDEBAR_PANEL = 'SWITCH_SIDEBAR_PANEL';
export const UPDATE_APP_PROPS = 'UPDATE_APP_PROPS';
export const UPDATE_CONFIG = 'UPDATE_CONFIG';
export const UPDATE_FIELDSETS = 'UPDATE_FIELDSETS';
export const UPDATE_FOCUSED_CUSTOM_OBJECT_FIELD =
	'UPDATE_FOCUSED_CUSTOM_OBJECT_FIELD';
export const UPDATE_DATA_DEFINITION = 'UPDATE_DATA_DEFINITION';
export const UPDATE_DATA_DEFINITION_AVAILABLE_LANGUAGE =
	'UPDATE_DATA_DEFINITION_AVAILABLE_LANGUAGE';
export const UPDATE_DATA_DEFINITION_FIELDS = 'UPDATE_DATA_DEFINITION_FIELDS';
export const UPDATE_DATA_LAYOUT = 'UPDATE_DATA_LAYOUT';
export const UPDATE_DATA_LAYOUT_FIELDS = 'UPDATE_DATA_LAYOUT_FIELDS';
export const UPDATE_DATA_LAYOUT_NAME = 'UPDATE_DATA_LAYOUT_NAME';
export const UPDATE_DATA_LAYOUT_RULE = 'UPDATE_DATA_LAYOUT_RULE';
export const UPDATE_EDITING_DATA_DEFINITION_ID =
	'UPDATE_EDITING_DATA_DEFINITION_ID';
export const UPDATE_EDITING_LANGUAGE_ID = 'UPDATE_EDITING_LANGUAGE_ID';
export const UPDATE_FIELD_TYPES = 'UPDATE_FIELD_TYPES';
export const UPDATE_FOCUSED_FIELD = 'UPDATE_FOCUSED_FIELD';
export const UPDATE_HOVERED_FIELD = 'UPDATE_HOVERED_FIELD';
export const UPDATE_IDS = 'UPDATE_IDS';
export const UPDATE_PAGES = 'UPDATE_PAGES';

export const dropCustomObjectField = ({
	dataDefinition,
	dataDefinitionFieldName,
	dataLayoutBuilder,
	fieldName,
	indexes,
	parentFieldName,
}) => {
	const dataDefinitionField = getDataDefinitionField(
		dataDefinition,
		dataDefinitionFieldName
	);

	const {
		appContext: [{editingLanguageId}],
		fieldTypes,
	} = dataLayoutBuilder.props;

	const settingsContext = getDDMFormFieldSettingsContext({
		dataDefinitionField,
		editingLanguageId,
		fieldTypes,
	});

	const {label} = dataDefinitionField;

	return {
		data: {
			fieldName,
			parentFieldName,
		},
		fieldType: {
			...fieldTypes.find(({name}) => {
				return name === dataDefinitionField.fieldType;
			}),
			editable: true,
			label:
				label[editingLanguageId] || label[themeDisplay.getLanguageId()],
			settingsContext,
		},
		indexes,
		skipFieldNameGeneration: true,
	};
};

export const dropLayoutBuilderField = ({
	fieldName,
	fieldTypeName,
	fieldTypes,
	indexes,
	parentFieldName,
}) => {
	return {
		data: {
			fieldName,
			parentFieldName,
		},
		fieldType: {
			...fieldTypes.find(({name}) => {
				return name === fieldTypeName;
			}),
			editable: true,
		},
		indexes,
	};
};
