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

package com.liferay.object.rest.internal.odata.entity.v1_0;

import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectFieldSettingConstants;
import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.field.setting.util.ObjectFieldSettingUtil;
import com.liferay.object.model.ObjectField;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.odata.entity.BooleanEntityField;
import com.liferay.portal.odata.entity.CollectionEntityField;
import com.liferay.portal.odata.entity.DateEntityField;
import com.liferay.portal.odata.entity.DateTimeEntityField;
import com.liferay.portal.odata.entity.DoubleEntityField;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.entity.IdEntityField;
import com.liferay.portal.odata.entity.IntegerEntityField;
import com.liferay.portal.odata.entity.StringEntityField;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Javier de Arcos
 */
public class ObjectEntryEntityModel implements EntityModel {

	public ObjectEntryEntityModel(List<ObjectField> objectFields) {
		_entityFieldsMap = HashMapBuilder.<String, EntityField>put(
			"creator", new StringEntityField("creator", locale -> "creator")
		).put(
			"creatorId",
			new IntegerEntityField("creatorId", locale -> Field.USER_ID)
		).put(
			"dateCreated",
			new DateTimeEntityField(
				"dateCreated", locale -> Field.CREATE_DATE,
				locale -> Field.CREATE_DATE)
		).put(
			"dateModified",
			new DateTimeEntityField(
				"dateModified", locale -> "modifiedDate",
				locale -> "modifiedDate")
		).put(
			"externalReferenceCode",
			() -> new StringEntityField(
				"externalReferenceCode", locale -> "externalReferenceCode")
		).put(
			"id", new IdEntityField("id", locale -> "id", String::valueOf)
		).put(
			"objectDefinitionId",
			new IntegerEntityField(
				"objectDefinitionId", locale -> "objectDefinitionId")
		).put(
			"siteId", new IntegerEntityField("siteId", locale -> Field.GROUP_ID)
		).put(
			"status",
			new CollectionEntityField(
				new IntegerEntityField("status", locale -> Field.STATUS))
		).put(
			"userId", new IntegerEntityField("userId", locale -> Field.USER_ID)
		).build();

		for (ObjectField objectField : objectFields) {
			if (objectField.isSystem()) {
				continue;
			}

			if (!Objects.equals(
					objectField.getRelationshipType(),
					ObjectRelationshipConstants.TYPE_ONE_TO_MANY)) {

				EntityField entityField = _getEntityField(objectField);

				if (entityField != null) {
					_entityFieldsMap.putIfAbsent(
						objectField.getName(), entityField);
				}

				continue;
			}

			String objectFieldName = objectField.getName();

			_entityFieldsMap.put(
				objectFieldName,
				new IdEntityField(
					objectFieldName, locale -> objectFieldName,
					String::valueOf));

			String objectRelationshipERCFieldName =
				ObjectFieldSettingUtil.getValue(
					ObjectFieldSettingConstants.
						NAME_OBJECT_RELATIONSHIP_ERC_FIELD_NAME,
					objectField);

			_entityFieldsMap.put(
				objectRelationshipERCFieldName,
				new StringEntityField(
					objectRelationshipERCFieldName, locale -> objectFieldName));

			String relationshipIdName = objectFieldName.substring(
				objectFieldName.lastIndexOf(StringPool.UNDERLINE) + 1);

			_entityFieldsMap.put(
				relationshipIdName,
				new IdEntityField(
					relationshipIdName, locale -> objectFieldName,
					String::valueOf));
		}
	}

	@Override
	public Map<String, EntityField> getEntityFieldsMap() {
		return _entityFieldsMap;
	}

	private EntityField _getEntityField(ObjectField objectField) {
		if (objectField.compareBusinessType(
				ObjectFieldConstants.BUSINESS_TYPE_AGGREGATION) ||
			objectField.compareBusinessType(
				ObjectFieldConstants.BUSINESS_TYPE_FORMULA)) {

			return null;
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT)) {

			return new StringEntityField(
				objectField.getName(), locale -> objectField.getName());
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_MULTISELECT_PICKLIST)) {

			return new CollectionEntityField(
				new StringEntityField(
					objectField.getName(), locale -> objectField.getName()));
		}

		if (Objects.equals(
				objectField.getDBType(),
				ObjectFieldConstants.DB_TYPE_BIG_DECIMAL) ||
			Objects.equals(
				objectField.getDBType(), ObjectFieldConstants.DB_TYPE_DOUBLE)) {

			return new DoubleEntityField(
				objectField.getName(), locale -> objectField.getName());
		}
		else if (Objects.equals(
					objectField.getDBType(),
					ObjectFieldConstants.DB_TYPE_BOOLEAN)) {

			return new BooleanEntityField(
				objectField.getName(), locale -> objectField.getName());
		}
		else if (Objects.equals(
					objectField.getDBType(),
					ObjectFieldConstants.DB_TYPE_CLOB) ||
				 Objects.equals(
					 objectField.getDBType(),
					 ObjectFieldConstants.DB_TYPE_STRING)) {

			return new StringEntityField(
				objectField.getName(), locale -> objectField.getName());
		}
		else if (Objects.equals(
					objectField.getDBType(),
					ObjectFieldConstants.DB_TYPE_DATE)) {

			return new DateEntityField(
				objectField.getName(), locale -> objectField.getName(),
				locale -> objectField.getName());
		}
		else if (Objects.equals(
					objectField.getDBType(),
					ObjectFieldConstants.DB_TYPE_INTEGER) ||
				 Objects.equals(
					 objectField.getDBType(),
					 ObjectFieldConstants.DB_TYPE_LONG)) {

			return new IntegerEntityField(
				objectField.getName(), locale -> objectField.getName());
		}

		return null;
	}

	private final Map<String, EntityField> _entityFieldsMap;

}