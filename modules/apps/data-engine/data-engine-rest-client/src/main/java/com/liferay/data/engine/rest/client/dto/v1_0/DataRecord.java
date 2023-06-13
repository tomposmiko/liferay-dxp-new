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

package com.liferay.data.engine.rest.client.dto.v1_0;

import com.liferay.data.engine.rest.client.function.UnsafeSupplier;

import javax.annotation.Generated;

/**
 * @author Jeyvison Nascimento
 * @generated
 */
@Generated("")
public class DataRecord {

	public Long getDataRecordCollectionId() {
		return dataRecordCollectionId;
	}

	public void setDataRecordCollectionId(Long dataRecordCollectionId) {
		this.dataRecordCollectionId = dataRecordCollectionId;
	}

	public void setDataRecordCollectionId(
		UnsafeSupplier<Long, Exception> dataRecordCollectionIdUnsafeSupplier) {

		try {
			dataRecordCollectionId = dataRecordCollectionIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long dataRecordCollectionId;

	public DataRecordValue[] getDataRecordValues() {
		return dataRecordValues;
	}

	public void setDataRecordValues(DataRecordValue[] dataRecordValues) {
		this.dataRecordValues = dataRecordValues;
	}

	public void setDataRecordValues(
		UnsafeSupplier<DataRecordValue[], Exception>
			dataRecordValuesUnsafeSupplier) {

		try {
			dataRecordValues = dataRecordValuesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected DataRecordValue[] dataRecordValues;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(UnsafeSupplier<Long, Exception> idUnsafeSupplier) {
		try {
			id = idUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long id;

}