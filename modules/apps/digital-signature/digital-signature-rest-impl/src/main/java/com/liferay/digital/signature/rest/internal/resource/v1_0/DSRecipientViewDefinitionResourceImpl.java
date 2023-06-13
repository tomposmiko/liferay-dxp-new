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

package com.liferay.digital.signature.rest.internal.resource.v1_0;

import com.liferay.digital.signature.manager.DSRecipientViewDefinitionManager;
import com.liferay.digital.signature.rest.dto.v1_0.DSEnvelopeSignatureURL;
import com.liferay.digital.signature.rest.dto.v1_0.DSRecipientViewDefinition;
import com.liferay.digital.signature.rest.resource.v1_0.DSRecipientViewDefinitionResource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author José Abelenda
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/ds-recipient-view-definition.properties",
	scope = ServiceScope.PROTOTYPE,
	service = DSRecipientViewDefinitionResource.class
)
public class DSRecipientViewDefinitionResourceImpl
	extends BaseDSRecipientViewDefinitionResourceImpl {

	@Override
	public DSEnvelopeSignatureURL postSiteDSRecipientViewDefinition(
			Long siteId, String dsEnvelopeId,
			DSRecipientViewDefinition dsRecipientViewDefinition)
		throws Exception {

		return new DSEnvelopeSignatureURL() {
			{
				url =
					_dsRecipientViewDefinitionManager.
						addDSRecipientViewDefinition(
							contextCompany.getCompanyId(), siteId, dsEnvelopeId,
							_toDSRecipientViewDefinition(
								dsRecipientViewDefinition));
			}
		};
	}

	private com.liferay.digital.signature.model.DSRecipientViewDefinition
		_toDSRecipientViewDefinition(
			DSRecipientViewDefinition dsRecipientViewDefinition) {

		return new com.liferay.digital.signature.model.
			DSRecipientViewDefinition() {

			{
				authenticationMethod =
					dsRecipientViewDefinition.getAuthenticationMethod();
				dsClientUserId = dsRecipientViewDefinition.getDsClientUserId();
				emailAddress = dsRecipientViewDefinition.getEmailAddress();
				returnURL = dsRecipientViewDefinition.getReturnURL();
				userName = dsRecipientViewDefinition.getUserName();
			}
		};
	}

	@Reference
	private DSRecipientViewDefinitionManager _dsRecipientViewDefinitionManager;

}