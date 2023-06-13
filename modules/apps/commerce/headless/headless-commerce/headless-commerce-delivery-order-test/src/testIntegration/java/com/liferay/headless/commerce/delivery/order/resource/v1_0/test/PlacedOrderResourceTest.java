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

package com.liferay.headless.commerce.delivery.order.resource.v1_0.test;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.product.constants.CommerceChannelConstants;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.headless.commerce.core.util.DateConfig;
import com.liferay.headless.commerce.delivery.order.client.dto.v1_0.PlacedOrder;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Andrea Sbarra
 */
@RunWith(Arquillian.class)
public class PlacedOrderResourceTest extends BasePlacedOrderResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_user = UserTestUtil.addUser(testCompany);

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			testCompany.getCompanyId(), testGroup.getGroupId(),
			_user.getUserId());

		_accountEntry = _accountEntryLocalService.addAccountEntry(
			_user.getUserId(), 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), null,
			RandomTestUtil.randomString() + "@liferay.com", null,
			RandomTestUtil.randomString(), "business", 1, _serviceContext);

		_commerceCurrency = _commerceCurrencyLocalService.addCommerceCurrency(
			_user.getUserId(), RandomTestUtil.randomString(),
			RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomString(), BigDecimal.ONE, new HashMap<>(), 2,
			2, "HALF_EVEN", false, RandomTestUtil.nextDouble(), true);

		_commerceChannel = _commerceChannelLocalService.addCommerceChannel(
			RandomTestUtil.randomString(), testGroup.getGroupId(),
			RandomTestUtil.randomString(),
			CommerceChannelConstants.CHANNEL_TYPE_SITE, null,
			_commerceCurrency.getCode(), _serviceContext);
	}

	@Ignore
	@Override
	@Test
	public void testGetPlacedOrderPaymentURL() throws Exception {
		super.testGetPlacedOrderPaymentURL();
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {
			"accountId", "orderUUID", "paymentMethod", "paymentStatus",
			"printedNote", "purchaseOrderNumber", "shippingOption"
		};
	}

	@Override
	protected PlacedOrder randomPlacedOrder() throws Exception {
		return new PlacedOrder() {
			{
				accountId = _accountEntry.getAccountEntryId();
				channelId = _commerceChannel.getCommerceChannelId();
				couponCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				createDate = RandomTestUtil.nextDate();
				currencyCode = _commerceCurrency.getCode();
				id = RandomTestUtil.randomLong();
				lastPriceUpdateDate = RandomTestUtil.nextDate();
				modifiedDate = RandomTestUtil.nextDate();
				orderTypeId = 1L;
				orderUUID = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				paymentStatus = RandomTestUtil.randomInt();
				paymentStatusLabel = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				placedOrderBillingAddressId = RandomTestUtil.randomLong();
				placedOrderShippingAddressId = RandomTestUtil.randomLong();
				printedNote = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				purchaseOrderNumber = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				shippingMethod = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				shippingOption = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				status = StringUtil.toLowerCase(RandomTestUtil.randomString());
				useAsBilling = RandomTestUtil.randomBoolean();
				valid = true;
			}
		};
	}

	@Override
	protected PlacedOrder testGetChannelAccountPlacedOrdersPage_addPlacedOrder(
			Long accountId, Long channelId, PlacedOrder placedOrder)
		throws Exception {

		return _addCommerceOrder(placedOrder);
	}

	@Override
	protected Long testGetChannelAccountPlacedOrdersPage_getAccountId()
		throws Exception {

		return _accountEntry.getAccountEntryId();
	}

	@Override
	protected Long testGetChannelAccountPlacedOrdersPage_getChannelId()
		throws Exception {

		return _commerceChannel.getCommerceChannelId();
	}

	@Override
	protected PlacedOrder testGetPlacedOrder_addPlacedOrder() throws Exception {
		return _addCommerceOrder(randomPlacedOrder());
	}

	@Override
	protected PlacedOrder testGraphQLPlacedOrder_addPlacedOrder()
		throws Exception {

		return _addCommerceOrder(randomPlacedOrder());
	}

	private PlacedOrder _addCommerceOrder(PlacedOrder placedOrder)
		throws Exception {

		DateConfig orderDateConfig = DateConfig.toDisplayDateConfig(
			placedOrder.getCreateDate(), _user.getTimeZone());

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.addCommerceOrder(
				_user.getUserId(), _commerceChannel.getGroupId(),
				placedOrder.getPlacedOrderBillingAddressId(),
				placedOrder.getAccountId(),
				_commerceCurrency.getCommerceCurrencyId(),
				placedOrder.getOrderTypeId(), 0,
				placedOrder.getPlacedOrderShippingAddressId(),
				placedOrder.getPaymentMethod(), orderDateConfig.getMonth(),
				orderDateConfig.getDay(), orderDateConfig.getYear(),
				orderDateConfig.getHour(), orderDateConfig.getMinute(),
				CommerceOrderConstants.ORDER_STATUS_COMPLETED,
				placedOrder.getPaymentStatus(),
				placedOrder.getPurchaseOrderNumber(), BigDecimal.ZERO,
				placedOrder.getShippingOption(), BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO, _serviceContext);

		_commerceOrders.add(commerceOrder);

		return new PlacedOrder() {
			{
				accountId = commerceOrder.getCommerceAccountId();
				channelId = _commerceChannel.getCommerceChannelId();
				couponCode = commerceOrder.getCouponCode();
				createDate = commerceOrder.getCreateDate();
				currencyCode = _commerceCurrency.getCode();
				id = commerceOrder.getCommerceOrderId();
				lastPriceUpdateDate = commerceOrder.getLastPriceUpdateDate();
				modifiedDate = commerceOrder.getModifiedDate();
				orderUUID = commerceOrder.getUuid();
				paymentMethod = commerceOrder.getCommercePaymentMethodKey();
				paymentStatus = commerceOrder.getPaymentStatus();
				placedOrderBillingAddressId =
					commerceOrder.getBillingAddressId();
				placedOrderShippingAddressId =
					commerceOrder.getShippingAddressId();
				printedNote = commerceOrder.getPrintedNote();
				purchaseOrderNumber = commerceOrder.getPurchaseOrderNumber();
				shippingOption = commerceOrder.getShippingOptionName();
				status = commerceOrder.getAdvanceStatus();
				valid = true;
			}
		};
	}

	@DeleteAfterTestRun
	private AccountEntry _accountEntry;

	@Inject
	private AccountEntryLocalService _accountEntryLocalService;

	@DeleteAfterTestRun
	private CommerceChannel _commerceChannel;

	@Inject
	private CommerceChannelLocalService _commerceChannelLocalService;

	@DeleteAfterTestRun
	private CommerceCurrency _commerceCurrency;

	@Inject
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;

	@Inject
	private CommerceOrderLocalService _commerceOrderLocalService;

	@DeleteAfterTestRun
	private final List<CommerceOrder> _commerceOrders = new ArrayList<>();

	private ServiceContext _serviceContext;

	@DeleteAfterTestRun
	private User _user;

}