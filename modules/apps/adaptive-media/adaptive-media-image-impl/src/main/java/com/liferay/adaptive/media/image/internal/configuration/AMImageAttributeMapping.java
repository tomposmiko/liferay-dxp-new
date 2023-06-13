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

package com.liferay.adaptive.media.image.internal.configuration;

import com.liferay.adaptive.media.AMAttribute;
import com.liferay.adaptive.media.image.processor.AMImageAttribute;
import com.liferay.adaptive.media.image.processor.AMImageProcessor;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Map;

/**
 * Gives convenient access to a set of media attributes. It offers a type-safe
 * interface to access attribute values, accepting only attributes of the
 * correct type (those for adaptive images), and returning values of the correct
 * type.
 *
 * @author Adolfo Pérez
 */
public class AMImageAttributeMapping {

	public static AMImageAttributeMapping fromFileVersion(
		FileVersion fileVersion) {

		return new AMImageAttributeMapping(
			HashMapBuilder.<AMAttribute<AMImageProcessor, ?>, Object>put(
				AMAttribute.getContentLengthAMAttribute(), fileVersion.getSize()
			).put(
				AMAttribute.getContentTypeAMAttribute(),
				fileVersion.getMimeType()
			).put(
				AMAttribute.getFileNameAMAttribute(), fileVersion.getFileName()
			).build());
	}

	/**
	 * Returns an {@link AMImageAttributeMapping} that uses the map as the
	 * underlying attribute storage.
	 *
	 * @param  properties the map to get the properties from
	 * @return a non-<code>null</code> mapping that provides type-safe access to
	 *         an underlying map
	 */
	public static AMImageAttributeMapping fromProperties(
		Map<String, String> properties) {

		if (properties == null) {
			throw new IllegalArgumentException("Properties map is null");
		}

		return new AMImageAttributeMapping(
			HashMapBuilder.<AMAttribute<AMImageProcessor, ?>, Object>put(
				AMAttribute.getConfigurationUuidAMAttribute(),
				_getValue(
					properties, AMAttribute.getConfigurationUuidAMAttribute())
			).put(
				AMAttribute.getContentLengthAMAttribute(),
				_getValue(properties, AMAttribute.getContentLengthAMAttribute())
			).put(
				AMAttribute.getContentTypeAMAttribute(),
				_getValue(properties, AMAttribute.getContentTypeAMAttribute())
			).put(
				AMAttribute.getFileNameAMAttribute(),
				_getValue(properties, AMAttribute.getFileNameAMAttribute())
			).put(
				AMImageAttribute.AM_IMAGE_ATTRIBUTE_HEIGHT,
				_getValue(
					properties, AMImageAttribute.AM_IMAGE_ATTRIBUTE_HEIGHT)
			).put(
				AMImageAttribute.AM_IMAGE_ATTRIBUTE_WIDTH,
				_getValue(properties, AMImageAttribute.AM_IMAGE_ATTRIBUTE_WIDTH)
			).build());
	}

	/**
	 * Returns an instance that contains the value of the
	 * attribute (if any) in this mapping.
	 *
	 * @param  amAttribute a non <code>null</code> attribute
	 * @return an instance that contains the value (if any)
	 */
	public <V> V getValue(AMAttribute<AMImageProcessor, V> amAttribute) {
		if (amAttribute == null) {
			throw new IllegalArgumentException(
				"Adaptive media attribute is null");
		}

		return (V)_values.get(amAttribute);
	}

	protected AMImageAttributeMapping(
		Map<AMAttribute<AMImageProcessor, ?>, Object> values) {

		_values = values;
	}

	private static <V> V _getValue(
		Map<String, String> properties,
		AMAttribute<AMImageProcessor, V> amAttribute) {

		String value = properties.get(amAttribute.getName());

		if (value == null) {
			return null;
		}

		return amAttribute.convert(value);
	}

	private final Map<AMAttribute<AMImageProcessor, ?>, Object> _values;

}