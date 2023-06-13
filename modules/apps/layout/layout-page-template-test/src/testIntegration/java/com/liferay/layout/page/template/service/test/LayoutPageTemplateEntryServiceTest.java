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
import com.liferay.asset.display.page.model.AssetDisplayPageEntry;
import com.liferay.asset.display.page.service.AssetDisplayPageEntryLocalServiceUtil;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentCollectionServiceUtil;
import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;
import com.liferay.fragment.service.FragmentEntryServiceUtil;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.exception.DuplicateLayoutPageTemplateEntryException;
import com.liferay.layout.page.template.exception.LayoutPageTemplateEntryNameException;
import com.liferay.layout.page.template.exception.RequiredLayoutPageTemplateEntryException;
import com.liferay.layout.page.template.model.LayoutPageTemplateCollection;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateCollectionServiceUtil;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalServiceUtil;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
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
			PermissionCheckerTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@After
	public void tearDown() throws Exception {
		for (AssetDisplayPageEntry assetDisplayPageEntry :
				_assetDisplayPageEntries) {

			AssetDisplayPageEntryLocalServiceUtil.deleteAssetDisplayPageEntry(
				assetDisplayPageEntry);
		}

		for (LayoutPageTemplateCollection layoutPageTemplateCollection :
				_layoutPageTemplateCollections) {

			LayoutPageTemplateCollectionServiceUtil.
				deleteLayoutPageTemplateCollection(
					layoutPageTemplateCollection.
						getLayoutPageTemplateCollectionId());
		}
	}

	@Test(expected = DuplicateLayoutPageTemplateEntryException.class)
	public void testAddDuplicateLayoutPageTemplateEntries() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			LayoutPageTemplateCollectionServiceUtil.
				addLayoutPageTemplateCollection(
					_group.getGroupId(), "Layout Page Template Collection",
					null, serviceContext);

		LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
			_group.getGroupId(),
			layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			"Layout Page Template Entry", serviceContext);

		LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
			_group.getGroupId(),
			layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			"Layout Page Template Entry", serviceContext);
	}

	@Test(expected = LayoutPageTemplateEntryNameException.class)
	public void testAddLayoutPageEntryWithNullName() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			LayoutPageTemplateCollectionServiceUtil.
				addLayoutPageTemplateCollection(
					_group.getGroupId(), "Layout Page Template Collection",
					null, serviceContext);

		LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
			_group.getGroupId(),
			layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			null, serviceContext);
	}

	@Test
	public void testAddLayoutPageTemplateEntry() throws PortalException {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			LayoutPageTemplateCollectionServiceUtil.
				addLayoutPageTemplateCollection(
					_group.getGroupId(), "Layout Page Template Collection",
					null, serviceContext);

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
				_group.getGroupId(),
				layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				"Layout Page Template Entry", serviceContext);

		Assert.assertEquals(
			"Layout Page Template Entry", layoutPageTemplateEntry.getName());
	}

	@Test
	public void testAddLayoutPageTemplateEntryByTypeAndStatus()
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			LayoutPageTemplateCollectionServiceUtil.
				addLayoutPageTemplateCollection(
					_group.getGroupId(), "Layout Page Template Collection",
					null, serviceContext);

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
				_group.getGroupId(),
				layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				"Layout Page Template Entry",
				LayoutPageTemplateEntryTypeConstants.TYPE_BASIC,
				WorkflowConstants.STATUS_DRAFT, serviceContext);

		Assert.assertEquals(
			"Layout Page Template Entry", layoutPageTemplateEntry.getName());
		Assert.assertEquals(
			LayoutPageTemplateEntryTypeConstants.TYPE_BASIC,
			layoutPageTemplateEntry.getType());
		Assert.assertEquals(
			WorkflowConstants.STATUS_DRAFT,
			layoutPageTemplateEntry.getStatus());
	}

	@Test(expected = LayoutPageTemplateEntryNameException.class)
	public void testAddLayoutPageTemplateEntryWithEmptyName() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			LayoutPageTemplateCollectionServiceUtil.
				addLayoutPageTemplateCollection(
					_group.getGroupId(), "Layout Page Template Collection",
					null, serviceContext);

		LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
			_group.getGroupId(),
			layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			StringPool.BLANK, serviceContext);
	}

	@Test
	public void testAddLayoutPageTemplateEntryWithFragmentEntries()
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry(serviceContext);

		FragmentCollection fragmentCollection =
			FragmentCollectionServiceUtil.addFragmentCollection(
				_group.getGroupId(), "Fragment Collection", StringPool.BLANK,
				serviceContext);

		FragmentEntry fragmentEntry1 =
			FragmentEntryServiceUtil.addFragmentEntry(
				_group.getGroupId(),
				fragmentCollection.getFragmentCollectionId(),
				"Fragment Entry 1", WorkflowConstants.STATUS_APPROVED,
				serviceContext);

		FragmentEntry fragmentEntry2 =
			FragmentEntryServiceUtil.addFragmentEntry(
				_group.getGroupId(),
				fragmentCollection.getFragmentCollectionId(),
				"Fragment Entry 2", WorkflowConstants.STATUS_APPROVED,
				serviceContext);

		long[] fragmentEntryIds = {
			fragmentEntry1.getFragmentEntryId(),
			fragmentEntry2.getFragmentEntryId()
		};

		LayoutPageTemplateEntryServiceUtil.updateLayoutPageTemplateEntry(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
			layoutPageTemplateEntry.getName(), fragmentEntryIds,
			serviceContext);

		List<FragmentEntryLink> actualLayoutPageTemplateEntriesCount =
			FragmentEntryLinkLocalServiceUtil.getFragmentEntryLinks(
				_group.getGroupId(),
				PortalUtil.getClassNameId(
					LayoutPageTemplateEntry.class.getName()),
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertEquals(
			actualLayoutPageTemplateEntriesCount.toString(), 2,
			actualLayoutPageTemplateEntriesCount.size());
	}

	@Test
	public void testAddMultipleLayoutPageTemplateEntries()
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			LayoutPageTemplateCollectionServiceUtil.
				addLayoutPageTemplateCollection(
					_group.getGroupId(), "Layout Page Template Collection",
					null, serviceContext);

		List<LayoutPageTemplateEntry> originalLayoutPageTemplateEntries =
			LayoutPageTemplateEntryServiceUtil.getLayoutPageTemplateEntries(
				layoutPageTemplateCollection.getGroupId(),
				layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
			_group.getGroupId(),
			layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			"Layout Page Template Entry 1", serviceContext);

		LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
			_group.getGroupId(),
			layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			"Layout Page Template Entry 2", serviceContext);

		List<LayoutPageTemplateEntry> actualLayoutPageTemplateEntries =
			LayoutPageTemplateEntryServiceUtil.getLayoutPageTemplateEntries(
				layoutPageTemplateCollection.getGroupId(),
				layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		Assert.assertEquals(
			actualLayoutPageTemplateEntries.toString(),
			originalLayoutPageTemplateEntries.size() + 2,
			actualLayoutPageTemplateEntries.size());
	}

	@Test(expected = RequiredLayoutPageTemplateEntryException.class)
	public void testAddRequiredLayoutPageTemplateEntries() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			LayoutPageTemplateCollectionServiceUtil.
				addLayoutPageTemplateCollection(
					_group.getGroupId(), "Layout Page Template Collection",
					null, serviceContext);

		_layoutPageTemplateCollections.add(layoutPageTemplateCollection);

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
				_group.getGroupId(),
				layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				"Layout Page Template Entry",
				LayoutPageTemplateEntryTypeConstants.TYPE_BASIC,
				serviceContext);

		AssetDisplayPageEntry assetDisplayPageEntry =
			AssetDisplayPageEntryLocalServiceUtil.addAssetDisplayPageEntry(
				TestPropsValues.getUserId(), _group.getGroupId(),
				RandomTestUtil.randomLong(), RandomTestUtil.randomLong(),
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				serviceContext);

		_assetDisplayPageEntries.add(assetDisplayPageEntry);

		LayoutPageTemplateEntryLocalServiceUtil.deleteLayoutPageTemplateEntry(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId());
	}

	@Test
	public void testDeleteLayoutPageTemplateEntries() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			LayoutPageTemplateCollectionServiceUtil.
				addLayoutPageTemplateCollection(
					_group.getGroupId(), "Layout Page Template Collection",
					null, serviceContext);

		LayoutPageTemplateEntry layoutPageTemplateEntry1 =
			LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
				_group.getGroupId(),
				layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				"Layout Page Template Entry 1", serviceContext);

		LayoutPageTemplateEntry layoutPageTemplateEntry2 =
			LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
				_group.getGroupId(),
				layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				"Layout Page Template Entry 2", serviceContext);

		long[] layoutPageTemplateEntries = {
			layoutPageTemplateEntry1.getLayoutPageTemplateEntryId(),
			layoutPageTemplateEntry2.getLayoutPageTemplateEntryId()
		};

		LayoutPageTemplateEntryServiceUtil.deleteLayoutPageTemplateEntries(
			layoutPageTemplateEntries);

		Assert.assertNull(
			LayoutPageTemplateEntryServiceUtil.fetchLayoutPageTemplateEntry(
				layoutPageTemplateEntry1.getLayoutPageTemplateEntryId()));

		Assert.assertNull(
			LayoutPageTemplateEntryServiceUtil.fetchLayoutPageTemplateEntry(
				layoutPageTemplateEntry2.getLayoutPageTemplateEntryId()));
	}

	@Test
	public void testDeleteLayoutPageTemplateEntry() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry(serviceContext);

		LayoutPageTemplateEntryServiceUtil.deleteLayoutPageTemplateEntry(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertNull(
			LayoutPageTemplateEntryServiceUtil.fetchLayoutPageTemplateEntry(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId()));
	}

	@Test
	public void testRemoveFragmentsFromLayoutPageTemplateEntry()
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry(serviceContext);

		FragmentCollection fragmentCollection =
			FragmentCollectionServiceUtil.addFragmentCollection(
				_group.getGroupId(), "Fragment Collection", StringPool.BLANK,
				serviceContext);

		FragmentEntry fragmentEntry1 =
			FragmentEntryServiceUtil.addFragmentEntry(
				_group.getGroupId(),
				fragmentCollection.getFragmentCollectionId(),
				"Fragment Entry 1", WorkflowConstants.STATUS_APPROVED,
				serviceContext);

		FragmentEntry fragmentEntry2 =
			FragmentEntryServiceUtil.addFragmentEntry(
				_group.getGroupId(),
				fragmentCollection.getFragmentCollectionId(),
				"Fragment Entry 2", WorkflowConstants.STATUS_APPROVED,
				serviceContext);

		long[] fragmentEntryIds = {
			fragmentEntry1.getFragmentEntryId(),
			fragmentEntry2.getFragmentEntryId()
		};

		LayoutPageTemplateEntryServiceUtil.updateLayoutPageTemplateEntry(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(), "New name",
			fragmentEntryIds, serviceContext);

		LayoutPageTemplateEntryServiceUtil.updateLayoutPageTemplateEntry(
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(), "New name",
			null, serviceContext);

		List<FragmentEntryLink> actualLayoutPageTemplateEntriesCount =
			FragmentEntryLinkLocalServiceUtil.getFragmentEntryLinks(
				_group.getGroupId(),
				PortalUtil.getClassNameId(
					LayoutPageTemplateEntry.class.getName()),
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertEquals(
			actualLayoutPageTemplateEntriesCount.toString(), 0,
			actualLayoutPageTemplateEntriesCount.size());
	}

	@Test
	public void testUpdateLayoutPageTemplateEntryClassNameAndClassType()
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry(serviceContext);

		long classNameId = PortalUtil.getClassNameId(Layout.class);
		long classTypeId = RandomTestUtil.randomLong();

		layoutPageTemplateEntry =
			LayoutPageTemplateEntryServiceUtil.updateLayoutPageTemplateEntry(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				classNameId, classTypeId);

		Assert.assertEquals(
			classNameId, layoutPageTemplateEntry.getClassNameId());

		Assert.assertEquals(
			classTypeId, layoutPageTemplateEntry.getClassTypeId());
	}

	@Test
	public void testUpdateLayoutPageTemplateEntryDefaultTemplate()
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry(serviceContext);

		layoutPageTemplateEntry =
			LayoutPageTemplateEntryServiceUtil.updateLayoutPageTemplateEntry(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(), true);

		Assert.assertEquals(true, layoutPageTemplateEntry.isDefaultTemplate());
	}

	@Test
	public void testUpdateLayoutPageTemplateEntryName() throws PortalException {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry(serviceContext);

		layoutPageTemplateEntry =
			LayoutPageTemplateEntryServiceUtil.updateLayoutPageTemplateEntry(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				"Layout Page Template Entry New");

		Assert.assertEquals(
			"Layout Page Template Entry New",
			layoutPageTemplateEntry.getName());
	}

	@Test
	public void testUpdateLayoutPageTemplateEntryStatus()
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry(serviceContext);

		layoutPageTemplateEntry =
			LayoutPageTemplateEntryServiceUtil.updateLayoutPageTemplateEntry(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				WorkflowConstants.STATUS_APPROVED);

		Assert.assertEquals(
			WorkflowConstants.STATUS_APPROVED,
			layoutPageTemplateEntry.getStatus());
	}

	private LayoutPageTemplateEntry _getLayoutPageTemplateEntry(
			ServiceContext serviceContext)
		throws PortalException {

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			LayoutPageTemplateCollectionServiceUtil.
				addLayoutPageTemplateCollection(
					_group.getGroupId(), "Layout Page Template Collection",
					null, serviceContext);

		return LayoutPageTemplateEntryServiceUtil.addLayoutPageTemplateEntry(
			_group.getGroupId(),
			layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(),
			"Layout Page Template Entry", serviceContext);
	}

	private final List<AssetDisplayPageEntry> _assetDisplayPageEntries =
		new ArrayList<>();

	@DeleteAfterTestRun
	private Group _group;

	private final List<LayoutPageTemplateCollection>
		_layoutPageTemplateCollections = new ArrayList<>();

}