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

package com.liferay.portlet.documentlibrary.store;

import com.liferay.document.library.kernel.store.Store;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.change.tracking.store.CTStoreFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceRegistration;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;
import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerMap;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 * @author Manuel de la Peña
 * @author Edward C. Han
 */
public class StoreFactory {

	public static StoreFactory getInstance() {
		if (_storeFactory == null) {
			_storeFactory = new StoreFactory();
		}

		return _storeFactory;
	}

	public void checkProperties() {
		if (_warned) {
			return;
		}

		String dlHookImpl = PropsUtil.get("dl.hook.impl");

		if (Validator.isNull(dlHookImpl)) {
			_warned = true;

			return;
		}

		boolean found = false;

		for (String key : _storeServiceTrackerMapHolder.keySet()) {
			Store store = getStore(key);

			Class<?> clazz = store.getClass();

			String className = clazz.getName();

			if (dlHookImpl.equals(className)) {
				PropsValues.DL_STORE_IMPL = className;

				found = true;

				break;
			}
		}

		if (!found) {
			PropsValues.DL_STORE_IMPL = dlHookImpl;
		}

		if (_log.isWarnEnabled()) {
			StringBundler sb = new StringBundler(11);

			sb.append("Liferay is configured with the legacy property ");
			sb.append("\"dl.hook.impl=");
			sb.append(dlHookImpl);
			sb.append("\" in portal-ext.properties. Please reconfigure to ");
			sb.append("use the new property \"");
			sb.append(PropsKeys.DL_STORE_IMPL);
			sb.append("\". Liferay will attempt to temporarily set \"");
			sb.append(PropsKeys.DL_STORE_IMPL);
			sb.append("=");
			sb.append(PropsValues.DL_STORE_IMPL);
			sb.append("\".");

			_log.warn(sb.toString());
		}

		_warned = true;
	}

	public Store getStore() {
		Store store = _defaultStore;

		if (store == null) {
			throw new IllegalStateException(
				"Store is not available. Caller service needs to wait for " +
					"store factory with \"dl.store.impl.enabled=true\".");
		}

		return store;
	}

	public Store getStore(String key) {
		return _storeServiceTrackerMapHolder.getService(key);
	}

	public String[] getStoreTypes() {
		Set<String> storeTypes = _storeServiceTrackerMapHolder.keySet();

		return storeTypes.toArray(new String[0]);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), with no direct replacement
	 */
	@Deprecated
	public void setStore(String key) {
	}

	private static final Log _log = LogFactoryUtil.getLog(StoreFactory.class);

	private static volatile Store _defaultStore;
	private static StoreFactory _storeFactory;
	private static final StoreServiceTrackerMapHolder
		_storeServiceTrackerMapHolder = new StoreServiceTrackerMapHolder();
	private static boolean _warned;

	private static class CTStoreFactoryServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<CTStoreFactory, ServiceTrackerMap<String, Store>> {

		@Override
		public ServiceTrackerMap<String, Store> addingService(
			ServiceReference<CTStoreFactory> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			CTStoreFactory ctStoreFactory = registry.getService(
				serviceReference);

			return ServiceTrackerCollections.openSingleValueMap(
				Store.class, "store.type",
				new StoreTypeServiceTrackerCustomizer(ctStoreFactory));
		}

		@Override
		public void modifiedService(
			ServiceReference<CTStoreFactory> serviceReference,
			ServiceTrackerMap<String, Store> serviceTrackerMap) {
		}

		@Override
		public void removedService(
			ServiceReference<CTStoreFactory> serviceReference,
			ServiceTrackerMap<String, Store> serviceTrackerMap) {

			serviceTrackerMap.close();

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);
		}

	}

	private static class StoreServiceTrackerMapHolder {

		public StoreServiceTrackerMapHolder() {
			Registry registry = RegistryUtil.getRegistry();

			_serviceTracker = registry.trackServices(
				CTStoreFactory.class,
				new CTStoreFactoryServiceTrackerCustomizer());

			_serviceTracker.open();
		}

		public Store getService(String key) {
			ServiceTrackerMap<String, Store> serviceTrackerMap =
				_serviceTracker.getService();

			if (serviceTrackerMap == null) {
				return null;
			}

			return serviceTrackerMap.getService(key);
		}

		public Set<String> keySet() {
			ServiceTrackerMap<String, Store> serviceTrackerMap =
				_serviceTracker.getService();

			if (serviceTrackerMap == null) {
				return Collections.emptySet();
			}

			return serviceTrackerMap.keySet();
		}

		private final ServiceTracker
			<CTStoreFactory, ServiceTrackerMap<String, Store>> _serviceTracker;

	}

	private static class StoreTypeServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<Store, Store> {

		public StoreTypeServiceTrackerCustomizer(
			CTStoreFactory ctStoreFactory) {

			_ctStoreFactory = ctStoreFactory;
		}

		@Override
		public Store addingService(ServiceReference<Store> serviceReference) {
			String storeType = GetterUtil.getString(
				serviceReference.getProperty("store.type"));

			Store store = _getStore(serviceReference, storeType);

			if (StringUtil.equals(storeType, PropsValues.DL_STORE_IMPL)) {
				_defaultStore = store;

				Map<String, Object> properties =
					HashMapBuilder.<String, Object>put(
						"dl.store.impl.enabled", "true"
					).build();

				Registry registry = RegistryUtil.getRegistry();

				_serviceRegistration = registry.registerService(
					StoreFactory.class,
					new StoreFactory() {

						@Override
						public Store getStore() {
							return store;
						}

					},
					properties);
			}

			return store;
		}

		@Override
		public void modifiedService(
			ServiceReference<Store> serviceReference, Store service) {
		}

		@Override
		public void removedService(
			ServiceReference<Store> serviceReference, Store service) {

			String storeType = GetterUtil.getString(
				serviceReference.getProperty("store.type"));

			if (StringUtil.equals(storeType, PropsValues.DL_STORE_IMPL)) {
				_serviceRegistration.unregister();
			}

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);
		}

		private Store _getStore(
			ServiceReference<Store> serviceReference, String storeType) {

			Registry registry = RegistryUtil.getRegistry();

			Store store = registry.getService(serviceReference);

			if (!GetterUtil.getBoolean(
					serviceReference.getProperty("ct.aware"))) {

				store = _ctStoreFactory.createCTStore(store, storeType);
			}

			return store;
		}

		private final CTStoreFactory _ctStoreFactory;
		private ServiceRegistration<StoreFactory> _serviceRegistration;

	}

}