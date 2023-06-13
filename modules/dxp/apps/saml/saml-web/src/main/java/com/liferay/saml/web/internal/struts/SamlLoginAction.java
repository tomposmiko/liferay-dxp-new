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

package com.liferay.saml.web.internal.struts;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.saml.constants.SamlWebKeys;
import com.liferay.saml.persistence.model.SamlSpIdpConnection;
import com.liferay.saml.persistence.service.SamlSpIdpConnectionLocalService;
import com.liferay.saml.runtime.configuration.SamlProviderConfiguration;
import com.liferay.saml.runtime.configuration.SamlProviderConfigurationHelper;
import com.liferay.saml.runtime.servlet.profile.SamlSpIdpConnectionsProfile;
import com.liferay.saml.util.JspUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Stian Sigvartsen
 */
@Component(property = "path=/portal/saml/login", service = StrutsAction.class)
public class SamlLoginAction extends BaseSamlStrutsAction {

	@Override
	public boolean isEnabled() {
		if (_samlProviderConfigurationHelper.isRoleSp()) {
			return _samlProviderConfigurationHelper.isEnabled();
		}

		return false;
	}

	@Override
	protected String doExecute(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		String entityId = ParamUtil.getString(
			httpServletRequest, "idpEntityId");

		long companyId = _portal.getCompanyId(httpServletRequest);

		if (Validator.isNotNull(entityId)) {
			httpServletRequest.setAttribute(
				SamlWebKeys.SAML_SP_IDP_CONNECTION,
				_samlSpIdpConnectionLocalService.getSamlSpIdpConnection(
					companyId, entityId));

			if (GetterUtil.getBoolean(
					ParamUtil.getBoolean(httpServletRequest, "forceAuthn"))) {

				AuthTokenUtil.checkCSRFToken(
					httpServletRequest, SamlLoginAction.class.getName());

				httpServletRequest.setAttribute(
					SamlWebKeys.FORCE_REAUTHENTICATION, Boolean.TRUE);
			}

			return null;
		}

		List<SamlSpIdpConnection> samlSpIdpConnections =
			_samlSpIdpConnectionLocalService.getSamlSpIdpConnections(companyId);

		Stream<SamlSpIdpConnection> stream = samlSpIdpConnections.stream();

		samlSpIdpConnections = stream.filter(
			samlSpIdpConnection -> isEnabled(
				samlSpIdpConnection, httpServletRequest)
		).collect(
			Collectors.toList()
		);

		if (samlSpIdpConnections.isEmpty()) {
			SamlProviderConfiguration samlProviderConfiguration =
				_samlProviderConfigurationHelper.getSamlProviderConfiguration();

			if (samlProviderConfiguration.allowShowingTheLoginPortlet()) {
				return null;
			}
		}

		boolean samlIdpRedirectMessageEnabled = GetterUtil.getBoolean(
			_props.get("saml.idp.redirect.message.enabled"), true);

		if (samlIdpRedirectMessageEnabled) {
			httpServletRequest.setAttribute(
				SamlWebKeys.SAML_IDP_REDIRECT_MESSAGE,
				_language.get(
					httpServletRequest,
					"redirecting-to-your-identity-provider"));
		}

		httpServletRequest.setAttribute(
			SamlWebKeys.SAML_SSO_LOGIN_CONTEXT,
			_toJSONObject(samlSpIdpConnections));

		JspUtil.dispatch(
			httpServletRequest, httpServletResponse,
			"/portal/saml/select_idp.jsp",
			"please-select-your-identity-provider",
			!samlIdpRedirectMessageEnabled);

		return null;
	}

	protected boolean isEnabled(
		SamlSpIdpConnection samlSpIdpConnection,
		HttpServletRequest httpServletRequest) {

		if (_samlSpIdpConnectionsProfile != null) {
			return _samlSpIdpConnectionsProfile.isEnabled(
				samlSpIdpConnection, httpServletRequest);
		}

		return samlSpIdpConnection.isEnabled();
	}

	private JSONObject _toJSONObject(
		List<SamlSpIdpConnection> samlSpIdpConnections) {

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		for (SamlSpIdpConnection samlSpIdpConnection : samlSpIdpConnections) {
			jsonArray.put(
				JSONUtil.put(
					"enabled", samlSpIdpConnection.isEnabled()
				).put(
					"entityId", samlSpIdpConnection.getSamlIdpEntityId()
				).put(
					"name", samlSpIdpConnection.getName()
				));
		}

		return JSONUtil.put("relevantIdpConnections", jsonArray);
	}

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference
	private Props _props;

	@Reference
	private SamlProviderConfigurationHelper _samlProviderConfigurationHelper;

	@Reference
	private SamlSpIdpConnectionLocalService _samlSpIdpConnectionLocalService;

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile SamlSpIdpConnectionsProfile _samlSpIdpConnectionsProfile;

}