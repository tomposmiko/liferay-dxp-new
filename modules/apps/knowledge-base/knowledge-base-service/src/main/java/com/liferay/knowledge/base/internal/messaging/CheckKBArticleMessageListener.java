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

package com.liferay.knowledge.base.internal.messaging;

import com.liferay.knowledge.base.internal.configuration.KBServiceConfiguration;
import com.liferay.knowledge.base.service.KBArticleLocalService;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerEntry;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.TimeUnit;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;

import java.util.Dictionary;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alicia García
 */
@Component(
	configurationPid = "com.liferay.knowledge.base.internal.configuration.KBServiceConfiguration",
	immediate = true,
	property = "model.class.name=com.liferay.knowledge.base.internal.configuration.KBServiceConfiguration",
	service = {
		CheckKBArticleMessageListener.class, ConfigurationModelListener.class
	}
)
public class CheckKBArticleMessageListener
	extends BaseMessageListener implements ConfigurationModelListener {

	@Override
	public void onAfterSave(String pid, Dictionary<String, Object> properties) {
		_schedulerEngineHelper.unregister(this);

		KBServiceConfiguration kbServiceConfiguration =
			ConfigurableUtil.createConfigurable(
				KBServiceConfiguration.class, properties);

		_registerSchedulerEntry(kbServiceConfiguration.checkInterval());
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		KBServiceConfiguration kbServiceConfiguration =
			ConfigurableUtil.createConfigurable(
				KBServiceConfiguration.class, properties);

		_registerSchedulerEntry(kbServiceConfiguration.checkInterval());
	}

	@Deactivate
	protected void deactivate() {
		_schedulerEngineHelper.unregister(this);
	}

	@Override
	protected void doReceive(Message message) throws Exception {
		if (GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-165476"))) {
			_kbArticleLocalService.checkKBArticles();
		}
	}

	private void _registerSchedulerEntry(int checkInterval) {
		Class<?> clazz = getClass();

		String className = clazz.getName();

		Trigger trigger = _triggerFactory.createTrigger(
			className, className, null, null, checkInterval, TimeUnit.MINUTE);

		SchedulerEntry schedulerEntry = new SchedulerEntryImpl(
			className, trigger);

		_schedulerEngineHelper.register(
			this, schedulerEntry, DestinationNames.SCHEDULER_DISPATCH);
	}

	@Reference
	private KBArticleLocalService _kbArticleLocalService;

	@Reference
	private SchedulerEngineHelper _schedulerEngineHelper;

	@Reference
	private TriggerFactory _triggerFactory;

}