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
import com.liferay.jenkins.results.parser.Job;
import com.liferay.jenkins.results.parser.PortalTestClassJob;
import com.liferay.jenkins.results.parser.job.property.JobProperty;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

/**
 * @author Yi-Chen Tsai
 */
public class ModulesJUnitBatchTestClassGroup extends JUnitBatchTestClassGroup {

	protected ModulesJUnitBatchTestClassGroup(
		JSONObject jsonObject, PortalTestClassJob portalTestClassJob) {

		super(jsonObject, portalTestClassJob);
	}

	protected ModulesJUnitBatchTestClassGroup(
		String batchName, PortalTestClassJob portalTestClassJob) {

		super(batchName, portalTestClassJob);
	}

	@Override
	protected List<JobProperty> getDefaultExcludesJobProperties() {
		List<JobProperty> excludesJobProperties = new ArrayList<>();

		excludesJobProperties.addAll(super.getDefaultExcludesJobProperties());

		for (File modulePullSubrepoDir :
				portalGitWorkingDirectory.getModulePullSubrepoDirs()) {

			excludesJobProperties.add(
				getJobProperty(
					"test.batch.class.names.excludes.subrepo",
					modulePullSubrepoDir, JobProperty.Type.EXCLUDE_GLOB));
		}

		return excludesJobProperties;
	}

	@Override
	protected List<JobProperty> getReleaseIncludesJobProperties() {
		List<JobProperty> includesJobProperties = new ArrayList<>();

		Set<File> releaseModuleAppDirs = _getReleaseModuleAppDirs();

		if (releaseModuleAppDirs == null) {
			return includesJobProperties;
		}

		for (File releaseModuleAppDir : releaseModuleAppDirs) {
			includesJobProperties.add(
				getJobProperty(
					"test.batch.class.names.includes.modules",
					releaseModuleAppDir, JobProperty.Type.INCLUDE_GLOB));
		}

		return includesJobProperties;
	}

	@Override
	protected List<JobProperty> getRelevantExcludesJobProperties() {
		Set<File> modifiedModuleDirsList = new HashSet<>();

		try {
			modifiedModuleDirsList.addAll(
				portalGitWorkingDirectory.getModifiedModuleDirsList());
		}
		catch (IOException ioException) {
			File workingDirectory =
				portalGitWorkingDirectory.getWorkingDirectory();

			throw new RuntimeException(
				JenkinsResultsParserUtil.combine(
					"Unable to get relevant module group directories in ",
					workingDirectory.getPath()),
				ioException);
		}

		List<JobProperty> excludesJobProperties = new ArrayList<>();

		excludesJobProperties.addAll(getDefaultExcludesJobProperties());

		for (File modifiedFile :
				portalGitWorkingDirectory.getModifiedFilesList()) {

			excludesJobProperties.addAll(
				_getJobProperties(
					modifiedFile,
					"modules.includes.required.test.batch.class.names.excludes",
					JobProperty.Type.MODULE_EXCLUDE_GLOB, null));
		}

		return excludesJobProperties;
	}

	@Override
	protected List<JobProperty> getRelevantIncludesJobProperties() {
		if (includeStableTestSuite && isStableTestSuiteBatch()) {
			return super.getRelevantIncludesJobProperties();
		}

		List<File> modifiedFilesList =
			portalGitWorkingDirectory.getModifiedFilesList();

		try {
			modifiedFilesList.addAll(
				portalGitWorkingDirectory.getModifiedModuleDirsList());
		}
		catch (IOException ioException) {
			File workingDirectory =
				portalGitWorkingDirectory.getWorkingDirectory();

			throw new RuntimeException(
				JenkinsResultsParserUtil.combine(
					"Unable to get relevant module group directories in ",
					workingDirectory.getPath()),
				ioException);
		}

		if (testRelevantChanges) {
			modifiedFilesList.addAll(
				getRequiredModuleDirs(new ArrayList<>(modifiedFilesList)));
		}

		Set<JobProperty> includesJobProperties = new HashSet<>();

		Matcher matcher = _singleModuleBatchNamePattern.matcher(batchName);

		String moduleName = null;

		if (matcher.find()) {
			moduleName = matcher.group("moduleName");
		}

		for (File modifiedFile : modifiedFilesList) {
			String modifiedModuleAbsolutePath =
				JenkinsResultsParserUtil.getCanonicalPath(modifiedFile);

			if ((moduleName != null) &&
				!modifiedModuleAbsolutePath.contains("/" + moduleName)) {

				continue;
			}

			includesJobProperties.addAll(
				_getJobProperties(
					modifiedFile, "test.batch.class.names.includes.modules",
					JobProperty.Type.MODULE_INCLUDE_GLOB, null));

			includesJobProperties.addAll(
				_getJobProperties(
					modifiedFile,
					"modules.includes.required.test.batch.class.names.includes",
					JobProperty.Type.MODULE_INCLUDE_GLOB, null));
		}

		File workingDirectory = portalGitWorkingDirectory.getWorkingDirectory();

		includesJobProperties.add(
			getJobProperty(
				"test.batch.class.names.includes.modules", workingDirectory,
				JobProperty.Type.INCLUDE_GLOB));

		includesJobProperties.add(
			getJobProperty(
				"modules.includes.required.test.batch.class.names.includes",
				workingDirectory, JobProperty.Type.MODULE_INCLUDE_GLOB));

		return new ArrayList<>(includesJobProperties);
	}

