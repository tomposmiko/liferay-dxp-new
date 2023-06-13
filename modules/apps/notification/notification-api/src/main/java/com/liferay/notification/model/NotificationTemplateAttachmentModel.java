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

package com.liferay.notification.model;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the NotificationTemplateAttachment service. Represents a row in the &quot;NTemplateAttachment&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.notification.model.impl.NotificationTemplateAttachmentModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.notification.model.impl.NotificationTemplateAttachmentImpl</code>.
 * </p>
 *
 * @author Gabriel Albuquerque
 * @see NotificationTemplateAttachment
 * @generated
 */
@ProviderType
public interface NotificationTemplateAttachmentModel
	extends BaseModel<NotificationTemplateAttachment>, MVCCModel, ShardedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a notification template attachment model instance should use the {@link NotificationTemplateAttachment} interface instead.
	 */

	/**
	 * Returns the primary key of this notification template attachment.
	 *
	 * @return the primary key of this notification template attachment
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this notification template attachment.
	 *
	 * @param primaryKey the primary key of this notification template attachment
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this notification template attachment.
	 *
	 * @return the mvcc version of this notification template attachment
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this notification template attachment.
	 *
	 * @param mvccVersion the mvcc version of this notification template attachment
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the notification template attachment ID of this notification template attachment.
	 *
	 * @return the notification template attachment ID of this notification template attachment
	 */
	public long getNotificationTemplateAttachmentId();

	/**
	 * Sets the notification template attachment ID of this notification template attachment.
	 *
	 * @param notificationTemplateAttachmentId the notification template attachment ID of this notification template attachment
	 */
	public void setNotificationTemplateAttachmentId(
		long notificationTemplateAttachmentId);

	/**
	 * Returns the company ID of this notification template attachment.
	 *
	 * @return the company ID of this notification template attachment
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this notification template attachment.
	 *
	 * @param companyId the company ID of this notification template attachment
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the notification template ID of this notification template attachment.
	 *
	 * @return the notification template ID of this notification template attachment
	 */
	public long getNotificationTemplateId();

	/**
	 * Sets the notification template ID of this notification template attachment.
	 *
	 * @param notificationTemplateId the notification template ID of this notification template attachment
	 */
	public void setNotificationTemplateId(long notificationTemplateId);

	/**
	 * Returns the object field ID of this notification template attachment.
	 *
	 * @return the object field ID of this notification template attachment
	 */
	public long getObjectFieldId();

	/**
	 * Sets the object field ID of this notification template attachment.
	 *
	 * @param objectFieldId the object field ID of this notification template attachment
	 */
	public void setObjectFieldId(long objectFieldId);

	@Override
	public NotificationTemplateAttachment cloneWithOriginalValues();

	public default String toXmlString() {
		return null;
	}

}