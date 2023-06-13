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

package com.liferay.commerce.product.type.virtual.web.internal.frontend.taglib.servlet.taglib;

import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.portlet.action.ActionHelper;
import com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants;
import com.liferay.commerce.product.type.virtual.web.internal.display.context.CPDefinitionVirtualSettingDisplayContext;
import com.liferay.commerce.product.type.virtual.web.internal.portlet.action.helper.CPDefinitionVirtualSettingActionHelper;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.item.selector.ItemSelector;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Joao Victor Alves
 */
@Component(
	property = "screen.navigation.entry.order:Integer=10",
	service = ScreenNavigationEntry.class
)
public class CPInstanceVirtualSettingsScreenNavigationEntry
	extends CPInstanceVirtualSettingsScreenNavigationCategory
	implements ScreenNavigationEntry<CPInstance> {

	@Override
	public String getEntryKey() {
		return getCategoryKey();
	}

	@Override
	public boolean isVisible(User user, CPInstance cpInstance) {
		if (cpInstance == null) {
			return false;
		}

		try {
			CPDefinition cpDefinition = cpInstance.getCPDefinition();

			String productTypeName = cpDefinition.getProductTypeName();

			if (productTypeName.equals(VirtualCPTypeConstants.NAME)) {
				return true;
			}
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}

		return false;
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		CPDefinitionVirtualSettingDisplayContext
			cpDefinitionVirtualSettingDisplayContext =
				new CPDefinitionVirtualSettingDisplayContext(
					_actionHelper, httpServletRequest, _dlAppService,
					_journalArticleService,
					_cpDefinitionVirtualSettingActionHelper, _itemSelector);

		httpServletRequest.setAttribute(
			WebKeys.PORTLET_DISPLAY_CONTEXT,
			cpDefinitionVirtualSettingDisplayContext);

		_jspRenderer.renderJSP(
			_servletContext, httpServletRequest, httpServletResponse,
			"/edit_cp_instance_virtual_setting.jsp");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CPInstanceVirtualSettingsScreenNavigationEntry.class);

	@Reference
	private ActionHelper _actionHelper;

	@Reference
	private CPDefinitionVirtualSettingActionHelper
		_cpDefinitionVirtualSettingActionHelper;

	@Reference
	private DLAppService _dlAppService;

	@Reference
	private ItemSelector _itemSelector;

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.product.type.virtual.web)"
	)
	private ServletContext _servletContext;

}