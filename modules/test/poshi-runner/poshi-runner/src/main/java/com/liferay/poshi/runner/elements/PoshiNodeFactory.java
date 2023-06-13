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

import com.google.common.reflect.ClassPath;

import com.liferay.poshi.runner.script.PoshiScriptParserException;
import com.liferay.poshi.runner.script.UnbalancedCodeException;
import com.liferay.poshi.runner.util.Dom4JUtil;
import com.liferay.poshi.runner.util.FileUtil;

import java.io.IOException;

import java.lang.reflect.Modifier;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @author Kenji Heigel
 */
public abstract class PoshiNodeFactory {

	public static boolean getValidatePoshiScript() {
		return _validatePoshiScript;
	}

	public static PoshiNode<?, ?> newPoshiNode(Node node) {
		PoshiNode<?, ?> newPoshiNode = null;

		if (node instanceof Comment) {
			newPoshiNode = _newPoshiComment((Comment)node);
		}

		if (node instanceof Element) {
			newPoshiNode = _newPoshiElement((Element)node);
		}

		if (newPoshiNode != null) {
			return newPoshiNode;
		}

		String nodeContent;

		try {
			nodeContent = Dom4JUtil.format(node);
		}
		catch (IOException ioe) {
			nodeContent = node.toString();
		}

		throw new RuntimeException("Unknown node\n" + nodeContent);
	}

	public static PoshiNode<?, ?> newPoshiNode(
			PoshiNode<?, ?> parentPoshiNode, String poshiScript)
		throws PoshiScriptParserException {

		PoshiNode<?, ?> newPoshiNode = null;

		newPoshiNode = _newPoshiComment(poshiScript);

		if (newPoshiNode != null) {
			return newPoshiNode;
		}

		newPoshiNode = _newPoshiElement(
			(PoshiElement)parentPoshiNode, poshiScript);

		if (newPoshiNode != null) {
			return newPoshiNode;
		}

		throw new PoshiScriptParserException(
			"Invalid Poshi Script syntax", poshiScript, parentPoshiNode);
	}

	public static PoshiNode<?, ?> newPoshiNode(String content, URL url) {
		try {
			content = content.trim();

			if (content.startsWith("<definition")) {
				Document document = Dom4JUtil.parse(content);

				return _definitionPoshiElement.clone(
					document.getRootElement(), url);
			}

			if (_definitionPoshiElement.isBalancedPoshiScript(content, true)) {
				return _definitionPoshiElement.clone(content, url);
			}
		}
		catch (DocumentException de) {
			throw new RuntimeException(
				"Unable to parse Poshi XML file: " + url.getFile(),
				de.getCause());
		}
		catch (PoshiScriptParserException pspe) {
			if (pspe instanceof UnbalancedCodeException) {
				pspe.setFilePath(url.getFile());
			}

			System.out.println(pspe.getMessage());
		}

		return null;
	}

	public static PoshiNode<?, ?> newPoshiNodeFromFile(URL url) {
		try {
			String content = FileUtil.read(url);

			return newPoshiNode(content, url);
		}
		catch (IOException ioe) {
			throw new RuntimeException(
				"Unable to read file: " + url.getFile(), ioe.getCause());
		}
	}

	public static void setValidatePoshiScript(boolean validate) {
		_validatePoshiScript = validate;
	}

	protected static boolean hasPoshiScriptParserException(URL url) {
		Set<String> failingFilePaths =
			PoshiScriptParserException.getUniqueErrorPaths();

		return failingFilePaths.contains(url.getFile());
	}

