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

package com.liferay.portal.upgrade.internal.release;

import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceComparator;
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapListener;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.service.ReleaseLocalService;
import com.liferay.portal.kernel.upgrade.ReleaseManager;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.upgrade.util.UpgradeProcessUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.version.Version;
import com.liferay.portal.osgi.debug.SystemChecker;
import com.liferay.portal.upgrade.PortalUpgradeProcess;
import com.liferay.portal.upgrade.internal.executor.UpgradeExecutor;
import com.liferay.portal.upgrade.internal.graph.ReleaseGraphManager;
import com.liferay.portal.upgrade.internal.registry.UpgradeInfo;
import com.liferay.portal.upgrade.internal.registry.UpgradeStepRegistratorThreadLocal;
import com.liferay.portal.util.PropsValues;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Alberto Chaparro
 * @author Samuel Ziemer
 */
@Component(service = {ReleaseManager.class, ReleaseManagerImpl.class})
public class ReleaseManagerImpl implements ReleaseManager {

	public Set<String> getBundleSymbolicNames() {
		return _serviceTrackerMap.keySet();
	}

	public String getSchemaVersionString(String bundleSymbolicName) {
		Release release = _releaseLocalService.fetchRelease(bundleSymbolicName);

		if ((release != null) &&
			Validator.isNotNull(release.getSchemaVersion())) {

			return release.getSchemaVersion();
		}

		return "0.0.0";
	}

