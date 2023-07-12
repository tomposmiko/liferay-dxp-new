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

import com.liferay.headless.delivery.dto.v1_0.KnowledgeBaseArticle;
import com.liferay.headless.delivery.dto.v1_0.Rating;
import com.liferay.headless.delivery.resource.v1_0.KnowledgeBaseArticleResource;
import com.liferay.petra.function.UnsafeBiConsumer;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
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
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.ActionUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.io.Serializable;

import java.util.Collections;
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
public abstract class BaseKnowledgeBaseArticleResourceImpl
	implements EntityModelResource, KnowledgeBaseArticleResource,
			   VulcanBatchEngineTaskItemDelegate<KnowledgeBaseArticle> {

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{knowledgeBaseArticleId}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Deletes the knowledge base article and returns a 204 if the operation succeeds."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("/knowledge-base-articles/{knowledgeBaseArticleId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public void deleteKnowledgeBaseArticle(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseArticleId")
			Long knowledgeBaseArticleId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/batch'  -u 'test@liferay.com:test'
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
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("/knowledge-base-articles/batch")
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response deleteKnowledgeBaseArticleBatch(
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
				KnowledgeBaseArticle.class.getName(), callbackURL, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{knowledgeBaseArticleId}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the knowledge base article."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path("/knowledge-base-articles/{knowledgeBaseArticleId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public KnowledgeBaseArticle getKnowledgeBaseArticle(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseArticleId")
			Long knowledgeBaseArticleId)
		throws Exception {

		return new KnowledgeBaseArticle();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PATCH' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{knowledgeBaseArticleId}' -d $'{"articleBody": ___, "customFields": ___, "description": ___, "friendlyUrlPath": ___, "keywords": ___, "parentKnowledgeBaseFolderId": ___, "taxonomyCategoryIds": ___, "title": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Updates only the fields received in the request body, leaving any other fields untouched."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.PATCH
	@javax.ws.rs.Path("/knowledge-base-articles/{knowledgeBaseArticleId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public KnowledgeBaseArticle patchKnowledgeBaseArticle(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseArticleId")
			Long knowledgeBaseArticleId,
			KnowledgeBaseArticle knowledgeBaseArticle)
		throws Exception {

		KnowledgeBaseArticle existingKnowledgeBaseArticle =
			getKnowledgeBaseArticle(knowledgeBaseArticleId);

		if (knowledgeBaseArticle.getActions() != null) {
			existingKnowledgeBaseArticle.setActions(
				knowledgeBaseArticle.getActions());
		}

		if (knowledgeBaseArticle.getArticleBody() != null) {
			existingKnowledgeBaseArticle.setArticleBody(
				knowledgeBaseArticle.getArticleBody());
		}

		if (knowledgeBaseArticle.getDateCreated() != null) {
			existingKnowledgeBaseArticle.setDateCreated(
				knowledgeBaseArticle.getDateCreated());
		}

		if (knowledgeBaseArticle.getDateModified() != null) {
			existingKnowledgeBaseArticle.setDateModified(
				knowledgeBaseArticle.getDateModified());
		}

		if (knowledgeBaseArticle.getDescription() != null) {
			existingKnowledgeBaseArticle.setDescription(
				knowledgeBaseArticle.getDescription());
		}

		if (knowledgeBaseArticle.getEncodingFormat() != null) {
			existingKnowledgeBaseArticle.setEncodingFormat(
				knowledgeBaseArticle.getEncodingFormat());
		}

		if (knowledgeBaseArticle.getFriendlyUrlPath() != null) {
			existingKnowledgeBaseArticle.setFriendlyUrlPath(
				knowledgeBaseArticle.getFriendlyUrlPath());
		}

		if (knowledgeBaseArticle.getKeywords() != null) {
			existingKnowledgeBaseArticle.setKeywords(
				knowledgeBaseArticle.getKeywords());
		}

		if (knowledgeBaseArticle.getNumberOfAttachments() != null) {
			existingKnowledgeBaseArticle.setNumberOfAttachments(
				knowledgeBaseArticle.getNumberOfAttachments());
		}

		if (knowledgeBaseArticle.getNumberOfKnowledgeBaseArticles() != null) {
			existingKnowledgeBaseArticle.setNumberOfKnowledgeBaseArticles(
				knowledgeBaseArticle.getNumberOfKnowledgeBaseArticles());
		}

		if (knowledgeBaseArticle.getParentKnowledgeBaseFolderId() != null) {
			existingKnowledgeBaseArticle.setParentKnowledgeBaseFolderId(
				knowledgeBaseArticle.getParentKnowledgeBaseFolderId());
		}

		if (knowledgeBaseArticle.getSiteId() != null) {
			existingKnowledgeBaseArticle.setSiteId(
				knowledgeBaseArticle.getSiteId());
		}

		if (knowledgeBaseArticle.getSubscribed() != null) {
			existingKnowledgeBaseArticle.setSubscribed(
				knowledgeBaseArticle.getSubscribed());
		}

		if (knowledgeBaseArticle.getTaxonomyCategoryIds() != null) {
			existingKnowledgeBaseArticle.setTaxonomyCategoryIds(
				knowledgeBaseArticle.getTaxonomyCategoryIds());
		}

		if (knowledgeBaseArticle.getTitle() != null) {
			existingKnowledgeBaseArticle.setTitle(
				knowledgeBaseArticle.getTitle());
		}

		if (knowledgeBaseArticle.getViewableBy() != null) {
			existingKnowledgeBaseArticle.setViewableBy(
				knowledgeBaseArticle.getViewableBy());
		}

		preparePatch(knowledgeBaseArticle, existingKnowledgeBaseArticle);

		return putKnowledgeBaseArticle(
			knowledgeBaseArticleId, existingKnowledgeBaseArticle);
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{knowledgeBaseArticleId}' -d $'{"articleBody": ___, "customFields": ___, "description": ___, "friendlyUrlPath": ___, "keywords": ___, "parentKnowledgeBaseFolderId": ___, "taxonomyCategoryIds": ___, "title": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Replaces the knowledge base article with the information sent in the request body. Any missing fields are deleted, unless they are required."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path("/knowledge-base-articles/{knowledgeBaseArticleId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public KnowledgeBaseArticle putKnowledgeBaseArticle(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseArticleId")
			Long knowledgeBaseArticleId,
			KnowledgeBaseArticle knowledgeBaseArticle)
		throws Exception {

		return new KnowledgeBaseArticle();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/batch'  -u 'test@liferay.com:test'
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
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.Path("/knowledge-base-articles/batch")
	@javax.ws.rs.Produces("application/json")
	@javax.ws.rs.PUT
	@Override
	public Response putKnowledgeBaseArticleBatch(
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
				KnowledgeBaseArticle.class.getName(), callbackURL, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{knowledgeBaseArticleId}/my-rating'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Deletes the knowledge base article's rating and returns a 204 if the operation succeeds."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path(
		"/knowledge-base-articles/{knowledgeBaseArticleId}/my-rating"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public void deleteKnowledgeBaseArticleMyRating(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseArticleId")
			Long knowledgeBaseArticleId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{knowledgeBaseArticleId}/my-rating'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the knowledge base article's rating."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path(
		"/knowledge-base-articles/{knowledgeBaseArticleId}/my-rating"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Rating getKnowledgeBaseArticleMyRating(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseArticleId")
			Long knowledgeBaseArticleId)
		throws Exception {

		return new Rating();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{knowledgeBaseArticleId}/my-rating' -d $'{"ratingValue": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Creates a rating for the knowledge base article."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path(
		"/knowledge-base-articles/{knowledgeBaseArticleId}/my-rating"
	)
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Rating postKnowledgeBaseArticleMyRating(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseArticleId")
			Long knowledgeBaseArticleId,
			Rating rating)
		throws Exception {

		return new Rating();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{knowledgeBaseArticleId}/my-rating' -d $'{"ratingValue": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Replaces the rating with the information sent in the request body. Any missing fields are deleted, unless they are required."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path(
		"/knowledge-base-articles/{knowledgeBaseArticleId}/my-rating"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public Rating putKnowledgeBaseArticleMyRating(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseArticleId")
			Long knowledgeBaseArticleId,
			Rating rating)
		throws Exception {

		return new Rating();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{knowledgeBaseArticleId}/subscribe'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Path(
		"/knowledge-base-articles/{knowledgeBaseArticleId}/subscribe"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public void putKnowledgeBaseArticleSubscribe(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseArticleId")
			Long knowledgeBaseArticleId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{knowledgeBaseArticleId}/unsubscribe'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Path(
		"/knowledge-base-articles/{knowledgeBaseArticleId}/unsubscribe"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public void putKnowledgeBaseArticleUnsubscribe(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseArticleId")
			Long knowledgeBaseArticleId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{parentKnowledgeBaseArticleId}/knowledge-base-articles'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the parent knowledge base article's child knowledge base articles. Results can be paginated, filtered, searched, and sorted."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "parentKnowledgeBaseArticleId"
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
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path(
		"/knowledge-base-articles/{parentKnowledgeBaseArticleId}/knowledge-base-articles"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Page<KnowledgeBaseArticle>
			getKnowledgeBaseArticleKnowledgeBaseArticlesPage(
				@io.swagger.v3.oas.annotations.Parameter(hidden = true)
				@javax.validation.constraints.NotNull
				@javax.ws.rs.PathParam("parentKnowledgeBaseArticleId")
				Long parentKnowledgeBaseArticleId,
				@io.swagger.v3.oas.annotations.Parameter(hidden = true)
				@javax.ws.rs.QueryParam("flatten")
				Boolean flatten,
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
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-articles/{parentKnowledgeBaseArticleId}/knowledge-base-articles' -d $'{"articleBody": ___, "customFields": ___, "description": ___, "friendlyUrlPath": ___, "keywords": ___, "parentKnowledgeBaseFolderId": ___, "taxonomyCategoryIds": ___, "title": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Creates a child knowledge base article of the knowledge base article identified by `parentKnowledgeBaseArticleId`."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "parentKnowledgeBaseArticleId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path(
		"/knowledge-base-articles/{parentKnowledgeBaseArticleId}/knowledge-base-articles"
	)
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public KnowledgeBaseArticle postKnowledgeBaseArticleKnowledgeBaseArticle(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("parentKnowledgeBaseArticleId")
			Long parentKnowledgeBaseArticleId,
			KnowledgeBaseArticle knowledgeBaseArticle)
		throws Exception {

		return new KnowledgeBaseArticle();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-folders/{knowledgeBaseFolderId}/knowledge-base-articles'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the folder's knowledge base articles. Results can be paginated, filtered, searched, flattened, and sorted."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseFolderId"
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
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path(
		"/knowledge-base-folders/{knowledgeBaseFolderId}/knowledge-base-articles"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Page<KnowledgeBaseArticle>
			getKnowledgeBaseFolderKnowledgeBaseArticlesPage(
				@io.swagger.v3.oas.annotations.Parameter(hidden = true)
				@javax.validation.constraints.NotNull
				@javax.ws.rs.PathParam("knowledgeBaseFolderId")
				Long knowledgeBaseFolderId,
				@io.swagger.v3.oas.annotations.Parameter(hidden = true)
				@javax.ws.rs.QueryParam("flatten")
				Boolean flatten,
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
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-folders/{knowledgeBaseFolderId}/knowledge-base-articles' -d $'{"articleBody": ___, "customFields": ___, "description": ___, "friendlyUrlPath": ___, "keywords": ___, "parentKnowledgeBaseFolderId": ___, "taxonomyCategoryIds": ___, "title": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Creates a new knowledge base article in the folder."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseFolderId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path(
		"/knowledge-base-folders/{knowledgeBaseFolderId}/knowledge-base-articles"
	)
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public KnowledgeBaseArticle postKnowledgeBaseFolderKnowledgeBaseArticle(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseFolderId")
			Long knowledgeBaseFolderId,
			KnowledgeBaseArticle knowledgeBaseArticle)
		throws Exception {

		return new KnowledgeBaseArticle();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/knowledge-base-folders/{knowledgeBaseFolderId}/knowledge-base-articles/batch'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "knowledgeBaseFolderId"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "callbackURL"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.Path(
		"/knowledge-base-folders/{knowledgeBaseFolderId}/knowledge-base-articles/batch"
	)
	@javax.ws.rs.POST
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response postKnowledgeBaseFolderKnowledgeBaseArticleBatch(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("knowledgeBaseFolderId")
			Long knowledgeBaseFolderId,
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
				KnowledgeBaseArticle.class.getName(), callbackURL, null, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/knowledge-base-articles'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the site's knowledge base articles. Results can be paginated, filtered, searched, flattened, and sorted."
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
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path("/sites/{siteId}/knowledge-base-articles")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Page<KnowledgeBaseArticle> getSiteKnowledgeBaseArticlesPage(
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
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/knowledge-base-articles' -d $'{"articleBody": ___, "customFields": ___, "description": ___, "friendlyUrlPath": ___, "keywords": ___, "parentKnowledgeBaseFolderId": ___, "taxonomyCategoryIds": ___, "title": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Creates a new knowledge base article."
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
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path("/sites/{siteId}/knowledge-base-articles")
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public KnowledgeBaseArticle postSiteKnowledgeBaseArticle(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("siteId")
			Long siteId,
			KnowledgeBaseArticle knowledgeBaseArticle)
		throws Exception {

		return new KnowledgeBaseArticle();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/knowledge-base-articles/batch'  -u 'test@liferay.com:test'
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
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.Path("/sites/{siteId}/knowledge-base-articles/batch")
	@javax.ws.rs.POST
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response postSiteKnowledgeBaseArticleBatch(
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
				KnowledgeBaseArticle.class.getName(), callbackURL, null, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/knowledge-base-articles/subscribe'  -u 'test@liferay.com:test'
	 */
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
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Path("/sites/{siteId}/knowledge-base-articles/subscribe")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public void putSiteKnowledgeBaseArticleSubscribe(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("siteId")
			Long siteId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/knowledge-base-articles/unsubscribe'  -u 'test@liferay.com:test'
	 */
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
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "KnowledgeBaseArticle"
			)
		}
	)
	@javax.ws.rs.Path("/sites/{siteId}/knowledge-base-articles/unsubscribe")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public void putSiteKnowledgeBaseArticleUnsubscribe(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("siteId")
			Long siteId)
		throws Exception {
	}

	@Override
	@SuppressWarnings("PMD.UnusedLocalVariable")
	public void create(
			java.util.Collection<KnowledgeBaseArticle> knowledgeBaseArticles,
			Map<String, Serializable> parameters)
		throws Exception {

		UnsafeConsumer<KnowledgeBaseArticle, Exception>
			knowledgeBaseArticleUnsafeConsumer = null;

		String createStrategy = (String)parameters.getOrDefault(
			"createStrategy", "INSERT");

		if ("INSERT".equalsIgnoreCase(createStrategy)) {
			if (parameters.containsKey("knowledgeBaseFolderId")) {
				knowledgeBaseArticleUnsafeConsumer = knowledgeBaseArticle ->
					postKnowledgeBaseFolderKnowledgeBaseArticle(
						Long.parseLong(
							(String)parameters.get("knowledgeBaseFolderId")),
						knowledgeBaseArticle);
			}
			else if (parameters.containsKey("siteId")) {
				knowledgeBaseArticleUnsafeConsumer =
					knowledgeBaseArticle -> postSiteKnowledgeBaseArticle(
						(Long)parameters.get("siteId"), knowledgeBaseArticle);
			}
			else {
				throw new NotSupportedException(
					"One of the following parameters must be specified: [knowledgeBaseFolderId, siteId]");
			}
		}

		if (knowledgeBaseArticleUnsafeConsumer == null) {
			throw new NotSupportedException(
				"Create strategy \"" + createStrategy +
					"\" is not supported for KnowledgeBaseArticle");
		}

		if (contextBatchUnsafeConsumer != null) {
			contextBatchUnsafeConsumer.accept(
				knowledgeBaseArticles, knowledgeBaseArticleUnsafeConsumer);
		}
		else {
			for (KnowledgeBaseArticle knowledgeBaseArticle :
					knowledgeBaseArticles) {

				knowledgeBaseArticleUnsafeConsumer.accept(knowledgeBaseArticle);
			}
		}
	}

	@Override
	public void delete(
			java.util.Collection<KnowledgeBaseArticle> knowledgeBaseArticles,
			Map<String, Serializable> parameters)
		throws Exception {

		for (KnowledgeBaseArticle knowledgeBaseArticle :
				knowledgeBaseArticles) {

			deleteKnowledgeBaseArticle(knowledgeBaseArticle.getId());
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
	public Page<KnowledgeBaseArticle> read(
			Filter filter, Pagination pagination, Sort[] sorts,
			Map<String, Serializable> parameters, String search)
		throws Exception {

		if (parameters.containsKey("siteId")) {
			return getSiteKnowledgeBaseArticlesPage(
				(Long)parameters.get("siteId"),
				Boolean.parseBoolean((String)parameters.get("flatten")), search,
				null, filter, pagination, sorts);
		}
		else if (parameters.containsKey("knowledgeBaseFolderId")) {
			return getKnowledgeBaseFolderKnowledgeBaseArticlesPage(
				Long.parseLong((String)parameters.get("knowledgeBaseFolderId")),
				Boolean.parseBoolean((String)parameters.get("flatten")), search,
				null, filter, pagination, sorts);
		}
		else {
			throw new NotSupportedException(
				"One of the following parameters must be specified: [siteId, knowledgeBaseFolderId]");
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
			java.util.Collection<KnowledgeBaseArticle> knowledgeBaseArticles,
			Map<String, Serializable> parameters)
		throws Exception {

		UnsafeConsumer<KnowledgeBaseArticle, Exception>
			knowledgeBaseArticleUnsafeConsumer = null;

		String updateStrategy = (String)parameters.getOrDefault(
			"updateStrategy", "UPDATE");

		if ("PARTIAL_UPDATE".equalsIgnoreCase(updateStrategy)) {
			knowledgeBaseArticleUnsafeConsumer =
				knowledgeBaseArticle -> patchKnowledgeBaseArticle(
					knowledgeBaseArticle.getId() != null ?
						knowledgeBaseArticle.getId() :
							Long.parseLong(
								(String)parameters.get(
									"knowledgeBaseArticleId")),
					knowledgeBaseArticle);
		}

		if ("UPDATE".equalsIgnoreCase(updateStrategy)) {
			knowledgeBaseArticleUnsafeConsumer =
				knowledgeBaseArticle -> putKnowledgeBaseArticle(
					knowledgeBaseArticle.getId() != null ?
						knowledgeBaseArticle.getId() :
							Long.parseLong(
								(String)parameters.get(
									"knowledgeBaseArticleId")),
					knowledgeBaseArticle);
		}

		if (knowledgeBaseArticleUnsafeConsumer == null) {
			throw new NotSupportedException(
				"Update strategy \"" + updateStrategy +
					"\" is not supported for KnowledgeBaseArticle");
		}

		if (contextBatchUnsafeConsumer != null) {
			contextBatchUnsafeConsumer.accept(
				knowledgeBaseArticles, knowledgeBaseArticleUnsafeConsumer);
		}
		else {
			for (KnowledgeBaseArticle knowledgeBaseArticle :
					knowledgeBaseArticles) {

				knowledgeBaseArticleUnsafeConsumer.accept(knowledgeBaseArticle);
			}
		}
	}

	public void setContextAcceptLanguage(AcceptLanguage contextAcceptLanguage) {
		this.contextAcceptLanguage = contextAcceptLanguage;
	}

	public void setContextBatchUnsafeConsumer(
		UnsafeBiConsumer
			<java.util.Collection<KnowledgeBaseArticle>,
			 UnsafeConsumer<KnowledgeBaseArticle, Exception>, Exception>
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
		KnowledgeBaseArticle knowledgeBaseArticle,
		KnowledgeBaseArticle existingKnowledgeBaseArticle) {
	}

	protected <T, R, E extends Throwable> List<R> transform(
		java.util.Collection<T> collection,
		UnsafeFunction<T, R, E> unsafeFunction) {

		return TransformUtil.transform(collection, unsafeFunction);
	}

	protected <T, R, E extends Throwable> R[] transform(
		T[] array, UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz) {

		return TransformUtil.transform(array, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> R[] transformToArray(
		java.util.Collection<T> collection,
		UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz) {

		return TransformUtil.transformToArray(
			collection, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> List<R> transformToList(
		T[] array, UnsafeFunction<T, R, E> unsafeFunction) {

		return TransformUtil.transformToList(array, unsafeFunction);
	}

	protected <T, R, E extends Throwable> List<R> unsafeTransform(
			java.util.Collection<T> collection,
			UnsafeFunction<T, R, E> unsafeFunction)
		throws E {

		return TransformUtil.unsafeTransform(collection, unsafeFunction);
	}

	protected <T, R, E extends Throwable> R[] unsafeTransform(
			T[] array, UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz)
		throws E {

		return TransformUtil.unsafeTransform(array, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> R[] unsafeTransformToArray(
			java.util.Collection<T> collection,
			UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz)
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
		<java.util.Collection<KnowledgeBaseArticle>,
		 UnsafeConsumer<KnowledgeBaseArticle, Exception>, Exception>
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

	private static final com.liferay.portal.kernel.log.Log _log =
		LogFactoryUtil.getLog(BaseKnowledgeBaseArticleResourceImpl.class);

}