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

package com.liferay.document.library.web.internal.portlet.action.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletActionRequest;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portletmvc4spring.test.mock.web.portlet.MockActionResponse;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
public class EditEntryMVCActionCommandTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testCheckIn() throws PortalException, PortletException {
		FileEntry initialFileEntry = _dlAppLocalService.addFileEntry(
			null, TestPropsValues.getUserId(), _group.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(), ContentTypes.TEXT_PLAIN, null, null,
			null, ServiceContextTestUtil.getServiceContext());

		_dlAppService.checkOutFileEntry(
			initialFileEntry.getFileEntryId(),
			ServiceContextTestUtil.getServiceContext());

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		_mvcActionCommand.processAction(
			new MockActionRequest(
				company, _group,
				HashMapBuilder.putAll(
					_getParameters(initialFileEntry, Constants.CHECKIN)
				).put(
					"changeLog", new String[] {"New Version"}
				).put(
					"rowIdsFileEntry",
					new String[] {
						String.valueOf(initialFileEntry.getFileEntryId())
					}
				).put(
					"versionIncrease",
					new String[] {String.valueOf(DLVersionNumberIncrease.MAJOR)}
				).build()),
			new MockActionResponse());

		FileEntry actualFileEntry = _dlAppLocalService.getFileEntry(
			initialFileEntry.getFileEntryId());

		FileVersion fileVersion = actualFileEntry.getFileVersion();

		Assert.assertEquals("New Version", fileVersion.getChangeLog());
	}

	@Test
	public void testCheckInAll() throws PortalException, PortletException {
		FileEntry initialFileEntry = _dlAppLocalService.addFileEntry(
			null, TestPropsValues.getUserId(), _group.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(), ContentTypes.TEXT_PLAIN, null, null,
			null, ServiceContextTestUtil.getServiceContext());

		_dlAppService.checkOutFileEntry(
			initialFileEntry.getFileEntryId(),
			ServiceContextTestUtil.getServiceContext());

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		_mvcActionCommand.processAction(
			new MockActionRequest(
				company, _group,
				HashMapBuilder.putAll(
					_getParameters(initialFileEntry, Constants.CHECKIN)
				).put(
					"changeLog", new String[] {"New Version"}
				).put(
					"selectAll", new String[] {String.valueOf(Boolean.TRUE)}
				).put(
					"versionIncrease",
					new String[] {String.valueOf(DLVersionNumberIncrease.MAJOR)}
				).build()),
			new MockActionResponse());

		FileEntry actualFileEntry = _dlAppLocalService.getFileEntry(
			initialFileEntry.getFileEntryId());

		FileVersion fileVersion = actualFileEntry.getFileVersion();

		Assert.assertEquals("New Version", fileVersion.getChangeLog());
	}

	@Test
	public void testCheckOut() throws PortalException, PortletException {
		FileEntry initialFileEntry = _dlAppLocalService.addFileEntry(
			null, TestPropsValues.getUserId(), _group.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(), ContentTypes.TEXT_PLAIN, null, null,
			null, ServiceContextTestUtil.getServiceContext());

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		_mvcActionCommand.processAction(
			new MockActionRequest(
				company, _group,
				HashMapBuilder.putAll(
					_getParameters(initialFileEntry, Constants.CHECKOUT)
				).put(
					"rowIdsFileEntry",
					new String[] {
						String.valueOf(initialFileEntry.getFileEntryId())
					}
				).build()),
			new MockActionResponse());

		FileEntry actualFileEntry = _dlAppLocalService.getFileEntry(
			initialFileEntry.getFileEntryId());

		Assert.assertTrue(actualFileEntry.isCheckedOut());
	}

	@Test
	public void testCheckOutAll() throws PortalException, PortletException {
		FileEntry initialFileEntry = _dlAppLocalService.addFileEntry(
			null, TestPropsValues.getUserId(), _group.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(), ContentTypes.TEXT_PLAIN, null, null,
			null, ServiceContextTestUtil.getServiceContext());

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		_mvcActionCommand.processAction(
			new MockActionRequest(
				company, _group,
				HashMapBuilder.putAll(
					_getParameters(initialFileEntry, Constants.CHECKOUT)
				).put(
					"selectAll", new String[] {String.valueOf(Boolean.TRUE)}
				).build()),
			new MockActionResponse());

		FileEntry actualFileEntry = _dlAppLocalService.getFileEntry(
			initialFileEntry.getFileEntryId());

		Assert.assertTrue(actualFileEntry.isCheckedOut());
	}

	private Map<String, String[]> _getParameters(
		FileEntry tempFileEntry, String cmd) {

		return HashMapBuilder.put(
			Constants.CMD, new String[] {cmd}
		).put(
			"folderId",
			new String[] {String.valueOf(tempFileEntry.getFolderId())}
		).put(
			"repositoryId",
			new String[] {String.valueOf(tempFileEntry.getRepositoryId())}
		).build();
	}

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private DLAppLocalService _dlAppLocalService;

	@Inject
	private DLAppService _dlAppService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject(
		filter = "component.name=com.liferay.document.library.web.internal.portlet.action.EditEntryMVCActionCommand"
	)
	private MVCActionCommand _mvcActionCommand;

	private static class MockActionRequest
		extends MockLiferayPortletActionRequest {

		public MockActionRequest(
			Company company, Group group, Map<String, String[]> parameters) {

			_company = company;
			_group = group;
			_parameters = Collections.unmodifiableMap(parameters);
		}

		@Override
		public Object getAttribute(String name) {
			if (Objects.equals(name, WebKeys.THEME_DISPLAY)) {
				try {
					return _getThemeDisplay();
				}
				catch (PortalException portalException) {
					throw new AssertionError(portalException);
				}
			}

			return super.getAttribute(name);
		}

		@Override
		public String getParameter(String name) {
			String[] values = _parameters.get(name);

			if (values == null) {
				return StringPool.BLANK;
			}

			return values[0];
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			return _parameters;
		}

		private ThemeDisplay _getThemeDisplay() throws PortalException {
			ThemeDisplay themeDisplay = new ThemeDisplay();

			themeDisplay.setCompany(_company);
			themeDisplay.setPermissionChecker(
				PermissionThreadLocal.getPermissionChecker());
			themeDisplay.setRequest(new MockHttpServletRequest());
			themeDisplay.setScopeGroupId(_group.getGroupId());
			themeDisplay.setServerName("localhost");
			themeDisplay.setServerPort(8080);
			themeDisplay.setSiteGroupId(_group.getGroupId());
			themeDisplay.setUser(TestPropsValues.getUser());

			return themeDisplay;
		}

		private final Company _company;
		private Group _group;
		private final Map<String, String[]> _parameters;

	}

}