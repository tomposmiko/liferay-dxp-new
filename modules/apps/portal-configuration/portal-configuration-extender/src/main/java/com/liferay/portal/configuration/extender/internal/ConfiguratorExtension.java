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

package com.liferay.portal.configuration.extender.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.io.IOException;

import java.util.Collection;
import java.util.Dictionary;

import org.apache.felix.utils.extender.Extension;
import org.apache.felix.utils.log.Logger;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * @author Carlos Sierra Andrés
 */
public class ConfiguratorExtension implements Extension {

	public ConfiguratorExtension(
		ConfigurationAdmin configurationAdmin, Logger logger,
		String bundleSymbolicName,
		Collection<NamedConfigurationContent> namedConfigurationContents) {

		_configurationAdmin = configurationAdmin;
		_logger = logger;
		_bundleSymbolicName = bundleSymbolicName;
		_configurationContents = namedConfigurationContents;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void start() throws Exception {
		for (NamedConfigurationContent namedConfigurationContent :
				_configurationContents) {

			try {
				_process(namedConfigurationContent);
			}
			catch (IOException ioe) {
				_logger.log(Logger.LOG_WARNING, ioe.getMessage(), ioe);
			}
		}
	}

	private void _process(NamedConfigurationContent namedConfigurationContent)
		throws InvalidSyntaxException, IOException {

		Configuration configuration = null;
		String configuratorURL = null;

		if (namedConfigurationContent.getFactoryPid() == null) {
			String pid = namedConfigurationContent.getPid();

			if (ArrayUtil.isNotEmpty(
					_configurationAdmin.listConfigurations(
						"(service.pid=" + pid + ")"))) {

				return;
			}

			configuration = _configurationAdmin.getConfiguration(
				pid, StringPool.QUESTION);
		}
		else {
			configuratorURL =
				_bundleSymbolicName + "#" + namedConfigurationContent.getPid();

			if (ArrayUtil.isNotEmpty(
					_configurationAdmin.listConfigurations(
						"(configurator.url=" + configuratorURL + ")"))) {

				return;
			}

			configuration = _configurationAdmin.createFactoryConfiguration(
				namedConfigurationContent.getFactoryPid(), StringPool.QUESTION);
		}

		Dictionary<String, Object> properties = null;

		try {
			properties = namedConfigurationContent.getProperties();
		}
		catch (Throwable t) {
			_logger.log(
				Logger.LOG_WARNING,
				StringBundler.concat(
					"Supplier from description ", namedConfigurationContent,
					" threw an exception: "),
				t);

			return;
		}

		if (configuratorURL != null) {
			properties.put("configurator.url", configuratorURL);
		}

		configuration.update(properties);
	}

	private final String _bundleSymbolicName;
	private final ConfigurationAdmin _configurationAdmin;
	private final Collection<NamedConfigurationContent> _configurationContents;
	private final Logger _logger;

}