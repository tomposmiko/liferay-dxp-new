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

package com.liferay.layout.page.template.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.exception.LayoutPageTemplateEntryNameException;
import com.liferay.layout.page.template.model.LayoutPageTemplateCollection;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryService;
import com.liferay.layout.page.template.service.persistence.LayoutPageTemplateEntryPersistence;
import com.liferay.layout.page.template.service.persistence.impl.constants.LayoutPersistenceConstants;
import com.liferay.layout.page.template.service.test.util.LayoutPageTemplateTestUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutPrototype;
import com.liferay.portal.kernel.model.Repository;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.service.LayoutPrototypeLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jürgen Kappler
 */
@RunWith(Arquillian.class)
public class LayoutPageTemplateEntryServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE,
			PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED,
				LayoutPersistenceConstants.BUNDLE_SYMBOLIC_NAME));

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_layoutPageTemplateCollection =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateCollection(
				_group.getGroupId());
	}

	@Test(
		expected = LayoutPageTemplateEntryNameException.MustNotBeDuplicate.class
	)
	public void testAddDuplicateLayoutPageTemplateEntries() throws Exception {
		String name = RandomTestUtil.randomString();

		LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			name);

		LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			name);
	}

	@Test(expected = LayoutPageTemplateEntryNameException.class)
	public void testAddLayoutPageEntryWithNullName() throws Exception {
		LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			null);
	}

	@Test
	public void testAddLayoutPageTemplateEntriesOfDifferentTypesWithTheSameName()
		throws Exception {

		String name = RandomTestUtil.randomString();

		LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			name, LayoutPageTemplateEntryTypeConstants.TYPE_BASIC,
			WorkflowConstants.STATUS_APPROVED);
		LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			name, LayoutPageTemplateEntryTypeConstants.TYPE_WIDGET_PAGE,
			WorkflowConstants.STATUS_APPROVED);
	}

	@Test
	public void testAddLayoutPageTemplateEntry() throws PortalException {
		String name = RandomTestUtil.randomString();

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				name);

		LayoutPageTemplateEntry persistedLayoutPageTemplateEntry =
			_layoutPageTemplateEntryPersistence.fetchByPrimaryKey(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertEquals(name, persistedLayoutPageTemplateEntry.getName());
	}

	@Test
	public void testAddLayoutPageTemplateEntryByTypeAndStatus()
		throws PortalException {

		String name = RandomTestUtil.randomString();

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				name, LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE,
				WorkflowConstants.STATUS_DRAFT);

		Assert.assertEquals(name, layoutPageTemplateEntry.getName());
		Assert.assertEquals(
			LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE,
			layoutPageTemplateEntry.getType());
		Assert.assertEquals(
			WorkflowConstants.STATUS_DRAFT,
			layoutPageTemplateEntry.getStatus());
	}

	@Test(expected = LayoutPageTemplateEntryNameException.class)
	public void testAddLayoutPageTemplateEntryWithEmptyName() throws Exception {
		LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			StringPool.BLANK);
	}

	@Test
	public void testAddLayoutPageTemplateEntryWithLayoutPrototype()
		throws PortalException {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				RandomTestUtil.randomString(),
				LayoutPageTemplateEntryTypeConstants.TYPE_WIDGET_PAGE,
				WorkflowConstants.STATUS_APPROVED);

		LayoutPageTemplateEntry persistedLayoutPageTemplateEntry =
			_layoutPageTemplateEntryPersistence.fetchByPrimaryKey(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertEquals(
			layoutPageTemplateEntry.getName(),
			persistedLayoutPageTemplateEntry.getName());

		Assert.assertNotEquals(
			0, layoutPageTemplateEntry.getLayoutPrototypeId());

		LayoutPrototype layoutPrototype =
			_layoutPrototypeLocalService.fetchLayoutPrototype(
				layoutPageTemplateEntry.getLayoutPrototypeId());

		Assert.assertNotNull(layoutPrototype);
		Assert.assertEquals(
			layoutPageTemplateEntry.getName(),
			layoutPrototype.getName(LocaleUtil.getMostRelevantLocale()));
	}

	@Test(expected = LayoutPageTemplateEntryNameException.class)
	public void testAddLayoutPageTemplateEntryWithSymbolInName()
		throws Exception {

		LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			"Test %&# Name");
	}

	@Test
	public void testAddLayoutPageTemplateEntryWithUTF8CharsInName()
		throws Exception {

		LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			"你好andこんにちは");
	}

	@Test
	public void testAddMultipleLayoutPageTemplateEntries()
		throws PortalException {

		List<LayoutPageTemplateEntry> originalLayoutPageTemplateEntries =
			_layoutPageTemplateEntryPersistence.findByG_L(
				_layoutPageTemplateCollection.getGroupId(),
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId());

		LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId());

		LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId());

		List<LayoutPageTemplateEntry> actualLayoutPageTemplateEntries =
			_layoutPageTemplateEntryPersistence.findByG_L(
				_layoutPageTemplateCollection.getGroupId(),
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId());

		Assert.assertEquals(
			actualLayoutPageTemplateEntries.toString(),
			originalLayoutPageTemplateEntries.size() + 2,
			actualLayoutPageTemplateEntries.size());
	}

	@Test
	public void testCopyLayoutPageTemplateEntry() throws Exception {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId());

		LayoutPageTemplateEntry copiedLayoutPageTemplateEntry =
			_layoutPageTemplateEntryService.copyLayoutPageTemplateEntry(
				_group.getGroupId(),
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			layoutPageTemplateEntry.getGroupId(),
			copiedLayoutPageTemplateEntry.getGroupId());
		Assert.assertEquals(
			layoutPageTemplateEntry.getCompanyId(),
			copiedLayoutPageTemplateEntry.getCompanyId());
		Assert.assertEquals(
			layoutPageTemplateEntry.getLayoutPageTemplateCollectionId(),
			copiedLayoutPageTemplateEntry.getLayoutPageTemplateCollectionId());
		Assert.assertNotEquals(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryKey(),
			copiedLayoutPageTemplateEntry.getLayoutPageTemplateEntryKey());
		Assert.assertEquals(
			layoutPageTemplateEntry.getClassNameId(),
			copiedLayoutPageTemplateEntry.getClassNameId());
		Assert.assertEquals(
			layoutPageTemplateEntry.getClassTypeId(),
			copiedLayoutPageTemplateEntry.getClassTypeId());
		Assert.assertTrue(
			StringUtil.startsWith(
				copiedLayoutPageTemplateEntry.getName(),
				StringBundler.concat(
					layoutPageTemplateEntry.getName(), " (",
					_language.get(LocaleUtil.getDefault(), "copy"), ")")));
		Assert.assertEquals(
			layoutPageTemplateEntry.getType(),
			copiedLayoutPageTemplateEntry.getType());
		Assert.assertEquals(
			0, copiedLayoutPageTemplateEntry.getPreviewFileEntryId());
		Assert.assertNotEquals(
			layoutPageTemplateEntry.getPlid(),
			copiedLayoutPageTemplateEntry.getPlid());
	}

	@Test
	public void testCopyLayoutPageTemplateEntryWithPreviewFileEntry()
		throws Exception {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId());

		Repository repository = _portletFileRepository.addPortletRepository(
			_group.getGroupId(), LayoutAdminPortletKeys.GROUP_PAGES,
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		FileEntry fileEntry = _portletFileRepository.addPortletFileEntry(
			_group.getGroupId(), TestPropsValues.getUserId(),
			FragmentCollection.class.getName(),
			_layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			LayoutAdminPortletKeys.GROUP_PAGES, repository.getDlFolderId(),
			new byte[0], "test.png", ContentTypes.IMAGE_PNG, false);

		layoutPageTemplateEntry =
			_layoutPageTemplateEntryService.updateLayoutPageTemplateEntry(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				fileEntry.getFileEntryId());

		LayoutPageTemplateEntry copiedLayoutPageTemplateEntry =
			_layoutPageTemplateEntryService.copyLayoutPageTemplateEntry(
				_group.getGroupId(),
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertNotEquals(
			layoutPageTemplateEntry.getPreviewFileEntryId(),
			copiedLayoutPageTemplateEntry.getPreviewFileEntryId());

		FileEntry copiedFileEntry = _portletFileRepository.getPortletFileEntry(
			copiedLayoutPageTemplateEntry.getPreviewFileEntryId());

		Assert.assertEquals(
			copiedLayoutPageTemplateEntry.getLayoutPageTemplateEntryId() +
				"_preview.png",
			copiedFileEntry.getFileName());
		Assert.assertEquals(
			fileEntry.getExtension(), copiedFileEntry.getExtension());

		FileVersion copiedFileVersion = copiedFileEntry.getFileVersion();

		Assert.assertEquals(
			WorkflowConstants.STATUS_APPROVED, copiedFileVersion.getStatus());
	}

	@Test
	public void testDeleteLayoutPageTemplateEntries() throws Exception {
		LayoutPageTemplateEntry layoutPageTemplateEntry1 =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId());

		LayoutPageTemplateEntry layoutPageTemplateEntry2 =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId());

		_layoutPageTemplateEntryService.deleteLayoutPageTemplateEntries(
			new long[] {
				layoutPageTemplateEntry1.getLayoutPageTemplateEntryId(),
				layoutPageTemplateEntry2.getLayoutPageTemplateEntryId()
			});

		Assert.assertNull(
			_layoutPageTemplateEntryPersistence.fetchByPrimaryKey(
				layoutPageTemplateEntry1.getLayoutPageTemplateEntryId()));

		Assert.assertNull(
			_layoutPageTemplateEntryPersistence.fetchByPrimaryKey(
				layoutPageTemplateEntry2.getLayoutPageTemplateEntryId()));
	}

	@Test
	public void testDeleteLayoutPageTemplateEntry() throws Exception {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId());

		_layoutPageTemplateEntryService.deleteLayoutPageTemplateEntry(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertNull(
			_layoutPageTemplateEntryPersistence.fetchByPrimaryKey(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId()));
	}

	@Test
	public void testDeleteLayoutPageTemplateEntryWithLayoutPrototype()
		throws Exception {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				RandomTestUtil.randomString(),
				LayoutPageTemplateEntryTypeConstants.TYPE_WIDGET_PAGE,
				WorkflowConstants.STATUS_APPROVED);

		_layoutPageTemplateEntryService.deleteLayoutPageTemplateEntry(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertNull(
			_layoutPageTemplateEntryPersistence.fetchByPrimaryKey(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId()));
		Assert.assertNull(
			_layoutPrototypeLocalService.fetchLayoutPrototype(
				layoutPageTemplateEntry.getLayoutPrototypeId()));
	}

	@Test
	public void testUpdateLayoutPageTemplateEntryDefaultTemplate()
		throws PortalException {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId());

		_layoutPageTemplateEntryService.updateLayoutPageTemplateEntry(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(), true);

		LayoutPageTemplateEntry persistedLayoutPageTemplateEntry =
			_layoutPageTemplateEntryPersistence.fetchByPrimaryKey(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertTrue(persistedLayoutPageTemplateEntry.isDefaultTemplate());
	}

	@Test
	public void testUpdateLayoutPageTemplateEntryName() throws PortalException {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				"tiger");

		LayoutPageTemplateEntry persistedLayoutPageTemplateEntry =
			_layoutPageTemplateEntryPersistence.fetchByPrimaryKey(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertEquals(
			"tiger",
			persistedLayoutPageTemplateEntry.getLayoutPageTemplateEntryKey());
		Assert.assertEquals(
			"tiger", persistedLayoutPageTemplateEntry.getName());

		_layoutPageTemplateEntryService.updateLayoutPageTemplateEntry(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(), "leopard");

		Assert.assertEquals(
			"leopard",
			persistedLayoutPageTemplateEntry.getLayoutPageTemplateEntryKey());
		Assert.assertEquals(
			"leopard", persistedLayoutPageTemplateEntry.getName());
	}

	@Test
	public void testUpdateLayoutPageTemplateEntryStatus()
		throws PortalException {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateTestUtil.addLayoutPageTemplateEntry(
				_layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId());

		layoutPageTemplateEntry = _layoutPageTemplateEntryService.updateStatus(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
			WorkflowConstants.STATUS_PENDING);

		LayoutPageTemplateEntry persistedLayoutPageTemplateEntry =
			_layoutPageTemplateEntryPersistence.fetchByPrimaryKey(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertEquals(
			WorkflowConstants.STATUS_PENDING,
			persistedLayoutPageTemplateEntry.getStatus());
	}

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private Language _language;

	private LayoutPageTemplateCollection _layoutPageTemplateCollection;

	@Inject
	private LayoutPageTemplateEntryPersistence
		_layoutPageTemplateEntryPersistence;

	@Inject
	private LayoutPageTemplateEntryService _layoutPageTemplateEntryService;

	@Inject
	private LayoutPrototypeLocalService _layoutPrototypeLocalService;

	@Inject
	private PortletFileRepository _portletFileRepository;

}