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

package com.liferay.client.extension.type;

import com.liferay.client.extension.type.annotation.CETProperty;
import com.liferay.client.extension.type.annotation.CETType;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Brian Wing Shun Chan
 */
@CETType(description = "This is a description.", name = "customElement")
@ProviderType
public interface CustomElementCET extends CET {

	@CETProperty(defaultValue = "", name = "cssURLs", type = "list", url = true)
	public String getCSSURLs();

	@CETProperty(
		defaultValue = "", name = "friendlyURLMapping", type = "string"
	)
	public String getFriendlyURLMapping();

	@CETProperty(defaultValue = "", name = "htmlElementName", type = "string")
	public String getHTMLElementName();

	@CETProperty(
		defaultValue = "", name = "portletCategoryName", type = "string"
	)
	public String getPortletCategoryName();

	@CETProperty(defaultValue = "", name = "urls", type = "list", url = true)
	public String getURLs();

	@CETProperty(
		defaultValue = "false", name = "instanceable", type = "boolean"
	)
	public boolean isInstanceable();

	@CETProperty(defaultValue = "false", name = "useESM", type = "boolean")
	public boolean isUseESM();

}