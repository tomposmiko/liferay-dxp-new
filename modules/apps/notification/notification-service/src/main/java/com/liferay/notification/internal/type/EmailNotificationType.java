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

package com.liferay.notification.internal.type;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.info.field.InfoField;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.notification.constants.NotificationConstants;
import com.liferay.notification.constants.NotificationPortletKeys;
import com.liferay.notification.constants.NotificationQueueEntryConstants;
import com.liferay.notification.constants.NotificationTemplateConstants;
import com.liferay.notification.context.NotificationContext;
import com.liferay.notification.exception.NotificationRecipientSettingValueException;
import com.liferay.notification.exception.NotificationTemplateFromException;
import com.liferay.notification.model.NotificationQueueEntry;
import com.liferay.notification.model.NotificationQueueEntryAttachment;
import com.liferay.notification.model.NotificationRecipient;
import com.liferay.notification.model.NotificationRecipientSetting;
import com.liferay.notification.model.NotificationTemplate;
import com.liferay.notification.model.NotificationTemplateAttachment;
import com.liferay.notification.service.NotificationQueueEntryAttachmentLocalService;
import com.liferay.notification.service.NotificationTemplateAttachmentLocalService;
import com.liferay.notification.type.BaseNotificationType;
import com.liferay.notification.type.NotificationType;
import com.liferay.notification.util.NotificationRecipientSettingUtil;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Repository;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.EmailAddressValidator;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.templateparser.TemplateNode;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.EmailAddressValidatorFactory;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.display.template.PortletDisplayTemplate;
import com.liferay.template.transformer.TemplateNodeFactory;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.InternetAddress;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Feliphe Marinho
 */
@Component(service = NotificationType.class)
public class EmailNotificationType extends BaseNotificationType {

	@Override
	public String getFromName(NotificationQueueEntry notificationQueueEntry) {
		NotificationRecipient notificationRecipient =
			notificationQueueEntry.getNotificationRecipient();

		Map<String, Object> notificationRecipientSettingsMap =
			NotificationRecipientSettingUtil.toMap(
				notificationRecipient.getNotificationRecipientSettings());

		return String.valueOf(notificationRecipientSettingsMap.get("fromName"));
	}

	@Override
	public String getRecipientSummary(
		NotificationQueueEntry notificationQueueEntry) {

		NotificationRecipient notificationRecipient =
			notificationQueueEntry.getNotificationRecipient();

		Map<String, Object> notificationRecipientSettingsMap =
			NotificationRecipientSettingUtil.toMap(
				notificationRecipient.getNotificationRecipientSettings());

		return String.valueOf(notificationRecipientSettingsMap.get("to"));
	}

	@Override
	public String getType() {
		return NotificationConstants.TYPE_EMAIL;
	}

	@Override
	public String getTypeLanguageKey() {
		return "email";
	}

	@Override
	public void resendNotification(
			NotificationQueueEntry notificationQueueEntry)
		throws PortalException {

		if (notificationQueueEntry.getStatus() !=
				NotificationQueueEntryConstants.STATUS_UNSENT) {

			notificationQueueEntry =
				notificationQueueEntryLocalService.updateStatus(
					notificationQueueEntry.getNotificationQueueEntryId(),
					NotificationQueueEntryConstants.STATUS_UNSENT);
		}

		_sendEmail(notificationQueueEntry);
	}

	@Override
	public void resendNotifications(int status, String type)
		throws PortalException {

		for (NotificationQueueEntry notificationQueueEntry :
				notificationQueueEntryLocalService.getNotificationEntries(
					type, status)) {

			resendNotification(notificationQueueEntry);
		}
	}

