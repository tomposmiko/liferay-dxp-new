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

import ClayLoadingIndicator from '@clayui/loading-indicator';
import classNames from 'classnames';
import React, {useMemo} from 'react';

import {ALLOWED_INPUT_TYPES} from '../../../../../../app/config/constants/allowedInputTypes';
import {FRAGMENT_ENTRY_TYPES} from '../../../../../../app/config/constants/fragmentEntryTypes';
import {FREEMARKER_FRAGMENT_ENTRY_PROCESSOR} from '../../../../../../app/config/constants/freemarkerFragmentEntryProcessor';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../../../../app/config/constants/layoutDataItemTypes';
import {
	useDispatch,
	useSelector,
	useSelectorCallback,
	useSelectorRef,
} from '../../../../../../app/contexts/StoreContext';
import selectFormConfiguration from '../../../../../../app/selectors/selectFormConfiguration';
import selectFragmentEntryLink from '../../../../../../app/selectors/selectFragmentEntryLink';
import selectLanguageId from '../../../../../../app/selectors/selectLanguageId';
import selectSegmentsExperienceId from '../../../../../../app/selectors/selectSegmentsExperienceId';
import FormService from '../../../../../../app/services/FormService';
import InfoItemService from '../../../../../../app/services/InfoItemService';
import updateEditableValues from '../../../../../../app/thunks/updateEditableValues';
import {CACHE_KEYS} from '../../../../../../app/utils/cache';
import {isFormRequiredField} from '../../../../../../app/utils/isFormRequiredField';
import {setIn} from '../../../../../../app/utils/setIn';
import useCache from '../../../../../../app/utils/useCache';
import Collapse from '../../../../../../common/components/Collapse';
import MappingFieldSelector from '../../../../../../common/components/MappingFieldSelector';
import {FieldSet} from './FieldSet';
import {FragmentGeneralPanel} from './FragmentGeneralPanel';

const DEFAULT_CONFIGURATION_VALUES = {};
const DEFAULT_FORM_CONFIGURATION = {classNameId: null, classTypeId: null};

const FIELD_ID_CONFIGURATION_KEY = 'inputFieldId';
const HELP_TEXT_CONFIGURATION_KEY = 'inputHelpText';
const REQUIRED_CONFIGURATION_KEY = 'inputRequired';
const SHOW_HELP_TEXT_CONFIGURATION_KEY = 'inputShowHelpText';

function getInputCommonConfiguration(configurationValues, formFields) {
	const fields = [];

	if (configurationValues[FIELD_ID_CONFIGURATION_KEY]) {
		const isRequiredField = isFormRequiredField(
			configurationValues[FIELD_ID_CONFIGURATION_KEY],
			formFields
		);

		fields.push({
			defaultValue: isRequiredField,
			disabled: isRequiredField,
			label: Liferay.Language.get('mark-as-required'),
			name: REQUIRED_CONFIGURATION_KEY,
			type: 'checkbox',
		});
	}

	fields.push(
		{
			defaultValue: true,
			label: Liferay.Language.get('show-label'),
			name: 'inputShowLabel',
			type: 'checkbox',
			typeOptions: {displayType: 'toggle'},
		},
		{
			defaultValue: '',
			label: Liferay.Language.get('label'),
			localizable: true,
			name: 'inputLabel',
			type: 'text',
		},
		{
			defaultValue: true,
			label: Liferay.Language.get('show-help-text'),
			name: SHOW_HELP_TEXT_CONFIGURATION_KEY,
			type: 'checkbox',
			typeOptions: {displayType: 'toggle'},
		}
	);

	if (configurationValues[SHOW_HELP_TEXT_CONFIGURATION_KEY] !== false) {
		fields.push({
			defaultValue: Liferay.Language.get(
				'guide-your-users-to-fill-in-the-field-by-adding-help-text-here'
			),
			label: Liferay.Language.get('help-text'),
			localizable: true,
			name: HELP_TEXT_CONFIGURATION_KEY,
			type: 'text',
		});
	}

	return fields;
}

function getTypeLabels(itemTypes, classNameId, classTypeId) {
	if (!itemTypes || !classNameId) {
		return {};
	}

	const selectedType = itemTypes.find(({value}) => value === classNameId);

	const selectedSubtype = selectedType.subtypes.length
		? selectedType.subtypes.find(({value}) => value === classTypeId)
		: {};

	return {
		subtype: selectedSubtype.label,
		type: selectedType.label,
	};
}

