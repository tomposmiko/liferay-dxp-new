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

package com.liferay.journal.service.base;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.journal.service.JournalArticleServiceUtil;
import com.liferay.journal.service.persistence.JournalArticleFinder;
import com.liferay.journal.service.persistence.JournalArticleLocalizationPersistence;
import com.liferay.journal.service.persistence.JournalArticlePersistence;
import com.liferay.journal.service.persistence.JournalArticleResourcePersistence;
import com.liferay.journal.service.persistence.JournalFolderFinder;
import com.liferay.journal.service.persistence.JournalFolderPersistence;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.util.PortalUtil;

import java.lang.reflect.Field;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the journal article remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.journal.service.impl.JournalArticleServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.journal.service.impl.JournalArticleServiceImpl
 * @generated
 */
public abstract class JournalArticleServiceBaseImpl
	extends BaseServiceImpl
	implements AopService, IdentifiableOSGiService, JournalArticleService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>JournalArticleService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>JournalArticleServiceUtil</code>.
	 */
	@Deactivate
	protected void deactivate() {
		_setServiceUtilService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			JournalArticleService.class, IdentifiableOSGiService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		journalArticleService = (JournalArticleService)aopProxy;

		_setServiceUtilService(journalArticleService);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return JournalArticleService.class.getName();
	}

	protected Class<?> getModelClass() {
		return JournalArticle.class;
	}

	protected String getModelClassName() {
		return JournalArticle.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = journalArticlePersistence.getDataSource();

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

	private void _setServiceUtilService(
		JournalArticleService journalArticleService) {

		try {
			Field field = JournalArticleServiceUtil.class.getDeclaredField(
				"_service");

			field.setAccessible(true);

			field.set(null, journalArticleService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Reference
	protected com.liferay.journal.service.JournalArticleLocalService
		journalArticleLocalService;

	protected JournalArticleService journalArticleService;

	@Reference
	protected JournalArticlePersistence journalArticlePersistence;

	@Reference
	protected JournalArticleFinder journalArticleFinder;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ClassNameService
		classNameService;

	@Reference
	protected com.liferay.portal.kernel.service.CompanyLocalService
		companyLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.CompanyService companyService;

	@Reference
	protected com.liferay.portal.kernel.service.GroupLocalService
		groupLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.GroupService groupService;

	@Reference
	protected com.liferay.portal.kernel.service.ImageLocalService
		imageLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ImageService imageService;

	@Reference
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.SystemEventLocalService
		systemEventLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserService userService;

	@Reference
	protected com.liferay.portal.kernel.service.WorkflowInstanceLinkLocalService
		workflowInstanceLinkLocalService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetCategoryLocalService
		assetCategoryLocalService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetCategoryService
		assetCategoryService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetEntryLocalService
		assetEntryLocalService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetEntryService
		assetEntryService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetLinkLocalService
		assetLinkLocalService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetTagLocalService
		assetTagLocalService;

	@Reference
	protected com.liferay.asset.kernel.service.AssetTagService assetTagService;

	@Reference
	protected com.liferay.document.library.kernel.service.DLAppLocalService
		dlAppLocalService;

	@Reference
	protected com.liferay.document.library.kernel.service.DLAppService
		dlAppService;

	@Reference
	protected com.liferay.expando.kernel.service.ExpandoRowLocalService
		expandoRowLocalService;

	@Reference
	protected com.liferay.ratings.kernel.service.RatingsStatsLocalService
		ratingsStatsLocalService;

	@Reference
	protected JournalArticleLocalizationPersistence
		journalArticleLocalizationPersistence;

	@Reference
	protected JournalArticleResourcePersistence
		journalArticleResourcePersistence;

	@Reference
	protected JournalFolderPersistence journalFolderPersistence;

	@Reference
	protected JournalFolderFinder journalFolderFinder;

	private static final Log _log = LogFactoryUtil.getLog(
		JournalArticleServiceBaseImpl.class);

}