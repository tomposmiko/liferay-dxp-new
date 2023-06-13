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

package com.liferay.commerce.order.web.internal.frontend.data.set.provider;

import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.frontend.model.LabelField;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderPayment;
import com.liferay.commerce.order.web.internal.constants.CommerceOrderFDSNames;
import com.liferay.commerce.order.web.internal.model.Payment;
import com.liferay.commerce.service.CommerceOrderPaymentLocalService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.frontend.data.set.provider.FDSDataProvider;
import com.liferay.frontend.data.set.provider.search.FDSKeywords;
import com.liferay.frontend.data.set.provider.search.FDSPagination;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.DateFormat;
import java.text.Format;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false, immediate = true,
	property = "fds.data.provider.key=" + CommerceOrderFDSNames.PAYMENTS,
	service = FDSDataProvider.class
)
public class CommercePaymentFDSDataProvider
	implements FDSDataProvider<Payment> {

	@Override
	public List<Payment> getItems(
			FDSKeywords fdsKeywords, FDSPagination fdsPagination,
			HttpServletRequest httpServletRequest, Sort sort)
		throws PortalException {

		List<Payment> payments = new ArrayList<>();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Format dateTimeFormat = FastDateFormatFactoryUtil.getDateTime(
			DateFormat.MEDIUM, DateFormat.MEDIUM, themeDisplay.getLocale(),
			themeDisplay.getTimeZone());

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			commerceOrderId);

		String amount = StringPool.BLANK;

		CommerceMoney totalCommerceMoney = commerceOrder.getTotalMoney();

		if (totalCommerceMoney != null) {
			amount = totalCommerceMoney.format(themeDisplay.getLocale());
		}

		List<CommerceOrderPayment> commerceOrderPayments =
			_commerceOrderPaymentLocalService.getCommerceOrderPayments(
				commerceOrder.getCommerceOrderId(),
				fdsPagination.getStartPosition(),
				fdsPagination.getEndPosition(), null);

		for (CommerceOrderPayment commerceOrderPayment :
				commerceOrderPayments) {

			payments.add(
				new Payment(
					commerceOrderPayment.getCommerceOrderPaymentId(),
					new LabelField(
						CommerceOrderPaymentConstants.getOrderPaymentLabelStyle(
							commerceOrderPayment.getStatus()),
						LanguageUtil.get(
							httpServletRequest,
							CommerceOrderPaymentConstants.
								getOrderPaymentStatusLabel(
									commerceOrderPayment.getStatus()))),
					amount,
					dateTimeFormat.format(commerceOrderPayment.getCreateDate()),
					commerceOrderPayment.getContent()));
		}

		return payments;
	}

	@Override
	public int getItemsCount(
			FDSKeywords fdsKeywords, HttpServletRequest httpServletRequest)
		throws PortalException {

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		return _commerceOrderPaymentLocalService.getCommerceOrderPaymentsCount(
			commerceOrderId);
	}

	@Reference
	private CommerceOrderPaymentLocalService _commerceOrderPaymentLocalService;

	@Reference
	private CommerceOrderService _commerceOrderService;

}