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

package com.liferay.account.service.http;

import com.liferay.account.service.AccountEntryServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.rmi.RemoteException;

/**
 * Provides the SOAP utility for the
 * <code>AccountEntryServiceUtil</code> service
 * utility. The static methods of this class call the same methods of the
 * service utility. However, the signatures are different because it is
 * difficult for SOAP to support certain types.
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a <code>java.util.List</code>,
 * that is translated to an array of
 * <code>com.liferay.account.model.AccountEntrySoap</code>. If the method in the
 * service utility returns a
 * <code>com.liferay.account.model.AccountEntry</code>, that is translated to a
 * <code>com.liferay.account.model.AccountEntrySoap</code>. Methods that SOAP
 * cannot safely wire are skipped.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/api/axis. Set the
 * property <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AccountEntryServiceHttp
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class AccountEntryServiceSoap {

	public static void activateAccountEntries(long[] accountEntryIds)
		throws RemoteException {

		try {
			AccountEntryServiceUtil.activateAccountEntries(accountEntryIds);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static com.liferay.account.model.AccountEntrySoap
			activateAccountEntry(long accountEntryId)
		throws RemoteException {

		try {
			com.liferay.account.model.AccountEntry returnValue =
				AccountEntryServiceUtil.activateAccountEntry(accountEntryId);

			return com.liferay.account.model.AccountEntrySoap.toSoapModel(
				returnValue);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static com.liferay.account.model.AccountEntrySoap addAccountEntry(
			long userId, long parentAccountEntryId, String name,
			String description, String[] domains, String email,
			byte[] logoBytes, String taxIdNumber, String type, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {

		try {
			com.liferay.account.model.AccountEntry returnValue =
				AccountEntryServiceUtil.addAccountEntry(
					userId, parentAccountEntryId, name, description, domains,
					email, logoBytes, taxIdNumber, type, status,
					serviceContext);

			return com.liferay.account.model.AccountEntrySoap.toSoapModel(
				returnValue);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static com.liferay.account.model.AccountEntrySoap
			addOrUpdateAccountEntry(
				String externalReferenceCode, long userId,
				long parentAccountEntryId, String name, String description,
				String[] domains, String emailAddress, byte[] logoBytes,
				String taxIdNumber, String type, int status,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {

		try {
			com.liferay.account.model.AccountEntry returnValue =
				AccountEntryServiceUtil.addOrUpdateAccountEntry(
					externalReferenceCode, userId, parentAccountEntryId, name,
					description, domains, emailAddress, logoBytes, taxIdNumber,
					type, status, serviceContext);

			return com.liferay.account.model.AccountEntrySoap.toSoapModel(
				returnValue);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static void deactivateAccountEntries(long[] accountEntryIds)
		throws RemoteException {

		try {
			AccountEntryServiceUtil.deactivateAccountEntries(accountEntryIds);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static com.liferay.account.model.AccountEntrySoap
			deactivateAccountEntry(long accountEntryId)
		throws RemoteException {

		try {
			com.liferay.account.model.AccountEntry returnValue =
				AccountEntryServiceUtil.deactivateAccountEntry(accountEntryId);

			return com.liferay.account.model.AccountEntrySoap.toSoapModel(
				returnValue);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static void deleteAccountEntries(long[] accountEntryIds)
		throws RemoteException {

		try {
			AccountEntryServiceUtil.deleteAccountEntries(accountEntryIds);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static void deleteAccountEntry(long accountEntryId)
		throws RemoteException {

		try {
			AccountEntryServiceUtil.deleteAccountEntry(accountEntryId);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static com.liferay.account.model.AccountEntrySoap fetchAccountEntry(
			long accountEntryId)
		throws RemoteException {

		try {
			com.liferay.account.model.AccountEntry returnValue =
				AccountEntryServiceUtil.fetchAccountEntry(accountEntryId);

			return com.liferay.account.model.AccountEntrySoap.toSoapModel(
				returnValue);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static com.liferay.account.model.AccountEntrySoap[]
			getAccountEntries(
				long companyId, int status, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.account.model.AccountEntry> orderByComparator)
		throws RemoteException {

		try {
			java.util.List<com.liferay.account.model.AccountEntry> returnValue =
				AccountEntryServiceUtil.getAccountEntries(
					companyId, status, start, end, orderByComparator);

			return com.liferay.account.model.AccountEntrySoap.toSoapModels(
				returnValue);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static com.liferay.account.model.AccountEntrySoap getAccountEntry(
			long accountEntryId)
		throws RemoteException {

		try {
			com.liferay.account.model.AccountEntry returnValue =
				AccountEntryServiceUtil.getAccountEntry(accountEntryId);

			return com.liferay.account.model.AccountEntrySoap.toSoapModel(
				returnValue);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static com.liferay.account.model.AccountEntrySoap updateAccountEntry(
			com.liferay.account.model.AccountEntrySoap accountEntry)
		throws RemoteException {

		try {
			com.liferay.account.model.AccountEntry returnValue =
				AccountEntryServiceUtil.updateAccountEntry(
					com.liferay.account.model.impl.AccountEntryModelImpl.
						toModel(accountEntry));

			return com.liferay.account.model.AccountEntrySoap.toSoapModel(
				returnValue);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static com.liferay.account.model.AccountEntrySoap updateAccountEntry(
			long accountEntryId, long parentAccountEntryId, String name,
			String description, boolean deleteLogo, String[] domains,
			String emailAddress, byte[] logoBytes, String taxIdNumber,
			int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {

		try {
			com.liferay.account.model.AccountEntry returnValue =
				AccountEntryServiceUtil.updateAccountEntry(
					accountEntryId, parentAccountEntryId, name, description,
					deleteLogo, domains, emailAddress, logoBytes, taxIdNumber,
					status, serviceContext);

			return com.liferay.account.model.AccountEntrySoap.toSoapModel(
				returnValue);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	public static com.liferay.account.model.AccountEntrySoap
			updateExternalReferenceCode(
				long accountEntryId, String externalReferenceCode)
		throws RemoteException {

		try {
			com.liferay.account.model.AccountEntry returnValue =
				AccountEntryServiceUtil.updateExternalReferenceCode(
					accountEntryId, externalReferenceCode);

			return com.liferay.account.model.AccountEntrySoap.toSoapModel(
				returnValue);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RemoteException(exception.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		AccountEntryServiceSoap.class);

}