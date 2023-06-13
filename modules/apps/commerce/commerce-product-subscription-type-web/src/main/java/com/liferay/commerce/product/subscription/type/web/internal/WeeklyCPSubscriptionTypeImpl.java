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

package com.liferay.commerce.product.subscription.type.web.internal;

import com.liferay.commerce.exception.CPSubscriptionTypeSettingsException;
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.util.CPSubscriptionType;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.kernel.util.Validator;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	property = {
		"commerce.product.subscription.type.name=" + CPConstants.WEEKLY_SUBSCRIPTION_TYPE,
		"commerce.product.subscription.type.order:Integer=20"
	},
	service = CPSubscriptionType.class
)
public class WeeklyCPSubscriptionTypeImpl implements CPSubscriptionType {

	@Override
	public UnicodeProperties
			getDeliverySubscriptionTypeSettingsUnicodeProperties(
				UnicodeProperties subscriptionTypeSettingsUnicodeProperties)
		throws PortalException {

		return _getSubscriptionTypeSettingsUnicodeProperties(
			"deliveryWeekDay", subscriptionTypeSettingsUnicodeProperties);
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "week");
	}

	@Override
	public String getName() {
		return CPConstants.WEEKLY_SUBSCRIPTION_TYPE;
	}

	@Override
	public Date getSubscriptionNextIterationDate(
		TimeZone timeZone, int subscriptionLength,
		UnicodeProperties subscriptionTypeSettingsUnicodeProperties,
		Date lastIterationDate) {

		Calendar calendar = CalendarFactoryUtil.getCalendar(timeZone);

		if (lastIterationDate == null) {
			lastIterationDate = getSubscriptionStartDate(
				timeZone, subscriptionTypeSettingsUnicodeProperties);
		}

		calendar.setTime(lastIterationDate);

		calendar.add(Calendar.WEEK_OF_YEAR, subscriptionLength);

		return calendar.getTime();
	}

	@Override
	public Date getSubscriptionStartDate(
		TimeZone timeZone,
		UnicodeProperties subscriptionTypeSettingsUnicodeProperties) {

		Date date = new Date();

		if ((subscriptionTypeSettingsUnicodeProperties == null) ||
			subscriptionTypeSettingsUnicodeProperties.isEmpty()) {

			return date;
		}

		int weekDay = GetterUtil.getInteger(
			subscriptionTypeSettingsUnicodeProperties.get("weekDay"));

		if ((weekDay < Calendar.SUNDAY) || (weekDay > Calendar.SATURDAY)) {
			return date;
		}

		Calendar calendar = CalendarFactoryUtil.getCalendar(
			date.getTime(), timeZone);

		int today = calendar.get(Calendar.DAY_OF_WEEK);

		if (today == weekDay) {
			return date;
		}

		if (weekDay < today) {
			weekDay += 7;
		}

		calendar.add(Calendar.DAY_OF_MONTH, weekDay - today);

		return calendar.getTime();
	}

	@Override
	public UnicodeProperties getSubscriptionTypeSettingsUnicodeProperties(
			UnicodeProperties subscriptionTypeSettingsUnicodeProperties)
		throws PortalException {

		return _getSubscriptionTypeSettingsUnicodeProperties(
			"weekDay", subscriptionTypeSettingsUnicodeProperties);
	}

	private UnicodeProperties _getSubscriptionTypeSettingsUnicodeProperties(
			String weekDayKey,
			UnicodeProperties subscriptionTypeSettingsUnicodeProperties)
		throws CPSubscriptionTypeSettingsException {

		if (subscriptionTypeSettingsUnicodeProperties == null) {
			return null;
		}

		return UnicodePropertiesBuilder.create(
			true
		).put(
			weekDayKey,
			() -> {
				String weekDayValue =
					subscriptionTypeSettingsUnicodeProperties.get(weekDayKey);

				if (Validator.isBlank(weekDayValue)) {
					throw new CPSubscriptionTypeSettingsException(
						"The " + weekDayKey + " field is mandatory");
				}

				int weekDay = GetterUtil.getInteger(weekDayValue, -1);

				if ((weekDay < Calendar.SUNDAY) ||
					(weekDay > Calendar.SATURDAY)) {

					throw new CPSubscriptionTypeSettingsException(
						StringBundler.concat(
							"Invalid ", weekDayKey, " ", weekDayValue));
				}

				return weekDayValue;
			}
		).build();
	}

	@Reference
	private Language _language;

}