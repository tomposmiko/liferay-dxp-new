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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alan Huang
 * @author Hugo Huijser
 */
public class PropertiesStylingCheck extends BaseFileCheck {

	@Override
	protected String doProcess(
		String fileName, String absolutePath, String content) {

		content = content.replaceAll("(\n\n)( *#+)( [^#\\s])", "$1$2\n$2$3");

		content = content.replaceAll(
			"(\n)( *#+)( [^#\\s].*\n)(?!\\2([ \n]|\\Z))", "$1$2$3$2\n");

		content = content.replaceAll(
			"(\\A|(?<!\\\\)\n)( *[\\w.-]+)(( +=)|(= +))(.*)(\\Z|\n)",
			"$1$2=$6$7");

		content = content.replaceAll("(?m)^(.*,) +(\\\\)$", "$1$2");

		Matcher matcher = _sqlPattern1.matcher(content);

		while (matcher.find()) {
			String match = matcher.group();
			String indent = matcher.group(1);

			String sqlClause = matcher.group(3);

			sqlClause = sqlClause.replaceAll(" AND (?=\\()", " AND \\\\\n");
			sqlClause = sqlClause.replaceAll(" OR (?=\\()", " OR \\\\\n");
			sqlClause = sqlClause.replaceAll("\\((?=\\()", "(\\\\\n");
			sqlClause = sqlClause.replaceAll("\\)(?=\\))", ")\\\\\n");

			String[] sqlClauses = sqlClause.split("\n");

			String replacement = StringBundler.concat(
				indent, matcher.group(2), "\\\n",
				_formatSQLClause(indent + StringPool.FOUR_SPACES, sqlClauses));

			return StringUtil.replaceFirst(
				content, match, replacement, matcher.start());
		}

		matcher = _sqlPattern2.matcher(content);

		while (matcher.find()) {
			int lineNumber = getLineNumber(content, matcher.start());

			if (Validator.isNull(matcher.group(4))) {
				continue;
			}

			String nextSQLClause = _getSQLClause(
				SourceUtil.getLine(content, lineNumber + 1));

			if (nextSQLClause == null) {
				continue;
			}

			String sqlClause = matcher.group(1);

			if (sqlClause.compareTo(nextSQLClause) > 0) {
				content = StringUtil.replaceFirst(
					content, nextSQLClause, sqlClause,
					getLineStartPos(content, lineNumber + 1));

				return StringUtil.replaceFirst(
					content, sqlClause, nextSQLClause,
					getLineStartPos(content, lineNumber));
			}
		}

		return content;
	}

	private String _formatSQLClause(String indent, String[] sqlClauses) {
		StringBundler sb = new StringBundler(sqlClauses.length * 3);

		for (String sqlClause : sqlClauses) {
			if (sqlClause.startsWith(")")) {
				indent = indent.substring(4);
			}

			sb.append(indent);
			sb.append(sqlClause);
			sb.append("\n");

			if (sqlClause.equals("(\\")) {
				indent = indent + StringPool.FOUR_SPACES;
			}
		}

		if (sb.length() > 0) {
			sb.setIndex(sb.index() - 1);
		}

		return sb.toString();
	}

	private String _getSQLClause(String line) {
		Matcher matcher = _sqlPattern2.matcher(line);

		if (matcher.find()) {
			return matcher.group(1);
		}

		return null;
	}

	private static final Pattern _sqlPattern1 = Pattern.compile(
		"(?<=\n)( +)(test.batch.run.property.query.+]=)([^\\\\].+)");
	private static final Pattern _sqlPattern2 = Pattern.compile(
		"\\s(\\(.* ([!=]=|~) .+\\))( (AND|OR) )?(\\\\)?");

}