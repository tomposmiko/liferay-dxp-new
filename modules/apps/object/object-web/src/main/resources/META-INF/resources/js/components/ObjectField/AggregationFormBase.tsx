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
	API,
	AutoComplete,
	SingleSelect,
	stringIncludesQuery,
} from '@liferay/object-js-components-web';
import React, {useEffect, useMemo, useState} from 'react';

import {normalizeFieldSettings} from '../../utils/fieldSettings';
import {ObjectFieldErrors} from './ObjectFieldFormBase';

interface IAggregationSourcePropertyProps {
	disabled?: boolean;
	editingField?: boolean;
	errors: ObjectFieldErrors;
	objectDefinitionId: number;
	objectFieldSettings: ObjectFieldSetting[];
	onAggregationFilterChange?: (aggregationFilterArray: []) => void;
	onRelationshipChange?: (objectDefinitionId2: number) => void;
	setValues: (values: Partial<ObjectField>) => void;
}

type TObjectRelationship = {
	label: LocalizedValue<string>;
	name: string;
	objectDefinitionId2: number;
};

const defaultLanguageId = Liferay.ThemeDisplay.getDefaultLanguageId();

const aggregationFunctions = [
	{
		label: Liferay.Language.get('count'),
		value: 'COUNT',
	},
	{
		label: Liferay.Language.get('sum'),
		value: 'SUM',
	},
	{
		label: Liferay.Language.get('average'),
		value: 'AVERAGE',
	},
	{
		label: Liferay.Language.get('min'),
		value: 'MIN',
	},
	{
		label: Liferay.Language.get('max'),
		value: 'MAX',
	},
];

