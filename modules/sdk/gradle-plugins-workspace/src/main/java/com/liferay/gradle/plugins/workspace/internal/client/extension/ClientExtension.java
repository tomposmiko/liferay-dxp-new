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

package com.liferay.gradle.plugins.workspace.internal.client.extension;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Gregory Amerson
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientExtension {

	@JsonAnySetter
	public void ignored(String name, Object value) {
		_typeSettings.put(name, value);
	}

	public Map<String, Object> toJSONMap() throws Exception {
		Properties clientExtensionProperties = _getClientExtensionProperties();

		String pid = clientExtensionProperties.getProperty(type);

		if (pid == null) {
			throw new Exception("Unable to find PID for type: " + type);
		}

		Map<String, Object> jsonMap = new HashMap<>();

		Map<String, Object> configMap = new HashMap<>();

		configMap.put("baseURL", "${portalURL}/o/" + projectName);
		configMap.put("description", description);
		configMap.put("dxp.lxc.liferay.com.virtualInstanceId", "default");
		configMap.put("name", name);
		configMap.put("properties", _encode(properties));
		configMap.put("sourceCodeURL", sourceCodeURL);
		configMap.put("type", type);

		Set<Map.Entry<String, Object>> set = _typeSettings.entrySet();

		set.forEach(
			entry -> {
				if (!pid.contains("CETConfiguration")) {
					configMap.put(entry.getKey(), entry.getValue());
				}
			});

		if (type.equals("oAuthApplicationHeadlessServer") ||
			type.equals("oAuthApplicationUserAgent")) {

			configMap.put(
				"homePageURL",
				_typeSettings.getOrDefault(
					"homePageURL",
					"https://$[conf:ext.lxc.liferay.com.mainDomain]"));
		}

		configMap.put("typeSettings", _encode(_typeSettings));

		jsonMap.put(pid + "~" + id, configMap);

		return jsonMap;
	}

	public String description = "";
	public String id;
	public String name = "";
	public String projectName;
	public Map<String, Object> properties = Collections.emptyMap();
	public String sourceCodeURL = "";
	public String type;

	private List<String> _encode(Map<String, Object> map) {
		Set<Map.Entry<String, Object>> set = map.entrySet();

		Stream<Map.Entry<String, Object>> stream = set.stream();

		return stream.map(
			entry -> {
				Object value = entry.getValue();

				if (value instanceof List) {
					value = StringUtil.merge(
						(List<?>)value, StringPool.NEW_LINE);
				}

				return StringBundler.concat(entry.getKey(), "=", value);
			}
		).collect(
			Collectors.toList()
		);
	}

	private Properties _getClientExtensionProperties() throws Exception {
		Properties properties = new Properties();

		properties.load(
			ClientExtension.class.getResourceAsStream(
				"client-extension.properties"));

		return properties;
	}

	private final Map<String, Object> _typeSettings = new HashMap<>();

}