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

package com.liferay.data.engine.rest.dto.v1_0;

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
 * @author Jeyvison Nascimento
 * @generated
 */
@Generated("")
@GraphQLName("DataLayoutColumn")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "DataLayoutColumn")
public class DataLayoutColumn {

	public Integer getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(Integer columnSize) {
		this.columnSize = columnSize;
	}

	@JsonIgnore
	public void setColumnSize(
		UnsafeSupplier<Integer, Exception> columnSizeUnsafeSupplier) {

		try {
			columnSize = columnSizeUnsafeSupplier.get();
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
	protected Integer columnSize;

	public String[] getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	@JsonIgnore
	public void setFieldNames(
		UnsafeSupplier<String[], Exception> fieldNamesUnsafeSupplier) {

		try {
			fieldNames = fieldNamesUnsafeSupplier.get();
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
	protected String[] fieldNames;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof DataLayoutColumn)) {
			return false;
		}

		DataLayoutColumn dataLayoutColumn = (DataLayoutColumn)object;

		return Objects.equals(toString(), dataLayoutColumn.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		sb.append("\"columnSize\": ");

		if (columnSize == null) {
			sb.append("null");
		}
		else {
			sb.append(columnSize);
		}

		sb.append(", ");

		sb.append("\"fieldNames\": ");

		if (fieldNames == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0; i < fieldNames.length; i++) {
				sb.append("\"");
				sb.append(fieldNames[i]);
				sb.append("\"");

				if ((i + 1) < fieldNames.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

}