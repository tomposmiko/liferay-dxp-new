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

package com.liferay.commerce.wish.list.service.base;

import com.liferay.commerce.wish.list.model.CommerceWishList;
import com.liferay.commerce.wish.list.service.CommerceWishListService;
import com.liferay.commerce.wish.list.service.CommerceWishListServiceUtil;
import com.liferay.commerce.wish.list.service.persistence.CommerceWishListItemPersistence;
import com.liferay.commerce.wish.list.service.persistence.CommerceWishListPersistence;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.lang.reflect.Field;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the commerce wish list remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.commerce.wish.list.service.impl.CommerceWishListServiceImpl}.
 * </p>
 *
 * @author Andrea Di Giorgi
 * @see com.liferay.commerce.wish.list.service.impl.CommerceWishListServiceImpl
 * @generated
 */
public abstract class CommerceWishListServiceBaseImpl
	extends BaseServiceImpl
	implements CommerceWishListService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CommerceWishListService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>CommerceWishListServiceUtil</code>.
	 */

	/**
	 * Returns the commerce wish list local service.
	 *
	 * @return the commerce wish list local service
	 */
	public com.liferay.commerce.wish.list.service.CommerceWishListLocalService
		getCommerceWishListLocalService() {

		return commerceWishListLocalService;
	}

	/**
	 * Sets the commerce wish list local service.
	 *
	 * @param commerceWishListLocalService the commerce wish list local service
	 */
	public void setCommerceWishListLocalService(
		com.liferay.commerce.wish.list.service.CommerceWishListLocalService
			commerceWishListLocalService) {

		this.commerceWishListLocalService = commerceWishListLocalService;
	}

	/**
	 * Returns the commerce wish list remote service.
	 *
	 * @return the commerce wish list remote service
	 */
	public CommerceWishListService getCommerceWishListService() {
		return commerceWishListService;
	}

	/**
	 * Sets the commerce wish list remote service.
	 *
	 * @param commerceWishListService the commerce wish list remote service
	 */
	public void setCommerceWishListService(
		CommerceWishListService commerceWishListService) {

		this.commerceWishListService = commerceWishListService;
	}

	/**
	 * Returns the commerce wish list persistence.
	 *
	 * @return the commerce wish list persistence
	 */
	public CommerceWishListPersistence getCommerceWishListPersistence() {
		return commerceWishListPersistence;
	}

	/**
	 * Sets the commerce wish list persistence.
	 *
	 * @param commerceWishListPersistence the commerce wish list persistence
	 */
	public void setCommerceWishListPersistence(
		CommerceWishListPersistence commerceWishListPersistence) {

		this.commerceWishListPersistence = commerceWishListPersistence;
	}

	/**
	 * Returns the commerce wish list item local service.
	 *
	 * @return the commerce wish list item local service
	 */
	public
		com.liferay.commerce.wish.list.service.CommerceWishListItemLocalService
			getCommerceWishListItemLocalService() {

		return commerceWishListItemLocalService;
	}

	/**
	 * Sets the commerce wish list item local service.
	 *
	 * @param commerceWishListItemLocalService the commerce wish list item local service
	 */
	public void setCommerceWishListItemLocalService(
		com.liferay.commerce.wish.list.service.CommerceWishListItemLocalService
			commerceWishListItemLocalService) {

		this.commerceWishListItemLocalService =
			commerceWishListItemLocalService;
	}

	/**
	 * Returns the commerce wish list item remote service.
	 *
	 * @return the commerce wish list item remote service
	 */
	public com.liferay.commerce.wish.list.service.CommerceWishListItemService
		getCommerceWishListItemService() {

		return commerceWishListItemService;
	}

	/**
	 * Sets the commerce wish list item remote service.
	 *
	 * @param commerceWishListItemService the commerce wish list item remote service
	 */
	public void setCommerceWishListItemService(
		com.liferay.commerce.wish.list.service.CommerceWishListItemService
			commerceWishListItemService) {

		this.commerceWishListItemService = commerceWishListItemService;
	}

	/**
	 * Returns the commerce wish list item persistence.
	 *
	 * @return the commerce wish list item persistence
	 */
	public CommerceWishListItemPersistence
		getCommerceWishListItemPersistence() {

		return commerceWishListItemPersistence;
	}

	/**
	 * Sets the commerce wish list item persistence.
	 *
	 * @param commerceWishListItemPersistence the commerce wish list item persistence
	 */
	public void setCommerceWishListItemPersistence(
		CommerceWishListItemPersistence commerceWishListItemPersistence) {

		this.commerceWishListItemPersistence = commerceWishListItemPersistence;
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
	 * Returns the class name remote service.
	 *
	 * @return the class name remote service
	 */
	public com.liferay.portal.kernel.service.ClassNameService
		getClassNameService() {

		return classNameService;
	}

	/**
	 * Sets the class name remote service.
	 *
	 * @param classNameService the class name remote service
	 */
	public void setClassNameService(
		com.liferay.portal.kernel.service.ClassNameService classNameService) {

		this.classNameService = classNameService;
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
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.kernel.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.kernel.service.UserService userService) {

		this.userService = userService;
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
		_setServiceUtilService(commerceWishListService);
	}

	public void destroy() {
		_setServiceUtilService(null);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CommerceWishListService.class.getName();
	}

	protected Class<?> getModelClass() {
		return CommerceWishList.class;
	}

	protected String getModelClassName() {
		return CommerceWishList.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = commerceWishListPersistence.getDataSource();

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
		CommerceWishListService commerceWishListService) {

		try {
			Field field = CommerceWishListServiceUtil.class.getDeclaredField(
				"_service");

			field.setAccessible(true);

			field.set(null, commerceWishListService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@BeanReference(
		type = com.liferay.commerce.wish.list.service.CommerceWishListLocalService.class
	)
	protected
		com.liferay.commerce.wish.list.service.CommerceWishListLocalService
			commerceWishListLocalService;

	@BeanReference(type = CommerceWishListService.class)
	protected CommerceWishListService commerceWishListService;

	@BeanReference(type = CommerceWishListPersistence.class)
	protected CommerceWishListPersistence commerceWishListPersistence;

	@BeanReference(
		type = com.liferay.commerce.wish.list.service.CommerceWishListItemLocalService.class
	)
	protected
		com.liferay.commerce.wish.list.service.CommerceWishListItemLocalService
			commerceWishListItemLocalService;

	@BeanReference(
		type = com.liferay.commerce.wish.list.service.CommerceWishListItemService.class
	)
	protected com.liferay.commerce.wish.list.service.CommerceWishListItemService
		commerceWishListItemService;

	@BeanReference(type = CommerceWishListItemPersistence.class)
	protected CommerceWishListItemPersistence commerceWishListItemPersistence;

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

	@ServiceReference(
		type = com.liferay.portal.kernel.service.ClassNameService.class
	)
	protected com.liferay.portal.kernel.service.ClassNameService
		classNameService;

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

	@ServiceReference(
		type = com.liferay.portal.kernel.service.UserService.class
	)
	protected com.liferay.portal.kernel.service.UserService userService;

	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceWishListServiceBaseImpl.class);

}