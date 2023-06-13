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

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.exportimport.kernel.staging.Staging;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.action.RenderPortletAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutRevision;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.AddPortletProvider;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.render.PortletRenderParts;
import com.liferay.portal.kernel.portlet.render.PortletRenderUtil;
import com.liferay.portal.kernel.service.LayoutRevisionLocalService;
import com.liferay.portal.kernel.service.LayoutService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.BufferCacheServletResponse;
import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstancePool;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.servlet.NamespaceServletRequest;
import com.liferay.portal.struts.Action;
import com.liferay.portal.util.LayoutClone;
import com.liferay.portal.util.LayoutCloneFactory;

import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "path=/portal/update_layout", service = StrutsAction.class
)
public class UpdateLayoutStrutsAction implements StrutsAction {

	@Override
	public String execute(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		long userId = themeDisplay.getUserId();

		Layout layout = themeDisplay.getLayout();
		LayoutTypePortlet layoutTypePortlet =
			themeDisplay.getLayoutTypePortlet();

		String cmd = ParamUtil.getString(httpServletRequest, Constants.CMD);

		String portletId = ParamUtil.getString(httpServletRequest, "p_p_id");

		boolean updateLayout = true;

		if (cmd.equals(Constants.ADD)) {
			if (portletId == null) {
				throw new IllegalArgumentException("Portlet ID is null");
			}

			String columnId = ParamUtil.getString(
				httpServletRequest, "p_p_col_id", null);
			int columnPos = ParamUtil.getInteger(
				httpServletRequest, "p_p_col_pos", -1);

			String originalPortletId = portletId;

			portletId = layoutTypePortlet.addPortletId(
				userId, portletId, columnId, columnPos);

			if (portletId == null) {
				throw new PortalException(
					StringBundler.concat(
						"Portlet ", originalPortletId,
						" cannot be added to layout ", layout.getPlid(),
						" by user ", userId));
			}

			storeAddContentPortletPreferences(
				httpServletRequest, layout, portletId, themeDisplay);

			if (layoutTypePortlet.isCustomizable() &&
				layoutTypePortlet.isCustomizedView() &&
				!layoutTypePortlet.isColumnDisabled(columnId)) {

				updateLayout = false;
			}
		}
		else if (cmd.equals(Constants.DELETE)) {
			if (layoutTypePortlet.hasPortletId(portletId)) {
				layoutTypePortlet.removePortletId(userId, portletId);

				if (layoutTypePortlet.isCustomizable() &&
					layoutTypePortlet.isCustomizedView()) {

					updateLayout = false;
				}
			}
		}
		else if (cmd.equals("minimize")) {
			boolean restore = ParamUtil.getBoolean(
				httpServletRequest, "p_p_restore");

			if (restore) {
				layoutTypePortlet.removeStateMinPortletId(portletId);
			}
			else {
				layoutTypePortlet.addStateMinPortletId(portletId);
			}

			updateLayout = false;
		}
		else if (cmd.equals("move")) {
			String columnId = ParamUtil.getString(
				httpServletRequest, "p_p_col_id");
			int columnPos = ParamUtil.getInteger(
				httpServletRequest, "p_p_col_pos");

			layoutTypePortlet.movePortletId(
				userId, portletId, columnId, columnPos);

			if (layoutTypePortlet.isCustomizable() &&
				layoutTypePortlet.isCustomizedView() &&
				!layoutTypePortlet.isColumnDisabled(columnId)) {

				updateLayout = false;
			}
		}
		else if (cmd.equals("redo_layout_revision")) {
			long layoutRevisionId = ParamUtil.getLong(
				httpServletRequest, "layoutRevisionId");
			long layoutSetBranchId = ParamUtil.getLong(
				httpServletRequest, "layoutSetBranchId");

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				httpServletRequest);

			_layoutRevisionLocalService.updateStatus(
				userId, layoutRevisionId, WorkflowConstants.STATUS_DRAFT,
				serviceContext);

			_staging.setRecentLayoutRevisionId(
				httpServletRequest, layoutSetBranchId, layout.getPlid(),
				layoutRevisionId);

			updateLayout = false;
		}
		else if (cmd.equals("select_layout_revision")) {
			long layoutRevisionId = ParamUtil.getLong(
				httpServletRequest, "layoutRevisionId");
			long layoutSetBranchId = ParamUtil.getLong(
				httpServletRequest, "layoutSetBranchId");

			_staging.setRecentLayoutRevisionId(
				httpServletRequest, layoutSetBranchId, layout.getPlid(),
				layoutRevisionId);

			updateLayout = false;
		}
		else if (cmd.equals("update_type_settings")) {
			UnicodeProperties layoutTypeSettingsUnicodeProperties =
				layout.getTypeSettingsProperties();

			UnicodeProperties formTypeSettingsUnicodeProperties =
				PropertiesParamUtil.getProperties(
					httpServletRequest, "TypeSettingsProperties--");

			layoutTypeSettingsUnicodeProperties.putAll(
				formTypeSettingsUnicodeProperties);
		}
		else if (cmd.equals("undo_layout_revision")) {
			long layoutRevisionId = ParamUtil.getLong(
				httpServletRequest, "layoutRevisionId");
			long layoutSetBranchId = ParamUtil.getLong(
				httpServletRequest, "layoutSetBranchId");

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				httpServletRequest);

