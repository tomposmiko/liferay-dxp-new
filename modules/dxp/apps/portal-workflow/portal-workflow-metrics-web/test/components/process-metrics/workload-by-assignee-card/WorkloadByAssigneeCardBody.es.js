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

import {AppContext} from '../../../../src/main/resources/META-INF/resources/js/components/AppContext.es';
import WorkloadByAssigneeCard from '../../../../src/main/resources/META-INF/resources/js/components/process-metrics/workload-by-assignee-card/WorkloadByAssigneeCard.es';
import {MockRouter} from '../../../mock/MockRouter.es';

const items = [
	{
		name: 'User 1',
		onTimeTaskCount: 10,
		overdueTaskCount: 5,
		taskCount: 15
	},
	{
		name: 'User 2',
		onTimeTaskCount: 3,
		overdueTaskCount: 7,
		taskCount: 10
	}
];
const data = {items, totalCount: 2};

const wrapper = ({children}) => (
	<AppContext.Provider value={{defaultDelta: 20}}>
		<MockRouter>{children}</MockRouter>
	</AppContext.Provider>
);

describe('The workload by assignee body should', () => {
	let getAllByTestId, getByTestId;

	afterEach(cleanup);

	describe('be rendered with overdue tab active', () => {
		beforeEach(() => {
			const renderResult = render(
				<WorkloadByAssigneeCard.Body
					currentTab="onTime"
					data={data}
					processId={12345}
					processStepKey="allSteps"
				/>,
				{wrapper}
			);
			getAllByTestId = renderResult.getAllByTestId;
		});

		test('Be rendered with "User 1" and "User 2" items', async () => {
			const assigneeNames = await waitForElement(() =>
				getAllByTestId('assigneeName')
			);

			expect(assigneeNames[0].innerHTML).toBe('User 1');
			expect(assigneeNames[1].innerHTML).toBe('User 2');
		});

		test('Be rendered with "View All Steps" button and total "(2)"', async () => {
			const viewAllAssignees = await waitForElement(() =>
				getAllByTestId('viewAllAssignees')
			);

			expect(viewAllAssignees[0].innerHTML).toBe(
				'view-all-assignees (2)'
			);
			expect(
				viewAllAssignees[0].parentNode.getAttribute('href')
			).not.toContain('filters.taskKeys%5B0%5D=allSteps');
		});
	});

	describe('be rendered with total tab active', () => {
		beforeEach(() => {
			const renderResult = render(
				<WorkloadByAssigneeCard.Body
					currentTab="total"
					data={{items: [items[0]], totalCount: 1}}
					processId={12345}
					processStepKey="review"
				/>,
				{wrapper}
			);
			getByTestId = renderResult.getByTestId;
		});

		test('and with "User 1" item', async () => {
			const assigneeName = await waitForElement(() =>
				getByTestId('assigneeName')
			);

			expect(assigneeName.innerHTML).toBe('User 1');
		});

		test('and with "View All Steps" button and total "(1)"', async () => {
			const viewAllAssignees = await waitForElement(() =>
				getByTestId('viewAllAssignees')
			);

			expect(viewAllAssignees.innerHTML).toBe('view-all-assignees (1)');
			expect(viewAllAssignees.parentNode.getAttribute('href')).toContain(
				'filters.taskKeys%5B0%5D=review'
			);
		});
	});

	describe('be rendered with onTime tab active', () => {
		beforeEach(() => {
			const renderResult = render(
				<WorkloadByAssigneeCard.Body
					currentTab="onTime"
					data={{items: [items[1]], totalCount: 1}}
					processId={12345}
					processStepKey="update"
				/>,
				{wrapper}
			);
			getByTestId = renderResult.getByTestId;
		});

		test('and with "User 1" item', async () => {
			const assigneeName = await waitForElement(() =>
				getByTestId('assigneeName')
			);

			expect(assigneeName.innerHTML).toBe('User 2');
		});

		test('and with "View All Steps" button and total "(1)"', async () => {
			const viewAllAssignees = await waitForElement(() =>
				getByTestId('viewAllAssignees')
			);

			expect(viewAllAssignees.innerHTML).toBe('view-all-assignees (1)');
			expect(viewAllAssignees.parentNode.getAttribute('href')).toContain(
				'filters.taskKeys%5B0%5D=update'
			);
		});
	});

	describe('be rendered with overdue tab active and empty state', () => {
		afterAll(cleanup);

		beforeAll(() => {
			const renderResult = render(
				<WorkloadByAssigneeCard.Body
					currentTab="overdue"
					data={{items: [], totalCount: 0}}
					processId={12345}
				/>,
				{wrapper}
			);

			getByTestId = renderResult.getByTestId;
		});

		test('Be rendered with a empty state', async () => {
			const emptyStateDiv = getByTestId('emptyState');
			expect(emptyStateDiv.children[0].children[0].innerHTML).toBe(
				'there-are-no-assigned-items-overdue-at-the-moment'
			);
		});
	});

	describe('be rendered with total tab active and empty state', () => {
		afterAll(cleanup);

		beforeAll(() => {
			const renderResult = render(
				<WorkloadByAssigneeCard.Body
					currentTab="total"
					data={{items: [], totalCount: 0}}
					processId={12345}
				/>,
				{wrapper}
			);

			getByTestId = renderResult.getByTestId;
		});

		test('Be rendered with a empty state', async () => {
			const emptyStateDiv = getByTestId('emptyState');
			expect(emptyStateDiv.children[0].children[0].innerHTML).toBe(
				'there-are-no-items-assigned-to-users-at-the-moment'
			);
		});
	});

	describe('be rendered with onTime tab active and empty state', () => {
		afterAll(cleanup);

		beforeAll(() => {
			const renderResult = render(
				<WorkloadByAssigneeCard.Body
					currentTab="onTime"
					data={{items: [], totalCount: 0}}
					processId={12345}
				/>,
				{wrapper}
			);

			getByTestId = renderResult.getByTestId;
		});

		test('Be rendered with a empty state', async () => {
			const emptyStateDiv = getByTestId('emptyState');
			expect(emptyStateDiv.children[0].children[0].innerHTML).toBe(
				'there-are-no-assigned-items-on-time-at-the-moment'
			);
		});
	});
});
