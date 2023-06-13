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

package com.liferay.commerce.product.content.web.internal.info.list.renderer;

import com.liferay.commerce.product.model.CPDefinitionSpecificationOptionValue;
import com.liferay.info.list.renderer.InfoListRenderer;
import com.liferay.info.taglib.list.renderer.BulletedBasicInfoListRenderer;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alessio Antonio Rendina
 */
@Component(immediate = true, service = InfoListRenderer.class)
public class BulletedCPDefinitionSpecificationOptionValueBasicInfoListRenderer
	extends BaseCPDefinitionSpecificationOptionValueBasicInfoListRenderer
	implements BulletedBasicInfoListRenderer
		<CPDefinitionSpecificationOptionValue> {
}