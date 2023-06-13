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

package com.liferay.jenkins.results.parser;

import java.lang.reflect.Proxy;

/**
 * @author Michael Hashimoto
 */
public class BuildRunnerFactory {

	public static BuildRunner<?, ?> newBuildRunner(BuildData buildData) {
		String jobName = buildData.getJobName();

		BuildRunner<?, ?> buildRunner = null;

		if (jobName.equals("root-cause-analysis-tool")) {
			buildRunner = new RootCauseAnalysisToolTopLevelBuildRunner(
				(PortalTopLevelBuildData)buildData);
		}

		if (jobName.contains("-batch")) {
			buildRunner = new DefaultPortalBatchBuildRunner(
				(PortalBatchBuildData)buildData);
		}

		if (buildRunner == null) {
			throw new RuntimeException("Invalid build data " + buildData);
		}

		return (BuildRunner<?, ?>)Proxy.newProxyInstance(
			BuildRunner.class.getClassLoader(),
			new Class<?>[] {BuildRunner.class}, new MethodLogger(buildRunner));
	}

}