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

package com.liferay.fragment.renderer.categorization.inputs.internal;

import com.liferay.asset.kernel.model.AssetVocabularyConstants;
import com.liferay.asset.taglib.servlet.taglib.AssetCategoriesSelectorTag;
import com.liferay.fragment.constants.FragmentConstants;
import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.renderer.FragmentRenderer;
import com.liferay.fragment.renderer.FragmentRendererContext;
import com.liferay.fragment.util.configuration.FragmentEntryConfigurationParser;
import com.liferay.frontend.taglib.clay.servlet.taglib.AlertTag;
import com.liferay.info.constants.InfoItemCreatorConstants;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.item.creator.InfoItemCreator;
import com.liferay.layout.constants.LayoutWebKeys;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.util.constants.LayoutDataItemTypeConstants;
import com.liferay.layout.util.structure.FormStyledLayoutStructureItem;
import com.liferay.layout.util.structure.FragmentStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructureItemUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.PrintWriter;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jorge Ferrer
 */
@Component(service = FragmentRenderer.class)
public class CategoriesInputFragmentRenderer implements FragmentRenderer {

	@Override
	public String getCollectionKey() {
		return "INPUTS";
	}

	@Override
	public String getConfiguration(
		FragmentRendererContext fragmentRendererContext) {

		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", getClass());

		try {
			JSONObject jsonObject = _jsonFactory.createJSONObject(
				StringUtil.read(
					getClass(),
					"/com/liferay/fragment/renderer/categorization/inputs" +
						"/internal/dependencies/configuration.json"));

			return _fragmentEntryConfigurationParser.translateConfiguration(
				jsonObject, resourceBundle);
		}
		catch (JSONException jsonException) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsonException);
			}

			return StringPool.BLANK;
		}
	}

	@Override
	public String getIcon() {
		return "categories";
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", getClass());

		return _language.get(resourceBundle, "categories");
	}

	@Override
	public int getType() {
		return FragmentConstants.TYPE_INPUT;
	}

	@Override
	public String getTypeOptions() {
		return JSONUtil.put(
			"fieldTypes", JSONUtil.putAll("categorization")
		).toString();
	}

	@Override
	public boolean isSelectable(HttpServletRequest httpServletRequest) {
		if (GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-161631"))) {
			return true;
		}

		return false;
	}

	@Override
	public void render(
		FragmentRendererContext fragmentRendererContext,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		try {
			FormStyledLayoutStructureItem formStyledLayoutStructureItem =
				_getFormStyledLayoutStructureItem(
					fragmentRendererContext.getFragmentEntryLink(),
					httpServletRequest);

			if (formStyledLayoutStructureItem == null) {
				return;
			}

			String className = _portal.getClassName(
				formStyledLayoutStructureItem.getClassNameId());

			InfoItemCreator<Object> infoItemCreator =
				_infoItemServiceRegistry.getFirstInfoItemService(
					InfoItemCreator.class, className);

			PrintWriter printWriter = httpServletResponse.getWriter();

			if (!infoItemCreator.supportsCategorization()) {
				_writeDisabledCategorizationAlert(
					httpServletRequest, httpServletResponse, printWriter);

				return;
			}

			printWriter.write("<div");

			if (Objects.equals(
					fragmentRendererContext.getMode(),
					FragmentEntryLinkConstants.EDIT)) {

				printWriter.write(" inert");
			}

			printWriter.write(StringPool.GREATER_THAN);

			AssetCategoriesSelectorTag assetCategoriesSelectorTag =
				new AssetCategoriesSelectorTag();

			assetCategoriesSelectorTag.setClassName(className);
			assetCategoriesSelectorTag.setClassTypePK(
				formStyledLayoutStructureItem.getClassTypeId());

			if (Objects.equals(
					infoItemCreator.getScope(),
					InfoItemCreatorConstants.SCOPE_COMPANY)) {

				ThemeDisplay themeDisplay =
					(ThemeDisplay)httpServletRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				assetCategoriesSelectorTag.setGroupIds(
					new long[] {themeDisplay.getCompanyGroupId()});
			}

			assetCategoriesSelectorTag.setShowLabel(false);
			assetCategoriesSelectorTag.setVisibilityTypes(
				_getVisibilityTypes(
					fragmentRendererContext.getFragmentEntryLink()));

			assetCategoriesSelectorTag.doTag(
				httpServletRequest, httpServletResponse);

			printWriter.write("</div>");
		}
		catch (Exception exception) {
			_log.error(
				"Unable to render categorization input fragment", exception);
		}
	}

	private FormStyledLayoutStructureItem _getFormStyledLayoutStructureItem(
			FragmentEntryLink fragmentEntryLink,
			HttpServletRequest httpServletRequest)
		throws PortalException {

		LayoutStructure layoutStructure = _getLayoutStructure(
			fragmentEntryLink, httpServletRequest);

		FragmentStyledLayoutStructureItem fragmentStyledLayoutStructureItem =
			(FragmentStyledLayoutStructureItem)
				layoutStructure.getLayoutStructureItemByFragmentEntryLinkId(
					fragmentEntryLink.getFragmentEntryLinkId());

		if (fragmentStyledLayoutStructureItem == null) {
			return null;
		}

		LayoutStructureItem layoutStructureItem =
			LayoutStructureItemUtil.getAncestor(
				fragmentStyledLayoutStructureItem.getItemId(),
				LayoutDataItemTypeConstants.TYPE_FORM, layoutStructure);

		if (!(layoutStructureItem instanceof FormStyledLayoutStructureItem)) {
			return null;
		}

		return (FormStyledLayoutStructureItem)layoutStructureItem;
	}

	private LayoutStructure _getLayoutStructure(
			FragmentEntryLink fragmentEntryLink,
			HttpServletRequest httpServletRequest)
		throws PortalException {

		LayoutStructure layoutStructure = null;

		if (httpServletRequest != null) {
			layoutStructure = (LayoutStructure)httpServletRequest.getAttribute(
				LayoutWebKeys.LAYOUT_STRUCTURE);
		}

		if (layoutStructure == null) {
			LayoutPageTemplateStructure layoutPageTemplateStructure =
				_layoutPageTemplateStructureLocalService.
					fetchLayoutPageTemplateStructure(
						fragmentEntryLink.getGroupId(),
						fragmentEntryLink.getPlid(), true);

			layoutStructure = LayoutStructure.of(
				layoutPageTemplateStructure.getData(
					fragmentEntryLink.getSegmentsExperienceId()));
		}

		return layoutStructure;
	}

	private int[] _getVisibilityTypes(FragmentEntryLink fragmentEntryLink) {
		String vocabularyType = GetterUtil.getString(
			_fragmentEntryConfigurationParser.getFieldValue(
				fragmentEntryLink.getConfiguration(),
				fragmentEntryLink.getEditableValues(),
				LocaleUtil.getMostRelevantLocale(), "vocabularyType"));

		if (Objects.equals(vocabularyType, "all")) {
			return AssetVocabularyConstants.VISIBILITY_TYPES;
		}

		if (Objects.equals(vocabularyType, "internal")) {
			return new int[] {
				AssetVocabularyConstants.VISIBILITY_TYPE_INTERNAL
			};
		}

		return new int[] {AssetVocabularyConstants.VISIBILITY_TYPE_PUBLIC};
	}

	private void _writeDisabledCategorizationAlert(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, PrintWriter printWriter)
		throws Exception {

		AlertTag alertTag = new AlertTag();

		alertTag.setMessage(
			_language.get(
				httpServletRequest.getLocale(),
				"categorization-is-disabled-for-the-selected-content"));
		alertTag.setTitle(
			_language.get(httpServletRequest.getLocale(), "info"));

		printWriter.write(
			alertTag.doTagAsString(httpServletRequest, httpServletResponse));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CategoriesInputFragmentRenderer.class);

	@Reference
	private FragmentEntryConfigurationParser _fragmentEntryConfigurationParser;

	@Reference
	private InfoItemServiceRegistry _infoItemServiceRegistry;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Reference
	private Portal _portal;

}