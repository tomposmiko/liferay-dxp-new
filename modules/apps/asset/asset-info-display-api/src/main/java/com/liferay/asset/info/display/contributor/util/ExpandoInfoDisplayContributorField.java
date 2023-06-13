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

package com.liferay.asset.info.display.contributor.util;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.info.display.contributor.field.InfoDisplayContributorField;
import com.liferay.info.display.contributor.field.InfoDisplayContributorFieldType;

import java.util.Locale;

/**
 * @author Pavel Savinov
 * @deprecated As of Mueller (7.2.x), replaced by {@link
 *			 com.liferay.info.display.contributor.field.ExpandoInfoDisplayContributorField}
 */
@Deprecated
public class ExpandoInfoDisplayContributorField
	extends com.liferay.info.display.contributor.field.
				ExpandoInfoDisplayContributorField
	implements InfoDisplayContributorField {

	public ExpandoInfoDisplayContributorField(
		String attributeName, ExpandoBridge expandoBridge) {

		super(attributeName, expandoBridge);
	}

	@Override
	public String getKey() {
		return super.getKey();
	}

	@Override
	public String getLabel(Locale locale) {
		return super.getLabel(locale);
	}

	@Override
	public InfoDisplayContributorFieldType getType() {
		return super.getType();
	}

	@Override
	public Object getValue(Object model, Locale locale) {
		return super.getValue(model, locale);
	}

}