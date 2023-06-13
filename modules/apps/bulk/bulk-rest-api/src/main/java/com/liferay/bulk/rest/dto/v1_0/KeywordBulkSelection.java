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

package com.liferay.bulk.rest.dto.v1_0;

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
 * @author Alejandro Tardín
 * @generated
 */
@Generated("")
@GraphQLName("KeywordBulkSelection")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "KeywordBulkSelection")
public class KeywordBulkSelection {

	public DocumentBulkSelection getDocumentBulkSelection() {
		return documentBulkSelection;
	}

	public void setDocumentBulkSelection(
		DocumentBulkSelection documentBulkSelection) {

		this.documentBulkSelection = documentBulkSelection;
	}

	@JsonIgnore
	public void setDocumentBulkSelection(
		UnsafeSupplier<DocumentBulkSelection, Exception>
			documentBulkSelectionUnsafeSupplier) {

		try {
			documentBulkSelection = documentBulkSelectionUnsafeSupplier.get();
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
	protected DocumentBulkSelection documentBulkSelection;

	public String[] getKeywordsToAdd() {
		return keywordsToAdd;
	}

	public void setKeywordsToAdd(String[] keywordsToAdd) {
		this.keywordsToAdd = keywordsToAdd;
	}

	@JsonIgnore
	public void setKeywordsToAdd(
		UnsafeSupplier<String[], Exception> keywordsToAddUnsafeSupplier) {

		try {
			keywordsToAdd = keywordsToAddUnsafeSupplier.get();
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
	protected String[] keywordsToAdd;

	public String[] getKeywordsToRemove() {
		return keywordsToRemove;
	}

	public void setKeywordsToRemove(String[] keywordsToRemove) {
		this.keywordsToRemove = keywordsToRemove;
	}

	@JsonIgnore
	public void setKeywordsToRemove(
		UnsafeSupplier<String[], Exception> keywordsToRemoveUnsafeSupplier) {

		try {
			keywordsToRemove = keywordsToRemoveUnsafeSupplier.get();
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
	protected String[] keywordsToRemove;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof KeywordBulkSelection)) {
			return false;
		}

		KeywordBulkSelection keywordBulkSelection =
			(KeywordBulkSelection)object;

		return Objects.equals(toString(), keywordBulkSelection.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		sb.append("\"documentBulkSelection\": ");

		if (documentBulkSelection == null) {
			sb.append("null");
		}
		else {
			sb.append(documentBulkSelection);
		}

		sb.append(", ");

		sb.append("\"keywordsToAdd\": ");

		if (keywordsToAdd == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0; i < keywordsToAdd.length; i++) {
				sb.append("\"");
				sb.append(keywordsToAdd[i]);
				sb.append("\"");

				if ((i + 1) < keywordsToAdd.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append(", ");

		sb.append("\"keywordsToRemove\": ");

		if (keywordsToRemove == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0; i < keywordsToRemove.length; i++) {
				sb.append("\"");
				sb.append(keywordsToRemove[i]);
				sb.append("\"");

				if ((i + 1) < keywordsToRemove.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

}