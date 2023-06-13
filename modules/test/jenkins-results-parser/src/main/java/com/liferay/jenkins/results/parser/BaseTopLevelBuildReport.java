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

import com.liferay.jenkins.results.parser.testray.TestrayS3Bucket;
import com.liferay.jenkins.results.parser.testray.TestrayS3Object;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public abstract class BaseTopLevelBuildReport
	extends BaseBuildReport implements TopLevelBuildReport {

	@Override
	public URL getBuildReportJSONTestrayURL() {
		JobReport jobReport = getJobReport();

		JenkinsMaster jenkinsMaster = jobReport.getJenkinsMaster();

		try {
			return new URL(
				JenkinsResultsParserUtil.combine(
					"https://testray.liferay.com/reports/production/logs/",
					getStartYearMonth(), "/", jenkinsMaster.getName(), "/",
					jobReport.getJobName(), "/",
					String.valueOf(getBuildNumber()), "/build-report.json.gz"));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Override
	public URL getBuildReportJSONUserContentURL() {
		JobReport jobReport = getJobReport();

		JenkinsMaster jenkinsMaster = jobReport.getJenkinsMaster();

		try {
			return new URL(
				JenkinsResultsParserUtil.combine(
					"https://", jenkinsMaster.getName(),
					".liferay.com/userContent/jobs/", jobReport.getJobName(),
					"/builds/", String.valueOf(getBuildNumber()),
					"/build-report.json.gz"));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Override
	public TestrayS3Object getBuildReportTestrayS3Object() {
		JobReport jobReport = getJobReport();

		JenkinsMaster jenkinsMaster = jobReport.getJenkinsMaster();

		TestrayS3Bucket testrayS3Bucket = TestrayS3Bucket.getInstance();

		return testrayS3Bucket.getTestrayS3Object(
			JenkinsResultsParserUtil.combine(
				getStartYearMonth(), "/", jenkinsMaster.getName(), "/",
				jobReport.getJobName(), "/", String.valueOf(getBuildNumber()),
				"/build-report.json.gz"));
	}

	@Override
	public URL getBuildResultJSONTestrayURL() {
		JobReport jobReport = getJobReport();

		JenkinsMaster jenkinsMaster = jobReport.getJenkinsMaster();

		try {
			return new URL(
				JenkinsResultsParserUtil.combine(
					"https://testray.liferay.com/reports/production/logs/",
					getStartYearMonth(), "/", jenkinsMaster.getName(), "/",
					jobReport.getJobName(), "/",
					String.valueOf(getBuildNumber()), "/build-result.json.gz"));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Override
	public URL getBuildResultJSONUserContentURL() {
		JobReport jobReport = getJobReport();

		JenkinsMaster jenkinsMaster = jobReport.getJenkinsMaster();

		try {
			return new URL(
				JenkinsResultsParserUtil.combine(
					"https://", jenkinsMaster.getName(),
					".liferay.com/userContent/jobs/", jobReport.getJobName(),
					"/builds/", String.valueOf(getBuildNumber()),
					"/build-result.json"));
		}
		catch (MalformedURLException malformedURLException) {
			throw new RuntimeException(malformedURLException);
		}
	}

	@Override
	public TestrayS3Object getBuildResultTestrayS3Object() {
		JobReport jobReport = getJobReport();

		JenkinsMaster jenkinsMaster = jobReport.getJenkinsMaster();

		TestrayS3Bucket testrayS3Bucket = TestrayS3Bucket.getInstance();

		return testrayS3Bucket.getTestrayS3Object(
			JenkinsResultsParserUtil.combine(
				getStartYearMonth(), "/", jenkinsMaster.getName(), "/",
				jobReport.getJobName(), "/", String.valueOf(getBuildNumber()),
				"/build-result.json.gz"));
	}

	@Override
	public List<DownstreamBuildReport> getDownstreamBuildReports() {
		if (_downstreamBuildReports != null) {
			return _downstreamBuildReports;
		}

		_downstreamBuildReports = new ArrayList<>();

		JSONObject buildReportJSONObject = getBuildReportJSONObject();

		JSONArray batchesJSONArray = buildReportJSONObject.optJSONArray(
			"batches");

		if (batchesJSONArray == null) {
			return _downstreamBuildReports;
		}

		for (int i = 0; i < batchesJSONArray.length(); i++) {
			JSONObject batchJSONObject = batchesJSONArray.optJSONObject(i);

			if (batchJSONObject == null) {
				continue;
			}

			String batchName = batchJSONObject.optString("batchName");
			JSONArray buildsJSONArray = batchJSONObject.optJSONArray("builds");

			if (JenkinsResultsParserUtil.isNullOrEmpty(batchName) ||
				(buildsJSONArray == null)) {

				continue;
			}

			for (int j = 0; j < buildsJSONArray.length(); j++) {
				_downstreamBuildReports.add(
					BuildReportFactory.newDownstreamBuildReport(
						batchName, buildsJSONArray.getJSONObject(j), this));
			}
		}

		return _downstreamBuildReports;
	}

	@Override
	public URL getTestResultsJSONUserContentURL() {
		JobReport jobReport = getJobReport();

		try {
			return new URL(
				JenkinsResultsParserUtil.combine(
					"https://test-1-0.liferay.com/userContent/testResults/",
					jobReport.getJobName(), "/builds/",
					String.valueOf(getBuildNumber()), "/test.results.json"));
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Override
	public String getTestSuiteName() {
		JSONObject buildReportJSONObject = getBuildReportJSONObject();

		return buildReportJSONObject.optString("testSuiteName");
	}

	@Override
	public long getTopLevelActiveDuration() {
		long topLevelPassiveBuildDuration = getTopLevelPassiveDuration();

		if (topLevelPassiveBuildDuration == 0L) {
			return 0L;
		}

		return getDuration() - topLevelPassiveBuildDuration;
	}

	@Override
	public long getTopLevelPassiveDuration() {
		StopWatchRecordsGroup stopWatchRecordsGroup =
			getStopWatchRecordsGroup();

		if (stopWatchRecordsGroup == null) {
			return 0L;
		}

		StopWatchRecord waitForInvokedJobsStopWatchRecord =
			stopWatchRecordsGroup.get("wait.for.invoked.jobs");
		StopWatchRecord waitForInvokedSmokeJobsStopWatchRecord =
			stopWatchRecordsGroup.get("wait.for.invoked.smoke.jobs");

		if ((waitForInvokedJobsStopWatchRecord != null) ||
			(waitForInvokedSmokeJobsStopWatchRecord != null)) {

			long topLevelPassiveBuildDuration = 0L;

			if (waitForInvokedJobsStopWatchRecord != null) {
				topLevelPassiveBuildDuration +=
					waitForInvokedJobsStopWatchRecord.getDuration();
			}

			if (waitForInvokedSmokeJobsStopWatchRecord != null) {
				topLevelPassiveBuildDuration +=
					waitForInvokedSmokeJobsStopWatchRecord.getDuration();
			}

			return topLevelPassiveBuildDuration;
		}

		StopWatchRecord invokeDownstreamBuildsStopWatchRecord =
			stopWatchRecordsGroup.get("invoke.downstream.builds");

		if (invokeDownstreamBuildsStopWatchRecord != null) {
			return invokeDownstreamBuildsStopWatchRecord.getDuration();
		}

		return 0L;
	}

	protected BaseTopLevelBuildReport(JSONObject buildReportJSONObject) {
		super(buildReportJSONObject);

		setStartDate(new Date(buildReportJSONObject.getLong("startTime")));
	}

	protected BaseTopLevelBuildReport(
		JSONObject buildJSONObject, JobReport jobReport) {

		super(buildJSONObject, jobReport);

		setStartDate(new Date(buildJSONObject.getLong("timestamp")));
	}

	protected BaseTopLevelBuildReport(TopLevelBuild topLevelBuild) {
		super(topLevelBuild.getBuildURL());

		setStartDate(new Date(topLevelBuild.getStartTime()));
	}

	protected BaseTopLevelBuildReport(URL buildURL) {
		super(buildURL);
	}

	protected JSONObject getBuildReportJSONObject(
		JSONObject buildResultJSONObject) {

		if ((buildResultJSONObject == null) ||
			!buildResultJSONObject.has("duration") ||
			!buildResultJSONObject.has("result") ||
			!buildResultJSONObject.has("stopWatchRecords")) {

			return null;
		}

		JSONObject buildReportJSONObject = new JSONObject();

		buildReportJSONObject.put(
			"batches", _getBatchesJSONArray(buildResultJSONObject));
		buildReportJSONObject.put("buildURL", String.valueOf(getBuildURL()));
		buildReportJSONObject.put(
			"duration", buildResultJSONObject.get("duration"));
		buildReportJSONObject.put(
			"result", buildResultJSONObject.get("result"));

		long startTime = buildResultJSONObject.optLong("startTime", 0L);

		if (startTime == 0L) {
			String startTimeString = _getVariableFromJenkinsConsole(
				"TOP_LEVEL_START_TIME");

			if ((startTimeString != null) && startTimeString.matches("\\d+")) {
				startTime = Integer.parseInt(startTimeString);
			}
		}

		if (startTime == 0L) {
			StopWatchRecordsGroup stopWatchRecordsGroup =
				new StopWatchRecordsGroup(buildResultJSONObject);

			StopWatchRecord stopWatchRecord = stopWatchRecordsGroup.get(
				"start.current.job");

			if (stopWatchRecord == null) {
				return null;
			}

			startTime = stopWatchRecord.getStartTimestamp();
		}

		buildReportJSONObject.put("startTime", startTime);

		buildReportJSONObject.put("status", "completed");
		buildReportJSONObject.put(
			"stopWatchRecords", buildResultJSONObject.get("stopWatchRecords"));

		String testSuiteName = _getTestSuiteNameFromBuildResult(
			buildResultJSONObject);

		if (JenkinsResultsParserUtil.isNullOrEmpty(testSuiteName)) {
			testSuiteName = _getVariableFromJenkinsConsole("CI_TEST_SUITE");
		}

		buildReportJSONObject.put("testSuiteName", testSuiteName);

		return buildReportJSONObject;
	}

	protected abstract File getJenkinsConsoleLocalFile();

	protected String getStartYearMonth() {
		return JenkinsResultsParserUtil.toDateString(
			getStartDate(), "yyyy-MM", "America/Los_Angeles");
	}

	private JSONArray _getBatchesJSONArray(JSONObject buildResultJSONObject) {
		JSONArray batchResultsJSONArray = buildResultJSONObject.optJSONArray(
			"batchResults");

		if ((batchResultsJSONArray == null) ||
			batchResultsJSONArray.isEmpty()) {

			return new JSONArray();
		}

		Map<String, List<JSONObject>> batchJSONObjectsMap = new HashMap<>();

		for (int i = 0; i < batchResultsJSONArray.length(); i++) {
			JSONObject batchResultJSONObject =
				batchResultsJSONArray.optJSONObject(i);

			if (batchResultJSONObject == null) {
				continue;
			}

			String batchName = batchResultJSONObject.getString("jobVariant");

			batchName = batchName.replaceAll("([^/]+)/.*", "$1");

			List<JSONObject> batchJSONObjects = batchJSONObjectsMap.get(
				batchName);

			if (batchJSONObjects == null) {
				batchJSONObjects = new ArrayList<>();

				batchJSONObjectsMap.put(batchName, batchJSONObjects);
			}

			if (batchResultJSONObject.has("buildResults")) {
				JSONArray buildResultsJSONArray =
					batchResultJSONObject.getJSONArray("buildResults");

				for (int j = 0; j < buildResultsJSONArray.length(); j++) {
					batchJSONObjects.add(
						buildResultsJSONArray.getJSONObject(j));
				}
			}
			else {
				batchJSONObjects.add(batchResultJSONObject);
			}
		}

		JSONArray batchesJSONArray = new JSONArray();

		for (Map.Entry<String, List<JSONObject>> batchJSONObjectsEntry :
				batchJSONObjectsMap.entrySet()) {

			JSONObject batchJSONObject = new JSONObject();

			batchJSONObject.put("batchName", batchJSONObjectsEntry.getKey());

			JSONArray buildsJSONArray = new JSONArray();

			for (JSONObject jsonObject : batchJSONObjectsEntry.getValue()) {
				if (!jsonObject.has("buildURL") ||
					!jsonObject.has("duration") || !jsonObject.has("result") ||
					!jsonObject.has("stopWatchRecords")) {

					continue;
				}

				JSONObject buildJSONObject = new JSONObject();

				buildJSONObject.put("axisName", jsonObject.opt("axisName"));
				buildJSONObject.put("buildURL", jsonObject.get("buildURL"));
				buildJSONObject.put("duration", jsonObject.get("duration"));
				buildJSONObject.put("result", jsonObject.get("result"));

				long startTime = jsonObject.optLong("startTime", 0L);

				if (startTime == 0L) {
					StopWatchRecordsGroup stopWatchRecordsGroup =
						new StopWatchRecordsGroup(jsonObject);

					StopWatchRecord stopWatchRecord = stopWatchRecordsGroup.get(
						"start.current.job");

					if (stopWatchRecord == null) {
						stopWatchRecord = stopWatchRecordsGroup.get(
							"run.current.job");
					}

					if (stopWatchRecord == null) {
						continue;
					}

					startTime = stopWatchRecord.getStartTimestamp();
				}

				buildJSONObject.put("startTime", startTime);

				buildJSONObject.put("status", "completed");
				buildJSONObject.put(
					"stopWatchRecords", jsonObject.get("stopWatchRecords"));
				buildJSONObject.put("testResults", new JSONArray());

				buildsJSONArray.put(buildJSONObject);
			}

			batchJSONObject.put("builds", buildsJSONArray);

			batchesJSONArray.put(batchJSONObject);
		}

		return batchesJSONArray;
	}

	private String _getTestSuiteNameFromBuildResult(
		JSONObject buildResultJSONObject) {

		String testSuiteName = buildResultJSONObject.optString("testSuiteName");

		if (!JenkinsResultsParserUtil.isNullOrEmpty(testSuiteName)) {
			return testSuiteName;
		}

		JSONArray batchResultsJSONArray = buildResultJSONObject.optJSONArray(
			"batchResults");

		if (batchResultsJSONArray == null) {
			return null;
		}

		for (int i = 0; i < batchResultsJSONArray.length(); i++) {
			JSONObject batchResultJSONObject =
				batchResultsJSONArray.getJSONObject(i);

			testSuiteName = batchResultJSONObject.optString("testSuiteName");

			if (!JenkinsResultsParserUtil.isNullOrEmpty(testSuiteName)) {
				return testSuiteName;
			}
		}

		return null;
	}

	private String _getVariableFromJenkinsConsole(String variableName) {
		File jenkinsConsoleLocalFile = getJenkinsConsoleLocalFile();

		if ((jenkinsConsoleLocalFile == null) ||
			!jenkinsConsoleLocalFile.exists()) {

			return null;
		}

		Pattern pattern = _getVariablePattern(variableName);

		BufferedReader bufferedReader = null;

		FileReader fileReader = null;

		try {
			fileReader = new FileReader(jenkinsConsoleLocalFile);

			bufferedReader = new BufferedReader(fileReader);

			String line;

			long start = JenkinsResultsParserUtil.getCurrentTimeMillis();

			while ((line = bufferedReader.readLine()) != null) {
				long end = JenkinsResultsParserUtil.getCurrentTimeMillis();

				long duration = end - start;

				if (duration >= (5 * 1000)) {
					break;
				}

				Matcher matcher = pattern.matcher(line);

				if (!matcher.find()) {
					continue;
				}

				return matcher.group("variableValue");
			}
		}
		catch (Exception exception) {
			return null;
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}

			if (fileReader != null) {
				try {
					fileReader.close();
				}
				catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		}

		return null;
	}

	private Pattern _getVariablePattern(String variableName) {
		Pattern variablePattern = _variablePatterns.get(variableName);

		if (variablePattern == null) {
			variablePattern = Pattern.compile(
				variableName + "=(?<variableValue>[^\\s]+)");

			_variablePatterns.put(variableName, variablePattern);
		}

		return variablePattern;
	}

	private static final Map<String, Pattern> _variablePatterns =
		new HashMap<>();

	private List<DownstreamBuildReport> _downstreamBuildReports;

}