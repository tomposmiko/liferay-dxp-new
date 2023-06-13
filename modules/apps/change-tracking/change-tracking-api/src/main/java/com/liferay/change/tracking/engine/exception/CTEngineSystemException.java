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

package com.liferay.change.tracking.engine.exception;

import com.liferay.portal.kernel.exception.SystemException;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Gergely Mathe
 */
@ProviderType
public class CTEngineSystemException extends SystemException {

	public CTEngineSystemException(long companyId) {
		_companyId = companyId;
	}

	public CTEngineSystemException(long companyId, String msg) {
		super(msg);

		_companyId = companyId;
	}

	public CTEngineSystemException(
		long companyId, String msg, Throwable cause) {

		super(msg, cause);

		_companyId = companyId;
	}

	public CTEngineSystemException(long companyId, Throwable cause) {
		super(cause);

		_companyId = companyId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	private final long _companyId;

}