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

package com.liferay.oauth2.provider.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.ShardedModel;

/**
 * The base model interface for the OAuth2ScopeGrant service. Represents a row in the &quot;OAuth2ScopeGrant&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.oauth2.provider.model.impl.OAuth2ScopeGrantModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.oauth2.provider.model.impl.OAuth2ScopeGrantImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2ScopeGrant
 * @see com.liferay.oauth2.provider.model.impl.OAuth2ScopeGrantImpl
 * @see com.liferay.oauth2.provider.model.impl.OAuth2ScopeGrantModelImpl
 * @generated
 */
@ProviderType
public interface OAuth2ScopeGrantModel extends BaseModel<OAuth2ScopeGrant>,
	ShardedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a o auth2 scope grant model instance should use the {@link OAuth2ScopeGrant} interface instead.
	 */

	/**
	 * Returns the primary key of this o auth2 scope grant.
	 *
	 * @return the primary key of this o auth2 scope grant
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this o auth2 scope grant.
	 *
	 * @param primaryKey the primary key of this o auth2 scope grant
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the o auth2 scope grant ID of this o auth2 scope grant.
	 *
	 * @return the o auth2 scope grant ID of this o auth2 scope grant
	 */
	public long getOAuth2ScopeGrantId();

	/**
	 * Sets the o auth2 scope grant ID of this o auth2 scope grant.
	 *
	 * @param oAuth2ScopeGrantId the o auth2 scope grant ID of this o auth2 scope grant
	 */
	public void setOAuth2ScopeGrantId(long oAuth2ScopeGrantId);

	/**
	 * Returns the company ID of this o auth2 scope grant.
	 *
	 * @return the company ID of this o auth2 scope grant
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this o auth2 scope grant.
	 *
	 * @param companyId the company ID of this o auth2 scope grant
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the o auth2 application scope aliases ID of this o auth2 scope grant.
	 *
	 * @return the o auth2 application scope aliases ID of this o auth2 scope grant
	 */
	public long getOAuth2ApplicationScopeAliasesId();

	/**
	 * Sets the o auth2 application scope aliases ID of this o auth2 scope grant.
	 *
	 * @param oAuth2ApplicationScopeAliasesId the o auth2 application scope aliases ID of this o auth2 scope grant
	 */
	public void setOAuth2ApplicationScopeAliasesId(
		long oAuth2ApplicationScopeAliasesId);

	/**
	 * Returns the application name of this o auth2 scope grant.
	 *
	 * @return the application name of this o auth2 scope grant
	 */
	@AutoEscape
	public String getApplicationName();

	/**
	 * Sets the application name of this o auth2 scope grant.
	 *
	 * @param applicationName the application name of this o auth2 scope grant
	 */
	public void setApplicationName(String applicationName);

	/**
	 * Returns the bundle symbolic name of this o auth2 scope grant.
	 *
	 * @return the bundle symbolic name of this o auth2 scope grant
	 */
	@AutoEscape
	public String getBundleSymbolicName();

	/**
	 * Sets the bundle symbolic name of this o auth2 scope grant.
	 *
	 * @param bundleSymbolicName the bundle symbolic name of this o auth2 scope grant
	 */
	public void setBundleSymbolicName(String bundleSymbolicName);

	/**
	 * Returns the scope of this o auth2 scope grant.
	 *
	 * @return the scope of this o auth2 scope grant
	 */
	@AutoEscape
	public String getScope();

	/**
	 * Sets the scope of this o auth2 scope grant.
	 *
	 * @param scope the scope of this o auth2 scope grant
	 */
	public void setScope(String scope);
}