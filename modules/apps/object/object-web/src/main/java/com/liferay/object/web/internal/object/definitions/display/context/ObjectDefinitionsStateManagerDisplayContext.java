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

package com.liferay.object.web.internal.object.definitions.display.context;

import com.liferay.frontend.data.set.model.FDSActionDropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Carlos Montenegro
 */
public class ObjectDefinitionsStateManagerDisplayContext
	extends BaseObjectDefinitionsDisplayContext {

	public ObjectDefinitionsStateManagerDisplayContext(
		HttpServletRequest httpServletRequest,
		ModelResourcePermission<ObjectDefinition>
			objectDefinitionModelResourcePermission) {

		super(httpServletRequest, objectDefinitionModelResourcePermission);
	}

	public CreationMenu getCreationMenu() throws PortalException {
		return new CreationMenu();
	}

	public List<FDSActionDropdownItem> getFDSActionDropdownItems()
		throws Exception {

		return Collections.singletonList(
			new FDSActionDropdownItem(
				PortletURLBuilder.create(
					getPortletURL()
				).setMVCRenderCommandName(
					"/object_definitions/edit_object_state"
				).setParameter(
					"objectFieldId", "{id}"
				).setWindowState(
					LiferayWindowState.POP_UP
				).buildString(),
				"view", "view",
				LanguageUtil.get(objectRequestHelper.getRequest(), "view"),
				"get", null, "sidePanel"));
	}

	public JSONObject getObjectStateFlowJSONObject(ObjectField objectField) {
		return JSONUtil.put(
			"label", objectField.getLabelMap()
		).put(
			"listTypeDefinitionId",
			Long.valueOf(objectField.getListTypeDefinitionId())
		).put(
			"objectFieldId", objectField.getObjectFieldId()
		);
	}

	@Override
	protected String getAPIURI() {
		return "/object-fields";
	}

}