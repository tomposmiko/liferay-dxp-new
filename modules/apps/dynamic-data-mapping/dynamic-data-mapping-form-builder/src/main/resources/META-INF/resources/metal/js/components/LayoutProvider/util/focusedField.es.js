import {
	generateFieldName,
	normalizeFieldName,
	updateFieldValidationProperty
} from './fields.es';
import {getField} from '../util/fields.es';
import {updateSettingsContextProperty} from './settings.es';

const shouldAutoGenerateName = focusedField => {
	const {fieldName, label} = focusedField;

	return fieldName.indexOf(normalizeFieldName(label)) === 0;
};

export const updateFocusedFieldName = (state, focusedField, value) => {
	const {fieldName, label} = focusedField;
	const normalizedFieldName = normalizeFieldName(value);

	const {pages} = state;
	let newFieldName;

	if (normalizedFieldName !== '') {
		newFieldName = generateFieldName(pages, value, fieldName);
	}
	else {
		newFieldName = generateFieldName(pages, label, fieldName);
	}

	if (newFieldName) {
		let {settingsContext} = focusedField;

		settingsContext = {
			...settingsContext,
			pages: updateFieldValidationProperty(settingsContext.pages, fieldName, 'fieldName', newFieldName)
		};

		focusedField = {
			...focusedField,
			fieldName: newFieldName,
			settingsContext: updateSettingsContextProperty(state, settingsContext, 'name', newFieldName)
		};
	}

	return focusedField;
};

export const updateFocusedFieldDataType = (state, focusedField, value) => {
	let {settingsContext} = focusedField;

	settingsContext = {
		...settingsContext,
		pages: updateFieldValidationProperty(settingsContext.pages, focusedField.fieldName, 'dataType', value)
	};

	return {
		...focusedField,
		dataType: value,
		settingsContext: updateSettingsContextProperty(state, settingsContext, 'dataType', value)
	};
};

export const updateFocusedFieldLabel = (state, focusedField, value) => {
	let {fieldName, settingsContext} = focusedField;

	if (shouldAutoGenerateName(focusedField)) {
		const updates = updateFocusedFieldName(state, focusedField, value);

		fieldName = updates.fieldName;
		settingsContext = updates.settingsContext;
	}

	return {
		...focusedField,
		fieldName,
		label: value,
		settingsContext: updateSettingsContextProperty(state, settingsContext, 'label', value)
	};
};

export const updateFocusedFieldProperty = (state, focusedField, propertyName, propertyValue) => {
	return {
		...focusedField,
		[propertyName]: propertyValue,
		settingsContext: updateSettingsContextProperty(state, focusedField.settingsContext, propertyName, propertyValue)
	};
};

export const updateFocusedFieldOptions = (state, focusedField, options) => {
	const withNewOptions = updateSettingsContextProperty(state, focusedField.settingsContext, 'options', options);

	const predefinedValue = getField(withNewOptions.pages, 'predefinedValue');

	if (predefinedValue) {
		const {value} = predefinedValue;

		if (value && Array.isArray(value)) {
			predefinedValue.value = value.filter(
				currentValue => options.some(
					option => option.value === currentValue
				)
			);
		}
	}

	return {
		...focusedField,
		options,
		predefinedValue: predefinedValue.value,
		settingsContext: updateSettingsContextProperty(
			state,
			withNewOptions,
			'predefinedValue',
			predefinedValue.value
		)
	};
};

export const updateFocusedField = (state, fieldName, value) => {
	let {focusedField} = state;

	if (fieldName === 'dataType') {
		focusedField = {
			...focusedField,
			...updateFocusedFieldDataType(state, focusedField, value)
		};
	}
	else if (fieldName === 'label') {
		focusedField = {
			...focusedField,
			...updateFocusedFieldLabel(state, focusedField, value)
		};
	}
	else if (fieldName === 'name') {
		focusedField = {
			...focusedField,
			...updateFocusedFieldName(state, focusedField, value)
		};
	}
	else if (fieldName === 'options') {
		focusedField = {
			...focusedField,
			...updateFocusedFieldOptions(state, focusedField, value)
		};
	}
	else {
		focusedField = {
			...focusedField,
			...updateFocusedFieldProperty(state, focusedField, fieldName, value)
		};
	}

	return focusedField;
};