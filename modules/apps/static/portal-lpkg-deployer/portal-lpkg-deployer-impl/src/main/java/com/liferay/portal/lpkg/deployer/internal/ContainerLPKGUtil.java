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

package com.liferay.portal.lpkg.deployer.internal;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.util.PropsValues;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Matthew Tambara
 */
public class ContainerLPKGUtil {

	public static List<File> deploy(File lpkgFile, Properties properties)
		throws IOException {

		List<File> lpkgFiles = new ArrayList<>();

		try (ZipFile zipFile = new ZipFile(lpkgFile)) {
			Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

			while (enumeration.hasMoreElements()) {
				ZipEntry zipEntry = enumeration.nextElement();

				String name = zipEntry.getName();

				if (!name.endsWith(".lpkg")) {
					if (properties != null) {
						zipEntry = zipFile.getEntry(
							"liferay-marketplace.properties");

						if (zipEntry != null) {
							try (InputStream inputStream =
									zipFile.getInputStream(zipEntry)) {

								properties.load(inputStream);
							}
						}
					}

					return null;
				}

				File deployerDir = new File(
					PropsValues.MODULE_FRAMEWORK_MARKETPLACE_DIR);

				File innerLPKGFile = new File(deployerDir, name);

				String innerLPKGCanonicalPath =
					innerLPKGFile.getCanonicalPath();

				if (!innerLPKGCanonicalPath.startsWith(
						deployerDir.getCanonicalPath() + File.separator)) {

					if (_log.isWarnEnabled()) {
						_log.warn("Invalid LPKG File name: " + name);
					}

					continue;
				}

				Files.copy(
					zipFile.getInputStream(zipEntry), innerLPKGFile.toPath(),
					StandardCopyOption.REPLACE_EXISTING);

				lpkgFiles.add(innerLPKGFile);
			}
		}

		if (lpkgFiles.isEmpty()) {
			return null;
		}

		lpkgFile.delete();

		return lpkgFiles;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ContainerLPKGUtil.class);

}