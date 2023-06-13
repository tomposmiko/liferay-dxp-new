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

package com.liferay.commerce.inventory.service.impl;

import com.liferay.commerce.inventory.exception.CommerceInventoryWarehouseActiveException;
import com.liferay.commerce.inventory.exception.CommerceInventoryWarehouseNameException;
import com.liferay.commerce.inventory.exception.DuplicateCommerceInventoryWarehouseException;
import com.liferay.commerce.inventory.exception.MVCCException;
import com.liferay.commerce.inventory.internal.search.CommerceInventoryWarehouseIndexer;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.service.CommerceInventoryReplenishmentItemLocalService;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseItemLocalService;
import com.liferay.commerce.inventory.service.base.CommerceInventoryWarehouseLocalServiceBaseImpl;
import com.liferay.expando.kernel.service.ExpandoRowLocalService;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 * @author Alessio Antonio Rendina
 */
@Component(
	property = "model.class.name=com.liferay.commerce.inventory.model.CommerceInventoryWarehouse",
	service = AopService.class
)
public class CommerceInventoryWarehouseLocalServiceImpl
	extends CommerceInventoryWarehouseLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceInventoryWarehouse addCommerceInventoryWarehouse(
			String externalReferenceCode, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, boolean active, String street1,
			String street2, String street3, String city, String zip,
			String commerceRegionCode, String commerceCountryCode,
			double latitude, double longitude, ServiceContext serviceContext)
		throws PortalException {

		User user = _userLocalService.getUser(serviceContext.getUserId());

		_validateExternalReferenceCode(
			0, user.getCompanyId(), externalReferenceCode);

		_validateNameMap(nameMap);
		_validateActive(active, latitude, longitude);

		CommerceInventoryWarehouse commerceInventoryWarehouse =
			commerceInventoryWarehousePersistence.create(
				counterLocalService.increment());

		commerceInventoryWarehouse.setExternalReferenceCode(
			externalReferenceCode);
		commerceInventoryWarehouse.setCompanyId(user.getCompanyId());
		commerceInventoryWarehouse.setUserId(user.getUserId());
		commerceInventoryWarehouse.setUserName(user.getFullName());
		commerceInventoryWarehouse.setNameMap(nameMap);
		commerceInventoryWarehouse.setDescriptionMap(descriptionMap);
		commerceInventoryWarehouse.setActive(active);
		commerceInventoryWarehouse.setStreet1(street1);
		commerceInventoryWarehouse.setStreet2(street2);
		commerceInventoryWarehouse.setStreet3(street3);
		commerceInventoryWarehouse.setCity(city);
		commerceInventoryWarehouse.setZip(zip);
		commerceInventoryWarehouse.setCommerceRegionCode(commerceRegionCode);
		commerceInventoryWarehouse.setCountryTwoLettersISOCode(
			commerceCountryCode);
		commerceInventoryWarehouse.setLatitude(latitude);
		commerceInventoryWarehouse.setLongitude(longitude);
		commerceInventoryWarehouse.setExpandoBridgeAttributes(serviceContext);

		commerceInventoryWarehouse =
			commerceInventoryWarehousePersistence.update(
				commerceInventoryWarehouse);

		Company company = _companyLocalService.getCompany(user.getCompanyId());

		_resourceLocalService.addResources(
			user.getCompanyId(), company.getGroupId(), user.getUserId(),
			CommerceInventoryWarehouse.class.getName(),
			commerceInventoryWarehouse.getCommerceInventoryWarehouseId(), false,
			true, true);

		return commerceInventoryWarehouse;
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public CommerceInventoryWarehouse deleteCommerceInventoryWarehouse(
			CommerceInventoryWarehouse commerceInventoryWarehouse)
		throws PortalException {

		commerceInventoryWarehouse =
			commerceInventoryWarehousePersistence.remove(
				commerceInventoryWarehouse);

		_commerceInventoryWarehouseItemLocalService.
			deleteCommerceInventoryWarehouseItems(
				commerceInventoryWarehouse.getCommerceInventoryWarehouseId());

		_commerceInventoryReplenishmentItemLocalService.
			deleteCommerceInventoryReplenishmentItems(
				commerceInventoryWarehouse.getCommerceInventoryWarehouseId());

		_expandoRowLocalService.deleteRows(
			commerceInventoryWarehouse.getCommerceInventoryWarehouseId());

		_resourceLocalService.deleteResource(
			commerceInventoryWarehouse, ResourceConstants.SCOPE_INDIVIDUAL);

		return commerceInventoryWarehouse;
	}

	@Override
	public CommerceInventoryWarehouse deleteCommerceInventoryWarehouse(
			long commerceInventoryWarehouseId)
		throws PortalException {

		CommerceInventoryWarehouse commerceInventoryWarehouse =
			commerceInventoryWarehousePersistence.findByPrimaryKey(
				commerceInventoryWarehouseId);

		return commerceInventoryWarehouseLocalService.
			deleteCommerceInventoryWarehouse(commerceInventoryWarehouse);
	}

	@Override
	public CommerceInventoryWarehouse
		fetchCommerceInventoryWarehouseByReferenceCode(
			String externalReferenceCode, long companyId) {

		return commerceInventoryWarehousePersistence.fetchByERC_C(
			externalReferenceCode, companyId);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceInventoryWarehouse geolocateCommerceInventoryWarehouse(
			long commerceInventoryWarehouseId, double latitude,
			double longitude)
		throws PortalException {

		CommerceInventoryWarehouse commerceInventoryWarehouse =
			commerceInventoryWarehousePersistence.findByPrimaryKey(
				commerceInventoryWarehouseId);

		commerceInventoryWarehouse.setLatitude(latitude);
		commerceInventoryWarehouse.setLongitude(longitude);

		return commerceInventoryWarehousePersistence.update(
			commerceInventoryWarehouse);
	}

	@Override
	public List<CommerceInventoryWarehouse> getCommerceInventoryWarehouses(
		long companyId) {

		return commerceInventoryWarehousePersistence.findByCompanyId(companyId);
	}

	@Override
	public List<CommerceInventoryWarehouse> getCommerceInventoryWarehouses(
		long companyId, boolean active, int start, int end,
		OrderByComparator<CommerceInventoryWarehouse> orderByComparator) {

		return commerceInventoryWarehousePersistence.findByC_A(
			companyId, active, start, end, orderByComparator);
	}

	@Override
	public List<CommerceInventoryWarehouse> getCommerceInventoryWarehouses(
		long companyId, boolean active, String commerceCountryCode, int start,
		int end,
		OrderByComparator<CommerceInventoryWarehouse> orderByComparator) {

		return commerceInventoryWarehousePersistence.findByC_A_C(
			companyId, active, commerceCountryCode, start, end,
			orderByComparator);
	}

	@Override
	public List<CommerceInventoryWarehouse> getCommerceInventoryWarehouses(
		long companyId, int start, int end,
		OrderByComparator<CommerceInventoryWarehouse> orderByComparator) {

		return commerceInventoryWarehousePersistence.findByCompanyId(
			companyId, start, end, orderByComparator);
	}

	@Override
	public List<CommerceInventoryWarehouse> getCommerceInventoryWarehouses(
		long companyId, long groupId, boolean active) {

		return commerceInventoryWarehouseFinder.findByC_G_A(
			companyId, groupId, active);
	}

	@Override
	public List<CommerceInventoryWarehouse> getCommerceInventoryWarehouses(
		long groupId, String sku) {

		return commerceInventoryWarehouseFinder.findByG_S(groupId, sku);
	}

	@Override
	public int getCommerceInventoryWarehousesCount(long companyId) {
		return commerceInventoryWarehousePersistence.countByCompanyId(
			companyId);
	}

	@Override
	public int getCommerceInventoryWarehousesCount(
		long companyId, boolean active) {

		return commerceInventoryWarehousePersistence.countByC_A(
			companyId, active);
	}

	@Override
	public int getCommerceInventoryWarehousesCount(
		long companyId, boolean active, String commerceCountryCode) {

		if (Validator.isNotNull(commerceCountryCode)) {
			return commerceInventoryWarehousePersistence.countByC_A_C(
				companyId, active, commerceCountryCode);
		}

		return commerceInventoryWarehouseLocalService.
			getCommerceInventoryWarehousesCount(companyId, active);
	}

	@Override
	public List<CommerceInventoryWarehouse> search(
			long companyId, Boolean active, String commerceCountryCode,
			String keywords, int start, int end, Sort sort)
		throws PortalException {

		SearchContext searchContext = _buildSearchContext(
			companyId, active, commerceCountryCode, keywords, start, end, sort);

		searchContext.setKeywords(keywords);

		return _search(searchContext);
	}

	@Override
	public int searchCommerceInventoryWarehousesCount(
			long companyId, Boolean active, String commerceCountryCode,
			String keywords)
		throws PortalException {

		SearchContext searchContext = _buildSearchContext(
			companyId, active, commerceCountryCode, keywords, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);

		searchContext.setKeywords(keywords);

		return _searchCommerceInventoryWarehousesCount(searchContext);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceInventoryWarehouse setActive(
			long commerceInventoryWarehouseId, boolean active)
		throws PortalException {

		CommerceInventoryWarehouse commerceInventoryWarehouse =
			commerceInventoryWarehousePersistence.findByPrimaryKey(
				commerceInventoryWarehouseId);

		_validateActive(
			active, commerceInventoryWarehouse.getLatitude(),
			commerceInventoryWarehouse.getLongitude());

		commerceInventoryWarehouse.setActive(active);

		return commerceInventoryWarehousePersistence.update(
			commerceInventoryWarehouse);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceInventoryWarehouse updateCommerceInventoryWarehouse(
			long commerceInventoryWarehouseId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, boolean active, String street1,
			String street2, String street3, String city, String zip,
			String commerceRegionCode, String commerceCountryCode,
			double latitude, double longitude, long mvccVersion,
			ServiceContext serviceContext)
		throws PortalException {

		_validateNameMap(nameMap);
		_validateActive(active, latitude, longitude);

		CommerceInventoryWarehouse commerceInventoryWarehouse =
			commerceInventoryWarehousePersistence.findByPrimaryKey(
				commerceInventoryWarehouseId);

		if (commerceInventoryWarehouse.getMvccVersion() != mvccVersion) {
			throw new MVCCException();
		}

		commerceInventoryWarehouse.setNameMap(nameMap);
		commerceInventoryWarehouse.setDescriptionMap(descriptionMap);
		commerceInventoryWarehouse.setActive(active);
		commerceInventoryWarehouse.setStreet1(street1);
		commerceInventoryWarehouse.setStreet2(street2);
		commerceInventoryWarehouse.setStreet3(street3);
		commerceInventoryWarehouse.setCity(city);
		commerceInventoryWarehouse.setZip(zip);
		commerceInventoryWarehouse.setCommerceRegionCode(commerceRegionCode);
		commerceInventoryWarehouse.setCountryTwoLettersISOCode(
			commerceCountryCode);
		commerceInventoryWarehouse.setLatitude(latitude);
		commerceInventoryWarehouse.setLongitude(longitude);
		commerceInventoryWarehouse.setExpandoBridgeAttributes(serviceContext);

		return commerceInventoryWarehousePersistence.update(
			commerceInventoryWarehouse);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceInventoryWarehouse
			updateCommerceInventoryWarehouseExternalReferenceCode(
				String externalReferenceCode, long commerceInventoryWarehouseId)
		throws PortalException {

		CommerceInventoryWarehouse commerceInventoryWarehouse =
			commerceInventoryWarehousePersistence.findByPrimaryKey(
				commerceInventoryWarehouseId);

		commerceInventoryWarehouse.setExternalReferenceCode(
			externalReferenceCode);

		return commerceInventoryWarehousePersistence.update(
			commerceInventoryWarehouse);
	}

	private SearchContext _buildSearchContext(
		long companyId, Boolean active, String commerceCountryCode,
		String keywords, int start, int end, Sort sort) {

		SearchContext searchContext = new SearchContext();

		searchContext.setAttributes(
			HashMapBuilder.<String, Serializable>put(
				CommerceInventoryWarehouseIndexer.FIELD_CITY, keywords
			).put(
				CommerceInventoryWarehouseIndexer.FIELD_STREET_1, keywords
			).put(
				CommerceInventoryWarehouseIndexer.FIELD_ZIP, keywords
			).put(
				Field.DESCRIPTION, keywords
			).put(
				Field.ENTRY_CLASS_PK, keywords
			).put(
				Field.NAME, keywords
			).put(
				"params",
				LinkedHashMapBuilder.<String, Object>put(
					"keywords", keywords
				).build()
			).put(
				CommerceInventoryWarehouseIndexer.FIELD_ACTIVE, () -> active
			).put(
				CommerceInventoryWarehouseIndexer.
					FIELD_COUNTRY_TWO_LETTERS_ISO_CODE,
				() -> {
					if (Validator.isNotNull(commerceCountryCode)) {
						return commerceCountryCode;
					}

					return null;
				}
			).build());
		searchContext.setCompanyId(companyId);
		searchContext.setEnd(end);

		if (sort != null) {
			searchContext.setSorts(sort);
		}

		searchContext.setStart(start);

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		return searchContext;
	}

	private List<CommerceInventoryWarehouse> _getCommerceInventoryWarehouses(
			Hits hits)
		throws PortalException {

		List<Document> documents = hits.toList();

		List<CommerceInventoryWarehouse> commerceInventoryWarehouses =
			new ArrayList<>(documents.size());

		for (Document document : documents) {
			long commerceInventoryWarehouseId = GetterUtil.getLong(
				document.get(Field.ENTRY_CLASS_PK));

			CommerceInventoryWarehouse commerceInventoryWarehouse =
				commerceInventoryWarehouseLocalService.
					fetchCommerceInventoryWarehouse(
						commerceInventoryWarehouseId);

			if (commerceInventoryWarehouse == null) {
				commerceInventoryWarehouses = null;

				Indexer<CommerceInventoryWarehouse> indexer =
					IndexerRegistryUtil.getIndexer(
						CommerceInventoryWarehouse.class);

				long companyId = GetterUtil.getLong(
					document.get(Field.COMPANY_ID));

				indexer.delete(companyId, document.getUID());
			}
			else if (commerceInventoryWarehouses != null) {
				commerceInventoryWarehouses.add(commerceInventoryWarehouse);
			}
		}

		return commerceInventoryWarehouses;
	}

	private List<CommerceInventoryWarehouse> _search(
			SearchContext searchContext)
		throws PortalException {

		Indexer<CommerceInventoryWarehouse> indexer =
			IndexerRegistryUtil.nullSafeGetIndexer(
				CommerceInventoryWarehouse.class);

		for (int i = 0; i < 10; i++) {
			Hits hits = indexer.search(searchContext, _SELECTED_FIELD_NAMES);

			List<CommerceInventoryWarehouse> commerceInventoryWarehouses =
				_getCommerceInventoryWarehouses(hits);

			if (commerceInventoryWarehouses != null) {
				return commerceInventoryWarehouses;
			}
		}

		throw new SearchException(
			"Unable to fix the search index after 10 attempts");
	}

	private int _searchCommerceInventoryWarehousesCount(
			SearchContext searchContext)
		throws PortalException {

		Indexer<CommerceInventoryWarehouse> indexer =
			IndexerRegistryUtil.nullSafeGetIndexer(
				CommerceInventoryWarehouse.class);

		return GetterUtil.getInteger(indexer.searchCount(searchContext));
	}

	private void _validateActive(
			boolean active, double latitude, double longitude)
		throws PortalException {

		if (active && (latitude == 0) && (longitude == 0)) {
			throw new CommerceInventoryWarehouseActiveException();
		}
	}

	private void _validateExternalReferenceCode(
			long commerceInventoryWarehouseId, long companyId,
			String externalReferenceCode)
		throws PortalException {

		if (Validator.isNull(externalReferenceCode)) {
			return;
		}

		CommerceInventoryWarehouse commerceInventoryWarehouse =
			commerceInventoryWarehousePersistence.fetchByERC_C(
				externalReferenceCode, companyId);

		if (commerceInventoryWarehouse == null) {
			return;
		}

		if (commerceInventoryWarehouse.getCommerceInventoryWarehouseId() !=
				commerceInventoryWarehouseId) {

			throw new DuplicateCommerceInventoryWarehouseException(
				"There is another commerce inventory with external reference " +
					"code " + externalReferenceCode);
		}
	}

	private void _validateNameMap(Map<Locale, String> nameMap)
		throws PortalException {

		if ((nameMap == null) || nameMap.isEmpty()) {
			throw new CommerceInventoryWarehouseNameException();
		}
	}

	private static final String[] _SELECTED_FIELD_NAMES = {
		Field.ENTRY_CLASS_PK, Field.COMPANY_ID
	};

	@Reference
	private CommerceInventoryReplenishmentItemLocalService
		_commerceInventoryReplenishmentItemLocalService;

	@Reference
	private CommerceInventoryWarehouseItemLocalService
		_commerceInventoryWarehouseItemLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ExpandoRowLocalService _expandoRowLocalService;

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private UserLocalService _userLocalService;

}