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

package com.liferay.batch.planner.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.batch.planner.service.http.BatchPlannerMappingServiceSoap}.
 *
 * @author Igor Beslic
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class BatchPlannerMappingSoap implements Serializable {

	public static BatchPlannerMappingSoap toSoapModel(
		BatchPlannerMapping model) {

		BatchPlannerMappingSoap soapModel = new BatchPlannerMappingSoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setBatchPlannerMappingId(model.getBatchPlannerMappingId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setBatchPlannerPlanId(model.getBatchPlannerPlanId());
		soapModel.setExternalFieldName(model.getExternalFieldName());
		soapModel.setExternalFieldType(model.getExternalFieldType());
		soapModel.setInternalFieldName(model.getInternalFieldName());
		soapModel.setInternalFieldType(model.getInternalFieldType());
		soapModel.setScript(model.getScript());

		return soapModel;
	}

	public static BatchPlannerMappingSoap[] toSoapModels(
		BatchPlannerMapping[] models) {

		BatchPlannerMappingSoap[] soapModels =
			new BatchPlannerMappingSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static BatchPlannerMappingSoap[][] toSoapModels(
		BatchPlannerMapping[][] models) {

		BatchPlannerMappingSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels =
				new BatchPlannerMappingSoap[models.length][models[0].length];
		}
		else {
			soapModels = new BatchPlannerMappingSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static BatchPlannerMappingSoap[] toSoapModels(
		List<BatchPlannerMapping> models) {

		List<BatchPlannerMappingSoap> soapModels =
			new ArrayList<BatchPlannerMappingSoap>(models.size());

		for (BatchPlannerMapping model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(
			new BatchPlannerMappingSoap[soapModels.size()]);
	}

	public BatchPlannerMappingSoap() {
	}

	public long getPrimaryKey() {
		return _batchPlannerMappingId;
	}

	public void setPrimaryKey(long pk) {
		setBatchPlannerMappingId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public long getBatchPlannerMappingId() {
		return _batchPlannerMappingId;
	}

	public void setBatchPlannerMappingId(long batchPlannerMappingId) {
		_batchPlannerMappingId = batchPlannerMappingId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public long getBatchPlannerPlanId() {
		return _batchPlannerPlanId;
	}

	public void setBatchPlannerPlanId(long batchPlannerPlanId) {
		_batchPlannerPlanId = batchPlannerPlanId;
	}

	public String getExternalFieldName() {
		return _externalFieldName;
	}

	public void setExternalFieldName(String externalFieldName) {
		_externalFieldName = externalFieldName;
	}

	public String getExternalFieldType() {
		return _externalFieldType;
	}

	public void setExternalFieldType(String externalFieldType) {
		_externalFieldType = externalFieldType;
	}

	public String getInternalFieldName() {
		return _internalFieldName;
	}

	public void setInternalFieldName(String internalFieldName) {
		_internalFieldName = internalFieldName;
	}

	public String getInternalFieldType() {
		return _internalFieldType;
	}

	public void setInternalFieldType(String internalFieldType) {
		_internalFieldType = internalFieldType;
	}

	public String getScript() {
		return _script;
	}

	public void setScript(String script) {
		_script = script;
	}

	private long _mvccVersion;
	private long _batchPlannerMappingId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private long _batchPlannerPlanId;
	private String _externalFieldName;
	private String _externalFieldType;
	private String _internalFieldName;
	private String _internalFieldType;
	private String _script;

}