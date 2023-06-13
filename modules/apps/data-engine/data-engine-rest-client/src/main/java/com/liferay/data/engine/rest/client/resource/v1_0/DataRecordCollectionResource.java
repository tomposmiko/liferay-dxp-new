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

package com.liferay.data.engine.rest.client.resource.v1_0;

import com.liferay.data.engine.rest.client.dto.v1_0.DataRecordCollection;
import com.liferay.data.engine.rest.client.http.HttpInvoker;
import com.liferay.data.engine.rest.client.pagination.Page;
import com.liferay.data.engine.rest.client.pagination.Pagination;
import com.liferay.data.engine.rest.client.serdes.v1_0.DataRecordCollectionSerDes;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Generated;

/**
 * @author Jeyvison Nascimento
 * @generated
 */
@Generated("")
public class DataRecordCollectionResource {

	public static Page<DataRecordCollection>
			getDataDefinitionDataRecordCollectionsPage(
				Long dataDefinitionId, String keywords, Pagination pagination)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			getDataDefinitionDataRecordCollectionsPageHttpResponse(
				dataDefinitionId, keywords, pagination);

		String content = httpResponse.getContent();

		_logger.fine("HTTP response content: " + content);

		_logger.fine("HTTP response message: " + httpResponse.getMessage());
		_logger.fine(
			"HTTP response status code: " + httpResponse.getStatusCode());

