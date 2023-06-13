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

package com.liferay.jenkins.results.parser.util;

/**
 * @author Michael Hashimoto
 */
public class TestPropertiesValues {

	public static final String REPOSITORY_DIR = TestPropertiesUtil.get(
		"repository.dir");

	public static final String REPOSITORY_HOSTNAME = TestPropertiesUtil.get(
		"repository.hostname");

	public static final String REPOSITORY_NAME = TestPropertiesUtil.get(
		"repository.name");

	public static final String REPOSITORY_UPSTREAM_BRANCH_NAME =
		TestPropertiesUtil.get("repository.upstream.branch.name");

	public static final String REPOSITORY_USERNAME = TestPropertiesUtil.get(
		"repository.username");

}