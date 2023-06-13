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

package com.liferay.knowledge.base.web.internal.portal.settings.configuration.admin.display;

import com.liferay.configuration.admin.display.ConfigurationScreen;
import com.liferay.knowledge.base.configuration.KBServiceConfigurationProvider;
import com.liferay.knowledge.base.web.internal.display.context.KBArticleCompanyConfigurationDisplayContext;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.Portal;

import java.io.IOException;

import java.util.Locale;

import javax.portlet.PortletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alicia García
 */
@Component(service = ConfigurationScreen.class)
public class KBConfigurationScreen implements ConfigurationScreen {

	@Override
	public String getCategoryKey() {
		return "knowledge-base";
	}

	@Override
	public String getKey() {
		return "knowledge-base-service";
	}

	@Override
	public String getName(Locale locale) {
		return _language.get(
			locale, "knowledge-base-service-configuration-name");
	}

	@Override
	public String getScope() {
		return ExtendedObjectClassDefinition.Scope.SYSTEM.getValue();
	}

	@Override
	public boolean isVisible() {
		if (FeatureFlagManagerUtil.isEnabled("LPS-165476")) {
			return true;
		}

		return false;
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		try {
			httpServletRequest.setAttribute(
				KBArticleCompanyConfigurationDisplayContext.class.getName(),
				new KBArticleCompanyConfigurationDisplayContext(
					httpServletRequest, _kbServiceConfigurationProvider,
					_portal.getLiferayPortletResponse(
						(PortletResponse)httpServletRequest.getAttribute(
							JavaConstants.JAVAX_PORTLET_RESPONSE))));

			RequestDispatcher requestDispatcher =
				_servletContext.getRequestDispatcher(
					"/admin/knowledge_base_settings" +
						"/kb_article_expiration_date_configuration.jsp");

			requestDispatcher.include(httpServletRequest, httpServletResponse);
		}
		catch (Exception exception) {
			throw new IOException(
				"Unable to render kb_article_expiration_date_configuration.jsp",
				exception);
		}
	}

	@Reference
	private KBServiceConfigurationProvider _kbServiceConfigurationProvider;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.knowledge.base.web)"
	)
	private ServletContext _servletContext;

}