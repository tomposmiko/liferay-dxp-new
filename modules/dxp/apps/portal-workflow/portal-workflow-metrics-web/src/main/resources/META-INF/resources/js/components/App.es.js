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

import React from 'react';
import {Route, HashRouter as Router, Switch} from 'react-router-dom';

import HeaderController from '../shared/components/header-controller/HeaderController.es';
import {withParams} from '../shared/components/router/routerUtil.es';
import {AppContextProvider} from './AppContext.es';
import InstanceListPage from './instance-list-page/InstanceListPage.es';
import PerformanceByAssigneePage from './performance-by-assignee-page/PerformanceByAssigneePage.es';
import PerformanceByStepPage from './performance-by-step-page/PerformanceByStepPage.es';
import ProcessListPage from './process-list-page/ProcessListPage.es';
import ProcessMetrics from './process-metrics/ProcessMetrics.es';
import SLAForm from './sla/SLAForm.es';
import SLAListCard from './sla/SLAListCard.es';
import WorkloadByAssigneePage from './workload-by-assignee-page/WorkloadByAssigneePage.es';

const App = props => {
	return (
		<Router>
			<AppContextProvider {...props}>
				<HeaderController basePath="/processes" />

				<div className="portal-workflow-metrics-app">
					<Switch>
						<Route
							exact
							path="/"
							render={withParams(ProcessListPage)}
						/>

						<Route
							path="/processes/:pageSize/:page/:sort"
							render={withParams(ProcessListPage)}
						/>

						<Route
							path="/metrics/:processId"
							render={withParams(ProcessMetrics)}
						/>

						<Route
							path="/instance/:processId/:pageSize/:page"
							render={withParams(InstanceListPage)}
						/>

						<Route
							exact
							path="/slas/:processId/:pageSize/:page"
							render={withParams(SLAListCard)}
						/>

						<Route
							exact
							path="/sla/new/:processId"
							render={withParams(SLAForm)}
						/>

						<Route
							exact
							path="/sla/edit/:processId/:id"
							render={withParams(SLAForm)}
						/>

						<Route
							exact
							path="/performance/step/:processId/:pageSize/:page/:sort"
							render={withParams(PerformanceByStepPage)}
						/>

						<Route
							exact
							path="/workload/assignee/:processId/:pageSize/:page/:sort"
							render={withParams(WorkloadByAssigneePage)}
						/>

						<Route
							exact
							path="/performance/assignee/:processId/:pageSize/:page/:sort"
							render={withParams(PerformanceByAssigneePage)}
						/>
					</Switch>
				</div>
			</AppContextProvider>
		</Router>
	);
};

export default App;
