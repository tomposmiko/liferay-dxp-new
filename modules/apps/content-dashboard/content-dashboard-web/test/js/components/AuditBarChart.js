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

import {cleanup, render} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import React from 'react';

import AuditBarChart from '../../../src/main/resources/META-INF/resources/js/components/AuditBarChart';

import '@testing-library/jest-dom/extend-expect';

const mockVocabulariesOneCategory = [
	{
		key: 'business-decision-maker',
		name: 'Business Decision Maker',
		value: 987,
		vocabularyName: 'Audience',
	},
	{
		key: 'business-end-user',
		name: 'Business End User',
		value: 1095,
		vocabularyName: 'Audience',
	},
	{
		key: 'technical-decision-maker',
		name: 'Technical Decision Maker',
		value: 2020,
		vocabularyName: 'Audience',
	},
	{
		key: 'technical-end-user',
		name: 'Technical End User',
		value: 422,
		vocabularyName: 'Audience',
	},
];

const mockVocabulariesTwoCategories = [
	{
		categories: [
			{
				key: 'education',
				name: 'Education',
				value: 478,
				vocabularyName: 'Stage',
			},
			{
				key: 'selection',
				name: 'Selection',
				value: 1055,
				vocabularyName: 'Stage',
			},
			{
				key: 'selection',
				name: 'Selection',
				value: 822,
				vocabularyName: 'Stage',
			},
		],
		key: 'business-decision-maker',
		name: 'Business Decision Maker',
		vocabularyName: 'Audience',
	},
	{
		categories: [
			{
				key: 'education',
				name: 'Education',
				value: 125,
				vocabularyName: 'Stage',
			},
			{
				key: 'selection',
				name: 'Selection',
				value: 1906,
				vocabularyName: 'Stage',
			},
			{
				key: 'solution',
				name: 'Solution',
				value: 987,
				vocabularyName: 'Stage',
			},
		],
		key: 'business-end-user',
		name: 'Business End User',
		vocabularyName: 'Audience',
	},
	{
		categories: [
			{
				key: 'education',
				name: 'Education',
				value: 444,
				vocabularyName: 'Stage',
			},
			{
				key: 'selection',
				name: 'Selection',
				value: 1733,
				vocabularyName: 'Stage',
			},
			{
				key: 'solution',
				name: 'Solution',
				value: 1807,
				vocabularyName: 'Stage',
			},
		],
		key: 'technical-decision-maker',
		name: 'Technical Decision Maker',
		vocabularyName: 'Audience',
	},
	{
		categories: [
			{
				key: 'education',
				name: 'Education',
				value: 125,
				vocabularyName: 'Stage',
			},
			{
				key: 'selection',
				name: 'Selection',
				value: 317,
				vocabularyName: 'Stage',
			},
			{
				key: 'solution',
				name: 'Solution',
				value: 187,
				vocabularyName: 'Stage',
			},
		],
		key: 'technical-end-user',
		name: 'Technical End User',
		vocabularyName: 'Audience',
	},
];

describe('AuditBarChart', () => {
	afterEach(() => {
		jest.clearAllMocks();
		cleanup();
	});

	it('renders audit bar chart from one category', () => {
		const {container, getByText} = render(
			<AuditBarChart
				rtl={false}
				vocabularies={mockVocabulariesOneCategory}
			/>
		);

		expect(getByText('Audience')).toBeInTheDocument();

		expect(getByText('Business Decision Maker')).toBeInTheDocument();
		expect(getByText('Business End User')).toBeInTheDocument();
		expect(getByText('Technical Decision Maker')).toBeInTheDocument();
		expect(getByText('Technical End User')).toBeInTheDocument();

		const bars = container.getElementsByClassName(
			'recharts-layer recharts-bar-rectangle'
		);
		expect(bars.length).toBe(4);
	});

	it('renders audit bar chart from two categories', () => {
		const {container, getByText} = render(
			<AuditBarChart
				rtl={false}
				vocabularies={mockVocabulariesTwoCategories}
			/>
		);

		expect(getByText('Stage:')).toBeInTheDocument();
		expect(getByText('Education')).toBeInTheDocument();
		expect(getByText('Selection')).toBeInTheDocument();
		expect(getByText('Solution')).toBeInTheDocument();

		expect(getByText('Audience')).toBeInTheDocument();
		expect(getByText('Business Decision Maker')).toBeInTheDocument();
		expect(getByText('Business End User')).toBeInTheDocument();
		expect(getByText('Technical Decision Maker')).toBeInTheDocument();
		expect(getByText('Technical End User')).toBeInTheDocument();

		const bars = container.getElementsByClassName(
			'recharts-layer recharts-bar-rectangle'
		);
		expect(bars.length).toBe(12);
	});

	it('renders audit bar chart only from checked categories from legend', () => {
		const {container, getByLabelText} = render(
			<AuditBarChart
				rtl={false}
				vocabularies={mockVocabulariesTwoCategories}
			/>
		);

		const bars = container.getElementsByClassName(
			'recharts-layer recharts-bar-rectangle'
		);

		const educationCheckbox = getByLabelText('Education');
		const selectionCheckbox = getByLabelText('Selection');
		const solutionCheckbox = getByLabelText('Solution');

		userEvent.click(educationCheckbox);
		expect(educationCheckbox.checked).toEqual(false);
		expect(bars.length).toBe(8);

		userEvent.click(selectionCheckbox);
		expect(selectionCheckbox.checked).toEqual(false);
		expect(bars.length).toBe(4);

		userEvent.click(solutionCheckbox);
		expect(solutionCheckbox.checked).toEqual(false);
		expect(bars.length).toBe(0);
	});

	it('renders audit bar chart message when there are no categories selected', () => {
		const {getByLabelText, getByText} = render(
			<AuditBarChart
				rtl={false}
				vocabularies={mockVocabulariesTwoCategories}
			/>
		);

		const educationCheckbox = getByLabelText('Education');
		const selectionCheckbox = getByLabelText('Selection');
		const solutionCheckbox = getByLabelText('Solution');

		userEvent.click(educationCheckbox);
		userEvent.click(selectionCheckbox);
		userEvent.click(solutionCheckbox);

		expect(
			getByText('there-are-no-categories-selected')
		).toBeInTheDocument();
		expect(
			getByText(
				'select-categories-from-the-checkboxes-in-the-legend-above'
			)
		).toBeInTheDocument();
	});
});
