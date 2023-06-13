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

package com.liferay.object.admin.rest.internal.resource.v1_0;

import com.liferay.list.type.service.ListTypeDefinitionLocalService;
import com.liferay.list.type.service.ListTypeEntryLocalService;
import com.liferay.object.admin.rest.dto.v1_0.ObjectDefinition;
import com.liferay.object.admin.rest.dto.v1_0.ObjectField;
import com.liferay.object.admin.rest.internal.dto.v1_0.converter.ObjectFieldDTOConverter;
import com.liferay.object.admin.rest.internal.dto.v1_0.util.ObjectFieldSettingUtil;
import com.liferay.object.admin.rest.internal.dto.v1_0.util.ObjectFieldUtil;
import com.liferay.object.admin.rest.internal.odata.entity.v1_0.ObjectFieldEntityModel;
import com.liferay.object.admin.rest.resource.v1_0.ObjectFieldResource;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectFieldService;
import com.liferay.object.service.ObjectFieldSettingLocalService;
import com.liferay.object.service.ObjectFilterLocalService;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.fields.NestedField;
import com.liferay.portal.vulcan.fields.NestedFieldSupport;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;
import com.liferay.portal.vulcan.util.SearchUtil;

import java.util.Objects;

import javax.ws.rs.core.MultivaluedMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/object-field.properties",
	scope = ServiceScope.PROTOTYPE,
	service = {NestedFieldSupport.class, ObjectFieldResource.class}
)
public class ObjectFieldResourceImpl
	extends BaseObjectFieldResourceImpl implements NestedFieldSupport {

	@Override
	public void deleteObjectField(Long objectFieldId) throws Exception {
		_objectFieldService.deleteObjectField(objectFieldId);
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap) {
		return _entityModel;
	}

	@Override
	public Page<ObjectField>
			getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
				String externalReferenceCode, String search, Filter filter,
				Pagination pagination, Sort[] sorts)
		throws Exception {

		com.liferay.object.model.ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.
				getObjectDefinitionByExternalReferenceCode(
					externalReferenceCode, contextCompany.getCompanyId());

		return _getObjectFieldsPage(
			objectDefinition, search, filter, pagination, sorts);
	}

	@NestedField(parentClass = ObjectDefinition.class, value = "objectFields")
	@Override
	public Page<ObjectField> getObjectDefinitionObjectFieldsPage(
			Long objectDefinitionId, String search, Filter filter,
			Pagination pagination, Sort[] sorts)
		throws Exception {

		return _getObjectFieldsPage(
			_objectDefinitionLocalService.getObjectDefinition(
				objectDefinitionId),
			search, filter, pagination, sorts);
	}

	@Override
	public ObjectField getObjectField(Long objectFieldId) throws Exception {
		return _toObjectField(
			_objectFieldService.getObjectField(objectFieldId));
	}

	@Override
	public ObjectField postObjectDefinitionByExternalReferenceCodeObjectField(
			String externalReferenceCode, ObjectField objectField)
		throws Exception {

		com.liferay.object.model.ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.
				getObjectDefinitionByExternalReferenceCode(
					externalReferenceCode, contextCompany.getCompanyId());

		return postObjectDefinitionObjectField(
			objectDefinition.getObjectDefinitionId(), objectField);
	}

	@Override
	public ObjectField postObjectDefinitionObjectField(
			Long objectDefinitionId, ObjectField objectField)
		throws Exception {

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-164948")) &&
			Objects.equals(
				objectField.getBusinessTypeAsString(),
				ObjectFieldConstants.BUSINESS_TYPE_FORMULA)) {

			throw new UnsupportedOperationException();
		}

		return _toObjectField(
			_objectFieldService.addCustomObjectField(
				objectField.getExternalReferenceCode(),
				ObjectFieldUtil.getListTypeDefinitionId(
					contextUser.getCompanyId(), _listTypeDefinitionLocalService,
					objectField),
				objectDefinitionId, objectField.getBusinessTypeAsString(),
				ObjectFieldUtil.getDBType(
					objectField.getDBTypeAsString(),
					objectField.getTypeAsString()),
				objectField.getDefaultValue(),
				GetterUtil.getBoolean(objectField.getIndexed()),
				GetterUtil.getBoolean(objectField.getIndexedAsKeyword()),
				objectField.getIndexedLanguageId(),
				LocalizedMapUtil.getLocalizedMap(objectField.getLabel()),
				objectField.getName(), objectField.getRequired(),
				GetterUtil.getBoolean(objectField.getState()),
				transformToList(
					objectField.getObjectFieldSettings(),
					objectFieldSetting ->
						ObjectFieldSettingUtil.toObjectFieldSetting(
							objectField.getBusinessTypeAsString(),
							ObjectFieldUtil.addListTypeDefinition(
								contextUser.getCompanyId(),
								_listTypeDefinitionLocalService,
								_listTypeEntryLocalService, objectField,
								contextUser.getUserId()),
							objectFieldSetting, _objectFieldSettingLocalService,
							_objectFilterLocalService))));
	}

	@Override
	public ObjectField putObjectField(
			Long objectFieldId, ObjectField objectField)
		throws Exception {

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-164948")) &&
			Objects.equals(
				objectField.getBusinessTypeAsString(),
				ObjectFieldConstants.BUSINESS_TYPE_FORMULA)) {

			throw new UnsupportedOperationException();
		}

		com.liferay.object.model.ObjectField serviceBuilderObjectField =
			_objectFieldService.getObjectField(objectFieldId);

		com.liferay.object.model.ObjectDefinition
			serviceBuilderObjectDefinition =
				_objectDefinitionLocalService.getObjectDefinition(
					serviceBuilderObjectField.getObjectDefinitionId());

		if (!serviceBuilderObjectDefinition.isApproved()) {
			objectField.setListTypeDefinitionId(
				ObjectFieldUtil.addListTypeDefinition(
					contextUser.getCompanyId(), _listTypeDefinitionLocalService,
					_listTypeEntryLocalService, objectField,
					contextUser.getUserId()));
		}

		if (Validator.isNull(objectField.getListTypeDefinitionId())) {
			objectField.setListTypeDefinitionId(
				serviceBuilderObjectField.getListTypeDefinitionId());
		}

		return _toObjectField(
			_objectFieldService.updateObjectField(
				objectField.getExternalReferenceCode(), objectFieldId,
				ObjectFieldUtil.getListTypeDefinitionId(
					contextUser.getCompanyId(), _listTypeDefinitionLocalService,
					objectField),
				objectField.getBusinessTypeAsString(),
				ObjectFieldUtil.getDBType(
					objectField.getDBTypeAsString(),
					objectField.getTypeAsString()),
				objectField.getDefaultValue(),
				GetterUtil.getBoolean(objectField.getIndexed()),
				GetterUtil.getBoolean(objectField.getIndexedAsKeyword()),
				objectField.getIndexedLanguageId(),
				LocalizedMapUtil.getLocalizedMap(objectField.getLabel()),
				objectField.getName(), objectField.getRequired(),
				GetterUtil.getBoolean(objectField.getState()),
				transformToList(
					objectField.getObjectFieldSettings(),
					objectFieldSetting ->
						ObjectFieldSettingUtil.toObjectFieldSetting(
							objectField.getBusinessTypeAsString(),
							objectField.getListTypeDefinitionId(),
							objectFieldSetting, _objectFieldSettingLocalService,
							_objectFilterLocalService))));
	}

	private Page<ObjectField> _getObjectFieldsPage(
			com.liferay.object.model.ObjectDefinition objectDefinition,
			String search, Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		return SearchUtil.search(
			HashMapBuilder.put(
				"create",
				addAction(
					ActionKeys.UPDATE, "postObjectDefinitionObjectField",
					com.liferay.object.model.ObjectDefinition.class.getName(),
					objectDefinition.getObjectDefinitionId())
			).put(
				"createBatch",
				addAction(
					ActionKeys.UPDATE, "postObjectDefinitionObjectFieldBatch",
					com.liferay.object.model.ObjectDefinition.class.getName(),
					objectDefinition.getObjectDefinitionId())
			).put(
				"deleteBatch",
				addAction(
					ActionKeys.DELETE, "deleteObjectFieldBatch",
					com.liferay.object.model.ObjectDefinition.class.getName(),
					null)
			).put(
				"get",
				addAction(
					ActionKeys.VIEW, "getObjectDefinitionObjectFieldsPage",
					com.liferay.object.model.ObjectDefinition.class.getName(),
					objectDefinition.getObjectDefinitionId())
			).put(
				"updateBatch",
				addAction(
					ActionKeys.UPDATE, "putObjectFieldBatch",
					com.liferay.object.model.ObjectDefinition.class.getName(),
					null)
			).build(),
			booleanQuery -> {
			},
			filter, com.liferay.object.model.ObjectField.class.getName(),
			search, pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			searchContext -> {
				searchContext.setAttribute(Field.NAME, search);
				searchContext.setAttribute("label", search);
				searchContext.setAttribute(
					"objectDefinitionId",
					objectDefinition.getObjectDefinitionId());
				searchContext.setCompanyId(contextCompany.getCompanyId());
			},
			sorts,
			document -> _toObjectField(
				objectDefinition,
				_objectFieldService.getObjectField(
					GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK)))));
	}

	private ObjectField _toObjectField(
			com.liferay.object.model.ObjectDefinition objectDefinition,
			com.liferay.object.model.ObjectField objectField)
		throws Exception {

		boolean updateable =
			(!objectDefinition.isApproved() && !objectDefinition.isSystem()) ||
			Objects.equals(
				objectDefinition.getExtensionDBTableName(),
				objectField.getDBTableName());

		return _objectFieldDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				false,
				HashMapBuilder.put(
					"delete",
					() -> {
						if (!updateable ||
							Validator.isNotNull(
								objectField.getRelationshipType()) ||
							objectField.isSystem()) {

							return null;
						}

						return addAction(
							ActionKeys.UPDATE, "deleteObjectField",
							com.liferay.object.model.ObjectDefinition.class.
								getName(),
							objectField.getObjectDefinitionId());
					}
				).put(
					"get",
					addAction(
						ActionKeys.VIEW, "getObjectField",
						com.liferay.object.model.ObjectDefinition.class.
							getName(),
						objectField.getObjectDefinitionId())
				).put(
					"update",
					() -> {
						if (!updateable) {
							return null;
						}

						return addAction(
							ActionKeys.UPDATE, "putObjectField",
							com.liferay.object.model.ObjectDefinition.class.
								getName(),
							objectField.getObjectDefinitionId());
					}
				).build(),
				null, null, contextAcceptLanguage.getPreferredLocale(), null,
				null),
			objectField);
	}

	private ObjectField _toObjectField(
			com.liferay.object.model.ObjectField objectField)
		throws Exception {

		return _toObjectField(
			_objectDefinitionLocalService.getObjectDefinition(
				objectField.getObjectDefinitionId()),
			objectField);
	}

	private static final EntityModel _entityModel =
		new ObjectFieldEntityModel();

	@Reference
	private ListTypeDefinitionLocalService _listTypeDefinitionLocalService;

	@Reference
	private ListTypeEntryLocalService _listTypeEntryLocalService;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectFieldDTOConverter _objectFieldDTOConverter;

	@Reference
	private ObjectFieldService _objectFieldService;

	@Reference
	private ObjectFieldSettingLocalService _objectFieldSettingLocalService;

	@Reference
	private ObjectFilterLocalService _objectFilterLocalService;

}