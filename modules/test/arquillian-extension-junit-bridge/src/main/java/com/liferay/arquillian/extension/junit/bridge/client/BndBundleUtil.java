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

package com.liferay.arquillian.extension.junit.bridge.client;

import aQute.bnd.build.Classpath;
import aQute.bnd.build.Project;
import aQute.bnd.build.ProjectBuilder;
import aQute.bnd.build.Workspace;
import aQute.bnd.osgi.Analyzer;
import aQute.bnd.osgi.Jar;

import com.liferay.arquillian.extension.junit.bridge.constants.Headers;
import com.liferay.arquillian.extension.junit.bridge.server.TestBundleActivator;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;

import java.io.File;

import java.lang.reflect.Method;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import java.nio.file.Files;
import java.nio.file.Path;

import java.security.CodeSource;
import java.security.ProtectionDomain;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Shuyang Zhou
 */
public class BndBundleUtil {

	public static Path createBundle(
			String className, List<String> filteredMethods, String hostAddress,
			int port, long passCode)
		throws Exception {

		ClassLoader classLoader = new URLClassLoader(_getClassPathURLs(), null);

		Class<?> clazz = classLoader.loadClass(BndBundleUtil.class.getName());

		Method method = clazz.getDeclaredMethod(
			"_createBundle", String.class, List.class, String.class,
			Integer.TYPE, Long.TYPE);

		method.setAccessible(true);

		return (Path)method.invoke(
			null, className, filteredMethods, hostAddress, port, passCode);
	}

	private static Path _createBundle(
			String className, List<String> filteredMethods, String hostAddress,
			int port, long passCode)
		throws Exception {

		File buildDir = new File(System.getProperty("user.dir"));

		try (Workspace workspace = new Workspace(buildDir);
			Project project = new Project(workspace, buildDir);
			ProjectBuilder projectBuilder = _createProjectBuilder(
				project, className, filteredMethods, hostAddress, port,
				passCode);
			Jar jar = projectBuilder.build();
			Analyzer analyzer = new Analyzer()) {

			analyzer.setProperties(project.getProperties());
			analyzer.setJar(jar);

			jar.setManifest(analyzer.calcManifest());

			Path path = Files.createTempFile(null, ".jar");

			jar.write(path.toFile());

			return path;
		}
	}

	private static ProjectBuilder _createProjectBuilder(
			Project project, String className, List<String> filteredMethods,
			String hostAddress, int port, long passCode)
		throws Exception {

		project.setProperty(Headers.TEST_BRIDGE_CLASS_NAME, className);
		project.setProperty(
			Headers.TEST_BRIDGE_FILTERED_METHOD_NAMES,
			StringUtil.merge(filteredMethods, StringPool.COMMA));
		project.setProperty(
			Headers.TEST_BRIDGE_REPORT_SERVER_HOST_NAME, hostAddress);
		project.setProperty(
			Headers.TEST_BRIDGE_REPORT_SERVER_PORT, String.valueOf(port));
		project.setProperty(
			Headers.TEST_BRIDGE_PASS_CODE, String.valueOf(passCode));
		project.setProperty(
			"Bundle-Activator", TestBundleActivator.class.getCanonicalName());

		Set<String> importPackages = new LinkedHashSet<>();

		importPackages.add("!aQute.bnd.build");
		importPackages.add("!aQute.bnd.osgi");
		importPackages.add("*");

		String importPackageString = project.getProperty("Import-Package");

		if (importPackageString != null) {
			importPackages.addAll(StringUtil.split(importPackageString));
		}

		project.setProperty(
			"Import-Package",
			StringUtil.merge(importPackages, StringPool.COMMA));

		Set<String> includeResources = new LinkedHashSet<>();

		includeResources.add("test-classes/integration");

		ProtectionDomain protectionDomain =
			BndBundleUtil.class.getProtectionDomain();

		CodeSource codeSource = protectionDomain.getCodeSource();

		URL url = codeSource.getLocation();

		File file = new File(url.toURI());

		includeResources.add("@" + file.getName());

		String includeResourceString = project.getProperty("-includeresource");

		if (includeResourceString != null) {
			includeResources.addAll(StringUtil.split(includeResourceString));
		}

		project.setProperty(
			"-includeresource",
			StringUtil.merge(includeResources, StringPool.COMMA));

		ProjectBuilder projectBuilder = new ProjectBuilder(project);

		projectBuilder.addClasspath(_getClassPathFiles());

		return projectBuilder;
	}

	private static List<File> _getClassPathFiles() {
		List<File> classPathFiles = new ArrayList<>();

		List<String> fileNames = StringUtil.split(
			System.getProperty("java.class.path"), File.pathSeparatorChar);

		for (String fileName : fileNames) {
			File file = new File(fileName);

			int length = fileName.length();

			if (file.isDirectory() ||
				fileName.regionMatches(true, length - 4, ".zip", 0, 4) ||
				fileName.regionMatches(true, length - 4, ".jar", 0, 4)) {

				classPathFiles.add(file);
			}
		}

		String resource = Classpath.class.getName();

		resource = resource.replace(CharPool.PERIOD, CharPool.SLASH);

		resource = "/".concat(resource);

		resource = resource.concat(".class");

		URL bndURL = BndBundleUtil.class.getResource(resource);

		String path = bndURL.getPath();

		int start = path.indexOf(CharPool.SLASH);

		int end = path.indexOf(CharPool.EXCLAMATION);

		classPathFiles.add(0, new File(path.substring(start, end)));

		return classPathFiles;
	}

	private static URL[] _getClassPathURLs() throws MalformedURLException {
		List<File> classPathFiles = _getClassPathFiles();

		List<URL> urls = new ArrayList<>();

		for (File file : classPathFiles) {
			URI uri = file.toURI();

			urls.add(uri.toURL());
		}

		return urls.toArray(new URL[classPathFiles.size()]);
	}

}