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

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.ToolsUtil;
import com.liferay.source.formatter.checks.util.JavaSourceUtil;
import com.liferay.source.formatter.checkstyle.util.DetailASTUtil;
import com.liferay.source.formatter.parser.JavaClass;
import com.liferay.source.formatter.parser.JavaClassParser;
import com.liferay.source.formatter.parser.JavaMethod;
import com.liferay.source.formatter.parser.JavaSignature;
import com.liferay.source.formatter.parser.JavaTerm;
import com.liferay.source.formatter.util.FileUtil;
import com.liferay.source.formatter.util.SourceFormatterUtil;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Hugo Huijser
 */
public class ChainingCheck extends BaseCheck {

	@Override
	public int[] getDefaultTokens() {
		return new int[] {
			TokenTypes.CLASS_DEF, TokenTypes.ENUM_DEF, TokenTypes.INTERFACE_DEF
		};
	}

	public void setAllowedClassNames(String allowedClassNames) {
		_allowedClassNames = ArrayUtil.append(
			_allowedClassNames, StringUtil.split(allowedClassNames));
	}

	public void setAllowedMethodNames(String allowedMethodNames) {
		_allowedMethodNames = ArrayUtil.append(
			_allowedMethodNames, StringUtil.split(allowedMethodNames));
	}

	public void setAllowedMockitoMethodNames(String allowedMockitoMethodNames) {
		_allowedMockitoMethodNames = ArrayUtil.append(
			_allowedMockitoMethodNames,
			StringUtil.split(allowedMockitoMethodNames));
	}

	public void setAllowedVariableTypeNames(String allowedVariableTypeNames) {
		_allowedVariableTypeNames = ArrayUtil.append(
			_allowedVariableTypeNames,
			StringUtil.split(allowedVariableTypeNames));
	}

	public void setBaseDirName(String baseDirName) {
		_baseDirName = baseDirName;
	}

	public void setRequiredChainingClassFileNames(
		String requiredChainingClassFileNames) {

		_requiredChainingClassFileNames = ArrayUtil.append(
			_requiredChainingClassFileNames,
			StringUtil.split(requiredChainingClassFileNames));
	}

	@Override
	protected void doVisitToken(DetailAST detailAST) {
		DetailAST parentDetailAST = detailAST.getParent();

		if (parentDetailAST != null) {
			return;
		}

		List<DetailAST> methodCallDetailASTList =
			DetailASTUtil.getAllChildTokens(
				detailAST, true, TokenTypes.METHOD_CALL);

		for (DetailAST methodCallDetailAST : methodCallDetailASTList) {
			DetailAST dotDetailAST = methodCallDetailAST.findFirstToken(
				TokenTypes.DOT);

			if (dotDetailAST != null) {
				List<DetailAST> childMethodCallDetailASTList =
					DetailASTUtil.getAllChildTokens(
						dotDetailAST, false, TokenTypes.METHOD_CALL);

				// Only check the method that is first in the chain

				if (!childMethodCallDetailASTList.isEmpty()) {
					continue;
				}
			}

			_checkAllowedChaining(methodCallDetailAST);

			List<String> chain = _getChain(methodCallDetailAST);

			_checkRequiredChaining(methodCallDetailAST, chain);

			int chainSize = chain.size();

			if (chainSize == 1) {
				continue;
			}

			if (chainSize == 2) {
				DetailAST elistDetailAST = methodCallDetailAST.findFirstToken(
					TokenTypes.ELIST);

				if ((elistDetailAST.getChildCount() == 0) &&
					(dotDetailAST == null)) {

					continue;
				}

				_checkMethodName(chain, "getClass", methodCallDetailAST);

				String name1 = chain.get(0);
				String name2 = chain.get(1);

				if (name1.equals("concat") && name2.equals("concat")) {
					continue;
				}
			}

			if (_isAllowedChainingMethodCall(
					methodCallDetailAST, chain, detailAST)) {

				continue;
			}

			int concatsCount = Collections.frequency(chain, "concat");

			if (concatsCount > 2) {
				log(methodCallDetailAST, _MSG_AVOID_TOO_MANY_CONCAT);

				continue;
			}

			if ((chainSize == 3) && (concatsCount == 2)) {
				continue;
			}

			log(
				methodCallDetailAST, _MSG_AVOID_CHAINING,
				DetailASTUtil.getMethodName(methodCallDetailAST));
		}
	}

