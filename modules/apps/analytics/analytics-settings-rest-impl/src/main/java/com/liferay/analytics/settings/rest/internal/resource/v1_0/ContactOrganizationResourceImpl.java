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
import com.liferay.analytics.settings.rest.internal.dto.v1_0.converter.ContactOrganizationDTOConverter;
import com.liferay.analytics.settings.rest.internal.dto.v1_0.converter.ContactOrganizationDTOConverterContext;
import com.liferay.analytics.settings.rest.manager.AnalyticsSettingsManager;
import com.liferay.analytics.settings.rest.resource.v1_0.ContactOrganizationResource;
import com.liferay.portal.kernel.model.OrganizationConstants;
import com.liferay.portal.kernel.model.OrganizationTable;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.LinkedHashMap;

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

		return Page.of(
			transform(
				_organizationLocalService.search(
					contextCompany.getCompanyId(),
					OrganizationConstants.ANY_PARENT_ORGANIZATION_ID, keywords,
					null, null, null, _getParams(),
					pagination.getStartPosition(), pagination.getEndPosition(),
					OrderByComparatorFactoryUtil.create(
						OrganizationTable.INSTANCE.getTableName(),
						sort.getFieldName(), !sort.isReverse())),
				organization -> _contactOrganizationDTOConverter.toDTO(
					new ContactOrganizationDTOConverterContext(
						organization.getOrganizationId(),
						contextAcceptLanguage.getPreferredLocale(),
						analyticsConfiguration.syncedOrganizationIds()),
					organization)),
			pagination,
			_organizationLocalService.searchCount(
				contextCompany.getCompanyId(),
				OrganizationConstants.ANY_PARENT_ORGANIZATION_ID, keywords,
				null, null, null, _getParams()));
	}

	private LinkedHashMap<String, Object> _getParams() {
		return LinkedHashMapBuilder.<String, Object>put(
			"active", Boolean.TRUE
		).build();
	}

	@Reference
	private AnalyticsSettingsManager _analyticsSettingsManager;

	@Reference
	private ContactOrganizationDTOConverter _contactOrganizationDTOConverter;

	@Reference
	private OrganizationLocalService _organizationLocalService;

}