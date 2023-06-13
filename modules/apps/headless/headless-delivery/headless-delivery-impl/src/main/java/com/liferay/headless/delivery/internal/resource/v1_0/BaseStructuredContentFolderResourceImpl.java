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

import com.liferay.headless.delivery.dto.v1_0.StructuredContentFolder;
import com.liferay.headless.delivery.resource.v1_0.StructuredContentFolderResource;
import com.liferay.petra.function.UnsafeFunction;
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
public abstract class BaseStructuredContentFolderResourceImpl
	implements StructuredContentFolderResource {

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/structured-content-folders'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(
		description = "Retrieves the Site's structured content folders. Results can be paginated, filtered, searched, flattened, and sorted."
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
	@Path("/sites/{siteId}/structured-content-folders")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContentFolder")})
	public Page<StructuredContentFolder> getSiteStructuredContentFoldersPage(
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
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/structured-content-folders' -d $'{"customFields": ___, "description": ___, "name": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@Operation(description = "Creates a new structured content folder.")
	@POST
	@Parameters(value = {@Parameter(in = ParameterIn.PATH, name = "siteId")})
	@Path("/sites/{siteId}/structured-content-folders")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContentFolder")})
	public StructuredContentFolder postSiteStructuredContentFolder(
			@NotNull @Parameter(hidden = true) @PathParam("siteId") Long siteId,
			StructuredContentFolder structuredContentFolder)
		throws Exception {

		return new StructuredContentFolder();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{parentStructuredContentFolderId}/structured-content-folders'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(
		description = "Retrieves the parent structured content folder's subfolders. Results can be paginated, filtered, searched, and sorted."
	)
	@Parameters(
		value = {
			@Parameter(
				in = ParameterIn.PATH, name = "parentStructuredContentFolderId"
			),
			@Parameter(in = ParameterIn.QUERY, name = "search"),
			@Parameter(in = ParameterIn.QUERY, name = "filter"),
			@Parameter(in = ParameterIn.QUERY, name = "page"),
			@Parameter(in = ParameterIn.QUERY, name = "pageSize"),
			@Parameter(in = ParameterIn.QUERY, name = "sort")
		}
	)
	@Path(
		"/structured-content-folders/{parentStructuredContentFolderId}/structured-content-folders"
	)
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContentFolder")})
	public Page<StructuredContentFolder>
			getStructuredContentFolderStructuredContentFoldersPage(
				@NotNull @Parameter(hidden = true)
				@PathParam("parentStructuredContentFolderId") Long
					parentStructuredContentFolderId,
				@Parameter(hidden = true) @QueryParam("search") String search,
				@Context Filter filter, @Context Pagination pagination,
				@Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{parentStructuredContentFolderId}/structured-content-folders' -d $'{"customFields": ___, "description": ___, "name": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@Operation(
		description = "Creates a new structured content folder in an existing folder."
	)
	@POST
	@Parameters(
		value = {
			@Parameter(
				in = ParameterIn.PATH, name = "parentStructuredContentFolderId"
			)
		}
	)
	@Path(
		"/structured-content-folders/{parentStructuredContentFolderId}/structured-content-folders"
	)
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContentFolder")})
	public StructuredContentFolder
			postStructuredContentFolderStructuredContentFolder(
				@NotNull @Parameter(hidden = true)
				@PathParam("parentStructuredContentFolderId") Long
					parentStructuredContentFolderId,
				StructuredContentFolder structuredContentFolder)
		throws Exception {

		return new StructuredContentFolder();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}'  -u 'test@liferay.com:test'
	 */
	@Override
	@DELETE
	@Operation(
		description = "Deletes the structured content folder and returns a 204 if the operation succeeds."
	)
	@Parameters(
		value = {
			@Parameter(
				in = ParameterIn.PATH, name = "structuredContentFolderId"
			)
		}
	)
	@Path("/structured-content-folders/{structuredContentFolderId}")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "StructuredContentFolder")})
	public void deleteStructuredContentFolder(
			@NotNull @Parameter(hidden = true)
			@PathParam("structuredContentFolderId") Long
				structuredContentFolderId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Operation(description = "Retrieves the structured content folder.")
	@Parameters(
		value = {
			@Parameter(
				in = ParameterIn.PATH, name = "structuredContentFolderId"
			)
		}
	)
	@Path("/structured-content-folders/{structuredContentFolderId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContentFolder")})
	public StructuredContentFolder getStructuredContentFolder(
			@NotNull @Parameter(hidden = true)
			@PathParam("structuredContentFolderId") Long
				structuredContentFolderId)
		throws Exception {

		return new StructuredContentFolder();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PATCH' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}' -d $'{"customFields": ___, "description": ___, "name": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@Operation(
		description = "Updates only the fields received in the request body, leaving any other fields untouched."
	)
	@PATCH
	@Parameters(
		value = {
			@Parameter(
				in = ParameterIn.PATH, name = "structuredContentFolderId"
			)
		}
	)
	@Path("/structured-content-folders/{structuredContentFolderId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContentFolder")})
	public StructuredContentFolder patchStructuredContentFolder(
			@NotNull @Parameter(hidden = true)
			@PathParam("structuredContentFolderId") Long
				structuredContentFolderId,
			StructuredContentFolder structuredContentFolder)
		throws Exception {

		StructuredContentFolder existingStructuredContentFolder =
			getStructuredContentFolder(structuredContentFolderId);

		if (structuredContentFolder.getDateCreated() != null) {
			existingStructuredContentFolder.setDateCreated(
				structuredContentFolder.getDateCreated());
		}

		if (structuredContentFolder.getDateModified() != null) {
			existingStructuredContentFolder.setDateModified(
				structuredContentFolder.getDateModified());
		}

		if (structuredContentFolder.getDescription() != null) {
			existingStructuredContentFolder.setDescription(
				structuredContentFolder.getDescription());
		}

		if (structuredContentFolder.getName() != null) {
			existingStructuredContentFolder.setName(
				structuredContentFolder.getName());
		}

		if (structuredContentFolder.getNumberOfStructuredContentFolders() !=
				null) {

			existingStructuredContentFolder.setNumberOfStructuredContentFolders(
				structuredContentFolder.getNumberOfStructuredContentFolders());
		}

		if (structuredContentFolder.getNumberOfStructuredContents() != null) {
			existingStructuredContentFolder.setNumberOfStructuredContents(
				structuredContentFolder.getNumberOfStructuredContents());
		}

		if (structuredContentFolder.getSiteId() != null) {
			existingStructuredContentFolder.setSiteId(
				structuredContentFolder.getSiteId());
		}

		if (structuredContentFolder.getSubscribed() != null) {
			existingStructuredContentFolder.setSubscribed(
				structuredContentFolder.getSubscribed());
		}

		if (structuredContentFolder.getViewableBy() != null) {
			existingStructuredContentFolder.setViewableBy(
				structuredContentFolder.getViewableBy());
		}

		preparePatch(structuredContentFolder, existingStructuredContentFolder);

		return putStructuredContentFolder(
			structuredContentFolderId, existingStructuredContentFolder);
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}' -d $'{"customFields": ___, "description": ___, "name": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@Operation(
		description = "Replaces the structured content folder with the information sent in the request body. Any missing fields are deleted, unless they are required."
	)
	@PUT
	@Parameters(
		value = {
			@Parameter(
				in = ParameterIn.PATH, name = "structuredContentFolderId"
			)
		}
	)
	@Path("/structured-content-folders/{structuredContentFolderId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "StructuredContentFolder")})
	public StructuredContentFolder putStructuredContentFolder(
			@NotNull @Parameter(hidden = true)
			@PathParam("structuredContentFolderId") Long
				structuredContentFolderId,
			StructuredContentFolder structuredContentFolder)
		throws Exception {

		return new StructuredContentFolder();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}/subscribe'  -u 'test@liferay.com:test'
	 */
	@Override
	@PUT
	@Parameters(
		value = {
			@Parameter(
				in = ParameterIn.PATH, name = "structuredContentFolderId"
			)
		}
	)
	@Path("/structured-content-folders/{structuredContentFolderId}/subscribe")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "StructuredContentFolder")})
	public void putStructuredContentFolderSubscribe(
			@NotNull @Parameter(hidden = true)
			@PathParam("structuredContentFolderId") Long
				structuredContentFolderId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}/unsubscribe'  -u 'test@liferay.com:test'
	 */
	@Override
	@PUT
	@Parameters(
		value = {
			@Parameter(
				in = ParameterIn.PATH, name = "structuredContentFolderId"
			)
		}
	)
	@Path("/structured-content-folders/{structuredContentFolderId}/unsubscribe")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "StructuredContentFolder")})
	public void putStructuredContentFolderUnsubscribe(
			@NotNull @Parameter(hidden = true)
			@PathParam("structuredContentFolderId") Long
				structuredContentFolderId)
		throws Exception {
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
		StructuredContentFolder structuredContentFolder,
		StructuredContentFolder existingStructuredContentFolder) {
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