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

package com.liferay.info.field.type;

import com.liferay.info.localized.InfoLocalizedValue;

import java.util.Collection;
import java.util.Locale;

/**
 * @author Alicia Garcia
 */
public class SelectInfoFieldType implements InfoFieldType {

	public static final Attribute<SelectInfoFieldType, Boolean> INLINE =
		new Attribute<>();

	public static final SelectInfoFieldType INSTANCE =
		new SelectInfoFieldType();

	public static final Attribute<SelectInfoFieldType, Boolean> MULTIPLE =
		new Attribute<>();

	public static final Attribute<SelectInfoFieldType, Collection<Option>>
		OPTIONS = new Attribute<>();

	@Override
	public String getName() {
		return "select";
	}

	public static class Option {

		public Option(
			boolean active, InfoLocalizedValue<String> label, String value) {

			_active = active;
			_label = label;
			_value = value;
		}

		public Option(InfoLocalizedValue<String> label, String value) {
			_label = label;
			_value = value;

			_active = false;
		}

		public String getLabel(Locale locale) {
			return _label.getValue(locale);
		}

		public String getValue() {
			return _value;
		}

		public boolean isActive() {
			return _active;
		}

		private final boolean _active;
		private final InfoLocalizedValue<String> _label;
		private final String _value;

	}

	private SelectInfoFieldType() {
	}

}