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

package com.liferay.commerce.notification.service.base;

import com.liferay.commerce.notification.model.CommerceNotificationTemplate;
import com.liferay.commerce.notification.service.CommerceNotificationTemplateLocalService;
import com.liferay.commerce.notification.service.CommerceNotificationTemplateLocalServiceUtil;
import com.liferay.commerce.notification.service.persistence.CommerceNotificationAttachmentPersistence;
import com.liferay.commerce.notification.service.persistence.CommerceNotificationQueueEntryPersistence;
import com.liferay.commerce.notification.service.persistence.CommerceNotificationTemplateCommerceAccountGroupRelPersistence;
import com.liferay.commerce.notification.service.persistence.CommerceNotificationTemplatePersistence;
import com.liferay.expando.kernel.service.persistence.ExpandoRowPersistence;
import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
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
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the commerce notification template local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.commerce.notification.service.impl.CommerceNotificationTemplateLocalServiceImpl}.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see com.liferay.commerce.notification.service.impl.CommerceNotificationTemplateLocalServiceImpl
 * @generated
 */
public abstract class CommerceNotificationTemplateLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements CommerceNotificationTemplateLocalService,
			   IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CommerceNotificationTemplateLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>CommerceNotificationTemplateLocalServiceUtil</code>.
	 */

	/**
	 * Adds the commerce notification template to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceNotificationTemplateLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceNotificationTemplate the commerce notification template
	 * @return the commerce notification template that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceNotificationTemplate addCommerceNotificationTemplate(
		CommerceNotificationTemplate commerceNotificationTemplate) {

		commerceNotificationTemplate.setNew(true);

		return commerceNotificationTemplatePersistence.update(
			commerceNotificationTemplate);
	}

	/**
	 * Creates a new commerce notification template with the primary key. Does not add the commerce notification template to the database.
	 *
	 * @param commerceNotificationTemplateId the primary key for the new commerce notification template
	 * @return the new commerce notification template
	 */
	@Override
	@Transactional(enabled = false)
	public CommerceNotificationTemplate createCommerceNotificationTemplate(
		long commerceNotificationTemplateId) {

		return commerceNotificationTemplatePersistence.create(
			commerceNotificationTemplateId);
	}

	/**
	 * Deletes the commerce notification template with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceNotificationTemplateLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceNotificationTemplateId the primary key of the commerce notification template
	 * @return the commerce notification template that was removed
	 * @throws PortalException if a commerce notification template with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CommerceNotificationTemplate deleteCommerceNotificationTemplate(
			long commerceNotificationTemplateId)
		throws PortalException {

		return commerceNotificationTemplatePersistence.remove(
			commerceNotificationTemplateId);
	}

	/**
	 * Deletes the commerce notification template from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceNotificationTemplateLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceNotificationTemplate the commerce notification template
	 * @return the commerce notification template that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CommerceNotificationTemplate deleteCommerceNotificationTemplate(
			CommerceNotificationTemplate commerceNotificationTemplate)
		throws PortalException {

		return commerceNotificationTemplatePersistence.remove(
			commerceNotificationTemplate);
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return commerceNotificationTemplatePersistence.dslQuery(dslQuery);
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
			CommerceNotificationTemplate.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return commerceNotificationTemplatePersistence.findWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.notification.model.impl.CommerceNotificationTemplateModelImpl</code>.
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

		return commerceNotificationTemplatePersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.notification.model.impl.CommerceNotificationTemplateModelImpl</code>.
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

		return commerceNotificationTemplatePersistence.findWithDynamicQuery(
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
		return commerceNotificationTemplatePersistence.countWithDynamicQuery(
			dynamicQuery);
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

		return commerceNotificationTemplatePersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public CommerceNotificationTemplate fetchCommerceNotificationTemplate(
		long commerceNotificationTemplateId) {

		return commerceNotificationTemplatePersistence.fetchByPrimaryKey(
			commerceNotificationTemplateId);
	}

	/**
	 * Returns the commerce notification template matching the UUID and group.
	 *
	 * @param uuid the commerce notification template's UUID
	 * @param groupId the primary key of the group
	 * @return the matching commerce notification template, or <code>null</code> if a matching commerce notification template could not be found
	 */
	@Override
	public CommerceNotificationTemplate
		fetchCommerceNotificationTemplateByUuidAndGroupId(
			String uuid, long groupId) {

		return commerceNotificationTemplatePersistence.fetchByUUID_G(
			uuid, groupId);
	}

	/**
	 * Returns the commerce notification template with the primary key.
	 *
	 * @param commerceNotificationTemplateId the primary key of the commerce notification template
	 * @return the commerce notification template
	 * @throws PortalException if a commerce notification template with the primary key could not be found
	 */
	@Override
	public CommerceNotificationTemplate getCommerceNotificationTemplate(
			long commerceNotificationTemplateId)
		throws PortalException {

		return commerceNotificationTemplatePersistence.findByPrimaryKey(
			commerceNotificationTemplateId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			commerceNotificationTemplateLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(
			CommerceNotificationTemplate.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceNotificationTemplateId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			commerceNotificationTemplateLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(
			CommerceNotificationTemplate.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceNotificationTemplateId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			commerceNotificationTemplateLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(
			CommerceNotificationTemplate.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceNotificationTemplateId");
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		final PortletDataContext portletDataContext) {

		final ExportActionableDynamicQuery exportActionableDynamicQuery =
			new ExportActionableDynamicQuery() {

				@Override
				public long performCount() throws PortalException {
					ManifestSummary manifestSummary =
						portletDataContext.getManifestSummary();

					StagedModelType stagedModelType = getStagedModelType();

					long modelAdditionCount = super.performCount();

					manifestSummary.addModelAdditionCount(
						stagedModelType, modelAdditionCount);

					long modelDeletionCount =
						ExportImportHelperUtil.getModelDeletionCount(
							portletDataContext, stagedModelType);

					manifestSummary.addModelDeletionCount(
						stagedModelType, modelDeletionCount);

					return modelAdditionCount;
				}

			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					portletDataContext.addDateRangeCriteria(
						dynamicQuery, "modifiedDate");
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<CommerceNotificationTemplate>() {

				@Override
				public void performAction(
						CommerceNotificationTemplate
							commerceNotificationTemplate)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, commerceNotificationTemplate);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(
					CommerceNotificationTemplate.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return commerceNotificationTemplatePersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return commerceNotificationTemplateLocalService.
			deleteCommerceNotificationTemplate(
				(CommerceNotificationTemplate)persistedModel);
	}

	public BasePersistence<CommerceNotificationTemplate> getBasePersistence() {
		return commerceNotificationTemplatePersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return commerceNotificationTemplatePersistence.findByPrimaryKey(
			primaryKeyObj);
	}

	/**
	 * Returns all the commerce notification templates matching the UUID and company.
	 *
	 * @param uuid the UUID of the commerce notification templates
	 * @param companyId the primary key of the company
	 * @return the matching commerce notification templates, or an empty list if no matches were found
	 */
	@Override
	public List<CommerceNotificationTemplate>
		getCommerceNotificationTemplatesByUuidAndCompanyId(
			String uuid, long companyId) {

		return commerceNotificationTemplatePersistence.findByUuid_C(
			uuid, companyId);
	}

	/**
	 * Returns a range of commerce notification templates matching the UUID and company.
	 *
	 * @param uuid the UUID of the commerce notification templates
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of commerce notification templates
	 * @param end the upper bound of the range of commerce notification templates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching commerce notification templates, or an empty list if no matches were found
	 */
	@Override
	public List<CommerceNotificationTemplate>
		getCommerceNotificationTemplatesByUuidAndCompanyId(
			String uuid, long companyId, int start, int end,
			OrderByComparator<CommerceNotificationTemplate> orderByComparator) {

		return commerceNotificationTemplatePersistence.findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the commerce notification template matching the UUID and group.
	 *
	 * @param uuid the commerce notification template's UUID
	 * @param groupId the primary key of the group
	 * @return the matching commerce notification template
	 * @throws PortalException if a matching commerce notification template could not be found
	 */
	@Override
	public CommerceNotificationTemplate
			getCommerceNotificationTemplateByUuidAndGroupId(
				String uuid, long groupId)
		throws PortalException {

		return commerceNotificationTemplatePersistence.findByUUID_G(
			uuid, groupId);
	}

	/**
	 * Returns a range of all the commerce notification templates.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.notification.model.impl.CommerceNotificationTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce notification templates
	 * @param end the upper bound of the range of commerce notification templates (not inclusive)
	 * @return the range of commerce notification templates
	 */
	@Override
	public List<CommerceNotificationTemplate> getCommerceNotificationTemplates(
		int start, int end) {

		return commerceNotificationTemplatePersistence.findAll(start, end);
	}

	/**
	 * Returns the number of commerce notification templates.
	 *
	 * @return the number of commerce notification templates
	 */
	@Override
	public int getCommerceNotificationTemplatesCount() {
		return commerceNotificationTemplatePersistence.countAll();
	}

	/**
	 * Updates the commerce notification template in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceNotificationTemplateLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceNotificationTemplate the commerce notification template
	 * @return the commerce notification template that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceNotificationTemplate updateCommerceNotificationTemplate(
		CommerceNotificationTemplate commerceNotificationTemplate) {

		return commerceNotificationTemplatePersistence.update(
			commerceNotificationTemplate);
	}

	/**
	 * Returns the commerce notification attachment local service.
	 *
	 * @return the commerce notification attachment local service
	 */
	public com.liferay.commerce.notification.service.
		CommerceNotificationAttachmentLocalService
			getCommerceNotificationAttachmentLocalService() {

		return commerceNotificationAttachmentLocalService;
	}

	/**
	 * Sets the commerce notification attachment local service.
	 *
	 * @param commerceNotificationAttachmentLocalService the commerce notification attachment local service
	 */
	public void setCommerceNotificationAttachmentLocalService(
		com.liferay.commerce.notification.service.
			CommerceNotificationAttachmentLocalService
				commerceNotificationAttachmentLocalService) {

		this.commerceNotificationAttachmentLocalService =
			commerceNotificationAttachmentLocalService;
	}

	/**
	 * Returns the commerce notification attachment persistence.
	 *
	 * @return the commerce notification attachment persistence
	 */
	public CommerceNotificationAttachmentPersistence
		getCommerceNotificationAttachmentPersistence() {

		return commerceNotificationAttachmentPersistence;
	}

	/**
	 * Sets the commerce notification attachment persistence.
	 *
	 * @param commerceNotificationAttachmentPersistence the commerce notification attachment persistence
	 */
	public void setCommerceNotificationAttachmentPersistence(
		CommerceNotificationAttachmentPersistence
			commerceNotificationAttachmentPersistence) {

		this.commerceNotificationAttachmentPersistence =
			commerceNotificationAttachmentPersistence;
	}

	/**
	 * Returns the commerce notification queue entry local service.
	 *
	 * @return the commerce notification queue entry local service
	 */
	public com.liferay.commerce.notification.service.
		CommerceNotificationQueueEntryLocalService
			getCommerceNotificationQueueEntryLocalService() {

		return commerceNotificationQueueEntryLocalService;
	}

	/**
	 * Sets the commerce notification queue entry local service.
	 *
	 * @param commerceNotificationQueueEntryLocalService the commerce notification queue entry local service
	 */
	public void setCommerceNotificationQueueEntryLocalService(
		com.liferay.commerce.notification.service.
			CommerceNotificationQueueEntryLocalService
				commerceNotificationQueueEntryLocalService) {

		this.commerceNotificationQueueEntryLocalService =
			commerceNotificationQueueEntryLocalService;
	}

	/**
	 * Returns the commerce notification queue entry persistence.
	 *
	 * @return the commerce notification queue entry persistence
	 */
	public CommerceNotificationQueueEntryPersistence
		getCommerceNotificationQueueEntryPersistence() {

		return commerceNotificationQueueEntryPersistence;
	}

	/**
	 * Sets the commerce notification queue entry persistence.
	 *
	 * @param commerceNotificationQueueEntryPersistence the commerce notification queue entry persistence
	 */
	public void setCommerceNotificationQueueEntryPersistence(
		CommerceNotificationQueueEntryPersistence
			commerceNotificationQueueEntryPersistence) {

		this.commerceNotificationQueueEntryPersistence =
			commerceNotificationQueueEntryPersistence;
	}

	/**
	 * Returns the commerce notification template local service.
	 *
	 * @return the commerce notification template local service
	 */
	public CommerceNotificationTemplateLocalService
		getCommerceNotificationTemplateLocalService() {

		return commerceNotificationTemplateLocalService;
	}

	/**
	 * Sets the commerce notification template local service.
	 *
	 * @param commerceNotificationTemplateLocalService the commerce notification template local service
	 */
	public void setCommerceNotificationTemplateLocalService(
		CommerceNotificationTemplateLocalService
			commerceNotificationTemplateLocalService) {

		this.commerceNotificationTemplateLocalService =
			commerceNotificationTemplateLocalService;
	}

	/**
	 * Returns the commerce notification template persistence.
	 *
	 * @return the commerce notification template persistence
	 */
	public CommerceNotificationTemplatePersistence
		getCommerceNotificationTemplatePersistence() {

		return commerceNotificationTemplatePersistence;
	}

	/**
	 * Sets the commerce notification template persistence.
	 *
	 * @param commerceNotificationTemplatePersistence the commerce notification template persistence
	 */
	public void setCommerceNotificationTemplatePersistence(
		CommerceNotificationTemplatePersistence
			commerceNotificationTemplatePersistence) {

		this.commerceNotificationTemplatePersistence =
			commerceNotificationTemplatePersistence;
	}

	/**
	 * Returns the commerce notification template commerce account group rel local service.
	 *
	 * @return the commerce notification template commerce account group rel local service
	 */
	public com.liferay.commerce.notification.service.
		CommerceNotificationTemplateCommerceAccountGroupRelLocalService
			getCommerceNotificationTemplateCommerceAccountGroupRelLocalService() {

		return commerceNotificationTemplateCommerceAccountGroupRelLocalService;
	}

	/**
	 * Sets the commerce notification template commerce account group rel local service.
	 *
	 * @param commerceNotificationTemplateCommerceAccountGroupRelLocalService the commerce notification template commerce account group rel local service
	 */
	public void
		setCommerceNotificationTemplateCommerceAccountGroupRelLocalService(
			com.liferay.commerce.notification.service.
				CommerceNotificationTemplateCommerceAccountGroupRelLocalService
					commerceNotificationTemplateCommerceAccountGroupRelLocalService) {

		this.commerceNotificationTemplateCommerceAccountGroupRelLocalService =
			commerceNotificationTemplateCommerceAccountGroupRelLocalService;
	}

	/**
	 * Returns the commerce notification template commerce account group rel persistence.
	 *
	 * @return the commerce notification template commerce account group rel persistence
	 */
	public CommerceNotificationTemplateCommerceAccountGroupRelPersistence
		getCommerceNotificationTemplateCommerceAccountGroupRelPersistence() {

		return commerceNotificationTemplateCommerceAccountGroupRelPersistence;
	}

	/**
	 * Sets the commerce notification template commerce account group rel persistence.
	 *
	 * @param commerceNotificationTemplateCommerceAccountGroupRelPersistence the commerce notification template commerce account group rel persistence
	 */
	public void
		setCommerceNotificationTemplateCommerceAccountGroupRelPersistence(
			CommerceNotificationTemplateCommerceAccountGroupRelPersistence
				commerceNotificationTemplateCommerceAccountGroupRelPersistence) {

		this.commerceNotificationTemplateCommerceAccountGroupRelPersistence =
			commerceNotificationTemplateCommerceAccountGroupRelPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService
		getCounterLocalService() {

		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService
			counterLocalService) {

		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService
		getClassNameLocalService() {

		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService
			classNameLocalService) {

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
	public com.liferay.portal.kernel.service.ResourceLocalService
		getResourceLocalService() {

		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService
			resourceLocalService) {

		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService
		getUserLocalService() {

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

	/**
	 * Returns the expando row local service.
	 *
	 * @return the expando row local service
	 */
	public com.liferay.expando.kernel.service.ExpandoRowLocalService
		getExpandoRowLocalService() {

		return expandoRowLocalService;
	}

	/**
	 * Sets the expando row local service.
	 *
	 * @param expandoRowLocalService the expando row local service
	 */
	public void setExpandoRowLocalService(
		com.liferay.expando.kernel.service.ExpandoRowLocalService
			expandoRowLocalService) {

		this.expandoRowLocalService = expandoRowLocalService;
	}

	/**
	 * Returns the expando row persistence.
	 *
	 * @return the expando row persistence
	 */
	public ExpandoRowPersistence getExpandoRowPersistence() {
		return expandoRowPersistence;
	}

	/**
	 * Sets the expando row persistence.
	 *
	 * @param expandoRowPersistence the expando row persistence
	 */
	public void setExpandoRowPersistence(
		ExpandoRowPersistence expandoRowPersistence) {

		this.expandoRowPersistence = expandoRowPersistence;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register(
			"com.liferay.commerce.notification.model.CommerceNotificationTemplate",
			commerceNotificationTemplateLocalService);

		_setLocalServiceUtilService(commerceNotificationTemplateLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.commerce.notification.model.CommerceNotificationTemplate");

		_setLocalServiceUtilService(null);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CommerceNotificationTemplateLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return CommerceNotificationTemplate.class;
	}

	protected String getModelClassName() {
		return CommerceNotificationTemplate.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				commerceNotificationTemplatePersistence.getDataSource();

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
		CommerceNotificationTemplateLocalService
			commerceNotificationTemplateLocalService) {

		try {
			Field field =
				CommerceNotificationTemplateLocalServiceUtil.class.
					getDeclaredField("_service");

			field.setAccessible(true);

			field.set(null, commerceNotificationTemplateLocalService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@BeanReference(
		type = com.liferay.commerce.notification.service.CommerceNotificationAttachmentLocalService.class
	)
	protected com.liferay.commerce.notification.service.
		CommerceNotificationAttachmentLocalService
			commerceNotificationAttachmentLocalService;

	@BeanReference(type = CommerceNotificationAttachmentPersistence.class)
	protected CommerceNotificationAttachmentPersistence
		commerceNotificationAttachmentPersistence;

	@BeanReference(
		type = com.liferay.commerce.notification.service.CommerceNotificationQueueEntryLocalService.class
	)
	protected com.liferay.commerce.notification.service.
		CommerceNotificationQueueEntryLocalService
			commerceNotificationQueueEntryLocalService;

	@BeanReference(type = CommerceNotificationQueueEntryPersistence.class)
	protected CommerceNotificationQueueEntryPersistence
		commerceNotificationQueueEntryPersistence;

	@BeanReference(type = CommerceNotificationTemplateLocalService.class)
	protected CommerceNotificationTemplateLocalService
		commerceNotificationTemplateLocalService;

	@BeanReference(type = CommerceNotificationTemplatePersistence.class)
	protected CommerceNotificationTemplatePersistence
		commerceNotificationTemplatePersistence;

	@BeanReference(
		type = com.liferay.commerce.notification.service.CommerceNotificationTemplateCommerceAccountGroupRelLocalService.class
	)
	protected com.liferay.commerce.notification.service.
		CommerceNotificationTemplateCommerceAccountGroupRelLocalService
			commerceNotificationTemplateCommerceAccountGroupRelLocalService;

	@BeanReference(
		type = CommerceNotificationTemplateCommerceAccountGroupRelPersistence.class
	)
	protected CommerceNotificationTemplateCommerceAccountGroupRelPersistence
		commerceNotificationTemplateCommerceAccountGroupRelPersistence;

	@ServiceReference(
		type = com.liferay.counter.kernel.service.CounterLocalService.class
	)
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.ClassNameLocalService.class
	)
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@ServiceReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.ResourceLocalService.class
	)
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.UserLocalService.class
	)
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;

	@ServiceReference(
		type = com.liferay.expando.kernel.service.ExpandoRowLocalService.class
	)
	protected com.liferay.expando.kernel.service.ExpandoRowLocalService
		expandoRowLocalService;

	@ServiceReference(type = ExpandoRowPersistence.class)
	protected ExpandoRowPersistence expandoRowPersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceNotificationTemplateLocalServiceBaseImpl.class);

	@ServiceReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry
		persistedModelLocalServiceRegistry;

}