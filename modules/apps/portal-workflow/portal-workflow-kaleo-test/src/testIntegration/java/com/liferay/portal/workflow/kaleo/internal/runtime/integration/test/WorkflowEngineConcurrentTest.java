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

package com.liferay.portal.workflow.kaleo.internal.runtime.integration.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.scheduler.SchedulerEngine;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.kernel.workflow.WorkflowDefinitionManager;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.workflow.kaleo.model.KaleoTimerInstanceToken;
import com.liferay.portal.workflow.kaleo.runtime.WorkflowEngine;
import com.liferay.portal.workflow.kaleo.runtime.util.WorkflowContextUtil;
import com.liferay.portal.workflow.kaleo.service.KaleoTimerInstanceTokenLocalService;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Dante Wang
 */
@RunWith(Arquillian.class)
public class WorkflowEngineConcurrentTest extends BaseWorkflowManagerTestCase {

	@Test
	public void testConcurrentExecuteTimerWorkflowInstance() throws Exception {
		WorkflowDefinition workflowDefinition =
			_workflowDefinitionManager.deployWorkflowDefinition(
				TestPropsValues.getCompanyId(), TestPropsValues.getUserId(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				FileUtil.getBytes(
					getResourceInputStream(
						"multiple-timer-workflow-definition.xml")));

		try (AutoCloseable autoCloseable1 = registryWorkflowHandler(
				workflowDefinition);
			AutoCloseable autoCloseable2 = _swapSchedulerEngine()) {

			Class<?> clazz = getClass();

			WorkflowHandlerRegistryUtil.startWorkflowInstance(
				TestPropsValues.getCompanyId(), 0, TestPropsValues.getUserId(),
				clazz.getName(), 1, null, new ServiceContext());

			Assert.assertEquals(
				_kaleoTimerInstanceTokenIds.toString(), 2,
				_kaleoTimerInstanceTokenIds.size());

			CountDownLatch countDownLatch = new CountDownLatch(1);

			FutureTask<?> futureTask1 = new FutureTask<>(
				_getCallable(
					countDownLatch, _kaleoTimerInstanceTokenIds.get(0)));
			FutureTask<?> futureTask2 = new FutureTask<>(
				_getCallable(
					countDownLatch, _kaleoTimerInstanceTokenIds.get(1)));

			Thread thread1 = new Thread(futureTask1);
			Thread thread2 = new Thread(futureTask2);

			thread1.start();
			thread2.start();

			countDownLatch.countDown();

			Assert.assertNotNull(futureTask1.get());
			Assert.assertNotNull(futureTask2.get());
		}
	}

	private Callable<?> _getCallable(
			CountDownLatch countDownLatch, long kaleoTimerInstanceTokenId)
		throws Exception {

		KaleoTimerInstanceToken kaleoTimerInstanceToken =
			_kaleoTimerInstanceTokenLocalService.getKaleoTimerInstanceToken(
				kaleoTimerInstanceTokenId);

		Map<String, Serializable> workflowContext = WorkflowContextUtil.convert(
			kaleoTimerInstanceToken.getWorkflowContext());

		ServiceContext serviceContext = (ServiceContext)workflowContext.get(
			WorkflowConstants.CONTEXT_SERVICE_CONTEXT);

		return () -> {
			countDownLatch.await();

			return _workflowEngine.executeTimerWorkflowInstance(
				kaleoTimerInstanceTokenId, serviceContext, workflowContext);
		};
	}

	private AutoCloseable _swapSchedulerEngine() {
		SchedulerEngine schedulerEngine =
			ReflectionTestUtil.getAndSetFieldValue(
				_schedulerEngineHelper, "_schedulerEngine",
				(SchedulerEngine)ProxyUtil.newProxyInstance(
					SchedulerEngine.class.getClassLoader(),
					new Class<?>[] {SchedulerEngine.class},
					(proxy, method, args) -> {
						if (Objects.equals(method.getName(), "schedule")) {
							Message message = (Message)args[3];

							_kaleoTimerInstanceTokenIds.add(
								message.getLong("kaleoTimerInstanceTokenId"));
						}

						return null;
					}));

		return () -> ReflectionTestUtil.setFieldValue(
			_schedulerEngineHelper, "_schedulerEngine", schedulerEngine);
	}

	private final List<Long> _kaleoTimerInstanceTokenIds = new ArrayList<>();

	@Inject
	private KaleoTimerInstanceTokenLocalService
		_kaleoTimerInstanceTokenLocalService;

	@Inject
	private SchedulerEngineHelper _schedulerEngineHelper;

	@Inject
	private WorkflowDefinitionManager _workflowDefinitionManager;

	@Inject
	private WorkflowEngine _workflowEngine;

}