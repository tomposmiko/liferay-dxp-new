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

package com.liferay.organization.apio.internal.architect.router;

import com.liferay.apio.architect.router.NestedCollectionRouter;
import com.liferay.organization.apio.architect.identifier.OrganizationIdentifier;
import com.liferay.organization.apio.internal.architect.router.base.BaseUserAccountOrganizationNestedCollectionRouter;
import com.liferay.person.apio.architect.identifier.PersonIdentifier;
import com.liferay.portal.kernel.model.Organization;

import org.osgi.service.component.annotations.Component;

/**
 * Provides the information necessary to expose the <a
 * href="http://schema.org/Organization">Organization</a> resources of a <a
 * href="http://schema.org/Person">Person</a> through a web API. The resources
 * are mapped from the internal model {@code Organization}.
 *
 * @author Eduardo Perez
 */
@Component(immediate = true, service = NestedCollectionRouter.class)
public class PersonUserAccountOrganizationNestedCollectionRouter
	extends BaseUserAccountOrganizationNestedCollectionRouter<PersonIdentifier>
	implements NestedCollectionRouter
		<Organization, Long, OrganizationIdentifier, Long, PersonIdentifier> {
}