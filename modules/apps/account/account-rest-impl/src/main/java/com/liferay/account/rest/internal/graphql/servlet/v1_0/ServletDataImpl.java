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

package com.liferay.account.rest.internal.graphql.servlet.v1_0;

import com.liferay.account.rest.internal.graphql.mutation.v1_0.Mutation;
import com.liferay.account.rest.internal.graphql.query.v1_0.Query;
import com.liferay.account.rest.resource.v1_0.AccountResource;
import com.liferay.account.rest.resource.v1_0.AccountRoleResource;
import com.liferay.account.rest.resource.v1_0.AccountUserResource;
import com.liferay.portal.vulcan.graphql.servlet.ServletData;

import javax.annotation.Generated;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author Drew Brokke
 * @generated
 */
@Component(immediate = true, service = ServletData.class)
@Generated("")
public class ServletDataImpl implements ServletData {

	@Activate
	public void activate(BundleContext bundleContext) {
		Mutation.setAccountResourceComponentServiceObjects(
			_accountResourceComponentServiceObjects);
		Mutation.setAccountRoleResourceComponentServiceObjects(
			_accountRoleResourceComponentServiceObjects);
		Mutation.setAccountUserResourceComponentServiceObjects(
			_accountUserResourceComponentServiceObjects);

		Query.setAccountResourceComponentServiceObjects(
			_accountResourceComponentServiceObjects);
		Query.setAccountRoleResourceComponentServiceObjects(
			_accountRoleResourceComponentServiceObjects);
		Query.setAccountUserResourceComponentServiceObjects(
			_accountUserResourceComponentServiceObjects);
	}

	@Override
	public Mutation getMutation() {
		return new Mutation();
	}

	@Override
	public String getPath() {
		return "/account-rest-graphql/v1_0";
	}

	@Override
	public Query getQuery() {
		return new Query();
	}

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<AccountResource>
		_accountResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<AccountRoleResource>
		_accountRoleResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<AccountUserResource>
		_accountUserResourceComponentServiceObjects;

}