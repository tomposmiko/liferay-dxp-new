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

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
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
 * @author Brian Wing Shun Chan
 * @generated
 */
@Generated("")
@GraphQLName("EmbeddingProviderConfiguration")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "EmbeddingProviderConfiguration")
public class EmbeddingProviderConfiguration implements Serializable {

	public static EmbeddingProviderConfiguration toDTO(String json) {
		return ObjectMapperUtil.readValue(
			EmbeddingProviderConfiguration.class, json);
	}

	public static EmbeddingProviderConfiguration unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(
			EmbeddingProviderConfiguration.class, json);
	}

	@Schema
	@Valid
	public Object getAttributes() {
		return attributes;
	}

	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}

	@JsonIgnore
	public void setAttributes(
		UnsafeSupplier<Object, Exception> attributesUnsafeSupplier) {

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
	protected Object attributes;

	@Schema
	public Integer getEmbeddingVectorDimensions() {
		return embeddingVectorDimensions;
	}

	public void setEmbeddingVectorDimensions(
		Integer embeddingVectorDimensions) {

		this.embeddingVectorDimensions = embeddingVectorDimensions;
	}

	@JsonIgnore
	public void setEmbeddingVectorDimensions(
		UnsafeSupplier<Integer, Exception>
			embeddingVectorDimensionsUnsafeSupplier) {

		try {
			embeddingVectorDimensions =
				embeddingVectorDimensionsUnsafeSupplier.get();
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
	protected Integer embeddingVectorDimensions;

	@Schema
	public String[] getLanguageIds() {
		return languageIds;
	}

	public void setLanguageIds(String[] languageIds) {
		this.languageIds = languageIds;
	}

	@JsonIgnore
	public void setLanguageIds(
		UnsafeSupplier<String[], Exception> languageIdsUnsafeSupplier) {

		try {
			languageIds = languageIdsUnsafeSupplier.get();
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
	protected String[] languageIds;

	@Schema
	public String[] getModelClassNames() {
		return modelClassNames;
	}

	public void setModelClassNames(String[] modelClassNames) {
		this.modelClassNames = modelClassNames;
	}

	@JsonIgnore
	public void setModelClassNames(
		UnsafeSupplier<String[], Exception> modelClassNamesUnsafeSupplier) {

		try {
			modelClassNames = modelClassNamesUnsafeSupplier.get();
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
	protected String[] modelClassNames;

	@Schema
	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	@JsonIgnore
	public void setProviderName(
		UnsafeSupplier<String, Exception> providerNameUnsafeSupplier) {

		try {
			providerName = providerNameUnsafeSupplier.get();
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
	protected String providerName;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof EmbeddingProviderConfiguration)) {
			return false;
		}

		EmbeddingProviderConfiguration embeddingProviderConfiguration =
			(EmbeddingProviderConfiguration)object;

		return Objects.equals(
			toString(), embeddingProviderConfiguration.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (attributes != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"attributes\": ");

			if (attributes instanceof Map) {
				sb.append(
					JSONFactoryUtil.createJSONObject((Map<?, ?>)attributes));
			}
			else if (attributes instanceof String) {
				sb.append("\"");
				sb.append(_escape((String)attributes));
				sb.append("\"");
			}
			else {
				sb.append(attributes);
			}
		}

		if (embeddingVectorDimensions != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"embeddingVectorDimensions\": ");

			sb.append(embeddingVectorDimensions);
		}

		if (languageIds != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"languageIds\": ");

			sb.append("[");

			for (int i = 0; i < languageIds.length; i++) {
				sb.append("\"");

				sb.append(_escape(languageIds[i]));

				sb.append("\"");

				if ((i + 1) < languageIds.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (modelClassNames != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"modelClassNames\": ");

			sb.append("[");

			for (int i = 0; i < modelClassNames.length; i++) {
				sb.append("\"");

				sb.append(_escape(modelClassNames[i]));

				sb.append("\"");

				if ((i + 1) < modelClassNames.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (providerName != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"providerName\": ");

			sb.append("\"");

			sb.append(_escape(providerName));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.search.experiences.rest.dto.v1_0.EmbeddingProviderConfiguration",
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