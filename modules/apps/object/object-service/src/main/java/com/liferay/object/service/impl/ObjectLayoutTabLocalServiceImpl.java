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

package com.liferay.object.service.impl;

import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationCategory;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.object.internal.layout.tab.screen.navigation.category.ObjectLayoutTabScreenNavigationCategory;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectLayoutTab;
import com.liferay.object.service.base.ObjectLayoutTabLocalServiceBaseImpl;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Feliphe Marinho
 */
@Component(
	property = "model.class.name=com.liferay.object.model.ObjectLayoutTab",
	service = AopService.class
)
public class ObjectLayoutTabLocalServiceImpl
	extends ObjectLayoutTabLocalServiceBaseImpl {

	@Override
	public ObjectLayoutTab addObjectLayoutTab(
			long userId, long objectLayoutId, long objectRelationshipId,
			Map<Locale, String> nameMap, int priority)
		throws PortalException {

		ObjectLayoutTab objectLayoutTab = objectLayoutTabPersistence.create(
			counterLocalService.increment());

		User user = _userLocalService.getUser(userId);

		objectLayoutTab.setCompanyId(user.getCompanyId());
		objectLayoutTab.setUserId(user.getUserId());
		objectLayoutTab.setUserName(user.getFullName());

		objectLayoutTab.setObjectLayoutId(objectLayoutId);
		objectLayoutTab.setObjectRelationshipId(objectRelationshipId);
		objectLayoutTab.setNameMap(nameMap);
		objectLayoutTab.setPriority(priority);

		return objectLayoutTabPersistence.update(objectLayoutTab);
	}

	@Override
	public void deleteObjectLayoutObjectLayoutTabs(long objectLayoutId)
		throws PortalException {

		for (ObjectLayoutTab objectLayoutTab :
				objectLayoutTabPersistence.findByObjectLayoutId(
					objectLayoutId)) {

			deleteObjectLayoutTab(objectLayoutTab);
		}
	}

	@Override
	public ObjectLayoutTab deleteObjectLayoutTab(long objectLayoutTabId)
		throws PortalException {

		ObjectLayoutTab objectLayoutTab =
			objectLayoutTabPersistence.findByPrimaryKey(objectLayoutTabId);

		return deleteObjectLayoutTab(objectLayoutTab);
	}

	@Override
	public ObjectLayoutTab deleteObjectLayoutTab(
		ObjectLayoutTab objectLayoutTab) {

		objectLayoutTabPersistence.remove(objectLayoutTab);

		ServiceRegistration<?> serviceRegistration = _serviceRegistrations.get(
			_getServiceRegistrationKey(objectLayoutTab));

		if (serviceRegistration != null) {
			serviceRegistration.unregister();

			_serviceRegistrations.remove(
				_getServiceRegistrationKey(objectLayoutTab));
		}

		return objectLayoutTab;
	}

	@Override
	public void deleteObjectRelationshipObjectLayoutTabs(
			long objectRelationshipId)
		throws PortalException {

		for (ObjectLayoutTab objectLayoutTab :
				objectLayoutTabPersistence.findByObjectRelationshipId(
					objectRelationshipId)) {

			deleteObjectLayoutTab(objectLayoutTab);
		}
	}

	@Override
	public List<ObjectLayoutTab> getObjectLayoutObjectLayoutTabs(
		long objectLayoutId) {

		return objectLayoutTabPersistence.findByObjectLayoutId(objectLayoutId);
	}

	@Override
	public void registerObjectLayoutTabScreenNavigationCategories(
		ObjectDefinition objectDefinition,
		List<ObjectLayoutTab> objectLayoutTabs) {

		for (int i = objectLayoutTabs.size() - 1; i >= 0; i--) {
			ObjectLayoutTab objectLayoutTab = objectLayoutTabs.get(i);

			_serviceRegistrations.computeIfAbsent(
				_getServiceRegistrationKey(objectLayoutTab),
				serviceRegistrationKey -> _bundleContext.registerService(
					new String[] {
						ScreenNavigationCategory.class.getName(),
						ScreenNavigationEntry.class.getName()
					},
					new ObjectLayoutTabScreenNavigationCategory(
						objectDefinition, objectLayoutTab),
					HashMapDictionaryBuilder.<String, Object>put(
						"screen.navigation.category.order:Integer",
						objectLayoutTab.getObjectLayoutTabId()
					).put(
						"screen.navigation.entry.order:Integer",
						objectLayoutTab.getObjectLayoutId()
					).build()));
		}
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	private String _getServiceRegistrationKey(ObjectLayoutTab objectLayoutTab) {
		return StringBundler.concat(
			objectLayoutTab.getCompanyId(), StringPool.POUND,
			objectLayoutTab.getObjectLayoutTabId());
	}

	private BundleContext _bundleContext;
	private final Map<String, ServiceRegistration<?>> _serviceRegistrations =
		new ConcurrentHashMap<>();

	@Reference
	private UserLocalService _userLocalService;

}