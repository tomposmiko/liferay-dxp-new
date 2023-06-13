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

package com.liferay.poshi.runner.elements;

import java.util.List;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @author Kenji Heigel
 */
public class WhilePoshiElement extends IfPoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new WhilePoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String poshiScript) {

		if (_isElementType(poshiScript)) {
			return new WhilePoshiElement(parentPoshiElement, poshiScript);
		}

		return null;
	}

	protected WhilePoshiElement() {
	}

	protected WhilePoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected WhilePoshiElement(List<Attribute> attributes, List<Node> nodes) {
		super(_ELEMENT_NAME, attributes, nodes);
	}

	protected WhilePoshiElement(
		PoshiElement parentPoshiElement, String poshiScript) {

		super(_ELEMENT_NAME, parentPoshiElement, poshiScript);
	}

	@Override
	protected String getBlockName() {
		String parentheticalContent = getParentheticalContent(
			super.getBlockName());

		StringBuilder sb = new StringBuilder();

		sb.append(getPoshiScriptKeyword());
		sb.append(" (");
		sb.append(parentheticalContent);

		if (attributeValue("max-iterations") != null) {
			sb.append(" && (maxIterations = \"");
			sb.append(attributeValue("max-iterations"));
			sb.append("\")");
		}

		sb.append(")");

		return sb.toString();
	}

	@Override
	protected String getCondition(String poshiScript) {
		String parentheticalContent = getParentheticalContent(poshiScript);

		if (parentheticalContent.contains("&& (maxIterations = ")) {
			int index = parentheticalContent.lastIndexOf("&&");

			String maxIterationsAssignment = parentheticalContent.substring(
				index + 2);

			maxIterationsAssignment = getParentheticalContent(
				maxIterationsAssignment);

			String maxIterationsValue = getValueFromAssignment(
				maxIterationsAssignment);

			addAttribute(
				"max-iterations", getQuotedContent(maxIterationsValue));

			parentheticalContent = parentheticalContent.substring(0, index);
		}

		return parentheticalContent.trim();
	}

	private boolean _isElementType(String poshiScript) {
		return isValidPoshiScriptBlock(_blockNamePattern, poshiScript);
	}

	private static final String _ELEMENT_NAME = "while";

	private static final String _POSHI_SCRIPT_KEYWORD = _ELEMENT_NAME;

	private static final Pattern _blockNamePattern = Pattern.compile(
		"^" + _POSHI_SCRIPT_KEYWORD + BLOCK_NAME_PARAMETER_REGEX,
		Pattern.DOTALL);

}