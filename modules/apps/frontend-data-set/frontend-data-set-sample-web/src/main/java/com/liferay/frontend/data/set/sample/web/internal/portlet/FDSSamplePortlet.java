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

package com.liferay.frontend.data.set.sample.web.internal.portlet;

import com.liferay.frontend.data.set.sample.web.internal.constants.FDSSamplePortletKeys;
import com.liferay.frontend.data.set.sample.web.internal.constants.FDSSampleWebKeys;
import com.liferay.frontend.data.set.sample.web.internal.display.context.FDSSampleDisplayContext;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.field.util.ObjectFieldUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.util.LocalizedMapUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.Serializable;

import java.util.Arrays;
import java.util.Calendar;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Javier de Arcos
 */
@Component(
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Frontend Data Set Sample",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/META-INF/resources/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + FDSSamplePortletKeys.FDS_SAMPLE,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.version=3.0"
	},
	service = Portlet.class
)
public class FDSSamplePortlet extends MVCPortlet {

	@Override
	public void doDispatch(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			_generate(themeDisplay.getCompanyId());
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		renderRequest.setAttribute(
			FDSSampleWebKeys.FDS_SAMPLE_DISPLAY_CONTEXT,
			new FDSSampleDisplayContext(
				_portal.getHttpServletRequest(renderRequest)));

		super.doDispatch(renderRequest, renderResponse);
	}

	private synchronized void _generate(long companyId) throws Exception {
		User user = _userLocalService.fetchUserByEmailAddress(
			companyId, "test@liferay.com");

		if (user == null) {
			return;
		}

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.fetchObjectDefinition(
				companyId, "C_FDSSample");

		if (objectDefinition != null) {
			return;
		}

		objectDefinition =
			_objectDefinitionLocalService.addCustomObjectDefinition(
				user.getUserId(), false,
				LocalizedMapUtil.getLocalizedMap("Frontend Data Set Sample"),
				"FDSSample", "100", null,
				LocalizedMapUtil.getLocalizedMap("Frontend Data Set Samples"),
				ObjectDefinitionConstants.SCOPE_COMPANY,
				ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT,
				Arrays.asList(
					ObjectFieldUtil.createObjectField(
						ObjectFieldConstants.BUSINESS_TYPE_TEXT,
						ObjectFieldConstants.DB_TYPE_STRING, true, false, null,
						"Title", "title", false),
					ObjectFieldUtil.createObjectField(
						ObjectFieldConstants.BUSINESS_TYPE_TEXT,
						ObjectFieldConstants.DB_TYPE_STRING, true, false, null,
						"Description", "description", false),
					ObjectFieldUtil.createObjectField(
						ObjectFieldConstants.BUSINESS_TYPE_DATE,
						ObjectFieldConstants.DB_TYPE_DATE, true, false, null,
						"Date", "date", false),
					ObjectFieldUtil.createObjectField(
						ObjectFieldConstants.BUSINESS_TYPE_TEXT,
						ObjectFieldConstants.DB_TYPE_STRING, true, false, null,
						"Color", "color", false),
					ObjectFieldUtil.createObjectField(
						ObjectFieldConstants.BUSINESS_TYPE_TEXT,
						ObjectFieldConstants.DB_TYPE_STRING, true, false, null,
						"Size", "size", false)));

		objectDefinition =
			_objectDefinitionLocalService.publishCustomObjectDefinition(
				user.getUserId(), objectDefinition.getObjectDefinitionId());

		String[] colors = {"Blue", "Green", "Red", "Yellow"};
		String[] sizes = {
			"Tiny", "Small", "Medium", "Large", "Huge", "Gargantuan"
		};

		Calendar calendar = Calendar.getInstance();

		calendar.set(2020, 0, 1, 1, 1, 1);
		calendar.set(Calendar.MILLISECOND, 0);

		for (int i = 1; i <= 100; i++) {
			_objectEntryLocalService.addObjectEntry(
				user.getUserId(), 0, objectDefinition.getObjectDefinitionId(),
				HashMapBuilder.<String, Serializable>put(
					"color", colors[i % 4]
				).put(
					"date", calendar.getTime()
				).put(
					"description", "This is a description for sample " + i + "."
				).put(
					"size", sizes[i % 6]
				).put(
					"title", "Sample" + i
				).build(),
				new ServiceContext());

			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DATE, 1);
			calendar.add(Calendar.HOUR, 1);
			calendar.add(Calendar.MINUTE, 1);
			calendar.add(Calendar.SECOND, 1);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FDSSamplePortlet.class);

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(&(original.bean=true)(bean.id=javax.servlet.ServletContext))"
	)
	private ServletContext _servletContext;

	@Reference
	private UserLocalService _userLocalService;

}