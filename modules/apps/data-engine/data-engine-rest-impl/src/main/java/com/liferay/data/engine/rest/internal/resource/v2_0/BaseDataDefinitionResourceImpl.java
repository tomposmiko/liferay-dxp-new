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

package com.liferay.data.engine.rest.internal.resource.v2_0;

import com.liferay.data.engine.rest.dto.v2_0.DataDefinition;
import com.liferay.data.engine.rest.resource.v2_0.DataDefinitionResource;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.permission.ModelPermissionsUtil;
import com.liferay.portal.vulcan.permission.PermissionUtil;
import com.liferay.portal.vulcan.util.ActionUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.constraints.NotNull;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * @author Jeyvison Nascimento
 * @generated
 */
@Generated("")
@Path("/v2.0")
public abstract class BaseDataDefinitionResourceImpl
	implements DataDefinitionResource {

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/data-engine/v2.0/data-definitions/by-content-type/{contentType}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "contentType"),
			@Parameter(in = ParameterIn.QUERY, name = "keywords"),
			@Parameter(in = ParameterIn.QUERY, name = "page"),
			@Parameter(in = ParameterIn.QUERY, name = "pageSize"),
			@Parameter(in = ParameterIn.QUERY, name = "sort")
		}
	)
	@Path("/data-definitions/by-content-type/{contentType}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public Page<DataDefinition> getDataDefinitionByContentTypeContentTypePage(
			@NotNull @Parameter(hidden = true) @PathParam("contentType") String
				contentType,
			@Parameter(hidden = true) @QueryParam("keywords") String keywords,
			@Context Pagination pagination, @Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/data-engine/v2.0/data-definitions/by-content-type/{contentType}' -d $'{"availableLanguageIds": ___, "contentType": ___, "dataDefinitionFields": ___, "dataDefinitionKey": ___, "dateCreated": ___, "dateModified": ___, "defaultDataLayout": ___, "defaultLanguageId": ___, "description": ___, "id": ___, "name": ___, "siteId": ___, "storageType": ___, "userId": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@POST
	@Parameters(
		value = {@Parameter(in = ParameterIn.PATH, name = "contentType")}
	)
	@Path("/data-definitions/by-content-type/{contentType}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public DataDefinition postDataDefinitionByContentType(
			@NotNull @Parameter(hidden = true) @PathParam("contentType") String
				contentType,
			DataDefinition dataDefinition)
		throws Exception {

		return new DataDefinition();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/data-engine/v2.0/data-definitions/data-definition-fields/field-types'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Path("/data-definitions/data-definition-fields/field-types")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public String getDataDefinitionDataDefinitionFieldFieldTypes()
		throws Exception {

		return StringPool.BLANK;
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/data-engine/v2.0/data-definitions/{dataDefinitionId}'  -u 'test@liferay.com:test'
	 */
	@Override
	@DELETE
	@Parameters(
		value = {@Parameter(in = ParameterIn.PATH, name = "dataDefinitionId")}
	)
	@Path("/data-definitions/{dataDefinitionId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public void deleteDataDefinition(
			@NotNull @Parameter(hidden = true) @PathParam("dataDefinitionId")
				Long dataDefinitionId)
		throws Exception {
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/data-engine/v2.0/data-definitions/{dataDefinitionId}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Parameters(
		value = {@Parameter(in = ParameterIn.PATH, name = "dataDefinitionId")}
	)
	@Path("/data-definitions/{dataDefinitionId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public DataDefinition getDataDefinition(
			@NotNull @Parameter(hidden = true) @PathParam("dataDefinitionId")
				Long dataDefinitionId)
		throws Exception {

		return new DataDefinition();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/data-engine/v2.0/data-definitions/{dataDefinitionId}' -d $'{"availableLanguageIds": ___, "contentType": ___, "dataDefinitionFields": ___, "dataDefinitionKey": ___, "dateCreated": ___, "dateModified": ___, "defaultDataLayout": ___, "defaultLanguageId": ___, "description": ___, "id": ___, "name": ___, "siteId": ___, "storageType": ___, "userId": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@PUT
	@Parameters(
		value = {@Parameter(in = ParameterIn.PATH, name = "dataDefinitionId")}
	)
	@Path("/data-definitions/{dataDefinitionId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public DataDefinition putDataDefinition(
			@NotNull @Parameter(hidden = true) @PathParam("dataDefinitionId")
				Long dataDefinitionId,
			DataDefinition dataDefinition)
		throws Exception {

		return new DataDefinition();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/data-engine/v2.0/data-definitions/{dataDefinitionId}/data-definition-field-links'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "dataDefinitionId"),
			@Parameter(in = ParameterIn.QUERY, name = "fieldName")
		}
	)
	@Path("/data-definitions/{dataDefinitionId}/data-definition-field-links")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public String getDataDefinitionDataDefinitionFieldLinks(
			@NotNull @Parameter(hidden = true) @PathParam("dataDefinitionId")
				Long dataDefinitionId,
			@NotNull @Parameter(hidden = true) @QueryParam("fieldName") String
				fieldName)
		throws Exception {

		return StringPool.BLANK;
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/data-engine/v2.0/data-definitions/{dataDefinitionId}/permissions'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "dataDefinitionId"),
			@Parameter(in = ParameterIn.QUERY, name = "roleNames")
		}
	)
	@Path("/data-definitions/{dataDefinitionId}/permissions")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public Page<com.liferay.portal.vulcan.permission.Permission>
			getDataDefinitionPermissionsPage(
				@NotNull @Parameter(hidden = true)
				@PathParam("dataDefinitionId") Long dataDefinitionId,
				@Parameter(hidden = true) @QueryParam("roleNames") String
					roleNames)
		throws Exception {

		String resourceName = getPermissionCheckerResourceName(
			dataDefinitionId);

		PermissionUtil.checkPermission(
			ActionKeys.PERMISSIONS, groupLocalService, resourceName,
			dataDefinitionId, getPermissionCheckerGroupId(dataDefinitionId));

		return Page.of(
			transform(
				PermissionUtil.getRoles(
					contextCompany, roleLocalService,
					StringUtil.split(roleNames)),
				role -> PermissionUtil.toPermission(
					contextCompany.getCompanyId(), dataDefinitionId,
					resourceActionLocalService.getResourceActions(resourceName),
					resourceName, resourcePermissionLocalService, role)));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PUT' 'http://localhost:8080/o/data-engine/v2.0/data-definitions/{dataDefinitionId}/permissions'  -u 'test@liferay.com:test'
	 */
	@Override
	@PUT
	@Parameters(
		value = {@Parameter(in = ParameterIn.PATH, name = "dataDefinitionId")}
	)
	@Path("/data-definitions/{dataDefinitionId}/permissions")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public void putDataDefinitionPermission(
			@NotNull @Parameter(hidden = true) @PathParam("dataDefinitionId")
				Long dataDefinitionId,
			com.liferay.portal.vulcan.permission.Permission[] permissions)
		throws Exception {

		String resourceName = getPermissionCheckerResourceName(
			dataDefinitionId);

		PermissionUtil.checkPermission(
			ActionKeys.PERMISSIONS, groupLocalService, resourceName,
			dataDefinitionId, getPermissionCheckerGroupId(dataDefinitionId));

		resourcePermissionLocalService.updateResourcePermissions(
			contextCompany.getCompanyId(), 0, resourceName,
			String.valueOf(dataDefinitionId),
			ModelPermissionsUtil.toModelPermissions(
				contextCompany.getCompanyId(), permissions, dataDefinitionId,
				resourceName, resourceActionLocalService,
				resourcePermissionLocalService, roleLocalService));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/data-engine/v2.0/sites/{siteId}/data-definitions/by-content-type/{contentType}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "siteId"),
			@Parameter(in = ParameterIn.PATH, name = "contentType"),
			@Parameter(in = ParameterIn.QUERY, name = "keywords"),
			@Parameter(in = ParameterIn.QUERY, name = "page"),
			@Parameter(in = ParameterIn.QUERY, name = "pageSize"),
			@Parameter(in = ParameterIn.QUERY, name = "sort")
		}
	)
	@Path("/sites/{siteId}/data-definitions/by-content-type/{contentType}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public Page<DataDefinition>
			getSiteDataDefinitionByContentTypeContentTypePage(
				@NotNull @Parameter(hidden = true) @PathParam("siteId") Long
					siteId,
				@NotNull @Parameter(hidden = true) @PathParam("contentType")
					String contentType,
				@Parameter(hidden = true) @QueryParam("keywords") String
					keywords,
				@Context Pagination pagination, @Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/data-engine/v2.0/sites/{siteId}/data-definitions/by-content-type/{contentType}' -d $'{"availableLanguageIds": ___, "contentType": ___, "dataDefinitionFields": ___, "dataDefinitionKey": ___, "dateCreated": ___, "dateModified": ___, "defaultDataLayout": ___, "defaultLanguageId": ___, "description": ___, "id": ___, "name": ___, "siteId": ___, "storageType": ___, "userId": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@POST
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "siteId"),
			@Parameter(in = ParameterIn.PATH, name = "contentType")
		}
	)
	@Path("/sites/{siteId}/data-definitions/by-content-type/{contentType}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public DataDefinition postSiteDataDefinitionByContentType(
			@NotNull @Parameter(hidden = true) @PathParam("siteId") Long siteId,
			@NotNull @Parameter(hidden = true) @PathParam("contentType") String
				contentType,
			DataDefinition dataDefinition)
		throws Exception {

		return new DataDefinition();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/data-engine/v2.0/sites/{siteId}/data-definitions/by-content-type/{contentType}/by-data-definition-key/{dataDefinitionKey}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "siteId"),
			@Parameter(in = ParameterIn.PATH, name = "contentType"),
			@Parameter(in = ParameterIn.PATH, name = "dataDefinitionKey")
		}
	)
	@Path(
		"/sites/{siteId}/data-definitions/by-content-type/{contentType}/by-data-definition-key/{dataDefinitionKey}"
	)
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "DataDefinition")})
	public DataDefinition getSiteDataDefinitionByContentTypeByDataDefinitionKey(
			@NotNull @Parameter(hidden = true) @PathParam("siteId") Long siteId,
			@NotNull @Parameter(hidden = true) @PathParam("contentType") String
				contentType,
			@NotNull @Parameter(hidden = true) @PathParam("dataDefinitionKey")
				String dataDefinitionKey)
		throws Exception {

		return new DataDefinition();
	}

	protected String getPermissionCheckerActionsResourceName(Object id)
		throws Exception {

		return getPermissionCheckerResourceName(id);
	}

	protected Long getPermissionCheckerGroupId(Object id) throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String getPermissionCheckerPortletName(Object id)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String getPermissionCheckerResourceName(Object id)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	public void setContextAcceptLanguage(AcceptLanguage contextAcceptLanguage) {
		this.contextAcceptLanguage = contextAcceptLanguage;
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

	protected Map<String, String> addAction(
		String actionName, GroupedModel groupedModel, String methodName) {

		return ActionUtil.addAction(
			actionName, getClass(), groupedModel, methodName,
			contextScopeChecker, contextUriInfo);
	}

	protected Map<String, String> addAction(
		String actionName, Long id, String methodName, String permissionName,
		Long siteId) {

		return ActionUtil.addAction(
			actionName, getClass(), id, methodName, permissionName,
			contextScopeChecker, siteId, contextUriInfo);
	}

	protected Map<String, String> addAction(
		String actionName, String methodName, String permissionName,
		Long siteId) {

		return addAction(
			actionName, siteId, methodName, permissionName, siteId);
	}

	protected void preparePatch(
		DataDefinition dataDefinition, DataDefinition existingDataDefinition) {
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
	protected com.liferay.portal.kernel.model.Company contextCompany;
	protected com.liferay.portal.kernel.model.User contextUser;
	protected GroupLocalService groupLocalService;
	protected HttpServletRequest contextHttpServletRequest;
	protected HttpServletResponse contextHttpServletResponse;
	protected ResourceActionLocalService resourceActionLocalService;
	protected ResourcePermissionLocalService resourcePermissionLocalService;
	protected RoleLocalService roleLocalService;
	protected Object contextScopeChecker;
	protected UriInfo contextUriInfo;

}