	@Override
	public void sendNotification(NotificationContext notificationContext)
		throws PortalException {

		long groupId = 0;

		User user = userLocalService.getUser(notificationContext.getUserId());

		Group userGroup = user.getGroup();

		if (userGroup != null) {
			groupId = userGroup.getGroupId();
		}

		siteDefaultLocale = portal.getSiteDefaultLocale(groupId);

		userLocale = user.getLocale();

		notificationContext.setFileEntryIds(
			_getFileEntryIds(user.getCompanyId(), notificationContext));

		NotificationTemplate notificationTemplate =
			notificationContext.getNotificationTemplate();

		String body = _formatBody(
			notificationTemplate.getBodyMap(), notificationContext);
		NotificationRecipient notificationRecipient =
			notificationTemplate.getNotificationRecipient();
		String subject = formatLocalizedContent(
			notificationTemplate.getSubjectMap(), notificationContext);

		Map<String, String> evaluatedNotificationRecipientSettings =
			HashMapBuilder.put(
				"bcc",
				formatContent(
					"bcc", notificationContext,
					notificationRecipient.getNotificationRecipientId())
			).put(
				"cc",
				formatContent(
					"cc", notificationContext,
					notificationRecipient.getNotificationRecipientId())
			).put(
				"from",
				formatContent(
					"from", notificationContext,
					notificationRecipient.getNotificationRecipientId())
			).put(
				"fromName",
				() -> {
					NotificationRecipientSetting notificationRecipientSetting =
						notificationRecipientSettingLocalService.
							getNotificationRecipientSetting(
								notificationRecipient.
									getNotificationRecipientId(),
								"fromName");

					return formatLocalizedContent(
						notificationRecipientSetting.getValueMap(),
						notificationContext);
				}
			).put(
				"to",
				() -> {
					NotificationRecipientSetting notificationRecipientSetting =
						notificationRecipientSettingLocalService.
							getNotificationRecipientSetting(
								notificationRecipient.
									getNotificationRecipientId(),
								"to");

					String to = _formatTo(
						notificationRecipientSetting.getValue(user.getLocale()),
						notificationContext);

					if (Validator.isNotNull(to)) {
						return to;
					}

					return formatLocalizedContent(
						notificationRecipientSetting.getValue(
							siteDefaultLocale),
						notificationContext);
				}
			).build();

		for (String emailAddress :
				StringUtil.split(
					evaluatedNotificationRecipientSettings.get("to"))) {

			EmailAddressValidator emailAddressValidator =
				EmailAddressValidatorFactory.getInstance();

			if (!emailAddressValidator.validate(
					user.getCompanyId(), emailAddress)) {

				if (_log.isInfoEnabled()) {
					_log.info("Invalid email address " + emailAddress);
				}

				continue;
			}

			User creatorUser = user;

			User toUser = userLocalService.fetchUserByEmailAddress(
				user.getCompanyId(), emailAddress);

			if (toUser == null) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"No user exists with email address " + emailAddress);
				}

