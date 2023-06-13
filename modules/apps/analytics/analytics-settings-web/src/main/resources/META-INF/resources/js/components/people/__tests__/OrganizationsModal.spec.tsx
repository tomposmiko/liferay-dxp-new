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
import {
	act,
	cleanup,
	fireEvent,
	render,
	screen,
	waitFor,
	within,
} from '@testing-library/react';
import React from 'react';
import ReactDOM from 'react-dom';

import {TEmptyState} from '../../table/StateRenderer';
import {TTableRequestParams} from '../../table/types';
import Modal from '../Modal';
import {EPeople} from '../People';

const responseWithData = {
	actions: {},
	facets: [],
	items: [
		{
			id: 44536,
			name: 'organizations test',
			selected: false,
		},
		{
			id: 44542,
			name: 'organizations test 2',
			selected: false,
		},
	],
	lastPage: 1,
	page: 1,
	pageSize: 20,
	totalCount: 2,
};

const responseWithDataAllSelected = {
	actions: {},
	facets: [],
	items: [
		{
			id: 44536,
			name: 'organizations test',
			selected: true,
		},
		{
			id: 44542,
			name: 'organizations test 2',
			selected: true,
		},
	],
	lastPage: 1,
	page: 1,
	pageSize: 20,
	totalCount: 2,
};

const responseWithEmptyState = {
	actions: {},
	facets: [],
	items: [],
	lastPage: 1,
	page: 1,
	pageSize: 20,
	totalCount: 0,
};

interface IComponentWithDataProps {
	requestFn: (params: TTableRequestParams) => Promise<any>;
}

interface IComponentWithEmptyStateProps {
	requestFn: (params: TTableRequestParams) => Promise<any>;
}

const ComponentWithData: React.FC<IComponentWithDataProps> = ({requestFn}) => {
	const {observer} = useModal({onClose: () => {}});

	const emptyState: TEmptyState = {
		contentRenderer: () => <></>,
		description: 'Empty State Description',
		noResultsTitle: 'Empty State No Results Title',
		title: 'Empty State Title',
	};

	return (
		<Modal
			columns={[
				{
					expanded: true,
					id: 'name',
					label: Liferay.Language.get('organizations'),
				},
			]}
			emptyState={emptyState}
			name={EPeople.OrganizationIds}
			observer={observer}
			onCloseModal={() => {}}
			requestFn={requestFn}
			syncAllAccounts
			syncAllContacts
			syncedIds={{
				syncedAccountGroupIds: [''],
				syncedOrganizationIds: [''],
				syncedUserGroupIds: [''],
			}}
			title="Assign Modal Title"
		/>
	);
};

const ComponentWithEmptyState: React.FC<IComponentWithEmptyStateProps> = ({
	requestFn,
}) => {
	const {observer} = useModal({onClose: () => {}});

	const emptyState: TEmptyState = {
		contentRenderer: () => <></>,
		description: 'Empty State Description',
		noResultsTitle: 'Empty State No Results Title',
		title: 'There are no organizations',
	};

	return (
		<Modal
			columns={[]}
			emptyState={emptyState}
			name={EPeople.OrganizationIds}
			observer={observer}
			onCloseModal={() => {}}
			requestFn={requestFn}
			syncAllAccounts
			syncAllContacts
			syncedIds={{
				syncedAccountGroupIds: [''],
				syncedOrganizationIds: [''],
				syncedUserGroupIds: [''],
			}}
			title="Add Organizations"
		/>
	);
};

describe('Organizations Modal', () => {
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

	it('renders Organizations Modal with data without crashing it', async () => {
		fetch.mockResponse(JSON.stringify(responseWithData));

		await act(async () => {
			render(
				<ComponentWithData requestFn={async () => responseWithData} />
			);

			jest.runAllTimers();

			await waitFor(() => screen.getByText('organizations test'));

			await waitFor(() => screen.getByText('organizations test 2'));
		});

		const modalContent = document.querySelector('.modal-content');

		const tableColumnText = screen.getByText('organizations test');

		const tableColumnText2 = screen.getByText('organizations test 2');

		expect(modalContent).toBeInTheDocument();

		expect(tableColumnText).toBeInTheDocument();

		expect(tableColumnText2).toBeInTheDocument();
	});

	it('renders Organizations Modal with Empty State without crashing it', async () => {
		fetch.mockResponse(JSON.stringify(responseWithEmptyState));

		await act(async () => {
			render(
				<ComponentWithEmptyState
					requestFn={async () => responseWithEmptyState}
				/>
			);
			jest.runAllTimers();
		});

		const organizationsTitle = screen.getByText('Add Organizations');

		const emptyStateTitle = screen.getByText('There are no organizations');

		const emptyStateDescription = screen.getByText(
			'Empty State Description'
		);

		expect(organizationsTitle).toBeInTheDocument();

		expect(emptyStateTitle).toBeInTheDocument();

		expect(emptyStateDescription).toBeInTheDocument();
	});

	//  TODO: Refactor the test below to be able to fetch the mocked data correctly. Only passes when it runs alone.

	it.skip('renders Organizations Modal, click on checkbox to select all items', async () => {
		fetch.mockResponse(JSON.stringify(responseWithData));

		await act(async () => {
			render(
				<ComponentWithData requestFn={async () => responseWithData} />
			);

			jest.runAllTimers();
		});

		const navigation = screen.getByRole('navigation');

		const selectAllCheckboxes = within(navigation).getByRole('checkbox');

		await act(async () => {
			fireEvent.click(selectAllCheckboxes);
		});

		fetch.mockResponse(JSON.stringify(responseWithDataAllSelected));

		const tableRow = document.querySelector(
			'tr[data-testid="organizations test"]'
		);

		const tableRow2 = document.querySelector(
			'tr[data-testid="organizations test 2"]'
		);

		expect(tableRow).toHaveClass('table-active');

		expect(tableRow2).toHaveClass('table-active');
	});
});
