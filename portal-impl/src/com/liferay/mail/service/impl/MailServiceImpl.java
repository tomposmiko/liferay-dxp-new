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

package com.liferay.mail.service.impl;

import com.liferay.mail.kernel.model.Account;
import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.cluster.Clusterable;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import javax.portlet.PortletPreferences;

/**
 * @author Brian Wing Shun Chan
 */
@CTAware
public class MailServiceImpl implements IdentifiableOSGiService, MailService {

	@Clusterable
	@Override
	public void clearSession() {
		clearSession(CompanyConstants.SYSTEM);
	}

	@Clusterable
	@Override
	public void clearSession(long companyId) {
		if (companyId == CompanyConstants.SYSTEM) {
			_sessions.clear();
		}

		_sessions.remove(companyId);
	}

	@Override
	public String getOSGiServiceIdentifier() {
		return MailService.class.getName();
	}

	@Override
	public Session getSession() {
		return getSession(CompanyThreadLocal.getCompanyId());
	}

	@Override
	public Session getSession(Account account) {
		Session session = Session.getInstance(_getProperties(account));

		if (_log.isDebugEnabled()) {
			session.setDebug(true);

			Properties sessionProperties = session.getProperties();

			sessionProperties.list(System.out);
		}

		return session;
	}

	@Override
	public Session getSession(long companyId) {
		Session session = _sessions.get(companyId);

		if (session != null) {
			return session;
		}

		session = InfrastructureUtil.getMailSession();

		PortletPreferences companyPortletPreferences =
			PrefsPropsUtil.getPreferences(companyId);
		PortletPreferences systemPortletPreferences =
			PrefsPropsUtil.getPreferences();

		Function<String, String> function =
			(String key) -> companyPortletPreferences.getValue(
				key,
				systemPortletPreferences.getValue(key, PropsUtil.get(key)));

		if (!GetterUtil.getBoolean(
				function.apply(PropsKeys.MAIL_SESSION_MAIL))) {

			_sessions.put(companyId, session);

			return session;
		}

		String advancedPropertiesString = function.apply(
			PropsKeys.MAIL_SESSION_MAIL_ADVANCED_PROPERTIES);
		String pop3Host = function.apply(PropsKeys.MAIL_SESSION_MAIL_POP3_HOST);
		String pop3Password = function.apply(
			PropsKeys.MAIL_SESSION_MAIL_POP3_PASSWORD);
		int pop3Port = GetterUtil.getInteger(
			function.apply(PropsKeys.MAIL_SESSION_MAIL_POP3_PORT));
		String pop3User = function.apply(PropsKeys.MAIL_SESSION_MAIL_POP3_USER);
		String smtpHost = function.apply(PropsKeys.MAIL_SESSION_MAIL_SMTP_HOST);
		String smtpPassword = function.apply(
			PropsKeys.MAIL_SESSION_MAIL_SMTP_PASSWORD);
		int smtpPort = GetterUtil.getInteger(
			function.apply(PropsKeys.MAIL_SESSION_MAIL_SMTP_PORT));
		boolean smtpStartTLSEnable = GetterUtil.getBoolean(
			function.apply(PropsKeys.MAIL_SESSION_MAIL_SMTP_STARTTLS_ENABLE));
		String smtpUser = function.apply(PropsKeys.MAIL_SESSION_MAIL_SMTP_USER);
		String storeProtocol = function.apply(
			PropsKeys.MAIL_SESSION_MAIL_STORE_PROTOCOL);
		String transportProtocol = function.apply(
			PropsKeys.MAIL_SESSION_MAIL_TRANSPORT_PROTOCOL);

		Properties properties = session.getProperties();

		// Incoming

		if (!storeProtocol.equals(Account.PROTOCOL_POPS)) {
			storeProtocol = Account.PROTOCOL_POP;
		}

		properties.setProperty("mail.store.protocol", storeProtocol);

		String storePrefix = "mail." + storeProtocol + ".";

		properties.setProperty(storePrefix + "host", pop3Host);
		properties.setProperty(storePrefix + "password", pop3Password);
		properties.setProperty(storePrefix + "port", String.valueOf(pop3Port));
		properties.setProperty(storePrefix + "user", pop3User);

		// Outgoing

		if (!transportProtocol.equals(Account.PROTOCOL_SMTPS)) {
			transportProtocol = Account.PROTOCOL_SMTP;
		}

		properties.setProperty("mail.transport.protocol", transportProtocol);

		String transportPrefix = "mail." + transportProtocol + ".";

		boolean smtpAuth = false;

		if (Validator.isNotNull(smtpPassword) ||
			Validator.isNotNull(smtpUser)) {

			smtpAuth = true;
		}

		properties.setProperty(
			transportPrefix + "auth", String.valueOf(smtpAuth));
		properties.setProperty(transportPrefix + "host", smtpHost);
		properties.setProperty(transportPrefix + "password", smtpPassword);
		properties.setProperty(
			transportPrefix + "port", String.valueOf(smtpPort));
		properties.setProperty(
			transportPrefix + "starttls.enable",
			String.valueOf(smtpStartTLSEnable));
		properties.setProperty(transportPrefix + "user", smtpUser);

		// Advanced

		try {
			if (Validator.isNotNull(advancedPropertiesString)) {
				Properties advancedProperties = PropertiesUtil.load(
					advancedPropertiesString);

				for (Map.Entry<Object, Object> entry :
						advancedProperties.entrySet()) {

					String key = (String)entry.getKey();
					String value = (String)entry.getValue();

					properties.setProperty(key, value);
				}
			}
		}
		catch (IOException ioException) {
			if (_log.isWarnEnabled()) {
				_log.warn(ioException);
			}
		}

		if (smtpAuth) {
			session = Session.getInstance(
				properties,
				new Authenticator() {

					protected PasswordAuthentication
						getPasswordAuthentication() {

						return new PasswordAuthentication(
							smtpUser, smtpPassword);
					}

				});
		}
		else {
			session = Session.getInstance(properties);
		}

		if (_log.isDebugEnabled()) {
			session.setDebug(true);

			properties.list(System.out);
		}

		_sessions.put(companyId, session);

		return session;
	}

	@Override
	public void sendEmail(MailMessage mailMessage) {
		TransactionCommitCallbackUtil.registerCallback(
			() -> {
				if (_log.isDebugEnabled()) {
					_log.debug("sendEmail");
				}

				MessageBusUtil.sendMessage(DestinationNames.MAIL, mailMessage);

				return null;
			});
	}

	private Properties _getProperties(Account account) {
		Properties properties = new Properties();

		String protocol = account.getProtocol();

		properties.setProperty("mail.transport.protocol", protocol);
		properties.setProperty("mail." + protocol + ".host", account.getHost());
		properties.setProperty(
			"mail." + protocol + ".port", String.valueOf(account.getPort()));

		if (account.isRequiresAuthentication()) {
			properties.setProperty("mail." + protocol + ".auth", "true");
			properties.setProperty(
				"mail." + protocol + ".user", account.getUser());
			properties.setProperty(
				"mail." + protocol + ".password", account.getPassword());
		}

		if (account.isSecure()) {
			properties.setProperty(
				"mail." + protocol + ".socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
			properties.setProperty(
				"mail." + protocol + ".socketFactory.fallback", "false");
			properties.setProperty(
				"mail." + protocol + ".socketFactory.port",
				String.valueOf(account.getPort()));
		}

		return properties;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MailServiceImpl.class);

	private final Map<Long, Session> _sessions = new ConcurrentHashMap<>();

}