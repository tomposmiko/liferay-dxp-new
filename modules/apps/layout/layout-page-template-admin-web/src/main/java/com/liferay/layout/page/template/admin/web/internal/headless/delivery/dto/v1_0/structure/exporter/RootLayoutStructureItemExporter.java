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

package com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.structure.exporter;

import com.liferay.headless.delivery.dto.v1_0.PageElement;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.layout.util.structure.RootLayoutStructureItem;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jürgen Kappler
 */
@Component(service = LayoutStructureItemExporter.class)
public class RootLayoutStructureItemExporter
	implements LayoutStructureItemExporter {

	@Override
	public String getClassName() {
		return RootLayoutStructureItem.class.getName();
	}

	@Override
	public PageElement getPageElement(
		long groupId, LayoutStructureItem layoutStructureItem,
		boolean saveInlineContent, boolean saveMappingConfiguration) {

		return new PageElement() {
			{
				type = PageElement.Type.ROOT;
			}
		};
	}

}