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
public class IsSetPoshiElement extends PoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new IsSetPoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String readableSyntax) {

		if (_isElementType(parentPoshiElement, readableSyntax)) {
			return new IsSetPoshiElement(parentPoshiElement, readableSyntax);
		}

		return null;
	}

	@Override
	public void parseReadableSyntax(String readableSyntax) {
		String issetContent = getParentheticalContent(readableSyntax);

		addAttribute("var", issetContent);
	}

	@Override
	public String toReadableSyntax() {
		return "isSet(" + attributeValue("var") + ")";
	}

	protected IsSetPoshiElement() {
	}

	protected IsSetPoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected IsSetPoshiElement(List<Attribute> attributes, List<Node> nodes) {
		super(_ELEMENT_NAME, attributes, nodes);
	}

	protected IsSetPoshiElement(
		PoshiElement parentPoshiElement, String readableSyntax) {

		super(_ELEMENT_NAME, parentPoshiElement, readableSyntax);
	}

	@Override
	protected String getBlockName() {
		return "isSet";
	}

	private boolean _isElementType(
		PoshiElement parentPoshiElement, String readableSyntax) {

		if (!isConditionValidInParent(parentPoshiElement)) {
			return false;
		}

		if (readableSyntax.startsWith("!") ||
			readableSyntax.startsWith("else if (")) {

			return false;
		}

		if (readableSyntax.startsWith("isSet(")) {
			return true;
		}

		return false;
	}

	private static final String _ELEMENT_NAME = "isset";

}