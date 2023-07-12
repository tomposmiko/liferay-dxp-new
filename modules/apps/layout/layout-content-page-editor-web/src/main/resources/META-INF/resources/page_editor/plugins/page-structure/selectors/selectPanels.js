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

import {EDITABLE_FRAGMENT_ENTRY_PROCESSOR} from '../../../app/config/constants/editableFragmentEntryProcessor';
import {EDITABLE_TYPES} from '../../../app/config/constants/editableTypes';
import {FRAGMENT_CONFIGURATION_ROLES} from '../../../app/config/constants/fragmentConfigurationRoles';
import {ITEM_TYPES} from '../../../app/config/constants/itemTypes';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../app/config/constants/layoutDataItemTypes';
import {VIEWPORT_SIZES} from '../../../app/config/constants/viewportSizes';
import selectCanUpdateEditables from '../../../app/selectors/selectCanUpdateEditables';
import selectCanUpdateItemConfiguration from '../../../app/selectors/selectCanUpdateItemConfiguration';
import selectEditableValue from '../../../app/selectors/selectEditableValue';
import isMapped from '../../../app/utils/isMapped';
import {CollectionGeneralPanel} from '../components/item-configuration-panels/CollectionGeneralPanel';
import ContainerLinkPanel from '../components/item-configuration-panels/ContainerLinkPanel';
import {ContainerStylesPanel} from '../components/item-configuration-panels/ContainerStylesPanel';
import EditableLinkPanel from '../components/item-configuration-panels/EditableLinkPanel';
import {FragmentGeneralPanel} from '../components/item-configuration-panels/FragmentGeneralPanel';
import {FragmentStylesPanel} from '../components/item-configuration-panels/FragmentStylesPanel';
import {ImagePropertiesPanel} from '../components/item-configuration-panels/ImagePropertiesPanel';
import {MappingPanel} from '../components/item-configuration-panels/MappingPanel';
import {RowGeneralPanel} from '../components/item-configuration-panels/RowGeneralPanel';
import {RowStylesPanel} from '../components/item-configuration-panels/RowStylesPanel';

export const PANEL_IDS = {
	collectionGeneral: 'collectionGeneral',
	containerLink: 'containerLink',
	containerStyles: 'containerStyles',
	editableLink: 'editableLink',
	editableMapping: 'editableMapping',
	fragmentGeneral: 'fragmentGeneral',
	fragmentStyles: 'fragmentStyles',
	imageProperties: 'imageProperties',
	rowGeneral: 'rowGeneral',
	rowStyles: 'rowStyles',
};

export const PANELS = {
	[PANEL_IDS.collectionGeneral]: {
		component: CollectionGeneralPanel,
		label: Liferay.Language.get('general'),
		priority: 0,
	},
	[PANEL_IDS.containerLink]: {
		component: ContainerLinkPanel,
		label: Liferay.Language.get('link'),
		priority: 0,
	},
	[PANEL_IDS.containerStyles]: {
		component: ContainerStylesPanel,
		label: Liferay.Language.get('styles'),
		priority: 0,
	},
	[PANEL_IDS.editableLink]: {
		component: EditableLinkPanel,
		label: Liferay.Language.get('link'),
		priority: 2,
	},
	[PANEL_IDS.editableMapping]: {
		component: MappingPanel,
		label: Liferay.Language.get('mapping'),
		priority: 1,
	},
	[PANEL_IDS.fragmentGeneral]: {
		component: FragmentGeneralPanel,
		label: Liferay.Language.get('general'),
		priority: 1,
	},
	[PANEL_IDS.fragmentStyles]: {
		component: FragmentStylesPanel,
		label: Liferay.Language.get('styles'),
		priority: 0,
	},
	[PANEL_IDS.imageProperties]: {
		component: ImagePropertiesPanel,
		label: Liferay.Language.get('image'),
		priority: 3,
	},
	[PANEL_IDS.rowGeneral]: {
		component: RowGeneralPanel,
		label: Liferay.Language.get('general'),
		priority: 1,
	},
	[PANEL_IDS.rowStyles]: {
		component: RowStylesPanel,
		label: Liferay.Language.get('styles'),
		priority: 0,
	},
};

export const selectPanels = (activeItemId, activeItemType, state) => {
	let activeItem = null;
	let panelsIds = {};

	if (activeItemType === ITEM_TYPES.layoutDataItem) {
		activeItem = state.layoutData.items[activeItemId];
	}
	else if (activeItemType === ITEM_TYPES.editable) {
		const [fragmentEntryLinkId] = activeItemId.split('-');

		const editableId = activeItemId.substr(
			activeItemId.indexOf('-') + 1,
			activeItemId.length
		);

		activeItem = {
			editableId,
			editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
			fragmentEntryLinkId,
			itemId: activeItemId,
			type:
				state.fragmentEntryLinks[fragmentEntryLinkId].editableTypes[
					editableId
				],
		};
	}

	if (!activeItem) {
		return {activeItem, panelsIds};
	}

	const canUpdateEditables = selectCanUpdateEditables(state);
	const canUpdateItemConfiguration = selectCanUpdateItemConfiguration(state);

	if (canUpdateEditables && activeItem.editableId) {
		const editableValue = selectEditableValue(
			state,
			activeItem.fragmentEntryLinkId,
			activeItem.editableId,
			activeItem.editableValueNamespace
		);
		const editableIsMapped = isMapped(editableValue);

		panelsIds = {
			[PANEL_IDS.editableLink]: [
				EDITABLE_TYPES.text,
				EDITABLE_TYPES.image,
				EDITABLE_TYPES.link,
			].includes(activeItem.type),
			[PANEL_IDS.imageProperties]:
				!editableIsMapped &&
				[EDITABLE_TYPES.image, EDITABLE_TYPES.backgroundImage].includes(
					activeItem.type
				),
			[PANEL_IDS.editableMapping]: true,
		};
	}
	else if (activeItem.type === LAYOUT_DATA_ITEM_TYPES.collection) {
		panelsIds = {
			[PANEL_IDS.collectionGeneral]:
				state.selectedViewportSize === VIEWPORT_SIZES.desktop &&
				canUpdateItemConfiguration,
		};
	}
	else if (activeItem.type === LAYOUT_DATA_ITEM_TYPES.container) {
		panelsIds = {
			[PANEL_IDS.containerStyles]: canUpdateItemConfiguration,
			[PANEL_IDS.containerLink]:
				state.selectedViewportSize === VIEWPORT_SIZES.desktop &&
				canUpdateItemConfiguration,
		};
	}
	else if (activeItem.type === LAYOUT_DATA_ITEM_TYPES.fragment) {
		const fieldSets =
			state.fragmentEntryLinks[activeItem.config.fragmentEntryLinkId]
				?.configuration?.fieldSets ?? [];

		panelsIds = {
			[PANEL_IDS.fragmentStyles]: canUpdateItemConfiguration,
			[PANEL_IDS.fragmentGeneral]:
				state.selectedViewportSize === VIEWPORT_SIZES.desktop &&
				canUpdateItemConfiguration &&
				fieldSets.some(
					(fieldSet) =>
						fieldSet.configurationRole !==
						FRAGMENT_CONFIGURATION_ROLES.style
				),
		};
	}
	else if (activeItem.type === LAYOUT_DATA_ITEM_TYPES.row) {
		panelsIds = {
			[PANEL_IDS.rowStyles]: canUpdateItemConfiguration,
			[PANEL_IDS.rowGeneral]:
				canUpdateItemConfiguration &&
				state.selectedViewportSize === VIEWPORT_SIZES.desktop,
		};
	}

	return {activeItem, panelsIds};
};
