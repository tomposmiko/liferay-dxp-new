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

package com.liferay.organizations.search.test;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ListTypeConstants;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.OrganizationConstants;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.CountryService;
import com.liferay.portal.kernel.service.OrganizationService;
import com.liferay.portal.kernel.service.RegionService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Igor Fabiano Nazar
 * @author Luan Maoski
 */
public class OrganizationFixture {

	public OrganizationFixture(
		OrganizationService organizationService, CountryService countryService,
		RegionService regionService, Language language) {

		_organizationService = organizationService;
		_countryService = countryService;
		_regionService = regionService;
		_language = language;
	}

	public Organization createOrganization(String organizationName)
		throws Exception {

		return createOrganization(organizationName, "united-states", "Alabama");
	}

	public Organization createOrganization(
			String organizationName, Map<String, Serializable> expandoValues)
		throws Exception {

		return createOrganization(
			organizationName, "united-states", "Alabama", expandoValues);
	}

	public Organization createOrganization(
			String organizationName, String countryName, String regionName)
		throws Exception, PortalException {

		return createOrganization(
			organizationName, countryName, regionName, null);
	}

	public Organization createOrganization(
			String organizationName, String countryName, String regionName,
			Map<String, Serializable> expando)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), getUserId());

		Country country = _countryService.getCountryByName(
			_group.getCompanyId(), countryName);

		Region region = _getRegion(regionName, country);

		if (expando != null) {
			serviceContext.setExpandoBridgeAttributes(expando);
		}

		Organization organization = _organizationService.addOrganization(
			OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID,
			organizationName, OrganizationConstants.TYPE_ORGANIZATION,
			region.getRegionId(), country.getCountryId(),
			ListTypeConstants.ORGANIZATION_STATUS_DEFAULT,
			RandomTestUtil.randomString(), RandomTestUtil.randomBoolean(),
			serviceContext);

		_organizatons.add(organization);

		return organization;
	}

	public List<String> getCountryNames(Organization organization) {
		Set<String> countryNames = new HashSet<>();

		Country country = _countryService.fetchCountry(
			organization.getCountryId());

		for (Locale locale : _language.getAvailableLocales()) {
			countryNames.add(StringUtil.toLowerCase(country.getName(locale)));
		}

		return new ArrayList<>(countryNames);
	}

	public List<Organization> getOrganizations() {
		return _organizatons;
	}

	public void setGroup(Group group) {
		_group = group;
	}

	public void setUp() throws Exception {
		UserTestUtil.setUser(TestPropsValues.getUser());

		CompanyThreadLocal.setCompanyId(TestPropsValues.getCompanyId());
	}

	public void updateDisplaySettings(Locale locale) throws Exception {
		Group group = GroupTestUtil.updateDisplaySettings(
			_group.getGroupId(), null, locale);

		_group.setModelAttributes(group.getModelAttributes());
	}

	protected long getUserId() throws Exception {
		return TestPropsValues.getUserId();
	}

	private Region _getRegion(String regionName, Country country) {
		for (Region region :
				_regionService.getRegions(country.getCountryId())) {

			if (StringUtil.equalsIgnoreCase(regionName, region.getName())) {
				return region;
			}
		}

		return null;
	}

	private final CountryService _countryService;
	private Group _group;
	private final Language _language;
	private final OrganizationService _organizationService;
	private final List<Organization> _organizatons = new ArrayList<>();
	private final RegionService _regionService;

}