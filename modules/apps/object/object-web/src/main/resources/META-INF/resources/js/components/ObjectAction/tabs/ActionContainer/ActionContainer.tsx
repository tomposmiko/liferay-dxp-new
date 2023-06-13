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
	Card,
	CodeEditor,
	CustomItem,
	Input,
	SidebarCategory,
} from '@liferay/object-js-components-web';
import React, {useCallback, useEffect, useState} from 'react';

import {ActionError} from '../..';
import PredefinedValuesTable from '../../PredefinedValuesTable';
import {fetchObjectDefinitionFields} from '../../fetchUtil';
import {WarningStates} from '../ActionBuilder';
import {ThenContainer} from './ThenContainer';
interface ActionContainerProps {
	currentObjectDefinitionFields: ObjectField[];
	errors: ActionError;
	newObjectActionExecutors: CustomItem<string>[];
	objectActionCodeEditorElements: SidebarCategory[];
	objectActionExecutors: CustomItem[];
	objectDefinitionExternalReferenceCode: string;
	objectDefinitionId: number;
	objectDefinitionsRelationshipsURL: string;
	objectFieldsMap: Map<string, ObjectField>;
	setCurrentObjectDefinitionFields: (values: ObjectField[]) => void;
	setValues: (values: Partial<ObjectAction>) => void;
	setWarningAlerts: (value: React.SetStateAction<WarningStates>) => void;
	systemObject: boolean;
	validateExpressionURL: string;
	values: Partial<ObjectAction>;
}

export function ActionContainer({
	currentObjectDefinitionFields,
	errors,
	newObjectActionExecutors,
	objectActionCodeEditorElements,
	objectActionExecutors,
	objectDefinitionExternalReferenceCode,
	objectDefinitionId,
	objectDefinitionsRelationshipsURL,
	objectFieldsMap,
	setCurrentObjectDefinitionFields,
	setValues,
	setWarningAlerts,
	systemObject,
	validateExpressionURL,
	values,
}: ActionContainerProps) {
	const [relationships, setRelationships] = useState<
		ObjectDefinitionsRelationship[]
	>([]);

	const isValidField = ({
		businessType,
		objectFieldSettings,
		system,
	}: ObjectField) => {
		const userRelationship = !!objectFieldSettings?.find(
			({name, value}) =>
				name === 'objectDefinition1ShortName' && value === 'User'
		);

		if (businessType === 'Relationship' && userRelationship) {
			return true;
		}

		return (
			businessType !== 'Aggregation' &&
			businessType !== 'Formula' &&
			businessType !== 'Relationship' &&
			!system
		);
	};

	const updateParameters = useCallback(
		async (value: string) => {
			const [externalReferenceCode, definitionIdValue] = value.split(',');

			const definitionId = Number(definitionIdValue);

			const object = relationships.find(
				(relationship) =>
					relationship.externalReferenceCode === externalReferenceCode
			);

			const parameters: ObjectActionParameters = {
				objectDefinitionExternalReferenceCode: externalReferenceCode,
				objectDefinitionId: definitionId,
				predefinedValues: [],
			};

			if (object?.related) {
				parameters.relatedObjectEntries = false;
			}
			const items = await API.getObjectFieldsByExternalReferenceCode(
				externalReferenceCode
			);

			const validFields: ObjectField[] = [];

			items.forEach((field) => {
				if (isValidField(field)) {
					validFields.push(field);

					if (
						field.required &&
						values.objectActionExecutorKey === 'add-object-entry'
					) {
						(parameters.predefinedValues as PredefinedValue[]).push(
							{
								inputAsValue: false,
								label: field.label,
								name: field.name,
								value: '',
							}
						);
					}
				}
			});

			setCurrentObjectDefinitionFields(validFields);

			const normalizedParameters = {...values.parameters};

			delete normalizedParameters.relatedObjectEntries;

			setValues({
				parameters: {
					...normalizedParameters,
					...(values.objectActionExecutorKey ===
						'add-object-entry' && {
						...parameters,
					}),
				},
			});

			setWarningAlerts((previousWarnings) => ({
				...previousWarnings,
				mandatoryRelationships: items.some(
					(field) =>
						field.businessType === 'Relationship' &&
						field.required === true
				),
			}));
		},
		// eslint-disable-next-line react-hooks/exhaustive-deps
		[relationships, values.objectActionExecutorKey, values.parameters]
	);

	useEffect(() => {
		if (values.objectActionExecutorKey === 'update-object-entry') {
			updateParameters(objectDefinitionExternalReferenceCode);
			fetchObjectDefinitionFields(
				objectDefinitionId,
				objectDefinitionExternalReferenceCode,
				values,
				isValidField,
				setCurrentObjectDefinitionFields,
				setValues
			);
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [
		objectDefinitionId,
		objectDefinitionExternalReferenceCode,
		objectDefinitionsRelationshipsURL,
		systemObject,
		values.objectActionExecutorKey,
	]);

	return (
		<Card title={Liferay.Language.get('action')}>
			<ThenContainer
				errors={errors}
				isValidField={isValidField}
				newObjectActionExecutors={newObjectActionExecutors}
				objectActionExecutors={objectActionExecutors}
				objectDefinitionExternalReferenceCode={
					objectDefinitionExternalReferenceCode
				}
				objectDefinitionId={objectDefinitionId}
				objectDefinitionsRelationshipsURL={
					objectDefinitionsRelationshipsURL
				}
				setCurrentObjectDefinitionFields={
					setCurrentObjectDefinitionFields
				}
				setRelationships={setRelationships}
				setValues={setValues}
				systemObject={systemObject}
				updateParameters={updateParameters}
				values={values}
			/>

			{(values.objectActionExecutorKey === 'add-object-entry' ||
				values.objectActionExecutorKey === 'update-object-entry') &&
				values.parameters?.objectDefinitionExternalReferenceCode && (
					<PredefinedValuesTable
						currentObjectDefinitionFields={
							currentObjectDefinitionFields
						}
						disableRequiredChecked={
							values.objectActionExecutorKey ===
							'update-object-entry'
						}
						errors={
							errors.predefinedValues as {
								[key: string]: string;
							}
						}
						objectFieldsMap={objectFieldsMap}
						setValues={setValues}
						title={
							values.objectActionExecutorKey ===
							'update-object-entry'
								? Liferay.Language.get('values')
								: ''
						}
						validateExpressionURL={validateExpressionURL}
						values={values}
					/>
				)}

			{values.objectActionExecutorKey === 'webhook' && (
				<>
					<Input
						error={errors.url}
						label={Liferay.Language.get('url')}
						name="url"
						onChange={({target: {value}}) => {
							setValues({
								parameters: {
									...values.parameters,
									url: value,
								},
							});
						}}
						required
						value={values.parameters?.url}
					/>

					<Input
						label={Liferay.Language.get('secret')}
						name="secret"
						onChange={({target: {value}}) => {
							setValues({
								parameters: {
									...values.parameters,
									secret: value,
								},
							});
						}}
						value={values.parameters?.secret}
					/>
				</>
			)}

			{values.objectActionExecutorKey === 'groovy' && (
				<CodeEditor
					error={errors.script}
					mode="groovy"
					onChange={(script, lineCount) =>
						setValues({
							parameters: {
								...values.parameters,
								lineCount,
								script,
							},
						})
					}
					sidebarElements={objectActionCodeEditorElements.filter(
						(element) => element.label === 'Fields'
					)}
					value={values.parameters?.script ?? ''}
				/>
			)}
		</Card>
	);
}