export function AggregationFormBase({
	disabled,
	errors,
	editingField,
	onAggregationFilterChange,
	onRelationshipChange,
	objectDefinitionId,
	objectFieldSettings = [],
	setValues,
}: IAggregationSourcePropertyProps) {
	const [relationshipsQuery, setRelationshipsQuery] = useState<string>('');
	const [relationshipFieldsQuery, setRelationshipFieldsQuery] = useState<
		string
	>('');
	const [
		selectedRelatedObjectRelationship,
		setSelectRelatedObjectRelationship,
	] = useState<TObjectRelationship>();
	const [selectedSummarizeField, setSelectedSummarizeField] = useState<
		string
	>();
	const [
		selectedAggregationFunction,
		setSelectedAggregationFunction,
	] = useState<{label: string; value: string}>();
	const [objectRelationships, setObjectRelationships] = useState<
		TObjectRelationship[]
	>();
	const [objectRelationshipFields, setObjectRelationshipFields] = useState<
		ObjectField[]
	>();

	const filteredObjectRelationships = useMemo(() => {
		return objectRelationships?.filter(({label}) =>
			stringIncludesQuery(
				label[defaultLanguageId] as string,
				relationshipsQuery
			)
		);
	}, [objectRelationships, relationshipsQuery]);

	const filteredObjectRelationshipFields = useMemo(() => {
		return objectRelationshipFields?.filter(({label}) =>
			stringIncludesQuery(
				label[defaultLanguageId] as string,
				relationshipFieldsQuery
			)
		);
	}, [objectRelationshipFields, relationshipFieldsQuery]);

	useEffect(() => {
		const makeFetch = async () => {
			const objectRelationshipsData = await API.getObjectRelationships(
				objectDefinitionId
			);

			setObjectRelationships(
				objectRelationshipsData.filter(
					(objectRelationship) =>
						!(
							objectRelationship.type === 'manyToMany' &&
							objectRelationship.reverse &&
							objectRelationship.objectDefinitionId1 ===
								objectRelationship.objectDefinitionId2
						)
				)
			);
		};

		makeFetch();
	}, [objectDefinitionId]);

	useEffect(() => {
		if (editingField && objectRelationships) {
			const makeFetch = async () => {
				const settings = normalizeFieldSettings(objectFieldSettings);

				const currentRelatedObjectRelationship = objectRelationships.find(
					(relationship) =>
						relationship.name === settings.objectRelationshipName
				) as ObjectRelationship;

				const currentFunction = aggregationFunctions.find(
					(aggregationFunction) =>
						aggregationFunction.value === settings.function
				);

				const relatedFields = await API.getObjectFields(
					currentRelatedObjectRelationship.objectDefinitionId2
				);

				const currentSummarizeField = relatedFields.find(
					(relatedField) =>
						relatedField.name === settings.objectFieldName
				) as ObjectField;

				if (onRelationshipChange) {
					onRelationshipChange(
						currentRelatedObjectRelationship.objectDefinitionId2
					);
				}

				setObjectRelationshipFields(
					relatedFields.filter(
						(objectField) =>
							objectField.businessType === 'Integer' ||
							objectField.businessType === 'LongInteger' ||
							objectField.businessType === 'Decimal' ||
							objectField.businessType === 'PrecisionDecimal'
					)
				);

				setSelectRelatedObjectRelationship(
					currentRelatedObjectRelationship
				);

				setSelectedAggregationFunction(currentFunction);

				if (currentSummarizeField) {
					setSelectedSummarizeField(
						currentSummarizeField.label[defaultLanguageId]
					);
				}
			};

			makeFetch();
		}
	}, [
		editingField,
		objectRelationships,
		objectFieldSettings,
		onRelationshipChange,
	]);

	const handleChangeRelatedObjectRelationship = async (
		objectRelationship: TObjectRelationship
	) => {
		setSelectRelatedObjectRelationship(objectRelationship);
		setSelectedSummarizeField('');

		const relatedFields = await API.getObjectFields(
			objectRelationship.objectDefinitionId2
		);

		const numericFields = relatedFields.filter(
			(objectField) =>
				objectField.businessType === 'Integer' ||
				objectField.businessType === 'LongInteger' ||
				objectField.businessType === 'Decimal' ||
				objectField.businessType === 'PrecisionDecimal'
		);

		setObjectRelationshipFields(numericFields);

		const fieldSettingWithoutSummarizeField = objectFieldSettings.filter(
			(fieldSettings) =>
				fieldSettings.name !== 'objectFieldName' &&
				fieldSettings.name !== 'filters' &&
				fieldSettings.name !== 'objectRelationshipName'
		);

		const newObjectFieldSettings: ObjectFieldSetting[] | undefined = [
			...fieldSettingWithoutSummarizeField,
			{
				name: 'objectRelationshipName',
				value: objectRelationship.name,
			},
			{
				name: 'filters',
				value: [],
			},
		];

		if (onAggregationFilterChange) {
			onAggregationFilterChange([]);
		}

		setValues({
			objectFieldSettings: newObjectFieldSettings,
		});

		if (onRelationshipChange) {
			onRelationshipChange(objectRelationship.objectDefinitionId2);
		}
	};

	const handleAggregationFunctionChange = ({
		label,
		value,
	}: {
		label: string;
		value: string;
	}) => {
		setSelectedAggregationFunction({label, value});

		let newObjectFieldSettings: ObjectFieldSetting[] | undefined;

		if (value === 'COUNT') {
			setSelectedSummarizeField('');

			const fieldSettingWithoutSummarizeField = objectFieldSettings.filter(
				(fieldSettings) => fieldSettings.name !== 'objectFieldName'
			);

			newObjectFieldSettings = [
				...fieldSettingWithoutSummarizeField.filter(
					(fieldSettings) => fieldSettings.name !== 'function'
				),
				{
					name: 'function',
					value,
				},
			];

			setValues({
				objectFieldSettings: newObjectFieldSettings,
			});

			return;
		}

		newObjectFieldSettings = [
			...objectFieldSettings.filter(
				(fieldSettings) => fieldSettings.name !== 'function'
			),
			{
				name: 'function',
				value,
			},
		];

		setValues({
			objectFieldSettings: newObjectFieldSettings,
		});
	};

	const handleSummarizeFieldChange = (objectField: ObjectField) => {
		setSelectedSummarizeField(objectField.label[defaultLanguageId]);

		const newObjectFieldSettings: ObjectFieldSetting[] | undefined = [
			...objectFieldSettings.filter(
				(fieldSettings) => fieldSettings.name !== 'objectFieldName'
			),
			{
				name: 'objectFieldName',
				value: objectField.name as string,
			},
		];

		setValues({
			objectFieldSettings: newObjectFieldSettings,
		});
	};

	return (
		<>
			<AutoComplete
				emptyStateMessage={Liferay.Language.get(
					'no-relationships-were-found'
				)}
				error={errors.objectRelationshipName}
				items={filteredObjectRelationships ?? []}
				label={Liferay.Language.get('relationship')}
				onChangeQuery={setRelationshipsQuery}
				onSelectItem={(item: TObjectRelationship) => {
					handleChangeRelatedObjectRelationship(item);
				}}
				query={relationshipsQuery}
				required
				value={
					selectedRelatedObjectRelationship?.label[defaultLanguageId]
				}
			>
				{({label}) => (
					<div className="d-flex justify-content-between">
						<div>{label[defaultLanguageId]}</div>
					</div>
				)}
			</AutoComplete>

			<SingleSelect
				disabled={disabled}
				error={errors.function}
				label={Liferay.Language.get('function')}
				onChange={handleAggregationFunctionChange}
				options={aggregationFunctions}
				required
				value={selectedAggregationFunction?.label}
			/>

			{selectedAggregationFunction?.value !== 'COUNT' && (
				<AutoComplete
					emptyStateMessage={Liferay.Language.get(
						'no-fields-were-found'
					)}
					error={errors.objectFieldName}
					items={filteredObjectRelationshipFields ?? []}
					label={Liferay.Language.get('field')}
					onChangeQuery={setRelationshipFieldsQuery}
					onSelectItem={(item: ObjectField) => {
						handleSummarizeFieldChange(item);
					}}
					query={relationshipFieldsQuery}
					required
					value={selectedSummarizeField}
				>
					{({label}) => (
						<div className="d-flex justify-content-between">
							<div>{label[defaultLanguageId]}</div>
						</div>
					)}
				</AutoComplete>
			)}
		</>
	);
}
