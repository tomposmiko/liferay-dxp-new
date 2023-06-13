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

import React, {useState} from 'react';

const AppContext = React.createContext();

const AppContextProvider = ({children, ...props}) => {
	const [status, setStatus] = useState();
	const [title, setTitle] = useState(Liferay.Language.get('metrics'));

	const state = {
		...props,
		setStatus,
		setTitle,
		status,
		title
	};

	return <AppContext.Provider value={state}>{children}</AppContext.Provider>;
};

const AppStatus = {
	slaDeleted: 'sla-deleted',
	slaSaved: 'sla-saved',
	slaUpdated: 'sla-updated'
};

export {AppContext, AppContextProvider, AppStatus};
