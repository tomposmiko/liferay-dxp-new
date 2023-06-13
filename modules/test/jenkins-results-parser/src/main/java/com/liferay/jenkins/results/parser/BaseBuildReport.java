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

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public abstract class BaseBuildReport implements BuildReport {

	@Override
	public int getBuildNumber() {
		Matcher matcher = _buildURLPattern.matcher(
			String.valueOf(getBuildURL()));

		if (!matcher.find()) {
			throw new RuntimeException("Invalid Build URL: " + getBuildURL());
		}

		return Integer.parseInt(matcher.group("buildNumber"));
	}

	@Override
	public JSONObject getBuildReportJSONObject() {
		if (buildReportJSONObject != null) {
			return buildReportJSONObject;
		}

		JSONObject buildJSONObject = getBuildJSONObject();

		buildReportJSONObject = new JSONObject();

		buildReportJSONObject.put("duration", buildJSONObject.get("duration"));
		buildReportJSONObject.put("result", buildJSONObject.get("result"));
		buildReportJSONObject.put(
			"startTime", buildJSONObject.get("timestamp"));

		return buildReportJSONObject;
	}

	@Override
	public URL getBuildURL() {
		return _buildURL;
	}

	@Override
	public long getDuration() {
		JSONObject buildReportJSONObject = getBuildReportJSONObject();

		return buildReportJSONObject.getLong("duration");
	}

	@Override
	public String getJobName() {
		Matcher matcher = _buildURLPattern.matcher(
			String.valueOf(getBuildURL()));

		if (!matcher.find()) {
			throw new RuntimeException("Invalid Build URL: " + getBuildURL());
		}

		return matcher.group("jobName");
	}

	@Override
	public JobReport getJobReport() {
		if (_jobReport != null) {
			return _jobReport;
		}

		Matcher matcher = _buildURLPattern.matcher(
			String.valueOf(getBuildURL()));

		if (!matcher.find()) {
			throw new RuntimeException("Invalid Build URL: " + getBuildURL());
		}

		try {
			_jobReport = JobReport.getInstance(
				new URL(matcher.group("jobURL")));
		}
		catch (MalformedURLException malformedURLException) {
			throw new RuntimeException(malformedURLException);
		}

		return _jobReport;
	}

	@Override
	public String getResult() {
		JSONObject buildReportJSONObject = getBuildReportJSONObject();

		return buildReportJSONObject.getString("result");
	}

	@Override
	public Date getStartDate() {
		if (_startDate != null) {
			return _startDate;
		}

		JSONObject buildReportJSONObject = getBuildReportJSONObject();

		if (buildReportJSONObject.has("startTime")) {
			_startDate = new Date(buildReportJSONObject.getLong("startTime"));
		}
		else {
			JSONObject buildJSONObject = getBuildJSONObject();

			_startDate = new Date(buildJSONObject.getLong("timestamp"));
		}

		return _startDate;
	}

	@Override
	public StopWatchRecordsGroup getStopWatchRecordsGroup() {
		return new StopWatchRecordsGroup(getBuildReportJSONObject());
	}

	protected BaseBuildReport(JSONObject buildReportJSONObject) {
		this.buildReportJSONObject = buildReportJSONObject;

		String buildURLString = buildReportJSONObject.getString("buildURL");

		Matcher matcher = _buildURLPattern.matcher(buildURLString);

		if (!matcher.find()) {
			throw new RuntimeException("Invalid Build URL: " + buildURLString);
		}

		try {
			_buildURL = new URL(
				JenkinsResultsParserUtil.combine(
					"https://", matcher.group("masterHostname"),
					".liferay.com/job/", matcher.group("jobName"), "/",
					matcher.group("buildNumber")));
		}
		catch (MalformedURLException malformedURLException) {
			throw new RuntimeException(malformedURLException);
		}
	}

	protected BaseBuildReport(JSONObject buildJSONObject, JobReport jobReport) {
		_buildJSONObject = buildJSONObject;
		_jobReport = jobReport;

		String buildURLString = buildJSONObject.getString("url");

		Matcher matcher = _buildURLPattern.matcher(buildURLString);

		if (!matcher.find()) {
			throw new RuntimeException("Invalid Build URL: " + buildURLString);
		}

		try {
			_buildURL = new URL(
				JenkinsResultsParserUtil.combine(
					"https://", matcher.group("masterHostname"),
					".liferay.com/job/", matcher.group("jobName"), "/",
					matcher.group("buildNumber")));
		}
		catch (MalformedURLException malformedURLException) {
			throw new RuntimeException(malformedURLException);
		}
	}

	protected BaseBuildReport(String buildURLString) {
		Matcher matcher = _buildURLPattern.matcher(buildURLString);

		if (!matcher.find()) {
			throw new RuntimeException("Invalid Build URL: " + buildURLString);
		}

		try {
			_buildURL = new URL(
				JenkinsResultsParserUtil.combine(
					"https://", matcher.group("masterHostname"),
					".liferay.com/job/", matcher.group("jobName"), "/",
					matcher.group("buildNumber")));
		}
		catch (MalformedURLException malformedURLException) {
			throw new RuntimeException(malformedURLException);
		}
	}

	protected BaseBuildReport(URL buildURL) {
		this(String.valueOf(buildURL));
	}

	protected JSONObject getBuildJSONObject() {
		if (_buildJSONObject != null) {
			return _buildJSONObject;
		}

		try {
			_buildJSONObject = JenkinsResultsParserUtil.toJSONObject(
				getBuildURL() + "/api/json");
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}

		return _buildJSONObject;
	}

	protected void setStartDate(Date startDate) {
		_startDate = startDate;
	}

	protected JSONObject buildReportJSONObject;

	private static final Pattern _buildURLPattern = Pattern.compile(
		"(?<jobURL>https?://(?<masterHostname>test-\\d+-\\d+)" +
			"(\\.liferay\\.com)?/job/(?<jobName>[^/]+))" +
				"(/AXIS_VARIABLE=(?<axisVariable>\\d+))?/(?<buildNumber>\\d+)");

	private JSONObject _buildJSONObject;
	private final URL _buildURL;
	private JobReport _jobReport;
	private Date _startDate;

}