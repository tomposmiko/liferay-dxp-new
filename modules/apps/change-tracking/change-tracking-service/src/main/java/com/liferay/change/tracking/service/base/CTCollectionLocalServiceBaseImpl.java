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

import aQute.bnd.annotation.ProviderType;

import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.service.CTCollectionLocalService;
import com.liferay.change.tracking.service.persistence.CTCollectionPersistence;
import com.liferay.change.tracking.service.persistence.CTEntryFinder;
import com.liferay.change.tracking.service.persistence.CTEntryPersistence;
import com.liferay.change.tracking.service.persistence.CTProcessPersistence;

import com.liferay.portal.kernel.bean.BeanReference;
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
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the ct collection local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.change.tracking.service.impl.CTCollectionLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.change.tracking.service.impl.CTCollectionLocalServiceImpl
 * @generated
 */
@ProviderType
public abstract class CTCollectionLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements CTCollectionLocalService,
		IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CTCollectionLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.change.tracking.service.CTCollectionLocalServiceUtil</code>.
	 */

	/**
	 * Adds the ct collection to the database. Also notifies the appropriate model listeners.
	 *
	 * @param ctCollection the ct collection
	 * @return the ct collection that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CTCollection addCTCollection(CTCollection ctCollection) {
		ctCollection.setNew(true);

		return ctCollectionPersistence.update(ctCollection);
	}

	/**
	 * Creates a new ct collection with the primary key. Does not add the ct collection to the database.
	 *
	 * @param ctCollectionId the primary key for the new ct collection
	 * @return the new ct collection
	 */
	@Override
	@Transactional(enabled = false)
	public CTCollection createCTCollection(long ctCollectionId) {
		return ctCollectionPersistence.create(ctCollectionId);
	}

	/**
	 * Deletes the ct collection with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param ctCollectionId the primary key of the ct collection
	 * @return the ct collection that was removed
	 * @throws PortalException if a ct collection with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CTCollection deleteCTCollection(long ctCollectionId)
		throws PortalException {
		return ctCollectionPersistence.remove(ctCollectionId);
	}

	/**
	 * Deletes the ct collection from the database. Also notifies the appropriate model listeners.
	 *
	 * @param ctCollection the ct collection
	 * @return the ct collection that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CTCollection deleteCTCollection(CTCollection ctCollection)
		throws PortalException {
		return ctCollectionPersistence.remove(ctCollection);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(CTCollection.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return ctCollectionPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.change.tracking.model.impl.CTCollectionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end) {
		return ctCollectionPersistence.findWithDynamicQuery(dynamicQuery,
			start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.change.tracking.model.impl.CTCollectionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end, OrderByComparator<T> orderByComparator) {
		return ctCollectionPersistence.findWithDynamicQuery(dynamicQuery,
			start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return ctCollectionPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection) {
		return ctCollectionPersistence.countWithDynamicQuery(dynamicQuery,
			projection);
	}

	@Override
	public CTCollection fetchCTCollection(long ctCollectionId) {
		return ctCollectionPersistence.fetchByPrimaryKey(ctCollectionId);
	}

	/**
	 * Returns the ct collection with the primary key.
	 *
	 * @param ctCollectionId the primary key of the ct collection
	 * @return the ct collection
	 * @throws PortalException if a ct collection with the primary key could not be found
	 */
	@Override
	public CTCollection getCTCollection(long ctCollectionId)
		throws PortalException {
		return ctCollectionPersistence.findByPrimaryKey(ctCollectionId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(ctCollectionLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(CTCollection.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("ctCollectionId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		IndexableActionableDynamicQuery indexableActionableDynamicQuery = new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(ctCollectionLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(CTCollection.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"ctCollectionId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(ctCollectionLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(CTCollection.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("ctCollectionId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return ctCollectionLocalService.deleteCTCollection((CTCollection)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return ctCollectionPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the ct collections.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.change.tracking.model.impl.CTCollectionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of ct collections
	 * @param end the upper bound of the range of ct collections (not inclusive)
	 * @return the range of ct collections
	 */
	@Override
	public List<CTCollection> getCTCollections(int start, int end) {
		return ctCollectionPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of ct collections.
	 *
	 * @return the number of ct collections
	 */
	@Override
	public int getCTCollectionsCount() {
		return ctCollectionPersistence.countAll();
	}

	/**
	 * Updates the ct collection in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param ctCollection the ct collection
	 * @return the ct collection that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CTCollection updateCTCollection(CTCollection ctCollection) {
		return ctCollectionPersistence.update(ctCollection);
	}

	/**
	 */
	@Override
	public void addCTEntryCTCollection(long ctEntryId, long ctCollectionId) {
		ctEntryPersistence.addCTCollection(ctEntryId, ctCollectionId);
	}

	/**
	 */
	@Override
	public void addCTEntryCTCollection(long ctEntryId, CTCollection ctCollection) {
		ctEntryPersistence.addCTCollection(ctEntryId, ctCollection);
	}

	/**
	 */
	@Override
	public void addCTEntryCTCollections(long ctEntryId, long[] ctCollectionIds) {
		ctEntryPersistence.addCTCollections(ctEntryId, ctCollectionIds);
	}

	/**
	 */
	@Override
	public void addCTEntryCTCollections(long ctEntryId,
		List<CTCollection> ctCollections) {
		ctEntryPersistence.addCTCollections(ctEntryId, ctCollections);
	}

	/**
	 */
	@Override
	public void clearCTEntryCTCollections(long ctEntryId) {
		ctEntryPersistence.clearCTCollections(ctEntryId);
	}

	/**
	 */
	@Override
	public void deleteCTEntryCTCollection(long ctEntryId, long ctCollectionId) {
		ctEntryPersistence.removeCTCollection(ctEntryId, ctCollectionId);
	}

	/**
	 */
	@Override
	public void deleteCTEntryCTCollection(long ctEntryId,
		CTCollection ctCollection) {
		ctEntryPersistence.removeCTCollection(ctEntryId, ctCollection);
	}

	/**
	 */
	@Override
	public void deleteCTEntryCTCollections(long ctEntryId,
		long[] ctCollectionIds) {
		ctEntryPersistence.removeCTCollections(ctEntryId, ctCollectionIds);
	}

	/**
	 */
	@Override
	public void deleteCTEntryCTCollections(long ctEntryId,
		List<CTCollection> ctCollections) {
		ctEntryPersistence.removeCTCollections(ctEntryId, ctCollections);
	}

	/**
	 * Returns the ctEntryIds of the ct entries associated with the ct collection.
	 *
	 * @param ctCollectionId the ctCollectionId of the ct collection
	 * @return long[] the ctEntryIds of ct entries associated with the ct collection
	 */
	@Override
	public long[] getCTEntryPrimaryKeys(long ctCollectionId) {
		return ctCollectionPersistence.getCTEntryPrimaryKeys(ctCollectionId);
	}

	/**
	 */
	@Override
	public List<CTCollection> getCTEntryCTCollections(long ctEntryId) {
		return ctEntryPersistence.getCTCollections(ctEntryId);
	}

	/**
	 */
	@Override
	public List<CTCollection> getCTEntryCTCollections(long ctEntryId,
		int start, int end) {
		return ctEntryPersistence.getCTCollections(ctEntryId, start, end);
	}

	/**
	 */
	@Override
	public List<CTCollection> getCTEntryCTCollections(long ctEntryId,
		int start, int end, OrderByComparator<CTCollection> orderByComparator) {
		return ctEntryPersistence.getCTCollections(ctEntryId, start, end,
			orderByComparator);
	}

	/**
	 */
	@Override
	public int getCTEntryCTCollectionsCount(long ctEntryId) {
		return ctEntryPersistence.getCTCollectionsSize(ctEntryId);
	}

	/**
	 */
	@Override
	public boolean hasCTEntryCTCollection(long ctEntryId, long ctCollectionId) {
		return ctEntryPersistence.containsCTCollection(ctEntryId, ctCollectionId);
	}

	/**
	 */
	@Override
	public boolean hasCTEntryCTCollections(long ctEntryId) {
		return ctEntryPersistence.containsCTCollections(ctEntryId);
	}

	/**
	 */
	@Override
	public void setCTEntryCTCollections(long ctEntryId, long[] ctCollectionIds) {
		ctEntryPersistence.setCTCollections(ctEntryId, ctCollectionIds);
	}

	/**
	 * Returns the ct collection local service.
	 *
	 * @return the ct collection local service
	 */
	public CTCollectionLocalService getCTCollectionLocalService() {
		return ctCollectionLocalService;
	}

	/**
	 * Sets the ct collection local service.
	 *
	 * @param ctCollectionLocalService the ct collection local service
	 */
	public void setCTCollectionLocalService(
		CTCollectionLocalService ctCollectionLocalService) {
		this.ctCollectionLocalService = ctCollectionLocalService;
	}

	/**
	 * Returns the ct collection persistence.
	 *
	 * @return the ct collection persistence
	 */
	public CTCollectionPersistence getCTCollectionPersistence() {
		return ctCollectionPersistence;
	}

	/**
	 * Sets the ct collection persistence.
	 *
	 * @param ctCollectionPersistence the ct collection persistence
	 */
	public void setCTCollectionPersistence(
		CTCollectionPersistence ctCollectionPersistence) {
		this.ctCollectionPersistence = ctCollectionPersistence;
	}

	/**
	 * Returns the ct entry local service.
	 *
	 * @return the ct entry local service
	 */
	public com.liferay.change.tracking.service.CTEntryLocalService getCTEntryLocalService() {
		return ctEntryLocalService;
	}

	/**
	 * Sets the ct entry local service.
	 *
	 * @param ctEntryLocalService the ct entry local service
	 */
	public void setCTEntryLocalService(
		com.liferay.change.tracking.service.CTEntryLocalService ctEntryLocalService) {
		this.ctEntryLocalService = ctEntryLocalService;
	}

	/**
	 * Returns the ct entry persistence.
	 *
	 * @return the ct entry persistence
	 */
	public CTEntryPersistence getCTEntryPersistence() {
		return ctEntryPersistence;
	}

	/**
	 * Sets the ct entry persistence.
	 *
	 * @param ctEntryPersistence the ct entry persistence
	 */
	public void setCTEntryPersistence(CTEntryPersistence ctEntryPersistence) {
		this.ctEntryPersistence = ctEntryPersistence;
	}

	/**
	 * Returns the ct entry finder.
	 *
	 * @return the ct entry finder
	 */
	public CTEntryFinder getCTEntryFinder() {
		return ctEntryFinder;
	}

	/**
	 * Sets the ct entry finder.
	 *
	 * @param ctEntryFinder the ct entry finder
	 */
	public void setCTEntryFinder(CTEntryFinder ctEntryFinder) {
		this.ctEntryFinder = ctEntryFinder;
	}

	/**
	 * Returns the ct process local service.
	 *
	 * @return the ct process local service
	 */
	public com.liferay.change.tracking.service.CTProcessLocalService getCTProcessLocalService() {
		return ctProcessLocalService;
	}

	/**
	 * Sets the ct process local service.
	 *
	 * @param ctProcessLocalService the ct process local service
	 */
	public void setCTProcessLocalService(
		com.liferay.change.tracking.service.CTProcessLocalService ctProcessLocalService) {
		this.ctProcessLocalService = ctProcessLocalService;
	}

	/**
	 * Returns the ct process persistence.
	 *
	 * @return the ct process persistence
	 */
	public CTProcessPersistence getCTProcessPersistence() {
		return ctProcessPersistence;
	}

	/**
	 * Sets the ct process persistence.
	 *
	 * @param ctProcessPersistence the ct process persistence
	 */
	public void setCTProcessPersistence(
		CTProcessPersistence ctProcessPersistence) {
		this.ctProcessPersistence = ctProcessPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService getClassNameLocalService() {
		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService) {
		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {
		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register("com.liferay.change.tracking.model.CTCollection",
			ctCollectionLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.change.tracking.model.CTCollection");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CTCollectionLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return CTCollection.class;
	}

	protected String getModelClassName() {
		return CTCollection.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = ctCollectionPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = CTCollectionLocalService.class)
	protected CTCollectionLocalService ctCollectionLocalService;
	@BeanReference(type = CTCollectionPersistence.class)
	protected CTCollectionPersistence ctCollectionPersistence;
	@BeanReference(type = com.liferay.change.tracking.service.CTEntryLocalService.class)
	protected com.liferay.change.tracking.service.CTEntryLocalService ctEntryLocalService;
	@BeanReference(type = CTEntryPersistence.class)
	protected CTEntryPersistence ctEntryPersistence;
	@BeanReference(type = CTEntryFinder.class)
	protected CTEntryFinder ctEntryFinder;
	@BeanReference(type = com.liferay.change.tracking.service.CTProcessLocalService.class)
	protected com.liferay.change.tracking.service.CTProcessLocalService ctProcessLocalService;
	@BeanReference(type = CTProcessPersistence.class)
	protected CTProcessPersistence ctProcessPersistence;
	@ServiceReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.ClassNameLocalService.class)
	protected com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService;
	@ServiceReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;
	@ServiceReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserLocalService.class)
	protected com.liferay.portal.kernel.service.UserLocalService userLocalService;
	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@ServiceReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry persistedModelLocalServiceRegistry;
}