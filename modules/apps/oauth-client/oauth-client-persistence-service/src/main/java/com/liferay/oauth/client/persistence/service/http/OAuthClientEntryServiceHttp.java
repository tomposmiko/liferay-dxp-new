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

package com.liferay.oauth.client.persistence.service.http;

import com.liferay.oauth.client.persistence.service.OAuthClientEntryServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * Provides the HTTP utility for the
 * <code>OAuthClientEntryServiceUtil</code> service
 * utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * <code>HttpPrincipal</code> parameter.
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <b>tunnel.servlet.hosts.allowed</b> in portal.properties to
 * configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class OAuthClientEntryServiceHttp {

	public static com.liferay.oauth.client.persistence.model.OAuthClientEntry
			addOAuthClientEntry(
				HttpPrincipal httpPrincipal, long userId,
				String authServerIssuer, String infoJSON, String parametersJSON)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class, "addOAuthClientEntry",
				_addOAuthClientEntryParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, userId, authServerIssuer, infoJSON, parametersJSON);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.oauth.client.persistence.model.OAuthClientEntry)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.oauth.client.persistence.model.OAuthClientEntry
			deleteOAuthClientEntry(
				HttpPrincipal httpPrincipal, long oAuthClientEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class, "deleteOAuthClientEntry",
				_deleteOAuthClientEntryParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, oAuthClientEntryId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.oauth.client.persistence.model.OAuthClientEntry)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.oauth.client.persistence.model.OAuthClientEntry
			deleteOAuthClientEntry(
				HttpPrincipal httpPrincipal, long companyId,
				String authServerIssuer, String clientId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class, "deleteOAuthClientEntry",
				_deleteOAuthClientEntryParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, companyId, authServerIssuer, clientId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.oauth.client.persistence.model.OAuthClientEntry)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.oauth.client.persistence.model.OAuthClientEntry>
			getAuthServerIssuerOAuthClientEntries(
				HttpPrincipal httpPrincipal, long companyId,
				String authServerIssuer) {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class,
				"getAuthServerIssuerOAuthClientEntries",
				_getAuthServerIssuerOAuthClientEntriesParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, companyId, authServerIssuer);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.oauth.client.persistence.model.OAuthClientEntry>)
					returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.oauth.client.persistence.model.OAuthClientEntry>
				getAuthServerTypeOAuthClientEntries(
					HttpPrincipal httpPrincipal, long companyId,
					String authServerType)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class,
				"getAuthServerTypeOAuthClientEntries",
				_getAuthServerTypeOAuthClientEntriesParameterTypes4);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, companyId, authServerType);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.oauth.client.persistence.model.OAuthClientEntry>)
					returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.oauth.client.persistence.model.OAuthClientEntry>
			getCompanyOAuthClientEntries(
				HttpPrincipal httpPrincipal, long companyId) {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class,
				"getCompanyOAuthClientEntries",
				_getCompanyOAuthClientEntriesParameterTypes5);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, companyId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.oauth.client.persistence.model.OAuthClientEntry>)
					returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.oauth.client.persistence.model.OAuthClientEntry>
			getCompanyOAuthClientEntries(
				HttpPrincipal httpPrincipal, long companyId, int start,
				int end) {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class,
				"getCompanyOAuthClientEntries",
				_getCompanyOAuthClientEntriesParameterTypes6);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, companyId, start, end);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.oauth.client.persistence.model.OAuthClientEntry>)
					returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.oauth.client.persistence.model.OAuthClientEntry
			getOAuthClientEntry(
				HttpPrincipal httpPrincipal, long companyId,
				String authServerIssuer, String clientId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class, "getOAuthClientEntry",
				_getOAuthClientEntryParameterTypes7);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, companyId, authServerIssuer, clientId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.oauth.client.persistence.model.OAuthClientEntry)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.oauth.client.persistence.model.OAuthClientEntry>
			getUserOAuthClientEntries(
				HttpPrincipal httpPrincipal, long userId) {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class, "getUserOAuthClientEntries",
				_getUserOAuthClientEntriesParameterTypes8);

			MethodHandler methodHandler = new MethodHandler(methodKey, userId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.oauth.client.persistence.model.OAuthClientEntry>)
					returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.oauth.client.persistence.model.OAuthClientEntry>
			getUserOAuthClientEntries(
				HttpPrincipal httpPrincipal, long userId, int start, int end) {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class, "getUserOAuthClientEntries",
				_getUserOAuthClientEntriesParameterTypes9);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, userId, start, end);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.oauth.client.persistence.model.OAuthClientEntry>)
					returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.oauth.client.persistence.model.OAuthClientEntry
			updateOAuthClientEntry(
				HttpPrincipal httpPrincipal, long oAuthClientEntryId,
				String authServerIssuer, String infoJSON, String parametersJSON)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				OAuthClientEntryServiceUtil.class, "updateOAuthClientEntry",
				_updateOAuthClientEntryParameterTypes10);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, oAuthClientEntryId, authServerIssuer, infoJSON,
				parametersJSON);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.oauth.client.persistence.model.OAuthClientEntry)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		OAuthClientEntryServiceHttp.class);

	private static final Class<?>[] _addOAuthClientEntryParameterTypes0 =
		new Class[] {long.class, String.class, String.class, String.class};
	private static final Class<?>[] _deleteOAuthClientEntryParameterTypes1 =
		new Class[] {long.class};
	private static final Class<?>[] _deleteOAuthClientEntryParameterTypes2 =
		new Class[] {long.class, String.class, String.class};
	private static final Class<?>[]
		_getAuthServerIssuerOAuthClientEntriesParameterTypes3 = new Class[] {
			long.class, String.class
		};
	private static final Class<?>[]
		_getAuthServerTypeOAuthClientEntriesParameterTypes4 = new Class[] {
			long.class, String.class
		};
	private static final Class<?>[]
		_getCompanyOAuthClientEntriesParameterTypes5 = new Class[] {long.class};
	private static final Class<?>[]
		_getCompanyOAuthClientEntriesParameterTypes6 = new Class[] {
			long.class, int.class, int.class
		};
	private static final Class<?>[] _getOAuthClientEntryParameterTypes7 =
		new Class[] {long.class, String.class, String.class};
	private static final Class<?>[] _getUserOAuthClientEntriesParameterTypes8 =
		new Class[] {long.class};
	private static final Class<?>[] _getUserOAuthClientEntriesParameterTypes9 =
		new Class[] {long.class, int.class, int.class};
	private static final Class<?>[] _updateOAuthClientEntryParameterTypes10 =
		new Class[] {long.class, String.class, String.class, String.class};

}