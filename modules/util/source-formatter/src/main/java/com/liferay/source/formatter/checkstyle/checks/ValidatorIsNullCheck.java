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

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.source.formatter.checkstyle.util.DetailASTUtil;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.List;

/**
 * @author Hugo Huijser
 */
public class ValidatorIsNullCheck extends BaseCheck {

	@Override
	public int[] getDefaultTokens() {
		return new int[] {TokenTypes.CTOR_DEF, TokenTypes.METHOD_DEF};
	}

	@Override
	protected void doVisitToken(DetailAST detailAST) {
		_checkMethod(detailAST, "Validator", "isNotNull");
		_checkMethod(detailAST, "Validator", "isNull");
	}

	private void _checkMethod(
		DetailAST detailAST, String className, String methodName) {

		List<DetailAST> methodCallDetailASTList = DetailASTUtil.getMethodCalls(
			detailAST, className, methodName);

		for (DetailAST methodCallDetailAST : methodCallDetailASTList) {
			DetailAST elistDetailAST = methodCallDetailAST.findFirstToken(
				TokenTypes.ELIST);

			DetailAST expressionDetailAST = elistDetailAST.findFirstToken(
				TokenTypes.EXPR);

			DetailAST childDetailAST = expressionDetailAST.getFirstChild();

			if (childDetailAST.getType() == TokenTypes.NUM_INT) {
				log(
					methodCallDetailAST, _MSG_INVALID_METHOD_NAME,
					StringBundler.concat(className, ".", methodName, "(long)"));

				continue;
			}

			if (childDetailAST.getType() != TokenTypes.IDENT) {
				continue;
			}

			DetailAST typeDetailAST = DetailASTUtil.getVariableTypeDetailAST(
				methodCallDetailAST, childDetailAST.getText());

			if (typeDetailAST == null) {
				continue;
			}

			childDetailAST = typeDetailAST.getFirstChild();

			if ((childDetailAST.getType() == TokenTypes.LITERAL_INT) ||
				(childDetailAST.getType() == TokenTypes.LITERAL_LONG)) {

				log(
					methodCallDetailAST, _MSG_INVALID_METHOD_NAME,
					StringBundler.concat(className, ".", methodName, "(long)"));

				continue;
			}

			String typeName = DetailASTUtil.getTypeName(typeDetailAST, true);

			if (Validator.isNotNull(typeName) && !typeName.equals("Long") &&
				!typeName.equals("Object") &&
				!typeName.equals("Serializable") &&
				!typeName.equals("String")) {

				log(
					methodCallDetailAST, _MSG_RESERVED_METHOD,
					StringBundler.concat(className, ".", methodName));
			}
		}
	}

	private static final String _MSG_INVALID_METHOD_NAME = "method.invalidName";

	private static final String _MSG_RESERVED_METHOD = "method.reserved";

}