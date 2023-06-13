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

import com.liferay.poshi.runner.util.Dom4JUtil;
import com.liferay.poshi.runner.util.Validator;

import java.io.IOException;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @author Kenji Heigel
 */
public class VarPoshiElement extends PoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new VarPoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String readableSyntax) {

		if (_isElementType(readableSyntax)) {
			return new VarPoshiElement(parentPoshiElement, readableSyntax);
		}

		return null;
	}

	public String getVarValue() {
		if (valueAttributeName == null) {
			for (Node node : Dom4JUtil.toNodeList(content())) {
				if (node instanceof CDATA) {
					StringBuilder sb = new StringBuilder();

					sb.append("\'\'\'");
					sb.append(node.getText());
					sb.append("\'\'\'");

					return sb.toString();
				}
			}
		}

		return attributeValue(valueAttributeName);
	}

	@Override
	public void parseReadableSyntax(String readableSyntax) {
		if (readableSyntax.startsWith("static")) {
			addAttribute("static", "true");

			readableSyntax = readableSyntax.replaceFirst("static", "");

			readableSyntax = readableSyntax.trim();
		}

		String name = getNameFromAssignment(readableSyntax);

		addAttribute("name", name);

		String value = getValueFromAssignment(readableSyntax);

		if (value.startsWith("\'\'\'")) {
			addCDATA(getReadableEscapedContent(value));

			return;
		}

		if (value.endsWith("\"") && value.startsWith("\"")) {
			value = getQuotedContent(value);

			value = value.replace("&quot;", "\"");

			addAttribute("value", value);

			return;
		}

		if (isValidUtilClassName(value) || value.startsWith("selenium.") ||
			value.startsWith("TestPropsUtil.")) {

			value = value.replaceFirst("\\.", "#");

			addAttribute("method", value);
		}
	}

	@Override
	public String toReadableSyntax() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\t");

		String staticAttribute = attributeValue("static");

		if (staticAttribute != null) {
			sb.append("static ");
		}

		PoshiElement parentElement = (PoshiElement)getParent();

		if (!(parentElement instanceof ExecutePoshiElement)) {
			sb.append(getName());
			sb.append(" ");
		}

		String name = attributeValue("name");

		sb.append(name);

		sb.append(" = ");

		String value = getVarValue();

		if (Validator.isNotNull(valueAttributeName)) {
			if (valueAttributeName.equals("method")) {
				if (isValidUtilClassName(value) ||
					value.startsWith("selenium#") ||
					value.startsWith("TestPropsUtil#")) {

					value = value.replaceFirst("#", ".");
				}
			}
			else {
				value = value.replaceAll("\"", "&quot;");

				if (parentElement instanceof ExecutePoshiElement) {
					value = value.replace("\\", "\\\\");
				}

				value = quoteContent(value);
			}
		}

		sb.append(value);

		if (!(parentElement instanceof ExecutePoshiElement)) {
			sb.append(";");
		}

		return sb.toString();
	}

	protected VarPoshiElement() {
	}

	protected VarPoshiElement(Element element) {
		this(_ELEMENT_NAME, element);
	}

	protected VarPoshiElement(List<Attribute> attributes, List<Node> nodes) {
		this(_ELEMENT_NAME, attributes, nodes);
	}

	protected VarPoshiElement(
		PoshiElement parentPoshiElement, String readableSyntax) {

		this(_ELEMENT_NAME, parentPoshiElement, readableSyntax);
	}

	protected VarPoshiElement(String name, Element element) {
		super(name, element);

		if (isElementType(name, element)) {
			initValueAttributeName(element);
		}
	}

	protected VarPoshiElement(
		String elementName, List<Attribute> attributes, List<Node> nodes) {

		super(elementName, attributes, nodes);
	}

	protected VarPoshiElement(
		String name, PoshiElement parentPoshiElement, String readableSyntax) {

		super(name, parentPoshiElement, readableSyntax);
	}

	@Override
	protected String getBlockName() {
		return null;
	}

	protected void initValueAttributeName(Element element) {
		if (element.attribute("method") != null) {
			valueAttributeName = "method";

			return;
		}

		if (element.attribute("value") != null) {
			valueAttributeName = "value";

			return;
		}

		for (Node node : Dom4JUtil.toNodeList(element.content())) {
			if (node instanceof CDATA) {
				add((CDATA)node.clone());

				return;
			}
		}

		try {
			throw new IllegalArgumentException(
				"Invalid variable element " + Dom4JUtil.format(element));
		}
		catch (IOException ioe) {
			throw new IllegalArgumentException("Invalid variable element", ioe);
		}
	}

	protected String valueAttributeName;

	private boolean _isElementType(String readableSyntax) {
		readableSyntax = readableSyntax.trim();

		if (!isBalancedReadableSyntax(readableSyntax)) {
			return false;
		}

		if (!readableSyntax.endsWith(";")) {
			return false;
		}

		if (!readableSyntax.startsWith("static var") &&
			!readableSyntax.startsWith("var ")) {

			return false;
		}

		if (readableSyntax.contains(" = return(")) {
			return false;
		}

		return true;
	}

	private static final String _ELEMENT_NAME = "var";

}