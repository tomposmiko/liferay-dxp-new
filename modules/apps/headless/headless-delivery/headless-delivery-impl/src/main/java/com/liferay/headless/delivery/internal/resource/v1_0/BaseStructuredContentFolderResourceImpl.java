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
public abstract class BaseStructuredContentFolderResourceImpl
	implements EntityModelResource, StructuredContentFolderResource,
			   VulcanBatchEngineTaskItemDelegate<StructuredContentFolder> {

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/asset-libraries/{assetLibraryId}/structured-content-folders'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "assetLibraryId"
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
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path(
		"/asset-libraries/{assetLibraryId}/structured-content-folders"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Page<StructuredContentFolder>
			getAssetLibraryStructuredContentFoldersPage(
				@io.swagger.v3.oas.annotations.Parameter(hidden = true)
				@javax.validation.constraints.NotNull
				@javax.ws.rs.PathParam("assetLibraryId")
				Long assetLibraryId,
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
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/asset-libraries/{assetLibraryId}/structured-content-folders' -d $'{"customFields": ___, "description": ___, "name": ___, "parentStructuredContentFolderId": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "assetLibraryId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path(
		"/asset-libraries/{assetLibraryId}/structured-content-folders"
	)
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public StructuredContentFolder postAssetLibraryStructuredContentFolder(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("assetLibraryId")
			Long assetLibraryId,
			StructuredContentFolder structuredContentFolder)
		throws Exception {

		return new StructuredContentFolder();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/asset-libraries/{assetLibraryId}/structured-content-folders/batch'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "assetLibraryId"
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
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.Path(
		"/asset-libraries/{assetLibraryId}/structured-content-folders/batch"
	)
	@javax.ws.rs.POST
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response postAssetLibraryStructuredContentFolderBatch(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("assetLibraryId")
			Long assetLibraryId,
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
				StructuredContentFolder.class.getName(), callbackURL, null,
				object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/structured-content-folders'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the site's structured content folders. Results can be paginated, filtered, searched, flattened, and sorted."
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
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path("/sites/{siteId}/structured-content-folders")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Page<StructuredContentFolder> getSiteStructuredContentFoldersPage(
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
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/structured-content-folders' -d $'{"customFields": ___, "description": ___, "name": ___, "parentStructuredContentFolderId": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Creates a new structured content folder."
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
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path("/sites/{siteId}/structured-content-folders")
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public StructuredContentFolder postSiteStructuredContentFolder(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("siteId")
			Long siteId,
			StructuredContentFolder structuredContentFolder)
		throws Exception {

		return new StructuredContentFolder();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/sites/{siteId}/structured-content-folders/batch'  -u 'test@liferay.com:test'
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
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.Path("/sites/{siteId}/structured-content-folders/batch")
	@javax.ws.rs.POST
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response postSiteStructuredContentFolderBatch(
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
				StructuredContentFolder.class.getName(), callbackURL, null,
				object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{parentStructuredContentFolderId}/structured-content-folders'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the parent structured content folder's subfolders. Results can be paginated, filtered, searched, and sorted."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "parentStructuredContentFolderId"
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
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path(
		"/structured-content-folders/{parentStructuredContentFolderId}/structured-content-folders"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Page<StructuredContentFolder>
			getStructuredContentFolderStructuredContentFoldersPage(
				@io.swagger.v3.oas.annotations.Parameter(hidden = true)
				@javax.validation.constraints.NotNull
				@javax.ws.rs.PathParam("parentStructuredContentFolderId")
				Long parentStructuredContentFolderId,
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
	 * curl -X 'POST' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{parentStructuredContentFolderId}/structured-content-folders' -d $'{"customFields": ___, "description": ___, "name": ___, "parentStructuredContentFolderId": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Creates a new structured content folder in an existing folder."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "parentStructuredContentFolderId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path(
		"/structured-content-folders/{parentStructuredContentFolderId}/structured-content-folders"
	)
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public StructuredContentFolder
			postStructuredContentFolderStructuredContentFolder(
				@io.swagger.v3.oas.annotations.Parameter(hidden = true)
				@javax.validation.constraints.NotNull
				@javax.ws.rs.PathParam("parentStructuredContentFolderId")
				Long parentStructuredContentFolderId,
				StructuredContentFolder structuredContentFolder)
		throws Exception {

		return new StructuredContentFolder();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Deletes the structured content folder and returns a 204 if the operation succeeds."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "structuredContentFolderId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("/structured-content-folders/{structuredContentFolderId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public void deleteStructuredContentFolder(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("structuredContentFolderId")
			Long structuredContentFolderId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/batch'  -u 'test@liferay.com:test'
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
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("/structured-content-folders/batch")
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response deleteStructuredContentFolderBatch(
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
				StructuredContentFolder.class.getName(), callbackURL, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Retrieves the structured content folder."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "structuredContentFolderId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path("/structured-content-folders/{structuredContentFolderId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public StructuredContentFolder getStructuredContentFolder(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("structuredContentFolderId")
			Long structuredContentFolderId)
		throws Exception {

		return new StructuredContentFolder();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PATCH' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}' -d $'{"customFields": ___, "description": ___, "name": ___, "parentStructuredContentFolderId": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Updates only the fields received in the request body, leaving any other fields untouched."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "structuredContentFolderId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.PATCH
	@javax.ws.rs.Path("/structured-content-folders/{structuredContentFolderId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public StructuredContentFolder patchStructuredContentFolder(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("structuredContentFolderId")
			Long structuredContentFolderId,
			StructuredContentFolder structuredContentFolder)
		throws Exception {

		StructuredContentFolder existingStructuredContentFolder =
			getStructuredContentFolder(structuredContentFolderId);

		if (structuredContentFolder.getActions() != null) {
			existingStructuredContentFolder.setActions(
				structuredContentFolder.getActions());
		}

		if (structuredContentFolder.getAssetLibraryKey() != null) {
			existingStructuredContentFolder.setAssetLibraryKey(
				structuredContentFolder.getAssetLibraryKey());
		}

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

		if (structuredContentFolder.getParentStructuredContentFolderId() !=
				null) {

			existingStructuredContentFolder.setParentStructuredContentFolderId(
				structuredContentFolder.getParentStructuredContentFolderId());
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
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}' -d $'{"customFields": ___, "description": ___, "name": ___, "parentStructuredContentFolderId": ___, "viewableBy": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Operation(
		description = "Replaces the structured content folder with the information sent in the request body. Any missing fields are deleted, unless they are required."
	)
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "structuredContentFolderId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path("/structured-content-folders/{structuredContentFolderId}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public StructuredContentFolder putStructuredContentFolder(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("structuredContentFolderId")
			Long structuredContentFolderId,
			StructuredContentFolder structuredContentFolder)
		throws Exception {

		return new StructuredContentFolder();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/batch'  -u 'test@liferay.com:test'
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
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.Path("/structured-content-folders/batch")
	@javax.ws.rs.Produces("application/json")
	@javax.ws.rs.PUT
	@Override
	public Response putStructuredContentFolderBatch(
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
				StructuredContentFolder.class.getName(), callbackURL, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}/subscribe'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "structuredContentFolderId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Path(
		"/structured-content-folders/{structuredContentFolderId}/subscribe"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public void putStructuredContentFolderSubscribe(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("structuredContentFolderId")
			Long structuredContentFolderId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/headless-delivery/v1.0/structured-content-folders/{structuredContentFolderId}/unsubscribe'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "structuredContentFolderId"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {
			@io.swagger.v3.oas.annotations.tags.Tag(
				name = "StructuredContentFolder"
			)
		}
	)
	@javax.ws.rs.Path(
		"/structured-content-folders/{structuredContentFolderId}/unsubscribe"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@javax.ws.rs.PUT
	@Override
	public void putStructuredContentFolderUnsubscribe(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("structuredContentFolderId")
			Long structuredContentFolderId)
		throws Exception {
	}

	@Override
	@SuppressWarnings("PMD.UnusedLocalVariable")
	public void create(
			Collection<StructuredContentFolder> structuredContentFolders,
			Map<String, Serializable> parameters)
		throws Exception {

		UnsafeConsumer<StructuredContentFolder, Exception>
			structuredContentFolderUnsafeConsumer = null;

		String createStrategy = (String)parameters.getOrDefault(
			"createStrategy", "INSERT");

		if ("INSERT".equalsIgnoreCase(createStrategy)) {
			if (parameters.containsKey("assetLibraryId")) {
				structuredContentFolderUnsafeConsumer =
					structuredContentFolder ->
						postAssetLibraryStructuredContentFolder(
							(Long)parameters.get("assetLibraryId"),
							structuredContentFolder);
			}
			else if (parameters.containsKey("siteId")) {
				structuredContentFolderUnsafeConsumer =
					structuredContentFolder -> postSiteStructuredContentFolder(
						(Long)parameters.get("siteId"),
						structuredContentFolder);
			}
			else {
				throw new NotSupportedException(
					"One of the following parameters must be specified: [assetLibraryId, siteId]");
			}
		}

		if (structuredContentFolderUnsafeConsumer == null) {
			throw new NotSupportedException(
				"Create strategy \"" + createStrategy +
					"\" is not supported for StructuredContentFolder");
		}

		if (contextBatchUnsafeConsumer != null) {
			contextBatchUnsafeConsumer.accept(
				structuredContentFolders,
				structuredContentFolderUnsafeConsumer);
		}
		else {
			for (StructuredContentFolder structuredContentFolder :
					structuredContentFolders) {

				structuredContentFolderUnsafeConsumer.accept(
					structuredContentFolder);
			}
		}
	}

	@Override
	public void delete(
			Collection<StructuredContentFolder> structuredContentFolders,
			Map<String, Serializable> parameters)
		throws Exception {

		for (StructuredContentFolder structuredContentFolder :
				structuredContentFolders) {

			deleteStructuredContentFolder(structuredContentFolder.getId());
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
	public Page<StructuredContentFolder> read(
			Filter filter, Pagination pagination, Sort[] sorts,
			Map<String, Serializable> parameters, String search)
		throws Exception {

		if (parameters.containsKey("assetLibraryId")) {
			return getAssetLibraryStructuredContentFoldersPage(
				(Long)parameters.get("assetLibraryId"),
				Boolean.parseBoolean((String)parameters.get("flatten")), search,
				null, filter, pagination, sorts);
		}
		else if (parameters.containsKey("siteId")) {
			return getSiteStructuredContentFoldersPage(
				(Long)parameters.get("siteId"),
				Boolean.parseBoolean((String)parameters.get("flatten")), search,
				null, filter, pagination, sorts);
		}
		else {
			throw new NotSupportedException(
				"One of the following parameters must be specified: [assetLibraryId, siteId]");
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
			Collection<StructuredContentFolder> structuredContentFolders,
			Map<String, Serializable> parameters)
		throws Exception {

		UnsafeConsumer<StructuredContentFolder, Exception>
			structuredContentFolderUnsafeConsumer = null;

		String updateStrategy = (String)parameters.getOrDefault(
			"updateStrategy", "UPDATE");

		if ("PARTIAL_UPDATE".equalsIgnoreCase(updateStrategy)) {
			structuredContentFolderUnsafeConsumer =
				structuredContentFolder -> patchStructuredContentFolder(
					structuredContentFolder.getId() != null ?
						structuredContentFolder.getId() :
							Long.parseLong(
								(String)parameters.get(
									"structuredContentFolderId")),
					structuredContentFolder);
		}

		if ("UPDATE".equalsIgnoreCase(updateStrategy)) {
			structuredContentFolderUnsafeConsumer =
				structuredContentFolder -> putStructuredContentFolder(
					structuredContentFolder.getId() != null ?
						structuredContentFolder.getId() :
							Long.parseLong(
								(String)parameters.get(
									"structuredContentFolderId")),
					structuredContentFolder);
		}

		if (structuredContentFolderUnsafeConsumer == null) {
			throw new NotSupportedException(
				"Update strategy \"" + updateStrategy +
					"\" is not supported for StructuredContentFolder");
		}

		if (contextBatchUnsafeConsumer != null) {
			contextBatchUnsafeConsumer.accept(
				structuredContentFolders,
				structuredContentFolderUnsafeConsumer);
		}
		else {
			for (StructuredContentFolder structuredContentFolder :
					structuredContentFolders) {

				structuredContentFolderUnsafeConsumer.accept(
					structuredContentFolder);
			}
		}
	}

	public void setContextAcceptLanguage(AcceptLanguage contextAcceptLanguage) {
		this.contextAcceptLanguage = contextAcceptLanguage;
	}

	public void setContextBatchUnsafeConsumer(
		UnsafeBiConsumer
			<Collection<StructuredContentFolder>,
			 UnsafeConsumer<StructuredContentFolder, Exception>, Exception>
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
		StructuredContentFolder structuredContentFolder,
		StructuredContentFolder existingStructuredContentFolder) {
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
		<Collection<StructuredContentFolder>,
		 UnsafeConsumer<StructuredContentFolder, Exception>, Exception>
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
		LogFactoryUtil.getLog(BaseStructuredContentFolderResourceImpl.class);

}