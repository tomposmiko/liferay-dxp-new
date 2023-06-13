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

package com.liferay.gradle.plugins.defaults.internal;

import com.liferay.gradle.plugins.LiferayBasePlugin;
import com.liferay.gradle.plugins.cache.CachePlugin;
import com.liferay.gradle.plugins.defaults.internal.util.CIUtil;
import com.liferay.gradle.plugins.defaults.internal.util.GradleUtil;
import com.liferay.gradle.plugins.node.NodePlugin;
import com.liferay.gradle.plugins.node.tasks.DownloadNodeTask;
import com.liferay.gradle.plugins.node.tasks.ExecuteNodeTask;
import com.liferay.gradle.plugins.node.tasks.ExecuteNpmTask;
import com.liferay.gradle.plugins.node.tasks.NpmInstallTask;
import com.liferay.gradle.plugins.test.integration.TestIntegrationBasePlugin;
import com.liferay.gradle.plugins.test.integration.TestIntegrationPlugin;
import com.liferay.gradle.util.Validator;

import java.io.File;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.logging.Logger;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskOutputs;

/**
 * @author Andrea Di Giorgi
 */
public class LiferayCIPlugin implements Plugin<Project> {

	public static final Plugin<Project> INSTANCE = new LiferayCIPlugin();

	public static final String RESTORE_HOTFIX_VERSION_TASK_NAME =
		"restoreHotfixVersion";

	public static final String UPDATE_HOTFIX_VERSION_TASK_NAME =
		"updateHotfixVersion";

	@Override
	public void apply(final Project project) {
		Task restoreHotfixVersionTask = _addTaskRestoreHotfixVersion(project);
		Task updateHotfixVersionTask = _addTaskUpdateHotfixVersion(project);

		_configureTasksDownloadNode(project);
		_configureTasksExecuteNode(project);
		_configureTasksExecuteNpm(
			project, restoreHotfixVersionTask, updateHotfixVersionTask);
		_configureTasksNpmInstall(project);

		GradleUtil.withPlugin(
			project, TestIntegrationPlugin.class,
			new Action<TestIntegrationPlugin>() {

				@Override
				public void execute(
					TestIntegrationPlugin testIntegrationPlugin) {

					_configureTaskTestIntegration(project);
				}

			});

		project.afterEvaluate(
			new Action<Project>() {

				@Override
				public void execute(Project project) {
					_configureTasksNpmInstallArgs(project);
				}

			});
	}

	private LiferayCIPlugin() {
	}

	private Task _addTaskRestoreHotfixVersion(final Project project) {
		Task task = project.task(RESTORE_HOTFIX_VERSION_TASK_NAME);

		task.setDescription("Restores the project hotfix version.");

		task.doLast(
			new Action<Task>() {

				@Override
				public void execute(Task task) {
					CIUtil.restoreHotfixVersion(
						project, _BND_HOTFIX_VERSION_FILE_NAME);

					for (String fileName : _JSON_HOTFIX_VERSION_FILE_NAMES) {
						CIUtil.restoreHotfixVersion(project, fileName);
					}
				}

			});

		return task;
	}

	private Task _addTaskUpdateHotfixVersion(final Project project) {
		Task task = project.task(UPDATE_HOTFIX_VERSION_TASK_NAME);

		task.setDescription("Updates the project hotfix version.");

		task.doLast(
			new Action<Task>() {

				@Override
				public void execute(Task task) {
					CIUtil.updateHotfixVersion(
						project, _BND_HOTFIX_VERSION_FILE_NAME);

					for (String fileName : _JSON_HOTFIX_VERSION_FILE_NAMES) {
						CIUtil.updateHotfixVersion(project, fileName);
					}
				}

			});

		return task;
	}

	private void _configureTaskDownloadNode(DownloadNodeTask downloadNodeTask) {
		downloadNodeTask.doFirst(
			new Action<Task>() {

				@Override
				public void execute(Task task) {
					if (GradleUtil.hasPlugin(
							task.getProject(), CachePlugin.class)) {

						throw new GradleException(
							"Unable to use Node.js on CI, please configure " +
								"com.liferay.cache or update the cache");
					}
				}

			});
	}

