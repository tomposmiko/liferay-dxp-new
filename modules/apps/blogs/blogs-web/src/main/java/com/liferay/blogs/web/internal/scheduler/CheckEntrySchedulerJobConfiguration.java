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

package com.liferay.blogs.web.internal.scheduler;

import com.liferay.blogs.configuration.BlogsConfiguration;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.petra.function.UnsafeRunnable;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.scheduler.SchedulerJobConfiguration;
import com.liferay.portal.kernel.scheduler.TimeUnit;
import com.liferay.portal.kernel.scheduler.TriggerConfiguration;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Zsolt Berentey
 */
@Component(
	configurationPid = "com.liferay.blogs.configuration.BlogsConfiguration",
	service = SchedulerJobConfiguration.class
)
public class CheckEntrySchedulerJobConfiguration
	implements SchedulerJobConfiguration {

	@Override
	public UnsafeRunnable<Exception> getJobExecutorUnsafeRunnable() {
		return _blogsEntryLocalService::checkEntries;
	}

	@Override
	public TriggerConfiguration getTriggerConfiguration() {
		return TriggerConfiguration.createTriggerConfiguration(
			_blogsConfiguration.entryCheckInterval(), TimeUnit.MINUTE);
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_blogsConfiguration = ConfigurableUtil.createConfigurable(
			BlogsConfiguration.class, properties);
	}

	private BlogsConfiguration _blogsConfiguration;

	@Reference
	private BlogsEntryLocalService _blogsEntryLocalService;

}