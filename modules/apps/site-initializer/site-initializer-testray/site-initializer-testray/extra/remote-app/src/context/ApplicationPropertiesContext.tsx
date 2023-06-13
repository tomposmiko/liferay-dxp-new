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

import React, {createContext} from 'react';

type DefaultProperties = {jiraBaseURL: string};

const defaultProperties = {
	jiraBaseURL: '',
};

type ApplicationContextProviderProps = {
	children: JSX.Element;
	properties: DefaultProperties;
};

export const ApplicationPropertiesContext = createContext<DefaultProperties>(
	defaultProperties
);

const ApplicationContextProvider: React.FC<ApplicationContextProviderProps> = ({
	children,
	properties,
}) => (
	<ApplicationPropertiesContext.Provider value={properties}>
		{children}
	</ApplicationPropertiesContext.Provider>
);

export default ApplicationContextProvider;
