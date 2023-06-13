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

package com.liferay.source.formatter.check;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.NaturalOrderStringComparator;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alan Huang
 */
public class PoshiAntCommandParametersOrderCheck extends BaseFileCheck {

	@Override
	protected String doProcess(
		String fileName, String absolutePath, String content) {

		ParameterNameComparator parameterNameComparator =
			new ParameterNameComparator();

		Matcher matcher1 = _antCommandCallPattern.matcher(content);

		while (matcher1.find()) {
			String antCommandCall = matcher1.group();

			if (getLevel(antCommandCall) != 0) {
				continue;
			}

			Matcher matcher2 = _valuePattern.matcher(antCommandCall);

			while (matcher2.find()) {
				Matcher matcher3 = _parameterPattern.matcher(matcher2.group(1));

				String previousParameter = null;

				while (matcher3.find()) {
					String paratemter = matcher3.group();

					if (previousParameter != null) {
						int compare = parameterNameComparator.compare(
							previousParameter, paratemter);

						if (compare > 0) {
							content = StringUtil.replaceFirst(
								content, paratemter, previousParameter,
								matcher1.start());

							return StringUtil.replaceFirst(
								content, previousParameter, paratemter,
								matcher1.start());
						}
					}

					previousParameter = paratemter;
				}
			}
		}

		return content;
	}

	private static final Pattern _antCommandCallPattern = Pattern.compile(
		"\n\t+AntCommand\\((.*?)\\);\n", Pattern.DOTALL);
	private static final Pattern _parameterPattern = Pattern.compile(
		" -D[^=]+?=(\\\\\"|).+?\\1(?= |\\Z)");
	private static final Pattern _valuePattern = Pattern.compile(
		"\t+value1 = \"(.+)\"\\);\n");

	private class ParameterNameComparator extends NaturalOrderStringComparator {

		@Override
		public int compare(String parameter1, String parameter2) {
			return super.compare(
				_getParamterName(parameter1), _getParamterName(parameter2));
		}

		private String _getParamterName(String parameter) {
			int x = parameter.indexOf(StringPool.EQUAL);

			return parameter.substring(0, x);
		}

	}

}