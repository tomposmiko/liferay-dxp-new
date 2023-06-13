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

package com.liferay.commerce.product.definitions.web.internal.frontend.data.set.filter;

import com.liferay.commerce.product.definitions.web.internal.constants.CommerceProductFDSNames;
import com.liferay.commerce.product.type.CPType;
import com.liferay.commerce.product.type.CPTypeRegistry;
import com.liferay.frontend.data.set.filter.BaseSelectionFDSFilter;
import com.liferay.frontend.data.set.filter.FDSFilter;
import com.liferay.frontend.data.set.filter.SelectionFDSFilterItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(
	property = "frontend.data.set.name=" + CommerceProductFDSNames.PRODUCT_DEFINITIONS,
	service = FDSFilter.class
)
public class ProductTypeSelectionFDSFilter extends BaseSelectionFDSFilter {

	@Override
	public String getId() {
		return "productType";
	}

	@Override
	public String getLabel() {
		return "product-type";
	}

	@Override
	public List<SelectionFDSFilterItem> getSelectionFDSFilterItems(
		Locale locale) {

		List<SelectionFDSFilterItem> selectionFDSFilterItems =
			new ArrayList<>();

		for (CPType cpType : _cpTypeRegistry.getCPTypes()) {
			selectionFDSFilterItems.add(
				new SelectionFDSFilterItem(
					cpType.getLabel(locale), cpType.getName()));
		}

		return selectionFDSFilterItems;
	}

	@Override
	public boolean isMultiple() {
		return false;
	}

	@Reference
	private CPTypeRegistry _cpTypeRegistry;

}