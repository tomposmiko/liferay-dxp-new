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
} from '@testing-library/react';
import React from 'react';
import ReactDOM from 'react-dom';

import {TEmptyState} from '../../table/StateRenderer';
import {TTableRequestParams} from '../../table/types';
import Modal from '../Modal';
import People, {EPeople} from '../People';

const accountsResponseUpdated = {
	syncAllAccounts: true,
	syncAllContacts: true,
};

const contactsResponseUpdated = {
	syncAllAccounts: false,
	syncAllContacts: true,
};

const response = {
	syncAllAccounts: false,
	syncAllContacts: false,
	syncedAccountGroupIds: [],
	syncedOrganizationIds: [],
	syncedUserGroupIds: [],
};

const responseEmptyStateModal = {
	actions: {},
	facets: [],
	items: [],
	lastPage: 1,
	page: 1,
	pageSize: 20,
	totalCount: 0,
};

const responseUpdated = {
	syncAllAccounts: true,
	syncAllContacts: true,
	syncedAccountGroupIds: [],
	syncedOrganizationIds: [],
	syncedUserGroupIds: [],
};

const responseWithDataModal = {
	actions: {},
	facets: [],
	items: [
		{
			id: 45149,
			name: 'test',
			selected: false,
		},
		{
			id: 45150,
			name: 'test2',
			selected: false,
		},
	],
	lastPage: 1,
	page: 1,
	pageSize: 20,
	totalCount: 2,
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
					label: Liferay.Language.get('test-groups'),
				},
			]}
			emptyState={emptyState}
			name={EPeople.AccountGroupIds}
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
			title="Add Test Group"
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
		title: 'Empty State Title',
	};

	return (
		<Modal
			columns={[]}
			emptyState={emptyState}
			name={EPeople.AccountGroupIds}
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
			title="Add Test Group"
		/>
	);
};

