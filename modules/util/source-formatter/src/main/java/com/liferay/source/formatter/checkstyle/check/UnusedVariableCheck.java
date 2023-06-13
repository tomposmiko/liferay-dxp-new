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

package com.liferay.source.formatter.checkstyle.check;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtil;

import java.util.List;

/**
 * @author Hugo Huijser
 */
public class UnusedVariableCheck extends BaseCheck {

	@Override
	public int[] getDefaultTokens() {
		return new int[] {TokenTypes.VARIABLE_DEF};
	}

	@Override
	protected void doVisitToken(DetailAST detailAST) {
		DetailAST parentDetailAST = detailAST.getParent();

		if (parentDetailAST.getType() == TokenTypes.OBJBLOCK) {
			parentDetailAST = parentDetailAST.getParent();

			if (parentDetailAST.getParent() != null) {
				return;
			}
		}
		else if (parentDetailAST.getType() != TokenTypes.SLIST) {
			return;
		}

		DetailAST modifiersDetailAST = detailAST.findFirstToken(
			TokenTypes.MODIFIERS);

		if (modifiersDetailAST.branchContains(TokenTypes.LITERAL_PROTECTED) ||
			modifiersDetailAST.branchContains(TokenTypes.LITERAL_PUBLIC)) {

			return;
		}

		if (modifiersDetailAST.branchContains(TokenTypes.ANNOTATION)) {
			if (!isAttributeValue(_CHECK_UNUSED_REFERENCE_VARIABLE_KEY)) {
				return;
			}

			String absolutePath = getAbsolutePath();

			int x = absolutePath.indexOf("/modules/");

			if (x == -1) {
				return;
			}

			String modulePath = absolutePath.substring(x + 1);

			if ((!modulePath.startsWith("modules/apps") &&
				 !modulePath.startsWith("modules/dxp")) ||
				!AnnotationUtil.containsAnnotation(detailAST, "Reference")) {

				return;
			}

			List<String> checkUnusedReferenceVariableDirNames =
				getAttributeValues(
					_CHECK_UNUSED_REFERENCE_VARIABLE_DIR_NAMES_KEY);

			if (!checkUnusedReferenceVariableDirNames.isEmpty()) {
				int i = 0;

				while (i < checkUnusedReferenceVariableDirNames.size()) {
					if (modulePath.startsWith(
							checkUnusedReferenceVariableDirNames.get(i))) {

						break;
					}

					i++;
				}

				if (i == checkUnusedReferenceVariableDirNames.size()) {
					return;
				}
			}

			DetailAST typeDetailAST = detailAST.findFirstToken(TokenTypes.TYPE);

			DetailAST firstChildDetailAST = typeDetailAST.getFirstChild();

			if (firstChildDetailAST == null) {
				return;
			}

			if (firstChildDetailAST.getType() != TokenTypes.DOT) {
				String variableTypeName = getTypeName(typeDetailAST, false);

				List<String> allowedUnusedVariableTypeNames =
					getAttributeValues(_ALLOWED_UNUSED_VARIABLE_TYPE_NAMES_KEY);

				if (allowedUnusedVariableTypeNames.contains(variableTypeName)) {
					return;
				}
			}
		}

		String variableName = getName(detailAST);

		if (variableName.equals("serialVersionUID")) {
			return;
		}

		List<DetailAST> variableCallerDetailASTList =
			getVariableCallerDetailASTList(detailAST, variableName);

		if (variableCallerDetailASTList.isEmpty()) {
			log(detailAST, _MSG_UNUSED_VARIABLE, variableName);

			return;
		}

		for (DetailAST variableCallerDetailAST : variableCallerDetailASTList) {
			if (_isInsideConstructor(variableCallerDetailAST) ||
				!_isInsidePrivateMethod(variableCallerDetailAST)) {

				return;
			}

			DetailAST previousSiblingDetailAST =
				variableCallerDetailAST.getPreviousSibling();

			if (previousSiblingDetailAST != null) {
				return;
			}

			parentDetailAST = variableCallerDetailAST.getParent();

			if (parentDetailAST.getType() != TokenTypes.ASSIGN) {
				return;
			}

			parentDetailAST = parentDetailAST.getParent();

			if (parentDetailAST.getType() != TokenTypes.EXPR) {
				return;
			}
		}

		log(detailAST, _MSG_UNUSED_VARIABLE_VALUE, variableName);
	}

	private boolean _isInsideConstructor(DetailAST detailAST) {
		DetailAST parentDetailAST = detailAST.getParent();

		while (parentDetailAST != null) {
			if (parentDetailAST.getType() == TokenTypes.CTOR_DEF) {
				return true;
			}

			parentDetailAST = parentDetailAST.getParent();
		}

		return false;
	}

	private boolean _isInsidePrivateMethod(DetailAST detailAST) {
		DetailAST parentDetailAST = detailAST.getParent();

		while (parentDetailAST != null) {
			if (parentDetailAST.getType() == TokenTypes.METHOD_DEF) {
				DetailAST modifiersDetailAST = parentDetailAST.findFirstToken(
					TokenTypes.MODIFIERS);

				if (modifiersDetailAST.branchContains(
						TokenTypes.LITERAL_PRIVATE)) {

					return true;
				}
			}

			parentDetailAST = parentDetailAST.getParent();
		}

		return false;
	}

	private static final String _ALLOWED_UNUSED_VARIABLE_TYPE_NAMES_KEY =
		"allowedUnusedVariableTypeNames";

	private static final String _CHECK_UNUSED_REFERENCE_VARIABLE_DIR_NAMES_KEY =
		"checkUnusedReferenceVariableDirNames";

	private static final String _CHECK_UNUSED_REFERENCE_VARIABLE_KEY =
		"checkUnusedReferenceVariable";

	private static final String _MSG_UNUSED_VARIABLE = "variable.unused";

	private static final String _MSG_UNUSED_VARIABLE_VALUE =
		"variable.value.unused";

}