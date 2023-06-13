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
import com.liferay.jenkins.results.parser.PortalTestClassJob;
import com.liferay.jenkins.results.parser.test.clazz.TestClassFactory;

import java.io.File;
import java.io.IOException;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael Hashimoto
 */
public class TCKJunitBatchTestClassGroup extends BatchTestClassGroup {

	@Override
	public int getAxisCount() {
		if (!isStableTestSuiteBatch() && testRelevantIntegrationUnitOnly) {
			return 0;
		}

		return super.getAxisCount();
	}

	protected TCKJunitBatchTestClassGroup(
		String batchName, PortalTestClassJob portalTestClassJob) {

		super(batchName, portalTestClassJob);

		_tckHomeDir = _getTCKHomeDir();

		excludesPathMatchers.addAll(
			getPathMatchers(
				getFirstPropertyValue("test.batch.class.names.excludes"),
				_tckHomeDir));

		includesPathMatchers.addAll(
			getPathMatchers(
				getFirstPropertyValue("test.batch.class.names.includes"),
				_tckHomeDir));

		if (includeStableTestSuite && isStableTestSuiteBatch()) {
			excludesPathMatchers.addAll(
				getPathMatchers(
					getFirstPropertyValue(
						"test.batch.class.names.excludes", batchName,
						NAME_STABLE_TEST_SUITE),
					_tckHomeDir));

			includesPathMatchers.addAll(
				getPathMatchers(
					getFirstPropertyValue(
						"test.batch.class.names.includes", batchName,
						NAME_STABLE_TEST_SUITE),
					_tckHomeDir));
		}

		setTestClasses();

		setAxisTestClassGroups();

		setSegmentTestClassGroups();
	}

	protected void setTestClasses() {
		final List<File> tckTestFiles = new ArrayList<>();

		try {
			Files.walkFileTree(
				_tckHomeDir.toPath(),
				new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(
							Path filePath,
							BasicFileAttributes basicFileAttributes)
						throws IOException {

						if (JenkinsResultsParserUtil.isFileExcluded(
								excludesPathMatchers, filePath.toFile())) {

							return FileVisitResult.SKIP_SUBTREE;
						}

						if (JenkinsResultsParserUtil.isFileIncluded(
								excludesPathMatchers, includesPathMatchers,
								filePath.toFile())) {

							tckTestFiles.add(filePath.toFile());
						}

						return FileVisitResult.CONTINUE;
					}

				});
		}
		catch (IOException ioException) {
			throw new RuntimeException(
				"Unable to search for test file names in " +
					_tckHomeDir.getPath(),
				ioException);
		}

		for (File tckTestFile : tckTestFiles) {
			testClasses.add(TestClassFactory.newTestClass(this, tckTestFile));
		}

		Collections.sort(testClasses);
	}

	private File _getTCKHomeDir() {
		String tckHome = JenkinsResultsParserUtil.getProperty(
			jobProperties, "tck.home");

		if ((tckHome == null) || tckHome.isEmpty()) {
			File jenkinsDir = new File(
				"/opt/dev/projects/github/liferay-jenkins-ee");

			if (jenkinsDir.exists()) {
				tckHome = JenkinsResultsParserUtil.getProperty(
					JenkinsResultsParserUtil.getProperties(
						new File(
							jenkinsDir,
							"commands/dependencies/test.properties")),
					"tck.home");
			}
		}

		if ((tckHome == null) || tckHome.isEmpty()) {
			throw new RuntimeException("Unable find the TCK home");
		}

		return new File(tckHome);
	}

	private final File _tckHomeDir;

}