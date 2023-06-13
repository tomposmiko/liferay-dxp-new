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

package com.liferay.notification.service.impl;

import com.liferay.notification.constants.NotificationTermContributorConstants;
import com.liferay.notification.exception.NotificationTemplateFromException;
import com.liferay.notification.exception.NotificationTemplateNameException;
import com.liferay.notification.model.NotificationQueueEntry;
import com.liferay.notification.model.NotificationTemplate;
import com.liferay.notification.service.NotificationQueueEntryLocalService;
import com.liferay.notification.service.base.NotificationTemplateLocalServiceBaseImpl;
import com.liferay.notification.service.persistence.NotificationQueueEntryPersistence;
import com.liferay.notification.term.contributor.NotificationTermContributor;
import com.liferay.notification.term.contributor.NotificationTermContributorRegistry;
import com.liferay.notification.type.NotificationType;
import com.liferay.notification.util.NotificationTypeRegistry;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.EmailAddressValidator;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.EmailAddressValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gabriel Albuquerque
 * @author Gustavo Lima
 */
@Component(
	property = "model.class.name=com.liferay.notification.model.NotificationTemplate",
	service = AopService.class
)
public class NotificationTemplateLocalServiceImpl
	extends NotificationTemplateLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public NotificationTemplate addNotificationTemplate(
			long userId, String bcc, Map<Locale, String> bodyMap, String cc,
			String description, String from, Map<Locale, String> fromNameMap,
			String name, Map<Locale, String> subjectMap,
			Map<Locale, String> toMap)
		throws PortalException {

		_validate(name, from);

		NotificationTemplate notificationTemplate =
			notificationTemplatePersistence.create(
				counterLocalService.increment());

		User user = _userLocalService.getUser(userId);

		notificationTemplate.setCompanyId(user.getCompanyId());
		notificationTemplate.setUserId(user.getUserId());
		notificationTemplate.setUserName(user.getFullName());

		notificationTemplate.setBcc(bcc);
		notificationTemplate.setBodyMap(bodyMap);
		notificationTemplate.setCc(cc);
		notificationTemplate.setDescription(description);
		notificationTemplate.setFrom(from);
		notificationTemplate.setFromNameMap(fromNameMap);
		notificationTemplate.setName(name);
		notificationTemplate.setSubjectMap(subjectMap);
		notificationTemplate.setToMap(toMap);

		notificationTemplate = notificationTemplatePersistence.update(
			notificationTemplate);

		_resourceLocalService.addResources(
			notificationTemplate.getCompanyId(), 0,
			notificationTemplate.getUserId(),
			NotificationTemplate.class.getName(),
			notificationTemplate.getNotificationTemplateId(), false, true,
			true);

		return notificationTemplate;
	}

	@Override
	public NotificationTemplate deleteNotificationTemplate(
			long notificationTemplateId)
		throws PortalException {

		NotificationTemplate notificationTemplate =
			notificationTemplatePersistence.findByPrimaryKey(
				notificationTemplateId);

		return notificationTemplateLocalService.deleteNotificationTemplate(
			notificationTemplate);
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public NotificationTemplate deleteNotificationTemplate(
			NotificationTemplate notificationTemplate)
		throws PortalException {

		notificationTemplate = notificationTemplatePersistence.remove(
			notificationTemplate);

		_resourceLocalService.deleteResource(
			notificationTemplate, ResourceConstants.SCOPE_INDIVIDUAL);

		List<NotificationQueueEntry> notificationQueueEntries =
			_notificationQueueEntryPersistence.findByNotificationTemplateId(
				notificationTemplate.getNotificationTemplateId());

		for (NotificationQueueEntry notificationQueueEntry :
				notificationQueueEntries) {

			notificationQueueEntry.setNotificationTemplateId(0);

			_notificationQueueEntryPersistence.update(notificationQueueEntry);
		}

		return notificationTemplate;
	}

	@Override
	public void sendNotificationTemplate(
			long userId, long notificationTemplateId,
			String notificationTypeKey, Object object)
		throws PortalException {

		if (Validator.isBlank(notificationTypeKey)) {
			return;
		}

		NotificationType notificationType =
			_notificationTypeRegistry.getNotificationType(notificationTypeKey);

		if (notificationType == null) {
			return;
		}

		NotificationTemplate notificationTemplate =
			notificationTemplatePersistence.findByPrimaryKey(
				notificationTemplateId);

		User user = _userLocalService.getUser(userId);

		String body = _formatContent(
			notificationTemplate.getBody(user.getLocale()), user.getLocale(),
			null, notificationType, object);

		Locale siteDefaultLocale = _portal.getSiteDefaultLocale(
			user.getGroupId());

		if (Validator.isNull(body)) {
			body = _formatContent(
				notificationTemplate.getBody(siteDefaultLocale),
				siteDefaultLocale, null, notificationType, object);
		}

		String fromName = notificationTemplate.getFromName(
			user.getLanguageId());

		if (Validator.isNull(fromName)) {
			fromName = notificationTemplate.getFromName(
				_portal.getSiteDefaultLocale(user.getGroupId()));
		}

		String subject = _formatContent(
			notificationTemplate.getSubject(user.getLocale()), user.getLocale(),
			null, notificationType, object);

		if (Validator.isNull(subject)) {
			subject = _formatContent(
				notificationTemplate.getSubject(siteDefaultLocale),
				siteDefaultLocale, null, notificationType, object);
		}

		String to = _formatContent(
			notificationTemplate.getTo(user.getLocale()), user.getLocale(),
			NotificationTermContributorConstants.RECIPIENT, notificationType,
			object);

		if (Validator.isNull(to)) {
			to = _formatContent(
				notificationTemplate.getTo(siteDefaultLocale),
				siteDefaultLocale,
				NotificationTermContributorConstants.RECIPIENT,
				notificationType, object);
		}

		EmailAddressValidator emailAddressValidator =
			EmailAddressValidatorFactory.getInstance();

		for (String emailAddressOrUserId : StringUtil.split(to)) {
			User toUser = _userLocalService.fetchUser(
				GetterUtil.getLong(emailAddressOrUserId));

			if ((toUser == null) &&
				emailAddressValidator.validate(
					user.getCompanyId(), emailAddressOrUserId)) {

				toUser = _userLocalService.fetchUserByEmailAddress(
					user.getCompanyId(), emailAddressOrUserId);

				if (toUser == null) {
					if (_log.isInfoEnabled()) {
						_log.info(
							"No user exists with email address " +
								emailAddressOrUserId);
					}

					User defaultUser = _userLocalService.getDefaultUser(
						CompanyThreadLocal.getCompanyId());

					_notificationQueueEntryLocalService.
						addNotificationQueueEntry(
							defaultUser.getUserId(),
							notificationTemplate.getNotificationTemplateId(),
							notificationTemplate.getBcc(), body,
							notificationTemplate.getCc(),
							notificationType.getClassName(object),
							notificationType.getClassPK(object),
							notificationTemplate.getFrom(), fromName, 0,
							subject, emailAddressOrUserId,
							emailAddressOrUserId);

					continue;
				}
			}

			_notificationQueueEntryLocalService.addNotificationQueueEntry(
				userId, notificationTemplate.getNotificationTemplateId(),
				notificationTemplate.getBcc(), body,
				notificationTemplate.getCc(),
				notificationType.getClassName(object),
				notificationType.getClassPK(object),
				notificationTemplate.getFrom(), fromName, 0, subject,
				toUser.getEmailAddress(), notificationTemplate.getName());
		}
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public NotificationTemplate updateNotificationTemplate(
			long notificationTemplateId, String bcc,
			Map<Locale, String> bodyMap, String cc, String description,
			String from, Map<Locale, String> fromNameMap, String name,
			Map<Locale, String> subjectMap, Map<Locale, String> toMap)
		throws PortalException {

		_validate(name, from);

		NotificationTemplate notificationTemplate =
			notificationTemplatePersistence.findByPrimaryKey(
				notificationTemplateId);

		notificationTemplate.setBcc(bcc);
		notificationTemplate.setBodyMap(bodyMap);
		notificationTemplate.setCc(cc);
		notificationTemplate.setDescription(description);
		notificationTemplate.setFrom(from);
		notificationTemplate.setFromNameMap(fromNameMap);
		notificationTemplate.setName(name);
		notificationTemplate.setSubjectMap(subjectMap);
		notificationTemplate.setToMap(toMap);

		return notificationTemplatePersistence.update(notificationTemplate);
	}

	private String _formatContent(
			String content, Locale locale,
			List<NotificationTermContributor> notificationTermContributors,
			Object object, List<String> termNames)
		throws PortalException {

		for (NotificationTermContributor notificationTermContributor :
				notificationTermContributors) {

			for (String termName : termNames) {
				content = StringUtil.replace(
					content, termName,
					notificationTermContributor.getTermValue(
						locale, object, termName));
			}
		}

		return content;
	}

	private String _formatContent(
			String content, Locale locale,
			String notificationTermContributorKey,
			NotificationType notificationType, Object object)
		throws PortalException {

		if (Validator.isNull(content)) {
			return StringPool.BLANK;
		}

		List<String> termNames = new ArrayList<>();

		Matcher matcher = _pattern.matcher(content);

		while (matcher.find()) {
			termNames.add(matcher.group());
		}

		if (Validator.isNotNull(notificationTermContributorKey)) {
			content = _formatContent(
				content, locale,
				_notificationTermContributorRegistry.
					getNotificationTermContributorsByNotificationTermContributorKey(
						notificationTermContributorKey),
				object, termNames);
		}

		return _formatContent(
			content, locale,
			_notificationTermContributorRegistry.
				getNotificationTermContributorsByNotificationTypeKey(
					notificationType.getKey()),
			object, termNames);
	}

	private void _validate(String name, String from) throws PortalException {
		if (Validator.isNull(name)) {
			throw new NotificationTemplateNameException("Name is null");
		}

		if (Validator.isNull(from)) {
			throw new NotificationTemplateFromException("From is null");
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		NotificationTemplateLocalServiceImpl.class);

	private static final Pattern _pattern = Pattern.compile(
		"\\[%[^\\[%]+%\\]", Pattern.CASE_INSENSITIVE);

	@Reference
	private NotificationQueueEntryLocalService
		_notificationQueueEntryLocalService;

	@Reference
	private NotificationQueueEntryPersistence
		_notificationQueueEntryPersistence;

	@Reference
	private NotificationTermContributorRegistry
		_notificationTermContributorRegistry;

	@Reference
	private NotificationTypeRegistry _notificationTypeRegistry;

	@Reference
	private Portal _portal;

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private UserLocalService _userLocalService;

}