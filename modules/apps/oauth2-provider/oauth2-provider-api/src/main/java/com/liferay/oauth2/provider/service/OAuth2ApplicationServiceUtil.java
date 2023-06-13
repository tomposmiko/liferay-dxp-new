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

package com.liferay.oauth2.provider.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for OAuth2Application. This utility wraps
 * <code>com.liferay.oauth2.provider.service.impl.OAuth2ApplicationServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2ApplicationService
 * @generated
 */
public class OAuth2ApplicationServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.oauth2.provider.service.impl.OAuth2ApplicationServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link OAuth2ApplicationServiceUtil} to access the o auth2 application remote service. Add custom service methods to <code>com.liferay.oauth2.provider.service.impl.OAuth2ApplicationServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static com.liferay.oauth2.provider.model.OAuth2Application
			addOAuth2Application(
				java.util.List<com.liferay.oauth2.provider.constants.GrantType>
					allowedGrantTypesList,
				long clientCredentialUserId, String clientId, int clientProfile,
				String clientSecret, String description,
				java.util.List<String> featuresList, String homePageURL,
				long iconFileEntryId, String name, String privacyPolicyURL,
				java.util.List<String> redirectURIsList,
				java.util.List<String> scopeAliasesList,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addOAuth2Application(
			allowedGrantTypesList, clientCredentialUserId, clientId,
			clientProfile, clientSecret, description, featuresList, homePageURL,
			iconFileEntryId, name, privacyPolicyURL, redirectURIsList,
			scopeAliasesList, serviceContext);
	}

	/**
	 * @deprecated As of Mueller (7.2.x)
	 */
	@Deprecated
	public static com.liferay.oauth2.provider.model.OAuth2Application
			addOAuth2Application(
				java.util.List<com.liferay.oauth2.provider.constants.GrantType>
					allowedGrantTypesList,
				String clientId, int clientProfile, String clientSecret,
				String description, java.util.List<String> featuresList,
				String homePageURL, long iconFileEntryId, String name,
				String privacyPolicyURL,
				java.util.List<String> redirectURIsList,
				java.util.List<String> scopeAliasesList,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addOAuth2Application(
			allowedGrantTypesList, clientId, clientProfile, clientSecret,
			description, featuresList, homePageURL, iconFileEntryId, name,
			privacyPolicyURL, redirectURIsList, scopeAliasesList,
			serviceContext);
	}

	public static com.liferay.oauth2.provider.model.OAuth2Application
			deleteOAuth2Application(long oAuth2ApplicationId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteOAuth2Application(oAuth2ApplicationId);
	}

	public static com.liferay.oauth2.provider.model.OAuth2Application
			fetchOAuth2Application(long companyId, String clientId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().fetchOAuth2Application(companyId, clientId);
	}

	public static com.liferay.oauth2.provider.model.OAuth2Application
			getOAuth2Application(long oAuth2ApplicationId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getOAuth2Application(oAuth2ApplicationId);
	}

	public static com.liferay.oauth2.provider.model.OAuth2Application
			getOAuth2Application(long companyId, String clientId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getOAuth2Application(companyId, clientId);
	}

	public static java.util.List
		<com.liferay.oauth2.provider.model.OAuth2Application>
			getOAuth2Applications(
				long companyId, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.oauth2.provider.model.OAuth2Application>
						orderByComparator) {

		return getService().getOAuth2Applications(
			companyId, start, end, orderByComparator);
	}

	public static int getOAuth2ApplicationsCount(long companyId) {
		return getService().getOAuth2ApplicationsCount(companyId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.oauth2.provider.model.OAuth2Application
			updateIcon(
				long oAuth2ApplicationId, java.io.InputStream inputStream)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateIcon(oAuth2ApplicationId, inputStream);
	}

	public static com.liferay.oauth2.provider.model.OAuth2Application
			updateOAuth2Application(
				long oAuth2ApplicationId,
				java.util.List<com.liferay.oauth2.provider.constants.GrantType>
					allowedGrantTypesList,
				long clientCredentialUserId, String clientId, int clientProfile,
				String clientSecret, String description,
				java.util.List<String> featuresList, String homePageURL,
				long iconFileEntryId, String name, String privacyPolicyURL,
				java.util.List<String> redirectURIsList,
				long auth2ApplicationScopeAliasesId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateOAuth2Application(
			oAuth2ApplicationId, allowedGrantTypesList, clientCredentialUserId,
			clientId, clientProfile, clientSecret, description, featuresList,
			homePageURL, iconFileEntryId, name, privacyPolicyURL,
			redirectURIsList, auth2ApplicationScopeAliasesId, serviceContext);
	}

	/**
	 * @deprecated As of Mueller (7.2.x)
	 */
	@Deprecated
	public static com.liferay.oauth2.provider.model.OAuth2Application
			updateOAuth2Application(
				long oAuth2ApplicationId,
				java.util.List<com.liferay.oauth2.provider.constants.GrantType>
					allowedGrantTypesList,
				String clientId, int clientProfile, String clientSecret,
				String description, java.util.List<String> featuresList,
				String homePageURL, long iconFileEntryId, String name,
				String privacyPolicyURL,
				java.util.List<String> redirectURIsList,
				long auth2ApplicationScopeAliasesId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateOAuth2Application(
			oAuth2ApplicationId, allowedGrantTypesList, clientId, clientProfile,
			clientSecret, description, featuresList, homePageURL,
			iconFileEntryId, name, privacyPolicyURL, redirectURIsList,
			auth2ApplicationScopeAliasesId, serviceContext);
	}

	public static com.liferay.oauth2.provider.model.OAuth2Application
			updateScopeAliases(
				long oAuth2ApplicationId,
				java.util.List<String> scopeAliasesList)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateScopeAliases(
			oAuth2ApplicationId, scopeAliasesList);
	}

	public static OAuth2ApplicationService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<OAuth2ApplicationService, OAuth2ApplicationService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(OAuth2ApplicationService.class);

		ServiceTracker<OAuth2ApplicationService, OAuth2ApplicationService>
			serviceTracker =
				new ServiceTracker
					<OAuth2ApplicationService, OAuth2ApplicationService>(
						bundle.getBundleContext(),
						OAuth2ApplicationService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}