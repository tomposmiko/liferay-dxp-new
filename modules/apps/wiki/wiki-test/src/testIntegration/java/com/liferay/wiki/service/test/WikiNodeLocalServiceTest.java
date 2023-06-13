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

package com.liferay.wiki.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ProgressTracker;
import com.liferay.portal.kernel.util.ProgressTrackerThreadLocal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.wiki.model.WikiNode;
import com.liferay.wiki.model.WikiPage;
import com.liferay.wiki.service.WikiNodeLocalServiceUtil;
import com.liferay.wiki.service.WikiPageLocalServiceUtil;
import com.liferay.wiki.util.test.WikiTestUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.Collections;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adolfo Pérez
 */
@RunWith(Arquillian.class)
public class WikiNodeLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testImportPages() throws Exception {
		_node = WikiTestUtil.addNode(TestPropsValues.getGroupId());

		ProgressTracker progressTracker = new ProgressTracker(
			StringUtil.randomString());

		ProgressTrackerThreadLocal.setProgressTracker(progressTracker);

		byte[] bytes = FileUtil.getBytes(
			getClass(),
			"/com/liferay/wiki/service/test/dependencies" +
				"/liferay_media_wiki.xml");

		InputStream is = new ByteArrayInputStream(bytes);

		WikiNodeLocalServiceUtil.importPages(
			TestPropsValues.getUserId(), _node.getNodeId(), "MediaWiki",
			new InputStream[] {is, null, null},
			Collections.<String, String[]>emptyMap());

		WikiPage importedPage = WikiPageLocalServiceUtil.fetchPage(
			_node.getNodeId(), "Liferay");

		Assert.assertNotNull(importedPage);
	}

	@Test
	public void testMediaLinks() throws Exception {
		_node = WikiTestUtil.addNode(TestPropsValues.getGroupId());

		ProgressTracker progressTracker = new ProgressTracker(
			StringUtil.randomString());

		ProgressTrackerThreadLocal.setProgressTracker(progressTracker);

		byte[] pagesBytes = FileUtil.getBytes(
			getClass(),
			"/com/liferay/wiki/service/test/dependencies/media_link_test.xml");

		InputStream pagesInputStream = new ByteArrayInputStream(pagesBytes);

		byte[] filesBytes = FileUtil.getBytes(
			getClass(),
			"/com/liferay/wiki/service/test/dependencies/media_link_test.zip");

		InputStream filesInputStream = new ByteArrayInputStream(filesBytes);

		WikiNodeLocalServiceUtil.importPages(
			TestPropsValues.getUserId(), _node.getNodeId(), "MediaWiki",
			new InputStream[] {pagesInputStream, null, filesInputStream},
			Collections.<String, String[]>emptyMap());

		WikiPage importedPage = WikiPageLocalServiceUtil.fetchPage(
			_node.getNodeId(), "Media link migration test");

		ThemeDisplay themeDisplay = new ThemeDisplay();

		Company company = CompanyLocalServiceUtil.getCompany(
			_node.getCompanyId());

		String portalURL = company.getPortalURL(_node.getGroupId());

		themeDisplay.setPortalURL(portalURL);

		WikiPage sharedImagesPage = WikiPageLocalServiceUtil.fetchPage(
			_node.getNodeId(), "SharedImages");

		long sharedImagesPageAttachmentsFolderId =
			sharedImagesPage.getAttachmentsFolderId();

		String testFileName = "media_link_test.docx";

		String linkLabel = "Download link";

		FileEntry attachmentFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				_node.getGroupId(), sharedImagesPageAttachmentsFolderId,
				testFileName);

		String attachmentFileEntryURL =
			PortletFileRepositoryUtil.getPortletFileEntryURL(
				themeDisplay, attachmentFileEntry, StringPool.BLANK);

		String linkTag = StringBundler.concat(
			"[[", attachmentFileEntryURL, StringPool.PIPE, linkLabel, "]]");

		String expectedContent = StringBundler.concat(
			"<<TableOfContents>>", StringPool.NEW_LINE, StringPool.NEW_LINE,
			linkTag);

		Assert.assertEquals(expectedContent, importedPage.getContent());
	}

	@DeleteAfterTestRun
	private WikiNode _node;

}