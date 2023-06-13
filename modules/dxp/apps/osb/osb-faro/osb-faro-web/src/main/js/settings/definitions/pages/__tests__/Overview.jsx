import * as Constants from 'shared/util/constants';
import mockStore from 'test/mock-store';
import React from 'react';
import {Overview} from '../Overview';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

describe('Definitions Overview', () => {
	it('should render a list of definitions that includes individuals, accounts, behaviors, events, search and interests', () => {
		// TODO: LRAC-4511 Remove DEVELOPER_MODE
		Constants.DEVELOPER_MODE = true;

		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<Overview groupId='23' />
				</StaticRouter>
			</Provider>
		);

		jest.runAllTimers();

		expect(
			container.querySelectorAll('.list-group-title a')[0]
		).toHaveTextContent('Individuals');

		expect(
			container.querySelectorAll('.list-group-title a')[1]
		).toHaveTextContent('Accounts');

		expect(
			container.querySelectorAll('.list-group-title a')[2]
		).toHaveTextContent('Behaviors');

		expect(
			container.querySelectorAll('.list-group-title a')[3]
		).toHaveTextContent('Events');

		expect(
			container.querySelectorAll('.list-group-title a')[4]
		).toHaveTextContent('Event Attributes');

		expect(
			container.querySelectorAll('.list-group-title a')[5]
		).toHaveTextContent('Search');

		expect(
			container.querySelectorAll('.list-group-title a')[6]
		).toHaveTextContent('Interests');
	});
});
