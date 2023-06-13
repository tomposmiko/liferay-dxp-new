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

import 'codemirror/mode/groovy/groovy';
import ClayTabs from '@clayui/tabs';
import {
	API,
	CustomItem,
	FormError,
	SidePanelForm,
	SidebarCategory,
	openToast,
	saveAndReload,
} from '@liferay/object-js-components-web';
import React, {useState} from 'react';

import ActionBuilder from './tabs/ActionBuilder';
import BasicInfo from './tabs/BasicInfo';
import {useObjectActionForm} from './useObjectActionForm';

const TABS = [
	Liferay.Language.get('basic-info'),
	Liferay.Language.get('action-builder'),
];

interface ActionProps {
	isApproved?: boolean;
	objectAction: Partial<ObjectAction>;
	objectActionCodeEditorElements: SidebarCategory[];
	objectActionExecutors: CustomItem[];
	objectActionTriggers: CustomItem[];
	objectDefinitionExternalReferenceCode: string;
	objectDefinitionId: number;
	objectDefinitionsRelationshipsURL: string;
	readOnly?: boolean;
	requestParams: {
		method: 'POST' | 'PUT';
		url: string;
	};
	successMessage: string;
	systemObject: boolean;
	title: string;
	validateExpressionURL: string;
}

interface ErrorMessage {
	fieldName: keyof ObjectAction;
	message?: string;
	messages?: ErrorMessage[];
}

interface Error {
	[key: string]: string | Error;
}

export type ActionError = FormError<ObjectAction & ObjectActionParameters> & {
	predefinedValues?: {[key: string]: string};
};

export default function Action({
	isApproved,
	objectAction: initialValues,
	objectActionCodeEditorElements,
	objectActionExecutors,
	objectActionTriggers,
	objectDefinitionExternalReferenceCode,
	objectDefinitionId,
	objectDefinitionsRelationshipsURL,
	readOnly,
	requestParams: {method, url},
	successMessage,
	systemObject,
	validateExpressionURL,
}: ActionProps) {
	const [backEndErrors, setBackEndErrors] = useState<Error>({});

	const onSubmit = async (objectAction: ObjectAction) => {
		if (objectAction.parameters) {
			delete objectAction?.parameters['lineCount'];
		}

		delete objectAction.objectDefinitionId;

		try {
			await API.save(url, objectAction, method);
			saveAndReload();
			openToast({message: successMessage});
		}
		catch (error) {
			const {detail} = error as {detail?: string};
			const details = JSON.parse(detail as string);
			const newErrors: Error = {};

			const parseError = (details: ErrorMessage[], errors: Error) => {
				details.forEach(({fieldName, message, messages}) => {
					if (message) {
						errors[fieldName] = message;
					}
					else {
						errors[fieldName] = {};
						parseError(
							messages as ErrorMessage[],
							errors[fieldName] as Error
						);
					}
				});
			};

			parseError(details, newErrors);

			setBackEndErrors(newErrors);

			const errorMessages = new Set<string>();

			const getErrorMessage = (errors: Error) => {
				Object.values(errors).forEach((value) => {
					if (typeof value === 'string') {
						if (!errorMessages.has(value)) {
							errorMessages.add(value);
						}
					}
					else {
						getErrorMessage(value);
					}
				});
			};

			if (newErrors) {
				getErrorMessage(newErrors);
				errorMessages.forEach((message) => {
					openToast({
						message,
						type: 'danger',
					});
				});
			}
		}
	};

	const {
		errors,
		handleChange,
		handleSubmit,
		setValues,
		values,
	} = useObjectActionForm({initialValues, onSubmit});

	const [activeIndex, setActiveIndex] = useState(0);

	return (
		<SidePanelForm
			onSubmit={handleSubmit}
			title={Liferay.Language.get('new-action')}
		>
			<ClayTabs className="side-panel-iframe__tabs">
				{TABS.map((label, index) => (
					<ClayTabs.Item
						active={activeIndex === index}
						key={index}
						onClick={() => setActiveIndex(index)}
					>
						{label}
					</ClayTabs.Item>
				))}
			</ClayTabs>

			<ClayTabs.Content activeIndex={activeIndex} fade>
				<ClayTabs.TabPane>
					<BasicInfo
						errors={
							Object.keys(errors).length ? errors : backEndErrors
						}
						handleChange={handleChange}
						isApproved={isApproved!}
						readOnly={readOnly}
						setValues={setValues}
						values={values}
					/>
				</ClayTabs.TabPane>

				<ClayTabs.TabPane>
					<ActionBuilder
						errors={
							Object.keys(errors).length ? errors : backEndErrors
						}
						isApproved={isApproved!}
						objectActionCodeEditorElements={
							objectActionCodeEditorElements
						}
						objectActionExecutors={objectActionExecutors}
						objectActionTriggers={objectActionTriggers}
						objectDefinitionExternalReferenceCode={
							objectDefinitionExternalReferenceCode
						}
						objectDefinitionId={
							objectDefinitionId ??
							initialValues.objectDefinitionId
						}
						objectDefinitionsRelationshipsURL={
							objectDefinitionsRelationshipsURL
						}
						setValues={setValues}
						systemObject={systemObject}
						validateExpressionURL={validateExpressionURL}
						values={values}
					/>
				</ClayTabs.TabPane>
			</ClayTabs.Content>
		</SidePanelForm>
	);
}