	private void _checkAllowedChaining(DetailAST methodCallDetailAST) {
		DetailAST parentDetailAST = methodCallDetailAST.getParent();

		while (true) {
			if ((parentDetailAST.getType() == TokenTypes.DOT) ||
				(parentDetailAST.getType() == TokenTypes.METHOD_CALL)) {

				parentDetailAST = parentDetailAST.getParent();

				continue;
			}

			break;
		}

		if (parentDetailAST.getType() != TokenTypes.EXPR) {
			return;
		}

		parentDetailAST = parentDetailAST.getParent();

		if (parentDetailAST.getType() != TokenTypes.ASSIGN) {
			return;
		}

		parentDetailAST = parentDetailAST.getParent();

		if (parentDetailAST.getType() != TokenTypes.VARIABLE_DEF) {
			return;
		}

		String classOrVariableName = _getClassOrVariableName(
			methodCallDetailAST);

		if (!Objects.equals(classOrVariableName, "Optional") &&
			!Objects.equals(classOrVariableName, "Stream") &&
			!Objects.equals(classOrVariableName, "Try")) {

			return;
		}

		DetailAST variableNameDetailAST = parentDetailAST.findFirstToken(
			TokenTypes.IDENT);

		String variableName = variableNameDetailAST.getText();

		String variableTypeName = DetailASTUtil.getVariableTypeName(
			methodCallDetailAST, variableName, false);

		if (!classOrVariableName.equals(variableTypeName)) {
			return;
		}

		List<DetailAST> identDetailASTList = _getIdentDetailASTList(
			parentDetailAST.getParent(), variableName);

		if ((identDetailASTList.size() == 2) &&
			Objects.equals(
				_findFirstParent(
					identDetailASTList.get(0), TokenTypes.ELIST,
					TokenTypes.LITERAL_IF),
				_findFirstParent(
					identDetailASTList.get(1), TokenTypes.ELIST,
					TokenTypes.LITERAL_IF))) {

			log(
				methodCallDetailAST, _MSG_ALLOWED_CHAINING,
				StringBundler.concat(
					classOrVariableName, StringPool.PERIOD,
					DetailASTUtil.getMethodName(methodCallDetailAST)));
		}
	}

	private void _checkMethodName(
		List<String> chainedMethodNames, String methodName,
		DetailAST methodCallDetailAST) {

		String firstMethodName = chainedMethodNames.get(0);

		if (firstMethodName.equals(methodName) &&
			!_isInsideConstructorThisCall(methodCallDetailAST) &&
			!DetailASTUtil.hasParentWithTokenType(
				methodCallDetailAST, TokenTypes.SUPER_CTOR_CALL)) {

			log(methodCallDetailAST, _MSG_AVOID_CHAINING, methodName);
		}
	}

	private void _checkRequiredChaining(
		DetailAST methodCallDetailAST, List<String> chain) {

		String classOrVariableName = _getClassOrVariableName(
			methodCallDetailAST);

		if (classOrVariableName == null) {
			return;
		}

		String variableTypeName = DetailASTUtil.getVariableTypeName(
			methodCallDetailAST, classOrVariableName, false);

		List<String> requiredChainingMethodNames =
			_getRequiredChainingMethodNames(variableTypeName);

		if (requiredChainingMethodNames == null) {
			return;
		}

		String methodName = chain.get(chain.size() - 1);

		if (!requiredChainingMethodNames.contains(methodName)) {
			return;
		}

		DetailAST topLevelMethodCallDetailAST = methodCallDetailAST;

		while (true) {
			DetailAST parentDetailAST = topLevelMethodCallDetailAST.getParent();

			if (parentDetailAST.getType() != TokenTypes.DOT) {
				break;
			}

			parentDetailAST = parentDetailAST.getParent();

			if (parentDetailAST.getType() != TokenTypes.METHOD_CALL) {
				break;
			}

			topLevelMethodCallDetailAST = parentDetailAST;
		}

		DetailAST parentDetailAST = topLevelMethodCallDetailAST.getParent();

		if (parentDetailAST.getType() != TokenTypes.EXPR) {
			return;
		}

		DetailAST nextSiblingDetailAST = parentDetailAST.getNextSibling();

		if ((nextSiblingDetailAST == null) ||
			(nextSiblingDetailAST.getType() != TokenTypes.SEMI)) {

			return;
		}

		nextSiblingDetailAST = nextSiblingDetailAST.getNextSibling();

		if ((nextSiblingDetailAST == null) ||
			(nextSiblingDetailAST.getType() != TokenTypes.EXPR)) {

			return;
		}

		DetailAST nextMethodCallDetailAST =
			nextSiblingDetailAST.getFirstChild();

		if (nextMethodCallDetailAST.getType() != TokenTypes.METHOD_CALL) {
			return;
		}

		String nextClassOrVariableName = _getClassOrVariableName(
			nextMethodCallDetailAST);

		if (Objects.equals(classOrVariableName, nextClassOrVariableName)) {
			log(
				methodCallDetailAST, _MSG_REQUIRED_CHAINING,
				classOrVariableName + "." + methodName);
		}
	}

