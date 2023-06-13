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

import com.google.common.collect.Lists;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Michael Hashimoto
 */
public abstract class BatchTestClassGroup extends BaseTestClassGroup {

	public int getAxisCount() {
		return axisTestClassGroups.size();
	}

	public AxisTestClassGroup getAxisTestClassGroup(int axisId) {
		return axisTestClassGroups.get(axisId);
	}

	public String getBatchName() {
		return batchName;
	}

	public PortalGitWorkingDirectory getPortalGitWorkingDirectory() {
		return portalGitWorkingDirectory;
	}

	public Properties getPortalTestProperties() {
		return portalTestProperties;
	}

	protected BatchTestClassGroup(
		String batchName, PortalGitWorkingDirectory portalGitWorkingDirectory,
		String testSuiteName) {

		this.batchName = batchName;
		this.portalGitWorkingDirectory = portalGitWorkingDirectory;
		this.testSuiteName = testSuiteName;

		portalTestProperties = JenkinsResultsParserUtil.getProperties(
			new File(
				this.portalGitWorkingDirectory.getWorkingDirectory(),
				"test.properties"));

		_setAutoBalanceTestFiles();
		_setTestRelevantChanges();
	}

	protected int getAxisMaxSize() {
		String axisMaxSize = _getAxisMaxSizePropertyValue();

		if (axisMaxSize != null) {
			return Integer.parseInt(axisMaxSize);
		}

		return _DEFAULT_AXIS_MAX_SIZE;
	}

	protected String getFirstMatchingPropertyName(
		String basePropertyName, Properties properties) {

		return getFirstMatchingPropertyName(basePropertyName, properties, null);
	}

	protected String getFirstMatchingPropertyName(
		String basePropertyName, Properties properties, String testSuiteName) {

		for (String propertyName : properties.stringPropertyNames()) {
			if (!propertyName.startsWith(basePropertyName)) {
				continue;
			}

			Matcher matcher = _propertyNamePattern.matcher(propertyName);

			if (matcher.find()) {
				String batchNameRegex = matcher.group("batchName");

				batchNameRegex = batchNameRegex.replace("*", ".+");

				if (!batchName.matches(batchNameRegex)) {
					continue;
				}

				String targetTestSuiteName = matcher.group("testSuiteName");

				if (Objects.equals(testSuiteName, targetTestSuiteName)) {
					return propertyName;
				}

				continue;
			}
		}

		return null;
	}

	protected String getFirstPropertyValue(String basePropertyName) {
		List<String> propertyNames = new ArrayList<>();

		if (testSuiteName != null) {
			propertyNames.add(
				JenkinsResultsParserUtil.combine(
					basePropertyName, "[", batchName, "][", testSuiteName,
					"]"));

			propertyNames.add(
				getFirstMatchingPropertyName(
					basePropertyName, portalTestProperties, testSuiteName));

			propertyNames.add(
				JenkinsResultsParserUtil.combine(
					basePropertyName, "[", testSuiteName, "]"));
		}

		propertyNames.add(
			JenkinsResultsParserUtil.combine(
				basePropertyName, "[", batchName, "]"));

		propertyNames.add(
			getFirstMatchingPropertyName(
				basePropertyName, portalTestProperties));

		propertyNames.add(basePropertyName);

		for (String propertyName : propertyNames) {
			if (propertyName == null) {
				continue;
			}

			if (portalTestProperties.containsKey(propertyName)) {
				String propertyValue = JenkinsResultsParserUtil.getProperty(
					portalTestProperties, propertyName);

				if ((propertyValue != null) && !propertyValue.isEmpty()) {
					return propertyValue;
				}
			}
		}

		return null;
	}

	protected void setAxisTestClassGroups() {
		int testClassFileCount = testClassFiles.size();

		if (testClassFileCount == 0) {
			if (includeAutoBalanceTests && !autoBalanceTestFiles.isEmpty()) {
				int id = 0;

				AxisTestClassGroup axisTestClassGroup = new AxisTestClassGroup(
					this, id);

				axisTestClassGroups.put(id, axisTestClassGroup);

				for (File autoBalanceTestFile : autoBalanceTestFiles) {
					axisTestClassGroup.addTestClassFile(autoBalanceTestFile);
				}
			}

			return;
		}

		int axisMaxSize = getAxisMaxSize();

		int axisCount = (int)Math.ceil(
			(double)testClassFileCount / axisMaxSize);

		int axisSize = (int)Math.ceil((double)testClassFileCount / axisCount);

		int id = 0;

		for (List<File> axisTestClassFiles :
				Lists.partition(testClassFiles, axisSize)) {

			AxisTestClassGroup axisTestClassGroup = new AxisTestClassGroup(
				this, id);

			axisTestClassGroups.put(id, axisTestClassGroup);

			for (File axisTestClassFile : axisTestClassFiles) {
				axisTestClassGroup.addTestClassFile(axisTestClassFile);
			}

			if (includeAutoBalanceTests) {
				for (File autoBalanceTestFile : autoBalanceTestFiles) {
					axisTestClassGroup.addTestClassFile(autoBalanceTestFile);
				}
			}

			id++;
		}
	}

	protected List<File> autoBalanceTestFiles = new ArrayList<>();
	protected final Map<Integer, AxisTestClassGroup> axisTestClassGroups =
		new HashMap<>();
	protected final String batchName;
	protected boolean includeAutoBalanceTests;
	protected final PortalGitWorkingDirectory portalGitWorkingDirectory;
	protected final Properties portalTestProperties;
	protected boolean testRelevantChanges;
	protected final String testSuiteName;

	private String _getAxisMaxSizePropertyValue() {
		return getFirstPropertyValue("test.batch.axis.max.size");
	}

	private void _setAutoBalanceTestFiles() {
		String propertyName = "test.class.names.auto.balance";

		String autoBalanceTestNames = getFirstPropertyValue(propertyName);

		if ((autoBalanceTestNames != null) &&
			!autoBalanceTestNames.equals("")) {

			for (String autoBalanceTestName : autoBalanceTestNames.split(",")) {
				autoBalanceTestFiles.add(new File(autoBalanceTestName));
			}
		}
	}

	private void _setTestRelevantChanges() {
		String propertyValue = getFirstPropertyValue("test.relevant.changes");

		if (propertyValue != null) {
			testRelevantChanges = Boolean.parseBoolean(propertyValue);

			return;
		}

		testRelevantChanges = _DEFAULT_TEST_RELEVANT_CHANGES;
	}

	private static final int _DEFAULT_AXIS_MAX_SIZE = 5000;

	private static final boolean _DEFAULT_TEST_RELEVANT_CHANGES = false;

	private final Pattern _propertyNamePattern = Pattern.compile(
		"[^\\]]+\\[(?<batchName>[^\\]]+)\\](\\[(?<testSuiteName>[^\\]]+)\\])?");

}