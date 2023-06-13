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

package com.liferay.object.web.internal.util;

import com.liferay.info.field.type.BooleanInfoFieldType;
import com.liferay.info.field.type.DateInfoFieldType;
import com.liferay.info.field.type.FileInfoFieldType;
import com.liferay.info.field.type.HTMLInfoFieldType;
import com.liferay.info.field.type.InfoFieldType;
import com.liferay.info.field.type.LongTextInfoFieldType;
import com.liferay.info.field.type.MultiselectInfoFieldType;
import com.liferay.info.field.type.NumberInfoFieldType;
import com.liferay.info.field.type.RelationshipInfoFieldType;
import com.liferay.info.field.type.SelectInfoFieldType;
import com.liferay.info.field.type.TextInfoFieldType;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.model.ObjectField;

import java.util.Objects;

/**
 * @author Eudaldo Alonso
 */
public class ObjectFieldDBTypeUtil {

	public static InfoFieldType getInfoFieldType(ObjectField objectField) {
		if (Objects.equals(
				objectField.getBusinessType(),
				ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT)) {

			return FileInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_BOOLEAN)) {

			return BooleanInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_DATE)) {

			return DateInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_DECIMAL) ||
				 Objects.equals(
					 objectField.getBusinessType(),
					 ObjectFieldConstants.BUSINESS_TYPE_INTEGER) ||
				 Objects.equals(
					 objectField.getBusinessType(),
					 ObjectFieldConstants.BUSINESS_TYPE_LONG_INTEGER) ||
				 Objects.equals(
					 objectField.getBusinessType(),
					 ObjectFieldConstants.BUSINESS_TYPE_PRECISION_DECIMAL)) {

			return NumberInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_LONG_TEXT)) {

			return LongTextInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_MULTISELECT_PICKLIST)) {

			return MultiselectInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_PICKLIST)) {

			return SelectInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_RELATIONSHIP)) {

			return RelationshipInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_RICH_TEXT)) {

			return HTMLInfoFieldType.INSTANCE;
		}

		return TextInfoFieldType.INSTANCE;
	}

}