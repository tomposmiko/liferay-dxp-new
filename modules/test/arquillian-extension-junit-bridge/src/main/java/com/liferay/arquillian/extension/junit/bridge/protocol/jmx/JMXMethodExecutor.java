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

package com.liferay.arquillian.extension.junit.bridge.protocol.jmx;

import com.liferay.petra.io.unsync.UnsyncByteArrayInputStream;

import java.io.InputStream;
import java.io.ObjectInputStream;

import java.lang.reflect.Method;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.jboss.arquillian.container.test.spi.ContainerMethodExecutor;
import org.jboss.arquillian.test.spi.TestMethodExecutor;
import org.jboss.arquillian.test.spi.TestResult;

/**
 * @author Matthew Tambara
 */
public class JMXMethodExecutor implements ContainerMethodExecutor {

	public JMXMethodExecutor(MBeanServerConnection mBeanServerConnection) {
		_jmxTestRunnerMBean = MBeanServerInvocationHandler.newProxyInstance(
			mBeanServerConnection, _objectName, JMXTestRunnerMBean.class,
			false);
	}

	@Override
	public TestResult invoke(TestMethodExecutor testMethodExecutor) {
		Object instance = testMethodExecutor.getInstance();

		Class<?> testClass = instance.getClass();

		Method method = testMethodExecutor.getMethod();

		try {
			byte[] data = _jmxTestRunnerMBean.runTestMethod(
				testClass.getName(), method.getName());

			try (InputStream inputStream = new UnsyncByteArrayInputStream(data);
				ObjectInputStream oos = new ObjectInputStream(inputStream)) {

				TestResult testResult = (TestResult)oos.readObject();

				testResult.setEnd(System.currentTimeMillis());

				return testResult;
			}
		}
		catch (Throwable t) {
			TestResult testResult = TestResult.failed(t);

			testResult.setEnd(System.currentTimeMillis());

			return testResult;
		}
	}

	private static final ObjectName _objectName;

	static {
		try {
			_objectName = new ObjectName(JMXTestRunnerMBean.OBJECT_NAME);
		}
		catch (MalformedObjectNameException mone) {
			throw new ExceptionInInitializerError(mone);
		}
	}

	private final JMXTestRunnerMBean _jmxTestRunnerMBean;

}