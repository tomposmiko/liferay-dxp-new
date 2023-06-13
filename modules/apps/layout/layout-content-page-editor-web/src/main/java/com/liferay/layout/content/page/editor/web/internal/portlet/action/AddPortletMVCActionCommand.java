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

package com.liferay.layout.content.page.editor.web.internal.portlet.action;

import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.FragmentEntryProcessorRegistry;
import com.liferay.fragment.renderer.DefaultFragmentRendererContext;
import com.liferay.fragment.renderer.FragmentPortletRenderer;
import com.liferay.fragment.renderer.FragmentRendererController;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.fragment.util.FragmentPortletSetupUtil;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.PortletIdException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
		"mvc.command.name=/content_layout/add_portlet"
	},
	service = MVCActionCommand.class
)
public class AddPortletMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		long classNameId = ParamUtil.getLong(actionRequest, "classNameId");

		long classPK = ParamUtil.getLong(actionRequest, "classPK");

		Layout layout = _layoutLocalService.getLayout(classPK);

		String portletId = PortletIdCodec.decodePortletName(
			ParamUtil.getString(actionRequest, "portletId"));

		PortletPermissionUtil.check(
			themeDisplay.getPermissionChecker(), layout.getGroupId(), layout,
			portletId, ActionKeys.ADD_TO_PAGE);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			actionRequest);

		try {
			String instanceId = StringPool.BLANK;

			Portlet portlet = _portletLocalService.getPortletById(portletId);

			if (portlet.isInstanceable()) {
				instanceId = PortletIdCodec.generateInstanceId();
			}
			else if (_portletPreferencesLocalService.getPortletPreferencesCount(
						PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layout.getPlid(),
						portletId) > 0) {

				throw new PortletIdException(
					"Cannot add non-instanceable portlet more than once");
			}

			String html = _getPortletFragmentEntryLinkHTML(
				serviceContext.getRequest(),
				_portal.getHttpServletResponse(actionResponse), portletId,
				instanceId);

			JSONObject editableValueJSONObject =
				_fragmentEntryProcessorRegistry.
					getDefaultEditableValuesJSONObject(html);

			editableValueJSONObject.put(
				"instanceId", instanceId
			).put(
				"portletId", portletId
			);

			FragmentEntryLink fragmentEntryLink =
				_fragmentEntryLinkLocalService.addFragmentEntryLink(
					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(), 0, 0, classNameId,
					classPK, StringPool.BLANK, html, StringPool.BLANK,
					editableValueJSONObject.toString(), StringPool.BLANK, 0,
					null, serviceContext);

			DefaultFragmentRendererContext defaultFragmentRendererContext =
				new DefaultFragmentRendererContext(fragmentEntryLink);

			defaultFragmentRendererContext.setMode(
				FragmentEntryLinkConstants.EDIT);

			jsonObject.put(
				"content",
				_fragmentRendererController.render(
					defaultFragmentRendererContext,
					_portal.getHttpServletRequest(actionRequest),
					_portal.getHttpServletResponse(actionResponse))
			).put(
				"editableValues", fragmentEntryLink.getEditableValues()
			);

			if (SessionErrors.contains(
					actionRequest, "fragmentEntryInvalidContent")) {

				jsonObject.put("error", true);
			}

			jsonObject.put(
				"fragmentEntryLinkId",
				fragmentEntryLink.getFragmentEntryLinkId()
			).put(
				"name",
				_portal.getPortletTitle(portletId, themeDisplay.getLocale())
			);

			SessionMessages.add(actionRequest, "fragmentEntryLinkAdded");
		}
		catch (PortalException pe) {
			_log.error(pe, pe);

			String errorMessage = "an-unexpected-error-occurred";

			jsonObject.put(
				"error",
				LanguageUtil.get(themeDisplay.getRequest(), errorMessage));
		}

		hideDefaultSuccessMessage(actionRequest);

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private String _getPortletFragmentEntryLinkHTML(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String portletId,
			String instanceId)
		throws Exception {

		PortletPreferences portletPreferences =
			PortletPreferencesFactoryUtil.getPortletPreferences(
				httpServletRequest, portletId);

		FragmentPortletSetupUtil.setPortletBareboneCSSClassName(
			portletPreferences);

		return _fragmentPortletRenderer.renderPortlet(
			httpServletRequest, httpServletResponse, portletId, instanceId,
			PortletPreferencesFactoryUtil.toXML(portletPreferences));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AddPortletMVCActionCommand.class);

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private FragmentEntryProcessorRegistry _fragmentEntryProcessorRegistry;

	@Reference
	private FragmentPortletRenderer _fragmentPortletRenderer;

	@Reference
	private FragmentRendererController _fragmentRendererController;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private PortletPreferencesLocalService _portletPreferencesLocalService;

}