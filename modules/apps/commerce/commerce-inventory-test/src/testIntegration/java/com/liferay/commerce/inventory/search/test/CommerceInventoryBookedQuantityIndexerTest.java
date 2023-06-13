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

package com.liferay.commerce.inventory.search.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.account.exception.CommerceAccountTypeException;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.inventory.model.CommerceInventoryBookedQuantity;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem;
import com.liferay.commerce.inventory.service.CommerceInventoryBookedQuantityLocalService;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelRel;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalServiceUtil;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.test.util.CommerceInventoryTestUtil;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.commerce.test.util.context.TestCommerceContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SortFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.frutilla.FrutillaRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Brian I. Kim
 */
@RunWith(Arquillian.class)
public class CommerceInventoryBookedQuantityIndexerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addUser();

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			_group.getGroupId(), _user.getUserId());

		try {
			_commerceAccount =
				CommerceAccountLocalServiceUtil.addPersonalCommerceAccount(
					_user.getUserId(), StringPool.BLANK, StringPool.BLANK,
					_serviceContext);
		}
		catch (CommerceAccountTypeException commerceAccountTypeException) {
			_commerceAccount =
				CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(
					_user.getUserId());
		}

		_commerceCurrency = CommerceCurrencyTestUtil.addCommerceCurrency(
			_group.getCompanyId());

		_commerceChannel = CommerceTestUtil.addCommerceChannel(
			_group.getGroupId(), _commerceCurrency.getCode());

		_commerceCatalog = CommerceCatalogLocalServiceUtil.addCommerceCatalog(
			null, RandomTestUtil.randomString(), _commerceCurrency.getCode(),
			LocaleUtil.US.getDisplayLanguage(), _serviceContext);

		_commerceContext = new TestCommerceContext(
			null, _commerceCurrency, _commerceChannel, _user, _group, null);

		_indexer = _indexerRegistry.nullSafeGetIndexer(
			CommerceInventoryBookedQuantity.class);
	}

	@After
	public void tearDown() throws Exception {
		List<CommerceInventoryBookedQuantity>
			commerceInventoryBookedQuantities =
				_commerceBookedQuantityLocalService.
					getCommerceInventoryBookedQuantities(
						QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (CommerceInventoryBookedQuantity commerceInventoryBookedQuantity :
				commerceInventoryBookedQuantities) {

			_commerceBookedQuantityLocalService.
				deleteCommerceInventoryBookedQuantity(
					commerceInventoryBookedQuantity);
		}

		for (CommerceOrderItem commerceOrderItem : _commerceOrderItems) {
			_commerceOrderItemLocalService.deleteCommerceOrderItem(
				_user.getUserId(), commerceOrderItem);
		}
	}

	@Test
	public void testEmptyKeywordSKUQuery() throws Exception {
		CPInstance cpInstance = CPTestUtil.addCPInstance(_group.getGroupId());

		cpInstance.setSku("Example SKU");

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		_cpInstances.add(cpInstance);

		_commerceInventoryWarehouse =
			CommerceInventoryTestUtil.addCommerceInventoryWarehouse(
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		_commerceInventoryWarehouseItems.add(
			CommerceInventoryTestUtil.addCommerceInventoryWarehouseItem(
				_user.getUserId(), _commerceInventoryWarehouse,
				cpInstance.getSku(), 2));

		_commerceChannelRel = CommerceTestUtil.addWarehouseCommerceChannelRel(
			_commerceInventoryWarehouse.getCommerceInventoryWarehouseId(),
			_commerceChannel.getCommerceChannelId());

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.addCommerceOrder(
				_user.getUserId(), _commerceChannel.getGroupId(),
				_commerceAccount.getCommerceAccountId(),
				_commerceCurrency.getCommerceCurrencyId(), 0);

		_commerceOrders.add(commerceOrder);

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.addCommerceOrderItem(
				_user.getUserId(), commerceOrder.getCommerceOrderId(),
				cpInstance.getCPInstanceId(), null, 2, 0, _commerceContext,
				_serviceContext);

		CommerceInventoryBookedQuantity commerceInventoryBookedQuantity =
			_commerceBookedQuantityLocalService.addCommerceBookedQuantity(
				_user.getUserId(), cpInstance.getSku(), 2, null,
				Collections.emptyMap());

		commerceOrderItem =
			_commerceOrderItemLocalService.updateCommerceOrderItem(
				commerceOrderItem.getCommerceOrderItemId(),
				commerceInventoryBookedQuantity.
					getCommerceInventoryBookedQuantityId());

		_assertSearch(
			StringPool.BLANK, cpInstance.getSku(),
			_commerceInventoryBookedQuantityLocalService.
				getCommerceInventoryBookedQuantities(
					commerceOrderItem.getCompanyId(),
					commerceOrderItem.getSku(), -1, -1));
	}

	@Test
	public void testKeywordSKUQuery() throws Exception {
		CPInstance cpInstance = CPTestUtil.addCPInstance(_group.getGroupId());

		cpInstance.setSku("Example SKU");

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		_cpInstances.add(cpInstance);

		_commerceInventoryWarehouse =
			CommerceInventoryTestUtil.addCommerceInventoryWarehouse(
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		_commerceInventoryWarehouseItems.add(
			CommerceInventoryTestUtil.addCommerceInventoryWarehouseItem(
				_user.getUserId(), _commerceInventoryWarehouse,
				cpInstance.getSku(), 2));

		_commerceChannelRel = CommerceTestUtil.addWarehouseCommerceChannelRel(
			_commerceInventoryWarehouse.getCommerceInventoryWarehouseId(),
			_commerceChannel.getCommerceChannelId());

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.addCommerceOrder(
				_user.getUserId(), _commerceChannel.getGroupId(),
				_commerceAccount.getCommerceAccountId(),
				_commerceCurrency.getCommerceCurrencyId(), 0);

		_commerceOrders.add(commerceOrder);

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.addCommerceOrderItem(
				_user.getUserId(), commerceOrder.getCommerceOrderId(),
				cpInstance.getCPInstanceId(), null, 2, 0, _commerceContext,
				_serviceContext);

		CommerceInventoryBookedQuantity commerceInventoryBookedQuantity =
			_commerceBookedQuantityLocalService.addCommerceBookedQuantity(
				_user.getUserId(), cpInstance.getSku(), 2, null,
				Collections.emptyMap());

		commerceOrderItem =
			_commerceOrderItemLocalService.updateCommerceOrderItem(
				commerceOrderItem.getCommerceOrderItemId(),
				commerceInventoryBookedQuantity.
					getCommerceInventoryBookedQuantityId());

		List<CommerceInventoryBookedQuantity>
			commerceInventoryBookedQuantities =
				_commerceInventoryBookedQuantityLocalService.
					getCommerceInventoryBookedQuantities(
						commerceOrderItem.getCompanyId(),
						commerceOrderItem.getSku(), -1, -1);

		_assertSearch(
			_commerceAccount.getName(), cpInstance.getSku(),
			commerceInventoryBookedQuantities);
		_assertSearch(
			String.valueOf(commerceOrder.getCommerceOrderId()),
			cpInstance.getSku(), commerceInventoryBookedQuantities);
	}

	@Rule
	public FrutillaRule frutillaRule = new FrutillaRule();

	private void _assertSearch(
			Hits hits,
			List<CommerceInventoryBookedQuantity>
				expectedCommerceInventoryBookedQuantities)
		throws Exception {

		List<CommerceInventoryBookedQuantity>
			actualCommerceInventoryBookedQuantities =
				_getCommerceInventoryBookedQuantities(hits);

		long[] actualInventoryBookedQuantityIds =
			_getCommerceInventoryBookedQuantityIds(
				actualCommerceInventoryBookedQuantities);

		long[] expectedInventoryBookedQuantityIds =
			_getCommerceInventoryBookedQuantityIds(
				expectedCommerceInventoryBookedQuantities);

		Assert.assertArrayEquals(
			actualInventoryBookedQuantityIds,
			expectedInventoryBookedQuantityIds);
	}

	private void _assertSearch(
			String keywords, String sku,
			List<CommerceInventoryBookedQuantity>
				expectedCommerceInventoryBookedQuantities)
		throws Exception {

		SearchContext searchContext = _getSearchContext(sku);

		searchContext.setKeywords(keywords);

		Hits hits = _indexer.search(searchContext);

		_assertSearch(hits, expectedCommerceInventoryBookedQuantities);
	}

	private List<CommerceInventoryBookedQuantity>
			_getCommerceInventoryBookedQuantities(Hits hits)
		throws Exception {

		Document[] documents = hits.getDocs();

		List<CommerceInventoryBookedQuantity>
			commerceInventoryBookedQuantities = new ArrayList<>(
				documents.length);

		for (Document document : documents) {
			commerceInventoryBookedQuantities.add(
				_getCommerceInventoryBookedQuantity(document));
		}

		return commerceInventoryBookedQuantities;
	}

	private CommerceInventoryBookedQuantity _getCommerceInventoryBookedQuantity(
			Document document)
		throws Exception {

		long commerceInventoryBookedQuantityId = GetterUtil.getLong(
			document.get(Field.ENTRY_CLASS_PK));

		return _commerceInventoryBookedQuantityLocalService.
			getCommerceInventoryBookedQuantity(
				commerceInventoryBookedQuantityId);
	}

	private long[] _getCommerceInventoryBookedQuantityIds(
		List<CommerceInventoryBookedQuantity>
			commerceInventoryBookedQuantities) {

		long[] commerceInventoryBookedQuantityIds =
			new long[commerceInventoryBookedQuantities.size()];

		for (int i = 0; i < commerceInventoryBookedQuantities.size(); i++) {
			CommerceInventoryBookedQuantity commerceInventoryBookedQuantity =
				commerceInventoryBookedQuantities.get(i);

			commerceInventoryBookedQuantityIds[i] =
				commerceInventoryBookedQuantity.
					getCommerceInventoryBookedQuantityId();
		}

		Arrays.sort(commerceInventoryBookedQuantityIds);

		return commerceInventoryBookedQuantityIds;
	}

	private SearchContext _getSearchContext(String sku) {
		SearchContext searchContext = new SearchContext();

		searchContext.setAttribute("sku", sku);
		searchContext.setCompanyId(_group.getCompanyId());
		searchContext.setSorts(SortFactoryUtil.getDefaultSorts());

		return searchContext;
	}

	@Inject
	private static IndexerRegistry _indexerRegistry;

	private CommerceAccount _commerceAccount;

	@Inject
	private CommerceInventoryBookedQuantityLocalService
		_commerceBookedQuantityLocalService;

	private CommerceCatalog _commerceCatalog;

	@DeleteAfterTestRun
	private CommerceChannel _commerceChannel;

	@DeleteAfterTestRun
	private CommerceChannelRel _commerceChannelRel;

	private CommerceContext _commerceContext;

	@DeleteAfterTestRun
	private CommerceCurrency _commerceCurrency;

	@Inject
	private CommerceInventoryBookedQuantityLocalService
		_commerceInventoryBookedQuantityLocalService;

	@DeleteAfterTestRun
	private CommerceInventoryWarehouse _commerceInventoryWarehouse;

	@DeleteAfterTestRun
	private List<CommerceInventoryWarehouseItem>
		_commerceInventoryWarehouseItems = new ArrayList<>();

	@Inject
	private CommerceOrderItemLocalService _commerceOrderItemLocalService;

	private final List<CommerceOrderItem> _commerceOrderItems =
		new ArrayList<>();

	@Inject
	private CommerceOrderLocalService _commerceOrderLocalService;

	@DeleteAfterTestRun
	private List<CommerceOrder> _commerceOrders = new ArrayList<>();

	@Inject
	private CPInstanceLocalService _cpInstanceLocalService;

	@DeleteAfterTestRun
	private List<CPInstance> _cpInstances = new ArrayList<>();

	@DeleteAfterTestRun
	private Group _group;

	private Indexer<CommerceInventoryBookedQuantity> _indexer;
	private ServiceContext _serviceContext;

	@DeleteAfterTestRun
	private User _user;

}