	@Override
	public String getShortStatusMessage(boolean onlyRequiredUpgrades) {
		String message =
			"%s upgrades in %s are pending. Run the upgrade process or type " +
				"upgrade:checkAll in the Gogo shell to get more information.";

		if (onlyRequiredUpgrades) {
			if (_isPendingRequiredModuleUpgrades()) {
				return String.format(message, "Required", "modules");
			}

			return StringPool.BLANK;
		}

		String where = StringPool.BLANK;

		try (Connection connection = DataAccess.getConnection()) {
			if (!PortalUpgradeProcess.isInLatestSchemaVersion(connection)) {
				where = "portal";
			}
		}
		catch (SQLException sqlException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to get pending upgrade information for the portal",
					sqlException);
			}
		}

		if (_isPendingModuleUpgrades()) {
			if (Validator.isNotNull(where)) {
				where = "and " + where;
			}
			else {
				where = "modules";
			}
		}

		if (Validator.isNotNull(where)) {
			return String.format(message, "Optional", where);
		}

		return StringPool.BLANK;
	}

	@Override
	public String getStatusMessage(boolean showUpgradeSteps) {
		StringBundler sb = new StringBundler(6);

		sb.append(_checkPortal(showUpgradeSteps));

		if (sb.length() > 0) {
			sb.append(StringPool.NEW_LINE);
		}

		sb.append(_checkModules(showUpgradeSteps));

		if (!_hasUnsatisfiedUpgradeComponents()) {
			sb.append("Unsatisfied components prevent upgrade processes to ");
			sb.append("be registered");

			sb.append(StringPool.NEW_LINE);
		}

		return sb.toString();
	}

	public Set<String> getUpgradableBundleSymbolicNames() {
		Set<String> upgradableBundleSymbolicNames = new HashSet<>();

		for (String bundleSymbolicName : getBundleSymbolicNames()) {
			if (_isUpgradable(bundleSymbolicName)) {
				upgradableBundleSymbolicNames.add(bundleSymbolicName);
			}
		}

		return upgradableBundleSymbolicNames;
	}

	public List<UpgradeInfo> getUpgradeInfos(String bundleSymbolicName) {
		return _serviceTrackerMap.getService(bundleSymbolicName);
	}

	@Override
	public boolean isUpgraded() throws Exception {
		try (Connection connection = DataAccess.getConnection()) {
			if (!PortalUpgradeProcess.isInLatestSchemaVersion(connection) ||
				_isPendingModuleUpgrades()) {

				return false;
			}
		}

		return _hasUnsatisfiedUpgradeComponents();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openMultiValueMap(
			bundleContext, UpgradeStep.class, null,
			new PropertyServiceReferenceMapper<String, UpgradeStep>(
				"upgrade.bundle.symbolic.name"),
			new ReleaseManagerImpl.UpgradeServiceTrackerCustomizer(
				bundleContext),
			Collections.reverseOrder(
				new PropertyServiceReferenceComparator<UpgradeStep>(
					"upgrade.from.schema.version")),
			new ReleaseManagerImpl.UpgradeInfoServiceTrackerMapListener());

		synchronized (this) {
			Set<String> bundleSymbolicNames = null;

			if (!PropsValues.UPGRADE_DATABASE_AUTO_RUN) {
				bundleSymbolicNames = new HashSet<>();

				for (Release release :
						_releaseLocalService.getReleases(
							QueryUtil.ALL_POS, QueryUtil.ALL_POS)) {

					bundleSymbolicNames.add(release.getBundleSymbolicName());
				}
			}

			for (String bundleSymbolicName : _serviceTrackerMap.keySet()) {
				if ((bundleSymbolicNames != null) &&
					bundleSymbolicNames.contains(bundleSymbolicName)) {

					continue;
				}

				List<UpgradeInfo> upgradeSteps = _serviceTrackerMap.getService(
					bundleSymbolicName);

				try {
					_upgradeExecutor.execute(bundleSymbolicName, upgradeSteps);
				}
				catch (Throwable throwable) {
					_log.error(
						"Failed upgrade process for module " +
							bundleSymbolicName,
						throwable);
				}
			}

			_activated = true;
		}
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private String _checkModules(boolean showUpgradeSteps) {
		StringBundler sb = new StringBundler();

		Set<String> bundleSymbolicNames = getBundleSymbolicNames();

		for (String bundleSymbolicName : bundleSymbolicNames) {
			String schemaVersionString = getSchemaVersionString(
				bundleSymbolicName);

			ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
				getUpgradeInfos(bundleSymbolicName));

			List<List<UpgradeInfo>> upgradeInfosList =
				releaseGraphManager.getUpgradeInfosList(schemaVersionString);

			int size = upgradeInfosList.size();

			if (size > 1) {
				sb.append("There are ");
				sb.append(size);
				sb.append(" possible end nodes for ");
				sb.append(schemaVersionString);
				sb.append(StringPool.NEW_LINE);
			}

			if (size == 0) {
				continue;
			}

			List<UpgradeInfo> upgradeInfos = upgradeInfosList.get(0);

			UpgradeInfo lastUpgradeInfo = upgradeInfos.get(
				upgradeInfos.size() - 1);

			sb.append(
				_getModulePendingUpgradeMessage(
					bundleSymbolicName, schemaVersionString,
					lastUpgradeInfo.getToSchemaVersionString()));

			if (showUpgradeSteps) {
				sb.append(StringPool.COLON);

				for (UpgradeInfo upgradeInfo : upgradeInfos) {
					UpgradeStep upgradeStep = upgradeInfo.getUpgradeStep();

					sb.append(StringPool.NEW_LINE);
					sb.append(StringPool.TAB);
					sb.append(
						_getPendingUpgradeProcessMessage(
							upgradeStep.getClass(),
							upgradeInfo.getFromSchemaVersionString(),
							upgradeInfo.getToSchemaVersionString()));
				}
			}

			sb.append(StringPool.NEW_LINE);
		}

		return sb.toString();
	}

	private String _checkPortal(boolean showUpgradeSteps) {
		try (Connection connection = DataAccess.getConnection()) {
			Version currentSchemaVersion =
				PortalUpgradeProcess.getCurrentSchemaVersion(connection);

			SortedMap<Version, UpgradeProcess> pendingUpgradeProcesses =
				PortalUpgradeProcess.getPendingUpgradeProcesses(
					currentSchemaVersion);

			if (!pendingUpgradeProcesses.isEmpty()) {
				Version latestSchemaVersion =
					PortalUpgradeProcess.getLatestSchemaVersion();

				StringBundler sb = new StringBundler();

				sb.append(
					_getModulePendingUpgradeMessage(
						"Portal", currentSchemaVersion.toString(),
						latestSchemaVersion.toString()));

				sb.append(" (requires upgrade tool or auto upgrade)");

				if (showUpgradeSteps) {
					sb.append(StringPool.COLON);

					for (SortedMap.Entry<Version, UpgradeProcess> entry :
							pendingUpgradeProcesses.entrySet()) {

						sb.append(StringPool.NEW_LINE);
						sb.append(StringPool.TAB);

						UpgradeProcess upgradeProcess = entry.getValue();
						Version version = entry.getKey();

						sb.append(
							_getPendingUpgradeProcessMessage(
								upgradeProcess.getClass(),
								currentSchemaVersion.toString(),
								version.toString()));

						sb.append(StringPool.NEW_LINE);

						currentSchemaVersion = version;
					}
				}

				return sb.toString();
			}
		}
		catch (SQLException sqlException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to get pending upgrade information for the portal",
					sqlException);
			}
		}

		return StringPool.BLANK;
	}

	private String _getModulePendingUpgradeMessage(
		String moduleName, String currentSchemaVersion,
		String finalSchemaVersion) {

		return StringBundler.concat(
			"There are upgrade processes available for ", moduleName, " from ",
			currentSchemaVersion, " to ", finalSchemaVersion);
	}

	private String _getPendingUpgradeProcessMessage(
		Class<?> upgradeClass, String fromSchemaVersion,
		String toSchemaVersion) {

		StringBundler sb = new StringBundler(6);

		String toMessage = toSchemaVersion;

		if (UpgradeProcessUtil.isRequiredSchemaVersion(
				Version.parseVersion(fromSchemaVersion),
				Version.parseVersion(toSchemaVersion))) {

			toMessage += " (REQUIRED)";
		}

		sb.append(fromSchemaVersion);
		sb.append(" to ");
		sb.append(toMessage);
		sb.append(StringPool.COLON);
		sb.append(StringPool.SPACE);
		sb.append(upgradeClass.getName());

		return sb.toString();
	}

	private boolean _hasUnsatisfiedUpgradeComponents() {
		String result = _systemChecker.check();

		return !result.contains("UpgradeStepRegistrator");
	}

	private boolean _isPendingModuleUpgrades() {
		for (String bundleSymbolicName : getBundleSymbolicNames()) {
			if (_isUpgradable(bundleSymbolicName)) {
				return true;
			}
		}

		return false;
	}

	private boolean _isPendingRequiredModuleUpgrades() {
		Set<String> upgradableBundleSymbolicNames =
			getUpgradableBundleSymbolicNames();

		for (String bundleSymbolicName : upgradableBundleSymbolicNames) {
			ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
				getUpgradeInfos(bundleSymbolicName));

			List<List<UpgradeInfo>> upgradeInfosList =
				releaseGraphManager.getUpgradeInfosList(
					getSchemaVersionString(bundleSymbolicName));

			List<UpgradeInfo> upgradeInfos = upgradeInfosList.get(0);

			for (UpgradeInfo upgradeInfo : upgradeInfos) {
				if (UpgradeProcessUtil.isRequiredSchemaVersion(
						Version.parseVersion(
							upgradeInfo.getFromSchemaVersionString()),
						Version.parseVersion(
							upgradeInfo.getToSchemaVersionString()))) {

					return true;
				}
			}
		}

		return false;
	}

	private boolean _isUpgradable(String bundleSymbolicName) {
		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			getUpgradeInfos(bundleSymbolicName));

		List<List<UpgradeInfo>> upgradeInfosList =
			releaseGraphManager.getUpgradeInfosList(
				getSchemaVersionString(bundleSymbolicName));

		if (upgradeInfosList.size() == 1) {
			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ReleaseManagerImpl.class);

	private boolean _activated;

	@Reference
	private ReleaseLocalService _releaseLocalService;

	private ServiceTrackerMap<String, List<UpgradeInfo>> _serviceTrackerMap;

	@Reference(
		target = "(component.name=com.liferay.portal.osgi.debug.declarative.service.internal.DeclarativeServiceUnsatisfiedComponentSystemChecker)"
	)
	private volatile SystemChecker _systemChecker;

	@Reference
	private UpgradeExecutor _upgradeExecutor;

	private static class UpgradeServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<UpgradeStep, UpgradeInfo> {

		public UpgradeServiceTrackerCustomizer(BundleContext bundleContext) {
			_bundleContext = bundleContext;
		}

		@Override
		public UpgradeInfo addingService(
			ServiceReference<UpgradeStep> serviceReference) {

			String fromSchemaVersionString =
				(String)serviceReference.getProperty(
					"upgrade.from.schema.version");
			String toSchemaVersionString = (String)serviceReference.getProperty(
				"upgrade.to.schema.version");
			int buildNumber = GetterUtil.getInteger(
				serviceReference.getProperty("build.number"));

			return new UpgradeInfo(
				fromSchemaVersionString, toSchemaVersionString, buildNumber,
				_bundleContext.getService(serviceReference));
		}

		@Override
		public void modifiedService(
			ServiceReference<UpgradeStep> serviceReference,
			UpgradeInfo upgradeInfo) {
		}

		@Override
		public void removedService(
			ServiceReference<UpgradeStep> serviceReference,
			UpgradeInfo upgradeInfo) {

			_bundleContext.ungetService(serviceReference);
		}

		private final BundleContext _bundleContext;

	}

	private class UpgradeInfoServiceTrackerMapListener
		implements ServiceTrackerMapListener
			<String, UpgradeInfo, List<UpgradeInfo>> {

		@Override
		public void keyEmitted(
			ServiceTrackerMap<String, List<UpgradeInfo>> serviceTrackerMap,
			String key, UpgradeInfo upgradeInfo,
			List<UpgradeInfo> upgradeInfos) {

			synchronized (ReleaseManagerImpl.this) {
				if (_activated &&
					UpgradeStepRegistratorThreadLocal.isEnabled() &&
					(PropsValues.UPGRADE_DATABASE_AUTO_RUN ||
					 (_releaseLocalService.fetchRelease(key) == null))) {

					_upgradeExecutor.execute(key, upgradeInfos);
				}
			}
		}

		@Override
		public void keyRemoved(
			ServiceTrackerMap<String, List<UpgradeInfo>> serviceTrackerMap,
			String key, UpgradeInfo upgradeInfo,
			List<UpgradeInfo> upgradeInfos) {
		}

	}

}