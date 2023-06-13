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

import com.liferay.fragment.contributor.FragmentCollectionContributorRegistry;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.renderer.FragmentRenderer;
import com.liferay.fragment.renderer.FragmentRendererRegistry;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	property = {
		"javax.portlet.name=" + ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
		"mvc.command.name=/layout_content_page_editor/get_fragment_entry_input_field_types"
	},
	service = MVCResourceCommand.class
)
public class GetFragmentEntryInputFieldTypesMVCResourceCommand
	extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String fragmentEntryKey = ParamUtil.getString(
			resourceRequest, "fragmentEntryKey");

		String typeOptions = _getFragmentTypeOptions(
			fragmentEntryKey,
			ParamUtil.getLong(
				resourceRequest, "groupId", themeDisplay.getScopeGroupId()));

		if (Validator.isNull(typeOptions)) {
			JSONPortletResponseUtil.writeJSON(
				resourceRequest, resourceResponse,
				_jsonFactory.createJSONArray());

			return;
		}

		JSONObject typeOptionsJSONObject = _jsonFactory.createJSONObject(
			typeOptions);

		JSONArray fieldTypesJSONArray = typeOptionsJSONObject.getJSONArray(
			"fieldTypes");

		if ((fieldTypesJSONArray == null) ||
			(fieldTypesJSONArray.length() == 0)) {

			JSONPortletResponseUtil.writeJSON(
				resourceRequest, resourceResponse,
				_jsonFactory.createJSONArray());

			return;
		}

		JSONPortletResponseUtil.writeJSON(
			resourceRequest, resourceResponse, fieldTypesJSONArray);
	}

	private String _getFragmentTypeOptions(
		String fragmentEntryKey, long groupId) {

		FragmentEntry fragmentEntry =
			_fragmentEntryLocalService.fetchFragmentEntry(
				groupId, fragmentEntryKey);

		if (fragmentEntry == null) {
			fragmentEntry =
				_fragmentCollectionContributorRegistry.getFragmentEntry(
					fragmentEntryKey);
		}

		if (fragmentEntry != null) {
			return fragmentEntry.getTypeOptions();
		}

		FragmentRenderer fragmentRenderer =
			_fragmentRendererRegistry.getFragmentRenderer(fragmentEntryKey);

		if (fragmentRenderer != null) {
			return fragmentRenderer.getTypeOptions();
		}

		return StringPool.BLANK;
	}

	@Reference
	private FragmentCollectionContributorRegistry
		_fragmentCollectionContributorRegistry;

	@Reference
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@Reference
	private FragmentRendererRegistry _fragmentRendererRegistry;

	@Reference
	private JSONFactory _jsonFactory;

}