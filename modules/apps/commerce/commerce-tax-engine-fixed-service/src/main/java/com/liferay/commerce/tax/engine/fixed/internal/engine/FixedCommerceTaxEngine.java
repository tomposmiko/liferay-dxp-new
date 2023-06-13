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

package com.liferay.commerce.tax.engine.fixed.internal.engine;

import com.liferay.commerce.exception.CommerceTaxEngineException;
import com.liferay.commerce.tax.CommerceTaxCalculateRequest;
import com.liferay.commerce.tax.CommerceTaxEngine;
import com.liferay.commerce.tax.CommerceTaxValue;
import com.liferay.commerce.tax.engine.fixed.model.CommerceTaxFixedRate;
import com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.math.BigDecimal;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.tax.engine.key=" + FixedCommerceTaxEngine.KEY,
	service = CommerceTaxEngine.class
)
public class FixedCommerceTaxEngine implements CommerceTaxEngine {

	public static final String KEY = "fixed-tax";

	@Override
	public CommerceTaxValue getCommerceTaxValue(
			CommerceTaxCalculateRequest commerceTaxCalculateRequest)
		throws CommerceTaxEngineException {

		CommerceTaxValue commerceTaxValue = null;

		try {
			CommerceTaxFixedRate commerceTaxFixedRate =
				_commerceTaxFixedRateLocalService.getCommerceTaxFixedRate(
					commerceTaxCalculateRequest.getTaxCategoryId(),
					commerceTaxCalculateRequest.getCommerceTaxMethodId());

			BigDecimal rate = BigDecimal.valueOf(
				commerceTaxFixedRate.getRate());

			BigDecimal amount = commerceTaxCalculateRequest.getPrice();

			BigDecimal taxValue = rate;

			if (commerceTaxCalculateRequest.isPercentage()) {
				taxValue = amount.multiply(rate);

				taxValue = taxValue.divide(_ONE_HUNDRED);
			}

			commerceTaxValue = new CommerceTaxValue(KEY, KEY, taxValue);
		}
		catch (PortalException pe) {
			_log.error(pe, pe);

			throw new CommerceTaxEngineException(pe);
		}

		return commerceTaxValue;
	}

	@Override
	public String getDescription(Locale locale) {
		ResourceBundle resourceBundle = _getResourceBundle(locale);

		return LanguageUtil.get(resourceBundle, "fixed-tax-description");
	}

	@Override
	public String getName(Locale locale) {
		ResourceBundle resourceBundle = _getResourceBundle(locale);

		return LanguageUtil.get(resourceBundle, KEY);
	}

	private ResourceBundle _getResourceBundle(Locale locale) {
		return ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());
	}

	private static final BigDecimal _ONE_HUNDRED = BigDecimal.valueOf(100);

	private static final Log _log = LogFactoryUtil.getLog(
		FixedCommerceTaxEngine.class);

	@Reference
	private CommerceTaxFixedRateLocalService _commerceTaxFixedRateLocalService;

}