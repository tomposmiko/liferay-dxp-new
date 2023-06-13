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

package com.liferay.layout.admin.web.internal.portlet.action;

import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryService;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + LayoutAdminPortletKeys.GROUP_PAGES,
		"mvc.command.name=/layout/delete_layout_page_template_entry"
	},
	service = MVCActionCommand.class
)
public class DeleteLayoutPageTemplateEntryMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long[] deleteLayoutPageTemplateEntryIds = null;

		long layoutPageTemplateEntryId = ParamUtil.getLong(
			actionRequest, "layoutPageTemplateEntryId");

		if (layoutPageTemplateEntryId > 0) {
			deleteLayoutPageTemplateEntryIds =
				new long[] {layoutPageTemplateEntryId};
		}
		else {
			deleteLayoutPageTemplateEntryIds = ParamUtil.getLongValues(
				actionRequest, "rowIds");
		}

		_layoutPageTemplateEntryService.deleteLayoutPageTemplateEntries(
			deleteLayoutPageTemplateEntryIds);
	}

	@Reference
	private LayoutPageTemplateEntryService _layoutPageTemplateEntryService;

}