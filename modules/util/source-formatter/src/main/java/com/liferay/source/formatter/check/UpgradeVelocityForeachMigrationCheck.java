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

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.source.formatter.check.constants.VelocityMigrationConstants;
import com.liferay.source.formatter.check.util.VelocityMigrationUtil;

/**
 * @author Nícolas Moura
 */
public class UpgradeVelocityForeachMigrationCheck
	extends BaseUpgradeVelocityMigrationCheck {

	@Override
	protected String migrateContent(String content) {
		String[] lines = StringUtil.splitLines(content);

		for (int i = 0; i < lines.length; i++) {
			String newLine = lines[i];

			if (newLine.contains(
					VelocityMigrationConstants.VELOCITY_FOREACH_START)) {

				newLine = VelocityMigrationUtil.removeFirstParenthesis(newLine);
				newLine = StringUtil.replace(
					newLine, VelocityMigrationConstants.VELOCITY_FOREACH_START,
					VelocityMigrationConstants.FREEMARKER_LIST_START);
				newLine = StringUtil.replace(
					newLine, "in",
					VelocityMigrationConstants.FREEMARKER_LIST_SEPARATOR);
				newLine = StringUtil.replaceLast(
					newLine, CharPool.CLOSE_PARENTHESIS, CharPool.GREATER_THAN);

				newLine = _changeForeachDeclarationOrder(newLine);

				VelocityMigrationUtil.replaceStatementEnd(
					i, lines,
					VelocityMigrationConstants.VELOCITY_FOREACH_START);

				lines[i] = newLine;
			}
		}

		return com.liferay.petra.string.StringUtil.merge(
			lines, StringPool.NEW_LINE);
	}

	private static String _changeForeachDeclarationOrder(String line) {
		String firstArgument = line.substring(
			line.indexOf(VelocityMigrationConstants.FREEMARKER_LIST_START) +
				VelocityMigrationConstants.FREEMARKER_LIST_START.length() + 1,
			line.indexOf(VelocityMigrationConstants.FREEMARKER_LIST_SEPARATOR) -
				1);
		String secondArgument = line.substring(
			line.indexOf(VelocityMigrationConstants.FREEMARKER_LIST_SEPARATOR) +
				VelocityMigrationConstants.FREEMARKER_LIST_SEPARATOR.length() +
					1,
			line.indexOf(CharPool.GREATER_THAN));

		String newLine = StringUtil.replaceFirst(
			line, firstArgument, secondArgument);

		return StringUtil.replaceLast(newLine, secondArgument, firstArgument);
	}

}