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

package com.liferay.portal.kernel.upgrade.util;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.version.Version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Luis Ortiz
 */
public class UpgradeVersionTreeMap extends TreeMap<Version, UpgradeProcess> {

	@Override
	public UpgradeProcess put(Version key, UpgradeProcess upgradeProcess) {
		_put(key, upgradeProcess.getUpgradeSteps());

		return upgradeProcess;
	}

	public void put(Version key, UpgradeProcess... upgradeProcesses) {
		List<UpgradeStep> upgradeStepList = new ArrayList<>();

		for (UpgradeProcess upgradeProcess : upgradeProcesses) {
			Collections.addAll(
				upgradeStepList, upgradeProcess.getUpgradeSteps());
		}

		_put(key, upgradeStepList.toArray(new UpgradeStep[0]));
	}

	private void _put(Version key, UpgradeStep... upgradeProcesses) {
		for (int i = 0; i < (upgradeProcesses.length - 1); i++) {
			UpgradeStep upgradeStep = upgradeProcesses[i];

			Version stepVersion = new Version(
				key.getMajor(), key.getMinor(), key.getMicro(),
				"step-" + (i + 1));

			super.put(stepVersion, (UpgradeProcess)upgradeStep);
		}

		Version finalVersion = new Version(
			key.getMajor(), key.getMinor(), key.getMicro());

		super.put(
			finalVersion,
			(UpgradeProcess)upgradeProcesses[upgradeProcesses.length - 1]);
	}

}