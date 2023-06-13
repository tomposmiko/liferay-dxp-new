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

package com.liferay.commerce.order.content.web.internal.frontend.data.set.provider;

import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.content.web.internal.constants.CommerceOrderFDSNames;
import com.liferay.commerce.order.content.web.internal.frontend.data.set.provider.search.OrderFDSKeywordsImpl;
import com.liferay.commerce.order.content.web.internal.model.OrderItem;
import com.liferay.commerce.price.CommerceOrderItemPrice;
import com.liferay.commerce.price.CommerceOrderPriceCalculation;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CPSubscriptionInfo;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.product.util.CPSubscriptionType;
import com.liferay.commerce.product.util.CPSubscriptionTypeRegistry;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.commerce.util.CommerceOrderItemQuantityFormatter;
import com.liferay.commerce.util.CommerceUtil;
import com.liferay.frontend.data.set.provider.FDSDataProvider;
import com.liferay.frontend.data.set.provider.search.FDSKeywords;
import com.liferay.frontend.data.set.provider.search.FDSPagination;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false, immediate = true,
	property = "fds.data.provider.key=" + CommerceOrderFDSNames.PLACED_ORDER_ITEMS,
	service = FDSDataProvider.class
)
public class PlacedCommerceOrderItemFDSDataProvider
	implements FDSDataProvider<OrderItem> {

	@Override
	public List<OrderItem> getItems(
			FDSKeywords fdsKeywords, FDSPagination fdsPagination,
			HttpServletRequest httpServletRequest, Sort sort)
		throws PortalException {

		BaseModelSearchResult<CommerceOrderItem> baseModelSearchResult =
			_getBaseModelSearchResult(
				fdsKeywords, fdsPagination, httpServletRequest, sort);

		try {
			return _getOrderItems(
				baseModelSearchResult.getBaseModels(), httpServletRequest);
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		return Collections.emptyList();
	}

	@Override
	public int getItemsCount(
			FDSKeywords fdsKeywords, HttpServletRequest httpServletRequest)
		throws PortalException {

		OrderFDSKeywordsImpl orderFDSKeywordsImpl =
			(OrderFDSKeywordsImpl)fdsKeywords;

		return _commerceOrderItemService.getCommerceOrderItemsCount(
			orderFDSKeywordsImpl.getCommerceOrderId());
	}

	private String _formatDiscountAmount(
			CommerceOrderItemPrice commerceOrderItemPrice, Locale locale)
		throws Exception {

		if (commerceOrderItemPrice.getDiscountAmount() == null) {
			return StringPool.BLANK;
		}

		CommerceMoney discountAmountCommerceMoney =
			commerceOrderItemPrice.getDiscountAmount();

		return discountAmountCommerceMoney.format(locale);
	}

	private String _formatFinalPrice(
			CommerceOrderItemPrice commerceOrderItemPrice, Locale locale)
		throws Exception {

		if (commerceOrderItemPrice.getFinalPrice() == null) {
			return StringPool.BLANK;
		}

		CommerceMoney finalPriceCommerceMoney =
			commerceOrderItemPrice.getFinalPrice();

		return finalPriceCommerceMoney.format(locale);
	}

	private String _formatPromoPrice(
			CommerceOrderItemPrice commerceOrderItemPrice, Locale locale)
		throws Exception {

		CommerceMoney promoPriceCommerceMoney =
			commerceOrderItemPrice.getPromoPrice();

		if (promoPriceCommerceMoney == null) {
			return StringPool.BLANK;
		}

		BigDecimal price = promoPriceCommerceMoney.getPrice();

		if (price.compareTo(BigDecimal.ZERO) <= 0) {
			return StringPool.BLANK;
		}

		return promoPriceCommerceMoney.format(locale);
	}

	private String _formatSubscriptionPeriod(
			CommerceOrderItem commerceOrderItem, Locale locale)
		throws Exception {

		CPInstance cpInstance = commerceOrderItem.fetchCPInstance();

		if ((cpInstance == null) ||
			(cpInstance.getCPSubscriptionInfo() == null)) {

			return null;
		}

		CPSubscriptionInfo cpSubscriptionInfo =
			cpInstance.getCPSubscriptionInfo();

		String period = StringPool.BLANK;

		CPSubscriptionType cpSubscriptionType =
			_cpSubscriptionTypeRegistry.getCPSubscriptionType(
				cpSubscriptionInfo.getSubscriptionType());

		if (cpSubscriptionType != null) {
			period = cpSubscriptionType.getLabel(locale);

			if (cpSubscriptionInfo.getSubscriptionLength() > 1) {
				period = LanguageUtil.get(
					locale,
					StringUtil.toLowerCase(
						cpSubscriptionType.getLabel(LocaleUtil.US) +
							CharPool.LOWER_CASE_S));
			}
		}

		return LanguageUtil.format(
			locale, "every-x-x",
			new Object[] {cpSubscriptionInfo.getSubscriptionLength(), period});
	}

	private String _formatUnitPrice(
			CommerceOrderItemPrice commerceOrderItemPrice, Locale locale)
		throws Exception {

		if (commerceOrderItemPrice.getUnitPrice() == null) {
			return StringPool.BLANK;
		}

		CommerceMoney unitPriceCommerceMoney =
			commerceOrderItemPrice.getUnitPrice();

		return unitPriceCommerceMoney.format(locale);
	}

	private BaseModelSearchResult<CommerceOrderItem> _getBaseModelSearchResult(
			FDSKeywords fdsKeywords, FDSPagination fdsPagination,
			HttpServletRequest httpServletRequest, Sort sort)
		throws PortalException {

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		int start = 0;
		int end = 0;

		if (fdsPagination != null) {
			start = fdsPagination.getStartPosition();
			end = fdsPagination.getEndPosition();
		}

		return _commerceOrderItemService.searchCommerceOrderItems(
			commerceOrderId, 0, fdsKeywords.getKeywords(), start, end, sort);
	}

	private List<OrderItem> _getChildOrderItems(
			CommerceOrderItem commerceOrderItem,
			HttpServletRequest httpServletRequest)
		throws Exception {

		return _getOrderItems(
			_commerceOrderItemService.getChildCommerceOrderItems(
				commerceOrderItem.getCommerceOrderItemId()),
			httpServletRequest);
	}

	private long _getCommerceOptionValueCPDefinitionId(
		CommerceOrderItem commerceOrderItem) {

		if (!commerceOrderItem.hasParentCommerceOrderItem()) {
			return commerceOrderItem.getCPDefinitionId();
		}

		return commerceOrderItem.getParentCommerceOrderItemCPDefinitionId();
	}

	private String _getCommerceOrderOptions(
			CommerceOrderItem commerceOrderItem, Locale locale)
		throws Exception {

		StringJoiner stringJoiner = new StringJoiner(StringPool.COMMA);

		List<KeyValuePair> commerceOptionValueKeyValuePairs =
			_cpInstanceHelper.getKeyValuePairs(
				_getCommerceOptionValueCPDefinitionId(commerceOrderItem),
				commerceOrderItem.getJson(), locale);

		for (KeyValuePair keyValuePair : commerceOptionValueKeyValuePairs) {
			stringJoiner.add(keyValuePair.getValue());
		}

		return stringJoiner.toString();
	}

	private List<OrderItem> _getOrderItems(
			List<CommerceOrderItem> commerceOrderItems,
			HttpServletRequest httpServletRequest)
		throws Exception {

		if (commerceOrderItems.isEmpty()) {
			return Collections.emptyList();
		}

		List<OrderItem> orderItems = new ArrayList<>();

		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			Locale locale = themeDisplay.getLocale();

			CommerceOrder commerceOrder = commerceOrderItem.getCommerceOrder();

			CommerceOrderItemPrice commerceOrderItemPrice =
				_commerceOrderPriceCalculation.getCommerceOrderItemPrice(
					commerceOrder.getCommerceCurrency(), commerceOrderItem);

			orderItems.add(
				new OrderItem(
					commerceOrderItem.getCPInstanceId(),
					_formatDiscountAmount(commerceOrderItemPrice, locale), null,
					_commerceOrderItemQuantityFormatter.format(
						commerceOrderItem, locale),
					_formatSubscriptionPeriod(commerceOrderItem, locale),
					commerceOrderItem.getName(locale),
					_getCommerceOrderOptions(commerceOrderItem, locale),
					commerceOrderItem.getCommerceOrderId(),
					commerceOrderItem.getCommerceOrderItemId(),
					_getChildOrderItems(commerceOrderItem, httpServletRequest),
					commerceOrderItem.getParentCommerceOrderItemId(),
					_formatUnitPrice(commerceOrderItemPrice, locale),
					_formatPromoPrice(commerceOrderItemPrice, locale),
					commerceOrderItem.getShippedQuantity(),
					commerceOrderItem.getSku(),
					_cpInstanceHelper.getCPInstanceThumbnailSrc(
						CommerceUtil.getCommerceAccountId(
							(CommerceContext)httpServletRequest.getAttribute(
								CommerceWebKeys.COMMERCE_CONTEXT)),
						commerceOrderItem.getCPInstanceId()),
					_formatFinalPrice(commerceOrderItemPrice, locale)));
		}

		return orderItems;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PlacedCommerceOrderItemFDSDataProvider.class);

	@Reference
	private CommerceOrderItemQuantityFormatter
		_commerceOrderItemQuantityFormatter;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private CommerceOrderPriceCalculation _commerceOrderPriceCalculation;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private CPSubscriptionTypeRegistry _cpSubscriptionTypeRegistry;

}