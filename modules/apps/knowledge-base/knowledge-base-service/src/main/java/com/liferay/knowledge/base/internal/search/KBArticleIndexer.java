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

package com.liferay.knowledge.base.internal.search;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.KBArticleLocalService;
import com.liferay.knowledge.base.util.KnowledgeBaseUtil;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Peter Shin
 * @author Brian Wing Shun Chan
 */
@Component(service = Indexer.class)
public class KBArticleIndexer extends BaseIndexer<KBArticle> {

	public static final String CLASS_NAME = KBArticle.class.getName();

	public KBArticleIndexer() {
		setDefaultSelectedFieldNames(
			Field.COMPANY_ID, Field.CONTENT, Field.CREATE_DATE,
			Field.DESCRIPTION, Field.ENTRY_CLASS_NAME, Field.ENTRY_CLASS_PK,
			Field.MODIFIED_DATE, Field.TITLE, Field.UID, Field.USER_NAME);
		setFilterSearch(true);
		setPermissionAware(true);
	}

	@Override
	public String getClassName() {
		return CLASS_NAME;
	}

	@Override
	public boolean hasPermission(
			PermissionChecker permissionChecker, String entryClassName,
			long entryClassPK, String actionId)
		throws PortalException {

		return _kbArticleModelResourcePermission.contains(
			permissionChecker, entryClassPK, ActionKeys.VIEW);
	}

	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, BooleanFilter fullQueryBooleanFilter,
			SearchContext searchContext)
		throws Exception {

		addSearchTerm(searchQuery, searchContext, Field.CONTENT, true);
		addSearchTerm(searchQuery, searchContext, Field.DESCRIPTION, true);
		addSearchTerm(searchQuery, searchContext, Field.TITLE, true);
		addSearchTerm(searchQuery, searchContext, Field.USER_NAME, true);
	}

	@Override
	public Hits search(SearchContext searchContext) throws SearchException {
		Hits hits = super.search(searchContext);

		String[] queryTerms = ArrayUtil.append(
			GetterUtil.getStringValues(hits.getQueryTerms()),
			KnowledgeBaseUtil.splitKeywords(searchContext.getKeywords()));

		hits.setQueryTerms(queryTerms);

		return hits;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext,
			(Class<ModelDocumentContributor<KBArticle>>)
				(Class<?>)ModelDocumentContributor.class,
			"(indexer.class.name=com.liferay.knowledge.base.model.KBArticle)");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerList.close();
	}

	@Override
	protected void doDelete(KBArticle kbArticle) throws Exception {
		deleteDocument(
			kbArticle.getCompanyId(), kbArticle.getResourcePrimKey());
	}

	@Override
	protected Document doGetDocument(KBArticle kbArticle) throws Exception {
		Document document = getBaseModelDocument(CLASS_NAME, kbArticle);

		_serviceTrackerList.forEach(
			modelDocumentContributor -> modelDocumentContributor.contribute(
				document, kbArticle));

		return document;
	}

	@Override
	protected Summary doGetSummary(
		Document document, Locale locale, String snippet,
		PortletRequest portletRequest, PortletResponse portletResponse) {

		String prefix = Field.SNIPPET + StringPool.UNDERLINE;

		String title = document.get(prefix + Field.TITLE, Field.TITLE);

		String content = snippet;

		if (Validator.isNull(snippet)) {
			content = document.get(
				prefix + Field.DESCRIPTION, Field.DESCRIPTION);

			if (Validator.isNull(content)) {
				content = document.get(prefix + Field.CONTENT, Field.CONTENT);
			}
		}

		Summary summary = new Summary(title, content);

		summary.setMaxContentLength(200);

		return summary;
	}

	@Override
	protected void doReindex(KBArticle kbArticle) throws Exception {
		indexWriterHelper.updateDocument(
			kbArticle.getCompanyId(), getDocument(kbArticle));

		_reindexAttachments(kbArticle);
	}

	@Override
	protected void doReindex(String className, long classPK) throws Exception {
		KBArticle kbArticle = kbArticleLocalService.fetchLatestKBArticle(
			classPK, WorkflowConstants.STATUS_ANY);

		if (kbArticle != null) {
			_reindexKBArticles(kbArticle);

			return;
		}

		long kbArticleId = classPK;

		kbArticle = kbArticleLocalService.fetchKBArticle(kbArticleId);

		if (kbArticle != null) {
			_reindexKBArticles(kbArticle);
		}
	}

	@Override
	protected void doReindex(String[] ids) throws Exception {
		long companyId = GetterUtil.getLong(ids[0]);

		_reindexKBArticles(companyId);
	}

	@Reference
	protected IndexWriterHelper indexWriterHelper;

	@Reference
	protected KBArticleLocalService kbArticleLocalService;

	private void _reindexAttachments(KBArticle kbArticle) throws Exception {
		Indexer<DLFileEntry> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
			DLFileEntry.class);

		for (FileEntry attachmentsFileEntry :
				kbArticle.getAttachmentsFileEntries()) {

			indexer.reindex((DLFileEntry)attachmentsFileEntry.getModel());
		}
	}

	private void _reindexKBArticles(KBArticle kbArticle) throws Exception {
		List<KBArticle> kbArticles =
			kbArticleLocalService.getKBArticleAndAllDescendantKBArticles(
				kbArticle.getResourcePrimKey(),
				WorkflowConstants.STATUS_APPROVED, null);

		Collection<Document> documents = new ArrayList<>();

		for (KBArticle curKBArticle : kbArticles) {
			documents.add(getDocument(curKBArticle));
		}

		indexWriterHelper.updateDocuments(
			kbArticle.getCompanyId(), documents, isCommitImmediately());
	}

	private void _reindexKBArticles(long companyId) throws Exception {
		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			kbArticleLocalService.getIndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				Property property = PropertyFactoryUtil.forName("status");

				dynamicQuery.add(
					property.eq(WorkflowConstants.STATUS_APPROVED));
			});
		indexableActionableDynamicQuery.setCompanyId(companyId);
		indexableActionableDynamicQuery.setPerformActionMethod(
			(KBArticle kbArticle) -> {
				try {
					indexableActionableDynamicQuery.addDocuments(
						getDocument(kbArticle));
				}
				catch (PortalException portalException) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							"Unable to index knowledge base article " +
								kbArticle.getKbArticleId(),
							portalException);
					}
				}
			});

		indexableActionableDynamicQuery.performActions();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		KBArticleIndexer.class);

	@Reference(
		target = "(model.class.name=com.liferay.knowledge.base.model.KBArticle)"
	)
	private ModelResourcePermission<KBArticle>
		_kbArticleModelResourcePermission;

	private ServiceTrackerList<ModelDocumentContributor<KBArticle>>
		_serviceTrackerList;

}