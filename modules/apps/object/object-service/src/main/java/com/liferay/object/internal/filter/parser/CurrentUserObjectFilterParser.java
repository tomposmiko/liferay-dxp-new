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

package com.liferay.object.internal.filter.parser;

import com.liferay.object.constants.ObjectFilterConstants;
import com.liferay.object.model.ObjectFilter;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Gabriel Albuquerque
 */
@Component(
	property = "filter.type=" + ObjectFilterConstants.TYPE_CURRENT_USER,
	service = ObjectFilterParser.class
)
public class CurrentUserObjectFilterParser implements ObjectFilterParser {

	@Override
	public String parse(ObjectFilter objectFilter) {
		Map<String, Object> map = ObjectMapperUtil.readValue(
			Map.class, objectFilter.getJSON());

		return StringBundler.concat(
			"(", objectFilter.getFilterBy(), " eq ",
			String.valueOf(map.get("currentUserId")), ")");
	}

}