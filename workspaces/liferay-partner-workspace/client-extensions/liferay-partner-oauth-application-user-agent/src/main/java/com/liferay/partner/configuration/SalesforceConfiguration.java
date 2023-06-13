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

package com.liferay.partner.configuration;

import com.sforce.async.AsyncApiException;
import com.sforce.async.BulkConnection;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author José Abelenda
 */
@Configuration
public class SalesforceConfiguration {

	@Bean
	public BulkConnection bulkConnection()
		throws AsyncApiException, ConnectionException {

		ConnectorConfig connectorConfig = new ConnectorConfig();

		connectorConfig.setAuthEndpoint(
			_salesforceApiEndpoint + "/" + _salesforceApiVersion);
		connectorConfig.setPassword(_salesforceApiPassword + _salesforceApiKey);
		connectorConfig.setUsername(_salesforceApiLogin);

		new PartnerConnection(connectorConfig);

		String serviceEndpoint = connectorConfig.getServiceEndpoint();

		String restEndpoint =
			serviceEndpoint.substring(0, serviceEndpoint.indexOf("Soap/")) +
				"async/" + _salesforceApiVersion;

		return new BulkConnection(
			new ConnectorConfig() {
				{
					setCompression(true);
					setRestEndpoint(restEndpoint);
					setSessionId(connectorConfig.getSessionId());
					setTraceMessage(false);
				}
			});
	}

	@Value("${salesforce.api.endpoint}")
	private String _salesforceApiEndpoint;

	@Value("${salesforce.api.key}")
	private String _salesforceApiKey;

	@Value("${salesforce.api.login}")
	private String _salesforceApiLogin;

	@Value("${salesforce.api.password}")
	private String _salesforceApiPassword;

	@Value("${salesforce.api.version}")
	private String _salesforceApiVersion;

}