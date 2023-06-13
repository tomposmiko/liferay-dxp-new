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

package com.liferay.osb.faro.web.internal.messaging;

import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.Trigger;

import java.util.Date;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Rachael Koestartyo
 */
@Component(
	immediate = true, service = SendDailyEmailReportMessageListener.class
)
public class SendDailyEmailReportMessageListener
	extends BaseEmailReportMessageListener {

	@Activate
	protected void activate() {
		Class<?> clazz = getClass();

		Trigger trigger = triggerFactory.createTrigger(
			clazz.getName(), clazz.getName(), new Date(), null, "0 0 0 * * ?");

		schedulerEngineHelper.register(
			this, new SchedulerEntryImpl(clazz.getName(), trigger),
			DestinationNames.SCHEDULER_DISPATCH);
	}

	@Deactivate
	protected void deactivate() {
		schedulerEngineHelper.unregister(this);
	}

	@Override
	protected String getFrequency() {
		return "daily";
	}

}