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

package com.liferay.headless.form.internal.resource.v1_0;

import com.liferay.headless.form.dto.v1_0.FormRecord;
import com.liferay.headless.form.internal.dto.v1_0.FormRecordImpl;
import com.liferay.headless.form.resource.v1_0.FormRecordResource;
import com.liferay.oauth2.provider.scope.RequiresScope;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.net.URI;

import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
public abstract class BaseFormRecordResourceImpl implements FormRecordResource {

	@Override
	@GET
	@Path("/form-records/{form-record-id}")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public FormRecord getFormRecord(
	@PathParam("form-record-id") Long formRecordId)
			throws Exception {

				return new FormRecordImpl();
	}
	@Override
	@Consumes("application/json")
	@PUT
	@Path("/form-records/{form-record-id}")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public FormRecord putFormRecord(
	@PathParam("form-record-id") Long formRecordId,FormRecord formRecord)
			throws Exception {

				return new FormRecordImpl();
	}
	@Override
	@GET
	@Path("/forms/{form-id}/form-records")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public Page<FormRecord> getFormFormRecordsPage(
	@PathParam("form-id") Long formId,@Context Pagination pagination)
			throws Exception {

				return Page.of(Collections.emptyList());
	}
	@Override
	@Consumes("application/json")
	@POST
	@Path("/forms/{form-id}/form-records")
	@Produces("application/json")
	@RequiresScope("everything.read")
	public FormRecord postFormFormRecord(
	@PathParam("form-id") Long formId,FormRecord formRecord)
			throws Exception {

				return new FormRecordImpl();
	}

	public void setContextCompany(Company contextCompany) {
		this.contextCompany = contextCompany;
	}

	protected String getJAXRSLink(String methodName, Object... values) {
		URI baseURI = contextUriInfo.getBaseUri();

		URI resourceURI = UriBuilder.fromResource(
			BaseFormRecordResourceImpl.class
		).build();

		URI methodURI = UriBuilder.fromMethod(
			BaseFormRecordResourceImpl.class, methodName
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