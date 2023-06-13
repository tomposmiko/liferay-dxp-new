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

package com.liferay.portal.template.freemarker.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.template.BaseTemplateResourceCache;
import com.liferay.portal.template.freemarker.configuration.FreeMarkerEngineConfiguration;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Tina Tian
 */
@Component(
	configurationPid = "com.liferay.portal.template.freemarker.configuration.FreeMarkerEngineConfiguration",
	service = FreeMarkerTemplateResourceCache.class
)
public class FreeMarkerTemplateResourceCache extends BaseTemplateResourceCache {

	@Activate
	protected void activate(Map<String, Object> properties) {
		FreeMarkerEngineConfiguration freeMarkerEngineConfiguration =
			ConfigurableUtil.createConfigurable(
				FreeMarkerEngineConfiguration.class, properties);

		init(
			freeMarkerEngineConfiguration.resourceModificationCheck(),
			_PORTAL_CACHE_NAME,
			StringBundler.concat(
				TemplateResource.class.getName(), StringPool.POUND,
				TemplateConstants.LANG_TYPE_FTL));
	}

	@Deactivate
	protected void deactivate() {
		destroy();
	}

	@Modified
	protected void modified(Map<String, Object> properties) {
		FreeMarkerEngineConfiguration freeMarkerEngineConfiguration =
			ConfigurableUtil.createConfigurable(
				FreeMarkerEngineConfiguration.class, properties);

		setModificationCheckInterval(
			freeMarkerEngineConfiguration.resourceModificationCheck());
	}

	private static final String _PORTAL_CACHE_NAME =
		FreeMarkerTemplateResourceCache.class.getName();

}