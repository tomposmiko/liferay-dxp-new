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

package com.liferay.osb.saml.internal.portlet.action;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.osb.saml.internal.configuration.OSBSamlConfiguration;
import com.liferay.osb.saml.internal.util.SymmetricEncryptor;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.saml.constants.SamlPortletKeys;
import com.liferay.saml.persistence.model.SamlSpIdpConnection;
import com.liferay.saml.persistence.service.SamlSpIdpConnectionLocalService;
import com.liferay.saml.runtime.configuration.SamlConfiguration;
import com.liferay.saml.runtime.configuration.SamlProviderConfiguration;
import com.liferay.saml.runtime.configuration.SamlProviderConfigurationHelper;
import com.liferay.saml.runtime.credential.KeyStoreManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marta Medio
 */
@Component(
	configurationPid = "com.liferay.saml.runtime.configuration.SamlConfiguration",
	immediate = true,
	property = {
		"javax.portlet.name=" + SamlPortletKeys.SAML_ADMIN,
		"mvc.command.name=/saml/saas/admin/export"
	},
	service = MVCActionCommand.class
)
public class ExportOSBSamlMVCActionCommand extends BaseMVCActionCommand {

	@Activate
	protected void activate(Map<String, Object> properties) {
		_samlConfiguration = ConfigurableUtil.createConfigurable(
			SamlConfiguration.class, properties);
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long companyId = _portal.getCompanyId(actionRequest);

		OSBSamlConfiguration osbSamlConfiguration =
			ConfigurationProviderUtil.getCompanyConfiguration(
				OSBSamlConfiguration.class, companyId);

		if (osbSamlConfiguration.productionEnvironment() ||
			Validator.isBlank(osbSamlConfiguration.preSharedKey()) ||
			Validator.isBlank(osbSamlConfiguration.targetInstanceImportURL())) {

			return;
		}

		try {
			Client client = _clientBuilder.build();

			WebTarget webTarget = client.target(
				UriBuilder.fromUri(
					osbSamlConfiguration.targetInstanceImportURL()));

			String json = webTarget.request(
				MediaType.APPLICATION_JSON
			).post(
				Entity.entity(
					_getEncryptedJSONPayload(
						companyId, osbSamlConfiguration.preSharedKey()),
					MediaType.TEXT_PLAIN),
				String.class
			);

			if (json != null) {
				JSONObject jsonObject = JSONFactoryUtil.createJSONObject(json);

				if (Objects.equals(jsonObject.get("result"), "resultError")) {
					SessionErrors.add(actionRequest, "exportError");
				}
			}
		}
		catch (Exception exception) {
			_log.error("Unable to export SAML configuration data", exception);

			SessionErrors.add(actionRequest, "exportError");
		}
	}

	private String _getEncryptedJSONPayload(long companyId, String preSharedKey)
		throws Exception {

		JSONObject jsonObject;

		try {
			jsonObject = JSONUtil.put("samlKeystore", _getKeyStore());
		}
		catch (Exception exception) {
			_log.error(
				"Unable to export the SAML KeyStore for companyId " + companyId,
				exception);

			throw exception;
		}

		jsonObject.put(
			"samlProviderConfiguration",
			_getSamlProviderConfigurationJSONObject()
		).put(
			"samlSpIdpConnections", _getSamlSpIdpConnectionsJSONArray(companyId)
		);

		try {
			return SymmetricEncryptor.encryptData(
				preSharedKey, jsonObject.toString());
		}
		catch (Exception exception) {
			_log.error("Unable to encrypt the JSON payload", exception);

			throw exception;
		}
	}

	private String _getKeyStore()
		throws CertificateException, IOException, KeyStoreException,
			   NoSuchAlgorithmException {

		KeyStore keyStore = _keyStoreManager.getKeyStore();
		ByteArrayOutputStream byteArrayOutputStream =
			new ByteArrayOutputStream();
		String keyStorePassword = _samlConfiguration.keyStorePassword();

		keyStore.store(byteArrayOutputStream, keyStorePassword.toCharArray());

		return Base64.encode(byteArrayOutputStream.toByteArray());
	}

