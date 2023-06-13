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

package com.liferay.portal.tools.service.builder.test.service.base;

import aQute.bnd.annotation.ProviderType;

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
import com.liferay.portal.kernel.service.version.VersionService;
import com.liferay.portal.kernel.service.version.VersionServiceListener;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;
import com.liferay.portal.tools.service.builder.test.model.LVEntry;
import com.liferay.portal.tools.service.builder.test.model.LVEntryLocalization;
import com.liferay.portal.tools.service.builder.test.model.LVEntryLocalizationVersion;
import com.liferay.portal.tools.service.builder.test.model.LVEntryVersion;
import com.liferay.portal.tools.service.builder.test.service.LVEntryLocalService;
import com.liferay.portal.tools.service.builder.test.service.persistence.LVEntryLocalizationPersistence;
import com.liferay.portal.tools.service.builder.test.service.persistence.LVEntryLocalizationVersionPersistence;
import com.liferay.portal.tools.service.builder.test.service.persistence.LVEntryPersistence;
import com.liferay.portal.tools.service.builder.test.service.persistence.LVEntryVersionPersistence;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the lv entry local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.tools.service.builder.test.service.impl.LVEntryLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portal.tools.service.builder.test.service.impl.LVEntryLocalServiceImpl
 * @generated
 */
