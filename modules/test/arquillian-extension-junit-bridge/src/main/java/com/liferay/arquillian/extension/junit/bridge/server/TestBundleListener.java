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

package com.liferay.arquillian.extension.junit.bridge.server;

import java.io.IOException;

import java.net.Socket;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.runners.model.TestClass;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

/**
 * @author Shuyang Zhou
 */
public class TestBundleListener implements BundleListener {

	public TestBundleListener(
		BundleContext systemBundleContext, Bundle testBundle,
		TestClass testClass, String reportServerHostName, int reportServerPort,
		long passCode) {

		_systemBundleContext = systemBundleContext;
		_testBundle = testBundle;
		_testClass = testClass;
		_reportServerHostName = reportServerHostName;
		_reportServerPort = reportServerPort;
		_passCode = passCode;
	}

	@Override
	public void bundleChanged(BundleEvent bundleEvent) {
		Bundle bundle = bundleEvent.getBundle();

		if (!_testBundle.equals(bundle)) {
			return;
		}

		_bundleChanged(bundle);
	}

	private synchronized void _bundleChanged(Bundle bundle) {
		if (bundle.getState() == Bundle.ACTIVE) {
			try {
				_socket = new Socket(_reportServerHostName, _reportServerPort);

				_testExecutorThread = new Thread(
					new TestExecutorRunnable(
						_testBundle, _testClass, _socket, _passCode),
					_testClass.getName() + "-executor-thread");

				_testExecutorThread.setDaemon(true);

				_testExecutorThread.start();
			}
			catch (IOException ioe) {
				_logger.log(
					Level.SEVERE, "Unable to connect back to client", ioe);
			}

			return;
		}

		if (bundle.getState() <= Bundle.RESOLVED) {
			_systemBundleContext.removeBundleListener(this);

			if (_testExecutorThread != null) {
				_testExecutorThread.interrupt();
			}

			if (_socket != null) {
				try {
					_socket.close();
				}
				catch (IOException ioe) {
					_logger.log(Level.SEVERE, "Unable to close socket", ioe);
				}
			}
		}
	}

	private static final Logger _logger = Logger.getLogger(
		TestBundleListener.class.getName());

	private final long _passCode;
	private final String _reportServerHostName;
	private final int _reportServerPort;
	private Socket _socket;
	private final BundleContext _systemBundleContext;
	private final Bundle _testBundle;
	private final TestClass _testClass;
	private Thread _testExecutorThread;

}