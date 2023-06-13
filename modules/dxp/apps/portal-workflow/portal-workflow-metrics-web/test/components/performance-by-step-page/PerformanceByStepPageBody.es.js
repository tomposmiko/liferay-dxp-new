/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import {cleanup, render, waitForElement} from '@testing-library/react';
import React from 'react';

import PerformanceByStepPage from '../../../src/main/resources/META-INF/resources/js/components/performance-by-step-page/PerformanceByStepPage.es';
import PromisesResolver from '../../../src/main/resources/META-INF/resources/js/shared/components/promises-resolver/PromisesResolver.es';
import {MockRouter} from '../../mock/MockRouter.es';

const items = [
	{
		breachedInstanceCount: 4,
		durationAvg: 10800000,
		instanceCount: 4,
		key: 'review',
		name: 'Review',
		onTimeInstanceCount: 4,
		overdueInstanceCount: 4
	},
	{
		breachedInstanceCount: 2,
		durationAvg: 475200000,
		instanceCount: 2,
		key: 'update',
		name: 'Update',
		onTimeInstanceCount: 2,
		overdueInstanceCount: 2
	}
];

const wrapper = ({children}) => (
	<MockRouter>
		<PromisesResolver promises={[Promise.resolve()]}>
			{children}
		</PromisesResolver>
	</MockRouter>
);

describe('The performance by step page body should', () => {
	let getAllByTestId;

	afterEach(cleanup);

	beforeEach(() => {
		const renderResult = render(
			<PerformanceByStepPage.Body
				data={{items, totalCount: items.length}}
				page="1"
				pageSize="5"
			/>,
			{wrapper}
		);

		getAllByTestId = renderResult.getAllByTestId;
	});

	test('Be rendered with step names', async () => {
		const stepName = await waitForElement(() => getAllByTestId('stepName'));

		expect(stepName[0].innerHTML).toEqual('Review');
		expect(stepName[1].innerHTML).toEqual('Update');
	});
});

describe('The subcomponents from workload by assignee page body should', () => {
	afterEach(cleanup);

	test('Be rendered with empty view and no content message', async () => {
		const {getByTestId} = render(<PerformanceByStepPage.Body.Empty />);

		const emptyStateDiv = getByTestId('emptyState');

		expect(emptyStateDiv.children[1].children[0].innerHTML).toBe(
			'there-is-no-data-at-the-moment'
		);
	});

	test('Be rendered with empty view and no results message', async () => {
		const {getByTestId} = render(
			<PerformanceByStepPage.Body.Empty filtered={true} />
		);

		const emptyStateDiv = getByTestId('emptyState');

		expect(emptyStateDiv.children[1].children[0].innerHTML).toBe(
			'no-results-were-found'
		);
	});

	test('Be rendered with error view and the expected message', () => {
		const {getByTestId} = render(<PerformanceByStepPage.Body.Error />);

		const emptyStateDiv = getByTestId('emptyState');

		expect(emptyStateDiv.children[0].children[0].innerHTML).toBe(
			'there-was-a-problem-retrieving-data-please-try-reloading-the-page'
		);
	});

	test('Be rendered with loading view', async () => {
		const {getByTestId} = render(<PerformanceByStepPage.Body.Loading />);

		const loadingStateDiv = getByTestId('loadingState');

		expect(loadingStateDiv).not.toBeNull();
	});
});
