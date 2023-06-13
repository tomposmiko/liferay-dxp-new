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

package com.liferay.saml.admin.rest.internal.graphql.query.v1_0;

import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.saml.admin.rest.dto.v1_0.SamlProvider;
import com.liferay.saml.admin.rest.resource.v1_0.SamlProviderResource;

import java.util.Map;
import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Stian Sigvartsen
 * @generated
 */
@Generated("")
public class Query {

	public static void setSamlProviderResourceComponentServiceObjects(
		ComponentServiceObjects<SamlProviderResource>
			samlProviderResourceComponentServiceObjects) {

		_samlProviderResourceComponentServiceObjects =
			samlProviderResourceComponentServiceObjects;
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {samlProvider{enabled, entityId, idp, keyStoreCredentialPassword, role, signMetadata, sp, sslRequired}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(description = "Retrieves the SAML Provider configuration.")
	public SamlProvider samlProvider() throws Exception {
		return _applyComponentServiceObjects(
			_samlProviderResourceComponentServiceObjects,
			this::_populateResourceContext,
			samlProviderResource -> samlProviderResource.getSamlProvider());
	}

	@GraphQLName("SamlProviderPage")
	public class SamlProviderPage {

		public SamlProviderPage(Page samlProviderPage) {
			actions = samlProviderPage.getActions();

			items = samlProviderPage.getItems();
			lastPage = samlProviderPage.getLastPage();
			page = samlProviderPage.getPage();
			pageSize = samlProviderPage.getPageSize();
			totalCount = samlProviderPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<SamlProvider> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

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
	}

	private static ComponentServiceObjects<SamlProviderResource>
		_samlProviderResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private BiFunction<Object, String, Filter> _filterBiFunction;
	private GroupLocalService _groupLocalService;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private RoleLocalService _roleLocalService;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private UriInfo _uriInfo;
	private com.liferay.portal.kernel.model.User _user;

}