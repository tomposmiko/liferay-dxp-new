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

package com.liferay.calendar.search.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.calendar.constants.CalendarActionKeys;
import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.model.CalendarResource;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.settings.LocalizedValuesMap;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.test.util.FieldValuesAssert;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Wade Cao
 * @author André de Oliveira
 */
@RunWith(Arquillian.class)
@Sync
public class CalendarBookingIndexerIndexedFieldsTest
	extends BaseCalendarIndexerTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerTestRule.INSTANCE,
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		setGroup(calendarFixture.addGroup());
		setIndexerClass(CalendarBooking.class);
	}

	@Test
	public void testIndexedFields() throws Exception {
		String originalTitle = "entity title";
		String translatedTitle = "entitas neve";

		String description = StringUtil.toLowerCase(
			RandomTestUtil.randomString());

		CalendarBooking calendarBooking = addCalendarBooking(
			new LocalizedValuesMap() {
				{
					put(LocaleUtil.US, originalTitle);
					put(LocaleUtil.HUNGARY, translatedTitle);
				}
			},
			new LocalizedValuesMap() {
				{
					put(LocaleUtil.US, originalTitle);
					put(LocaleUtil.HUNGARY, translatedTitle);
				}
			},
			new LocalizedValuesMap() {
				{
					put(LocaleUtil.US, description);
					put(LocaleUtil.HUNGARY, description);
				}
			});

		Map<String, String> map = new HashMap<>();

		map.put(
			Field.CLASS_NAME_ID,
			String.valueOf(portal.getClassNameId(Calendar.class)));

		map.put(Field.EXPIRATION_DATE, "99950812133000");
		map.put(
			Field.EXPIRATION_DATE.concat("_sortable"), "9223372036854775807");
		map.put(Field.PRIORITY, "0.0");
		map.put(Field.PUBLISH_DATE, "19700101000000");
		map.put(Field.PUBLISH_DATE.concat("_sortable"), "0");
		map.put(Field.RELATED_ENTRY, "true");
		map.put(Field.STAGING_GROUP, "false");
		map.put(Field.STATUS, "0");
		map.put("viewActionId", CalendarActionKeys.VIEW_BOOKING_DETAILS);

		populateTitle(originalTitle, map);

		populateTranslatedTitle(translatedTitle, map);

		CalendarResource calendarResource =
			calendarBooking.getCalendarResource();

		populateCalendarResource(calendarResource, map);

		Calendar calendar = calendarResource.getDefaultCalendar();

		populateCalendar(calendar, map);

		populateCalendarBooking(calendarBooking, map);

		DateFormat dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			"yyyyMMddHHmm");

		populateCalendarDate(
			Field.CREATE_DATE, calendar.getCreateDate(), dateFormat, map);
		populateCalendarDate(
			Field.MODIFIED_DATE, calendar.getModifiedDate(), dateFormat, map);

		calendarFieldsFixture.populateGroupRoleId(map);
		calendarFieldsFixture.populateRoleId("Owner", map);
		calendarFieldsFixture.populateUID(calendarBooking, map);

		String keywords = "nev";

		Document document = calendarSearchFixture.searchOnlyOne(
			keywords, LocaleUtil.HUNGARY);

		adjustDatePrecision(Field.CREATE_DATE, document, dateFormat);
		adjustDatePrecision(Field.MODIFIED_DATE, document, dateFormat);

		FieldValuesAssert.assertFieldValues(map, document, keywords);
	}

	protected CalendarBooking addCalendarBooking(
			LocalizedValuesMap titleLocalizedValuesMap,
			LocalizedValuesMap nameLocalizedValuesMap,
			LocalizedValuesMap descriptionLocalizedValuesMap)
		throws PortalException {

		ServiceContext serviceContext = calendarFixture.getServiceContext();

		Calendar calendar = calendarFixture.addCalendar(
			nameLocalizedValuesMap, descriptionLocalizedValuesMap,
			serviceContext);

		return calendarFixture.addCalendarBooking(
			titleLocalizedValuesMap, calendar, serviceContext);
	}

	protected void adjustDatePrecision(
			String field, Document document, DateFormat dateFormat)
		throws Exception {

		Date date1 = document.getDate(field);

		Date date2 = dateFormat.parse(dateFormat.format(date1));

		document.addDate(field, date2);
		document.addKeyword(field.concat("_sortable"), date2.getTime());
	}

	protected void populateCalendar(
		Calendar calendar, Map<String, String> map) {

		map.put(Field.DEFAULT_LANGUAGE_ID, calendar.getDefaultLanguageId());
		map.put(Field.USER_ID, String.valueOf(calendar.getUserId()));
		map.put(
			Field.USER_NAME, StringUtil.toLowerCase(calendar.getUserName()));
		map.put("visible", "true");
	}

	protected void populateCalendarBooking(
		CalendarBooking calendarBooking, Map<String, String> map) {

		map.put(
			Field.CLASS_PK, String.valueOf(calendarBooking.getCalendarId()));
		map.put(Field.ENTRY_CLASS_NAME, calendarBooking.getModelClassName());
		map.put(
			Field.ENTRY_CLASS_PK,
			String.valueOf(calendarBooking.getCalendarBookingId()));
		map.put(
			"calendarBookingId",
			String.valueOf(calendarBooking.getCalendarBookingId()));
		map.put("endTime", String.valueOf(calendarBooking.getEndTime()));
		map.put(
			"endTime_sortable", String.valueOf(calendarBooking.getEndTime()));
		map.put("startTime", String.valueOf(calendarBooking.getStartTime()));
		map.put(
			"startTime_sortable",
			String.valueOf(calendarBooking.getStartTime()));
	}

	protected void populateCalendarDate(
			String fieldName, Date date, DateFormat dateFormat,
			Map<String, String> map)
		throws ParseException {

		String dateString = dateFormat.format(date);

		map.put(fieldName, dateString + "00");

		Date date2 = dateFormat.parse(dateString);

		map.put(fieldName.concat("_sortable"), String.valueOf(date2.getTime()));
	}

	protected void populateCalendarResource(
		CalendarResource calendarResource, Map<String, String> map) {

		map.put(
			Field.COMPANY_ID, String.valueOf(calendarResource.getCompanyId()));
		map.put(Field.GROUP_ID, String.valueOf(calendarResource.getGroupId()));
		map.put(
			Field.SCOPE_GROUP_ID,
			String.valueOf(calendarResource.getGroupId()));
	}

	protected void populateTitle(String title, Map<String, String> map) {
		map.put(Field.TITLE + "_en_US", title);

		map.put("localized_title", title);
		map.put("localized_title_ca_ES", title);
		map.put("localized_title_ca_ES_sortable", title);
		map.put("localized_title_de_DE", title);
		map.put("localized_title_de_DE_sortable", title);
		map.put("localized_title_en_US", title);
		map.put("localized_title_en_US_sortable", title);
		map.put("localized_title_es_ES", title);
		map.put("localized_title_es_ES_sortable", title);
		map.put("localized_title_fi_FI", title);
		map.put("localized_title_fi_FI_sortable", title);
		map.put("localized_title_fr_FR", title);
		map.put("localized_title_fr_FR_sortable", title);
		map.put("localized_title_iw_IL", title);
		map.put("localized_title_iw_IL_sortable", title);
		map.put("localized_title_ja_JP", title);
		map.put("localized_title_ja_JP_sortable", title);
		map.put("localized_title_nl_NL", title);
		map.put("localized_title_nl_NL_sortable", title);
		map.put("localized_title_pt_BR", title);
		map.put("localized_title_pt_BR_sortable", title);
		map.put("localized_title_zh_CN", title);
		map.put("localized_title_zh_CN_sortable", title);
	}

	protected void populateTranslatedTitle(
		String title, Map<String, String> map) {

		map.put(Field.TITLE + "_hu_HU", title);

		map.put("localized_title_hu_HU", title);
		map.put("localized_title_hu_HU_sortable", title);
	}

	@Inject
	protected Portal portal;

}