	private DetailAST _findFirstParent(DetailAST detailAST, int... types) {
		DetailAST parentDetailAST = detailAST.getParent();

		while (true) {
			if (parentDetailAST == null) {
				return null;
			}

			if (ArrayUtil.contains(types, parentDetailAST.getType())) {
				return parentDetailAST;
			}

			parentDetailAST = parentDetailAST.getParent();
		}
	}

	private List<String> _getChain(DetailAST methodCallDetailAST) {
		List<String> chain = new ArrayList<>();

		chain.add(DetailASTUtil.getMethodName(methodCallDetailAST));

		while (true) {
			DetailAST parentDetailAST = methodCallDetailAST.getParent();

			if (parentDetailAST.getType() != TokenTypes.DOT) {
				return chain;
			}

			DetailAST grandParentDetailAST = parentDetailAST.getParent();

			if (grandParentDetailAST.getType() != TokenTypes.METHOD_CALL) {
				DetailAST siblingDetailAST =
					methodCallDetailAST.getNextSibling();

				if (siblingDetailAST.getType() == TokenTypes.IDENT) {
					chain.add(siblingDetailAST.getText());
				}

				return chain;
			}

			methodCallDetailAST = grandParentDetailAST;

			chain.add(DetailASTUtil.getMethodName(methodCallDetailAST));
		}
	}

	private String _getClassOrVariableName(DetailAST methodCallDetailAST) {
		DetailAST dotDetailAST = methodCallDetailAST.findFirstToken(
			TokenTypes.DOT);

		if (dotDetailAST == null) {
			return null;
		}

		DetailAST firstChildDetailAST = dotDetailAST.getFirstChild();

		FullIdent fullIdent = null;

		if (firstChildDetailAST.getType() == TokenTypes.LITERAL_NEW) {
			fullIdent = FullIdent.createFullIdent(
				firstChildDetailAST.getFirstChild());
		}
		else {
			fullIdent = FullIdent.createFullIdent(dotDetailAST);
		}

		String s = fullIdent.getText();

		int x = s.lastIndexOf(CharPool.PERIOD);

		if (x == -1) {
			return s;
		}

		return s.substring(0, x);
	}

	private DetailAST _getGlobalVariableDefinitonDetailAST(
		DetailAST methodCallDetailAST) {

		DetailAST parentDetailAST = methodCallDetailAST.getParent();

		while (true) {
			if ((parentDetailAST == null) ||
				(parentDetailAST.getType() == TokenTypes.CTOR_DEF) ||
				(parentDetailAST.getType() == TokenTypes.METHOD_DEF)) {

				return null;
			}

			if (parentDetailAST.getType() == TokenTypes.VARIABLE_DEF) {
				DetailAST grandParentDetailAST = parentDetailAST.getParent();

				if (grandParentDetailAST.getType() == TokenTypes.OBJBLOCK) {
					return parentDetailAST;
				}

				return null;
			}

			parentDetailAST = parentDetailAST.getParent();
		}
	}

	private List<DetailAST> _getIdentDetailASTList(
		DetailAST detailAST, String name) {

		List<DetailAST> identDetailASTList = new ArrayList<>();

		for (DetailAST identDetailAST :
				DetailASTUtil.getAllChildTokens(
					detailAST, true, TokenTypes.IDENT)) {

			if (name.equals(identDetailAST.getText())) {
				identDetailASTList.add(identDetailAST);
			}
		}

		return identDetailASTList;
	}

