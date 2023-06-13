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

package com.liferay.headless.commerce.admin.order.internal.odata.entity.v1_0;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.odata.entity.CollectionEntityField;
import com.liferay.portal.odata.entity.DateTimeEntityField;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.entity.IntegerEntityField;
import com.liferay.portal.odata.entity.StringEntityField;

import java.util.Map;

/**
 * @author Alessio Antonio Rendina
 */
public class OrderEntityModel implements EntityModel {

	public OrderEntityModel() {
		_entityFieldsMap = EntityModel.toEntityFieldsMap(
			new CollectionEntityField(
				new IntegerEntityField(
					"accountId", locale -> "commerceAccountId")),
			new IntegerEntityField("channelId", locale -> "commerceChannelId"),
			new CollectionEntityField(
				new IntegerEntityField("orderStatus", locale -> "orderStatus")),
			new IntegerEntityField("orderId", locale -> Field.ENTRY_CLASS_PK),
			new DateTimeEntityField(
				"createDate",
				locale -> Field.getSortableFieldName(Field.CREATE_DATE),
				locale -> Field.CREATE_DATE),
			new StringEntityField(
				"creatorEmailAddress", locale -> "orderCreatorEmailAddress"),
			new DateTimeEntityField(
				"modifiedDate",
				locale -> Field.getSortableFieldName(Field.MODIFIED_DATE),
				locale -> Field.MODIFIED_DATE),
			new DateTimeEntityField(
				"orderDate", locale -> Field.getSortableFieldName("orderDate"),
				locale -> "orderDate"));
	}

	@Override
	public Map<String, EntityField> getEntityFieldsMap() {
		return _entityFieldsMap;
	}

	private final Map<String, EntityField> _entityFieldsMap;

}