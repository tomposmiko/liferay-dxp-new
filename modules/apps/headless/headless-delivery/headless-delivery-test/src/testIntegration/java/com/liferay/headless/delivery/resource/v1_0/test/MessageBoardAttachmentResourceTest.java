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

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.headless.delivery.client.dto.v1_0.MessageBoardAttachment;
import com.liferay.headless.delivery.client.http.HttpInvoker;
import com.liferay.message.boards.model.MBMessage;
import com.liferay.message.boards.model.MBThread;
import com.liferay.message.boards.test.util.MBTestUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.constants.TestDataConstants;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.io.File;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Javier Gamarra
 */
@RunWith(Arquillian.class)
public class MessageBoardAttachmentResourceTest
	extends BaseMessageBoardAttachmentResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		MBMessage mbMessage = _addMBMessage();

		_mbThread = mbMessage.getThread();
	}

	@Override
	@Test
	public void testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode()
		throws Exception {

		super.
			testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode();

		MessageBoardAttachment messageBoardAttachment =
			testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_addMessageBoardAttachment();

		// Message board attachment associated to a different message board
		// message

		MBMessage mbMessage = _mbMessage;

		MessageBoardAttachment newMessageBoardAttachment =
			testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_addMessageBoardAttachment();

		assertHttpResponseStatusCode(
			404,
			messageBoardAttachmentResource.
				deleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCodeHttpResponse(
					testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_getSiteId(),
					mbMessage.getExternalReferenceCode(),
					newMessageBoardAttachment.getExternalReferenceCode()));

		// Nonexistent message board message

		assertHttpResponseStatusCode(
			404,
			messageBoardAttachmentResource.
				deleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCodeHttpResponse(
					testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_getSiteId(),
					RandomTestUtil.randomString(),
					messageBoardAttachment.getExternalReferenceCode()));

		// Nonexistent message board message attachment

		assertHttpResponseStatusCode(
			404,
			messageBoardAttachmentResource.
				deleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCodeHttpResponse(
					testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_getSiteId(),
					testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_getMessageBoardMessageExternalReferenceCode(),
					RandomTestUtil.randomString()));
	}

	@Override
	protected void assertValid(
			MessageBoardAttachment messageBoardAttachment,
			Map<String, File> multipartFiles)
		throws Exception {

		Assert.assertEquals(
			new String(FileUtil.getBytes(multipartFiles.get("file"))),
			_read(
				"http://localhost:8080" +
					messageBoardAttachment.getContentUrl()));
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"title"};
	}

	@Override
	protected Map<String, File> getMultipartFiles() throws Exception {
		return HashMapBuilder.<String, File>put(
			"file",
			() -> {
				File file = new File(_tempFileName);

				FileUtil.write(file, TestDataConstants.TEST_BYTE_ARRAY);

				return file;
			}
		).build();
	}

	@Override
	protected MessageBoardAttachment randomMessageBoardAttachment()
		throws Exception {

		MessageBoardAttachment messageBoardAttachment =
			super.randomMessageBoardAttachment();

		_tempFileName = FileUtil.createTempFileName();

		File file = new File(_tempFileName);

		messageBoardAttachment.setTitle(file.getName());

		return messageBoardAttachment;
	}

	@Override
	protected MessageBoardAttachment
			testDeleteMessageBoardAttachment_addMessageBoardAttachment()
		throws Exception {

		return messageBoardAttachmentResource.
			postMessageBoardThreadMessageBoardAttachment(
				_mbThread.getThreadId(), randomMessageBoardAttachment(),
				getMultipartFiles());
	}

	@Override
	protected MessageBoardAttachment
			testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_addMessageBoardAttachment()
		throws Exception {

		_mbMessage = _addMBMessage();

		return messageBoardAttachmentResource.
			postMessageBoardMessageMessageBoardAttachment(
				_mbMessage.getMessageId(), randomMessageBoardAttachment(),
				getMultipartFiles());
	}

	@Override
	protected String
			testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_getMessageBoardMessageExternalReferenceCode()
		throws Exception {

		return _mbMessage.getExternalReferenceCode();
	}

	@Override
	protected Long
			testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_getSiteId()
		throws Exception {

		return testGroup.getGroupId();
	}

	@Override
	protected MessageBoardAttachment
			testGetMessageBoardAttachment_addMessageBoardAttachment()
		throws Exception {

		return messageBoardAttachmentResource.
			postMessageBoardThreadMessageBoardAttachment(
				_mbThread.getThreadId(), randomMessageBoardAttachment(),
				getMultipartFiles());
	}

	@Override
	protected Long
		testGetMessageBoardMessageMessageBoardAttachmentsPage_getMessageBoardMessageId() {

		return _mbThread.getRootMessageId();
	}

	@Override
	protected Long
		testGetMessageBoardThreadMessageBoardAttachmentsPage_getMessageBoardThreadId() {

		return _mbThread.getThreadId();
	}

	@Override
	protected MessageBoardAttachment
			testGetSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_addMessageBoardAttachment()
		throws Exception {

		_mbMessage = _addMBMessage();

		return messageBoardAttachmentResource.
			postMessageBoardMessageMessageBoardAttachment(
				_mbMessage.getMessageId(), randomMessageBoardAttachment(),
				getMultipartFiles());
	}

	@Override
	protected String
			testGetSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_getMessageBoardMessageExternalReferenceCode()
		throws Exception {

		return _mbMessage.getExternalReferenceCode();
	}

	@Override
	protected Long
			testGetSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_getSiteId()
		throws Exception {

		return testGroup.getGroupId();
	}

	@Override
	protected MessageBoardAttachment
			testGraphQLGetSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_addMessageBoardAttachment()
		throws Exception {

		return testDeleteSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_addMessageBoardAttachment();
	}

	@Override
	protected String
			testGraphQLGetSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_getMessageBoardMessageExternalReferenceCode()
		throws Exception {

		return _mbMessage.getExternalReferenceCode();
	}

	@Override
	protected Long
			testGraphQLGetSiteMessageBoardMessageByExternalReferenceCodeMessageBoardMessageExternalReferenceCodeMessageBoardAttachmentByExternalReferenceCode_getSiteId()
		throws Exception {

		return testGroup.getGroupId();
	}

	@Override
	protected MessageBoardAttachment
			testGraphQLMessageBoardAttachment_addMessageBoardAttachment()
		throws Exception {

		return testDeleteMessageBoardAttachment_addMessageBoardAttachment();
	}

	private MBMessage _addMBMessage() throws Exception {
		return MBTestUtil.addMessage(
			testGroup.getGroupId(),
			UserLocalServiceUtil.getDefaultUserId(testGroup.getCompanyId()),
			RandomTestUtil.randomString(), RandomTestUtil.randomString());
	}

	private String _read(String url) throws Exception {
		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);
		httpInvoker.path(url);
		httpInvoker.userNameAndPassword("test@liferay.com:test");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse.getContent();
	}

	private MBMessage _mbMessage;
	private MBThread _mbThread;
	private String _tempFileName;

}