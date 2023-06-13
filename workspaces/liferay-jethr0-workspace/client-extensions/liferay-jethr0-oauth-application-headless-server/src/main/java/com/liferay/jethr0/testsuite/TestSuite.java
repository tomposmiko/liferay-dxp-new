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

package com.liferay.jethr0.testsuite;

import com.liferay.jethr0.entity.Entity;
import com.liferay.jethr0.project.Project;

import java.util.List;

/**
 * @author Michael Hashimoto
 */
public interface TestSuite extends Entity {

	public void addProject(Project project);

	public void addProjects(List<Project> projects);

	public String getName();

	public List<Project> getProjects();

	public void removeProject(Project project);

	public void removeProjects(List<Project> projects);

	public void setName(String name);

}