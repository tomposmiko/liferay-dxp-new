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

import {useEffect, useState} from 'react';

import {INITIAL_FILTER} from '../utils/constants/initialFilter';
import getActivityPeriodFilterTerm from '../utils/getActivityPeriodFilterTerm';
import getSearchFilterTerm from '../utils/getSearchFilterTerm';

export default function useFilters() {
	const [filters, setFilters] = useState(INITIAL_FILTER);

	const [filtersTerm, setFilterTerm] = useState('');

	const onFilter = (newFilters: Partial<typeof INITIAL_FILTER>) =>
		setFilters((previousFilters) => ({...previousFilters, ...newFilters}));

	useEffect(() => {
		let initialFilter = '';
		let hasFilter = false;

		if (
			filters.activityPeriod.dates.endDate ||
			filters.activityPeriod.dates.startDate
		) {
			hasFilter = true;
			initialFilter = getActivityPeriodFilterTerm(
				initialFilter,
				filters.activityPeriod
			);
		}

		if (filters.searchTerm) {
			initialFilter = getSearchFilterTerm(
				initialFilter,
				filters.searchTerm
			);
		}

		onFilter({
			hasValue: hasFilter,
		});

		setFilterTerm(initialFilter);
	}, [filters.activityPeriod, filters.searchTerm, setFilters]);

	return {filters, filtersTerm, onFilter};
}
