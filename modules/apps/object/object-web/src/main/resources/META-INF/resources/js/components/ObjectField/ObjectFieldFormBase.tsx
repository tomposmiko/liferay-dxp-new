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

import ClayForm, {ClayToggle} from '@clayui/form';
import {
	API,
	AutoComplete,
	FormError,
	Input,
	SingleSelect,
	stringIncludesQuery,
} from '@liferay/object-js-components-web';
import React, {
	ChangeEventHandler,
	ReactNode,
	useEffect,
	useMemo,
	useState,
} from 'react';

import {toCamelCase} from '../../utils/string';
import {AggregationFormBase} from './AggregationFormBase';
import {AttachmentFormBase} from './AttachmentFormBase';
import {FORMULA_OUTPUT_OPTIONS, FormulaOutput} from './formulaFieldUtil';

import './ObjectFieldFormBase.scss';

interface IProps {
	children?: ReactNode;
	creationLanguageId2?: Locale;
	disabled?: boolean;
	editingField?: boolean;
	errors: ObjectFieldErrors;
	handleChange: ChangeEventHandler<HTMLInputElement>;
	objectDefinitionExternalReferenceCode: string;
	objectField: Partial<ObjectField>;
	objectFieldTypes: ObjectFieldType[];
	objectName: string;
	objectRelationshipId?: number;
	onAggregationFilterChange?: (aggregationFilterArray: []) => void;
	onRelationshipChange?: (
		objectDefinitionExternalReferenceCode2: string
	) => void;
	setValues: (values: Partial<ObjectField>) => void;
}

type TObjectRelationship = {
	deletionType: string;
	id: number;
	label: LocalizedValue<string>;
	name: string;
	objectDefinitionExternalReferenceCode2: number;
};

export type ObjectFieldErrors = FormError<
	ObjectField & {[key in ObjectFieldSettingName]: unknown}
>;

const defaultLanguageId = Liferay.ThemeDisplay.getDefaultLanguageId();

const fieldSettingsMap = new Map<string, ObjectFieldSetting[]>([
	[
		'Attachment',
		[
			{
				name: 'acceptedFileExtensions',
				value: 'jpeg, jpg, pdf, png',
			},
			{
				name: 'maximumFileSize',
				value: 100,
			},
		],
	],
	[
		'LongText' || 'Text',
		[
			{
				name: 'showCounter',
				value: false,
			},
		],
	],
]);

async function getFieldSettingsByBusinessType(
	objectRelationshipId: number,
	setOneToManyRelationship: (value: TObjectRelationship) => void,
	setPickListItems: (value: PickListItem[]) => void,
	setPickLists: (value: PickList[]) => void,
	setSelectedOutput: (value: string) => void,
	setValues: (values: Partial<ObjectField>) => void,
	values: Partial<ObjectField>
) {
	const {
		businessType,
		defaultValue,
		listTypeDefinitionId,
		objectFieldSettings,
		state,
	} = values;

	if (businessType === 'Picklist' || businessType === 'MultiselectPicklist') {
		const picklistData = await API.getPickLists();

		setPickLists(picklistData);

		if (state && listTypeDefinitionId) {
			const picklistItemsData = await API.getPickListItems(
				listTypeDefinitionId
			);

			setPickListItems(picklistItemsData);
		}

		if (businessType === 'Picklist' && objectFieldSettings?.length) {
			const [{value}] = objectFieldSettings;
			const {objectStates} = value as ObjectFieldPicklistSetting;
			const defaultPicklistValue = objectStates.find(
				({key}) => key === defaultValue
			);

			if (!defaultPicklistValue && defaultValue) {
				setValues({defaultValue: undefined});
			}
		}
	}

	if (businessType === 'Formula') {
		const output = objectFieldSettings?.find(
			(fieldSetting) => fieldSetting.name === 'output'
		);

		if (output) {
			setSelectedOutput(
				FORMULA_OUTPUT_OPTIONS.find(
					(formulaOption) => formulaOption.value === output?.value
				)?.label as string
			);
		}
	}

	if (businessType === 'Relationship' && objectRelationshipId) {
		const relationshipData = await API.getRelationship<TObjectRelationship>(
			objectRelationshipId!
		);

		if (relationshipData.id) {
			setOneToManyRelationship(relationshipData);
		}
	}
}

