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

package com.liferay.data.cleanup.internal.upgrade;

import com.liferay.change.tracking.service.CTCollectionLocalService;
import com.liferay.change.tracking.service.CTEntryLocalService;
import com.liferay.change.tracking.store.service.CTSContentLocalService;
import com.liferay.data.cleanup.internal.configuration.DataRemovalConfiguration;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.service.ReleaseLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import java.util.Map;
import java.util.function.Supplier;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Preston Crary
 */
@Component(
	configurationPid = "com.liferay.data.cleanup.internal.configuration.DataRemovalConfiguration",
	immediate = true, service = UpgradeStepRegistrator.class
)
public class DataRemoval implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		try {
			_removeModuleData(
				_dataRemovalConfiguration::removeDLPreviewCTSContentData,
				"com.liferay.change.tracking.service",
				() -> new UpgradeDLPreviewCTSContentData(
					_ctCollectionLocalService, _ctEntryLocalService, _portal));

			_removeModuleData(
				_dataRemovalConfiguration::removePublishedCTSContentData,
				"com.liferay.change.tracking.store.service",
				() -> new UpgradePublishedCTSContentData(
					_ctsContentLocalService, _portal));
		}
		catch (UpgradeException upgradeException) {
			ReflectionUtil.throwException(upgradeException);
		}
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_dataRemovalConfiguration = ConfigurableUtil.createConfigurable(
			DataRemovalConfiguration.class, properties);
	}

	private void _removeModuleData(
			Supplier<Boolean> booleanSupplier, String servletContextName,
			Supplier<UpgradeProcess> upgradeProcessSupplier)
		throws UpgradeException {

		if (booleanSupplier.get()) {
			Release release = _releaseLocalService.fetchRelease(
				servletContextName);

			if (release != null) {
				UpgradeProcess upgradeProcess = upgradeProcessSupplier.get();

				upgradeProcess.upgrade();

				CacheRegistryUtil.clear();
			}
		}
	}

	@Reference
	private CTCollectionLocalService _ctCollectionLocalService;

	@Reference
	private CTEntryLocalService _ctEntryLocalService;

	@Reference
	private CTSContentLocalService _ctsContentLocalService;

	private DataRemovalConfiguration _dataRemovalConfiguration;

	@Reference
	private Portal _portal;

	@Reference
	private ReleaseLocalService _releaseLocalService;

}