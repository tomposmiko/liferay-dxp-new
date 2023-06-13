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

package com.liferay.analytics.settings.rest.internal.resource.v1_0;

import com.liferay.analytics.settings.configuration.AnalyticsConfiguration;
import com.liferay.analytics.settings.rest.dto.v1_0.ContactOrganization;
import com.liferay.analytics.settings.rest.internal.dto.v1_0.converter.ContactOrganizationDTOConverterContext;
import com.liferay.analytics.settings.rest.manager.AnalyticsSettingsManager;
import com.liferay.analytics.settings.rest.resource.v1_0.ContactOrganizationResource;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.OrganizationConstants;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Riccardo Ferrari
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/contact-organization.properties",
	scope = ServiceScope.PROTOTYPE, service = ContactOrganizationResource.class
)
public class ContactOrganizationResourceImpl
	extends BaseContactOrganizationResourceImpl {

	@Override
	public Page<ContactOrganization> getContactOrganizationsPage(
			String keywords, Pagination pagination, Sort[] sorts)
		throws Exception {

		AnalyticsConfiguration analyticsConfiguration =
			_analyticsSettingsManager.getAnalyticsConfiguration(
				contextCompany.getCompanyId());

		if (sorts == null) {
			sorts = new Sort[] {new Sort("name", Sort.STRING_TYPE, false)};
		}

		Sort sort = sorts[0];

		BaseModelSearchResult<Organization> organizationBaseModelSearchResult =
			_organizationLocalService.searchOrganizations(
				contextCompany.getCompanyId(),
				OrganizationConstants.ANY_PARENT_ORGANIZATION_ID, keywords,
				null, pagination.getStartPosition(),
				pagination.getEndPosition(), sort);

		return Page.of(
			transform(
				organizationBaseModelSearchResult.getBaseModels(),
				organization -> _contactOrganizationDTOConverter.toDTO(
					new ContactOrganizationDTOConverterContext(
						organization.getOrganizationId(),
						contextAcceptLanguage.getPreferredLocale(),
						analyticsConfiguration.syncedOrganizationIds()),
					organization)),
			pagination, organizationBaseModelSearchResult.getLength());
	}

	@Reference
	private AnalyticsSettingsManager _analyticsSettingsManager;

	@Reference(
		target = "(component.name=com.liferay.analytics.settings.rest.internal.dto.v1_0.converter.ContactOrganizationDTOConverter)"
	)
	private DTOConverter<Organization, ContactOrganization>
		_contactOrganizationDTOConverter;

	@Reference
	private OrganizationLocalService _organizationLocalService;

}