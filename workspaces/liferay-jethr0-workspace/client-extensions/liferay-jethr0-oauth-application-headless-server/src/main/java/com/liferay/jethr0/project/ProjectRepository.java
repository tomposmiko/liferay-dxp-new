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

package com.liferay.jethr0.project;

import com.liferay.jethr0.entity.repository.BaseEntityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Michael Hashimoto
 */
public class ProjectRepository extends BaseEntityRepository<Project> {

	public List<Project> getByState(Project.State state) {
		List<Project> projects = new ArrayList<>();

		for (Project project : get()) {
			if (!Objects.equals(project.getState(), state)) {
				continue;
			}

			projects.add(project);
		}

		return projects;
	}

	@Override
	public ProjectDALO getEntityDALO() {
		return _projectDALO;
	}

	@Autowired
	private ProjectDALO _projectDALO;

}