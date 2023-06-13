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
public class TakeScreenshotPoshiElement extends PoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new TakeScreenshotPoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String readableSyntax) {

		if (_isElementType(readableSyntax)) {
			return new TakeScreenshotPoshiElement(
				parentPoshiElement, readableSyntax);
		}

		return null;
	}

	@Override
	public void parseReadableSyntax(String readableSyntax) {
	}

	@Override
	public String toReadableSyntax() {
		String readableSyntax = super.toReadableSyntax();

		return createReadableBlock(readableSyntax);
	}

	protected TakeScreenshotPoshiElement() {
	}

	protected TakeScreenshotPoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected TakeScreenshotPoshiElement(
		List<Attribute> attributes, List<Node> nodes) {

		super(_ELEMENT_NAME, attributes, nodes);
	}

	protected TakeScreenshotPoshiElement(
		PoshiElement parentPoshiElement, String readableSyntax) {

		super(_ELEMENT_NAME, parentPoshiElement, readableSyntax);
	}

	@Override
	protected String createReadableBlock(String content) {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\n");
		sb.append(getPad());
		sb.append(getBlockName());
		sb.append("();");

		return sb.toString();
	}

	@Override
	protected String getBlockName() {
		return "takeScreenshot";
	}

	private boolean _isElementType(String readableSyntax) {
		if (readableSyntax.startsWith(getBlockName())) {
			return true;
		}

		return false;
	}

	private static final String _ELEMENT_NAME = "take-screenshot";

}