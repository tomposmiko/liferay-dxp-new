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

package com.liferay.digital.signature.rest.internal.graphql.mutation.v1_0;

import com.liferay.digital.signature.rest.dto.v1_0.DSEnvelope;
import com.liferay.digital.signature.rest.dto.v1_0.DSEnvelopeSignatureURL;
import com.liferay.digital.signature.rest.dto.v1_0.DSRecipientViewDefinition;
import com.liferay.digital.signature.rest.resource.v1_0.DSEnvelopeResource;
import com.liferay.digital.signature.rest.resource.v1_0.DSRecipientViewDefinitionResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineExportTaskResource;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.constraints.NotEmpty;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author José Abelenda
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setDSEnvelopeResourceComponentServiceObjects(
		ComponentServiceObjects<DSEnvelopeResource>
			dsEnvelopeResourceComponentServiceObjects) {

		_dsEnvelopeResourceComponentServiceObjects =
			dsEnvelopeResourceComponentServiceObjects;
	}

	public static void
		setDSRecipientViewDefinitionResourceComponentServiceObjects(
			ComponentServiceObjects<DSRecipientViewDefinitionResource>
				dsRecipientViewDefinitionResourceComponentServiceObjects) {

		_dsRecipientViewDefinitionResourceComponentServiceObjects =
			dsRecipientViewDefinitionResourceComponentServiceObjects;
	}

	@GraphQLField
	public Response createSiteDSEnvelopesPageExportBatch(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_dsEnvelopeResourceComponentServiceObjects,
			this::_populateResourceContext,
			dsEnvelopeResource ->
				dsEnvelopeResource.postSiteDSEnvelopesPageExportBatch(
					Long.valueOf(siteKey), callbackURL, contentType,
					fieldNames));
	}

	@GraphQLField
	public DSEnvelope createSiteDSEnvelope(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("dsEnvelope") DSEnvelope dsEnvelope)
		throws Exception {

		return _applyComponentServiceObjects(
			_dsEnvelopeResourceComponentServiceObjects,
			this::_populateResourceContext,
			dsEnvelopeResource -> dsEnvelopeResource.postSiteDSEnvelope(
				Long.valueOf(siteKey), dsEnvelope));
	}

	@GraphQLField
	public Response createSiteDSEnvelopeBatch(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("dsEnvelope") DSEnvelope dsEnvelope,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_dsEnvelopeResourceComponentServiceObjects,
			this::_populateResourceContext,
			dsEnvelopeResource -> dsEnvelopeResource.postSiteDSEnvelopeBatch(
				Long.valueOf(siteKey), dsEnvelope, callbackURL, object));
	}

	@GraphQLField
	public DSEnvelopeSignatureURL createSiteDSRecipientViewDefinition(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("dsEnvelopeId") String dsEnvelopeId,
			@GraphQLName("dsRecipientViewDefinition") DSRecipientViewDefinition
				dsRecipientViewDefinition)
		throws Exception {

		return _applyComponentServiceObjects(
			_dsRecipientViewDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			dsRecipientViewDefinitionResource ->
				dsRecipientViewDefinitionResource.
					postSiteDSRecipientViewDefinition(
						Long.valueOf(siteKey), dsEnvelopeId,
						dsRecipientViewDefinition));
	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private <T, E1 extends Throwable, E2 extends Throwable> void
			_applyVoidComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeConsumer<T, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			unsafeFunction.accept(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(DSEnvelopeResource dsEnvelopeResource)
		throws Exception {

		dsEnvelopeResource.setContextAcceptLanguage(_acceptLanguage);
		dsEnvelopeResource.setContextCompany(_company);
		dsEnvelopeResource.setContextHttpServletRequest(_httpServletRequest);
		dsEnvelopeResource.setContextHttpServletResponse(_httpServletResponse);
		dsEnvelopeResource.setContextUriInfo(_uriInfo);
		dsEnvelopeResource.setContextUser(_user);
		dsEnvelopeResource.setGroupLocalService(_groupLocalService);
		dsEnvelopeResource.setRoleLocalService(_roleLocalService);

		dsEnvelopeResource.setVulcanBatchEngineExportTaskResource(
			_vulcanBatchEngineExportTaskResource);

		dsEnvelopeResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			DSRecipientViewDefinitionResource dsRecipientViewDefinitionResource)
		throws Exception {

		dsRecipientViewDefinitionResource.setContextAcceptLanguage(
			_acceptLanguage);
		dsRecipientViewDefinitionResource.setContextCompany(_company);
		dsRecipientViewDefinitionResource.setContextHttpServletRequest(
			_httpServletRequest);
		dsRecipientViewDefinitionResource.setContextHttpServletResponse(
			_httpServletResponse);
		dsRecipientViewDefinitionResource.setContextUriInfo(_uriInfo);
		dsRecipientViewDefinitionResource.setContextUser(_user);
		dsRecipientViewDefinitionResource.setGroupLocalService(
			_groupLocalService);
		dsRecipientViewDefinitionResource.setRoleLocalService(
			_roleLocalService);
	}

	private static ComponentServiceObjects<DSEnvelopeResource>
		_dsEnvelopeResourceComponentServiceObjects;
	private static ComponentServiceObjects<DSRecipientViewDefinitionResource>
		_dsRecipientViewDefinitionResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private GroupLocalService _groupLocalService;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private RoleLocalService _roleLocalService;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private UriInfo _uriInfo;
	private com.liferay.portal.kernel.model.User _user;
	private VulcanBatchEngineExportTaskResource
		_vulcanBatchEngineExportTaskResource;
	private VulcanBatchEngineImportTaskResource
		_vulcanBatchEngineImportTaskResource;

}