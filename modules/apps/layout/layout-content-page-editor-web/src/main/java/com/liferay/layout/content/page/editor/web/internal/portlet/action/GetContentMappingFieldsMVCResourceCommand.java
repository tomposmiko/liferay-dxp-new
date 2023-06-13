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

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.ClassTypeReader;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldType;
import com.liferay.info.display.contributor.InfoDisplayField;
import com.liferay.info.display.field.ClassTypesInfoDisplayFieldProvider;
import com.liferay.journal.model.JournalArticle;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
		"mvc.command.name=/content_layout/get_content_structure_mapping_fields"
	},
	service = MVCResourceCommand.class
)
public class GetContentMappingFieldsMVCResourceCommand
	extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				JournalArticle.class.getName());

		ClassTypeReader classTypeReader =
			assetRendererFactory.getClassTypeReader();

		long ddmStructureId = ParamUtil.getLong(
			resourceRequest, "ddmStructureId");

		List<InfoDisplayField> infoDisplayFields =
			_classTypesInfoDisplayFieldProvider.getClassTypeInfoDisplayFields(
				classTypeReader.getClassType(
					ddmStructureId, themeDisplay.getLocale()),
				themeDisplay.getLocale());

		for (InfoDisplayField infoDisplayField : infoDisplayFields) {
			boolean disabled = !ArrayUtil.contains(
				_SUPPORTED_DDM_TYPES, infoDisplayField.getType());

			String label = infoDisplayField.getLabel();

			if (disabled) {
				label += StringUtil.appendParentheticalSuffix(
					StringPool.SPACE,
					LanguageUtil.get(
						_portal.getHttpServletRequest(resourceRequest),
						"not-supported"));
			}

			JSONObject jsonObject = JSONUtil.put(
				"disabled", disabled
			).put(
				"key", infoDisplayField.getKey()
			).put(
				"label", label
			).put(
				"type", infoDisplayField.getType()
			);

			jsonArray.put(jsonObject);
		}

		JSONPortletResponseUtil.writeJSON(
			resourceRequest, resourceResponse, jsonArray);
	}

	private static final String[] _SUPPORTED_DDM_TYPES = {
		DDMFormFieldType.IMAGE, DDMFormFieldType.TEXT,
		DDMFormFieldType.TEXT_AREA, DDMFormFieldType.TEXT_HTML
	};

	@Reference
	private ClassTypesInfoDisplayFieldProvider
		_classTypesInfoDisplayFieldProvider;

	@Reference
	private Portal _portal;

}