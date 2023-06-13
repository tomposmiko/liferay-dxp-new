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

package com.liferay.portal.odata.entity;

import com.liferay.portal.kernel.util.StringBundler;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Models an complex entity field. A Entity field with a {@code
 * EntityField.Type.COMPLEX}
 *
 * @author Cristina González
 * @review
 */
public class ComplexEntityField extends EntityField {

	/**
	 * Creates a new {@code EntityField} with its name, type and the list of
	 * entityfields contained inside it.
	 *
	 * @param  name the entity field's name
	 * @param  entityFields the list of entity Fields
	 * @review
	 */
	public ComplexEntityField(String name, List<EntityField> entityFields) {
		super(
			name, EntityField.Type.COMPLEX, locale -> name, locale -> name,
			fieldValue -> String.valueOf(fieldValue));

		if (entityFields == null) {
			_entityFieldsMap = Collections.emptyMap();
		}
		else {
			Stream<EntityField> stream = entityFields.stream();

			_entityFieldsMap = stream.map(
				entityField -> new AbstractMap.SimpleEntry<>(
					entityField.getName(), entityField)
			).collect(
				Collectors.toMap(
					entry -> entry.getKey(), entry -> entry.getValue())
			);
		}
	}

	/**
	 * Returns a Map with all the entity fields of this entity fields.
	 *
	 * @return the entity field map
	 * @review
	 */
	public Map<String, EntityField> getEntityFieldsMap() {
		return _entityFieldsMap;
	}

	@Override
	public String toString() {
		Type type = getType();

		return StringBundler.concat(
			"{entityFields: ", _entityFieldsMap.toString(), ", name: ",
			getName(), ", type: ", type.name(), "}");
	}

	private final Map<String, EntityField> _entityFieldsMap;

}