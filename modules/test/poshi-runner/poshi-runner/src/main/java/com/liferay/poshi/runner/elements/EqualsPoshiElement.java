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
public class EqualsPoshiElement extends PoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new EqualsPoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String poshiScript) {

		if (_isElementType(parentPoshiElement, poshiScript)) {
			return new EqualsPoshiElement(parentPoshiElement, poshiScript);
		}

		return null;
	}

	@Override
	public void parsePoshiScript(String poshiScript) {
		String[] equalsContentArray = poshiScript.split("==");

		String arg1 = equalsContentArray[0].trim();

		arg1 = getQuotedContent(arg1);

		addAttribute("arg1", arg1);

		String arg2 = equalsContentArray[1].trim();

		arg2 = getQuotedContent(arg2);

		addAttribute("arg2", arg2);
	}

	@Override
	public String toPoshiScript() {
		StringBuilder sb = new StringBuilder();

		sb.append("\"");
		sb.append(attributeValue("arg1"));
		sb.append("\" == \"");
		sb.append(attributeValue("arg2"));
		sb.append("\"");

		return sb.toString();
	}

	protected EqualsPoshiElement() {
	}

	protected EqualsPoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected EqualsPoshiElement(List<Attribute> attributes, List<Node> nodes) {
		super(_ELEMENT_NAME, attributes, nodes);
	}

	protected EqualsPoshiElement(
		PoshiElement parentPoshiElement, String poshiScript) {

		super(_ELEMENT_NAME, parentPoshiElement, poshiScript);
	}

	@Override
	protected String getBlockName() {
		return "equals";
	}

	private boolean _isElementType(
		PoshiElement parentPoshiElement, String poshiScript) {

		if (!isConditionValidInParent(parentPoshiElement)) {
			return false;
		}

		if (poshiScript.contains(" && ") || poshiScript.contains(" || ") ||
			poshiScript.startsWith("!(") ||
			poshiScript.startsWith("else if (")) {

			return false;
		}

		if (poshiScript.contains("==")) {
			return true;
		}

		return false;
	}

	private static final String _ELEMENT_NAME = "equals";

}