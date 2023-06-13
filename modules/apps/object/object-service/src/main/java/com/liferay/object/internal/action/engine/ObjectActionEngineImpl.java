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

package com.liferay.object.internal.action.engine;

import com.liferay.dynamic.data.mapping.expression.CreateExpressionRequest;
import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.object.action.engine.ObjectActionEngine;
import com.liferay.object.action.executor.ObjectActionExecutor;
import com.liferay.object.action.executor.ObjectActionExecutorRegistry;
import com.liferay.object.constants.ObjectActionConstants;
import com.liferay.object.internal.action.util.ObjectActionVariablesUtil;
import com.liferay.object.model.ObjectAction;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectActionLocalService;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Brian Wing Shun Chan
 */
@Component(service = ObjectActionEngine.class)
public class ObjectActionEngineImpl implements ObjectActionEngine {

	@Override
	public void executeObjectActions(
		String className, long companyId, String objectActionTriggerKey,
		JSONObject payloadJSONObject, long userId) {

		try {
			_executeObjectActions(
				className, companyId, objectActionTriggerKey, payloadJSONObject,
				userId);
		}
		catch (Exception exception) {
			_log.error(exception);
		}
	}

	private boolean _evaluateConditionExpression(
			String conditionExpression, Map<String, Object> variables)
		throws Exception {

		if (Validator.isNull(conditionExpression)) {
			return true;
		}

		DDMExpression<Boolean> ddmExpression =
			_ddmExpressionFactory.createExpression(
				CreateExpressionRequest.Builder.newBuilder(
					conditionExpression
				).build());

		ddmExpression.setVariables(variables);

		return ddmExpression.evaluate();
	}

	private void _executeObjectActions(
			String className, long companyId, String objectActionTriggerKey,
			JSONObject payloadJSONObject, long userId)
		throws Exception {

		if ((companyId == 0) || (userId == 0)) {
			return;
		}

		User user = _userLocalService.fetchUser(userId);

		if ((user == null) || (companyId != user.getCompanyId())) {
			return;
		}

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.fetchObjectDefinitionByClassName(
				user.getCompanyId(), className);

		if (objectDefinition == null) {
			return;
		}

		payloadJSONObject.put(
			"companyId", companyId
		).put(
			"objectDefinitionId", objectDefinition.getObjectDefinitionId()
		).put(
			"userId", userId
		);

		Set<Long> objectActionIds = _objectActionIdsThreadLocal.get();
		Map<String, Object> variables = ObjectActionVariablesUtil.toVariables(
			_dtoConverterRegistry, objectDefinition, payloadJSONObject);

		for (ObjectAction objectAction :
				_objectActionLocalService.getObjectActions(
					objectDefinition.getObjectDefinitionId(),
					objectActionTriggerKey)) {

			try {
				if (objectActionIds.contains(
						objectAction.getObjectActionId()) ||
					!_evaluateConditionExpression(
						objectAction.getConditionExpression(), variables)) {

					continue;
				}

				objectActionIds.add(objectAction.getObjectActionId());

				ObjectActionExecutor objectActionExecutor =
					_objectActionExecutorRegistry.getObjectActionExecutor(
						objectAction.getObjectActionExecutorKey());

				objectActionExecutor.execute(
					companyId, objectAction.getParametersUnicodeProperties(),
					payloadJSONObject, userId);

				_objectActionLocalService.updateStatus(
					objectAction.getObjectActionId(),
					ObjectActionConstants.STATUS_SUCCESS);
			}
			catch (Exception exception) {
				_log.error(exception);

				_objectActionLocalService.updateStatus(
					objectAction.getObjectActionId(),
					ObjectActionConstants.STATUS_FAILED);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectActionEngineImpl.class);

	private static final ThreadLocal<Set<Long>> _objectActionIdsThreadLocal =
		new CentralizedThreadLocal<>(
			ObjectActionEngineImpl.class.getName() +
				"._objectActionIdsThreadLocal",
			HashSet::new);

	@Reference
	private DDMExpressionFactory _ddmExpressionFactory;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private ObjectActionExecutorRegistry _objectActionExecutorRegistry;

	@Reference
	private ObjectActionLocalService _objectActionLocalService;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private UserLocalService _userLocalService;

}