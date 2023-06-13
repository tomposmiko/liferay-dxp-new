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

package com.liferay.gradle.plugins.rest.builder;

import com.liferay.gradle.util.GradleUtil;
import com.liferay.gradle.util.Validator;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.Optional;

/**
 * @author Peter Shin
 */
public class BuildRESTTask extends JavaExec {

	public BuildRESTTask() {
		setMain("com.liferay.portal.tools.rest.builder.RESTBuilder");
	}

	@Override
	public void exec() {
		setArgs(_getCompleteArgs());

		super.exec();
	}

	@InputFile
	@Optional
	public File getCopyrightFile() {
		return GradleUtil.toFile(getProject(), _copyrightFile);
	}

	@InputDirectory
	public File getRESTConfigDir() {
		return GradleUtil.toFile(getProject(), _restConfigDir);
	}

	public void setCopyrightFile(Object copyrightFile) {
		_copyrightFile = copyrightFile;
	}

	public void setRESTConfigDir(Object restConfigDir) {
		_restConfigDir = restConfigDir;
	}

	private static void _addArg(List<String> args, String name, File file) {
		if (file != null) {
			_addArg(args, name, file.getAbsolutePath());
		}
	}

	private static void _addArg(List<String> args, String name, String value) {
		if (Validator.isNotNull(value)) {
			args.add(name);
			args.add(value);
		}
	}

	private List<String> _getCompleteArgs() {
		List<String> args = new ArrayList<>(getArgs());

		_addArg(args, "--copyright-file", getCopyrightFile());
		_addArg(args, "--rest-config-dir", getRESTConfigDir());

		return args;
	}

	private Object _copyrightFile;
	private Object _restConfigDir;

}