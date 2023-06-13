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

package com.liferay.portal.workflow.kaleo.internal.search.spi.model.index.contributor;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.liferay.portal.workflow.kaleo.internal.search.KaleoTaskInstanceTokenField;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskInstanceToken;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.liferay.portal.workflow.kaleo.model.KaleoTaskInstanceToken",
	service = ModelDocumentContributor.class
)
public class KaleoTaskInstanceTokenModelDocumentContributor
	implements ModelDocumentContributor<KaleoTaskInstanceToken> {

	@Override
	public void contribute(
		Document document, KaleoTaskInstanceToken kaleoTaskInstanceToken) {

		List<KaleoTaskAssignmentInstance> kaleoTaskAssignmentInstances =
			kaleoTaskInstanceToken.getKaleoTaskAssignmentInstances();

		Set<Long> assigneeClassNameIds = new HashSet<>();
		Set<Long> assigneeClassPKs = new HashSet<>();
		Set<Long> assigneeGroupIds = new HashSet<>();

		for (KaleoTaskAssignmentInstance kaleoTaskAssignmentInstance :
				kaleoTaskAssignmentInstances) {

			assigneeClassNameIds.add(
				portal.getClassNameId(
					kaleoTaskAssignmentInstance.getAssigneeClassName()));
			assigneeClassPKs.add(
				kaleoTaskAssignmentInstance.getAssigneeClassPK());
			assigneeGroupIds.add(kaleoTaskAssignmentInstance.getGroupId());
		}

		document.addKeyword(
			KaleoTaskInstanceTokenField.ASSIGNEE_CLASS_NAME_IDS,
			assigneeClassNameIds.toArray(
				new Long[assigneeClassNameIds.size()]));
		document.addKeyword(
			KaleoTaskInstanceTokenField.ASSIGNEE_CLASS_PKS,
			assigneeClassPKs.toArray(new Long[assigneeClassPKs.size()]));
		document.addKeyword(
			KaleoTaskInstanceTokenField.ASSIGNEE_GROUP_IDS,
			assigneeGroupIds.toArray(new Long[assigneeGroupIds.size()]));
		document.addKeyword(
			KaleoTaskInstanceTokenField.COMPLETED,
			kaleoTaskInstanceToken.isCompleted());
		document.addKeyword(
			KaleoTaskInstanceTokenField.CLASS_NAME,
			kaleoTaskInstanceToken.getClassName());
		document.addKeyword(
			Field.CLASS_PK, kaleoTaskInstanceToken.getClassPK());
		document.addDate(
			KaleoTaskInstanceTokenField.DUE_DATE,
			kaleoTaskInstanceToken.getDueDate());
		document.addKeyword(
			KaleoTaskInstanceTokenField.KALEO_INSTANCE_ID,
			kaleoTaskInstanceToken.getKaleoInstanceId());
		document.addKeyword(
			KaleoTaskInstanceTokenField.TASK_NAME,
			kaleoTaskInstanceToken.getKaleoTaskName());

		AssetEntry assetEntry = getAssetEntry(kaleoTaskInstanceToken);

		if (assetEntry == null) {
			return;
		}

		document.addKeyword(
			KaleoTaskInstanceTokenField.ASSET_CLASS_NAME_ID,
			assetEntry.getClassNameId());
		document.addKeyword(
			KaleoTaskInstanceTokenField.ASSET_CLASS_PK,
			assetEntry.getClassPK());

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		String siteDefaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		String[] titleLanguageIds = getLanguageIds(
			siteDefaultLanguageId, assetEntry.getTitle());

		for (String titleLanguageId : titleLanguageIds) {
			document.addText(
				LocalizationUtil.getLocalizedName(
					KaleoTaskInstanceTokenField.ASSET_TITLE, titleLanguageId),
				assetEntry.getTitle(titleLanguageId));
		}

		String[] descriptionLanguageIds = getLanguageIds(
			siteDefaultLanguageId, assetEntry.getDescription());

		for (String descriptionLanguageId : descriptionLanguageIds) {
			document.addText(
				LocalizationUtil.getLocalizedName(
					KaleoTaskInstanceTokenField.ASSET_DESCRIPTION,
					descriptionLanguageId),
				assetEntry.getDescription(descriptionLanguageId));
		}
	}

	protected AssetEntry getAssetEntry(
		KaleoTaskInstanceToken kaleoTaskInstanceToken) {

		try {
			AssetRendererFactory<?> assetRendererFactory =
				getAssetRendererFactory(kaleoTaskInstanceToken.getClassName());

			AssetRenderer<?> assetRenderer =
				assetRendererFactory.getAssetRenderer(
					kaleoTaskInstanceToken.getClassPK());

			return assetEntryLocalService.getEntry(
				assetRenderer.getClassName(), assetRenderer.getClassPK());
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}
		}

		return null;
	}

	protected AssetRendererFactory<?> getAssetRendererFactory(
		String className) {

		return AssetRendererFactoryRegistryUtil.
			getAssetRendererFactoryByClassName(className);
	}

	protected String[] getLanguageIds(
		String defaultLanguageId, String content) {

		String[] languageIds = LocalizationUtil.getAvailableLanguageIds(
			content);

		if (languageIds.length == 0) {
			languageIds = new String[] {defaultLanguageId};
		}

		return languageIds;
	}

	@Reference
	protected AssetEntryLocalService assetEntryLocalService;

	@Reference
	protected ClassNameLocalService classNameLocalService;

	@Reference
	protected Portal portal;

	private static final Log _log = LogFactoryUtil.getLog(
		KaleoTaskInstanceTokenModelDocumentContributor.class);

}