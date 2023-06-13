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

package com.liferay.source.formatter.check;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.source.formatter.check.util.GradleSourceUtil;

import java.io.File;

import java.util.List;

/**
 * @author Peter Shin
 */
public class GradleDependencyConfigurationCheck extends BaseFileCheck {

	@Override
	protected String doProcess(
		String fileName, String absolutePath, String content) {

		List<String> blocks = GradleSourceUtil.getDependenciesBlocks(content);

		for (String dependencies : blocks) {
			if (isModulesApp(absolutePath, false) &&
				_hasBNDFile(absolutePath) &&
				!GradleSourceUtil.isSpringBootExecutable(content)) {

				content = _formatDependencies(content, dependencies);
			}

			if (absolutePath.contains("/third-party/")) {
				content = _fixTransitive(content, dependencies);
			}

			if (isAttributeValue(_CHECK_API_DEPENDENCIES_KEY, absolutePath)) {
				content = _fixConfigurations(
					content, dependencies, "compile", "api");
			}

			if (isAttributeValue(
					_CHECK_RUNTIME_ONLY_DEPENDENCIES_KEY, absolutePath)) {

				content = _fixConfigurations(
					content, dependencies, "runtime", "runtimeOnly");
			}

			if (isAttributeValue(
					_CHECK_TEST_IMPLEMENTATION_DEPENDENCIES_KEY,
					absolutePath)) {

				content = _fixConfigurations(
					content, dependencies, "testCompile", "testImplementation");
			}

			if (isAttributeValue(
					_CHECK_TEST_INTEGRATION_IMPLEMENTATION_DEPENDENCIES_KEY,
					absolutePath)) {

				content = _fixConfigurations(
					content, dependencies, "testIntegrationCompile",
					"testIntegrationImplementation");
			}

			if (isAttributeValue(
					_CHECK_TEST_INTEGRATION_RUNTIME_ONLY_DEPENDENCIES_KEY,
					absolutePath)) {

				content = _fixConfigurations(
					content, dependencies, "testIntegrationRuntime",
					"testIntegrationRuntimeOnly");
			}

			if (isAttributeValue(
					_CHECK_TEST_RUNTIME_ONLY_DEPENDENCIES_KEY, absolutePath)) {

				content = _fixConfigurations(
					content, dependencies, "testRuntime", "testRuntimeOnly");
			}
		}

		return content;
	}

	private String _fixConfigurations(
		String content, String dependencies, String oldConfiguration,
		String newConfiguration) {

		int x = dependencies.indexOf("\n");
		int y = dependencies.lastIndexOf("\n");

		if (x == y) {
			return content;
		}

		dependencies = dependencies.substring(x, y + 1);

		for (String oldDependency : StringUtil.splitLines(dependencies)) {
			String newDependency = oldDependency;

			String configuration = GradleSourceUtil.getConfiguration(
				oldDependency);

			if (configuration.equals(oldConfiguration)) {
				newDependency = StringUtil.replaceFirst(
					oldDependency, oldConfiguration, newConfiguration);
			}

			content = StringUtil.replaceFirst(
				content, oldDependency, newDependency);
		}

		return content;
	}

	private String _fixTransitive(String content, String dependencies) {
		int x = dependencies.indexOf("\n");
		int y = dependencies.lastIndexOf("\n");

		if (x == y) {
			return content;
		}

		dependencies = dependencies.substring(x, y + 1);

		for (String oldDependency : StringUtil.splitLines(dependencies)) {
			String configuration = GradleSourceUtil.getConfiguration(
				oldDependency);
			String newDependency = oldDependency;

			if (configuration.equals("compileOnly") &&
				!oldDependency.contains("transitive: false")) {

				newDependency = oldDependency + ", transitive: false";

				content = StringUtil.replaceFirst(
					content, oldDependency, newDependency);
			}
		}

		return content;
	}

	private String _formatDependencies(String content, String dependencies) {
		int x = dependencies.indexOf("\n");
		int y = dependencies.lastIndexOf("\n");

		if (x == y) {
			return content;
		}

		dependencies = dependencies.substring(x, y + 1);

		for (String oldDependency : StringUtil.splitLines(dependencies)) {
			String configuration = GradleSourceUtil.getConfiguration(
				oldDependency);
			String newDependency = oldDependency;

			if (configuration.equals("compile")) {
				newDependency = StringUtil.replaceFirst(
					oldDependency, "compile", "compileOnly");

				content = StringUtil.replaceFirst(
					content, oldDependency, newDependency);
			}
			else if (configuration.equals("compileOnly")) {
				newDependency = StringUtil.removeSubstrings(
					oldDependency, "transitive: false, ", "transitive: true,");

				content = StringUtil.replaceFirst(
					content, oldDependency, newDependency);
			}
		}

		return content;
	}

	private boolean _hasBNDFile(String absolutePath) {
		if (!absolutePath.endsWith("/build.gradle")) {
			return false;
		}

		int pos = absolutePath.lastIndexOf(StringPool.SLASH);

		File file = new File(absolutePath.substring(0, pos + 1) + "bnd.bnd");

		return file.exists();
	}

	private static final String _CHECK_API_DEPENDENCIES_KEY =
		"checkAPIDependencies";

	private static final String _CHECK_RUNTIME_ONLY_DEPENDENCIES_KEY =
		"checkRuntimeOnlyDependencies";

	private static final String _CHECK_TEST_IMPLEMENTATION_DEPENDENCIES_KEY =
		"checkTestImplementationDependencies";

	private static final String
		_CHECK_TEST_INTEGRATION_IMPLEMENTATION_DEPENDENCIES_KEY =
			"checkTestIntegrationImplementationDependencies";

	private static final String
		_CHECK_TEST_INTEGRATION_RUNTIME_ONLY_DEPENDENCIES_KEY =
			"checkTestIntegrationRuntimeOnlyDependencies";

	private static final String _CHECK_TEST_RUNTIME_ONLY_DEPENDENCIES_KEY =
		"checkTestRuntimeOnlyDependencies";

}