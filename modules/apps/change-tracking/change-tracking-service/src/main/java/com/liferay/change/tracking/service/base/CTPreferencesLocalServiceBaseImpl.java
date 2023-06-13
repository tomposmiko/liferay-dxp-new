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

package com.liferay.change.tracking.service.base;

import com.liferay.change.tracking.model.CTPreferences;
import com.liferay.change.tracking.service.CTPreferencesLocalService;
import com.liferay.change.tracking.service.CTPreferencesLocalServiceUtil;
import com.liferay.change.tracking.service.persistence.CTPreferencesPersistence;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the ct preferences local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.change.tracking.service.impl.CTPreferencesLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.change.tracking.service.impl.CTPreferencesLocalServiceImpl
 * @generated
 */
public abstract class CTPreferencesLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, CTPreferencesLocalService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CTPreferencesLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>CTPreferencesLocalServiceUtil</code>.
	 */

	/**
	 * Adds the ct preferences to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CTPreferencesLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ctPreferences the ct preferences
	 * @return the ct preferences that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CTPreferences addCTPreferences(CTPreferences ctPreferences) {
		ctPreferences.setNew(true);

		return ctPreferencesPersistence.update(ctPreferences);
	}

	/**
	 * Creates a new ct preferences with the primary key. Does not add the ct preferences to the database.
	 *
	 * @param ctPreferencesId the primary key for the new ct preferences
	 * @return the new ct preferences
	 */
	@Override
	@Transactional(enabled = false)
	public CTPreferences createCTPreferences(long ctPreferencesId) {
		return ctPreferencesPersistence.create(ctPreferencesId);
	}

	/**
	 * Deletes the ct preferences with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CTPreferencesLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ctPreferencesId the primary key of the ct preferences
	 * @return the ct preferences that was removed
	 * @throws PortalException if a ct preferences with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CTPreferences deleteCTPreferences(long ctPreferencesId)
		throws PortalException {

		return ctPreferencesPersistence.remove(ctPreferencesId);
	}

	/**
	 * Deletes the ct preferences from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CTPreferencesLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ctPreferences the ct preferences
	 * @return the ct preferences that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CTPreferences deleteCTPreferences(CTPreferences ctPreferences) {
		return ctPreferencesPersistence.remove(ctPreferences);
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return ctPreferencesPersistence.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(DSLQuery dslQuery) {
		Long count = dslQuery(dslQuery);

		return count.intValue();
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			CTPreferences.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return ctPreferencesPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.change.tracking.model.impl.CTPreferencesModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return ctPreferencesPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.change.tracking.model.impl.CTPreferencesModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return ctPreferencesPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return ctPreferencesPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return ctPreferencesPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public CTPreferences fetchCTPreferences(long ctPreferencesId) {
		return ctPreferencesPersistence.fetchByPrimaryKey(ctPreferencesId);
	}

	/**
	 * Returns the ct preferences with the primary key.
	 *
	 * @param ctPreferencesId the primary key of the ct preferences
	 * @return the ct preferences
	 * @throws PortalException if a ct preferences with the primary key could not be found
	 */
	@Override
	public CTPreferences getCTPreferences(long ctPreferencesId)
		throws PortalException {

		return ctPreferencesPersistence.findByPrimaryKey(ctPreferencesId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(ctPreferencesLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(CTPreferences.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("ctPreferencesId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			ctPreferencesLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(CTPreferences.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"ctPreferencesId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(ctPreferencesLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(CTPreferences.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("ctPreferencesId");
	}

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return ctPreferencesPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return ctPreferencesLocalService.deleteCTPreferences(
			(CTPreferences)persistedModel);
	}

	public BasePersistence<CTPreferences> getBasePersistence() {
		return ctPreferencesPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return ctPreferencesPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the ct preferenceses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.change.tracking.model.impl.CTPreferencesModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of ct preferenceses
	 * @param end the upper bound of the range of ct preferenceses (not inclusive)
	 * @return the range of ct preferenceses
	 */
	@Override
	public List<CTPreferences> getCTPreferenceses(int start, int end) {
		return ctPreferencesPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of ct preferenceses.
	 *
	 * @return the number of ct preferenceses
	 */
	@Override
	public int getCTPreferencesesCount() {
		return ctPreferencesPersistence.countAll();
	}

	/**
	 * Updates the ct preferences in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CTPreferencesLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param ctPreferences the ct preferences
	 * @return the ct preferences that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CTPreferences updateCTPreferences(CTPreferences ctPreferences) {
		return ctPreferencesPersistence.update(ctPreferences);
	}

	@Deactivate
	protected void deactivate() {
		_setLocalServiceUtilService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			CTPreferencesLocalService.class, IdentifiableOSGiService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		ctPreferencesLocalService = (CTPreferencesLocalService)aopProxy;

		_setLocalServiceUtilService(ctPreferencesLocalService);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CTPreferencesLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return CTPreferences.class;
	}

	protected String getModelClassName() {
		return CTPreferences.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = ctPreferencesPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	private void _setLocalServiceUtilService(
		CTPreferencesLocalService ctPreferencesLocalService) {

		try {
			Field field = CTPreferencesLocalServiceUtil.class.getDeclaredField(
				"_service");

			field.setAccessible(true);

			field.set(null, ctPreferencesLocalService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	protected CTPreferencesLocalService ctPreferencesLocalService;

	@Reference
	protected CTPreferencesPersistence ctPreferencesPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		CTPreferencesLocalServiceBaseImpl.class);

}