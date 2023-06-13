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

import {StatusBarOptions} from '../components/StatusBadge/StatusBadge';

export const DATA_COLORS = {
	'metrics.blocked': '#F8D72E',
	'metrics.failed': '#E73A45',
	'metrics.incomplete': '#E3E9EE',
	'metrics.passed': '#3CD587',
	'metrics.test-fix': '#59BBFC',
};

export enum Statuses {
	PASSED = 'PASSED',
	FAILED = 'FAILED',
	BLOCKED = 'BLOCKED',
	TEST_FIX = 'TEST FIX',
	INCOMPLETE = 'INCOMPLETE',
}

export const chartColors = {
	[Statuses.BLOCKED]: DATA_COLORS['metrics.blocked'],
	[Statuses.FAILED]: DATA_COLORS['metrics.failed'],
	[Statuses.INCOMPLETE]: DATA_COLORS['metrics.incomplete'],
	[Statuses.PASSED]: DATA_COLORS['metrics.passed'],
	[Statuses.TEST_FIX]: DATA_COLORS['metrics.test-fix'],
};

export const LABEL_GREATER_THAN_99 = '> 99';
export const LABEL_LESS_THAN_1 = '< 1';

export const PAGINATION_DELTA = [20, 50, 75, 100, 150];

export const PAGINATION = {
	delta: PAGINATION_DELTA,
	ellipsisBuffer: 3,
};

export enum TEST_STATUS {
	Blocked = 4,
	'Did Not Run' = 6,
	Failed = 3,
	'In Progress' = 1,
	'Passed' = 2,
	'Test Fix' = 7,
	'Untested' = 0,
}

export const TEST_STATUS_LABEL: any = {
	0: 'Untested',
	1: 'In Progress',
	2: 'Passed',
	3: 'Failed',
	4: 'Blocked',
	6: 'Did Not Run',
	7: 'Test Fix',
};

const getStatusLabel = (status: number): StatusBarOptions =>
	String(
		(TEST_STATUS_LABEL as any)[status]
	)?.toLowerCase() as StatusBarOptions;

export enum SUB_TASK_STATUS {
	'ABANDONED' = 2,
	'COMPLETE' = 3,
	'IN_ANALYSIS' = 1,
	'OPEN' = 4,
}

export const SUBTASK_STATUS = {
	1: {color: 'label-warning', label: 'IN ANALYSIS'},
	2: {color: 'label-secondary', label: 'ABANDONED'},
	3: {color: 'label-primary', label: 'COMPLETE'},
	4: {color: 'label-secondary', label: 'OPEN'},
};

export const BUILD_STATUS = {
	0: {color: 'label-warning', label: 'IN ANALYSIS'},
	2: {color: 'label-secondary', label: 'OPEN'},
};

export {getStatusLabel};
