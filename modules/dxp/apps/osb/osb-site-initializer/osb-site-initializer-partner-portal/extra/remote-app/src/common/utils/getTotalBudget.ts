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

export default function getTotalBudget(
	mdfRequestActivities: MDFRequestActivity[]
) {
	return mdfRequestActivities.reduce(
		(previousValue: number, currentValue: MDFRequestActivity) => {
			if (!currentValue.removed) {
				const sumBudgets = currentValue.budgets.reduce(
					(previousValue, currentValue) =>
						previousValue +
						((!currentValue.removed && currentValue.cost) || 0),
					0
				);

				return previousValue + sumBudgets;
			}

			return previousValue;
		},
		0
	);
}
