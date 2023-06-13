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

package com.liferay.layout.internal.util;

import com.liferay.exportimport.kernel.staging.LayoutStagingUtil;
import com.liferay.exportimport.kernel.staging.Staging;
import com.liferay.layout.internal.action.provider.LayoutActionProvider;
import com.liferay.layout.security.permission.resource.LayoutContentModelResourcePermission;
import com.liferay.layout.util.LayoutsTree;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutBranch;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutRevision;
import com.liferay.portal.kernel.model.impl.VirtualLayout;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutService;
import com.liferay.portal.kernel.service.permission.LayoutPermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.SessionClicks;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalService;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Eduardo Lundgren
 * @author Bruno Basto
 * @author Marcellus Tavares
 * @author Zsolt Szabó
 * @author Tibor Lipusz
 */
@Component(service = LayoutsTree.class)
public class LayoutsTreeImpl implements LayoutsTree {

	@Override
	public JSONArray getLayoutsJSONArray(
			long[] expandedLayoutIds, long groupId,
			HttpServletRequest httpServletRequest, boolean includeActions,
			boolean incomplete, boolean loadMore, long parentLayoutId,
			boolean privateLayout, String treeId)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return _getLayoutsJSONArray(
			false, expandedLayoutIds, groupId, httpServletRequest,
			includeActions, incomplete, loadMore, parentLayoutId, privateLayout,
			themeDisplay, treeId);
	}

	private Layout _fetchCurrentLayout(HttpServletRequest httpServletRequest) {
		long selPlid = ParamUtil.getLong(httpServletRequest, "selPlid");

		if (selPlid > 0) {
			return _layoutLocalService.fetchLayout(selPlid);
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		if (!layout.isTypeControlPanel()) {
			return layout;
		}

		return null;
	}

	private List<Layout> _getAncestorLayouts(
			HttpServletRequest httpServletRequest)
		throws Exception {

		Layout layout = _fetchCurrentLayout(httpServletRequest);

		if (layout == null) {
			return Collections.emptyList();
		}

		List<Layout> ancestorLayouts = _layoutService.getAncestorLayouts(
			layout.getPlid());

		ancestorLayouts.add(layout);

		return ancestorLayouts;
	}

	private Layout _getDraftLayout(Layout layout) {
		if (!layout.isTypeContent()) {
			return null;
		}

		Layout draftLayout = layout.fetchDraftLayout();

		if (draftLayout == null) {
			return null;
		}

		if (draftLayout.isDraft() || !layout.isPublished()) {
			return draftLayout;
		}

		return null;
	}

	private JSONArray _getLayoutsJSONArray(
			boolean childLayout, long[] expandedLayoutIds, long groupId,
			HttpServletRequest httpServletRequest, boolean includeActions,
			boolean incomplete, boolean loadMore, long parentLayoutId,
			boolean privateLayout, ThemeDisplay themeDisplay, String treeId)
		throws Exception {

		int count = _layoutService.getLayoutsCount(
			groupId, privateLayout, parentLayoutId);

		if (count <= 0) {
			return _jsonFactory.createJSONArray();
		}

		JSONArray layoutsJSONArray = _jsonFactory.createJSONArray();

		List<Layout> ancestorLayouts = _getAncestorLayouts(httpServletRequest);

		List<Layout> layouts = _getPaginatedLayouts(
			httpServletRequest, groupId, privateLayout, parentLayoutId,
			loadMore, incomplete, treeId, childLayout, count,
			_layoutLocalService.getLayoutsCount(
				_groupLocalService.getGroup(groupId), privateLayout,
				parentLayoutId));

		Layout afterDeleteSelectedLayout = null;
		Layout secondLayout = null;

		int index = 0;

		for (Layout layout : layouts) {
			if (index == 1) {
				secondLayout = layout;

				break;
			}

			index++;
		}

		for (Layout layout : layouts) {
			int childLayoutsCount = 0;
			JSONArray childLayoutsJSONArray = null;

			if (_isExpandableLayout(
					ancestorLayouts, expandedLayoutIds, layout)) {

				if (layout instanceof VirtualLayout) {
					VirtualLayout virtualLayout = (VirtualLayout)layout;

					childLayoutsJSONArray = _getLayoutsJSONArray(
						true, expandedLayoutIds,
						virtualLayout.getSourceGroupId(), httpServletRequest,
						includeActions, incomplete, loadMore,
						virtualLayout.getLayoutId(),
						virtualLayout.isPrivateLayout(), themeDisplay, treeId);
				}
				else {
					childLayoutsJSONArray = _getLayoutsJSONArray(
						true, expandedLayoutIds, groupId, httpServletRequest,
						includeActions, incomplete, loadMore,
						layout.getLayoutId(), layout.isPrivateLayout(),
						themeDisplay, treeId);
				}

				childLayoutsCount = childLayoutsJSONArray.length();
			}
			else {
				childLayoutsCount = _layoutService.getLayoutsCount(
					groupId, privateLayout, layout.getLayoutId());

				childLayoutsJSONArray = _jsonFactory.createJSONArray();
			}

			if (includeActions) {
				if ((afterDeleteSelectedLayout == null) &&
					(layout.getParentLayoutId() !=
						LayoutConstants.DEFAULT_PARENT_LAYOUT_ID)) {

					afterDeleteSelectedLayout = _layoutLocalService.fetchLayout(
						layout.getParentPlid());
				}

				if (afterDeleteSelectedLayout == null) {
					afterDeleteSelectedLayout = secondLayout;
				}
			}

			layoutsJSONArray.put(
				_toJSONObject(
					afterDeleteSelectedLayout, childLayoutsCount,
					childLayoutsJSONArray, httpServletRequest, includeActions,
					layout, themeDisplay));

			if (includeActions) {
				afterDeleteSelectedLayout = layout;
			}
		}

		return layoutsJSONArray;
	}

	private int _getLoadedLayoutsCount(
			HttpSession httpSession, long groupId, boolean privateLayout,
			long layoutId, String treeId)
		throws Exception {

		String key = StringBundler.concat(
			treeId, StringPool.COLON, groupId, StringPool.COLON, privateLayout,
			":Pagination");

		String paginationJSON = SessionClicks.get(
			httpSession, key, _jsonFactory.getNullJSON());

		JSONObject paginationJSONObject = _jsonFactory.createJSONObject(
			paginationJSON);

		return paginationJSONObject.getInt(String.valueOf(layoutId), 0);
	}

	private List<Layout> _getPaginatedLayouts(
			HttpServletRequest httpServletRequest, long groupId,
			boolean privateLayout, long parentLayoutId, boolean loadMore,
			boolean incomplete, String treeId, boolean childLayout, int count,
			int totalCount)
		throws Exception {

		if (!_isPaginationEnabled(httpServletRequest)) {
			return _layoutService.getLayouts(
				groupId, privateLayout, parentLayoutId, incomplete,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}

		int loadedLayoutsCount = _getLoadedLayoutsCount(
			httpServletRequest.getSession(), groupId, privateLayout,
			parentLayoutId, treeId);

		int start = ParamUtil.getInteger(httpServletRequest, "start");

		start = Math.max(0, Math.min(start, count));

		int end = ParamUtil.getInteger(
			httpServletRequest, "end",
			start + PropsValues.LAYOUT_MANAGE_PAGES_INITIAL_CHILDREN);

		if (loadedLayoutsCount > end) {
			end = loadedLayoutsCount;
		}

		if (loadMore) {
			String key = StringBundler.concat(
				treeId, StringPool.COLON, groupId, StringPool.COLON,
				privateLayout, ":Pagination");

			String paginationJSON = SessionClicks.get(
				httpServletRequest.getSession(), key,
				_jsonFactory.getNullJSON());

			JSONObject paginationJSONObject = _jsonFactory.createJSONObject(
				paginationJSON);

			paginationJSONObject.put(String.valueOf(parentLayoutId), end);

			SessionClicks.put(
				httpServletRequest.getSession(), key,
				paginationJSONObject.toString());
		}

		end = Math.max(start, Math.min(end, count));

		if (childLayout &&
			(count > PropsValues.LAYOUT_MANAGE_PAGES_INITIAL_CHILDREN) &&
			(start == PropsValues.LAYOUT_MANAGE_PAGES_INITIAL_CHILDREN)) {

			start = end;
		}

		if (count != totalCount) {
			List<Layout> layouts = _layoutService.getLayouts(
				groupId, privateLayout, parentLayoutId, incomplete,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			return layouts.subList(start, end);
		}

		return _layoutService.getLayouts(
			groupId, privateLayout, parentLayoutId, incomplete, start, end);
	}

	private boolean _isExpandableLayout(
		List<Layout> ancestorLayouts, long[] expandedLayoutIds, Layout layout) {

		if (ancestorLayouts.contains(layout) ||
			ArrayUtil.contains(expandedLayoutIds, layout.getLayoutId())) {

			return true;
		}

		return false;
	}

	private boolean _isPaginationEnabled(
		HttpServletRequest httpServletRequest) {

		boolean paginate = ParamUtil.getBoolean(
			httpServletRequest, "paginate", true);

		if (paginate &&
			(PropsValues.LAYOUT_MANAGE_PAGES_INITIAL_CHILDREN > -1)) {

			return true;
		}

		return false;
	}

	private JSONObject _toJSONObject(
			Layout afterDeleteSelectedLayout, long childLayoutsCount,
			JSONArray childLayoutsJSONArray,
			HttpServletRequest httpServletRequest, boolean includeActions,
			Layout layout, ThemeDisplay themeDisplay)
		throws Exception {

		Layout draftLayout = _getDraftLayout(layout);

		boolean hasUpdatePermission =
			_layoutPermission.containsLayoutUpdatePermission(
				themeDisplay.getPermissionChecker(), layout);

		JSONObject jsonObject = JSONUtil.put(
			"actions",
			() -> {
				if (includeActions) {
					LayoutActionProvider layoutActionProvider =
						new LayoutActionProvider(
							httpServletRequest, _language,
							_siteNavigationMenuLocalService);

					return layoutActionProvider.getActionsJSONArray(
						layout, afterDeleteSelectedLayout);
				}

				return null;
			}
		).put(
			"children",
			() -> {
				if (childLayoutsJSONArray.length() > 0) {
					return childLayoutsJSONArray;
				}

				return null;
			}
		).put(
			"groupId",
			() -> {
				if (layout instanceof VirtualLayout) {
					VirtualLayout virtualLayout = (VirtualLayout)layout;

					return virtualLayout.getSourceGroupId();
				}

				return layout.getGroupId();
			}
		).put(
			"hasChildren", layout.hasChildren()
		).put(
			"icon", layout.getIcon()
		).put(
			"id", layout.getPlid()
		).put(
			"layoutId", layout.getLayoutId()
		).put(
			"name",
			() -> {
				if ((draftLayout != null) &&
					(hasUpdatePermission || !layout.isPublished() ||
					 _layoutContentModelResourcePermission.contains(
						 themeDisplay.getPermissionChecker(), layout.getPlid(),
						 ActionKeys.UPDATE))) {

					return layout.getName(themeDisplay.getLocale()) +
						StringPool.STAR;
				}

				return layout.getName(themeDisplay.getLocale());
			}
		).put(
			"paginated",
			() -> {
				if (childLayoutsCount != childLayoutsJSONArray.length()) {
					return true;
				}

				return null;
			}
		).put(
			"plid", layout.getPlid()
		).put(
			"priority", layout.getPriority()
		).put(
			"privateLayout", layout.isPrivateLayout()
		).put(
			"regularURL",
			() -> {
				if (hasUpdatePermission || layout.isPublished()) {
					return layout.getRegularURL(httpServletRequest);
				}

				return StringPool.BLANK;
			}
		).put(
			"target",
			GetterUtil.getString(
				HtmlUtil.escape(layout.getTypeSettingsProperty("target")),
				"_self")
		).put(
			"type", layout.getType()
		);

		LayoutRevision layoutRevision = LayoutStagingUtil.getLayoutRevision(
			layout);

		if (layoutRevision != null) {
			if (_staging.isIncomplete(
					layout, layoutRevision.getLayoutSetBranchId())) {

				jsonObject.put("incomplete", true);
			}

			LayoutBranch layoutBranch = layoutRevision.getLayoutBranch();

			if (!layoutBranch.isMaster()) {
				jsonObject.put("layoutBranchName", layoutBranch.getName());
			}

			if (layoutRevision.isHead()) {
				jsonObject.put("layoutRevisionHead", true);
			}

			jsonObject.put(
				"layoutRevisionId", layoutRevision.getLayoutRevisionId());
		}

		return jsonObject;
	}

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private LayoutContentModelResourcePermission
		_layoutContentModelResourcePermission;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPermission _layoutPermission;

	@Reference
	private LayoutService _layoutService;

	@Reference
	private SiteNavigationMenuLocalService _siteNavigationMenuLocalService;

	@Reference
	private Staging _staging;

}