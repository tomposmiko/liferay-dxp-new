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

package com.liferay.knowledge.base.web.internal.layout.display.page;

import com.liferay.info.item.InfoItemReference;
import com.liferay.knowledge.base.constants.KBFolderConstants;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.KBArticleLocalService;
import com.liferay.layout.display.page.LayoutDisplayPageObjectProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.constants.FriendlyURLResolverConstants;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(service = LayoutDisplayPageProvider.class)
public class KBArticleLayoutDisplayPageProvider
	implements LayoutDisplayPageProvider<KBArticle> {

	@Override
	public String getClassName() {
		return KBArticle.class.getName();
	}

	@Override
	public LayoutDisplayPageObjectProvider<KBArticle>
		getLayoutDisplayPageObjectProvider(
			InfoItemReference infoItemReference) {

		try {
			KBArticle kbArticle = _kbArticleLocalService.fetchKBArticle(
				infoItemReference.getClassPK());

			if ((kbArticle == null) || kbArticle.isDraft()) {
				return null;
			}

			return new KBArticleLayoutDisplayPageObjectProvider(kbArticle);
		}
		catch (PortalException portalException) {
			throw new RuntimeException(portalException);
		}
	}

	@Override
	public LayoutDisplayPageObjectProvider<KBArticle>
		getLayoutDisplayPageObjectProvider(long groupId, String urlTitle) {

		try {
			List<String> parts = StringUtil.split(urlTitle, CharPool.SLASH);

			KBArticle kbArticle =
				_kbArticleLocalService.fetchKBArticleByUrlTitle(
					groupId, _getKBFolderId(groupId, parts),
					parts.get(parts.size() - 1));

			if (kbArticle == null) {
				return null;
			}

			return new KBArticleLayoutDisplayPageObjectProvider(kbArticle);
		}
		catch (PortalException portalException) {
			throw new RuntimeException(portalException);
		}
	}

	@Override
	public String getURLSeparator() {
		return FriendlyURLResolverConstants.
			URL_SEPARATOR_KNOWLEDGE_BASE_ARTICLE;
	}

	private long _getKBFolderId(long groupId, List<String> urlTitleParts) {
		if (urlTitleParts.size() == 1) {
			return KBFolderConstants.DEFAULT_PARENT_FOLDER_ID;
		}

		KBArticle kbArticle = _kbArticleLocalService.fetchKBArticleByUrlTitle(
			groupId, urlTitleParts.get(0), urlTitleParts.get(1));

		if (kbArticle != null) {
			return kbArticle.getKbFolderId();
		}

		return KBFolderConstants.DEFAULT_PARENT_FOLDER_ID;
	}

	@Reference
	private KBArticleLocalService _kbArticleLocalService;

}