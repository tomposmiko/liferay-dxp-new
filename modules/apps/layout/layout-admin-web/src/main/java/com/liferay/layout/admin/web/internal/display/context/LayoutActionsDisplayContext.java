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

package com.liferay.layout.admin.web.internal.display.context;

import com.liferay.exportimport.kernel.staging.StagingUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.security.PermissionsURLTag;

import java.util.List;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class LayoutActionsDisplayContext {

	public LayoutActionsDisplayContext(HttpServletRequest httpServletRequest) {
		_httpServletRequest = httpServletRequest;

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public List<DropdownItem> getDropdownItems() {
		Layout layout = _getLayout();

		return DropdownItemListBuilder.addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					DropdownItemListBuilder.add(
						() -> _isShowConfigureAction(layout),
						dropdownItem -> {
							dropdownItem.setHref(
								_getConfigureLayoutURL(layout));
							dropdownItem.setIcon("cog");
							dropdownItem.setLabel(
								LanguageUtil.get(
									_httpServletRequest, "configure"));
						}
					).add(
						() -> _isShowPermissionsAction(layout),
						dropdownItem -> {
							dropdownItem.putData("action", "permissionLayout");
							dropdownItem.putData(
								"permissionLayoutURL",
								_getPermissionsURL(layout));
							dropdownItem.setIcon("password-policies");
							dropdownItem.setLabel(
								LanguageUtil.get(
									_httpServletRequest, "permissions"));
						}
					).build());
				dropdownGroupItem.setSeparator(true);
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					DropdownItemListBuilder.add(
						() -> _isShowDeleteAction(layout),
						dropdownItem -> {
							dropdownItem.putData("action", "deleteLayout");
							dropdownItem.putData(
								"deleteLayoutURL", _getDeleteLayoutURL(layout));

							String messageKey =
								"are-you-sure-you-want-to-delete-the-page-x.-" +
									"it-will-be-removed-immediately";

							if (layout.hasChildren() &&
								layout.hasScopeGroup()) {

								messageKey = StringBundler.concat(
									"are-you-sure-you-want-to-delete-the-page-",
									"x.-this-page-serves-as-a-scope-for-",
									"content-and-also-contains-child-pages");
							}
							else if (layout.hasChildren()) {
								messageKey = StringBundler.concat(
									"are-you-sure-you-want-to-delete-the-page-",
									"x.-this-page-contains-child-pages-that-",
									"will-also-be-removed");
							}
							else if (layout.hasScopeGroup()) {
								messageKey = StringBundler.concat(
									"are-you-sure-you-want-to-delete-the-page-",
									"x.-this-page-serves-as-a-scope-for-",
									"content");
							}

							dropdownItem.putData(
								"message",
								LanguageUtil.format(
									_httpServletRequest, messageKey,
									HtmlUtil.escape(
										layout.getName(
											_themeDisplay.getLocale()))));

							dropdownItem.setIcon("trash");
							dropdownItem.setLabel(
								LanguageUtil.get(
									_httpServletRequest, "delete"));
						}
					).build());
				dropdownGroupItem.setSeparator(true);
			}
		).build();
	}

	private String _getConfigureLayoutURL(Layout layout) {
		String currentURL = PortalUtil.getCurrentURL(_httpServletRequest);

		return PortletURLBuilder.create(
			PortalUtil.getControlPanelPortletURL(
				_httpServletRequest, LayoutAdminPortletKeys.GROUP_PAGES,
				PortletRequest.RENDER_PHASE)
		).setMVCRenderCommandName(
			"/layout_admin/edit_layout"
		).setRedirect(
			currentURL
		).setBackURL(
			currentURL
		).setParameter(
			"groupId", layout.getGroupId()
		).setParameter(
			"privateLayout", layout.isPrivateLayout()
		).setParameter(
			"selPlid", layout.getPlid()
		).buildPortletURL(
		).toString();
	}

	private String _getDeleteLayoutURL(Layout layout) {
		return PortletURLBuilder.create(
			PortalUtil.getControlPanelPortletURL(
				_httpServletRequest, LayoutAdminPortletKeys.GROUP_PAGES,
				PortletRequest.ACTION_PHASE)
		).setActionName(
			"/layout_admin/delete_layout"
		).setRedirect(
			PortalUtil.getControlPanelPortletURL(
				_httpServletRequest, LayoutAdminPortletKeys.GROUP_PAGES,
				PortletRequest.RENDER_PHASE)
		).setParameter(
			"selPlid", layout.getPlid()
		).buildString();
	}

	private Layout _getLayout() {
		Layout layout = _themeDisplay.getLayout();

		if (layout.isDraftLayout()) {
			layout = LayoutLocalServiceUtil.fetchLayout(layout.getClassPK());
		}

		return layout;
	}

	private String _getPermissionsURL(Layout layout) throws Exception {
		return PermissionsURLTag.doTag(
			StringPool.BLANK, Layout.class.getName(),
			HtmlUtil.escape(layout.getName(_themeDisplay.getLocale())), null,
			String.valueOf(layout.getPlid()),
			LiferayWindowState.POP_UP.toString(), null,
			_themeDisplay.getRequest());
	}

	private boolean _isShowConfigureAction(Layout layout)
		throws PortalException {

		return LayoutPermissionUtil.containsLayoutUpdatePermission(
			_themeDisplay.getPermissionChecker(), layout);
	}

	private boolean _isShowDeleteAction(Layout layout) throws PortalException {
		if (StagingUtil.isIncomplete(layout) ||
			!LayoutPermissionUtil.contains(
				_themeDisplay.getPermissionChecker(), layout,
				ActionKeys.DELETE)) {

			return false;
		}

		Group group = layout.getGroup();

		int layoutsCount = LayoutLocalServiceUtil.getLayoutsCount(
			group, false, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

		if (group.isGuest() && !layout.isPrivateLayout() &&
			layout.isRootLayout() && (layoutsCount == 1)) {

			return false;
		}

		return true;
	}

	private boolean _isShowPermissionsAction(Layout layout)
		throws PortalException {

		if (StagingUtil.isIncomplete(layout)) {
			return false;
		}

		Group group = layout.getGroup();

		if (group.isLayoutPrototype()) {
			return false;
		}

		return LayoutPermissionUtil.contains(
			_themeDisplay.getPermissionChecker(), layout,
			ActionKeys.PERMISSIONS);
	}

	private final HttpServletRequest _httpServletRequest;
	private final ThemeDisplay _themeDisplay;

}