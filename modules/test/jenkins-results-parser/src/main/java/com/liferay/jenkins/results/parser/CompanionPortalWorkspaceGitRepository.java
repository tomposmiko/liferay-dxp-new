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
import java.io.IOException;

import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public class CompanionPortalWorkspaceGitRepository
	extends BasePortalWorkspaceGitRepository {

	public static final String TYPE = "portal.companion";

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public void setUp() {
		super.setUp();

		String parentUpstreamBranchName =
			_parentWorkspaceGitRepository.getUpstreamBranchName();

		if (parentUpstreamBranchName.contains("-private")) {
			try {
				JenkinsResultsParserUtil.write(
					new File(
						_parentWorkspaceGitRepository.getDirectory(),
						"git-commit-portal"),
					getBranchSHA());
			}
			catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}

			try {
				AntUtil.callTarget(
					_parentWorkspaceGitRepository.getDirectory(),
					"build-working-dir.xml", "prepare-working-dir");
			}
			catch (AntException ae) {
				throw new RuntimeException(ae);
			}

			return;
		}

		File modulesPrivateDir = new File(getDirectory(), "modules/private");

		if (!modulesPrivateDir.exists()) {
			return;
		}

		File parentModulesPrivateDir = new File(
			_parentWorkspaceGitRepository.getDirectory(), "modules/private");

		try {
			JenkinsResultsParserUtil.copy(
				modulesPrivateDir, parentModulesPrivateDir);
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	protected CompanionPortalWorkspaceGitRepository(JSONObject jsonObject) {
		super(jsonObject);

		BuildDatabase buildDatabase = BuildDatabaseUtil.getBuildDatabase();

		_parentWorkspaceGitRepository = buildDatabase.getWorkspaceGitRepository(
			PrimaryPortalWorkspaceGitRepository.TYPE);
	}

	protected CompanionPortalWorkspaceGitRepository(
		PullRequest pullRequest, String upstreamBranchName,
		WorkspaceGitRepository parentWorkspaceGitRepository) {

		super(pullRequest, upstreamBranchName);

		_parentWorkspaceGitRepository = parentWorkspaceGitRepository;
	}

	protected CompanionPortalWorkspaceGitRepository(
		RemoteGitRef remoteGitRef, String upstreamBranchName,
		WorkspaceGitRepository parentWorkspaceGitRepository) {

		super(remoteGitRef, upstreamBranchName);

		_parentWorkspaceGitRepository = parentWorkspaceGitRepository;
	}

	private final WorkspaceGitRepository _parentWorkspaceGitRepository;

}