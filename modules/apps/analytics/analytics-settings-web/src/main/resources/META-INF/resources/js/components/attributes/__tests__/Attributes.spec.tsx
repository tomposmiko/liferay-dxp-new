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

import fetch from 'jest-fetch-mock';

import '@testing-library/jest-dom/extend-expect';
import {useModal} from '@clayui/modal';
import {act, cleanup, render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import React from 'react';
import ReactDOM from 'react-dom';

import {TTableRequestParams} from '../../table/types';
import Attributes from '../Attributes';
import Modal from '../Modal';

const response = {
	account: 25,
	order: 0,
	people: 44,
	product: 34,
};

const responseModal = {
	actions: {},
	facets: [],
	items: [
		{
			example: 'True',
			name: 'agreedToTermsOfUse',
			required: false,
			selected: true,
			source: 'user',
			type: 'Boolean',
		},
		{
			example: '31st Oct 2008',
			name: 'birthday',
			required: false,
			selected: true,
			source: 'contact',
			type: 'Date',
		},
	],
};

interface IComponentWithDataProps {
	requestFn: (params: TTableRequestParams) => Promise<any>;
}

const ComponentWithData: React.FC<IComponentWithDataProps> = ({requestFn}) => {
	const {observer} = useModal({onClose: () => {}});

	return (
		<Modal
			observer={observer}
			onCancel={() => {}}
			onSubmit={() => {}}
			requestFn={requestFn}
			title="Assign Modal Title"
		/>
	);
};

describe('Attributes', () => {
	beforeAll(() => {

		// @ts-ignore

		ReactDOM.createPortal = jest.fn((element) => {
			return element;
		});
		jest.useFakeTimers();
	});

	afterAll(() => {
		jest.useRealTimers();
	});

	afterEach(() => {
		jest.clearAllTimers();
		jest.restoreAllMocks();
		cleanup();
	});

	it('renders Attributes without crashing', async () => {
		fetch.mockResponseOnce(JSON.stringify(response));

		await act(async () => {
			render(<Attributes />);
		});

		expect(screen.getByText('people')).toBeInTheDocument();

		expect(screen.getByText('account')).toBeInTheDocument();

		expect(screen.getByText('products')).toBeInTheDocument();

		expect(screen.getByText('order')).toBeInTheDocument();
	});

	it('renders Attributes with values for account, order, people, product', async () => {
		fetch.mockResponseOnce(JSON.stringify(response));

		render(<Attributes />);

		expect(await screen.findByRole(/people/i)).toHaveTextContent(
			/people44/i
		);
		expect(await screen.findByRole(/people/i)).toHaveTextContent(
			/selected/i
		);

		expect(await screen.findByRole(/account/i)).toHaveTextContent(
			/account25/i
		);

		expect(await screen.findByRole(/account/i)).toHaveTextContent(
			/selected/i
		);

		expect(await screen.findByRole(/product/i)).toHaveTextContent(
			/products34/i
		);

		expect(await screen.findByRole(/product/i)).toHaveTextContent(
			/selected/i
		);

		expect(await screen.findByRole(/order/i)).toHaveTextContent(/order0/i);

		expect(await screen.findByRole(/order/i)).toHaveTextContent(
			/selected/i
		);
	});

	it('renders Attributes with select buttons', async () => {
		fetch.mockResponseOnce(JSON.stringify(response));

		await act(async () => {
			render(<Attributes />);
		});

		const buttons = screen.getAllByText('select-attributes');

		expect(buttons[0]).toBeInTheDocument();

		expect(buttons[1]).toBeInTheDocument();

		expect(buttons[2]).toBeInTheDocument();

		expect(buttons[0]).toHaveAttribute('type', 'button');

		expect(buttons[1]).toHaveAttribute('type', 'button');

		expect(buttons[2]).toHaveAttribute('type', 'button');
	});

	// This test works but its printing some warnings in the console.
	// TODO: Improve this test to get rid of them.

	it.skip('renders Modal with data when select button is clicked', async () => {
		fetch.mockResponse(JSON.stringify(response));

		render(<Attributes />);

		const buttons = await screen.findAllByText('select-attributes');

		const modalContent = document.getElementsByClassName('modal-content');

		userEvent.click(buttons[0]);

		await act(async () => {
			fetch.mockResponseOnce(JSON.stringify(responseModal));

			render(<ComponentWithData requestFn={async () => responseModal} />);

			jest.useFakeTimers();

			await waitFor(() => screen.getByText('agreedToTermsOfUse'));

			await waitFor(() => screen.getByText('birthday'));
		});

		expect(modalContent).toBeTruthy();

		expect(screen.getByText('agreedToTermsOfUse')).toBeInTheDocument();

		expect(screen.getByText('Boolean')).toBeInTheDocument();

		expect(screen.getByText('True')).toBeInTheDocument();

		expect(screen.getByText('user')).toBeInTheDocument();

		expect(screen.getByText('birthday')).toBeInTheDocument();

		expect(screen.getByText('Date')).toBeInTheDocument();

		expect(screen.getByText('31st Oct 2008')).toBeInTheDocument();

		expect(screen.getByText('contact')).toBeInTheDocument();
	});
});
