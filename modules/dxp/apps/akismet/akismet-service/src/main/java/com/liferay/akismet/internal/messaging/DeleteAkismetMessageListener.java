/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.akismet.internal.messaging;

import com.liferay.akismet.internal.configuration.AkismetServiceConfiguration;
import com.liferay.akismet.service.AkismetEntryLocalService;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.util.Time;

import java.util.Date;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jamie Sammons
 */
@Component(
	configurationPid = "com.liferay.akismet.internal.configuration.AkismetServiceConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE, service = {}
)
public class DeleteAkismetMessageListener extends BaseMessageListener {

	@Activate
	protected void activate(Map<String, Object> properties) {
		_akismetServiceConfiguration = ConfigurableUtil.createConfigurable(
			AkismetServiceConfiguration.class, properties);

		String className = getClass().getName();

		Trigger trigger = _triggerFactory.createTrigger(
			className, className, null, null, "0 0 0 * * ?");

		_schedulerEntryImpl = new SchedulerEntryImpl(
			getClass().getName(), trigger);

		_schedulerEngineHelper.register(
			this, _schedulerEntryImpl, DestinationNames.SCHEDULER_DISPATCH);
	}

	@Deactivate
	protected void deactivate() {
		try {
			if (_log.isDebugEnabled()) {
				_log.debug("Unscheduling trigger");
			}

			Trigger trigger = _schedulerEntryImpl.getTrigger();

			_schedulerEngineHelper.unschedule(
				trigger.getJobName(), trigger.getGroupName(),
				StorageType.MEMORY_CLUSTERED);
		}
		catch (SchedulerException schedulerException) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to unschedule trigger", schedulerException);
			}
		}

		_schedulerEngineHelper.unregister(this);
	}

	@Override
	protected void doReceive(Message message) {
		int reportableTime =
			_akismetServiceConfiguration.akismetReportableTime();

		_akismetEntryLocalService.deleteAkismetEntry(
			new Date(System.currentTimeMillis() - (reportableTime * Time.DAY)));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DeleteAkismetMessageListener.class);

	@Reference
	private AkismetEntryLocalService _akismetEntryLocalService;

	private AkismetServiceConfiguration _akismetServiceConfiguration;

	@Reference
	private SchedulerEngineHelper _schedulerEngineHelper;

	private SchedulerEntryImpl _schedulerEntryImpl;

	@Reference
	private TriggerFactory _triggerFactory;

}