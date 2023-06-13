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

package com.liferay.poshi.runner.script;

import com.liferay.poshi.runner.elements.PoshiElement;
import com.liferay.poshi.runner.elements.PoshiNode;
import com.liferay.poshi.runner.util.StringUtil;
import com.liferay.poshi.runner.util.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kenji Heigel
 */
public class PoshiScriptParserException extends Exception {

	public static final String TRANSLATION_LOSS_MESSAGE =
		"Poshi Script syntax is not preserved in translation";

	public static List<String> getFailingFilePaths() {
		return failingFilePaths;
	}

	public PoshiScriptParserException(String msg) {
		super(msg);
	}

	public PoshiScriptParserException(String msg, PoshiNode poshiNode) {
		super(msg);

		setErrorLineNumber(poshiNode.getPoshiScriptLineNumber());
		setFilePath(poshiNode.getFilePath());
		setPoshiNode(poshiNode);
	}

	public PoshiScriptParserException(
		String msg, String poshiScript, PoshiNode parentPoshiNode) {

		super(msg);

		setFilePath(parentPoshiNode.getFilePath());

		setPoshiNode(parentPoshiNode);

		String parentPoshiScript = parentPoshiNode.getPoshiScript();

		parentPoshiScript = parentPoshiScript.replaceFirst("^[\\n\\r]*", "");

		int startingLineNumber = parentPoshiNode.getPoshiScriptLineNumber();

		if (parentPoshiNode instanceof PoshiElement) {
			PoshiElement parentPoshiElement = (PoshiElement)parentPoshiNode;

			startingLineNumber = parentPoshiElement.getPoshiScriptLineNumber(
				true);
		}

		int index = parentPoshiScript.indexOf(poshiScript.trim());

		setErrorLineNumber(
			startingLineNumber +
				StringUtil.count(parentPoshiScript, "\n", index));
	}

	public int getErrorLineNumber() {
		return _errorLineNumber;
	}

	public String getErrorSnippet() {
		PoshiElement rootPoshiElement = getRootPoshiElement(getPoshiNode());

		int startingLineNumber = 1;
		int errorLineNumber = getErrorLineNumber();

		startingLineNumber = Math.max(
			errorLineNumber - _ERROR_SNIPPET_PREFIX_SIZE, 0);

		String poshiScript = rootPoshiElement.getPoshiScript();

		String[] lines = poshiScript.split("\n");

		int endingLineNumber = lines.length;

		endingLineNumber = Math.min(
			errorLineNumber + _ERROR_SNIPPET_POSTFIX_SIZE, endingLineNumber);

		StringBuilder sb = new StringBuilder();

		int currentLineNumber = startingLineNumber;

		String lineNumberString = String.valueOf(errorLineNumber);

		int pad = lineNumberString.length() + 2;

		while (currentLineNumber <= endingLineNumber) {
			StringBuilder prefix = new StringBuilder();

			if (currentLineNumber == errorLineNumber) {
				prefix.append(">");
			}
			else {
				prefix.append(" ");
			}

			prefix.append(" ");

			prefix.append(currentLineNumber);

			sb.append(String.format("%" + pad + "s", prefix.toString()));
			sb.append(" |");

			String line = lines[currentLineNumber - 1];

			sb.append(line.replace("\t", "    "));

			sb.append("\n");

			currentLineNumber++;
		}

		return sb.toString();
	}

	public String getFilePath() {
		return _filePath;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();

		sb.append(super.getMessage());
		sb.append(" at:\n");
		sb.append(getFilePath());
		sb.append(":");
		sb.append(getErrorLineNumber());
		sb.append("\n");
		sb.append(getErrorSnippet());

		return sb.toString();
	}

	public PoshiNode getPoshiNode() {
		return _poshiNode;
	}

	public PoshiElement getRootPoshiElement(PoshiNode poshiNode) {
		if (Validator.isNotNull(poshiNode.getParent())) {
			PoshiElement parentPoshiElement =
				(PoshiElement)poshiNode.getParent();

			return getRootPoshiElement(parentPoshiElement);
		}

		return (PoshiElement)poshiNode;
	}

	public void setErrorLineNumber(int errorLineNumber) {
		_errorLineNumber = errorLineNumber;
	}

	public void setFilePath(String filePath) {
		_filePath = filePath;

		failingFilePaths.add(filePath);
	}

	public void setPoshiNode(PoshiNode poshiNode) {
		_poshiNode = poshiNode;
	}

	protected static List<String> failingFilePaths = new ArrayList<>();

	private static final int _ERROR_SNIPPET_POSTFIX_SIZE = 10;

	private static final int _ERROR_SNIPPET_PREFIX_SIZE = 10;

	private int _errorLineNumber;
	private String _filePath = "Unknown file";
	private PoshiNode _poshiNode;

}