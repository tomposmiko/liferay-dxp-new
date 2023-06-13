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

package com.liferay.object.internal.notification.type;

import com.liferay.notification.type.NotificationType;
import com.liferay.object.model.ObjectEntry;
import com.liferay.petra.string.StringPool;

import java.util.Locale;

/**
 * @author Gustavo Lima
 */
public class ObjectDefinitionNotificationType implements NotificationType {

	public ObjectDefinitionNotificationType(String key, String label) {
		_key = key;
		_label = label;
	}

	@Override
	public String getClassName(Object object) {
		if (!(object instanceof ObjectEntry)) {
			throw new IllegalArgumentException(
				"Object " + object + " is not an object entry");
		}

		return ObjectEntry.class.getName();
	}

	@Override
	public long getClassPK(Object object) {
		if (!(object instanceof ObjectEntry)) {
			throw new IllegalArgumentException(
				"Object " + object + " is not an object entry");
		}

		ObjectEntry objectEntry = (ObjectEntry)object;

		return objectEntry.getObjectEntryId();
	}

	@Override
	public String getKey() {
		return _key;
	}

	@Override
	public String getLabel(Locale locale) {
		return StringPool.BLANK;
	}

	private final String _key;
	private final String _label;

}