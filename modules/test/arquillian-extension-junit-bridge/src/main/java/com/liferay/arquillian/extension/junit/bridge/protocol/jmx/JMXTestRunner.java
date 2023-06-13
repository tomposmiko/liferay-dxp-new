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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.management.NotificationBroadcasterSupport;

import org.jboss.arquillian.container.test.spi.TestRunner;
import org.jboss.arquillian.container.test.spi.util.TestRunners;
import org.jboss.arquillian.test.spi.TestResult;

/**
 * @author Matthew Tambara
 */
public class JMXTestRunner
	extends NotificationBroadcasterSupport implements JMXTestRunnerMBean {

	public JMXTestRunner(ClassLoader classLoader) {
		_classLoader = classLoader;
	}

	@Override
	public byte[] runTestMethod(String className, String methodName) {
		try {
			TestRunner testRunner = TestRunners.getTestRunner(
				JMXTestRunner.class.getClassLoader());

			Class<?> testClass = _classLoader.loadClass(className);

			return _toByteArray(testRunner.execute(testClass, methodName));
		}
		catch (ClassNotFoundException cnfe) {
			TestResult testResult = TestResult.failed(cnfe);

			testResult.setEnd(System.currentTimeMillis());

			return _toByteArray(testResult);
		}
	}

	private static byte[] _toByteArray(TestResult testResult) {
		try (ByteArrayOutputStream byteArrayOutputStream =
				new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream)) {

			objectOutputStream.writeObject(testResult);

			objectOutputStream.flush();

			return byteArrayOutputStream.toByteArray();
		}
		catch (IOException ioe) {
			throw new RuntimeException(
				"Unable to serialize object: " + testResult, ioe);
		}
	}

	private final ClassLoader _classLoader;

}