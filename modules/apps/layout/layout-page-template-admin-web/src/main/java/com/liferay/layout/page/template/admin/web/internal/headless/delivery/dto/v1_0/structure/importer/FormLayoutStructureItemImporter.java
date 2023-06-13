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

package com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.structure.importer;

import com.liferay.headless.delivery.dto.v1_0.ContextReference;
import com.liferay.headless.delivery.dto.v1_0.PageElement;
import com.liferay.layout.page.template.util.AlignConverter;
import com.liferay.layout.page.template.util.ContentDisplayConverter;
import com.liferay.layout.page.template.util.FlexWrapConverter;
import com.liferay.layout.page.template.util.JustifyConverter;
import com.liferay.layout.util.structure.FormStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eudaldo Alonso
 */
@Component(service = LayoutStructureItemImporter.class)
public class FormLayoutStructureItemImporter
	extends BaseLayoutStructureItemImporter
	implements LayoutStructureItemImporter {

	@Override
	public LayoutStructureItem addLayoutStructureItem(
			LayoutStructure layoutStructure,
			LayoutStructureItemImporterContext
				layoutStructureItemImporterContext,
			PageElement pageElement, Set<String> warningMessages)
		throws Exception {

		FormStyledLayoutStructureItem formStyledLayoutStructureItem =
			(FormStyledLayoutStructureItem)
				layoutStructure.addFormStyledLayoutStructureItem(
					layoutStructureItemImporterContext.getParentItemId(),
					layoutStructureItemImporterContext.getPosition());

		Map<String, Object> definitionMap = getDefinitionMap(
			pageElement.getDefinition());

		if (definitionMap == null) {
			return formStyledLayoutStructureItem;
		}

		if (definitionMap.containsKey("cssClasses")) {
			List<String> cssClasses = (List<String>)definitionMap.get(
				"cssClasses");

			formStyledLayoutStructureItem.setCssClasses(
				new HashSet<>(cssClasses));
		}

		if (definitionMap.containsKey("customCSS")) {
			formStyledLayoutStructureItem.setCustomCSS(
				String.valueOf(definitionMap.get("customCSS")));
		}

		if (definitionMap.containsKey("customCSSViewports")) {
			List<Map<String, Object>> customCSSViewports =
				(List<Map<String, Object>>)definitionMap.get(
					"customCSSViewports");

			for (Map<String, Object> customCSSViewport : customCSSViewports) {
				formStyledLayoutStructureItem.setCustomCSSViewport(
					(String)customCSSViewport.get("id"),
					(String)customCSSViewport.get("customCSS"));
			}
		}

		Map<String, Object> sourceMap = (Map<String, Object>)definitionMap.get(
			"formConfig");

		if (sourceMap != null) {
			Map<String, Object> itemReferenceMap =
				(Map<String, Object>)sourceMap.get("formReference");

			if (Objects.equals(
					ContextReference.ContextSource.DISPLAY_PAGE_ITEM.getValue(),
					(String)itemReferenceMap.get("contextSource"))) {

				formStyledLayoutStructureItem.setFormConfig(
					FormStyledLayoutStructureItem.
						FORM_CONFIG_DISPLAY_PAGE_ITEM_TYPE);
			}
			else {
				formStyledLayoutStructureItem.setClassNameId(
					portal.getClassNameId(
						(String)itemReferenceMap.get("className")));

				Integer classType = (Integer)itemReferenceMap.get("classType");

				if (classType != null) {
					formStyledLayoutStructureItem.setClassTypeId(classType);
				}

				formStyledLayoutStructureItem.setFormConfig(
					FormStyledLayoutStructureItem.FORM_CONFIG_OTHER_ITEM_TYPE);
			}

			JSONObject successMessageJSONObject = _getSuccessMessageJSONObject(
				layoutStructureItemImporterContext, sourceMap);

			if (successMessageJSONObject != null) {
				formStyledLayoutStructureItem.setSuccessMessageJSONObject(
					successMessageJSONObject);
			}
		}

		Map<String, Object> fragmentStyleMap =
			(Map<String, Object>)definitionMap.get("fragmentStyle");

		if (fragmentStyleMap != null) {
			JSONObject jsonObject = JSONUtil.put(
				"styles",
				toStylesJSONObject(
					layoutStructureItemImporterContext, fragmentStyleMap));

			formStyledLayoutStructureItem.updateItemConfig(jsonObject);
		}

		if (definitionMap.containsKey("fragmentViewports")) {
			List<Map<String, Object>> fragmentViewports =
				(List<Map<String, Object>>)definitionMap.get(
					"fragmentViewports");

			for (Map<String, Object> fragmentViewport : fragmentViewports) {
				JSONObject jsonObject = JSONUtil.put(
					(String)fragmentViewport.get("id"),
					toFragmentViewportStylesJSONObject(fragmentViewport));

				formStyledLayoutStructureItem.updateItemConfig(jsonObject);
			}
		}

		if (definitionMap.containsKey("indexed")) {
			formStyledLayoutStructureItem.setIndexed(
				GetterUtil.getBoolean(definitionMap.get("indexed")));
		}

		Map<String, Object> formLayout = (Map<String, Object>)definitionMap.get(
			"layout");

		if (formLayout != null) {
			String align = String.valueOf(
				formLayout.getOrDefault("align", StringPool.BLANK));

			if (Validator.isNotNull(align)) {
				formStyledLayoutStructureItem.setAlign(
					AlignConverter.convertToInternalValue(align));
			}

			String contentDisplay = String.valueOf(
				formLayout.getOrDefault("contentDisplay", StringPool.BLANK));

			if (Validator.isNotNull(contentDisplay)) {
				formStyledLayoutStructureItem.setContentDisplay(
					ContentDisplayConverter.convertToInternalValue(
						contentDisplay));
			}

			String flexWrap = String.valueOf(
				formLayout.getOrDefault("flexWrap", StringPool.BLANK));

			if (Validator.isNotNull(flexWrap)) {
				formStyledLayoutStructureItem.setFlexWrap(
					FlexWrapConverter.convertToInternalValue(flexWrap));
			}

			String justify = String.valueOf(
				formLayout.getOrDefault("justify", StringPool.BLANK));

			if (Validator.isNotNull(justify)) {
				formStyledLayoutStructureItem.setJustify(
					JustifyConverter.convertToInternalValue(justify));
			}

			String widthType = StringUtil.toLowerCase(
				(String)formLayout.get("widthType"));

			if (widthType != null) {
				formStyledLayoutStructureItem.setWidthType(widthType);
			}
		}

		if (definitionMap.containsKey("name")) {
			formStyledLayoutStructureItem.setName(
				GetterUtil.getString(definitionMap.get("name")));
		}

		return formStyledLayoutStructureItem;
	}

	@Override
	public PageElement.Type getPageElementType() {
		return PageElement.Type.FORM;
	}

	private JSONObject _getLocalizedValuesJSONObject(
		String key, Map<String, Object> propertiesMap) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		Map<String, Object> map = (Map<String, Object>)propertiesMap.get(key);

		if (MapUtil.isEmpty(map)) {
			return jsonObject;
		}

		Map<String, Object> localizedMap = (Map<String, Object>)map.get(
			"value_i18n");

		if (localizedMap == null) {
			return jsonObject;
		}

		for (Map.Entry<String, Object> entry : localizedMap.entrySet()) {
			jsonObject.put(entry.getKey(), entry.getValue());
		}

		return JSONUtil.put(key, jsonObject);
	}

	private JSONObject _getSuccessMessageJSONObject(
		LayoutStructureItemImporterContext layoutStructureItemImporterContext,
		Map<String, Object> sourceMap) {

		Map<String, Object> formSuccessSubmissionResultMap =
			(Map<String, Object>)sourceMap.get("formSuccessSubmissionResult");

		if (MapUtil.isEmpty(formSuccessSubmissionResultMap)) {
			return null;
		}

		if (formSuccessSubmissionResultMap.containsKey("message")) {
			return _getLocalizedValuesJSONObject(
				"message", formSuccessSubmissionResultMap);
		}
		else if (formSuccessSubmissionResultMap.containsKey("itemReference")) {
			Map<String, Object> itemReference =
				(Map<String, Object>)formSuccessSubmissionResultMap.get(
					"itemReference");

			return JSONUtil.put(
				"layout",
				getLayoutFromItemReferenceJSONObject(
					itemReference, layoutStructureItemImporterContext));
		}
		else if (formSuccessSubmissionResultMap.containsKey("url")) {
			return _getLocalizedValuesJSONObject(
				"url", formSuccessSubmissionResultMap);
		}

		return null;
	}

}