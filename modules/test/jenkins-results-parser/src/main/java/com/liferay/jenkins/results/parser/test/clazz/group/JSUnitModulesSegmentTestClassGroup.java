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

package com.liferay.jenkins.results.parser.test.clazz.group;

import com.liferay.jenkins.results.parser.JenkinsResultsParserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Hashimoto
 */
public class JSUnitModulesSegmentTestClassGroup extends SegmentTestClassGroup {

	public JSUnitModulesSegmentTestClassGroup(
		BatchTestClassGroup parentBatchTestClassGroup) {

		super(parentBatchTestClassGroup);
	}

	@Override
	public String getTestCasePropertiesContent() {
		StringBuilder sb = new StringBuilder();

		List<String> axisIndexes = new ArrayList<>();

		for (int axisIndex = 0; axisIndex < getAxisCount(); axisIndex++) {
			axisIndexes.add(String.valueOf(axisIndex));

			AxisTestClassGroup axisTestClassGroup = getAxisTestClassGroup(
				axisIndex);

			List<TestClass.TestClassMethod> testClassMethods =
				new ArrayList<>();

			for (TestClass testClass : axisTestClassGroup.getTestClasses()) {
				testClassMethods.addAll(testClass.getTestClassMethods());
			}

			sb.append("TEST_CLASS_GROUP_");
			sb.append(axisIndex);
			sb.append("=");

			for (TestClass.TestClassMethod testClassMethod : testClassMethods) {
				sb.append(testClassMethod.getName());
				sb.append(",");
			}

			if (!testClassMethods.isEmpty()) {
				sb.setLength(sb.length() - 1);
			}

			sb.append("\n");
		}

		sb.append("TEST_CLASS_GROUPS=");
		sb.append(JenkinsResultsParserUtil.join(" ", axisIndexes));
		sb.append("\n");

		return sb.toString();
	}

}