export function FormInputGeneralPanel({item}) {
	const dispatch = useDispatch();
	const languageId = useSelector(selectLanguageId);
	const segmentsExperienceId = useSelector(selectSegmentsExperienceId);

	const fragmentEntryLinkRef = useSelectorRef((state) =>
		selectFragmentEntryLink(state, item)
	);

	const configurationValues = useSelectorCallback(
		(state) =>
			selectFragmentEntryLink(state, item).editableValues[
				FREEMARKER_FRAGMENT_ENTRY_PROCESSOR
			] || DEFAULT_CONFIGURATION_VALUES,
		[item.itemId]
	);

	const {classNameId, classTypeId, formId} = useSelectorCallback(
		(state) =>
			selectFormConfiguration(item, state.layoutData) ||
			DEFAULT_FORM_CONFIGURATION,
		[item.itemId]
	);

	const formFields = useCache({
		fetcher: () => FormService.getFormFields({classNameId, classTypeId}),
		key: [CACHE_KEYS.formFields, classNameId, classTypeId],
	});

	const fields = useMemo(() => {
		let nextFields = getInputCommonConfiguration(
			configurationValues,
			formFields
		);

		const fieldSetsWithoutLabel =
			fragmentEntryLinkRef.current.configuration?.fieldSets?.filter(
				(fieldSet) => !fieldSet.configurationRole && !fieldSet.label
			) ?? [];

		nextFields = [
			...nextFields,
			...fieldSetsWithoutLabel.flatMap((fieldSet) => fieldSet.fields),
		];

		return nextFields;
	}, [configurationValues, fragmentEntryLinkRef, formFields]);

	const handleValueSelect = (key, value) => {
		const keyPath = [FREEMARKER_FRAGMENT_ENTRY_PROCESSOR, key];

		const localizable =
			fields.find((field) => field.name === key)?.localizable || false;

		if (localizable) {
			keyPath.push(languageId);
		}

		dispatch(
			updateEditableValues({
				editableValues: setIn(
					fragmentEntryLinkRef.current.editableValues,
					keyPath,
					value
				),
				fragmentEntryLinkId:
					fragmentEntryLinkRef.current.fragmentEntryLinkId,
				languageId,
				segmentsExperienceId,
			})
		);
	};

	return (
		<>
			<div className="mb-3">
				<Collapse
					label={Liferay.Language.get('form-input-options')}
					open
				>
					<FormInputMappingOptions
						configurationValues={configurationValues}
						form={{
							classNameId,
							classTypeId,
							fields: formFields,
							formId,
						}}
						item={item}
						onValueSelect={handleValueSelect}
					/>

					<FieldSet
						fields={fields}
						item={item}
						label=""
						languageId={languageId}
						onValueSelect={handleValueSelect}
						values={configurationValues}
					/>
				</Collapse>
			</div>

			<FragmentGeneralPanel item={item} />
		</>
	);
}

function FormInputMappingOptions({
	configurationValues,
	form,
	item,
	onValueSelect,
}) {
	const {classNameId, classTypeId, fields, formId} = form;

	const inputType = useSelectorCallback(
		(state) => {
			const element = document.createElement('div');
			element.innerHTML = selectFragmentEntryLink(state, item).content;

			if (element.querySelector('select')) {
				return 'select';
			}
			else if (element.querySelector('textarea')) {
				return 'textarea';
			}

			return element.querySelector('input')?.type || 'text';
		},
		[item.itemId]
	);

	const itemTypes = useCache({
		fetcher: () => InfoItemService.getAvailableInfoItemFormProviders(),
		key: [CACHE_KEYS.itemTypes],
	});

	const {subtype, type} = useMemo(
		() => getTypeLabels(itemTypes, classNameId, classTypeId),
		[itemTypes, classNameId, classTypeId]
	);

	const filteredFields = useSelectorCallback(
		(state) => {
			if (!fields) {
				return fields;
			}

			let nextFields = fields;

			const selectedFields = (() => {
				const selectedFields = [];

				const findSelectedFields = (itemId) => {
					const inputItem = state.layoutData.items[itemId];

					if (
						inputItem?.itemId !== item.itemId &&
						inputItem?.type === LAYOUT_DATA_ITEM_TYPES.fragment
					) {
						const {
							editableValues,
							fragmentEntryType,
						} = selectFragmentEntryLink(state, inputItem);

						if (
							fragmentEntryType === FRAGMENT_ENTRY_TYPES.input &&
							editableValues[FREEMARKER_FRAGMENT_ENTRY_PROCESSOR][
								FIELD_ID_CONFIGURATION_KEY
							]
						) {
							selectedFields.push(
								editableValues[
									FREEMARKER_FRAGMENT_ENTRY_PROCESSOR
								][FIELD_ID_CONFIGURATION_KEY]
							);
						}
					}

					inputItem?.children.forEach(findSelectedFields);
				};

				findSelectedFields(formId);

				return selectedFields;
			})();

			nextFields = nextFields
				.map((fieldset) => ({
					...fieldset,
					fields: fieldset.fields.filter(
						(field) =>
							ALLOWED_INPUT_TYPES[field.type]?.includes(
								inputType
							) && !selectedFields.includes(field.key)
					),
				}))
				.filter((fieldset) => fieldset.fields.length);

			return nextFields;
		},
		[item.itemId, fields, inputType]
	);

	if (!classNameId || !classTypeId) {
		return null;
	}

	return filteredFields ? (
		<>
			<MappingFieldSelector
				fieldType={inputType}
				fields={filteredFields}
				onValueSelect={(event) =>
					onValueSelect(
						FIELD_ID_CONFIGURATION_KEY,
						event.target.value === 'unmapped'
							? null
							: event.target.value
					)
				}
				value={configurationValues[FIELD_ID_CONFIGURATION_KEY] || ''}
			/>
			{type && (
				<p
					className={classNames(
						'page-editor__mapping-panel__type-label',
						{
							'mb-0': subtype,
							'mb-3': !subtype,
						}
					)}
				>
					<span className="mr-1">
						{Liferay.Language.get('content-type')}:
					</span>

					{type}
				</p>
			)}

			{subtype && (
				<p className="mb-3 page-editor__mapping-panel__type-label">
					<span className="mr-1">
						{Liferay.Language.get('subtype')}:
					</span>

					{subtype}
				</p>
			)}
		</>
	) : (
		<ClayLoadingIndicator />
	);
}
