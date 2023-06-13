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

package com.liferay.digital.signature.model;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;

/**
 * @author Brian Wing Shun Chan
 */
public class DSDocument {

	public String getAssignTabsToDSRecipientId() {
		return assignTabsToDSRecipientId;
	}

	public String getData() {
		return data;
	}

	public String getDSDocumentId() {
		return dsDocumentId;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public String getName() {
		return name;
	}

	public String getURI() {
		return uri;
	}

	public boolean isTransformPDFFields() {
		return transformPDFFields;
	}

	public void setAssignTabsToDSRecipientId(String assignTabsToDSRecipientId) {
		this.assignTabsToDSRecipientId = assignTabsToDSRecipientId;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setDSDocumentId(String dsDocumentId) {
		this.dsDocumentId = dsDocumentId;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTransformPDFFields(boolean transformPDFFields) {
		this.transformPDFFields = transformPDFFields;
	}

	public void setURI(String uri) {
		this.uri = uri;
	}

	public JSONObject toJSONObject() {
		return JSONUtil.put(
			"assignTabsToRecipientId", getAssignTabsToDSRecipientId()
		).put(
			"documentBase64", getData()
		).put(
			"documentId", getDSDocumentId()
		).put(
			"fileExtension", getFileExtension()
		).put(
			"name", getName()
		).put(
			"transformPdfFields", isTransformPDFFields()
		);
	}

	protected String assignTabsToDSRecipientId;
	protected String data;
	protected String dsDocumentId;
	protected String fileExtension;
	protected String name;
	protected boolean transformPDFFields;
	protected String uri;

}