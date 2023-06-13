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

import {FormikHelpers} from 'formik';

import {PRMPageRoute} from '../../../common/enums/prmPageRoute';
import mdfRequestDTO from '../../../common/interfaces/dto/mdfRequestDTO';
import LiferayPicklist from '../../../common/interfaces/liferayPicklist';
import MDFRequest from '../../../common/interfaces/mdfRequest';
import {Liferay} from '../../../common/services/liferay';
import createMDFRequestActivities from '../../../common/services/liferay/object/activity/createMDFRequestActivities';
import updateMDFRequestActivities from '../../../common/services/liferay/object/activity/updateMDFRequestActivities';
import createMDFRequestActivityBudget from '../../../common/services/liferay/object/budgets/createMDFRequestActivityBudgets';
import updateMDFRequestActivityBudget from '../../../common/services/liferay/object/budgets/updateMDFRequestActivityBudgets';
import {ResourceName} from '../../../common/services/liferay/object/enum/resourceName';
import createMDFRequest from '../../../common/services/liferay/object/mdf-requests/createMDFRequest';
import updateMDFRequest from '../../../common/services/liferay/object/mdf-requests/updateMDFRequest';
import {Status} from '../../../common/utils/constants/status';
import getTotalMDFRequest from '../../../common/utils/getTotalMDFRequest';
import createMDFRequestActivitiesProxyAPI from './createMDFRequestActivitiesProxyAPI';
import createMDFRequestProxyAPI from './createMDFRequestProxyAPI';

export default async function submitForm(
	values: MDFRequest,
	formikHelpers: Omit<FormikHelpers<MDFRequest>, 'setFieldValue'>,
	siteURL: string,
	currentRequestStatus?: LiferayPicklist
) {
	formikHelpers.setSubmitting(true);

	if (currentRequestStatus) {
		values.mdfRequestStatus = currentRequestStatus;
	}

	const totalMDFRequestAmount = getTotalMDFRequest(values.activities);

	if (
		totalMDFRequestAmount >= 15000 &&
		values.mdfRequestStatus !== Status.DRAFT
	) {
		values.mdfRequestStatus = Status.MARKETING_DIRECTOR_REVIEW;
	}

	let dtoMDFRequest: mdfRequestDTO | undefined = undefined;

	if (
		Liferay.FeatureFlags['LPS-164528'] &&
		values.mdfRequestStatus !== Status.DRAFT
	) {
		dtoMDFRequest = await createMDFRequestProxyAPI(values);
	}
	else if (values.id) {
		dtoMDFRequest = await updateMDFRequest(
			ResourceName.MDF_REQUEST_DXP,
			values
		);
	}
	else {
		dtoMDFRequest = await createMDFRequest(
			ResourceName.MDF_REQUEST_DXP,
			values
		);
	}

	if (values?.activities?.length && dtoMDFRequest?.id) {
		const dtoMDFRequestActivities = await Promise.all(
			values?.activities?.map(async (activity) => {
				if (
					Liferay.FeatureFlags['LPS-164528'] &&
					values.mdfRequestStatus !== Status.DRAFT
				) {
					return await createMDFRequestActivitiesProxyAPI(
						activity,
						values.company,
						dtoMDFRequest?.id,
						dtoMDFRequest?.externalReferenceCodeSF
					);
				}

				if (activity.id) {
					return await updateMDFRequestActivities(
						ResourceName.ACTIVITY_DXP,
						activity,
						values.company,
						dtoMDFRequest?.id,
						dtoMDFRequest?.externalReferenceCodeSF
					);
				}

				return await createMDFRequestActivities(
					ResourceName.ACTIVITY_DXP,
					activity,
					values.company,
					dtoMDFRequest?.id,
					dtoMDFRequest?.externalReferenceCodeSF
				);
			})
		);

		if (dtoMDFRequestActivities?.length) {
			await Promise.all(
				values.activities.map(async (activity, index) => {
					const dtoActivity = dtoMDFRequestActivities[index];

					if (activity.budgets?.length && dtoActivity?.id) {
						activity.budgets?.map(async (budget) => {
							if (budget?.id) {
								return await updateMDFRequestActivityBudget(
									dtoActivity.id as number,
									budget
								);
							}

							return await createMDFRequestActivityBudget(
								dtoActivity.id as number,
								budget
							);
						});
					}
				})
			);
		}
	}

	if (values.id) {
		Liferay.Util.navigate(
			`${siteURL}/${PRMPageRoute.MDF_REQUESTS_LISTING}?edit-success=true`
		);
	}

	Liferay.Util.navigate(
		`${siteURL}/${PRMPageRoute.MDF_REQUESTS_LISTING}/?new-success=true`
	);
}