	protected static void validatePoshiScriptContent(
			PoshiElement poshiElement, URL url)
		throws DocumentException, IOException, PoshiScriptParserException {

		if (!_validatePoshiScript) {
			return;
		}

		String poshiXMLString = Dom4JUtil.format(poshiElement);

		PoshiNode newPoshiElement = newPoshiNode(poshiXMLString, url);

		String newPoshiScript = newPoshiElement.toPoshiScript();

		String poshiScript = FileUtil.read(url);

		poshiScript = poshiScript.replaceAll("\\s+", "");

		if (!poshiScript.equals(newPoshiScript.replaceAll("\\s+", ""))) {
			PoshiScriptParserException pspe = new PoshiScriptParserException(
				"Data loss has occurred while parsing Poshi Script",
				newPoshiElement);

			throw pspe;
		}
	}

	private static DefinitionPoshiElement _getDefinitionPoshiElement() {
		for (PoshiElement poshiElement : _poshiElements) {
			if (poshiElement instanceof DefinitionPoshiElement) {
				return (DefinitionPoshiElement)poshiElement;
			}
		}

		return new DefinitionPoshiElement();
	}

	private static PoshiComment _newPoshiComment(Comment comment) {
		for (PoshiComment poshiComment : _poshiComments) {
			PoshiComment newPoshiComment = poshiComment.clone(comment);

			if (newPoshiComment != null) {
				return newPoshiComment;
			}
		}

		return null;
	}

	private static PoshiComment _newPoshiComment(String poshiScript)
		throws PoshiScriptParserException {

		for (PoshiComment poshiComment : _poshiComments) {
			PoshiComment newPoshiComment = poshiComment.clone(poshiScript);

			if (newPoshiComment != null) {
				return newPoshiComment;
			}
		}

		return null;
	}

	private static PoshiElement _newPoshiElement(Element element) {
		for (PoshiElement poshiElement : _poshiElements) {
			PoshiElement newPoshiElement = poshiElement.clone(element);

			if (newPoshiElement != null) {
				return newPoshiElement;
			}
		}

		return null;
	}

	private static PoshiElement _newPoshiElement(
			PoshiElement parentPoshiElement, String poshiScript)
		throws PoshiScriptParserException {

		for (PoshiElement poshiElement : _poshiElements) {
			PoshiElement newPoshiElement = poshiElement.clone(
				parentPoshiElement, poshiScript);

			if (newPoshiElement != null) {
				return newPoshiElement;
			}
		}

		return null;
	}

	private static final DefinitionPoshiElement _definitionPoshiElement;
	private static final List<PoshiComment> _poshiComments = new ArrayList<>();
	private static final List<PoshiElement> _poshiElements = new ArrayList<>();
	private static boolean _validatePoshiScript = true;

	static {
		try {
			ClassPath classPath = ClassPath.from(
				PoshiNode.class.getClassLoader());

			List<Class<?>> poshiElementClasses = new ArrayList<>();

			for (ClassPath.ClassInfo classInfo :
					classPath.getTopLevelClasses(
						"com.liferay.poshi.runner.elements")) {

				poshiElementClasses.add(classInfo.load());
			}

			Collections.sort(
				poshiElementClasses,
				new Comparator<Class<?>>() {

					@Override
					public int compare(Class<?> class1, Class<?> class2) {
						String className1 = class1.getName();
						String className2 = class2.getName();

						return className1.compareTo(className2);
					}

				});

			for (Class<?> clazz : poshiElementClasses) {
				if (Modifier.isAbstract(clazz.getModifiers()) ||
					!(PoshiComment.class.isAssignableFrom(clazz) ||
					  PoshiElement.class.isAssignableFrom(clazz))) {

					continue;
				}

				PoshiNode<?, ?> poshiNode =
					(PoshiNode<?, ?>)clazz.newInstance();

				if (poshiNode instanceof PoshiComment) {
					_poshiComments.add((PoshiComment)poshiNode);

					continue;
				}

				if (poshiNode instanceof PoshiElement) {
					_poshiElements.add((PoshiElement)poshiNode);
				}
			}

			_definitionPoshiElement = _getDefinitionPoshiElement();
		}
		catch (IllegalAccessException | InstantiationException | IOException
					e) {

			throw new RuntimeException(e);
		}
	}

}