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

import {useMemo} from 'react';

import {LiferayPicklistName} from '../../../common/enums/liferayPicklistName';
import useGetListTypeDefinitions from '../../../common/services/liferay/list-type-definitions/useGetListTypeDefinitions';
import useGetMyUserAccount from '../../../common/services/liferay/user-account/useGetMyUserAccount';
import getEntriesByListTypeDefinitions from '../../../common/utils/getEntriesByListTypeDefinitions';

export default function useDynamicFieldEntries() {
	const {data: userAccount} = useGetMyUserAccount();
	const {data: listTypeDefinitions} = useGetListTypeDefinitions([
		LiferayPicklistName.MDF_REQUEST_STATUS,
	]);

	const companiesEntries = useMemo(
		() =>
			userAccount?.accountBriefs.map((accountBrief) => ({
				label: accountBrief.name,
				value: accountBrief.id,
			})) as React.OptionHTMLAttributes<HTMLOptionElement>[],
		[userAccount?.accountBriefs]
	);
	const userAccountRoles = useMemo(
		() =>
			userAccount?.roleBriefs.map((roleBrief) => ({
				label: roleBrief.name,
				value: roleBrief.id,
			})) as React.OptionHTMLAttributes<HTMLOptionElement>[],
		[userAccount?.roleBriefs]
	);

	const fieldEntries = useMemo(
		() => getEntriesByListTypeDefinitions(listTypeDefinitions?.items),
		[listTypeDefinitions?.items]
	);

	return {
		accountRoleEntries: (accountId?: number) =>
			userAccount?.accountBriefs.find(
				(accountBrief) => accountBrief.id === accountId
			)?.roleBriefs,
		companiesEntries,
		fieldEntries,
		roleEntries: userAccount?.roleBriefs,
		userAccountRoles,
	};
}
