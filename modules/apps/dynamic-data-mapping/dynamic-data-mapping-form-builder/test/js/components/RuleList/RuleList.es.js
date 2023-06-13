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

import RuleList from '../../../src/main/resources/META-INF/resources/js/components/RuleList/RuleList.es';

let component;

const spritemap = 'icons.svg';

const pages = [
	{
		rows: [
			{
				columns: [
					{
						fields: [
							{
								fieldName: 'text1',
								label: 'label text 1'
							},
							{
								fieldName: 'text2',
								label: 'label text 2'
							}
						]
					}
				]
			}
		]
	}
];

const configDefault = {
	pages,
	rules: [
		{
			actions: [
				{
					action: 'require',
					expression: '[x+2]',
					label: 'label text 1',
					target: 'text1'
				}
			],
			conditions: [
				{
					operands: [
						{
							type: 'field',
							value: 'text1'
						},
						{
							type: 'value',
							value: 'value 2'
						}
					],
					operator: 'equals-to'
				}
			],
			['logical-operator']: 'OR'
		}
	],
	spritemap
};

describe('RuleList', () => {
	beforeEach(() => {
		jest.useFakeTimers();
	});

	afterEach(() => {
		if (component) {
			component.dispose();
		}
	});

	it('returns the field label for each action', () => {
		component = new RuleList(configDefault);

		const contextLabel =
			component.pages[0].rows[0].columns[0].fields[0].label;

		const actionLabel = component.rules[0].actions[0].label;

		jest.runAllTimers();

		expect(actionLabel).toEqual(contextLabel);
	});

	it('shows message when rule list is empty', () => {
		component = new RuleList({
			pages,
			rules: [],
			spritemap,
			strings: {
				emptyListText:
					'there-are-no-rules-yet-click-on-plus-icon-below-to-add-the-first'
			}
		});

		expect(component).toMatchSnapshot();
	});
});
