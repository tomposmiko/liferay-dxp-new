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

package com.liferay.object.rest.internal.vulcan.openapi.contributor;

import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.rest.internal.vulcan.openapi.contributor.util.OpenAPIContributorUtil;
import com.liferay.object.rest.openapi.v1_0.ObjectEntryOpenAPIResource;
import com.liferay.object.rest.openapi.v1_0.ObjectEntryOpenAPIResourceProvider;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.system.JaxRsApplicationDescriptor;
import com.liferay.object.system.SystemObjectDefinitionMetadata;
import com.liferay.object.system.SystemObjectDefinitionMetadataRegistry;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.openapi.OpenAPIContext;
import com.liferay.portal.vulcan.openapi.contributor.OpenAPIContributor;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luis Miguel Barcos
 */
@Component(service = OpenAPIContributor.class)
public class RelatedObjectEntryOpenAPIContributor
	extends BaseOpenAPIContributor {

	@Override
	public void contribute(OpenAPI openAPI, OpenAPIContext openAPIContext)
		throws Exception {

		if (openAPIContext == null) {
			return;
		}

		Map<ObjectDefinition, SystemObjectDefinitionMetadata>
			systemObjectDefinitionMetadataMap = new HashMap<>();

		for (ObjectDefinition systemObjectDefinition :
				_objectDefinitionLocalService.getSystemObjectDefinitions()) {

			SystemObjectDefinitionMetadata systemObjectDefinitionMetadata =
				_systemObjectDefinitionMetadataRegistry.
					getSystemObjectDefinitionMetadata(
						systemObjectDefinition.getName());

			if (systemObjectDefinitionMetadata == null) {
				continue;
			}

			JaxRsApplicationDescriptor jaxRsApplicationDescriptor =
				systemObjectDefinitionMetadata.getJaxRsApplicationDescriptor();

			String path = openAPIContext.getPath();

			if (path.contains(
					jaxRsApplicationDescriptor.getApplicationPath())) {

				systemObjectDefinitionMetadataMap.put(
					systemObjectDefinition, systemObjectDefinitionMetadata);
			}
		}

		for (Map.Entry<ObjectDefinition, SystemObjectDefinitionMetadata> entry :
				systemObjectDefinitionMetadataMap.entrySet()) {

			ObjectDefinition systemObjectDefinition = entry.getKey();

			for (ObjectRelationship systemObjectRelationship :
					_objectRelationshipLocalService.getObjectRelationships(
						systemObjectDefinition.getObjectDefinitionId())) {

				_contribute(
					openAPI, systemObjectDefinition, entry.getValue(),
					systemObjectRelationship, openAPIContext.getVersion());
			}
		}
	}

	@Activate
	protected void activate() {
		init(_dtoConverterRegistry, _systemObjectDefinitionMetadataRegistry);
	}

	private void _contribute(
			OpenAPI openAPI, ObjectDefinition systemObjectDefinition,
			SystemObjectDefinitionMetadata systemObjectDefinitionMetadata,
			ObjectRelationship systemObjectRelationship, String version)
		throws Exception {

		ObjectDefinition relatedObjectDefinition = _getRelatedObjectDefinition(
			systemObjectDefinition, systemObjectRelationship);

		if (!relatedObjectDefinition.isActive()) {
			return;
		}

		Paths paths = openAPI.getPaths();

		String relatedSchemaName = getSchemaName(relatedObjectDefinition);

		ObjectEntryOpenAPIResource objectEntryOpenAPIResource =
			_objectEntryOpenAPIResourceProvider.getObjectEntryOpenAPIResource(
				relatedObjectDefinition);

		Map<String, Schema> relatedSchemas =
			objectEntryOpenAPIResource.getSchemas();

		OpenAPIContributorUtil.copySchemas(
			relatedSchemaName, relatedSchemas,
			relatedObjectDefinition.isUnmodifiableSystemObject(), openAPI);

		JaxRsApplicationDescriptor jaxRsApplicationDescriptor =
			systemObjectDefinitionMetadata.getJaxRsApplicationDescriptor();
		String schemaName = getSchemaName(systemObjectDefinition);

		String name = StringBundler.concat(
			StringPool.SLASH, version, StringPool.SLASH,
			jaxRsApplicationDescriptor.getPath(), StringPool.SLASH,
			_getIdParameterTemplate(schemaName), StringPool.SLASH,
			systemObjectRelationship.getName());

		paths.addPathItem(
			name,
			new PathItem() {
				{
					get(
						_getGetOperation(
							systemObjectRelationship, relatedSchemaName,
							schemaName));
				}
			});
		paths.addPathItem(
			StringBundler.concat(
				name, StringPool.SLASH,
				_getIdParameterTemplate(
					relatedObjectDefinition.getShortName())),
			new PathItem() {
				{
					delete(
						_getDeleteOperation(
							systemObjectRelationship, relatedSchemaName,
							schemaName));
					put(
						_getPutOperation(
							systemObjectRelationship, relatedSchemaName,
							schemaName));
				}
			});
	}

	private Content _getContent(String schemaName) {
		Content content = new Content();

		MediaType mediaType = new MediaType();

		if (schemaName != null) {
			Schema schema = new Schema();

			schema.set$ref(schemaName);

			mediaType.setSchema(schema);
		}

		content.addMediaType("application/json", mediaType);
		content.addMediaType("application/xml", mediaType);

		return content;
	}

	private Operation _getDeleteOperation(
		ObjectRelationship objectRelationship, String relatedSchemaName,
		String schemaName) {

		return new Operation() {
			{
				operationId(
					_getOperationId(
						"delete", objectRelationship.getName(), schemaName));
				parameters(
					Arrays.asList(
						new Parameter() {
							{
								in("path");
								name(_getIdParameterName(schemaName));
								required(true);
							}
						},
						new Parameter() {
							{
								in("path");
								name(_getIdParameterName(relatedSchemaName));
								required(true);
							}
						}));
				responses(
					new ApiResponses() {
						{
							setDefault(
								new ApiResponse() {
									{
										setContent(_getContent(null));
									}
								});
						}
					});
				tags(Arrays.asList(schemaName));
			}
		};
	}

	private Operation _getGetOperation(
		ObjectRelationship objectRelationship, String relatedSchemaName,
		String schemaName) {

		String parameterName = _getIdParameterName(schemaName);

		return new Operation() {
			{
				operationId(
					_getOperationId(
						"get", objectRelationship.getName(), schemaName));
				parameters(
					Arrays.asList(
						new Parameter() {
							{
								in("path");
								name(parameterName);
								required(true);
							}
						}));
				responses(
					new ApiResponses() {
						{
							setDefault(
								new ApiResponse() {
									{
										setContent(
											_getContent(
												OpenAPIContributorUtil.
													getPageSchemaName(
														relatedSchemaName)));
									}
								});
						}
					});
				tags(Arrays.asList(schemaName));
			}
		};
	}

	private String _getIdParameterName(String name) {
		return StringUtil.lowerCaseFirstLetter(name) + "Id";
	}

	private String _getIdParameterTemplate(String name) {
		return StringPool.OPEN_CURLY_BRACE + _getIdParameterName(name) +
			StringPool.CLOSE_CURLY_BRACE;
	}

	private String _getOperationId(
		String method, String objectRelationshipName,
		String systemObjectDefinitionName) {

		String sufix = "";

		if (StringUtil.equals(method, "get")) {
			sufix = "Page";
		}

		return StringBundler.concat(
			method, systemObjectDefinitionName,
			StringUtil.upperCaseFirstLetter(objectRelationshipName), sufix);
	}

	private Operation _getPutOperation(
		ObjectRelationship objectRelationship, String relatedSchemaName,
		String schemaName) {

		return new Operation() {
			{
				operationId(
					_getOperationId(
						"put", objectRelationship.getName(), schemaName));
				parameters(
					Arrays.asList(
						new Parameter() {
							{
								in("path");
								name(_getIdParameterName(schemaName));
								required(true);
							}
						},
						new Parameter() {
							{
								in("path");
								name(_getIdParameterName(relatedSchemaName));
								required(true);
							}
						}));
				responses(
					new ApiResponses() {
						{
							setDefault(
								new ApiResponse() {
									{
										setContent(
											_getContent(relatedSchemaName));
									}
								});
						}
					});
				tags(Arrays.asList(schemaName));
			}
		};
	}

	private ObjectDefinition _getRelatedObjectDefinition(
			ObjectDefinition systemObjectDefinition,
			ObjectRelationship systemObjectRelationship)
		throws Exception {

		long objectDefinitionId1 =
			systemObjectRelationship.getObjectDefinitionId1();

		if (objectDefinitionId1 !=
				systemObjectDefinition.getObjectDefinitionId()) {

			return _objectDefinitionLocalService.getObjectDefinition(
				systemObjectRelationship.getObjectDefinitionId1());
		}

		return _objectDefinitionLocalService.getObjectDefinition(
			systemObjectRelationship.getObjectDefinitionId2());
	}

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryOpenAPIResourceProvider
		_objectEntryOpenAPIResourceProvider;

	@Reference
	private ObjectRelationshipLocalService _objectRelationshipLocalService;

	@Reference
	private SystemObjectDefinitionMetadataRegistry
		_systemObjectDefinitionMetadataRegistry;

}