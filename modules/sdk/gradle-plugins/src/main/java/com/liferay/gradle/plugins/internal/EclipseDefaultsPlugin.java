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

package com.liferay.gradle.plugins.internal;

import com.liferay.gradle.plugins.BaseDefaultsPlugin;
import com.liferay.gradle.plugins.internal.util.GradleUtil;

import groovy.lang.Closure;

import groovy.util.Node;

import java.util.Iterator;
import java.util.List;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.XmlProvider;
import org.gradle.plugins.ide.api.FileContentMerger;
import org.gradle.plugins.ide.api.XmlFileContentMerger;
import org.gradle.plugins.ide.eclipse.EclipsePlugin;
import org.gradle.plugins.ide.eclipse.model.AbstractClasspathEntry;
import org.gradle.plugins.ide.eclipse.model.Classpath;
import org.gradle.plugins.ide.eclipse.model.ClasspathEntry;
import org.gradle.plugins.ide.eclipse.model.EclipseClasspath;
import org.gradle.plugins.ide.eclipse.model.EclipseModel;
import org.gradle.plugins.ide.eclipse.model.EclipseProject;

/**
 * @author Andrea Di Giorgi
 */
public class EclipseDefaultsPlugin extends BaseDefaultsPlugin<EclipsePlugin> {

	public static final Plugin<Project> INSTANCE = new EclipseDefaultsPlugin();

	@Override
	protected void configureDefaults(
		Project project, EclipsePlugin eclipsePlugin) {

		_configureEclipseClasspathFile(project);
		_configureEclipseProject(project);
		_configureTaskEclipse(eclipsePlugin);
	}

	@Override
	protected Class<EclipsePlugin> getPluginClass() {
		return EclipsePlugin.class;
	}

	private EclipseDefaultsPlugin() {
	}

	private void _configureEclipseClasspathFile(Project project) {
		EclipseModel eclipseModel = GradleUtil.getExtension(
			project, EclipseModel.class);

		EclipseClasspath eclipseClasspath = eclipseModel.getClasspath();

		FileContentMerger fileContentMerger = eclipseClasspath.getFile();

		Closure<Void> closure = new Closure<Void>(project) {

			@SuppressWarnings("unused")
			public void doCall(Classpath classpath) {
				List<ClasspathEntry> classpathEntries = classpath.getEntries();

				Iterator<ClasspathEntry> iterator = classpathEntries.iterator();

				while (iterator.hasNext()) {
					ClasspathEntry classpathEntry = iterator.next();

					if (!(classpathEntry instanceof AbstractClasspathEntry)) {
						continue;
					}

					AbstractClasspathEntry abstractClasspathEntry =
						(AbstractClasspathEntry)classpathEntry;

					String kind = abstractClasspathEntry.getKind();
					String path = abstractClasspathEntry.getPath();

					if (kind.equals("lib") && path.endsWith(".pom")) {
						iterator.remove();
					}
				}
			}

		};

		fileContentMerger.whenMerged(closure);
	}

	private void _configureEclipseProject(Project project) {
		EclipseModel eclipseModel = GradleUtil.getExtension(
			project, EclipseModel.class);

		EclipseProject eclipseProject = eclipseModel.getProject();

		List<String> natures = eclipseProject.getNatures();

		natures.add("com.liferay.ide.core.liferayNature");

		Action<XmlProvider> action = new Action<XmlProvider>() {

			@Override
			public void execute(XmlProvider xmlProvider) {
				Node projectDescriptionNode = xmlProvider.asNode();

				Node filteredResourcesNode = projectDescriptionNode.appendNode(
					"filteredResources");

				Node filterNode = filteredResourcesNode.appendNode("filter");

				filterNode.appendNode("id", System.currentTimeMillis());
				filterNode.appendNode("name");
				filterNode.appendNode("type", "26");

				Node matcherNode = filterNode.appendNode("matcher");

				matcherNode.appendNode(
					"id", "org.eclipse.ui.ide.orFilterMatcher");

				Node argumentsNode = matcherNode.appendNode("arguments");

				for (String filteredDirName : _FILTERED_DIR_NAMES) {
					Node curMatcherNode = argumentsNode.appendNode("matcher");

					curMatcherNode.appendNode(
						"arguments",
						"1.0-name-matches-false-false-" + filteredDirName);
					curMatcherNode.appendNode(
						"id", "org.eclipse.ui.ide.multiFilter");
				}
			}

		};

		XmlFileContentMerger xmlFileContentMerger = eclipseProject.getFile();

		xmlFileContentMerger.withXml(action);
	}

	private void _configureTaskEclipse(EclipsePlugin eclipsePlugin) {
		Task task = eclipsePlugin.getLifecycleTask();

		task.dependsOn(eclipsePlugin.getCleanTask());
	}

	private static final String[] _FILTERED_DIR_NAMES =
		{".git", ".gradle", "build", "node_modules", "tmp"};

}