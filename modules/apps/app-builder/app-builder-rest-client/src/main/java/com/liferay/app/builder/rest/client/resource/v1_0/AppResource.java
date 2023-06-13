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

package com.liferay.app.builder.rest.client.resource.v1_0;

import com.liferay.app.builder.rest.client.constant.v1_0.DeploymentAction;
import com.liferay.app.builder.rest.client.dto.v1_0.App;
import com.liferay.app.builder.rest.client.http.HttpInvoker;
import com.liferay.app.builder.rest.client.pagination.Page;
import com.liferay.app.builder.rest.client.pagination.Pagination;
import com.liferay.app.builder.rest.client.problem.Problem;
import com.liferay.app.builder.rest.client.serdes.v1_0.AppSerDes;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Generated;

/**
 * @author Gabriel Albuquerque
 * @generated
 */
@Generated("")
public interface AppResource {

	public static Builder builder() {
		return new Builder();
	}

	public void deleteApp(Long appId) throws Exception;

	public HttpInvoker.HttpResponse deleteAppHttpResponse(Long appId)
		throws Exception;

	public App getApp(Long appId) throws Exception;

	public HttpInvoker.HttpResponse getAppHttpResponse(Long appId)
		throws Exception;

	public App putApp(Long appId, App app) throws Exception;

	public HttpInvoker.HttpResponse putAppHttpResponse(Long appId, App app)
		throws Exception;

	public void putAppDeployment(Long appId, DeploymentAction deploymentAction)
		throws Exception;

	public HttpInvoker.HttpResponse putAppDeploymentHttpResponse(
			Long appId, DeploymentAction deploymentAction)
		throws Exception;

	public Page<App> getDataDefinitionAppsPage(
			Long dataDefinitionId, String keywords, Pagination pagination,
			String sortString)
		throws Exception;

	public HttpInvoker.HttpResponse getDataDefinitionAppsPageHttpResponse(
			Long dataDefinitionId, String keywords, Pagination pagination,
			String sortString)
		throws Exception;

	public App postDataDefinitionApp(Long dataDefinitionId, App app)
		throws Exception;

	public HttpInvoker.HttpResponse postDataDefinitionAppHttpResponse(
			Long dataDefinitionId, App app)
		throws Exception;

	public Page<App> getSiteAppsPage(
			Long siteId, String keywords, Pagination pagination,
			String sortString)
		throws Exception;

	public HttpInvoker.HttpResponse getSiteAppsPageHttpResponse(
			Long siteId, String keywords, Pagination pagination,
			String sortString)
		throws Exception;

	public static class Builder {

		public Builder authentication(String login, String password) {
			_login = login;
			_password = password;

			return this;
		}

		public AppResource build() {
			return new AppResourceImpl(this);
		}

		public Builder endpoint(String host, int port, String scheme) {
			_host = host;
			_port = port;
			_scheme = scheme;

			return this;
		}

		public Builder header(String key, String value) {
			_headers.put(key, value);

			return this;
		}

		public Builder locale(Locale locale) {
			_locale = locale;

			return this;
		}

		public Builder parameter(String key, String value) {
			_parameters.put(key, value);

			return this;
		}

		private Builder() {
		}

		private Map<String, String> _headers = new LinkedHashMap<>();
		private String _host = "localhost";
		private Locale _locale;
		private String _login = "test@liferay.com";
		private String _password = "test";
		private Map<String, String> _parameters = new LinkedHashMap<>();
		private int _port = 8080;
		private String _scheme = "http";

	}

	public static class AppResourceImpl implements AppResource {

		public void deleteApp(Long appId) throws Exception {
			HttpInvoker.HttpResponse httpResponse = deleteAppHttpResponse(
				appId);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return;
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse deleteAppHttpResponse(Long appId)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.DELETE);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port + "/o/app-builder/v1.0/apps/{appId}",
				appId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public App getApp(Long appId) throws Exception {
			HttpInvoker.HttpResponse httpResponse = getAppHttpResponse(appId);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return AppSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse getAppHttpResponse(Long appId)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port + "/o/app-builder/v1.0/apps/{appId}",
				appId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public App putApp(Long appId, App app) throws Exception {
			HttpInvoker.HttpResponse httpResponse = putAppHttpResponse(
				appId, app);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return AppSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse putAppHttpResponse(Long appId, App app)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(app.toString(), "application/json");

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.PUT);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port + "/o/app-builder/v1.0/apps/{appId}",
				appId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public void putAppDeployment(
				Long appId, DeploymentAction deploymentAction)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				putAppDeploymentHttpResponse(appId, deploymentAction);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());
		}

		public HttpInvoker.HttpResponse putAppDeploymentHttpResponse(
				Long appId, DeploymentAction deploymentAction)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(deploymentAction.toString(), "application/json");

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.PUT);

			if (deploymentAction != null) {
				httpInvoker.parameter(
					"deploymentAction", String.valueOf(deploymentAction));
			}

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/app-builder/v1.0/apps/{appId}/deployment",
				appId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Page<App> getDataDefinitionAppsPage(
				Long dataDefinitionId, String keywords, Pagination pagination,
				String sortString)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				getDataDefinitionAppsPageHttpResponse(
					dataDefinitionId, keywords, pagination, sortString);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return Page.of(content, AppSerDes::toDTO);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse getDataDefinitionAppsPageHttpResponse(
				Long dataDefinitionId, String keywords, Pagination pagination,
				String sortString)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

			if (keywords != null) {
				httpInvoker.parameter("keywords", String.valueOf(keywords));
			}

			if (pagination != null) {
				httpInvoker.parameter(
					"page", String.valueOf(pagination.getPage()));
				httpInvoker.parameter(
					"pageSize", String.valueOf(pagination.getPageSize()));
			}

			if (sortString != null) {
				httpInvoker.parameter("sort", sortString);
			}

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/app-builder/v1.0/data-definitions/{dataDefinitionId}/apps",
				dataDefinitionId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public App postDataDefinitionApp(Long dataDefinitionId, App app)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				postDataDefinitionAppHttpResponse(dataDefinitionId, app);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return AppSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse postDataDefinitionAppHttpResponse(
				Long dataDefinitionId, App app)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(app.toString(), "application/json");

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/app-builder/v1.0/data-definitions/{dataDefinitionId}/apps",
				dataDefinitionId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Page<App> getSiteAppsPage(
				Long siteId, String keywords, Pagination pagination,
				String sortString)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse = getSiteAppsPageHttpResponse(
				siteId, keywords, pagination, sortString);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return Page.of(content, AppSerDes::toDTO);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse getSiteAppsPageHttpResponse(
				Long siteId, String keywords, Pagination pagination,
				String sortString)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

			if (keywords != null) {
				httpInvoker.parameter("keywords", String.valueOf(keywords));
			}

			if (pagination != null) {
				httpInvoker.parameter(
					"page", String.valueOf(pagination.getPage()));
				httpInvoker.parameter(
					"pageSize", String.valueOf(pagination.getPageSize()));
			}

			if (sortString != null) {
				httpInvoker.parameter("sort", sortString);
			}

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port + "/o/app-builder/v1.0/sites/{siteId}/apps",
				siteId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		private AppResourceImpl(Builder builder) {
			_builder = builder;
		}

		private static final Logger _logger = Logger.getLogger(
			AppResource.class.getName());

		private Builder _builder;

	}

}