			LayoutRevision layoutRevision =
				_layoutRevisionLocalService.updateStatus(
					userId, layoutRevisionId, WorkflowConstants.STATUS_INACTIVE,
					serviceContext);

			_staging.setRecentLayoutRevisionId(
				httpServletRequest, layoutSetBranchId, layout.getPlid(),
				layoutRevision.getParentLayoutRevisionId());

			updateLayout = false;
		}

		if (updateLayout) {

			// LEP-3648

			layoutTypePortlet.resetModes();
			layoutTypePortlet.resetStates();

			layout = _layoutService.updateLayout(
				layout.getGroupId(), layout.isPrivateLayout(),
				layout.getLayoutId(), layout.getTypeSettings());
		}
		else {
			LayoutClone layoutClone = LayoutCloneFactory.getInstance();

			if (layoutClone != null) {
				layoutClone.update(
					httpServletRequest, layout.getPlid(),
					layout.getTypeSettings());
			}
		}

		if (cmd.equals(Constants.ADD) && (portletId != null)) {
			addPortlet(httpServletRequest, httpServletResponse, portletId);
		}

		return StringPool.BLANK;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, AddPortletProvider.class, "model.class.name");
	}

	protected void addPortlet(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String portletId)
		throws Exception {

		// Run the render portlet action to add a portlet without refreshing.

		Action renderPortletAction = (Action)InstancePool.get(
			RenderPortletAction.class.getName());

		// Pass in the portlet id because the portlet id may be the instance id.
		// Namespace the request if necessary. See LEP-4644.

		Portlet portlet = _portletLocalService.getPortletById(
			_portal.getCompanyId(httpServletRequest), portletId);

		DynamicServletRequest dynamicRequest = null;

		if (portlet.isPrivateRequestAttributes()) {
			String portletNamespace = _portal.getPortletNamespace(
				portlet.getPortletId());

			dynamicRequest = new NamespaceServletRequest(
				httpServletRequest, portletNamespace, portletNamespace);
		}
		else {
			dynamicRequest = new DynamicServletRequest(httpServletRequest);
		}

		dynamicRequest.setParameter("p_p_id", portletId);

		String dataType = StringUtil.toLowerCase(
			ParamUtil.getString(httpServletRequest, "dataType"));

		if (dataType.equals("json")) {
			BufferCacheServletResponse bufferCacheServletResponse =
				new BufferCacheServletResponse(httpServletResponse);

			renderPortletAction.execute(
				null, dynamicRequest, bufferCacheServletResponse);

			String portletHTML = bufferCacheServletResponse.getString();

			portletHTML = portletHTML.trim();

			PortletRenderParts portletRenderParts =
				PortletRenderUtil.getPortletRenderParts(
					httpServletRequest, portletHTML, portlet);

			JSONObject jsonObject = JSONUtil.put(
				"footerCssPaths", portletRenderParts.getFooterCssPaths()
			).put(
				"footerJavaScriptPaths",
				portletRenderParts.getFooterJavaScriptPaths()
			).put(
				"headerCssPaths", portletRenderParts.getHeaderCssPaths()
			).put(
				"headerJavaScriptPaths",
				portletRenderParts.getHeaderJavaScriptPaths()
			).put(
				"portletHTML", portletRenderParts.getPortletHTML()
			).put(
				"refresh", portletRenderParts.isRefresh()
			);

			httpServletResponse.setContentType(ContentTypes.APPLICATION_JSON);

			ServletResponseUtil.write(
				httpServletResponse, jsonObject.toString());
		}
		else {
			renderPortletAction.execute(
				null, dynamicRequest, httpServletResponse);
		}
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	protected void storeAddContentPortletPreferences(
			HttpServletRequest httpServletRequest, Layout layout,
			String portletId, ThemeDisplay themeDisplay)
		throws Exception {

		// We need to get the portlet setup before doing anything else to ensure
		// that it is created in the database

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				layout, portletId);

		String[] portletData = StringUtil.split(
			ParamUtil.getString(httpServletRequest, "portletData"));

		if (portletData.length == 0) {
			return;
		}

		long classPK = GetterUtil.getLong(portletData[0]);

		String className = GetterUtil.getString(portletData[1]);

		if ((classPK <= 0) || Validator.isNull(className)) {
			return;
		}

		AddPortletProvider addPortletProvider = _serviceTrackerMap.getService(
			className);

		if (addPortletProvider == null) {
			addPortletProvider = _serviceTrackerMap.getService(
				AssetEntry.class.getName());
		}

		if (addPortletProvider != null) {
			addPortletProvider.updatePortletPreferences(
				portletSetup, portletId, className, classPK, themeDisplay);
		}

		portletSetup.store();
	}

	@Reference
	private LayoutRevisionLocalService _layoutRevisionLocalService;

	@Reference
	private LayoutService _layoutService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	private ServiceTrackerMap<String, AddPortletProvider> _serviceTrackerMap;

	@Reference
	private Staging _staging;

}