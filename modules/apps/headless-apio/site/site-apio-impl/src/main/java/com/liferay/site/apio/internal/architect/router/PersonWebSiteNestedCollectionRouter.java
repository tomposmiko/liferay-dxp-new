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

package com.liferay.site.apio.internal.architect.router;

import com.liferay.apio.architect.router.NestedCollectionRouter;
import com.liferay.person.apio.architect.identifier.PersonIdentifier;
import com.liferay.site.apio.architect.identifier.WebSiteIdentifier;
import com.liferay.site.apio.internal.architect.router.base.BaseUserAccountWebSiteNestedCollectionRouter;
import com.liferay.site.apio.internal.model.GroupWrapper;

import org.osgi.service.component.annotations.Component;

/**
 * Provides the information necessary to expose the <a
 * href="http://schema.org/WebSite">WebSite</a> resources of a <a
 * href="http://schema.org/Person">Person</a> through a web API. The resources
 * are mapped from the internal model {@link GroupWrapper}.
 *
 * @author Eduardo Perez
 */
@Component(immediate = true, service = NestedCollectionRouter.class)
public class PersonWebSiteNestedCollectionRouter
	extends BaseUserAccountWebSiteNestedCollectionRouter<PersonIdentifier>
	implements NestedCollectionRouter
		<GroupWrapper, Long, WebSiteIdentifier, Long, PersonIdentifier> {
}