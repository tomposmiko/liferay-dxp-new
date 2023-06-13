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

package com.liferay.portal.language;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoader;
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoaderUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.ServiceProxyFactory;
import com.liferay.portal.kernel.util.SetUtil;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Shuyang Zhou
 * @author Kamesh Sampath
 */
public class LanguageResources {

	public static ResourceBundleLoader PORTAL_RESOURCE_BUNDLE_LOADER =
		new ResourceBundleLoader() {

			@Override
			public ResourceBundle loadResourceBundle(Locale locale) {
				return LanguageResources.getResourceBundle(locale);
			}

		};

	public static String getMessage(Locale locale, String key) {
		if (locale == null) {
			return null;
		}

		String overrideValue = _getOverrideValue(key, locale);

		if (overrideValue != null) {
			return overrideValue;
		}

		String value = null;

		MapHolder mapHolder = _serviceTrackerMap.getService(locale);

		if (mapHolder != null) {
			Map<String, String> languageMap = mapHolder.getMap();

			value = languageMap.get(key);
		}

		if (value == null) {
			return getMessage(getSuperLocale(locale), key);
		}

		return value;
	}

	public static ResourceBundle getResourceBundle(Locale locale) {
		return new LanguageResourcesBundle(locale);
	}

	public static Locale getSuperLocale(Locale locale) {
		Locale superLocale = _superLocales.get(locale);

		if (superLocale != null) {
			if (superLocale == _nullLocale) {
				return null;
			}

			return superLocale;
		}

		superLocale = _getSuperLocale(locale);

		if (superLocale == null) {
			_superLocales.put(locale, _nullLocale);
		}
		else {
			_superLocales.put(locale, superLocale);
		}

		return superLocale;
	}

	public void afterPropertiesSet() {
		ResourceBundleLoaderUtil.setPortalResourceBundleLoader(
			PORTAL_RESOURCE_BUNDLE_LOADER);
	}

	public void destroy() {
		_serviceTrackerMap.close();
	}

	private static String _getOverrideValue(String key, Locale locale) {
		LanguageOverrideProvider languageOverrideProvider =
			_languageOverrideProvider;

		if (languageOverrideProvider == null) {
			return null;
		}

		String value = languageOverrideProvider.get(key, locale);

		if (value == null) {
			return null;
		}

		return value;
	}

	private static Set<String> _getSetWithOverrideKeys(
		Set<String> keySet, Locale locale) {

		LanguageOverrideProvider languageOverrideProvider =
			_languageOverrideProvider;

		if (languageOverrideProvider == null) {
			return keySet;
		}

		Set<String> overrideKeySet = languageOverrideProvider.keySet(locale);

		if (SetUtil.isEmpty(overrideKeySet)) {
			return keySet;
		}

		Set<String> resultSet = new HashSet<>(keySet);

		resultSet.addAll(overrideKeySet);

		return resultSet;
	}

	private static Locale _getSuperLocale(Locale locale) {
		String variant = locale.getVariant();

		if (variant.length() > 0) {
			return new Locale(locale.getLanguage(), locale.getCountry());
		}

		String country = locale.getCountry();

		if (country.length() > 0) {
			Locale priorityLocale = LanguageUtil.getLocale(
				locale.getLanguage());

			if (priorityLocale != null) {
				variant = priorityLocale.getVariant();
			}

			if ((priorityLocale != null) && !locale.equals(priorityLocale) &&
				(variant.length() <= 0)) {

				return new Locale(
					priorityLocale.getLanguage(), priorityLocale.getCountry());
			}

			return LocaleUtil.fromLanguageId(locale.getLanguage(), false, true);
		}

		String language = locale.getLanguage();

		if (language.length() > 0) {
			return _blankLocale;
		}

		return null;
	}

