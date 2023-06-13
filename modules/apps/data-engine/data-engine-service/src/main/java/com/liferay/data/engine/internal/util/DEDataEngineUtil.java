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

package com.liferay.data.engine.internal.util;

import com.liferay.data.engine.model.DEDataDefinitionField;

import java.util.Map;

/**
 * @author Gabriel Albuquerque
 */
public final class DEDataEngineUtil {

	public static Object getDEDataDefinitionFieldValue(
		DEDataDefinitionField deDataDefinitionField,
		Map<String, Object> values) {

		if (deDataDefinitionField.isLocalizable()) {
			return (Map<String, Object>)values.get(
				deDataDefinitionField.getName());
		}
		else if (deDataDefinitionField.isRepeatable()) {
			return (Object[])values.get(deDataDefinitionField.getName());
		}

		return values.get(deDataDefinitionField.getName());
	}

}