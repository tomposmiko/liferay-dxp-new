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

package com.liferay.commerce.order.web.internal.frontend.data.set.filter;

import com.liferay.commerce.order.status.CommerceOrderStatus;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.commerce.order.web.internal.constants.CommerceOrderFDSNames;
import com.liferay.frontend.data.set.filter.BaseCheckBoxFDSFilter;
import com.liferay.frontend.data.set.filter.CheckBoxFDSFilterItem;
import com.liferay.frontend.data.set.filter.FDSFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(
	enabled = false, immediate = true,
	property = "frontend.data.set.name=" + CommerceOrderFDSNames.ALL_ORDERS,
	service = FDSFilter.class
)
public class CommerceOrderStatusFDSFilter extends BaseCheckBoxFDSFilter {

	@Override
	public List<CheckBoxFDSFilterItem> getCheckBoxFDSFilterItems(
		Locale locale) {

		List<CheckBoxFDSFilterItem> checkBoxFDSFilterItems = new ArrayList<>();

		for (CommerceOrderStatus commerceOrderStatus :
				_commerceOrderStatusRegistry.getCommerceOrderStatuses()) {

			checkBoxFDSFilterItems.add(
				new CheckBoxFDSFilterItem(
					commerceOrderStatus.getLabel(locale),
					commerceOrderStatus.getKey()));
		}

		return checkBoxFDSFilterItems;
	}

	@Override
	public String getId() {
		return "orderStatus";
	}

	@Override
	public String getLabel() {
		return "order-status";
	}

	@Reference
	private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;

}