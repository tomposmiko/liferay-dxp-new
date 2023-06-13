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

package com.liferay.object.rest.internal.resource.v1_0;

import com.liferay.object.exception.NoSuchObjectDefinitionException;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.related.models.ObjectRelatedModelsProvider;
import com.liferay.object.related.models.ObjectRelatedModelsProviderRegistry;
import com.liferay.object.rest.dto.v1_0.ObjectEntry;
import com.liferay.object.rest.manager.v1_0.ObjectEntryManager;
import com.liferay.object.rest.manager.v1_0.ObjectEntryManagerRegistry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.service.ObjectRelationshipService;
import com.liferay.object.system.JaxRsApplicationDescriptor;
import com.liferay.object.system.SystemObjectDefinitionMetadata;
import com.liferay.object.system.SystemObjectDefinitionMetadataRegistry;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.security.auth.GuestOrUserUtil;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.net.URI;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luis Miguel Barcos
 */
@Component(
	factory = "com.liferay.object.rest.internal.resource.v1_0.RelatedObjectEntryResourceImpl",
	property = {"api.version=v1.0", "osgi.jaxrs.resource=true"},
	service = RelatedObjectEntryResourceImpl.class
)
public class RelatedObjectEntryResourceImpl
	extends BaseRelatedObjectEntryResourceImpl {

	@Override
	public void deleteObjectRelationshipMappingTableValues(
			String previousPath, Long objectEntryId,
			String objectRelationshipName, Long relatedObjectEntryId)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled("LPS-153324")) {
			throw new NotFoundException();
		}

		ObjectDefinition systemObjectDefinition = _getSystemObjectDefinition(
			previousPath);

		ObjectRelationship objectRelationship =
			_objectRelationshipLocalService.
				getObjectRelationshipByObjectDefinitionId(
					systemObjectDefinition.getObjectDefinitionId(),
					objectRelationshipName);

		_checkRelatedObjectEntry(
			objectRelationship, relatedObjectEntryId, systemObjectDefinition);

		_checkSystemObjectEntry(objectEntryId, systemObjectDefinition);

		ObjectRelatedModelsProvider objectRelatedModelsProvider =
			_objectRelatedModelsProviderRegistry.getObjectRelatedModelsProvider(
				systemObjectDefinition.getClassName(),
				objectRelationship.getType());

		objectRelatedModelsProvider.disassociateRelatedModels(
			GuestOrUserUtil.getUserId(),
			objectRelationship.getObjectRelationshipId(),
			_getPrimaryKey1(
				objectRelationship.getObjectDefinitionId1(), objectEntryId,
				relatedObjectEntryId, systemObjectDefinition),
			_getPrimaryKey2(
				objectRelationship.getObjectDefinitionId1(), objectEntryId,
				relatedObjectEntryId, systemObjectDefinition));
	}

	@Override
	public Page<Object> getRelatedObjectEntriesPage(
			String previousPath, Long objectEntryId,
			String objectRelationshipName, Pagination pagination)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled("LPS-153324")) {
			throw new NotFoundException();
		}

		ObjectDefinition systemObjectDefinition = _getSystemObjectDefinition(
			previousPath);

		ObjectRelationship objectRelationship =
			_objectRelationshipLocalService.
				getObjectRelationshipByObjectDefinitionId(
					systemObjectDefinition.getObjectDefinitionId(),
					objectRelationshipName);

		ObjectDefinition relatedObjectDefinition = _getRelatedObjectDefinition(
			systemObjectDefinition, objectRelationship);

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				systemObjectDefinition.getStorageType());

		if (relatedObjectDefinition.isSystem()) {
			return objectEntryManager.getRelatedSystemObjectEntries(
				systemObjectDefinition, objectEntryId, objectRelationshipName,
				pagination);
		}

		return (Page)objectEntryManager.getObjectEntryRelatedObjectEntries(
			_getDefaultDTOConverterContext(
				systemObjectDefinition, objectEntryId, _uriInfo),
			systemObjectDefinition, objectEntryId, objectRelationshipName,
			pagination);
	}

	@Override
	public ObjectEntry putObjectRelationshipMappingTableValues(
			String previousPath, Long objectEntryId,
			String objectRelationshipName, Long relatedObjectEntryId,
			Pagination pagination)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled("LPS-153324")) {
			throw new NotFoundException();
		}

		ObjectDefinition systemObjectDefinition = _getSystemObjectDefinition(
			previousPath);

		ObjectRelationship objectRelationship =
			_objectRelationshipLocalService.
				getObjectRelationshipByObjectDefinitionId(
					systemObjectDefinition.getObjectDefinitionId(),
					objectRelationshipName);

		_objectRelationshipService.addObjectRelationshipMappingTableValues(
			objectRelationship.getObjectRelationshipId(),
			_getPrimaryKey1(
				objectRelationship.getObjectDefinitionId1(), objectEntryId,
				relatedObjectEntryId, systemObjectDefinition),
			_getPrimaryKey2(
				objectRelationship.getObjectDefinitionId1(), objectEntryId,
				relatedObjectEntryId, systemObjectDefinition),
			new ServiceContext());

		return _getRelatedObjectEntry(
			objectRelationship, relatedObjectEntryId, systemObjectDefinition);
	}

	private void _checkRelatedObjectEntry(
			ObjectRelationship objectRelationship, long relatedObjectEntryId,
			ObjectDefinition systemObjectDefinition)
		throws Exception {

		_getRelatedObjectEntry(
			objectRelationship, relatedObjectEntryId, systemObjectDefinition);
	}

	private void _checkSystemObjectEntry(
			long objectEntryId, ObjectDefinition systemObjectDefinition)
		throws Exception {

		PersistedModelLocalService persistedModelLocalService =
			_persistedModelLocalServiceRegistry.getPersistedModelLocalService(
				systemObjectDefinition.getClassName());

		persistedModelLocalService.getPersistedModel(objectEntryId);
	}

	private DefaultDTOConverterContext _getDefaultDTOConverterContext(
		ObjectDefinition objectDefinition, Long objectEntryId,
		UriInfo uriInfo) {

		DefaultDTOConverterContext defaultDTOConverterContext =
			new DefaultDTOConverterContext(
				_dtoConverterRegistry, objectEntryId,
				LocaleUtil.fromLanguageId(
					objectDefinition.getDefaultLanguageId(), true, false),
				uriInfo, null);

		defaultDTOConverterContext.setAttribute("addActions", Boolean.FALSE);

		return defaultDTOConverterContext;
	}

	private long _getPrimaryKey1(
		long objectDefinitionId, long objectEntryId, long relatedObjectEntryId,
		ObjectDefinition systemObjectDefinition) {

		if (objectDefinitionId ==
				systemObjectDefinition.getObjectDefinitionId()) {

			return objectEntryId;
		}

		return relatedObjectEntryId;
	}

	private long _getPrimaryKey2(
		long objectDefinitionId, long objectEntryId, long relatedObjectEntryId,
		ObjectDefinition systemObjectDefinition) {

		if (objectDefinitionId ==
				systemObjectDefinition.getObjectDefinitionId()) {

			return relatedObjectEntryId;
		}

		return objectEntryId;
	}

	private ObjectDefinition _getRelatedObjectDefinition(
			ObjectDefinition objectDefinition,
			ObjectRelationship objectRelationship)
		throws Exception {

		ObjectDefinition relatedObjectDefinition = null;

		if (objectRelationship.getObjectDefinitionId1() !=
				objectDefinition.getObjectDefinitionId()) {

			relatedObjectDefinition =
				_objectDefinitionLocalService.getObjectDefinition(
					objectRelationship.getObjectDefinitionId1());
		}
		else {
			relatedObjectDefinition =
				_objectDefinitionLocalService.getObjectDefinition(
					objectRelationship.getObjectDefinitionId2());
		}

		if (!relatedObjectDefinition.isActive()) {
			throw new NoSuchObjectDefinitionException(
				"No active object definition found for relationship " +
					objectRelationship.getName());
		}

		return relatedObjectDefinition;
	}

	private ObjectEntry _getRelatedObjectEntry(
			ObjectRelationship objectRelationship, long relatedObjectEntryId,
			ObjectDefinition systemObjectDefinition)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				systemObjectDefinition.getStorageType());

		ObjectDefinition relatedObjectDefinition = _getRelatedObjectDefinition(
			systemObjectDefinition, objectRelationship);

		return objectEntryManager.getObjectEntry(
			_getDefaultDTOConverterContext(
				relatedObjectDefinition, relatedObjectEntryId, _uriInfo),
			relatedObjectDefinition, relatedObjectEntryId);
	}

	private ObjectDefinition _getSystemObjectDefinition(String previousPath) {
		SystemObjectDefinitionMetadata systemObjectDefinitionMetadata =
			_getSystemObjectDefinitionMetadata(previousPath);

		ObjectDefinition systemObjectDefinition =
			_objectDefinitionLocalService.fetchSystemObjectDefinition(
				systemObjectDefinitionMetadata.getName());

		if (systemObjectDefinition != null) {
			return systemObjectDefinition;
		}

		throw new NotFoundException(
			"No system object definition metadata for name \"" +
				systemObjectDefinitionMetadata.getName() + "\"");
	}

	private SystemObjectDefinitionMetadata _getSystemObjectDefinitionMetadata(
		String previousPath) {

		URI uri = _uriInfo.getBaseUri();

		String path = uri.getPath();

		String restContextPath = path.split("/")[2] + "/v1.0/" + previousPath;

		for (ObjectDefinition systemObjectDefinition :
				_objectDefinitionLocalService.getSystemObjectDefinitions()) {

			SystemObjectDefinitionMetadata systemObjectDefinitionMetadata =
				_systemObjectDefinitionMetadataRegistry.
					getSystemObjectDefinitionMetadata(
						systemObjectDefinition.getName());

			JaxRsApplicationDescriptor jaxRsApplicationDescriptor =
				systemObjectDefinitionMetadata.getJaxRsApplicationDescriptor();

			if (StringUtil.equals(
					jaxRsApplicationDescriptor.getRESTContextPath(),
					restContextPath)) {

				return systemObjectDefinitionMetadata;
			}
		}

		throw new NotFoundException(
			"No system object definition metadata for REST context path \"" +
				restContextPath + "\"");
	}

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryManagerRegistry _objectEntryManagerRegistry;

	@Reference
	private ObjectRelatedModelsProviderRegistry
		_objectRelatedModelsProviderRegistry;

	@Reference
	private ObjectRelationshipLocalService _objectRelationshipLocalService;

	@Reference
	private ObjectRelationshipService _objectRelationshipService;

	@Reference
	private PersistedModelLocalServiceRegistry
		_persistedModelLocalServiceRegistry;

	@Reference
	private SystemObjectDefinitionMetadataRegistry
		_systemObjectDefinitionMetadataRegistry;

	@Context
	private UriInfo _uriInfo;

}