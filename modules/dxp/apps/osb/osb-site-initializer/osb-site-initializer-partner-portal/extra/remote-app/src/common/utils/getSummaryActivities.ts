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

import MDFRequestActivity from '../interfaces/mdfRequestActivity';
import getTotalBudget from './getTotalBudget';
import getTotalMDFRequest from './getTotalMDFRequest';

interface DateActivities {
	endDates: Date[];
	startDates: Date[];
}

export default function getSummaryActivities(
	mdfRequestActivities: MDFRequestActivity[]
) {
	if (mdfRequestActivities.length) {
		const datesActivities = mdfRequestActivities.reduce<DateActivities>(
			(previousValue, currentValue) => {
				const endDateAccumulator = previousValue.endDates;
				const startDateAccumulator = previousValue.startDates;

				if (currentValue.endDate) {
					endDateAccumulator.push(currentValue.endDate);
				}

				if (currentValue.startDate) {
					startDateAccumulator.push(currentValue.startDate);
				}

				return {
					endDates: endDateAccumulator,
					startDates: startDateAccumulator,
				};
			},
			{endDates: [] as Date[], startDates: [] as Date[]}
		);

		const minDateActivity = datesActivities.startDates.reduce(
			(dateAccumulator, startDate) =>
				dateAccumulator < startDate ? dateAccumulator : startDate
		);

		const maxDateActivity = datesActivities.endDates.reduce(
			(dateAccumulator, endDate) =>
				dateAccumulator > endDate ? dateAccumulator : endDate
		);

		return {
			maxDateActivity,
			minDateActivity,
			totalCostOfExpense: getTotalBudget(mdfRequestActivities),
			totalMDFRequestAmount: getTotalMDFRequest(mdfRequestActivities),
		};
	}
}
