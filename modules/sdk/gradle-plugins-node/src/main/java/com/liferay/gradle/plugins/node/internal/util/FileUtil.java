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

package com.liferay.gradle.plugins.node.internal.util;

import com.liferay.gradle.util.OSDetector;

import groovy.json.JsonSlurper;

import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.UncheckedIOException;
import org.gradle.api.file.CopySpec;
import org.gradle.api.logging.Logger;
import org.gradle.internal.hash.HashUtil;
import org.gradle.internal.hash.HashValue;
import org.gradle.process.ExecResult;
import org.gradle.process.ExecSpec;

/**
 * @author Andrea Di Giorgi
 */
public class FileUtil extends com.liferay.gradle.util.FileUtil {

	public static void createBinDirLinks(Logger logger, File nodeModulesDir)
		throws IOException {

		JsonSlurper jsonSlurper = new JsonSlurper();

		Path nodeModulesDirPath = nodeModulesDir.toPath();

		Path nodeModulesBinDirPath = nodeModulesDirPath.resolve(
			_NODE_MODULES_BIN_DIR_NAME);

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
				nodeModulesDirPath, _directoryStreamFilter)) {

			for (Path dirPath : directoryStream) {
				Path packageJsonPath = dirPath.resolve("package.json");

				if (Files.notExists(packageJsonPath)) {
					continue;
				}

				Map<String, Object> packageJsonMap =
					(Map<String, Object>)jsonSlurper.parse(
						packageJsonPath.toFile());

				Object binObject = packageJsonMap.get("bin");

				if (!(binObject instanceof Map<?, ?>)) {
					continue;
				}

				Map<String, String> binJsonMap = (Map<String, String>)binObject;

				if (binJsonMap.isEmpty()) {
					continue;
				}

				Files.createDirectories(nodeModulesBinDirPath);

				for (Map.Entry<String, String> entry : binJsonMap.entrySet()) {
					String linkFileName = entry.getKey();
					String linkTargetFileName = entry.getValue();

					Path linkPath = nodeModulesBinDirPath.resolve(linkFileName);
					Path linkTargetPath = dirPath.resolve(linkTargetFileName);

					Files.deleteIfExists(linkPath);

					Files.createSymbolicLink(linkPath, linkTargetPath);

					if (logger.isInfoEnabled()) {
						logger.info(
							"Created binary symbolic link {} which targets {}",
							linkPath, linkTargetPath);
					}
				}
			}
		}
	}

	public static void deleteSymbolicLinks(Path dirPath) throws IOException {
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
				dirPath)) {

			for (Path path : directoryStream) {
				if (Files.isSymbolicLink(path)) {
					Files.delete(path);
				}
			}
		}
	}

	public static String getDigest(Iterable<File> files) {
		StringBuilder sb = new StringBuilder();

		SortedSet<File> sortedFiles = null;

		try {
			sortedFiles = _flattenAndSort(files);
		}
		catch (IOException ioe) {
			throw new GradleException("Unable to flatten files", ioe);
		}

		for (File file : sortedFiles) {
			if (!file.exists()) {
				continue;
			}

			try {
				List<String> lines = Files.readAllLines(
					file.toPath(), StandardCharsets.UTF_8);

				sb.append(Integer.toHexString(lines.hashCode()));
			}
			catch (IOException ioe) {
				HashValue hashValue = HashUtil.sha1(file);

				sb.append(hashValue.asHexString());
			}

			sb.append('-');
		}

		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}

		return sb.toString();
	}

	public static void removeBinDirLinks(
			final Logger logger, File nodeModulesDir)
		throws IOException {

		Files.walkFileTree(
			nodeModulesDir.toPath(),
			new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(
						Path dirPath, BasicFileAttributes basicFileAttributes)
					throws IOException {

					String dirName = String.valueOf(dirPath.getFileName());

					if (dirName.equals(_NODE_MODULES_BIN_DIR_NAME)) {
						if (logger.isInfoEnabled()) {
							logger.info(
								"Removing binary symbolic links from {}",
								dirPath);
						}

						FileUtil.deleteSymbolicLinks(dirPath);

						return FileVisitResult.SKIP_SUBTREE;
					}

					return FileVisitResult.CONTINUE;
				}

			});
	}

	public static void syncDir(
		Project project, final File sourceDir, final File targetDir,
		boolean nativeSync) {

		ExecResult execResult = null;

		if (nativeSync) {
			execResult = project.exec(
				new Action<ExecSpec>() {

					@Override
					public void execute(ExecSpec execSpec) {
						if (OSDetector.isWindows()) {
							execSpec.args(
								"/MIR", "/NDL", "/NFL", "/NJH", "/NJS", "/NP",
								sourceDir.getAbsolutePath(),
								targetDir.getAbsolutePath());

							execSpec.setExecutable("robocopy");
						}
						else {
							execSpec.args(
								"--archive", "--delete",
								sourceDir.getAbsolutePath() + File.separator,
								targetDir.getAbsolutePath());

							execSpec.setExecutable("rsync");
						}

						execSpec.setIgnoreExitValue(true);
					}

				});
		}

		if ((execResult != null) && (execResult.getExitValue() == 0)) {
			return;
		}

		project.delete(targetDir);

		try {
			project.copy(
				new Action<CopySpec>() {

					@Override
					public void execute(CopySpec copySpec) {
						copySpec.from(sourceDir);
						copySpec.into(targetDir);
					}

				});
		}
		catch (RuntimeException re) {
			project.delete(targetDir);

			throw re;
		}
	}

	private static SortedSet<File> _flattenAndSort(Iterable<File> files)
		throws IOException {

		final SortedSet<File> sortedFiles = new TreeSet<>(new FileComparator());

		if (files == null) {
			return sortedFiles;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				Files.walkFileTree(
					file.toPath(),
					new SimpleFileVisitor<Path>() {

						@Override
						public FileVisitResult visitFile(
								Path path,
								BasicFileAttributes basicFileAttributes)
							throws IOException {

							sortedFiles.add(path.toFile());

							return FileVisitResult.CONTINUE;
						}

					});
			}
			else {
				sortedFiles.add(file);
			}
		}

		return sortedFiles;
	}

	private static final String _NODE_MODULES_BIN_DIR_NAME = ".bin";

	private static final DirectoryStream.Filter<Path> _directoryStreamFilter =
		new DirectoryStream.Filter<Path>() {

			@Override
			public boolean accept(Path path) throws IOException {
				return Files.isDirectory(path);
			}

		};

	private static class FileComparator implements Comparator<File> {

		@Override
		public int compare(File file1, File file2) {
			String canonicalPath1 = _getCanonicalPath(file1);
			String canonicalPath2 = _getCanonicalPath(file2);

			return canonicalPath1.compareTo(canonicalPath2);
		}

		private static String _getCanonicalPath(File file) {
			String canonicalPath = null;

			try {
				canonicalPath = file.getCanonicalPath();
			}
			catch (IOException ioe) {
				String message = "Unable to get canonical path of " + file;

				throw new UncheckedIOException(message, ioe);
			}

			if (File.separatorChar != '/') {
				canonicalPath = canonicalPath.replace(File.separatorChar, '/');
			}

			return canonicalPath;
		}

	}

}