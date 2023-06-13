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

package com.liferay.dynamic.data.mapping.io.internal;

import com.liferay.dynamic.data.mapping.io.DDMFormLayoutJSONSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutSerializerSerializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutSerializerSerializeResponse;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutSerializerTracker;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 * @deprecated As of Judson (7.1.x), replaced by {@link com.liferay.dynamic.data.mapping.io.internal.DDMFormLayoutJSONSerializer}
 */
@Component(immediate = true, service = DDMFormLayoutJSONSerializer.class)
@Deprecated
public class DDMFormLayoutJSONSerializerImpl
	implements DDMFormLayoutJSONSerializer {

	@Override
	public String serialize(DDMFormLayout ddmFormLayout) {
		DDMFormLayoutSerializer ddmFormLayoutSerializer =
			_ddmFormLayoutSerializerTracker.getDDMFormLayoutSerializer("json");

		DDMFormLayoutSerializerSerializeRequest.Builder builder =
			DDMFormLayoutSerializerSerializeRequest.Builder.newBuilder(
				ddmFormLayout);

		DDMFormLayoutSerializerSerializeResponse
			ddmFormLayoutSerializerSerializeResponse =
				ddmFormLayoutSerializer.serialize(builder.build());

		return ddmFormLayoutSerializerSerializeResponse.getContent();
	}

	@Reference(unbind = "-")
	public void setDDMFormLayoutSerializerTracker(
		DDMFormLayoutSerializerTracker ddmFormLayoutSerializerTracker) {

		_ddmFormLayoutSerializerTracker = ddmFormLayoutSerializerTracker;
	}

	private DDMFormLayoutSerializerTracker _ddmFormLayoutSerializerTracker;

}