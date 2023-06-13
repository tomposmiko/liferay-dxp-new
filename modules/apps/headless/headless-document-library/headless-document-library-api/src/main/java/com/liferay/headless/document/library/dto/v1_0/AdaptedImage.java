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

package com.liferay.headless.document.library.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;

import java.util.Objects;

import javax.annotation.Generated;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
@GraphQLName("AdaptedImage")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "AdaptedImage")
public class AdaptedImage {

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	@JsonIgnore
	public void setContentUrl(
		UnsafeSupplier<String, Exception> contentUrlUnsafeSupplier) {

		try {
			contentUrl = contentUrlUnsafeSupplier.get();
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
	protected String contentUrl;

	public Number getHeight() {
		return height;
	}

	public void setHeight(Number height) {
		this.height = height;
	}

	@JsonIgnore
	public void setHeight(
		UnsafeSupplier<Number, Exception> heightUnsafeSupplier) {

		try {
			height = heightUnsafeSupplier.get();
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
	protected Number height;

	public String getResolutionName() {
		return resolutionName;
	}

	public void setResolutionName(String resolutionName) {
		this.resolutionName = resolutionName;
	}

	@JsonIgnore
	public void setResolutionName(
		UnsafeSupplier<String, Exception> resolutionNameUnsafeSupplier) {

		try {
			resolutionName = resolutionNameUnsafeSupplier.get();
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
	protected String resolutionName;

	public Number getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(Number sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}

	@JsonIgnore
	public void setSizeInBytes(
		UnsafeSupplier<Number, Exception> sizeInBytesUnsafeSupplier) {

		try {
			sizeInBytes = sizeInBytesUnsafeSupplier.get();
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
	protected Number sizeInBytes;

	public Number getWidth() {
		return width;
	}

	public void setWidth(Number width) {
		this.width = width;
	}

	@JsonIgnore
	public void setWidth(
		UnsafeSupplier<Number, Exception> widthUnsafeSupplier) {

		try {
			width = widthUnsafeSupplier.get();
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
	protected Number width;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AdaptedImage)) {
			return false;
		}

		AdaptedImage adaptedImage = (AdaptedImage)object;

		return Objects.equals(toString(), adaptedImage.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		sb.append("\"contentUrl\": ");

		sb.append("\"");
		sb.append(contentUrl);
		sb.append("\"");
		sb.append(", ");

		sb.append("\"height\": ");

		sb.append(height);
		sb.append(", ");

		sb.append("\"resolutionName\": ");

		sb.append("\"");
		sb.append(resolutionName);
		sb.append("\"");
		sb.append(", ");

		sb.append("\"sizeInBytes\": ");

		sb.append(sizeInBytes);
		sb.append(", ");

		sb.append("\"width\": ");

		sb.append(width);

		sb.append("}");

		return sb.toString();
	}

}