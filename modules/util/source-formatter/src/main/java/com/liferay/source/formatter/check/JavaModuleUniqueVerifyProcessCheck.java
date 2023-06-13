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

import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.source.formatter.SourceFormatterExcludes;
import com.liferay.source.formatter.check.util.SourceUtil;
import com.liferay.source.formatter.parser.JavaClass;
import com.liferay.source.formatter.parser.JavaClassParser;
import com.liferay.source.formatter.parser.JavaTerm;
import com.liferay.source.formatter.parser.ParseException;
import com.liferay.source.formatter.util.FileUtil;
import com.liferay.source.formatter.util.SourceFormatterUtil;

import java.io.File;
import java.io.IOException;

import java.util.List;

/**
 * @author Alan Huang
 */
public class JavaModuleUniqueVerifyProcessCheck extends BaseJavaTermCheck {

	@Override
	public boolean isModuleSourceCheck() {
		return true;
	}

	@Override
	protected String doProcess(
			String fileName, String absolutePath, JavaTerm javaTerm,
			String fileContent)
		throws IOException, ParseException {

		JavaClass javaClass = (JavaClass)javaTerm;

		if (javaClass.getParentJavaClass() != null) {
			return javaTerm.getContent();
		}

		List<String> extendedClassNames = javaClass.getExtendedClassNames(true);

		if (!extendedClassNames.contains(
				"com.liferay.portal.verify.VerifyProcess")) {

			return javaTerm.getContent();
		}

		int x = absolutePath.indexOf("/src/");

		if (x == -1) {
			return javaTerm.getContent();
		}

		List<String> javaFileNames = SourceFormatterUtil.scanForFiles(
			absolutePath.substring(0, x + 5), new String[0],
			new String[] {"**/*.java"}, new SourceFormatterExcludes(), true);

		int extendedVerifyProcessClassCount = 0;

		for (String javaFileName : javaFileNames) {
			javaFileName = StringUtil.replace(
				javaFileName, CharPool.BACK_SLASH, CharPool.SLASH);

			File file = new File(javaFileName);

			if (!file.exists()) {
				continue;
			}

			String content = FileUtil.read(file);

			if (SourceUtil.containsUnquoted(content, "@generated") ||
				SourceUtil.containsUnquoted(content, "$ANTLR")) {

				continue;
			}

			javaClass = JavaClassParser.parseJavaClass(javaFileName, content);

			extendedClassNames = javaClass.getExtendedClassNames(true);

			if (extendedClassNames.contains(
					"com.liferay.portal.verify.VerifyProcess")) {

				extendedVerifyProcessClassCount++;
			}

			if (extendedVerifyProcessClassCount > 1) {
				addMessage(
					fileName,
					"A module can not have more than 1 verify process class " +
						"(class extends VerifyProcess)");

				return javaTerm.getContent();
			}
		}

		return javaTerm.getContent();
	}

	@Override
	protected String[] getCheckableJavaTermNames() {
		return new String[] {JAVA_CLASS};
	}

}