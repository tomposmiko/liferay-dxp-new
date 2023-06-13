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

package com.liferay.fragment.entry.processor.drop.zone.listener;

import com.liferay.fragment.entry.processor.drop.zone.FragmentEntryProcessorDropZoneTestUtil;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.FragmentEntryProcessorContext;
import com.liferay.fragment.processor.FragmentEntryProcessorRegistry;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.util.structure.FragmentDropZoneLayoutStructureItem;
import com.liferay.layout.util.structure.FragmentStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.uuid.PortalUUIDImpl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * @author Lourdes Fernández Besada
 */
public class DropZoneFragmentEntryLinkListenerTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		_setUpPortalUUIDUtil();
	}

	@Before
	public void setUp() {
		_setUpDropZoneFragmentEntryLinkListener();
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditAddingDropZone()
		throws Exception {

		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		String dropZoneId1 = RandomTestUtil.randomString();
		String dropZoneId2 = RandomTestUtil.randomString();

		FragmentDropZoneLayoutStructureItem[]
			fragmentDropZoneLayoutStructureItems =
				FragmentEntryProcessorDropZoneTestUtil.
					addFragmentDropZoneLayoutStructureItems(
						fragmentEntryLink, layoutStructure, dropZoneId1,
						dropZoneId2);

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem1 =
				fragmentDropZoneLayoutStructureItems[0];
		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem2 =
				fragmentDropZoneLayoutStructureItems[1];

		String newDropZoneId = RandomTestUtil.randomString();

		_setUpFragmentEntryProcessorRegistry(
			fragmentEntryLink,
			FragmentEntryProcessorDropZoneTestUtil.getHTML(
				dropZoneId1, newDropZoneId, dropZoneId2));

		_setUpLayoutPageTemplateStructure(layoutStructure.toString());

		_assertUpdateLayoutPageTemplateStructureData(
			fragmentEntryLink,
			new KeyValuePair(
				dropZoneId1, fragmentDropZoneLayoutStructureItem1.getItemId()),
			new KeyValuePair(newDropZoneId, StringPool.BLANK),
			new KeyValuePair(
				dropZoneId2, fragmentDropZoneLayoutStructureItem2.getItemId()));
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditMode() throws Exception {
		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		FragmentEntryProcessorDropZoneTestUtil.
			addFragmentDropZoneLayoutStructureItem(
				fragmentEntryLink, layoutStructure, StringPool.BLANK);

		_setUpFragmentEntryProcessorRegistry(
			fragmentEntryLink,
			FragmentEntryProcessorDropZoneTestUtil.getHTML(StringPool.BLANK));

		_setUpLayoutPageTemplateStructure(layoutStructure.toString());

		_assertUpdateLayoutPageTemplateStructureData(true, fragmentEntryLink);
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditModeDifferentIds()
		throws Exception {

		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		FragmentEntryProcessorDropZoneTestUtil.
			addFragmentDropZoneLayoutStructureItem(
				fragmentEntryLink, layoutStructure,
				RandomTestUtil.randomString());

		String elementDropZoneId = RandomTestUtil.randomString();

		_setUpFragmentEntryProcessorRegistry(
			fragmentEntryLink,
			FragmentEntryProcessorDropZoneTestUtil.getHTML(elementDropZoneId));

		_setUpLayoutPageTemplateStructure(layoutStructure.toString());

		_assertUpdateLayoutPageTemplateStructureData(
			elementDropZoneId, fragmentEntryLink, StringPool.BLANK);
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditModeSameIds()
		throws Exception {

		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		String dropZoneId = RandomTestUtil.randomString();

		FragmentEntryProcessorDropZoneTestUtil.
			addFragmentDropZoneLayoutStructureItem(
				fragmentEntryLink, layoutStructure, dropZoneId);

		_setUpFragmentEntryProcessorRegistry(
			fragmentEntryLink,
			FragmentEntryProcessorDropZoneTestUtil.getHTML(dropZoneId));

		_setUpLayoutPageTemplateStructure(layoutStructure.toString());

		_assertUpdateLayoutPageTemplateStructureData(true, fragmentEntryLink);
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditMultipleDropZonesWithoutIds()
		throws Exception {

		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		FragmentEntryProcessorDropZoneTestUtil.
			addFragmentDropZoneLayoutStructureItem(
				fragmentEntryLink, layoutStructure, StringPool.BLANK);

		_setUpFragmentEntryProcessorRegistry(
			fragmentEntryLink,
			FragmentEntryProcessorDropZoneTestUtil.getHTML(StringPool.BLANK));

		_setUpLayoutPageTemplateStructure(layoutStructure.toString());

		_assertUpdateLayoutPageTemplateStructureData(true, fragmentEntryLink);
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditRemovingDropZone()
		throws Exception {

		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		String dropZoneId1 = RandomTestUtil.randomString();
		String dropZoneId2 = RandomTestUtil.randomString();

		FragmentDropZoneLayoutStructureItem[]
			fragmentDropZoneLayoutStructureItems =
				FragmentEntryProcessorDropZoneTestUtil.
					addFragmentDropZoneLayoutStructureItems(
						fragmentEntryLink, layoutStructure, dropZoneId1,
						RandomTestUtil.randomString(), dropZoneId2);

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem1 =
				fragmentDropZoneLayoutStructureItems[0];

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem2 =
				fragmentDropZoneLayoutStructureItems[2];

		_setUpFragmentEntryProcessorRegistry(
			fragmentEntryLink,
			FragmentEntryProcessorDropZoneTestUtil.getHTML(
				dropZoneId1, dropZoneId2));

		_setUpLayoutPageTemplateStructure(layoutStructure.toString());

		_assertUpdateLayoutPageTemplateStructureData(
			fragmentEntryLink,
			new KeyValuePair(
				dropZoneId1, fragmentDropZoneLayoutStructureItem1.getItemId()),
			new KeyValuePair(
				dropZoneId2, fragmentDropZoneLayoutStructureItem2.getItemId()));
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditSwappingDropZones()
		throws Exception {

		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		String dropZoneId1 = RandomTestUtil.randomString();
		String dropZoneId2 = RandomTestUtil.randomString();
		String dropZoneId3 = RandomTestUtil.randomString();

		FragmentDropZoneLayoutStructureItem[]
			fragmentDropZoneLayoutStructureItems =
				FragmentEntryProcessorDropZoneTestUtil.
					addFragmentDropZoneLayoutStructureItems(
						fragmentEntryLink, layoutStructure, dropZoneId1,
						dropZoneId2, dropZoneId3);

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem1 =
				fragmentDropZoneLayoutStructureItems[0];

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem2 =
				fragmentDropZoneLayoutStructureItems[1];

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem3 =
				fragmentDropZoneLayoutStructureItems[2];

		_setUpFragmentEntryProcessorRegistry(
			fragmentEntryLink,
			FragmentEntryProcessorDropZoneTestUtil.getHTML(
				dropZoneId2, dropZoneId3, dropZoneId1));

		_setUpLayoutPageTemplateStructure(layoutStructure.toString());

		_assertUpdateLayoutPageTemplateStructureData(
			fragmentEntryLink,
			new KeyValuePair(
				dropZoneId2, fragmentDropZoneLayoutStructureItem2.getItemId()),
			new KeyValuePair(
				dropZoneId3, fragmentDropZoneLayoutStructureItem3.getItemId()),
			new KeyValuePair(
				dropZoneId1, fragmentDropZoneLayoutStructureItem1.getItemId()));
	}

	private static void _setUpPortalUUIDUtil() {
		PortalUUIDUtil portalUUIDUtil = new PortalUUIDUtil();

		portalUUIDUtil.setPortalUUID(new PortalUUIDImpl());
	}

	private void _assertUpdateLayoutPageTemplateStructureData(
			boolean never, FragmentEntryLink fragmentEntryLink,
			KeyValuePair... dropZoneIdItemIdKeyValuePairs)
		throws Exception {

		ServiceContextThreadLocal.pushServiceContext(new ServiceContext());

		_dropZoneFragmentEntryLinkListener.updateLayoutPageTemplateStructure(
			fragmentEntryLink);

		if (never) {
			Mockito.verify(
				_layoutPageTemplateStructureLocalService, Mockito.never()
			).updateLayoutPageTemplateStructureData(
				Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(),
				Mockito.anyString()
			);

			return;
		}

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_layoutPageTemplateStructureLocalService
		).updateLayoutPageTemplateStructureData(
			Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(),
			argumentCaptor.capture()
		);

		LayoutStructure layoutStructure = LayoutStructure.of(
			argumentCaptor.getValue());

		FragmentStyledLayoutStructureItem fragmentStyledLayoutStructureItem =
			_getFragmentStyledLayoutStructureItem(layoutStructure);

		Assert.assertEquals(
			fragmentEntryLink.getFragmentEntryLinkId(),
			fragmentStyledLayoutStructureItem.getFragmentEntryLinkId());

		List<String> childrenItemIds =
			fragmentStyledLayoutStructureItem.getChildrenItemIds();

		Assert.assertEquals(
			childrenItemIds.toString(), dropZoneIdItemIdKeyValuePairs.length,
			childrenItemIds.size());

		for (int i = 0; i < dropZoneIdItemIdKeyValuePairs.length; i++) {
			LayoutStructureItem layoutStructureItem =
				layoutStructure.getLayoutStructureItem(childrenItemIds.get(i));

			Assert.assertTrue(
				layoutStructureItem instanceof
					FragmentDropZoneLayoutStructureItem);

			String dropZoneId = dropZoneIdItemIdKeyValuePairs[i].getKey();

			if (!Validator.isBlank(dropZoneId)) {
				FragmentDropZoneLayoutStructureItem
					fragmentDropZoneLayoutStructureItem =
						(FragmentDropZoneLayoutStructureItem)
							layoutStructureItem;

				Assert.assertEquals(
					dropZoneId,
					fragmentDropZoneLayoutStructureItem.
						getFragmentDropZoneId());
			}

			String itemId = dropZoneIdItemIdKeyValuePairs[i].getValue();

			if (!Validator.isBlank(itemId)) {
				Assert.assertEquals(itemId, layoutStructureItem.getItemId());
			}
		}
	}

	private void _assertUpdateLayoutPageTemplateStructureData(
			FragmentEntryLink fragmentEntryLink,
			KeyValuePair... dropZoneIdItemIdKeyValuePairs)
		throws Exception {

		_assertUpdateLayoutPageTemplateStructureData(
			false, fragmentEntryLink, dropZoneIdItemIdKeyValuePairs);
	}

	private void _assertUpdateLayoutPageTemplateStructureData(
			String dropZoneId, FragmentEntryLink fragmentEntryLink,
			String itemId)
		throws Exception {

		_assertUpdateLayoutPageTemplateStructureData(
			fragmentEntryLink, new KeyValuePair(dropZoneId, itemId));
	}

	private FragmentStyledLayoutStructureItem
		_getFragmentStyledLayoutStructureItem(LayoutStructure layoutStructure) {

		LayoutStructureItem mainLayoutStructureItem =
			layoutStructure.getMainLayoutStructureItem();

		List<String> childrenItemIds =
			mainLayoutStructureItem.getChildrenItemIds();

		Assert.assertEquals(
			childrenItemIds.toString(), 1, childrenItemIds.size());

		LayoutStructureItem containerStyledLayoutStructureItem =
			layoutStructure.getLayoutStructureItem(childrenItemIds.get(0));

		List<String> containerChildrenItemIds =
			containerStyledLayoutStructureItem.getChildrenItemIds();

		Assert.assertEquals(
			containerChildrenItemIds.toString(), 1,
			containerChildrenItemIds.size());

		LayoutStructureItem fragmentStyledLayoutStructureItem =
			layoutStructure.getLayoutStructureItem(
				containerChildrenItemIds.get(0));

		Assert.assertTrue(
			fragmentStyledLayoutStructureItem instanceof
				FragmentStyledLayoutStructureItem);

		return (FragmentStyledLayoutStructureItem)
			fragmentStyledLayoutStructureItem;
	}

	private void _setUpDropZoneFragmentEntryLinkListener() {
		_dropZoneFragmentEntryLinkListener =
			new DropZoneFragmentEntryLinkListener();

		_layoutPageTemplateStructureLocalService = Mockito.mock(
			LayoutPageTemplateStructureLocalService.class);

		ReflectionTestUtil.setFieldValue(
			_dropZoneFragmentEntryLinkListener,
			"_layoutPageTemplateStructureLocalService",
			_layoutPageTemplateStructureLocalService);

		_fragmentEntryProcessorRegistry = Mockito.mock(
			FragmentEntryProcessorRegistry.class);

		ReflectionTestUtil.setFieldValue(
			_dropZoneFragmentEntryLinkListener,
			"_fragmentEntryProcessorRegistry", _fragmentEntryProcessorRegistry);
	}

	private void _setUpFragmentEntryProcessorRegistry(
			FragmentEntryLink fragmentEntryLink, String processedHTML)
		throws Exception {

		Mockito.when(
			_fragmentEntryProcessorRegistry.processFragmentEntryLinkHTML(
				Mockito.eq(fragmentEntryLink),
				Mockito.any(FragmentEntryProcessorContext.class))
		).thenReturn(
			processedHTML
		);
	}

	private void _setUpLayoutPageTemplateStructure(String data) {
		LayoutPageTemplateStructure layoutPageTemplateStructure = Mockito.mock(
			LayoutPageTemplateStructure.class);

		Mockito.when(
			layoutPageTemplateStructure.getData(Mockito.anyLong())
		).thenReturn(
			data
		);

		Mockito.when(
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					Mockito.anyLong(), Mockito.anyLong())
		).thenReturn(
			layoutPageTemplateStructure
		);
	}

	private DropZoneFragmentEntryLinkListener
		_dropZoneFragmentEntryLinkListener;
	private FragmentEntryProcessorRegistry _fragmentEntryProcessorRegistry;
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

}