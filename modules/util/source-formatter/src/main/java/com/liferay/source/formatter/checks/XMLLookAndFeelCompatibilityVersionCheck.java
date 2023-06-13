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

package com.liferay.source.formatter.checks;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.source.formatter.checks.util.SourceUtil;
import com.liferay.source.formatter.util.FileUtil;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 * @author Peter Shin
 */
public class XMLLookAndFeelCompatibilityVersionCheck extends BaseFileCheck {

	@Override
	public boolean isPortalCheck() {
		return true;
	}

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws DocumentException, IOException {

		if (fileName.endsWith("-look-and-feel.xml")) {
			_checkCompatibility(fileName, absolutePath, content);
		}

		return content;
	}

	private void _checkCompatibility(
			String fileName, String absolutePath, String content)
		throws DocumentException, IOException {

		if (!isPortalSource() || !isModulesApp(absolutePath, false)) {
			return;
		}

		boolean privateApp = isModulesApp(absolutePath, true);

		String portalVersion = _getPortalVersion(privateApp);

		if (Validator.isNull(portalVersion)) {
			return;
		}

		Document document = SourceUtil.readXML(content);

		Element rootElement = document.getRootElement();

		List<Element> compatibilityElements = rootElement.elements(
			"compatibility");

		for (Element compatibilityElement : compatibilityElements) {
			if (!_hasPortalVersions(compatibilityElement, portalVersion)) {
				StringBundler sb = new StringBundler(4);

				sb.append("Missing version: <version>");
				sb.append(portalVersion);
				sb.append(StringPool.PLUS);
				sb.append("</version>");

				addMessage(fileName, sb.toString());
			}
		}
	}

	private String _getPortalVersion(boolean privateApp) throws IOException {
		String portalVersion = _getPublicPortalVersion();

		if (privateApp) {
			portalVersion = _getPrivatePortalVersion();
		}

		if (Validator.isNull(portalVersion) || privateApp) {
			return portalVersion;
		}

		Matcher matcher = _portalVersionPattern.matcher(portalVersion);

		if (matcher.find()) {
			portalVersion = matcher.group(1) + ".0";
		}

		return portalVersion;
	}

	private synchronized String _getPrivatePortalVersion() throws IOException {
		if (_privatePortalVersion != null) {
			return _privatePortalVersion;
		}

		_privatePortalVersion = StringPool.BLANK;

		if (!isPortalSource()) {
			return _privatePortalVersion;
		}

		File workingDirPropertiesFile = new File(
			getPortalDir(), "working.dir.properties");

		if (!workingDirPropertiesFile.exists()) {
			return _privatePortalVersion;
		}

		String content = FileUtil.read(workingDirPropertiesFile);

		Matcher matcher = _privateBranchNamePattern.matcher(content);

		if (!matcher.find()) {
			return _privatePortalVersion;
		}

		String privateBranchName = StringUtil.trim(matcher.group(1));

		if (Validator.isNull(privateBranchName)) {
			return _privatePortalVersion;
		}

		String s = Pattern.quote("lp.version[" + privateBranchName + "]=");

		Pattern pattern = Pattern.compile(s + "(.*)");

		matcher = pattern.matcher(content);

		if (!matcher.find()) {
			return _privatePortalVersion;
		}

		_privatePortalVersion = StringUtil.trim(matcher.group(1));

		return _privatePortalVersion;
	}

	private synchronized String _getPublicPortalVersion() throws IOException {
		if (_publicPortalVersion != null) {
			return _publicPortalVersion;
		}

		_publicPortalVersion = StringPool.BLANK;

		if (!isPortalSource()) {
			return _publicPortalVersion;
		}

		File workingDirPropertiesFile = new File(
			getPortalDir(), "working.dir.properties");

		if (workingDirPropertiesFile.exists()) {
			String content = FileUtil.read(workingDirPropertiesFile);

			Matcher matcher = _privateBranchNamePattern.matcher(content);

			if (matcher.find()) {
				String privateBranchName = StringUtil.trim(matcher.group(1));

				if (Validator.isNotNull(privateBranchName) &&
					privateBranchName.endsWith("-private")) {

					String branchName = StringUtil.replaceLast(
						privateBranchName, "-private", StringPool.BLANK);

					String s = Pattern.quote("lp.version[" + branchName + "]=");

					Pattern pattern = Pattern.compile(s + "(.*)");

					matcher = pattern.matcher(content);

					if (matcher.find()) {
						String match = matcher.group(1);

						_publicPortalVersion = StringUtil.trim(match);
					}
				}
			}
		}
		else {
			File releaseInfoJavaFile = new File(
				getPortalDir(), _PORTAL_KERNEL_RELEASE_INFO_JAVA_FILE_NAME);

			if (releaseInfoJavaFile.exists()) {
				String content = FileUtil.read(releaseInfoJavaFile);

				Matcher matcher =
					_portalKernelReleaseInfoVersionPattern.matcher(content);

				if (matcher.find()) {
					_publicPortalVersion = StringUtil.trim(matcher.group(1));
				}
			}
		}

		return _publicPortalVersion;
	}

	private boolean _hasPortalVersions(
		Element compatibilityElement, String portalVersion) {

		List<Element> versionElements = compatibilityElement.elements(
			"version");

		for (Element versionElement : versionElements) {
			String version = versionElement.getText();

			if (StringUtil.equalsIgnoreCase(version, portalVersion + "+")) {
				return true;
			}
		}

		return false;
	}

	private static final String _PORTAL_KERNEL_RELEASE_INFO_JAVA_FILE_NAME =
		"portal-kernel/src/com/liferay/portal/kernel/util/ReleaseInfo.java";

	private static final Pattern _portalKernelReleaseInfoVersionPattern =
		Pattern.compile("private static final String _VERSION = \"(.*)\";");
	private static final Pattern _portalVersionPattern = Pattern.compile(
		"(\\w+\\.\\w+)\\.\\w+");
	private static final Pattern _privateBranchNamePattern = Pattern.compile(
		"private.branch.name=(.*)\n");

	private String _privatePortalVersion;
	private String _publicPortalVersion;

}