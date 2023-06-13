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

import ClayTabs from '@clayui/tabs';
import {
	API,
	SidePanelContent,
	invalidateRequired,
	openToast,
	saveAndReload,
} from '@liferay/object-js-components-web';
import React, {useEffect, useState} from 'react';

import {TabsVisitor} from '../../utils/visitor';
import InfoScreen from './InfoScreen/InfoScreen';
import LayoutScreen from './LayoutScreen/LayoutScreen';
import {
	LayoutContextProvider,
	TYPES,
	useLayoutContext,
} from './objectLayoutContext';
import {
	TObjectField,
	TObjectLayout,
	TObjectLayoutTab,
	TObjectRelationship,
} from './types';

const defaultLanguageId = Liferay.ThemeDisplay.getDefaultLanguageId();

const TABS = [
	{
		Component: InfoScreen,
		label: Liferay.Language.get('info'),
	},
	{
		Component: LayoutScreen,
		label: Liferay.Language.get('layout'),
	},
];

type TNormalizeObjectFields = ({
	objectFields,
	objectLayout,
}: {
	objectFields: TObjectField[];
	objectLayout: TObjectLayout;
}) => TObjectField[];

const normalizeObjectFields: TNormalizeObjectFields = ({
	objectFields,
	objectLayout,
}) => {
	const visitor = new TabsVisitor(objectLayout);

	const objectFieldIds = objectFields.map(({id}) => id);

	const normalizedObjectFields = [...objectFields];

	visitor.mapFields((field) => {
		const objectFieldIndex = objectFieldIds.indexOf(field.objectFieldId);
		normalizedObjectFields[objectFieldIndex].inLayout = true;
	});

	return normalizedObjectFields;
};

type TNormalizeObjectRelationships = ({
	objectLayoutTabs,
	objectRelationships,
}: {
	objectLayoutTabs: TObjectLayoutTab[];
	objectRelationships: TObjectRelationship[];
}) => TObjectRelationship[];

const normalizeObjectRelationships: TNormalizeObjectRelationships = ({
	objectLayoutTabs,
	objectRelationships,
}) => {
	const objectRelationshipIds = objectRelationships.map(({id}) => id);

	const normalizedObjectRelationships = [...objectRelationships];

	objectLayoutTabs.forEach(({objectRelationshipId}) => {
		if (objectRelationshipId) {
			const objectRelationshipIndex = objectRelationshipIds.indexOf(
				objectRelationshipId
			);

			normalizedObjectRelationships[
				objectRelationshipIndex
			].inLayout = true;
		}
	});

	return normalizedObjectRelationships;
};

const Layout: React.FC<React.HTMLAttributes<HTMLElement>> = () => {
	const [
		{isViewOnly, objectFields, objectLayout, objectLayoutId},
		dispatch,
	] = useLayoutContext();
	const [activeIndex, setActiveIndex] = useState<number>(0);
	const [loading, setLoading] = useState<boolean>(true);

	useEffect(() => {
		const makeFetch = async () => {
			const {
				defaultObjectLayout,
				name,
				objectDefinitionId,
				objectLayoutTabs,
			} = await API.fetchJSON<TObjectLayout>(
				`/o/object-admin/v1.0/object-layouts/${objectLayoutId}`
			);

			const objectDefinition = await API.getObjectDefinition(
				objectDefinitionId
			);

			const objectFields = await API.getObjectFields(objectDefinitionId);

			const objectRelationships = await API.getObjectRelationships(
				objectDefinitionId
			);

			const objectLayout = {
				defaultObjectLayout,
				name,
				objectDefinitionId,
				objectLayoutTabs,
			};

			dispatch({
				payload: {
					objectLayout,
				},
				type: TYPES.ADD_OBJECT_LAYOUT,
			});

			const filteredObjectFields = objectFields.filter(
				({system}) => !system
			);

			dispatch({
				payload: {objectDefinition},
				type: TYPES.ADD_OBJECT_DEFINITION,
			});

			dispatch({
				payload: {
					objectFields: normalizeObjectFields({
						objectFields: filteredObjectFields,
						objectLayout,
					}),
				},
				type: TYPES.ADD_OBJECT_FIELDS,
			});

			dispatch({
				payload: {
					objectRelationships: normalizeObjectRelationships({
						objectLayoutTabs,
						objectRelationships,
					}),
				},
				type: TYPES.ADD_OBJECT_RELATIONSHIPS,
			});

			setLoading(false);
		};

		makeFetch();
	}, [objectLayoutId, dispatch]);

	const saveObjectLayout = async () => {
		const hasFieldsInLayout = objectFields.some(
			(objectField) => objectField.inLayout
		);

		if (invalidateRequired(objectLayout.name[defaultLanguageId])) {
			openToast({
				message: Liferay.Language.get('a-name-is-required'),
				type: 'danger',
			});

			return;
		}

		if (!hasFieldsInLayout) {
			openToast({
				message: Liferay.Language.get('please-add-at-least-one-field'),
				type: 'danger',
			});

			return;
		}

		if (objectLayout.objectLayoutTabs[0].objectRelationshipId > 0) {
			openToast({
				message: Liferay.Language.get(
					'the-layouts-first-tab-must-be-a-field-tab'
				),
				type: 'danger',
			});

			return;
		}

		try {
			await API.save(
				`/o/object-admin/v1.0/object-layouts/${objectLayoutId}`,
				objectLayout
			);
			saveAndReload();
			openToast({
				message: Liferay.Language.get(
					'the-object-layout-was-updated-successfully'
				),
			});
		}
		catch ({message}) {
			openToast({message: message as string, type: 'danger'});
		}
	};

	return (
		<SidePanelContent
			onSave={saveObjectLayout}
			readOnly={isViewOnly || loading}
			title={Liferay.Language.get('layout')}
		>
			<ClayTabs className="side-panel-iframe__tabs">
				{TABS.map(({label}, index) => (
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
				{TABS.map(({Component}, index) => (
					<ClayTabs.TabPane key={index}>
						{!loading && <Component />}
					</ClayTabs.TabPane>
				))}
			</ClayTabs.Content>
		</SidePanelContent>
	);
};

interface ILayoutWrapperProps extends React.HTMLAttributes<HTMLElement> {
	isViewOnly: boolean;
	objectFieldTypes: ObjectFieldType[];
	objectLayoutId: string;
}

const LayoutWrapper: React.FC<ILayoutWrapperProps> = ({
	isViewOnly,
	objectFieldTypes,
	objectLayoutId,
}) => {
	return (
		<LayoutContextProvider
			value={{
				isViewOnly,
				objectFieldTypes,
				objectLayoutId,
			}}
		>
			<Layout />
		</LayoutContextProvider>
	);
};

export default LayoutWrapper;
