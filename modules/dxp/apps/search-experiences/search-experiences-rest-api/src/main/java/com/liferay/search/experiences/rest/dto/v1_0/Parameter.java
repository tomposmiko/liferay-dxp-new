/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.rest.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
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
 * @author Brian Wing Shun Chan
 * @generated
 */
@Generated("")
@GraphQLName("Parameter")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "Parameter")
public class Parameter implements Serializable {

	public static Parameter toDTO(String json) {
		return ObjectMapperUtil.readValue(Parameter.class, json);
	}

	public static Parameter unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(Parameter.class, json);
	}

	@Schema
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@JsonIgnore
	public void setDateFormat(
		UnsafeSupplier<String, Exception> dateFormatUnsafeSupplier) {

		try {
			dateFormat = dateFormatUnsafeSupplier.get();
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
	protected String dateFormat;

	@Schema
	public Double getDefaultValueDouble() {
		return defaultValueDouble;
	}

	public void setDefaultValueDouble(Double defaultValueDouble) {
		this.defaultValueDouble = defaultValueDouble;
	}

	@JsonIgnore
	public void setDefaultValueDouble(
		UnsafeSupplier<Double, Exception> defaultValueDoubleUnsafeSupplier) {

		try {
			defaultValueDouble = defaultValueDoubleUnsafeSupplier.get();
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
	protected Double defaultValueDouble;

	@Schema
	@Valid
	public Float getDefaultValueFloat() {
		return defaultValueFloat;
	}

	public void setDefaultValueFloat(Float defaultValueFloat) {
		this.defaultValueFloat = defaultValueFloat;
	}

	@JsonIgnore
	public void setDefaultValueFloat(
		UnsafeSupplier<Float, Exception> defaultValueFloatUnsafeSupplier) {

		try {
			defaultValueFloat = defaultValueFloatUnsafeSupplier.get();
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
	protected Float defaultValueFloat;

	@Schema
	public Integer getDefaultValueInteger() {
		return defaultValueInteger;
	}

	public void setDefaultValueInteger(Integer defaultValueInteger) {
		this.defaultValueInteger = defaultValueInteger;
	}

	@JsonIgnore
	public void setDefaultValueInteger(
		UnsafeSupplier<Integer, Exception> defaultValueIntegerUnsafeSupplier) {

		try {
			defaultValueInteger = defaultValueIntegerUnsafeSupplier.get();
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
	protected Integer defaultValueInteger;

	@Schema
	public Long getDefaultValueLong() {
		return defaultValueLong;
	}

	public void setDefaultValueLong(Long defaultValueLong) {
		this.defaultValueLong = defaultValueLong;
	}

	@JsonIgnore
	public void setDefaultValueLong(
		UnsafeSupplier<Long, Exception> defaultValueLongUnsafeSupplier) {

		try {
			defaultValueLong = defaultValueLongUnsafeSupplier.get();
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
	protected Long defaultValueLong;

	@Schema
	public String getDefaultValueString() {
		return defaultValueString;
	}

	public void setDefaultValueString(String defaultValueString) {
		this.defaultValueString = defaultValueString;
	}

	@JsonIgnore
	public void setDefaultValueString(
		UnsafeSupplier<String, Exception> defaultValueStringUnsafeSupplier) {

		try {
			defaultValueString = defaultValueStringUnsafeSupplier.get();
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
	protected String defaultValueString;

	@Schema
	public Integer[] getDefaultValuesIntegerArray() {
		return defaultValuesIntegerArray;
	}

	public void setDefaultValuesIntegerArray(
		Integer[] defaultValuesIntegerArray) {

		this.defaultValuesIntegerArray = defaultValuesIntegerArray;
	}

	@JsonIgnore
	public void setDefaultValuesIntegerArray(
		UnsafeSupplier<Integer[], Exception>
			defaultValuesIntegerArrayUnsafeSupplier) {

		try {
			defaultValuesIntegerArray =
				defaultValuesIntegerArrayUnsafeSupplier.get();
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
	protected Integer[] defaultValuesIntegerArray;

	@Schema
	public Long[] getDefaultValuesLongArray() {
		return defaultValuesLongArray;
	}

	public void setDefaultValuesLongArray(Long[] defaultValuesLongArray) {
		this.defaultValuesLongArray = defaultValuesLongArray;
	}

	@JsonIgnore
	public void setDefaultValuesLongArray(
		UnsafeSupplier<Long[], Exception>
			defaultValuesLongArrayUnsafeSupplier) {

		try {
			defaultValuesLongArray = defaultValuesLongArrayUnsafeSupplier.get();
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
	protected Long[] defaultValuesLongArray;

	@Schema
	public String[] getDefaultValuesStringArray() {
		return defaultValuesStringArray;
	}

	public void setDefaultValuesStringArray(String[] defaultValuesStringArray) {
		this.defaultValuesStringArray = defaultValuesStringArray;
	}

	@JsonIgnore
	public void setDefaultValuesStringArray(
		UnsafeSupplier<String[], Exception>
			defaultValuesStringArrayUnsafeSupplier) {

		try {
			defaultValuesStringArray =
				defaultValuesStringArrayUnsafeSupplier.get();
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
	protected String[] defaultValuesStringArray;

	@Schema
	public Double getMaxValueDouble() {
		return maxValueDouble;
	}

	public void setMaxValueDouble(Double maxValueDouble) {
		this.maxValueDouble = maxValueDouble;
	}

	@JsonIgnore
	public void setMaxValueDouble(
		UnsafeSupplier<Double, Exception> maxValueDoubleUnsafeSupplier) {

		try {
			maxValueDouble = maxValueDoubleUnsafeSupplier.get();
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
	protected Double maxValueDouble;

	@Schema
	@Valid
	public Float getMaxValueFloat() {
		return maxValueFloat;
	}

	public void setMaxValueFloat(Float maxValueFloat) {
		this.maxValueFloat = maxValueFloat;
	}

	@JsonIgnore
	public void setMaxValueFloat(
		UnsafeSupplier<Float, Exception> maxValueFloatUnsafeSupplier) {

		try {
			maxValueFloat = maxValueFloatUnsafeSupplier.get();
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
	protected Float maxValueFloat;

	@Schema
	public Integer getMaxValueInteger() {
		return maxValueInteger;
	}

	public void setMaxValueInteger(Integer maxValueInteger) {
		this.maxValueInteger = maxValueInteger;
	}

	@JsonIgnore
	public void setMaxValueInteger(
		UnsafeSupplier<Integer, Exception> maxValueIntegerUnsafeSupplier) {

		try {
			maxValueInteger = maxValueIntegerUnsafeSupplier.get();
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
	protected Integer maxValueInteger;

	@Schema
	public Long getMaxValueLong() {
		return maxValueLong;
	}

	public void setMaxValueLong(Long maxValueLong) {
		this.maxValueLong = maxValueLong;
	}

	@JsonIgnore
	public void setMaxValueLong(
		UnsafeSupplier<Long, Exception> maxValueLongUnsafeSupplier) {

		try {
			maxValueLong = maxValueLongUnsafeSupplier.get();
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
	protected Long maxValueLong;

	@Schema
	public Double getMinValueDouble() {
		return minValueDouble;
	}

	public void setMinValueDouble(Double minValueDouble) {
		this.minValueDouble = minValueDouble;
	}

	@JsonIgnore
	public void setMinValueDouble(
		UnsafeSupplier<Double, Exception> minValueDoubleUnsafeSupplier) {

		try {
			minValueDouble = minValueDoubleUnsafeSupplier.get();
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
	protected Double minValueDouble;

	@Schema
	@Valid
	public Float getMinValueFloat() {
		return minValueFloat;
	}

	public void setMinValueFloat(Float minValueFloat) {
		this.minValueFloat = minValueFloat;
	}

	@JsonIgnore
	public void setMinValueFloat(
		UnsafeSupplier<Float, Exception> minValueFloatUnsafeSupplier) {

		try {
			minValueFloat = minValueFloatUnsafeSupplier.get();
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
	protected Float minValueFloat;

	@Schema
	public Integer getMinValueInteger() {
		return minValueInteger;
	}

	public void setMinValueInteger(Integer minValueInteger) {
		this.minValueInteger = minValueInteger;
	}

	@JsonIgnore
	public void setMinValueInteger(
		UnsafeSupplier<Integer, Exception> minValueIntegerUnsafeSupplier) {

		try {
			minValueInteger = minValueIntegerUnsafeSupplier.get();
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
	protected Integer minValueInteger;

	@Schema
	public Long getMinValueLong() {
		return minValueLong;
	}

	public void setMinValueLong(Long minValueLong) {
		this.minValueLong = minValueLong;
	}

	@JsonIgnore
	public void setMinValueLong(
		UnsafeSupplier<Long, Exception> minValueLongUnsafeSupplier) {

		try {
			minValueLong = minValueLongUnsafeSupplier.get();
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
	protected Long minValueLong;

	@Schema
	@Valid
	public ParameterType getParameterType() {
		return parameterType;
	}

	@JsonIgnore
	public String getParameterTypeAsString() {
		if (parameterType == null) {
			return null;
		}

		return parameterType.toString();
	}

	public void setParameterType(ParameterType parameterType) {
		this.parameterType = parameterType;
	}

	@JsonIgnore
	public void setParameterType(
		UnsafeSupplier<ParameterType, Exception> parameterTypeUnsafeSupplier) {

		try {
			parameterType = parameterTypeUnsafeSupplier.get();
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
	protected ParameterType parameterType;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Parameter)) {
			return false;
		}

		Parameter parameter = (Parameter)object;

		return Objects.equals(toString(), parameter.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (dateFormat != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"dateFormat\": ");

			sb.append("\"");

			sb.append(_escape(dateFormat));

			sb.append("\"");
		}

		if (defaultValueDouble != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"defaultValueDouble\": ");

			sb.append(defaultValueDouble);
		}

		if (defaultValueFloat != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"defaultValueFloat\": ");

			sb.append(defaultValueFloat);
		}

		if (defaultValueInteger != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"defaultValueInteger\": ");

			sb.append(defaultValueInteger);
		}

		if (defaultValueLong != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"defaultValueLong\": ");

			sb.append(defaultValueLong);
		}

		if (defaultValueString != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"defaultValueString\": ");

			sb.append("\"");

			sb.append(_escape(defaultValueString));

			sb.append("\"");
		}

		if (defaultValuesIntegerArray != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"defaultValuesIntegerArray\": ");

			sb.append("[");

			for (int i = 0; i < defaultValuesIntegerArray.length; i++) {
				sb.append(defaultValuesIntegerArray[i]);

				if ((i + 1) < defaultValuesIntegerArray.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (defaultValuesLongArray != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"defaultValuesLongArray\": ");

			sb.append("[");

			for (int i = 0; i < defaultValuesLongArray.length; i++) {
				sb.append(defaultValuesLongArray[i]);

				if ((i + 1) < defaultValuesLongArray.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (defaultValuesStringArray != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"defaultValuesStringArray\": ");

			sb.append("[");

			for (int i = 0; i < defaultValuesStringArray.length; i++) {
				sb.append("\"");

				sb.append(_escape(defaultValuesStringArray[i]));

				sb.append("\"");

				if ((i + 1) < defaultValuesStringArray.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (maxValueDouble != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"maxValueDouble\": ");

			sb.append(maxValueDouble);
		}

		if (maxValueFloat != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"maxValueFloat\": ");

			sb.append(maxValueFloat);
		}

		if (maxValueInteger != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"maxValueInteger\": ");

			sb.append(maxValueInteger);
		}

		if (maxValueLong != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"maxValueLong\": ");

			sb.append(maxValueLong);
		}

		if (minValueDouble != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"minValueDouble\": ");

			sb.append(minValueDouble);
		}

		if (minValueFloat != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"minValueFloat\": ");

			sb.append(minValueFloat);
		}

		if (minValueInteger != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"minValueInteger\": ");

			sb.append(minValueInteger);
		}

		if (minValueLong != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"minValueLong\": ");

			sb.append(minValueLong);
		}

		if (parameterType != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"parameterType\": ");

			sb.append("\"");

			sb.append(parameterType);

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.search.experiences.rest.dto.v1_0.Parameter",
		name = "x-class-name"
	)
	public String xClassName;

	@GraphQLName("ParameterType")
	public static enum ParameterType {

		DATE("Date"), DOUBLE("Double"), FLOAT("Float"), INTEGER("Integer"),
		INTEGER_ARRAY("IntegerArray"), LONG("Long"), LONG_ARRAY("LongArray"),
		STRING("String"), STRING_ARRAY("StringArray"), TIME_RANGE("TimeRange");

		@JsonCreator
		public static ParameterType create(String value) {
			if ((value == null) || value.equals("")) {
				return null;
			}

			for (ParameterType parameterType : values()) {
				if (Objects.equals(parameterType.getValue(), value)) {
					return parameterType;
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

		private ParameterType(String value) {
			_value = value;
		}

		private final String _value;

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		return string.replaceAll("\"", "\\\\\"");
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
			sb.append(entry.getKey());
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
				sb.append(value);
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

}