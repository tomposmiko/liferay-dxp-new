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

package com.liferay.document.library.content.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.document.library.content.exception.NoSuchContentException;
import com.liferay.document.library.content.model.DLContent;
import com.liferay.document.library.content.model.impl.DLContentImpl;
import com.liferay.document.library.content.model.impl.DLContentModelImpl;
import com.liferay.document.library.content.service.persistence.DLContentPersistence;
import com.liferay.document.library.content.service.persistence.impl.constants.DLPersistenceConstants;
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
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the document library content service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = DLContentPersistence.class)
@ProviderType
public class DLContentPersistenceImpl
	extends BasePersistenceImpl<DLContent> implements DLContentPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>DLContentUtil</code> to access the document library content persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		DLContentImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByC_R;
	private FinderPath _finderPathWithoutPaginationFindByC_R;
	private FinderPath _finderPathCountByC_R;

	/**
	 * Returns all the document library contents where companyId = &#63; and repositoryId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @return the matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R(long companyId, long repositoryId) {
		return findByC_R(
			companyId, repositoryId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the document library contents where companyId = &#63; and repositoryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @return the range of matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R(
		long companyId, long repositoryId, int start, int end) {

		return findByC_R(companyId, repositoryId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the document library contents where companyId = &#63; and repositoryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R(
		long companyId, long repositoryId, int start, int end,
		OrderByComparator<DLContent> orderByComparator) {

		return findByC_R(
			companyId, repositoryId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the document library contents where companyId = &#63; and repositoryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R(
		long companyId, long repositoryId, int start, int end,
		OrderByComparator<DLContent> orderByComparator,
		boolean retrieveFromCache) {

		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			pagination = false;
			finderPath = _finderPathWithoutPaginationFindByC_R;
			finderArgs = new Object[] {companyId, repositoryId};
		}
		else {
			finderPath = _finderPathWithPaginationFindByC_R;
			finderArgs = new Object[] {
				companyId, repositoryId, start, end, orderByComparator
			};
		}

		List<DLContent> list = null;

		if (retrieveFromCache) {
			list = (List<DLContent>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DLContent dlContent : list) {
					if ((companyId != dlContent.getCompanyId()) ||
						(repositoryId != dlContent.getRepositoryId())) {

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
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_DLCONTENT_WHERE);

			query.append(_FINDER_COLUMN_C_R_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_R_REPOSITORYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else if (pagination) {
				query.append(DLContentModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(repositoryId);

				if (!pagination) {
					list = (List<DLContent>)QueryUtil.list(
						q, getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DLContent>)QueryUtil.list(
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
	 * Returns the first document library content in the ordered set where companyId = &#63; and repositoryId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching document library content
	 * @throws NoSuchContentException if a matching document library content could not be found
	 */
	@Override
	public DLContent findByC_R_First(
			long companyId, long repositoryId,
			OrderByComparator<DLContent> orderByComparator)
		throws NoSuchContentException {

		DLContent dlContent = fetchByC_R_First(
			companyId, repositoryId, orderByComparator);

		if (dlContent != null) {
			return dlContent;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", repositoryId=");
		msg.append(repositoryId);

		msg.append("}");

		throw new NoSuchContentException(msg.toString());
	}

	/**
	 * Returns the first document library content in the ordered set where companyId = &#63; and repositoryId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching document library content, or <code>null</code> if a matching document library content could not be found
	 */
	@Override
	public DLContent fetchByC_R_First(
		long companyId, long repositoryId,
		OrderByComparator<DLContent> orderByComparator) {

		List<DLContent> list = findByC_R(
			companyId, repositoryId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last document library content in the ordered set where companyId = &#63; and repositoryId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching document library content
	 * @throws NoSuchContentException if a matching document library content could not be found
	 */
	@Override
	public DLContent findByC_R_Last(
			long companyId, long repositoryId,
			OrderByComparator<DLContent> orderByComparator)
		throws NoSuchContentException {

		DLContent dlContent = fetchByC_R_Last(
			companyId, repositoryId, orderByComparator);

		if (dlContent != null) {
			return dlContent;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", repositoryId=");
		msg.append(repositoryId);

		msg.append("}");

		throw new NoSuchContentException(msg.toString());
	}

	/**
	 * Returns the last document library content in the ordered set where companyId = &#63; and repositoryId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching document library content, or <code>null</code> if a matching document library content could not be found
	 */
	@Override
	public DLContent fetchByC_R_Last(
		long companyId, long repositoryId,
		OrderByComparator<DLContent> orderByComparator) {

		int count = countByC_R(companyId, repositoryId);

		if (count == 0) {
			return null;
		}

		List<DLContent> list = findByC_R(
			companyId, repositoryId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the document library contents before and after the current document library content in the ordered set where companyId = &#63; and repositoryId = &#63;.
	 *
	 * @param contentId the primary key of the current document library content
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next document library content
	 * @throws NoSuchContentException if a document library content with the primary key could not be found
	 */
	@Override
	public DLContent[] findByC_R_PrevAndNext(
			long contentId, long companyId, long repositoryId,
			OrderByComparator<DLContent> orderByComparator)
		throws NoSuchContentException {

		DLContent dlContent = findByPrimaryKey(contentId);

		Session session = null;

		try {
			session = openSession();

			DLContent[] array = new DLContentImpl[3];

			array[0] = getByC_R_PrevAndNext(
				session, dlContent, companyId, repositoryId, orderByComparator,
				true);

			array[1] = dlContent;

			array[2] = getByC_R_PrevAndNext(
				session, dlContent, companyId, repositoryId, orderByComparator,
				false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DLContent getByC_R_PrevAndNext(
		Session session, DLContent dlContent, long companyId, long repositoryId,
		OrderByComparator<DLContent> orderByComparator, boolean previous) {

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(4);
		}

		query.append(_SQL_SELECT_DLCONTENT_WHERE);

		query.append(_FINDER_COLUMN_C_R_COMPANYID_2);

		query.append(_FINDER_COLUMN_C_R_REPOSITORYID_2);

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
			query.append(DLContentModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		qPos.add(repositoryId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(dlContent)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<DLContent> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the document library contents where companyId = &#63; and repositoryId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 */
	@Override
	public void removeByC_R(long companyId, long repositoryId) {
		for (DLContent dlContent :
				findByC_R(
					companyId, repositoryId, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(dlContent);
		}
	}

	/**
	 * Returns the number of document library contents where companyId = &#63; and repositoryId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @return the number of matching document library contents
	 */
	@Override
	public int countByC_R(long companyId, long repositoryId) {
		FinderPath finderPath = _finderPathCountByC_R;

		Object[] finderArgs = new Object[] {companyId, repositoryId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_DLCONTENT_WHERE);

			query.append(_FINDER_COLUMN_C_R_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_R_REPOSITORYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(repositoryId);

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

	private static final String _FINDER_COLUMN_C_R_COMPANYID_2 =
		"dlContent.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_R_REPOSITORYID_2 =
		"dlContent.repositoryId = ?";

	private FinderPath _finderPathWithPaginationFindByC_R_P;
	private FinderPath _finderPathWithoutPaginationFindByC_R_P;
	private FinderPath _finderPathCountByC_R_P;

	/**
	 * Returns all the document library contents where companyId = &#63; and repositoryId = &#63; and path = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @return the matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R_P(
		long companyId, long repositoryId, String path) {

		return findByC_R_P(
			companyId, repositoryId, path, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the document library contents where companyId = &#63; and repositoryId = &#63; and path = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @return the range of matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R_P(
		long companyId, long repositoryId, String path, int start, int end) {

		return findByC_R_P(companyId, repositoryId, path, start, end, null);
	}

	/**
	 * Returns an ordered range of all the document library contents where companyId = &#63; and repositoryId = &#63; and path = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R_P(
		long companyId, long repositoryId, String path, int start, int end,
		OrderByComparator<DLContent> orderByComparator) {

		return findByC_R_P(
			companyId, repositoryId, path, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the document library contents where companyId = &#63; and repositoryId = &#63; and path = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R_P(
		long companyId, long repositoryId, String path, int start, int end,
		OrderByComparator<DLContent> orderByComparator,
		boolean retrieveFromCache) {

		path = Objects.toString(path, "");

		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			pagination = false;
			finderPath = _finderPathWithoutPaginationFindByC_R_P;
			finderArgs = new Object[] {companyId, repositoryId, path};
		}
		else {
			finderPath = _finderPathWithPaginationFindByC_R_P;
			finderArgs = new Object[] {
				companyId, repositoryId, path, start, end, orderByComparator
			};
		}

		List<DLContent> list = null;

		if (retrieveFromCache) {
			list = (List<DLContent>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DLContent dlContent : list) {
					if ((companyId != dlContent.getCompanyId()) ||
						(repositoryId != dlContent.getRepositoryId()) ||
						!path.equals(dlContent.getPath())) {

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
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_DLCONTENT_WHERE);

			query.append(_FINDER_COLUMN_C_R_P_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_R_P_REPOSITORYID_2);

			boolean bindPath = false;

			if (path.isEmpty()) {
				query.append(_FINDER_COLUMN_C_R_P_PATH_3);
			}
			else {
				bindPath = true;

				query.append(_FINDER_COLUMN_C_R_P_PATH_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else if (pagination) {
				query.append(DLContentModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(repositoryId);

				if (bindPath) {
					qPos.add(path);
				}

				if (!pagination) {
					list = (List<DLContent>)QueryUtil.list(
						q, getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DLContent>)QueryUtil.list(
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
	 * Returns the first document library content in the ordered set where companyId = &#63; and repositoryId = &#63; and path = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching document library content
	 * @throws NoSuchContentException if a matching document library content could not be found
	 */
	@Override
	public DLContent findByC_R_P_First(
			long companyId, long repositoryId, String path,
			OrderByComparator<DLContent> orderByComparator)
		throws NoSuchContentException {

		DLContent dlContent = fetchByC_R_P_First(
			companyId, repositoryId, path, orderByComparator);

		if (dlContent != null) {
			return dlContent;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", repositoryId=");
		msg.append(repositoryId);

		msg.append(", path=");
		msg.append(path);

		msg.append("}");

		throw new NoSuchContentException(msg.toString());
	}

	/**
	 * Returns the first document library content in the ordered set where companyId = &#63; and repositoryId = &#63; and path = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching document library content, or <code>null</code> if a matching document library content could not be found
	 */
	@Override
	public DLContent fetchByC_R_P_First(
		long companyId, long repositoryId, String path,
		OrderByComparator<DLContent> orderByComparator) {

		List<DLContent> list = findByC_R_P(
			companyId, repositoryId, path, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last document library content in the ordered set where companyId = &#63; and repositoryId = &#63; and path = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching document library content
	 * @throws NoSuchContentException if a matching document library content could not be found
	 */
	@Override
	public DLContent findByC_R_P_Last(
			long companyId, long repositoryId, String path,
			OrderByComparator<DLContent> orderByComparator)
		throws NoSuchContentException {

		DLContent dlContent = fetchByC_R_P_Last(
			companyId, repositoryId, path, orderByComparator);

		if (dlContent != null) {
			return dlContent;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", repositoryId=");
		msg.append(repositoryId);

		msg.append(", path=");
		msg.append(path);

		msg.append("}");

		throw new NoSuchContentException(msg.toString());
	}

	/**
	 * Returns the last document library content in the ordered set where companyId = &#63; and repositoryId = &#63; and path = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching document library content, or <code>null</code> if a matching document library content could not be found
	 */
	@Override
	public DLContent fetchByC_R_P_Last(
		long companyId, long repositoryId, String path,
		OrderByComparator<DLContent> orderByComparator) {

		int count = countByC_R_P(companyId, repositoryId, path);

		if (count == 0) {
			return null;
		}

		List<DLContent> list = findByC_R_P(
			companyId, repositoryId, path, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the document library contents before and after the current document library content in the ordered set where companyId = &#63; and repositoryId = &#63; and path = &#63;.
	 *
	 * @param contentId the primary key of the current document library content
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next document library content
	 * @throws NoSuchContentException if a document library content with the primary key could not be found
	 */
	@Override
	public DLContent[] findByC_R_P_PrevAndNext(
			long contentId, long companyId, long repositoryId, String path,
			OrderByComparator<DLContent> orderByComparator)
		throws NoSuchContentException {

		path = Objects.toString(path, "");

		DLContent dlContent = findByPrimaryKey(contentId);

		Session session = null;

		try {
			session = openSession();

			DLContent[] array = new DLContentImpl[3];

			array[0] = getByC_R_P_PrevAndNext(
				session, dlContent, companyId, repositoryId, path,
				orderByComparator, true);

			array[1] = dlContent;

			array[2] = getByC_R_P_PrevAndNext(
				session, dlContent, companyId, repositoryId, path,
				orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DLContent getByC_R_P_PrevAndNext(
		Session session, DLContent dlContent, long companyId, long repositoryId,
		String path, OrderByComparator<DLContent> orderByComparator,
		boolean previous) {

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(5);
		}

		query.append(_SQL_SELECT_DLCONTENT_WHERE);

		query.append(_FINDER_COLUMN_C_R_P_COMPANYID_2);

		query.append(_FINDER_COLUMN_C_R_P_REPOSITORYID_2);

		boolean bindPath = false;

		if (path.isEmpty()) {
			query.append(_FINDER_COLUMN_C_R_P_PATH_3);
		}
		else {
			bindPath = true;

			query.append(_FINDER_COLUMN_C_R_P_PATH_2);
		}

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
			query.append(DLContentModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		qPos.add(repositoryId);

		if (bindPath) {
			qPos.add(path);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(dlContent)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<DLContent> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the document library contents where companyId = &#63; and repositoryId = &#63; and path = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 */
	@Override
	public void removeByC_R_P(long companyId, long repositoryId, String path) {
		for (DLContent dlContent :
				findByC_R_P(
					companyId, repositoryId, path, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(dlContent);
		}
	}

	/**
	 * Returns the number of document library contents where companyId = &#63; and repositoryId = &#63; and path = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @return the number of matching document library contents
	 */
	@Override
	public int countByC_R_P(long companyId, long repositoryId, String path) {
		path = Objects.toString(path, "");

		FinderPath finderPath = _finderPathCountByC_R_P;

		Object[] finderArgs = new Object[] {companyId, repositoryId, path};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_DLCONTENT_WHERE);

			query.append(_FINDER_COLUMN_C_R_P_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_R_P_REPOSITORYID_2);

			boolean bindPath = false;

			if (path.isEmpty()) {
				query.append(_FINDER_COLUMN_C_R_P_PATH_3);
			}
			else {
				bindPath = true;

				query.append(_FINDER_COLUMN_C_R_P_PATH_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(repositoryId);

				if (bindPath) {
					qPos.add(path);
				}

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

	private static final String _FINDER_COLUMN_C_R_P_COMPANYID_2 =
		"dlContent.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_R_P_REPOSITORYID_2 =
		"dlContent.repositoryId = ? AND ";

	private static final String _FINDER_COLUMN_C_R_P_PATH_2 =
		"dlContent.path = ?";

	private static final String _FINDER_COLUMN_C_R_P_PATH_3 =
		"(dlContent.path IS NULL OR dlContent.path = '')";

	private FinderPath _finderPathWithPaginationFindByC_R_LikeP;
	private FinderPath _finderPathWithPaginationCountByC_R_LikeP;

	/**
	 * Returns all the document library contents where companyId = &#63; and repositoryId = &#63; and path LIKE &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @return the matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R_LikeP(
		long companyId, long repositoryId, String path) {

		return findByC_R_LikeP(
			companyId, repositoryId, path, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the document library contents where companyId = &#63; and repositoryId = &#63; and path LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @return the range of matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R_LikeP(
		long companyId, long repositoryId, String path, int start, int end) {

		return findByC_R_LikeP(companyId, repositoryId, path, start, end, null);
	}

	/**
	 * Returns an ordered range of all the document library contents where companyId = &#63; and repositoryId = &#63; and path LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R_LikeP(
		long companyId, long repositoryId, String path, int start, int end,
		OrderByComparator<DLContent> orderByComparator) {

		return findByC_R_LikeP(
			companyId, repositoryId, path, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the document library contents where companyId = &#63; and repositoryId = &#63; and path LIKE &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching document library contents
	 */
	@Override
	public List<DLContent> findByC_R_LikeP(
		long companyId, long repositoryId, String path, int start, int end,
		OrderByComparator<DLContent> orderByComparator,
		boolean retrieveFromCache) {

		path = Objects.toString(path, "");

		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = _finderPathWithPaginationFindByC_R_LikeP;
		finderArgs = new Object[] {
			companyId, repositoryId, path, start, end, orderByComparator
		};

		List<DLContent> list = null;

		if (retrieveFromCache) {
			list = (List<DLContent>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DLContent dlContent : list) {
					if ((companyId != dlContent.getCompanyId()) ||
						(repositoryId != dlContent.getRepositoryId()) ||
						!StringUtil.wildcardMatches(
							dlContent.getPath(), path, '_', '%', '\\', true)) {

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
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_DLCONTENT_WHERE);

			query.append(_FINDER_COLUMN_C_R_LIKEP_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_R_LIKEP_REPOSITORYID_2);

			boolean bindPath = false;

			if (path.isEmpty()) {
				query.append(_FINDER_COLUMN_C_R_LIKEP_PATH_3);
			}
			else {
				bindPath = true;

				query.append(_FINDER_COLUMN_C_R_LIKEP_PATH_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else if (pagination) {
				query.append(DLContentModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(repositoryId);

				if (bindPath) {
					qPos.add(path);
				}

				if (!pagination) {
					list = (List<DLContent>)QueryUtil.list(
						q, getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DLContent>)QueryUtil.list(
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
	 * Returns the first document library content in the ordered set where companyId = &#63; and repositoryId = &#63; and path LIKE &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching document library content
	 * @throws NoSuchContentException if a matching document library content could not be found
	 */
	@Override
	public DLContent findByC_R_LikeP_First(
			long companyId, long repositoryId, String path,
			OrderByComparator<DLContent> orderByComparator)
		throws NoSuchContentException {

		DLContent dlContent = fetchByC_R_LikeP_First(
			companyId, repositoryId, path, orderByComparator);

		if (dlContent != null) {
			return dlContent;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", repositoryId=");
		msg.append(repositoryId);

		msg.append(", path=");
		msg.append(path);

		msg.append("}");

		throw new NoSuchContentException(msg.toString());
	}

	/**
	 * Returns the first document library content in the ordered set where companyId = &#63; and repositoryId = &#63; and path LIKE &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching document library content, or <code>null</code> if a matching document library content could not be found
	 */
	@Override
	public DLContent fetchByC_R_LikeP_First(
		long companyId, long repositoryId, String path,
		OrderByComparator<DLContent> orderByComparator) {

		List<DLContent> list = findByC_R_LikeP(
			companyId, repositoryId, path, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last document library content in the ordered set where companyId = &#63; and repositoryId = &#63; and path LIKE &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching document library content
	 * @throws NoSuchContentException if a matching document library content could not be found
	 */
	@Override
	public DLContent findByC_R_LikeP_Last(
			long companyId, long repositoryId, String path,
			OrderByComparator<DLContent> orderByComparator)
		throws NoSuchContentException {

		DLContent dlContent = fetchByC_R_LikeP_Last(
			companyId, repositoryId, path, orderByComparator);

		if (dlContent != null) {
			return dlContent;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(", repositoryId=");
		msg.append(repositoryId);

		msg.append(", path=");
		msg.append(path);

		msg.append("}");

		throw new NoSuchContentException(msg.toString());
	}

	/**
	 * Returns the last document library content in the ordered set where companyId = &#63; and repositoryId = &#63; and path LIKE &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching document library content, or <code>null</code> if a matching document library content could not be found
	 */
	@Override
	public DLContent fetchByC_R_LikeP_Last(
		long companyId, long repositoryId, String path,
		OrderByComparator<DLContent> orderByComparator) {

		int count = countByC_R_LikeP(companyId, repositoryId, path);

		if (count == 0) {
			return null;
		}

		List<DLContent> list = findByC_R_LikeP(
			companyId, repositoryId, path, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the document library contents before and after the current document library content in the ordered set where companyId = &#63; and repositoryId = &#63; and path LIKE &#63;.
	 *
	 * @param contentId the primary key of the current document library content
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next document library content
	 * @throws NoSuchContentException if a document library content with the primary key could not be found
	 */
	@Override
	public DLContent[] findByC_R_LikeP_PrevAndNext(
			long contentId, long companyId, long repositoryId, String path,
			OrderByComparator<DLContent> orderByComparator)
		throws NoSuchContentException {

		path = Objects.toString(path, "");

		DLContent dlContent = findByPrimaryKey(contentId);

		Session session = null;

		try {
			session = openSession();

			DLContent[] array = new DLContentImpl[3];

			array[0] = getByC_R_LikeP_PrevAndNext(
				session, dlContent, companyId, repositoryId, path,
				orderByComparator, true);

			array[1] = dlContent;

			array[2] = getByC_R_LikeP_PrevAndNext(
				session, dlContent, companyId, repositoryId, path,
				orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DLContent getByC_R_LikeP_PrevAndNext(
		Session session, DLContent dlContent, long companyId, long repositoryId,
		String path, OrderByComparator<DLContent> orderByComparator,
		boolean previous) {

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(5);
		}

		query.append(_SQL_SELECT_DLCONTENT_WHERE);

		query.append(_FINDER_COLUMN_C_R_LIKEP_COMPANYID_2);

		query.append(_FINDER_COLUMN_C_R_LIKEP_REPOSITORYID_2);

		boolean bindPath = false;

		if (path.isEmpty()) {
			query.append(_FINDER_COLUMN_C_R_LIKEP_PATH_3);
		}
		else {
			bindPath = true;

			query.append(_FINDER_COLUMN_C_R_LIKEP_PATH_2);
		}

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
			query.append(DLContentModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		qPos.add(repositoryId);

		if (bindPath) {
			qPos.add(path);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(dlContent)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<DLContent> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the document library contents where companyId = &#63; and repositoryId = &#63; and path LIKE &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 */
	@Override
	public void removeByC_R_LikeP(
		long companyId, long repositoryId, String path) {

		for (DLContent dlContent :
				findByC_R_LikeP(
					companyId, repositoryId, path, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(dlContent);
		}
	}

	/**
	 * Returns the number of document library contents where companyId = &#63; and repositoryId = &#63; and path LIKE &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @return the number of matching document library contents
	 */
	@Override
	public int countByC_R_LikeP(
		long companyId, long repositoryId, String path) {

		path = Objects.toString(path, "");

		FinderPath finderPath = _finderPathWithPaginationCountByC_R_LikeP;

		Object[] finderArgs = new Object[] {companyId, repositoryId, path};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_DLCONTENT_WHERE);

			query.append(_FINDER_COLUMN_C_R_LIKEP_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_R_LIKEP_REPOSITORYID_2);

			boolean bindPath = false;

			if (path.isEmpty()) {
				query.append(_FINDER_COLUMN_C_R_LIKEP_PATH_3);
			}
			else {
				bindPath = true;

				query.append(_FINDER_COLUMN_C_R_LIKEP_PATH_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(repositoryId);

				if (bindPath) {
					qPos.add(path);
				}

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

	private static final String _FINDER_COLUMN_C_R_LIKEP_COMPANYID_2 =
		"dlContent.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_R_LIKEP_REPOSITORYID_2 =
		"dlContent.repositoryId = ? AND ";

	private static final String _FINDER_COLUMN_C_R_LIKEP_PATH_2 =
		"dlContent.path LIKE ?";

	private static final String _FINDER_COLUMN_C_R_LIKEP_PATH_3 =
		"(dlContent.path IS NULL OR dlContent.path LIKE '')";

	private FinderPath _finderPathFetchByC_R_P_V;
	private FinderPath _finderPathCountByC_R_P_V;

	/**
	 * Returns the document library content where companyId = &#63; and repositoryId = &#63; and path = &#63; and version = &#63; or throws a <code>NoSuchContentException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param version the version
	 * @return the matching document library content
	 * @throws NoSuchContentException if a matching document library content could not be found
	 */
	@Override
	public DLContent findByC_R_P_V(
			long companyId, long repositoryId, String path, String version)
		throws NoSuchContentException {

		DLContent dlContent = fetchByC_R_P_V(
			companyId, repositoryId, path, version);

		if (dlContent == null) {
			StringBundler msg = new StringBundler(10);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("companyId=");
			msg.append(companyId);

			msg.append(", repositoryId=");
			msg.append(repositoryId);

			msg.append(", path=");
			msg.append(path);

			msg.append(", version=");
			msg.append(version);

			msg.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(msg.toString());
			}

			throw new NoSuchContentException(msg.toString());
		}

		return dlContent;
	}

	/**
	 * Returns the document library content where companyId = &#63; and repositoryId = &#63; and path = &#63; and version = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param version the version
	 * @return the matching document library content, or <code>null</code> if a matching document library content could not be found
	 */
	@Override
	public DLContent fetchByC_R_P_V(
		long companyId, long repositoryId, String path, String version) {

		return fetchByC_R_P_V(companyId, repositoryId, path, version, true);
	}

	/**
	 * Returns the document library content where companyId = &#63; and repositoryId = &#63; and path = &#63; and version = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param version the version
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the matching document library content, or <code>null</code> if a matching document library content could not be found
	 */
	@Override
	public DLContent fetchByC_R_P_V(
		long companyId, long repositoryId, String path, String version,
		boolean retrieveFromCache) {

		path = Objects.toString(path, "");
		version = Objects.toString(version, "");

		Object[] finderArgs = new Object[] {
			companyId, repositoryId, path, version
		};

		Object result = null;

		if (retrieveFromCache) {
			result = finderCache.getResult(
				_finderPathFetchByC_R_P_V, finderArgs, this);
		}

		if (result instanceof DLContent) {
			DLContent dlContent = (DLContent)result;

			if ((companyId != dlContent.getCompanyId()) ||
				(repositoryId != dlContent.getRepositoryId()) ||
				!Objects.equals(path, dlContent.getPath()) ||
				!Objects.equals(version, dlContent.getVersion())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(6);

			query.append(_SQL_SELECT_DLCONTENT_WHERE);

			query.append(_FINDER_COLUMN_C_R_P_V_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_R_P_V_REPOSITORYID_2);

			boolean bindPath = false;

			if (path.isEmpty()) {
				query.append(_FINDER_COLUMN_C_R_P_V_PATH_3);
			}
			else {
				bindPath = true;

				query.append(_FINDER_COLUMN_C_R_P_V_PATH_2);
			}

			boolean bindVersion = false;

			if (version.isEmpty()) {
				query.append(_FINDER_COLUMN_C_R_P_V_VERSION_3);
			}
			else {
				bindVersion = true;

				query.append(_FINDER_COLUMN_C_R_P_V_VERSION_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(repositoryId);

				if (bindPath) {
					qPos.add(path);
				}

				if (bindVersion) {
					qPos.add(version);
				}

				List<DLContent> list = q.list();

				if (list.isEmpty()) {
					finderCache.putResult(
						_finderPathFetchByC_R_P_V, finderArgs, list);
				}
				else {
					DLContent dlContent = list.get(0);

					result = dlContent;

					cacheResult(dlContent);
				}
			}
			catch (Exception e) {
				finderCache.removeResult(_finderPathFetchByC_R_P_V, finderArgs);

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
			return (DLContent)result;
		}
	}

	/**
	 * Removes the document library content where companyId = &#63; and repositoryId = &#63; and path = &#63; and version = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param version the version
	 * @return the document library content that was removed
	 */
	@Override
	public DLContent removeByC_R_P_V(
			long companyId, long repositoryId, String path, String version)
		throws NoSuchContentException {

		DLContent dlContent = findByC_R_P_V(
			companyId, repositoryId, path, version);

		return remove(dlContent);
	}

	/**
	 * Returns the number of document library contents where companyId = &#63; and repositoryId = &#63; and path = &#63; and version = &#63;.
	 *
	 * @param companyId the company ID
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param version the version
	 * @return the number of matching document library contents
	 */
	@Override
	public int countByC_R_P_V(
		long companyId, long repositoryId, String path, String version) {

		path = Objects.toString(path, "");
		version = Objects.toString(version, "");

		FinderPath finderPath = _finderPathCountByC_R_P_V;

		Object[] finderArgs = new Object[] {
			companyId, repositoryId, path, version
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_COUNT_DLCONTENT_WHERE);

			query.append(_FINDER_COLUMN_C_R_P_V_COMPANYID_2);

			query.append(_FINDER_COLUMN_C_R_P_V_REPOSITORYID_2);

			boolean bindPath = false;

			if (path.isEmpty()) {
				query.append(_FINDER_COLUMN_C_R_P_V_PATH_3);
			}
			else {
				bindPath = true;

				query.append(_FINDER_COLUMN_C_R_P_V_PATH_2);
			}

			boolean bindVersion = false;

			if (version.isEmpty()) {
				query.append(_FINDER_COLUMN_C_R_P_V_VERSION_3);
			}
			else {
				bindVersion = true;

				query.append(_FINDER_COLUMN_C_R_P_V_VERSION_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				qPos.add(repositoryId);

				if (bindPath) {
					qPos.add(path);
				}

				if (bindVersion) {
					qPos.add(version);
				}

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

	private static final String _FINDER_COLUMN_C_R_P_V_COMPANYID_2 =
		"dlContent.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_R_P_V_REPOSITORYID_2 =
		"dlContent.repositoryId = ? AND ";

	private static final String _FINDER_COLUMN_C_R_P_V_PATH_2 =
		"dlContent.path = ? AND ";

	private static final String _FINDER_COLUMN_C_R_P_V_PATH_3 =
		"(dlContent.path IS NULL OR dlContent.path = '') AND ";

	private static final String _FINDER_COLUMN_C_R_P_V_VERSION_2 =
		"dlContent.version = ?";

	private static final String _FINDER_COLUMN_C_R_P_V_VERSION_3 =
		"(dlContent.version IS NULL OR dlContent.version = '')";

	public DLContentPersistenceImpl() {
		setModelClass(DLContent.class);

		setModelImplClass(DLContentImpl.class);
		setModelPKClass(long.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("path", "path_");
		dbColumnNames.put("data", "data_");
		dbColumnNames.put("size", "size_");

		setDBColumnNames(dbColumnNames);
	}

	/**
	 * Caches the document library content in the entity cache if it is enabled.
	 *
	 * @param dlContent the document library content
	 */
	@Override
	public void cacheResult(DLContent dlContent) {
		entityCache.putResult(
			entityCacheEnabled, DLContentImpl.class, dlContent.getPrimaryKey(),
			dlContent);

		finderCache.putResult(
			_finderPathFetchByC_R_P_V,
			new Object[] {
				dlContent.getCompanyId(), dlContent.getRepositoryId(),
				dlContent.getPath(), dlContent.getVersion()
			},
			dlContent);

		dlContent.resetOriginalValues();
	}

	/**
	 * Caches the document library contents in the entity cache if it is enabled.
	 *
	 * @param dlContents the document library contents
	 */
	@Override
	public void cacheResult(List<DLContent> dlContents) {
		for (DLContent dlContent : dlContents) {
			if (entityCache.getResult(
					entityCacheEnabled, DLContentImpl.class,
					dlContent.getPrimaryKey()) == null) {

				cacheResult(dlContent);
			}
			else {
				dlContent.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all document library contents.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(DLContentImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the document library content.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(DLContent dlContent) {
		entityCache.removeResult(
			entityCacheEnabled, DLContentImpl.class, dlContent.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache((DLContentModelImpl)dlContent, true);
	}

	@Override
	public void clearCache(List<DLContent> dlContents) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (DLContent dlContent : dlContents) {
			entityCache.removeResult(
				entityCacheEnabled, DLContentImpl.class,
				dlContent.getPrimaryKey());

			clearUniqueFindersCache((DLContentModelImpl)dlContent, true);
		}
	}

	protected void cacheUniqueFindersCache(
		DLContentModelImpl dlContentModelImpl) {

		Object[] args = new Object[] {
			dlContentModelImpl.getCompanyId(),
			dlContentModelImpl.getRepositoryId(), dlContentModelImpl.getPath(),
			dlContentModelImpl.getVersion()
		};

		finderCache.putResult(
			_finderPathCountByC_R_P_V, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByC_R_P_V, args, dlContentModelImpl, false);
	}

	protected void clearUniqueFindersCache(
		DLContentModelImpl dlContentModelImpl, boolean clearCurrent) {

		if (clearCurrent) {
			Object[] args = new Object[] {
				dlContentModelImpl.getCompanyId(),
				dlContentModelImpl.getRepositoryId(),
				dlContentModelImpl.getPath(), dlContentModelImpl.getVersion()
			};

			finderCache.removeResult(_finderPathCountByC_R_P_V, args);
			finderCache.removeResult(_finderPathFetchByC_R_P_V, args);
		}

		if ((dlContentModelImpl.getColumnBitmask() &
			 _finderPathFetchByC_R_P_V.getColumnBitmask()) != 0) {

			Object[] args = new Object[] {
				dlContentModelImpl.getOriginalCompanyId(),
				dlContentModelImpl.getOriginalRepositoryId(),
				dlContentModelImpl.getOriginalPath(),
				dlContentModelImpl.getOriginalVersion()
			};

			finderCache.removeResult(_finderPathCountByC_R_P_V, args);
			finderCache.removeResult(_finderPathFetchByC_R_P_V, args);
		}
	}

	/**
	 * Creates a new document library content with the primary key. Does not add the document library content to the database.
	 *
	 * @param contentId the primary key for the new document library content
	 * @return the new document library content
	 */
	@Override
	public DLContent create(long contentId) {
		DLContent dlContent = new DLContentImpl();

		dlContent.setNew(true);
		dlContent.setPrimaryKey(contentId);

		dlContent.setCompanyId(companyProvider.getCompanyId());

		return dlContent;
	}

	/**
	 * Removes the document library content with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param contentId the primary key of the document library content
	 * @return the document library content that was removed
	 * @throws NoSuchContentException if a document library content with the primary key could not be found
	 */
	@Override
	public DLContent remove(long contentId) throws NoSuchContentException {
		return remove((Serializable)contentId);
	}

	/**
	 * Removes the document library content with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the document library content
	 * @return the document library content that was removed
	 * @throws NoSuchContentException if a document library content with the primary key could not be found
	 */
	@Override
	public DLContent remove(Serializable primaryKey)
		throws NoSuchContentException {

		Session session = null;

		try {
			session = openSession();

			DLContent dlContent = (DLContent)session.get(
				DLContentImpl.class, primaryKey);

			if (dlContent == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchContentException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(dlContent);
		}
		catch (NoSuchContentException nsee) {
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
	protected DLContent removeImpl(DLContent dlContent) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(dlContent)) {
				dlContent = (DLContent)session.get(
					DLContentImpl.class, dlContent.getPrimaryKeyObj());
			}

			if (dlContent != null) {
				session.delete(dlContent);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (dlContent != null) {
			clearCache(dlContent);
		}

		return dlContent;
	}

	@Override
	public DLContent updateImpl(DLContent dlContent) {
		boolean isNew = dlContent.isNew();

		if (!(dlContent instanceof DLContentModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(dlContent.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(dlContent);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in dlContent proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom DLContent implementation " +
					dlContent.getClass());
		}

		DLContentModelImpl dlContentModelImpl = (DLContentModelImpl)dlContent;

		Session session = null;

		try {
			session = openSession();

			if (dlContent.isNew()) {
				session.save(dlContent);

				dlContent.setNew(false);
			}
			else {
				session.evict(dlContent);
				session.saveOrUpdate(dlContent);
			}

			session.flush();
			session.clear();
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
			Object[] args = new Object[] {
				dlContentModelImpl.getCompanyId(),
				dlContentModelImpl.getRepositoryId()
			};

			finderCache.removeResult(_finderPathCountByC_R, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByC_R, args);

			args = new Object[] {
				dlContentModelImpl.getCompanyId(),
				dlContentModelImpl.getRepositoryId(),
				dlContentModelImpl.getPath()
			};

			finderCache.removeResult(_finderPathCountByC_R_P, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByC_R_P, args);

			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}
		else {
			if ((dlContentModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByC_R.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					dlContentModelImpl.getOriginalCompanyId(),
					dlContentModelImpl.getOriginalRepositoryId()
				};

				finderCache.removeResult(_finderPathCountByC_R, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByC_R, args);

				args = new Object[] {
					dlContentModelImpl.getCompanyId(),
					dlContentModelImpl.getRepositoryId()
				};

				finderCache.removeResult(_finderPathCountByC_R, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByC_R, args);
			}

			if ((dlContentModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByC_R_P.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					dlContentModelImpl.getOriginalCompanyId(),
					dlContentModelImpl.getOriginalRepositoryId(),
					dlContentModelImpl.getOriginalPath()
				};

				finderCache.removeResult(_finderPathCountByC_R_P, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByC_R_P, args);

				args = new Object[] {
					dlContentModelImpl.getCompanyId(),
					dlContentModelImpl.getRepositoryId(),
					dlContentModelImpl.getPath()
				};

				finderCache.removeResult(_finderPathCountByC_R_P, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByC_R_P, args);
			}
		}

		entityCache.putResult(
			entityCacheEnabled, DLContentImpl.class, dlContent.getPrimaryKey(),
			dlContent, false);

		clearUniqueFindersCache(dlContentModelImpl, false);
		cacheUniqueFindersCache(dlContentModelImpl);

		dlContent.resetOriginalValues();

		return dlContent;
	}

	/**
	 * Returns the document library content with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the document library content
	 * @return the document library content
	 * @throws NoSuchContentException if a document library content with the primary key could not be found
	 */
	@Override
	public DLContent findByPrimaryKey(Serializable primaryKey)
		throws NoSuchContentException {

		DLContent dlContent = fetchByPrimaryKey(primaryKey);

		if (dlContent == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchContentException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return dlContent;
	}

	/**
	 * Returns the document library content with the primary key or throws a <code>NoSuchContentException</code> if it could not be found.
	 *
	 * @param contentId the primary key of the document library content
	 * @return the document library content
	 * @throws NoSuchContentException if a document library content with the primary key could not be found
	 */
	@Override
	public DLContent findByPrimaryKey(long contentId)
		throws NoSuchContentException {

		return findByPrimaryKey((Serializable)contentId);
	}

	/**
	 * Returns the document library content with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param contentId the primary key of the document library content
	 * @return the document library content, or <code>null</code> if a document library content with the primary key could not be found
	 */
	@Override
	public DLContent fetchByPrimaryKey(long contentId) {
		return fetchByPrimaryKey((Serializable)contentId);
	}

	/**
	 * Returns all the document library contents.
	 *
	 * @return the document library contents
	 */
	@Override
	public List<DLContent> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the document library contents.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @return the range of document library contents
	 */
	@Override
	public List<DLContent> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the document library contents.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of document library contents
	 */
	@Override
	public List<DLContent> findAll(
		int start, int end, OrderByComparator<DLContent> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the document library contents.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>DLContentModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of document library contents
	 * @param end the upper bound of the range of document library contents (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of document library contents
	 */
	@Override
	public List<DLContent> findAll(
		int start, int end, OrderByComparator<DLContent> orderByComparator,
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

		List<DLContent> list = null;

		if (retrieveFromCache) {
			list = (List<DLContent>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_DLCONTENT);

				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_DLCONTENT;

				if (pagination) {
					sql = sql.concat(DLContentModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<DLContent>)QueryUtil.list(
						q, getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DLContent>)QueryUtil.list(
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
	 * Removes all the document library contents from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (DLContent dlContent : findAll()) {
			remove(dlContent);
		}
	}

	/**
	 * Returns the number of document library contents.
	 *
	 * @return the number of document library contents
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_DLCONTENT);

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
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "contentId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_DLCONTENT;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return DLContentModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the document library content persistence.
	 */
	@Activate
	public void activate() {
		DLContentModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		DLContentModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_finderPathWithPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, DLContentImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, DLContentImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathWithPaginationFindByC_R = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, DLContentImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByC_R",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByC_R = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, DLContentImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByC_R",
			new String[] {Long.class.getName(), Long.class.getName()},
			DLContentModelImpl.COMPANYID_COLUMN_BITMASK |
			DLContentModelImpl.REPOSITORYID_COLUMN_BITMASK |
			DLContentModelImpl.VERSION_COLUMN_BITMASK);

		_finderPathCountByC_R = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_R",
			new String[] {Long.class.getName(), Long.class.getName()});

		_finderPathWithPaginationFindByC_R_P = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, DLContentImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByC_R_P",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByC_R_P = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, DLContentImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByC_R_P",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName()
			},
			DLContentModelImpl.COMPANYID_COLUMN_BITMASK |
			DLContentModelImpl.REPOSITORYID_COLUMN_BITMASK |
			DLContentModelImpl.PATH_COLUMN_BITMASK |
			DLContentModelImpl.VERSION_COLUMN_BITMASK);

		_finderPathCountByC_R_P = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_R_P",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName()
			});

		_finderPathWithPaginationFindByC_R_LikeP = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, DLContentImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByC_R_LikeP",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithPaginationCountByC_R_LikeP = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByC_R_LikeP",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName()
			});

		_finderPathFetchByC_R_P_V = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, DLContentImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByC_R_P_V",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName(), String.class.getName()
			},
			DLContentModelImpl.COMPANYID_COLUMN_BITMASK |
			DLContentModelImpl.REPOSITORYID_COLUMN_BITMASK |
			DLContentModelImpl.PATH_COLUMN_BITMASK |
			DLContentModelImpl.VERSION_COLUMN_BITMASK);

		_finderPathCountByC_R_P_V = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_R_P_V",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName(), String.class.getName()
			});
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(DLContentImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	@Reference(
		target = DLPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.document.library.content.model.DLContent"),
			true);
	}

	@Override
	@Reference(
		target = DLPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = DLPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
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

	private static final String _SQL_SELECT_DLCONTENT =
		"SELECT dlContent FROM DLContent dlContent";

	private static final String _SQL_SELECT_DLCONTENT_WHERE =
		"SELECT dlContent FROM DLContent dlContent WHERE ";

	private static final String _SQL_COUNT_DLCONTENT =
		"SELECT COUNT(dlContent) FROM DLContent dlContent";

	private static final String _SQL_COUNT_DLCONTENT_WHERE =
		"SELECT COUNT(dlContent) FROM DLContent dlContent WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "dlContent.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No DLContent exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No DLContent exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		DLContentPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"path", "data", "size"});

}