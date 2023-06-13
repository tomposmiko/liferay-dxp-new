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

import com.liferay.portal.instance.lifecycle.Clusterable;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.CompanyConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
@Component(
	service = {
		CompanyFeatureFlagsProvider.class, PortalInstanceLifecycleListener.class
	}
)
public class CompanyFeatureFlagsProvider
	implements Clusterable, PortalInstanceLifecycleListener {

	public CompanyFeatureFlags getOrCreateCompanyFeatureFlags(long companyId) {
		return _companyFeatureFlagsMap.computeIfAbsent(
			companyId, _companyFeatureFlagsFactory::create);
	}

	@Override
	public void portalInstanceRegistered(Company company) {
		getOrCreateCompanyFeatureFlags(company.getCompanyId());
	}

	@Override
	public void portalInstanceUnregistered(Company company) {
		_companyFeatureFlagsMap.remove(company.getCompanyId());
	}

	public <T> T withCompanyFeatureFlags(
		long companyId, Function<CompanyFeatureFlags, T> function) {

		return function.apply(getOrCreateCompanyFeatureFlags(companyId));
	}

	@Activate
	protected void activate() {
		getOrCreateCompanyFeatureFlags(CompanyConstants.SYSTEM);
	}

	@Reference
	private CompanyFeatureFlagsFactory _companyFeatureFlagsFactory;

	private final Map<Long, CompanyFeatureFlags> _companyFeatureFlagsMap =
		new ConcurrentHashMap<>();

}