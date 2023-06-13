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

package com.liferay.batch.planner.service.base;

import com.liferay.batch.planner.model.BatchPlannerPlan;
import com.liferay.batch.planner.service.BatchPlannerPlanLocalService;
import com.liferay.batch.planner.service.BatchPlannerPlanLocalServiceUtil;
import com.liferay.batch.planner.service.persistence.BatchPlannerPlanPersistence;
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
 * Provides the base implementation for the batch planner plan local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.batch.planner.service.impl.BatchPlannerPlanLocalServiceImpl}.
 * </p>
 *
 * @author Igor Beslic
 * @see com.liferay.batch.planner.service.impl.BatchPlannerPlanLocalServiceImpl
 * @generated
 */
public abstract class BatchPlannerPlanLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, BatchPlannerPlanLocalService,
			   IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>BatchPlannerPlanLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>BatchPlannerPlanLocalServiceUtil</code>.
	 */

	/**
	 * Adds the batch planner plan to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect BatchPlannerPlanLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param batchPlannerPlan the batch planner plan
	 * @return the batch planner plan that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public BatchPlannerPlan addBatchPlannerPlan(
		BatchPlannerPlan batchPlannerPlan) {

		batchPlannerPlan.setNew(true);

		return batchPlannerPlanPersistence.update(batchPlannerPlan);
	}

	/**
	 * Creates a new batch planner plan with the primary key. Does not add the batch planner plan to the database.
	 *
	 * @param batchPlannerPlanId the primary key for the new batch planner plan
	 * @return the new batch planner plan
	 */
	@Override
	@Transactional(enabled = false)
	public BatchPlannerPlan createBatchPlannerPlan(long batchPlannerPlanId) {
		return batchPlannerPlanPersistence.create(batchPlannerPlanId);
	}

	/**
	 * Deletes the batch planner plan with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect BatchPlannerPlanLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param batchPlannerPlanId the primary key of the batch planner plan
	 * @return the batch planner plan that was removed
	 * @throws PortalException if a batch planner plan with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public BatchPlannerPlan deleteBatchPlannerPlan(long batchPlannerPlanId)
		throws PortalException {

		return batchPlannerPlanPersistence.remove(batchPlannerPlanId);
	}

	/**
	 * Deletes the batch planner plan from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect BatchPlannerPlanLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param batchPlannerPlan the batch planner plan
	 * @return the batch planner plan that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public BatchPlannerPlan deleteBatchPlannerPlan(
		BatchPlannerPlan batchPlannerPlan) {

		return batchPlannerPlanPersistence.remove(batchPlannerPlan);
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return batchPlannerPlanPersistence.dslQuery(dslQuery);
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
			BatchPlannerPlan.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return batchPlannerPlanPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.batch.planner.model.impl.BatchPlannerPlanModelImpl</code>.
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

		return batchPlannerPlanPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.batch.planner.model.impl.BatchPlannerPlanModelImpl</code>.
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

		return batchPlannerPlanPersistence.findWithDynamicQuery(
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
		return batchPlannerPlanPersistence.countWithDynamicQuery(dynamicQuery);
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

		return batchPlannerPlanPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public BatchPlannerPlan fetchBatchPlannerPlan(long batchPlannerPlanId) {
		return batchPlannerPlanPersistence.fetchByPrimaryKey(
			batchPlannerPlanId);
	}

	/**
	 * Returns the batch planner plan with the primary key.
	 *
	 * @param batchPlannerPlanId the primary key of the batch planner plan
	 * @return the batch planner plan
	 * @throws PortalException if a batch planner plan with the primary key could not be found
	 */
	@Override
	public BatchPlannerPlan getBatchPlannerPlan(long batchPlannerPlanId)
		throws PortalException {

		return batchPlannerPlanPersistence.findByPrimaryKey(batchPlannerPlanId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			batchPlannerPlanLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(BatchPlannerPlan.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("batchPlannerPlanId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			batchPlannerPlanLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(BatchPlannerPlan.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"batchPlannerPlanId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			batchPlannerPlanLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(BatchPlannerPlan.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("batchPlannerPlanId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return batchPlannerPlanPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return batchPlannerPlanLocalService.deleteBatchPlannerPlan(
			(BatchPlannerPlan)persistedModel);
	}

	@Override
	public BasePersistence<BatchPlannerPlan> getBasePersistence() {
		return batchPlannerPlanPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return batchPlannerPlanPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the batch planner plans.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.batch.planner.model.impl.BatchPlannerPlanModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of batch planner plans
	 * @param end the upper bound of the range of batch planner plans (not inclusive)
	 * @return the range of batch planner plans
	 */
	@Override
	public List<BatchPlannerPlan> getBatchPlannerPlans(int start, int end) {
		return batchPlannerPlanPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of batch planner plans.
	 *
	 * @return the number of batch planner plans
	 */
	@Override
	public int getBatchPlannerPlansCount() {
		return batchPlannerPlanPersistence.countAll();
	}

	/**
	 * Updates the batch planner plan in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect BatchPlannerPlanLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param batchPlannerPlan the batch planner plan
	 * @return the batch planner plan that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public BatchPlannerPlan updateBatchPlannerPlan(
		BatchPlannerPlan batchPlannerPlan) {

		return batchPlannerPlanPersistence.update(batchPlannerPlan);
	}

	@Deactivate
	protected void deactivate() {
		_setLocalServiceUtilService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			BatchPlannerPlanLocalService.class, IdentifiableOSGiService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		batchPlannerPlanLocalService = (BatchPlannerPlanLocalService)aopProxy;

		_setLocalServiceUtilService(batchPlannerPlanLocalService);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return BatchPlannerPlanLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return BatchPlannerPlan.class;
	}

	protected String getModelClassName() {
		return BatchPlannerPlan.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = batchPlannerPlanPersistence.getDataSource();

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
		BatchPlannerPlanLocalService batchPlannerPlanLocalService) {

		try {
			Field field =
				BatchPlannerPlanLocalServiceUtil.class.getDeclaredField(
					"_service");

			field.setAccessible(true);

			field.set(null, batchPlannerPlanLocalService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	protected BatchPlannerPlanLocalService batchPlannerPlanLocalService;

	@Reference
	protected BatchPlannerPlanPersistence batchPlannerPlanPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

}