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

package com.liferay.asset.categories.internal.configuration.provider;

import com.liferay.asset.categories.configuration.AssetCategoriesCompanyConfiguration;
import com.liferay.asset.kernel.configuration.provider.AssetCategoryConfigurationProvider;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = AssetCategoryConfigurationProvider.class)
public class AssetCategoryConfigurationProviderImpl
	implements AssetCategoryConfigurationProvider {

	@Override
	public boolean isSearchHierarchical(long companyId)
		throws ConfigurationException {

		AssetCategoriesCompanyConfiguration
			assetCategoriesCompanyConfiguration =
				_configurationProvider.getCompanyConfiguration(
					AssetCategoriesCompanyConfiguration.class, companyId);

		return assetCategoriesCompanyConfiguration.
			includeChildrenCategoriesWhenSearchingParentCategories();
	}

	@Reference
	private ConfigurationProvider _configurationProvider;

}