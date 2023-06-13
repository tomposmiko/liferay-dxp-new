/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.contacts.service.persistence;

import com.liferay.osb.faro.contacts.model.ContactsLayoutTemplate;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the contacts layout template service. This utility wraps <code>com.liferay.osb.faro.contacts.service.persistence.impl.ContactsLayoutTemplatePersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Shinn Lok
 * @see ContactsLayoutTemplatePersistence
 * @generated
 */
public class ContactsLayoutTemplateUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(
		ContactsLayoutTemplate contactsLayoutTemplate) {

		getPersistence().clearCache(contactsLayoutTemplate);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, ContactsLayoutTemplate> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<ContactsLayoutTemplate> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<ContactsLayoutTemplate> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<ContactsLayoutTemplate> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static ContactsLayoutTemplate update(
		ContactsLayoutTemplate contactsLayoutTemplate) {

		return getPersistence().update(contactsLayoutTemplate);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static ContactsLayoutTemplate update(
		ContactsLayoutTemplate contactsLayoutTemplate,
		ServiceContext serviceContext) {

		return getPersistence().update(contactsLayoutTemplate, serviceContext);
	}

	/**
	 * Returns all the contacts layout templates where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findByGroupId(long groupId) {
		return getPersistence().findByGroupId(groupId);
	}

	/**
	 * Returns a range of all the contacts layout templates where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ContactsLayoutTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of contacts layout templates
	 * @param end the upper bound of the range of contacts layout templates (not inclusive)
	 * @return the range of matching contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findByGroupId(
		long groupId, int start, int end) {

		return getPersistence().findByGroupId(groupId, start, end);
	}

	/**
	 * Returns an ordered range of all the contacts layout templates where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ContactsLayoutTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of contacts layout templates
	 * @param end the upper bound of the range of contacts layout templates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator) {

		return getPersistence().findByGroupId(
			groupId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the contacts layout templates where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ContactsLayoutTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of contacts layout templates
	 * @param end the upper bound of the range of contacts layout templates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByGroupId(
			groupId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first contacts layout template in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching contacts layout template
	 * @throws NoSuchContactsLayoutTemplateException if a matching contacts layout template could not be found
	 */
	public static ContactsLayoutTemplate findByGroupId_First(
			long groupId,
			OrderByComparator<ContactsLayoutTemplate> orderByComparator)
		throws com.liferay.osb.faro.contacts.exception.
			NoSuchContactsLayoutTemplateException {

		return getPersistence().findByGroupId_First(groupId, orderByComparator);
	}

	/**
	 * Returns the first contacts layout template in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching contacts layout template, or <code>null</code> if a matching contacts layout template could not be found
	 */
	public static ContactsLayoutTemplate fetchByGroupId_First(
		long groupId,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator) {

		return getPersistence().fetchByGroupId_First(
			groupId, orderByComparator);
	}

	/**
	 * Returns the last contacts layout template in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching contacts layout template
	 * @throws NoSuchContactsLayoutTemplateException if a matching contacts layout template could not be found
	 */
	public static ContactsLayoutTemplate findByGroupId_Last(
			long groupId,
			OrderByComparator<ContactsLayoutTemplate> orderByComparator)
		throws com.liferay.osb.faro.contacts.exception.
			NoSuchContactsLayoutTemplateException {

		return getPersistence().findByGroupId_Last(groupId, orderByComparator);
	}

	/**
	 * Returns the last contacts layout template in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching contacts layout template, or <code>null</code> if a matching contacts layout template could not be found
	 */
	public static ContactsLayoutTemplate fetchByGroupId_Last(
		long groupId,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator) {

		return getPersistence().fetchByGroupId_Last(groupId, orderByComparator);
	}

	/**
	 * Returns the contacts layout templates before and after the current contacts layout template in the ordered set where groupId = &#63;.
	 *
	 * @param contactsLayoutTemplateId the primary key of the current contacts layout template
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next contacts layout template
	 * @throws NoSuchContactsLayoutTemplateException if a contacts layout template with the primary key could not be found
	 */
	public static ContactsLayoutTemplate[] findByGroupId_PrevAndNext(
			long contactsLayoutTemplateId, long groupId,
			OrderByComparator<ContactsLayoutTemplate> orderByComparator)
		throws com.liferay.osb.faro.contacts.exception.
			NoSuchContactsLayoutTemplateException {

		return getPersistence().findByGroupId_PrevAndNext(
			contactsLayoutTemplateId, groupId, orderByComparator);
	}

	/**
	 * Removes all the contacts layout templates where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	public static void removeByGroupId(long groupId) {
		getPersistence().removeByGroupId(groupId);
	}

	/**
	 * Returns the number of contacts layout templates where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching contacts layout templates
	 */
	public static int countByGroupId(long groupId) {
		return getPersistence().countByGroupId(groupId);
	}

	/**
	 * Returns all the contacts layout templates where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the matching contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findByG_T(
		long groupId, int type) {

		return getPersistence().findByG_T(groupId, type);
	}

	/**
	 * Returns a range of all the contacts layout templates where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ContactsLayoutTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of contacts layout templates
	 * @param end the upper bound of the range of contacts layout templates (not inclusive)
	 * @return the range of matching contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findByG_T(
		long groupId, int type, int start, int end) {

		return getPersistence().findByG_T(groupId, type, start, end);
	}

	/**
	 * Returns an ordered range of all the contacts layout templates where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ContactsLayoutTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of contacts layout templates
	 * @param end the upper bound of the range of contacts layout templates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findByG_T(
		long groupId, int type, int start, int end,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator) {

		return getPersistence().findByG_T(
			groupId, type, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the contacts layout templates where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ContactsLayoutTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of contacts layout templates
	 * @param end the upper bound of the range of contacts layout templates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findByG_T(
		long groupId, int type, int start, int end,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByG_T(
			groupId, type, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first contacts layout template in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching contacts layout template
	 * @throws NoSuchContactsLayoutTemplateException if a matching contacts layout template could not be found
	 */
	public static ContactsLayoutTemplate findByG_T_First(
			long groupId, int type,
			OrderByComparator<ContactsLayoutTemplate> orderByComparator)
		throws com.liferay.osb.faro.contacts.exception.
			NoSuchContactsLayoutTemplateException {

		return getPersistence().findByG_T_First(
			groupId, type, orderByComparator);
	}

	/**
	 * Returns the first contacts layout template in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching contacts layout template, or <code>null</code> if a matching contacts layout template could not be found
	 */
	public static ContactsLayoutTemplate fetchByG_T_First(
		long groupId, int type,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator) {

		return getPersistence().fetchByG_T_First(
			groupId, type, orderByComparator);
	}

	/**
	 * Returns the last contacts layout template in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching contacts layout template
	 * @throws NoSuchContactsLayoutTemplateException if a matching contacts layout template could not be found
	 */
	public static ContactsLayoutTemplate findByG_T_Last(
			long groupId, int type,
			OrderByComparator<ContactsLayoutTemplate> orderByComparator)
		throws com.liferay.osb.faro.contacts.exception.
			NoSuchContactsLayoutTemplateException {

		return getPersistence().findByG_T_Last(
			groupId, type, orderByComparator);
	}

	/**
	 * Returns the last contacts layout template in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching contacts layout template, or <code>null</code> if a matching contacts layout template could not be found
	 */
	public static ContactsLayoutTemplate fetchByG_T_Last(
		long groupId, int type,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator) {

		return getPersistence().fetchByG_T_Last(
			groupId, type, orderByComparator);
	}

	/**
	 * Returns the contacts layout templates before and after the current contacts layout template in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param contactsLayoutTemplateId the primary key of the current contacts layout template
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next contacts layout template
	 * @throws NoSuchContactsLayoutTemplateException if a contacts layout template with the primary key could not be found
	 */
	public static ContactsLayoutTemplate[] findByG_T_PrevAndNext(
			long contactsLayoutTemplateId, long groupId, int type,
			OrderByComparator<ContactsLayoutTemplate> orderByComparator)
		throws com.liferay.osb.faro.contacts.exception.
			NoSuchContactsLayoutTemplateException {

		return getPersistence().findByG_T_PrevAndNext(
			contactsLayoutTemplateId, groupId, type, orderByComparator);
	}

	/**
	 * Removes all the contacts layout templates where groupId = &#63; and type = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 */
	public static void removeByG_T(long groupId, int type) {
		getPersistence().removeByG_T(groupId, type);
	}

	/**
	 * Returns the number of contacts layout templates where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the number of matching contacts layout templates
	 */
	public static int countByG_T(long groupId, int type) {
		return getPersistence().countByG_T(groupId, type);
	}

	/**
	 * Caches the contacts layout template in the entity cache if it is enabled.
	 *
	 * @param contactsLayoutTemplate the contacts layout template
	 */
	public static void cacheResult(
		ContactsLayoutTemplate contactsLayoutTemplate) {

		getPersistence().cacheResult(contactsLayoutTemplate);
	}

	/**
	 * Caches the contacts layout templates in the entity cache if it is enabled.
	 *
	 * @param contactsLayoutTemplates the contacts layout templates
	 */
	public static void cacheResult(
		List<ContactsLayoutTemplate> contactsLayoutTemplates) {

		getPersistence().cacheResult(contactsLayoutTemplates);
	}

	/**
	 * Creates a new contacts layout template with the primary key. Does not add the contacts layout template to the database.
	 *
	 * @param contactsLayoutTemplateId the primary key for the new contacts layout template
	 * @return the new contacts layout template
	 */
	public static ContactsLayoutTemplate create(long contactsLayoutTemplateId) {
		return getPersistence().create(contactsLayoutTemplateId);
	}

	/**
	 * Removes the contacts layout template with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param contactsLayoutTemplateId the primary key of the contacts layout template
	 * @return the contacts layout template that was removed
	 * @throws NoSuchContactsLayoutTemplateException if a contacts layout template with the primary key could not be found
	 */
	public static ContactsLayoutTemplate remove(long contactsLayoutTemplateId)
		throws com.liferay.osb.faro.contacts.exception.
			NoSuchContactsLayoutTemplateException {

		return getPersistence().remove(contactsLayoutTemplateId);
	}

	public static ContactsLayoutTemplate updateImpl(
		ContactsLayoutTemplate contactsLayoutTemplate) {

		return getPersistence().updateImpl(contactsLayoutTemplate);
	}

	/**
	 * Returns the contacts layout template with the primary key or throws a <code>NoSuchContactsLayoutTemplateException</code> if it could not be found.
	 *
	 * @param contactsLayoutTemplateId the primary key of the contacts layout template
	 * @return the contacts layout template
	 * @throws NoSuchContactsLayoutTemplateException if a contacts layout template with the primary key could not be found
	 */
	public static ContactsLayoutTemplate findByPrimaryKey(
			long contactsLayoutTemplateId)
		throws com.liferay.osb.faro.contacts.exception.
			NoSuchContactsLayoutTemplateException {

		return getPersistence().findByPrimaryKey(contactsLayoutTemplateId);
	}

	/**
	 * Returns the contacts layout template with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param contactsLayoutTemplateId the primary key of the contacts layout template
	 * @return the contacts layout template, or <code>null</code> if a contacts layout template with the primary key could not be found
	 */
	public static ContactsLayoutTemplate fetchByPrimaryKey(
		long contactsLayoutTemplateId) {

		return getPersistence().fetchByPrimaryKey(contactsLayoutTemplateId);
	}

	/**
	 * Returns all the contacts layout templates.
	 *
	 * @return the contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the contacts layout templates.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ContactsLayoutTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of contacts layout templates
	 * @param end the upper bound of the range of contacts layout templates (not inclusive)
	 * @return the range of contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the contacts layout templates.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ContactsLayoutTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of contacts layout templates
	 * @param end the upper bound of the range of contacts layout templates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findAll(
		int start, int end,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the contacts layout templates.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ContactsLayoutTemplateModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of contacts layout templates
	 * @param end the upper bound of the range of contacts layout templates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of contacts layout templates
	 */
	public static List<ContactsLayoutTemplate> findAll(
		int start, int end,
		OrderByComparator<ContactsLayoutTemplate> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the contacts layout templates from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of contacts layout templates.
	 *
	 * @return the number of contacts layout templates
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static ContactsLayoutTemplatePersistence getPersistence() {
		return _persistence;
	}

	private static volatile ContactsLayoutTemplatePersistence _persistence;

}