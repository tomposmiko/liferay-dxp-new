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

package com.liferay.portal.k8s.agent.internal.model.listener;

import com.liferay.petra.string.StringUtil;
import com.liferay.portal.k8s.agent.PortalK8sConfigMapModifier;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.VirtualHost;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.VirtualHostLocalService;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Raymond Augé
 */
@Component(
	configurationPid = "com.liferay.portal.k8s.agent.configuration.v1.PortalK8sAgentConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true,
	service = ModelListener.class
)
public class VirtualHostModelListener extends BaseModelListener<VirtualHost> {

	@Override
	public void onAfterCreate(VirtualHost virtualHost)
		throws ModelListenerException {

		Company company = _companyLocalService.fetchCompanyById(
			virtualHost.getCompanyId());

		String webId = company.getWebId();

		if (Objects.equals(webId, PropsValues.COMPANY_DEFAULT_WEB_ID)) {
			return;
		}

		List<String> virtualHostNames = new ArrayList<>();

		for (VirtualHost curVirtualHost :
				_virtualHostLocalService.getVirtualHosts(
					company.getCompanyId())) {

			virtualHostNames.add(curVirtualHost.getHostname());
		}

		_portalK8sConfigMapModifier.modifyConfigMap(
			model -> {
				Map<String, String> data = model.data();

				data.put(
					"com.liferay.lxc.dxp.mainDomain",
					company.getVirtualHostname());
				data.put(
					"com.liferay.lxc.dxp.domains",
					StringUtil.merge(virtualHostNames, "\n"));

				Map<String, String> labels = model.labels();

				labels.put("lxc.liferay.com/metadataType", "dxp");
				labels.put("dxp.lxc.liferay.com/virtualInstanceId", webId);
			},
			webId.concat("-lxc-dxp-metadata"));
	}

	@Override
	public void onAfterRemove(VirtualHost virtualHost)
		throws ModelListenerException {

		Company company = _companyLocalService.fetchCompanyById(
			virtualHost.getCompanyId());

		String webId = company.getWebId();

		if (Objects.equals(webId, PropsValues.COMPANY_DEFAULT_WEB_ID)) {
			return;
		}

		_portalK8sConfigMapModifier.modifyConfigMap(
			model -> {
				Map<String, String> data = model.data();

				data.clear();

				Map<String, String> labels = model.labels();

				labels.clear();
			},
			webId.concat("-lxc-dxp-metadata"));
	}

	@Override
	public void onAfterUpdate(
			VirtualHost originalVirtualHost, VirtualHost virtualHost)
		throws ModelListenerException {

		onAfterCreate(virtualHost);
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private PortalK8sConfigMapModifier _portalK8sConfigMapModifier;

	@Reference
	private VirtualHostLocalService _virtualHostLocalService;

}