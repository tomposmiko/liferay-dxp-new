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

package com.liferay.commerce.avalara.connector.internal;

import com.liferay.commerce.avalara.connector.CommerceAvalaraConnector;
import com.liferay.commerce.avalara.connector.configuration.CommerceAvalaraConnectorConfiguration;
import com.liferay.commerce.avalara.connector.exception.CommerceAvalaraConnectionException;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;
import com.liferay.portal.kernel.util.Base64;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.FetchResult;
import net.avalara.avatax.rest.client.models.CompanyModel;
import net.avalara.avatax.rest.client.models.PingResultModel;
import net.avalara.avatax.rest.client.models.TaxCodeModel;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Calvin Keum
 * @author Katie Nesterovich
 * @author Riccardo Alberti
 */
@Component(service = CommerceAvalaraConnector.class)
public class CommerceAvalaraConnectorImpl implements CommerceAvalaraConnector {

	@Override
	public Map<String, String> getCompanyCodes() throws Exception {
		AvaTaxClient avaTaxClient = _getAvaTaxClient();

		FetchResult<CompanyModel> companyModelFetchResult =
			avaTaxClient.queryCompanies(null, null, 0, 0, null);

		Map<String, String> companyCodes = new HashMap<>();

		for (CompanyModel companyModel : companyModelFetchResult.getValue()) {
			companyCodes.put(
				companyModel.getName(), companyModel.getCompanyCode());
		}

		return companyCodes;
	}

	@Override
	public List<TaxCodeModel> getTaxCodeModels() throws Exception {
		AvaTaxClient avaTaxClient = _getAvaTaxClient();

		FetchResult<TaxCodeModel> taxCodeModelFetchResult =
			avaTaxClient.listTaxCodes("isActive eq true", 0, 0, null);

		return taxCodeModelFetchResult.getValue();
	}

	public String getTaxRateByZipCode() throws Exception {
		AvaTaxClient avaTaxClient = _getAvaTaxClient();

		return avaTaxClient.downloadTaxRatesByZipCode(
			new Date() {

				@Override
				public String toString() {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd");

					return simpleDateFormat.format(this);
				}

			},
			null);
	}

	@Override
	public void verifyConnection(
			String accountNumber, String licenseKey, String serviceURL)
		throws Exception {

		try {
			AvaTaxClient avaTaxClient = _getAvaTaxClient(
				accountNumber, licenseKey, serviceURL);

			PingResultModel pingResultModel = avaTaxClient.ping();

			if (!pingResultModel.getAuthenticated()) {
				throw new CommerceAvalaraConnectionException();
			}
		}
		catch (Exception exception) {
			throw new CommerceAvalaraConnectionException(exception.getCause());
		}
	}

	private AvaTaxClient _getAvaTaxClient() throws PortalException {
		CommerceAvalaraConnectorConfiguration
			commerceAvalaraConnectorConfiguration =
				_configurationProvider.getConfiguration(
					CommerceAvalaraConnectorConfiguration.class,
					new CompanyServiceSettingsLocator(
						CompanyThreadLocal.getCompanyId(),
						CommerceAvalaraConnectorConfiguration.class.getName()));

		return _getAvaTaxClient(
			commerceAvalaraConnectorConfiguration.accountNumber(),
			commerceAvalaraConnectorConfiguration.licenseKey(),
			commerceAvalaraConnectorConfiguration.serviceURL());
	}

	private AvaTaxClient _getAvaTaxClient(
		String accountNumber, String licenseKey, String serviceURL) {

		AvaTaxClient avaTaxClient = new AvaTaxClient(
			"LiferayCommerceAvalaraConnector", "1.0", "Liferay", serviceURL);

		String securityHeader = StringBundler.concat(
			accountNumber, StringPool.COLON, licenseKey);

		return avaTaxClient.withSecurity(
			Base64.encode(securityHeader.getBytes()));
	}

	@Reference
	private ConfigurationProvider _configurationProvider;

}