	private void _configureTaskExecuteNode(ExecuteNodeTask executeNodeTask) {
		executeNodeTask.setNpmInstallRetries(_NPM_INSTALL_RETRIES);
	}

	private void _configureTaskExecuteNodeArgs(
		ExecuteNodeTask executeNodeTask, Map<String, String> newArgs) {

		List<Object> args = executeNodeTask.getArgs();

		for (Map.Entry<String, String> entry : newArgs.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			boolean changed = false;

			for (int i = 0; i < args.size(); i++) {
				String arg = GradleUtil.toString(args.get(i));

				if (arg.startsWith(key)) {
					changed = true;

					args.set(i, key + value);

					break;
				}
			}

			if (!changed) {
				args.add(key + value);
			}
		}

		executeNodeTask.setArgs(args);
	}

	private void _configureTaskExecuteNpm(
		ExecuteNpmTask executeNpmTask, String registry,
		Task restoreHotfixVersionTask, Task updateHotfixVersionTask) {

		if (Validator.isNotNull(registry)) {
			executeNpmTask.setRegistry(registry);
		}

		Project project = executeNpmTask.getProject();

		TaskContainer taskContainer = project.getTasks();

		Task deployTask = taskContainer.findByName(
			LiferayBasePlugin.DEPLOY_TASK_NAME);

		if (deployTask != null) {
			String hotfixVersion = CIUtil.getBNDHotfixVersion(
				deployTask.getProject(), _BND_HOTFIX_VERSION_FILE_NAME);

			if (hotfixVersion != null) {
				executeNpmTask.dependsOn(updateHotfixVersionTask);

				deployTask.finalizedBy(restoreHotfixVersionTask);
			}
		}
	}

	private void _configureTaskNpmInstall(NpmInstallTask npmInstallTask) {
		if (Validator.isNull(System.getenv("FIX_PACKS_RELEASE_ENVIRONMENT"))) {
			npmInstallTask.setNodeModulesCacheDir(_NODE_MODULES_CACHE_DIR);
		}

		npmInstallTask.setRemoveShrinkwrappedUrls(Boolean.TRUE);
		npmInstallTask.setUseNpmCI(Boolean.FALSE);
	}

	private void _configureTaskNpmRunBuild(ExecuteNpmTask executeNpmTask) {
		if (Validator.isNull(System.getenv("FIX_PACKS_RELEASE_ENVIRONMENT"))) {
			return;
		}

		TaskOutputs taskOutputs = executeNpmTask.getOutputs();

		taskOutputs.upToDateWhen(
			new Spec<Task>() {

				@Override
				public boolean isSatisfiedBy(Task task) {
					return false;
				}

			});
	}

	private void _configureTasksDownloadNode(Project project) {
		TaskContainer taskContainer = project.getTasks();

		taskContainer.withType(
			DownloadNodeTask.class,
			new Action<DownloadNodeTask>() {

				@Override
				public void execute(DownloadNodeTask downloadNodeTask) {
					_configureTaskDownloadNode(downloadNodeTask);
				}

			});
	}

	private void _configureTasksExecuteNode(Project project) {
		TaskContainer taskContainer = project.getTasks();

		taskContainer.withType(
			ExecuteNodeTask.class,
			new Action<ExecuteNodeTask>() {

				@Override
				public void execute(ExecuteNodeTask executeNodeTask) {
					_configureTaskExecuteNode(executeNodeTask);
				}

			});
	}

	private void _configureTasksExecuteNpm(
		Project project, final Task restoreHotfixVersionTask,
		final Task updateHotfixVersionTask) {

		final String ciRegistry = GradleUtil.getProperty(
			project, "nodejs.npm.ci.registry", (String)null);

		TaskContainer taskContainer = project.getTasks();

		taskContainer.withType(
			ExecuteNpmTask.class,
			new Action<ExecuteNpmTask>() {

				@Override
				public void execute(ExecuteNpmTask executeNpmTask) {
					_configureTaskExecuteNpm(
						executeNpmTask, ciRegistry, restoreHotfixVersionTask,
						updateHotfixVersionTask);

					String taskName = executeNpmTask.getName();

					if (Objects.equals(
							taskName, NodePlugin.NPM_RUN_BUILD_TASK_NAME)) {

						_configureTaskNpmRunBuild(executeNpmTask);
					}
				}

			});
	}

