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

package com.liferay.jenkins.results.parser.test.clazz.group;

import com.liferay.jenkins.results.parser.JenkinsResultsParserUtil;
import com.liferay.jenkins.results.parser.PluginsGitWorkingDirectory;
import com.liferay.jenkins.results.parser.PortalTestClassJob;

import java.io.File;
import java.io.IOException;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author Michael Hashimoto
 */
public class PluginsBatchTestClassGroup extends BatchTestClassGroup {

	public static class PluginsBatchTestClass extends BaseTestClass {

		protected static PluginsBatchTestClass getInstance(
			String batchName, File pluginDir) {

			return new PluginsBatchTestClass(batchName, pluginDir);
		}

		protected PluginsBatchTestClass(String batchName, File file) {
			super(file);

			addTestMethod(batchName);
		}

	}

	protected PluginsBatchTestClassGroup(
		String batchName, PortalTestClassJob portalTestClassJob) {

		super(batchName, portalTestClassJob);

		Properties portalReleaseProperties =
			JenkinsResultsParserUtil.getProperties(
				new File(
					portalGitWorkingDirectory.getWorkingDirectory(),
					"release.properties"));

		try {
			_pluginsGitWorkingDirectory = new PluginsGitWorkingDirectory(
				portalGitWorkingDirectory.getUpstreamBranchName(),
				JenkinsResultsParserUtil.getProperty(
					portalReleaseProperties, "lp.plugins.dir"));

			excludesPathMatchers.addAll(
				getPathMatchers(
					getFirstPropertyValue("test.batch.plugin.names.excludes"),
					_pluginsGitWorkingDirectory.getWorkingDirectory()));

			includesPathMatchers.addAll(
				getPathMatchers(
					getFirstPropertyValue("test.batch.plugin.names.includes"),
					_pluginsGitWorkingDirectory.getWorkingDirectory()));

			setTestClasses();

			setAxisTestClassGroups();
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	protected void setTestClasses() {
		File workingDirectory =
			_pluginsGitWorkingDirectory.getWorkingDirectory();

		try {
			Files.walkFileTree(
				workingDirectory.toPath(),
				new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult preVisitDirectory(
							Path filePath, BasicFileAttributes attrs)
						throws IOException {

						if (_pathExcluded(filePath)) {
							return FileVisitResult.SKIP_SUBTREE;
						}

						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFile(
							Path filePath, BasicFileAttributes attrs)
						throws IOException {

						if (_pathIncluded(filePath) &&
							!_pathExcluded(filePath)) {

							File file = filePath.toFile();

							testClasses.add(
								PluginsBatchTestClass.getInstance(
									batchName, file.getParentFile()));
						}

						return FileVisitResult.CONTINUE;
					}

					private boolean _pathExcluded(Path path) {
						return _pathMatches(path, excludesPathMatchers);
					}

					private boolean _pathIncluded(Path path) {
						return _pathMatches(path, includesPathMatchers);
					}

					private boolean _pathMatches(
						Path path, List<PathMatcher> pathMatchers) {

						for (PathMatcher pathMatcher : pathMatchers) {
							if (pathMatcher.matches(path)) {
								return true;
							}
						}

						return false;
					}

				});
		}
		catch (IOException ioe) {
			throw new RuntimeException(
				"Unable to search for test file names in " +
					workingDirectory.getPath(),
				ioe);
		}

		Collections.sort(testClasses);
	}

	private final PluginsGitWorkingDirectory _pluginsGitWorkingDirectory;

}