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

package com.liferay.source.formatter.check;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.source.formatter.util.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Nícolas Moura
 */
public class UpgradeVelocityCommentMigrationCheck extends BaseFileCheck {

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws IOException {

		if (!fileName.endsWith(".vm")) {
			return content;
		}

		int periodIndex = fileName.lastIndexOf(StringPool.PERIOD);
		int slashIndex = fileName.lastIndexOf(StringPool.SLASH);

		File file = new File(
			StringBundler.concat(
				fileName.substring(0, slashIndex), "/migrated/",
				fileName.substring(slashIndex + 1, periodIndex), ".ftl"));

		if (file.length() != 0) {
			content = FileUtil.read(file);
		}

		content = _formatContent(content);

		FileUtil.write(file, content);

		return content;
	}

	private String _formatContent(String content) {
		String[] lines = content.split(StringPool.NEW_LINE);

		for (String line : lines) {
			if (line.contains("##") && (line.length() != 2)) {
				String newComment = line.replace("##", "<#--") + " -->";

				if (newComment.contains("Velocity Transform Template")) {
					newComment = StringUtil.replace(
						newComment, "Velocity Transform Template",
						"FreeMarker Template");
				}

				content = StringUtil.replace(content, line, newComment);
			}
		}

		return StringUtil.removeSubstring(content, "##");
	}

}