@ProviderType
public abstract class LVEntryLocalServiceBaseImpl extends BaseLocalServiceImpl
	implements LVEntryLocalService, IdentifiableOSGiService,
		VersionService<LVEntry, LVEntryVersion> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>LVEntryLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.portal.tools.service.builder.test.service.LVEntryLocalServiceUtil</code>.
	 */

	/**
	 * Adds the lv entry to the database. Also notifies the appropriate model listeners.
	 *
	 * @param lvEntry the lv entry
	 * @return the lv entry that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public LVEntry addLVEntry(LVEntry lvEntry) {
		lvEntry.setNew(true);

		return lvEntryPersistence.update(lvEntry);
	}

	/**
	 * Creates a new lv entry. Does not add the lv entry to the database.
	 *
	 * @return the new lv entry
	 */
	@Override
	@Transactional(enabled = false)
	public LVEntry create() {
		long primaryKey = counterLocalService.increment(LVEntry.class.getName());

		LVEntry draftLVEntry = lvEntryPersistence.create(primaryKey);

		draftLVEntry.setHeadId(primaryKey);

		return draftLVEntry;
	}

	/**
	 * Deletes the lv entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param lvEntryId the primary key of the lv entry
	 * @return the lv entry that was removed
	 * @throws PortalException if a lv entry with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public LVEntry deleteLVEntry(long lvEntryId) throws PortalException {
		LVEntry lvEntry = lvEntryPersistence.fetchByPrimaryKey(lvEntryId);

		if (lvEntry != null) {
			delete(lvEntry);
		}

		return lvEntry;
	}

	/**
	 * Deletes the lv entry from the database. Also notifies the appropriate model listeners.
	 *
	 * @param lvEntry the lv entry
	 * @return the lv entry that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public LVEntry deleteLVEntry(LVEntry lvEntry) {
		try {
			delete(lvEntry);

			return lvEntry;
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(LVEntry.class,
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
		return lvEntryPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.portal.tools.service.builder.test.model.impl.LVEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return lvEntryPersistence.findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.portal.tools.service.builder.test.model.impl.LVEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return lvEntryPersistence.findWithDynamicQuery(dynamicQuery, start,
			end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return lvEntryPersistence.countWithDynamicQuery(dynamicQuery);
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
		return lvEntryPersistence.countWithDynamicQuery(dynamicQuery, projection);
	}

	@Override
	public LVEntry fetchLVEntry(long lvEntryId) {
		return lvEntryPersistence.fetchByPrimaryKey(lvEntryId);
	}

	/**
	 * Returns the lv entry with the primary key.
	 *
	 * @param lvEntryId the primary key of the lv entry
	 * @return the lv entry
	 * @throws PortalException if a lv entry with the primary key could not be found
	 */
	@Override
	public LVEntry getLVEntry(long lvEntryId) throws PortalException {
		return lvEntryPersistence.findByPrimaryKey(lvEntryId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(lvEntryLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(LVEntry.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("lvEntryId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		IndexableActionableDynamicQuery indexableActionableDynamicQuery = new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(lvEntryLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(LVEntry.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName("lvEntryId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(lvEntryLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(LVEntry.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("lvEntryId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return lvEntryLocalService.deleteLVEntry((LVEntry)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return lvEntryPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the lv entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.portal.tools.service.builder.test.model.impl.LVEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of lv entries
	 * @param end the upper bound of the range of lv entries (not inclusive)
	 * @return the range of lv entries
	 */
	@Override
	public List<LVEntry> getLVEntries(int start, int end) {
		return lvEntryPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of lv entries.
	 *
	 * @return the number of lv entries
	 */
	@Override
	public int getLVEntriesCount() {
		return lvEntryPersistence.countAll();
	}

	/**
	 * Updates the lv entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param lvEntry the lv entry
	 * @return the lv entry that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public LVEntry updateLVEntry(LVEntry draftLVEntry)
		throws PortalException {
		return updateDraft(draftLVEntry);
	}

	@Override
	public LVEntryLocalization fetchLVEntryLocalization(long lvEntryId,
		String languageId) {
		return lvEntryLocalizationPersistence.fetchByLvEntryId_LanguageId(lvEntryId,
			languageId);
	}

	@Override
	public LVEntryLocalization getLVEntryLocalization(long lvEntryId,
		String languageId) throws PortalException {
		return lvEntryLocalizationPersistence.findByLvEntryId_LanguageId(lvEntryId,
			languageId);
	}

	@Override
	public List<LVEntryLocalization> getLVEntryLocalizations(long lvEntryId) {
		return lvEntryLocalizationPersistence.findByLvEntryId(lvEntryId);
	}

	@Override
	public LVEntryLocalization updateLVEntryLocalization(LVEntry draftLVEntry,
		String languageId, String title, String content)
		throws PortalException {
		draftLVEntry = lvEntryPersistence.findByPrimaryKey(draftLVEntry.getPrimaryKey());

		if (draftLVEntry.isHead()) {
			throw new IllegalArgumentException("Can only update draft entries " +
				draftLVEntry.getPrimaryKey());
		}

		LVEntryLocalization lvEntryLocalization = lvEntryLocalizationPersistence.fetchByLvEntryId_LanguageId(draftLVEntry.getLvEntryId(),
				languageId);

		return _updateLVEntryLocalization(draftLVEntry, lvEntryLocalization,
			languageId, title, content);
	}

	@Override
	public List<LVEntryLocalization> updateLVEntryLocalizations(
		LVEntry draftLVEntry, Map<String, String> titleMap,
		Map<String, String> contentMap) throws PortalException {
		draftLVEntry = lvEntryPersistence.findByPrimaryKey(draftLVEntry.getPrimaryKey());

		if (draftLVEntry.isHead()) {
			throw new IllegalArgumentException("Can only update draft entries " +
				draftLVEntry.getPrimaryKey());
		}

		Map<String, String[]> localizedValuesMap = new HashMap<String, String[]>();

		for (Map.Entry<String, String> entry : titleMap.entrySet()) {
			String languageId = entry.getKey();

			String[] localizedValues = localizedValuesMap.get(languageId);

			if (localizedValues == null) {
				localizedValues = new String[2];

				localizedValuesMap.put(languageId, localizedValues);
			}

			localizedValues[0] = entry.getValue();
		}

		for (Map.Entry<String, String> entry : contentMap.entrySet()) {
			String languageId = entry.getKey();

			String[] localizedValues = localizedValuesMap.get(languageId);

			if (localizedValues == null) {
				localizedValues = new String[2];

				localizedValuesMap.put(languageId, localizedValues);
			}

			localizedValues[1] = entry.getValue();
		}

		List<LVEntryLocalization> lvEntryLocalizations = new ArrayList<LVEntryLocalization>(localizedValuesMap.size());

		for (LVEntryLocalization lvEntryLocalization : lvEntryLocalizationPersistence.findByLvEntryId(
				draftLVEntry.getLvEntryId())) {
			String[] localizedValues = localizedValuesMap.remove(lvEntryLocalization.getLanguageId());

			if (localizedValues == null) {
				lvEntryLocalizationPersistence.remove(lvEntryLocalization);
			}
			else {
				lvEntryLocalization.setTitle(localizedValues[0]);
				lvEntryLocalization.setContent(localizedValues[1]);

				lvEntryLocalizations.add(lvEntryLocalizationPersistence.update(
						lvEntryLocalization));
			}
		}

		long batchCounter = counterLocalService.increment(LVEntryLocalization.class.getName(),
				localizedValuesMap.size()) - localizedValuesMap.size();

		for (Map.Entry<String, String[]> entry : localizedValuesMap.entrySet()) {
			String languageId = entry.getKey();
			String[] localizedValues = entry.getValue();

			LVEntryLocalization lvEntryLocalization = lvEntryLocalizationPersistence.create(++batchCounter);

			lvEntryLocalization.setHeadId(lvEntryLocalization.getPrimaryKey());

			lvEntryLocalization.setLvEntryId(draftLVEntry.getLvEntryId());

			lvEntryLocalization.setLanguageId(languageId);

			lvEntryLocalization.setTitle(localizedValues[0]);
			lvEntryLocalization.setContent(localizedValues[1]);

			lvEntryLocalizations.add(lvEntryLocalizationPersistence.update(
					lvEntryLocalization));
		}

		return lvEntryLocalizations;
	}

	private LVEntryLocalization _updateLVEntryLocalization(
		LVEntry draftLVEntry, LVEntryLocalization lvEntryLocalization,
		String languageId, String title, String content)
		throws PortalException {
		if (lvEntryLocalization == null) {
			long lvEntryLocalizationId = counterLocalService.increment(LVEntryLocalization.class.getName());

			lvEntryLocalization = lvEntryLocalizationPersistence.create(lvEntryLocalizationId);

			lvEntryLocalization.setLvEntryId(draftLVEntry.getLvEntryId());
			lvEntryLocalization.setLanguageId(languageId);
		}

		lvEntryLocalization.setHeadId(lvEntryLocalization.getPrimaryKey());

		lvEntryLocalization.setTitle(title);
		lvEntryLocalization.setContent(content);

		return lvEntryLocalizationPersistence.update(lvEntryLocalization);
	}

	/**
	 * Returns the lv entry local service.
	 *
	 * @return the lv entry local service
	 */
	public LVEntryLocalService getLVEntryLocalService() {
		return lvEntryLocalService;
	}

	/**
	 * Sets the lv entry local service.
	 *
	 * @param lvEntryLocalService the lv entry local service
	 */
	public void setLVEntryLocalService(LVEntryLocalService lvEntryLocalService) {
		this.lvEntryLocalService = lvEntryLocalService;
	}

	/**
	 * Returns the lv entry persistence.
	 *
	 * @return the lv entry persistence
	 */
	public LVEntryPersistence getLVEntryPersistence() {
		return lvEntryPersistence;
	}

	/**
	 * Sets the lv entry persistence.
	 *
	 * @param lvEntryPersistence the lv entry persistence
	 */
	public void setLVEntryPersistence(LVEntryPersistence lvEntryPersistence) {
		this.lvEntryPersistence = lvEntryPersistence;
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
	 * Returns the lv entry version persistence.
	 *
	 * @return the lv entry version persistence
	 */
	public LVEntryVersionPersistence getLVEntryVersionPersistence() {
		return lvEntryVersionPersistence;
	}

	/**
	 * Sets the lv entry version persistence.
	 *
	 * @param lvEntryVersionPersistence the lv entry version persistence
	 */
	public void setLVEntryVersionPersistence(
		LVEntryVersionPersistence lvEntryVersionPersistence) {
		this.lvEntryVersionPersistence = lvEntryVersionPersistence;
	}

	/**
	 * Returns the lv entry localization persistence.
	 *
	 * @return the lv entry localization persistence
	 */
	public LVEntryLocalizationPersistence getLVEntryLocalizationPersistence() {
		return lvEntryLocalizationPersistence;
	}

	/**
	 * Sets the lv entry localization persistence.
	 *
	 * @param lvEntryLocalizationPersistence the lv entry localization persistence
	 */
	public void setLVEntryLocalizationPersistence(
		LVEntryLocalizationPersistence lvEntryLocalizationPersistence) {
		this.lvEntryLocalizationPersistence = lvEntryLocalizationPersistence;
	}

	/**
	 * Returns the lv entry localization version persistence.
	 *
	 * @return the lv entry localization version persistence
	 */
	public LVEntryLocalizationVersionPersistence getLVEntryLocalizationVersionPersistence() {
		return lvEntryLocalizationVersionPersistence;
	}

	/**
	 * Sets the lv entry localization version persistence.
	 *
	 * @param lvEntryLocalizationVersionPersistence the lv entry localization version persistence
	 */
	public void setLVEntryLocalizationVersionPersistence(
		LVEntryLocalizationVersionPersistence lvEntryLocalizationVersionPersistence) {
		this.lvEntryLocalizationVersionPersistence = lvEntryLocalizationVersionPersistence;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register("com.liferay.portal.tools.service.builder.test.model.LVEntry",
			lvEntryLocalService);

		registerListener(new LVEntryLocalizationVersionServiceListener());
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.portal.tools.service.builder.test.model.LVEntry");
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public LVEntry checkout(LVEntry publishedLVEntry, int version)
		throws PortalException {
		if (!publishedLVEntry.isHead()) {
			throw new IllegalArgumentException(
				"Unable to checkout with unpublished changes " +
				publishedLVEntry.getHeadId());
		}

		LVEntry draftLVEntry = lvEntryPersistence.fetchByHeadId(publishedLVEntry.getPrimaryKey());

		if (draftLVEntry != null) {
			throw new IllegalArgumentException(
				"Unable to checkout with unpublished changes " +
				publishedLVEntry.getPrimaryKey());
		}

		LVEntryVersion lvEntryVersion = getVersion(publishedLVEntry, version);

		draftLVEntry = _createDraft(publishedLVEntry);

		lvEntryVersion.populateVersionedModel(draftLVEntry);

		draftLVEntry = lvEntryPersistence.update(draftLVEntry);

		for (VersionServiceListener<LVEntry, LVEntryVersion> versionServiceListener : _versionServiceListeners) {
			versionServiceListener.afterCheckout(draftLVEntry, version);
		}

		return draftLVEntry;
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	public LVEntry delete(LVEntry publishedLVEntry) throws PortalException {
		if (!publishedLVEntry.isHead()) {
			throw new IllegalArgumentException("LVEntry is a draft " +
				publishedLVEntry.getPrimaryKey());
		}

		LVEntry draftLVEntry = lvEntryPersistence.fetchByHeadId(publishedLVEntry.getPrimaryKey());

		if (draftLVEntry != null) {
			deleteDraft(draftLVEntry);
		}

		for (LVEntryVersion lvEntryVersion : getVersions(publishedLVEntry)) {
			lvEntryVersionPersistence.remove(lvEntryVersion);
		}

		lvEntryPersistence.remove(publishedLVEntry);

		for (VersionServiceListener<LVEntry, LVEntryVersion> versionServiceListener : _versionServiceListeners) {
			versionServiceListener.afterDelete(publishedLVEntry);
		}

		return publishedLVEntry;
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	public LVEntry deleteDraft(LVEntry draftLVEntry) throws PortalException {
		if (draftLVEntry.isHead()) {
			throw new IllegalArgumentException("LVEntry is not a draft " +
				draftLVEntry.getPrimaryKey());
		}

		lvEntryPersistence.remove(draftLVEntry);

		for (VersionServiceListener<LVEntry, LVEntryVersion> versionServiceListener : _versionServiceListeners) {
			versionServiceListener.afterDeleteDraft(draftLVEntry);
		}

		return draftLVEntry;
	}

	@Override
	public LVEntryVersion deleteVersion(LVEntryVersion lvEntryVersion)
		throws PortalException {
		LVEntryVersion latestLVEntryVersion = lvEntryVersionPersistence.findByLvEntryId_First(lvEntryVersion.getVersionedModelId(),
				null);

		if (latestLVEntryVersion.getVersion() == lvEntryVersion.getVersion()) {
			throw new IllegalArgumentException(
				"Unable to delete latest version " +
				lvEntryVersion.getVersion());
		}

		lvEntryVersion = lvEntryVersionPersistence.remove(lvEntryVersion);

		for (VersionServiceListener<LVEntry, LVEntryVersion> versionServiceListener : _versionServiceListeners) {
			versionServiceListener.afterDeleteVersion(lvEntryVersion);
		}

		return lvEntryVersion;
	}

	@Override
	public LVEntry fetchDraft(LVEntry lvEntry) {
		if (lvEntry.isHead()) {
			return lvEntryPersistence.fetchByHeadId(lvEntry.getPrimaryKey());
		}

		return lvEntry;
	}

	@Override
	public LVEntry fetchDraft(long primaryKey) {
		return lvEntryPersistence.fetchByHeadId(primaryKey);
	}

	@Override
	public LVEntryVersion fetchLatestVersion(LVEntry lvEntry) {
		long primaryKey = lvEntry.getHeadId();

		if (lvEntry.isHead()) {
			primaryKey = lvEntry.getPrimaryKey();
		}

		return lvEntryVersionPersistence.fetchByLvEntryId_First(primaryKey, null);
	}

	@Override
	public LVEntry fetchPublished(LVEntry lvEntry) {
		if (lvEntry.isHead()) {
			return lvEntry;
		}

		if (lvEntry.getHeadId() == lvEntry.getPrimaryKey()) {
			return null;
		}

		return lvEntryPersistence.fetchByPrimaryKey(lvEntry.getHeadId());
	}

	@Override
	public LVEntry fetchPublished(long primaryKey) {
		LVEntry lvEntry = lvEntryPersistence.fetchByPrimaryKey(primaryKey);

		if ((lvEntry == null) ||
				(lvEntry.getHeadId() == lvEntry.getPrimaryKey())) {
			return null;
		}

		return lvEntry;
	}

	@Override
	public LVEntry getDraft(LVEntry lvEntry) throws PortalException {
		if (!lvEntry.isHead()) {
			return lvEntry;
		}

		LVEntry draftLVEntry = lvEntryPersistence.fetchByHeadId(lvEntry.getPrimaryKey());

		if (draftLVEntry == null) {
			draftLVEntry = lvEntryLocalService.updateDraft(_createDraft(lvEntry));
		}

		return draftLVEntry;
	}

	@Override
	public LVEntry getDraft(long primaryKey) throws PortalException {
		LVEntry draftLVEntry = lvEntryPersistence.fetchByHeadId(primaryKey);

		if (draftLVEntry == null) {
			LVEntry lvEntry = lvEntryPersistence.findByPrimaryKey(primaryKey);

			draftLVEntry = lvEntryLocalService.updateDraft(_createDraft(lvEntry));
		}

		return draftLVEntry;
	}

	@Override
	public LVEntryVersion getVersion(LVEntry lvEntry, int version)
		throws PortalException {
		long primaryKey = lvEntry.getHeadId();

		if (lvEntry.isHead()) {
			primaryKey = lvEntry.getPrimaryKey();
		}

		return lvEntryVersionPersistence.findByLvEntryId_Version(primaryKey,
			version);
	}

	@Override
	public List<LVEntryVersion> getVersions(LVEntry lvEntry) {
		long primaryKey = lvEntry.getPrimaryKey();

		if (!lvEntry.isHead()) {
			if (lvEntry.getHeadId() == lvEntry.getPrimaryKey()) {
				return Collections.emptyList();
			}

			primaryKey = lvEntry.getHeadId();
		}

		return lvEntryVersionPersistence.findByLvEntryId(primaryKey);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public LVEntry publishDraft(LVEntry draftLVEntry) throws PortalException {
		if (draftLVEntry.isHead()) {
			throw new IllegalArgumentException("Can only publish drafts " +
				draftLVEntry.getPrimaryKey());
		}

		LVEntry headLVEntry = null;

		int version = 1;

		if (draftLVEntry.getHeadId() == draftLVEntry.getPrimaryKey()) {
			headLVEntry = create();

			draftLVEntry.setHeadId(headLVEntry.getPrimaryKey());
		}
		else {
			headLVEntry = lvEntryPersistence.findByPrimaryKey(draftLVEntry.getHeadId());

			LVEntryVersion latestLVEntryVersion = lvEntryVersionPersistence.findByLvEntryId_First(draftLVEntry.getHeadId(),
					null);

			version = latestLVEntryVersion.getVersion() + 1;
		}

		LVEntryVersion lvEntryVersion = lvEntryVersionPersistence.create(counterLocalService.increment(
					LVEntryVersion.class.getName()));

		lvEntryVersion.setVersion(version);
		lvEntryVersion.setVersionedModelId(headLVEntry.getPrimaryKey());

		draftLVEntry.populateVersionModel(lvEntryVersion);

		lvEntryVersionPersistence.update(lvEntryVersion);

		lvEntryVersion.populateVersionedModel(headLVEntry);

		headLVEntry.setHeadId(-headLVEntry.getPrimaryKey());

		headLVEntry = lvEntryPersistence.update(headLVEntry);

		for (VersionServiceListener<LVEntry, LVEntryVersion> versionServiceListener : _versionServiceListeners) {
			versionServiceListener.afterPublishDraft(draftLVEntry, version);
		}

		deleteDraft(draftLVEntry);

		return headLVEntry;
	}

	@Override
	public void registerListener(
		VersionServiceListener<LVEntry, LVEntryVersion> versionServiceListener) {
		_versionServiceListeners.add(versionServiceListener);
	}

	@Override
	public void unregisterListener(
		VersionServiceListener<LVEntry, LVEntryVersion> versionServiceListener) {
		_versionServiceListeners.remove(versionServiceListener);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public LVEntry updateDraft(LVEntry draftLVEntry) throws PortalException {
		if (draftLVEntry.isHead()) {
			throw new IllegalArgumentException("Can only update draft entries " +
				draftLVEntry.getPrimaryKey());
		}

		LVEntry previousLVEntry = lvEntryPersistence.fetchByPrimaryKey(draftLVEntry.getPrimaryKey());

		draftLVEntry = lvEntryPersistence.update(draftLVEntry);

		if (previousLVEntry == null) {
			for (VersionServiceListener<LVEntry, LVEntryVersion> versionServiceListener : _versionServiceListeners) {
				versionServiceListener.afterCreateDraft(draftLVEntry);
			}
		}
		else {
			for (VersionServiceListener<LVEntry, LVEntryVersion> versionServiceListener : _versionServiceListeners) {
				versionServiceListener.afterUpdateDraft(draftLVEntry);
			}
		}

		return draftLVEntry;
	}

	private LVEntry _createDraft(LVEntry publishedLVEntry)
		throws PortalException {
		LVEntry draftLVEntry = create();

		draftLVEntry.setUuid(publishedLVEntry.getUuid());
		draftLVEntry.setHeadId(publishedLVEntry.getPrimaryKey());
		draftLVEntry.setDefaultLanguageId(publishedLVEntry.getDefaultLanguageId());
		draftLVEntry.setGroupId(publishedLVEntry.getGroupId());
		draftLVEntry.setUniqueGroupKey(publishedLVEntry.getUniqueGroupKey());

		draftLVEntry.resetOriginalValues();

		return draftLVEntry;
	}

	private final Set<VersionServiceListener<LVEntry, LVEntryVersion>> _versionServiceListeners =
		Collections.newSetFromMap(new ConcurrentHashMap<VersionServiceListener<LVEntry, LVEntryVersion>, Boolean>());

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return LVEntryLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return LVEntry.class;
	}

	protected String getModelClassName() {
		return LVEntry.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = lvEntryPersistence.getDataSource();

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

	@BeanReference(type = LVEntryLocalService.class)
	protected LVEntryLocalService lvEntryLocalService;
	@BeanReference(type = LVEntryPersistence.class)
	protected LVEntryPersistence lvEntryPersistence;
	@ServiceReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@BeanReference(type = LVEntryVersionPersistence.class)
	protected LVEntryVersionPersistence lvEntryVersionPersistence;
	@BeanReference(type = LVEntryLocalizationPersistence.class)
	protected LVEntryLocalizationPersistence lvEntryLocalizationPersistence;
	@BeanReference(type = LVEntryLocalizationVersionPersistence.class)
	protected LVEntryLocalizationVersionPersistence lvEntryLocalizationVersionPersistence;
	@ServiceReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry persistedModelLocalServiceRegistry;

	private class LVEntryLocalizationVersionServiceListener
		implements VersionServiceListener<LVEntry, LVEntryVersion> {
		@Override
		public void afterCheckout(LVEntry draftLVEntry, int version)
			throws PortalException {
			Map<String, LVEntryLocalization> publishedLVEntryLocalizationMap = new HashMap<String, LVEntryLocalization>();

			for (LVEntryLocalization publishedLVEntryLocalization : lvEntryLocalizationPersistence.findByLvEntryId(
					draftLVEntry.getHeadId())) {
				publishedLVEntryLocalizationMap.put(publishedLVEntryLocalization.getLanguageId(),
					publishedLVEntryLocalization);
			}

			List<LVEntryLocalizationVersion> lvEntryLocalizationVersions = lvEntryLocalizationVersionPersistence.findByLvEntryId_Version(draftLVEntry.getHeadId(),
					version);

			long lvEntryLocalizationVersionBatchCounter = counterLocalService.increment(LVEntryLocalizationVersion.class.getName(),
					lvEntryLocalizationVersions.size()) -
				lvEntryLocalizationVersions.size();

			for (LVEntryLocalizationVersion lvEntryLocalizationVersion : lvEntryLocalizationVersions) {
				LVEntryLocalization draftLVEntryLocalization = lvEntryLocalizationPersistence.create(++lvEntryLocalizationVersionBatchCounter);

				long headId = draftLVEntryLocalization.getPrimaryKey();

				LVEntryLocalization publishedLVEntryLocalization = publishedLVEntryLocalizationMap.get(lvEntryLocalizationVersion.getLanguageId());

				if (publishedLVEntryLocalization != null) {
					headId = publishedLVEntryLocalization.getPrimaryKey();
				}

				draftLVEntryLocalization.setHeadId(headId);

				draftLVEntryLocalization.setLvEntryId(draftLVEntry.getPrimaryKey());
				draftLVEntryLocalization.setLanguageId(lvEntryLocalizationVersion.getLanguageId());

				draftLVEntryLocalization.setTitle(lvEntryLocalizationVersion.getTitle());
				draftLVEntryLocalization.setContent(lvEntryLocalizationVersion.getContent());

				lvEntryLocalizationPersistence.update(draftLVEntryLocalization);
			}
		}

		@Override
		public void afterCreateDraft(LVEntry draftLVEntry)
			throws PortalException {
			if (draftLVEntry.getHeadId() == draftLVEntry.getPrimaryKey()) {
				return;
			}

			for (LVEntryLocalization publishedLVEntryLocalization : lvEntryLocalizationPersistence.findByLvEntryId(
					draftLVEntry.getHeadId())) {
				_updateLVEntryLocalization(draftLVEntry, null,
					publishedLVEntryLocalization.getLanguageId(),
					publishedLVEntryLocalization.getTitle(),
					publishedLVEntryLocalization.getContent());
			}
		}

		@Override
		public void afterDelete(LVEntry publishedLVEntry)
			throws PortalException {
			lvEntryLocalizationPersistence.removeByLvEntryId(publishedLVEntry.getPrimaryKey());
			lvEntryLocalizationVersionPersistence.removeByLvEntryId(publishedLVEntry.getPrimaryKey());
		}

		@Override
		public void afterDeleteDraft(LVEntry draftLVEntry)
			throws PortalException {
			lvEntryLocalizationPersistence.removeByLvEntryId(draftLVEntry.getPrimaryKey());
		}

		@Override
		public void afterDeleteVersion(LVEntryVersion lvEntryVersion)
			throws PortalException {
			lvEntryLocalizationVersionPersistence.removeByLvEntryId_Version(lvEntryVersion.getVersionedModelId(),
				lvEntryVersion.getVersion());
		}

		@Override
		public void afterPublishDraft(LVEntry draftLVEntry, int version)
			throws PortalException {
			Map<String, LVEntryLocalization> draftLVEntryLocalizationMap = new HashMap<String, LVEntryLocalization>();

			for (LVEntryLocalization draftLVEntryLocalization : lvEntryLocalizationPersistence.findByLvEntryId(
					draftLVEntry.getPrimaryKey())) {
				draftLVEntryLocalizationMap.put(draftLVEntryLocalization.getLanguageId(),
					draftLVEntryLocalization);
			}

			long lvEntryLocalizationVersionBatchCounter = counterLocalService.increment(LVEntryLocalizationVersion.class.getName(),
					draftLVEntryLocalizationMap.size()) -
				draftLVEntryLocalizationMap.size();

			for (LVEntryLocalization publishedLVEntryLocalization : lvEntryLocalizationPersistence.findByLvEntryId(
					draftLVEntry.getHeadId())) {
				LVEntryLocalization draftLVEntryLocalization = draftLVEntryLocalizationMap.remove(publishedLVEntryLocalization.getLanguageId());

				if (draftLVEntryLocalization == null) {
					lvEntryLocalizationPersistence.remove(publishedLVEntryLocalization);
				}
				else {
					publishedLVEntryLocalization.setHeadId(-publishedLVEntryLocalization.getPrimaryKey());

					publishedLVEntryLocalization.setTitle(draftLVEntryLocalization.getTitle());
					publishedLVEntryLocalization.setContent(draftLVEntryLocalization.getContent());

					lvEntryLocalizationPersistence.update(publishedLVEntryLocalization);

					_publishLVEntryLocalizationVersion(publishedLVEntryLocalization,
						++lvEntryLocalizationVersionBatchCounter, version);
				}
			}

			long lvEntryLocalizationBatchCounter = counterLocalService.increment(LVEntryLocalization.class.getName(),
					draftLVEntryLocalizationMap.size()) -
				draftLVEntryLocalizationMap.size();

			for (LVEntryLocalization draftLVEntryLocalization : draftLVEntryLocalizationMap.values()) {
				LVEntryLocalization lvEntryLocalization = lvEntryLocalizationPersistence.create(++lvEntryLocalizationBatchCounter);

				lvEntryLocalization.setHeadId(lvEntryLocalization.getPrimaryKey());
				lvEntryLocalization.setLvEntryId(draftLVEntry.getHeadId());
				lvEntryLocalization.setLanguageId(draftLVEntryLocalization.getLanguageId());

				lvEntryLocalization.setTitle(draftLVEntryLocalization.getTitle());
				lvEntryLocalization.setContent(draftLVEntryLocalization.getContent());

				lvEntryLocalizationPersistence.update(lvEntryLocalization);

				_publishLVEntryLocalizationVersion(lvEntryLocalization,
					++lvEntryLocalizationVersionBatchCounter, version);
			}
		}

		@Override
		public void afterUpdateDraft(LVEntry draftLVEntry) {
		}

		private void _publishLVEntryLocalizationVersion(
			LVEntryLocalization lvEntryLocalization, long primaryKey,
			int version) {
			LVEntryLocalizationVersion lvEntryLocalizationVersion = lvEntryLocalizationVersionPersistence.create(primaryKey);

			lvEntryLocalizationVersion.setVersion(version);
			lvEntryLocalizationVersion.setVersionedModelId(lvEntryLocalization.getPrimaryKey());

			lvEntryLocalization.populateVersionModel(lvEntryLocalizationVersion);

			lvEntryLocalizationVersionPersistence.update(lvEntryLocalizationVersion);
		}
	}
}