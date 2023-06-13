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

package com.liferay.headless.web.experience.internal.resource.v1_0;

import com.liferay.headless.web.experience.dto.v1_0.StructuredContent;
import com.liferay.headless.web.experience.internal.dto.v1_0.StructuredContentImpl;
import com.liferay.headless.web.experience.resource.v1_0.StructuredContentResource;
import com.liferay.oauth2.provider.scope.RequiresScope;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.net.URI;

import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
@Path("/v1.0")
public abstract class BaseStructuredContentResourceImpl implements StructuredContentResource {

	@Override
	@GET
	@Path("/content-structures/{content-structure-id}/structured-contents")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public Page<StructuredContent> getContentSpaceContentStructureStructuredContentsPage(
	@PathParam("content-space-id") Long contentSpaceId,@PathParam("content-structure-id") Long contentStructureId,@Context Filter filter,@Context Pagination pagination,@Context Sort[] sorts)
			throws Exception {

				return Page.of(Collections.emptyList());
	}
	@Override
	@GET
	@Path("/content-spaces/{content-space-id}/structured-contents")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public Page<StructuredContent> getContentSpaceStructuredContentsPage(
	@PathParam("content-space-id") Long contentSpaceId,@Context Filter filter,@Context Pagination pagination,@Context Sort[] sorts)
			throws Exception {

				return Page.of(Collections.emptyList());
	}
	@Override
	@Consumes("application/json")
	@POST
	@Path("/content-spaces/{content-space-id}/structured-contents")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public StructuredContent postContentSpaceStructuredContent(
	@PathParam("content-space-id") Long contentSpaceId,StructuredContent structuredContent)
			throws Exception {

				return new StructuredContentImpl();
	}
	@Override
	@DELETE
	@Path("/structured-contents/{structured-content-id}")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public boolean deleteStructuredContent(
	@PathParam("structured-content-id") Long structuredContentId)
			throws Exception {

				return false;
	}
	@Override
	@GET
	@Path("/structured-contents/{structured-content-id}")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public StructuredContent getStructuredContent(
	@PathParam("structured-content-id") Long structuredContentId)
			throws Exception {

				return new StructuredContentImpl();
	}
	@Override
	@Consumes("application/json")
	@PATCH
	@Path("/structured-contents/{structured-content-id}")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public StructuredContent patchStructuredContent(
	@PathParam("structured-content-id") Long structuredContentId,StructuredContent structuredContent)
			throws Exception {

				StructuredContent existingStructuredContent = getStructuredContent(structuredContentId);

						if (Validator.isNotNull(structuredContent.getAvailableLanguages())) {
							existingStructuredContent.setAvailableLanguages(structuredContent.getAvailableLanguages());
	}
						if (Validator.isNotNull(structuredContent.getContentSpace())) {
							existingStructuredContent.setContentSpace(structuredContent.getContentSpace());
	}
						if (Validator.isNotNull(structuredContent.getContentStructureId())) {
							existingStructuredContent.setContentStructureId(structuredContent.getContentStructureId());
	}
						if (Validator.isNotNull(structuredContent.getDateCreated())) {
							existingStructuredContent.setDateCreated(structuredContent.getDateCreated());
	}
						if (Validator.isNotNull(structuredContent.getDateModified())) {
							existingStructuredContent.setDateModified(structuredContent.getDateModified());
	}
						if (Validator.isNotNull(structuredContent.getDatePublished())) {
							existingStructuredContent.setDatePublished(structuredContent.getDatePublished());
	}
						if (Validator.isNotNull(structuredContent.getDescription())) {
							existingStructuredContent.setDescription(structuredContent.getDescription());
	}
						if (Validator.isNotNull(structuredContent.getId())) {
							existingStructuredContent.setId(structuredContent.getId());
	}
						if (Validator.isNotNull(structuredContent.getKeywords())) {
							existingStructuredContent.setKeywords(structuredContent.getKeywords());
	}
						if (Validator.isNotNull(structuredContent.getLastReviewed())) {
							existingStructuredContent.setLastReviewed(structuredContent.getLastReviewed());
	}
						if (Validator.isNotNull(structuredContent.getTitle())) {
							existingStructuredContent.setTitle(structuredContent.getTitle());
	}

				return putStructuredContent(structuredContentId, existingStructuredContent);
	}
	@Override
	@Consumes("application/json")
	@PUT
	@Path("/structured-contents/{structured-content-id}")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public StructuredContent putStructuredContent(
	@PathParam("structured-content-id") Long structuredContentId,StructuredContent structuredContent)
			throws Exception {

				return new StructuredContentImpl();
	}
	@Override
	@GET
	@Path("/structured-contents/{structured-content-id}/rendered-content/{template-id}")
	@Produces("text/html")
	@RequiresScope("everything.read")
	public String getStructuredContentTemplate(
	@PathParam("structured-content-id") Long structuredContentId,@PathParam("template-id") Long templateId)
			throws Exception {

				return StringPool.BLANK;
	}

	public void setContextCompany(Company contextCompany) {
		this.contextCompany = contextCompany;
	}

	protected String getJAXRSLink(String methodName, Object... values) {
		URI baseURI = contextUriInfo.getBaseUri();

		URI resourceURI = UriBuilder.fromResource(
			BaseStructuredContentResourceImpl.class
		).build();

		URI methodURI = UriBuilder.fromMethod(
			BaseStructuredContentResourceImpl.class, methodName
		).build(
			values
		);

		return baseURI.toString() + resourceURI.toString() + methodURI.toString();
	}

	protected <T, R> List<R> transform(List<T> list, UnsafeFunction<T, R, Throwable> unsafeFunction) {
		return TransformUtil.transform(list, unsafeFunction);
	}

	@Context
	protected AcceptLanguage contextAcceptLanguage;

	@Context
	protected Company contextCompany;

	@Context
	protected UriInfo contextUriInfo;

}