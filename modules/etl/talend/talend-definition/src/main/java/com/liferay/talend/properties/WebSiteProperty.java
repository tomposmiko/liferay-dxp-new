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

package com.liferay.talend.properties;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

/**
 * @author Zoltán Takács
 */
public class WebSiteProperty extends BaseContextAwareStringProperty {

	public WebSiteProperty(String name) {
		super(name);
	}

	public String getWebSiteURL() {
		UriBuilder uriBuilder = UriBuilder.fromPath(getHost());

		URI webSiteURI = uriBuilder.path(
			"p"
		).path(
			"commerce-web-site"
		).path(
			"{webSiteId}"
		).build(
			getValue()
		);

		return webSiteURI.toString();
	}

}