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

package com.liferay.headless.delivery.internal.resource.v1_0;

import com.liferay.headless.delivery.dto.v1_0.MessageBoardThread;
import com.liferay.headless.delivery.dto.v1_0.Rating;
import com.liferay.headless.delivery.resource.v1_0.MessageBoardThreadResource;
import com.liferay.petra.function.UnsafeBiConsumer;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.ResourceAction;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourceLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.filter.ExpressionConvert;
import com.liferay.portal.odata.filter.FilterParser;
import com.liferay.portal.odata.filter.FilterParserProvider;
import com.liferay.portal.odata.sort.SortField;
import com.liferay.portal.odata.sort.SortParser;
import com.liferay.portal.odata.sort.SortParserProvider;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.VulcanBatchEngineTaskItemDelegate;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.permission.Permission;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.ActionUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.io.Serializable;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
@javax.ws.rs.Path("/v1.0")
public abstract class BaseMessageBoardThreadResourceImpl
	implements EntityModelResource, MessageBoardThreadResource,
			   VulcanBatchEngineTaskItemDelegate<MessageBoardThread> {

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-sections/{messageBoardSectionId}/message-board-threads'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the message board section's threads. Results can be paginated, filtered, searched, and sorted."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardSectionId"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "aggregationTerms"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "filter"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "page"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "pageSize"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "search"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "sort"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path(
		"/message-board-sections/{messageBoardSectionId}/message-board-threads"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Page<MessageBoardThread>
			getMessageBoardSectionMessageBoardThreadsPage(
				@io.swagger.v3.oas.annotations.Parameter(hidden = true)
				@javax.validation.constraints.NotNull
				@javax.ws.rs.PathParam("messageBoardSectionId")
				Long messageBoardSectionId,
				@io.swagger.v3.oas.annotations.Parameter(hidden = true)
				@javax.ws.rs.QueryParam("search")
				String search,
				@javax.ws.rs.core.Context
					com.liferay.portal.vulcan.aggregation.Aggregation
						aggregation,
				@javax.ws.rs.core.Context Filter filter,
				@javax.ws.rs.core.Context Pagination pagination,
				@javax.ws.rs.core.Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-sections/{messageBoardSectionId}/message-board-threads' -d $'{"articleBody": ___, "creatorStatistics": ___, "customFields": ___, "encodingFormat": ___, "friendlyUrlPath": ___, "hasValidAnswer": ___, "headline": ___, "keywords": ___, "messageBoardSectionId": ___, "seen": ___, "showAsQuestion": ___, "subscribed": ___, "taxonomyCategoryIds": ___, "threadType": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Creates a new message board thread inside a section."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardSectionId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path(
		"/message-board-sections/{messageBoardSectionId}/message-board-threads"
	)
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public MessageBoardThread postMessageBoardSectionMessageBoardThread(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardSectionId")
			Long messageBoardSectionId,
			MessageBoardThread messageBoardThread)
		throws Exception {

		return new MessageBoardThread();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-sections/{messageBoardSectionId}/message-board-threads/batch'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardSectionId"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "callbackURL"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.Path(
		"/message-board-sections/{messageBoardSectionId}/message-board-threads/batch"
	)
	@javax.ws.rs.POST
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response postMessageBoardSectionMessageBoardThreadBatch(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardSectionId")
			Long messageBoardSectionId,
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("callbackURL")
			String callbackURL,
			Object object)
		throws Exception {

		vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(
			contextAcceptLanguage);
		vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
		vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(
			contextHttpServletRequest);
		vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
		vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

		Response.ResponseBuilder responseBuilder = Response.accepted();

		return responseBuilder.entity(
			vulcanBatchEngineImportTaskResource.postImportTask(
				MessageBoardThread.class.getName(), callbackURL, null, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/ranked'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "dateCreated"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "dateModified"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "messageBoardSectionId"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "page"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "pageSize"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "sort"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path("/message-board-threads/ranked")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Page<MessageBoardThread> getMessageBoardThreadsRankedPage(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("dateCreated")
			java.util.Date dateCreated,
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("dateModified")
			java.util.Date dateModified,
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("messageBoardSectionId")
			Long messageBoardSectionId,
			@javax.ws.rs.core.Context Pagination pagination,
			@javax.ws.rs.core.Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/{messageBoardThreadId}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Deletes the message board thread and returns a 204 if the operation succeeds."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardThreadId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("/message-board-threads/{messageBoardThreadId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public void deleteMessageBoardThread(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardThreadId")
			Long messageBoardThreadId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/batch'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "callbackURL"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("/message-board-threads/batch")
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response deleteMessageBoardThreadBatch(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("callbackURL")
			String callbackURL,
			Object object)
		throws Exception {

		vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(
			contextAcceptLanguage);
		vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
		vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(
			contextHttpServletRequest);
		vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
		vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

		Response.ResponseBuilder responseBuilder = Response.accepted();

		return responseBuilder.entity(
			vulcanBatchEngineImportTaskResource.deleteImportTask(
				MessageBoardThread.class.getName(), callbackURL, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/{messageBoardThreadId}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the message board thread."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardThreadId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path("/message-board-threads/{messageBoardThreadId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public MessageBoardThread getMessageBoardThread(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardThreadId")
			Long messageBoardThreadId)
		throws Exception {

		return new MessageBoardThread();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PATCH' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/{messageBoardThreadId}' -d $'{"articleBody": ___, "creatorStatistics": ___, "customFields": ___, "encodingFormat": ___, "friendlyUrlPath": ___, "hasValidAnswer": ___, "headline": ___, "keywords": ___, "messageBoardSectionId": ___, "seen": ___, "showAsQuestion": ___, "subscribed": ___, "taxonomyCategoryIds": ___, "threadType": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Updates only the fields received in the request body, leaving any other fields untouched."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardThreadId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.PATCH
	@javax.ws.rs.Path("/message-board-threads/{messageBoardThreadId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public MessageBoardThread patchMessageBoardThread(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardThreadId")
			Long messageBoardThreadId,
			MessageBoardThread messageBoardThread)
		throws Exception {

		MessageBoardThread existingMessageBoardThread = getMessageBoardThread(
			messageBoardThreadId);

		if (messageBoardThread.getActions() != null) {
			existingMessageBoardThread.setActions(
				messageBoardThread.getActions());
		}

		if (messageBoardThread.getArticleBody() != null) {
			existingMessageBoardThread.setArticleBody(
				messageBoardThread.getArticleBody());
		}

		if (messageBoardThread.getDateCreated() != null) {
			existingMessageBoardThread.setDateCreated(
				messageBoardThread.getDateCreated());
		}

		if (messageBoardThread.getDateModified() != null) {
			existingMessageBoardThread.setDateModified(
				messageBoardThread.getDateModified());
		}

		if (messageBoardThread.getEncodingFormat() != null) {
			existingMessageBoardThread.setEncodingFormat(
				messageBoardThread.getEncodingFormat());
		}

		if (messageBoardThread.getFriendlyUrlPath() != null) {
			existingMessageBoardThread.setFriendlyUrlPath(
				messageBoardThread.getFriendlyUrlPath());
		}

		if (messageBoardThread.getHasValidAnswer() != null) {
			existingMessageBoardThread.setHasValidAnswer(
				messageBoardThread.getHasValidAnswer());
		}

		if (messageBoardThread.getHeadline() != null) {
			existingMessageBoardThread.setHeadline(
				messageBoardThread.getHeadline());
		}

		if (messageBoardThread.getKeywords() != null) {
			existingMessageBoardThread.setKeywords(
				messageBoardThread.getKeywords());
		}

		if (messageBoardThread.getLocked() != null) {
			existingMessageBoardThread.setLocked(
				messageBoardThread.getLocked());
		}

		if (messageBoardThread.getMessageBoardSectionId() != null) {
			existingMessageBoardThread.setMessageBoardSectionId(
				messageBoardThread.getMessageBoardSectionId());
		}

		if (messageBoardThread.getNumberOfMessageBoardAttachments() != null) {
			existingMessageBoardThread.setNumberOfMessageBoardAttachments(
				messageBoardThread.getNumberOfMessageBoardAttachments());
		}

		if (messageBoardThread.getNumberOfMessageBoardMessages() != null) {
			existingMessageBoardThread.setNumberOfMessageBoardMessages(
				messageBoardThread.getNumberOfMessageBoardMessages());
		}

		if (messageBoardThread.getSeen() != null) {
			existingMessageBoardThread.setSeen(messageBoardThread.getSeen());
		}

		if (messageBoardThread.getShowAsQuestion() != null) {
			existingMessageBoardThread.setShowAsQuestion(
				messageBoardThread.getShowAsQuestion());
		}

		if (messageBoardThread.getSiteId() != null) {
			existingMessageBoardThread.setSiteId(
				messageBoardThread.getSiteId());
		}

		if (messageBoardThread.getSubscribed() != null) {
			existingMessageBoardThread.setSubscribed(
				messageBoardThread.getSubscribed());
		}

		if (messageBoardThread.getTaxonomyCategoryIds() != null) {
			existingMessageBoardThread.setTaxonomyCategoryIds(
				messageBoardThread.getTaxonomyCategoryIds());
		}

		if (messageBoardThread.getThreadType() != null) {
			existingMessageBoardThread.setThreadType(
				messageBoardThread.getThreadType());
		}

		if (messageBoardThread.getViewCount() != null) {
			existingMessageBoardThread.setViewCount(
				messageBoardThread.getViewCount());
		}

		if (messageBoardThread.getViewableBy() != null) {
			existingMessageBoardThread.setViewableBy(
				messageBoardThread.getViewableBy());
		}

		preparePatch(messageBoardThread, existingMessageBoardThread);

		return putMessageBoardThread(
			messageBoardThreadId, existingMessageBoardThread);
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/{messageBoardThreadId}' -d $'{"articleBody": ___, "creatorStatistics": ___, "customFields": ___, "encodingFormat": ___, "friendlyUrlPath": ___, "hasValidAnswer": ___, "headline": ___, "keywords": ___, "messageBoardSectionId": ___, "seen": ___, "showAsQuestion": ___, "subscribed": ___, "taxonomyCategoryIds": ___, "threadType": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Replaces the message board thread with the information sent in the request body. Any missing fields are deleted, unless they are required."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardThreadId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path("/message-board-threads/{messageBoardThreadId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public MessageBoardThread putMessageBoardThread(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardThreadId")
			Long messageBoardThreadId,
			MessageBoardThread messageBoardThread)
		throws Exception {

		return new MessageBoardThread();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/batch'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "callbackURL"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.Path("/message-board-threads/batch")
	@javax.ws.rs.Produces("application/json")
	@javax.ws.rs.PUT
	@Override
	public Response putMessageBoardThreadBatch(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("callbackURL")
			String callbackURL,
			Object object)
		throws Exception {

		vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(
			contextAcceptLanguage);
		vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
		vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(
			contextHttpServletRequest);
		vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
		vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

		Response.ResponseBuilder responseBuilder = Response.accepted();

		return responseBuilder.entity(
			vulcanBatchEngineImportTaskResource.putImportTask(
				MessageBoardThread.class.getName(), callbackURL, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/{messageBoardThreadId}/my-rating'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Deletes the message board thread's rating and returns a 204 if the operation succeeds."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardThreadId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("/message-board-threads/{messageBoardThreadId}/my-rating")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public void deleteMessageBoardThreadMyRating(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardThreadId")
			Long messageBoardThreadId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/{messageBoardThreadId}/my-rating'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the message board thread's rating."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardThreadId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path("/message-board-threads/{messageBoardThreadId}/my-rating")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Rating getMessageBoardThreadMyRating(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardThreadId")
			Long messageBoardThreadId)
		throws Exception {

		return new Rating();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/{messageBoardThreadId}/my-rating' -d $'{"ratingValue": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Creates the message board thread's rating."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardThreadId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path("/message-board-threads/{messageBoardThreadId}/my-rating")
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Rating postMessageBoardThreadMyRating(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardThreadId")
			Long messageBoardThreadId,
			Rating rating)
		throws Exception {

		return new Rating();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/{messageBoardThreadId}/my-rating' -d $'{"ratingValue": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Replaces the rating with the information sent in the request body. Any missing fields are deleted, unless they are required."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardThreadId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path("/message-board-threads/{messageBoardThreadId}/my-rating")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public Rating putMessageBoardThreadMyRating(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardThreadId")
			Long messageBoardThreadId,
			Rating rating)
		throws Exception {

		return new Rating();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/{messageBoardThreadId}/subscribe'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardThreadId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Path("/message-board-threads/{messageBoardThreadId}/subscribe")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public void putMessageBoardThreadSubscribe(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardThreadId")
			Long messageBoardThreadId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/message-board-threads/{messageBoardThreadId}/unsubscribe'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "messageBoardThreadId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Path(
		"/message-board-threads/{messageBoardThreadId}/unsubscribe"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public void putMessageBoardThreadUnsubscribe(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("messageBoardThreadId")
			Long messageBoardThreadId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/message-board-threads'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the site's message board threads. Results can be paginated, filtered, searched, flattened, and sorted."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "siteId"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "aggregationTerms"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "flatten"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "filter"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "page"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "pageSize"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "search"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "sort"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path("/sites/{siteId}/message-board-threads")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Page<MessageBoardThread> getSiteMessageBoardThreadsPage(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("siteId")
			Long siteId,
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("flatten")
			Boolean flatten,
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("search")
			String search,
			@javax.ws.rs.core.Context
				com.liferay.portal.vulcan.aggregation.Aggregation aggregation,
			@javax.ws.rs.core.Context Filter filter,
			@javax.ws.rs.core.Context Pagination pagination,
			@javax.ws.rs.core.Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/message-board-threads' -d $'{"articleBody": ___, "creatorStatistics": ___, "customFields": ___, "encodingFormat": ___, "friendlyUrlPath": ___, "hasValidAnswer": ___, "headline": ___, "keywords": ___, "messageBoardSectionId": ___, "seen": ___, "showAsQuestion": ___, "subscribed": ___, "taxonomyCategoryIds": ___, "threadType": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Creates a new message board thread."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "siteId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path("/sites/{siteId}/message-board-threads")
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public MessageBoardThread postSiteMessageBoardThread(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("siteId")
			Long siteId,
			MessageBoardThread messageBoardThread)
		throws Exception {

		return new MessageBoardThread();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/message-board-threads/batch'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "siteId"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "callbackURL"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.Path("/sites/{siteId}/message-board-threads/batch")
	@javax.ws.rs.POST
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response postSiteMessageBoardThreadBatch(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("siteId")
			Long siteId,
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("callbackURL")
			String callbackURL,
			Object object)
		throws Exception {

		vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(
			contextAcceptLanguage);
		vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
		vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(
			contextHttpServletRequest);
		vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
		vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

		Response.ResponseBuilder responseBuilder = Response.accepted();

		return responseBuilder.entity(
			vulcanBatchEngineImportTaskResource.postImportTask(
				MessageBoardThread.class.getName(), callbackURL, null, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/message-board-threads/by-friendly-url-path/{friendlyUrlPath}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "siteId"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "friendlyUrlPath"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(name = "MessageBoardThread")
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path(
		"/sites/{siteId}/message-board-threads/by-friendly-url-path/{friendlyUrlPath}"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public MessageBoardThread getSiteMessageBoardThreadByFriendlyUrlPath(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("siteId")
			Long siteId,
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("friendlyUrlPath")
			String friendlyUrlPath)
		throws Exception {

		return new MessageBoardThread();
	}

	@Override
	@SuppressWarnings("PMD.UnusedLocalVariable")
	public void create(
			Collection<MessageBoardThread> messageBoardThreads,
			Map<String, Serializable> parameters)
		throws Exception {

		UnsafeConsumer<MessageBoardThread, Exception>
			messageBoardThreadUnsafeConsumer = null;

		String createStrategy = (String)parameters.getOrDefault(
			"createStrategy", "INSERT");

		if ("INSERT".equalsIgnoreCase(createStrategy)) {
			if (parameters.containsKey("messageBoardSectionId")) {
				messageBoardThreadUnsafeConsumer =
					messageBoardThread ->
						postMessageBoardSectionMessageBoardThread(
							Long.parseLong(
								(String)parameters.get(
									"messageBoardSectionId")),
							messageBoardThread);
			}
			else if (parameters.containsKey("siteId")) {
				messageBoardThreadUnsafeConsumer =
					messageBoardThread -> postSiteMessageBoardThread(
						(Long)parameters.get("siteId"), messageBoardThread);
			}
			else {
				throw new NotSupportedException(
					"One of the following parameters must be specified: [messageBoardSectionId, siteId]");
			}
		}

		if (messageBoardThreadUnsafeConsumer == null) {
			throw new NotSupportedException(
				"Create strategy \"" + createStrategy +
					"\" is not supported for MessageBoardThread");
		}

		if (contextBatchUnsafeConsumer != null) {
			contextBatchUnsafeConsumer.accept(
				messageBoardThreads, messageBoardThreadUnsafeConsumer);
		}
		else {
			for (MessageBoardThread messageBoardThread : messageBoardThreads) {
				messageBoardThreadUnsafeConsumer.accept(messageBoardThread);
			}
		}
	}

	@Override
	public void delete(
			Collection<MessageBoardThread> messageBoardThreads,
			Map<String, Serializable> parameters)
		throws Exception {

		for (MessageBoardThread messageBoardThread : messageBoardThreads) {
			deleteMessageBoardThread(messageBoardThread.getId());
		}
	}

	public Set<String> getAvailableCreateStrategies() {
		return SetUtil.fromArray("INSERT");
	}

	public Set<String> getAvailableUpdateStrategies() {
		return SetUtil.fromArray("PARTIAL_UPDATE", "UPDATE");
	}

	@Override
	public EntityModel getEntityModel(Map<String, List<String>> multivaluedMap)
		throws Exception {

		return getEntityModel(
			new MultivaluedHashMap<String, Object>(multivaluedMap));
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap)
		throws Exception {

		return null;
	}

	public String getVersion() {
		return "v1.0";
	}

	@Override
	public Page<MessageBoardThread> read(
			Filter filter, Pagination pagination, Sort[] sorts,
			Map<String, Serializable> parameters, String search)
		throws Exception {

		if (parameters.containsKey("siteId")) {
			return getSiteMessageBoardThreadsPage(
				(Long)parameters.get("siteId"),
				Boolean.parseBoolean((String)parameters.get("flatten")), search,
				null, filter, pagination, sorts);
		}
		else if (parameters.containsKey("messageBoardSectionId")) {
			return getMessageBoardSectionMessageBoardThreadsPage(
				Long.parseLong((String)parameters.get("messageBoardSectionId")),
				search, null, filter, pagination, sorts);
		}
		else {
			throw new NotSupportedException(
				"One of the following parameters must be specified: [siteId, messageBoardSectionId]");
		}
	}

	@Override
	public void setLanguageId(String languageId) {
		this.contextAcceptLanguage = new AcceptLanguage() {

			@Override
			public List<Locale> getLocales() {
				return null;
			}

			@Override
			public String getPreferredLanguageId() {
				return languageId;
			}

			@Override
			public Locale getPreferredLocale() {
				return LocaleUtil.fromLanguageId(languageId);
			}

		};
	}

	@Override
	public void update(
			Collection<MessageBoardThread> messageBoardThreads,
			Map<String, Serializable> parameters)
		throws Exception {

		UnsafeConsumer<MessageBoardThread, Exception>
			messageBoardThreadUnsafeConsumer = null;

		String updateStrategy = (String)parameters.getOrDefault(
			"updateStrategy", "UPDATE");

		if ("PARTIAL_UPDATE".equalsIgnoreCase(updateStrategy)) {
			messageBoardThreadUnsafeConsumer =
				messageBoardThread -> patchMessageBoardThread(
					messageBoardThread.getId() != null ?
						messageBoardThread.getId() :
							Long.parseLong(
								(String)parameters.get("messageBoardThreadId")),
					messageBoardThread);
		}

		if ("UPDATE".equalsIgnoreCase(updateStrategy)) {
			messageBoardThreadUnsafeConsumer =
				messageBoardThread -> putMessageBoardThread(
					messageBoardThread.getId() != null ?
						messageBoardThread.getId() :
							Long.parseLong(
								(String)parameters.get("messageBoardThreadId")),
					messageBoardThread);
		}

		if (messageBoardThreadUnsafeConsumer == null) {
			throw new NotSupportedException(
				"Update strategy \"" + updateStrategy +
					"\" is not supported for MessageBoardThread");
		}

		if (contextBatchUnsafeConsumer != null) {
			contextBatchUnsafeConsumer.accept(
				messageBoardThreads, messageBoardThreadUnsafeConsumer);
		}
		else {
			for (MessageBoardThread messageBoardThread : messageBoardThreads) {
				messageBoardThreadUnsafeConsumer.accept(messageBoardThread);
			}
		}
	}

	public void setContextAcceptLanguage(AcceptLanguage contextAcceptLanguage) {
		this.contextAcceptLanguage = contextAcceptLanguage;
	}

	public void setContextBatchUnsafeConsumer(
		UnsafeBiConsumer
			<Collection<MessageBoardThread>,
			 UnsafeConsumer<MessageBoardThread, Exception>, Exception>
				contextBatchUnsafeConsumer) {

		this.contextBatchUnsafeConsumer = contextBatchUnsafeConsumer;
	}

	public void setContextCompany(
		com.liferay.portal.kernel.model.Company contextCompany) {

		this.contextCompany = contextCompany;
	}

	public void setContextHttpServletRequest(
		HttpServletRequest contextHttpServletRequest) {

		this.contextHttpServletRequest = contextHttpServletRequest;
	}

	public void setContextHttpServletResponse(
		HttpServletResponse contextHttpServletResponse) {

		this.contextHttpServletResponse = contextHttpServletResponse;
	}

	public void setContextUriInfo(UriInfo contextUriInfo) {
		this.contextUriInfo = contextUriInfo;
	}

	public void setContextUser(
		com.liferay.portal.kernel.model.User contextUser) {

		this.contextUser = contextUser;
	}

	public void setExpressionConvert(
		ExpressionConvert<Filter> expressionConvert) {

		this.expressionConvert = expressionConvert;
	}

	public void setFilterParserProvider(
		FilterParserProvider filterParserProvider) {

		this.filterParserProvider = filterParserProvider;
	}

	public void setGroupLocalService(GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

	public void setResourceActionLocalService(
		ResourceActionLocalService resourceActionLocalService) {

		this.resourceActionLocalService = resourceActionLocalService;
	}

	public void setResourcePermissionLocalService(
		ResourcePermissionLocalService resourcePermissionLocalService) {

		this.resourcePermissionLocalService = resourcePermissionLocalService;
	}

	public void setRoleLocalService(RoleLocalService roleLocalService) {
		this.roleLocalService = roleLocalService;
	}

	public void setSortParserProvider(SortParserProvider sortParserProvider) {
		this.sortParserProvider = sortParserProvider;
	}

	public void setVulcanBatchEngineImportTaskResource(
		VulcanBatchEngineImportTaskResource
			vulcanBatchEngineImportTaskResource) {

		this.vulcanBatchEngineImportTaskResource =
			vulcanBatchEngineImportTaskResource;
	}

	@Override
	public Filter toFilter(
		String filterString, Map<String, List<String>> multivaluedMap) {

		try {
			EntityModel entityModel = getEntityModel(multivaluedMap);

			FilterParser filterParser = filterParserProvider.provide(
				entityModel);

			com.liferay.portal.odata.filter.Filter oDataFilter =
				new com.liferay.portal.odata.filter.Filter(
					filterParser.parse(filterString));

			return expressionConvert.convert(
				oDataFilter.getExpression(),
				contextAcceptLanguage.getPreferredLocale(), entityModel);
		}
		catch (Exception exception) {
			_log.error("Invalid filter " + filterString, exception);

			return null;
		}
	}

	@Override
	public Sort[] toSorts(String sortString) {
		if (Validator.isNull(sortString)) {
			return null;
		}

		try {
			SortParser sortParser = sortParserProvider.provide(
				getEntityModel(Collections.emptyMap()));

			if (sortParser == null) {
				return null;
			}

			com.liferay.portal.odata.sort.Sort oDataSort =
				new com.liferay.portal.odata.sort.Sort(
					sortParser.parse(sortString));

			List<SortField> sortFields = oDataSort.getSortFields();

			Sort[] sorts = new Sort[sortFields.size()];

			for (int i = 0; i < sortFields.size(); i++) {
				SortField sortField = sortFields.get(i);

				sorts[i] = new Sort(
					sortField.getSortableFieldName(
						contextAcceptLanguage.getPreferredLocale()),
					!sortField.isAscending());
			}

			return sorts;
		}
		catch (Exception exception) {
			_log.error("Invalid sort " + sortString, exception);

			return new Sort[0];
		}
	}

	protected Map<String, String> addAction(
		String actionName, GroupedModel groupedModel, String methodName) {

		return ActionUtil.addAction(
			actionName, getClass(), groupedModel, methodName,
			contextScopeChecker, contextUriInfo);
	}

	protected Map<String, String> addAction(
		String actionName, Long id, String methodName, Long ownerId,
		String permissionName, Long siteId) {

		return ActionUtil.addAction(
			actionName, getClass(), id, methodName, contextScopeChecker,
			ownerId, permissionName, siteId, contextUriInfo);
	}

	protected Map<String, String> addAction(
		String actionName, Long id, String methodName,
		ModelResourcePermission modelResourcePermission) {

		return ActionUtil.addAction(
			actionName, getClass(), id, methodName, contextScopeChecker,
			modelResourcePermission, contextUriInfo);
	}

	protected Map<String, String> addAction(
		String actionName, String methodName, String permissionName,
		Long siteId) {

		return addAction(
			actionName, siteId, methodName, null, permissionName, siteId);
	}

	protected void preparePatch(
		MessageBoardThread messageBoardThread,
		MessageBoardThread existingMessageBoardThread) {
	}

	protected <T, R, E extends Throwable> List<R> transform(
		Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction) {

		return TransformUtil.transform(collection, unsafeFunction);
	}

	protected <T, R, E extends Throwable> R[] transform(
		T[] array, UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz) {

		return TransformUtil.transform(array, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> R[] transformToArray(
		Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction,
		Class<?> clazz) {

		return TransformUtil.transformToArray(
			collection, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> List<R> transformToList(
		T[] array, UnsafeFunction<T, R, E> unsafeFunction) {

		return TransformUtil.transformToList(array, unsafeFunction);
	}

	protected <T, R, E extends Throwable> List<R> unsafeTransform(
			Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction)
		throws E {

		return TransformUtil.unsafeTransform(collection, unsafeFunction);
	}

	protected <T, R, E extends Throwable> R[] unsafeTransform(
			T[] array, UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz)
		throws E {

		return TransformUtil.unsafeTransform(array, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> R[] unsafeTransformToArray(
			Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction,
			Class<?> clazz)
		throws E {

		return TransformUtil.unsafeTransformToArray(
			collection, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> List<R> unsafeTransformToList(
			T[] array, UnsafeFunction<T, R, E> unsafeFunction)
		throws E {

		return TransformUtil.unsafeTransformToList(array, unsafeFunction);
	}

	protected AcceptLanguage contextAcceptLanguage;
	protected UnsafeBiConsumer
		<Collection<MessageBoardThread>,
		 UnsafeConsumer<MessageBoardThread, Exception>, Exception>
			contextBatchUnsafeConsumer;
	protected com.liferay.portal.kernel.model.Company contextCompany;
	protected HttpServletRequest contextHttpServletRequest;
	protected HttpServletResponse contextHttpServletResponse;
	protected Object contextScopeChecker;
	protected UriInfo contextUriInfo;
	protected com.liferay.portal.kernel.model.User contextUser;
	protected ExpressionConvert<Filter> expressionConvert;
	protected FilterParserProvider filterParserProvider;
	protected GroupLocalService groupLocalService;
	protected ResourceActionLocalService resourceActionLocalService;
	protected ResourcePermissionLocalService resourcePermissionLocalService;
	protected RoleLocalService roleLocalService;
	protected SortParserProvider sortParserProvider;
	protected VulcanBatchEngineImportTaskResource
		vulcanBatchEngineImportTaskResource;

	private void _checkResources(
			long companyId, long resourceId, String resourceName)
		throws PortalException {

		int count = resourcePermissionLocalService.getResourcePermissionsCount(
			companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(resourceId));

		if (count == 0) {
			ResourceLocalServiceUtil.addResources(
				companyId, resourceId, 0, resourceName,
				String.valueOf(resourceId), false, true, true);
		}
	}

	private Collection<Permission> _getPermissions(
			long companyId, List<ResourceAction> resourceActions,
			long resourceId, String resourceName, String[] roleNames)
		throws Exception {

		_checkResources(companyId, resourceId, resourceName);

		Map<String, Permission> permissions = new LinkedHashMap<>();

		List<ResourcePermission> resourcePermissions = new ArrayList<>();

		try {
			Method method = ResourcePermissionLocalService.class.getMethod(
				"getResourcePermissions", String.class);

			resourcePermissions = (List<ResourcePermission>)method.invoke(
				resourcePermissionLocalService, resourceName);
		}
		catch (NoSuchMethodException noSuchMethodException) {
			resourcePermissions =
				resourcePermissionLocalService.getResourcePermissions(
					companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL,
					String.valueOf(resourceId));
		}

		for (ResourcePermission resourcePermission : resourcePermissions) {
			if ((resourcePermission.getPrimKeyId() == 0) ||
				(resourcePermission.getPrimKeyId() == resourceId)) {

				com.liferay.portal.kernel.model.Role role =
					roleLocalService.getRole(resourcePermission.getRoleId());

				if ((roleNames == null) ||
					((roleNames != null) &&
					 ArrayUtil.contains(roleNames, role.getName()))) {

					Permission permission = permissions.get(role.getName());

					if (permission == null) {
						permission = _toPermission(
							resourceActions, resourcePermission, role);

						if (permission != null) {
							permissions.put(role.getName(), permission);
						}
					}
					else {
						Set<String> actionsIdsSet = new HashSet<>();

						Collections.addAll(
							actionsIdsSet, permission.getActionIds());

						Permission newPermission = _toPermission(
							resourceActions, resourcePermission, role);

						Collections.addAll(
							actionsIdsSet, newPermission.getActionIds());

						permission.setActionIds(
							actionsIdsSet.toArray(new String[0]));
					}
				}
			}
		}

		return permissions.values();
	}

	private Permission _toPermission(
		List<ResourceAction> resourceActions,
		ResourcePermission resourcePermission,
		com.liferay.portal.kernel.model.Role role) {

		Set<String> actionsIdsSet = new HashSet<>();

		long actionIds = resourcePermission.getActionIds();

		for (ResourceAction resourceAction : resourceActions) {
			long bitwiseValue = resourceAction.getBitwiseValue();

			if ((actionIds & bitwiseValue) == bitwiseValue) {
				actionsIdsSet.add(resourceAction.getActionId());
			}
		}

		if (actionsIdsSet.isEmpty()) {
			return null;
		}

		return new Permission() {
			{
				actionIds = actionsIdsSet.toArray(new String[0]);
				roleName = role.getName();
			}
		};
	}

	private static final com.liferay.portal.kernel.log.Log _log =
		LogFactoryUtil.getLog(BaseMessageBoardThreadResourceImpl.class);

}