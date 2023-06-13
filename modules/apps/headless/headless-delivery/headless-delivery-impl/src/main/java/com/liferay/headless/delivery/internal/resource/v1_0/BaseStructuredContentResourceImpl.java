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

import com.liferay.headless.delivery.dto.v1_0.Rating;
import com.liferay.headless.delivery.dto.v1_0.StructuredContent;
import com.liferay.headless.delivery.resource.v1_0.StructuredContentResource;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.TransformUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.constraints.NotNull;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
@Path("/v1.0")
public abstract class BaseStructuredContentResourceImpl
	implements StructuredContentResource {

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/content-structures/{contentStructureId}/structured-contents'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(
		description = "Retrieves a list of the content structure's structured content. Results can be paginated, filtered, searched, and sorted."
	)
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "contentStructureId"),
			@Parameter(in = ParameterIn.QUERY, name = "search"),
			@Parameter(in = ParameterIn.QUERY, name = "filter"),
			@Parameter(in = ParameterIn.QUERY, name = "page"),
			@Parameter(in = ParameterIn.QUERY, name = "pageSize"),
			@Parameter(in = ParameterIn.QUERY, name = "sort")
		}
	)
	@Path("/content-structures/{contentStructureId}/structured-contents")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public Page<StructuredContent> getContentStructureStructuredContentsPage(
			@NotNull @Parameter(hidden = true) @PathParam("contentStructureId")
				Long contentStructureId,
			@Parameter(hidden = true) @QueryParam("search") String search,
			@Context Filter filter, @Context Pagination pagination,
			@Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/structured-contents'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(
		description = "Retrieves the Site's structured content. Results can be paginated, filtered, searched, flattened, and sorted."
	)
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "siteId"),
			@Parameter(in = ParameterIn.QUERY, name = "flatten"),
			@Parameter(in = ParameterIn.QUERY, name = "search"),
			@Parameter(in = ParameterIn.QUERY, name = "filter"),
			@Parameter(in = ParameterIn.QUERY, name = "page"),
			@Parameter(in = ParameterIn.QUERY, name = "pageSize"),
			@Parameter(in = ParameterIn.QUERY, name = "sort")
		}
	)
	@Path("/sites/{siteId}/structured-contents")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public Page<StructuredContent> getSiteStructuredContentsPage(
			@NotNull @Parameter(hidden = true) @PathParam("siteId") Long siteId,
			@Parameter(hidden = true) @QueryParam("flatten") Boolean flatten,
			@Parameter(hidden = true) @QueryParam("search") String search,
			@Context Filter filter, @Context Pagination pagination,
			@Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/structured-contents' -d $'{"contentFields": ___, "contentStructureId": ___, "customFields": ___, "datePublished": ___, "description": ___, "friendlyUrlPath": ___, "keywords": ___, "taxonomyCategoryIds": ___, "title": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@Operation(description = "Creates a new structured content.")
	@POST
	@Parameters(value = {@Parameter(in = ParameterIn.PATH, name = "siteId")})
	@Path("/sites/{siteId}/structured-contents")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public StructuredContent postSiteStructuredContent(
			@NotNull @Parameter(hidden = true) @PathParam("siteId") Long siteId,
			StructuredContent structuredContent)
		throws Exception {

		return new StructuredContent();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/structured-contents/by-key/{key}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(
		description = "Retrieves a structured content by its key (`articleKey`)."
	)
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "siteId"),
			@Parameter(in = ParameterIn.PATH, name = "key")
		}
	)
	@Path("/sites/{siteId}/structured-contents/by-key/{key}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public StructuredContent getSiteStructuredContentByKey(
			@NotNull @Parameter(hidden = true) @PathParam("siteId") Long siteId,
			@NotNull @Parameter(hidden = true) @PathParam("key") String key)
		throws Exception {

		return new StructuredContent();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/structured-contents/by-uuid/{uuid}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(description = "Retrieves a structured content by its UUID.")
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "siteId"),
			@Parameter(in = ParameterIn.PATH, name = "uuid")
		}
	)
	@Path("/sites/{siteId}/structured-contents/by-uuid/{uuid}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public StructuredContent getSiteStructuredContentByUuid(
			@NotNull @Parameter(hidden = true) @PathParam("siteId") Long siteId,
			@NotNull @Parameter(hidden = true) @PathParam("uuid") String uuid)
		throws Exception {

		return new StructuredContent();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}/structured-contents'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(
		description = "Retrieves the folder's structured content. Results can be paginated, filtered, searched, and sorted."
	)
	@Parameters(
		value = {
			@Parameter(
				in = ParameterIn.PATH, name = "structuredContentFolderId"
			),
			@Parameter(in = ParameterIn.QUERY, name = "flatten"),
			@Parameter(in = ParameterIn.QUERY, name = "search"),
			@Parameter(in = ParameterIn.QUERY, name = "filter"),
			@Parameter(in = ParameterIn.QUERY, name = "page"),
			@Parameter(in = ParameterIn.QUERY, name = "pageSize"),
			@Parameter(in = ParameterIn.QUERY, name = "sort")
		}
	)
	@Path(
		"/structured-content-folders/{structuredContentFolderId}/structured-contents"
	)
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public Page<StructuredContent>
			getStructuredContentFolderStructuredContentsPage(
				@NotNull @Parameter(hidden = true)
				@PathParam("structuredContentFolderId") Long
					structuredContentFolderId,
				@Parameter(hidden = true) @QueryParam("flatten") Boolean
					flatten,
				@Parameter(hidden = true) @QueryParam("search") String search,
				@Context Filter filter, @Context Pagination pagination,
				@Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}/structured-contents' -d $'{"contentFields": ___, "contentStructureId": ___, "customFields": ___, "datePublished": ___, "description": ___, "friendlyUrlPath": ___, "keywords": ___, "taxonomyCategoryIds": ___, "title": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@Operation(description = "Creates a new structured content in the folder.")
	@POST
	@Parameters(
		value = {
			@Parameter(
				in = ParameterIn.PATH, name = "structuredContentFolderId"
			)
		}
	)
	@Path(
		"/structured-content-folders/{structuredContentFolderId}/structured-contents"
	)
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public StructuredContent postStructuredContentFolderStructuredContent(
			@NotNull @Parameter(hidden = true)
			@PathParam("structuredContentFolderId") Long
				structuredContentFolderId,
			StructuredContent structuredContent)
		throws Exception {

		return new StructuredContent();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content/{structuredContentId}/subscribe'  -u 'test@liferay.com:test'
	 */
	@Override
	@PUT
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId")
		}
	)
	@Path("/structured-content/{structuredContentId}/subscribe")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "StructuredContent")})
	public void putStructuredContentSubscribe(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content/{structuredContentId}/unsubscribe'  -u 'test@liferay.com:test'
	 */
	@Override
	@PUT
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId")
		}
	)
	@Path("/structured-content/{structuredContentId}/unsubscribe")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "StructuredContent")})
	public void putStructuredContentUnsubscribe(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/structured-contents/{structuredContentId}'  -u 'test@liferay.com:test'
	 */
	@Override
	@DELETE
	@Operation(
		description = "Deletes the structured content and returns a 204 if the operation succeeds."
	)
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId")
		}
	)
	@Path("/structured-contents/{structuredContentId}")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "StructuredContent")})
	public void deleteStructuredContent(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/structured-contents/{structuredContentId}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(description = "Retrieves the structured content via its ID.")
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId")
		}
	)
	@Path("/structured-contents/{structuredContentId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public StructuredContent getStructuredContent(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId)
		throws Exception {

		return new StructuredContent();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PATCH' 'http://localhost:8080/o/headless-delivery/v1.0/structured-contents/{structuredContentId}' -d $'{"contentFields": ___, "contentStructureId": ___, "customFields": ___, "datePublished": ___, "description": ___, "friendlyUrlPath": ___, "keywords": ___, "taxonomyCategoryIds": ___, "title": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@Operation(
		description = "Updates only the fields received in the request body, leaving any other fields untouched."
	)
	@PATCH
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId")
		}
	)
	@Path("/structured-contents/{structuredContentId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public StructuredContent patchStructuredContent(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId,
			StructuredContent structuredContent)
		throws Exception {

		StructuredContent existingStructuredContent = getStructuredContent(
			structuredContentId);

		if (structuredContent.getAvailableLanguages() != null) {
			existingStructuredContent.setAvailableLanguages(
				structuredContent.getAvailableLanguages());
		}

		if (structuredContent.getContentStructureId() != null) {
			existingStructuredContent.setContentStructureId(
				structuredContent.getContentStructureId());
		}

		if (structuredContent.getDateCreated() != null) {
			existingStructuredContent.setDateCreated(
				structuredContent.getDateCreated());
		}

		if (structuredContent.getDateModified() != null) {
			existingStructuredContent.setDateModified(
				structuredContent.getDateModified());
		}

		if (structuredContent.getDatePublished() != null) {
			existingStructuredContent.setDatePublished(
				structuredContent.getDatePublished());
		}

		if (structuredContent.getDescription() != null) {
			existingStructuredContent.setDescription(
				structuredContent.getDescription());
		}

		if (structuredContent.getFriendlyUrlPath() != null) {
			existingStructuredContent.setFriendlyUrlPath(
				structuredContent.getFriendlyUrlPath());
		}

		if (structuredContent.getKey() != null) {
			existingStructuredContent.setKey(structuredContent.getKey());
		}

		if (structuredContent.getKeywords() != null) {
			existingStructuredContent.setKeywords(
				structuredContent.getKeywords());
		}

		if (structuredContent.getNumberOfComments() != null) {
			existingStructuredContent.setNumberOfComments(
				structuredContent.getNumberOfComments());
		}

		if (structuredContent.getSiteId() != null) {
			existingStructuredContent.setSiteId(structuredContent.getSiteId());
		}

		if (structuredContent.getSubscribed() != null) {
			existingStructuredContent.setSubscribed(
				structuredContent.getSubscribed());
		}

		if (structuredContent.getTaxonomyCategoryIds() != null) {
			existingStructuredContent.setTaxonomyCategoryIds(
				structuredContent.getTaxonomyCategoryIds());
		}

		if (structuredContent.getTitle() != null) {
			existingStructuredContent.setTitle(structuredContent.getTitle());
		}

		if (structuredContent.getUuid() != null) {
			existingStructuredContent.setUuid(structuredContent.getUuid());
		}

		if (structuredContent.getViewableBy() != null) {
			existingStructuredContent.setViewableBy(
				structuredContent.getViewableBy());
		}

		preparePatch(structuredContent, existingStructuredContent);

		return putStructuredContent(
			structuredContentId, existingStructuredContent);
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-contents/{structuredContentId}' -d $'{"contentFields": ___, "contentStructureId": ___, "customFields": ___, "datePublished": ___, "description": ___, "friendlyUrlPath": ___, "keywords": ___, "taxonomyCategoryIds": ___, "title": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@Operation(
		description = "Replaces the structured content with the information sent in the request body. Any missing fields are deleted, unless they are required."
	)
	@PUT
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId")
		}
	)
	@Path("/structured-contents/{structuredContentId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public StructuredContent putStructuredContent(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId,
			StructuredContent structuredContent)
		throws Exception {

		return new StructuredContent();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/structured-contents/{structuredContentId}/my-rating'  -u 'test@liferay.com:test'
	 */
	@Override
	@DELETE
	@Operation(
		description = "Deletes the structured content's rating and returns a 204 if the operation succeeds."
	)
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId")
		}
	)
	@Path("/structured-contents/{structuredContentId}/my-rating")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "StructuredContent")})
	public void deleteStructuredContentMyRating(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/structured-contents/{structuredContentId}/my-rating'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(description = "Retrieves the structured content's rating.")
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId")
		}
	)
	@Path("/structured-contents/{structuredContentId}/my-rating")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public Rating getStructuredContentMyRating(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId)
		throws Exception {

		return new Rating();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/structured-contents/{structuredContentId}/my-rating' -d $'{"ratingValue": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@Operation(description = "Create a rating for the structured content.")
	@POST
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId")
		}
	)
	@Path("/structured-contents/{structuredContentId}/my-rating")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public Rating postStructuredContentMyRating(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId,
			Rating rating)
		throws Exception {

		return new Rating();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-contents/{structuredContentId}/my-rating' -d $'{"ratingValue": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@Operation(
		description = "Replaces the rating with the information sent in the request body. Any missing fields are deleted, unless they are required."
	)
	@PUT
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId")
		}
	)
	@Path("/structured-contents/{structuredContentId}/my-rating")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContent")})
	public Rating putStructuredContentMyRating(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId,
			Rating rating)
		throws Exception {

		return new Rating();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/structured-contents/{structuredContentId}/rendered-content/{templateId}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(
		description = "Retrieves the structured content's rendered template (the result of applying the structure's values to a template)."
	)
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "structuredContentId"),
			@Parameter(in = ParameterIn.PATH, name = "templateId")
		}
	)
	@Path(
		"/structured-contents/{structuredContentId}/rendered-content/{templateId}"
	)
	@Produces("text/html")
	@Tags(value = {@Tag(name = "StructuredContent")})
	public String getStructuredContentRenderedContentTemplate(
			@NotNull @Parameter(hidden = true) @PathParam("structuredContentId")
				Long structuredContentId,
			@NotNull @Parameter(hidden = true) @PathParam("templateId") Long
				templateId)
		throws Exception {

		return StringPool.BLANK;
	}

	public void setContextAcceptLanguage(AcceptLanguage contextAcceptLanguage) {
		this.contextAcceptLanguage = contextAcceptLanguage;
	}

	public void setContextCompany(Company contextCompany) {
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

	public void setContextUser(User contextUser) {
		this.contextUser = contextUser;
	}

	protected void preparePatch(
		StructuredContent structuredContent,
		StructuredContent existingStructuredContent) {
	}

	protected <T, R> List<R> transform(
		java.util.Collection<T> collection,
		UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transform(collection, unsafeFunction);
	}

	protected <T, R> R[] transform(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction,
		Class<?> clazz) {

		return TransformUtil.transform(array, unsafeFunction, clazz);
	}

	protected <T, R> R[] transformToArray(
		java.util.Collection<T> collection,
		UnsafeFunction<T, R, Exception> unsafeFunction, Class<?> clazz) {

		return TransformUtil.transformToArray(
			collection, unsafeFunction, clazz);
	}

	protected <T, R> List<R> transformToList(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transformToList(array, unsafeFunction);
	}

	protected AcceptLanguage contextAcceptLanguage;
	protected Company contextCompany;
	protected HttpServletRequest contextHttpServletRequest;
	protected HttpServletResponse contextHttpServletResponse;
	protected UriInfo contextUriInfo;
	protected User contextUser;

}