	private JSONObject _getSamlProviderConfigurationJSONObject() {
		SamlProviderConfiguration samlProviderConfiguration =
			_samlProviderConfigurationHelper.getSamlProviderConfiguration();

		return JSONUtil.put(
			"saml.entity.id", samlProviderConfiguration.entityId()
		).put(
			"saml.idp.assertion.lifetime",
			samlProviderConfiguration.defaultAssertionLifetime()
		).put(
			"saml.idp.authn.request.signature.required",
			samlProviderConfiguration.authnRequestSignatureRequired()
		).put(
			"saml.idp.session.maximum.age",
			samlProviderConfiguration.sessionMaximumAge()
		).put(
			"saml.idp.session.timeout",
			samlProviderConfiguration.sessionTimeout()
		).put(
			"saml.keystore.credential.password",
			samlProviderConfiguration.keyStoreCredentialPassword()
		).put(
			"saml.keystore.encryption.credential.password",
			samlProviderConfiguration.keyStoreEncryptionCredentialPassword()
		).put(
			"saml.role", samlProviderConfiguration.role()
		).put(
			"saml.sign.metadata", samlProviderConfiguration.signMetadata()
		).put(
			"saml.sp.allow.showing.the.login.portlet",
			samlProviderConfiguration.allowShowingTheLoginPortlet()
		).put(
			"saml.sp.assertion.signature.required",
			samlProviderConfiguration.assertionSignatureRequired()
		).put(
			"saml.sp.clock.skew", samlProviderConfiguration.clockSkew()
		).put(
			"saml.sp.ldap.import.enabled",
			samlProviderConfiguration.ldapImportEnabled()
		).put(
			"saml.sp.sign.authn.request",
			samlProviderConfiguration.signAuthnRequest()
		).put(
			"saml.ssl.required", samlProviderConfiguration.sslRequired()
		);
	}

	private JSONObject _getSamlSpIdpConnectionExpandoValuesJSONObject(
		SamlSpIdpConnection samlSpIdpConnection) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		ExpandoBridge expandoBridge = samlSpIdpConnection.getExpandoBridge();

		Enumeration<String> enumeration = expandoBridge.getAttributeNames();

		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();

			jsonObject.put(name, expandoBridge.getAttribute(name, false));
		}

		return jsonObject;
	}

	private JSONArray _getSamlSpIdpConnectionsJSONArray(long companyId) {
		JSONArray samlSpIdpConnectionsJSONArray =
			JSONFactoryUtil.createJSONArray();

		List<SamlSpIdpConnection> samlSpIdpConnectionsList =
			_samlSpIdpConnectionLocalService.getSamlSpIdpConnections(companyId);

		for (SamlSpIdpConnection samlSpIdpConnection :
				samlSpIdpConnectionsList) {

			samlSpIdpConnectionsJSONArray.put(
				JSONUtil.put(
					"assertionSignatureRequired",
					samlSpIdpConnection.isAssertionSignatureRequired()
				).put(
					"clockSkew", samlSpIdpConnection.getClockSkew()
				).put(
					"enabled", samlSpIdpConnection.isEnabled()
				).put(
					"expandoValues",
					_getSamlSpIdpConnectionExpandoValuesJSONObject(
						samlSpIdpConnection)
				).put(
					"forceAuthn", samlSpIdpConnection.isForceAuthn()
				).put(
					"ldapImportEnabled",
					samlSpIdpConnection.isLdapImportEnabled()
				).put(
					"metadataUrl", samlSpIdpConnection.getMetadataUrl()
				).put(
					"metadataXml", samlSpIdpConnection.getMetadataXml()
				).put(
					"name", samlSpIdpConnection.getName()
				).put(
					"nameIdFormat", samlSpIdpConnection.getNameIdFormat()
				).put(
					"samlIdpEntityId", samlSpIdpConnection.getSamlIdpEntityId()
				).put(
					"samlSpIdpConnectionId",
					samlSpIdpConnection.getSamlSpIdpConnectionId()
				).put(
					"signAuthnRequest", samlSpIdpConnection.isSignAuthnRequest()
				).put(
					"unknownUsersAreStrangers",
					samlSpIdpConnection.isUnknownUsersAreStrangers()
				).put(
					"userAttributeMappings",
					samlSpIdpConnection.getUserAttributeMappings()
				));
		}

		return samlSpIdpConnectionsJSONArray;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ExportOSBSamlMVCActionCommand.class);

	@Reference
	private ClientBuilder _clientBuilder;

	@Reference(name = "KeyStoreManager")
	private KeyStoreManager _keyStoreManager;

	@Reference
	private Portal _portal;

	private SamlConfiguration _samlConfiguration;

	@Reference
	private SamlProviderConfigurationHelper _samlProviderConfigurationHelper;

	@Reference
	private SamlSpIdpConnectionLocalService _samlSpIdpConnectionLocalService;

}