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
		PoshiElement parentPoshiElement, String poshiScript) {

		if (_isElementType(poshiScript)) {
			return new EchoPoshiElement(parentPoshiElement, poshiScript);
		}

		return null;
	}

	@Override
	public void parsePoshiScript(String poshiScript) {
		String content = getQuotedContent(poshiScript);

		addAttribute("message", content);
	}

	@Override
	public String toPoshiScript() {
		String message = attributeValue("message");

		return createPoshiScriptSnippet(message);
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
		PoshiElement parentPoshiElement, String poshiScript) {

		super(_ELEMENT_NAME, parentPoshiElement, poshiScript);
	}

	protected EchoPoshiElement(String name, Element element) {
		super(name, element);
	}

	protected EchoPoshiElement(
		String elementName, List<Attribute> attributes, List<Node> nodes) {

		super(elementName, attributes, nodes);
	}

	protected EchoPoshiElement(
		String name, PoshiElement parentPoshiElement, String poshiScript) {

		super(name, parentPoshiElement, poshiScript);
	}

	@Override
	protected String createPoshiScriptSnippet(String content) {
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

	private boolean _isElementType(String poshiScript) {
		poshiScript = poshiScript.trim();

		if (!isBalancedPoshiScript(poshiScript)) {
			return false;
		}

		if (!poshiScript.endsWith(");")) {
			return false;
		}

		if (!poshiScript.startsWith("echo(")) {
			return false;
		}

		return true;
	}

	private static final String _ELEMENT_NAME = "echo";

}