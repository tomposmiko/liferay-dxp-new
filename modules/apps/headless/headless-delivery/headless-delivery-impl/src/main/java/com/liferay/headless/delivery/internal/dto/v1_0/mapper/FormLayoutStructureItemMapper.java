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

package com.liferay.headless.delivery.internal.dto.v1_0.mapper;

import com.liferay.headless.delivery.dto.v1_0.ClassTypeReference;
import com.liferay.headless.delivery.dto.v1_0.ContextReference;
import com.liferay.headless.delivery.dto.v1_0.FormConfig;
import com.liferay.headless.delivery.dto.v1_0.FragmentInlineValue;
import com.liferay.headless.delivery.dto.v1_0.Layout;
import com.liferay.headless.delivery.dto.v1_0.MessageFormSubmissionResult;
import com.liferay.headless.delivery.dto.v1_0.PageElement;
import com.liferay.headless.delivery.dto.v1_0.PageFormDefinition;
import com.liferay.headless.delivery.dto.v1_0.SitePageFormSubmissionResult;
import com.liferay.headless.delivery.dto.v1_0.URLFormSubmissionResult;
import com.liferay.headless.delivery.internal.dto.v1_0.mapper.util.FragmentMappedValueUtil;
import com.liferay.headless.delivery.internal.dto.v1_0.mapper.util.LocalizedValueUtil;
import com.liferay.headless.delivery.internal.dto.v1_0.mapper.util.StyledLayoutStructureItemUtil;
import com.liferay.layout.page.template.util.AlignConverter;
import com.liferay.layout.page.template.util.ContentDisplayConverter;
import com.liferay.layout.page.template.util.FlexWrapConverter;
import com.liferay.layout.page.template.util.JustifyConverter;
import com.liferay.layout.util.structure.FormStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(service = LayoutStructureItemMapper.class)
public class FormLayoutStructureItemMapper
	extends BaseStyledLayoutStructureItemMapper {

	@Override
	public String getClassName() {
		return FormStyledLayoutStructureItem.class.getName();
	}

	@Override
	public PageElement getPageElement(
		long groupId, LayoutStructureItem layoutStructureItem,
		boolean saveInlineContent, boolean saveMappingConfiguration) {

		FormStyledLayoutStructureItem formStyledLayoutStructureItem =
			(FormStyledLayoutStructureItem)layoutStructureItem;

		return new PageElement() {
			{
				definition = new PageFormDefinition() {
					{
						cssClasses =
							StyledLayoutStructureItemUtil.getCssClasses(
								formStyledLayoutStructureItem);
						customCSS = StyledLayoutStructureItemUtil.getCustomCSS(
							formStyledLayoutStructureItem);
						customCSSViewports =
							StyledLayoutStructureItemUtil.getCustomCSSViewports(
								formStyledLayoutStructureItem);
						formConfig = new FormConfig() {
							{
								formReference = _toFormReference(
									formStyledLayoutStructureItem);
								formSuccessSubmissionResult =
									_toFormSuccessSubmissionResult(
										saveInlineContent,
										saveMappingConfiguration,
										formStyledLayoutStructureItem);
							}
						};
						indexed = formStyledLayoutStructureItem.isIndexed();
						layout = _toLayout(formStyledLayoutStructureItem);

						setFragmentStyle(
							() -> {
								JSONObject itemConfigJSONObject =
									formStyledLayoutStructureItem.
										getItemConfigJSONObject();

								return toFragmentStyle(
									itemConfigJSONObject.getJSONObject(
										"styles"),
									saveMappingConfiguration);
							});
						setFragmentViewports(
							() -> getFragmentViewPorts(
								formStyledLayoutStructureItem.
									getItemConfigJSONObject()));
						setName(formStyledLayoutStructureItem::getName);
					}
				};
				type = Type.FORM;
			}
		};
	}

	private Object _toFormReference(
		FormStyledLayoutStructureItem formStyledLayoutStructureItem) {

		if (formStyledLayoutStructureItem.getFormConfig() ==
				FormStyledLayoutStructureItem.FORM_CONFIG_OTHER_ITEM_TYPE) {

			return new ClassTypeReference() {
				{
					className = _portal.getClassName(
						formStyledLayoutStructureItem.getClassNameId());
					classType = formStyledLayoutStructureItem.getClassTypeId();
				}
			};
		}

		return new ContextReference() {
			{
				contextSource = ContextSource.DISPLAY_PAGE_ITEM;
			}
		};
	}

	private Object _toFormSuccessSubmissionResult(
		boolean saveInlineContent, boolean saveMappingConfiguration,
		FormStyledLayoutStructureItem formStyledLayoutStructureItem) {

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-149720"))) {
			return null;
		}

		JSONObject successMessageJSONObject =
			formStyledLayoutStructureItem.getSuccessMessageJSONObject();

		if (successMessageJSONObject == null) {
			return null;
		}

		if (saveInlineContent && successMessageJSONObject.has("message")) {
			return new MessageFormSubmissionResult() {
				{
					message = _toFragmentInlineValue(
						successMessageJSONObject.getJSONObject("message"));
				}
			};
		}

		if (saveInlineContent && successMessageJSONObject.has("url")) {
			return new URLFormSubmissionResult() {
				{
					url = _toFragmentInlineValue(
						successMessageJSONObject.getJSONObject("url"));
				}
			};
		}

		if (saveMappingConfiguration &&
			successMessageJSONObject.has("layout")) {

			JSONObject layoutJSONObject =
				successMessageJSONObject.getJSONObject("layout");

			return new SitePageFormSubmissionResult() {
				{
					itemReference =
						FragmentMappedValueUtil.toLayoutClassFieldsReference(
							layoutJSONObject);
				}
			};
		}

		return null;
	}

	private FragmentInlineValue _toFragmentInlineValue(JSONObject jsonObject) {
		return new FragmentInlineValue() {
			{
				value_i18n = LocalizedValueUtil.toLocalizedValues(jsonObject);
			}
		};
	}

	private Layout _toLayout(
		FormStyledLayoutStructureItem formStyledLayoutStructureItem) {

		return new Layout() {
			{
				setAlign(
					() -> {
						String align = formStyledLayoutStructureItem.getAlign();

						if (Validator.isNull(align)) {
							return null;
						}

						return Align.create(
							AlignConverter.convertToExternalValue(align));
					});
				setContentDisplay(
					() -> {
						String contentDisplay =
							formStyledLayoutStructureItem.getContentDisplay();

						if (Validator.isNull(contentDisplay)) {
							return null;
						}

						return ContentDisplay.create(
							ContentDisplayConverter.convertToExternalValue(
								contentDisplay));
					});
				setFlexWrap(
					() -> {
						String flexWrap =
							formStyledLayoutStructureItem.getFlexWrap();

						if (Validator.isNull(flexWrap)) {
							return null;
						}

						return FlexWrap.create(
							FlexWrapConverter.convertToExternalValue(flexWrap));
					});
				setJustify(
					() -> {
						String justify =
							formStyledLayoutStructureItem.getJustify();

						if (Validator.isNull(justify)) {
							return null;
						}

						return Justify.create(
							JustifyConverter.convertToExternalValue(justify));
					});
				setWidthType(
					() -> {
						String widthType =
							formStyledLayoutStructureItem.getWidthType();

						if (Validator.isNotNull(widthType)) {
							return WidthType.create(
								StringUtil.upperCaseFirstLetter(widthType));
						}

						return null;
					});
			}
		};
	}

	@Reference
	private Portal _portal;

}