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

package com.liferay.journal.internal.search;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.HitsOpenSearchImpl;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.OpenSearch;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Wesley Gong
 */
@Component(service = OpenSearch.class)
public class JournalOpenSearchImpl extends HitsOpenSearchImpl {

	public static final String TITLE = "Liferay Journal Search: ";

	@Override
	public String getClassName() {
		return JournalArticle.class.getName();
	}

	@Override
	public Indexer<JournalArticle> getIndexer() {
		return IndexerRegistryUtil.getIndexer(JournalArticle.class);
	}

	@Override
	public String getSearchPath() {
		return StringPool.BLANK;
	}

	@Override
	public String getTitle(String keywords) {
		return TITLE + keywords;
	}

	@Override
	protected String getURL(
			ThemeDisplay themeDisplay, long groupId, Document result,
			PortletURL portletURL)
		throws Exception {

		AssetRendererFactory<JournalArticle> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClass(
				JournalArticle.class);

		String articleId = result.get("articleId");

		JournalArticle article = _journalArticleService.getArticle(
			groupId, articleId);

		AssetRenderer<JournalArticle> assetRenderer =
			assetRendererFactory.getAssetRenderer(article.getResourcePrimKey());

		String noSuchEntryRedirect = StringPool.BLANK;

		AssetEntry assetEntry = assetRendererFactory.getAssetEntry(
			JournalArticle.class.getName(), article.getResourcePrimKey());

		if (assetEntry != null) {
			portletURL.setParameter(
				"assetEntryId", String.valueOf(assetEntry.getEntryId()));
			portletURL.setParameter("groupId", String.valueOf(groupId));
			portletURL.setParameter("articleId", articleId);

			noSuchEntryRedirect = portletURL.toString();
		}

		return assetRenderer.getURLViewInContext(
			themeDisplay, noSuchEntryRedirect);
	}

	@Reference
	private JournalArticleService _journalArticleService;

}