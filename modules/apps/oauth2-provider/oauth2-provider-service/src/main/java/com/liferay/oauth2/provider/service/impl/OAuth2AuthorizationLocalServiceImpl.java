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

package com.liferay.oauth2.provider.service.impl;

import com.liferay.oauth2.provider.exception.NoSuchOAuth2AuthorizationException;
import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.model.OAuth2ScopeGrant;
import com.liferay.oauth2.provider.service.base.OAuth2AuthorizationLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public class OAuth2AuthorizationLocalServiceImpl
	extends OAuth2AuthorizationLocalServiceBaseImpl {

	@Override
	public OAuth2Authorization addOAuth2Authorization(
		long companyId, long userId, String userName, long oAuth2ApplicationId,
		long oAuth2ApplicationScopeAliasesId, String accessTokenContent,
		Date accessTokenCreateDate, Date accessTokenExpirationDate,
		String remoteIPInfo, String refreshTokenContent,
		Date refreshTokenCreateDate, Date refreshTokenExpirationDate) {

		long oAuth2AuthorizationId = counterLocalService.increment(
			OAuth2Authorization.class.getName());

		OAuth2Authorization oAuth2Authorization = createOAuth2Authorization(
			oAuth2AuthorizationId);

		oAuth2Authorization.setCompanyId(companyId);
		oAuth2Authorization.setUserId(userId);
		oAuth2Authorization.setUserName(userName);
		oAuth2Authorization.setCreateDate(new Date());
		oAuth2Authorization.setOAuth2ApplicationId(oAuth2ApplicationId);
		oAuth2Authorization.setOAuth2ApplicationScopeAliasesId(
			oAuth2ApplicationScopeAliasesId);
		oAuth2Authorization.setAccessTokenContent(accessTokenContent);
		oAuth2Authorization.setAccessTokenCreateDate(accessTokenCreateDate);
		oAuth2Authorization.setAccessTokenExpirationDate(
			accessTokenExpirationDate);
		oAuth2Authorization.setRemoteIPInfo(remoteIPInfo);
		oAuth2Authorization.setRefreshTokenContent(refreshTokenContent);
		oAuth2Authorization.setRefreshTokenCreateDate(refreshTokenCreateDate);
		oAuth2Authorization.setRefreshTokenExpirationDate(
			refreshTokenExpirationDate);

		return oAuth2AuthorizationPersistence.update(oAuth2Authorization);
	}

	@Override
	public OAuth2Authorization deleteOAuth2Authorization(
			long oAuth2AuthorizationId)
		throws PortalException {

		Collection<OAuth2ScopeGrant> oAuth2ScopeGrants =
			oAuth2AuthorizationLocalService.getOAuth2ScopeGrants(
				oAuth2AuthorizationId);

		for (OAuth2ScopeGrant oAuth2ScopeGrant : oAuth2ScopeGrants) {
			oAuth2ScopeGrantLocalService.deleteOAuth2ScopeGrant(
				oAuth2ScopeGrant);
		}

		return oAuth2AuthorizationPersistence.remove(oAuth2AuthorizationId);
	}

	@Override
	public OAuth2Authorization fetchOAuth2AuthorizationByAccessTokenContent(
		String accessTokenContent) {

		return oAuth2AuthorizationPersistence.fetchByAccessTokenContent(
			accessTokenContent);
	}

	@Override
	public OAuth2Authorization fetchOAuth2AuthorizationByRefreshTokenContent(
		String refreshTokenContent) {

		return oAuth2AuthorizationPersistence.fetchByRefreshTokenContent(
			refreshTokenContent);
	}

	@Override
	public OAuth2Authorization getOAuth2AuthorizationByAccessTokenContent(
			String accessTokenContent)
		throws NoSuchOAuth2AuthorizationException {

		return oAuth2AuthorizationPersistence.findByAccessTokenContent(
			accessTokenContent);
	}

	@Override
	public OAuth2Authorization getOAuth2AuthorizationByRefreshTokenContent(
			String refreshTokenContent)
		throws NoSuchOAuth2AuthorizationException {

		return oAuth2AuthorizationPersistence.findByRefreshTokenContent(
			refreshTokenContent);
	}

	@Override
	public List<OAuth2Authorization> getOAuth2Authorizations(
		long oAuth2ApplicationId, int start, int end,
		OrderByComparator<OAuth2Authorization> orderByComparator) {

		return oAuth2AuthorizationPersistence.findByOAuth2ApplicationId(
			oAuth2ApplicationId, start, end, orderByComparator);
	}

	@Override
	public int getOAuth2AuthorizationsCount(long oAuth2ApplicationId) {
		return oAuth2AuthorizationPersistence.countByOAuth2ApplicationId(
			oAuth2ApplicationId);
	}

	@Override
	public Collection<OAuth2ScopeGrant> getOAuth2ScopeGrants(
		long oAuth2AuthorizationId) {

		return oAuth2AuthorizationPersistence.getOAuth2ScopeGrants(
			oAuth2AuthorizationId);
	}

	@Override
	public List<OAuth2Authorization> getUserOAuth2Authorizations(
		long userId, int start, int end,
		OrderByComparator<OAuth2Authorization> orderByComparator) {

		return oAuth2AuthorizationPersistence.findByUserId(
			userId, start, end, orderByComparator);
	}

	@Override
	public int getUserOAuth2AuthorizationsCount(long userId) {
		return oAuth2AuthorizationPersistence.countByUserId(userId);
	}

}