		return Page.of(content, DataRecordCollectionSerDes::toDTO);
	}

	public static HttpInvoker.HttpResponse
			getDataDefinitionDataRecordCollectionsPageHttpResponse(
				Long dataDefinitionId, String keywords, Pagination pagination)
		throws Exception {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

		if (keywords != null) {
			httpInvoker.parameter("keywords", String.valueOf(keywords));
		}

		if (pagination != null) {
			httpInvoker.parameter("page", String.valueOf(pagination.getPage()));
			httpInvoker.parameter(
				"pageSize", String.valueOf(pagination.getPageSize()));
		}

		httpInvoker.path(
			"http://localhost:8080/o/data-engine/v1.0/data-definitions/{dataDefinitionId}/data-record-collections",
			dataDefinitionId);

		httpInvoker.userNameAndPassword("test@liferay.com:test");

		return httpInvoker.invoke();
	}

	public static DataRecordCollection postDataDefinitionDataRecordCollection(
			Long dataDefinitionId, DataRecordCollection dataRecordCollection)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			postDataDefinitionDataRecordCollectionHttpResponse(
				dataDefinitionId, dataRecordCollection);

		String content = httpResponse.getContent();

		_logger.fine("HTTP response content: " + content);

		_logger.fine("HTTP response message: " + httpResponse.getMessage());
		_logger.fine(
			"HTTP response status code: " + httpResponse.getStatusCode());

		try {
			return DataRecordCollectionSerDes.toDTO(content);
		}
		catch (Exception e) {
			_logger.log(
				Level.WARNING, "Unable to process HTTP response: " + content,
				e);

			throw e;
		}
	}

	public static HttpInvoker.HttpResponse
			postDataDefinitionDataRecordCollectionHttpResponse(
				Long dataDefinitionId,
				DataRecordCollection dataRecordCollection)
		throws Exception {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(dataRecordCollection.toString(), "application/json");

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

		httpInvoker.path(
			"http://localhost:8080/o/data-engine/v1.0/data-definitions/{dataDefinitionId}/data-record-collections",
			dataDefinitionId);

		httpInvoker.userNameAndPassword("test@liferay.com:test");

		return httpInvoker.invoke();
	}

	public static void deleteDataRecordCollection(Long dataRecordCollectionId)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			deleteDataRecordCollectionHttpResponse(dataRecordCollectionId);

		String content = httpResponse.getContent();

		_logger.fine("HTTP response content: " + content);

		_logger.fine("HTTP response message: " + httpResponse.getMessage());
		_logger.fine(
			"HTTP response status code: " + httpResponse.getStatusCode());
	}

	public static HttpInvoker.HttpResponse
			deleteDataRecordCollectionHttpResponse(Long dataRecordCollectionId)
		throws Exception {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.DELETE);

		httpInvoker.path(
			"http://localhost:8080/o/data-engine/v1.0/data-record-collections/{dataRecordCollectionId}",
			dataRecordCollectionId);

		httpInvoker.userNameAndPassword("test@liferay.com:test");

		return httpInvoker.invoke();
	}

	public static DataRecordCollection getDataRecordCollection(
			Long dataRecordCollectionId)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			getDataRecordCollectionHttpResponse(dataRecordCollectionId);

		String content = httpResponse.getContent();

		_logger.fine("HTTP response content: " + content);

		_logger.fine("HTTP response message: " + httpResponse.getMessage());
		_logger.fine(
			"HTTP response status code: " + httpResponse.getStatusCode());

		try {
			return DataRecordCollectionSerDes.toDTO(content);
		}
		catch (Exception e) {
			_logger.log(
				Level.WARNING, "Unable to process HTTP response: " + content,
				e);

			throw e;
		}
	}

	public static HttpInvoker.HttpResponse getDataRecordCollectionHttpResponse(
			Long dataRecordCollectionId)
		throws Exception {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

		httpInvoker.path(
			"http://localhost:8080/o/data-engine/v1.0/data-record-collections/{dataRecordCollectionId}",
			dataRecordCollectionId);

		httpInvoker.userNameAndPassword("test@liferay.com:test");

		return httpInvoker.invoke();
	}

	public static DataRecordCollection putDataRecordCollection(
			Long dataRecordCollectionId,
			DataRecordCollection dataRecordCollection)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			putDataRecordCollectionHttpResponse(
				dataRecordCollectionId, dataRecordCollection);

		String content = httpResponse.getContent();

		_logger.fine("HTTP response content: " + content);

		_logger.fine("HTTP response message: " + httpResponse.getMessage());
		_logger.fine(
			"HTTP response status code: " + httpResponse.getStatusCode());

		try {
			return DataRecordCollectionSerDes.toDTO(content);
		}
		catch (Exception e) {
			_logger.log(
				Level.WARNING, "Unable to process HTTP response: " + content,
				e);

			throw e;
		}
	}

	public static HttpInvoker.HttpResponse putDataRecordCollectionHttpResponse(
			Long dataRecordCollectionId,
			DataRecordCollection dataRecordCollection)
		throws Exception {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(dataRecordCollection.toString(), "application/json");

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.PUT);

		httpInvoker.path(
			"http://localhost:8080/o/data-engine/v1.0/data-record-collections/{dataRecordCollectionId}",
			dataRecordCollectionId);

		httpInvoker.userNameAndPassword("test@liferay.com:test");

		return httpInvoker.invoke();
	}

	public static void postDataRecordCollectionDataRecordCollectionPermission(
			Long dataRecordCollectionId, String operation,
			com.liferay.data.engine.rest.client.dto.v1_0.
				DataRecordCollectionPermission dataRecordCollectionPermission)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			postDataRecordCollectionDataRecordCollectionPermissionHttpResponse(
				dataRecordCollectionId, operation,
				dataRecordCollectionPermission);

		String content = httpResponse.getContent();

		_logger.fine("HTTP response content: " + content);

		_logger.fine("HTTP response message: " + httpResponse.getMessage());
		_logger.fine(
			"HTTP response status code: " + httpResponse.getStatusCode());
	}

	public static HttpInvoker.HttpResponse
			postDataRecordCollectionDataRecordCollectionPermissionHttpResponse(
				Long dataRecordCollectionId, String operation,
				com.liferay.data.engine.rest.client.dto.v1_0.
					DataRecordCollectionPermission
						dataRecordCollectionPermission)
		throws Exception {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(
			dataRecordCollectionPermission.toString(), "application/json");

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

		if (operation != null) {
			httpInvoker.parameter("operation", String.valueOf(operation));
		}

		httpInvoker.path(
			"http://localhost:8080/o/data-engine/v1.0/data-record-collections/{dataRecordCollectionId}/data-record-collection-permissions",
			dataRecordCollectionId);

		httpInvoker.userNameAndPassword("test@liferay.com:test");

		return httpInvoker.invoke();
	}

	public static void postSiteDataRecordCollectionPermission(
			Long siteId, String operation,
			com.liferay.data.engine.rest.client.dto.v1_0.
				DataRecordCollectionPermission dataRecordCollectionPermission)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			postSiteDataRecordCollectionPermissionHttpResponse(
				siteId, operation, dataRecordCollectionPermission);

		String content = httpResponse.getContent();

		_logger.fine("HTTP response content: " + content);

		_logger.fine("HTTP response message: " + httpResponse.getMessage());
		_logger.fine(
			"HTTP response status code: " + httpResponse.getStatusCode());
	}

	public static HttpInvoker.HttpResponse
			postSiteDataRecordCollectionPermissionHttpResponse(
				Long siteId, String operation,
				com.liferay.data.engine.rest.client.dto.v1_0.
					DataRecordCollectionPermission
						dataRecordCollectionPermission)
		throws Exception {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(
			dataRecordCollectionPermission.toString(), "application/json");

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

		if (operation != null) {
			httpInvoker.parameter("operation", String.valueOf(operation));
		}

		httpInvoker.path(
			"http://localhost:8080/o/data-engine/v1.0/sites/{siteId}/data-record-collection-permissions",
			siteId);

		httpInvoker.userNameAndPassword("test@liferay.com:test");

		return httpInvoker.invoke();
	}

	public static Page<DataRecordCollection> getSiteDataRecordCollectionsPage(
			Long siteId, String keywords, Pagination pagination)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			getSiteDataRecordCollectionsPageHttpResponse(
				siteId, keywords, pagination);

		String content = httpResponse.getContent();

		_logger.fine("HTTP response content: " + content);

		_logger.fine("HTTP response message: " + httpResponse.getMessage());
		_logger.fine(
			"HTTP response status code: " + httpResponse.getStatusCode());

		return Page.of(content, DataRecordCollectionSerDes::toDTO);
	}

	public static HttpInvoker.HttpResponse
			getSiteDataRecordCollectionsPageHttpResponse(
				Long siteId, String keywords, Pagination pagination)
		throws Exception {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

		if (keywords != null) {
			httpInvoker.parameter("keywords", String.valueOf(keywords));
		}

		if (pagination != null) {
			httpInvoker.parameter("page", String.valueOf(pagination.getPage()));
			httpInvoker.parameter(
				"pageSize", String.valueOf(pagination.getPageSize()));
		}

		httpInvoker.path(
			"http://localhost:8080/o/data-engine/v1.0/sites/{siteId}/data-record-collections",
			siteId);

		httpInvoker.userNameAndPassword("test@liferay.com:test");

		return httpInvoker.invoke();
	}

	private static final Logger _logger = Logger.getLogger(
		DataRecordCollectionResource.class.getName());

}