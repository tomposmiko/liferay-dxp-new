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

package com.liferay.oauth2.provider.internal.upgrade.registry;

import com.liferay.oauth2.provider.internal.upgrade.v2_0_0.OAuth2ApplicationScopeAliasesUpgradeProcess;
import com.liferay.oauth2.provider.internal.upgrade.v3_0_0.OAuth2ApplicationClientCredentialUserUpgradeUser;
import com.liferay.oauth2.provider.internal.upgrade.v3_2_0.OAuth2ApplicationFeatureUpgradeProcess;
import com.liferay.oauth2.provider.internal.upgrade.v4_1_0.OAuth2ApplicationClientAuthenticationMethodUpgradeProcess;
import com.liferay.oauth2.provider.internal.upgrade.v4_2_1.OAuth2ScopeGrantRemoveCompanyIdFromObjectsRelatedUpgradeProcess;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.upgrade.BaseExternalReferenceCodeUpgradeProcess;
import com.liferay.portal.kernel.upgrade.BaseUuidUpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeProcessFactory;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 */
@Component(service = UpgradeStepRegistrator.class)
public class OAuth2ServiceUpgradeStepRegistrator
	implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"1.0.0", "1.1.0",
			UpgradeProcessFactory.alterColumnType(
				"OAuth2ScopeGrant", "scope", "VARCHAR(240) null"));

		registry.register(
			"1.1.0", "1.2.0",
			UpgradeProcessFactory.addColumns(
				"OAuth2Authorization", "remoteHostInfo VARCHAR(255) null"));

		registry.register(
			"1.2.0", "1.3.0",
			UpgradeProcessFactory.addColumns(
				"OAuth2ScopeGrant", "scopeAliases TEXT null"));

		registry.register(
			"1.3.0", "2.0.0",
			new OAuth2ApplicationScopeAliasesUpgradeProcess(
				_companyLocalService, _scopeLocator),
			UpgradeProcessFactory.dropColumns(
				"OAuth2ApplicationScopeAliases", "scopeAliases",
				"scopeAliasesHash"));

		registry.register(
			"2.0.0", "3.0.0",
			UpgradeProcessFactory.addColumns(
				"OAuth2Application", "clientCredentialUserId LONG"),
			UpgradeProcessFactory.addColumns(
				"OAuth2Application",
				"clientCredentialUserName VARCHAR(75) null"),
			new OAuth2ApplicationClientCredentialUserUpgradeUser());

		registry.register(
			"3.0.0", "3.1.0",
			UpgradeProcessFactory.addColumns(
				"OAuth2Application", "rememberDevice BOOLEAN"),
			UpgradeProcessFactory.addColumns(
				"OAuth2Application", "trustedApplication BOOLEAN"),
			UpgradeProcessFactory.addColumns(
				"OAuth2Authorization",
				"rememberDeviceContent VARCHAR(75) null"));

		registry.register(
			"3.1.0", "4.0.0", new OAuth2ApplicationFeatureUpgradeProcess());

		registry.register(
			"4.0.0", "4.0.1",
			UpgradeProcessFactory.alterColumnType(
				"OAuth2Application", "allowedGrantTypes", "VARCHAR(128) null"));

		registry.register(
			"4.0.1", "4.1.0",
			new OAuth2ApplicationClientAuthenticationMethodUpgradeProcess());

		registry.register(
			"4.1.0", "4.2.0",
			new BaseUuidUpgradeProcess() {

				@Override
				protected String[][] getTableAndPrimaryKeyColumnNames() {
					return new String[][] {
						{"OAuth2Application", "oAuth2ApplicationId"}
					};
				}

			});

		registry.register(
			"4.2.0", "4.2.1",
			new BaseExternalReferenceCodeUpgradeProcess() {

				@Override
				protected String[][] getTableAndPrimaryKeyColumnNames() {
					return new String[][] {
						{"OAuth2Application", "oAuth2ApplicationId"}
					};
				}

			});

		registry.register(
			"4.2.1", "4.2.2",
			new OAuth2ScopeGrantRemoveCompanyIdFromObjectsRelatedUpgradeProcess());
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ScopeLocator _scopeLocator;

}