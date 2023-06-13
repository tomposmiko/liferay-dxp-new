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

package com.liferay.commerce.currency.internal.model.listener.util;

import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Locale;

/**
 * @author Shuyang Zhou
 */
public class ImportDefaultValuesUtil {

	public static void importDefaultValues(
		CommerceCurrencyLocalService commerceCurrencyLocalService,
		Company company) {

		TransactionCommitCallbackUtil.registerCallback(
			() -> {
				Locale defaultLocale = LocaleThreadLocal.getDefaultLocale();
				Locale siteDefaultLocale =
					LocaleThreadLocal.getSiteDefaultLocale();

				try {
					LocaleThreadLocal.setDefaultLocale(company.getLocale());
					LocaleThreadLocal.setSiteDefaultLocale(null);

					ServiceContext serviceContext = new ServiceContext();

					serviceContext.setCompanyId(company.getCompanyId());
					serviceContext.setLanguageId(
						LocaleUtil.toLanguageId(company.getLocale()));

					User guestUser = company.getGuestUser();

					serviceContext.setUserId(guestUser.getUserId());

					commerceCurrencyLocalService.importDefaultValues(
						false, serviceContext);

					return null;
				}
				finally {
					LocaleThreadLocal.setDefaultLocale(defaultLocale);
					LocaleThreadLocal.setSiteDefaultLocale(siteDefaultLocale);
				}
			});
	}

}