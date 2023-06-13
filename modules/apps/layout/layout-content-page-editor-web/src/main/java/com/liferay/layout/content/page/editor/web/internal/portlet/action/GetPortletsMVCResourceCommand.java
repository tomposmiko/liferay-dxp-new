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

import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletApp;
import com.liferay.portal.kernel.model.PortletCategory;
import com.liferay.portal.kernel.model.PortletItem;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletConfigFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactory;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.PortletItemLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.util.comparator.PortletCategoryComparator;
import com.liferay.portal.kernel.util.comparator.PortletTitleComparator;
import com.liferay.portal.util.PortletCategoryUtil;
import com.liferay.portal.util.WebAppPool;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.portlet.PortletConfig;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pablo Molina
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
		"mvc.command.name=/layout_content_page_editor/get_portlets"
	},
	service = MVCResourceCommand.class
)
public class GetPortletsMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException {

		HttpServletRequest httpServletRequest =
			_portal.getOriginalServletRequest(
				_portal.getHttpServletRequest(resourceRequest));

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		try {
			JSONPortletResponseUtil.writeJSON(
				resourceRequest, resourceResponse,
				_getPortletsJSONArray(httpServletRequest, themeDisplay));
		}
		catch (Exception exception) {
			_log.error("Unable to get portlets", exception);

			JSONPortletResponseUtil.writeJSON(
				resourceRequest, resourceResponse,
				JSONUtil.put(
					"error",
					_language.get(
						themeDisplay.getRequest(),
						"an-unexpected-error-occurred")));
		}
	}

	private Set<String> _getHighlightedPortletIds(
		HttpServletRequest httpServletRequest,
		PortletCategory highlightedPortletCategory) {

		Set<String> highlightedPortletIds = new TreeSet<>(
			highlightedPortletCategory.getPortletIds());

		PortalPreferences portalPreferences =
			_portletPreferencesFactory.getPortalPreferences(httpServletRequest);

		highlightedPortletIds.removeAll(
			SetUtil.fromArray(
				portalPreferences.getValues(
					ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
					"nonhighlightedPortletIds", new String[0])));
		highlightedPortletIds.addAll(
			SetUtil.fromArray(
				portalPreferences.getValues(
					ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
					"highlightedPortletIds", new String[0])));

		return highlightedPortletIds;
	}

	private JSONArray _getPortletCategoriesJSONArray(
			Set<String> highlightedPortletIds,
			HttpServletRequest httpServletRequest,
			PortletCategory portletCategory, ThemeDisplay themeDisplay)
		throws Exception {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		List<PortletCategory> portletCategories = ListUtil.fromCollection(
			portletCategory.getCategories());

		portletCategories = ListUtil.sort(
			portletCategories,
			new PortletCategoryComparator(themeDisplay.getLocale()));

		for (PortletCategory currentPortletCategory : portletCategories) {
			if (currentPortletCategory.isHidden()) {
				continue;
			}

			jsonArray.put(
				JSONUtil.put(
					"categories",
					_getPortletCategoriesJSONArray(
						highlightedPortletIds, httpServletRequest,
						currentPortletCategory, themeDisplay)
				).put(
					"path",
					StringUtil.replace(
						currentPortletCategory.getPath(),
						new String[] {"/", "."}, new String[] {"-", "-"})
				).put(
					"portlets",
					_getPortletsJSONArray(
						highlightedPortletIds, httpServletRequest,
						currentPortletCategory, themeDisplay)
				).put(
					"title",
					_getPortletCategoryTitle(
						httpServletRequest, currentPortletCategory,
						themeDisplay)
				));
		}

		return jsonArray;
	}

	private String _getPortletCategoryTitle(
		HttpServletRequest httpServletRequest, PortletCategory portletCategory,
		ThemeDisplay themeDisplay) {

		for (String portletId :
				PortletCategoryUtil.getFirstChildPortletIds(portletCategory)) {

			Portlet portlet = _portletLocalService.getPortletById(
				themeDisplay.getCompanyId(), portletId);

			if (portlet == null) {
				continue;
			}

			PortletApp portletApp = portlet.getPortletApp();

			if (!portletApp.isWARFile()) {
				continue;
			}

			PortletConfig portletConfig = PortletConfigFactoryUtil.create(
				portlet, httpServletRequest.getServletContext());

			ResourceBundle portletResourceBundle =
				portletConfig.getResourceBundle(themeDisplay.getLocale());

			String title = ResourceBundleUtil.getString(
				portletResourceBundle, portletCategory.getName());

			if (Validator.isNotNull(title)) {
				return title;
			}
		}

		return _language.get(httpServletRequest, portletCategory.getName());
	}

	private JSONArray _getPortletItemsJSONArray(
			Portlet portlet, ThemeDisplay themeDisplay)
		throws Exception {

		List<PortletItem> portletItems =
			_portletItemLocalService.getPortletItems(
				themeDisplay.getScopeGroupId(), portlet.getPortletId(),
				PortletPreferences.class.getName());

		if (ListUtil.isEmpty(portletItems)) {
			return JSONFactoryUtil.createJSONArray();
		}

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (PortletItem portletItem : portletItems) {
			jsonArray.put(
				JSONUtil.put(
					"instanceable", portlet.isInstanceable()
				).put(
					"portletId", portlet.getPortletId()
				).put(
					"portletItemId", portletItem.getPortletItemId()
				).put(
					"title", HtmlUtil.escape(portletItem.getName())
				));
		}

		return jsonArray;
	}

	private List<Portlet> _getPortlets(
		Set<String> highlightedPortletIds, PortletCategory portletCategory,
		ThemeDisplay themeDisplay) {

		List<Portlet> portlets = new ArrayList<>();

		Set<String> portletIds = portletCategory.getPortletIds();

		if (GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-158737")) &&
			Objects.equals(portletCategory.getName(), "category.highlighted")) {

			portletIds = highlightedPortletIds;
		}

		for (String portletId : portletIds) {
			Portlet portlet = _portletLocalService.getPortletById(
				themeDisplay.getCompanyId(), portletId);

			if ((portlet == null) ||
				ArrayUtil.contains(
					_UNSUPPORTED_PORTLETS_NAMES, portlet.getPortletName())) {

				continue;
			}

			try {
				if (PortletPermissionUtil.contains(
						themeDisplay.getPermissionChecker(),
						themeDisplay.getLayout(), portlet,
						ActionKeys.ADD_TO_PAGE)) {

					portlets.add(portlet);
				}
			}
			catch (PortalException portalException) {
				_log.error(
					"Unable to check portlet permissions for " +
						portlet.getPortletId(),
					portalException);
			}
		}

		return portlets;
	}

	private JSONArray _getPortletsJSONArray(
			HttpServletRequest httpServletRequest, ThemeDisplay themeDisplay)
		throws Exception {

		PortletCategory rootPortletCategory = (PortletCategory)WebAppPool.get(
			themeDisplay.getCompanyId(), WebKeys.PORTLET_CATEGORY);

		PortletCategory highlightedPortletCategory =
			rootPortletCategory.getCategory("category.highlighted");

		PortletCategory portletCategory =
			PortletCategoryUtil.getRelevantPortletCategory(
				themeDisplay.getPermissionChecker(),
				themeDisplay.getCompanyId(), themeDisplay.getLayout(),
				rootPortletCategory, themeDisplay.getLayoutTypePortlet());

		return _getPortletCategoriesJSONArray(
			_getHighlightedPortletIds(
				httpServletRequest, highlightedPortletCategory),
			httpServletRequest, portletCategory, themeDisplay);
	}

	private JSONArray _getPortletsJSONArray(
			Set<String> highlightedPortletIds,
			HttpServletRequest httpServletRequest,
			PortletCategory portletCategory, ThemeDisplay themeDisplay)
		throws Exception {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		HttpSession httpSession = httpServletRequest.getSession();

		ServletContext servletContext = httpSession.getServletContext();

		List<Portlet> portlets = _getPortlets(
			highlightedPortletIds, portletCategory, themeDisplay);

		portlets = ListUtil.sort(
			portlets,
			new PortletTitleComparator(
				servletContext, themeDisplay.getLocale()));

		for (Portlet portlet : portlets) {
			jsonArray.put(
				JSONUtil.put(
					"highlighted",
					highlightedPortletIds.contains(portlet.getPortletId())
				).put(
					"instanceable", portlet.isInstanceable()
				).put(
					"portletId", portlet.getPortletId()
				).put(
					"portletItems",
					_getPortletItemsJSONArray(portlet, themeDisplay)
				).put(
					"title",
					_portal.getPortletTitle(
						portlet, servletContext, themeDisplay.getLocale())
				));
		}

		return jsonArray;
	}

	private static final String[] _UNSUPPORTED_PORTLETS_NAMES = {
		"com_liferay_nested_portlets_web_portlet_NestedPortletsPortlet"
	};

	private static final Log _log = LogFactoryUtil.getLog(
		GetPortletsMVCResourceCommand.class);

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference
	private PortletItemLocalService _portletItemLocalService;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private PortletPreferencesFactory _portletPreferencesFactory;

	@Reference
	private PortletPreferencesLocalService _portletPreferencesLocalService;

}