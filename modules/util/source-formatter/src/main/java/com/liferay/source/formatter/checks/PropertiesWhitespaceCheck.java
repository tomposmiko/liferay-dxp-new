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
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

/**
 * @author Hugo Huijser
 */
public class PropertiesWhitespaceCheck extends WhitespaceCheck {

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws IOException {

		StringBundler sb = new StringBundler();

		try (UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(new UnsyncStringReader(content))) {

			String line = null;
			String previousLine = StringPool.BLANK;

			int lineNumber = 0;

			while ((line = unsyncBufferedReader.readLine()) != null) {
				lineNumber++;

				if (line.startsWith(StringPool.TAB)) {
					line = StringUtil.replace(
						line, CharPool.TAB, StringPool.FOUR_SPACES);
				}

				if (line.contains(" \t")) {
					line = StringUtil.replace(
						line, " \t", " " + StringPool.FOUR_SPACES);
				}

				sb.append(line);
				sb.append("\n");

				if (!previousLine.matches("\\s+[^\\s#].*[,=]\\\\")) {
					previousLine = line;

					continue;
				}

				int leadingSpaceCount = _getLeadingSpaceCount(line);

				int expectedLeadingSpaceCount = _getLeadingSpaceCount(
					previousLine);

				if (previousLine.endsWith("=\\")) {
					expectedLeadingSpaceCount += 4;
				}

				if (leadingSpaceCount != expectedLeadingSpaceCount) {
					StringBundler sb2 = new StringBundler(5);

					sb2.append("Line starts with '");
					sb2.append(leadingSpaceCount);
					sb2.append("' spaces, but '");
					sb2.append(expectedLeadingSpaceCount);
					sb2.append("' spaces are expected");

					addMessage(fileName, sb2.toString(), lineNumber);
				}

				previousLine = line;
			}
		}

		content = sb.toString();

		return super.doProcess(fileName, absolutePath, content);
	}

	@Override
	protected boolean isAllowTrailingSpaces(String line) {
		String trimmedLine = StringUtil.removeChar(line, CharPool.SPACE);

		if (trimmedLine.endsWith(StringPool.EQUAL)) {
			return true;
		}

		return false;
	}

	private int _getLeadingSpaceCount(String line) {
		int leadingSpaceCount = 0;

		while (line.startsWith(StringPool.SPACE)) {
			line = line.substring(1);

			leadingSpaceCount++;
		}

		return leadingSpaceCount;
	}

}