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

import {Root, createRoot} from 'react-dom/client';
import {SWRConfig} from 'swr';

import TestrayRouter from './TestrayRouter';
import AccountContextProvider from './context/AccountContext';
import ClayIconProvider from './context/ClayIconProvider';

import './styles/index.scss';
import ApplicationContextProvider from './context/ApplicationPropertiesContext';
import SWRCacheProvider from './services/SWRCacheProvider';
import fetcher from './services/fetcher';

class Testray extends HTMLElement {
	private root: Root | undefined;

	connectedCallback() {
		const properties = {
			jiraBaseURL: this.getAttribute('jiraBaseURL') || '',
		};

		if (!this.root) {
			this.root = createRoot(this);

			this.root.render(
				<SWRConfig
					value={{
						fetcher,
						provider: SWRCacheProvider,
						revalidateOnFocus: false,
					}}
				>
					<ApplicationContextProvider properties={properties}>
						<AccountContextProvider>
							<ClayIconProvider>
								<TestrayRouter />
							</ClayIconProvider>
						</AccountContextProvider>
					</ApplicationContextProvider>
				</SWRConfig>
			);
		}
	}
}

const ELEMENT_ID = 'liferay-remote-app-testray';

if (!customElements.get(ELEMENT_ID)) {
	customElements.define(ELEMENT_ID, Testray);
}
