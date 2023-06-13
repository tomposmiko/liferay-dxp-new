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
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.media.constants.CommerceMediaConstants;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.constants.CommerceChannelConstants;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants;
import com.liferay.commerce.product.type.virtual.order.model.CommerceVirtualOrderItem;
import com.liferay.commerce.product.type.virtual.order.service.CommerceVirtualOrderItemLocalService;
import com.liferay.commerce.product.type.virtual.order.util.CommerceVirtualOrderItemChecker;
import com.liferay.commerce.product.type.virtual.service.CPDefinitionVirtualSettingLocalService;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.commerce.test.util.context.TestCommerceContext;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.headless.commerce.admin.order.client.dto.v1_0.OrderItem;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.DataGuard;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;

import java.math.BigDecimal;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Crescenzo Rega
 * @author Stefano Motta
 */
@DataGuard(scope = DataGuard.Scope.METHOD)
@RunWith(Arquillian.class)
public class OrderItemResourceTest extends BaseOrderItemResourceTestCase {

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
			RandomTestUtil.randomString() + "@liferay.com", null, null,
			"business", 1, _serviceContext);

		_commerceCurrency = _commerceCurrencyLocalService.addCommerceCurrency(
			_user.getUserId(), RandomTestUtil.randomString(),
			RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomString(), BigDecimal.ONE,
			RandomTestUtil.randomLocaleStringMap(), 2, 2, "HALF_EVEN", false,
			RandomTestUtil.nextDouble(), true);

		_commerceCatalog = CommerceTestUtil.addCommerceCatalog(
			testCompany.getCompanyId(), testGroup.getGroupId(),
			_user.getUserId(), _commerceCurrency.getCode());

		_commerceChannel = _commerceChannelLocalService.addCommerceChannel(
			RandomTestUtil.randomString(), testGroup.getGroupId(),
			RandomTestUtil.randomString(),
			CommerceChannelConstants.CHANNEL_TYPE_SITE, null,
			_commerceCurrency.getCode(), _serviceContext);
		_commerceOrder = CommerceTestUtil.addB2BCommerceOrder(
			testGroup.getGroupId(), _user.getUserId(),
			_accountEntry.getAccountEntryId(),
			_commerceCurrency.getCommerceCurrencyId());
	}

	@Ignore
	@Override
	@Test
	public void testDeleteOrderItem() throws Exception {
		super.testDeleteOrderItem();
	}

	@Ignore
	@Override
	@Test
	public void testDeleteOrderItemByExternalReferenceCode() throws Exception {
		super.testDeleteOrderItemByExternalReferenceCode();
	}

	@Test
	public void testGetdOrderItemWithURL() throws Exception {
		String url = "http://www.example.com/myfiles/download";

		OrderItem postOrderItem = _addCommerceOrderItem(_getOrderItem(0, url));

		OrderItem getOrderItem = orderItemResource.getOrderItem(
			postOrderItem.getId());

		String[] virtualItemURLs = {url};

		Assert.assertEquals(virtualItemURLs, getOrderItem.getVirtualItemURLs());
	}

	@Test
	public void testGetOrderItemWithFileEntry() throws Exception {
		FileEntry fileEntry = _dlAppLocalService.addFileEntry(
			null, _user.getUserId(), testGroup.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString() + ".jpg", ContentTypes.IMAGE_JPEG,
			FileUtil.getBytes(
				OrderItemResourceTest.class, "dependencies/image.jpg"),
			null, null, _serviceContext);

		OrderItem postOrderItem = _addCommerceOrderItem(
			_getOrderItem(fileEntry.getFileEntryId(), null));

		OrderItem getOrderItem = orderItemResource.getOrderItem(
			postOrderItem.getId());

		CommerceVirtualOrderItem commerceVirtualOrderItem =
			_commerceVirtualOrderItemLocalService.
				fetchCommerceVirtualOrderItemByCommerceOrderItemId(
					getOrderItem.getId());

		String[] virtualItemURLs = {
			StringBundler.concat(
				_portal.getPathModule(), StringPool.SLASH,
				CommerceMediaConstants.SERVLET_PATH,
				CommerceMediaConstants.URL_SEPARATOR_VIRTUAL_ORDER_ITEM,
				commerceVirtualOrderItem.getCommerceVirtualOrderItemId(),
				CommerceMediaConstants.URL_SEPARATOR_FILE,
				fileEntry.getFileEntryId())
		};

		Assert.assertEquals(virtualItemURLs, getOrderItem.getVirtualItemURLs());
	}

	@Ignore
	@Override
	@Test
	public void testGraphQLDeleteOrderItem() throws Exception {
		super.testGraphQLDeleteOrderItem();
	}

	@Ignore
	@Override
	@Test
	public void testPatchOrderItem() throws Exception {
		super.testPatchOrderItem();
	}

	@Ignore
	@Override
	@Test
	public void testPatchOrderItemByExternalReferenceCode() throws Exception {
		super.testPatchOrderItemByExternalReferenceCode();
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"quantity"};
	}

	@Override
	protected String[] getIgnoredEntityFieldNames() {
		return new String[] {"sku"};
	}

	@Override
	protected OrderItem randomOrderItem() throws Exception {
		return _getOrderItem(0, RandomTestUtil.randomString());
	}

	@Override
	protected OrderItem testDeleteOrderItem_addOrderItem() throws Exception {
		return orderItemResource.postOrderIdOrderItem(
			_commerceOrder.getCommerceOrderId(), randomOrderItem());
	}

	@Override
	protected OrderItem
			testDeleteOrderItemByExternalReferenceCode_addOrderItem()
		throws Exception {

		return orderItemResource.postOrderByExternalReferenceCodeOrderItem(
			_commerceOrder.getExternalReferenceCode(), randomOrderItem());
	}

	@Override
	protected OrderItem
			testGetOrderByExternalReferenceCodeOrderItemsPage_addOrderItem(
				String externalReferenceCode, OrderItem orderItem)
		throws Exception {

		return orderItemResource.postOrderByExternalReferenceCodeOrderItem(
			externalReferenceCode, orderItem);
	}

	@Override
	protected String
			testGetOrderByExternalReferenceCodeOrderItemsPage_getExternalReferenceCode()
		throws Exception {

		return _commerceOrder.getExternalReferenceCode();
	}

	@Override
	protected OrderItem testGetOrderIdOrderItemsPage_addOrderItem(
			Long id, OrderItem orderItem)
		throws Exception {

		return orderItemResource.postOrderIdOrderItem(id, orderItem);
	}

	@Override
	protected Long testGetOrderIdOrderItemsPage_getId() throws Exception {
		return _commerceOrder.getCommerceOrderId();
	}

	@Override
	protected OrderItem testGetOrderItem_addOrderItem() throws Exception {
		return orderItemResource.postOrderIdOrderItem(
			_commerceOrder.getCommerceOrderId(), randomOrderItem());
	}

	@Override
	protected OrderItem testGetOrderItemByExternalReferenceCode_addOrderItem()
		throws Exception {

		return orderItemResource.postOrderByExternalReferenceCodeOrderItem(
			_commerceOrder.getExternalReferenceCode(), randomOrderItem());
	}

	@Override
	protected OrderItem testGetOrderItemsPage_addOrderItem(OrderItem orderItem)
		throws Exception {

		return orderItemResource.postOrderIdOrderItem(
			_commerceOrder.getCommerceOrderId(), orderItem);
	}

	@Override
	protected OrderItem testGraphQLOrderItem_addOrderItem() throws Exception {
		return orderItemResource.postOrderIdOrderItem(
			_commerceOrder.getCommerceOrderId(), randomOrderItem());
	}

	@Override
	protected OrderItem
			testPostOrderByExternalReferenceCodeOrderItem_addOrderItem(
				OrderItem orderItem)
		throws Exception {

		return orderItemResource.postOrderByExternalReferenceCodeOrderItem(
			_commerceOrder.getExternalReferenceCode(), orderItem);
	}

	@Override
	protected OrderItem testPostOrderIdOrderItem_addOrderItem(
			OrderItem orderItem)
		throws Exception {

		return orderItemResource.postOrderIdOrderItem(
			_commerceOrder.getCommerceOrderId(), orderItem);
	}

	@Override
	protected OrderItem testPutOrderItem_addOrderItem() throws Exception {
		return orderItemResource.postOrderIdOrderItem(
			_commerceOrder.getCommerceOrderId(), randomOrderItem());
	}

	@Override
	protected OrderItem testPutOrderItemByExternalReferenceCode_addOrderItem()
		throws Exception {

		return orderItemResource.postOrderByExternalReferenceCodeOrderItem(
			_commerceOrder.getExternalReferenceCode(), randomOrderItem());
	}

	private OrderItem _addCommerceOrderItem(OrderItem orderItem)
		throws Exception {

		CommerceAccount commerceAccount =
			_commerceAccountLocalService.getCommerceAccount(
				_accountEntry.getAccountEntryId());

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.addCommerceOrderItem(
				_user.getUserId(), _commerceOrder.getCommerceOrderId(),
				orderItem.getSkuId(), null, orderItem.getQuantity(),
				orderItem.getQuantity(),
				new TestCommerceContext(
					_commerceCurrency, _commerceChannel, _user, testGroup,
					commerceAccount, _commerceOrder),
				_serviceContext);

		_commerceVirtualOrderItemChecker.checkCommerceVirtualOrderItems(
			_commerceOrder.getCommerceOrderId());

		return new OrderItem() {
			{
				bookedQuantityId = commerceOrderItem.getBookedQuantityId();
				deliveryGroup = commerceOrderItem.getDeliveryGroup();
				discountManuallyAdjusted =
					commerceOrderItem.isDiscountManuallyAdjusted();
				externalReferenceCode =
					commerceOrderItem.getExternalReferenceCode();
				id = commerceOrderItem.getCommerceOrderItemId();
				orderExternalReferenceCode =
					_commerceOrder.getExternalReferenceCode();
				orderId = _commerceOrder.getCommerceOrderId();
				priceManuallyAdjusted =
					commerceOrderItem.isPriceManuallyAdjusted();
				printedNote = commerceOrderItem.getPrintedNote();
				quantity = commerceOrderItem.getQuantity();
				requestedDeliveryDate =
					commerceOrderItem.getRequestedDeliveryDate();
				shippedQuantity = commerceOrderItem.getShippedQuantity();
				shippingAddressId = commerceOrderItem.getShippingAddressId();
				sku = commerceOrderItem.getSku();
				skuExternalReferenceCode =
					commerceOrderItem.getExternalReferenceCode();
				skuId = commerceOrderItem.getCPInstanceId();
				subscription = commerceOrderItem.isSubscription();

				setVirtualItemURLs(
					() -> {
						CommerceVirtualOrderItem commerceVirtualOrderItem =
							_commerceVirtualOrderItemLocalService.
								fetchCommerceVirtualOrderItemByCommerceOrderItemId(
									commerceOrderItem.getCommerceOrderItemId());

						if (commerceVirtualOrderItem == null) {
							return null;
						}

						return new String[] {commerceVirtualOrderItem.getUrl()};
					});
			}
		};
	}

	private OrderItem _getOrderItem(long fileEntryId, String url)
		throws Exception {

		CPDefinition cpDefinition = CPTestUtil.addCPDefinitionFromCatalog(
			_commerceCatalog.getGroupId(), VirtualCPTypeConstants.NAME, true,
			true);

		List<CPInstance> cpInstances = cpDefinition.getCPInstances();

		CPInstance cpInstance = cpInstances.get(0);

		_cpDefinitionVirtualSettingLocalService.addCPDefinitionVirtualSetting(
			cpDefinition.getModelClassName(), cpDefinition.getCPDefinitionId(),
			fileEntryId, url, CommerceOrderConstants.ORDER_STATUS_PENDING, 0,
			RandomTestUtil.randomInt(), true, 0, "sampleUrl", false, null, 0,
			_serviceContext);

		CommerceTestUtil.updateBackOrderCPDefinitionInventory(cpDefinition);

		return new OrderItem() {
			{
				bookedQuantityId = RandomTestUtil.randomLong();
				deliveryGroup = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				discountManuallyAdjusted = RandomTestUtil.randomBoolean();
				externalReferenceCode = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
				orderExternalReferenceCode =
					_commerceOrder.getExternalReferenceCode();
				orderId = _commerceOrder.getCommerceOrderId();
				priceManuallyAdjusted = RandomTestUtil.randomBoolean();
				printedNote = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				quantity = RandomTestUtil.randomInt(1, 100);
				requestedDeliveryDate = RandomTestUtil.nextDate();
				shippedQuantity = RandomTestUtil.randomInt();
				shippingAddressId = RandomTestUtil.randomLong();
				sku = cpInstance.getSku();
				skuExternalReferenceCode =
					cpInstance.getExternalReferenceCode();
				skuId = cpInstance.getCPInstanceId();
				subscription = RandomTestUtil.randomBoolean();
				unitOfMeasure = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
			}
		};
	}

	private AccountEntry _accountEntry;

	@Inject
	private AccountEntryLocalService _accountEntryLocalService;

	@Inject
	private CommerceAccountLocalService _commerceAccountLocalService;

	private CommerceCatalog _commerceCatalog;
	private CommerceChannel _commerceChannel;

	@Inject
	private CommerceChannelLocalService _commerceChannelLocalService;

	private CommerceCurrency _commerceCurrency;

	@Inject
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;

	private CommerceOrder _commerceOrder;

	@Inject
	private CommerceOrderItemLocalService _commerceOrderItemLocalService;

	@Inject
	private CommerceVirtualOrderItemChecker _commerceVirtualOrderItemChecker;

	@Inject
	private CommerceVirtualOrderItemLocalService
		_commerceVirtualOrderItemLocalService;

	@Inject
	private CPDefinitionVirtualSettingLocalService
		_cpDefinitionVirtualSettingLocalService;

	@Inject
	private DLAppLocalService _dlAppLocalService;

	@Inject
	private Portal _portal;

	private ServiceContext _serviceContext;
	private User _user;

}