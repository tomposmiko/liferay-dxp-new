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
@GraphQLName("DataRecordCollectionPermission")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "DataRecordCollectionPermission")
public class DataRecordCollectionPermission {

	public Boolean getAddDataRecord() {
		return addDataRecord;
	}

	public void setAddDataRecord(Boolean addDataRecord) {
		this.addDataRecord = addDataRecord;
	}

	@JsonIgnore
	public void setAddDataRecord(
		UnsafeSupplier<Boolean, Exception> addDataRecordUnsafeSupplier) {

		try {
			addDataRecord = addDataRecordUnsafeSupplier.get();
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
	protected Boolean addDataRecord;

	public Boolean getAddDataRecordCollection() {
		return addDataRecordCollection;
	}

	public void setAddDataRecordCollection(Boolean addDataRecordCollection) {
		this.addDataRecordCollection = addDataRecordCollection;
	}

	@JsonIgnore
	public void setAddDataRecordCollection(
		UnsafeSupplier<Boolean, Exception>
			addDataRecordCollectionUnsafeSupplier) {

		try {
			addDataRecordCollection =
				addDataRecordCollectionUnsafeSupplier.get();
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
	protected Boolean addDataRecordCollection;

	public Boolean getDefinePermissions() {
		return definePermissions;
	}

	public void setDefinePermissions(Boolean definePermissions) {
		this.definePermissions = definePermissions;
	}

	@JsonIgnore
	public void setDefinePermissions(
		UnsafeSupplier<Boolean, Exception> definePermissionsUnsafeSupplier) {

		try {
			definePermissions = definePermissionsUnsafeSupplier.get();
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
	protected Boolean definePermissions;

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	@JsonIgnore
	public void setDelete(
		UnsafeSupplier<Boolean, Exception> deleteUnsafeSupplier) {

		try {
			delete = deleteUnsafeSupplier.get();
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
	protected Boolean delete;

	public Boolean getDeleteDataRecord() {
		return deleteDataRecord;
	}

	public void setDeleteDataRecord(Boolean deleteDataRecord) {
		this.deleteDataRecord = deleteDataRecord;
	}

	@JsonIgnore
	public void setDeleteDataRecord(
		UnsafeSupplier<Boolean, Exception> deleteDataRecordUnsafeSupplier) {

		try {
			deleteDataRecord = deleteDataRecordUnsafeSupplier.get();
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
	protected Boolean deleteDataRecord;

	public Boolean getExportDataRecord() {
		return exportDataRecord;
	}

	public void setExportDataRecord(Boolean exportDataRecord) {
		this.exportDataRecord = exportDataRecord;
	}

	@JsonIgnore
	public void setExportDataRecord(
		UnsafeSupplier<Boolean, Exception> exportDataRecordUnsafeSupplier) {

		try {
			exportDataRecord = exportDataRecordUnsafeSupplier.get();
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
	protected Boolean exportDataRecord;

	public String[] getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String[] roleNames) {
		this.roleNames = roleNames;
	}

	@JsonIgnore
	public void setRoleNames(
		UnsafeSupplier<String[], Exception> roleNamesUnsafeSupplier) {

		try {
			roleNames = roleNamesUnsafeSupplier.get();
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
	protected String[] roleNames;

	public Boolean getUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	@JsonIgnore
	public void setUpdate(
		UnsafeSupplier<Boolean, Exception> updateUnsafeSupplier) {

		try {
			update = updateUnsafeSupplier.get();
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
	protected Boolean update;

	public Boolean getUpdateDataRecord() {
		return updateDataRecord;
	}

	public void setUpdateDataRecord(Boolean updateDataRecord) {
		this.updateDataRecord = updateDataRecord;
	}

	@JsonIgnore
	public void setUpdateDataRecord(
		UnsafeSupplier<Boolean, Exception> updateDataRecordUnsafeSupplier) {

		try {
			updateDataRecord = updateDataRecordUnsafeSupplier.get();
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
	protected Boolean updateDataRecord;

	public Boolean getView() {
		return view;
	}

	public void setView(Boolean view) {
		this.view = view;
	}

	@JsonIgnore
	public void setView(UnsafeSupplier<Boolean, Exception> viewUnsafeSupplier) {
		try {
			view = viewUnsafeSupplier.get();
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
	protected Boolean view;

	public Boolean getViewDataRecord() {
		return viewDataRecord;
	}

	public void setViewDataRecord(Boolean viewDataRecord) {
		this.viewDataRecord = viewDataRecord;
	}

	@JsonIgnore
	public void setViewDataRecord(
		UnsafeSupplier<Boolean, Exception> viewDataRecordUnsafeSupplier) {

		try {
			viewDataRecord = viewDataRecordUnsafeSupplier.get();
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
	protected Boolean viewDataRecord;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof DataRecordCollectionPermission)) {
			return false;
		}

		DataRecordCollectionPermission dataRecordCollectionPermission =
			(DataRecordCollectionPermission)object;

		return Objects.equals(
			toString(), dataRecordCollectionPermission.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		sb.append("\"addDataRecord\": ");

		if (addDataRecord == null) {
			sb.append("null");
		}
		else {
			sb.append(addDataRecord);
		}

		sb.append(", ");

		sb.append("\"addDataRecordCollection\": ");

		if (addDataRecordCollection == null) {
			sb.append("null");
		}
		else {
			sb.append(addDataRecordCollection);
		}

		sb.append(", ");

		sb.append("\"definePermissions\": ");

		if (definePermissions == null) {
			sb.append("null");
		}
		else {
			sb.append(definePermissions);
		}

		sb.append(", ");

		sb.append("\"delete\": ");

		if (delete == null) {
			sb.append("null");
		}
		else {
			sb.append(delete);
		}

		sb.append(", ");

		sb.append("\"deleteDataRecord\": ");

		if (deleteDataRecord == null) {
			sb.append("null");
		}
		else {
			sb.append(deleteDataRecord);
		}

		sb.append(", ");

		sb.append("\"exportDataRecord\": ");

		if (exportDataRecord == null) {
			sb.append("null");
		}
		else {
			sb.append(exportDataRecord);
		}

		sb.append(", ");

		sb.append("\"roleNames\": ");

		if (roleNames == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0; i < roleNames.length; i++) {
				sb.append("\"");
				sb.append(roleNames[i]);
				sb.append("\"");

				if ((i + 1) < roleNames.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append(", ");

		sb.append("\"update\": ");

		if (update == null) {
			sb.append("null");
		}
		else {
			sb.append(update);
		}

		sb.append(", ");

		sb.append("\"updateDataRecord\": ");

		if (updateDataRecord == null) {
			sb.append("null");
		}
		else {
			sb.append(updateDataRecord);
		}

		sb.append(", ");

		sb.append("\"view\": ");

		if (view == null) {
			sb.append("null");
		}
		else {
			sb.append(view);
		}

		sb.append(", ");

		sb.append("\"viewDataRecord\": ");

		if (viewDataRecord == null) {
			sb.append("null");
		}
		else {
			sb.append(viewDataRecord);
		}

		sb.append("}");

		return sb.toString();
	}

}