	private DetailAST _getOuterMethodCallDetailAST(DetailAST detailAST) {
		while (true) {
			if ((detailAST.getType() != TokenTypes.DOT) &&
				(detailAST.getType() != TokenTypes.METHOD_CALL)) {

				return null;
			}

			DetailAST parentDetailAST = detailAST.getParent();

			if ((detailAST.getType() == TokenTypes.METHOD_CALL) &&
				(parentDetailAST.getType() != TokenTypes.DOT)) {

				break;
			}

			detailAST = parentDetailAST;
		}

		while (true) {
			DetailAST parentDetailAST = detailAST.getParent();

			if (parentDetailAST == null) {
				return null;
			}

			if (parentDetailAST.getType() == TokenTypes.METHOD_CALL) {
				detailAST = parentDetailAST;

				break;
			}

			detailAST = parentDetailAST;
		}

		while (true) {
			if ((detailAST.getType() != TokenTypes.DOT) &&
				(detailAST.getType() != TokenTypes.METHOD_CALL)) {

				return null;
			}

			DetailAST childDetailAST = detailAST.getFirstChild();

			if ((detailAST.getType() == TokenTypes.DOT) &&
				(childDetailAST.getType() != TokenTypes.METHOD_CALL)) {

				return detailAST.getParent();
			}

			detailAST = childDetailAST;
		}
	}

	private List<String> _getRequiredChainingMethodNames(String className) {
		if (_requiredChainingMethodNamesMap != null) {
			return _requiredChainingMethodNamesMap.get(className);
		}

		_requiredChainingMethodNamesMap = new HashMap<>();

		for (String requiredChainingClassFileName :
				_requiredChainingClassFileNames) {

			File file = SourceFormatterUtil.getFile(
				_baseDirName, requiredChainingClassFileName,
				ToolsUtil.PORTAL_MAX_DIR_LEVEL);

			if (file == null) {
				continue;
			}

			JavaClass javaClass = null;

			try {
				javaClass = JavaClassParser.parseJavaClass(
					requiredChainingClassFileName, FileUtil.read(file));
			}
			catch (Exception e) {
				continue;
			}

			List<String> requiredChainingMethodNames = new ArrayList<>();

			for (JavaTerm javaTerm : javaClass.getChildJavaTerms()) {
				if (!(javaTerm instanceof JavaMethod)) {
					continue;
				}

				JavaMethod javaMethod = (JavaMethod)javaTerm;

				if (javaMethod.getAccessModifier() !=
						JavaTerm.ACCESS_MODIFIER_PUBLIC) {

					continue;
				}

				JavaSignature javaSignature = javaMethod.getSignature();

				if (Objects.equals(
						javaClass.getName(), javaSignature.getReturnType())) {

					requiredChainingMethodNames.add(javaMethod.getName());
				}
			}

			_requiredChainingMethodNamesMap.put(
				javaClass.getName(), requiredChainingMethodNames);
		}

		return _requiredChainingMethodNamesMap.get(className);
	}

	private String _getReturnType(
		String methodName, DetailAST classDefinitionDetailAST) {

		List<DetailAST> methodDefinitionDetailASTList =
			DetailASTUtil.getAllChildTokens(
				classDefinitionDetailAST, true, TokenTypes.METHOD_DEF);

		for (DetailAST methodDefinitionDetailAST :
				methodDefinitionDetailASTList) {

			DetailAST nameDetailAST = methodDefinitionDetailAST.findFirstToken(
				TokenTypes.IDENT);

			if (methodName.equals(nameDetailAST.getText())) {
				return DetailASTUtil.getTypeName(
					methodDefinitionDetailAST, false);
			}
		}

		return null;
	}

