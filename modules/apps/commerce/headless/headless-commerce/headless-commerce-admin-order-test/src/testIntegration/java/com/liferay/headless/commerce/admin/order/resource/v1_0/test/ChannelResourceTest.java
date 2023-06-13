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

package com.liferay.headless.commerce.admin.order.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.model.CommerceOrderType;
import com.liferay.commerce.model.CommerceOrderTypeRel;
import com.liferay.commerce.order.rule.constants.COREntryConstants;
import com.liferay.commerce.order.rule.model.COREntry;
import com.liferay.commerce.order.rule.model.COREntryRel;
import com.liferay.commerce.order.rule.service.COREntryLocalService;
import com.liferay.commerce.order.rule.service.COREntryRelLocalService;
import com.liferay.commerce.product.constants.CommerceChannelConstants;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceOrderTypeLocalService;
import com.liferay.commerce.service.CommerceOrderTypeRelLocalService;
import com.liferay.headless.commerce.admin.order.client.dto.v1_0.Channel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.DataGuard;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Stefano Motta
 */
@DataGuard(scope = DataGuard.Scope.METHOD)
@RunWith(Arquillian.class)
public class ChannelResourceTest extends BaseChannelResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		User user = UserTestUtil.addUser(testCompany);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				testCompany.getCompanyId(), testGroup.getGroupId(),
				user.getUserId());

		CommerceCurrency commerceCurrency =
			_commerceCurrencyLocalService.addCommerceCurrency(
				user.getUserId(), RandomTestUtil.randomString(),
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomString(), BigDecimal.ONE,
				RandomTestUtil.randomLocaleStringMap(), 2, 2, "HALF_EVEN",
				false, RandomTestUtil.nextDouble(), true);

		_commerceChannel = _commerceChannelLocalService.addCommerceChannel(
			RandomTestUtil.randomString(), testGroup.getGroupId(),
			RandomTestUtil.randomString(),
			CommerceChannelConstants.CHANNEL_TYPE_SITE, null,
			commerceCurrency.getCode(), serviceContext);

		CommerceOrderType commerceOrderType =
			_commerceOrderTypeLocalService.addCommerceOrderType(
				RandomTestUtil.randomString(), user.getUserId(),
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomLocaleStringMap(), true, 1, 1, 2022, 12, 0,
				RandomTestUtil.nextInt(), 0, 0, 0, 0, 0, true, serviceContext);

		_commerceOrderTypeRel =
			_commerceOrderTypeRelLocalService.addCommerceOrderTypeRel(
				user.getUserId(), CommerceChannel.class.getName(),
				_commerceChannel.getCommerceChannelId(),
				commerceOrderType.getCommerceOrderTypeId(), serviceContext);

		COREntry corEntry = _corEntryLocalService.addCOREntry(
			RandomTestUtil.randomString(), user.getUserId(), true,
			RandomTestUtil.randomString(), 1, 1, 2022, 12, 0, 0, 0, 0, 0, 0,
			true, RandomTestUtil.randomString(), RandomTestUtil.nextInt(),
			COREntryConstants.TYPE_MINIMUM_ORDER_AMOUNT, null, serviceContext);

		_corEntryRel = _corEntryRelLocalService.addCOREntryRel(
			user.getUserId(), CommerceChannel.class.getName(),
			_commerceChannel.getCommerceChannelId(), corEntry.getCOREntryId());
	}

	@Ignore
	@Override
	@Test
	public void testGetOrderByExternalReferenceCodeChannel() throws Exception {
		super.testGetOrderByExternalReferenceCodeChannel();
	}

	@Ignore
	@Override
	@Test
	public void testGetOrderIdChannel() throws Exception {
		super.testGetOrderIdChannel();
	}

	@Ignore
	@Override
	@Test
	public void testGraphQLGetOrderByExternalReferenceCodeChannel()
		throws Exception {

		super.testGraphQLGetOrderByExternalReferenceCodeChannel();
	}

	@Ignore
	@Override
	@Test
	public void testGraphQLGetOrderIdChannel() throws Exception {
		super.testGraphQLGetOrderIdChannel();
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"currencyCode", "name"};
	}

	@Override
	protected Channel randomChannel() throws Exception {
		return new Channel() {
			{
				currencyCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				type = CommerceChannelConstants.CHANNEL_TYPE_SITE;
			}
		};
	}

	@Override
	protected Channel testGetOrderByExternalReferenceCodeChannel_addChannel()
		throws Exception {

		return _toChannel();
	}

	@Override
	protected Channel testGetOrderIdChannel_addChannel() throws Exception {
		return _toChannel();
	}

	@Override
	protected Channel testGetOrderRuleChannelChannel_addChannel()
		throws Exception {

		return _toChannel();
	}

	@Override
	protected Long testGetOrderRuleChannelChannel_getOrderRuleChannelId()
		throws Exception {

		return _corEntryRel.getCOREntryRelId();
	}

	@Override
	protected Channel testGetOrderTypeChannelChannel_addChannel()
		throws Exception {

		return _toChannel();
	}

	@Override
	protected Long testGetOrderTypeChannelChannel_getOrderTypeChannelId()
		throws Exception {

		return _commerceOrderTypeRel.getCommerceOrderTypeRelId();
	}

	@Override
	protected Channel testGraphQLChannel_addChannel() throws Exception {
		return _toChannel();
	}

	@Override
	protected Long testGraphQLGetOrderRuleChannelChannel_getOrderRuleChannelId()
		throws Exception {

		return _corEntryRel.getCOREntryRelId();
	}

	@Override
	protected Long testGraphQLGetOrderTypeChannelChannel_getOrderTypeChannelId()
		throws Exception {

		return _commerceOrderTypeRel.getCommerceOrderTypeRelId();
	}

	private Channel _toChannel() throws Exception {
		return new Channel() {
			{
				currencyCode = _commerceChannel.getCommerceCurrencyCode();
				externalReferenceCode =
					_commerceChannel.getExternalReferenceCode();
				id = _commerceChannel.getCommerceChannelId();
				name = _commerceChannel.getName();
				type = _commerceChannel.getType();
			}
		};
	}

	private CommerceChannel _commerceChannel;

	@Inject
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Inject
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;

	@Inject
	private CommerceOrderTypeLocalService _commerceOrderTypeLocalService;

	private CommerceOrderTypeRel _commerceOrderTypeRel;

	@Inject
	private CommerceOrderTypeRelLocalService _commerceOrderTypeRelLocalService;

	@Inject
	private COREntryLocalService _corEntryLocalService;

	private COREntryRel _corEntryRel;

	@Inject
	private COREntryRelLocalService _corEntryRelLocalService;

}