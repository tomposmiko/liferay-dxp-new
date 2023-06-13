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
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutPrototype;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutPrototypeLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Objects;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Víctor Galán
 */
@Component(
	property = {
		"javax.portlet.name=" + LayoutAdminPortletKeys.GROUP_PAGES,
		"mvc.command.name=/layout_admin/get_layout_page_template_entry_list"
	},
	service = MVCResourceCommand.class
)
public class GetLayoutPageTemplateEntryListMVCResourceCommand
	extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		for (LayoutPageTemplateEntry layoutPageTemplateEntry :
				_layoutPageTemplateEntryService.getLayoutPageTemplateEntries(
					themeDisplay.getScopeGroupId(),
					ParamUtil.getLong(
						resourceRequest, "layoutPageTemplateCollectionId"),
					WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS)) {

			jsonArray.put(
				JSONUtil.put(
					"layoutPageTemplateEntryId",
					String.valueOf(
						layoutPageTemplateEntry.getLayoutPageTemplateEntryId())
				).put(
					"name", layoutPageTemplateEntry.getName()
				).put(
					"previewLayoutURL",
					() -> {
						String layoutFullURL = null;

						if (Objects.equals(
								layoutPageTemplateEntry.getType(),
								LayoutPageTemplateEntryTypeConstants.
									TYPE_WIDGET_PAGE)) {

							LayoutPrototype layoutPrototype =
								_layoutPrototypeLocalService.
									fetchLayoutPrototype(
										layoutPageTemplateEntry.
											getLayoutPrototypeId());

							if (layoutPrototype == null) {
								return null;
							}

							Group layoutPrototypeGroup =
								layoutPrototype.getGroup();

							layoutFullURL = layoutPrototypeGroup.getDisplayURL(
								themeDisplay, true);
						}
						else {
							Layout layout = _layoutLocalService.fetchLayout(
								layoutPageTemplateEntry.getPlid());

							layoutFullURL = _portal.getLayoutFullURL(
								layout, themeDisplay);
						}

						return HttpComponentsUtil.setParameter(
							layoutFullURL, "p_l_mode", Constants.PREVIEW);
					}
				));
		}

		JSONPortletResponseUtil.writeJSON(
			resourceRequest, resourceResponse, jsonArray);
	}

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateEntryService _layoutPageTemplateEntryService;

	@Reference
	private LayoutPrototypeLocalService _layoutPrototypeLocalService;

	@Reference
	private Portal _portal;

}