	private boolean _isAllowedChainingMethodCall(
		DetailAST methodCallDetailAST, List<String> chainedMethodNames,
		DetailAST detailAST) {

		DetailAST globalVariableDefinitonDetailAST =
			_getGlobalVariableDefinitonDetailAST(methodCallDetailAST);

		if ((globalVariableDefinitonDetailAST != null) &&
			((detailAST.getType() != TokenTypes.CLASS_DEF) ||
			 _isInsideInnerClass(
				 globalVariableDefinitonDetailAST, detailAST))) {

			return true;
		}

		if (_isInsideConstructorThisCall(methodCallDetailAST) ||
			DetailASTUtil.hasParentWithTokenType(
				methodCallDetailAST, TokenTypes.SUPER_CTOR_CALL)) {

			return true;
		}

		for (String allowedMethodName : _allowedMethodNames) {
			if (chainedMethodNames.contains(allowedMethodName)) {
				return true;
			}
		}

		FileContents fileContents = getFileContents();

		String fileName = StringUtil.replace(
			fileContents.getFileName(), CharPool.BACK_SLASH, CharPool.SLASH);

		if (fileName.contains("/test/") ||
			fileName.contains("/testIntegration/")) {

			for (String allowedMockitoMethodName : _allowedMockitoMethodNames) {
				if (chainedMethodNames.contains(allowedMockitoMethodName)) {
					return true;
				}
			}
		}

		DetailAST dotDetailAST = methodCallDetailAST.findFirstToken(
			TokenTypes.DOT);

		if (dotDetailAST == null) {
			String className = JavaSourceUtil.getClassName(fileName);

			for (String allowedClassName : _allowedClassNames) {
				if (className.matches(allowedClassName)) {
					return true;
				}
			}

			String returnType = _getReturnType(
				chainedMethodNames.get(0), detailAST);

			if (returnType != null) {
				for (String allowedVariableTypeName :
						_allowedVariableTypeNames) {

					if (returnType.matches(allowedVariableTypeName)) {
						return true;
					}
				}
			}

			return false;
		}

		String classOrVariableName = _getClassOrVariableName(
			methodCallDetailAST);

		if (classOrVariableName != null) {
			if (_isLambdaVariable(methodCallDetailAST, classOrVariableName)) {
				return true;
			}

			for (String s :
					StringUtil.split(classOrVariableName, CharPool.PERIOD)) {

				for (String allowedClassName : _allowedClassNames) {
					if (s.matches("(?i)" + allowedClassName)) {
						return true;
					}
				}
			}

			String variableTypeName = DetailASTUtil.getVariableTypeName(
				methodCallDetailAST, classOrVariableName, false);

			if (Validator.isNotNull(variableTypeName)) {
				for (String allowedVariableTypeName :
						_allowedVariableTypeNames) {

					if (variableTypeName.matches(allowedVariableTypeName)) {
						return true;
					}
				}
			}
		}

		DetailAST outerMethodCallDetailAST = _getOuterMethodCallDetailAST(
			methodCallDetailAST);

		if (outerMethodCallDetailAST != null) {
			return _isAllowedChainingMethodCall(
				outerMethodCallDetailAST, _getChain(outerMethodCallDetailAST),
				detailAST);
		}

		return false;
	}

	private boolean _isInsideConstructorThisCall(
		DetailAST methodCallDetailAST) {

		DetailAST parentDetailAST = methodCallDetailAST.getParent();

		while (parentDetailAST != null) {
			String parentDetailASTText = parentDetailAST.getText();

			if ((parentDetailAST.getType() == TokenTypes.CTOR_CALL) &&
				parentDetailASTText.equals("this")) {

				return true;
			}

			parentDetailAST = parentDetailAST.getParent();
		}

		return false;
	}

	private boolean _isInsideInnerClass(
		DetailAST globalVariableDefinitonDetailAST,
		DetailAST outerClassDefinitionDetailAST) {

		DetailAST detailAST = DetailASTUtil.getParentWithTokenType(
			globalVariableDefinitonDetailAST, TokenTypes.CLASS_DEF,
			TokenTypes.ENUM_DEF, TokenTypes.INTERFACE_DEF,
			TokenTypes.LITERAL_NEW);

		if (!detailAST.equals(outerClassDefinitionDetailAST)) {
			return true;
		}

		return false;
	}

	private boolean _isLambdaVariable(
		DetailAST methodCallDetailAST, String variableName) {

		DetailAST parentDetailAST = methodCallDetailAST.getParent();

		while (parentDetailAST != null) {
			if (parentDetailAST.getType() != TokenTypes.LAMBDA) {
				parentDetailAST = parentDetailAST.getParent();

				continue;
			}

			DetailAST nameDetailAST = parentDetailAST.findFirstToken(
				TokenTypes.IDENT);

			if ((nameDetailAST != null) &&
				variableName.equals(nameDetailAST.getText())) {

				return true;
			}

			return false;
		}

		return false;
	}

	private static final String _MSG_ALLOWED_CHAINING = "chaining.allowed";

	private static final String _MSG_AVOID_CHAINING = "chaining.avoid";

	private static final String _MSG_AVOID_TOO_MANY_CONCAT =
		"concat.avoid.too.many";

	private static final String _MSG_REQUIRED_CHAINING = "chaining.required";

	private String[] _allowedClassNames = new String[0];
	private String[] _allowedMethodNames = new String[0];
	private String[] _allowedMockitoMethodNames = new String[0];
	private String[] _allowedVariableTypeNames = new String[0];
	private String _baseDirName;
	private String[] _requiredChainingClassFileNames = new String[0];
	private Map<String, List<String>> _requiredChainingMethodNamesMap;

}