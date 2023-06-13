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

package com.liferay.layout.util.template.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.layout.util.template.LayoutConverter;
import com.liferay.layout.util.template.LayoutConverterRegistry;
import com.liferay.layout.util.template.LayoutData;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.LayoutTypePortletConstants;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Rubén Pulido
 */
@RunWith(Arquillian.class)
public class LayoutConverterTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		ServiceContextThreadLocal.pushServiceContext(
			ServiceContextTestUtil.getServiceContext(
				_group, TestPropsValues.getUserId()));
	}

	@After
	public void tearDown() throws Exception {
		ServiceContextThreadLocal.popServiceContext();
	}

	@Test
	public void testConvertOneColumnMultiplePortlets() throws Exception {
		Map<String, String[]> portletIdsMap = new HashMap<String, String[]>() {
			{
				put(
					"column-1",
					new String[] {
						"com_liferay_hello_velocity_web_portlet_" +
							"HelloVelocityPortlet",
						"com_liferay_hello_world_web_portlet_HelloWorldPortlet",
						"hello_soy_portlet"
					});
			}
		};

		List<Map<String, String[]>> portletIdsMaps =
			new ArrayList<Map<String, String[]>>() {
				{
					add(portletIdsMap);
				}
			};

		_testConvert("1_column", portletIdsMaps);
	}

	@Test
	public void testConvertOneColumnNoPortlets() throws Exception {
		_testConvertNoPortlets("1_column");
	}

	@Test
	public void testConvertOneColumnSinglePortlet() throws Exception {
		Map<String, String[]> portletIdsMap = new HashMap<String, String[]>() {
			{
				put(
					"column-1",
					new String[] {
						"com_liferay_hello_world_web_portlet_HelloWorldPortlet"
					});
			}
		};

		List<Map<String, String[]>> portletIdsMaps =
			new ArrayList<Map<String, String[]>>() {
				{
					add(portletIdsMap);
				}
			};

		_testConvert("1_column", portletIdsMaps);
	}

	@Test
	public void testConvertOneTwoColumnsIMultiplePortlets() throws Exception {
		_testConvertOneTwoColumnsMultiplePortlets("1_2_columns_i");
	}

	@Test
	public void testConvertOneTwoColumnsINoPortlets() throws Exception {
		_testConvertNoPortlets("1_2_columns_i");
	}

	@Test
	public void testConvertOneTwoColumnsISinglePortlet() throws Exception {
		_testConvertOneTwoColumnsSinglePortlet("1_2_columns_i");
	}

	@Test
	public void testConvertThreeColumnsMultiplePortlets() throws Exception {
		Map portletIdsMap = new TreeMap();

		portletIdsMap.put(
			"column-1",
			new String[] {
				"com_liferay_hello_velocity_web_portlet_HelloVelocityPortlet",
				"com_liferay_chart_sample_web_portlet_ChartSamplePortlet"
			});

		portletIdsMap.put(
			"column-2",
			new String[] {
				"com_liferay_hello_world_web_portlet_HelloWorldPortlet",
				"hello_soy_portlet"
			});

		portletIdsMap.put(
			"column-3",
			new String[] {
				"com_liferay_clay_sample_web_portlet_ClaySamplePortlet",
				"com_liferay_clay_sample_web_portlet_ClaySamplePortlet"
			});

		_testConvert("3_columns", Collections.singletonList(portletIdsMap));
	}

	@Test
	public void testConvertThreeColumnsNoPortlets() throws Exception {
		_testConvertNoPortlets("3_columns");
	}

	@Test
	public void testConvertThreeColumnsSinglePortlet() throws Exception {
		Map portletIdsMap = new TreeMap();

		portletIdsMap.put(
			"column-1",
			new String[] {
				"com_liferay_hello_velocity_web_portlet_HelloVelocityPortlet"
			});

		portletIdsMap.put(
			"column-2",
			new String[] {
				"com_liferay_hello_world_web_portlet_HelloWorldPortlet"
			});

		portletIdsMap.put("column-3", new String[] {"hello_soy_portlet"});

		_testConvert("3_columns", Collections.singletonList(portletIdsMap));
	}

	@Test
	public void testConvertTwoColumnsIIIMultiplePortlets() throws Exception {
		_testConvertTwoColumnsMultiplePortlets("2_columns_iii");
	}

	@Test
	public void testConvertTwoColumnsIIINoPortlets() throws Exception {
		_testConvertNoPortlets("2_columns_iii");
	}

	@Test
	public void testConvertTwoColumnsIIISinglePortlet() throws Exception {
		_testConvertTwoColumnsSinglePortlet("2_columns_iii");
	}

	@Test
	public void testConvertTwoColumnsIIMultiplePortlets() throws Exception {
		_testConvertTwoColumnsMultiplePortlets("2_columns_ii");
	}

	@Test
	public void testConvertTwoColumnsIINoPortlets() throws Exception {
		_testConvertNoPortlets("2_columns_ii");
	}

	@Test
	public void testConvertTwoColumnsIISinglePortlet() throws Exception {
		_testConvertTwoColumnsSinglePortlet("2_columns_ii");
	}

	@Test
	public void testConvertTwoColumnsIMultiplePortlets() throws Exception {
		_testConvertTwoColumnsMultiplePortlets("2_columns_i");
	}

	@Test
	public void testConvertTwoColumnsINoPortlets() throws Exception {
		_testConvertNoPortlets("2_columns_i");
	}

	@Test
	public void testConvertTwoColumnsISinglePortlet() throws Exception {
		_testConvertTwoColumnsSinglePortlet("2_columns_i");
	}

	@Test
	public void testIsConvertibleFalseWidgetPageCustomizable()
		throws Exception {

		UnicodeProperties typeSettingsProperties = new UnicodeProperties();

		typeSettingsProperties.setProperty(
			LayoutConstants.CUSTOMIZABLE_LAYOUT, Boolean.TRUE.toString());

		Layout layout = LayoutTestUtil.addLayout(
			_group.getGroupId(), typeSettingsProperties.toString());

		LayoutConverter layoutConverter =
			_layoutConverterRegistry.getLayoutConverter(
				_getLayoutTemplateId(layout));

		Assert.assertEquals(false, layoutConverter.isConvertible(layout));
	}

	@Test
	public void testIsConvertibleFalseWidgetPageWithNestedApplicationsWidget()
		throws Exception {

		UnicodeProperties typeSettingsProperties = new UnicodeProperties();

		typeSettingsProperties.put(
			LayoutTypePortletConstants.NESTED_COLUMN_IDS,
			StringUtil.randomString());

		Layout layout = LayoutTestUtil.addLayout(
			_group.getGroupId(), typeSettingsProperties.toString());

		LayoutConverter layoutConverter =
			_layoutConverterRegistry.getLayoutConverter(
				_getLayoutTemplateId(layout));

		Assert.assertEquals(false, layoutConverter.isConvertible(layout));
	}

	@Test
	public void testIsConvertibleTrue() throws Exception {
		Layout layout = LayoutTestUtil.addLayout(_group.getGroupId());

		LayoutConverter layoutConverter =
			_layoutConverterRegistry.getLayoutConverter(
				_getLayoutTemplateId(layout));

		Assert.assertEquals(true, layoutConverter.isConvertible(layout));
	}

	private String _getLayoutTemplateId(Layout layout) {
		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		return layoutTypePortlet.getLayoutTemplateId();
	}

	private String _read(String fileName) throws Exception {
		return new String(
			FileUtil.getBytes(getClass(), "dependencies/" + fileName));
	}

	private void _testConvert(
			String layoutTemplateId, List<Map<String, String[]>> portletIdsMaps)
		throws Exception {

		int columnId = 0;
		List<Map<String, List<String>>> encodedPortletIdsMaps =
			new ArrayList<>();

		UnicodeProperties typeSettingsProperties = new UnicodeProperties();

		typeSettingsProperties.setProperty(
			LayoutTypePortletConstants.LAYOUT_TEMPLATE_ID, layoutTemplateId);

		Layout layout = LayoutTestUtil.addLayout(
			_group.getGroupId(), typeSettingsProperties.toString());

		for (Map<String, String[]> portletIdsMap : portletIdsMaps) {
			Set<Map.Entry<String, String[]>> entries = portletIdsMap.entrySet();

			Map<String, List<String>> encodedPortletIdsMap = new TreeMap<>();

			for (Map.Entry<String, String[]> entry : entries) {
				columnId++;

				encodedPortletIdsMap.put(entry.getKey(), new ArrayList<>());

				List<String> encodedPortletIds = encodedPortletIdsMap.get(
					entry.getKey());

				for (String portletId : entry.getValue()) {
					Portlet portlet = _portletLocalService.getPortletById(
						_group.getCompanyId(), portletId);

					String encodedPortletId = portletId;

					if (portlet.isInstanceable()) {
						encodedPortletId = PortletIdCodec.encode(portletId);
					}

					LayoutTestUtil.addPortletToLayout(
						TestPropsValues.getUserId(), layout, encodedPortletId,
						"column-" + columnId, new HashMap<>());

					encodedPortletIds.add(encodedPortletId);
				}
			}

			encodedPortletIdsMaps.add(encodedPortletIdsMap);
		}

		LayoutConverter layoutConverter =
			_layoutConverterRegistry.getLayoutConverter(
				_getLayoutTemplateId(layout));

		LayoutData layoutData = layoutConverter.convert(layout);

		JSONObject layoutDataJSONObject = layoutData.getLayoutDataJSONObject();

		String expectedLayoutData = _read(
			String.format("expected_layout_data_%s.json", layoutTemplateId));

		List<FragmentEntryLink> fragmentEntryLinks =
			_fragmentEntryLinkLocalService.getFragmentEntryLinks(
				_group.getGroupId(),
				_portal.getClassNameId(Layout.class.getName()),
				layout.getPlid());

		List<FragmentEntryLink> sortedFragmentEntryLinks = ListUtil.sort(
			fragmentEntryLinks,
			Comparator.comparing(FragmentEntryLink::getFragmentEntryLinkId));

		int fromIndex = 0;

		for (Map<String, List<String>> encodedPortletIdsMap :
				encodedPortletIdsMaps) {

			for (Map.Entry<String, List<String>> entry :
					encodedPortletIdsMap.entrySet()) {

				List<String> portletIds = entry.getValue();

				int numberOfPortletsInColumn = portletIds.size();

				List<FragmentEntryLink> fragmentEntryLinksInColumn =
					sortedFragmentEntryLinks.subList(
						fromIndex, fromIndex + numberOfPortletsInColumn);

				fromIndex = fromIndex + numberOfPortletsInColumn;

				Set<String> existingPortletIds = new HashSet<>();
				List<String> fragmentEntryLinkIdsInColumn = new ArrayList<>();

				for (FragmentEntryLink fragmentEntryLink :
						fragmentEntryLinksInColumn) {

					fragmentEntryLinkIdsInColumn.add(
						String.format(
							"\"%s\"",
							fragmentEntryLink.getFragmentEntryLinkId()));

					JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
						fragmentEntryLink.getEditableValues());

					String portletId = jsonObject.getString("portletId");

					String instanceId = jsonObject.getString("instanceId");

					if (Validator.isNotNull(instanceId)) {
						portletId = PortletIdCodec.encode(
							portletId, instanceId);
					}

					existingPortletIds.add(portletId);
				}

				Assert.assertEquals(
					fragmentEntryLinkIdsInColumn.toString(), portletIds.size(),
					fragmentEntryLinkIdsInColumn.size());

				for (String portletId : portletIds) {
					Assert.assertTrue(existingPortletIds.contains(portletId));
				}

				String fragmentEntryLinkIdsJoined = StringUtil.merge(
					fragmentEntryLinkIdsInColumn, StringPool.COMMA_AND_SPACE);

				expectedLayoutData = StringUtil.replaceFirst(
					expectedLayoutData, "[]",
					String.format("[%s]", fragmentEntryLinkIdsJoined));
			}
		}

		JSONObject expectedLayoutDataJSONObject =
			JSONFactoryUtil.createJSONObject(expectedLayoutData);

		Assert.assertEquals(
			expectedLayoutDataJSONObject.toJSONString(),
			layoutDataJSONObject.toJSONString());
	}

	private void _testConvertNoPortlets(String layoutTemplateId)
		throws Exception {

		_testConvert(layoutTemplateId, new ArrayList<>());
	}

	private void _testConvertOneTwoColumnsMultiplePortlets(
			String layoutTemplateId)
		throws Exception {

		Map<String, String[]> portletIdsMap1 = new TreeMap<String, String[]>() {
			{
				put(
					"column-1",
					new String[] {
						"com_liferay_chart_sample_web_portlet_" +
							"ChartSamplePortlet",
						"com_liferay_chart_sample_web_portlet_" +
							"ChartSamplePortlet"
					});
			}
		};
		Map<String, String[]> portletIdsMap2 = new TreeMap<String, String[]>() {
			{
				put(
					"column-1",
					new String[] {
						"com_liferay_hello_velocity_web_portlet_" +
							"HelloVelocityPortlet",
						"com_liferay_chart_sample_web_portlet_" +
							"ChartSamplePortlet"
					});
				put(
					"column-2",
					new String[] {
						"com_liferay_hello_world_web_portlet_HelloWorldPortlet",
						"com_liferay_chart_sample_web_portlet_" +
							"ChartSamplePortlet"
					});
			}
		};

		List<Map<String, String[]>> portletIdsMaps =
			new ArrayList<Map<String, String[]>>() {
				{
					add(portletIdsMap1);
					add(portletIdsMap2);
				}
			};

		_testConvert(layoutTemplateId, portletIdsMaps);
	}

	private void _testConvertOneTwoColumnsSinglePortlet(String layoutTemplateId)
		throws Exception {

		Map<String, String[]> portletIdsMap1 = new TreeMap<String, String[]>() {
			{
				put(
					"column-1",
					new String[] {
						"com_liferay_chart_sample_web_portlet_" +
							"ChartSamplePortlet"
					});
			}
		};
		Map<String, String[]> portletIdsMap2 = new TreeMap<String, String[]>() {
			{
				put(
					"column-1",
					new String[] {
						"com_liferay_hello_velocity_web_portlet_" +
							"HelloVelocityPortlet"
					});
				put(
					"column-2",
					new String[] {
						"com_liferay_hello_world_web_portlet_HelloWorldPortlet"
					});
			}
		};

		List<Map<String, String[]>> portletIdsMaps =
			new ArrayList<Map<String, String[]>>() {
				{
					add(portletIdsMap1);
					add(portletIdsMap2);
				}
			};

		_testConvert(layoutTemplateId, portletIdsMaps);
	}

	private void _testConvertTwoColumnsMultiplePortlets(String layoutTemplateId)
		throws Exception {

		Map<String, String[]> portletIdsMap = new TreeMap<String, String[]>() {
			{
				put(
					"column-1",
					new String[] {
						"com_liferay_hello_velocity_web_portlet_" +
							"HelloVelocityPortlet",
						"com_liferay_chart_sample_web_portlet_" +
							"ChartSamplePortlet"
					});
				put(
					"column-2",
					new String[] {
						"com_liferay_hello_world_web_portlet_HelloWorldPortlet",
						"hello_soy_portlet"
					});
			}
		};

		List<Map<String, String[]>> portletIdsMaps =
			new ArrayList<Map<String, String[]>>() {
				{
					add(portletIdsMap);
				}
			};

		_testConvert(layoutTemplateId, portletIdsMaps);
	}

	private void _testConvertTwoColumnsSinglePortlet(String layoutTemplateId)
		throws Exception {

		Map<String, String[]> portletIdsMap = new TreeMap<String, String[]>() {
			{
				put(
					"column-1",
					new String[] {
						"com_liferay_hello_velocity_web_portlet_" +
							"HelloVelocityPortlet"
					});
				put(
					"column-2",
					new String[] {
						"com_liferay_hello_world_web_portlet_HelloWorldPortlet"
					});
			}
		};

		List<Map<String, String[]>> portletIdsMaps =
			new ArrayList<Map<String, String[]>>() {
				{
					add(portletIdsMap);
				}
			};

		_testConvert(layoutTemplateId, portletIdsMaps);
	}

	@Inject
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private LayoutConverterRegistry _layoutConverterRegistry;

	@Inject
	private Portal _portal;

	@Inject
	private PortletLocalService _portletLocalService;

}