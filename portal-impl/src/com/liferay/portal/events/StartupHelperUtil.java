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

package com.liferay.portal.events;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.exception.ResourceActionsException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogContextRegistryUtil;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.patcher.PatcherUtil;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.ResourceActionLocalServiceUtil;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.util.UpgradeProcessUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.version.Version;
import com.liferay.portal.tools.DBUpgrader;
import com.liferay.portal.upgrade.PortalUpgradeProcess;
import com.liferay.portal.upgrade.log.UpgradeLogContext;
import com.liferay.portal.util.PropsValues;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 * @author Raymond Augé
 */
public class StartupHelperUtil {

	public static void initResourceActions() {
		ResourceActionLocalServiceUtil.checkResourceActions();

		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			ResourceActionsUtil.populateModelResources(
				StartupHelperUtil.class.getClassLoader(),
				PropsValues.RESOURCE_ACTIONS_CONFIGS);
		}
		catch (ResourceActionsException resourceActionsException) {
			ReflectionUtil.throwException(resourceActionsException);
		}
	}

	public static boolean isDBNew() {
		return _dbNew;
	}

	public static boolean isStartupFinished() {
		return _startupFinished;
	}

	public static boolean isUpgrading() {
		return _upgrading;
	}

	public static void printPatchLevel() {
		if (_log.isInfoEnabled()) {
			String installedPatches = StringUtil.merge(
				PatcherUtil.getInstalledPatches(), StringPool.COMMA_AND_SPACE);

			if (Validator.isNull(installedPatches)) {
				_log.info("There are no patches installed");
			}
			else {
				_log.info(
					"The following patches are installed: " + installedPatches);
			}
		}
	}

	public static void setDBNew(boolean dbNew) {
		_dbNew = dbNew;
	}

	public static void setStartupFinished(boolean startupFinished) {
		_startupFinished = startupFinished;
	}

	public static void setUpgrading(boolean upgrading) {
		_upgrading = upgrading;

		if (_upgrading) {
			if (PropsValues.UPGRADE_LOG_CONTEXT_ENABLED) {
				LogContextRegistryUtil.registerLogContext(
					UpgradeLogContext.getInstance());
			}

			if (PropsValues.UPGRADE_REPORT_ENABLED) {
				DBUpgrader.startUpgradeReportLogAppender();
			}
		}
		else {
			DBUpgrader.stopUpgradeReportLogAppender();

			LogContextRegistryUtil.unregisterLogContext(
				UpgradeLogContext.getInstance());
		}
	}

	public static void upgradeProcess(int buildNumber) throws UpgradeException {
		List<String> upgradeProcessClassNames = new ArrayList<>();

		if (FeatureFlagManagerUtil.isEnabled("LPS-157670")) {
			Collections.addAll(
				upgradeProcessClassNames,
				"com.liferay.portal.upgrade.UpgradeProcess_6_1_1",
				"com.liferay.portal.upgrade.UpgradeProcess_6_2_0");
		}

		Collections.addAll(
			upgradeProcessClassNames,
			"com.liferay.portal.upgrade.UpgradeProcess_7_0_0",
			"com.liferay.portal.upgrade.UpgradeProcess_7_0_1",
			"com.liferay.portal.upgrade.UpgradeProcess_7_0_3",
			"com.liferay.portal.upgrade.UpgradeProcess_7_0_5",
			"com.liferay.portal.upgrade.UpgradeProcess_7_0_6",
			"com.liferay.portal.upgrade.PortalUpgradeProcess");

		List<UpgradeProcess> upgradeProcesses =
			UpgradeProcessUtil.initUpgradeProcesses(
				PortalClassLoaderUtil.getClassLoader(),
				upgradeProcessClassNames.toArray(new String[0]));

		_upgraded = UpgradeProcessUtil.upgradeProcess(
			buildNumber, upgradeProcesses);
	}

	public static void verifyRequiredSchemaVersion() throws Exception {
		if (_log.isDebugEnabled()) {
			_log.debug("Check the portal's required schema version");
		}

		try (Connection connection = DataAccess.getConnection()) {
			if (PortalUpgradeProcess.isInRequiredSchemaVersion(connection)) {
				return;
			}

			Version currentSchemaVersion =
				PortalUpgradeProcess.getCurrentSchemaVersion(
					DataAccess.getConnection());

			Version requiredSchemaVersion =
				PortalUpgradeProcess.getRequiredSchemaVersion();

			String msg;

			if (currentSchemaVersion.compareTo(requiredSchemaVersion) < 0) {
				msg =
					"You must first upgrade the portal to the required " +
						"schema version " + requiredSchemaVersion;
			}
			else {
				msg =
					"Current portal schema version " + currentSchemaVersion +
						" requires a newer version of Liferay";
			}

			System.out.println(msg);

			throw new RuntimeException(msg);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		StartupHelperUtil.class);

	private static volatile boolean _dbNew;
	private static boolean _startupFinished;
	private static boolean _upgraded;
	private static boolean _upgrading;

}