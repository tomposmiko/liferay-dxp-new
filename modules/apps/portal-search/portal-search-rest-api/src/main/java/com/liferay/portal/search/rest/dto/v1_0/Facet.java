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

package com.liferay.portal.search.rest.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
 * @author Petteri Karttunen
 * @generated
 */
@Generated("")
@GraphQLName("Facet")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "Facet")
public class Facet implements Serializable {

	public static Facet toDTO(String json) {
		return ObjectMapperUtil.readValue(Facet.class, json);
	}

	public static Facet unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(Facet.class, json);
	}

	@Schema
	public String getAggregationName() {
		return aggregationName;
	}

	public void setAggregationName(String aggregationName) {
		this.aggregationName = aggregationName;
	}

	@JsonIgnore
	public void setAggregationName(
		UnsafeSupplier<String, Exception> aggregationNameUnsafeSupplier) {

		try {
			aggregationName = aggregationNameUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String aggregationName;

	@Schema
	@Valid
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@JsonIgnore
	public void setAttributes(
		UnsafeSupplier<Map<String, Object>, Exception>
			attributesUnsafeSupplier) {

		try {
			attributes = attributesUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Map<String, Object> attributes;

	@Schema
	public Integer getFrequencyThreshold() {
		return frequencyThreshold;
	}

	public void setFrequencyThreshold(Integer frequencyThreshold) {
		this.frequencyThreshold = frequencyThreshold;
	}

	@JsonIgnore
	public void setFrequencyThreshold(
		UnsafeSupplier<Integer, Exception> frequencyThresholdUnsafeSupplier) {

		try {
			frequencyThreshold = frequencyThresholdUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Integer frequencyThreshold;

	@Schema
	public Integer getMaxTerms() {
		return maxTerms;
	}

	public void setMaxTerms(Integer maxTerms) {
		this.maxTerms = maxTerms;
	}

	@JsonIgnore
	public void setMaxTerms(
		UnsafeSupplier<Integer, Exception> maxTermsUnsafeSupplier) {

		try {
			maxTerms = maxTermsUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Integer maxTerms;

	@Schema
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public void setName(UnsafeSupplier<String, Exception> nameUnsafeSupplier) {
		try {
			name = nameUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String name;

	@Schema
	@Valid
	public Object[] getValues() {
		return values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

	@JsonIgnore
	public void setValues(
		UnsafeSupplier<Object[], Exception> valuesUnsafeSupplier) {

		try {
			values = valuesUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Object[] values;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Facet)) {
			return false;
		}

		Facet facet = (Facet)object;

		return Objects.equals(toString(), facet.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (aggregationName != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"aggregationName\": ");

			sb.append("\"");

			sb.append(_escape(aggregationName));

			sb.append("\"");
		}

		if (attributes != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"attributes\": ");

			sb.append(_toJSON(attributes));
		}

		if (frequencyThreshold != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"frequencyThreshold\": ");

			sb.append(frequencyThreshold);
		}

		if (maxTerms != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"maxTerms\": ");

			sb.append(maxTerms);
		}

		if (name != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(name));

			sb.append("\"");
		}

		if (values != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"values\": ");

			sb.append("[");

			for (int i = 0; i < values.length; i++) {
				sb.append("\"");

				sb.append(_escape(values[i]));

				sb.append("\"");

				if ((i + 1) < values.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.portal.search.rest.dto.v1_0.Facet",
		name = "x-class-name"
	)
	public String xClassName;

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