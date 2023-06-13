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

package com.liferay.dynamic.data.mapping.form.builder.internal.servlet;

import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.dynamic.data.mapping.util.comparator.DataProviderInstanceNameComparator;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	property = {
		"dynamic.data.mapping.form.builder.servlet=true",
		"osgi.http.whiteboard.context.path=/dynamic-data-mapping-form-builder-data-provider-instances",
		"osgi.http.whiteboard.servlet.name=com.liferay.dynamic.data.mapping.form.builder.internal.servlet.DDMDataProviderInstancesServlet",
		"osgi.http.whiteboard.servlet.pattern=/dynamic-data-mapping-form-builder-data-provider-instances/*"
	},
	service = Servlet.class
)
public class DDMDataProviderInstancesServlet extends BaseDDMFormBuilderServlet {

	@Override
	protected void doGet(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		JSONArray dataProviderInstancesJSONArray =
			_getDataProviderInstancesJSONArray(httpServletRequest);

		if (dataProviderInstancesJSONArray == null) {
			httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);

			return;
		}

		httpServletResponse.setContentType(ContentTypes.APPLICATION_JSON);
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);

		ServletResponseUtil.write(
			httpServletResponse, dataProviderInstancesJSONArray.toString());
	}

	private JSONArray _getDataProviderInstancesJSONArray(
		HttpServletRequest httpServletRequest) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		String languageId = ParamUtil.getString(
			httpServletRequest, "languageId", themeDisplay.getLanguageId());

		Locale locale = LocaleUtil.fromLanguageId(languageId);

		long scopeGroupId = ParamUtil.getLong(
			httpServletRequest, "scopeGroupId", themeDisplay.getScopeGroupId());

		Group scopeGroup = themeDisplay.getScopeGroup();

		if (scopeGroup.isStagingGroup()) {
			scopeGroupId = scopeGroup.getLiveGroupId();
		}

		int start = ParamUtil.getInteger(
			httpServletRequest, "start", QueryUtil.ALL_POS);
		int end = ParamUtil.getInteger(
			httpServletRequest, "end", QueryUtil.ALL_POS);

		DataProviderInstanceNameComparator dataProviderInstanceNameComparator =
			new DataProviderInstanceNameComparator(true);

		List<DDMDataProviderInstance> ddmDataProviderInstances =
			ListUtil.concat(
				_ddmDataProviderInstanceService.search(
					themeDisplay.getCompanyId(), new long[] {scopeGroupId},
					null, start, end, dataProviderInstanceNameComparator),
				_ddmDataProviderInstanceService.search(
					themeDisplay.getCompanyId(),
					_portal.getAncestorSiteGroupIds(scopeGroupId), null, start,
					end, dataProviderInstanceNameComparator));

		JSONArray dataProviderInstancesJSONArray =
			_jsonFactory.createJSONArray();

		for (DDMDataProviderInstance ddmDataProviderInstance :
				ddmDataProviderInstances) {

			JSONObject dataProviderInstanceJSONObject =
				_jsonFactory.createJSONObject();

			dataProviderInstanceJSONObject.put(
				"id", ddmDataProviderInstance.getDataProviderInstanceId()
			).put(
				"name", ddmDataProviderInstance.getName(locale)
			).put(
				"uuid", ddmDataProviderInstance.getUuid()
			);

			dataProviderInstancesJSONArray.put(dataProviderInstanceJSONObject);
		}

		return dataProviderInstancesJSONArray;
	}

	private static final long serialVersionUID = 1L;

	@Reference
	private DDMDataProviderInstanceService _ddmDataProviderInstanceService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Portal _portal;

}