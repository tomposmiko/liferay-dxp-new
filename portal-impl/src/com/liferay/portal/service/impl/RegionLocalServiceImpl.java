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

package com.liferay.portal.service.impl;

import com.liferay.petra.sql.dsl.DSLFunctionFactoryUtil;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.petra.sql.dsl.query.FromStep;
import com.liferay.petra.sql.dsl.query.JoinStep;
import com.liferay.petra.sql.dsl.query.OrderByStep;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.RegionCodeException;
import com.liferay.portal.kernel.exception.RegionNameException;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.OrganizationTable;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.model.RegionLocalizationTable;
import com.liferay.portal.kernel.model.RegionTable;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.service.AddressLocalService;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.persistence.CountryPersistence;
import com.liferay.portal.kernel.service.persistence.OrganizationPersistence;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.base.RegionLocalServiceBaseImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public class RegionLocalServiceImpl extends RegionLocalServiceBaseImpl {

	@Override
	public Region addRegion(
			long countryId, boolean active, String name, double position,
			String regionCode, ServiceContext serviceContext)
		throws PortalException {

		_countryPersistence.findByPrimaryKey(countryId);

		validate(name, regionCode);

		long regionId = counterLocalService.increment();

		Region region = regionPersistence.create(regionId);

		region.setCompanyId(serviceContext.getCompanyId());

		User user = _userLocalService.getUser(serviceContext.getUserId());

		region.setUserId(user.getUserId());
		region.setUserName(user.getFullName());

		region.setCountryId(countryId);
		region.setActive(active);
		region.setName(name);
		region.setPosition(position);
		region.setRegionCode(regionCode);

		return regionPersistence.update(region);
	}

	@Override
	public void deleteCountryRegions(long countryId) {
		for (Region region :
				getRegions(
					countryId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			deleteRegion(region);
		}
	}

	@Override
	public Region deleteRegion(long regionId) throws PortalException {
		Region region = regionPersistence.findByPrimaryKey(regionId);

		return deleteRegion(region);
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public Region deleteRegion(Region region) {

		// Region

		regionPersistence.remove(region);

		// Address

		_addressLocalService.deleteRegionAddresses(region.getRegionId());

		// Organizations

		for (Organization organization :
				_organizationPersistence.<List<Organization>>dslQuery(
					DSLQueryFactoryUtil.select(
						OrganizationTable.INSTANCE
					).from(
						OrganizationTable.INSTANCE
					).where(
						OrganizationTable.INSTANCE.regionId.eq(
							region.getRegionId())
					))) {

			organization.setRegionId(0);

			_organizationLocalService.updateOrganization(organization);
		}

		return region;
	}

	@Override
	public Region fetchRegion(long countryId, String regionCode) {
		return regionPersistence.fetchByC_R(countryId, regionCode);
	}

	@Override
	public Region getRegion(long countryId, String regionCode)
		throws PortalException {

		return regionPersistence.findByC_R(countryId, regionCode);
	}

	@Override
	public List<Region> getRegions(long countryId, boolean active)
		throws PortalException {

		return regionPersistence.findByC_A(countryId, active);
	}

	@Override
	public List<Region> getRegions(
		long countryId, boolean active, int start, int end,
		OrderByComparator<Region> orderByComparator) {

		return regionPersistence.findByC_A(
			countryId, active, start, end, orderByComparator);
	}

	@Override
	public List<Region> getRegions(
		long countryId, int start, int end,
		OrderByComparator<Region> orderByComparator) {

		return regionPersistence.findByCountryId(
			countryId, start, end, orderByComparator);
	}

	@Override
	public List<Region> getRegions(long companyId, String a2, boolean active)
		throws PortalException {

		Country country = _countryPersistence.findByC_A2(companyId, a2);

		return regionPersistence.findByC_A(country.getCountryId(), active);
	}

	@Override
	public int getRegionsCount(long countryId) {
		return regionPersistence.countByCountryId(countryId);
	}

	@Override
	public int getRegionsCount(long countryId, boolean active) {
		return regionPersistence.countByC_A(countryId, active);
	}

	@Override
	public BaseModelSearchResult<Region> searchRegions(
			long companyId, Boolean active, String keywords,
			LinkedHashMap<String, Object> params, int start, int end,
			OrderByComparator<Region> orderByComparator)
		throws PortalException {

		return BaseModelSearchResult.unsafeCreateWithStartAndEnd(
			startAndEnd -> regionPersistence.dslQuery(
				_getGroupByStep(
					DSLQueryFactoryUtil.selectDistinct(RegionTable.INSTANCE),
					companyId, active, keywords, params
				).orderBy(
					RegionTable.INSTANCE, orderByComparator
				).limit(
					startAndEnd.getStart(), startAndEnd.getEnd()
				)),
			regionPersistence.dslQueryCount(
				_getGroupByStep(
					DSLQueryFactoryUtil.countDistinct(
						RegionTable.INSTANCE.regionId),
					companyId, active, keywords, params)),
			start, end);
	}

	@Override
	public Region updateActive(long regionId, boolean active)
		throws PortalException {

		Region region = regionPersistence.findByPrimaryKey(regionId);

		region.setActive(active);

		return regionPersistence.update(region);
	}

	@Override
	public Region updateRegion(
			long regionId, boolean active, String name, double position,
			String regionCode)
		throws PortalException {

		Region region = regionPersistence.findByPrimaryKey(regionId);

		validate(name, regionCode);

		region.setActive(active);
		region.setName(name);
		region.setPosition(position);
		region.setRegionCode(regionCode);

		return regionPersistence.update(region);
	}

	protected void validate(String name, String regionCode)
		throws PortalException {

		if (Validator.isNull(regionCode)) {
			throw new RegionCodeException();
		}

		if (Validator.isNull(name)) {
			throw new RegionNameException();
		}
	}

	private OrderByStep _getGroupByStep(
		FromStep fromStep, long companyId, Boolean active, String keywords,
		LinkedHashMap<String, Object> params) {

		JoinStep joinStep = fromStep.from(
			RegionTable.INSTANCE
		).leftJoinOn(
			RegionLocalizationTable.INSTANCE,
			RegionTable.INSTANCE.regionId.eq(
				RegionLocalizationTable.INSTANCE.regionId)
		);

		return joinStep.where(
			() -> {
				Predicate predicate = RegionTable.INSTANCE.companyId.eq(
					companyId);

				if (active != null) {
					predicate = predicate.and(
						RegionTable.INSTANCE.active.eq(active));
				}

				if (Validator.isNotNull(keywords)) {
					String[] terms = CustomSQLUtil.keywords(keywords, true);

					Predicate keywordsPredicate = null;

					for (String term : terms) {
						Predicate namePredicate = DSLFunctionFactoryUtil.lower(
							RegionTable.INSTANCE.name
						).like(
							term
						);

						Predicate titlePredicate = DSLFunctionFactoryUtil.lower(
							RegionLocalizationTable.INSTANCE.title
						).like(
							term
						);

						Predicate termPredicate = namePredicate.or(
							titlePredicate);

						if (keywordsPredicate == null) {
							keywordsPredicate = termPredicate;
						}
						else {
							keywordsPredicate = keywordsPredicate.or(
								termPredicate);
						}
					}

					if (keywordsPredicate != null) {
						predicate = predicate.and(
							keywordsPredicate.withParentheses());
					}
				}

				if (MapUtil.isNotEmpty(params)) {
					long countryId = (long)params.get("countryId");

					if (countryId > 0) {
						predicate = predicate.and(
							RegionTable.INSTANCE.countryId.eq(countryId));
					}
				}

				return predicate;
			});
	}

	@BeanReference(type = AddressLocalService.class)
	private AddressLocalService _addressLocalService;

	@BeanReference(type = CountryPersistence.class)
	private CountryPersistence _countryPersistence;

	@BeanReference(type = OrganizationLocalService.class)
	private OrganizationLocalService _organizationLocalService;

	@BeanReference(type = OrganizationPersistence.class)
	private OrganizationPersistence _organizationPersistence;

	@BeanReference(type = UserLocalService.class)
	private UserLocalService _userLocalService;

}