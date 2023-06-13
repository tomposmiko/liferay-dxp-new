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

import {act, cleanup, fireEvent, render, waitFor} from '@testing-library/react';
import React from 'react';

import '@testing-library/jest-dom/extend-expect';

import RedirectPattern from '../src/main/resources/META-INF/resources/js/RedirectPatterns';

const defaultProps = {
	portletNamespace: '_portlet_namespace_',
	strings: {
		absoluteURL: 'absoluteURL',
		relativeURL: 'relativeURL',
	},
	userAgents: [
		{label: 'all', value: 'all'},
		{label: 'bot', value: 'bot'},
		{label: 'human', value: 'human'},
	],
};

const renderComponent = (props = defaultProps) =>
	render(<RedirectPattern {...props} />);

describe('RedirectPatterns', () => {
	afterEach(cleanup);

	it('render basic elements if there are no patterns', () => {
		const {getByLabelText, getByText, queryByLabelText} = renderComponent();

		expect(getByLabelText('destination-url')).toBeTruthy();
		expect(getByLabelText('pattern-field-label')).toBeTruthy();

		expect(getByLabelText('add')).toBeTruthy();
		expect(queryByLabelText('remove')).not.toBeInTheDocument();
		expect(getByText('save')).not.toBeDisabled();
	});

	it('fill values if there are patterns', () => {
		const {getByDisplayValue} = renderComponent({
			...defaultProps,
			patterns: [
				{
					destinationURL: 'http://localhost:8080',
					pattern: '/test',
					userAgent: 'bot',
				},
			],
		});

		expect(getByDisplayValue('/test')).toBeInTheDocument();
		expect(getByDisplayValue('http://localhost:8080')).toBeInTheDocument();
	});

	it('add new elements if click in add button', async () => {
		const {getByLabelText, queryAllByLabelText} = renderComponent();

		expect(queryAllByLabelText('add').length).toBe(1);

		act(() => {
			fireEvent.click(getByLabelText('add'));
		});

		await waitFor(() => {
			expect(queryAllByLabelText('add').length).toBe(2);
			expect(queryAllByLabelText('remove').length).toBe(1);
		});
	});

	it('removes selected element if click in remove button', async () => {
		const {
			getByDisplayValue,
			queryAllByDisplayValue,
			queryAllByLabelText,
		} = renderComponent({
			...defaultProps,
			patterns: [
				{
					destinationURL: 'http://localhost:8080/1',
					pattern: '/test1',
					userAgent: 'bot',
				},
				{
					destinationURL: 'http://localhost:8080/2',
					pattern: '/test2',
					userAgent: 'all',
				},
				{
					destinationURL: 'http://localhost:8080/3',
					pattern: '/test3',
					userAgent: 'bot',
				},
			],
		});

		const removeButtons = queryAllByLabelText('remove');

		expect(removeButtons.length).toBe(2);

		act(() => {
			fireEvent.click(removeButtons[0]);
		});

		await waitFor(() => {
			expect(getByDisplayValue('/test1')).toBeInTheDocument();
			expect(getByDisplayValue('/test3')).toBeInTheDocument();
			expect(queryAllByDisplayValue('/test2').length).toBe(0);
		});
	});

	it('save button is disabled if there is any pattern with error', () => {
		const {getByLabelText, getByText} = renderComponent();

		const saveButton = getByText('save');
		const destinationURLInput = getByLabelText('destination-url');

		fireEvent.change(destinationURLInput, {target: {value: 'wrong-url'}});
		fireEvent.blur(destinationURLInput);

		expect(saveButton).toBeDisabled();

		fireEvent.change(destinationURLInput, {target: {value: '/valid-url'}});
		fireEvent.blur(destinationURLInput);

		expect(saveButton).not.toBeDisabled();
	});
});
