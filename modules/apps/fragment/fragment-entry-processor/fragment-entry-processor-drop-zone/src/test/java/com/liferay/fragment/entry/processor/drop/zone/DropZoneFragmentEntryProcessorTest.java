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

package com.liferay.fragment.entry.processor.drop.zone;

import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.exception.FragmentEntryContentException;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.DefaultFragmentEntryProcessorContext;
import com.liferay.layout.constants.LayoutWebKeys;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.util.structure.FragmentDropZoneLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.uuid.PortalUUIDImpl;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Lourdes Fernández Besada
 */
public class DropZoneFragmentEntryProcessorTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		_setUpDropZoneFragmentEntryProcessor();

		_setUpPortalUUIDUtil();

		_setUpPropsUtil();
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

		String html = FragmentEntryProcessorDropZoneTestUtil.getHTML(
			dropZoneId1, newDropZoneId, dropZoneId2);

		_setFeatureFlag(false);

		Assert.assertEquals(
			_getExpectedHTML(
				new KeyValuePair(
					dropZoneId1,
					fragmentDropZoneLayoutStructureItem1.getItemId()),
				new KeyValuePair(
					newDropZoneId,
					fragmentDropZoneLayoutStructureItem2.getItemId()),
				new KeyValuePair(dropZoneId2, StringPool.BLANK)),
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html,
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));

		_setFeatureFlag(true);

		Assert.assertEquals(
			_getExpectedHTML(
				new KeyValuePair(
					dropZoneId1,
					fragmentDropZoneLayoutStructureItem1.getItemId()),
				new KeyValuePair(newDropZoneId, StringPool.BLANK),
				new KeyValuePair(
					dropZoneId2,
					fragmentDropZoneLayoutStructureItem2.getItemId())),
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html,
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditMode() throws Exception {
		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem =
				FragmentEntryProcessorDropZoneTestUtil.
					addFragmentDropZoneLayoutStructureItem(
						fragmentEntryLink, layoutStructure, StringPool.BLANK);

		Assert.assertEquals(
			_getExpectedHTML(
				StringPool.BLANK,
				fragmentDropZoneLayoutStructureItem.getItemId()),
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink,
				FragmentEntryProcessorDropZoneTestUtil.getHTML(
					StringPool.BLANK),
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditModeDifferentIds()
		throws Exception {

		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem =
				FragmentEntryProcessorDropZoneTestUtil.
					addFragmentDropZoneLayoutStructureItem(
						fragmentEntryLink, layoutStructure,
						RandomTestUtil.randomString());

		String elementDropZoneId = RandomTestUtil.randomString();

		String html = FragmentEntryProcessorDropZoneTestUtil.getHTML(
			elementDropZoneId);

		_setFeatureFlag(false);

		Assert.assertEquals(
			_getExpectedHTML(
				elementDropZoneId,
				fragmentDropZoneLayoutStructureItem.getItemId()),
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html,
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));

		_setFeatureFlag(true);

		Assert.assertEquals(
			_getExpectedHTML(elementDropZoneId, StringPool.BLANK),
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html,
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditModeSameIds()
		throws Exception {

		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		String fragmentDropZoneId = RandomTestUtil.randomString();

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem =
				FragmentEntryProcessorDropZoneTestUtil.
					addFragmentDropZoneLayoutStructureItem(
						fragmentEntryLink, layoutStructure, fragmentDropZoneId);

		Assert.assertEquals(
			_getExpectedHTML(
				fragmentDropZoneId,
				fragmentDropZoneLayoutStructureItem.getItemId()),
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink,
				FragmentEntryProcessorDropZoneTestUtil.getHTML(
					fragmentDropZoneId),
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));
	}

	@Test
	public void testProcessFragmentEntryLinkHTMLInEditMultipleDropZonesWithoutIds()
		throws Exception {

		FragmentEntryLink fragmentEntryLink =
			FragmentEntryProcessorDropZoneTestUtil.getMockFragmentEntryLink();

		LayoutStructure layoutStructure = new LayoutStructure();

		FragmentDropZoneLayoutStructureItem[]
			fragmentDropZoneLayoutStructureItems =
				FragmentEntryProcessorDropZoneTestUtil.
					addFragmentDropZoneLayoutStructureItems(
						fragmentEntryLink, layoutStructure, StringPool.BLANK,
						StringPool.BLANK);

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem1 =
				fragmentDropZoneLayoutStructureItems[0];
		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem2 =
				fragmentDropZoneLayoutStructureItems[1];

		String dropZoneId1 = RandomTestUtil.randomString();
		String dropZoneId2 = RandomTestUtil.randomString();

		String expectedHTML = _getExpectedHTML(
			new KeyValuePair(
				dropZoneId1, fragmentDropZoneLayoutStructureItem1.getItemId()),
			new KeyValuePair(
				dropZoneId2, fragmentDropZoneLayoutStructureItem2.getItemId()));

		String html = FragmentEntryProcessorDropZoneTestUtil.getHTML(
			dropZoneId1, dropZoneId2);

		_setFeatureFlag(false);

		Assert.assertEquals(
			expectedHTML,
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html,
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));

		_setFeatureFlag(true);

		Assert.assertEquals(
			expectedHTML,
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html,
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));
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
			removedFragmentDropZoneLayoutStructureItem =
				fragmentDropZoneLayoutStructureItems[1];

		String html = FragmentEntryProcessorDropZoneTestUtil.getHTML(
			dropZoneId1, dropZoneId2);

		_setFeatureFlag(false);

		Assert.assertEquals(
			_getExpectedHTML(
				new KeyValuePair(
					dropZoneId1,
					fragmentDropZoneLayoutStructureItem1.getItemId()),
				new KeyValuePair(
					dropZoneId2,
					removedFragmentDropZoneLayoutStructureItem.getItemId())),
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html,
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));

		_setFeatureFlag(true);

		FragmentDropZoneLayoutStructureItem
			fragmentDropZoneLayoutStructureItem2 =
				fragmentDropZoneLayoutStructureItems[2];

		Assert.assertEquals(
			_getExpectedHTML(
				new KeyValuePair(
					dropZoneId1,
					fragmentDropZoneLayoutStructureItem1.getItemId()),
				new KeyValuePair(
					dropZoneId2,
					fragmentDropZoneLayoutStructureItem2.getItemId())),
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html,
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));
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

		String html = FragmentEntryProcessorDropZoneTestUtil.getHTML(
			dropZoneId2, dropZoneId3, dropZoneId1);

		_setFeatureFlag(false);

		Assert.assertEquals(
			_getExpectedHTML(
				new KeyValuePair(
					dropZoneId2,
					fragmentDropZoneLayoutStructureItem1.getItemId()),
				new KeyValuePair(
					dropZoneId3,
					fragmentDropZoneLayoutStructureItem2.getItemId()),
				new KeyValuePair(
					dropZoneId1,
					fragmentDropZoneLayoutStructureItem3.getItemId())),
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html,
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));

		_setFeatureFlag(true);

		Assert.assertEquals(
			_getExpectedHTML(
				new KeyValuePair(
					dropZoneId2,
					fragmentDropZoneLayoutStructureItem2.getItemId()),
				new KeyValuePair(
					dropZoneId3,
					fragmentDropZoneLayoutStructureItem3.getItemId()),
				new KeyValuePair(
					dropZoneId1,
					fragmentDropZoneLayoutStructureItem1.getItemId())),
			_dropZoneFragmentEntryProcessor.processFragmentEntryLinkHTML(
				fragmentEntryLink, html,
				new DefaultFragmentEntryProcessorContext(
					_getMockHttpServletRequest(layoutStructure), null,
					FragmentEntryLinkConstants.EDIT,
					LocaleUtil.getMostRelevantLocale())));
	}

	@Test(expected = FragmentEntryContentException.class)
	public void testValidateFragmentEntryHTMLDuplicatedId() throws Exception {
		_setFeatureFlag(true);

		String dropZoneId = RandomTestUtil.randomString();

		_dropZoneFragmentEntryProcessor.validateFragmentEntryHTML(
			FragmentEntryProcessorDropZoneTestUtil.getHTML(
				dropZoneId, dropZoneId),
			null);
	}

	@Test(expected = FragmentEntryContentException.class)
	public void testValidateFragmentEntryHTMLMissingId() throws Exception {
		_setFeatureFlag(true);

		_dropZoneFragmentEntryProcessor.validateFragmentEntryHTML(
			FragmentEntryProcessorDropZoneTestUtil.getHTML(
				StringPool.BLANK, RandomTestUtil.randomString()),
			null);
	}

	@Test
	public void testValidateFragmentEntryHTMLNoValidationWhenFFDisabled()
		throws Exception {

		_setFeatureFlag(false);

		String dropZoneId = RandomTestUtil.randomString();

		String duplicatedIdHTML =
			FragmentEntryProcessorDropZoneTestUtil.getHTML(
				dropZoneId, dropZoneId);

		_dropZoneFragmentEntryProcessor.validateFragmentEntryHTML(
			duplicatedIdHTML, null);

		String missingIdHTML = FragmentEntryProcessorDropZoneTestUtil.getHTML(
			StringPool.BLANK, dropZoneId);

		_dropZoneFragmentEntryProcessor.validateFragmentEntryHTML(
			missingIdHTML, null);
	}

	@Test
	public void testValidateFragmentEntryHTMLValidWithIds() throws Exception {
		_setFeatureFlag(true);

		_dropZoneFragmentEntryProcessor.validateFragmentEntryHTML(
			FragmentEntryProcessorDropZoneTestUtil.getHTML(
				RandomTestUtil.randomString(), RandomTestUtil.randomString()),
			null);
	}

	@Test
	public void testValidateFragmentEntryHTMLValidWithoutIds()
		throws Exception {

		_setFeatureFlag(true);

		_dropZoneFragmentEntryProcessor.validateFragmentEntryHTML(
			FragmentEntryProcessorDropZoneTestUtil.getHTML(
				StringPool.BLANK, StringPool.BLANK),
			null);
	}

	private static void _setUpDropZoneFragmentEntryProcessor() {
		_dropZoneFragmentEntryProcessor = new DropZoneFragmentEntryProcessor();

		_layoutPageTemplateStructureLocalService = Mockito.mock(
			LayoutPageTemplateStructureLocalService.class);

		ReflectionTestUtil.setFieldValue(
			_dropZoneFragmentEntryProcessor,
			"_layoutPageTemplateStructureLocalService",
			_layoutPageTemplateStructureLocalService);

		_language = Mockito.mock(Language.class);

		ReflectionTestUtil.setFieldValue(
			_dropZoneFragmentEntryProcessor, "_language", _language);

		_portal = Mockito.mock(Portal.class);

		ReflectionTestUtil.setFieldValue(
			_dropZoneFragmentEntryProcessor, "_portal", _portal);
	}

	private static void _setUpPortalUUIDUtil() {
		PortalUUIDUtil portalUUIDUtil = new PortalUUIDUtil();

		portalUUIDUtil.setPortalUUID(new PortalUUIDImpl());
	}

	private static void _setUpPropsUtil() {
		_props = Mockito.mock(Props.class);

		ReflectionTestUtil.setFieldValue(PropsUtil.class, "_props", _props);
	}

	private String _getExpectedHTML(
		KeyValuePair... dropZoneIdItemIdKeyValuePairs) {

		StringBundler sb = new StringBundler("<div class=\"fragment_1\">");

		for (KeyValuePair keyValuePair : dropZoneIdItemIdKeyValuePairs) {
			sb.append("<lfr-drop-zone");

			String dropZoneId = keyValuePair.getKey();

			if (!Validator.isBlank(dropZoneId)) {
				sb.append(" data-lfr-drop-zone-id=\"");
				sb.append(dropZoneId);
				sb.append(StringPool.QUOTE);
			}

			String itemId = keyValuePair.getValue();

			if (!Validator.isBlank(itemId)) {
				sb.append(" uuid=\"");
				sb.append(itemId);
				sb.append(StringPool.QUOTE);
			}

			sb.append("></lfr-drop-zone>");
		}

		sb.append("</div>");

		return sb.toString();
	}

	private String _getExpectedHTML(String dropZoneId, String itemId) {
		return _getExpectedHTML(new KeyValuePair(dropZoneId, itemId));
	}

	private HttpServletRequest _getMockHttpServletRequest(
		LayoutStructure layoutStructure) {

		HttpServletRequest httpServletRequest = Mockito.mock(
			HttpServletRequest.class);

		Mockito.when(
			httpServletRequest.getAttribute(LayoutWebKeys.LAYOUT_STRUCTURE)
		).thenReturn(
			layoutStructure
		);

		return httpServletRequest;
	}

	private void _setFeatureFlag(boolean enabled) {
		Mockito.when(
			_props.get("feature.flag.LPS-167932")
		).thenReturn(
			Boolean.toString(enabled)
		);
	}

	private static DropZoneFragmentEntryProcessor
		_dropZoneFragmentEntryProcessor;
	private static Language _language;
	private static LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;
	private static Portal _portal;
	private static Props _props;

}