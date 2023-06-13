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

package com.liferay.batch.engine.internal.auto.deploy;

import com.liferay.batch.engine.BatchEngineImportTaskExecutor;
import com.liferay.batch.engine.model.BatchEngineImportTask;
import com.liferay.batch.engine.model.impl.BatchEngineImportTaskImpl;
import com.liferay.batch.engine.service.BatchEngineImportTaskLocalService;
import com.liferay.petra.concurrent.NoticeableExecutorService;
import com.liferay.petra.executor.PortalExecutorManager;
import com.liferay.portal.kernel.deploy.auto.AutoDeployException;
import com.liferay.portal.kernel.deploy.auto.AutoDeployer;
import com.liferay.portal.kernel.deploy.auto.context.AutoDeploymentContext;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.util.FileImpl;

import java.io.File;
import java.io.Serializable;

import java.net.URL;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Raymond Augé
 */
public class BatchEngineAutoDeployListenerTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		ReflectionTestUtil.setFieldValue(
			_batchEngineAutoDeployListener, "_batchEngineImportTaskExecutor",
			new BatchEngineImportTaskExecutor() {

				@Override
				public void execute(
					BatchEngineImportTask batchEngineImportTask) {

					_batchEngineImportTasks.add(batchEngineImportTask);
				}

			});
		ReflectionTestUtil.setFieldValue(
			_batchEngineAutoDeployListener,
			"_batchEngineImportTaskLocalService",
			_batchEngineImportTaskLocalService);
		ReflectionTestUtil.setFieldValue(
			_batchEngineAutoDeployListener, "_file", FileImpl.getInstance());
		ReflectionTestUtil.setFieldValue(
			_batchEngineAutoDeployListener, "_portalExecutorManager",
			_portalExecutorManager);

		Mockito.when(
			_batchEngineImportTaskLocalService.addBatchEngineImportTask(
				AdditionalMatchers.or(Mockito.isNull(), Mockito.anyString()),
				Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(),
				AdditionalMatchers.or(Mockito.isNull(), Mockito.anyString()),
				Mockito.anyString(), Mockito.any(byte[].class),
				Mockito.anyString(), Mockito.anyString(),
				AdditionalMatchers.or(
					Mockito.isNull(), Mockito.<String, String>anyMap()),
				Mockito.anyInt(), Mockito.anyString(),
				AdditionalMatchers.or(
					Mockito.isNull(), Mockito.<String, Serializable>anyMap()),
				AdditionalMatchers.or(Mockito.isNull(), Mockito.anyString()))
		).then(
			new Answer<BatchEngineImportTask>() {

				public BatchEngineImportTask answer(
						InvocationOnMock invocationOnMock)
					throws Throwable {

					BatchEngineImportTask batchEngineImportTask =
						new BatchEngineImportTaskImpl();

					batchEngineImportTask.setCompanyId(
						(long)invocationOnMock.getArguments()[2]);
					batchEngineImportTask.setBatchSize(
						(long)invocationOnMock.getArguments()[3]);
					batchEngineImportTask.setCallbackURL(
						(String)invocationOnMock.getArguments()[4]);
					batchEngineImportTask.setClassName(
						(String)invocationOnMock.getArguments()[5]);
					batchEngineImportTask.setContentType(
						(String)invocationOnMock.getArguments()[7]);
					batchEngineImportTask.setExecuteStatus(
						(String)invocationOnMock.getArguments()[8]);
					batchEngineImportTask.setExternalReferenceCode(
						(String)invocationOnMock.getArguments()[0]);
					batchEngineImportTask.setFieldNameMapping(
						(Map<String, Serializable>)
							invocationOnMock.getArguments()[9]);
					batchEngineImportTask.setImportStrategy(
						(int)invocationOnMock.getArguments()[10]);
					batchEngineImportTask.setOperation(
						(String)invocationOnMock.getArguments()[11]);
					batchEngineImportTask.setParameters(
						(Map<String, Serializable>)
							invocationOnMock.getArguments()[9]);
					batchEngineImportTask.setTaskItemDelegateName(
						(String)invocationOnMock.getArguments()[12]);

					return batchEngineImportTask;
				}

			}
		);

		Mockito.when(
			_portalExecutorManager.getPortalExecutor(Mockito.anyString())
		).thenReturn(
			_noticeableExecutorService
		);

		Mockito.when(
			_noticeableExecutorService.submit(Mockito.any(Runnable.class))
		).then(
			new Answer<Future<?>>() {

				public Future<?> answer(InvocationOnMock invocationOnMock) {
					Runnable runnable = (Runnable)invocationOnMock.getArgument(
						0);

					runnable.run();

					return null;
				}

			}
		);
	}

	@Test
	public void testWithEmptyZip() throws Exception {
		AutoDeploymentContext autoDeploymentContext =
			new AutoDeploymentContext();

		autoDeploymentContext.setFile(_getFile("batch0.zip"));

		boolean deployable = _batchEngineAutoDeployListener.isDeployable(
			autoDeploymentContext);

		Assert.assertFalse(deployable);

		int result = _batchEngineAutoDeployListener.deploy(
			autoDeploymentContext);

		Assert.assertEquals(AutoDeployer.CODE_DEFAULT, result);

		Mockito.verify(
			_noticeableExecutorService, Mockito.times(0)
		).submit(
			Mockito.any(Runnable.class)
		);

		Assert.assertTrue(_batchEngineImportTasks.isEmpty());
	}

	@Test
	public void testWithHierarchy() throws Exception {
		AutoDeploymentContext autoDeploymentContext =
			new AutoDeploymentContext();

		autoDeploymentContext.setFile(_getFile("batch4.zip"));

		boolean deployable = _batchEngineAutoDeployListener.isDeployable(
			autoDeploymentContext);

		Assert.assertTrue(deployable);

		int result = _batchEngineAutoDeployListener.deploy(
			autoDeploymentContext);

		Assert.assertEquals(AutoDeployer.CODE_DEFAULT, result);

		Mockito.verify(
			_noticeableExecutorService, Mockito.times(3)
		).submit(
			Mockito.any(Runnable.class)
		);

		Assert.assertEquals(
			_batchEngineImportTasks.toString(), 3,
			_batchEngineImportTasks.size());
	}

	@Test
	public void testWithMissingData() throws Exception {
		AutoDeploymentContext autoDeploymentContext =
			new AutoDeploymentContext();

		autoDeploymentContext.setFile(_getFile("batch2.zip"));

		boolean deployable = _batchEngineAutoDeployListener.isDeployable(
			autoDeploymentContext);

		Assert.assertFalse(deployable);

		try {
			_batchEngineAutoDeployListener.deploy(autoDeploymentContext);

			Assert.fail();
		}
		catch (AutoDeployException autoDeployException) {
			Throwable throwable = autoDeployException.getCause();

			Assert.assertEquals(
				IllegalStateException.class, throwable.getClass());

			String message = throwable.getMessage();

			Assert.assertTrue(message.startsWith("Invalid batch engine file"));
		}

		Mockito.verify(
			_noticeableExecutorService, Mockito.times(0)
		).submit(
			Mockito.any(Runnable.class)
		);

		Assert.assertTrue(_batchEngineImportTasks.isEmpty());
	}

	@Test
	public void testWithMultiData() throws Exception {
		AutoDeploymentContext autoDeploymentContext =
			new AutoDeploymentContext();

		autoDeploymentContext.setFile(_getFile("batch3.zip"));

		boolean deployable = _batchEngineAutoDeployListener.isDeployable(
			autoDeploymentContext);

		Assert.assertTrue(deployable);

		int result = _batchEngineAutoDeployListener.deploy(
			autoDeploymentContext);

		Assert.assertEquals(AutoDeployer.CODE_DEFAULT, result);

		Mockito.verify(
			_noticeableExecutorService, Mockito.times(2)
		).submit(
			Mockito.any(Runnable.class)
		);

		Assert.assertEquals(
			_batchEngineImportTasks.toString(), 2,
			_batchEngineImportTasks.size());
	}

	@Test
	public void testWithSingleData() throws Exception {
		AutoDeploymentContext autoDeploymentContext =
			new AutoDeploymentContext();

		autoDeploymentContext.setFile(_getFile("batch1.zip"));

		boolean deployable = _batchEngineAutoDeployListener.isDeployable(
			autoDeploymentContext);

		Assert.assertTrue(deployable);

		int result = _batchEngineAutoDeployListener.deploy(
			autoDeploymentContext);

		Assert.assertEquals(AutoDeployer.CODE_DEFAULT, result);

		Mockito.verify(
			_noticeableExecutorService, Mockito.times(1)
		).submit(
			Mockito.any(Runnable.class)
		);

		Assert.assertEquals(
			_batchEngineImportTasks.toString(), 1,
			_batchEngineImportTasks.size());
	}

	private File _getFile(String fileName) throws Exception {
		URL url = BatchEngineAutoDeployListenerTest.class.getResource(fileName);

		Assert.assertEquals("file", url.getProtocol());

		Path path = Paths.get(url.toURI());

		return path.toFile();
	}

	private final BatchEngineAutoDeployListener _batchEngineAutoDeployListener =
		new BatchEngineAutoDeployListener();

	@Mock
	private BatchEngineImportTaskLocalService
		_batchEngineImportTaskLocalService;

	private final List<BatchEngineImportTask> _batchEngineImportTasks =
		new ArrayList<>();

	@Mock
	private NoticeableExecutorService _noticeableExecutorService;

	@Mock
	private PortalExecutorManager _portalExecutorManager;

}