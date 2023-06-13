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

package com.liferay.fragment.input.template.parser;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eudaldo Alonso
 */
public class InputTemplateNode extends LinkedHashMap<String, Object> {

	public InputTemplateNode(
		String errorMessage, String helpText, String label, String name,
		boolean required, boolean showHelpText, boolean showLabel, String type,
		String value) {

		_errorMessage = errorMessage;
		_helpText = helpText;
		_label = label;
		_name = name;
		_required = required;
		_showHelpText = showHelpText;
		_showLabel = showLabel;
		_type = type;
		_value = value;

		put("errorMessage", errorMessage);
		put("helpText", helpText);
		put("label", label);
		put("name", name);
		put("required", required);
		put("showHelpText", showHelpText);
		put("showLabel", showLabel);
		put("type", type);
		put("value", value);

		put("options", _options);
	}

	public void addAttribute(String name, Object object) {
		_attributes.put(name, object);
	}

	public void addOption(String label, String value) {
		_options.add(new Option(label, value));
	}

	public Map<String, Object> getAttributes() {
		return _attributes;
	}

	public String getErrorMessage() {
		return _errorMessage;
	}

	public String getHelpText() {
		return _helpText;
	}

	public String getInputLabel() {
		return _label;
	}

	public String getInputName() {
		return _name;
	}

	public String getInputValue() {
		return _value;
	}

	public List<InputTemplateNode.Option> getOptions() {
		return _options;
	}

	public String getType() {
		return _type;
	}

	public boolean isRequired() {
		return _required;
	}

	public boolean isShowHelpText() {
		return _showHelpText;
	}

	public boolean isShowLabel() {
		return _showLabel;
	}

	public JSONObject toJSONObject() {
		return JSONUtil.put(
			"attributes",
			() -> {
				JSONObject attributesJSONObject =
					JSONFactoryUtil.createJSONObject();

				for (Map.Entry<String, Object> entry : _attributes.entrySet()) {
					attributesJSONObject.put(entry.getKey(), entry.getValue());
				}

				return attributesJSONObject;
			}
		).put(
			"errorMessage", _errorMessage
		).put(
			"helpText", _helpText
		).put(
			"label", _label
		).put(
			"name", _name
		).put(
			"options",
			() -> {
				JSONObject optionsJSONObject =
					JSONFactoryUtil.createJSONObject();

				for (Option option : _options) {
					optionsJSONObject.put(option.getLabel(), option.getValue());
				}

				return optionsJSONObject;
			}
		).put(
			"required", _required
		).put(
			"showHelpText", _showHelpText
		).put(
			"showLabel", _showLabel
		).put(
			"type", _type
		).put(
			"value", _value
		);
	}

	public static class Option {

		public Option(String label, String value) {
			_label = label;
			_value = value;
		}

		public String getLabel() {
			return _label;
		}

		public String getValue() {
			return _value;
		}

		private final String _label;
		private final String _value;

	}

	private final Map<String, Object> _attributes = new HashMap<>();
	private final String _errorMessage;
	private final String _helpText;
	private final String _label;
	private final String _name;
	private final List<InputTemplateNode.Option> _options = new ArrayList<>();
	private final boolean _required;
	private final boolean _showHelpText;
	private final boolean _showLabel;
	private final String _type;
	private final String _value;

}