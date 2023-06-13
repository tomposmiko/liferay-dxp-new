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

package com.liferay.data.engine.rest.internal.resource.v1_0;

import com.liferay.data.engine.rest.dto.v1_0.DataRecord;
import com.liferay.data.engine.rest.resource.v1_0.DataRecordResource;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.string.StringPool;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * @author Jeyvison Nascimento
 * @generated
 */
@Generated("")
@Path("/v1.0")
public abstract class BaseDataRecordResourceImpl implements DataRecordResource {

	@Override
	@GET
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.QUERY, name = "page"),
			@Parameter(in = ParameterIn.QUERY, name = "pageSize")
		}
	)
	@Path("/data-record-collections/{data-record-collection-id}/data-records")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "DataRecord")})
	public Page<DataRecord> getDataRecordCollectionDataRecordsPage(
			@NotNull @PathParam("data-record-collection-id") Long
				dataRecordCollectionId,
			@Context Pagination pagination)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	@Override
	@Consumes("application/json")
	@POST
	@Path("/data-record-collections/{data-record-collection-id}/data-records")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "DataRecord")})
	public DataRecord postDataRecordCollectionDataRecord(
			@NotNull @PathParam("data-record-collection-id") Long
				dataRecordCollectionId,
			DataRecord dataRecord)
		throws Exception {

		return new DataRecord();
	}

	@Override
	@GET
	@Path(
		"/data-record-collections/{data-record-collection-id}/data-records/export"
	)
	@Produces("application/json")
	@Tags(value = {@Tag(name = "DataRecord")})
	public String getDataRecordCollectionDataRecordExport(
			@NotNull @PathParam("data-record-collection-id") Long
				dataRecordCollectionId)
		throws Exception {

		return StringPool.BLANK;
	}

	@Override
	@DELETE
	@Path("/data-records/{data-record-id}")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "DataRecord")})
	public void deleteDataRecord(
			@NotNull @PathParam("data-record-id") Long dataRecordId)
		throws Exception {
	}

	@Override
	@GET
	@Path("/data-records/{data-record-id}")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "DataRecord")})
	public DataRecord getDataRecord(
			@NotNull @PathParam("data-record-id") Long dataRecordId)
		throws Exception {

		return new DataRecord();
	}

	@Override
	@Consumes("application/json")
	@PUT
	@Path("/data-records/{data-record-id}")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "DataRecord")})
	public DataRecord putDataRecord(
			@NotNull @PathParam("data-record-id") Long dataRecordId,
			DataRecord dataRecord)
		throws Exception {

		return new DataRecord();
	}

	public void setContextCompany(Company contextCompany) {
		this.contextCompany = contextCompany;
	}

	protected void preparePatch(DataRecord dataRecord) {
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