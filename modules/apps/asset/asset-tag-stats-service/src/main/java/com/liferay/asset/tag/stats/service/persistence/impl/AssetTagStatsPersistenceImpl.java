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

package com.liferay.asset.tag.stats.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.asset.tag.stats.exception.NoSuchTagStatsException;
import com.liferay.asset.tag.stats.model.AssetTagStats;
import com.liferay.asset.tag.stats.model.impl.AssetTagStatsImpl;
import com.liferay.asset.tag.stats.model.impl.AssetTagStatsModelImpl;
import com.liferay.asset.tag.stats.service.persistence.AssetTagStatsPersistence;
import com.liferay.asset.tag.stats.service.persistence.impl.constants.AssetPersistenceConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.CompanyProvider;
import com.liferay.portal.kernel.service.persistence.CompanyProviderWrapper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the asset tag stats service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = AssetTagStatsPersistence.class)
@ProviderType
public class AssetTagStatsPersistenceImpl
	extends BasePersistenceImpl<AssetTagStats>
	implements AssetTagStatsPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>AssetTagStatsUtil</code> to access the asset tag stats persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		AssetTagStatsImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByTagId;
	private FinderPath _finderPathWithoutPaginationFindByTagId;
	private FinderPath _finderPathCountByTagId;

	/**
	 * Returns all the asset tag statses where tagId = &#63;.
	 *
	 * @param tagId the tag ID
	 * @return the matching asset tag statses
	 */
	@Override
	public List<AssetTagStats> findByTagId(long tagId) {
		return findByTagId(tagId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset tag statses where tagId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>AssetTagStatsModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param tagId the tag ID
	 * @param start the lower bound of the range of asset tag statses
	 * @param end the upper bound of the range of asset tag statses (not inclusive)
	 * @return the range of matching asset tag statses
	 */
	@Override
	public List<AssetTagStats> findByTagId(long tagId, int start, int end) {
		return findByTagId(tagId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset tag statses where tagId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>AssetTagStatsModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param tagId the tag ID
	 * @param start the lower bound of the range of asset tag statses
	 * @param end the upper bound of the range of asset tag statses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching asset tag statses
	 */
	@Override
	public List<AssetTagStats> findByTagId(
		long tagId, int start, int end,
		OrderByComparator<AssetTagStats> orderByComparator) {

		return findByTagId(tagId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the asset tag statses where tagId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>AssetTagStatsModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param tagId the tag ID
	 * @param start the lower bound of the range of asset tag statses
	 * @param end the upper bound of the range of asset tag statses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching asset tag statses
	 */
	@Override
	public List<AssetTagStats> findByTagId(
		long tagId, int start, int end,
		OrderByComparator<AssetTagStats> orderByComparator,
		boolean retrieveFromCache) {

		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			pagination = false;
			finderPath = _finderPathWithoutPaginationFindByTagId;
			finderArgs = new Object[] {tagId};
		}
		else {
			finderPath = _finderPathWithPaginationFindByTagId;
			finderArgs = new Object[] {tagId, start, end, orderByComparator};
		}

		List<AssetTagStats> list = null;

		if (retrieveFromCache) {
			list = (List<AssetTagStats>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AssetTagStats assetTagStats : list) {
					if ((tagId != assetTagStats.getTagId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_ASSETTAGSTATS_WHERE);

			query.append(_FINDER_COLUMN_TAGID_TAGID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else if (pagination) {
				query.append(AssetTagStatsModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(tagId);

				if (!pagination) {
					list = (List<AssetTagStats>)QueryUtil.list(
						q, getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AssetTagStats>)QueryUtil.list(
						q, getDialect(), start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first asset tag stats in the ordered set where tagId = &#63;.
	 *
	 * @param tagId the tag ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset tag stats
	 * @throws NoSuchTagStatsException if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats findByTagId_First(
			long tagId, OrderByComparator<AssetTagStats> orderByComparator)
		throws NoSuchTagStatsException {

		AssetTagStats assetTagStats = fetchByTagId_First(
			tagId, orderByComparator);

		if (assetTagStats != null) {
			return assetTagStats;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("tagId=");
		msg.append(tagId);

		msg.append("}");

		throw new NoSuchTagStatsException(msg.toString());
	}

	/**
	 * Returns the first asset tag stats in the ordered set where tagId = &#63;.
	 *
	 * @param tagId the tag ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset tag stats, or <code>null</code> if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats fetchByTagId_First(
		long tagId, OrderByComparator<AssetTagStats> orderByComparator) {

		List<AssetTagStats> list = findByTagId(tagId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last asset tag stats in the ordered set where tagId = &#63;.
	 *
	 * @param tagId the tag ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset tag stats
	 * @throws NoSuchTagStatsException if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats findByTagId_Last(
			long tagId, OrderByComparator<AssetTagStats> orderByComparator)
		throws NoSuchTagStatsException {

		AssetTagStats assetTagStats = fetchByTagId_Last(
			tagId, orderByComparator);

		if (assetTagStats != null) {
			return assetTagStats;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("tagId=");
		msg.append(tagId);

		msg.append("}");

		throw new NoSuchTagStatsException(msg.toString());
	}

	/**
	 * Returns the last asset tag stats in the ordered set where tagId = &#63;.
	 *
	 * @param tagId the tag ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset tag stats, or <code>null</code> if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats fetchByTagId_Last(
		long tagId, OrderByComparator<AssetTagStats> orderByComparator) {

		int count = countByTagId(tagId);

		if (count == 0) {
			return null;
		}

		List<AssetTagStats> list = findByTagId(
			tagId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the asset tag statses before and after the current asset tag stats in the ordered set where tagId = &#63;.
	 *
	 * @param tagStatsId the primary key of the current asset tag stats
	 * @param tagId the tag ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next asset tag stats
	 * @throws NoSuchTagStatsException if a asset tag stats with the primary key could not be found
	 */
	@Override
	public AssetTagStats[] findByTagId_PrevAndNext(
			long tagStatsId, long tagId,
			OrderByComparator<AssetTagStats> orderByComparator)
		throws NoSuchTagStatsException {

		AssetTagStats assetTagStats = findByPrimaryKey(tagStatsId);

		Session session = null;

		try {
			session = openSession();

			AssetTagStats[] array = new AssetTagStatsImpl[3];

			array[0] = getByTagId_PrevAndNext(
				session, assetTagStats, tagId, orderByComparator, true);

			array[1] = assetTagStats;

			array[2] = getByTagId_PrevAndNext(
				session, assetTagStats, tagId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AssetTagStats getByTagId_PrevAndNext(
		Session session, AssetTagStats assetTagStats, long tagId,
		OrderByComparator<AssetTagStats> orderByComparator, boolean previous) {

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ASSETTAGSTATS_WHERE);

		query.append(_FINDER_COLUMN_TAGID_TAGID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AssetTagStatsModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(tagId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						assetTagStats)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<AssetTagStats> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the asset tag statses where tagId = &#63; from the database.
	 *
	 * @param tagId the tag ID
	 */
	@Override
	public void removeByTagId(long tagId) {
		for (AssetTagStats assetTagStats :
				findByTagId(
					tagId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(assetTagStats);
		}
	}

	/**
	 * Returns the number of asset tag statses where tagId = &#63;.
	 *
	 * @param tagId the tag ID
	 * @return the number of matching asset tag statses
	 */
	@Override
	public int countByTagId(long tagId) {
		FinderPath finderPath = _finderPathCountByTagId;

		Object[] finderArgs = new Object[] {tagId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_ASSETTAGSTATS_WHERE);

			query.append(_FINDER_COLUMN_TAGID_TAGID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(tagId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_TAGID_TAGID_2 =
		"assetTagStats.tagId = ?";

	private FinderPath _finderPathWithPaginationFindByClassNameId;
	private FinderPath _finderPathWithoutPaginationFindByClassNameId;
	private FinderPath _finderPathCountByClassNameId;

	/**
	 * Returns all the asset tag statses where classNameId = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @return the matching asset tag statses
	 */
	@Override
	public List<AssetTagStats> findByClassNameId(long classNameId) {
		return findByClassNameId(
			classNameId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset tag statses where classNameId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>AssetTagStatsModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param start the lower bound of the range of asset tag statses
	 * @param end the upper bound of the range of asset tag statses (not inclusive)
	 * @return the range of matching asset tag statses
	 */
	@Override
	public List<AssetTagStats> findByClassNameId(
		long classNameId, int start, int end) {

		return findByClassNameId(classNameId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset tag statses where classNameId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>AssetTagStatsModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param start the lower bound of the range of asset tag statses
	 * @param end the upper bound of the range of asset tag statses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching asset tag statses
	 */
	@Override
	public List<AssetTagStats> findByClassNameId(
		long classNameId, int start, int end,
		OrderByComparator<AssetTagStats> orderByComparator) {

		return findByClassNameId(
			classNameId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the asset tag statses where classNameId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>AssetTagStatsModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param start the lower bound of the range of asset tag statses
	 * @param end the upper bound of the range of asset tag statses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching asset tag statses
	 */
	@Override
	public List<AssetTagStats> findByClassNameId(
		long classNameId, int start, int end,
		OrderByComparator<AssetTagStats> orderByComparator,
		boolean retrieveFromCache) {

		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			pagination = false;
			finderPath = _finderPathWithoutPaginationFindByClassNameId;
			finderArgs = new Object[] {classNameId};
		}
		else {
			finderPath = _finderPathWithPaginationFindByClassNameId;
			finderArgs = new Object[] {
				classNameId, start, end, orderByComparator
			};
		}

		List<AssetTagStats> list = null;

		if (retrieveFromCache) {
			list = (List<AssetTagStats>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (AssetTagStats assetTagStats : list) {
					if ((classNameId != assetTagStats.getClassNameId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_ASSETTAGSTATS_WHERE);

			query.append(_FINDER_COLUMN_CLASSNAMEID_CLASSNAMEID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else if (pagination) {
				query.append(AssetTagStatsModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classNameId);

				if (!pagination) {
					list = (List<AssetTagStats>)QueryUtil.list(
						q, getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AssetTagStats>)QueryUtil.list(
						q, getDialect(), start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first asset tag stats in the ordered set where classNameId = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset tag stats
	 * @throws NoSuchTagStatsException if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats findByClassNameId_First(
			long classNameId,
			OrderByComparator<AssetTagStats> orderByComparator)
		throws NoSuchTagStatsException {

		AssetTagStats assetTagStats = fetchByClassNameId_First(
			classNameId, orderByComparator);

		if (assetTagStats != null) {
			return assetTagStats;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("classNameId=");
		msg.append(classNameId);

		msg.append("}");

		throw new NoSuchTagStatsException(msg.toString());
	}

	/**
	 * Returns the first asset tag stats in the ordered set where classNameId = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset tag stats, or <code>null</code> if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats fetchByClassNameId_First(
		long classNameId, OrderByComparator<AssetTagStats> orderByComparator) {

		List<AssetTagStats> list = findByClassNameId(
			classNameId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last asset tag stats in the ordered set where classNameId = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset tag stats
	 * @throws NoSuchTagStatsException if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats findByClassNameId_Last(
			long classNameId,
			OrderByComparator<AssetTagStats> orderByComparator)
		throws NoSuchTagStatsException {

		AssetTagStats assetTagStats = fetchByClassNameId_Last(
			classNameId, orderByComparator);

		if (assetTagStats != null) {
			return assetTagStats;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("classNameId=");
		msg.append(classNameId);

		msg.append("}");

		throw new NoSuchTagStatsException(msg.toString());
	}

	/**
	 * Returns the last asset tag stats in the ordered set where classNameId = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset tag stats, or <code>null</code> if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats fetchByClassNameId_Last(
		long classNameId, OrderByComparator<AssetTagStats> orderByComparator) {

		int count = countByClassNameId(classNameId);

		if (count == 0) {
			return null;
		}

		List<AssetTagStats> list = findByClassNameId(
			classNameId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the asset tag statses before and after the current asset tag stats in the ordered set where classNameId = &#63;.
	 *
	 * @param tagStatsId the primary key of the current asset tag stats
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next asset tag stats
	 * @throws NoSuchTagStatsException if a asset tag stats with the primary key could not be found
	 */
	@Override
	public AssetTagStats[] findByClassNameId_PrevAndNext(
			long tagStatsId, long classNameId,
			OrderByComparator<AssetTagStats> orderByComparator)
		throws NoSuchTagStatsException {

		AssetTagStats assetTagStats = findByPrimaryKey(tagStatsId);

		Session session = null;

		try {
			session = openSession();

			AssetTagStats[] array = new AssetTagStatsImpl[3];

			array[0] = getByClassNameId_PrevAndNext(
				session, assetTagStats, classNameId, orderByComparator, true);

			array[1] = assetTagStats;

			array[2] = getByClassNameId_PrevAndNext(
				session, assetTagStats, classNameId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AssetTagStats getByClassNameId_PrevAndNext(
		Session session, AssetTagStats assetTagStats, long classNameId,
		OrderByComparator<AssetTagStats> orderByComparator, boolean previous) {

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ASSETTAGSTATS_WHERE);

		query.append(_FINDER_COLUMN_CLASSNAMEID_CLASSNAMEID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AssetTagStatsModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(classNameId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						assetTagStats)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<AssetTagStats> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the asset tag statses where classNameId = &#63; from the database.
	 *
	 * @param classNameId the class name ID
	 */
	@Override
	public void removeByClassNameId(long classNameId) {
		for (AssetTagStats assetTagStats :
				findByClassNameId(
					classNameId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(assetTagStats);
		}
	}

	/**
	 * Returns the number of asset tag statses where classNameId = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @return the number of matching asset tag statses
	 */
	@Override
	public int countByClassNameId(long classNameId) {
		FinderPath finderPath = _finderPathCountByClassNameId;

		Object[] finderArgs = new Object[] {classNameId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_ASSETTAGSTATS_WHERE);

			query.append(_FINDER_COLUMN_CLASSNAMEID_CLASSNAMEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classNameId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_CLASSNAMEID_CLASSNAMEID_2 =
		"assetTagStats.classNameId = ?";

	private FinderPath _finderPathFetchByT_C;
	private FinderPath _finderPathCountByT_C;

	/**
	 * Returns the asset tag stats where tagId = &#63; and classNameId = &#63; or throws a <code>NoSuchTagStatsException</code> if it could not be found.
	 *
	 * @param tagId the tag ID
	 * @param classNameId the class name ID
	 * @return the matching asset tag stats
	 * @throws NoSuchTagStatsException if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats findByT_C(long tagId, long classNameId)
		throws NoSuchTagStatsException {

		AssetTagStats assetTagStats = fetchByT_C(tagId, classNameId);

		if (assetTagStats == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("tagId=");
			msg.append(tagId);

			msg.append(", classNameId=");
			msg.append(classNameId);

			msg.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(msg.toString());
			}

			throw new NoSuchTagStatsException(msg.toString());
		}

		return assetTagStats;
	}

	/**
	 * Returns the asset tag stats where tagId = &#63; and classNameId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param tagId the tag ID
	 * @param classNameId the class name ID
	 * @return the matching asset tag stats, or <code>null</code> if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats fetchByT_C(long tagId, long classNameId) {
		return fetchByT_C(tagId, classNameId, true);
	}

	/**
	 * Returns the asset tag stats where tagId = &#63; and classNameId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param tagId the tag ID
	 * @param classNameId the class name ID
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the matching asset tag stats, or <code>null</code> if a matching asset tag stats could not be found
	 */
	@Override
	public AssetTagStats fetchByT_C(
		long tagId, long classNameId, boolean retrieveFromCache) {

		Object[] finderArgs = new Object[] {tagId, classNameId};

		Object result = null;

		if (retrieveFromCache) {
			result = finderCache.getResult(
				_finderPathFetchByT_C, finderArgs, this);
		}

		if (result instanceof AssetTagStats) {
			AssetTagStats assetTagStats = (AssetTagStats)result;

			if ((tagId != assetTagStats.getTagId()) ||
				(classNameId != assetTagStats.getClassNameId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_ASSETTAGSTATS_WHERE);

			query.append(_FINDER_COLUMN_T_C_TAGID_2);

			query.append(_FINDER_COLUMN_T_C_CLASSNAMEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(tagId);

				qPos.add(classNameId);

				List<AssetTagStats> list = q.list();

				if (list.isEmpty()) {
					finderCache.putResult(
						_finderPathFetchByT_C, finderArgs, list);
				}
				else {
					AssetTagStats assetTagStats = list.get(0);

					result = assetTagStats;

					cacheResult(assetTagStats);
				}
			}
			catch (Exception e) {
				finderCache.removeResult(_finderPathFetchByT_C, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (AssetTagStats)result;
		}
	}

	/**
	 * Removes the asset tag stats where tagId = &#63; and classNameId = &#63; from the database.
	 *
	 * @param tagId the tag ID
	 * @param classNameId the class name ID
	 * @return the asset tag stats that was removed
	 */
	@Override
	public AssetTagStats removeByT_C(long tagId, long classNameId)
		throws NoSuchTagStatsException {

		AssetTagStats assetTagStats = findByT_C(tagId, classNameId);

		return remove(assetTagStats);
	}

	/**
	 * Returns the number of asset tag statses where tagId = &#63; and classNameId = &#63;.
	 *
	 * @param tagId the tag ID
	 * @param classNameId the class name ID
	 * @return the number of matching asset tag statses
	 */
	@Override
	public int countByT_C(long tagId, long classNameId) {
		FinderPath finderPath = _finderPathCountByT_C;

		Object[] finderArgs = new Object[] {tagId, classNameId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ASSETTAGSTATS_WHERE);

			query.append(_FINDER_COLUMN_T_C_TAGID_2);

			query.append(_FINDER_COLUMN_T_C_CLASSNAMEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(tagId);

				qPos.add(classNameId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_T_C_TAGID_2 =
		"assetTagStats.tagId = ? AND ";

	private static final String _FINDER_COLUMN_T_C_CLASSNAMEID_2 =
		"assetTagStats.classNameId = ?";

	public AssetTagStatsPersistenceImpl() {
		setModelClass(AssetTagStats.class);

		setModelImplClass(AssetTagStatsImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the asset tag stats in the entity cache if it is enabled.
	 *
	 * @param assetTagStats the asset tag stats
	 */
	@Override
	public void cacheResult(AssetTagStats assetTagStats) {
		entityCache.putResult(
			entityCacheEnabled, AssetTagStatsImpl.class,
			assetTagStats.getPrimaryKey(), assetTagStats);

		finderCache.putResult(
			_finderPathFetchByT_C,
			new Object[] {
				assetTagStats.getTagId(), assetTagStats.getClassNameId()
			},
			assetTagStats);

		assetTagStats.resetOriginalValues();
	}

	/**
	 * Caches the asset tag statses in the entity cache if it is enabled.
	 *
	 * @param assetTagStatses the asset tag statses
	 */
	@Override
	public void cacheResult(List<AssetTagStats> assetTagStatses) {
		for (AssetTagStats assetTagStats : assetTagStatses) {
			if (entityCache.getResult(
					entityCacheEnabled, AssetTagStatsImpl.class,
					assetTagStats.getPrimaryKey()) == null) {

				cacheResult(assetTagStats);
			}
			else {
				assetTagStats.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all asset tag statses.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(AssetTagStatsImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the asset tag stats.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(AssetTagStats assetTagStats) {
		entityCache.removeResult(
			entityCacheEnabled, AssetTagStatsImpl.class,
			assetTagStats.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache((AssetTagStatsModelImpl)assetTagStats, true);
	}

	@Override
	public void clearCache(List<AssetTagStats> assetTagStatses) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (AssetTagStats assetTagStats : assetTagStatses) {
			entityCache.removeResult(
				entityCacheEnabled, AssetTagStatsImpl.class,
				assetTagStats.getPrimaryKey());

			clearUniqueFindersCache(
				(AssetTagStatsModelImpl)assetTagStats, true);
		}
	}

	protected void cacheUniqueFindersCache(
		AssetTagStatsModelImpl assetTagStatsModelImpl) {

		Object[] args = new Object[] {
			assetTagStatsModelImpl.getTagId(),
			assetTagStatsModelImpl.getClassNameId()
		};

		finderCache.putResult(
			_finderPathCountByT_C, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByT_C, args, assetTagStatsModelImpl, false);
	}

	protected void clearUniqueFindersCache(
		AssetTagStatsModelImpl assetTagStatsModelImpl, boolean clearCurrent) {

		if (clearCurrent) {
			Object[] args = new Object[] {
				assetTagStatsModelImpl.getTagId(),
				assetTagStatsModelImpl.getClassNameId()
			};

			finderCache.removeResult(_finderPathCountByT_C, args);
			finderCache.removeResult(_finderPathFetchByT_C, args);
		}

		if ((assetTagStatsModelImpl.getColumnBitmask() &
			 _finderPathFetchByT_C.getColumnBitmask()) != 0) {

			Object[] args = new Object[] {
				assetTagStatsModelImpl.getOriginalTagId(),
				assetTagStatsModelImpl.getOriginalClassNameId()
			};

			finderCache.removeResult(_finderPathCountByT_C, args);
			finderCache.removeResult(_finderPathFetchByT_C, args);
		}
	}

	/**
	 * Creates a new asset tag stats with the primary key. Does not add the asset tag stats to the database.
	 *
	 * @param tagStatsId the primary key for the new asset tag stats
	 * @return the new asset tag stats
	 */
	@Override
	public AssetTagStats create(long tagStatsId) {
		AssetTagStats assetTagStats = new AssetTagStatsImpl();

		assetTagStats.setNew(true);
		assetTagStats.setPrimaryKey(tagStatsId);

		assetTagStats.setCompanyId(companyProvider.getCompanyId());

		return assetTagStats;
	}

	/**
	 * Removes the asset tag stats with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param tagStatsId the primary key of the asset tag stats
	 * @return the asset tag stats that was removed
	 * @throws NoSuchTagStatsException if a asset tag stats with the primary key could not be found
	 */
	@Override
	public AssetTagStats remove(long tagStatsId)
		throws NoSuchTagStatsException {

		return remove((Serializable)tagStatsId);
	}

	/**
	 * Removes the asset tag stats with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the asset tag stats
	 * @return the asset tag stats that was removed
	 * @throws NoSuchTagStatsException if a asset tag stats with the primary key could not be found
	 */
	@Override
	public AssetTagStats remove(Serializable primaryKey)
		throws NoSuchTagStatsException {

		Session session = null;

		try {
			session = openSession();

			AssetTagStats assetTagStats = (AssetTagStats)session.get(
				AssetTagStatsImpl.class, primaryKey);

			if (assetTagStats == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchTagStatsException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(assetTagStats);
		}
		catch (NoSuchTagStatsException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected AssetTagStats removeImpl(AssetTagStats assetTagStats) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(assetTagStats)) {
				assetTagStats = (AssetTagStats)session.get(
					AssetTagStatsImpl.class, assetTagStats.getPrimaryKeyObj());
			}

			if (assetTagStats != null) {
				session.delete(assetTagStats);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (assetTagStats != null) {
			clearCache(assetTagStats);
		}

		return assetTagStats;
	}

	@Override
	public AssetTagStats updateImpl(AssetTagStats assetTagStats) {
		boolean isNew = assetTagStats.isNew();

		if (!(assetTagStats instanceof AssetTagStatsModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(assetTagStats.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					assetTagStats);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in assetTagStats proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom AssetTagStats implementation " +
					assetTagStats.getClass());
		}

		AssetTagStatsModelImpl assetTagStatsModelImpl =
			(AssetTagStatsModelImpl)assetTagStats;

		Session session = null;

		try {
			session = openSession();

			if (assetTagStats.isNew()) {
				session.save(assetTagStats);

				assetTagStats.setNew(false);
			}
			else {
				assetTagStats = (AssetTagStats)session.merge(assetTagStats);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!_columnBitmaskEnabled) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else if (isNew) {
			Object[] args = new Object[] {assetTagStatsModelImpl.getTagId()};

			finderCache.removeResult(_finderPathCountByTagId, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByTagId, args);

			args = new Object[] {assetTagStatsModelImpl.getClassNameId()};

			finderCache.removeResult(_finderPathCountByClassNameId, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByClassNameId, args);

			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}
		else {
			if ((assetTagStatsModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByTagId.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					assetTagStatsModelImpl.getOriginalTagId()
				};

				finderCache.removeResult(_finderPathCountByTagId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByTagId, args);

				args = new Object[] {assetTagStatsModelImpl.getTagId()};

				finderCache.removeResult(_finderPathCountByTagId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByTagId, args);
			}

			if ((assetTagStatsModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByClassNameId.
					 getColumnBitmask()) != 0) {

				Object[] args = new Object[] {
					assetTagStatsModelImpl.getOriginalClassNameId()
				};

				finderCache.removeResult(_finderPathCountByClassNameId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByClassNameId, args);

				args = new Object[] {assetTagStatsModelImpl.getClassNameId()};

				finderCache.removeResult(_finderPathCountByClassNameId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByClassNameId, args);
			}
		}

		entityCache.putResult(
			entityCacheEnabled, AssetTagStatsImpl.class,
			assetTagStats.getPrimaryKey(), assetTagStats, false);

		clearUniqueFindersCache(assetTagStatsModelImpl, false);
		cacheUniqueFindersCache(assetTagStatsModelImpl);

		assetTagStats.resetOriginalValues();

		return assetTagStats;
	}

	/**
	 * Returns the asset tag stats with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the asset tag stats
	 * @return the asset tag stats
	 * @throws NoSuchTagStatsException if a asset tag stats with the primary key could not be found
	 */
	@Override
	public AssetTagStats findByPrimaryKey(Serializable primaryKey)
		throws NoSuchTagStatsException {

		AssetTagStats assetTagStats = fetchByPrimaryKey(primaryKey);

		if (assetTagStats == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchTagStatsException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return assetTagStats;
	}

	/**
	 * Returns the asset tag stats with the primary key or throws a <code>NoSuchTagStatsException</code> if it could not be found.
	 *
	 * @param tagStatsId the primary key of the asset tag stats
	 * @return the asset tag stats
	 * @throws NoSuchTagStatsException if a asset tag stats with the primary key could not be found
	 */
	@Override
	public AssetTagStats findByPrimaryKey(long tagStatsId)
		throws NoSuchTagStatsException {

		return findByPrimaryKey((Serializable)tagStatsId);
	}

	/**
	 * Returns the asset tag stats with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param tagStatsId the primary key of the asset tag stats
	 * @return the asset tag stats, or <code>null</code> if a asset tag stats with the primary key could not be found
	 */
	@Override
	public AssetTagStats fetchByPrimaryKey(long tagStatsId) {
		return fetchByPrimaryKey((Serializable)tagStatsId);
	}

	/**
	 * Returns all the asset tag statses.
	 *
	 * @return the asset tag statses
	 */
	@Override
	public List<AssetTagStats> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset tag statses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>AssetTagStatsModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of asset tag statses
	 * @param end the upper bound of the range of asset tag statses (not inclusive)
	 * @return the range of asset tag statses
	 */
	@Override
	public List<AssetTagStats> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset tag statses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>AssetTagStatsModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of asset tag statses
	 * @param end the upper bound of the range of asset tag statses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of asset tag statses
	 */
	@Override
	public List<AssetTagStats> findAll(
		int start, int end,
		OrderByComparator<AssetTagStats> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the asset tag statses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>AssetTagStatsModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of asset tag statses
	 * @param end the upper bound of the range of asset tag statses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of asset tag statses
	 */
	@Override
	public List<AssetTagStats> findAll(
		int start, int end, OrderByComparator<AssetTagStats> orderByComparator,
		boolean retrieveFromCache) {

		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			pagination = false;
			finderPath = _finderPathWithoutPaginationFindAll;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<AssetTagStats> list = null;

		if (retrieveFromCache) {
			list = (List<AssetTagStats>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_ASSETTAGSTATS);

				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_ASSETTAGSTATS;

				if (pagination) {
					sql = sql.concat(AssetTagStatsModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<AssetTagStats>)QueryUtil.list(
						q, getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<AssetTagStats>)QueryUtil.list(
						q, getDialect(), start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the asset tag statses from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (AssetTagStats assetTagStats : findAll()) {
			remove(assetTagStats);
		}
	}

	/**
	 * Returns the number of asset tag statses.
	 *
	 * @return the number of asset tag statses
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_ASSETTAGSTATS);

				count = (Long)q.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				finderCache.removeResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "tagStatsId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_ASSETTAGSTATS;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return AssetTagStatsModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the asset tag stats persistence.
	 */
	@Activate
	public void activate() {
		AssetTagStatsModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		AssetTagStatsModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_finderPathWithPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, AssetTagStatsImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, AssetTagStatsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathWithPaginationFindByTagId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, AssetTagStatsImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByTagId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByTagId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, AssetTagStatsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByTagId",
			new String[] {Long.class.getName()},
			AssetTagStatsModelImpl.TAGID_COLUMN_BITMASK |
			AssetTagStatsModelImpl.ASSETCOUNT_COLUMN_BITMASK);

		_finderPathCountByTagId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByTagId",
			new String[] {Long.class.getName()});

		_finderPathWithPaginationFindByClassNameId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, AssetTagStatsImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByClassNameId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByClassNameId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, AssetTagStatsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByClassNameId",
			new String[] {Long.class.getName()},
			AssetTagStatsModelImpl.CLASSNAMEID_COLUMN_BITMASK |
			AssetTagStatsModelImpl.ASSETCOUNT_COLUMN_BITMASK);

		_finderPathCountByClassNameId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByClassNameId",
			new String[] {Long.class.getName()});

		_finderPathFetchByT_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, AssetTagStatsImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByT_C",
			new String[] {Long.class.getName(), Long.class.getName()},
			AssetTagStatsModelImpl.TAGID_COLUMN_BITMASK |
			AssetTagStatsModelImpl.CLASSNAMEID_COLUMN_BITMASK);

		_finderPathCountByT_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByT_C",
			new String[] {Long.class.getName(), Long.class.getName()});
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(AssetTagStatsImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	@Reference(
		target = AssetPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.asset.tag.stats.model.AssetTagStats"),
			true);
	}

	@Override
	@Reference(
		target = AssetPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = AssetPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private boolean _columnBitmaskEnabled;

	@Reference(service = CompanyProviderWrapper.class)
	protected CompanyProvider companyProvider;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_ASSETTAGSTATS =
		"SELECT assetTagStats FROM AssetTagStats assetTagStats";

	private static final String _SQL_SELECT_ASSETTAGSTATS_WHERE =
		"SELECT assetTagStats FROM AssetTagStats assetTagStats WHERE ";

	private static final String _SQL_COUNT_ASSETTAGSTATS =
		"SELECT COUNT(assetTagStats) FROM AssetTagStats assetTagStats";

	private static final String _SQL_COUNT_ASSETTAGSTATS_WHERE =
		"SELECT COUNT(assetTagStats) FROM AssetTagStats assetTagStats WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "assetTagStats.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No AssetTagStats exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No AssetTagStats exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		AssetTagStatsPersistenceImpl.class);

}