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

package com.liferay.headless.delivery.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.delivery.client.dto.v1_0.MessageBoardThread;
import com.liferay.headless.delivery.client.http.HttpInvoker;
import com.liferay.headless.delivery.client.pagination.Page;
import com.liferay.headless.delivery.client.pagination.Pagination;
import com.liferay.headless.delivery.client.resource.v1_0.MessageBoardThreadResource;
import com.liferay.headless.delivery.client.serdes.v1_0.MessageBoardThreadSerDes;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.InvocationTargetException;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.time.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseMessageBoardThreadResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();
		testLocale = LocaleUtil.getDefault();

		testCompany = CompanyLocalServiceUtil.getCompany(
			testGroup.getCompanyId());

		_messageBoardThreadResource.setContextCompany(testCompany);
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testClientSerDesToDTO() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				enable(SerializationFeature.INDENT_OUTPUT);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		MessageBoardThread messageBoardThread1 = randomMessageBoardThread();

		String json = objectMapper.writeValueAsString(messageBoardThread1);

		MessageBoardThread messageBoardThread2 = MessageBoardThreadSerDes.toDTO(
			json);

		Assert.assertTrue(equals(messageBoardThread1, messageBoardThread2));
	}

	@Test
	public void testClientSerDesToJSON() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		MessageBoardThread messageBoardThread = randomMessageBoardThread();

		String json1 = objectMapper.writeValueAsString(messageBoardThread);
		String json2 = MessageBoardThreadSerDes.toJSON(messageBoardThread);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		MessageBoardThread messageBoardThread = randomMessageBoardThread();

		messageBoardThread.setArticleBody(regex);
		messageBoardThread.setEncodingFormat(regex);
		messageBoardThread.setHeadline(regex);
		messageBoardThread.setThreadType(regex);

		String json = MessageBoardThreadSerDes.toJSON(messageBoardThread);

		Assert.assertFalse(json.contains(regex));

		messageBoardThread = MessageBoardThreadSerDes.toDTO(json);

		Assert.assertEquals(regex, messageBoardThread.getArticleBody());
		Assert.assertEquals(regex, messageBoardThread.getEncodingFormat());
		Assert.assertEquals(regex, messageBoardThread.getHeadline());
		Assert.assertEquals(regex, messageBoardThread.getThreadType());
	}

	@Test
	public void testGetMessageBoardSectionMessageBoardThreadsPage()
		throws Exception {

		Long messageBoardSectionId =
			testGetMessageBoardSectionMessageBoardThreadsPage_getMessageBoardSectionId();
		Long irrelevantMessageBoardSectionId =
			testGetMessageBoardSectionMessageBoardThreadsPage_getIrrelevantMessageBoardSectionId();

		if ((irrelevantMessageBoardSectionId != null)) {
			MessageBoardThread irrelevantMessageBoardThread =
				testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
					irrelevantMessageBoardSectionId,
					randomIrrelevantMessageBoardThread());

			Page<MessageBoardThread> page =
				MessageBoardThreadResource.
					getMessageBoardSectionMessageBoardThreadsPage(
						irrelevantMessageBoardSectionId, null, null,
						Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantMessageBoardThread),
				(List<MessageBoardThread>)page.getItems());
			assertValid(page);
		}

		MessageBoardThread messageBoardThread1 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, randomMessageBoardThread());

		MessageBoardThread messageBoardThread2 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, randomMessageBoardThread());

		Page<MessageBoardThread> page =
			MessageBoardThreadResource.
				getMessageBoardSectionMessageBoardThreadsPage(
					messageBoardSectionId, null, null, Pagination.of(1, 2),
					null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(messageBoardThread1, messageBoardThread2),
			(List<MessageBoardThread>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetMessageBoardSectionMessageBoardThreadsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardSectionId =
			testGetMessageBoardSectionMessageBoardThreadsPage_getMessageBoardSectionId();

		MessageBoardThread messageBoardThread1 = randomMessageBoardThread();

		messageBoardThread1 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, messageBoardThread1);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardThread> page =
				MessageBoardThreadResource.
					getMessageBoardSectionMessageBoardThreadsPage(
						messageBoardSectionId, null,
						getFilterString(
							entityField, "between", messageBoardThread1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardThread1),
				(List<MessageBoardThread>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardSectionMessageBoardThreadsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardSectionId =
			testGetMessageBoardSectionMessageBoardThreadsPage_getMessageBoardSectionId();

		MessageBoardThread messageBoardThread1 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, randomMessageBoardThread());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardThread messageBoardThread2 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, randomMessageBoardThread());

		for (EntityField entityField : entityFields) {
			Page<MessageBoardThread> page =
				MessageBoardThreadResource.
					getMessageBoardSectionMessageBoardThreadsPage(
						messageBoardSectionId, null,
						getFilterString(entityField, "eq", messageBoardThread1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardThread1),
				(List<MessageBoardThread>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardSectionMessageBoardThreadsPageWithPagination()
		throws Exception {

		Long messageBoardSectionId =
			testGetMessageBoardSectionMessageBoardThreadsPage_getMessageBoardSectionId();

		MessageBoardThread messageBoardThread1 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, randomMessageBoardThread());

		MessageBoardThread messageBoardThread2 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, randomMessageBoardThread());

		MessageBoardThread messageBoardThread3 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, randomMessageBoardThread());

		Page<MessageBoardThread> page1 =
			MessageBoardThreadResource.
				getMessageBoardSectionMessageBoardThreadsPage(
					messageBoardSectionId, null, null, Pagination.of(1, 2),
					null);

		List<MessageBoardThread> messageBoardThreads1 =
			(List<MessageBoardThread>)page1.getItems();

		Assert.assertEquals(
			messageBoardThreads1.toString(), 2, messageBoardThreads1.size());

		Page<MessageBoardThread> page2 =
			MessageBoardThreadResource.
				getMessageBoardSectionMessageBoardThreadsPage(
					messageBoardSectionId, null, null, Pagination.of(2, 2),
					null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<MessageBoardThread> messageBoardThreads2 =
			(List<MessageBoardThread>)page2.getItems();

		Assert.assertEquals(
			messageBoardThreads2.toString(), 1, messageBoardThreads2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(
				messageBoardThread1, messageBoardThread2, messageBoardThread3),
			new ArrayList<MessageBoardThread>() {
				{
					addAll(messageBoardThreads1);
					addAll(messageBoardThreads2);
				}
			});
	}

	@Test
	public void testGetMessageBoardSectionMessageBoardThreadsPageWithSortDateTime()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardSectionId =
			testGetMessageBoardSectionMessageBoardThreadsPage_getMessageBoardSectionId();

		MessageBoardThread messageBoardThread1 = randomMessageBoardThread();
		MessageBoardThread messageBoardThread2 = randomMessageBoardThread();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				messageBoardThread1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		messageBoardThread1 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, messageBoardThread1);

		messageBoardThread2 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, messageBoardThread2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardThread> ascPage =
				MessageBoardThreadResource.
					getMessageBoardSectionMessageBoardThreadsPage(
						messageBoardSectionId, null, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardThread1, messageBoardThread2),
				(List<MessageBoardThread>)ascPage.getItems());

			Page<MessageBoardThread> descPage =
				MessageBoardThreadResource.
					getMessageBoardSectionMessageBoardThreadsPage(
						messageBoardSectionId, null, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardThread2, messageBoardThread1),
				(List<MessageBoardThread>)descPage.getItems());
		}
	}

	@Test
	public void testGetMessageBoardSectionMessageBoardThreadsPageWithSortString()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardSectionId =
			testGetMessageBoardSectionMessageBoardThreadsPage_getMessageBoardSectionId();

		MessageBoardThread messageBoardThread1 = randomMessageBoardThread();
		MessageBoardThread messageBoardThread2 = randomMessageBoardThread();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				messageBoardThread1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(
				messageBoardThread2, entityField.getName(), "Bbb");
		}

		messageBoardThread1 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, messageBoardThread1);

		messageBoardThread2 =
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				messageBoardSectionId, messageBoardThread2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardThread> ascPage =
				MessageBoardThreadResource.
					getMessageBoardSectionMessageBoardThreadsPage(
						messageBoardSectionId, null, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardThread1, messageBoardThread2),
				(List<MessageBoardThread>)ascPage.getItems());

			Page<MessageBoardThread> descPage =
				MessageBoardThreadResource.
					getMessageBoardSectionMessageBoardThreadsPage(
						messageBoardSectionId, null, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardThread2, messageBoardThread1),
				(List<MessageBoardThread>)descPage.getItems());
		}
	}

	protected MessageBoardThread
			testGetMessageBoardSectionMessageBoardThreadsPage_addMessageBoardThread(
				Long messageBoardSectionId,
				MessageBoardThread messageBoardThread)
		throws Exception {

		return MessageBoardThreadResource.
			postMessageBoardSectionMessageBoardThread(
				messageBoardSectionId, messageBoardThread);
	}

	protected Long
			testGetMessageBoardSectionMessageBoardThreadsPage_getMessageBoardSectionId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetMessageBoardSectionMessageBoardThreadsPage_getIrrelevantMessageBoardSectionId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostMessageBoardSectionMessageBoardThread()
		throws Exception {

		MessageBoardThread randomMessageBoardThread =
			randomMessageBoardThread();

		MessageBoardThread postMessageBoardThread =
			testPostMessageBoardSectionMessageBoardThread_addMessageBoardThread(
				randomMessageBoardThread);

		assertEquals(randomMessageBoardThread, postMessageBoardThread);
		assertValid(postMessageBoardThread);
	}

	protected MessageBoardThread
			testPostMessageBoardSectionMessageBoardThread_addMessageBoardThread(
				MessageBoardThread messageBoardThread)
		throws Exception {

		return MessageBoardThreadResource.
			postMessageBoardSectionMessageBoardThread(
				testGetMessageBoardSectionMessageBoardThreadsPage_getMessageBoardSectionId(),
				messageBoardThread);
	}

	@Test
	public void testDeleteMessageBoardThread() throws Exception {
		MessageBoardThread messageBoardThread =
			testDeleteMessageBoardThread_addMessageBoardThread();

		assertHttpResponseStatusCode(
			204,
			MessageBoardThreadResource.deleteMessageBoardThreadHttpResponse(
				messageBoardThread.getId()));

		assertHttpResponseStatusCode(
			404,
			MessageBoardThreadResource.getMessageBoardThreadHttpResponse(
				messageBoardThread.getId()));

		assertHttpResponseStatusCode(
			404,
			MessageBoardThreadResource.getMessageBoardThreadHttpResponse(0L));
	}

	protected MessageBoardThread
			testDeleteMessageBoardThread_addMessageBoardThread()
		throws Exception {

		return MessageBoardThreadResource.postSiteMessageBoardThread(
			testGroup.getGroupId(), randomMessageBoardThread());
	}

	@Test
	public void testGetMessageBoardThread() throws Exception {
		MessageBoardThread postMessageBoardThread =
			testGetMessageBoardThread_addMessageBoardThread();

		MessageBoardThread getMessageBoardThread =
			MessageBoardThreadResource.getMessageBoardThread(
				postMessageBoardThread.getId());

		assertEquals(postMessageBoardThread, getMessageBoardThread);
		assertValid(getMessageBoardThread);
	}

	protected MessageBoardThread
			testGetMessageBoardThread_addMessageBoardThread()
		throws Exception {

		return MessageBoardThreadResource.postSiteMessageBoardThread(
			testGroup.getGroupId(), randomMessageBoardThread());
	}

	@Test
	public void testPatchMessageBoardThread() throws Exception {
		MessageBoardThread postMessageBoardThread =
			testPatchMessageBoardThread_addMessageBoardThread();

		MessageBoardThread randomPatchMessageBoardThread =
			randomPatchMessageBoardThread();

		MessageBoardThread patchMessageBoardThread =
			MessageBoardThreadResource.patchMessageBoardThread(
				postMessageBoardThread.getId(), randomPatchMessageBoardThread);

		MessageBoardThread expectedPatchMessageBoardThread =
			(MessageBoardThread)BeanUtils.cloneBean(postMessageBoardThread);

		_beanUtilsBean.copyProperties(
			expectedPatchMessageBoardThread, randomPatchMessageBoardThread);

		MessageBoardThread getMessageBoardThread =
			MessageBoardThreadResource.getMessageBoardThread(
				patchMessageBoardThread.getId());

		assertEquals(expectedPatchMessageBoardThread, getMessageBoardThread);
		assertValid(getMessageBoardThread);
	}

	protected MessageBoardThread
			testPatchMessageBoardThread_addMessageBoardThread()
		throws Exception {

		return MessageBoardThreadResource.postSiteMessageBoardThread(
			testGroup.getGroupId(), randomMessageBoardThread());
	}

	@Test
	public void testPutMessageBoardThread() throws Exception {
		MessageBoardThread postMessageBoardThread =
			testPutMessageBoardThread_addMessageBoardThread();

		MessageBoardThread randomMessageBoardThread =
			randomMessageBoardThread();

		MessageBoardThread putMessageBoardThread =
			MessageBoardThreadResource.putMessageBoardThread(
				postMessageBoardThread.getId(), randomMessageBoardThread);

		assertEquals(randomMessageBoardThread, putMessageBoardThread);
		assertValid(putMessageBoardThread);

		MessageBoardThread getMessageBoardThread =
			MessageBoardThreadResource.getMessageBoardThread(
				putMessageBoardThread.getId());

		assertEquals(randomMessageBoardThread, getMessageBoardThread);
		assertValid(getMessageBoardThread);
	}

	protected MessageBoardThread
			testPutMessageBoardThread_addMessageBoardThread()
		throws Exception {

		return MessageBoardThreadResource.postSiteMessageBoardThread(
			testGroup.getGroupId(), randomMessageBoardThread());
	}

	@Test
	public void testDeleteMessageBoardThreadMyRating() throws Exception {
		MessageBoardThread messageBoardThread =
			testDeleteMessageBoardThreadMyRating_addMessageBoardThread();

		assertHttpResponseStatusCode(
			204,
			MessageBoardThreadResource.
				deleteMessageBoardThreadMyRatingHttpResponse(
					messageBoardThread.getId()));

		assertHttpResponseStatusCode(
			404,
			MessageBoardThreadResource.
				getMessageBoardThreadMyRatingHttpResponse(
					messageBoardThread.getId()));

		assertHttpResponseStatusCode(
			404,
			MessageBoardThreadResource.
				getMessageBoardThreadMyRatingHttpResponse(0L));
	}

	protected MessageBoardThread
			testDeleteMessageBoardThreadMyRating_addMessageBoardThread()
		throws Exception {

		return MessageBoardThreadResource.postSiteMessageBoardThread(
			testGroup.getGroupId(), randomMessageBoardThread());
	}

	@Test
	public void testGetMessageBoardThreadMyRating() throws Exception {
		Assert.assertTrue(true);
	}

	@Test
	public void testPostMessageBoardThreadMyRating() throws Exception {
		Assert.assertTrue(true);
	}

	@Test
	public void testPutMessageBoardThreadMyRating() throws Exception {
		Assert.assertTrue(true);
	}

	@Test
	public void testGetSiteMessageBoardThreadsPage() throws Exception {
		Long siteId = testGetSiteMessageBoardThreadsPage_getSiteId();
		Long irrelevantSiteId =
			testGetSiteMessageBoardThreadsPage_getIrrelevantSiteId();

		if ((irrelevantSiteId != null)) {
			MessageBoardThread irrelevantMessageBoardThread =
				testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
					irrelevantSiteId, randomIrrelevantMessageBoardThread());

			Page<MessageBoardThread> page =
				MessageBoardThreadResource.getSiteMessageBoardThreadsPage(
					irrelevantSiteId, null, null, null, Pagination.of(1, 2),
					null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantMessageBoardThread),
				(List<MessageBoardThread>)page.getItems());
			assertValid(page);
		}

		MessageBoardThread messageBoardThread1 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, randomMessageBoardThread());

		MessageBoardThread messageBoardThread2 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, randomMessageBoardThread());

		Page<MessageBoardThread> page =
			MessageBoardThreadResource.getSiteMessageBoardThreadsPage(
				siteId, null, null, null, Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(messageBoardThread1, messageBoardThread2),
			(List<MessageBoardThread>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetSiteMessageBoardThreadsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteMessageBoardThreadsPage_getSiteId();

		MessageBoardThread messageBoardThread1 = randomMessageBoardThread();

		messageBoardThread1 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, messageBoardThread1);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardThread> page =
				MessageBoardThreadResource.getSiteMessageBoardThreadsPage(
					siteId, null, null,
					getFilterString(
						entityField, "between", messageBoardThread1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardThread1),
				(List<MessageBoardThread>)page.getItems());
		}
	}

	@Test
	public void testGetSiteMessageBoardThreadsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteMessageBoardThreadsPage_getSiteId();

		MessageBoardThread messageBoardThread1 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, randomMessageBoardThread());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardThread messageBoardThread2 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, randomMessageBoardThread());

		for (EntityField entityField : entityFields) {
			Page<MessageBoardThread> page =
				MessageBoardThreadResource.getSiteMessageBoardThreadsPage(
					siteId, null, null,
					getFilterString(entityField, "eq", messageBoardThread1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardThread1),
				(List<MessageBoardThread>)page.getItems());
		}
	}

	@Test
	public void testGetSiteMessageBoardThreadsPageWithPagination()
		throws Exception {

		Long siteId = testGetSiteMessageBoardThreadsPage_getSiteId();

		MessageBoardThread messageBoardThread1 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, randomMessageBoardThread());

		MessageBoardThread messageBoardThread2 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, randomMessageBoardThread());

		MessageBoardThread messageBoardThread3 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, randomMessageBoardThread());

		Page<MessageBoardThread> page1 =
			MessageBoardThreadResource.getSiteMessageBoardThreadsPage(
				siteId, null, null, null, Pagination.of(1, 2), null);

		List<MessageBoardThread> messageBoardThreads1 =
			(List<MessageBoardThread>)page1.getItems();

		Assert.assertEquals(
			messageBoardThreads1.toString(), 2, messageBoardThreads1.size());

		Page<MessageBoardThread> page2 =
			MessageBoardThreadResource.getSiteMessageBoardThreadsPage(
				siteId, null, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<MessageBoardThread> messageBoardThreads2 =
			(List<MessageBoardThread>)page2.getItems();

		Assert.assertEquals(
			messageBoardThreads2.toString(), 1, messageBoardThreads2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(
				messageBoardThread1, messageBoardThread2, messageBoardThread3),
			new ArrayList<MessageBoardThread>() {
				{
					addAll(messageBoardThreads1);
					addAll(messageBoardThreads2);
				}
			});
	}

	@Test
	public void testGetSiteMessageBoardThreadsPageWithSortDateTime()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteMessageBoardThreadsPage_getSiteId();

		MessageBoardThread messageBoardThread1 = randomMessageBoardThread();
		MessageBoardThread messageBoardThread2 = randomMessageBoardThread();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				messageBoardThread1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		messageBoardThread1 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, messageBoardThread1);

		messageBoardThread2 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, messageBoardThread2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardThread> ascPage =
				MessageBoardThreadResource.getSiteMessageBoardThreadsPage(
					siteId, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardThread1, messageBoardThread2),
				(List<MessageBoardThread>)ascPage.getItems());

			Page<MessageBoardThread> descPage =
				MessageBoardThreadResource.getSiteMessageBoardThreadsPage(
					siteId, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardThread2, messageBoardThread1),
				(List<MessageBoardThread>)descPage.getItems());
		}
	}

	@Test
	public void testGetSiteMessageBoardThreadsPageWithSortString()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteMessageBoardThreadsPage_getSiteId();

		MessageBoardThread messageBoardThread1 = randomMessageBoardThread();
		MessageBoardThread messageBoardThread2 = randomMessageBoardThread();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				messageBoardThread1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(
				messageBoardThread2, entityField.getName(), "Bbb");
		}

		messageBoardThread1 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, messageBoardThread1);

		messageBoardThread2 =
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				siteId, messageBoardThread2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardThread> ascPage =
				MessageBoardThreadResource.getSiteMessageBoardThreadsPage(
					siteId, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardThread1, messageBoardThread2),
				(List<MessageBoardThread>)ascPage.getItems());

			Page<MessageBoardThread> descPage =
				MessageBoardThreadResource.getSiteMessageBoardThreadsPage(
					siteId, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardThread2, messageBoardThread1),
				(List<MessageBoardThread>)descPage.getItems());
		}
	}

	protected MessageBoardThread
			testGetSiteMessageBoardThreadsPage_addMessageBoardThread(
				Long siteId, MessageBoardThread messageBoardThread)
		throws Exception {

		return MessageBoardThreadResource.postSiteMessageBoardThread(
			siteId, messageBoardThread);
	}

	protected Long testGetSiteMessageBoardThreadsPage_getSiteId()
		throws Exception {

		return testGroup.getGroupId();
	}

	protected Long testGetSiteMessageBoardThreadsPage_getIrrelevantSiteId()
		throws Exception {

		return irrelevantGroup.getGroupId();
	}

	@Test
	public void testPostSiteMessageBoardThread() throws Exception {
		MessageBoardThread randomMessageBoardThread =
			randomMessageBoardThread();

		MessageBoardThread postMessageBoardThread =
			testPostSiteMessageBoardThread_addMessageBoardThread(
				randomMessageBoardThread);

		assertEquals(randomMessageBoardThread, postMessageBoardThread);
		assertValid(postMessageBoardThread);
	}

	protected MessageBoardThread
			testPostSiteMessageBoardThread_addMessageBoardThread(
				MessageBoardThread messageBoardThread)
		throws Exception {

		return MessageBoardThreadResource.postSiteMessageBoardThread(
			testGetSiteMessageBoardThreadsPage_getSiteId(), messageBoardThread);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		MessageBoardThread messageBoardThread1,
		MessageBoardThread messageBoardThread2) {

		Assert.assertTrue(
			messageBoardThread1 + " does not equal " + messageBoardThread2,
			equals(messageBoardThread1, messageBoardThread2));
	}

	protected void assertEquals(
		List<MessageBoardThread> messageBoardThreads1,
		List<MessageBoardThread> messageBoardThreads2) {

		Assert.assertEquals(
			messageBoardThreads1.size(), messageBoardThreads2.size());

		for (int i = 0; i < messageBoardThreads1.size(); i++) {
			MessageBoardThread messageBoardThread1 = messageBoardThreads1.get(
				i);
			MessageBoardThread messageBoardThread2 = messageBoardThreads2.get(
				i);

			assertEquals(messageBoardThread1, messageBoardThread2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<MessageBoardThread> messageBoardThreads1,
		List<MessageBoardThread> messageBoardThreads2) {

		Assert.assertEquals(
			messageBoardThreads1.size(), messageBoardThreads2.size());

		for (MessageBoardThread messageBoardThread1 : messageBoardThreads1) {
			boolean contains = false;

			for (MessageBoardThread messageBoardThread2 :
					messageBoardThreads2) {

				if (equals(messageBoardThread1, messageBoardThread2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				messageBoardThreads2 + " does not contain " +
					messageBoardThread1,
				contains);
		}
	}

	protected void assertValid(MessageBoardThread messageBoardThread) {
		boolean valid = true;

		if (messageBoardThread.getDateCreated() == null) {
			valid = false;
		}

		if (messageBoardThread.getDateModified() == null) {
			valid = false;
		}

		if (messageBoardThread.getId() == null) {
			valid = false;
		}

		if (!Objects.equals(
				messageBoardThread.getSiteId(), testGroup.getGroupId())) {

			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("aggregateRating", additionalAssertFieldName)) {
				if (messageBoardThread.getAggregateRating() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("articleBody", additionalAssertFieldName)) {
				if (messageBoardThread.getArticleBody() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (messageBoardThread.getCreator() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (messageBoardThread.getCustomFields() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("encodingFormat", additionalAssertFieldName)) {
				if (messageBoardThread.getEncodingFormat() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("headline", additionalAssertFieldName)) {
				if (messageBoardThread.getHeadline() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("keywords", additionalAssertFieldName)) {
				if (messageBoardThread.getKeywords() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfMessageBoardAttachments",
					additionalAssertFieldName)) {

				if (messageBoardThread.getNumberOfMessageBoardAttachments() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfMessageBoardMessages",
					additionalAssertFieldName)) {

				if (messageBoardThread.getNumberOfMessageBoardMessages() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("relatedContents", additionalAssertFieldName)) {
				if (messageBoardThread.getRelatedContents() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("showAsQuestion", additionalAssertFieldName)) {
				if (messageBoardThread.getShowAsQuestion() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("threadType", additionalAssertFieldName)) {
				if (messageBoardThread.getThreadType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("viewableBy", additionalAssertFieldName)) {
				if (messageBoardThread.getViewableBy() == null) {
					valid = false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Page<MessageBoardThread> page) {
		boolean valid = false;

		Collection<MessageBoardThread> messageBoardThreads = page.getItems();

		int size = messageBoardThreads.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected boolean equals(
		MessageBoardThread messageBoardThread1,
		MessageBoardThread messageBoardThread2) {

		if (messageBoardThread1 == messageBoardThread2) {
			return true;
		}

		if (!Objects.equals(
				messageBoardThread1.getSiteId(),
				messageBoardThread2.getSiteId())) {

			return false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("aggregateRating", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getAggregateRating(),
						messageBoardThread2.getAggregateRating())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("articleBody", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getArticleBody(),
						messageBoardThread2.getArticleBody())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getCreator(),
						messageBoardThread2.getCreator())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getCustomFields(),
						messageBoardThread2.getCustomFields())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getDateCreated(),
						messageBoardThread2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getDateModified(),
						messageBoardThread2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("encodingFormat", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getEncodingFormat(),
						messageBoardThread2.getEncodingFormat())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("headline", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getHeadline(),
						messageBoardThread2.getHeadline())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getId(),
						messageBoardThread2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("keywords", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getKeywords(),
						messageBoardThread2.getKeywords())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfMessageBoardAttachments",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						messageBoardThread1.
							getNumberOfMessageBoardAttachments(),
						messageBoardThread2.
							getNumberOfMessageBoardAttachments())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfMessageBoardMessages",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						messageBoardThread1.getNumberOfMessageBoardMessages(),
						messageBoardThread2.
							getNumberOfMessageBoardMessages())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("relatedContents", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getRelatedContents(),
						messageBoardThread2.getRelatedContents())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("showAsQuestion", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getShowAsQuestion(),
						messageBoardThread2.getShowAsQuestion())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("threadType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getThreadType(),
						messageBoardThread2.getThreadType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("viewableBy", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardThread1.getViewableBy(),
						messageBoardThread2.getViewableBy())) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected Collection<EntityField> getEntityFields() throws Exception {
		if (!(_messageBoardThreadResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_messageBoardThreadResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		Collection<EntityField> entityFields = getEntityFields();

		Stream<EntityField> stream = entityFields.stream();

		return stream.filter(
			entityField -> Objects.equals(entityField.getType(), type)
		).collect(
			Collectors.toList()
		);
	}

	protected String getFilterString(
		EntityField entityField, String operator,
		MessageBoardThread messageBoardThread) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("aggregateRating")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("articleBody")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardThread.getArticleBody()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("creator")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("customFields")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("dateCreated")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							messageBoardThread.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							messageBoardThread.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(
					_dateFormat.format(messageBoardThread.getDateCreated()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("dateModified")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							messageBoardThread.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							messageBoardThread.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(
					_dateFormat.format(messageBoardThread.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("encodingFormat")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardThread.getEncodingFormat()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("headline")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardThread.getHeadline()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("keywords")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("numberOfMessageBoardAttachments")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("numberOfMessageBoardMessages")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("relatedContents")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("showAsQuestion")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("siteId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("threadType")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardThread.getThreadType()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("viewableBy")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected MessageBoardThread randomMessageBoardThread() throws Exception {
		return new MessageBoardThread() {
			{
				articleBody = RandomTestUtil.randomString();
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				encodingFormat = RandomTestUtil.randomString();
				headline = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
				showAsQuestion = RandomTestUtil.randomBoolean();
				siteId = testGroup.getGroupId();
				threadType = RandomTestUtil.randomString();
			}
		};
	}

	protected MessageBoardThread randomIrrelevantMessageBoardThread()
		throws Exception {

		MessageBoardThread randomIrrelevantMessageBoardThread =
			randomMessageBoardThread();

		randomIrrelevantMessageBoardThread.setSiteId(
			irrelevantGroup.getGroupId());

		return randomIrrelevantMessageBoardThread;
	}

	protected MessageBoardThread randomPatchMessageBoardThread()
		throws Exception {

		return randomMessageBoardThread();
	}

	protected Group irrelevantGroup;
	protected Company testCompany;
	protected Group testGroup;
	protected Locale testLocale;
	protected String testUserNameAndPassword = "test@liferay.com:test";

	private static final Log _log = LogFactoryUtil.getLog(
		BaseMessageBoardThreadResourceTestCase.class);

	private static BeanUtilsBean _beanUtilsBean = new BeanUtilsBean() {

		@Override
		public void copyProperty(Object bean, String name, Object value)
			throws IllegalAccessException, InvocationTargetException {

			if (value != null) {
				super.copyProperty(bean, name, value);
			}
		}

	};
	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.headless.delivery.resource.v1_0.MessageBoardThreadResource
			_messageBoardThreadResource;

}