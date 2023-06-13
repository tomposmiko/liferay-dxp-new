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

package com.liferay.template.taglib.internal.util;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.osgi.util.service.Snapshot;
import com.liferay.portlet.display.template.PortletDisplayTemplate;

/**
 * @author Eudaldo Alonso
 */
public class PortletDisplayTemplateUtil {

	public static String getDisplayStyle(String ddmTemplateKey) {
		PortletDisplayTemplate portletDisplayTemplate =
			_portletDisplayTemplateSnapshot.get();

		return portletDisplayTemplate.getDisplayStyle(ddmTemplateKey);
	}

	public static DDMTemplate getPortletDisplayTemplateDDMTemplate(
		long groupId, long classNameId, String displayStyle,
		boolean useDefault) {

		PortletDisplayTemplate portletDisplayTemplate =
			_portletDisplayTemplateSnapshot.get();

		return portletDisplayTemplate.getPortletDisplayTemplateDDMTemplate(
			groupId, classNameId, displayStyle, useDefault);
	}

	private static final Snapshot<PortletDisplayTemplate>
		_portletDisplayTemplateSnapshot = new Snapshot<>(
			PortletDisplayTemplateUtil.class, PortletDisplayTemplate.class);

}