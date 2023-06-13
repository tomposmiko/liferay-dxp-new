/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.avalara.connector.web.internal.health.status;

import com.liferay.commerce.avalara.connector.constants.CommerceAvalaraConstants;
import com.liferay.commerce.avalara.connector.engine.CommerceAvalaraConnectorEngine;
import com.liferay.commerce.constants.CommerceHealthStatusConstants;
import com.liferay.commerce.health.status.CommerceHealthHttpStatus;
import com.liferay.commerce.product.model.CPTaxCategory;
import com.liferay.commerce.product.service.CPTaxCategoryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;

import java.util.Locale;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Katie Nesterovich
 */
@Component(
	immediate = true,
	property = {
		"commerce.health.status.display.order:Integer=150",
		"commerce.health.status.key=" + AvalaraTaxCodesCommerceHealthHttpStatus.KEY
	},
	service = CommerceHealthHttpStatus.class
)
public class AvalaraTaxCodesCommerceHealthHttpStatus
	implements CommerceHealthHttpStatus {

	public static final String KEY =
		"avalara.tax.codes.commerce.health.status.key";

	@Override
	public void fixIssue(HttpServletRequest httpServletRequest)
		throws PortalException {

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				httpServletRequest);

			Callable<Object> avalaraTaxCodesCallable =
				new AvalaraTaxCodesCommerceHealthHttpStatus.
					AvalaraTaxCodesCallable(serviceContext);

			TransactionInvokerUtil.invoke(
				_transactionConfig, avalaraTaxCodesCallable);
		}
		catch (Throwable throwable) {
			_log.error(throwable, throwable);
		}
	}

	@Override
	public String getDescription(Locale locale) {
		return _language.get(
			locale, "avalara.tax.codes.commerce.health.status.description");
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getName(Locale locale) {
		return _language.get(locale, KEY);
	}

	@Override
	public int getType() {
		return CommerceHealthStatusConstants.
			COMMERCE_HEALTH_STATUS_TYPE_VIRTUAL_INSTANCE;
	}

	@Override
	public boolean isFixed(long companyId, long commerceChannelId)
		throws PortalException {

		CPTaxCategory cpTaxCategory =
			_cpTaxCategoryLocalService.
				fetchCPTaxCategoryByExternalReferenceCode(
					CommerceAvalaraConstants.
						CP_TAX_CATEGORY_ERC_TANGIBLE_PERSONAL_PROPERTY,
					companyId);

		if (cpTaxCategory == null) {
			return false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AvalaraTaxCodesCommerceHealthHttpStatus.class);

	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRED, new Class<?>[] {Exception.class});

	@Reference
	private CommerceAvalaraConnectorEngine _commerceAvalaraConnectorEngine;

	@Reference
	private CPTaxCategoryLocalService _cpTaxCategoryLocalService;

	@Reference
	private Language _language;

	private class AvalaraTaxCodesCallable implements Callable<Object> {

		@Override
		public Object call() throws Exception {
			try {
				_commerceAvalaraConnectorEngine.addTaxCategories(
					_serviceContext.getUserId());
			}
			catch (Exception exception) {
				_log.error(exception);
			}

			return null;
		}

		private AvalaraTaxCodesCallable(ServiceContext serviceContext) {
			_serviceContext = serviceContext;
		}

		private final ServiceContext _serviceContext;

	}

}