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

package com.liferay.headless.collaboration.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import com.liferay.headless.collaboration.dto.v1_0.MessageBoardMessage;
import com.liferay.headless.collaboration.resource.v1_0.MessageBoardMessageResource;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.InvocationTargetException;

import java.net.URL;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

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
public abstract class BaseMessageBoardMessageResourceTestCase {

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

		_resourceURL = new URL(
			"http://localhost:8080/o/headless-collaboration/v1.0");
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testDeleteMessageBoardMessage() throws Exception {
		MessageBoardMessage messageBoardMessage =
			testDeleteMessageBoardMessage_addMessageBoardMessage();

		assertResponseCode(
			204,
			invokeDeleteMessageBoardMessageResponse(
				messageBoardMessage.getId()));

		assertResponseCode(
			404,
			invokeGetMessageBoardMessageResponse(messageBoardMessage.getId()));
	}

	protected MessageBoardMessage
			testDeleteMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void invokeDeleteMessageBoardMessage(Long messageBoardMessageId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setDelete(true);

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}",
					messageBoardMessageId);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}
	}

	protected Http.Response invokeDeleteMessageBoardMessageResponse(
			Long messageBoardMessageId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setDelete(true);

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}",
					messageBoardMessageId);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetMessageBoardMessage() throws Exception {
		MessageBoardMessage postMessageBoardMessage =
			testGetMessageBoardMessage_addMessageBoardMessage();

		MessageBoardMessage getMessageBoardMessage =
			invokeGetMessageBoardMessage(postMessageBoardMessage.getId());

		assertEquals(postMessageBoardMessage, getMessageBoardMessage);
		assertValid(getMessageBoardMessage);
	}

	protected MessageBoardMessage
			testGetMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected MessageBoardMessage invokeGetMessageBoardMessage(
			Long messageBoardMessageId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}",
					messageBoardMessageId);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(
				string, MessageBoardMessage.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokeGetMessageBoardMessageResponse(
			Long messageBoardMessageId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}",
					messageBoardMessageId);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPatchMessageBoardMessage() throws Exception {
		MessageBoardMessage postMessageBoardMessage =
			testPatchMessageBoardMessage_addMessageBoardMessage();

		MessageBoardMessage randomPatchMessageBoardMessage =
			randomPatchMessageBoardMessage();

		MessageBoardMessage patchMessageBoardMessage =
			invokePatchMessageBoardMessage(
				postMessageBoardMessage.getId(),
				randomPatchMessageBoardMessage);

		MessageBoardMessage expectedPatchMessageBoardMessage =
			(MessageBoardMessage)BeanUtils.cloneBean(postMessageBoardMessage);

		_beanUtilsBean.copyProperties(
			expectedPatchMessageBoardMessage, randomPatchMessageBoardMessage);

		MessageBoardMessage getMessageBoardMessage =
			invokeGetMessageBoardMessage(patchMessageBoardMessage.getId());

		assertEquals(expectedPatchMessageBoardMessage, getMessageBoardMessage);
		assertValid(getMessageBoardMessage);
	}

	protected MessageBoardMessage
			testPatchMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected MessageBoardMessage invokePatchMessageBoardMessage(
			Long messageBoardMessageId, MessageBoardMessage messageBoardMessage)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(messageBoardMessage),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}",
					messageBoardMessageId);

		options.setLocation(location);

		options.setPatch(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(
				string, MessageBoardMessage.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePatchMessageBoardMessageResponse(
			Long messageBoardMessageId, MessageBoardMessage messageBoardMessage)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(messageBoardMessage),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}",
					messageBoardMessageId);

		options.setLocation(location);

		options.setPatch(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPutMessageBoardMessage() throws Exception {
		MessageBoardMessage postMessageBoardMessage =
			testPutMessageBoardMessage_addMessageBoardMessage();

		MessageBoardMessage randomMessageBoardMessage =
			randomMessageBoardMessage();

		MessageBoardMessage putMessageBoardMessage =
			invokePutMessageBoardMessage(
				postMessageBoardMessage.getId(), randomMessageBoardMessage);

		assertEquals(randomMessageBoardMessage, putMessageBoardMessage);
		assertValid(putMessageBoardMessage);

		MessageBoardMessage getMessageBoardMessage =
			invokeGetMessageBoardMessage(putMessageBoardMessage.getId());

		assertEquals(randomMessageBoardMessage, getMessageBoardMessage);
		assertValid(getMessageBoardMessage);
	}

	protected MessageBoardMessage
			testPutMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected MessageBoardMessage invokePutMessageBoardMessage(
			Long messageBoardMessageId, MessageBoardMessage messageBoardMessage)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(messageBoardMessage),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}",
					messageBoardMessageId);

		options.setLocation(location);

		options.setPut(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(
				string, MessageBoardMessage.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePutMessageBoardMessageResponse(
			Long messageBoardMessageId, MessageBoardMessage messageBoardMessage)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(messageBoardMessage),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}",
					messageBoardMessageId);

		options.setLocation(location);

		options.setPut(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPage()
		throws Exception {

		Long messageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getMessageBoardMessageId();
		Long irrelevantMessageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getIrrelevantMessageBoardMessageId();

		if ((irrelevantMessageBoardMessageId != null)) {
			MessageBoardMessage irrelevantMessageBoardMessage =
				testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
					irrelevantMessageBoardMessageId,
					randomIrrelevantMessageBoardMessage());

			Page<MessageBoardMessage> page =
				invokeGetMessageBoardMessageMessageBoardMessagesPage(
					irrelevantMessageBoardMessageId, null, null,
					Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantMessageBoardMessage),
				(List<MessageBoardMessage>)page.getItems());
			assertValid(page);
		}

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, randomMessageBoardMessage());

		Page<MessageBoardMessage> page =
			invokeGetMessageBoardMessageMessageBoardMessagesPage(
				messageBoardMessageId, null, null, Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(messageBoardMessage1, messageBoardMessage2),
			(List<MessageBoardMessage>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getMessageBoardMessageId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();
		MessageBoardMessage messageBoardMessage2 = randomMessageBoardMessage();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				messageBoardMessage1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, messageBoardMessage1);

		Thread.sleep(1000);

		messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, messageBoardMessage2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				invokeGetMessageBoardMessageMessageBoardMessagesPage(
					messageBoardMessageId, null,
					getFilterString(entityField, "eq", messageBoardMessage1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getMessageBoardMessageId();

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, randomMessageBoardMessage());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, randomMessageBoardMessage());

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				invokeGetMessageBoardMessageMessageBoardMessagesPage(
					messageBoardMessageId, null,
					getFilterString(entityField, "eq", messageBoardMessage1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithPagination()
		throws Exception {

		Long messageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getMessageBoardMessageId();

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage3 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, randomMessageBoardMessage());

		Page<MessageBoardMessage> page1 =
			invokeGetMessageBoardMessageMessageBoardMessagesPage(
				messageBoardMessageId, null, null, Pagination.of(1, 2), null);

		List<MessageBoardMessage> messageBoardMessages1 =
			(List<MessageBoardMessage>)page1.getItems();

		Assert.assertEquals(
			messageBoardMessages1.toString(), 2, messageBoardMessages1.size());

		Page<MessageBoardMessage> page2 =
			invokeGetMessageBoardMessageMessageBoardMessagesPage(
				messageBoardMessageId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<MessageBoardMessage> messageBoardMessages2 =
			(List<MessageBoardMessage>)page2.getItems();

		Assert.assertEquals(
			messageBoardMessages2.toString(), 1, messageBoardMessages2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(
				messageBoardMessage1, messageBoardMessage2,
				messageBoardMessage3),
			new ArrayList<MessageBoardMessage>() {
				{
					addAll(messageBoardMessages1);
					addAll(messageBoardMessages2);
				}
			});
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithSortDateTime()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getMessageBoardMessageId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();
		MessageBoardMessage messageBoardMessage2 = randomMessageBoardMessage();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				messageBoardMessage1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, messageBoardMessage1);

		Thread.sleep(1000);

		messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, messageBoardMessage2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> ascPage =
				invokeGetMessageBoardMessageMessageBoardMessagesPage(
					messageBoardMessageId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardMessage1, messageBoardMessage2),
				(List<MessageBoardMessage>)ascPage.getItems());

			Page<MessageBoardMessage> descPage =
				invokeGetMessageBoardMessageMessageBoardMessagesPage(
					messageBoardMessageId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardMessage2, messageBoardMessage1),
				(List<MessageBoardMessage>)descPage.getItems());
		}
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithSortString()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getMessageBoardMessageId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();
		MessageBoardMessage messageBoardMessage2 = randomMessageBoardMessage();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				messageBoardMessage1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(
				messageBoardMessage2, entityField.getName(), "Bbb");
		}

		messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, messageBoardMessage1);

		messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardMessageId, messageBoardMessage2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> ascPage =
				invokeGetMessageBoardMessageMessageBoardMessagesPage(
					messageBoardMessageId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardMessage1, messageBoardMessage2),
				(List<MessageBoardMessage>)ascPage.getItems());

			Page<MessageBoardMessage> descPage =
				invokeGetMessageBoardMessageMessageBoardMessagesPage(
					messageBoardMessageId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardMessage2, messageBoardMessage1),
				(List<MessageBoardMessage>)descPage.getItems());
		}
	}

	protected MessageBoardMessage
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				Long messageBoardMessageId,
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetMessageBoardMessageMessageBoardMessagesPage_getMessageBoardMessageId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetMessageBoardMessageMessageBoardMessagesPage_getIrrelevantMessageBoardMessageId()
		throws Exception {

		return null;
	}

	protected Page<MessageBoardMessage>
			invokeGetMessageBoardMessageMessageBoardMessagesPage(
				Long messageBoardMessageId, String search, String filterString,
				Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}/message-board-messages",
					messageBoardMessageId);

		location = HttpUtil.addParameter(location, "filter", filterString);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		location = HttpUtil.addParameter(location, "sort", sortString);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		return _outputObjectMapper.readValue(
			string,
			new TypeReference<Page<MessageBoardMessage>>() {
			});
	}

	protected Http.Response
			invokeGetMessageBoardMessageMessageBoardMessagesPageResponse(
				Long messageBoardMessageId, String search, String filterString,
				Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}/message-board-messages",
					messageBoardMessageId);

		location = HttpUtil.addParameter(location, "filter", filterString);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		location = HttpUtil.addParameter(location, "sort", sortString);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPostMessageBoardMessageMessageBoardMessage()
		throws Exception {

		MessageBoardMessage randomMessageBoardMessage =
			randomMessageBoardMessage();

		MessageBoardMessage postMessageBoardMessage =
			testPostMessageBoardMessageMessageBoardMessage_addMessageBoardMessage(
				randomMessageBoardMessage);

		assertEquals(randomMessageBoardMessage, postMessageBoardMessage);
		assertValid(postMessageBoardMessage);
	}

	protected MessageBoardMessage
			testPostMessageBoardMessageMessageBoardMessage_addMessageBoardMessage(
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected MessageBoardMessage
			invokePostMessageBoardMessageMessageBoardMessage(
				Long messageBoardMessageId,
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(messageBoardMessage),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}/message-board-messages",
					messageBoardMessageId);

		options.setLocation(location);

		options.setPost(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(
				string, MessageBoardMessage.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response
			invokePostMessageBoardMessageMessageBoardMessageResponse(
				Long messageBoardMessageId,
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(messageBoardMessage),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/message-board-messages/{message-board-message-id}/message-board-messages",
					messageBoardMessageId);

		options.setLocation(location);

		options.setPost(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPage()
		throws Exception {

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();
		Long irrelevantMessageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getIrrelevantMessageBoardThreadId();

		if ((irrelevantMessageBoardThreadId != null)) {
			MessageBoardMessage irrelevantMessageBoardMessage =
				testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
					irrelevantMessageBoardThreadId,
					randomIrrelevantMessageBoardMessage());

			Page<MessageBoardMessage> page =
				invokeGetMessageBoardThreadMessageBoardMessagesPage(
					irrelevantMessageBoardThreadId, null, null,
					Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantMessageBoardMessage),
				(List<MessageBoardMessage>)page.getItems());
			assertValid(page);
		}

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		Page<MessageBoardMessage> page =
			invokeGetMessageBoardThreadMessageBoardMessagesPage(
				messageBoardThreadId, null, null, Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(messageBoardMessage1, messageBoardMessage2),
			(List<MessageBoardMessage>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();
		MessageBoardMessage messageBoardMessage2 = randomMessageBoardMessage();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				messageBoardMessage1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, messageBoardMessage1);

		Thread.sleep(1000);

		messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, messageBoardMessage2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				invokeGetMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null,
					getFilterString(entityField, "eq", messageBoardMessage1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				invokeGetMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null,
					getFilterString(entityField, "eq", messageBoardMessage1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithPagination()
		throws Exception {

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage3 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		Page<MessageBoardMessage> page1 =
			invokeGetMessageBoardThreadMessageBoardMessagesPage(
				messageBoardThreadId, null, null, Pagination.of(1, 2), null);

		List<MessageBoardMessage> messageBoardMessages1 =
			(List<MessageBoardMessage>)page1.getItems();

		Assert.assertEquals(
			messageBoardMessages1.toString(), 2, messageBoardMessages1.size());

		Page<MessageBoardMessage> page2 =
			invokeGetMessageBoardThreadMessageBoardMessagesPage(
				messageBoardThreadId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<MessageBoardMessage> messageBoardMessages2 =
			(List<MessageBoardMessage>)page2.getItems();

		Assert.assertEquals(
			messageBoardMessages2.toString(), 1, messageBoardMessages2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(
				messageBoardMessage1, messageBoardMessage2,
				messageBoardMessage3),
			new ArrayList<MessageBoardMessage>() {
				{
					addAll(messageBoardMessages1);
					addAll(messageBoardMessages2);
				}
			});
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithSortDateTime()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();
		MessageBoardMessage messageBoardMessage2 = randomMessageBoardMessage();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				messageBoardMessage1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, messageBoardMessage1);

		Thread.sleep(1000);

		messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, messageBoardMessage2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> ascPage =
				invokeGetMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardMessage1, messageBoardMessage2),
				(List<MessageBoardMessage>)ascPage.getItems());

			Page<MessageBoardMessage> descPage =
				invokeGetMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardMessage2, messageBoardMessage1),
				(List<MessageBoardMessage>)descPage.getItems());
		}
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithSortString()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();
		MessageBoardMessage messageBoardMessage2 = randomMessageBoardMessage();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				messageBoardMessage1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(
				messageBoardMessage2, entityField.getName(), "Bbb");
		}

		messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, messageBoardMessage1);

		messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, messageBoardMessage2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> ascPage =
				invokeGetMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardMessage1, messageBoardMessage2),
				(List<MessageBoardMessage>)ascPage.getItems());

			Page<MessageBoardMessage> descPage =
				invokeGetMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardMessage2, messageBoardMessage1),
				(List<MessageBoardMessage>)descPage.getItems());
		}
	}

	protected MessageBoardMessage
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				Long messageBoardThreadId,
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetMessageBoardThreadMessageBoardMessagesPage_getIrrelevantMessageBoardThreadId()
		throws Exception {

		return null;
	}

	protected Page<MessageBoardMessage>
			invokeGetMessageBoardThreadMessageBoardMessagesPage(
				Long messageBoardThreadId, String search, String filterString,
				Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/message-board-threads/{message-board-thread-id}/message-board-messages",
					messageBoardThreadId);

		location = HttpUtil.addParameter(location, "filter", filterString);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		location = HttpUtil.addParameter(location, "sort", sortString);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		return _outputObjectMapper.readValue(
			string,
			new TypeReference<Page<MessageBoardMessage>>() {
			});
	}

	protected Http.Response
			invokeGetMessageBoardThreadMessageBoardMessagesPageResponse(
				Long messageBoardThreadId, String search, String filterString,
				Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/message-board-threads/{message-board-thread-id}/message-board-messages",
					messageBoardThreadId);

		location = HttpUtil.addParameter(location, "filter", filterString);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		location = HttpUtil.addParameter(location, "sort", sortString);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPostMessageBoardThreadMessageBoardMessage()
		throws Exception {

		MessageBoardMessage randomMessageBoardMessage =
			randomMessageBoardMessage();

		MessageBoardMessage postMessageBoardMessage =
			testPostMessageBoardThreadMessageBoardMessage_addMessageBoardMessage(
				randomMessageBoardMessage);

		assertEquals(randomMessageBoardMessage, postMessageBoardMessage);
		assertValid(postMessageBoardMessage);
	}

	protected MessageBoardMessage
			testPostMessageBoardThreadMessageBoardMessage_addMessageBoardMessage(
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected MessageBoardMessage
			invokePostMessageBoardThreadMessageBoardMessage(
				Long messageBoardThreadId,
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(messageBoardMessage),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/message-board-threads/{message-board-thread-id}/message-board-messages",
					messageBoardThreadId);

		options.setLocation(location);

		options.setPost(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(
				string, MessageBoardMessage.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response
			invokePostMessageBoardThreadMessageBoardMessageResponse(
				Long messageBoardThreadId,
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(messageBoardMessage),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/message-board-threads/{message-board-thread-id}/message-board-messages",
					messageBoardThreadId);

		options.setLocation(location);

		options.setPost(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	protected void assertResponseCode(
		int expectedResponseCode, Http.Response actualResponse) {

		Assert.assertEquals(
			expectedResponseCode, actualResponse.getResponseCode());
	}

	protected void assertEquals(
		MessageBoardMessage messageBoardMessage1,
		MessageBoardMessage messageBoardMessage2) {

		Assert.assertTrue(
			messageBoardMessage1 + " does not equal " + messageBoardMessage2,
			equals(messageBoardMessage1, messageBoardMessage2));
	}

	protected void assertEquals(
		List<MessageBoardMessage> messageBoardMessages1,
		List<MessageBoardMessage> messageBoardMessages2) {

		Assert.assertEquals(
			messageBoardMessages1.size(), messageBoardMessages2.size());

		for (int i = 0; i < messageBoardMessages1.size(); i++) {
			MessageBoardMessage messageBoardMessage1 =
				messageBoardMessages1.get(i);
			MessageBoardMessage messageBoardMessage2 =
				messageBoardMessages2.get(i);

			assertEquals(messageBoardMessage1, messageBoardMessage2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<MessageBoardMessage> messageBoardMessages1,
		List<MessageBoardMessage> messageBoardMessages2) {

		Assert.assertEquals(
			messageBoardMessages1.size(), messageBoardMessages2.size());

		for (MessageBoardMessage messageBoardMessage1 : messageBoardMessages1) {
			boolean contains = false;

			for (MessageBoardMessage messageBoardMessage2 :
					messageBoardMessages2) {

				if (equals(messageBoardMessage1, messageBoardMessage2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				messageBoardMessages2 + " does not contain " +
					messageBoardMessage1,
				contains);
		}
	}

	protected void assertValid(MessageBoardMessage messageBoardMessage) {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertValid(Page<MessageBoardMessage> page) {
		boolean valid = false;

		Collection<MessageBoardMessage> messageBoardMessages = page.getItems();

		int size = messageBoardMessages.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected boolean equals(
		MessageBoardMessage messageBoardMessage1,
		MessageBoardMessage messageBoardMessage2) {

		if (messageBoardMessage1 == messageBoardMessage2) {
			return true;
		}

		return false;
	}

	protected Collection<EntityField> getEntityFields() throws Exception {
		if (!(_messageBoardMessageResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_messageBoardMessageResource;

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
		MessageBoardMessage messageBoardMessage) {

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

		if (entityFieldName.equals("anonymous")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("articleBody")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardMessage.getArticleBody()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("contentSpaceId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("creator")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("dateCreated")) {
			sb.append(_dateFormat.format(messageBoardMessage.getDateCreated()));

			return sb.toString();
		}

		if (entityFieldName.equals("dateModified")) {
			sb.append(
				_dateFormat.format(messageBoardMessage.getDateModified()));

			return sb.toString();
		}

		if (entityFieldName.equals("encodingFormat")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardMessage.getEncodingFormat()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("headline")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardMessage.getHeadline()));
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

		if (entityFieldName.equals("showAsAnswer")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("taxonomyCategories")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("taxonomyCategoryIds")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("viewableBy")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected MessageBoardMessage randomMessageBoardMessage() {
		return new MessageBoardMessage() {
			{
				anonymous = RandomTestUtil.randomBoolean();
				articleBody = RandomTestUtil.randomString();
				contentSpaceId = RandomTestUtil.randomLong();
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				encodingFormat = RandomTestUtil.randomString();
				headline = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
				showAsAnswer = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected MessageBoardMessage randomIrrelevantMessageBoardMessage() {
		return randomMessageBoardMessage();
	}

	protected MessageBoardMessage randomPatchMessageBoardMessage() {
		return randomMessageBoardMessage();
	}

	protected Group irrelevantGroup;
	protected Group testGroup;

	protected static class Page<T> {

		public Collection<T> getItems() {
			return new ArrayList<>(items);
		}

		public long getLastPage() {
			return lastPage;
		}

		public long getPage() {
			return page;
		}

		public long getPageSize() {
			return pageSize;
		}

		public long getTotalCount() {
			return totalCount;
		}

		@JsonProperty
		protected Collection<T> items;

		@JsonProperty
		protected long lastPage;

		@JsonProperty
		protected long page;

		@JsonProperty
		protected long pageSize;

		@JsonProperty
		protected long totalCount;

	}

	private Http.Options _createHttpOptions() {
		Http.Options options = new Http.Options();

		options.addHeader("Accept", "application/json");

		String userNameAndPassword = "test@liferay.com:test";

		String encodedUserNameAndPassword = Base64.encode(
			userNameAndPassword.getBytes());

		options.addHeader(
			"Authorization", "Basic " + encodedUserNameAndPassword);

		options.addHeader("Content-Type", "application/json");

		return options;
	}

	private String _toPath(String template, Object... values) {
		if (ArrayUtil.isEmpty(values)) {
			return template;
		}

		for (int i = 0; i < values.length; i++) {
			template = template.replaceFirst(
				"\\{.*?\\}", String.valueOf(values[i]));
		}

		return template;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseMessageBoardMessageResourceTestCase.class);

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
	private final static ObjectMapper _inputObjectMapper = new ObjectMapper() {
		{
			setFilterProvider(
				new SimpleFilterProvider() {
					{
						addFilter(
							"Liferay.Vulcan",
							SimpleBeanPropertyFilter.serializeAll());
					}
				});
			setSerializationInclusion(JsonInclude.Include.NON_NULL);
		}
	};
	private final static ObjectMapper _outputObjectMapper = new ObjectMapper() {
		{
			setFilterProvider(
				new SimpleFilterProvider() {
					{
						addFilter(
							"Liferay.Vulcan",
							SimpleBeanPropertyFilter.serializeAll());
					}
				});
		}
	};

	@Inject
	private MessageBoardMessageResource _messageBoardMessageResource;

	private URL _resourceURL;

}