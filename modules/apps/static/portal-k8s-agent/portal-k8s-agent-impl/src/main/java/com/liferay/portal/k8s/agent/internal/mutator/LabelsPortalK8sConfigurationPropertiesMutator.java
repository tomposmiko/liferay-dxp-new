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

package com.liferay.portal.k8s.agent.internal.mutator;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.k8s.agent.mutator.PortalK8sConfigurationPropertiesMutator;

import java.util.Dictionary;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceRanking;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true, service = PortalK8sConfigurationPropertiesMutator.class
)
@ServiceRanking(2000)
public class LabelsPortalK8sConfigurationPropertiesMutator
	implements PortalK8sConfigurationPropertiesMutator {

	@Override
	public void mutateConfigurationProperties(
		Map<String, String> annotations, Map<String, String> labels,
		Dictionary<String, Object> properties) {

		for (Map.Entry<String, String> entry : labels.entrySet()) {
			String key = entry.getKey();

			if (key.contains(StringPool.SLASH)) {
				key = StringUtil.replace(key, CharPool.SLASH, CharPool.PERIOD);
			}

			properties.put(key, entry.getValue());
		}
	}

}