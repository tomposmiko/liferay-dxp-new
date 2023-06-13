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

package com.liferay.document.library.internal.search;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.RelatedSearchResult;
import com.liferay.portal.kernel.search.SearchResult;
import com.liferay.portal.kernel.search.SearchResultManager;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.SummaryFactory;
import com.liferay.portal.kernel.search.result.SearchResultContributor;
import com.liferay.portal.kernel.search.result.SearchResultTranslator;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.internal.result.SearchResultManagerImpl;
import com.liferay.portal.search.internal.result.SearchResultTranslatorImpl;
import com.liferay.portal.search.internal.result.SummaryFactoryImpl;
import com.liferay.portal.search.test.util.BaseSearchResultUtilTestCase;
import com.liferay.portal.search.test.util.SearchTestUtil;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author André de Oliveira
 */
public class SearchResultUtilDLFileEntryTest
	extends BaseSearchResultUtilTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@After
	public void tearDown() {
		_assetRendererFactoryRegistryUtilMockedStatic.close();
	}

	@Test
	public void testDLFileEntry() throws Exception {
		SearchResult searchResult = assertOneSearchResult(
			SearchTestUtil.createDocument(_CLASS_NAME_DL_FILE_ENTRY));

		Assert.assertEquals(
			_CLASS_NAME_DL_FILE_ENTRY, searchResult.getClassName());
		Assert.assertEquals(
			SearchTestUtil.ENTRY_CLASS_PK, searchResult.getClassPK());

		assertEmptyFileEntryRelatedSearchResults(searchResult);

		Assert.assertNull(searchResult.getSummary());

		Mockito.verifyNoInteractions(_dlAppLocalService);

		assertEmptyCommentRelatedSearchResults(searchResult);
		assertEmptyVersions(searchResult);
	}

	@Test
	public void testDLFileEntryAttachment() throws Exception {
		Mockito.when(
			_assetRenderer.getSearchSummary(Mockito.any())
		).thenReturn(
			SearchTestUtil.SUMMARY_CONTENT
		);

		Mockito.when(
			_assetRenderer.getTitle((Locale)Mockito.any())
		).thenReturn(
			SearchTestUtil.SUMMARY_TITLE
		);

		Mockito.when(
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				Mockito.anyString())
		).thenAnswer(
			invocation -> {
				String className = invocation.getArgument(0);

				if (_CLASS_NAME_DL_FILE_ENTRY.equals(className)) {
					return null;
				}

				if (SearchTestUtil.ATTACHMENT_OWNER_CLASS_NAME.equals(
						className)) {

					return _assetRendererFactory;
				}

				throw new IllegalArgumentException();
			}
		);

		Mockito.doReturn(
			_assetRenderer
		).when(
			_assetRendererFactory
		).getAssetRenderer(
			SearchTestUtil.ATTACHMENT_OWNER_CLASS_PK
		);

		Mockito.when(
			_dlAppLocalService.getFileEntry(SearchTestUtil.ENTRY_CLASS_PK)
		).thenReturn(
			_fileEntry
		);

		Mockito.doThrow(
			new IllegalArgumentException()
		).when(
			_indexerRegistry
		).getIndexer(
			Mockito.anyString()
		);

		Mockito.doReturn(
			_indexer
		).when(
			_indexerRegistry
		).getIndexer(
			_CLASS_NAME_DL_FILE_ENTRY
		);

		Mockito.doReturn(
			null
		).when(
			_indexerRegistry
		).getIndexer(
			SearchTestUtil.ATTACHMENT_OWNER_CLASS_NAME
		);

		String title = RandomTestUtil.randomString();
		String content = RandomTestUtil.randomString();

		Summary summary = new Summary(null, title, content);

		Mockito.doReturn(
			summary
		).when(
			_indexer
		).getSummary(
			Mockito.any(), Mockito.anyString(), Mockito.isNull(),
			Mockito.isNull()
		);

		SearchResult searchResult = assertOneSearchResult(
			SearchTestUtil.createAttachmentDocument(_CLASS_NAME_DL_FILE_ENTRY));

		Assert.assertEquals(
			SearchTestUtil.ATTACHMENT_OWNER_CLASS_NAME,
			searchResult.getClassName());
		Assert.assertEquals(
			SearchTestUtil.ATTACHMENT_OWNER_CLASS_PK,
			searchResult.getClassPK());

		Summary searchResultSummary = searchResult.getSummary();

		Assert.assertNotSame(summary, searchResultSummary);
		Assert.assertEquals(
			SearchTestUtil.SUMMARY_CONTENT, searchResultSummary.getContent());
		Assert.assertEquals(
			SearchTestUtil.SUMMARY_TITLE, searchResultSummary.getTitle());

		List<RelatedSearchResult<FileEntry>> relatedSearchResults =
			searchResult.getFileEntryRelatedSearchResults();

		Assert.assertEquals(
			relatedSearchResults.toString(), 1, relatedSearchResults.size());

		RelatedSearchResult<FileEntry> relatedSearchResult =
			relatedSearchResults.get(0);

		FileEntry relatedSearchResultFileEntry = relatedSearchResult.getModel();

		Assert.assertSame(_fileEntry, relatedSearchResultFileEntry);

		Summary relatedSearchResultSummary = relatedSearchResult.getSummary();

		Assert.assertSame(summary, relatedSearchResultSummary);
		Assert.assertEquals(content, relatedSearchResultSummary.getContent());
		Assert.assertEquals(title, relatedSearchResultSummary.getTitle());

		assertEmptyCommentRelatedSearchResults(searchResult);
		assertEmptyVersions(searchResult);
	}

	@Test
	public void testDLFileEntryMissing() throws Exception {
		Mockito.when(
			_dlAppLocalService.getFileEntry(SearchTestUtil.ENTRY_CLASS_PK)
		).thenReturn(
			null
		);

		SearchResult searchResult = assertOneSearchResult(
			SearchTestUtil.createAttachmentDocument(_CLASS_NAME_DL_FILE_ENTRY));

		Assert.assertEquals(
			SearchTestUtil.ATTACHMENT_OWNER_CLASS_NAME,
			searchResult.getClassName());
		Assert.assertEquals(
			SearchTestUtil.ATTACHMENT_OWNER_CLASS_PK,
			searchResult.getClassPK());

		assertEmptyFileEntryRelatedSearchResults(searchResult);

		Mockito.verify(
			_dlAppLocalService
		).getFileEntry(
			SearchTestUtil.ENTRY_CLASS_PK
		);

		Assert.assertNull(searchResult.getSummary());

		Mockito.verify(
			_indexerRegistry
		).getIndexer(
			SearchTestUtil.ATTACHMENT_OWNER_CLASS_NAME
		);

		AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
			SearchTestUtil.ATTACHMENT_OWNER_CLASS_NAME);

		_assetRendererFactoryRegistryUtilMockedStatic.verify(
			() ->
				AssetRendererFactoryRegistryUtil.
					getAssetRendererFactoryByClassName(
						SearchTestUtil.ATTACHMENT_OWNER_CLASS_NAME),
			Mockito.atLeastOnce());

		assertEmptyCommentRelatedSearchResults(searchResult);
		assertEmptyVersions(searchResult);
	}

	@Test
	public void testDLFileEntryWithBrokenIndexer() throws Exception {
		Mockito.when(
			_dlAppLocalService.getFileEntry(SearchTestUtil.ENTRY_CLASS_PK)
		).thenReturn(
			_fileEntry
		);

		Mockito.doThrow(
			new IllegalArgumentException()
		).when(
			_indexer
		).getSummary(
			Mockito.any(), Mockito.anyString(), Mockito.any(), Mockito.any()
		);

		Mockito.when(
			_indexerRegistry.getIndexer(Mockito.anyString())
		).thenReturn(
			_indexer
		);

		Document document = SearchTestUtil.createAttachmentDocument(
			_CLASS_NAME_DL_FILE_ENTRY);

		String snippet = RandomTestUtil.randomString();

		document.add(new Field(Field.SNIPPET, snippet));

		try (LogCapture logCapture = LoggerTestUtil.configureJDKLogger(
				SearchResultTranslatorImpl.class.getName(), Level.WARNING)) {

			SearchResult searchResult = assertOneSearchResult(document);

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 1, logEntries.size());

			LogEntry logEntry = logEntries.get(0);

			long entryClassPK = GetterUtil.getLong(
				document.get(Field.ENTRY_CLASS_PK));

			Assert.assertEquals(
				"Search index is stale and contains entry {" + entryClassPK +
					"}",
				logEntry.getMessage());

			Assert.assertEquals(
				SearchTestUtil.ATTACHMENT_OWNER_CLASS_NAME,
				searchResult.getClassName());
			Assert.assertEquals(
				SearchTestUtil.ATTACHMENT_OWNER_CLASS_PK,
				searchResult.getClassPK());
			Assert.assertNull(searchResult.getSummary());

			Mockito.verify(
				_indexerRegistry
			).getIndexer(
				_CLASS_NAME_DL_FILE_ENTRY
			);

			Mockito.verify(
				_indexer
			).getSummary(
				document, snippet, null, null
			);

			assertEmptyFileEntryRelatedSearchResults(searchResult);

			Mockito.verify(
				_dlAppLocalService
			).getFileEntry(
				SearchTestUtil.ENTRY_CLASS_PK
			);

			assertEmptyCommentRelatedSearchResults(searchResult);
			assertEmptyVersions(searchResult);
		}
	}

	@Override
	protected SearchResultTranslator createSearchResultTranslator() {
		SearchResultTranslatorImpl searchResultTranslatorImpl =
			new SearchResultTranslatorImpl();

		searchResultTranslatorImpl.setSearchResultManager(
			_createSearchResultManager());

		return searchResultTranslatorImpl;
	}

	private SearchResultContributor _createSearchResultContributor() {
		DLFileEntrySearchResultContributor dlFileEntrySearchResultContributor =
			new DLFileEntrySearchResultContributor();

		dlFileEntrySearchResultContributor.setClassNameLocalService(
			classNameLocalService);
		dlFileEntrySearchResultContributor.setDLAppLocalService(
			_dlAppLocalService);
		dlFileEntrySearchResultContributor.setSummaryFactory(
			_createSummaryFactory());

		return dlFileEntrySearchResultContributor;
	}

	private SearchResultManager _createSearchResultManager() {
		SearchResultManagerImpl searchResultManagerImpl =
			new SearchResultManagerImpl();

		searchResultManagerImpl.addSearchResultContributor(
			_createSearchResultContributor());
		searchResultManagerImpl.setClassNameLocalService(classNameLocalService);
		searchResultManagerImpl.setSummaryFactory(_createSummaryFactory());

		return searchResultManagerImpl;
	}

	private SummaryFactory _createSummaryFactory() {
		SummaryFactoryImpl summaryFactoryImpl = new SummaryFactoryImpl();

		summaryFactoryImpl.setIndexerRegistry(_indexerRegistry);

		return summaryFactoryImpl;
	}

	private static final String _CLASS_NAME_DL_FILE_ENTRY =
		DLFileEntry.class.getName();

	@SuppressWarnings("rawtypes")
	private AssetRenderer _assetRenderer = Mockito.mock(AssetRenderer.class);

	private final AssetRendererFactory<?> _assetRendererFactory = Mockito.mock(
		AssetRendererFactory.class);
	private final MockedStatic<AssetRendererFactoryRegistryUtil>
		_assetRendererFactoryRegistryUtilMockedStatic = Mockito.mockStatic(
			AssetRendererFactoryRegistryUtil.class);
	private final DLAppLocalService _dlAppLocalService = Mockito.mock(
		DLAppLocalService.class);
	private final FileEntry _fileEntry = Mockito.mock(FileEntry.class);
	private final Indexer<Object> _indexer = Mockito.mock(Indexer.class);
	private final IndexerRegistry _indexerRegistry = Mockito.mock(
		IndexerRegistry.class);

}