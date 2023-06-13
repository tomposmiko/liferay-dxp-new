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

import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.headless.commerce.admin.order.client.dto.v1_0.OrderNote;
import com.liferay.portal.kernel.model.User;
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
public class OrderNoteResourceTest extends BaseOrderNoteResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_user = UserTestUtil.addUser(testCompany);

		AccountEntry accountEntry = _accountEntryLocalService.addAccountEntry(
			_user.getUserId(), 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), null,
			RandomTestUtil.randomString() + "@liferay.com", null, null,
			"business", 1,
			ServiceContextTestUtil.getServiceContext(
				testCompany.getCompanyId(), testGroup.getGroupId(),
				_user.getUserId()));
		CommerceCurrency commerceCurrency =
			_commerceCurrencyLocalService.addCommerceCurrency(
				_user.getUserId(), RandomTestUtil.randomString(),
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomString(), BigDecimal.ONE,
				RandomTestUtil.randomLocaleStringMap(), 2, 2, "HALF_EVEN",
				false, RandomTestUtil.nextDouble(), true);

		_commerceOrder = _commerceOrderLocalService.addCommerceOrder(
			_user.getUserId(), testGroup.getGroupId(),
			accountEntry.getAccountEntryId(),
			commerceCurrency.getCommerceCurrencyId(),
			CommerceOrderConstants.TYPE_PK_FULFILLMENT);
	}

	@Ignore
	@Override
	@Test
	public void testDeleteOrderNote() throws Exception {
		super.testDeleteOrderNote();
	}

	@Ignore
	@Override
	@Test
	public void testDeleteOrderNoteByExternalReferenceCode() throws Exception {
		super.testDeleteOrderNoteByExternalReferenceCode();
	}

	@Ignore
	@Override
	@Test
	public void testGraphQLDeleteOrderNote() throws Exception {
		super.testDeleteOrderNoteByExternalReferenceCode();
	}

	@Ignore
	@Override
	@Test
	public void testPatchOrderNote() throws Exception {
		super.testPatchOrderNote();
	}

	@Ignore
	@Override
	@Test
	public void testPatchOrderNoteByExternalReferenceCode() throws Exception {
		super.testPatchOrderNoteByExternalReferenceCode();
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"content", "restricted"};
	}

	@Override
	protected OrderNote randomOrderNote() throws Exception {
		return new OrderNote() {
			{
				author = _user.getFullName();
				content = StringUtil.toLowerCase(RandomTestUtil.randomString());
				orderExternalReferenceCode =
					_commerceOrder.getExternalReferenceCode();
				orderId = _commerceOrder.getCommerceOrderId();
				restricted = false;
			}
		};
	}

	@Override
	protected OrderNote testDeleteOrderNote_addOrderNote() throws Exception {
		return orderNoteResource.postOrderIdOrderNote(
			_commerceOrder.getCommerceOrderId(), randomOrderNote());
	}

	@Override
	protected OrderNote
			testDeleteOrderNoteByExternalReferenceCode_addOrderNote()
		throws Exception {

		return orderNoteResource.postOrderByExternalReferenceCodeOrderNote(
			_commerceOrder.getExternalReferenceCode(), randomOrderNote());
	}

	@Override
	protected OrderNote
			testGetOrderByExternalReferenceCodeOrderNotesPage_addOrderNote(
				String externalReferenceCode, OrderNote orderNote)
		throws Exception {

		return orderNoteResource.postOrderByExternalReferenceCodeOrderNote(
			externalReferenceCode, orderNote);
	}

	@Override
	protected String
			testGetOrderByExternalReferenceCodeOrderNotesPage_getExternalReferenceCode()
		throws Exception {

		return _commerceOrder.getExternalReferenceCode();
	}

	@Override
	protected OrderNote testGetOrderIdOrderNotesPage_addOrderNote(
			Long id, OrderNote orderNote)
		throws Exception {

		return orderNoteResource.postOrderIdOrderNote(id, orderNote);
	}

	@Override
	protected Long testGetOrderIdOrderNotesPage_getId() throws Exception {
		return _commerceOrder.getCommerceOrderId();
	}

	@Override
	protected OrderNote testGetOrderNote_addOrderNote() throws Exception {
		return orderNoteResource.postOrderIdOrderNote(
			_commerceOrder.getCommerceOrderId(), randomOrderNote());
	}

	@Override
	protected OrderNote testGetOrderNoteByExternalReferenceCode_addOrderNote()
		throws Exception {

		return orderNoteResource.postOrderByExternalReferenceCodeOrderNote(
			_commerceOrder.getExternalReferenceCode(), randomOrderNote());
	}

	@Override
	protected OrderNote testGraphQLOrderNote_addOrderNote() throws Exception {
		return orderNoteResource.postOrderIdOrderNote(
			_commerceOrder.getCommerceOrderId(), randomOrderNote());
	}

	@Override
	protected OrderNote
			testPostOrderByExternalReferenceCodeOrderNote_addOrderNote(
				OrderNote orderNote)
		throws Exception {

		return orderNoteResource.postOrderByExternalReferenceCodeOrderNote(
			_commerceOrder.getExternalReferenceCode(), orderNote);
	}

	@Override
	protected OrderNote testPostOrderIdOrderNote_addOrderNote(
			OrderNote orderNote)
		throws Exception {

		return orderNoteResource.postOrderIdOrderNote(
			_commerceOrder.getCommerceOrderId(), orderNote);
	}

	@Inject
	private AccountEntryLocalService _accountEntryLocalService;

	@Inject
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;

	private CommerceOrder _commerceOrder;

	@Inject
	private CommerceOrderLocalService _commerceOrderLocalService;

	private User _user;

}