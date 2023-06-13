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
import com.liferay.headless.form.resource.v1_0.FormRecordResource;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.TransformUtil;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import javax.validation.constraints.NotNull;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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
	@Parameters(
		value = {@Parameter(in = ParameterIn.PATH, name = "formRecordId")}
	)
	@Path("/form-records/{formRecordId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "FormRecord")})
	public FormRecord getFormRecord(
			@NotNull @Parameter(hidden = true) @PathParam("formRecordId") Long
				formRecordId)
		throws Exception {

		return new FormRecord();
	}

	@Override
	@Consumes({"application/json", "application/xml"})
	@PUT
	@Parameters(
		value = {@Parameter(in = ParameterIn.PATH, name = "formRecordId")}
	)
	@Path("/form-records/{formRecordId}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "FormRecord")})
	public FormRecord putFormRecord(
			@NotNull @Parameter(hidden = true) @PathParam("formRecordId") Long
				formRecordId,
			FormRecord formRecord)
		throws Exception {

		return new FormRecord();
	}

	@Override
	@GET
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "formId"),
			@Parameter(in = ParameterIn.QUERY, name = "page"),
			@Parameter(in = ParameterIn.QUERY, name = "pageSize")
		}
	)
	@Path("/forms/{formId}/form-records")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "FormRecord")})
	public Page<FormRecord> getFormFormRecordsPage(
			@NotNull @Parameter(hidden = true) @PathParam("formId") Long formId,
			@Context Pagination pagination)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	@Override
	@Consumes({"application/json", "application/xml"})
	@POST
	@Parameters(value = {@Parameter(in = ParameterIn.PATH, name = "formId")})
	@Path("/forms/{formId}/form-records")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "FormRecord")})
	public FormRecord postFormFormRecord(
			@NotNull @Parameter(hidden = true) @PathParam("formId") Long formId,
			FormRecord formRecord)
		throws Exception {

		return new FormRecord();
	}

	@Override
	@GET
	@Parameters(value = {@Parameter(in = ParameterIn.PATH, name = "formId")})
	@Path("/forms/{formId}/form-records/by-latest-draft")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "FormRecord")})
	public FormRecord getFormFormRecordByLatestDraft(
			@NotNull @Parameter(hidden = true) @PathParam("formId") Long formId)
		throws Exception {

		return new FormRecord();
	}

	public void setContextCompany(Company contextCompany) {
		this.contextCompany = contextCompany;
	}

	protected void preparePatch(
		FormRecord formRecord, FormRecord existingFormRecord) {
	}

	protected <T, R> List<R> transform(
		Collection<T> collection,
		UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transform(collection, unsafeFunction);
	}

	protected <T, R> R[] transform(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction,
		Class<?> clazz) {

		return TransformUtil.transform(array, unsafeFunction, clazz);
	}

	protected <T, R> R[] transformToArray(
		Collection<T> collection,
		UnsafeFunction<T, R, Exception> unsafeFunction, Class<?> clazz) {

		return TransformUtil.transformToArray(
			collection, unsafeFunction, clazz);
	}

	protected <T, R> List<R> transformToList(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transformToList(array, unsafeFunction);
	}

	@Context
	protected AcceptLanguage contextAcceptLanguage;

	@Context
	protected Company contextCompany;

	@Context
	protected UriInfo contextUriInfo;

}