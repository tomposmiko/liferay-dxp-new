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

package com.liferay.osb.faro.web.internal.controller.main;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.oauth2.provider.constants.ClientProfile;
import com.liferay.oauth2.provider.constants.GrantType;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.service.OAuth2AuthorizationService;
import com.liferay.osb.faro.web.internal.application.ApiApplication;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.FaroController;
import com.liferay.osb.faro.web.internal.model.display.main.TokenDisplay;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.io.BigEndianCodec;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.SecureRandomUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.Portal;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true, service = {FaroController.class, OAuth2Controller.class}
)
@Path("/{groupId}/oauth2")
@Produces(MediaType.APPLICATION_JSON)
public class OAuth2Controller extends BaseFaroController {

	@GET
	@Path("/tokens")
	@RolesAllowed(RoleConstants.SITE_ADMINISTRATOR)
	public List<TokenDisplay> getTokens(@PathParam("groupId") long groupId)
		throws Exception {

		List<OAuth2Authorization> userOAuth2Authorizations =
			_getUserOAuth2AuthorizationsByGroupId(groupId);

		Stream<OAuth2Authorization> stream = userOAuth2Authorizations.stream();

		return stream.map(
			this::_mapTokenDisplay
		).collect(
			Collectors.toList()
		);
	}

	@Path("/tokens/new")
	@POST
	@RolesAllowed(RoleConstants.SITE_ADMINISTRATOR)
	public TokenDisplay newToken(
			@PathParam("groupId") long groupId,
			@Context HttpServletRequest httpServletRequest)
		throws Exception {

		OAuth2Application oAuth2Application = _getOrCreateOAuth2Application(
			httpServletRequest);

		JSONObject jsonObject = _jsonFactory.createJSONObject(
			_invokeOAuth2Endpoint(
				oAuth2Application.getClientId(),
				oAuth2Application.getClientSecret()));

		OAuth2Authorization oAuth2Authorization =
			_fetchUserOAuth2AuthorizationByAccessToken(
				jsonObject.getString("access_token"));

		_setOAuth2AuthorizationGroupId(groupId, oAuth2Authorization);

		return _mapTokenDisplay(oAuth2Authorization);
	}

	@Path("/tokens/{token}/revoke")
	@POST
	@RolesAllowed(RoleConstants.SITE_ADMINISTRATOR)
	public void revokeToken(
			@PathParam("groupId") long groupId,
			@PathParam("token") String token)
		throws Exception {

		OAuth2Authorization oAuth2Authorization =
			_fetchUserOAuth2AuthorizationByAccessToken(token);

		if (oAuth2Authorization == null) {
			throw new IllegalArgumentException(
				"Unable to revoke OAuth2 authorization with token " + token);
		}

		_oAuth2AuthorizationService.revokeOAuth2Authorization(
			oAuth2Authorization.getOAuth2AuthorizationId());
	}

