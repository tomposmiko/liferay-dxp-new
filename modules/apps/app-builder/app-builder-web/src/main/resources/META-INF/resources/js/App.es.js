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

import {ClayModalProvider} from '@clayui/modal';
import classNames from 'classnames';
import React, {useContext} from 'react';
import {DndProvider} from 'react-dnd';
import {HTML5Backend} from 'react-dnd-html5-backend';
import {HashRouter as Router, Route, Switch} from 'react-router-dom';

import {AppContext, AppContextProvider} from './AppContext.es';
import NavigationBar from './components/navigation-bar/NavigationBar.es';
import ListCustomObjects from './pages/custom-object/ListCustomObjects.es';
import ListNativeObjects from './pages/native-object/ListNativeObjects.es';
import ViewObject from './pages/object/ViewObject.es';

export const AppNavigationBar = () => {
	const {showNativeObjectsTab} = useContext(AppContext);

	if (!showNativeObjectsTab) {
		return null;
	}

	return (
		<NavigationBar
			tabs={[
				{
					active: true,
					exact: true,
					label: Liferay.Language.get('custom'),
					path: () => '/',
				},
				{
					label: Liferay.Language.get('native'),
					path: () => '/native-objects',
				},
			]}
		/>
	);
};

export default (props) => (
	<DndProvider backend={HTML5Backend}>
		<AppContextProvider {...props}>
			<ClayModalProvider>
				<Router>
					<div
						className={classNames('custom-object-app', {
							'publications-enabled': document.querySelector(
								'.change-tracking-indicator'
							),
						})}
					>
						<Switch>
							<Route
								component={ListCustomObjects}
								exact
								path="/"
							/>

							<Route
								component={ListNativeObjects}
								path="/native-objects"
							/>

							<Route
								component={ViewObject}
								path="/:objectType/:dataDefinitionId(\d+)"
							/>
						</Switch>
					</div>
				</Router>
			</ClayModalProvider>
		</AppContextProvider>
	</DndProvider>
);
