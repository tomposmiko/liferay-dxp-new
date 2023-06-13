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

package com.liferay.portlet.asset.service.persistence.impl;

import com.liferay.asset.kernel.exception.NoSuchLinkException;
import com.liferay.asset.kernel.model.AssetLink;
import com.liferay.asset.kernel.model.AssetLinkTable;
import com.liferay.asset.kernel.service.persistence.AssetLinkPersistence;
import com.liferay.asset.kernel.service.persistence.AssetLinkUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.change.tracking.CTColumnResolutionType;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.change.tracking.helper.CTPersistenceHelperUtil;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portlet.asset.model.impl.AssetLinkImpl;
import com.liferay.portlet.asset.model.impl.AssetLinkModelImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The persistence implementation for the asset link service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class AssetLinkPersistenceImpl
	extends BasePersistenceImpl<AssetLink> implements AssetLinkPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>AssetLinkUtil</code> to access the asset link persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		AssetLinkImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByE1;
	private FinderPath _finderPathWithoutPaginationFindByE1;
	private FinderPath _finderPathCountByE1;

	/**
	 * Returns all the asset links where entryId1 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @return the matching asset links
	 */
	@Override
	public List<AssetLink> findByE1(long entryId1) {
		return findByE1(entryId1, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset links where entryId1 = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId1 the entry id1
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @return the range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE1(long entryId1, int start, int end) {
		return findByE1(entryId1, start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset links where entryId1 = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId1 the entry id1
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE1(
		long entryId1, int start, int end,
		OrderByComparator<AssetLink> orderByComparator) {

		return findByE1(entryId1, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the asset links where entryId1 = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId1 the entry id1
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE1(
		long entryId1, int start, int end,
		OrderByComparator<AssetLink> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByE1;
				finderArgs = new Object[] {entryId1};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByE1;
			finderArgs = new Object[] {entryId1, start, end, orderByComparator};
		}

		List<AssetLink> list = null;

		if (useFinderCache && productionMode) {
			list = (List<AssetLink>)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AssetLink assetLink : list) {
					if (entryId1 != assetLink.getEntryId1()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E1_ENTRYID1_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AssetLinkModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId1);

				list = (List<AssetLink>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first asset link in the ordered set where entryId1 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE1_First(
			long entryId1, OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE1_First(entryId1, orderByComparator);

		if (assetLink != null) {
			return assetLink;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("entryId1=");
		sb.append(entryId1);

		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

	/**
	 * Returns the first asset link in the ordered set where entryId1 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE1_First(
		long entryId1, OrderByComparator<AssetLink> orderByComparator) {

		List<AssetLink> list = findByE1(entryId1, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last asset link in the ordered set where entryId1 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE1_Last(
			long entryId1, OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE1_Last(entryId1, orderByComparator);

		if (assetLink != null) {
			return assetLink;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("entryId1=");
		sb.append(entryId1);

		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

	/**
	 * Returns the last asset link in the ordered set where entryId1 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE1_Last(
		long entryId1, OrderByComparator<AssetLink> orderByComparator) {

		int count = countByE1(entryId1);

		if (count == 0) {
			return null;
		}

		List<AssetLink> list = findByE1(
			entryId1, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the asset links before and after the current asset link in the ordered set where entryId1 = &#63;.
	 *
	 * @param linkId the primary key of the current asset link
	 * @param entryId1 the entry id1
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next asset link
	 * @throws NoSuchLinkException if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink[] findByE1_PrevAndNext(
			long linkId, long entryId1,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = findByPrimaryKey(linkId);

		Session session = null;

		try {
			session = openSession();

			AssetLink[] array = new AssetLinkImpl[3];

			array[0] = getByE1_PrevAndNext(
				session, assetLink, entryId1, orderByComparator, true);

			array[1] = assetLink;

			array[2] = getByE1_PrevAndNext(
				session, assetLink, entryId1, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AssetLink getByE1_PrevAndNext(
		Session session, AssetLink assetLink, long entryId1,
		OrderByComparator<AssetLink> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_ASSETLINK_WHERE);

		sb.append(_FINDER_COLUMN_E1_ENTRYID1_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AssetLinkModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(entryId1);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(assetLink)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AssetLink> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the asset links where entryId1 = &#63; from the database.
	 *
	 * @param entryId1 the entry id1
	 */
	@Override
	public void removeByE1(long entryId1) {
		for (AssetLink assetLink :
				findByE1(
					entryId1, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(assetLink);
		}
	}

	/**
	 * Returns the number of asset links where entryId1 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @return the number of matching asset links
	 */
	@Override
	public int countByE1(long entryId1) {
		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByE1;

			finderArgs = new Object[] {entryId1};

			count = (Long)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E1_ENTRYID1_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId1);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_E1_ENTRYID1_2 =
		"assetLink.entryId1 = ?";

	private FinderPath _finderPathWithPaginationFindByE2;
	private FinderPath _finderPathWithoutPaginationFindByE2;
	private FinderPath _finderPathCountByE2;

	/**
	 * Returns all the asset links where entryId2 = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @return the matching asset links
	 */
	@Override
	public List<AssetLink> findByE2(long entryId2) {
		return findByE2(entryId2, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset links where entryId2 = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId2 the entry id2
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @return the range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE2(long entryId2, int start, int end) {
		return findByE2(entryId2, start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset links where entryId2 = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId2 the entry id2
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE2(
		long entryId2, int start, int end,
		OrderByComparator<AssetLink> orderByComparator) {

		return findByE2(entryId2, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the asset links where entryId2 = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId2 the entry id2
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE2(
		long entryId2, int start, int end,
		OrderByComparator<AssetLink> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByE2;
				finderArgs = new Object[] {entryId2};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByE2;
			finderArgs = new Object[] {entryId2, start, end, orderByComparator};
		}

		List<AssetLink> list = null;

		if (useFinderCache && productionMode) {
			list = (List<AssetLink>)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AssetLink assetLink : list) {
					if (entryId2 != assetLink.getEntryId2()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E2_ENTRYID2_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AssetLinkModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId2);

				list = (List<AssetLink>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first asset link in the ordered set where entryId2 = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE2_First(
			long entryId2, OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE2_First(entryId2, orderByComparator);

		if (assetLink != null) {
			return assetLink;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("entryId2=");
		sb.append(entryId2);

		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

	/**
	 * Returns the first asset link in the ordered set where entryId2 = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE2_First(
		long entryId2, OrderByComparator<AssetLink> orderByComparator) {

		List<AssetLink> list = findByE2(entryId2, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last asset link in the ordered set where entryId2 = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE2_Last(
			long entryId2, OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE2_Last(entryId2, orderByComparator);

		if (assetLink != null) {
			return assetLink;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("entryId2=");
		sb.append(entryId2);

		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

	/**
	 * Returns the last asset link in the ordered set where entryId2 = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE2_Last(
		long entryId2, OrderByComparator<AssetLink> orderByComparator) {

		int count = countByE2(entryId2);

		if (count == 0) {
			return null;
		}

		List<AssetLink> list = findByE2(
			entryId2, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the asset links before and after the current asset link in the ordered set where entryId2 = &#63;.
	 *
	 * @param linkId the primary key of the current asset link
	 * @param entryId2 the entry id2
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next asset link
	 * @throws NoSuchLinkException if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink[] findByE2_PrevAndNext(
			long linkId, long entryId2,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = findByPrimaryKey(linkId);

		Session session = null;

		try {
			session = openSession();

			AssetLink[] array = new AssetLinkImpl[3];

			array[0] = getByE2_PrevAndNext(
				session, assetLink, entryId2, orderByComparator, true);

			array[1] = assetLink;

			array[2] = getByE2_PrevAndNext(
				session, assetLink, entryId2, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AssetLink getByE2_PrevAndNext(
		Session session, AssetLink assetLink, long entryId2,
		OrderByComparator<AssetLink> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_ASSETLINK_WHERE);

		sb.append(_FINDER_COLUMN_E2_ENTRYID2_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AssetLinkModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(entryId2);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(assetLink)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AssetLink> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the asset links where entryId2 = &#63; from the database.
	 *
	 * @param entryId2 the entry id2
	 */
	@Override
	public void removeByE2(long entryId2) {
		for (AssetLink assetLink :
				findByE2(
					entryId2, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(assetLink);
		}
	}

	/**
	 * Returns the number of asset links where entryId2 = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @return the number of matching asset links
	 */
	@Override
	public int countByE2(long entryId2) {
		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByE2;

			finderArgs = new Object[] {entryId2};

			count = (Long)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E2_ENTRYID2_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId2);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_E2_ENTRYID2_2 =
		"assetLink.entryId2 = ?";

	private FinderPath _finderPathWithPaginationFindByE_E;
	private FinderPath _finderPathWithoutPaginationFindByE_E;
	private FinderPath _finderPathCountByE_E;

	/**
	 * Returns all the asset links where entryId1 = &#63; and entryId2 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @return the matching asset links
	 */
	@Override
	public List<AssetLink> findByE_E(long entryId1, long entryId2) {
		return findByE_E(
			entryId1, entryId2, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset links where entryId1 = &#63; and entryId2 = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @return the range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE_E(
		long entryId1, long entryId2, int start, int end) {

		return findByE_E(entryId1, entryId2, start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset links where entryId1 = &#63; and entryId2 = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE_E(
		long entryId1, long entryId2, int start, int end,
		OrderByComparator<AssetLink> orderByComparator) {

		return findByE_E(
			entryId1, entryId2, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the asset links where entryId1 = &#63; and entryId2 = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE_E(
		long entryId1, long entryId2, int start, int end,
		OrderByComparator<AssetLink> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByE_E;
				finderArgs = new Object[] {entryId1, entryId2};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByE_E;
			finderArgs = new Object[] {
				entryId1, entryId2, start, end, orderByComparator
			};
		}

		List<AssetLink> list = null;

		if (useFinderCache && productionMode) {
			list = (List<AssetLink>)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AssetLink assetLink : list) {
					if ((entryId1 != assetLink.getEntryId1()) ||
						(entryId2 != assetLink.getEntryId2())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E_E_ENTRYID1_2);

			sb.append(_FINDER_COLUMN_E_E_ENTRYID2_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AssetLinkModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId1);

				queryPos.add(entryId2);

				list = (List<AssetLink>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first asset link in the ordered set where entryId1 = &#63; and entryId2 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE_E_First(
			long entryId1, long entryId2,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE_E_First(
			entryId1, entryId2, orderByComparator);

		if (assetLink != null) {
			return assetLink;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("entryId1=");
		sb.append(entryId1);

		sb.append(", entryId2=");
		sb.append(entryId2);

		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

	/**
	 * Returns the first asset link in the ordered set where entryId1 = &#63; and entryId2 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE_E_First(
		long entryId1, long entryId2,
		OrderByComparator<AssetLink> orderByComparator) {

		List<AssetLink> list = findByE_E(
			entryId1, entryId2, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last asset link in the ordered set where entryId1 = &#63; and entryId2 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE_E_Last(
			long entryId1, long entryId2,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE_E_Last(
			entryId1, entryId2, orderByComparator);

		if (assetLink != null) {
			return assetLink;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("entryId1=");
		sb.append(entryId1);

		sb.append(", entryId2=");
		sb.append(entryId2);

		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

	/**
	 * Returns the last asset link in the ordered set where entryId1 = &#63; and entryId2 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE_E_Last(
		long entryId1, long entryId2,
		OrderByComparator<AssetLink> orderByComparator) {

		int count = countByE_E(entryId1, entryId2);

		if (count == 0) {
			return null;
		}

		List<AssetLink> list = findByE_E(
			entryId1, entryId2, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the asset links before and after the current asset link in the ordered set where entryId1 = &#63; and entryId2 = &#63;.
	 *
	 * @param linkId the primary key of the current asset link
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next asset link
	 * @throws NoSuchLinkException if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink[] findByE_E_PrevAndNext(
			long linkId, long entryId1, long entryId2,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = findByPrimaryKey(linkId);

		Session session = null;

		try {
			session = openSession();

			AssetLink[] array = new AssetLinkImpl[3];

			array[0] = getByE_E_PrevAndNext(
				session, assetLink, entryId1, entryId2, orderByComparator,
				true);

			array[1] = assetLink;

			array[2] = getByE_E_PrevAndNext(
				session, assetLink, entryId1, entryId2, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AssetLink getByE_E_PrevAndNext(
		Session session, AssetLink assetLink, long entryId1, long entryId2,
		OrderByComparator<AssetLink> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_ASSETLINK_WHERE);

		sb.append(_FINDER_COLUMN_E_E_ENTRYID1_2);

		sb.append(_FINDER_COLUMN_E_E_ENTRYID2_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AssetLinkModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(entryId1);

		queryPos.add(entryId2);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(assetLink)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AssetLink> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the asset links where entryId1 = &#63; and entryId2 = &#63; from the database.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 */
	@Override
	public void removeByE_E(long entryId1, long entryId2) {
		for (AssetLink assetLink :
				findByE_E(
					entryId1, entryId2, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(assetLink);
		}
	}

	/**
	 * Returns the number of asset links where entryId1 = &#63; and entryId2 = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @return the number of matching asset links
	 */
	@Override
	public int countByE_E(long entryId1, long entryId2) {
		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByE_E;

			finderArgs = new Object[] {entryId1, entryId2};

			count = (Long)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E_E_ENTRYID1_2);

			sb.append(_FINDER_COLUMN_E_E_ENTRYID2_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId1);

				queryPos.add(entryId2);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_E_E_ENTRYID1_2 =
		"assetLink.entryId1 = ? AND ";

	private static final String _FINDER_COLUMN_E_E_ENTRYID2_2 =
		"assetLink.entryId2 = ?";

	private FinderPath _finderPathWithPaginationFindByE1_T;
	private FinderPath _finderPathWithoutPaginationFindByE1_T;
	private FinderPath _finderPathCountByE1_T;

	/**
	 * Returns all the asset links where entryId1 = &#63; and type = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param type the type
	 * @return the matching asset links
	 */
	@Override
	public List<AssetLink> findByE1_T(long entryId1, int type) {
		return findByE1_T(
			entryId1, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset links where entryId1 = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId1 the entry id1
	 * @param type the type
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @return the range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE1_T(
		long entryId1, int type, int start, int end) {

		return findByE1_T(entryId1, type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset links where entryId1 = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId1 the entry id1
	 * @param type the type
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE1_T(
		long entryId1, int type, int start, int end,
		OrderByComparator<AssetLink> orderByComparator) {

		return findByE1_T(entryId1, type, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the asset links where entryId1 = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId1 the entry id1
	 * @param type the type
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE1_T(
		long entryId1, int type, int start, int end,
		OrderByComparator<AssetLink> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByE1_T;
				finderArgs = new Object[] {entryId1, type};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByE1_T;
			finderArgs = new Object[] {
				entryId1, type, start, end, orderByComparator
			};
		}

		List<AssetLink> list = null;

		if (useFinderCache && productionMode) {
			list = (List<AssetLink>)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AssetLink assetLink : list) {
					if ((entryId1 != assetLink.getEntryId1()) ||
						(type != assetLink.getType())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E1_T_ENTRYID1_2);

			sb.append(_FINDER_COLUMN_E1_T_TYPE_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AssetLinkModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId1);

				queryPos.add(type);

				list = (List<AssetLink>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first asset link in the ordered set where entryId1 = &#63; and type = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE1_T_First(
			long entryId1, int type,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE1_T_First(
			entryId1, type, orderByComparator);

		if (assetLink != null) {
			return assetLink;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("entryId1=");
		sb.append(entryId1);

		sb.append(", type=");
		sb.append(type);

		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

	/**
	 * Returns the first asset link in the ordered set where entryId1 = &#63; and type = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE1_T_First(
		long entryId1, int type,
		OrderByComparator<AssetLink> orderByComparator) {

		List<AssetLink> list = findByE1_T(
			entryId1, type, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last asset link in the ordered set where entryId1 = &#63; and type = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE1_T_Last(
			long entryId1, int type,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE1_T_Last(
			entryId1, type, orderByComparator);

		if (assetLink != null) {
			return assetLink;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("entryId1=");
		sb.append(entryId1);

		sb.append(", type=");
		sb.append(type);

		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

	/**
	 * Returns the last asset link in the ordered set where entryId1 = &#63; and type = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE1_T_Last(
		long entryId1, int type,
		OrderByComparator<AssetLink> orderByComparator) {

		int count = countByE1_T(entryId1, type);

		if (count == 0) {
			return null;
		}

		List<AssetLink> list = findByE1_T(
			entryId1, type, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the asset links before and after the current asset link in the ordered set where entryId1 = &#63; and type = &#63;.
	 *
	 * @param linkId the primary key of the current asset link
	 * @param entryId1 the entry id1
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next asset link
	 * @throws NoSuchLinkException if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink[] findByE1_T_PrevAndNext(
			long linkId, long entryId1, int type,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = findByPrimaryKey(linkId);

		Session session = null;

		try {
			session = openSession();

			AssetLink[] array = new AssetLinkImpl[3];

			array[0] = getByE1_T_PrevAndNext(
				session, assetLink, entryId1, type, orderByComparator, true);

			array[1] = assetLink;

			array[2] = getByE1_T_PrevAndNext(
				session, assetLink, entryId1, type, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AssetLink getByE1_T_PrevAndNext(
		Session session, AssetLink assetLink, long entryId1, int type,
		OrderByComparator<AssetLink> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_ASSETLINK_WHERE);

		sb.append(_FINDER_COLUMN_E1_T_ENTRYID1_2);

		sb.append(_FINDER_COLUMN_E1_T_TYPE_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AssetLinkModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(entryId1);

		queryPos.add(type);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(assetLink)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AssetLink> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the asset links where entryId1 = &#63; and type = &#63; from the database.
	 *
	 * @param entryId1 the entry id1
	 * @param type the type
	 */
	@Override
	public void removeByE1_T(long entryId1, int type) {
		for (AssetLink assetLink :
				findByE1_T(
					entryId1, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(assetLink);
		}
	}

	/**
	 * Returns the number of asset links where entryId1 = &#63; and type = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param type the type
	 * @return the number of matching asset links
	 */
	@Override
	public int countByE1_T(long entryId1, int type) {
		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByE1_T;

			finderArgs = new Object[] {entryId1, type};

			count = (Long)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E1_T_ENTRYID1_2);

			sb.append(_FINDER_COLUMN_E1_T_TYPE_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId1);

				queryPos.add(type);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_E1_T_ENTRYID1_2 =
		"assetLink.entryId1 = ? AND ";

	private static final String _FINDER_COLUMN_E1_T_TYPE_2 =
		"assetLink.type = ?";

	private FinderPath _finderPathWithPaginationFindByE2_T;
	private FinderPath _finderPathWithoutPaginationFindByE2_T;
	private FinderPath _finderPathCountByE2_T;

	/**
	 * Returns all the asset links where entryId2 = &#63; and type = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @return the matching asset links
	 */
	@Override
	public List<AssetLink> findByE2_T(long entryId2, int type) {
		return findByE2_T(
			entryId2, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset links where entryId2 = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @return the range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE2_T(
		long entryId2, int type, int start, int end) {

		return findByE2_T(entryId2, type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset links where entryId2 = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE2_T(
		long entryId2, int type, int start, int end,
		OrderByComparator<AssetLink> orderByComparator) {

		return findByE2_T(entryId2, type, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the asset links where entryId2 = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching asset links
	 */
	@Override
	public List<AssetLink> findByE2_T(
		long entryId2, int type, int start, int end,
		OrderByComparator<AssetLink> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByE2_T;
				finderArgs = new Object[] {entryId2, type};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByE2_T;
			finderArgs = new Object[] {
				entryId2, type, start, end, orderByComparator
			};
		}

		List<AssetLink> list = null;

		if (useFinderCache && productionMode) {
			list = (List<AssetLink>)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AssetLink assetLink : list) {
					if ((entryId2 != assetLink.getEntryId2()) ||
						(type != assetLink.getType())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E2_T_ENTRYID2_2);

			sb.append(_FINDER_COLUMN_E2_T_TYPE_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(AssetLinkModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId2);

				queryPos.add(type);

				list = (List<AssetLink>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first asset link in the ordered set where entryId2 = &#63; and type = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE2_T_First(
			long entryId2, int type,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE2_T_First(
			entryId2, type, orderByComparator);

		if (assetLink != null) {
			return assetLink;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("entryId2=");
		sb.append(entryId2);

		sb.append(", type=");
		sb.append(type);

		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

	/**
	 * Returns the first asset link in the ordered set where entryId2 = &#63; and type = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE2_T_First(
		long entryId2, int type,
		OrderByComparator<AssetLink> orderByComparator) {

		List<AssetLink> list = findByE2_T(
			entryId2, type, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last asset link in the ordered set where entryId2 = &#63; and type = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE2_T_Last(
			long entryId2, int type,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE2_T_Last(
			entryId2, type, orderByComparator);

		if (assetLink != null) {
			return assetLink;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("entryId2=");
		sb.append(entryId2);

		sb.append(", type=");
		sb.append(type);

		sb.append("}");

		throw new NoSuchLinkException(sb.toString());
	}

	/**
	 * Returns the last asset link in the ordered set where entryId2 = &#63; and type = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE2_T_Last(
		long entryId2, int type,
		OrderByComparator<AssetLink> orderByComparator) {

		int count = countByE2_T(entryId2, type);

		if (count == 0) {
			return null;
		}

		List<AssetLink> list = findByE2_T(
			entryId2, type, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the asset links before and after the current asset link in the ordered set where entryId2 = &#63; and type = &#63;.
	 *
	 * @param linkId the primary key of the current asset link
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next asset link
	 * @throws NoSuchLinkException if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink[] findByE2_T_PrevAndNext(
			long linkId, long entryId2, int type,
			OrderByComparator<AssetLink> orderByComparator)
		throws NoSuchLinkException {

		AssetLink assetLink = findByPrimaryKey(linkId);

		Session session = null;

		try {
			session = openSession();

			AssetLink[] array = new AssetLinkImpl[3];

			array[0] = getByE2_T_PrevAndNext(
				session, assetLink, entryId2, type, orderByComparator, true);

			array[1] = assetLink;

			array[2] = getByE2_T_PrevAndNext(
				session, assetLink, entryId2, type, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected AssetLink getByE2_T_PrevAndNext(
		Session session, AssetLink assetLink, long entryId2, int type,
		OrderByComparator<AssetLink> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_ASSETLINK_WHERE);

		sb.append(_FINDER_COLUMN_E2_T_ENTRYID2_2);

		sb.append(_FINDER_COLUMN_E2_T_TYPE_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(AssetLinkModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(entryId2);

		queryPos.add(type);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(assetLink)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<AssetLink> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the asset links where entryId2 = &#63; and type = &#63; from the database.
	 *
	 * @param entryId2 the entry id2
	 * @param type the type
	 */
	@Override
	public void removeByE2_T(long entryId2, int type) {
		for (AssetLink assetLink :
				findByE2_T(
					entryId2, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(assetLink);
		}
	}

	/**
	 * Returns the number of asset links where entryId2 = &#63; and type = &#63;.
	 *
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @return the number of matching asset links
	 */
	@Override
	public int countByE2_T(long entryId2, int type) {
		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByE2_T;

			finderArgs = new Object[] {entryId2, type};

			count = (Long)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E2_T_ENTRYID2_2);

			sb.append(_FINDER_COLUMN_E2_T_TYPE_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId2);

				queryPos.add(type);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_E2_T_ENTRYID2_2 =
		"assetLink.entryId2 = ? AND ";

	private static final String _FINDER_COLUMN_E2_T_TYPE_2 =
		"assetLink.type = ?";

	private FinderPath _finderPathFetchByE_E_T;
	private FinderPath _finderPathCountByE_E_T;

	/**
	 * Returns the asset link where entryId1 = &#63; and entryId2 = &#63; and type = &#63; or throws a <code>NoSuchLinkException</code> if it could not be found.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @return the matching asset link
	 * @throws NoSuchLinkException if a matching asset link could not be found
	 */
	@Override
	public AssetLink findByE_E_T(long entryId1, long entryId2, int type)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByE_E_T(entryId1, entryId2, type);

		if (assetLink == null) {
			StringBundler sb = new StringBundler(8);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("entryId1=");
			sb.append(entryId1);

			sb.append(", entryId2=");
			sb.append(entryId2);

			sb.append(", type=");
			sb.append(type);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchLinkException(sb.toString());
		}

		return assetLink;
	}

	/**
	 * Returns the asset link where entryId1 = &#63; and entryId2 = &#63; and type = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @return the matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE_E_T(long entryId1, long entryId2, int type) {
		return fetchByE_E_T(entryId1, entryId2, type, true);
	}

	/**
	 * Returns the asset link where entryId1 = &#63; and entryId2 = &#63; and type = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching asset link, or <code>null</code> if a matching asset link could not be found
	 */
	@Override
	public AssetLink fetchByE_E_T(
		long entryId1, long entryId2, int type, boolean useFinderCache) {

		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {entryId1, entryId2, type};
		}

		Object result = null;

		if (useFinderCache && productionMode) {
			result = FinderCacheUtil.getResult(
				_finderPathFetchByE_E_T, finderArgs, this);
		}

		if (result instanceof AssetLink) {
			AssetLink assetLink = (AssetLink)result;

			if ((entryId1 != assetLink.getEntryId1()) ||
				(entryId2 != assetLink.getEntryId2()) ||
				(type != assetLink.getType())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(5);

			sb.append(_SQL_SELECT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E_E_T_ENTRYID1_2);

			sb.append(_FINDER_COLUMN_E_E_T_ENTRYID2_2);

			sb.append(_FINDER_COLUMN_E_E_T_TYPE_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId1);

				queryPos.add(entryId2);

				queryPos.add(type);

				List<AssetLink> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						FinderCacheUtil.putResult(
							_finderPathFetchByE_E_T, finderArgs, list);
					}
				}
				else {
					AssetLink assetLink = list.get(0);

					result = assetLink;

					cacheResult(assetLink);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (AssetLink)result;
		}
	}

	/**
	 * Removes the asset link where entryId1 = &#63; and entryId2 = &#63; and type = &#63; from the database.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @return the asset link that was removed
	 */
	@Override
	public AssetLink removeByE_E_T(long entryId1, long entryId2, int type)
		throws NoSuchLinkException {

		AssetLink assetLink = findByE_E_T(entryId1, entryId2, type);

		return remove(assetLink);
	}

	/**
	 * Returns the number of asset links where entryId1 = &#63; and entryId2 = &#63; and type = &#63;.
	 *
	 * @param entryId1 the entry id1
	 * @param entryId2 the entry id2
	 * @param type the type
	 * @return the number of matching asset links
	 */
	@Override
	public int countByE_E_T(long entryId1, long entryId2, int type) {
		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByE_E_T;

			finderArgs = new Object[] {entryId1, entryId2, type};

			count = (Long)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_ASSETLINK_WHERE);

			sb.append(_FINDER_COLUMN_E_E_T_ENTRYID1_2);

			sb.append(_FINDER_COLUMN_E_E_T_ENTRYID2_2);

			sb.append(_FINDER_COLUMN_E_E_T_TYPE_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(entryId1);

				queryPos.add(entryId2);

				queryPos.add(type);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_E_E_T_ENTRYID1_2 =
		"assetLink.entryId1 = ? AND ";

	private static final String _FINDER_COLUMN_E_E_T_ENTRYID2_2 =
		"assetLink.entryId2 = ? AND ";

	private static final String _FINDER_COLUMN_E_E_T_TYPE_2 =
		"assetLink.type = ?";

	public AssetLinkPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("type", "type_");

		setDBColumnNames(dbColumnNames);

		setModelClass(AssetLink.class);

		setModelImplClass(AssetLinkImpl.class);
		setModelPKClass(long.class);

		setTable(AssetLinkTable.INSTANCE);
	}

	/**
	 * Caches the asset link in the entity cache if it is enabled.
	 *
	 * @param assetLink the asset link
	 */
	@Override
	public void cacheResult(AssetLink assetLink) {
		if (assetLink.getCtCollectionId() != 0) {
			return;
		}

		EntityCacheUtil.putResult(
			AssetLinkImpl.class, assetLink.getPrimaryKey(), assetLink);

		FinderCacheUtil.putResult(
			_finderPathFetchByE_E_T,
			new Object[] {
				assetLink.getEntryId1(), assetLink.getEntryId2(),
				assetLink.getType()
			},
			assetLink);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the asset links in the entity cache if it is enabled.
	 *
	 * @param assetLinks the asset links
	 */
	@Override
	public void cacheResult(List<AssetLink> assetLinks) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (assetLinks.size() > _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (AssetLink assetLink : assetLinks) {
			if (assetLink.getCtCollectionId() != 0) {
				continue;
			}

			if (EntityCacheUtil.getResult(
					AssetLinkImpl.class, assetLink.getPrimaryKey()) == null) {

				cacheResult(assetLink);
			}
		}
	}

	/**
	 * Clears the cache for all asset links.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>com.liferay.portal.kernel.dao.orm.FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		EntityCacheUtil.clearCache(AssetLinkImpl.class);

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the asset link.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>com.liferay.portal.kernel.dao.orm.FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(AssetLink assetLink) {
		EntityCacheUtil.removeResult(AssetLinkImpl.class, assetLink);
	}

	@Override
	public void clearCache(List<AssetLink> assetLinks) {
		for (AssetLink assetLink : assetLinks) {
			EntityCacheUtil.removeResult(AssetLinkImpl.class, assetLink);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			EntityCacheUtil.removeResult(AssetLinkImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		AssetLinkModelImpl assetLinkModelImpl) {

		Object[] args = new Object[] {
			assetLinkModelImpl.getEntryId1(), assetLinkModelImpl.getEntryId2(),
			assetLinkModelImpl.getType()
		};

		FinderCacheUtil.putResult(
			_finderPathCountByE_E_T, args, Long.valueOf(1), false);
		FinderCacheUtil.putResult(
			_finderPathFetchByE_E_T, args, assetLinkModelImpl, false);
	}

	/**
	 * Creates a new asset link with the primary key. Does not add the asset link to the database.
	 *
	 * @param linkId the primary key for the new asset link
	 * @return the new asset link
	 */
	@Override
	public AssetLink create(long linkId) {
		AssetLink assetLink = new AssetLinkImpl();

		assetLink.setNew(true);
		assetLink.setPrimaryKey(linkId);

		assetLink.setCompanyId(CompanyThreadLocal.getCompanyId());

		return assetLink;
	}

	/**
	 * Removes the asset link with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param linkId the primary key of the asset link
	 * @return the asset link that was removed
	 * @throws NoSuchLinkException if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink remove(long linkId) throws NoSuchLinkException {
		return remove((Serializable)linkId);
	}

	/**
	 * Removes the asset link with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the asset link
	 * @return the asset link that was removed
	 * @throws NoSuchLinkException if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink remove(Serializable primaryKey)
		throws NoSuchLinkException {

		Session session = null;

		try {
			session = openSession();

			AssetLink assetLink = (AssetLink)session.get(
				AssetLinkImpl.class, primaryKey);

			if (assetLink == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchLinkException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(assetLink);
		}
		catch (NoSuchLinkException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected AssetLink removeImpl(AssetLink assetLink) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(assetLink)) {
				assetLink = (AssetLink)session.get(
					AssetLinkImpl.class, assetLink.getPrimaryKeyObj());
			}

			if ((assetLink != null) &&
				CTPersistenceHelperUtil.isRemove(assetLink)) {

				session.delete(assetLink);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (assetLink != null) {
			clearCache(assetLink);
		}

		return assetLink;
	}

	@Override
	public AssetLink updateImpl(AssetLink assetLink) {
		boolean isNew = assetLink.isNew();

		if (!(assetLink instanceof AssetLinkModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(assetLink.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(assetLink);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in assetLink proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom AssetLink implementation " +
					assetLink.getClass());
		}

		AssetLinkModelImpl assetLinkModelImpl = (AssetLinkModelImpl)assetLink;

		if (isNew && (assetLink.getCreateDate() == null)) {
			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			Date date = new Date();

			if (serviceContext == null) {
				assetLink.setCreateDate(date);
			}
			else {
				assetLink.setCreateDate(serviceContext.getCreateDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (CTPersistenceHelperUtil.isInsert(assetLink)) {
				if (!isNew) {
					session.evict(
						AssetLinkImpl.class, assetLink.getPrimaryKeyObj());
				}

				session.save(assetLink);
			}
			else {
				assetLink = (AssetLink)session.merge(assetLink);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (assetLink.getCtCollectionId() != 0) {
			if (isNew) {
				assetLink.setNew(false);
			}

			assetLink.resetOriginalValues();

			return assetLink;
		}

		EntityCacheUtil.putResult(
			AssetLinkImpl.class, assetLinkModelImpl, false, true);

		cacheUniqueFindersCache(assetLinkModelImpl);

		if (isNew) {
			assetLink.setNew(false);
		}

		assetLink.resetOriginalValues();

		return assetLink;
	}

	/**
	 * Returns the asset link with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the asset link
	 * @return the asset link
	 * @throws NoSuchLinkException if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink findByPrimaryKey(Serializable primaryKey)
		throws NoSuchLinkException {

		AssetLink assetLink = fetchByPrimaryKey(primaryKey);

		if (assetLink == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchLinkException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return assetLink;
	}

	/**
	 * Returns the asset link with the primary key or throws a <code>NoSuchLinkException</code> if it could not be found.
	 *
	 * @param linkId the primary key of the asset link
	 * @return the asset link
	 * @throws NoSuchLinkException if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink findByPrimaryKey(long linkId) throws NoSuchLinkException {
		return findByPrimaryKey((Serializable)linkId);
	}

	/**
	 * Returns the asset link with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the asset link
	 * @return the asset link, or <code>null</code> if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink fetchByPrimaryKey(Serializable primaryKey) {
		if (CTPersistenceHelperUtil.isProductionMode(
				AssetLink.class, primaryKey)) {

			return super.fetchByPrimaryKey(primaryKey);
		}

		AssetLink assetLink = null;

		Session session = null;

		try {
			session = openSession();

			assetLink = (AssetLink)session.get(AssetLinkImpl.class, primaryKey);

			if (assetLink != null) {
				cacheResult(assetLink);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return assetLink;
	}

	/**
	 * Returns the asset link with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param linkId the primary key of the asset link
	 * @return the asset link, or <code>null</code> if a asset link with the primary key could not be found
	 */
	@Override
	public AssetLink fetchByPrimaryKey(long linkId) {
		return fetchByPrimaryKey((Serializable)linkId);
	}

	@Override
	public Map<Serializable, AssetLink> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		if (CTPersistenceHelperUtil.isProductionMode(AssetLink.class)) {
			return super.fetchByPrimaryKeys(primaryKeys);
		}

		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, AssetLink> map =
			new HashMap<Serializable, AssetLink>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			AssetLink assetLink = fetchByPrimaryKey(primaryKey);

			if (assetLink != null) {
				map.put(primaryKey, assetLink);
			}

			return map;
		}

		if ((databaseInMaxParameters > 0) &&
			(primaryKeys.size() > databaseInMaxParameters)) {

			Iterator<Serializable> iterator = primaryKeys.iterator();

			while (iterator.hasNext()) {
				Set<Serializable> page = new HashSet<>();

				for (int i = 0;
					 (i < databaseInMaxParameters) && iterator.hasNext(); i++) {

					page.add(iterator.next());
				}

				map.putAll(fetchByPrimaryKeys(page));
			}

			return map;
		}

		StringBundler sb = new StringBundler((primaryKeys.size() * 2) + 1);

		sb.append(getSelectSQL());
		sb.append(" WHERE ");
		sb.append(getPKDBName());
		sb.append(" IN (");

		for (Serializable primaryKey : primaryKeys) {
			sb.append((long)primaryKey);

			sb.append(",");
		}

		sb.setIndex(sb.index() - 1);

		sb.append(")");

		String sql = sb.toString();

		Session session = null;

		try {
			session = openSession();

			Query query = session.createQuery(sql);

			for (AssetLink assetLink : (List<AssetLink>)query.list()) {
				map.put(assetLink.getPrimaryKeyObj(), assetLink);

				cacheResult(assetLink);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the asset links.
	 *
	 * @return the asset links
	 */
	@Override
	public List<AssetLink> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset links.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @return the range of asset links
	 */
	@Override
	public List<AssetLink> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset links.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of asset links
	 */
	@Override
	public List<AssetLink> findAll(
		int start, int end, OrderByComparator<AssetLink> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the asset links.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AssetLinkModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of asset links
	 * @param end the upper bound of the range of asset links (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of asset links
	 */
	@Override
	public List<AssetLink> findAll(
		int start, int end, OrderByComparator<AssetLink> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<AssetLink> list = null;

		if (useFinderCache && productionMode) {
			list = (List<AssetLink>)FinderCacheUtil.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_ASSETLINK);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_ASSETLINK;

				sql = sql.concat(AssetLinkModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<AssetLink>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the asset links from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (AssetLink assetLink : findAll()) {
			remove(assetLink);
		}
	}

	/**
	 * Returns the number of asset links.
	 *
	 * @return the number of asset links
	 */
	@Override
	public int countAll() {
		boolean productionMode = CTPersistenceHelperUtil.isProductionMode(
			AssetLink.class);

		Long count = null;

		if (productionMode) {
			count = (Long)FinderCacheUtil.getResult(
				_finderPathCountAll, FINDER_ARGS_EMPTY, this);
		}

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_ASSETLINK);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					FinderCacheUtil.putResult(
						_finderPathCountAll, FINDER_ARGS_EMPTY, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return EntityCacheUtil.getEntityCache();
	}

	@Override
	protected String getPKDBName() {
		return "linkId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_ASSETLINK;
	}

	@Override
	public Set<String> getCTColumnNames(
		CTColumnResolutionType ctColumnResolutionType) {

		return _ctColumnNamesMap.getOrDefault(
			ctColumnResolutionType, Collections.emptySet());
	}

	@Override
	public List<String> getMappingTableNames() {
		return _mappingTableNames;
	}

	@Override
	public Map<String, Integer> getTableColumnsMap() {
		return AssetLinkModelImpl.TABLE_COLUMNS_MAP;
	}

	@Override
	public String getTableName() {
		return "AssetLink";
	}

	@Override
	public List<String[]> getUniqueIndexColumnNames() {
		return _uniqueIndexColumnNames;
	}

	private static final Map<CTColumnResolutionType, Set<String>>
		_ctColumnNamesMap = new EnumMap<CTColumnResolutionType, Set<String>>(
			CTColumnResolutionType.class);
	private static final List<String> _mappingTableNames =
		new ArrayList<String>();
	private static final List<String[]> _uniqueIndexColumnNames =
		new ArrayList<String[]>();

	static {
		Set<String> ctControlColumnNames = new HashSet<String>();
		Set<String> ctStrictColumnNames = new HashSet<String>();

		ctControlColumnNames.add("mvccVersion");
		ctControlColumnNames.add("ctCollectionId");
		ctStrictColumnNames.add("companyId");
		ctStrictColumnNames.add("userId");
		ctStrictColumnNames.add("userName");
		ctStrictColumnNames.add("createDate");
		ctStrictColumnNames.add("entryId1");
		ctStrictColumnNames.add("entryId2");
		ctStrictColumnNames.add("type_");
		ctStrictColumnNames.add("weight");

		_ctColumnNamesMap.put(
			CTColumnResolutionType.CONTROL, ctControlColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.PK, Collections.singleton("linkId"));
		_ctColumnNamesMap.put(
			CTColumnResolutionType.STRICT, ctStrictColumnNames);

		_uniqueIndexColumnNames.add(
			new String[] {"entryId1", "entryId2", "type_"});
	}

	/**
	 * Initializes the asset link persistence.
	 */
	public void afterPropertiesSet() {
		Registry registry = RegistryUtil.getRegistry();

		_argumentsResolverServiceRegistration = registry.registerService(
			ArgumentsResolver.class, new AssetLinkModelArgumentsResolver(),
			HashMapBuilder.<String, Object>put(
				"model.class.name", AssetLink.class.getName()
			).build());

		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD));

		_finderPathWithPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByE1 = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByE1",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"entryId1"}, true);

		_finderPathWithoutPaginationFindByE1 = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByE1",
			new String[] {Long.class.getName()}, new String[] {"entryId1"},
			true);

		_finderPathCountByE1 = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByE1",
			new String[] {Long.class.getName()}, new String[] {"entryId1"},
			false);

		_finderPathWithPaginationFindByE2 = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByE2",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"entryId2"}, true);

		_finderPathWithoutPaginationFindByE2 = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByE2",
			new String[] {Long.class.getName()}, new String[] {"entryId2"},
			true);

		_finderPathCountByE2 = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByE2",
			new String[] {Long.class.getName()}, new String[] {"entryId2"},
			false);

		_finderPathWithPaginationFindByE_E = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByE_E",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"entryId1", "entryId2"}, true);

		_finderPathWithoutPaginationFindByE_E = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByE_E",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"entryId1", "entryId2"}, true);

		_finderPathCountByE_E = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByE_E",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"entryId1", "entryId2"}, false);

		_finderPathWithPaginationFindByE1_T = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByE1_T",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"entryId1", "type_"}, true);

		_finderPathWithoutPaginationFindByE1_T = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByE1_T",
			new String[] {Long.class.getName(), Integer.class.getName()},
			new String[] {"entryId1", "type_"}, true);

		_finderPathCountByE1_T = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByE1_T",
			new String[] {Long.class.getName(), Integer.class.getName()},
			new String[] {"entryId1", "type_"}, false);

		_finderPathWithPaginationFindByE2_T = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByE2_T",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"entryId2", "type_"}, true);

		_finderPathWithoutPaginationFindByE2_T = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByE2_T",
			new String[] {Long.class.getName(), Integer.class.getName()},
			new String[] {"entryId2", "type_"}, true);

		_finderPathCountByE2_T = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByE2_T",
			new String[] {Long.class.getName(), Integer.class.getName()},
			new String[] {"entryId2", "type_"}, false);

		_finderPathFetchByE_E_T = _createFinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByE_E_T",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName()
			},
			new String[] {"entryId1", "entryId2", "type_"}, true);

		_finderPathCountByE_E_T = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByE_E_T",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName()
			},
			new String[] {"entryId1", "entryId2", "type_"}, false);

		_setAssetLinkUtilPersistence(this);
	}

	public void destroy() {
		_setAssetLinkUtilPersistence(null);

		EntityCacheUtil.removeCache(AssetLinkImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	private void _setAssetLinkUtilPersistence(
		AssetLinkPersistence assetLinkPersistence) {

		try {
			Field field = AssetLinkUtil.class.getDeclaredField("_persistence");

			field.setAccessible(true);

			field.set(null, assetLinkPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	private static final String _SQL_SELECT_ASSETLINK =
		"SELECT assetLink FROM AssetLink assetLink";

	private static final String _SQL_SELECT_ASSETLINK_WHERE =
		"SELECT assetLink FROM AssetLink assetLink WHERE ";

	private static final String _SQL_COUNT_ASSETLINK =
		"SELECT COUNT(assetLink) FROM AssetLink assetLink";

	private static final String _SQL_COUNT_ASSETLINK_WHERE =
		"SELECT COUNT(assetLink) FROM AssetLink assetLink WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "assetLink.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No AssetLink exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No AssetLink exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		AssetLinkPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"type"});

	private FinderPath _createFinderPath(
		String cacheName, String methodName, String[] params,
		String[] columnNames, boolean baseModelResult) {

		FinderPath finderPath = new FinderPath(
			cacheName, methodName, params, columnNames, baseModelResult);

		if (!cacheName.equals(FINDER_CLASS_NAME_LIST_WITH_PAGINATION)) {
			Registry registry = RegistryUtil.getRegistry();

			_serviceRegistrations.add(
				registry.registerService(
					FinderPath.class, finderPath,
					HashMapBuilder.<String, Object>put(
						"cache.name", cacheName
					).build()));
		}

		return finderPath;
	}

	private Set<ServiceRegistration<FinderPath>> _serviceRegistrations =
		new HashSet<>();
	private ServiceRegistration<ArgumentsResolver>
		_argumentsResolverServiceRegistration;

	private static class AssetLinkModelArgumentsResolver
		implements ArgumentsResolver {

		@Override
		public Object[] getArguments(
			FinderPath finderPath, BaseModel<?> baseModel, boolean checkColumn,
			boolean original) {

			String[] columnNames = finderPath.getColumnNames();

			if ((columnNames == null) || (columnNames.length == 0)) {
				if (baseModel.isNew()) {
					return new Object[0];
				}

				return null;
			}

			AssetLinkModelImpl assetLinkModelImpl =
				(AssetLinkModelImpl)baseModel;

			long columnBitmask = assetLinkModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(assetLinkModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						assetLinkModelImpl.getColumnBitmask(columnName);
				}

				if (finderPath.isBaseModelResult() &&
					(AssetLinkPersistenceImpl.
						FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION ==
							finderPath.getCacheName())) {

					finderPathColumnBitmask |= _ORDER_BY_COLUMNS_BITMASK;
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(assetLinkModelImpl, columnNames, original);
			}

			return null;
		}

		private static Object[] _getValue(
			AssetLinkModelImpl assetLinkModelImpl, String[] columnNames,
			boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] = assetLinkModelImpl.getColumnOriginalValue(
						columnName);
				}
				else {
					arguments[i] = assetLinkModelImpl.getColumnValue(
						columnName);
				}
			}

			return arguments;
		}

		private static final Map<FinderPath, Long>
			_finderPathColumnBitmasksCache = new ConcurrentHashMap<>();

		private static final long _ORDER_BY_COLUMNS_BITMASK;

		static {
			long orderByColumnsBitmask = 0;

			orderByColumnsBitmask |= AssetLinkModelImpl.getColumnBitmask(
				"weight");

			_ORDER_BY_COLUMNS_BITMASK = orderByColumnsBitmask;
		}

	}

}