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

package com.liferay.layout.admin.web.internal.handler;

import com.liferay.asset.kernel.exception.AssetCategoryException;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.LayoutNameException;
import com.liferay.portal.kernel.exception.LayoutTypeException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTypeController;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.LayoutTypeControllerTracker;

import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jürgen Kappler
 */
@Component(immediate = true, service = LayoutExceptionRequestHandler.class)
public class LayoutExceptionRequestHandler {

	public void handlePortalException(
			ActionRequest actionRequest, ActionResponse actionResponse,
			PortalException portalException)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String errorMessage = null;

		if (portalException instanceof AssetCategoryException) {
			AssetCategoryException assetCategoryException =
				(AssetCategoryException)portalException;

			AssetVocabulary assetVocabulary =
				assetCategoryException.getVocabulary();

			String assetVocabularyTitle = StringPool.BLANK;

			if (assetVocabulary != null) {
				assetVocabularyTitle = assetVocabulary.getTitle(
					themeDisplay.getLocale());
			}

			if (assetCategoryException.getType() ==
					AssetCategoryException.AT_LEAST_ONE_CATEGORY) {

				errorMessage = LanguageUtil.format(
					themeDisplay.getRequest(),
					"please-select-at-least-one-category-for-x",
					assetVocabularyTitle);
			}
			else if (assetCategoryException.getType() ==
						AssetCategoryException.TOO_MANY_CATEGORIES) {

				errorMessage = LanguageUtil.format(
					themeDisplay.getRequest(),
					"you-cannot-select-more-than-one-category-for-x",
					assetVocabularyTitle);
			}
		}
		else if (portalException instanceof LayoutNameException) {
			LayoutNameException layoutNameException =
				(LayoutNameException)portalException;

			if (layoutNameException.getType() == LayoutNameException.TOO_LONG) {
				errorMessage = LanguageUtil.format(
					themeDisplay.getRequest(),
					"page-name-cannot-exceed-x-characters",
					ModelHintsUtil.getMaxLength(
						Layout.class.getName(), "friendlyURL"));
			}
			else {
				errorMessage = LanguageUtil.get(
					themeDisplay.getRequest(),
					"please-enter-a-valid-name-for-the-page");
			}
		}
		else if (portalException instanceof LayoutTypeException) {
			LayoutTypeException layoutTypeException =
				(LayoutTypeException)portalException;

			if ((layoutTypeException.getType() ==
					LayoutTypeException.FIRST_LAYOUT) ||
				(layoutTypeException.getType() ==
					LayoutTypeException.NOT_INSTANCEABLE)) {

				errorMessage = _handleLayoutTypeException(
					actionRequest, layoutTypeException.getType());
			}
		}

		if (Validator.isNull(errorMessage)) {
			errorMessage = LanguageUtil.get(
				themeDisplay.getRequest(), "an-unexpected-error-occurred");
		}

		JSONObject jsonObject = JSONUtil.put("errorMessage", errorMessage);

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private String _handleLayoutTypeException(
		ActionRequest actionRequest, int exceptionType) {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String errorMessage = "pages-of-type-x-cannot-be-selected";

		if (exceptionType == LayoutTypeException.FIRST_LAYOUT) {
			errorMessage = "the-first-page-cannot-be-of-type-x";
		}

		String type = ParamUtil.getString(actionRequest, "type");

		LayoutTypeController layoutTypeController =
			LayoutTypeControllerTracker.getLayoutTypeController(type);

		ResourceBundle layoutTypeResourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", themeDisplay.getLocale(),
			layoutTypeController.getClass());

		String layoutTypeName = LanguageUtil.get(
			layoutTypeResourceBundle, "layout.types." + type);

		return LanguageUtil.format(
			themeDisplay.getRequest(), errorMessage, layoutTypeName);
	}

}