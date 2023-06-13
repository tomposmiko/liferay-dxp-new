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

package com.liferay.notification.context;

import com.liferay.notification.model.NotificationQueueEntry;
import com.liferay.notification.model.NotificationRecipient;
import com.liferay.notification.model.NotificationRecipientSetting;
import com.liferay.notification.model.NotificationTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Feliphe Marinho
 */
public class NotificationContext {

	public List<Long> getAttachmentObjectFieldIds() {
		if (_attachmentObjectFieldIds == null) {
			return Collections.emptyList();
		}

		return _attachmentObjectFieldIds;
	}

	public String getClassName() {
		return _className;
	}

	public long getClassPK() {
		return _classPK;
	}

	public String getExternalReferenceCode() {
		return _externalReferenceCode;
	}

	public List<Long> getFileEntryIds() {
		if (_fileEntryIds == null) {
			return Collections.emptyList();
		}

		return _fileEntryIds;
	}

	public NotificationQueueEntry getNotificationQueueEntry() {
		return _notificationQueueEntry;
	}

	public NotificationRecipient getNotificationRecipient() {
		return _notificationRecipient;
	}

	public List<NotificationRecipientSetting>
		getNotificationRecipientSettings() {

		return _notificationRecipientSettings;
	}

	public NotificationTemplate getNotificationTemplate() {
		return _notificationTemplate;
	}

	public String getPortletId() {
		return _portletId;
	}

	public Map<String, Object> getTermValues() {
		return _termValues;
	}

	public String getType() {
		return _type;
	}

	public long getUserId() {
		return _userId;
	}

	public void setAttachmentObjectFieldIds(
		List<Long> attachmentObjectFieldIds) {

		_attachmentObjectFieldIds = attachmentObjectFieldIds;
	}

	public void setClassName(String className) {
		_className = className;
	}

	public void setClassPK(long classPK) {
		_classPK = classPK;
	}

	public void setExternalReferenceCode(String externalReferenceCode) {
		_externalReferenceCode = externalReferenceCode;
	}

	public void setFileEntryIds(List<Long> fileEntryIds) {
		_fileEntryIds = fileEntryIds;
	}

	public void setNotificationQueueEntry(
		NotificationQueueEntry notificationQueueEntry) {

		_notificationQueueEntry = notificationQueueEntry;
	}

	public void setNotificationRecipient(
		NotificationRecipient notificationRecipient) {

		_notificationRecipient = notificationRecipient;
	}

	public void setNotificationRecipientSettings(
		List<NotificationRecipientSetting> notificationRecipientSettings) {

		_notificationRecipientSettings = notificationRecipientSettings;
	}

	public void setNotificationTemplate(
		NotificationTemplate notificationTemplate) {

		_notificationTemplate = notificationTemplate;
	}

	public void setPortletId(String portletId) {
		_portletId = portletId;
	}

	public void setTermValues(Map<String, Object> termValues) {
		_termValues = termValues;
	}

	public void setType(String type) {
		_type = type;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	private List<Long> _attachmentObjectFieldIds;
	private String _className;
	private long _classPK;
	private String _externalReferenceCode;
	private List<Long> _fileEntryIds;
	private NotificationQueueEntry _notificationQueueEntry;
	private NotificationRecipient _notificationRecipient;
	private List<NotificationRecipientSetting> _notificationRecipientSettings;
	private NotificationTemplate _notificationTemplate;
	private String _portletId;
	private Map<String, Object> _termValues;
	private String _type;
	private long _userId;

}