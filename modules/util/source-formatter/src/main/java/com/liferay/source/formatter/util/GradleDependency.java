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

package com.liferay.source.formatter.util;

import java.text.MessageFormat;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author Kevin Lee
 */
public class GradleDependency implements Comparable<GradleDependency> {

	public static final Comparator<GradleDependency>
		COMPARATOR_LAST_LINE_NUMBER_DESC = Comparator.comparingInt(
			GradleDependency::getLastLineNumber
		).reversed();

	public GradleDependency(
		String configuration, String group, String name, String version) {

		this(configuration, group, name, version, -1, -1);
	}

	public GradleDependency(
		String configuration, String group, String name, String version,
		int lineNumber, int lastLineNumber) {

		_configuration = configuration;
		_group = group;
		_name = name;
		_version = version;
		_lineNumber = lineNumber;
		_lastLineNumber = lastLineNumber;
	}

	@Override
	public int compareTo(GradleDependency gradleDependency) {
		String string = toString();

		return string.compareTo(gradleDependency.toString());
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof GradleDependency)) {
			return false;
		}

		GradleDependency gradleDependency = (GradleDependency)object;

		if (Objects.equals(_configuration, gradleDependency._configuration) &&
			Objects.equals(_group, gradleDependency._group) &&
			Objects.equals(_name, gradleDependency._name)) {

			return true;
		}

		return false;
	}

	public String getConfiguration() {
		return _configuration;
	}

	public String getGroup() {
		return _group;
	}

	public int getLastLineNumber() {
		return _lastLineNumber;
	}

	public int getLineNumber() {
		return _lineNumber;
	}

	public String getName() {
		return _name;
	}

	public String getVersion() {
		return _version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_configuration, _group, _name);
	}

	@Override
	public String toString() {
		if (_version == null) {
			return MessageFormat.format(
				"{0} group: \"{1}\", name: \"{2}\"", _configuration, _group,
				_name);
		}

		return MessageFormat.format(
			"{0} group: \"{1}\", name: \"{2}\", version: \"{3}\"",
			_configuration, _group, _name, _version);
	}

	private final String _configuration;
	private final String _group;
	private final int _lastLineNumber;
	private final int _lineNumber;
	private final String _name;
	private final String _version;

}