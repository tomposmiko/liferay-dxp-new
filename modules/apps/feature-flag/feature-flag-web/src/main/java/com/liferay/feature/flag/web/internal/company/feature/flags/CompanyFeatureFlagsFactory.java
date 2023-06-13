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

package com.liferay.feature.flag.web.internal.company.feature.flags;

import com.liferay.feature.flag.web.internal.constants.FeatureFlagConstants;
import com.liferay.feature.flag.web.internal.manager.FeatureFlagPreferencesManager;
import com.liferay.feature.flag.web.internal.model.DependencyAwareFeatureFlag;
import com.liferay.feature.flag.web.internal.model.FeatureFlag;
import com.liferay.feature.flag.web.internal.model.FeatureFlagImpl;
import com.liferay.feature.flag.web.internal.model.LanguageAwareFeatureFlag;
import com.liferay.feature.flag.web.internal.model.PreferenceAwareFeatureFlag;
import com.liferay.petra.lang.SafeCloseable;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PropsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
@Component(service = CompanyFeatureFlagsFactory.class)
public class CompanyFeatureFlagsFactory {

	public CompanyFeatureFlags create(long companyId) {
		try (SafeCloseable safeCloseable =
				CompanyThreadLocal.setWithSafeCloseable(companyId)) {

			if (!GetterUtil.getBoolean(
					PropsUtil.get(FeatureFlagConstants.getKey("LPS-167698")))) {

				return new CompanyFeatureFlags(Collections.emptyMap());
			}

			Map<String, FeatureFlag> featureFlagsMap = new HashMap<>();

			Properties properties = PropsUtil.getProperties(
				FeatureFlagConstants.FEATURE_FLAG + StringPool.PERIOD, true);

			for (String stringPropertyName : properties.stringPropertyNames()) {
				Matcher matcher = _pattern.matcher(stringPropertyName);

				if (!matcher.find()) {
					continue;
				}

				FeatureFlag featureFlag = new FeatureFlagImpl(
					stringPropertyName);

				featureFlag = new LanguageAwareFeatureFlag(
					featureFlag, _language);
				featureFlag = new PreferenceAwareFeatureFlag(
					companyId, featureFlag, _featureFlagPreferencesManager);

				featureFlagsMap.put(featureFlag.getKey(), featureFlag);
			}

			for (Map.Entry<String, FeatureFlag> entry :
					featureFlagsMap.entrySet()) {

				List<FeatureFlag> dependencyFeatureFlags = new ArrayList<>();

				FeatureFlag featureFlag = entry.getValue();

				for (String dependencyKey : featureFlag.getDependencyKeys()) {
					if (Objects.equals(featureFlag.getKey(), dependencyKey)) {
						_log.error(
							"A feature flag cannot depend on itself: " +
								dependencyKey);

						continue;
					}

					FeatureFlag dependencyFeatureFlag = featureFlagsMap.get(
						dependencyKey);

					if (dependencyFeatureFlag != null) {
						if (!ArrayUtil.contains(
								dependencyFeatureFlag.getDependencyKeys(),
								featureFlag.getKey())) {

							dependencyFeatureFlags.add(dependencyFeatureFlag);
						}
						else {
							_log.error(
								StringBundler.concat(
									"Skipping circular dependency ",
									dependencyKey, " for feature flag ",
									featureFlag.getKey()));
						}
					}
				}

				if (ListUtil.isNotEmpty(dependencyFeatureFlags)) {
					entry.setValue(
						new DependencyAwareFeatureFlag(
							featureFlag,
							dependencyFeatureFlags.toArray(
								new FeatureFlag[0])));
				}
			}

			return new CompanyFeatureFlags(
				Collections.unmodifiableMap(featureFlagsMap));
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CompanyFeatureFlagsFactory.class);

	private static final Pattern _pattern = Pattern.compile("^([A-Z\\-0-9]+)$");

	@Reference
	private FeatureFlagPreferencesManager _featureFlagPreferencesManager;

	@Reference
	private Language _language;

}