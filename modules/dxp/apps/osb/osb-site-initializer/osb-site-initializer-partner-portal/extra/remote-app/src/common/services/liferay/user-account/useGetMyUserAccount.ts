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

import {Liferay} from '..';
import useSWR from 'swr';

import LiferayAccountBrief from '../../../interfaces/liferayAccountBrief';
import Role from '../../../interfaces/role';
import {LiferayAPIs} from '../common/enums/apis';
import liferayFetcher from '../common/utils/fetcher';

interface Telephone {
	id: number;
	phoneNumber: string;
}

interface UserAccountContactInformation {
	telephones: Telephone[];
}

interface UserAccount {
	accountBriefs: LiferayAccountBrief[];
	emailAddress: string;
	familyName: string;
	givenName: string;
	id: number;
	roleBriefs: Role[];
	userAccountContactInformation: UserAccountContactInformation;
}

export default function useGetMyUserAccount(skip?: boolean) {
	return useSWR(
		skip
			? null
			: [
					`/o/${LiferayAPIs.HEADERLESS_ADMIN_USER}/my-user-account`,
					Liferay.authToken,
			  ],
		(url, token) => liferayFetcher<UserAccount>(url, token)
	);
}
