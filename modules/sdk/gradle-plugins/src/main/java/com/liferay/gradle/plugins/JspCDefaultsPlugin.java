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

package com.liferay.gradle.plugins;

import com.liferay.gradle.plugins.internal.util.FileUtil;
import com.liferay.gradle.plugins.internal.util.GradleUtil;
import com.liferay.gradle.plugins.jasper.jspc.CompileJSPTask;
import com.liferay.gradle.plugins.jasper.jspc.JspCPlugin;
import com.liferay.gradle.plugins.util.BndBuilderUtil;

import java.io.File;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.CopySpec;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetOutput;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.util.PatternFilterable;

/**
 * @author Andrea Di Giorgi
 */
public class JspCDefaultsPlugin extends BaseDefaultsPlugin<JspCPlugin> {

	public static final String COMPILE_JSP_INCLUDE_PROPERTY_NAME =
		"compile.jsp.include";

	public static final Plugin<Project> INSTANCE = new JspCDefaultsPlugin();

	@Override
	protected void configureDefaults(Project project, JspCPlugin jspCPlugin) {
		_configureTaskGenerateJSPJava(project);
		_configureTaskJar(project);
		_configureTaskProcessResources(project);

		project.afterEvaluate(
			new Action<Project>() {

				@Override
				public void execute(Project project) {
					_configureBundleExtensionDefaults(project);
				}

			});
	}

	@Override
	protected Class<JspCPlugin> getPluginClass() {
		return JspCPlugin.class;
	}

	private JspCDefaultsPlugin() {
	}

	private void _configureBundleExtensionDefaults(Project project) {
		Map<String, Object> bundleInstructions = BndBuilderUtil.getInstructions(
			project);

		StringBuilder sb = new StringBuilder();

		JavaCompile javaCompile = (JavaCompile)GradleUtil.getTask(
			project, JspCPlugin.COMPILE_JSP_TASK_NAME);

		sb.append(FileUtil.getAbsolutePath(javaCompile.getDestinationDir()));

		sb.append(',');

		CompileJSPTask compileJSPTask = (CompileJSPTask)GradleUtil.getTask(
			project, JspCPlugin.GENERATE_JSP_JAVA_TASK_NAME);

		sb.append(FileUtil.getAbsolutePath(compileJSPTask.getDestinationDir()));

		bundleInstructions.put("-add-resource", sb.toString());
	}

	private void _configureTaskGenerateJSPJava(final Project project) {
		final CompileJSPTask compileJSPTask =
			(CompileJSPTask)GradleUtil.getTask(
				project, JspCPlugin.GENERATE_JSP_JAVA_TASK_NAME);

		Copy copy = (Copy)GradleUtil.getTask(
			compileJSPTask.getProject(),
			JavaPlugin.PROCESS_RESOURCES_TASK_NAME);

		compileJSPTask.dependsOn(copy);

		compileJSPTask.setWebAppDir(
			new Callable<File>() {

				@Override
				public File call() throws Exception {
					SourceSet sourceSet = GradleUtil.getSourceSet(
						compileJSPTask.getProject(),
						SourceSet.MAIN_SOURCE_SET_NAME);

					SourceSetOutput sourceSetOutput = sourceSet.getOutput();

					return new File(
						sourceSetOutput.getResourcesDir(),
						"META-INF/resources");
				}

			});
	}

	private void _configureTaskJar(final Project project) {
		boolean compileJspInclude = GradleUtil.getProperty(
			project, COMPILE_JSP_INCLUDE_PROPERTY_NAME, false);

		if (!compileJspInclude) {
			return;
		}

		Jar jar = (Jar)GradleUtil.getTask(project, JavaPlugin.JAR_TASK_NAME);

		JavaCompile javaCompile = (JavaCompile)GradleUtil.getTask(
			project, JspCPlugin.COMPILE_JSP_TASK_NAME);

		jar.dependsOn(javaCompile);
	}

	private void _configureTaskProcessResources(Project project) {
		Copy copy = (Copy)GradleUtil.getTask(
			project, JavaPlugin.PROCESS_RESOURCES_TASK_NAME);

		SourceSet sourceSet = GradleUtil.getSourceSet(
			project, SourceSet.MAIN_SOURCE_SET_NAME);

		SourceDirectorySet sourceDirectorySet = sourceSet.getResources();

		FileTree fileTree = sourceDirectorySet.getAsFileTree();

		fileTree = fileTree.matching(
			new Action<PatternFilterable>() {

				@Override
				public void execute(PatternFilterable patternFilterable) {
					patternFilterable.include("**/*.tld");
				}

			});

		copy.from(
			fileTree.getFiles(),
			new Action<CopySpec>() {

				@Override
				public void execute(CopySpec copySpec) {
					copySpec.into("META-INF/resources/WEB-INF");
				}

			});

		Set<File> srcDirs = sourceDirectorySet.getSrcDirs();

		Iterator<File> iterator = srcDirs.iterator();

		if (iterator.hasNext()) {
			File tagsDir = new File(iterator.next(), "META-INF/tags");

			if (tagsDir.exists()) {
				copy.from(
					tagsDir,
					new Action<CopySpec>() {

						@Override
						public void execute(CopySpec copySpec) {
							copySpec.into("META-INF/resources/META-INF/tags");
						}

					});
			}
		}
	}

}