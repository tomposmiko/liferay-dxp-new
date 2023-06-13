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

package com.liferay.fragment.service.http;

import aQute.bnd.annotation.ProviderType;

import com.liferay.fragment.service.FragmentEntryServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * Provides the HTTP utility for the
 * {@link FragmentEntryServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * {@link HttpPrincipal} parameter.
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
 * @see FragmentEntryServiceSoap
 * @see HttpPrincipal
 * @see FragmentEntryServiceUtil
 * @generated
 */
@ProviderType
public class FragmentEntryServiceHttp {
	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		String name, int type, int status,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"addFragmentEntry", _addFragmentEntryParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, name, type, status, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		String name, int status,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"addFragmentEntry", _addFragmentEntryParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, name, status, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		String fragmentEntryKey, String name, int type, int status,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"addFragmentEntry", _addFragmentEntryParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, fragmentEntryKey, name, type, status,
					serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		String fragmentEntryKey, String name, int status,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"addFragmentEntry", _addFragmentEntryParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, fragmentEntryKey, name, status,
					serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		String name, String css, String html, String js, int type, int status,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"addFragmentEntry", _addFragmentEntryParameterTypes4);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, name, css, html, js, type, status,
					serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		String name, String css, String html, String js, int status,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"addFragmentEntry", _addFragmentEntryParameterTypes5);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, name, css, html, js, status,
					serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		String fragmentEntryKey, String name, String css, String html,
		String js, int type, int status,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"addFragmentEntry", _addFragmentEntryParameterTypes6);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, fragmentEntryKey, name, css, html,
					js, type, status, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		String fragmentEntryKey, String name, String css, String html,
		String js, int status,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"addFragmentEntry", _addFragmentEntryParameterTypes7);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, fragmentEntryKey, name, css, html,
					js, status, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void deleteFragmentEntries(HttpPrincipal httpPrincipal,
		long[] fragmentEntriesIds)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"deleteFragmentEntries",
					_deleteFragmentEntriesParameterTypes8);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					fragmentEntriesIds);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry deleteFragmentEntry(
		HttpPrincipal httpPrincipal, long fragmentEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"deleteFragmentEntry", _deleteFragmentEntryParameterTypes9);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					fragmentEntryId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry fetchFragmentEntry(
		HttpPrincipal httpPrincipal, long fragmentEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"fetchFragmentEntry", _fetchFragmentEntryParameterTypes10);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					fragmentEntryId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static int getFragmentCollectionsCount(HttpPrincipal httpPrincipal,
		long groupId, long fragmentCollectionId) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentCollectionsCount",
					_getFragmentCollectionsCountParameterTypes11);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static int getFragmentCollectionsCount(HttpPrincipal httpPrincipal,
		long groupId, long fragmentCollectionId, int status) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentCollectionsCount",
					_getFragmentCollectionsCountParameterTypes12);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, status);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static int getFragmentCollectionsCount(HttpPrincipal httpPrincipal,
		long groupId, long fragmentCollectionId, String name) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentCollectionsCount",
					_getFragmentCollectionsCountParameterTypes13);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, name);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static int getFragmentCollectionsCount(HttpPrincipal httpPrincipal,
		long groupId, long fragmentCollectionId, String name, int status) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentCollectionsCount",
					_getFragmentCollectionsCountParameterTypes14);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, name, status);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static int getFragmentCollectionsCountByType(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		int type) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentCollectionsCountByType",
					_getFragmentCollectionsCountByTypeParameterTypes15);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, type);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry> getFragmentEntries(
		HttpPrincipal httpPrincipal, long fragmentCollectionId) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentEntries", _getFragmentEntriesParameterTypes16);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					fragmentCollectionId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.fragment.model.FragmentEntry>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry> getFragmentEntries(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		int status) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentEntries", _getFragmentEntriesParameterTypes17);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, status);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.fragment.model.FragmentEntry>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry> getFragmentEntries(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		int start, int end) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentEntries", _getFragmentEntriesParameterTypes18);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, start, end);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.fragment.model.FragmentEntry>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry> getFragmentEntries(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.fragment.model.FragmentEntry> orderByComparator) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentEntries", _getFragmentEntriesParameterTypes19);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, status, start, end, orderByComparator);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.fragment.model.FragmentEntry>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry> getFragmentEntries(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.fragment.model.FragmentEntry> orderByComparator) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentEntries", _getFragmentEntriesParameterTypes20);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, start, end, orderByComparator);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.fragment.model.FragmentEntry>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry> getFragmentEntries(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		String name, int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.fragment.model.FragmentEntry> orderByComparator) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentEntries", _getFragmentEntriesParameterTypes21);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, name, status, start, end,
					orderByComparator);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.fragment.model.FragmentEntry>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry> getFragmentEntries(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		String name, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.fragment.model.FragmentEntry> orderByComparator) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentEntries", _getFragmentEntriesParameterTypes22);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, name, start, end, orderByComparator);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.fragment.model.FragmentEntry>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry> getFragmentEntriesByType(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		int type, int status) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentEntriesByType",
					_getFragmentEntriesByTypeParameterTypes23);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, type, status);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.fragment.model.FragmentEntry>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry> getFragmentEntriesByType(
		HttpPrincipal httpPrincipal, long groupId, long fragmentCollectionId,
		int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.fragment.model.FragmentEntry> orderByComparator) {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getFragmentEntriesByType",
					_getFragmentEntriesByTypeParameterTypes24);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					fragmentCollectionId, type, start, end, orderByComparator);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.fragment.model.FragmentEntry>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static String[] getTempFileNames(HttpPrincipal httpPrincipal,
		long groupId, String folderName)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"getTempFileNames", _getTempFileNamesParameterTypes25);

			MethodHandler methodHandler = new MethodHandler(methodKey, groupId,
					folderName);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (String[])returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry moveFragmentEntry(
		HttpPrincipal httpPrincipal, long fragmentEntryId,
		long fragmentCollectionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"moveFragmentEntry", _moveFragmentEntryParameterTypes26);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					fragmentEntryId, fragmentCollectionId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry updateFragmentEntry(
		HttpPrincipal httpPrincipal, long fragmentEntryId,
		long previewFileEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"updateFragmentEntry", _updateFragmentEntryParameterTypes27);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					fragmentEntryId, previewFileEntryId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry updateFragmentEntry(
		HttpPrincipal httpPrincipal, long fragmentEntryId, String name)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"updateFragmentEntry", _updateFragmentEntryParameterTypes28);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					fragmentEntryId, name);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry updateFragmentEntry(
		HttpPrincipal httpPrincipal, long fragmentEntryId, String name,
		String css, String html, String js, int status)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"updateFragmentEntry", _updateFragmentEntryParameterTypes29);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					fragmentEntryId, name, css, html, js, status);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.fragment.model.FragmentEntry updateFragmentEntry(
		HttpPrincipal httpPrincipal, long fragmentEntryId, String name,
		String css, String html, String js, long previewFileEntryId, int status)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(FragmentEntryServiceUtil.class,
					"updateFragmentEntry", _updateFragmentEntryParameterTypes30);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					fragmentEntryId, name, css, html, js, previewFileEntryId,
					status);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.fragment.model.FragmentEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(FragmentEntryServiceHttp.class);
	private static final Class<?>[] _addFragmentEntryParameterTypes0 = new Class[] {
			long.class, long.class, String.class, int.class, int.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _addFragmentEntryParameterTypes1 = new Class[] {
			long.class, long.class, String.class, int.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _addFragmentEntryParameterTypes2 = new Class[] {
			long.class, long.class, String.class, String.class, int.class,
			int.class, com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _addFragmentEntryParameterTypes3 = new Class[] {
			long.class, long.class, String.class, String.class, int.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _addFragmentEntryParameterTypes4 = new Class[] {
			long.class, long.class, String.class, String.class, String.class,
			String.class, int.class, int.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _addFragmentEntryParameterTypes5 = new Class[] {
			long.class, long.class, String.class, String.class, String.class,
			String.class, int.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _addFragmentEntryParameterTypes6 = new Class[] {
			long.class, long.class, String.class, String.class, String.class,
			String.class, String.class, int.class, int.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _addFragmentEntryParameterTypes7 = new Class[] {
			long.class, long.class, String.class, String.class, String.class,
			String.class, String.class, int.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _deleteFragmentEntriesParameterTypes8 = new Class[] {
			long[].class
		};
	private static final Class<?>[] _deleteFragmentEntryParameterTypes9 = new Class[] {
			long.class
		};
	private static final Class<?>[] _fetchFragmentEntryParameterTypes10 = new Class[] {
			long.class
		};
	private static final Class<?>[] _getFragmentCollectionsCountParameterTypes11 =
		new Class[] { long.class, long.class };
	private static final Class<?>[] _getFragmentCollectionsCountParameterTypes12 =
		new Class[] { long.class, long.class, int.class };
	private static final Class<?>[] _getFragmentCollectionsCountParameterTypes13 =
		new Class[] { long.class, long.class, String.class };
	private static final Class<?>[] _getFragmentCollectionsCountParameterTypes14 =
		new Class[] { long.class, long.class, String.class, int.class };
	private static final Class<?>[] _getFragmentCollectionsCountByTypeParameterTypes15 =
		new Class[] { long.class, long.class, int.class };
	private static final Class<?>[] _getFragmentEntriesParameterTypes16 = new Class[] {
			long.class
		};
	private static final Class<?>[] _getFragmentEntriesParameterTypes17 = new Class[] {
			long.class, long.class, int.class
		};
	private static final Class<?>[] _getFragmentEntriesParameterTypes18 = new Class[] {
			long.class, long.class, int.class, int.class
		};
	private static final Class<?>[] _getFragmentEntriesParameterTypes19 = new Class[] {
			long.class, long.class, int.class, int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[] _getFragmentEntriesParameterTypes20 = new Class[] {
			long.class, long.class, int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[] _getFragmentEntriesParameterTypes21 = new Class[] {
			long.class, long.class, String.class, int.class, int.class,
			int.class, com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[] _getFragmentEntriesParameterTypes22 = new Class[] {
			long.class, long.class, String.class, int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[] _getFragmentEntriesByTypeParameterTypes23 = new Class[] {
			long.class, long.class, int.class, int.class
		};
	private static final Class<?>[] _getFragmentEntriesByTypeParameterTypes24 = new Class[] {
			long.class, long.class, int.class, int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[] _getTempFileNamesParameterTypes25 = new Class[] {
			long.class, String.class
		};
	private static final Class<?>[] _moveFragmentEntryParameterTypes26 = new Class[] {
			long.class, long.class
		};
	private static final Class<?>[] _updateFragmentEntryParameterTypes27 = new Class[] {
			long.class, long.class
		};
	private static final Class<?>[] _updateFragmentEntryParameterTypes28 = new Class[] {
			long.class, String.class
		};
	private static final Class<?>[] _updateFragmentEntryParameterTypes29 = new Class[] {
			long.class, String.class, String.class, String.class, String.class,
			int.class
		};
	private static final Class<?>[] _updateFragmentEntryParameterTypes30 = new Class[] {
			long.class, String.class, String.class, String.class, String.class,
			long.class, int.class
		};
}