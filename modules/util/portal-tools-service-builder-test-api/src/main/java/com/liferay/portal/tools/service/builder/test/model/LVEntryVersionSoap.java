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

package com.liferay.portal.tools.service.builder.test.model;

import aQute.bnd.annotation.ProviderType;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@ProviderType
public class LVEntryVersionSoap implements Serializable {
	public static LVEntryVersionSoap toSoapModel(LVEntryVersion model) {
		LVEntryVersionSoap soapModel = new LVEntryVersionSoap();

		soapModel.setLvEntryVersionId(model.getLvEntryVersionId());
		soapModel.setVersion(model.getVersion());
		soapModel.setDefaultLanguageId(model.getDefaultLanguageId());
		soapModel.setLvEntryId(model.getLvEntryId());
		soapModel.setGroupId(model.getGroupId());

		return soapModel;
	}

	public static LVEntryVersionSoap[] toSoapModels(LVEntryVersion[] models) {
		LVEntryVersionSoap[] soapModels = new LVEntryVersionSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static LVEntryVersionSoap[][] toSoapModels(LVEntryVersion[][] models) {
		LVEntryVersionSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new LVEntryVersionSoap[models.length][models[0].length];
		}
		else {
			soapModels = new LVEntryVersionSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static LVEntryVersionSoap[] toSoapModels(List<LVEntryVersion> models) {
		List<LVEntryVersionSoap> soapModels = new ArrayList<LVEntryVersionSoap>(models.size());

		for (LVEntryVersion model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new LVEntryVersionSoap[soapModels.size()]);
	}

	public LVEntryVersionSoap() {
	}

	public long getPrimaryKey() {
		return _lvEntryVersionId;
	}

	public void setPrimaryKey(long pk) {
		setLvEntryVersionId(pk);
	}

	public long getLvEntryVersionId() {
		return _lvEntryVersionId;
	}

	public void setLvEntryVersionId(long lvEntryVersionId) {
		_lvEntryVersionId = lvEntryVersionId;
	}

	public int getVersion() {
		return _version;
	}

	public void setVersion(int version) {
		_version = version;
	}

	public String getDefaultLanguageId() {
		return _defaultLanguageId;
	}

	public void setDefaultLanguageId(String defaultLanguageId) {
		_defaultLanguageId = defaultLanguageId;
	}

	public long getLvEntryId() {
		return _lvEntryId;
	}

	public void setLvEntryId(long lvEntryId) {
		_lvEntryId = lvEntryId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	private long _lvEntryVersionId;
	private int _version;
	private String _defaultLanguageId;
	private long _lvEntryId;
	private long _groupId;
}