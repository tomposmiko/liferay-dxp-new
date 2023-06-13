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

package com.liferay.headless.delivery.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

import javax.validation.Valid;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
@GraphQLName(
	description = "Represents settings related with the site navigation menu of a page.",
	value = "SitePageNavigationMenuSettings"
)
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "SitePageNavigationMenuSettings")
public class SitePageNavigationMenuSettings implements Serializable {

	public static SitePageNavigationMenuSettings toDTO(String json) {
		return ObjectMapperUtil.readValue(
			SitePageNavigationMenuSettings.class, json);
	}

	public static SitePageNavigationMenuSettings unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(
			SitePageNavigationMenuSettings.class, json);
	}

	@Schema(description = "The default parameter for a page.")
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	@JsonIgnore
	public void setQueryString(
		UnsafeSupplier<String, Exception> queryStringUnsafeSupplier) {

		try {
			queryString = queryStringUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "The default parameter for a page.")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String queryString;

	@Schema(
		description = "The page's description to be used as summary for search engines."
	)
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@JsonIgnore
	public void setTarget(
		UnsafeSupplier<String, Exception> targetUnsafeSupplier) {

		try {
			target = targetUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(
		description = "The page's description to be used as summary for search engines."
	)
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String target;

	@Schema(description = "The target's type (specific frame or new tab).")
	@Valid
	public TargetType getTargetType() {
		return targetType;
	}

	@JsonIgnore
	public String getTargetTypeAsString() {
		if (targetType == null) {
			return null;
		}

		return targetType.toString();
	}

	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
	}

	@JsonIgnore
	public void setTargetType(
		UnsafeSupplier<TargetType, Exception> targetTypeUnsafeSupplier) {

		try {
			targetType = targetTypeUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(
		description = "The target's type (specific frame or new tab)."
	)
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected TargetType targetType;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof SitePageNavigationMenuSettings)) {
			return false;
		}

		SitePageNavigationMenuSettings sitePageNavigationMenuSettings =
			(SitePageNavigationMenuSettings)object;

		return Objects.equals(
			toString(), sitePageNavigationMenuSettings.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (queryString != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"queryString\": ");

			sb.append("\"");

			sb.append(_escape(queryString));

			sb.append("\"");
		}

		if (target != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"target\": ");

			sb.append("\"");

			sb.append(_escape(target));

			sb.append("\"");
		}

		if (targetType != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"targetType\": ");

			sb.append("\"");

			sb.append(targetType);

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.headless.delivery.dto.v1_0.SitePageNavigationMenuSettings",
		name = "x-class-name"
	)
	public String xClassName;

	@GraphQLName("TargetType")
	public static enum TargetType {

		SPECIFIC_FRAME("SpecificFrame"), NEW_TAB("NewTab");

		@JsonCreator
		public static TargetType create(String value) {
			if ((value == null) || value.equals("")) {
				return null;
			}

			for (TargetType targetType : values()) {
				if (Objects.equals(targetType.getValue(), value)) {
					return targetType;
				}
			}

			throw new IllegalArgumentException("Invalid enum value: " + value);
		}

		@JsonValue
		public String getValue() {
			return _value;
		}

		@Override
		public String toString() {
			return _value;
		}

		private TargetType(String value) {
			_value = value;
		}

		private final String _value;

	}

	private static String _escape(Object object) {
		return StringUtil.replace(
			String.valueOf(object), _JSON_ESCAPE_STRINGS[0],
			_JSON_ESCAPE_STRINGS[1]);
	}

	private static boolean _isArray(Object value) {
		if (value == null) {
			return false;
		}

		Class<?> clazz = value.getClass();

		return clazz.isArray();
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(_escape(entry.getKey()));
			sb.append("\": ");

			Object value = entry.getValue();

			if (_isArray(value)) {
				sb.append("[");

				Object[] valueArray = (Object[])value;

				for (int i = 0; i < valueArray.length; i++) {
					if (valueArray[i] instanceof String) {
						sb.append("\"");
						sb.append(valueArray[i]);
						sb.append("\"");
					}
					else {
						sb.append(valueArray[i]);
					}

					if ((i + 1) < valueArray.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof Map) {
				sb.append(_toJSON((Map<String, ?>)value));
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(value));
				sb.append("\"");
			}
			else {
				sb.append(value);
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static final String[][] _JSON_ESCAPE_STRINGS = {
		{"\\", "\"", "\b", "\f", "\n", "\r", "\t"},
		{"\\\\", "\\\"", "\\b", "\\f", "\\n", "\\r", "\\t"}
	};

}