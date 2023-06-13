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

package com.liferay.layout.content.page.editor.web.internal.portlet.action.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.blogs.constants.BlogsPortletKeys;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentCollectionLocalService;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.PortletIdException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletActionRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletActionResponse;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.io.InputStream;

import java.util.List;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Jürgen Kappler
 */
@RunWith(Arquillian.class)
@Sync
public class AddPortletReactMVCActionCommandTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_company = _companyLocalService.getCompany(_group.getCompanyId());

		_layout = LayoutTestUtil.addLayout(_group);

		_layoutPageTemplateStructure =
			_layoutPageTemplateStructureLocalService.
				addLayoutPageTemplateStructure(
					TestPropsValues.getUserId(), _group.getGroupId(),
					_portal.getClassNameId(Layout.class.getName()),
					_layout.getPlid(), _read("layout_data.json"),
					ServiceContextTestUtil.getServiceContext(
						_group, TestPropsValues.getUserId()));
	}

	@Test
	public void testCanAddFragmentEntryLinkFromWidget() throws Exception {
		List<FragmentEntryLink> originalFragmentEntryLinks =
			_fragmentEntryLinkLocalService.getFragmentEntryLinks(
				_group.getGroupId(),
				_portal.getClassNameId(Layout.class.getName()),
				_layout.getPlid());

		MockLiferayPortletActionRequest actionRequest = _getMockActionRequest();

		actionRequest.addParameter("portletId", JournalPortletKeys.JOURNAL);

		ReflectionTestUtil.invoke(
			_mvcActionCommand, "processAddPortlet",
			new Class<?>[] {ActionRequest.class, ActionResponse.class},
			actionRequest, new MockActionResponse());

		List<FragmentEntryLink> actualFragmentEntryLinks =
			_fragmentEntryLinkLocalService.getFragmentEntryLinks(
				_group.getGroupId(),
				_portal.getClassNameId(Layout.class.getName()),
				_layout.getPlid());

		Assert.assertEquals(
			actualFragmentEntryLinks.toString(),
			originalFragmentEntryLinks.size() + 1,
			actualFragmentEntryLinks.size());
	}

	@Test(expected = PrincipalException.MustHavePermission.class)
	public void testCannotAddInvalidWidgets() throws Exception {
		MockLiferayPortletActionRequest actionRequest = _getMockActionRequest();

		actionRequest.addParameter("portletId", RandomTestUtil.randomString());

		ReflectionTestUtil.invoke(
			_mvcActionCommand, "processAddPortlet",
			new Class<?>[] {ActionRequest.class, ActionResponse.class},
			actionRequest, new MockActionResponse());
	}

	@Test(expected = PortletIdException.class)
	public void testCannotAddMultipleUninstanceableWidgets() throws Exception {
		MockLiferayPortletActionRequest actionRequest = _getMockActionRequest();

		actionRequest.addParameter("portletId", BlogsPortletKeys.BLOGS);

		ReflectionTestUtil.invoke(
			_mvcActionCommand, "processAddPortlet",
			new Class<?>[] {ActionRequest.class, ActionResponse.class},
			actionRequest, new MockActionResponse());

		JSONObject jsonObject = ReflectionTestUtil.invoke(
			_mvcActionCommand, "processAddPortlet",
			new Class<?>[] {ActionRequest.class, ActionResponse.class},
			actionRequest, new MockActionResponse());

		Assert.assertTrue(jsonObject.has("error"));
	}

	@Test
	public void testFragmentEntryLinkFromWidgetResponse() throws Exception {
		MockLiferayPortletActionRequest actionRequest = _getMockActionRequest();

		actionRequest.addParameter("portletId", JournalPortletKeys.JOURNAL);

		JSONObject jsonObject = ReflectionTestUtil.invoke(
			_mvcActionCommand, "processAddPortlet",
			new Class<?>[] {ActionRequest.class, ActionResponse.class},
			actionRequest, new MockActionResponse());

		JSONObject fragmentEntryLinkJSONObject = jsonObject.getJSONObject(
			"fragmentEntryLink");

		Assert.assertNotNull(fragmentEntryLinkJSONObject);

		Assert.assertTrue(
			fragmentEntryLinkJSONObject.has("fragmentEntryLinkId"));

		long fragmentEntryLinkId = fragmentEntryLinkJSONObject.getLong(
			"fragmentEntryLinkId");

		FragmentEntryLink fragmentEntryLink =
			_fragmentEntryLinkLocalService.fetchFragmentEntryLink(
				fragmentEntryLinkId);

		Assert.assertNotNull(fragmentEntryLink);

		String editableValues = fragmentEntryLink.getEditableValues();

		Assert.assertTrue(editableValues.contains(JournalPortletKeys.JOURNAL));

		Locale defaultLocale = _portal.getSiteDefaultLocale(_group);

		String expectedTitle = _portal.getPortletTitle(
			JournalPortletKeys.JOURNAL, defaultLocale);

		Assert.assertEquals(
			expectedTitle, fragmentEntryLinkJSONObject.getString("name"));
	}

	@Test
	public void testLayoutDataFromWidgetResponse() throws Exception {
		MockLiferayPortletActionRequest actionRequest = _getMockActionRequest();

		actionRequest.addParameter("itemType", RandomTestUtil.randomString());
		actionRequest.addParameter("parentItemId", "root");
		actionRequest.addParameter("portletId", JournalPortletKeys.JOURNAL);
		actionRequest.addParameter("position", "0");

		JSONObject jsonObject = ReflectionTestUtil.invoke(
			_mvcActionCommand, "processAddPortlet",
			new Class<?>[] {ActionRequest.class, ActionResponse.class},
			actionRequest, new MockActionResponse());

		JSONObject layoutDataJSONObject = jsonObject.getJSONObject(
			"layoutData");

		JSONObject fragmentEntryLinkJSONObject = jsonObject.getJSONObject(
			"fragmentEntryLink");

		String fragmentEntryLinkId = fragmentEntryLinkJSONObject.getString(
			"fragmentEntryLinkId");

		JSONObject itemsJSONObject = layoutDataJSONObject.getJSONObject(
			"items");

		for (String key : itemsJSONObject.keySet()) {
			if (key.equals("root")) {
				continue;
			}

			JSONObject newItemJSONObject = itemsJSONObject.getJSONObject(key);

			JSONObject configJSONObject = newItemJSONObject.getJSONObject(
				"config");

			Assert.assertEquals(
				fragmentEntryLinkId,
				configJSONObject.getString("fragmentEntryLinkId"));

			Assert.assertEquals(
				"root", newItemJSONObject.getString("parentId"));

			JSONObject rootItemJSONObject = itemsJSONObject.getJSONObject(
				"root");

			JSONArray childrenJSONArray = rootItemJSONObject.getJSONArray(
				"children");

			Assert.assertEquals(
				newItemJSONObject.getString("itemId"),
				childrenJSONArray.getString(0));
		}
	}

	private MockActionRequest _getMockActionRequest() throws PortalException {
		ThemeDisplay themeDisplay = _getThemeDisplay();

		MockActionRequest mockActionRequest = new MockActionRequest(
			_layout, themeDisplay);

		mockActionRequest.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);
		mockActionRequest.addParameter(
			"groupId", String.valueOf(_group.getGroupId()));
		mockActionRequest.addParameter(
			"classNameId",
			String.valueOf(_portal.getClassNameId(Layout.class.getName())));
		mockActionRequest.addParameter(
			"classPK", String.valueOf(_layout.getPlid()));

		return mockActionRequest;
	}

	private ThemeDisplay _getThemeDisplay() throws PortalException {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(_company);
		themeDisplay.setLayout(_layout);
		themeDisplay.setLayoutSet(_layout.getLayoutSet());
		themeDisplay.setLayoutTypePortlet(
			(LayoutTypePortlet)_layout.getLayoutType());
		themeDisplay.setLocale(_portal.getSiteDefaultLocale(_group));

		LayoutSet layoutSet = _group.getPublicLayoutSet();

		themeDisplay.setLookAndFeel(layoutSet.getTheme(), null);

		themeDisplay.setPermissionChecker(
			PermissionThreadLocal.getPermissionChecker());
		themeDisplay.setPlid(_layout.getPlid());
		themeDisplay.setRealUser(TestPropsValues.getUser());
		themeDisplay.setScopeGroupId(_group.getGroupId());
		themeDisplay.setSiteGroupId(_group.getGroupId());
		themeDisplay.setUser(TestPropsValues.getUser());

		return themeDisplay;
	}

	private String _read(String fileName) throws Exception {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/" + fileName);

		return StringUtil.read(inputStream);
	}

	private Company _company;

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private FragmentCollectionLocalService _fragmentCollectionLocalService;

	@Inject
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Inject
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@DeleteAfterTestRun
	private Group _group;

	private Layout _layout;

	@DeleteAfterTestRun
	private LayoutPageTemplateStructure _layoutPageTemplateStructure;

	@Inject
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Inject(filter = "mvc.command.name=/content_layout/add_portlet_react")
	private MVCActionCommand _mvcActionCommand;

	@Inject
	private Portal _portal;

	private static class MockActionRequest
		extends MockLiferayPortletActionRequest {

		public MockActionRequest(Layout layout, ThemeDisplay themeDisplay) {
			_layout = layout;
			_themeDisplay = themeDisplay;
		}

		@Override
		public HttpServletRequest getHttpServletRequest() {
			MockHttpServletRequest httpServletRequest =
				new MockHttpServletRequest();

			httpServletRequest.setAttribute(
				JavaConstants.JAVAX_PORTLET_RESPONSE, new MockActionResponse());
			httpServletRequest.setAttribute(WebKeys.LAYOUT, _layout);
			httpServletRequest.setAttribute(
				WebKeys.THEME_DISPLAY, _themeDisplay);

			return httpServletRequest;
		}

		private final Layout _layout;
		private final ThemeDisplay _themeDisplay;

	}

	private static class MockActionResponse
		extends MockLiferayPortletActionResponse {

		@Override
		public HttpServletResponse getHttpServletResponse() {
			return new MockHttpServletResponse();
		}

	}

}