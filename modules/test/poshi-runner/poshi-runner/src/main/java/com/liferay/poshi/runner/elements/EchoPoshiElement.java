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

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @author Kenji Heigel
 */
public class EchoPoshiElement extends PoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new EchoPoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String readableSyntax) {

		if (_isElementType(readableSyntax)) {
			return new EchoPoshiElement(parentPoshiElement, readableSyntax);
		}

		return null;
	}

	@Override
	public void parseReadableSyntax(String readableSyntax) {
		String content = getQuotedContent(readableSyntax);

		addAttribute("message", content);
	}

	@Override
	public String toReadableSyntax() {
		String message = attributeValue("message");

		return createReadableBlock(message);
	}

	protected EchoPoshiElement() {
	}

	protected EchoPoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected EchoPoshiElement(List<Attribute> attributes, List<Node> nodes) {
		this(_ELEMENT_NAME, attributes, nodes);
	}

	protected EchoPoshiElement(
		PoshiElement parentPoshiElement, String readableSyntax) {

		super(_ELEMENT_NAME, parentPoshiElement, readableSyntax);
	}

	protected EchoPoshiElement(String name, Element element) {
		super(name, element);
	}

	protected EchoPoshiElement(
		String elementName, List<Attribute> attributes, List<Node> nodes) {

		super(elementName, attributes, nodes);
	}

	protected EchoPoshiElement(
		String name, PoshiElement parentPoshiElement, String readableSyntax) {

		super(name, parentPoshiElement, readableSyntax);
	}

	@Override
	protected String createReadableBlock(String content) {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\n");
		sb.append(getPad());
		sb.append(getBlockName());
		sb.append("(\"");
		sb.append(content.trim());
		sb.append("\");");

		return sb.toString();
	}

	@Override
	protected String getBlockName() {
		return "echo";
	}

	private boolean _isElementType(String readableSyntax) {
		readableSyntax = readableSyntax.trim();

		if (!isBalancedReadableSyntax(readableSyntax)) {
			return false;
		}

		if (!readableSyntax.endsWith(");")) {
			return false;
		}

		if (!readableSyntax.startsWith("echo(")) {
			return false;
		}

		return true;
	}

	private static final String _ELEMENT_NAME = "echo";

}