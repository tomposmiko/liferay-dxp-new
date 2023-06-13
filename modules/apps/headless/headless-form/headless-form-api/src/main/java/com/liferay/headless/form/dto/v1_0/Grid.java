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

package com.liferay.headless.form.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

import javax.annotation.Generated;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
@GraphQLName("Grid")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "Grid")
public class Grid {

	@Schema(description = "https://www.schema.org/FormFieldOption")
	public Column[] getColumns() {
		return columns;
	}

	public void setColumns(Column[] columns) {
		this.columns = columns;
	}

	@JsonIgnore
	public void setColumns(
		UnsafeSupplier<Column[], Exception> columnsUnsafeSupplier) {

		try {
			columns = columnsUnsafeSupplier.get();
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
	protected Column[] columns;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public void setId(UnsafeSupplier<Long, Exception> idUnsafeSupplier) {
		try {
			id = idUnsafeSupplier.get();
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
	protected Long id;

	@Schema(description = "https://www.schema.org/FormFieldOption")
	public Row[] getRows() {
		return rows;
	}

	public void setRows(Row[] rows) {
		this.rows = rows;
	}

	@JsonIgnore
	public void setRows(UnsafeSupplier<Row[], Exception> rowsUnsafeSupplier) {
		try {
			rows = rowsUnsafeSupplier.get();
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
	protected Row[] rows;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Grid)) {
			return false;
		}

		Grid grid = (Grid)object;

		return Objects.equals(toString(), grid.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		sb.append("\"columns\": ");

		if (columns == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0; i < columns.length; i++) {
				sb.append(columns[i]);

				if ((i + 1) < columns.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append(", ");

		sb.append("\"id\": ");

		sb.append(id);
		sb.append(", ");

		sb.append("\"rows\": ");

		if (rows == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0; i < rows.length; i++) {
				sb.append(rows[i]);

				if ((i + 1) < rows.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

}