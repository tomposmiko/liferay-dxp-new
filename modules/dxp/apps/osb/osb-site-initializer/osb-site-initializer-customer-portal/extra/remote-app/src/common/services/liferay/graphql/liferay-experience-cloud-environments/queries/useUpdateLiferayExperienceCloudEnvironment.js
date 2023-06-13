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

import {gql, useMutation} from '@apollo/client';

export const UPDATE_LIFERAY_EXPERIENCE_CLOUD_ENVIRONMENT = gql`
	mutation updateLiferayExperienceCloudEnvironment(
		$liferayExperienceCloudEnvironmentId: Long!
		$LiferayExperienceCloudEnvironment: InputC_LiferayExperienceCloudEnvironment!
	) {
		updateLiferayExperienceCloudEnvironment(
			liferayExperienceCloudEnvironmentId: $liferayExperienceCloudEnvironmentId
			input: $LiferayExperienceCloudEnvironment
		)
			@rest(
				method: "PUT"
				type: "C_LiferayExperienceCloudEnvironment"
				path: "/c/liferayexperiencecloudenvironments/{args.liferayExperienceCloudEnvironmentId}"
			) {
			liferayExperienceCloudEnvironmentId
			projectId
		}
	}
`;

export function useUpdateLiferayExperienceCloudEnvironment(
	variables,
	options = {displaySuccess: false}
) {
	return useMutation(
		UPDATE_LIFERAY_EXPERIENCE_CLOUD_ENVIRONMENT,
		{
			context: {
				displaySuccess: options.displaySuccess,
				type: 'liferay-rest',
			},
		},
		variables
	);
}
