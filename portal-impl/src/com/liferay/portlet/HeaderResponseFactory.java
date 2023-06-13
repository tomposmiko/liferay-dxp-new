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

package com.liferay.portlet;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.portlet.LiferayHeaderResponse;
import com.liferay.portlet.internal.HeaderRequestImpl;
import com.liferay.portlet.internal.HeaderResponseImpl;

import javax.portlet.HeaderRequest;
import javax.portlet.filter.HeaderRequestWrapper;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Neil Griffin
 */
@ProviderType
public class HeaderResponseFactory {

	public static LiferayHeaderResponse create(
		HeaderRequest headerRequest, HttpServletResponse response) {

		while (headerRequest instanceof HeaderRequestWrapper) {
			headerRequest = ((HeaderRequestWrapper)headerRequest).getRequest();
		}

		HeaderResponseImpl headerResponseImpl = new HeaderResponseImpl();

		headerResponseImpl.init((HeaderRequestImpl)headerRequest, response);

		return headerResponseImpl;
	}

}