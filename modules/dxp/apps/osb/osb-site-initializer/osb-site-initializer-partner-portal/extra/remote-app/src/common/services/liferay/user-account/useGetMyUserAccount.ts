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

import {LiferayAPIs} from '../common/enums/apis';
import liferayFetcher from '../common/utils/fetcher';

interface AccountBrief {
	id: number;
	name: string;
}

interface RoleBrief {
	id: number;
	name: string;
}

interface Telephone {
	id: number;
	phoneNumber: string;
}

interface UserAccountContactInformation {
	telephones: Telephone[];
}

interface UserAccount {
	accountBriefs: AccountBrief[];
	emailAddress: string;
	familyName: string;
	givenName: string;
	id: number;
	roleBriefs: RoleBrief[];
	userAccountContactInformation: UserAccountContactInformation;
}

export default function useGetMyUserAccount() {
	return useSWR(
		[
			`/o/${LiferayAPIs.HEADERLESS_ADMIN_USER}/my-user-account`,
			Liferay.authToken,
		],
		(url, token) => liferayFetcher<UserAccount>(url, token)
	);
}
