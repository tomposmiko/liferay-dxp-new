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

package com.liferay.batch.engine;

/**
 * @author Shuyang Zhou
 */
public enum BatchEngineTaskContentType {

	CSV("csv"), JSON("json"), JSONL("jsonl"), JSONT("batch-engine-data.json"),
	XLS("xls"), XLSX("xlsx");

	public String getFileExtension() {
		return _fileExtension;
	}

	private BatchEngineTaskContentType(String fileExtension) {
		_fileExtension = fileExtension;
	}

	private final String _fileExtension;

}