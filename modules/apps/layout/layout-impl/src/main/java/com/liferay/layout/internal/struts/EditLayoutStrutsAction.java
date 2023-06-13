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

package com.liferay.layout.internal.struts;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.LayoutTypeException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutService;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ming-Gih Lam
 * @author Hugo Huijser
 */
@Component(property = "path=/portal/edit_layout", service = StrutsAction.class)
public class EditLayoutStrutsAction implements StrutsAction {

	@Override
	public String execute(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		try {
			_updateParentLayoutId(httpServletRequest);

			jsonObject.put("status", HttpServletResponse.SC_OK);
		}
		catch (LayoutTypeException layoutTypeException) {
			jsonObject.put(
				"message",
				_getLayoutTypeExceptionMessage(
					httpServletRequest, layoutTypeException));

			long plid = ParamUtil.getLong(httpServletRequest, "plid");

			if ((layoutTypeException.getType() ==
					LayoutTypeException.FIRST_LAYOUT) &&
				(plid > 0)) {

				Layout layout = _layoutLocalService.getLayout(plid);

				jsonObject.put(
					"groupId", layout.getGroupId()
				).put(
					"layoutId", layout.getLayoutId()
				).put(
					"originalParentLayoutId", layout.getParentLayoutId()
				).put(
					"originalParentPlid", layout.getParentPlid()
				).put(
					"originalPriority", layout.getPriority()
				).put(
					"plid", plid
				).put(
					"status", HttpServletResponse.SC_BAD_REQUEST
				);
			}
		}

		ServletResponseUtil.write(httpServletResponse, jsonObject.toString());

		return null;
	}

	private String _getLayoutTypeExceptionMessage(
		HttpServletRequest httpServletRequest,
		LayoutTypeException layoutTypeException) {

		if (layoutTypeException.getType() == LayoutTypeException.FIRST_LAYOUT) {
			return _language.format(
				httpServletRequest,
				"you-cannot-move-this-page-because-the-resulting-order-would-" +
					"place-a-page-of-type-x-as-the-first-page",
				"layout.types." + layoutTypeException.getLayoutType());
		}

		if (layoutTypeException.getType() ==
				LayoutTypeException.NOT_PARENTABLE) {

			return _language.get(
				httpServletRequest,
				"a-page-cannot-become-a-child-of-a-page-that-is-not-" +
					"parentable");
		}

		return StringPool.BLANK;
	}

	private void _updateParentLayoutId(HttpServletRequest httpServletRequest)
		throws Exception {

		long plid = ParamUtil.getLong(httpServletRequest, "plid");
		long parentPlid = ParamUtil.getLong(httpServletRequest, "parentPlid");
		int priority = ParamUtil.getInteger(httpServletRequest, "priority");

		_layoutService.updateParentLayoutIdAndPriority(
			plid, parentPlid, priority);
	}

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutService _layoutService;

}