describe('People', () => {
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

	it('renders People component without crashing it', async () => {
		fetch.mockResponseOnce(JSON.stringify(response));

		await act(async () => {
			render(<People />);
		});

		const syncAllContactsAndAccounts = screen.getByText(
			'sync-all-contacts-and-accounts'
		);
		const syncContacts = screen.getByText('sync-all-contacts');

		const contactsDescription = screen.getByText(
			'sync-contacts-label-description'
		);

		const syncAccounts = screen.getByText('sync-all-accounts');

		const accountsDescription = screen.getByText(
			'sync-contacts-label-description'
		);

		const selectAccounts = screen.getByText('select-accounts');

		const selectContacts = screen.getByText('select-contacts');

		expect(syncAllContactsAndAccounts).toBeInTheDocument();

		expect(syncContacts).toBeInTheDocument();

		expect(selectContacts).toBeInTheDocument();

		expect(contactsDescription).toBeInTheDocument();

		expect(syncAccounts).toBeInTheDocument();

		expect(selectAccounts).toBeInTheDocument();

		expect(accountsDescription).toBeInTheDocument();
	});

	it('renders component, clicks on "sync all" switch and check if contacts and accounts are all toggled', async () => {
		fetch.mockResponse(JSON.stringify(response));

		const {container} = render(<People />);

		const syncContactsAndAccounts = await screen.findByRole('checkbox', {
			name: 'sync-all-contacts-and-accounts',
		});

		const toggleSwitch = container.querySelector('.toggle-switch-check');

		await act(async () => {
			fireEvent.click(syncContactsAndAccounts);
		});

		fetch.mockResponse(JSON.stringify(responseUpdated));

		expect(toggleSwitch).toHaveAttribute(
			'data-testid',
			'sync-all-contacts-and-accounts__true'
		);

		const allSelected = container.querySelectorAll(
			'.list-group-item-disabled'
		);

		expect(allSelected[0]).toBeInTheDocument();

		expect(allSelected[1]).toBeInTheDocument();

		expect(allSelected[2]).toBeInTheDocument();
	});

	it('renders component, clicks on "Contacts" and "Accounts" switches and checks if "sync all" switch is toggled', async () => {
		fetch.mockResponse(JSON.stringify(response));

		const {container} = render(<People />);

		const allToggleSwitches = container.querySelectorAll(
			'.toggle-switch-check'
		);

		expect(allToggleSwitches[0]).toHaveAttribute(
			'data-testid',
			'sync-all-contacts-and-accounts__false'
		);

		expect(allToggleSwitches[1]).toHaveAttribute(
			'data-testid',
			'sync-all-contacts__false'
		);

		expect(allToggleSwitches[2]).toHaveAttribute(
			'data-testid',
			'sync-all-accounts__false'
		);

		const syncContacts = await screen.findByRole('checkbox', {
			name: 'sync-all-contacts',
		});

		await act(async () => {
			fireEvent.click(syncContacts);
		});

		fetch.mockResponse(JSON.stringify(contactsResponseUpdated));

		expect(allToggleSwitches[1]).toHaveAttribute(
			'data-testid',
			'sync-all-contacts__true'
		);

		const syncAccounts = await screen.findByRole('checkbox', {
			name: 'sync-all-accounts',
		});

		await act(async () => {
			fireEvent.click(syncAccounts);
		});

		fetch.mockResponse(JSON.stringify(accountsResponseUpdated));

		expect(allToggleSwitches[2]).toHaveAttribute(
			'data-testid',
			'sync-all-accounts__true'
		);

		expect(allToggleSwitches[0]).toHaveAttribute(
			'data-testid',
			'sync-all-contacts-and-accounts__true'
		);
	});

	it('renders component, clicks on "user groups" to open modal with empty state', async () => {
		fetch.mockResponseOnce(JSON.stringify(response));

		render(<People />);

		const userGroups = screen.getByText('user-groups');

		await act(async () => {
			fireEvent.click(userGroups);

			fetch.mockResponseOnce(JSON.stringify(responseEmptyStateModal));

			render(
				<ComponentWithEmptyState
					requestFn={async () => responseEmptyStateModal}
				/>
			);

			jest.useFakeTimers();

			await waitFor(() => screen.getByText('Empty State Title'));

			await waitFor(() => screen.getByText('Empty State Description'));
		});

		const modalContent = document.querySelector('.modal-content');

		expect(modalContent).toBeInTheDocument();

		expect(screen.getByText('Empty State Title')).toBeInTheDocument();

		expect(screen.getByText('Empty State Description')).toBeInTheDocument();
	});

	it('renders component, clicks on "user groups" to open modal with data', async () => {
		fetch.mockResponseOnce(JSON.stringify(response));

		render(<People />);

		const userGroups = screen.getByText('user-groups');

		await act(async () => {
			fireEvent.click(userGroups);

			fetch.mockResponseOnce(JSON.stringify(responseEmptyStateModal));

			render(
				<ComponentWithData
					requestFn={async () => responseWithDataModal}
				/>
			);

			jest.useFakeTimers();

			await waitFor(() => screen.getByText('test'));

			await waitFor(() => screen.getByText('test2'));
		});

		const modalContent = document.querySelector('.modal-content');

		expect(modalContent).toBeInTheDocument();

		expect(screen.getByText('Add Test Group')).toBeInTheDocument();

		expect(screen.getByText('test')).toBeInTheDocument();

		expect(screen.getByText('test2')).toBeInTheDocument();
	});

	it('renders component, clicks on "organizations" to open modal with empty state', async () => {
		fetch.mockResponseOnce(JSON.stringify(response));

		render(<People />);

		const organizations = screen.getByText('organizations');

		await act(async () => {
			fireEvent.click(organizations);

			fetch.mockResponseOnce(JSON.stringify(responseEmptyStateModal));

			render(
				<ComponentWithEmptyState
					requestFn={async () => responseEmptyStateModal}
				/>
			);

			jest.useFakeTimers();

			await waitFor(() => screen.getByText('Empty State Title'));

			await waitFor(() => screen.getByText('Empty State Description'));
		});

		const modalContent = document.querySelector('.modal-content');

		expect(modalContent).toBeInTheDocument();

		expect(screen.getByText('Empty State Title')).toBeInTheDocument();

		expect(screen.getByText('Empty State Description')).toBeInTheDocument();
	});

	it('renders component, clicks on "organizations" to open modal with data', async () => {
		fetch.mockResponseOnce(JSON.stringify(response));

		render(<People />);

		const organizations = screen.getByText('organizations');

		await act(async () => {
			fireEvent.click(organizations);

			fetch.mockResponseOnce(JSON.stringify(responseEmptyStateModal));

			render(
				<ComponentWithData
					requestFn={async () => responseWithDataModal}
				/>
			);

			jest.useFakeTimers();

			await waitFor(() => screen.getByText('test'));

			await waitFor(() => screen.getByText('test2'));
		});

		const modalContent = document.querySelector('.modal-content');

		expect(modalContent).toBeInTheDocument();

		expect(screen.getByText('Add Test Group')).toBeInTheDocument();

		expect(screen.getByText('test')).toBeInTheDocument();

		expect(screen.getByText('test2')).toBeInTheDocument();
	});

	it('renders component, clicks on "sync by accounts groups" to open modal with empty state', async () => {
		fetch.mockResponseOnce(JSON.stringify(response));

		render(<People />);

		const accountGroups = screen.getByText('sync-by-account-groups');

		await act(async () => {
			fireEvent.click(accountGroups);

			fetch.mockResponseOnce(JSON.stringify(responseEmptyStateModal));

			render(
				<ComponentWithEmptyState
					requestFn={async () => responseEmptyStateModal}
				/>
			);

			jest.useFakeTimers();

			await waitFor(() => screen.getByText('Empty State Title'));

			await waitFor(() => screen.getByText('Empty State Description'));
		});

		const modalContent = document.querySelector('.modal-content');

		expect(modalContent).toBeInTheDocument();

		expect(screen.getByText('Empty State Title')).toBeInTheDocument();

		expect(screen.getByText('Empty State Description')).toBeInTheDocument();
	});

	it('renders component, clicks on "sync by accounts groups" to open modal with data', async () => {
		fetch.mockResponseOnce(JSON.stringify(response));

		render(<People />);

		const accountGroups = screen.getByText('sync-by-account-groups');

		await act(async () => {
			fireEvent.click(accountGroups);

			fetch.mockResponseOnce(JSON.stringify(responseEmptyStateModal));

			render(
				<ComponentWithData
					requestFn={async () => responseWithDataModal}
				/>
			);

			jest.useFakeTimers();

			await waitFor(() => screen.getByText('test'));

			await waitFor(() => screen.getByText('test2'));
		});

		const modalContent = document.querySelector('.modal-content');

		expect(modalContent).toBeInTheDocument();

		expect(screen.getByText('Add Test Group')).toBeInTheDocument();

		expect(screen.getByText('test')).toBeInTheDocument();

		expect(screen.getByText('test2')).toBeInTheDocument();
	});
});
