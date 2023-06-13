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

package com.liferay.commerce.product.definitions.web.internal.frontend.taglib.servlet.taglib;

import com.liferay.commerce.product.definitions.web.internal.display.context.CPDefinitionDisplayLayoutDisplayContext;
import com.liferay.commerce.product.model.CPDisplayLayout;
import com.liferay.commerce.product.portlet.action.ActionHelper;
import com.liferay.commerce.product.service.CPDefinitionService;
import com.liferay.commerce.product.service.CPDisplayLayoutService;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.product.servlet.taglib.ui.constants.CPDefinitionScreenNavigationConstants;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationCategory;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.item.selector.ItemSelector;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	property = {
		"screen.navigation.category.order:Integer=20",
		"screen.navigation.entry.order:Integer=10"
	},
	service = {ScreenNavigationCategory.class, ScreenNavigationEntry.class}
)
public class
	LayoutPageTemplateEntryCPDefinitionDisplayLayoutSelectionScreenNavigationCategory
		implements ScreenNavigationCategory,
				   ScreenNavigationEntry<CPDisplayLayout> {

	@Override
	public String getCategoryKey() {
		return CPDefinitionScreenNavigationConstants.
			CATEGORY_KEY_DISPLAY_PAGE_TEMPLATE;
	}

	@Override
	public String getEntryKey() {
		return CPDefinitionScreenNavigationConstants.
			CATEGORY_KEY_DISPLAY_PAGE_TEMPLATE;
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(
			locale,
			CPDefinitionScreenNavigationConstants.
				CATEGORY_KEY_DISPLAY_PAGE_TEMPLATE);
	}

	@Override
	public String getScreenNavigationKey() {
		return CPDefinitionScreenNavigationConstants.
			SCREEN_NAVIGATION_KEY_CP_DEFINITION_DISPLAY_LAYOUT_GENERAL;
	}

	@Override
	public boolean isVisible(User user, CPDisplayLayout cpDisplayLayout) {
		if (cpDisplayLayout == null) {
			return true;
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		try {
			LayoutPageTemplateEntry layoutPageTemplateEntry =
				_layoutPageTemplateEntryLocalService.
					fetchLayoutPageTemplateEntryByUuidAndGroupId(
						cpDisplayLayout.getLayoutPageTemplateEntryUuid(),
						cpDisplayLayout.getGroupId());

			if ((layoutPageTemplateEntry != null) &&
				_layoutPageTemplateEntryModelResourcePermission.contains(
					permissionChecker, layoutPageTemplateEntry,
					ActionKeys.VIEW)) {

				return true;
			}
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}

		if (Validator.isNotNull(
				cpDisplayLayout.getLayoutPageTemplateEntryUuid())) {

			return true;
		}

		return false;
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		CPDefinitionDisplayLayoutDisplayContext
			cpDefinitionDisplayLayoutDisplayContext =
				new CPDefinitionDisplayLayoutDisplayContext(
					_actionHelper, httpServletRequest,
					_commerceChannelLocalService, _cpDefinitionService,
					_cpDisplayLayoutService, _groupLocalService, _itemSelector,
					_layoutLocalService, _layoutPageTemplateEntryLocalService);

		httpServletRequest.setAttribute(
			WebKeys.PORTLET_DISPLAY_CONTEXT,
			cpDefinitionDisplayLayoutDisplayContext);

		_jspRenderer.renderJSP(
			_setServletContext, httpServletRequest, httpServletResponse,
			"/display_layout/layout_page_template_entry_selection.jsp");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutPageTemplateEntryCPDefinitionDisplayLayoutSelectionScreenNavigationCategory.class);

	@Reference
	private ActionHelper _actionHelper;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CPDefinitionService _cpDefinitionService;

	@Reference
	private CPDisplayLayoutService _cpDisplayLayoutService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private ItemSelector _itemSelector;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private Language _language;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.layout.page.template.model.LayoutPageTemplateEntry)"
	)
	private ModelResourcePermission<LayoutPageTemplateEntry>
		_layoutPageTemplateEntryModelResourcePermission;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.product.definitions.web)"
	)
	private ServletContext _setServletContext;

}