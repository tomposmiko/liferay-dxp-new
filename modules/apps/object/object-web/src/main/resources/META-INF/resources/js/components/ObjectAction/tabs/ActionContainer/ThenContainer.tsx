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

import {ClayCheckbox} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayLabel from '@clayui/label';
import {ClayTooltipProvider} from '@clayui/tooltip';
import {
	API,
	Card,
	CustomItem,
	SelectWithOption,
	SingleSelect,
} from '@liferay/object-js-components-web';
import React, {useEffect, useMemo, useState} from 'react';

import {
	ObjectsOptionsList,
	fetchObjectDefinitionFields,
	fetchObjectDefinitions,
} from '../../fetchUtil';

import './ThenContainer.scss';
import {ActionError} from '../..';

interface ThenContainerProps {
	errors: ActionError;
	isValidField: (
		{businessType, name, objectFieldSettings, system}: ObjectField,
		isObjectActionSystem?: boolean
	) => boolean;
	newObjectActionExecutors: CustomItem<string>[];
	objectActionExecutors: CustomItem[];
	objectDefinitionExternalReferenceCode: string;
	objectDefinitionId: number;
	objectDefinitionsRelationshipsURL: string;
	setCurrentObjectDefinitionFields: (values: ObjectField[]) => void;
	setRelationships: (values: ObjectDefinitionsRelationship[]) => void;
	setValues: (values: Partial<ObjectAction>) => void;
	systemObject: boolean;
	updateParameters: (value: string) => Promise<void>;
	values: Partial<ObjectAction>;
}

export function ThenContainer({
	errors,
	isValidField,
	newObjectActionExecutors,
	objectActionExecutors,
	objectDefinitionExternalReferenceCode,
	objectDefinitionId,
	objectDefinitionsRelationshipsURL,
	setCurrentObjectDefinitionFields,
	setRelationships,
	setValues,
	systemObject,
	updateParameters,
	values,
}: ThenContainerProps) {
	const [notificationTemplates, setNotificationTemplates] = useState<
		CustomItem<string>[]
	>([]);

	const notificationTemplateLabel = useMemo(() => {
		return notificationTemplates.find(
			({value}) =>
				value ===
				values.parameters?.notificationTemplateExternalReferenceCode
		)?.label;
	}, [notificationTemplates, values.parameters]);

	const [objectsOptions, setObjectOptions] = useState<ObjectsOptionsList>([]);

	const [selectedObjectDefinition, setSelectedObjectDefinition] = useState(
		''
	);

	const actionExecutors = useMemo(() => {
		const executors = new Map<string, string>();

		newObjectActionExecutors.forEach(({label, value}) => {
			value && executors.set(value, label);
		});

		return executors;
	}, [newObjectActionExecutors]);

	useEffect(() => {
		if (values.objectActionExecutorKey === 'notification') {
			const makeFetch = async () => {
				const NotificationTemplatesResponse = await API.getNotificationTemplates();

				let notificationArray: NotificationTemplate[] = NotificationTemplatesResponse;

				if (systemObject) {
					notificationArray = NotificationTemplatesResponse.filter(
						(notificationTemplate) =>
							notificationTemplate.type !== 'userNotification'
					);
				}

				setNotificationTemplates(
					notificationArray.map(
						({externalReferenceCode, name, type}) => ({
							label: name,
							type,
							value: externalReferenceCode,
						})
					)
				);
			};

			makeFetch();
		}

		if (values.objectActionExecutorKey === 'add-object-entry') {
			fetchObjectDefinitions(
				objectDefinitionsRelationshipsURL,
				values,
				setRelationships,
				setObjectOptions
			);

			fetchObjectDefinitionFields(
				objectDefinitionId,
				objectDefinitionExternalReferenceCode,
				systemObject,
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
		<Card title={Liferay.Language.get('then[object]')} viewMode="inline">
			<div className="lfr-object__action-builder-then">
				<SingleSelect
					error={errors.objectActionExecutorKey}
					onChange={({value}) => {
						if (value === 'add-object-entry') {
							fetchObjectDefinitions(
								objectDefinitionsRelationshipsURL,
								values,
								setRelationships,
								setObjectOptions
							);
						}

						if (values.objectActionExecutorKey !== value) {
							return setValues({
								objectActionExecutorKey: value,
								parameters: {},
							});
						}
					}}
					options={
						Liferay.FeatureFlags['LPS-153714']
							? newObjectActionExecutors
							: objectActionExecutors
					}
					placeholder={Liferay.Language.get('choose-an-action')}
					value={actionExecutors.get(
						values.objectActionExecutorKey ?? ''
					)}
				/>

				{values.objectActionExecutorKey === 'add-object-entry' && (
					<>
						on
						<SelectWithOption
							aria-label={Liferay.Language.get(
								'choose-an-object'
							)}
							error={errors.objectDefinitionExternalReferenceCode}
							items={objectsOptions}
							onSelectChange={(label, value) => {
								updateParameters(value);
								setSelectedObjectDefinition(label);
							}}
							placeholder={Liferay.Language.get(
								'choose-an-object'
							)}
							value={selectedObjectDefinition}
						/>
						{values.parameters?.relatedObjectEntries !==
							undefined && (
							<>
								<ClayCheckbox
									checked={
										values.parameters
											.relatedObjectEntries === true
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
					<SingleSelect<CustomItem<string>>
						className="lfr-object__action-builder-notification-then"
						error={errors.objectActionExecutorKey}
						onChange={({value}) => {
							setValues({
								parameters: {
									...values.parameters,
									notificationTemplateExternalReferenceCode: value,
								},
							});
						}}
						options={notificationTemplates}
						required
						value={notificationTemplateLabel}
					>
						{notificationTemplates.map(
							(option) =>
								option.type && (
									<ClayLabel
										displayType={
											option.type === 'email'
												? 'success'
												: 'info'
										}
									>
										{option.type === 'email'
											? Liferay.Language.get('email')
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
	);
}
