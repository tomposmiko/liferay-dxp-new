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

package com.liferay.jenkins.results.parser;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public class DefaultTopLevelBuildReport extends BaseTopLevelBuildReport {

	@Override
	public JSONObject getBuildReportJSONObject() {
		if (buildReportJSONObject != null) {
			return buildReportJSONObject;
		}

		buildReportJSONObject = new JSONObject();

		buildReportJSONObject.put("buildURL", _topLevelBuild.getBuildURL());
		buildReportJSONObject.put("duration", _topLevelBuild.getDuration());
		buildReportJSONObject.put("result", _topLevelBuild.getResult());
		buildReportJSONObject.put("startTime", _topLevelBuild.getStartTime());

		StopWatchRecordsGroup stopWatchRecordsGroup =
			_topLevelBuild.getStopWatchRecordsGroup();

		if (stopWatchRecordsGroup != null) {
			buildReportJSONObject.put(
				"stopWatchRecords", stopWatchRecordsGroup.getJSONArray());
		}

		Map<String, List<JSONObject>> downstreamBuildMap = new HashMap<>();

		for (Build build : _topLevelBuild.getDownstreamBuilds(null)) {
			String batchName = "default";

			List<JSONObject> downstreamBuildJSONObjects = new ArrayList<>();

			if (build instanceof BatchBuild) {
				BatchBuild batchBuild = (BatchBuild)build;

				batchName = batchBuild.getBatchName();

				for (AxisBuild axisBuild :
						batchBuild.getDownstreamAxisBuilds()) {

					downstreamBuildJSONObjects.add(
						_getDownstreamBuildJSONObject(axisBuild));
				}
			}
			else {
				if (build instanceof AxisBuild) {
					AxisBuild axisBuild = (AxisBuild)build;

					batchName = axisBuild.getBatchName();
				}
				else if (build instanceof DownstreamBuild) {
					DownstreamBuild downstreamBuild = (DownstreamBuild)build;

					batchName = downstreamBuild.getBatchName();
				}

				downstreamBuildJSONObjects.add(
					_getDownstreamBuildJSONObject(build));
			}

			downstreamBuildJSONObjects.addAll(
				downstreamBuildMap.getOrDefault(
					batchName, new ArrayList<JSONObject>()));

			downstreamBuildMap.put(batchName, downstreamBuildJSONObjects);
		}

		JSONArray batchesJSONArray = new JSONArray();

		for (Map.Entry<String, List<JSONObject>> downstreamBuildEntry :
				downstreamBuildMap.entrySet()) {

			JSONObject batchJSONObject = new JSONObject();

			batchJSONObject.put("batchName", downstreamBuildEntry.getKey());
			batchJSONObject.put("builds", downstreamBuildEntry.getValue());

			batchesJSONArray.put(batchJSONObject);
		}

		buildReportJSONObject.put("batches", batchesJSONArray);

		buildReportJSONObject.put(
			"testSuiteName", _topLevelBuild.getTestSuiteName());

		return buildReportJSONObject;
	}

	protected DefaultTopLevelBuildReport(TopLevelBuild topLevelBuild) {
		super(topLevelBuild);

		_topLevelBuild = topLevelBuild;

		_jenkinsConsoleLocalFile = new File(
			System.getenv("WORKSPACE"),
			JenkinsResultsParserUtil.getDistinctTimeStamp());

		try {
			JenkinsResultsParserUtil.write(
				_jenkinsConsoleLocalFile, topLevelBuild.getConsoleText());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Override
	protected JSONObject getBuildJSONObject() {
		return _topLevelBuild.getBuildJSONObject();
	}

	@Override
	protected File getJenkinsConsoleLocalFile() {
		return _jenkinsConsoleLocalFile;
	}

	private JSONObject _getDownstreamBuildJSONObject(Build build) {
		JSONObject downstreamBuildJSONObject = new JSONObject();

		if (build instanceof AxisBuild) {
			AxisBuild axisBuild = (AxisBuild)build;

			downstreamBuildJSONObject.put("axisName", axisBuild.getAxisName());
		}
		else if (build instanceof DownstreamBuild) {
			DownstreamBuild downstreamBuild = (DownstreamBuild)build;

			downstreamBuildJSONObject.put(
				"axisName", downstreamBuild.getAxisName());
		}

		downstreamBuildJSONObject.put("buildURL", build.getBuildURL());
		downstreamBuildJSONObject.put("duration", build.getDuration());
		downstreamBuildJSONObject.put("result", build.getResult());
		downstreamBuildJSONObject.put("startTime", build.getStartTime());

		StopWatchRecordsGroup stopWatchRecordsGroup =
			build.getStopWatchRecordsGroup();

		if (stopWatchRecordsGroup != null) {
			downstreamBuildJSONObject.put(
				"stopWatchRecords", stopWatchRecordsGroup.getJSONArray());
		}

		JSONArray testResultsJSONArray = new JSONArray();

		for (TestResult testResult : build.getTestResults(null)) {
			testResultsJSONArray.put(_getTestResultJSONObject(testResult));
		}

		downstreamBuildJSONObject.put("testResults", testResultsJSONArray);

		return downstreamBuildJSONObject;
	}

	private JSONObject _getTestResultJSONObject(TestResult testResult) {
		JSONObject testResultJSONObject = new JSONObject();

		testResultJSONObject.put("duration", testResult.getDuration());

		String errorDetails = testResult.getErrorDetails();

		if (errorDetails != null) {
			if (errorDetails.contains("\n")) {
				int index = errorDetails.indexOf("\n");

				errorDetails = errorDetails.substring(0, index);
			}

			if (errorDetails.length() > 200) {
				errorDetails = errorDetails.substring(0, 200);
			}

			testResultJSONObject.put("errorDetails", errorDetails);
		}

		testResultJSONObject.put("name", testResult.getDisplayName());
		testResultJSONObject.put("status", testResult.getStatus());

		return testResultJSONObject;
	}

	private final File _jenkinsConsoleLocalFile;
	private final TopLevelBuild _topLevelBuild;

}