	private String _getAppTitle(File appBndFile) {
		Properties appBndProperties = JenkinsResultsParserUtil.getProperties(
			appBndFile);

		String appTitle = appBndProperties.getProperty(
			"Liferay-Releng-App-Title");

		return appTitle.replace(
			"${liferay.releng.app.title.prefix}", _getAppTitlePrefix());
	}

	private String _getAppTitlePrefix() {
		Job job = getJob();

		if (job.getBuildProfile() == Job.BuildProfile.DXP) {
			return "Liferay";
		}

		return "Liferay CE";
	}

	private Set<String> _getBundledAppNames() {
		Set<String> bundledAppNames = new HashSet<>();

		File liferayHome = _getLiferayHome();

		if ((liferayHome == null) || !liferayHome.exists()) {
			return bundledAppNames;
		}

		List<File> bundledApps = JenkinsResultsParserUtil.findFiles(
			liferayHome, ".*\\.lpkg");

		for (File bundledApp : bundledApps) {
			String bundledAppName = bundledApp.getName();

			bundledAppNames.add(bundledAppName);
		}

		return bundledAppNames;
	}

	private List<JobProperty> _getJobProperties(
		File file, String basePropertyName, JobProperty.Type jobType,
		Set<File> traversedPropertyFileSet) {

		List<JobProperty> jobPropertiesList = new ArrayList<>();

		File modulesBaseDir = new File(
			portalGitWorkingDirectory.getWorkingDirectory(), "modules");

		if ((file == null) || file.equals(modulesBaseDir)) {
			return jobPropertiesList;
		}

		if (!file.isDirectory()) {
			file = file.getParentFile();
		}

		File testPropertiesFile = new File(file, "test.properties");

		if (traversedPropertyFileSet == null) {
			traversedPropertyFileSet = new HashSet<>();
		}

		if (testPropertiesFile.exists() &&
			!traversedPropertyFileSet.contains(testPropertiesFile)) {

			JobProperty jobProperty = getJobProperty(
				basePropertyName, file, jobType);

			String jobPropertyValue = jobProperty.getValue();

			if (JenkinsResultsParserUtil.isNullOrEmpty(jobPropertyValue) &&
				!jobPropertiesList.contains(jobProperty)) {

				jobPropertiesList.add(jobProperty);
			}

			traversedPropertyFileSet.add(testPropertiesFile);
		}

		JobProperty ignoreParentsJobProperty = getJobProperty(
			"ignoreParents[" + getTestSuiteName() + "]", file,
			JobProperty.Type.MODULE_TEST_DIR);

		boolean ignoreParents = Boolean.valueOf(
			ignoreParentsJobProperty.getValue());

		if (ignoreParents) {
			return jobPropertiesList;
		}

		jobPropertiesList.addAll(
			_getJobProperties(
				file.getParentFile(), basePropertyName, jobType,
				traversedPropertyFileSet));

		return jobPropertiesList;
	}

	private File _getLiferayHome() {
		Properties buildProperties = JenkinsResultsParserUtil.getProperties(
			new File(
				portalGitWorkingDirectory.getWorkingDirectory(),
				"build.properties"));

		String liferayHomePath = buildProperties.getProperty("liferay.home");

		if (liferayHomePath == null) {
			return null;
		}

		return new File(liferayHomePath);
	}

	private Set<File> _getReleaseModuleAppDirs() {
		Set<String> bundledAppNames = _getBundledAppNames();

		Set<File> releaseModuleAppDirs = new HashSet<>();

		for (File moduleAppDir : portalGitWorkingDirectory.getModuleAppDirs()) {
			File appBndFile = new File(moduleAppDir, "app.bnd");

			String appTitle = _getAppTitle(appBndFile);

			for (String bundledAppName : bundledAppNames) {
				String regex = JenkinsResultsParserUtil.combine(
					"((.* - )?", Pattern.quote(appTitle), " -.*|",
					Pattern.quote(appTitle), ")\\.lpkg");

				if (!bundledAppName.matches(regex)) {
					continue;
				}

				List<File> skipTestIntegrationCheckFiles =
					JenkinsResultsParserUtil.findFiles(
						moduleAppDir,
						".lfrbuild-ci-skip-test-integration-check");

				if (!skipTestIntegrationCheckFiles.isEmpty()) {
					System.out.println("Ignoring " + moduleAppDir);

					continue;
				}

				releaseModuleAppDirs.add(moduleAppDir);
			}
		}

		return releaseModuleAppDirs;
	}

	private static final Pattern _singleModuleBatchNamePattern =
		Pattern.compile("modules-unit-(?<moduleName>\\S+)-jdk\\d+");

}