	private void _configureTasksNpmInstall(Project project) {
		TaskContainer taskContainer = project.getTasks();

		taskContainer.withType(
			NpmInstallTask.class,
			new Action<NpmInstallTask>() {

				@Override
				public void execute(NpmInstallTask npmInstallTask) {
					_configureTaskNpmInstall(npmInstallTask);
				}

			});
	}

	private void _configureTasksNpmInstallArgs(Project project) {
		final String ciSassBinarySite = GradleUtil.getProperty(
			project, "nodejs.npm.ci.sass.binary.site", (String)null);

		if (Validator.isNull(ciSassBinarySite)) {
			return;
		}

		TaskContainer taskContainer = project.getTasks();

		taskContainer.withType(
			NpmInstallTask.class,
			new Action<NpmInstallTask>() {

				@Override
				public void execute(NpmInstallTask npmInstallTask) {
					_configureTaskExecuteNodeArgs(
						npmInstallTask,
						Collections.singletonMap(
							_SASS_BINARY_SITE_ARG, ciSassBinarySite));
				}

			});
	}

	private void _configureTaskTestIntegration(Project project) {
		Task testIntegrationTask = GradleUtil.getTask(
			project, TestIntegrationBasePlugin.TEST_INTEGRATION_TASK_NAME);

		Action<Task> action = new Action<Task>() {

			@Override
			public void execute(Task task) {
				Project project = task.getProject();

				Logger logger = project.getLogger();

				SourceSet sourceSet = GradleUtil.getSourceSet(
					project,
					TestIntegrationBasePlugin.TEST_INTEGRATION_SOURCE_SET_NAME);

				Configuration configuration = GradleUtil.getConfiguration(
					project, sourceSet.getCompileConfigurationName());

				DependencySet dependencySet = configuration.getDependencies();

				for (ProjectDependency projectDependency :
						dependencySet.withType(ProjectDependency.class)) {

					Project dependencyProject =
						projectDependency.getDependencyProject();

					if (CIUtil.isExcludedDependencyProject(
							project, dependencyProject)) {

						if (logger.isLifecycleEnabled()) {
							logger.lifecycle(
								"Excluded project dependency {} for {}",
								dependencyProject.getPath(), project.getPath());
						}

						continue;
					}

					File lfrBuildCIFile = dependencyProject.file(
						".lfrbuild-ci");
					File lfrBuildCISkipTestIntegrationCheckFile =
						dependencyProject.file(
							".lfrbuild-ci-skip-test-integration-check");
					File lfrBuildPortalFile = dependencyProject.file(
						".lfrbuild-portal");

					if (lfrBuildCISkipTestIntegrationCheckFile.exists()) {
						if (lfrBuildCIFile.exists() ||
							lfrBuildPortalFile.exists()) {

							throw new GradleException(
								"Please delete marker file " +
									lfrBuildCISkipTestIntegrationCheckFile);
						}
					}
					else if (!lfrBuildCIFile.exists() &&
							 !lfrBuildPortalFile.exists()) {

						throw new GradleException(
							"Please create marker file " + lfrBuildPortalFile);
					}
				}
			}

		};

		testIntegrationTask.doFirst(action);
	}

	private static final String _BND_HOTFIX_VERSION_FILE_NAME = "bnd.bnd";

	private static final String[] _JSON_HOTFIX_VERSION_FILE_NAMES = {
		"package-lock.json", "package.json"
	};

	private static final File _NODE_MODULES_CACHE_DIR = new File(
		System.getProperty("user.home"), ".liferay/node-modules-cache");

	private static final int _NPM_INSTALL_RETRIES = 3;

	private static final String _SASS_BINARY_SITE_ARG = "--sass-binary-site=";

}