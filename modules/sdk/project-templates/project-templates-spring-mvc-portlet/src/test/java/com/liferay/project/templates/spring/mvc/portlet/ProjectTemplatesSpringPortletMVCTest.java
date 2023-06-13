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

package com.liferay.project.templates.spring.mvc.portlet;

import com.liferay.maven.executor.MavenExecutor;
import com.liferay.project.templates.BaseProjectTemplatesTestCase;
import com.liferay.project.templates.extensions.util.Validator;
import com.liferay.project.templates.util.FileTestUtil;

import java.io.File;

import java.net.URI;

import java.util.Arrays;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author Lawrence Lee
 */
@RunWith(Parameterized.class)
public class ProjectTemplatesSpringPortletMVCTest
	implements BaseProjectTemplatesTestCase {

	@ClassRule
	public static final MavenExecutor mavenExecutor = new MavenExecutor();

	@Parameterized.Parameters(
		name = "Testcase-{index}: testing {0}, {1}, {2}, {3}, {4}"
	)
	public static Iterable<Object[]> data() {
		return Arrays.asList(
			new Object[][] {
				{"springportletmvc", "embedded", "jsp", "dxp", "7.0.10.17"},
				{"springportletmvc", "embedded", "jsp", "dxp", "7.1.10.7"},
				{"springportletmvc", "embedded", "jsp", "dxp", "7.2.10.7"},
				{"springportletmvc", "embedded", "jsp", "portal", "7.3.7"},
				{"springportletmvc", "embedded", "jsp", "portal", "7.4.3.29"},
				{"portletmvc4spring", "embedded", "jsp", "dxp", "7.1.10.7"},
				{"portletmvc4spring", "embedded", "jsp", "dxp", "7.2.10.7"},
				{"portletmvc4spring", "embedded", "jsp", "portal", "7.3.7"},
				{"portletmvc4spring", "embedded", "jsp", "portal", "7.4.3.29"},
				{
					"portletmvc4spring", "embedded", "thymeleaf", "dxp",
					"7.1.10.7"
				},
				{
					"portletmvc4spring", "embedded", "thymeleaf", "dxp",
					"7.2.10.7"
				},
				{
					"portletmvc4spring", "embedded", "thymeleaf", "portal",
					"7.3.7"
				},
				{
					"portletmvc4spring", "embedded", "thymeleaf", "portal",
					"7.4.3.29"
				}
			});
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		String gradleDistribution = System.getProperty("gradle.distribution");

		if (Validator.isNull(gradleDistribution)) {
			Properties properties = FileTestUtil.readProperties(
				"gradle-wrapper/gradle/wrapper/gradle-wrapper.properties");

			gradleDistribution = properties.getProperty("distributionUrl");
		}

		Assert.assertTrue(gradleDistribution.contains(GRADLE_WRAPPER_VERSION));

		_gradleDistribution = URI.create(gradleDistribution);
	}

	public ProjectTemplatesSpringPortletMVCTest(
		String framework, String frameworkDependencies, String viewType,
		String liferayProduct, String liferayVersion) {

		_framework = framework;
		_frameworkDependencies = frameworkDependencies;
		_viewType = viewType;
		_liferayProduct = liferayProduct;
		_liferayVersion = liferayVersion;
	}

	@Test
	public void testSpringPortletMVC() throws Exception {
		File gradleWorkspaceDir = buildWorkspace(
			temporaryFolder, "gradle", "gradleWS", _liferayVersion,
			mavenExecutor);

		String liferayWorkspaceProduct = getLiferayWorkspaceProduct(
			_liferayVersion);

		if (liferayWorkspaceProduct != null) {
			writeGradlePropertiesInWorkspace(
				gradleWorkspaceDir,
				"liferay.workspace.product=" + liferayWorkspaceProduct);
		}

		File gradleWorkspaceModulesDir = new File(
			gradleWorkspaceDir, "modules");

		File gradleProjectDir = _buildSpringMVCTemplate(
			gradleWorkspaceModulesDir, "gradle", _framework,
			_frameworkDependencies, _viewType, _liferayVersion);

		testNotContains(
			gradleProjectDir, "src/main/webapp/WEB-INF/web.xml", "false");

		testExists(
			gradleProjectDir,
			"src/main/webapp/WEB-INF/spring-context/portlet/Sample.xml");
		testExists(
			gradleProjectDir,
			"src/main/java/com/test/controller/UserController.java");

		testTemplateWarPortletDTD(gradleProjectDir, _liferayVersion);

		if (_liferayVersion.startsWith("7.0")) {
			testContains(
				gradleProjectDir, "src/main/webapp/WEB-INF/web.xml",
				"version=\"3.0\" xmlns=\"http://java.sun.com/xml/ns/javaee");
		}
		else {
			testContains(
				gradleProjectDir, "src/main/webapp/WEB-INF/web.xml",
				"version=\"3.1\" xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"");
		}

		if (_viewType.equals("jsp")) {
			if (_framework.equals("springportletmvc")) {
				testContains(
					gradleProjectDir,
					"src/main/webapp/WEB-INF/spring-context" +
						"/portlet-application-context.xml",
					"org.springframework.web.servlet.view.JstlView");
			}
			else {
				testContains(
					gradleProjectDir,
					"src/main/webapp/WEB-INF/spring-context" +
						"/portlet-application-context.xml",
					"com.liferay.portletmvc4spring.PortletJstlView");
			}
		}

		if (_viewType.equals("jsp")) {
			testExists(
				gradleProjectDir,
				"src/main/webapp/WEB-INF/views/greeting.jspx");

			testNotExists(
				gradleProjectDir,
				"src/main/webapp/WEB-INF/views/greeting.html");
		}
		else {
			testExists(
				gradleProjectDir,
				"src/main/webapp/WEB-INF/views/greeting.html");
			testNotExists(
				gradleProjectDir,
				"src/main/webapp/WEB-INF/views/greeting.jspx");
		}

		if (_viewType.equals("jsp") || _framework.equals("portletmvc4spring")) {
			testNotExists(
				gradleProjectDir,
				"src/main/java/com/test/spring4/ServletContextFactory.java");
		}

		File mavenWorkspaceDir = buildWorkspace(
			temporaryFolder, "maven", "mavenWS", _liferayVersion,
			mavenExecutor);

		File mavenModulesDir = new File(mavenWorkspaceDir, "modules");

		File mavenProjectDir = _buildSpringMVCTemplate(
			mavenModulesDir, "maven", _framework, _frameworkDependencies,
			_viewType, _liferayVersion);

		if (isBuildProjects()) {
			File gradleOutputDir = new File(gradleProjectDir, "build/libs");
			File mavenOutputDir = new File(mavenProjectDir, "target");

			buildProjects(
				_gradleDistribution, mavenExecutor, gradleWorkspaceDir,
				mavenProjectDir, gradleOutputDir, mavenOutputDir,
				":modules:sampleSpringMVCPortlet" + GRADLE_TASK_PATH_BUILD);
		}
	}

	@Rule
	public final TemporaryFolder temporaryFolder = new TemporaryFolder();

	private File _buildSpringMVCTemplate(
			File destinationDir, String buildType, String framework,
			String frameworkDependencies, String viewType,
			String liferayVersion)
		throws Exception {

		String template = "spring-mvc-portlet";
		String name = "sampleSpringMVCPortlet";

		if (buildType.equals("maven")) {
			String groupId = "com.test";

			return buildTemplateWithMaven(
				destinationDir, destinationDir, template, name, groupId,
				mavenExecutor, "-DclassName=Sample", "-Dframework=" + framework,
				"-DframeworkDependencies=" + frameworkDependencies,
				"-DliferayProduct=" + _liferayProduct,
				"-DliferayVersion=" + liferayVersion, "-Dpackage=com.test",
				"-DviewType=" + viewType);
		}

		return buildTemplateWithGradle(
			destinationDir, template, name, "--class-name", "Sample",
			"--framework", framework, "--framework-dependencies",
			frameworkDependencies, "--liferay-product", _liferayProduct,
			"--liferay-version", liferayVersion, "--package-name", "com.test",
			"--view-type", viewType);
	}

	private static URI _gradleDistribution;

	private final String _framework;
	private final String _frameworkDependencies;
	private final String _liferayProduct;
	private final String _liferayVersion;
	private final String _viewType;

}