export default function ObjectFieldFormBase({
	children,
	creationLanguageId2,
	disabled,
	editingField,
	errors,
	handleChange,
	objectDefinitionExternalReferenceCode,
	objectField: values,
	objectFieldTypes,
	objectName,
	objectRelationshipId,
	onAggregationFilterChange,
	onRelationshipChange,
	setValues,
}: IProps) {
	const businessTypeMap = useMemo(() => {
		const businessTypeMap = new Map<string, ObjectFieldType>();

		objectFieldTypes.forEach((type) => {
			businessTypeMap.set(type.businessType, type);
		});

		return businessTypeMap;
	}, [objectFieldTypes]);

	const [picklistDefaultValueQuery, setPicklistDefaultValueQuery] = useState<
		string
	>('');
	const [pickLists, setPickLists] = useState<Partial<PickList>[]>([]);
	const [picklistQuery, setPicklistQuery] = useState<string>('');
	const [pickListItems, setPickListItems] = useState<PickListItem[]>([]);
	const [oneToManyRelationship, setOneToManyRelationship] = useState<
		TObjectRelationship
	>();
	const [selectedOutput, setSelectedOutput] = useState<string>('');
	const [objectDefinition, setObjectDefinition] = useState<
		ObjectDefinition
	>();

	const validListTypeDefinitionId =
		values.listTypeDefinitionId !== undefined &&
		values.listTypeDefinitionId !== 0;

	const filteredPicklistItems = useMemo(() => {
		return pickListItems.filter(({name}) => {
			return stringIncludesQuery(name, picklistDefaultValueQuery);
		});
	}, [picklistDefaultValueQuery, pickListItems]);

	const filteredPicklist = useMemo(() => {
		return pickLists.filter(({name}) => {
			return stringIncludesQuery(name as string, picklistQuery);
		});
	}, [picklistQuery, pickLists]);

	const selectedPicklist = useMemo(() => {
		return pickLists.find(({id}) => values.listTypeDefinitionId === id);
	}, [pickLists, values.listTypeDefinitionId]);

	const handleTypeChange = async (option: ObjectFieldType) => {
		const objectFieldSettings: ObjectFieldSetting[] =
			fieldSettingsMap.get(option.businessType) || [];

		const isSearchableByText =
			option.businessType === 'Attachment' || option.dbType === 'String';

		const indexedAsKeyword = isSearchableByText && values.indexedAsKeyword;

		const indexedLanguageId =
			isSearchableByText && !values.indexedAsKeyword
				? values.indexedLanguageId ?? defaultLanguageId
				: null;

		setValues({
			DBType: option.dbType,
			businessType: option.businessType,
			defaultValue: '',
			indexedAsKeyword,
			indexedLanguageId,
			listTypeDefinitionExternalReferenceCode: '',
			listTypeDefinitionId: 0,
			objectFieldSettings,
			state: false,
		});
	};

	const getMandatoryToggleDisabledState = () => {
		if (
			objectDefinition?.accountEntryRestricted &&
			objectDefinition?.accountEntryRestrictedObjectFieldName ===
				values.name
		) {
			return true;
		}

		if (
			oneToManyRelationship &&
			oneToManyRelationship.deletionType !== 'disassociate'
		) {
			return false;
		}

		const readOnlySetting = values.objectFieldSettings?.find(
			(fieldSetting) => fieldSetting.name === 'readOnly'
		);

		if (
			readOnlySetting?.value === 'true' ||
			readOnlySetting?.value === 'conditional'
		) {
			return true;
		}

		return disabled || values.state;
	};

	useEffect(() => {
		const makeFetch = async () => {
			const objectDefinitionResponse = await API.getObjectDefinitionByExternalReferenceCode(
				objectDefinitionExternalReferenceCode
			);

			setObjectDefinition(objectDefinitionResponse);

			await getFieldSettingsByBusinessType(
				objectRelationshipId as number,
				setOneToManyRelationship,
				setPickListItems,
				setPickLists,
				setSelectedOutput,
				setValues,
				values
			);
		};

		makeFetch();
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [objectDefinitionExternalReferenceCode, values.businessType]);

	return (
		<>
			<Input
				disabled={disabled}
				error={errors.name}
				label={Liferay.Language.get('field-name')}
				name="name"
				onChange={handleChange}
				required
				value={
					values.name ??
					toCamelCase(values.label?.[defaultLanguageId] ?? '', true)
				}
			/>

			<SingleSelect<ObjectFieldType>
				disabled={disabled}
				error={errors.businessType}
				label={Liferay.Language.get('type')}
				onChange={handleTypeChange}
				options={objectFieldTypes}
				required
				value={businessTypeMap.get(values.businessType ?? '')?.label}
			/>

			{values.businessType === 'Attachment' && (
				<AttachmentFormBase
					disabled={disabled}
					error={errors.fileSource}
					objectFieldSettings={
						values.objectFieldSettings as ObjectFieldSetting[]
					}
					objectName={objectName}
					setValues={setValues}
				/>
			)}

			{values.businessType === 'Aggregation' && (
				<AggregationFormBase
					creationLanguageId2={creationLanguageId2 as Locale}
					editingField={editingField}
					errors={errors}
					objectDefinitionExternalReferenceCode={
						objectDefinitionExternalReferenceCode
					}
					objectFieldSettings={
						values.objectFieldSettings as ObjectFieldSetting[]
					}
					onAggregationFilterChange={onAggregationFilterChange}
					onRelationshipChange={onRelationshipChange}
					setValues={setValues}
				/>
			)}

			{values.businessType === 'Formula' && (
				<SingleSelect<FormulaOutput>
					error={errors.output}
					label={Liferay.Language.get('output')}
					onChange={({label, value}) => {
						let newObjectFieldSettings: ObjectFieldSetting[] = [];

						if (values.objectFieldSettings) {
							newObjectFieldSettings = values.objectFieldSettings?.filter(
								(objectFieldSetting) =>
									objectFieldSetting.name !== 'output'
							) as ObjectFieldSetting[];
						}

						setValues({
							objectFieldSettings: [
								...newObjectFieldSettings,
								{
									name: 'output',
									value,
								},
							],
						});

						setSelectedOutput(label);
					}}
					options={FORMULA_OUTPUT_OPTIONS}
					required
					value={selectedOutput}
				/>
			)}

			{(values.businessType === 'Picklist' ||
				values.businessType === 'MultiselectPicklist') && (
				<AutoComplete<Partial<PickList>>
					disabled={disabled}
					emptyStateMessage={Liferay.Language.get('option-not-found')}
					error={errors.listTypeDefinitionId}
					items={filteredPicklist}
					label={Liferay.Language.get('picklist')}
					onChangeQuery={setPicklistQuery}
					onSelectItem={(item) => {
						setValues({
							defaultValue: '',
							listTypeDefinitionExternalReferenceCode:
								item.externalReferenceCode,
							listTypeDefinitionId: item.id,
							state: false,
						});
					}}
					query={picklistQuery}
					value={selectedPicklist?.name}
				>
					{({name}) => (
						<div className="d-flex justify-content-between">
							<div>{name}</div>
						</div>
					)}
				</AutoComplete>
			)}

			{children}

			<ClayForm.Group className="lfr-objects__object-field-form-base-form-group-toggles">
				{values.businessType !== 'Aggregation' &&
					values.businessType !== 'Formula' && (
						<ClayToggle
							disabled={getMandatoryToggleDisabledState()}
							label={Liferay.Language.get('mandatory')}
							name="required"
							onToggle={(required) => setValues({required})}
							toggled={values.required || values.state}
						/>
					)}

				{values.businessType === 'Picklist' &&
					validListTypeDefinitionId && (
						<ClayToggle
							disabled={disabled}
							label={Liferay.Language.get('mark-as-state')}
							name="state"
							onToggle={async (state) => {
								if (state) {
									setValues({required: state, state});
									setPickListItems(
										await API.getPickListItems(
											values.listTypeDefinitionId!
										)
									);
								}
								else {
									setValues({
										defaultValue: '',
										required: state,
										state,
									});
								}
							}}
							toggled={values.state}
						/>
					)}
			</ClayForm.Group>

			{values.state && (
				<AutoComplete<PickListItem>
					emptyStateMessage={Liferay.Language.get('option-not-found')}
					error={errors.defaultValue}
					items={filteredPicklistItems}
					label={Liferay.Language.get('default-value')}
					onChangeQuery={setPicklistDefaultValueQuery}
					onSelectItem={(item) => {
						setValues({
							defaultValue: item.key,
						});
					}}
					placeholder={Liferay.Language.get('choose-an-option')}
					query={picklistDefaultValueQuery}
					required
					value={
						filteredPicklistItems.find(
							({key}) => key === values.defaultValue
						)?.name
					}
				>
					{({name}) => (
						<div className="d-flex justify-content-between">
							<div>{name}</div>
						</div>
					)}
				</AutoComplete>
			)}
		</>
	);
}
