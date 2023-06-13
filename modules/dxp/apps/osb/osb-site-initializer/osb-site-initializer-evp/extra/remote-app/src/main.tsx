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

import {Root, createRoot} from 'react-dom/client';

import './style/index.css';

const NoRouteSelected = () => <div className="evp-app">No route selected</div>;

type EVPComponentType = {
	[key: string]: JSX.Element;
};

const EVPComponent: EVPComponentType = {
	'no-route-selected': <NoRouteSelected />,
};

class EVPRemoteAppComponent extends HTMLElement {
	public root: Root | undefined;

	connectedCallback() {
		type propertyType = {
			route: string;
		};

		const properties: propertyType = {
			route: this.getAttribute('route') || 'no-route-selected',
		};

		if (!this.root) {
			this.root = createRoot(this);

			this.root.render(EVPComponent[properties.route]);
		}
	}
}

const ELEMENT_NAME = 'liferay-remote-app-evp';

if (!customElements.get(ELEMENT_NAME)) {
	customElements.define(ELEMENT_NAME, EVPRemoteAppComponent);
}
