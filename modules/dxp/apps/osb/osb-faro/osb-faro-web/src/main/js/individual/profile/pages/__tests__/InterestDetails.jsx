import * as data from 'test/data';
import InterestDetails from '../InterestDetails';
import React from 'react';
import {Individual} from 'shared/util/records';
import {render, waitForElementToBeRemoved} from '@testing-library/react';
import {StaticRouter} from 'react-router';

const defaultProps = {
	active: 'true',
	groupId: '23',
	id: 'test',
	individual: new Individual(data.mockIndividual()),
	interestId: 1
};

jest.unmock('react-dom');

describe('InterestDetails', () => {
	it('should render', async () => {
		const {container} = render(
			<StaticRouter>
				<InterestDetails {...defaultProps} />
			</StaticRouter>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});

	it('should render an active pages list tab', async () => {
		const {container, getByText} = render(
			<StaticRouter>
				<InterestDetails {...defaultProps} />
			</StaticRouter>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(getByText('Active Pages')).toBeTruthy();
		expect(getByText('Active Pages').parentElement).toHaveClass('active');
	});

	it('should render an inactive pages list tab', async () => {
		const {container, getByText} = render(
			<StaticRouter>
				<InterestDetails {...defaultProps} active='false' />
			</StaticRouter>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(getByText('Inactive Pages')).toBeTruthy();
		expect(getByText('Inactive Pages').parentElement).toHaveClass('active');
	});
});
