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

package com.liferay.portal.template;

import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheException;
import com.liferay.portal.kernel.cache.PortalCacheHelperUtil;
import com.liferay.portal.kernel.cache.PortalCacheListener;
import com.liferay.portal.kernel.cache.PortalCacheManagerNames;
import com.liferay.portal.kernel.cache.SingleVMPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceCache;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.ProxyFactory;

/**
 * @author Tina Tian
 */
public abstract class BaseTemplateResourceCache
	implements TemplateResourceCache {

	@Override
	public void clear() {
		if (!isEnabled()) {
			return;
		}

		_multiVMPortalCache.removeAll();
		_singleVMPortalCache.removeAll();
	}

	@Override
	public TemplateResource getTemplateResource(String templateId) {
		if (!isEnabled()) {
			return null;
		}

		TemplateResource templateResource = _singleVMPortalCache.get(
			templateId);

		if (templateResource == null) {
			templateResource = _multiVMPortalCache.get(templateId);
		}

		if ((templateResource != null) &&
			(templateResource != DUMMY_TEMPLATE_RESOURCE) &&
			(_modificationCheckInterval > 0)) {

			long expireTime =
				templateResource.getLastModified() + _modificationCheckInterval;

			if (System.currentTimeMillis() > expireTime) {
				remove(templateId);

				templateResource = null;

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Remove expired template resource " + templateId);
				}
			}
		}

		return templateResource;
	}

	@Override
	public boolean isEnabled() {
		if (_modificationCheckInterval == 0) {
			return false;
		}

		return true;
	}

	@Override
	public void put(String templateId, TemplateResource templateResource) {
		if (!isEnabled()) {
			return;
		}

		if (templateResource == null) {
			_singleVMPortalCache.put(templateId, DUMMY_TEMPLATE_RESOURCE);
		}
		else if (templateResource instanceof URLTemplateResource) {
			_singleVMPortalCache.put(
				templateId, new CacheTemplateResource(templateResource));
		}
		else if (templateResource instanceof CacheTemplateResource ||
				 templateResource instanceof StringTemplateResource) {

			_multiVMPortalCache.put(templateId, templateResource);
		}
		else {
			_multiVMPortalCache.put(
				templateId, new CacheTemplateResource(templateResource));
		}
	}

	@Override
	public void remove(String templateId) {
		if (!isEnabled()) {
			return;
		}

		_multiVMPortalCache.remove(templateId);
		_singleVMPortalCache.remove(templateId);
	}

	@Override
	public void setSecondLevelPortalCache(
		PortalCache<TemplateResource, ?> portalCache) {

		if (!isEnabled()) {
			return;
		}

		if (_templateResourcePortalCacheListener != null) {
			_multiVMPortalCache.unregisterPortalCacheListener(
				_templateResourcePortalCacheListener);
			_singleVMPortalCache.unregisterPortalCacheListener(
				_templateResourcePortalCacheListener);
		}

		_templateResourcePortalCacheListener =
			new TemplateResourcePortalCacheListener(portalCache);

		_multiVMPortalCache.registerPortalCacheListener(
			_templateResourcePortalCacheListener);
		_singleVMPortalCache.registerPortalCacheListener(
			_templateResourcePortalCacheListener);
	}

	protected void destroy() {
		if (!isEnabled()) {
			return;
		}

		PortalCacheHelperUtil.removePortalCache(
			PortalCacheManagerNames.MULTI_VM, _portalCacheName);

		_multiVMPortalCache = null;

		PortalCacheHelperUtil.removePortalCache(
			PortalCacheManagerNames.SINGLE_VM, _portalCacheName);

		_singleVMPortalCache = null;

		_templateResourcePortalCacheListener = null;
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x), replaced by {@link
	 *             #init(long, String)}
	 */
	@Deprecated
	protected void init(
		long modificationCheckInterval, MultiVMPool multiVMPool,
		SingleVMPool singleVMPool, String portalCacheName) {

		init(modificationCheckInterval, portalCacheName);
	}

	protected void init(
		long modificationCheckInterval, String portalCacheName) {

		_modificationCheckInterval = modificationCheckInterval;
		_portalCacheName = portalCacheName;

		if (isEnabled()) {
			_multiVMPortalCache = PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.MULTI_VM, portalCacheName);
			_singleVMPortalCache = PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.SINGLE_VM, portalCacheName);
		}
	}

	protected static final TemplateResource DUMMY_TEMPLATE_RESOURCE =
		ProxyFactory.newDummyInstance(TemplateResource.class);

	private static final Log _log = LogFactoryUtil.getLog(
		BaseTemplateResourceCache.class);

	private volatile long _modificationCheckInterval;
	private volatile PortalCache<String, TemplateResource> _multiVMPortalCache;
	private String _portalCacheName;
	private volatile PortalCache<String, TemplateResource> _singleVMPortalCache;
	private volatile TemplateResourcePortalCacheListener
		_templateResourcePortalCacheListener;

	private class TemplateResourcePortalCacheListener
		implements PortalCacheListener<String, TemplateResource> {

		@Override
		public void dispose() {
		}

		@Override
		public void notifyEntryEvicted(
				PortalCache<String, TemplateResource> portalCache, String key,
				TemplateResource templateResource, int timeToLive)
			throws PortalCacheException {

			if (templateResource != null) {
				_portalCache.remove(templateResource);
			}
		}

		@Override
		public void notifyEntryExpired(
				PortalCache<String, TemplateResource> portalCache, String key,
				TemplateResource templateResource, int timeToLive)
			throws PortalCacheException {

			if (templateResource != null) {
				_portalCache.remove(templateResource);
			}
		}

		@Override
		public void notifyEntryPut(
				PortalCache<String, TemplateResource> portalCache, String key,
				TemplateResource templateResource, int timeToLive)
			throws PortalCacheException {
		}

		@Override
		public void notifyEntryRemoved(
				PortalCache<String, TemplateResource> portalCache, String key,
				TemplateResource templateResource, int timeToLive)
			throws PortalCacheException {

			if (templateResource != null) {
				_portalCache.remove(templateResource);
			}
		}

		@Override
		public void notifyEntryUpdated(
				PortalCache<String, TemplateResource> portalCache, String key,
				TemplateResource templateResource, int timeToLive)
			throws PortalCacheException {

			if (templateResource != null) {
				_portalCache.remove(templateResource);
			}
		}

		@Override
		public void notifyRemoveAll(
				PortalCache<String, TemplateResource> portalCache)
			throws PortalCacheException {

			_portalCache.removeAll();
		}

		private TemplateResourcePortalCacheListener(
			PortalCache<TemplateResource, ?> portalCache) {

			_portalCache = portalCache;
		}

		private final PortalCache<TemplateResource, ?> _portalCache;

	}

}