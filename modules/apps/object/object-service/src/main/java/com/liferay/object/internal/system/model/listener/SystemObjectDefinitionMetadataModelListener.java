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

package com.liferay.object.internal.system.model.listener;

import com.liferay.object.action.engine.ObjectActionEngine;
import com.liferay.object.constants.ObjectActionTriggerConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectValidationRuleLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.extension.EntityExtensionThreadLocal;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Brian Wing Shun Chan
 */
public class SystemObjectDefinitionMetadataModelListener<T extends BaseModel<T>>
	extends BaseModelListener<T> {

	public SystemObjectDefinitionMetadataModelListener(
		DTOConverterRegistry dtoConverterRegistry, JSONFactory jsonFactory,
		Class<T> modelClass, ObjectActionEngine objectActionEngine,
		ObjectDefinitionLocalService objectDefinitionLocalService,
		ObjectEntryLocalService objectEntryLocalService,
		ObjectValidationRuleLocalService objectValidationRuleLocalService,
		UserLocalService userLocalService) {

		_dtoConverterRegistry = dtoConverterRegistry;
		_jsonFactory = jsonFactory;
		_modelClass = modelClass;
		_objectActionEngine = objectActionEngine;
		_objectDefinitionLocalService = objectDefinitionLocalService;
		_objectEntryLocalService = objectEntryLocalService;
		_objectValidationRuleLocalService = objectValidationRuleLocalService;
		_userLocalService = userLocalService;
	}

	@Override
	public Class<?> getModelClass() {
		return _modelClass;
	}

	@Override
	public void onAfterCreate(T baseModel) throws ModelListenerException {
		_executeObjectActions(
			ObjectActionTriggerConstants.KEY_ON_AFTER_ADD, null,
			(T)baseModel.clone());
	}

	@Override
	public void onAfterRemove(T baseModel) throws ModelListenerException {
		_executeObjectActions(
			ObjectActionTriggerConstants.KEY_ON_AFTER_DELETE, null, baseModel);
	}

	@Override
	public void onAfterUpdate(T originalBaseModel, T baseModel)
		throws ModelListenerException {

		_executeObjectActions(
			ObjectActionTriggerConstants.KEY_ON_AFTER_UPDATE, originalBaseModel,
			(T)baseModel.clone());
	}

	@Override
	public void onBeforeCreate(T model) throws ModelListenerException {
		_validateSystemObject(model);
	}

	@Override
	public void onBeforeRemove(T baseModel) throws ModelListenerException {
		try {
			ObjectDefinition objectDefinition =
				_objectDefinitionLocalService.fetchObjectDefinitionByClassName(
					_getCompanyId(baseModel), _modelClass.getName());

			if (objectDefinition == null) {
				return;
			}

			_objectEntryLocalService.
				deleteExtensionDynamicObjectDefinitionTableValues(
					objectDefinition,
					GetterUtil.getLong(baseModel.getPrimaryKeyObj()));
		}
		catch (PortalException portalException) {
			throw new ModelListenerException(portalException);
		}
	}

	@Override
	public void onBeforeUpdate(T originalModel, T model)
		throws ModelListenerException {

		_validateSystemObject(model);
	}

	private void _executeObjectActions(
			String objectActionTriggerKey, T originalBaseModel, T baseModel)
		throws ModelListenerException {

		try {
			ObjectDefinition objectDefinition =
				_objectDefinitionLocalService.fetchObjectDefinitionByClassName(
					_getCompanyId(baseModel), _modelClass.getName());

			if (objectDefinition == null) {
				return;
			}

			long userId = PrincipalThreadLocal.getUserId();

			if (userId == 0) {
				userId = _getUserId(baseModel);
			}

			_objectActionEngine.executeObjectActions(
				_modelClass.getName(), _getCompanyId(baseModel),
				objectActionTriggerKey,
				_getPayloadJSONObject(
					objectActionTriggerKey, objectDefinition, originalBaseModel,
					baseModel, userId),
				userId);
		}
		catch (PortalException portalException) {
			throw new ModelListenerException(portalException);
		}
	}

	private long _getCompanyId(T baseModel) {
		Map<String, Function<Object, Object>> functions =
			(Map<String, Function<Object, Object>>)
				(Map<String, ?>)baseModel.getAttributeGetterFunctions();

		Function<Object, Object> function = functions.get("companyId");

		if (function == null) {
			throw new IllegalArgumentException(
				"Base model does not have a company ID column");
		}

		return (Long)function.apply(baseModel);
	}

	private DTOConverter<T, ?> _getDTOConverter() {
		return (DTOConverter<T, ?>)_dtoConverterRegistry.getDTOConverter(
			_modelClass.getName());
	}

	private String _getDTOConverterType() {
		DTOConverter<T, ?> dtoConverter = _getDTOConverter();

		if (dtoConverter == null) {
			return _modelClass.getSimpleName();
		}

		return dtoConverter.getContentType();
	}

	private JSONObject _getPayloadJSONObject(
			String objectActionTriggerKey, ObjectDefinition objectDefinition,
			T originalBaseModel, T baseModel, long userId)
		throws PortalException {

		String dtoConverterType = _getDTOConverterType();

		return JSONUtil.put(
			"classPK", baseModel.getPrimaryKeyObj()
		).put(
			"extendedProperties",
			HashMapBuilder.<String, Object>putAll(
				_objectEntryLocalService.
					getExtensionDynamicObjectDefinitionTableValues(
						objectDefinition,
						GetterUtil.getLong(baseModel.getPrimaryKeyObj()))
			).putAll(
				EntityExtensionThreadLocal.getExtendedProperties()
			).build()
		).put(
			"model" + _modelClass.getSimpleName(),
			baseModel.getModelAttributes()
		).put(
			"modelDTO" + dtoConverterType, _toDTO(baseModel, userId)
		).put(
			"objectActionTriggerKey", objectActionTriggerKey
		).put(
			"original" + _modelClass.getSimpleName(),
			() -> {
				if (originalBaseModel == null) {
					return null;
				}

				return originalBaseModel.getModelAttributes();
			}
		).put(
			"originalDTO" + dtoConverterType,
			() -> {
				if (originalBaseModel == null) {
					return null;
				}

				return _toDTO(originalBaseModel, userId);
			}
		);
	}

	private long _getUserId(T baseModel) {
		Map<String, Function<Object, Object>> functions =
			(Map<String, Function<Object, Object>>)
				(Map<String, ?>)baseModel.getAttributeGetterFunctions();

		Function<Object, Object> function = functions.get("userId");

		if (function == null) {
			throw new IllegalArgumentException(
				"Base model does not have a user ID column");
		}

		return (Long)function.apply(baseModel);
	}

	private Map<String, Object> _toDTO(T baseModel, long userId) {
		DTOConverter<T, ?> dtoConverter = _getDTOConverter();

		Map<String, Object> modelAttributes = baseModel.getModelAttributes();

		if (dtoConverter == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"No DTO converter found for " + _modelClass.getName());
			}

			return modelAttributes;
		}

		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("No user found with user ID " + userId);
			}

			return modelAttributes;
		}

		DefaultDTOConverterContext defaultDTOConverterContext =
			new DefaultDTOConverterContext(
				false, Collections.emptyMap(), _dtoConverterRegistry,
				baseModel.getPrimaryKeyObj(), user.getLocale(), null, user);

		try {
			Object object = dtoConverter.toDTO(defaultDTOConverterContext);

			if (object == null) {
				return modelAttributes;
			}

			JSONObject jsonObject = _jsonFactory.createJSONObject(
				_jsonFactory.looseSerializeDeep(object));

			return jsonObject.put(
				"createDate", modelAttributes.get("createDate")
			).put(
				"modifiedDate", modelAttributes.get("modifiedDate")
			).put(
				"status", modelAttributes.get("status")
			).put(
				"userName", user.getFullName()
			).put(
				"uuid", modelAttributes.get("uuid")
			).toMap();
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return baseModel.getModelAttributes();
	}

	private void _validateSystemObject(T model) throws ModelListenerException {
		try {
			ObjectDefinition objectDefinition =
				_objectDefinitionLocalService.fetchObjectDefinitionByClassName(
					_getCompanyId(model), _modelClass.getName());

			if (objectDefinition == null) {
				return;
			}

			_objectValidationRuleLocalService.validate(
				model, objectDefinition.getObjectDefinitionId());
		}
		catch (PortalException portalException) {
			throw new ModelListenerException(portalException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SystemObjectDefinitionMetadataModelListener.class);

	private final DTOConverterRegistry _dtoConverterRegistry;
	private final JSONFactory _jsonFactory;
	private final Class<?> _modelClass;
	private final ObjectActionEngine _objectActionEngine;
	private final ObjectDefinitionLocalService _objectDefinitionLocalService;
	private final ObjectEntryLocalService _objectEntryLocalService;
	private final ObjectValidationRuleLocalService
		_objectValidationRuleLocalService;
	private final UserLocalService _userLocalService;

}