/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.engine.client.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.faro.engine.client.exception.FaroEngineClientException;
import com.liferay.osb.faro.engine.client.model.LCPProject;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

/**
 * @author Geyson Silva
 */
public class EngineServiceURLUtil {

	public static String getBackendExternalURL(FaroProject faroProject)
		throws URISyntaxException {

		String url = getBackendURL(faroProject, null);

		if (_OSB_ASAH_BACKEND_URL.contains("{wedeploy}")) {
			return url;
		}

		return _getExternalURL(url);
	}

	public static String getBackendURL(FaroProject faroProject, String path)
		throws URISyntaxException {

		String url = StringUtil.replace(
			_getClusterBaseURL(faroProject), "{service}", "osbasahbackend");

		return _getURL(faroProject, url, path);
	}

	public static String getPublisherExternalURL(FaroProject faroProject)
		throws URISyntaxException {

		String url = getPublisherURL(faroProject, null);

		if (_OSB_ASAH_PUBLISHER_URL.contains("{wedeploy}")) {
			return url;
		}

		return _getExternalURL(url);
	}

	public static String getPublisherURL(FaroProject faroProject, String path)
		throws URISyntaxException {

		String url = StringUtil.replace(
			_getClusterBaseURL(faroProject), "{service}", "osbasahpublisher");

		return _getURL(faroProject, url, path);
	}

	private static String _getClusterBaseURL(FaroProject faroProject) {
		if (StringUtils.isNotBlank(_OSB_ASAH_LOCAL_CLUSTER_URL)) {
			return _OSB_ASAH_LOCAL_CLUSTER_URL;
		}

		LCPProject.Cluster cluster = LCPProject.Cluster.fromString(
			faroProject.getServerLocation());

		if (cluster == null) {
			throw new FaroEngineClientException(
				"Invalid server location: " + faroProject.getServerLocation());
		}

		return cluster.getBaseURL();
	}

	private static String _getExternalURL(String url) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			Map<String, Object> map = objectMapper.readValue(
				new URL(url.concat("/context")),
				new TypeReference<Map<String, Object>>() {
				});

			Map<String, Object> environment = (Map<String, Object>)map.get(
				"environment");

			return (String)environment.getOrDefault("EXTERNAL_URL", url);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception, exception);
			}

			return url;
		}
	}

	private static String _getURL(
			FaroProject faroProject, String url, String path)
		throws URISyntaxException {

		url = StringUtil.replace(
			url, "{weDeployKey}", faroProject.getWeDeployKey());

		URIBuilder uriBuilder = new URIBuilder(url);

		uriBuilder.setPath(path);

		URI uri = uriBuilder.build();

		return uri.toString();
	}

	private static final String _OSB_ASAH_BACKEND_URL = System.getenv(
		"OSB_ASAH_BACKEND_URL");

	private static final String _OSB_ASAH_LOCAL_CLUSTER_URL = System.getenv(
		"OSB_ASAH_LOCAL_CLUSTER_URL");

	private static final String _OSB_ASAH_PUBLISHER_URL = System.getenv(
		"OSB_ASAH_PUBLISHER_URL");

	private static final Log _log = LogFactoryUtil.getLog(
		EngineServiceURLUtil.class);

}