	private OAuth2Authorization _fetchUserOAuth2AuthorizationByAccessToken(
			String accessToken)
		throws Exception {

		List<OAuth2Authorization> userOAuth2Authorizations =
			_oAuth2AuthorizationService.getUserOAuth2Authorizations(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		for (OAuth2Authorization userOAuth2Authorization :
				userOAuth2Authorizations) {

			if (Objects.equals(
					userOAuth2Authorization.getAccessTokenContent(),
					accessToken)) {

				return userOAuth2Authorization;
			}
		}

		return null;
	}

	private boolean _filterOAuth2AuthorizationByGroupId(
		long groupId, OAuth2Authorization oAuth2Authorization) {

		ExpandoBridge expandoBridge = oAuth2Authorization.getExpandoBridge();

		if (Objects.equals(
				groupId, expandoBridge.getAttribute("groupId", false))) {

			return true;
		}

		return false;
	}

	private String _generateApplicationName() {
		Matcher matcher = _baseIdPattern.matcher(_generateSecureRandomString());

		return matcher.replaceFirst("app-$1-$2-$3-$4-$5");
	}

	private String _generateClientSecret() {
		Matcher matcher = _baseIdPattern.matcher(_generateSecureRandomString());

		return matcher.replaceFirst("secret-$1-$2-$3-$4-$5");
	}

	private String _generateSecureRandomString() {
		int size = 16;

		StringBundler sb = new StringBundler(size);

		int count = (int)Math.ceil((double)size / 8);

		byte[] buffer = new byte[count * 8];

		for (int i = 0; i < count; i++) {
			BigEndianCodec.putLong(buffer, i * 8, SecureRandomUtil.nextLong());
		}

		for (int i = 0; i < size; i++) {
			sb.append(Integer.toHexString(0xFF & buffer[i]));
		}

		return sb.toString();
	}

	private OAuth2Application _getOrCreateOAuth2Application(
			HttpServletRequest httpServletRequest)
		throws Exception {

		User user = getUser();

		OAuth2Application oAuth2Application =
			_oAuth2ApplicationLocalService.fetchOAuth2Application(
				getCompanyId(), user.getEmailAddress());

		if (oAuth2Application != null) {
			return oAuth2Application;
		}

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			OAuth2Application.class.getName(), httpServletRequest);

		ClientProfile clientProfile = ClientProfile.HEADLESS_SERVER;

		return _oAuth2ApplicationLocalService.addOAuth2Application(
			user.getCompanyId(), user.getUserId(), user.getFullName(),
			Arrays.asList(GrantType.CLIENT_CREDENTIALS), user.getEmailAddress(),
			clientProfile.id(), _generateClientSecret(), StringPool.BLANK,
			Collections.emptyList(), StringPool.BLANK, 0,
			_generateApplicationName(), StringPool.BLANK,
			Collections.emptyList(),
			Arrays.asList(
				ApiApplication.OAuth2ScopeAliases.RECOMMENDATIONS_EVERYTHING,
				ApiApplication.OAuth2ScopeAliases.REPORTS_EVERYTHING),
			serviceContext);
	}

	private List<OAuth2Authorization> _getUserOAuth2AuthorizationsByGroupId(
			long groupId)
		throws Exception {

		List<OAuth2Authorization> userOAuth2Authorizations =
			_oAuth2AuthorizationService.getUserOAuth2Authorizations(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Stream<OAuth2Authorization> stream = userOAuth2Authorizations.stream();

		return stream.filter(
			oAuth2Authorization -> _filterOAuth2AuthorizationByGroupId(
				groupId, oAuth2Authorization)
		).collect(
			Collectors.toList()
		);
	}

	private String _invokeOAuth2Endpoint(String clientId, String clientSecret)
		throws Exception {

		Client client = ClientBuilder.newClient();

		WebTarget webTarget = client.target(
			String.format(_O_AUTH2_ENDPOINT_TEMPLATE, _FARO_URL));

		Invocation.Builder builder = webTarget.request(
			MediaType.APPLICATION_FORM_URLENCODED);

		builder.accept(MediaType.APPLICATION_JSON);

		Form form = new Form();

		form.param("grant_type", "client_credentials");
		form.param("client_id", clientId);
		form.param("client_secret", clientSecret);

		Invocation invocation = builder.buildPost(Entity.form(form));

		Future<String> future = invocation.submit(String.class);

		return future.get(3, TimeUnit.SECONDS);
	}

	private TokenDisplay _mapTokenDisplay(
		OAuth2Authorization oAuth2Authorization) {

		Date expirationDate =
			oAuth2Authorization.getRefreshTokenExpirationDate();

		if (expirationDate == null) {
			expirationDate = oAuth2Authorization.getAccessTokenExpirationDate();
		}

		return new TokenDisplay(
			oAuth2Authorization.getCreateDate(), expirationDate,
			oAuth2Authorization.getAccessTokenCreateDate(),
			oAuth2Authorization.getAccessTokenContent());
	}

	private void _setOAuth2AuthorizationGroupId(
		long groupId, OAuth2Authorization oAuth2Authorization) {

		ExpandoBridge expandoBridge = oAuth2Authorization.getExpandoBridge();

		expandoBridge.setAttribute("groupId", groupId, false);
	}

	private static final String _FARO_URL = System.getenv("FARO_URL");

	private static final String _O_AUTH2_ENDPOINT_TEMPLATE =
		"%s/o/oauth2/token";

	private static final Pattern _baseIdPattern = Pattern.compile(
		"(.{8})(.{4})(.{4})(.{4})(.*)");

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	@Reference
	private OAuth2AuthorizationService _oAuth2AuthorizationService;

	@Reference
	private Portal _portal;

}