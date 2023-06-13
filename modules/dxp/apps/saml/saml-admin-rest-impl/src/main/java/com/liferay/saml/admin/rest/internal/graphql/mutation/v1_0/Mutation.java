/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.saml.admin.rest.internal.graphql.mutation.v1_0;

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
import com.liferay.saml.admin.rest.dto.v1_0.SamlProvider;
import com.liferay.saml.admin.rest.resource.v1_0.SamlProviderResource;

import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Stian Sigvartsen
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setSamlProviderResourceComponentServiceObjects(
		ComponentServiceObjects<SamlProviderResource>
			samlProviderResourceComponentServiceObjects) {

		_samlProviderResourceComponentServiceObjects =
			samlProviderResourceComponentServiceObjects;
	}

	@GraphQLField(description = "Patch the SAML Provider configuration.")
	public SamlProvider patchSamlProvider(
			@GraphQLName("samlProvider") SamlProvider samlProvider)
		throws Exception {

		return _applyComponentServiceObjects(
			_samlProviderResourceComponentServiceObjects,
			this::_populateResourceContext,
			samlProviderResource -> samlProviderResource.patchSamlProvider(
				samlProvider));
	}

	@GraphQLField(
		description = "Creates a full SAML Provider configuration with peer connections."
	)
	public SamlProvider createSamlProvider(
			@GraphQLName("samlProvider") SamlProvider samlProvider)
		throws Exception {

		return _applyComponentServiceObjects(
			_samlProviderResourceComponentServiceObjects,
			this::_populateResourceContext,
			samlProviderResource -> samlProviderResource.postSamlProvider(
				samlProvider));
	}

	@GraphQLField
	public Response createSamlProviderBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_samlProviderResourceComponentServiceObjects,
			this::_populateResourceContext,
			samlProviderResource -> samlProviderResource.postSamlProviderBatch(
				callbackURL, object));
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

	private void _populateResourceContext(
			SamlProviderResource samlProviderResource)
		throws Exception {

		samlProviderResource.setContextAcceptLanguage(_acceptLanguage);
		samlProviderResource.setContextCompany(_company);
		samlProviderResource.setContextHttpServletRequest(_httpServletRequest);
		samlProviderResource.setContextHttpServletResponse(
			_httpServletResponse);
		samlProviderResource.setContextUriInfo(_uriInfo);
		samlProviderResource.setContextUser(_user);
		samlProviderResource.setGroupLocalService(_groupLocalService);
		samlProviderResource.setRoleLocalService(_roleLocalService);

		samlProviderResource.setVulcanBatchEngineExportTaskResource(
			_vulcanBatchEngineExportTaskResource);

		samlProviderResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private static ComponentServiceObjects<SamlProviderResource>
		_samlProviderResourceComponentServiceObjects;

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