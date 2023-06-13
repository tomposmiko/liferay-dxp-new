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

import {useContext} from 'react';
import {Outlet, useParams} from 'react-router-dom';
import {KeyedMutator} from 'swr';

import {TestrayContext} from '../../../context/TestrayContext';
import {useFetch} from '../../../hooks/useFetch';
import {Liferay} from '../../../services/liferay';
import {UserAccount, liferayUserAccountsImpl} from '../../../services/rest';

const UserOutlet = () => {
	const {userId} = useParams();

	const [{myUserAccount}, , mutateMyUserAccount] = useContext(TestrayContext);

	const {data, mutate} = useFetch(
		liferayUserAccountsImpl.getResource(userId as string),
		{
			swrConfig: {shouldFetch: userId},
		}
	);

	const context = {
		mutateUser: userId
			? userId === Liferay.ThemeDisplay.getUserId()
				? (response: KeyedMutator<UserAccount>) => {
						(mutateMyUserAccount as any)(response);
						mutate(response);
				  }
				: mutate
			: mutateMyUserAccount,
		userAccount: userId ? data : myUserAccount,
	};

	if (!context.userAccount) {
		return null;
	}

	return <Outlet context={context} />;
};
export default UserOutlet;
