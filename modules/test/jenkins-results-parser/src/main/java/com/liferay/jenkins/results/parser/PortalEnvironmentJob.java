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

import java.io.File;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael Hashimoto
 */
public class PortalEnvironmentJob
	extends BaseJob implements PortalTestClassJob {

	@Override
	public Set<String> getDistTypes() {
		return new HashSet<>();
	}

	@Override
	public PortalGitWorkingDirectory getPortalGitWorkingDirectory() {
		return GitWorkingDirectoryFactory.newPortalGitWorkingDirectory(
			getPortalBranchName());
	}

	protected PortalEnvironmentJob(
		String jobName, BuildProfile buildProfile, String portalBranchName) {

		super(jobName, buildProfile);

		_portalBranchName = portalBranchName;

		PortalGitWorkingDirectory portalGitWorkingDirectory =
			getPortalGitWorkingDirectory();

		jobPropertiesFiles.add(
			new File(
				portalGitWorkingDirectory.getWorkingDirectory(),
				"test.properties"));

		GitWorkingDirectory jenkinsGitWorkingDirectory =
			GitWorkingDirectoryFactory.newJenkinsGitWorkingDirectory();

		jobPropertiesFiles.add(
			new File(
				jenkinsGitWorkingDirectory.getWorkingDirectory(),
				"commands/dependencies/test-environment.properties"));

		readJobProperties();
	}

	protected String getPortalBranchName() {
		return _portalBranchName;
	}

	@Override
	protected Set<String> getRawBatchNames() {
		String environmentJobNames = JenkinsResultsParserUtil.getProperty(
			getJobProperties(), "environment.job.names", getPortalBranchName());

		return new HashSet<>(Arrays.asList(environmentJobNames.split(",")));
	}

	private final String _portalBranchName;

}