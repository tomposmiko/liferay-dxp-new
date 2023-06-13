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

package com.liferay.feature.flag.web.internal.model;

import com.liferay.feature.flag.web.internal.constants.FeatureFlagConstants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;

import java.util.Locale;

/**
 * @author Drew Brokke
 */
public class FeatureFlagImpl implements FeatureFlag {

	public FeatureFlagImpl(String key) {
		this(
			GetterUtil.getStringValues(
				PropsUtil.getArray(
					FeatureFlagConstants.getKey(key, "dependencies"))),
			GetterUtil.getString(
				PropsUtil.get(FeatureFlagConstants.getKey(key, "description"))),
			GetterUtil.getBoolean(
				PropsUtil.get(FeatureFlagConstants.getKey(key))),
			FeatureFlagStatus.toFeatureFlagStatus(
				PropsUtil.get(FeatureFlagConstants.getKey(key, "status"))),
			key,
			GetterUtil.getString(
				PropsUtil.get(FeatureFlagConstants.getKey(key, "title")), key));
	}

	public FeatureFlagImpl(
		String[] dependencyKeys, String description, boolean enabled,
		FeatureFlagStatus featureFlagStatus, String key, String title) {

		_dependencyKeys = dependencyKeys;
		_description = description;
		_enabled = enabled;
		_featureFlagStatus = featureFlagStatus;
		_key = key;
		_title = title;
	}

	@Override
	public String[] getDependencyKeys() {
		return _dependencyKeys;
	}

	@Override
	public String getDescription(Locale locale) {
		return _description;
	}

	@Override
	public FeatureFlagStatus getFeatureFlagStatus() {
		return _featureFlagStatus;
	}

	@Override
	public String getKey() {
		return _key;
	}

	@Override
	public String getTitle(Locale locale) {
		return _title;
	}

	@Override
	public boolean isEnabled() {
		return _enabled;
	}

	private final String[] _dependencyKeys;
	private final String _description;
	private final boolean _enabled;
	private final FeatureFlagStatus _featureFlagStatus;
	private final String _key;
	private final String _title;

}