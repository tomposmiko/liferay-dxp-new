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

package com.liferay.data.engine.service;

/**
 * @author Jeyvison Nascimento
 */
public class DEDataDefinitionCountRequest {

	public long getCompanyId() {
		return _companyId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public static final class Builder {

		public DEDataDefinitionCountRequest build() {
			return _deDataDefinitionCountRequest;
		}

		public Builder inCompany(long companyId) {
			_deDataDefinitionCountRequest._companyId = companyId;

			return this;
		}

		public Builder inGroup(long groupId) {
			_deDataDefinitionCountRequest._groupId = groupId;

			return this;
		}

		private final DEDataDefinitionCountRequest
			_deDataDefinitionCountRequest = new DEDataDefinitionCountRequest();

	}

	private DEDataDefinitionCountRequest() {
	}

	private long _companyId;
	private long _groupId;

}