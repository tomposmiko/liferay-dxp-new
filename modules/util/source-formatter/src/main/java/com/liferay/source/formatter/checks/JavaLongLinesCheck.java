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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.ToolsUtil;
import com.liferay.source.formatter.checks.util.JavaSourceUtil;
import com.liferay.source.formatter.checks.util.SourceUtil;

import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hugo Huijser
 */
public class JavaLongLinesCheck extends BaseFileCheck {

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws IOException {

		if (fileName.endsWith("Table.java")) {
			return content;
		}

		try (UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(new UnsyncStringReader(content))) {

			String line = null;

			int lineNumber = 0;

			while ((line = unsyncBufferedReader.readLine()) != null) {
				lineNumber++;

				if (line.startsWith("import ") || line.startsWith("package ") ||
					line.startsWith(StringPool.SPACE) ||
					line.matches("\\s*\\*.*")) {

					continue;
				}

				int lineLength = getLineLength(line);

				if (lineLength <= getMaxLineLength()) {
					continue;
				}

				// Allow lines with long method names or long
				// extended/implemented class names

				if (line.matches("\t*(extends|implements) [\\w.]+ \\{") ||
					line.matches(
						"\t*(private|protected|public) void \\w+\\(\\)" +
							"( \\{)?")) {

					continue;
				}

				String trimmedLine = StringUtil.trimLeading(line);

				if (isExcludedPath(
						_LINE_LENGTH_EXCLUDES, absolutePath, lineNumber) ||
					_isAnnotationParameter(content, trimmedLine)) {

					continue;
				}

				if (trimmedLine.matches("@\\S+")) {
					continue;
				}

				String truncateLongLinesContent = _getTruncateLongLinesContent(
					content, line, trimmedLine, lineNumber);

				if ((truncateLongLinesContent != null) &&
					!truncateLongLinesContent.equals(content)) {

					return truncateLongLinesContent;
				}

				addMessage(fileName, "> " + getMaxLineLength(), lineNumber);
			}
		}

		return content;
	}

	private int _getIfClauseLineBreakPos(String line) {
		int x = line.lastIndexOf(" || ", getMaxLineLength() - 3);
		int y = line.lastIndexOf(" && ", getMaxLineLength() - 3);

		int z = Math.max(x, y);

		if ((z != -1) && !ToolsUtil.isInsideQuotes(line, z)) {
			return z + 3;
		}

		if (!line.endsWith(" ||") && !line.endsWith(" &&") &&
			!line.endsWith(") {")) {

			return -1;
		}

		x = line.indexOf("= ");

		if ((x != -1) && !ToolsUtil.isInsideQuotes(line, x)) {
			return x + 1;
		}

		x = line.indexOf("> ");

		if ((x != -1) && !ToolsUtil.isInsideQuotes(line, x)) {
			return x + 1;
		}

		x = line.indexOf("< ");

		if ((x != -1) && !ToolsUtil.isInsideQuotes(line, x)) {
			return x + 1;
		}

		for (x = getMaxLineLength() + 1;;) {
			x = line.lastIndexOf(StringPool.COMMA_AND_SPACE, x - 1);

			if (x == -1) {
				break;
			}

			if (ToolsUtil.isInsideQuotes(line, x)) {
				continue;
			}

			String linePart = line.substring(0, x);

			if (getLevel(linePart) == 0) {
				return x + 1;
			}
		}

		for (x = 0;;) {
			x = line.indexOf(CharPool.OPEN_PARENTHESIS, x + 1);

			if (x == -1) {
				break;
			}

			if (ToolsUtil.isInsideQuotes(line, x)) {
				continue;
			}

			if (Character.isLetterOrDigit(line.charAt(x - 1)) &&
				(line.charAt(x + 1) != CharPool.CLOSE_PARENTHESIS)) {

				return x + 1;
			}
		}

		x = line.indexOf(CharPool.PERIOD);

		if ((x != -1) && !ToolsUtil.isInsideQuotes(line, x)) {
			String linePart = StringUtil.trim(line.substring(0, x));

			if (!linePart.contains(StringPool.SPACE)) {
				return x + 1;
			}
		}

		return -1;
	}

