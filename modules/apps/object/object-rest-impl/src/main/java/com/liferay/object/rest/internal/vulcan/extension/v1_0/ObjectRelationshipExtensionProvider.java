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

package com.liferay.object.rest.internal.vulcan.extension.v1_0;

import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.rest.dto.v1_0.ObjectEntry;
import com.liferay.object.rest.manager.v1_0.ObjectEntryManager;
import com.liferay.object.rest.manager.v1_0.ObjectEntryManagerRegistry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.extension.ExtensionProvider;
import com.liferay.portal.vulcan.extension.PropertyDefinition;
import com.liferay.portal.vulcan.extension.validation.PropertyValidator;
import com.liferay.portal.vulcan.fields.NestedFieldsContext;
import com.liferay.portal.vulcan.fields.NestedFieldsContextThreadLocal;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Correa
 */
@Component(service = ExtensionProvider.class)
public class ObjectRelationshipExtensionProvider
	extends BaseObjectExtensionProvider {

	@Override
	public Map<String, Serializable> getExtendedProperties(
			long companyId, String className, Object entity)
		throws Exception {

		NestedFieldsContext nestedFieldsContext =
			NestedFieldsContextThreadLocal.getNestedFieldsContext();

		if (nestedFieldsContext == null) {
			return Collections.emptyMap();
		}

		ObjectDefinition objectDefinition = getObjectDefinition(
			companyId, className);

		List<ObjectRelationship> objectRelationships = _getObjectRelationships(
			nestedFieldsContext.getFieldNames(), objectDefinition);

		if (objectRelationships.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<String, Serializable> extendedProperties = new HashMap<>();

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				objectDefinition.getStorageType());
		long primaryKey = getPrimaryKey(entity);

		for (ObjectRelationship objectRelationship : objectRelationships) {
			Page<ObjectEntry> relatedObjectEntriesPage =
				objectEntryManager.getObjectEntryRelatedObjectEntries(
					_getDefaultDTOConverterContext(
						objectDefinition, primaryKey, null),
					objectDefinition, primaryKey, objectRelationship.getName(),
					Pagination.of(QueryUtil.ALL_POS, QueryUtil.ALL_POS));

			extendedProperties.put(
				objectRelationship.getName(),
				(Serializable)relatedObjectEntriesPage.getItems());
		}

		return extendedProperties;
	}

	@Override
	public Map<String, PropertyDefinition> getExtendedPropertyDefinitions(
			long companyId, String className)
		throws Exception {

		Map<String, PropertyDefinition> extendedPropertyDefinitions =
			new HashMap<>();

		ObjectDefinition objectDefinition = getObjectDefinition(
			companyId, className);

		for (ObjectRelationship objectRelationship :
				_objectRelationshipLocalService.getObjectRelationships(
					objectDefinition.getObjectDefinitionId())) {

			if (!Objects.equals(
					objectRelationship.getType(),
					ObjectRelationshipConstants.TYPE_MANY_TO_MANY) &&
				!Objects.equals(
					objectRelationship.getType(),
					ObjectRelationshipConstants.TYPE_ONE_TO_MANY)) {

				continue;
			}

			ObjectDefinition relatedObjectDefinition =
				_getRelatedObjectDefinition(
					objectDefinition, objectRelationship);

			if (relatedObjectDefinition.isSystem()) {
				continue;
			}

			extendedPropertyDefinitions.put(
				objectRelationship.getName(),
				new PropertyDefinition(
					Collections.singleton(ObjectEntry.class), null,
					StringUtil.removeFirst(
						relatedObjectDefinition.getName(), "C_"),
					StringBundler.concat(
						"Information about the object relationship ",
						objectRelationship.getName(),
						" can be embedded with \"nestedFields\"."),
					objectRelationship.getName(),
					PropertyDefinition.PropertyType.MULTIPLE_ELEMENT,
					new UnsupportedOperationPropertyValidator(), false));
		}

		return extendedPropertyDefinitions;
	}

	@Override
	public boolean isApplicableExtension(long companyId, String className) {
		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-162964"))) {
			return false;
		}

		return super.isApplicableExtension(companyId, className);
	}

	@Override
	public void setExtendedProperties(
		long companyId, long userId, String className, Object entity,
		Map<String, Serializable> extendedProperties) {
	}

	private DefaultDTOConverterContext _getDefaultDTOConverterContext(
		ObjectDefinition objectDefinition, Long objectEntryId,
		UriInfo uriInfo) {

		DefaultDTOConverterContext defaultDTOConverterContext =
			new DefaultDTOConverterContext(
				false, null, _dtoConverterRegistry, objectEntryId,
				LocaleUtil.fromLanguageId(
					objectDefinition.getDefaultLanguageId(), true, false),
				uriInfo, null);

		defaultDTOConverterContext.setAttribute("addActions", Boolean.FALSE);

		return defaultDTOConverterContext;
	}

	private List<ObjectRelationship> _getObjectRelationships(
			List<String> fieldNames, ObjectDefinition objectDefinition)
		throws Exception {

		List<ObjectRelationship> objectRelationships = new ArrayList<>();

		for (String fieldName : fieldNames) {
			ObjectRelationship objectRelationship =
				_objectRelationshipLocalService.
					fetchObjectRelationshipByObjectDefinitionId1(
						objectDefinition.getObjectDefinitionId(), fieldName);

			if ((objectRelationship == null) ||
				(!Objects.equals(
					objectRelationship.getType(),
					ObjectRelationshipConstants.TYPE_MANY_TO_MANY) &&
				 !Objects.equals(
					 objectRelationship.getType(),
					 ObjectRelationshipConstants.TYPE_ONE_TO_MANY))) {

				continue;
			}

			ObjectDefinition relatedObjectDefinition =
				_getRelatedObjectDefinition(
					objectDefinition, objectRelationship);

			if (relatedObjectDefinition.isSystem()) {
				continue;
			}

			objectRelationships.add(objectRelationship);
		}

		return objectRelationships;
	}

	private ObjectDefinition _getRelatedObjectDefinition(
			ObjectDefinition objectDefinition,
			ObjectRelationship objectRelationship)
		throws Exception {

		long objectDefinitionId1 = objectRelationship.getObjectDefinitionId1();

		if (objectDefinitionId1 != objectDefinition.getObjectDefinitionId()) {
			return _objectDefinitionLocalService.getObjectDefinition(
				objectRelationship.getObjectDefinitionId1());
		}

		return _objectDefinitionLocalService.getObjectDefinition(
			objectRelationship.getObjectDefinitionId2());
	}

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

	@Reference
	private ObjectEntryManagerRegistry _objectEntryManagerRegistry;

	@Reference
	private ObjectRelationshipLocalService _objectRelationshipLocalService;

	private class UnsupportedOperationPropertyValidator
		implements PropertyValidator {

		@Override
		public void validate(
			PropertyDefinition propertyDefinition, Object propertyValue) {

			throw new UnsupportedOperationException(
				"The property " + propertyDefinition.getPropertyName() +
					" cannot be set");
		}

	}

}