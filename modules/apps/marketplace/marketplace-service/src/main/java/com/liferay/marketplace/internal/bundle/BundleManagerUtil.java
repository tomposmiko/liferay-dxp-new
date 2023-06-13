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

package com.liferay.marketplace.internal.bundle;

import com.liferay.osgi.util.service.Snapshot;

import java.io.File;

import java.util.List;
import java.util.jar.Manifest;

import org.osgi.framework.Bundle;

/**
 * @author Ryan Park
 */
public class BundleManagerUtil {

	public static Bundle getBundle(String symbolicName, String version) {
		BundleManagerImpl bundleManagerImpl = _bundleManagerImplSnapshot.get();

		return bundleManagerImpl.getBundle(symbolicName, version);
	}

	public static List<Bundle> getBundles() {
		BundleManagerImpl bundleManagerImpl = _bundleManagerImplSnapshot.get();

		return bundleManagerImpl.getBundles();
	}

	public static List<Bundle> getInstalledBundles() {
		BundleManagerImpl bundleManagerImpl = _bundleManagerImplSnapshot.get();

		return bundleManagerImpl.getInstalledBundles();
	}

	public static Manifest getManifest(File file) {
		BundleManagerImpl bundleManagerImpl = _bundleManagerImplSnapshot.get();

		return bundleManagerImpl.getManifest(file);
	}

	public static void installLPKG(File file) throws Exception {
		BundleManagerImpl bundleManagerImpl = _bundleManagerImplSnapshot.get();

		bundleManagerImpl.installLPKG(file);
	}

	public static boolean isInstalled(Bundle bundle) {
		BundleManagerImpl bundleManagerImpl = _bundleManagerImplSnapshot.get();

		return bundleManagerImpl.isInstalled(bundle);
	}

	public static boolean isInstalled(String symbolicName, String version) {
		BundleManagerImpl bundleManagerImpl = _bundleManagerImplSnapshot.get();

		return bundleManagerImpl.isInstalled(symbolicName, version);
	}

	public static void uninstallBundle(Bundle bundle) {
		BundleManagerImpl bundleManagerImpl = _bundleManagerImplSnapshot.get();

		bundleManagerImpl.uninstallBundle(bundle);
	}

	public static void uninstallBundle(String symbolicName, String version) {
		BundleManagerImpl bundleManagerImpl = _bundleManagerImplSnapshot.get();

		bundleManagerImpl.uninstallBundle(symbolicName, version);
	}

	private static final Snapshot<BundleManagerImpl>
		_bundleManagerImplSnapshot = new Snapshot<>(
			BundleManagerUtil.class, BundleManagerImpl.class);

}