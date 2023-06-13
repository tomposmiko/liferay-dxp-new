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

package com.liferay.jethr0.gitbranch.repository;

import com.liferay.jethr0.entity.repository.BaseEntityRepository;
import com.liferay.jethr0.gitbranch.GitBranch;
import com.liferay.jethr0.gitbranch.dalo.GitBranchDALO;
import com.liferay.jethr0.project.Project;
import com.liferay.jethr0.project.dalo.ProjectsToGitBranchesDALO;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Hashimoto
 */
@Configuration
public class GitBranchRepository extends BaseEntityRepository<GitBranch> {

	public Set<GitBranch> getAll(Project project) {
		Set<GitBranch> projectGitBranches = new HashSet<>();

		Set<Long> gitBranchIds = _projectsToGitBranchesDALO.getChildEntityIds(
			project);

		for (GitBranch gitBranch : getAll()) {
			if (!gitBranchIds.contains(gitBranch.getId())) {
				continue;
			}

			gitBranch.addProject(project);

			project.addGitBranch(gitBranch);

			projectGitBranches.add(gitBranch);
		}

		return projectGitBranches;
	}

	@Override
	public GitBranchDALO getEntityDALO() {
		return _gitBranchDALO;
	}

	@Autowired
	private GitBranchDALO _gitBranchDALO;

	@Autowired
	private ProjectsToGitBranchesDALO _projectsToGitBranchesDALO;

}