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

package com.liferay.commerce.product.content.web.internal.fragment.renderer;

import com.liferay.commerce.inventory.engine.CommerceInventoryEngine;
import com.liferay.commerce.model.CPDefinitionInventory;
import com.liferay.commerce.product.content.web.internal.servlet.ServletContextUtil;
import com.liferay.commerce.product.exception.CPDefinitionIgnoreSKUCombinationsException;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.service.CPDefinitionInventoryLocalService;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.renderer.FragmentRenderer;
import com.liferay.fragment.renderer.FragmentRendererContext;
import com.liferay.fragment.util.configuration.FragmentEntryConfigurationParser;
import com.liferay.info.constants.InfoDisplayWebKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Stefano Motta
 */
@Component(service = FragmentRenderer.class)
public class DynamicFieldFragmentRenderer implements FragmentRenderer {

	@Override
	public String getCollectionKey() {
		return "commerce-product";
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
					"/com/liferay/commerce/product/content/web/internal" +
						"/fragment/renderer/dynamic_field/dependencies" +
							"/configuration.json"));

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
	public String getLabel(Locale locale) {
		return _language.get(locale, "dynamic-field");
	}

	@Override
	public boolean isSelectable(HttpServletRequest httpServletRequest) {
		return true;
	}

	@Override
	public void render(
			FragmentRendererContext fragmentRendererContext,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		String field = _getConfigurationValue(
			fragmentRendererContext.getFragmentEntryLink(), "field");

		httpServletRequest.setAttribute(
			"liferay-commerce:dynamic-field:field", field);

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		httpServletRequest.setAttribute(
			"liferay-commerce:dynamic-field:label",
			_language.get(
				themeDisplay.getLocale(),
				_getConfigurationValue(
					fragmentRendererContext.getFragmentEntryLink(), "label")));

		httpServletRequest.setAttribute(
			"liferay-commerce:dynamic-field:labelElementType",
			_getConfigurationValue(
				fragmentRendererContext.getFragmentEntryLink(),
				"labelElementType"));
		httpServletRequest.setAttribute(
			"liferay-commerce:dynamic-field:valueElementType",
			_getConfigurationValue(
				fragmentRendererContext.getFragmentEntryLink(),
				"valueElementType"));

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(
				"/fragment/renderer/dynamic_field/page.jsp");

		Object infoItem = httpServletRequest.getAttribute(
			InfoDisplayWebKeys.INFO_ITEM);

		try {
			if ((infoItem == null) || !(infoItem instanceof CPDefinition)) {
				if (_isEditMode(httpServletRequest)) {
					httpServletRequest.setAttribute(
						"liferay-commerce:dynamic-field:fieldValue",
						_getFieldLabel(
							fragmentRendererContext.getFragmentEntryLink(),
							field));

					requestDispatcher.include(
						httpServletRequest, httpServletResponse);
				}

				return;
			}

			CPDefinition cpDefinition = (CPDefinition)infoItem;

			if (field.equals("availability.stockQuantity")) {
				CPDefinitionInventory cpDefinitionInventory =
					_cpDefinitionInventoryLocalService.
						fetchCPDefinitionInventoryByCPDefinitionId(
							cpDefinition.getCPDefinitionId());

				if (!cpDefinitionInventory.isDisplayStockQuantity()) {
					return;
				}
			}

			httpServletRequest.setAttribute(
				"liferay-commerce:dynamic-field:fieldValue",
				_getCPInstanceFieldValue(
					httpServletRequest, cpDefinition, field));

			String namespace = (String)httpServletRequest.getAttribute(
				"liferay-commerce:dynamic-field:namespace");

			if (Validator.isNull(namespace)) {
				PortletDisplay portletDisplay =
					themeDisplay.getPortletDisplay();

				namespace = portletDisplay.getNamespace();

				httpServletRequest.setAttribute(
					"liferay-commerce:dynamic-field:namespace", namespace);
			}

			requestDispatcher.include(httpServletRequest, httpServletResponse);
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	private String _getConfigurationValue(
		FragmentEntryLink fragmentEntryLink, String name) {

		return GetterUtil.getString(
			_fragmentEntryConfigurationParser.getFieldValue(
				fragmentEntryLink.getConfiguration(),
				fragmentEntryLink.getEditableValues(),
				LocaleUtil.getMostRelevantLocale(), name));
	}

	private String _getCPInstanceFieldValue(
			HttpServletRequest httpServletRequest, CPDefinition cpDefinition,
			String field)
		throws PortalException {

		CPInstanceHelper cpInstanceHelper =
			ServletContextUtil.getCPInstanceHelper();

		try {
			CPInstance cpInstance = cpInstanceHelper.getDefaultCPInstance(
				cpDefinition.getCPDefinitionId());

			if (field.equals("availability.stockQuantity")) {
				CommerceChannel commerceChannel =
					_commerceChannelLocalService.
						fetchCommerceChannelBySiteGroupId(
							_portal.getScopeGroupId(httpServletRequest));

				CommerceCatalog commerceCatalog =
					cpDefinition.getCommerceCatalog();

				return String.valueOf(
					_commerceInventoryEngine.getStockQuantity(
						cpInstance.getCompanyId(), commerceCatalog.getGroupId(),
						commerceChannel.getGroupId(), cpInstance.getSku()));
			}
			else if (field.equals("gtin")) {
				return cpInstance.getGtin();
			}
			else if (field.equals("manufacturerPartNumber")) {
				return cpInstance.getManufacturerPartNumber();
			}
			else if (field.equals("sku")) {
				return cpInstance.getSku();
			}
		}
		catch (CPDefinitionIgnoreSKUCombinationsException
					cpDefinitionIgnoreSKUCombinationsException) {

			if (_log.isDebugEnabled()) {
				_log.debug(cpDefinitionIgnoreSKUCombinationsException);
			}
		}

		return StringPool.BLANK;
	}

	private String _getFieldLabel(
		FragmentEntryLink fragmentEntryLink, String field) {

		try {
			JSONObject configurationJSONObject = _jsonFactory.createJSONObject(
				fragmentEntryLink.getConfiguration());

			JSONArray fieldSetsJSONArray = configurationJSONObject.getJSONArray(
				"fieldSets");

			JSONArray fieldsJSONArray = fieldSetsJSONArray.getJSONObject(
				0
			).getJSONArray(
				"fields"
			);

			JSONObject typeOptionsJSONObject = fieldsJSONArray.getJSONObject(
				0
			).getJSONObject(
				"typeOptions"
			);

			JSONArray validValuesJSONArray = typeOptionsJSONObject.getJSONArray(
				"validValues");

			for (Object validValueObject : validValuesJSONArray) {
				JSONObject validValueJSONObject = (JSONObject)validValueObject;

				String value = validValueJSONObject.getString("value");

				if (value.equals(field)) {
					return validValueJSONObject.getString("label");
				}
			}
		}
		catch (JSONException jsonException) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsonException);
			}
		}

		return StringPool.BLANK;
	}

	private boolean _isEditMode(HttpServletRequest httpServletRequest) {
		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		String layoutMode = ParamUtil.getString(
			originalHttpServletRequest, "p_l_mode", Constants.VIEW);

		if (layoutMode.equals(Constants.EDIT)) {
			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DynamicFieldFragmentRenderer.class);

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceInventoryEngine _commerceInventoryEngine;

	@Reference
	private CPDefinitionInventoryLocalService
		_cpDefinitionInventoryLocalService;

	@Reference
	private FragmentEntryConfigurationParser _fragmentEntryConfigurationParser;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.product.content.web)"
	)
	private ServletContext _servletContext;

}