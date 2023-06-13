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

package com.liferay.analytics.settings.rest.internal.graphql.mutation.v1_0;

import com.liferay.analytics.settings.rest.dto.v1_0.Channel;
import com.liferay.analytics.settings.rest.dto.v1_0.ContactConfiguration;
import com.liferay.analytics.settings.rest.dto.v1_0.DataSourceToken;
import com.liferay.analytics.settings.rest.dto.v1_0.Field;
import com.liferay.analytics.settings.rest.resource.v1_0.ChannelResource;
import com.liferay.analytics.settings.rest.resource.v1_0.ContactConfigurationResource;
import com.liferay.analytics.settings.rest.resource.v1_0.DataSourceResource;
import com.liferay.analytics.settings.rest.resource.v1_0.FieldResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Riccardo Ferrari
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setChannelResourceComponentServiceObjects(
		ComponentServiceObjects<ChannelResource>
			channelResourceComponentServiceObjects) {

		_channelResourceComponentServiceObjects =
			channelResourceComponentServiceObjects;
	}

	public static void setContactConfigurationResourceComponentServiceObjects(
		ComponentServiceObjects<ContactConfigurationResource>
			contactConfigurationResourceComponentServiceObjects) {

		_contactConfigurationResourceComponentServiceObjects =
			contactConfigurationResourceComponentServiceObjects;
	}

	public static void setDataSourceResourceComponentServiceObjects(
		ComponentServiceObjects<DataSourceResource>
			dataSourceResourceComponentServiceObjects) {

		_dataSourceResourceComponentServiceObjects =
			dataSourceResourceComponentServiceObjects;
	}

	public static void setFieldResourceComponentServiceObjects(
		ComponentServiceObjects<FieldResource>
			fieldResourceComponentServiceObjects) {

		_fieldResourceComponentServiceObjects =
			fieldResourceComponentServiceObjects;
	}

	@GraphQLField
	public Channel patchChannel(@GraphQLName("channel") Channel channel)
		throws Exception {

		return _applyComponentServiceObjects(
			_channelResourceComponentServiceObjects,
			this::_populateResourceContext,
			channelResource -> channelResource.patchChannel(channel));
	}

	@GraphQLField
	public Channel createChannel(@GraphQLName("channel") Channel channel)
		throws Exception {

		return _applyComponentServiceObjects(
			_channelResourceComponentServiceObjects,
			this::_populateResourceContext,
			channelResource -> channelResource.postChannel(channel));
	}

	@GraphQLField
	public boolean updateContactConfiguration(
			@GraphQLName("contactConfiguration") ContactConfiguration
				contactConfiguration)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_contactConfigurationResourceComponentServiceObjects,
			this::_populateResourceContext,
			contactConfigurationResource ->
				contactConfigurationResource.putContactConfiguration(
					contactConfiguration));

		return true;
	}

	@GraphQLField
	public boolean deleteDataSource() throws Exception {
		_applyVoidComponentServiceObjects(
			_dataSourceResourceComponentServiceObjects,
			this::_populateResourceContext,
			dataSourceResource -> dataSourceResource.deleteDataSource());

		return true;
	}

	@GraphQLField
	public boolean createDataSource(
			@GraphQLName("dataSourceToken") DataSourceToken dataSourceToken)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_dataSourceResourceComponentServiceObjects,
			this::_populateResourceContext,
			dataSourceResource -> dataSourceResource.postDataSource(
				dataSourceToken));

		return true;
	}

	@GraphQLField
	public boolean patchFieldAccount(@GraphQLName("fields") Field[] fields)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_fieldResourceComponentServiceObjects,
			this::_populateResourceContext,
			fieldResource -> fieldResource.patchFieldAccount(fields));

		return true;
	}

	@GraphQLField
	public boolean patchFieldOrder(@GraphQLName("fields") Field[] fields)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_fieldResourceComponentServiceObjects,
			this::_populateResourceContext,
			fieldResource -> fieldResource.patchFieldOrder(fields));

		return true;
	}

	@GraphQLField
	public boolean patchFieldPeople(@GraphQLName("fields") Field[] fields)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_fieldResourceComponentServiceObjects,
			this::_populateResourceContext,
			fieldResource -> fieldResource.patchFieldPeople(fields));

		return true;
	}

	@GraphQLField
	public boolean patchFieldProduct(@GraphQLName("fields") Field[] fields)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_fieldResourceComponentServiceObjects,
			this::_populateResourceContext,
			fieldResource -> fieldResource.patchFieldProduct(fields));

		return true;
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

	private void _populateResourceContext(ChannelResource channelResource)
		throws Exception {

		channelResource.setContextAcceptLanguage(_acceptLanguage);
		channelResource.setContextCompany(_company);
		channelResource.setContextHttpServletRequest(_httpServletRequest);
		channelResource.setContextHttpServletResponse(_httpServletResponse);
		channelResource.setContextUriInfo(_uriInfo);
		channelResource.setContextUser(_user);
		channelResource.setGroupLocalService(_groupLocalService);
		channelResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(
			ContactConfigurationResource contactConfigurationResource)
		throws Exception {

		contactConfigurationResource.setContextAcceptLanguage(_acceptLanguage);
		contactConfigurationResource.setContextCompany(_company);
		contactConfigurationResource.setContextHttpServletRequest(
			_httpServletRequest);
		contactConfigurationResource.setContextHttpServletResponse(
			_httpServletResponse);
		contactConfigurationResource.setContextUriInfo(_uriInfo);
		contactConfigurationResource.setContextUser(_user);
		contactConfigurationResource.setGroupLocalService(_groupLocalService);
		contactConfigurationResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(DataSourceResource dataSourceResource)
		throws Exception {

		dataSourceResource.setContextAcceptLanguage(_acceptLanguage);
		dataSourceResource.setContextCompany(_company);
		dataSourceResource.setContextHttpServletRequest(_httpServletRequest);
		dataSourceResource.setContextHttpServletResponse(_httpServletResponse);
		dataSourceResource.setContextUriInfo(_uriInfo);
		dataSourceResource.setContextUser(_user);
		dataSourceResource.setGroupLocalService(_groupLocalService);
		dataSourceResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(FieldResource fieldResource)
		throws Exception {

		fieldResource.setContextAcceptLanguage(_acceptLanguage);
		fieldResource.setContextCompany(_company);
		fieldResource.setContextHttpServletRequest(_httpServletRequest);
		fieldResource.setContextHttpServletResponse(_httpServletResponse);
		fieldResource.setContextUriInfo(_uriInfo);
		fieldResource.setContextUser(_user);
		fieldResource.setGroupLocalService(_groupLocalService);
		fieldResource.setRoleLocalService(_roleLocalService);
	}

	private static ComponentServiceObjects<ChannelResource>
		_channelResourceComponentServiceObjects;
	private static ComponentServiceObjects<ContactConfigurationResource>
		_contactConfigurationResourceComponentServiceObjects;
	private static ComponentServiceObjects<DataSourceResource>
		_dataSourceResourceComponentServiceObjects;
	private static ComponentServiceObjects<FieldResource>
		_fieldResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private GroupLocalService _groupLocalService;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private RoleLocalService _roleLocalService;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private UriInfo _uriInfo;
	private com.liferay.portal.kernel.model.User _user;

}