	private static final Locale _blankLocale = new Locale(StringPool.BLANK);
	private static volatile LanguageOverrideProvider _languageOverrideProvider =
		ServiceProxyFactory.newServiceTrackedInstance(
			LanguageOverrideProvider.class, LanguageResources.class,
			"_languageOverrideProvider", false, true);
	private static final Locale _nullLocale = new Locale(StringPool.BLANK);
	private static final ServiceTrackerMap<Locale, MapHolder>
		_serviceTrackerMap;
	private static final Map<Locale, Locale> _superLocales =
		new ConcurrentHashMap<>();

	private static class LanguageResourcesBundle extends ResourceBundle {

		@Override
		public Enumeration<String> getKeys() {
			Set<String> keySet = Collections.emptySet();

			MapHolder mapHolder = _serviceTrackerMap.getService(_locale);

			if (mapHolder != null) {
				Map<String, String> languageMap = mapHolder.getMap();

				keySet = languageMap.keySet();
			}

			keySet = _getSetWithOverrideKeys(keySet, _locale);

			if (parent == null) {
				return Collections.enumeration(keySet);
			}

			return new ResourceBundleEnumeration(keySet, parent.getKeys());
		}

		@Override
		public Locale getLocale() {
			return _locale;
		}

		@Override
		protected Object handleGetObject(String key) {
			String overrideValue = _getOverrideValue(key, _locale);

			if (overrideValue != null) {
				return overrideValue;
			}

			MapHolder mapHolder = _serviceTrackerMap.getService(_locale);

			if (mapHolder == null) {
				return null;
			}

			Map<String, String> languageMap = mapHolder.getMap();

			return languageMap.get(key);
		}

		@Override
		protected Set<String> handleKeySet() {
			Set<String> keySet = Collections.emptySet();

			MapHolder mapHolder = _serviceTrackerMap.getService(_locale);

			if (mapHolder != null) {
				Map<String, String> languageMap = mapHolder.getMap();

				keySet = languageMap.keySet();
			}

			return _getSetWithOverrideKeys(keySet, _locale);
		}

		private LanguageResourcesBundle(Locale locale) {
			_locale = locale;

			Locale superLocale = getSuperLocale(locale);

			if (superLocale != null) {
				setParent(new LanguageResourcesBundle(superLocale));
			}
		}

		private final Locale _locale;

	}

	private static class MapHolder {

		public void close() {
			if (_map != null) {
				_bundleContext.ungetService(_serviceReference);
			}
		}

		public Map<String, String> getMap() {
			if (_map == null) {
				ResourceBundle resourceBundle = _bundleContext.getService(
					_serviceReference);

				Map<String, String> languageMap = new HashMap<>();

				Enumeration<String> enumeration = resourceBundle.getKeys();

				while (enumeration.hasMoreElements()) {
					String key = enumeration.nextElement();

					String value = ResourceBundleUtil.getString(
						resourceBundle, key);

					languageMap.put(key, value);
				}

				_map = languageMap;
			}

			return _map;
		}

		private MapHolder(
			BundleContext bundleContext,
			ServiceReference<ResourceBundle> serviceReference) {

			_bundleContext = bundleContext;
			_serviceReference = serviceReference;
		}

		private final BundleContext _bundleContext;
		private Map<String, String> _map;
		private final ServiceReference<ResourceBundle> _serviceReference;

	}

	static {
		BundleContext bundleContext = SystemBundleUtil.getBundleContext();

		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, ResourceBundle.class,
			"(&(!(javax.portlet.name=*))(language.id=*))",
			(serviceReference, emitter) -> {
				String languageId = GetterUtil.getString(
					serviceReference.getProperty("language.id"));

				emitter.emit(LocaleUtil.fromLanguageId(languageId, false));
			},
			new ServiceTrackerCustomizer<ResourceBundle, MapHolder>() {

				@Override
				public MapHolder addingService(
					ServiceReference<ResourceBundle> serviceReference) {

					return new MapHolder(bundleContext, serviceReference);
				}

				@Override
				public void modifiedService(
					ServiceReference<ResourceBundle> serviceReference,
					MapHolder mapHolder) {
				}

				@Override
				public void removedService(
					ServiceReference<ResourceBundle> serviceReference,
					MapHolder mapHolder) {

					mapHolder.close();
				}

			});
	}

}