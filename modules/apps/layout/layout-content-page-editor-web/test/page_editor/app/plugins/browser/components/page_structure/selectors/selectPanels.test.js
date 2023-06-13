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

import {ITEM_TYPES} from '../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/itemTypes';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/layoutDataItemTypes';
import {VIEWPORT_SIZES} from '../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/viewportSizes';
import {selectPanels} from '../../../../../../../../src/main/resources/META-INF/resources/page_editor/plugins/browser/components/page_structure/selectors/selectPanels';

jest.mock(
	'../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config',
	() => ({
		config: {
			formTypes: [
				{
					hasPermission: false,
					label: 'Form Type 1',
					value: '11111',
				},
			],
		},
	})
);

const STATE = {
	fragmentEntryLinks: {
		fragmentEntryLinkId: {},
	},
	layoutData: {
		items: {
			container: {
				itemId: 'container',
				parentId: '',
				type: LAYOUT_DATA_ITEM_TYPES.container,
			},
			form: {
				children: [],
				config: {
					classNameId: '11111',
					classTypeId: '0',
				},
				itemId: 'form',
				parentId: '',
				type: LAYOUT_DATA_ITEM_TYPES.form,
			},
			fragment: {
				config: {
					fragmentEntryLinkId: 'fragmentEntryLinkId',
				},
				itemId: 'fragment',
				parentId: '',
				type: LAYOUT_DATA_ITEM_TYPES.fragment,
			},
			row: {
				itemId: 'row',
				parentId: '',
				type: LAYOUT_DATA_ITEM_TYPES.row,
			},
		},
		rootItems: {
			main: 'container',
		},
		version: 1,
	},
	restrictedItemIds: new Set(),
	selectedViewportSize: VIEWPORT_SIZES.desktop,
};

describe('selectPanels', () => {
	beforeAll(() => {
		Liferay.FeatureFlags['LPS-169923'] = true;
	});

	afterAll(() => {
		Liferay.FeatureFlags['LPS-169923'] = false;
	});

	it('return advanced panel if the user has UPDATE_LAYOUT_ADVANCED_OPTIONS permissions', () => {
		const nextState = {
			...STATE,
			permissions: {
				UPDATE: false,
				UPDATE_LAYOUT_ADVANCED_OPTIONS: true,
				UPDATE_LAYOUT_LIMITED: true,
			},
		};

		const panels = selectPanels(
			'container',
			ITEM_TYPES.layoutDataItem,
			nextState
		);

		expect(panels).toEqual(
			expect.objectContaining({
				panelsIds: expect.objectContaining({
					containerAdvanced: true,
				}),
			})
		);
	});

	it('return advanced panel if the user has UPDATE permissions', () => {
		const nextState = {
			...STATE,
			permissions: {
				UPDATE: true,
			},
		};

		const panels = selectPanels(
			'container',
			ITEM_TYPES.layoutDataItem,
			nextState
		);

		expect(panels).toEqual(
			expect.objectContaining({
				panelsIds: expect.objectContaining({
					containerAdvanced: true,
				}),
			})
		);
	});

	it('does not return advanced panel if the user does not have UPDATE or UPDATE_LAYOUT_ADVANCED_OPTIONS permissions', () => {
		const nextState = {
			...STATE,
			permissions: {
				UPDATE: false,
				UPDATE_LAYOUT_ADVANCED_OPTIONS: false,
				UPDATE_LAYOUT_LIMITED: true,
			},
		};

		const panels = selectPanels(
			'container',
			ITEM_TYPES.layoutDataItem,
			nextState
		);

		expect(panels).toEqual(
			expect.objectContaining({
				panelsIds: expect.objectContaining({
					containerAdvanced: false,
				}),
			})
		);
	});

	it('does not return styles panel if the user does not have UPDATE or UPDATE_LAYOUT_LIMITED', () => {
		const nextState = {
			...STATE,
			permissions: {
				UPDATE: false,
				UPDATE_LAYOUT_BASIC: true,
				UPDATE_LAYOUT_LIMITED: false,
			},
		};

		const panels = selectPanels(
			'fragment',
			ITEM_TYPES.layoutDataItem,
			nextState
		);

		expect(panels).toEqual(
			expect.objectContaining({
				panelsIds: expect.objectContaining({
					fragmentStyles: false,
				}),
			})
		);
	});

	it('returns only general panel if the fragment is restricted', () => {
		const nextState = {
			...STATE,
			permissions: {
				UPDATE: true,
			},
			restrictedItemIds: new Set(['fragment']),
		};

		const panels = selectPanels(
			'fragment',
			ITEM_TYPES.layoutDataItem,
			nextState
		);

		expect(panels).toEqual(
			expect.objectContaining({
				panelsIds: expect.objectContaining({
					fragmentGeneral: true,
				}),
			})
		);
	});

	it('return styles panel if the user does not have UPDATE or UPDATE_LAYOUT_LIMITED for container and row types', () => {
		const nextState = {
			...STATE,
			permissions: {
				UPDATE: false,
				UPDATE_LAYOUT_BASIC: true,
				UPDATE_LAYOUT_LIMITED: false,
			},
		};

		const containerPanels = selectPanels(
			'container',
			ITEM_TYPES.layoutDataItem,
			nextState
		);

		const rowPanels = selectPanels(
			'container',
			ITEM_TYPES.layoutDataItem,
			nextState
		);

		expect(containerPanels).toEqual(
			expect.objectContaining({
				panelsIds: expect.objectContaining({
					containerStyles: true,
				}),
			})
		);

		expect(rowPanels).toEqual(
			expect.objectContaining({
				panelsIds: expect.objectContaining({
					containerStyles: true,
				}),
			})
		);
	});

	it('only returns form general panel (not the styles and advanced panels) if the item mapped to the form does not have permissions', () => {
		const nextState = {
			...STATE,
			permissions: {
				UPDATE: true,
				UPDATE_LAYOUT_BASIC: true,
				UPDATE_LAYOUT_LIMITED: true,
			},
		};

		const panels = selectPanels(
			'form',
			ITEM_TYPES.layoutDataItem,
			nextState
		);

		expect(panels).toEqual(
			expect.objectContaining({
				panelsIds: expect.objectContaining({
					formGeneral: true,
				}),
			})
		);
		expect(panels).toEqual(
			expect.objectContaining({
				panelsIds: expect.not.objectContaining({
					containerStyles: false,
					formAdvancedPanel: false,
				}),
			})
		);
	});
});
