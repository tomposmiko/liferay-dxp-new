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

package com.liferay.portal.service.persistence.impl;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PortletConstants;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.service.persistence.PortletPreferencesFinder;
import com.liferay.portal.kernel.service.persistence.PortletPreferencesUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.impl.PortletPreferencesImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.io.Serializable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @author Hugo Huijser
 */
public class PortletPreferencesFinderImpl
	extends PortletPreferencesFinderBaseImpl
	implements PortletPreferencesFinder {

	public static final String COUNT_BY_O_O_P =
		PortletPreferencesFinder.class.getName() + ".countByO_O_P";

	public static final String COUNT_BY_O_O_P_P_P =
		PortletPreferencesFinder.class.getName() + ".countByO_O_P_P_P";

	public static final String FIND_BY_PORTLET_ID =
		PortletPreferencesFinder.class.getName() + ".findByPortletId";

	public static final String FIND_BY_C_G_O_O_P_P =
		PortletPreferencesFinder.class.getName() + ".findByC_G_O_O_P_P";

	public static final FinderPath FINDER_PATH_FIND_BY_C_G_O_O_P_P =
		new FinderPath(
			PortletPreferencesPersistenceImpl.
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByC_G_O_O_P_P",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Long.class.getName(), Integer.class.getName(),
				String.class.getName(), Boolean.class.getName()
			},
			new String[] {"ownerId", "ownerType", "portletId"}, true);

	@Override
	public long countByO_O_P(
		long ownerId, int ownerType, String portletId,
		boolean excludeDefaultPreferences) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_BY_O_O_P);

			if (ownerId == -1) {
				sql = StringUtil.removeSubstring(sql, _OWNER_ID_SQL);
			}

			if (excludeDefaultPreferences) {
				sql = StringUtil.replace(
					sql, "[$PORTLET_PREFERENCES_PREFERENCES_DEFAULT$]",
					PortletConstants.DEFAULT_PREFERENCES);
			}
			else {
				sql = StringUtil.removeSubstring(sql, _PREFERENCES_SQL);
			}

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			if (ownerId != -1) {
				queryPos.add(ownerId);
			}

			queryPos.add(ownerType);
			queryPos.add(portletId);
			queryPos.add(portletId.concat("%_INSTANCE_%"));

			int count = 0;

			Iterator<Long> iterator = sqlQuery.iterate();

			while (iterator.hasNext()) {
				Long l = iterator.next();

				if (l != null) {
					count += l.intValue();
				}
			}

			return count;
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public long countByO_O_P_P_P(
		long ownerId, int ownerType, long plid, String portletId,
		boolean excludeDefaultPreferences) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_BY_O_O_P_P_P);

			if (ownerId == -1) {
				sql = StringUtil.removeSubstring(sql, _OWNER_ID_SQL);
			}

			if (plid == -1) {
				sql = StringUtil.removeSubstring(sql, _PLID_SQL);
			}
			else {
				sql = StringUtil.removeSubstring(sql, _PORTLET_ID_INSTANCE_SQL);
			}

			if (excludeDefaultPreferences) {
				sql = StringUtil.replace(
					sql, "[$PORTLET_PREFERENCES_PREFERENCES_DEFAULT$]",
					PortletConstants.DEFAULT_PREFERENCES);
			}
			else {
				sql = StringUtil.removeSubstring(sql, _PREFERENCES_SQL);
			}

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			if (ownerId != -1) {
				queryPos.add(ownerId);
			}

			queryPos.add(ownerType);
			queryPos.add(portletId);

			if (plid != -1) {
				queryPos.add(plid);
			}
			else {
				queryPos.add(portletId.concat("%_INSTANCE_%"));
			}

			int count = 0;

			Iterator<Long> iterator = sqlQuery.iterate();

			while (iterator.hasNext()) {
				Long l = iterator.next();

				if (l != null) {
					count += l.intValue();
				}
			}

			return count;
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public Map<Serializable, PortletPreferences> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return PortletPreferencesUtil.fetchByPrimaryKeys(primaryKeys);
	}

	@Override
	public List<PortletPreferences> findByPortletId(String portletId) {
		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_PORTLET_ID);

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addEntity(
				"PortletPreferences", PortletPreferencesImpl.class);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(portletId);

			return sqlQuery.list(true);
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public List<PortletPreferences> findByC_G_O_O_P_P(
		long companyId, long groupId, long ownerId, int ownerType,
		String portletId, boolean privateLayout) {

		Object[] finderArgs = {
			companyId, groupId, ownerId, ownerType, portletId, privateLayout
		};

		List<PortletPreferences> list =
			(List<PortletPreferences>)FinderCacheUtil.getResult(
				FINDER_PATH_FIND_BY_C_G_O_O_P_P, finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (PortletPreferences portletPreferences : list) {
				if ((ownerId != portletPreferences.getOwnerId()) ||
					(ownerType != portletPreferences.getOwnerType()) ||
					!isInstanceOf(
						portletPreferences.getPortletId(), portletId)) {

					list = null;

					break;
				}
			}
		}

		if (list == null) {
			Session session = null;

			try {
				session = openSession();

				String sql = CustomSQLUtil.get(FIND_BY_C_G_O_O_P_P);

				SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

				sqlQuery.addEntity(
					"PortletPreferences", PortletPreferencesImpl.class);

				QueryPos queryPos = QueryPos.getInstance(sqlQuery);

				queryPos.add(companyId);
				queryPos.add(groupId);
				queryPos.add(ownerId);
				queryPos.add(ownerType);
				queryPos.add(portletId);
				queryPos.add(portletId.concat("_INSTANCE_%"));
				queryPos.add(privateLayout);

				list = sqlQuery.list(true);

				PortletPreferencesUtil.cacheResult(list);

				FinderCacheUtil.putResult(
					FINDER_PATH_FIND_BY_C_G_O_O_P_P, finderArgs, list);
			}
			catch (Exception exception) {
				throw new SystemException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	protected boolean isInstanceOf(
		String portletPreferencesPortletId, String portletId) {

		portletPreferencesPortletId = GetterUtil.getString(
			portletPreferencesPortletId);
		portletId = GetterUtil.getString(portletId);

		if (portletPreferencesPortletId.equals(portletId)) {
			return true;
		}

		String portletName = PortletIdCodec.decodePortletName(
			portletPreferencesPortletId);

		return Objects.equals(portletName, portletId);
	}

	private static final String _OWNER_ID_SQL =
		"(PortletPreferences.ownerId = ?) AND";

	private static final String _PLID_SQL = "AND (PortletPreferences.plid = ?)";

	private static final String _PORTLET_ID_INSTANCE_SQL =
		"OR (PortletPreferences.portletId LIKE ?)";

	private static final String _PREFERENCES_SQL =
		"AND (CAST_CLOB_TEXT(PortletPreferences.preferences) != " +
			"'[$PORTLET_PREFERENCES_PREFERENCES_DEFAULT$]')";

}