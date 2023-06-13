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

package com.liferay.portal.search.engine.adapter.document;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.search.Document;

import java.util.function.Consumer;

/**
 * @author Michael C. Han
 */
@ProviderType
public class UpdateDocumentRequest
	implements BulkableDocumentRequest<UpdateDocumentRequest>,
			   DocumentRequest<UpdateDocumentResponse> {

	public UpdateDocumentRequest(
		String indexName, String uid, Document document) {

		_indexName = indexName;
		_uid = uid;
		_document = document;
	}

	@Override
	public void accept(Consumer<UpdateDocumentRequest> consumer) {
		consumer.accept(this);
	}

	@Override
	public UpdateDocumentResponse accept(
		DocumentRequestExecutor documentRequestExecutor) {

		return documentRequestExecutor.executeDocumentRequest(this);
	}

	public Document getDocument() {
		return _document;
	}

	public String getIndexName() {
		return _indexName;
	}

	public String getUid() {
		return _uid;
	}

	public boolean isRefresh() {
		return _refresh;
	}

	public void setRefresh(boolean refresh) {
		_refresh = refresh;
	}

	private final Document _document;
	private final String _indexName;
	private boolean _refresh;
	private final String _uid;

}