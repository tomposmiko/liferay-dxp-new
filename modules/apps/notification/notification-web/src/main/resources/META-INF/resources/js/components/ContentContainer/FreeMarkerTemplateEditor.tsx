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
	AutoComplete,
	CodeEditorLocalized,
	SidebarCategory,
	filterArrayByQuery,
	getLocalizableLabel,
} from '@liferay/object-js-components-web';
import {createResourceURL, fetch} from 'frontend-js-web';
import React, {useMemo, useState} from 'react';
interface FreeMarkerTemplateEditorProps {
	baseResourceURL: string;
	objectDefinitions: ObjectDefinition[];
	selectedLocale: Liferay.Language.Locale;
	setSelectedLocale: (value: Liferay.Language.Locale) => void;
	setValues: (values: Partial<NotificationTemplate>) => void;
	values: NotificationTemplate;
}

export function FreeMarkerTemplateEditor({
	baseResourceURL,
	objectDefinitions,
	selectedLocale,
	setSelectedLocale,
	setValues,
	values,
}: FreeMarkerTemplateEditorProps) {
	const [query, setQuery] = useState<string>('');
	const [selectedEntity, setSelectedEntity] = useState<ObjectDefinition>();
	const [entityFields, setEntityFields] = useState<SidebarCategory[]>([]);

	const filteredEntities = useMemo(() => {
		const availableObjectDefinitions = objectDefinitions.filter(
			(objectDefinition) => {
				const {label: statusLabel} = objectDefinition.status;

				if (objectDefinition.system) {
					return (
						objectDefinition.name !== 'Address' &&
						objectDefinition.name !== 'User' &&
						objectDefinition.name !== 'AccountEntry' &&
						objectDefinition.name !== 'CommercePricingClass'
					);
				}

				return statusLabel === 'approved';
			}
		);

		return filterArrayByQuery({
			array: availableObjectDefinitions,
			query,
			str: 'label',
		});
	}, [objectDefinitions, query]);

	const getEntityFields = async (objectDefinitionId: number) => {
		const response = await fetch(
			createResourceURL(baseResourceURL, {
				objectDefinitionId,
				p_p_resource_id:
					'/notification_templates/notification_template_ftl_elements',
			}).toString()
		);

		setEntityFields(await response.json());
	};

	return (
		<CodeEditorLocalized
			CustomSidebarContent={
				<AutoComplete<ObjectDefinition>
					emptyStateMessage={Liferay.Language.get(
						'no-entities-were-found'
					)}
					items={filteredEntities ?? []}
					label={Liferay.Language.get('entity')}
					onChangeQuery={setQuery}
					onSelectItem={(item) => {
						setSelectedEntity(item);
						getEntityFields(item.id);
					}}
					query={query}
					value={getLocalizableLabel(
						selectedEntity?.defaultLanguageId as Locale,
						selectedEntity?.label,
						selectedEntity?.name as string
					)}
				>
					{({label, name}) => (
						<div className="d-flex justify-content-between">
							<div>
								{getLocalizableLabel(
									selectedEntity?.defaultLanguageId as Locale,
									label,
									name
								)}
							</div>
						</div>
					)}
				</AutoComplete>
			}
			mode="freemarker"
			onSelectedLocaleChange={({label}) => {
				setSelectedLocale(label);
			}}
			onTranslationsChange={(translations) => {
				setValues({
					...values,
					body: translations,
				});
			}}
			placeholder={`<#--${Liferay.Language.get(
				'add-elements-from-the-sidebar-to-define-your-template'
			)}-->`}
			selectedLocale={selectedLocale}
			sidebarElements={entityFields}
			translations={values.body}
		/>
	);
}