	private String _getTruncateLongLinesContent(
		String content, String line, String trimmedLine, int lineNumber) {

		String indent = SourceUtil.getIndent(line);

		if (trimmedLine.matches("\\w+\\.\\w+[,);]*")) {
			int x = line.indexOf(StringPool.PERIOD);

			String firstLine = line.substring(0, x + 1);
			String secondLine = indent + StringPool.TAB + line.substring(x + 1);

			return StringUtil.replace(
				content, "\n" + line + "\n",
				StringBundler.concat("\n", firstLine, "\n", secondLine, "\n"));
		}

		if (line.endsWith(StringPool.OPEN_PARENTHESIS) ||
			line.endsWith(StringPool.SEMICOLON)) {

			int x = line.indexOf(" = ");

			if (x != -1) {
				String firstLine = line.substring(0, x + 2);

				if (firstLine.contains(StringPool.QUOTE)) {
					return null;
				}

				String secondLine =
					indent + StringPool.TAB + line.substring(x + 3);

				if (line.endsWith(StringPool.SEMICOLON)) {
					return StringUtil.replace(
						content, "\n" + line + "\n",
						StringBundler.concat(
							"\n", firstLine, "\n", secondLine, "\n"));
				}
				else if (Validator.isNotNull(
							getLine(content, lineNumber + 1))) {

					return StringUtil.replace(
						content, "\n" + line + "\n",
						StringBundler.concat(
							"\n", firstLine, "\n", secondLine, "\n",
							StringPool.TAB));
				}
			}
		}

		if (line.endsWith(StringPool.CLOSE_PARENTHESIS) ||
			line.endsWith(StringPool.COMMA) ||
			line.endsWith(StringPool.OPEN_CURLY_BRACE) ||
			line.endsWith(StringPool.SEMICOLON)) {

			int x = 0;

			while (true) {
				x = line.indexOf(", ", x + 1);

				if (x == -1) {
					break;
				}

				if (!ToolsUtil.isInsideQuotes(line, x) &&
					JavaSourceUtil.isValidJavaParameter(line.substring(0, x))) {

					String firstLine = line.substring(0, x + 1);
					String secondLine = indent + line.substring(x + 2);

					return StringUtil.replace(
						content, "\n" + line + "\n",
						StringBundler.concat(
							"\n", firstLine, "\n", secondLine, "\n"));
				}
			}
		}

		if ((line.endsWith(StringPool.OPEN_CURLY_BRACE) ||
			 line.endsWith(StringPool.SEMICOLON)) &&
			(trimmedLine.startsWith("private ") ||
			 trimmedLine.startsWith("protected ") ||
			 trimmedLine.startsWith("public "))) {

			int x = line.indexOf(" throws ");

			if (x != -1) {
				String firstLine = line.substring(0, x);
				String secondLine =
					indent + StringPool.TAB + line.substring(x + 1);

				return StringUtil.replace(
					content, "\n" + line + "\n",
					StringBundler.concat(
						"\n", firstLine, "\n", secondLine, "\n"));
			}
		}

		if ((line.endsWith(StringPool.CLOSE_PARENTHESIS) ||
			 line.endsWith(StringPool.OPEN_CURLY_BRACE)) &&
			(trimmedLine.startsWith("private ") ||
			 trimmedLine.startsWith("protected ") ||
			 trimmedLine.startsWith("public "))) {

			int x = line.indexOf(CharPool.OPEN_PARENTHESIS);

			if ((x != -1) &&
				(line.charAt(x + 1) != CharPool.CLOSE_PARENTHESIS)) {

				String secondLineIndent = indent + StringPool.TAB;

				if (line.endsWith(StringPool.CLOSE_PARENTHESIS)) {
					secondLineIndent += StringPool.TAB;
				}

				String firstLine = line.substring(0, x + 1);
				String secondLine = secondLineIndent + line.substring(x + 1);

				return StringUtil.replace(
					content, "\n" + line + "\n",
					StringBundler.concat(
						"\n", firstLine, "\n", secondLine, "\n"));
			}
		}

		if (line.endsWith(StringPool.SEMICOLON)) {
			int x = line.indexOf(CharPool.OPEN_PARENTHESIS);

			if (x != -1) {
				char c = line.charAt(x - 1);

				if ((c != CharPool.SPACE) && (c != CharPool.TAB) &&
					(line.charAt(x + 1) != CharPool.CLOSE_PARENTHESIS)) {

					String firstLine = line.substring(0, x + 1);

					if (firstLine.contains(StringPool.QUOTE)) {
						return null;
					}

					String secondLine =
						indent + StringPool.TAB + line.substring(x + 1);

					return StringUtil.replace(
						content, "\n" + line + "\n",
						StringBundler.concat(
							"\n", firstLine, "\n", secondLine, "\n"));
				}
			}
			else {
				x = line.lastIndexOf(StringPool.SPACE);

				if ((x != -1) && !ToolsUtil.isInsideQuotes(line, x)) {
					String firstLine = line.substring(0, x);
					String secondLine =
						indent + StringPool.TAB + line.substring(x + 1);

					if (getLineLength(secondLine) <= getMaxLineLength()) {
						return StringUtil.replace(
							content, "\n" + line + "\n",
							StringBundler.concat(
								"\n", firstLine, "\n", secondLine, "\n"));
					}
				}
			}
		}

		if (line.contains(StringPool.TAB + "for (") && line.endsWith(" {")) {
			int x = line.indexOf(" : ");

			if (x != -1) {
				String firstLine = line.substring(0, x + 2);
				String secondLine = StringBundler.concat(
					indent, StringPool.TAB, StringPool.TAB,
					line.substring(x + 3));

				return StringUtil.replace(
					content, "\n" + line + "\n",
					StringBundler.concat(
						"\n", firstLine, "\n", secondLine, "\n\n"));
			}
		}

		Matcher matcher = _annotationPattern2.matcher(trimmedLine);

		if (matcher.find() && (getLevel(line) == 0)) {
			StringBundler sb = new StringBundler();

			sb.append(indent);
			sb.append(matcher.group(1));
			sb.append("\n");

			int x = -1;

			int y = matcher.end(1) - 1;

			while (true) {
				x = trimmedLine.indexOf(StringPool.COMMA, x + 1);

				if (x == -1) {
					sb.append(indent);
					sb.append("\t");
					sb.append(
						trimmedLine.substring(y + 1, trimmedLine.length() - 1));
					sb.append("\n");
					sb.append(indent);
					sb.append(")");

					break;
				}

				if (ToolsUtil.isInsideQuotes(trimmedLine, x) ||
					(getLevel(trimmedLine.substring(0, x), "{", "}") != 0)) {

					continue;
				}

				sb.append(indent);
				sb.append("\t");
				sb.append(trimmedLine.substring(y + 1, x + 1));
				sb.append("\n");

				y = x + 1;
			}

			return StringUtil.replace(
				content, "\n" + line + "\n", "\n" + sb.toString() + "\n");
		}

		int i = _getIfClauseLineBreakPos(line);

		if (i == -1) {
			return null;
		}

		String firstLine = line.substring(0, i);
		String secondLine = indent + line.substring(i);

		if (secondLine.endsWith(") {")) {
			return StringUtil.replace(
				content, "\n" + line + "\n",
				StringBundler.concat(
					"\n", firstLine, "\n", secondLine, "\n\n"));
		}

		return StringUtil.replace(
			content, "\n" + line + "\n",
			StringBundler.concat("\n", firstLine, "\n", secondLine, "\n"));
	}

	private boolean _isAnnotationParameter(String content, String line) {
		int x = -1;

		while (true) {
			x = line.indexOf(StringPool.COMMA_AND_SPACE, x + 1);

			if (x == -1) {
				break;
			}

			if (!ToolsUtil.isInsideQuotes(line, x)) {
				return false;
			}
		}

		Matcher matcher = _annotationPattern1.matcher(content);

		while (matcher.find()) {
			x = matcher.end();

			while (true) {
				x = content.indexOf(StringPool.CLOSE_PARENTHESIS, x + 1);

				if (x == -1) {
					break;
				}

				String annotationParameters = content.substring(
					matcher.end() - 2, x + 1);

				if (getLevel(annotationParameters) == 0) {
					if (annotationParameters.contains(line)) {
						return true;
					}

					break;
				}
			}
		}

		return false;
	}

	private static final String _LINE_LENGTH_EXCLUDES = "line.length.excludes";

	private static final Pattern _annotationPattern1 = Pattern.compile(
		"\n\t*@(.+)\\(\n");
	private static final Pattern _annotationPattern2 = Pattern.compile(
		"^(@\\w+\\().*\\)");

}