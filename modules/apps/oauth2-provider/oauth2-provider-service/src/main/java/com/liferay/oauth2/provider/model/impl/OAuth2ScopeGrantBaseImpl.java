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

package com.liferay.oauth2.provider.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.oauth2.provider.model.OAuth2ScopeGrant;
import com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalServiceUtil;

/**
 * The extended model base implementation for the OAuth2ScopeGrant service. Represents a row in the &quot;OAuth2ScopeGrant&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link OAuth2ScopeGrantImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2ScopeGrantImpl
 * @see OAuth2ScopeGrant
 * @generated
 */
@ProviderType
public abstract class OAuth2ScopeGrantBaseImpl extends OAuth2ScopeGrantModelImpl
	implements OAuth2ScopeGrant {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a o auth2 scope grant model instance should use the <code>OAuth2ScopeGrant</code> interface instead.
	 */
	@Override
	public void persist() {
		if (this.isNew()) {
			OAuth2ScopeGrantLocalServiceUtil.addOAuth2ScopeGrant(this);
		}
		else {
			OAuth2ScopeGrantLocalServiceUtil.updateOAuth2ScopeGrant(this);
		}
	}
}