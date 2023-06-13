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

package com.liferay.segments.internal.asah.client.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Matthew Kong
 */
public class Individual {

	public Individual() {
	}

	public Map<String, Set<String>> getDataSourceIndividualPKs() {
		return _dataSourceIndividualPKs;
	}

	public Date getDateCreated() {
		return _dateCreated;
	}

	public Date getDateModified() {
		return _dateModified;
	}

	public Map<String, List<Field>> getDemographics() {
		return _demographics;
	}

	public String getId() {
		return _id;
	}

	public void setDataSourceIndividualPKs(
		Map<String, Set<String>> dataSourceIndividualPKs) {

		_dataSourceIndividualPKs = dataSourceIndividualPKs;
	}

	public void setDateCreated(Date dateCreated) {
		_dateCreated = dateCreated;
	}

	public void setDateModified(Date dateModified) {
		_dateModified = dateModified;
	}

	public void setDemographics(Map<String, List<Field>> demographics) {
		_demographics = demographics;
	}

	public void setId(String id) {
		_id = id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(11);

		sb.append("{dataSourceIndividualPKs=");
		sb.append(_dataSourceIndividualPKs);
		sb.append(", dateCreated=");
		sb.append(_dateCreated);
		sb.append(", dateModified=");
		sb.append(_dateModified);
		sb.append(", demographics=");
		sb.append(_demographics);
		sb.append(", id=");
		sb.append(_id);
		sb.append("}");

		return sb.toString();
	}

	private Map<String, Set<String>> _dataSourceIndividualPKs = new HashMap<>();
	private Date _dateCreated;
	private Date _dateModified;
	private Map<String, List<Field>> _demographics = new HashMap<>();
	private String _id;

}