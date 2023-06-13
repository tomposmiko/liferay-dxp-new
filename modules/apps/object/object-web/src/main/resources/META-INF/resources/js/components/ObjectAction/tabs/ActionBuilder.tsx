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

import ClayAlert from '@clayui/alert';
import ClayForm, {ClayCheckbox, ClaySelect, ClayToggle} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayLabel from '@clayui/label';
import {ClayTooltipProvider} from '@clayui/tooltip';
import {
	API,
	Card,
	CodeEditor,
	CustomItem,
	ExpressionBuilder,
	Input,
	SelectWithOption,
	SidebarCategory,
	SingleSelect,
	invalidateRequired,
} from '@liferay/object-js-components-web';
import React, {useEffect, useMemo, useState} from 'react';

import PredefinedValuesTable from '../PredefinedValuesTable';

import './ActionBuilder.scss';
import {ActionError} from '../index';

type ObjectsOptionsList = Array<
	(
		| React.ComponentProps<typeof ClaySelect.Option>
		| React.ComponentProps<typeof ClaySelect.OptGroup>
	) & {
		options?: Array<React.ComponentProps<typeof ClaySelect.Option>>;
		type?: 'group';
	}
>;

export default function ActionBuilder({
	errors,
	objectActionCodeEditorElements,
	objectActionExecutors,
	objectActionTriggers,
	objectDefinitionsRelationshipsURL,
	setValues,
	validateExpressionURL,
	values,
}: IProps) {
	const [notificationTemplates, setNotificationTemplates] = useState<
		CustomItem<number>[]
	>([]);

	const [objectsOptions, setObjectOptions] = useState<ObjectsOptionsList>([]);

	const notificationTemplateLabel = useMemo(() => {
		return notificationTemplates.find(
			({value}) => value === values.parameters?.notificationTemplateId
		)?.label;
	}, [notificationTemplates, values.parameters]);

	const [relationships, setRelationships] = useState<
		ObjectDefinitionsRelationship[]
	>([]);

	const [
		currentObjectDefinitionFields,
		setCurrentObjectDefinitionFields,
	] = useState<ObjectField[]>([]);

	const [infoAlert, setInfoAlert] = useState(true);

	const [warningAlerts, setWarningAlerts] = useState<WarningStates>({
		mandatoryRelationships: false,
		requiredFields: false,
	});

	const fetchObjectDefinitions = async () => {
		const relationships = await API.fetchJSON<
			ObjectDefinitionsRelationship[]
		>(objectDefinitionsRelationshipsURL);

		const relatedObjects: SelectItem[] = [];
		const unrelatedObjects: SelectItem[] = [];

		relationships?.forEach((object) => {
			const {id, label} = object;

			const target = object.related ? relatedObjects : unrelatedObjects;

			target.push({label, value: id});
		});

		const objectsOptionsList = [];

		if (!values.parameters?.objectDefinitionId) {
			objectsOptionsList.push({
				disabled: true,
				label: Liferay.Language.get('choose-an-object'),
				selected: true,
				value: '',
			});
		}
		const fillSelect = (label: string, options: SelectItem[]) => {
			if (options.length) {
				objectsOptionsList.push({label, options, type: 'group'});
			}
		};

		fillSelect(Liferay.Language.get('related-objects'), relatedObjects);

		fillSelect(Liferay.Language.get('unrelated-objects'), unrelatedObjects);

		setObjectOptions(objectsOptionsList);
		setRelationships(relationships);
	};

	const actionExecutors = useMemo(() => {
		const executors = new Map<string, string>();

		objectActionExecutors.forEach(({label, value}) => {
			value && executors.set(value, label);
		});

		return executors;
	}, [objectActionExecutors]);

	const actionTriggers = useMemo(() => {
		const triggers = new Map<string, string>();

		objectActionTriggers.forEach(({label, value}) => {
			value && triggers.set(value, label);
		});

		return triggers;
	}, [objectActionTriggers]);

	const objectFieldsMap = useMemo(() => {
		const fields = new Map<string, ObjectField>();

		currentObjectDefinitionFields.forEach((field) => {
			fields.set(field.name, field);
		});

		return fields;
	}, [currentObjectDefinitionFields]);

	useEffect(() => {
		if (values.objectActionExecutorKey === 'notification') {
			API.getNotificationTemplates().then((items) => {
				const notificationsArray = items.map(({id, name, type}) => ({
					label: name,
					type,
					value: id,
				}));

				setNotificationTemplates(notificationsArray);
			});
		}
	}, [values]);

	const handleSave = (conditionExpression?: string) => {
		setValues({conditionExpression});
	};

	const isValidField = ({businessType, system}: ObjectField) =>
		businessType !== 'Aggregation' &&
		businessType !== 'Formula' &&
		businessType !== 'Relationship' &&
		!system;

	const fetchObjectDefinitionFields = async () => {
		let validFields: ObjectField[] = [];

		if (values.parameters?.objectDefinitionId) {
			const items = await API.getObjectFields(
				values.parameters.objectDefinitionId
			);

			validFields = items.filter(isValidField);
		}

		setCurrentObjectDefinitionFields(validFields);

		const {
			predefinedValues = [],
		} = values.parameters as ObjectActionParameters;

		const predefinedValuesMap = new Map<string, PredefinedValue>();

		predefinedValues.forEach((field) => {
			predefinedValuesMap.set(field.name, field);
		});

		const newPredefinedValues: PredefinedValue[] = [];

		validFields.forEach(({name, required}) => {
			if (predefinedValuesMap.has(name)) {
				const field = predefinedValuesMap.get(name);

				newPredefinedValues.push(field as PredefinedValue);
			}
			else if (required) {
				newPredefinedValues.push({
					inputAsValue: false,
					name,
					value: '',
				});
			}
		});
		setValues({
			parameters: {
				...values.parameters,
				predefinedValues: newPredefinedValues,
			},
		});
	};

	const handleSelectObject = async ({
		target: {value},
	}: React.ChangeEvent<HTMLSelectElement>) => {
		const objectDefinitionId = parseInt(value, 10);

		const object = relationships.find(({id}) => id === objectDefinitionId);

		const parameters: ObjectActionParameters = {
			objectDefinitionId,
			predefinedValues: [],
		};

		if (object?.related) {
			parameters.relatedObjectEntries = false;
		}

		const items = await API.getObjectFields(objectDefinitionId);

		const validFields: ObjectField[] = [];

		items.forEach((field) => {
			if (isValidField(field)) {
				validFields.push(field);

				if (field.required) {
					(parameters.predefinedValues as PredefinedValue[]).push({
						inputAsValue: false,
						name: field.name,
						value: '',
					});
				}
			}
		});

		setCurrentObjectDefinitionFields(validFields);

		const normalizedParameters = {...values.parameters};

		delete normalizedParameters.relatedObjectEntries;

		setValues({
			parameters: {
				...normalizedParameters,
				...parameters,
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
	};

	useEffect(() => {
		if (values.objectActionExecutorKey === 'add-object-entry') {
			fetchObjectDefinitions();
			fetchObjectDefinitionFields();
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	useEffect(() => {
		const predefinedValues = values.parameters?.predefinedValues;

		const requiredFields = predefinedValues
			? predefinedValues.filter(
					({name}) => objectFieldsMap.get(name)?.required
			  )
			: [];

		const hasEmptyValues = requiredFields?.some((item) =>
			invalidateRequired(item.value)
		);

		setWarningAlerts((previousWarnings) => ({
			...previousWarnings,
			requiredFields: hasEmptyValues,
		}));
	}, [
		values.parameters?.predefinedValues,
		objectFieldsMap,
		setWarningAlerts,
	]);

	const closeWarningAlert = (warning: string) => {
		setWarningAlerts((previousWarnings) => ({
			...previousWarnings,
			[warning]: false,
		}));
	};

	return (
		<>
			{infoAlert && (
				<ClayAlert
					className="lfr-objects__side-panel-content-container"
					displayType="info"
					onClose={() => setInfoAlert(false)}
					title={`${Liferay.Language.get('info')}:`}
				>
					{Liferay.Language.get(
						'create-conditions-and-predefined-values-using-expressions'
					) + ' '}

					<a
						className="alert-link"
						href="https://learn.liferay.com/dxp/latest/en/building-applications/objects/creating-and-managing-objects/expression-builder-validations-reference.html"
						target="_blank"
					>
						{Liferay.Language.get('click-here-for-documentation')}
					</a>
				</ClayAlert>
			)}

			<Card title={Liferay.Language.get('trigger')}>
				<Card
					title={Liferay.Language.get('when[object]')}
					viewMode="inline"
				>
					<SingleSelect
						error={errors.objectActionTriggerKey}
						onChange={({value}) =>
							setValues({
								conditionExpression: undefined,
								objectActionTriggerKey: value,
							})
						}
						options={objectActionTriggers}
						placeholder={Liferay.Language.get('choose-a-trigger')}
						value={actionTriggers.get(
							values.objectActionTriggerKey ?? ''
						)}
					/>
				</Card>
			</Card>

			<Card title={Liferay.Language.get('condition')}>
				<ClayForm.Group>
					<ClayToggle
						label={Liferay.Language.get('enable-condition')}
						name="condition"
						onToggle={(enable) =>
							setValues({
								conditionExpression: enable ? '' : undefined,
							})
						}
						toggled={!(values.conditionExpression === undefined)}
					/>
				</ClayForm.Group>

				{values.conditionExpression !== undefined && (
					<ExpressionBuilder
						error={errors.conditionExpression}
						feedbackMessage={Liferay.Language.get(
							'use-expressions-to-create-a-condition'
						)}
						label={Liferay.Language.get('expression-builder')}
						name="conditionExpression"
						onChange={({target: {value}}) =>
							setValues({conditionExpression: value})
						}
						onOpenModal={() => {
							const parentWindow = Liferay.Util.getOpener();

							parentWindow.Liferay.fire(
								'openExpressionBuilderModal',
								{
									onSave: handleSave,
									required: true,
									source: values.conditionExpression,
									validateExpressionURL,
								}
							);
						}}
						placeholder={Liferay.Language.get(
							'create-an-expression'
						)}
						value={values.conditionExpression as string}
					/>
				)}
			</Card>

			{warningAlerts.requiredFields && (
				<ClayAlert
					className="lfr-objects__side-panel-content-container"
					displayType="warning"
					onClose={() => closeWarningAlert('requiredFields')}
					title={`${Liferay.Language.get('warning')}:`}
				>
					{Liferay.Language.get(
						'required-fields-must-have-predefined-values'
					)}
				</ClayAlert>
			)}

			{warningAlerts.mandatoryRelationships && (
				<ClayAlert
					className="lfr-objects__side-panel-content-container"
					displayType="warning"
					onClose={() => closeWarningAlert('mandatoryRelationships')}
					title={`${Liferay.Language.get('warning')}:`}
				>
					{Liferay.Language.get(
						'the-selected-object-definition-has-mandatory-relationship-fields'
					)}
				</ClayAlert>
			)}

			<Card title={Liferay.Language.get('action')}>
				<Card
					title={Liferay.Language.get('then[object]')}
					viewMode="inline"
				>
					<div className="lfr-object__action-builder-then">
						<SingleSelect
							error={errors.objectActionExecutorKey}
							onChange={({value}) => {
								if (value === 'add-object-entry') {
									fetchObjectDefinitions();
								}
								setValues({
									objectActionExecutorKey: value,
									parameters: {},
								});
							}}
							options={objectActionExecutors}
							placeholder={Liferay.Language.get(
								'choose-an-action'
							)}
							value={actionExecutors.get(
								values.objectActionExecutorKey ?? ''
							)}
						/>

						{values.objectActionExecutorKey ===
							'add-object-entry' && (
							<>
								on
								<SelectWithOption
									aria-label={Liferay.Language.get(
										'choose-an-object'
									)}
									error={errors.objectDefinitionId}
									onChange={handleSelectObject}
									options={objectsOptions}
									value={
										values.parameters?.objectDefinitionId
									}
								/>
								{values.parameters?.relatedObjectEntries !==
									undefined && (
									<>
										<ClayCheckbox
											checked={
												values.parameters
													.relatedObjectEntries ===
												true
											}
											disabled={false}
											label={Liferay.Language.get(
												'also-relate-entries'
											)}
											onChange={({target: {checked}}) => {
												setValues({
													parameters: {
														...values.parameters,
														relatedObjectEntries: checked,
													},
												});
											}}
										/>
										<ClayTooltipProvider>
											<div
												data-tooltip-align="top"
												title={Liferay.Language.get(
													'automatically-relate-object-entries-involved-in-the-action'
												)}
											>
												<ClayIcon
													className=".lfr-object__action-builder-tooltip-icon"
													symbol="question-circle-full"
												/>
											</div>
										</ClayTooltipProvider>
									</>
								)}
							</>
						)}

						{values.objectActionExecutorKey === 'notification' && (
							<SingleSelect<CustomItem<number>>
								className="lfr-object__action-builder-notification-then"
								error={errors.objectActionExecutorKey}
								onChange={({value}) => {
									setValues({
										parameters: {
											...values.parameters,
											notificationTemplateId: value,
										},
									});
								}}
								options={notificationTemplates}
								required
								value={notificationTemplateLabel}
							>
								{notificationTemplates.map(
									(option) =>
										Liferay.FeatureFlags['LPS-162133'] &&
										option.type && (
											<ClayLabel
												displayType={
													option.type === 'email'
														? 'success'
														: 'info'
												}
											>
												{option.type === 'email'
													? Liferay.Language.get(
															'email'
													  )
													: Liferay.Language.get(
															'user-notification'
													  )}
											</ClayLabel>
										)
								)}
							</SingleSelect>
						)}
					</div>
				</Card>

				{values.objectActionExecutorKey === 'add-object-entry' &&
					values.parameters?.objectDefinitionId && (
						<PredefinedValuesTable
							currentObjectDefinitionFields={
								currentObjectDefinitionFields
							}
							errors={
								errors.predefinedValues as {
									[key: string]: string;
								}
							}
							objectFieldsMap={objectFieldsMap}
							setValues={setValues}
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
		</>
	);
}

interface IProps {
	errors: ActionError;
	objectActionCodeEditorElements: SidebarCategory[];
	objectActionExecutors: CustomItem[];
	objectActionTriggers: CustomItem[];
	objectDefinitionsRelationshipsURL: string;
	setValues: (values: Partial<ObjectAction>) => void;
	validateExpressionURL: string;
	values: Partial<ObjectAction>;
}

interface SelectItem {
	label: string;
	value: number;
}

interface WarningStates {
	mandatoryRelationships: boolean;
	requiredFields: boolean;
}
