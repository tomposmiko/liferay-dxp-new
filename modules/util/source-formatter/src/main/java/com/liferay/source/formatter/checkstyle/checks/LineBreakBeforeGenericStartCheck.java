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

package com.liferay.source.formatter.checkstyle.checks;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.source.formatter.checkstyle.util.DetailASTUtil;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * @author Hugo Huijser
 */
public class LineBreakBeforeGenericStartCheck extends BaseCheck {

	@Override
	public int[] getDefaultTokens() {
		return new int[] {TokenTypes.GENERIC_START};
	}

	@Override
	protected void doVisitToken(DetailAST detailAST) {
		if (DetailASTUtil.isAtLineStart(
				detailAST,
				getLine(DetailASTUtil.getStartLineNumber(detailAST) - 1))) {

			return;
		}

		DetailAST nextSiblingDetailAST = detailAST.getNextSibling();

		while (true) {
			if (nextSiblingDetailAST == null) {
				return;
			}

			if (nextSiblingDetailAST.getType() != TokenTypes.GENERIC_END) {
				nextSiblingDetailAST = nextSiblingDetailAST.getNextSibling();

				continue;
			}

			if (detailAST.getLineNo() == nextSiblingDetailAST.getLineNo()) {
				return;
			}

			String line = getLine(detailAST.getLineNo() - 1);

			String s = StringUtil.trim(
				line.substring(0, detailAST.getColumnNo()));

			log(detailAST, _MSG_INCORRECT_LINE_BREAK, s);

			return;
		}
	}

	private static final String _MSG_INCORRECT_LINE_BREAK =
		"line.break.incorrect";

}