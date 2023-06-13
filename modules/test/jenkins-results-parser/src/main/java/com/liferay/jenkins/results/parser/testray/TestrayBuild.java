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

package com.liferay.jenkins.results.parser.testray;

import com.liferay.jenkins.results.parser.BuildReportFactory;
import com.liferay.jenkins.results.parser.JenkinsResultsParserUtil;
import com.liferay.jenkins.results.parser.TopLevelBuildReport;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public class TestrayBuild {

	public TestrayBuild(TestrayRoutine testrayRoutine, JSONObject jsonObject) {
		_testrayRoutine = testrayRoutine;
		_jsonObject = jsonObject;

		_testrayProject = _testrayRoutine.getTestrayProject();
		_testrayServer = _testrayRoutine.getTestrayServer();

		_testrayProductVersion = _testrayProject.getTestrayProductVersionByID(
			_jsonObject.getInt("testrayProductVersionId"));
	}

	public String getDescription() {
		return _jsonObject.getString("description");
	}

	public int getID() {
		return _jsonObject.getInt("testrayBuildId");
	}

	public String getName() {
		return _jsonObject.getString("name");
	}

	public String getStartYearMonth() {
		Matcher matcher = _getTestrayAttachmentURLMatcher();

		if (matcher == null) {
			return null;
		}

		return matcher.group("startYearMonth");
	}

	public List<TestrayCaseResult> getTestrayCaseResults() {
		List<TestrayCaseResult> testrayCaseResults = new ArrayList<>();

		String urlString = String.valueOf(getURL());

		String caseResultsAPIURLString = urlString.replace(
			"runs", "case_results.json");

		try {
			JSONObject jsonObject = JenkinsResultsParserUtil.toJSONObject(
				caseResultsAPIURLString);

			JSONArray dataJSONArray = jsonObject.getJSONArray("data");

			for (int i = 0; i < dataJSONArray.length(); i++) {
				JSONObject dataJSONObject = dataJSONArray.getJSONObject(i);

				TestrayCaseResult testrayCaseResult = new TestrayCaseResult(
					this, dataJSONObject);

				testrayCaseResults.add(testrayCaseResult);
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}

		return testrayCaseResults;
	}

	public TestrayProductVersion getTestrayProductVersion() {
		return _testrayProductVersion;
	}

	public TestrayProject getTestrayProject() {
		return _testrayProject;
	}

	public TestrayRoutine getTestrayRoutine() {
		return _testrayRoutine;
	}

	public TestrayServer getTestrayServer() {
		return _testrayServer;
	}

	public TopLevelBuildReport getTopLevelBuildReport() {
		if (_topLevelBuildReport != null) {
			return _topLevelBuildReport;
		}

		_topLevelBuildReport = BuildReportFactory.newTopLevelBuildReport(this);

		return _topLevelBuildReport;
	}

	public URL getTopLevelBuildURL() {
		Matcher matcher = _getTestrayAttachmentURLMatcher();

		if (matcher == null) {
			return null;
		}

		try {
			return new URL(
				JenkinsResultsParserUtil.combine(
					"https://", matcher.group("topLevelMasterHostname"),
					".liferay.com/job/", matcher.group("topLevelJobName"), "/",
					matcher.group("topLevelBuildNumber"), "/"));
		}
		catch (MalformedURLException malformedURLException) {
			throw new RuntimeException(malformedURLException);
		}
	}

	public URL getURL() {
		try {
			return new URL(_jsonObject.getString("htmlURL"));
		}
		catch (MalformedURLException malformedURLException) {
			throw new RuntimeException(malformedURLException);
		}
	}

	private Matcher _getTestrayAttachmentURLMatcher() {
		if (_testrayAttachmentURLMatcher != null) {
			return _testrayAttachmentURLMatcher;
		}

		List<TestrayCaseResult> testrayCaseResults = getTestrayCaseResults();

		if (testrayCaseResults.isEmpty()) {
			return null;
		}

		int count = 0;

		for (TestrayCaseResult testrayCaseResult : testrayCaseResults) {
			count++;

			if (count >= 5) {
				break;
			}

			for (TestrayAttachment testrayAttachment :
					testrayCaseResult.getTestrayAttachments()) {

				Matcher testrayAttachmentURLMatcher =
					_testrayAttachmentURLPattern.matcher(
						String.valueOf(testrayAttachment.getURL()));

				if (testrayAttachmentURLMatcher.find()) {
					_testrayAttachmentURLMatcher = testrayAttachmentURLMatcher;

					return _testrayAttachmentURLMatcher;
				}
			}
		}

		return null;
	}

	private static final Pattern _testrayAttachmentURLPattern = Pattern.compile(
		JenkinsResultsParserUtil.combine(
			"https://testray.liferay.com/reports/production/logs/",
			"(?<startYearMonth>\\d{4}-\\d{2})/",
			"(?<topLevelMasterHostname>test-\\d+-\\d+)/",
			"(?<topLevelJobName>[^/]+)/(?<topLevelBuildNumber>\\d+)/.*"));

	private final JSONObject _jsonObject;
	private Matcher _testrayAttachmentURLMatcher;
	private final TestrayProductVersion _testrayProductVersion;
	private final TestrayProject _testrayProject;
	private final TestrayRoutine _testrayRoutine;
	private final TestrayServer _testrayServer;
	private TopLevelBuildReport _topLevelBuildReport;

}