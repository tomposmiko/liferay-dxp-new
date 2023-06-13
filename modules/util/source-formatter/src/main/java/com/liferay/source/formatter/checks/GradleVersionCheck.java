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

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.source.formatter.util.FileUtil;

import java.io.File;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hugo Huijser
 */
public class GradleVersionCheck extends BaseFileCheck {

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws IOException {

		Matcher matcher = _versionPattern.matcher(content);

		while (matcher.find()) {
			String name = matcher.group(2);
			String version = matcher.group(3);

			_checkDefaultVersion(
				fileName, content, name, version, matcher.start());

			if (isSubrepository() || absolutePath.contains("/modules/apps/") ||
				absolutePath.contains("/modules/private/apps/")) {

				if (!_isTestUtilModule(absolutePath)) {
					content = _fixMicroVersion(
						fileName, content, matcher.group(1), name, version);
				}
			}
		}

		return content;
	}

	private void _checkDefaultVersion(
		String fileName, String content, String name, String version, int pos) {

		if (version.equals("default") &&
			!name.equals("com.liferay.portal.impl") &&
			!name.equals("com.liferay.portal.kernel") &&
			!name.equals("com.liferay.portal.test") &&
			!name.equals("com.liferay.portal.test.integration") &&
			!name.equals("com.liferay.util.bridges") &&
			!name.equals("com.liferay.util.java") &&
			!name.equals("com.liferay.util.taglib")) {

			addMessage(
				fileName, "Do not use 'default' version for '" + name + "'",
				"gradle_versioning.markdown", getLineNumber(content, pos));
		}
	}

	private String _fixMicroVersion(
			String fileName, String content, String line, String name,
			String version)
		throws IOException {

		if (!line.startsWith("compileOnly ") && !line.startsWith("provided ")) {
			return content;
		}

		if (!name.startsWith("com.liferay.") ||
			!version.matches("[0-9]+\\.[0-9]+\\.[1-9][0-9]*")) {

			return content;
		}

		int pos = fileName.lastIndexOf(CharPool.SLASH);

		String bndFileLocation = fileName.substring(0, pos + 1) + "bnd.bnd";

		File bndFile = new File(bndFileLocation);

		if (bndFile.exists()) {
			String bndFileContent = FileUtil.read(bndFile);

			Matcher matcher = _bndConditionalPackagePattern.matcher(
				bndFileContent);

			if (matcher.find()) {
				String conditionalPackageContent = matcher.group();

				if (conditionalPackageContent.contains(name)) {
					return content;
				}
			}
		}

		pos = version.lastIndexOf(".");

		String newLine = StringUtil.replaceFirst(
			line, "version: \"" + version + "\"",
			"version: \"" + version.substring(0, pos + 1) + "0\"");

		return StringUtil.replaceFirst(content, line, newLine);
	}

	private boolean _isTestUtilModule(String absolutePath) {
		int x = absolutePath.lastIndexOf(StringPool.SLASH);

		int y = absolutePath.lastIndexOf(StringPool.SLASH, x - 1);

		String moduleName = absolutePath.substring(y + 1, x);

		if (!moduleName.endsWith("-test-util")) {
			return false;
		}

		return true;
	}

	private static final Pattern _bndConditionalPackagePattern =
		Pattern.compile(
			"-conditionalpackage:(.*[^\\\\])(\n|\\Z)", Pattern.DOTALL);
	private static final Pattern _versionPattern = Pattern.compile(
		"\n\t*(.* name: \"(.*?)\", version: \"(.*?)\")");

}