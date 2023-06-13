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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.ToolsUtil;
import com.liferay.source.formatter.checks.util.GradleSourceUtil;
import com.liferay.source.formatter.checks.util.SourceUtil;

import java.io.Serializable;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hugo Huijser
 * @author Peter Shin
 */
public class GradleDependenciesCheck extends BaseFileCheck {

	@Override
	protected String doProcess(
		String fileName, String absolutePath, String content) {

		List<String> blocks = GradleSourceUtil.getDependenciesBlocks(content);

		for (String dependencies : blocks) {
			content = _formatDependencies(content, dependencies);
		}

		return content;
	}

	private String _formatDependencies(String content, String dependencies) {
		String indent = SourceUtil.getIndent(dependencies);

		int x = dependencies.indexOf("\n");
		int y = dependencies.lastIndexOf("\n");

		if (x == y) {
			return content;
		}

		dependencies = dependencies.substring(x, y + 1);

		Matcher matcher = _incorrectWhitespacePattern.matcher(dependencies);

		while (matcher.find()) {
			if (!ToolsUtil.isInsideQuotes(dependencies, matcher.start())) {
				String newDependencies = StringUtil.insert(
					dependencies, StringPool.SPACE, matcher.end() - 1);

				return StringUtil.replace(
					content, dependencies, newDependencies);
			}
		}

		if (dependencies.contains(StringPool.APOSTROPHE)) {
			String newDependencies = StringUtil.replace(
				dependencies, CharPool.APOSTROPHE, CharPool.QUOTE);

			return StringUtil.replace(content, dependencies, newDependencies);
		}

		Set<String> uniqueDependencies = new TreeSet<>(
			new GradleDependencyComparator());

		for (String dependency : StringUtil.splitLines(dependencies)) {
			dependency = dependency.trim();

			if (Validator.isNull(dependency)) {
				continue;
			}

			matcher = _incorrectGroupNameVersionPattern.matcher(dependency);

			if (matcher.find()) {
				StringBundler sb = new StringBundler(9);

				sb.append(matcher.group(1));
				sb.append(" group: \"");
				sb.append(matcher.group(2));
				sb.append("\", name: \"");
				sb.append(matcher.group(3));
				sb.append("\", version: \"");
				sb.append(matcher.group(4));
				sb.append("\"");
				sb.append(matcher.group(5));

				dependency = sb.toString();
			}

			uniqueDependencies.add(dependency);
		}

		StringBundler sb = new StringBundler();

		String previousConfiguration = null;

		for (String dependency : uniqueDependencies) {
			String configuration = GradleSourceUtil.getConfiguration(
				dependency);

			if ((previousConfiguration == null) ||
				!previousConfiguration.equals(configuration)) {

				previousConfiguration = configuration;

				sb.append("\n");
			}

			sb.append(indent);
			sb.append("\t");
			sb.append(dependency);
			sb.append("\n");
		}

		return StringUtil.replace(content, dependencies, sb.toString());
	}

	private final Pattern _incorrectGroupNameVersionPattern = Pattern.compile(
		"(^[^\\s]+)\\s+\"([^:]+?):([^:]+?):([^\"]+?)\"(.*?)", Pattern.DOTALL);
	private final Pattern _incorrectWhitespacePattern = Pattern.compile(
		":[^ \n]");

	private class GradleDependencyComparator
		implements Comparator<String>, Serializable {

		@Override
		public int compare(String dependency1, String dependency2) {
			String configuration1 = GradleSourceUtil.getConfiguration(
				dependency1);
			String configuration2 = GradleSourceUtil.getConfiguration(
				dependency2);

			if (!configuration1.equals(configuration2)) {
				return dependency1.compareTo(dependency2);
			}

			String group1 = _getPropertyValue(dependency1, "group");
			String group2 = _getPropertyValue(dependency2, "group");

			if ((group1 != null) && group1.equals(group2)) {
				String name1 = _getPropertyValue(dependency1, "name");
				String name2 = _getPropertyValue(dependency2, "name");

				if ((name1 != null) && name1.equals(name2)) {
					return 0;
				}
			}

			return dependency1.compareTo(dependency2);
		}

		private String _getPropertyValue(
			String dependency, String propertyName) {

			Pattern pattern = Pattern.compile(
				".* " + propertyName + ": \"(.+?)\"");

			Matcher matcher = pattern.matcher(dependency);

			if (matcher.find()) {
				return matcher.group(1);
			}

			return null;
		}

	}

}