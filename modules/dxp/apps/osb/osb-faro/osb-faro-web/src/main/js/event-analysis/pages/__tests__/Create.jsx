import * as data from 'test/data';
import client from 'shared/apollo/client';
import EventAnalysisCreate from '../Create';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {
	cleanup,
	fireEvent,
	render,
	waitForElement,
	waitForElementToBeRemoved
} from '@testing-library/react';
import {DISPLAY_NAME} from 'shared/util/pagination';
import {MemoryRouter, Route} from 'react-router-dom';
import {MockedProvider} from '@apollo/react-testing';
import {mockEventDefinitionsReq, mockTimeRangeReq} from 'test/graphql-data';
import {OrderByDirections} from 'shared/util/constants';
import {Provider} from 'react-redux';
import {range} from 'lodash';
import {Routes} from 'shared/util/router';

jest.unmock('react-dom');

jest.mock('react-router-dom', () => ({
	...jest.requireActual('react-router-dom'),
	useParams: () => ({
		channelId: '456',
		groupId: '123'
	})
}));

const WrappedComponent = () => (
	<Provider store={mockStore()}>
		<ApolloProvider client={client}>
			<MockedProvider
				mocks={[
					mockTimeRangeReq(),
					mockEventDefinitionsReq(
						range(10).map(i =>
							data.mockEventDefinition(i, {
								__typename: 'EventDefinition'
							})
						),
						{
							eventType: 'ALL',
							hidden: false,
							keyword: '',
							size: 200,
							sort: {
								column: DISPLAY_NAME,
								type: OrderByDirections.Ascending
							}
						}
					)
				]}
			>
				<MemoryRouter
					initialEntries={[
						'/workspace/123/456/event-analysis/create'
					]}
				>
					<Route path={Routes.EVENT_ANALYSIS_CREATE}>
						<EventAnalysisCreate />
					</Route>
				</MemoryRouter>
			</MockedProvider>
		</ApolloProvider>
	</Provider>
);

describe('Event Analysis Create', () => {
	afterEach(cleanup);

	it('should render', async () => {
		const {container} = render(<WrappedComponent />);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});

	it('should render empty state', async () => {
		const {container, getByPlaceholderText, getByText} = render(
			<WrappedComponent />
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(getByPlaceholderText('Unnamed Analysis')).toBeTruthy();
		expect(getByText('Add an event to analyze.')).toBeTruthy();
		expect(
			container.querySelector('.dropdown-range-key-root button')
				.textContent
		).toEqual('Last 30 days');
		expect(container.querySelector('.event-list').textContent).toBe('');
		expect(
			container.querySelector(
				'.attribute-breakdown-section-root .attribute-container'
			)
		).toBeFalsy();
		expect(
			container.querySelector(
				'.attribute-filter-section-root .attribute-container'
			)
		).toBeFalsy();
		expect(
			container.querySelector('.compare-to-previous-checkbox input')
				.checked
		).toBeFalsy();
	});

	it('should render disabled button to save event analysis', async () => {
		const {container, getByText} = render(<WrappedComponent />);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(getByText('Save Analysis')).toBeDisabled();
	});

	it('should enable the save button when there is at least one name and one event added', async () => {
		const {container, getByText} = render(<WrappedComponent />);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		const inputName = container.querySelector('input.title-input');

		fireEvent.change(inputName, {
			target: {
				value: 'My First Event Analysis'
			}
		});

		expect(getByText('My First Event Analysis')).toBeTruthy();

		expect(getByText('Save Analysis')).toBeDisabled();

		const addEventButton = container.querySelector('.add-event-button');

		fireEvent.click(addEventButton);

		jest.runAllTimers();

		const dropdown = document.querySelector(
			'.base-dropdown-menu-root.show'
		);

		await waitForElement(() => dropdown);

		const assetClickedButton = document.querySelector(
			'.base-dropdown-list > li button'
		);

		fireEvent.click(assetClickedButton);

		jest.runAllTimers();

		expect(getByText('Save Analysis')).toBeEnabled();
	});

	it('should contain a link to return to the event analysis list on the cancel button', () => {
		const {getByText} = render(<WrappedComponent />);

		expect(getByText('Cancel')).toHaveAttribute(
			'href',
			'/workspace/123/456/event-analysis'
		);
	});
});
