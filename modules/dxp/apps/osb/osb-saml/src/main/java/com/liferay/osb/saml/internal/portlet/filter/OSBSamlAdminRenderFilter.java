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

package com.liferay.osb.saml.internal.portlet.filter;

import com.liferay.osb.saml.internal.configuration.OSBSamlConfiguration;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.saml.constants.SamlPortletKeys;
import com.liferay.saml.constants.SamlProviderConfigurationKeys;
import com.liferay.saml.runtime.configuration.SamlProviderConfiguration;
import com.liferay.saml.runtime.configuration.SamlProviderConfigurationHelper;
import com.liferay.saml.runtime.credential.KeyStoreManager;

import java.io.IOException;

import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.PortletFilter;
import javax.portlet.filter.RenderFilter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marta Medio
 */
@Component(
	configurationPid = "com.liferay.saml.runtime.configuration.SamlKeyStoreManagerConfiguration",
	immediate = true,
	property = "javax.portlet.name=" + SamlPortletKeys.SAML_ADMIN, service = {}
)
public class OSBSamlAdminRenderFilter implements RenderFilter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(
			RenderRequest renderRequest, RenderResponse renderResponse,
			FilterChain chain)
		throws IOException, PortletException {

		chain.doFilter(renderRequest, renderResponse);

		String mvcRenderCommandName = ParamUtil.getString(
			renderRequest, "mvcRenderCommandName", null);
		String tabs1 = ParamUtil.getString(renderRequest, "tabs1", "general");

		if (((mvcRenderCommandName != null) &&
			 !Objects.equals(mvcRenderCommandName, "/admin")) ||
			!Objects.equals(tabs1, "general")) {

			return;
		}

		try {
			OSBSamlConfiguration osbSamlConfiguration =
				ConfigurationProviderUtil.getCompanyConfiguration(
					OSBSamlConfiguration.class,
					_portal.getCompanyId(renderRequest));

			if (osbSamlConfiguration.productionEnvironment() ||
				Validator.isBlank(osbSamlConfiguration.preSharedKey()) ||
				Validator.isBlank(
					osbSamlConfiguration.targetInstanceImportURL())) {

				return;
			}
		}
		catch (ConfigurationException configurationException) {
			_log.error(
				"Unable to get SaaS instance configuration",
				configurationException);

			return;
		}

		SamlProviderConfiguration samlProviderConfiguration =
			_samlProviderConfigurationHelper.getSamlProviderConfiguration();

		if (!Objects.equals(
				SamlProviderConfigurationKeys.SAML_ROLE_SP,
				samlProviderConfiguration.role())) {

			return;
		}

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher("/export.jsp");

		try {
			requestDispatcher.include(
				_portal.getHttpServletRequest(renderRequest),
				_portal.getHttpServletResponse(renderResponse));
		}
		catch (Exception exception) {
			throw new PortletException(
				"Unable to include export.jsp", exception);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws PortletException {
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		if (Objects.equals(
				_keyStoreManagerServiceReference.getProperty("component.name"),
				"com.liferay.saml.opensaml.integration.internal.credential." +
					"DLKeyStoreManagerImpl")) {

			_serviceRegistration = bundleContext.registerService(
				PortletFilter.class, this, new HashMapDictionary<>(properties));
		}
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OSBSamlAdminRenderFilter.class);

	@Reference(name = "KeyStoreManager")
	private ServiceReference<KeyStoreManager> _keyStoreManagerServiceReference;

	@Reference
	private Portal _portal;

	@Reference
	private SamlProviderConfigurationHelper _samlProviderConfigurationHelper;

	private ServiceRegistration<?> _serviceRegistration;

	@Reference(target = "(osgi.web.symbolicname=com.liferay.osb.saml)")
	private ServletContext _servletContext;

}