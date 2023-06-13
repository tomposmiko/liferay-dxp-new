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

package com.liferay.mobile.device.rules.service.base;

import com.liferay.mobile.device.rules.model.MDRAction;
import com.liferay.mobile.device.rules.service.MDRActionService;
import com.liferay.mobile.device.rules.service.MDRActionServiceUtil;
import com.liferay.mobile.device.rules.service.persistence.MDRActionPersistence;
import com.liferay.mobile.device.rules.service.persistence.MDRRuleGroupInstancePersistence;
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
 * Provides the base implementation for the mdr action remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.mobile.device.rules.service.impl.MDRActionServiceImpl}.
 * </p>
 *
 * @author Edward C. Han
 * @see com.liferay.mobile.device.rules.service.impl.MDRActionServiceImpl
 * @generated
 */
public abstract class MDRActionServiceBaseImpl
	extends BaseServiceImpl
	implements AopService, IdentifiableOSGiService, MDRActionService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>MDRActionService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>MDRActionServiceUtil</code>.
	 */
	@Deactivate
	protected void deactivate() {
		_setServiceUtilService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			MDRActionService.class, IdentifiableOSGiService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		mdrActionService = (MDRActionService)aopProxy;

		_setServiceUtilService(mdrActionService);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return MDRActionService.class.getName();
	}

	protected Class<?> getModelClass() {
		return MDRAction.class;
	}

	protected String getModelClassName() {
		return MDRAction.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = mdrActionPersistence.getDataSource();

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

	private void _setServiceUtilService(MDRActionService mdrActionService) {
		try {
			Field field = MDRActionServiceUtil.class.getDeclaredField(
				"_service");

			field.setAccessible(true);

			field.set(null, mdrActionService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Reference
	protected com.liferay.mobile.device.rules.service.MDRActionLocalService
		mdrActionLocalService;

	protected MDRActionService mdrActionService;

	@Reference
	protected MDRActionPersistence mdrActionPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserService userService;

	@Reference
	protected MDRRuleGroupInstancePersistence mdrRuleGroupInstancePersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		MDRActionServiceBaseImpl.class);

}