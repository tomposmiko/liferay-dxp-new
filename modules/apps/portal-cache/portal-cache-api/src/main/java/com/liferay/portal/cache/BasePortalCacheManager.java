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

package com.liferay.portal.cache;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.cache.configuration.PortalCacheConfiguration;
import com.liferay.portal.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheException;
import com.liferay.portal.kernel.cache.PortalCacheListener;
import com.liferay.portal.kernel.cache.PortalCacheListenerScope;
import com.liferay.portal.kernel.cache.PortalCacheManager;
import com.liferay.portal.kernel.cache.PortalCacheManagerListener;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Tina Tian
 */
public abstract class BasePortalCacheManager<K extends Serializable, V>
	implements PortalCacheManager<K, V> {

	@Override
	public void clearAll() throws PortalCacheException {
		doClearAll();
	}

	@Override
	public void destroy() {
		_portalCaches.clear();

		doDestroy();
	}

	@Override
	public PortalCache<K, V> fetchPortalCache(String portalCacheName) {
		return _portalCaches.get(portalCacheName);
	}

	@Override
	public PortalCache<K, V> getPortalCache(String portalCacheName)
		throws PortalCacheException {

		return getPortalCache(portalCacheName, false);
	}

	@Override
	public PortalCache<K, V> getPortalCache(
			String portalCacheName, boolean mvcc)
		throws PortalCacheException {

		return getPortalCache(portalCacheName, mvcc, false);
	}

	@Override
	public PortalCache<K, V> getPortalCache(
			String portalCacheName, boolean mvcc, boolean sharded)
		throws PortalCacheException {

		return _portalCaches.compute(
			portalCacheName,
			(key, value) -> {
				if (value != null) {
					_verifyMVCCPortalCache(value, mvcc);
					_verifyShardedPortalCache(value, sharded);

					return value;
				}

				PortalCacheConfiguration portalCacheConfiguration =
					_portalCacheManagerConfiguration.
						getPortalCacheConfiguration(portalCacheName);

				value = createPortalCache(portalCacheConfiguration, sharded);

				_initPortalCacheListeners(value, portalCacheConfiguration);

				if (mvcc) {
					value = (PortalCache<K, V>)new MVCCPortalCache<>(
						(LowLevelCache<K, MVCCModel>)value);
				}

				if (isTransactionalPortalCacheEnabled() &&
					isTransactionalPortalCache(portalCacheName)) {

					value = new TransactionalPortalCache<>(value, mvcc);
				}

				return value;
			});
	}

	@Override
	public Set<PortalCacheManagerListener> getPortalCacheManagerListeners() {
		return aggregatedPortalCacheManagerListener.
			getPortalCacheManagerListeners();
	}

	@Override
	public String getPortalCacheManagerName() {
		return _portalCacheManagerName;
	}

	public String[] getTransactionalPortalCacheNames() {
		return _transactionalPortalCacheNames;
	}

	public boolean isTransactionalPortalCacheEnabled() {
		return _transactionalPortalCacheEnabled;
	}

	@Override
	public boolean registerPortalCacheManagerListener(
		PortalCacheManagerListener portalCacheManagerListener) {

		return aggregatedPortalCacheManagerListener.addPortalCacheListener(
			portalCacheManagerListener);
	}

	@Override
	public void removePortalCache(String portalCacheName) {
		doRemovePortalCache(_portalCaches.remove(portalCacheName));
	}

	@Override
	public void removePortalCaches(long companyId) {
		Set<PortalCache<K, V>> shardedPortalCaches = new HashSet<>();

		for (PortalCache<K, V> portalCache : _portalCaches.values()) {
			if (portalCache.isSharded()) {
				shardedPortalCaches.add(portalCache);
			}
		}

		if (shardedPortalCaches.isEmpty()) {
			return;
		}

		doRemoveShardedPortalCache(companyId, shardedPortalCaches);
	}

	public void setPortalCacheManagerName(String portalCacheManagerName) {
		_portalCacheManagerName = portalCacheManagerName;
	}

	public void setTransactionalPortalCacheEnabled(
		boolean transactionalPortalCacheEnabled) {

		_transactionalPortalCacheEnabled = transactionalPortalCacheEnabled;
	}

	public void setTransactionalPortalCacheNames(
		String[] transactionalPortalCacheNames) {

		_transactionalPortalCacheNames = transactionalPortalCacheNames;
	}

	@Override
	public boolean unregisterPortalCacheManagerListener(
		PortalCacheManagerListener portalCacheManagerListener) {

		return aggregatedPortalCacheManagerListener.removePortalCacheListener(
			portalCacheManagerListener);
	}

	@Override
	public void unregisterPortalCacheManagerListeners() {
		aggregatedPortalCacheManagerListener.clearAll();
	}

	protected abstract PortalCache<K, V> createPortalCache(
		PortalCacheConfiguration portalCacheConfiguration, boolean sharded);

	protected abstract void doClearAll();

	protected abstract void doDestroy();

	protected abstract void doRemovePortalCache(PortalCache<K, V> portalCache);

	protected abstract void doRemoveShardedPortalCache(
		long companyId, Set<PortalCache<K, V>> shardedPortalCaches);

	protected abstract PortalCacheManagerConfiguration
		getPortalCacheManagerConfiguration();

	protected void initialize() {
		if (_portalCacheManagerConfiguration != null) {
			return;
		}

		if (Validator.isNull(_portalCacheManagerName)) {
			throw new IllegalArgumentException(
				"Portal cache manager name is not specified");
		}

		initPortalCacheManager();

		_portalCacheManagerConfiguration = getPortalCacheManagerConfiguration();

		for (Properties properties :
				_portalCacheManagerConfiguration.
					getPortalCacheManagerListenerPropertiesSet()) {

			PortalCacheManagerListener portalCacheManagerListener =
				portalCacheManagerListenerFactory.create(this, properties);

			if (portalCacheManagerListener != null) {
				registerPortalCacheManagerListener(portalCacheManagerListener);
			}
		}
	}

	protected abstract void initPortalCacheManager();

	protected boolean isTransactionalPortalCache(String portalCacheName) {
		for (String namePattern : getTransactionalPortalCacheNames()) {
			if (StringUtil.wildcardMatches(
					portalCacheName, namePattern, CharPool.QUESTION,
					CharPool.STAR, CharPool.PERCENT, true)) {

				return true;
			}
		}

		return false;
	}

	protected void reconfigPortalCache(
		PortalCacheManagerConfiguration portalCacheManagerConfiguration) {

		for (String portalCacheName :
				portalCacheManagerConfiguration.getPortalCacheNames()) {

			PortalCacheConfiguration portalCacheConfiguration =
				portalCacheManagerConfiguration.getPortalCacheConfiguration(
					portalCacheName);

			_portalCacheManagerConfiguration.putPortalCacheConfiguration(
				portalCacheName, portalCacheConfiguration);

			PortalCache<K, V> portalCache = _portalCaches.get(portalCacheName);

			if (portalCache == null) {
				continue;
			}

			removeConfigurableEhcachePortalCacheListeners(portalCache);

			_initPortalCacheListeners(portalCache, portalCacheConfiguration);
		}
	}

	protected abstract void removeConfigurableEhcachePortalCacheListeners(
		PortalCache<K, V> portalCache);

	protected final AggregatedPortalCacheManagerListener
		aggregatedPortalCacheManagerListener =
			new AggregatedPortalCacheManagerListener();
	protected PortalCacheListenerFactory portalCacheListenerFactory;
	protected PortalCacheManagerListenerFactory<PortalCacheManager<K, V>>
		portalCacheManagerListenerFactory;

	private void _initPortalCacheListeners(
		PortalCache<K, V> portalCache,
		PortalCacheConfiguration portalCacheConfiguration) {

		if (portalCacheConfiguration == null) {
			return;
		}

		for (Properties properties :
				portalCacheConfiguration.
					getPortalCacheListenerPropertiesSet()) {

			PortalCacheListener<K, V> portalCacheListener =
				portalCacheListenerFactory.create(properties);

			if (portalCacheListener == null) {
				continue;
			}

			PortalCacheListenerScope portalCacheListenerScope =
				(PortalCacheListenerScope)properties.remove(
					PortalCacheConfiguration.
						PORTAL_CACHE_LISTENER_PROPERTIES_KEY_SCOPE);

			if (portalCacheListenerScope == null) {
				portalCacheListenerScope = PortalCacheListenerScope.ALL;
			}

			portalCache.registerPortalCacheListener(
				portalCacheListener, portalCacheListenerScope);
		}
	}

	private void _verifyMVCCPortalCache(
		PortalCache<K, V> portalCache, boolean mvcc) {

		if (mvcc == portalCache.isMVCC()) {
			return;
		}

		StringBundler sb = new StringBundler(9);

		sb.append("Unable to get portal cache ");
		sb.append(portalCache.getPortalCacheName());
		sb.append(" from portal cache manager ");
		sb.append(_portalCacheManagerName);
		sb.append(" as a ");

		if (mvcc) {
			sb.append("MVCC ");
		}
		else {
			sb.append("non-MVCC ");
		}

		sb.append("portal cache, cause a ");

		if (portalCache.isMVCC()) {
			sb.append("MVCC ");
		}
		else {
			sb.append("non-MVCC ");
		}

		sb.append("portal cache with same name exists.");

		throw new IllegalStateException(sb.toString());
	}

	private void _verifyShardedPortalCache(
		PortalCache<K, V> portalCache, boolean sharded) {

		if (sharded == portalCache.isSharded()) {
			return;
		}

		StringBundler sb = new StringBundler(9);

		sb.append("Unable to get portal cache ");
		sb.append(portalCache.getPortalCacheName());
		sb.append(" from portal cache manager ");
		sb.append(_portalCacheManagerName);
		sb.append(" as a ");

		if (sharded) {
			sb.append("sharded ");
		}
		else {
			sb.append("nonsharded ");
		}

		sb.append("portal cache, cause a ");

		if (portalCache.isSharded()) {
			sb.append("sharded ");
		}
		else {
			sb.append("nonsharded ");
		}

		sb.append("portal cache with same name exists.");

		throw new IllegalStateException(sb.toString());
	}

	private PortalCacheManagerConfiguration _portalCacheManagerConfiguration;
	private String _portalCacheManagerName;
	private final ConcurrentMap<String, PortalCache<K, V>> _portalCaches =
		new ConcurrentHashMap<>();
	private boolean _transactionalPortalCacheEnabled;
	private String[] _transactionalPortalCacheNames = StringPool.EMPTY_ARRAY;

}