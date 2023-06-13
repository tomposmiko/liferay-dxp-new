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
	getDownPosition,
	getUpPosition,
} from '../../../src/main/resources/META-INF/resources/site_navigation_menu_editor/components/MenuItem';

describe('MenuItem', () => {

	// LPS-177667

	describe('keyboard DND', () => {
		const items = [
			{
				children: [],
				parentSiteNavigationMenuItemId: '0',
				siteNavigationMenuItemId: 'OLa',
			},
			{
				children: [
					{
						children: [
							{
								children: [],
								parentSiteNavigationMenuItemId: 'B',
								siteNavigationMenuItemId: 'C',
							},
							{
								children: [],
								parentSiteNavigationMenuItemId: 'B',
								siteNavigationMenuItemId: 'D',
							},
						],
						parentSiteNavigationMenuItemId: 'A',
						siteNavigationMenuItemId: 'B',
					},
					{
						children: [],
						parentSiteNavigationMenuItemId: 'A',
						siteNavigationMenuItemId: 'E',
					},
				],
				parentSiteNavigationMenuItemId: '0',
				siteNavigationMenuItemId: 'A',
			},
			{
				children: [],
				parentSiteNavigationMenuItemId: '0',
				siteNavigationMenuItemId: 'F',
			},
		];

		const states = [
			{order: 0, parentId: '0'},
			{order: 0, parentId: 'OLa'},
			{order: 1, parentId: '0'},
			{order: 0, parentId: 'A'},
			{order: 0, parentId: 'B'},
			{order: 0, parentId: 'C'},
			{order: 1, parentId: 'B'},
			{order: 0, parentId: 'D'},
			{order: 2, parentId: 'B'},
			{order: 1, parentId: 'A'},
			{order: 0, parentId: 'E'},
			{order: 2, parentId: 'A'},
			{order: 2, parentId: '0'},
			{order: 0, parentId: 'F'},
			{order: 3, parentId: '0'},
		];

		const getTestCases = (testStates) =>
			testStates.map((state, index, array) => {
				const from = state;
				const to = array[index + 1];
				const name = `${from.parentId}[${from.order}] -> ${
					to ? `${to.parentId}[${to.order}]` : 'null'
				}`;

				return [name, state, array[index + 1]];
			});

		describe('getDownPosition', () => {
			test.each(getTestCases(states))('%p', (_, from, to) => {
				expect(
					getDownPosition({
						items,
						order: from.order,
						parentSiteNavigationMenuItemId: from.parentId,
					})
				).toEqual(
					to
						? {
								order: to.order,
								parentSiteNavigationMenuItemId: to.parentId,
						  }
						: null
				);
			});
		});

		describe('getUpPosition', () => {
			test.each(getTestCases(states.reverse()))('%p', (_, from, to) => {
				expect(
					getUpPosition({
						items,
						order: from.order,
						parentSiteNavigationMenuItemId: from.parentId,
					})
				).toEqual(
					to
						? {
								order: to.order,
								parentSiteNavigationMenuItemId: to.parentId,
						  }
						: null
				);
			});
		});
	});
});
