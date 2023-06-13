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

package com.liferay.asset.categories.admin.web.internal.portlet.action;

import com.liferay.asset.categories.admin.web.constants.AssetCategoriesAdminPortletKeys;
import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.NoSuchClassTypeException;
import com.liferay.asset.kernel.model.AssetCategoryConstants;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.model.AssetVocabularyConstants;
import com.liferay.asset.kernel.model.ClassTypeReader;
import com.liferay.asset.kernel.service.AssetVocabularyService;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.NoSuchModelException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.asset.util.AssetVocabularySettingsHelper;

import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Diego Hu
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + AssetCategoriesAdminPortletKeys.ASSET_CATEGORIES_ADMIN,
		"mvc.command.name=/asset_categories_admin/edit_asset_vocabulary"
	},
	service = MVCActionCommand.class
)
public class EditAssetVocabularyMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long vocabularyId = ParamUtil.getLong(actionRequest, "vocabularyId");

		Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "title");
		Map<Locale, String> descriptionMap =
			LocalizationUtil.getLocalizationMap(actionRequest, "description");

		AssetVocabulary vocabulary = null;

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			AssetVocabulary.class.getName(), actionRequest);

		if (vocabularyId <= 0) {

			// Add vocabulary

			int visibilityType = ParamUtil.getInteger(
				actionRequest, "visibilityType",
				AssetVocabularyConstants.VISIBILITY_TYPE_PUBLIC);

			vocabulary = _assetVocabularyService.addVocabulary(
				serviceContext.getScopeGroupId(), StringPool.BLANK, titleMap,
				descriptionMap, _getSettings(actionRequest), visibilityType,
				serviceContext);
		}
		else {

			// Update vocabulary

			vocabulary = _assetVocabularyService.updateVocabulary(
				vocabularyId, StringPool.BLANK, titleMap, descriptionMap,
				_getSettings(actionRequest), serviceContext);
		}

		actionRequest.setAttribute(
			WebKeys.REDIRECT, _getRedirectURL(actionResponse, vocabulary));
	}

	private String _getRedirectURL(
		ActionResponse actionResponse, AssetVocabulary vocabulary) {

		return PortletURLBuilder.createRenderURL(
			_portal.getLiferayPortletResponse(actionResponse)
		).setMVCPath(
			"/view.jsp"
		).setParameter(
			"vocabularyId", vocabulary.getVocabularyId()
		).buildString();
	}

	private String _getSettings(ActionRequest actionRequest) throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		int[] indexes = StringUtil.split(
			ParamUtil.getString(actionRequest, "indexes"), 0);

		long[] classNameIds = new long[indexes.length];
		long[] classTypePKs = new long[indexes.length];
		boolean[] requireds = new boolean[indexes.length];

		for (int i = 0; i < indexes.length; i++) {
			int index = indexes[i];

			classNameIds[i] = ParamUtil.getLong(
				actionRequest, "classNameId" + index);

			classTypePKs[i] = ParamUtil.getLong(
				actionRequest,
				StringBundler.concat(
					"subtype", classNameIds[i], "-classNameId", index),
				AssetCategoryConstants.ALL_CLASS_TYPE_PK);

			if (classTypePKs[i] != -1) {
				AssetRendererFactory<?> assetRendererFactory =
					AssetRendererFactoryRegistryUtil.
						getAssetRendererFactoryByClassNameId(classNameIds[i]);

				ClassTypeReader classTypeReader =
					assetRendererFactory.getClassTypeReader();

				try {
					classTypeReader.getClassType(
						classTypePKs[i], themeDisplay.getLocale());
				}
				catch (NoSuchModelException noSuchModelException) {
					throw new NoSuchClassTypeException(noSuchModelException);
				}
			}

			requireds[i] = ParamUtil.getBoolean(
				actionRequest, "required" + index);
		}

		AssetVocabularySettingsHelper vocabularySettingsHelper =
			new AssetVocabularySettingsHelper();

		vocabularySettingsHelper.setClassNameIdsAndClassTypePKs(
			classNameIds, classTypePKs, requireds);

		vocabularySettingsHelper.setMultiValued(
			ParamUtil.getBoolean(actionRequest, "multiValued"));

		return vocabularySettingsHelper.toString();
	}

	@Reference
	private AssetVocabularyService _assetVocabularyService;

	@Reference
	private Portal _portal;

}