				creatorUser = userLocalService.getGuestUser(
					CompanyThreadLocal.getCompanyId());
			}

			prepareNotificationContext(
				creatorUser, body, notificationContext,
				HashMapBuilder.putAll(
					evaluatedNotificationRecipientSettings
				).put(
					"to", emailAddress
				).build(),
				subject);

			_sendEmail(
				notificationQueueEntryLocalService.addNotificationQueueEntry(
					notificationContext));
		}
	}

	@Override
	public Object[] toRecipients(
		List<NotificationRecipientSetting> notificationRecipientSettings) {

		return new Object[] {
			NotificationRecipientSettingUtil.toMap(
				notificationRecipientSettings)
		};
	}

	@Override
	public void validateNotificationTemplate(
			NotificationContext notificationContext)
		throws PortalException {

		super.validateNotificationTemplate(notificationContext);

		Map<String, Object> notificationRecipientSettingsMap =
			NotificationRecipientSettingUtil.toMap(
				notificationContext.getNotificationRecipientSettings());

		if (Validator.isNull(notificationRecipientSettingsMap.get("from"))) {
			throw new NotificationTemplateFromException("From is null");
		}

		if (Validator.isNull(
				notificationRecipientSettingsMap.get("fromName"))) {

			throw new NotificationRecipientSettingValueException(
				"From name is null");
		}

		if (Validator.isNull(notificationRecipientSettingsMap.get("to"))) {
			throw new NotificationRecipientSettingValueException("To is null");
		}
	}

	private void _addFileAttachments(
		MailMessage mailMessage, long notificationQueueEntryId) {

		for (NotificationQueueEntryAttachment notificationQueueEntryAttachment :
				_notificationQueueEntryAttachmentLocalService.
					getNotificationQueueEntryNotificationQueueEntryAttachments(
						notificationQueueEntryId)) {

			try {
				FileEntry fileEntry =
					_portletFileRepository.getPortletFileEntry(
						notificationQueueEntryAttachment.getFileEntryId());

				mailMessage.addFileAttachment(
					FileUtil.createTempFile(fileEntry.getContentStream()),
					fileEntry.getFileName());
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}
			}
		}
	}

	private String _formatBody(
			Map<Locale, String> bodyMap,
			NotificationContext notificationContext)
		throws PortalException {

		NotificationTemplate notificationTemplate =
			notificationContext.getNotificationTemplate();

		if (Objects.equals(
				NotificationTemplateConstants.EDITOR_TYPE_RICH_TEXT,
				notificationTemplate.getEditorType())) {

			return formatLocalizedContent(bodyMap, notificationContext);
		}

		StringWriter stringWriter = new StringWriter();

		String body = notificationTemplate.getBody(userLocale);

		if (Validator.isNull(body)) {
			body = notificationTemplate.getBody(siteDefaultLocale);
		}

		Template template = TemplateManagerUtil.getTemplate(
			TemplateConstants.LANG_TYPE_FTL,
			new StringTemplateResource(
				NotificationTemplate.class.getName() + StringPool.POUND +
					notificationTemplate.getNotificationTemplateId(),
				body),
			PropsValues.NOTIFICATION_EMAIL_TEMPLATE_RESTRICTED);

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setLocale(siteDefaultLocale);

		InfoItemFieldValuesProvider<Object> infoItemFieldValuesProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				InfoItemFieldValuesProvider.class,
				notificationContext.getClassName());
		PersistedModelLocalService persistedModelLocalService =
			_persistedModelLocalServiceRegistry.getPersistedModelLocalService(
				notificationContext.getClassName());

		InfoItemFieldValues infoItemFieldValues =
			infoItemFieldValuesProvider.getInfoItemFieldValues(
				persistedModelLocalService.getPersistedModel(
					notificationContext.getClassPK()));

		for (InfoFieldValue<Object> infoFieldValue :
				infoItemFieldValues.getInfoFieldValues()) {

			InfoField<?> infoField = infoFieldValue.getInfoField();

			if (StringUtil.startsWith(
					infoField.getName(),
					PortletDisplayTemplate.DISPLAY_STYLE_PREFIX)) {

				continue;
			}

			TemplateNode templateNode = _templateNodeFactory.createTemplateNode(
				infoFieldValue, themeDisplay);

			template.put(infoField.getName(), templateNode);
			template.put(infoField.getUniqueId(), templateNode);
		}

		template.processTemplate(stringWriter);

		return stringWriter.toString();
	}

	private String _formatTo(String to, NotificationContext notificationContext)
		throws PortalException {

		if (Validator.isNull(to)) {
			return StringPool.BLANK;
		}

		Set<String> emailAddresses = new HashSet<>();

		Matcher matcher = _emailAddressPattern.matcher(to);

		while (matcher.find()) {
			emailAddresses.add(matcher.group());
		}

		return formatLocalizedContent(
			StringUtil.merge(emailAddresses), notificationContext);
	}

	private List<Long> _getFileEntryIds(
			long companyId, NotificationContext notificationContext)
		throws PortalException {

		Group group = _groupLocalService.getCompanyGroup(companyId);

		Repository repository = _getRepository(group.getGroupId());

		if (repository == null) {
			return new ArrayList<>();
		}

		List<Long> fileEntryIds = new ArrayList<>();

		NotificationTemplate notificationTemplate =
			notificationContext.getNotificationTemplate();

		for (NotificationTemplateAttachment notificationTemplateAttachment :
				_notificationTemplateAttachmentLocalService.
					getNotificationTemplateAttachments(
						notificationTemplate.getNotificationTemplateId())) {

			ObjectField objectField = _objectFieldLocalService.fetchObjectField(
				notificationTemplateAttachment.getObjectFieldId());

			DLFileEntry dlFileEntry = _dlFileEntryLocalService.fetchDLFileEntry(
				MapUtil.getLong(
					notificationContext.getTermValues(),
					objectField.getName()));

			if (dlFileEntry == null) {
				continue;
			}

			FileEntry fileEntry = _portletFileRepository.addPortletFileEntry(
				null, repository.getGroupId(),
				userLocalService.getGuestUserId(companyId),
				NotificationTemplate.class.getName(), 0,
				NotificationPortletKeys.NOTIFICATION_TEMPLATES,
				repository.getDlFolderId(), dlFileEntry.getContentStream(),
				_portletFileRepository.getUniqueFileName(
					group.getGroupId(), repository.getDlFolderId(),
					dlFileEntry.getFileName()),
				dlFileEntry.getMimeType(), false);

			fileEntryIds.add(fileEntry.getFileEntryId());
		}

		return fileEntryIds;
	}

	private Repository _getRepository(long groupId) {
		Repository repository = _portletFileRepository.fetchPortletRepository(
			groupId, NotificationPortletKeys.NOTIFICATION_TEMPLATES);

		if (repository != null) {
			return repository;
		}

		try {
			return _portletFileRepository.addPortletRepository(
				groupId, NotificationPortletKeys.NOTIFICATION_TEMPLATES,
				new ServiceContext());
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return null;
		}
	}

	private void _sendEmail(NotificationQueueEntry notificationQueueEntry) {
		TransactionCommitCallbackUtil.registerCallback(
			() -> {
				try {
					NotificationRecipient notificationRecipient =
						notificationQueueEntry.getNotificationRecipient();

					Map<String, Object> notificationRecipientSettingsMap =
						NotificationRecipientSettingUtil.toMap(
							notificationRecipient.
								getNotificationRecipientSettings());

					MailMessage mailMessage = new MailMessage(
						new InternetAddress(
							String.valueOf(
								notificationRecipientSettingsMap.get("from")),
							String.valueOf(
								notificationRecipientSettingsMap.get(
									"fromName"))),
						new InternetAddress(
							String.valueOf(
								notificationRecipientSettingsMap.get("to"))),
						notificationQueueEntry.getSubject(),
						notificationQueueEntry.getBody(), true);

					_addFileAttachments(
						mailMessage,
						notificationQueueEntry.getNotificationQueueEntryId());

					mailMessage.setBCC(
						_toInternetAddresses(
							String.valueOf(
								notificationRecipientSettingsMap.get("bcc"))));
					mailMessage.setCC(
						_toInternetAddresses(
							String.valueOf(
								notificationRecipientSettingsMap.get("cc"))));

					MessageBusUtil.sendMessage(
						DestinationNames.MAIL, mailMessage);

					notificationQueueEntryLocalService.updateStatus(
						notificationQueueEntry.getNotificationQueueEntryId(),
						NotificationQueueEntryConstants.STATUS_SENT);
				}
				catch (Exception exception) {
					if (_log.isDebugEnabled()) {
						_log.debug(exception);
					}

					if (notificationQueueEntry.getStatus() !=
							NotificationQueueEntryConstants.STATUS_FAILED) {

						notificationQueueEntryLocalService.updateStatus(
							notificationQueueEntry.
								getNotificationQueueEntryId(),
							NotificationQueueEntryConstants.STATUS_FAILED);
					}
				}

				return null;
			});
	}

	private InternetAddress[] _toInternetAddresses(String string)
		throws Exception {

		List<InternetAddress> internetAddresses = new ArrayList<>();

		for (String internetAddressString : StringUtil.split(string)) {
			internetAddresses.add(new InternetAddress(internetAddressString));
		}

		return internetAddresses.toArray(new InternetAddress[0]);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EmailNotificationType.class);

	private static final Pattern _emailAddressPattern = Pattern.compile(
		"[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@" +
			"(?:\\w(?:[\\w-]*\\w)?\\.)+(\\w(?:[\\w-]*\\w))");

	@Reference
	private DLFileEntryLocalService _dlFileEntryLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private InfoItemServiceRegistry _infoItemServiceRegistry;

	@Reference
	private MailService _mailService;

	@Reference
	private NotificationQueueEntryAttachmentLocalService
		_notificationQueueEntryAttachmentLocalService;

	@Reference
	private NotificationTemplateAttachmentLocalService
		_notificationTemplateAttachmentLocalService;

	@Reference
	private ObjectFieldLocalService _objectFieldLocalService;

	@Reference
	private PersistedModelLocalServiceRegistry
		_persistedModelLocalServiceRegistry;

	@Reference
	private PortletFileRepository _portletFileRepository;

	@Reference
	private TemplateNodeFactory _templateNodeFactory;

}