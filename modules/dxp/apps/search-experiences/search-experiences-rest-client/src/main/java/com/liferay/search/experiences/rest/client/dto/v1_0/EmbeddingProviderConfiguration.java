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

package com.liferay.search.experiences.rest.client.dto.v1_0;

import com.liferay.search.experiences.rest.client.function.UnsafeSupplier;
import com.liferay.search.experiences.rest.client.serdes.v1_0.EmbeddingProviderConfigurationSerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@Generated("")
public class EmbeddingProviderConfiguration implements Cloneable, Serializable {

	public static EmbeddingProviderConfiguration toDTO(String json) {
		return EmbeddingProviderConfigurationSerDes.toDTO(json);
	}

	public Object getAttributes() {
		return attributes;
	}

	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}

	public void setAttributes(
		UnsafeSupplier<Object, Exception> attributesUnsafeSupplier) {

		try {
			attributes = attributesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Object attributes;

	public Integer getEmbeddingVectorDimensions() {
		return embeddingVectorDimensions;
	}

	public void setEmbeddingVectorDimensions(
		Integer embeddingVectorDimensions) {

		this.embeddingVectorDimensions = embeddingVectorDimensions;
	}

	public void setEmbeddingVectorDimensions(
		UnsafeSupplier<Integer, Exception>
			embeddingVectorDimensionsUnsafeSupplier) {

		try {
			embeddingVectorDimensions =
				embeddingVectorDimensionsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer embeddingVectorDimensions;

	public String[] getLanguageIds() {
		return languageIds;
	}

	public void setLanguageIds(String[] languageIds) {
		this.languageIds = languageIds;
	}

	public void setLanguageIds(
		UnsafeSupplier<String[], Exception> languageIdsUnsafeSupplier) {

		try {
			languageIds = languageIdsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] languageIds;

	public String[] getModelClassNames() {
		return modelClassNames;
	}

	public void setModelClassNames(String[] modelClassNames) {
		this.modelClassNames = modelClassNames;
	}

	public void setModelClassNames(
		UnsafeSupplier<String[], Exception> modelClassNamesUnsafeSupplier) {

		try {
			modelClassNames = modelClassNamesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] modelClassNames;

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public void setProviderName(
		UnsafeSupplier<String, Exception> providerNameUnsafeSupplier) {

		try {
			providerName = providerNameUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String providerName;

	@Override
	public EmbeddingProviderConfiguration clone()
		throws CloneNotSupportedException {

		return (EmbeddingProviderConfiguration)super.clone();
	}

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
		return EmbeddingProviderConfigurationSerDes.toJSON(this);
	}

}