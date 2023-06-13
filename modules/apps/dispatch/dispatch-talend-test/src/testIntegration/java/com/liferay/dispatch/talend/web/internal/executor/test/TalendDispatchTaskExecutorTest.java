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

package com.liferay.dispatch.talend.web.internal.executor.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dispatch.constants.DispatchConstants;
import com.liferay.dispatch.executor.DispatchTaskClusterMode;
import com.liferay.dispatch.executor.DispatchTaskStatus;
import com.liferay.dispatch.model.DispatchLog;
import com.liferay.dispatch.model.DispatchTrigger;
import com.liferay.dispatch.repository.DispatchFileRepository;
import com.liferay.dispatch.service.DispatchLogLocalService;
import com.liferay.dispatch.service.DispatchTriggerLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DataGuard;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Igor Beslic
 */
@DataGuard(scope = DataGuard.Scope.METHOD)
@RunWith(Arquillian.class)
public class TalendDispatchTaskExecutorTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE,
			SynchronousDestinationTestRule.INSTANCE);

	@Test
	public void testExecute() throws Exception {
		DispatchTrigger dispatchTrigger =
			_dispatchTriggerLocalService.addDispatchTrigger(
				null, TestPropsValues.getUserId(), "talend",
				new UnicodeProperties(), "TalendDispatchTrigger", false);

		_dispatchFileRepository.addFileEntry(
			dispatchTrigger.getUserId(), dispatchTrigger.getDispatchTriggerId(),
			_TALEND_CONTEXT_PRINTER_SAMPLE_ZIP, 0, "application/zip",
			TalendDispatchTaskExecutorTest.class.getResourceAsStream(
				"/" + _TALEND_CONTEXT_PRINTER_SAMPLE_ZIP));

		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR) + 1;

		dispatchTrigger = _dispatchTriggerLocalService.updateDispatchTrigger(
			dispatchTrigger.getDispatchTriggerId(), false, "* * * * * *",
			DispatchTaskClusterMode.SINGLE_NODE_PERSISTED, 5, 5, year, 11, 11,
			false, false, 4, 4, year, 0, 0, "UTC");

		_simulateSchedulerEvent(dispatchTrigger.getDispatchTriggerId());

		List<DispatchLog> dispatchLogs =
			_dispatchLogLocalService.getDispatchLogs(
				dispatchTrigger.getDispatchTriggerId(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS);

		DispatchLog dispatchLog = dispatchLogs.get(0);

		Assert.assertEquals(
			DispatchTaskStatus.SUCCESSFUL,
			DispatchTaskStatus.valueOf(dispatchLog.getStatus()));
	}

	@Test
	public void testExecuteLiferayOutputBlog() throws Exception {
		Group group = GroupTestUtil.addGroup();

		User user = _getSiteAdministratorUser(group.getGroupId());

		DispatchTrigger dispatchTrigger =
			_dispatchTriggerLocalService.addDispatchTrigger(
				null, TestPropsValues.getUserId(), "talend",
				UnicodePropertiesBuilder.put(
					"liferayUser", user.getEmailAddress()
				).put(
					"liferayUserPassword", user.getPasswordUnencrypted()
				).put(
					"siteId", group.getGroupId()
				).build(),
				"TalendDispatchTrigger", false);

		_dispatchFileRepository.addFileEntry(
			dispatchTrigger.getUserId(), dispatchTrigger.getDispatchTriggerId(),
			_TALEND_LIFERAY_OUTPUT_BLOG_SAMPLE_ZIP, 0, "application/zip",
			TalendDispatchTaskExecutorTest.class.getResourceAsStream(
				"/" + _TALEND_LIFERAY_OUTPUT_BLOG_SAMPLE_ZIP));

		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR) + 1;

		dispatchTrigger = _dispatchTriggerLocalService.updateDispatchTrigger(
			dispatchTrigger.getDispatchTriggerId(), false, "* * * * * *",
			DispatchTaskClusterMode.SINGLE_NODE_PERSISTED, 5, 5, year, 11, 11,
			false, false, 4, 4, year, 0, 0, "UTC");

		_simulateSchedulerEvent(dispatchTrigger.getDispatchTriggerId());

		List<DispatchLog> dispatchLogs =
			_dispatchLogLocalService.getDispatchLogs(
				dispatchTrigger.getDispatchTriggerId(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS);

		DispatchLog dispatchLog = dispatchLogs.get(0);

		Assert.assertEquals(
			DispatchTaskStatus.SUCCESSFUL,
			DispatchTaskStatus.valueOf(dispatchLog.getStatus()));
	}

	private User _getSiteAdministratorUser(long groupId) throws Exception {
		User user = UserTestUtil.addUser(
			TestPropsValues.getCompanyId(), TestPropsValues.getUserId(),
			RandomTestUtil.randomString(),
			RandomTestUtil.randomString() + "@liferay.com",
			RandomTestUtil.randomString(), LocaleUtil.getDefault(),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(), null,
			ServiceContextTestUtil.getServiceContext());

		UserLocalServiceUtil.updateEmailAddressVerified(user.getUserId(), true);

		Role role = RoleLocalServiceUtil.getRole(
			TestPropsValues.getCompanyId(), RoleConstants.SITE_ADMINISTRATOR);

		UserGroupRoleLocalServiceUtil.addUserGroupRoles(
			new long[] {user.getUserId()}, groupId, role.getRoleId());

		return user;
	}

	private void _simulateSchedulerEvent(long dispatchTriggerId)
		throws Exception {

		Message message = new Message();

		message.setPayload(
			String.format("{\"dispatchTriggerId\": %d}", dispatchTriggerId));

		_messageListener.receive(message);
	}

	private static final String _TALEND_CONTEXT_PRINTER_SAMPLE_ZIP =
		"etl-talend-context-printer-sample-1.0.zip";

	private static final String _TALEND_LIFERAY_OUTPUT_BLOG_SAMPLE_ZIP =
		"etl-talend-liferay-output-blog-sample-1.0.zip";

	@Inject
	private DispatchFileRepository _dispatchFileRepository;

	@Inject
	private DispatchLogLocalService _dispatchLogLocalService;

	@Inject
	private DispatchTriggerLocalService _dispatchTriggerLocalService;

	@Inject(
		filter = "destination.name=" + DispatchConstants.EXECUTOR_DESTINATION_NAME
	)
	private MessageListener _messageListener;

}