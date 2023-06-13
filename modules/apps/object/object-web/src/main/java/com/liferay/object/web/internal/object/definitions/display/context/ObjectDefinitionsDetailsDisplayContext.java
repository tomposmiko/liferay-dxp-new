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

package com.liferay.object.web.internal.object.definitions.display.context;

import com.liferay.application.list.PanelCategory;
import com.liferay.application.list.PanelCategoryRegistry;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.object.constants.ObjectActionKeys;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.scope.ObjectScopeProvider;
import com.liferay.object.scope.ObjectScopeProviderRegistry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.web.internal.constants.ObjectWebKeys;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marco Leo
 */
public class ObjectDefinitionsDetailsDisplayContext
	extends BaseObjectDefinitionsDisplayContext {

	public ObjectDefinitionsDetailsDisplayContext(
		HttpServletRequest httpServletRequest,
		ObjectDefinitionLocalService objectDefinitionLocalService,
		ModelResourcePermission<ObjectDefinition>
			objectDefinitionModelResourcePermission,
		ObjectRelationshipLocalService objectRelationshipLocalService,
		ObjectScopeProviderRegistry objectScopeProviderRegistry,
		PanelCategoryRegistry panelCategoryRegistry) {

		super(httpServletRequest, objectDefinitionModelResourcePermission);

		_objectDefinitionLocalService = objectDefinitionLocalService;
		_objectRelationshipLocalService = objectRelationshipLocalService;
		_objectScopeProviderRegistry = objectScopeProviderRegistry;
		_panelCategoryRegistry = panelCategoryRegistry;
	}

	public List<Map<String, Object>> getNonrelationshipObjectFieldsInfo() {
		List<ObjectField> objectFields = ListUtil.filter(
			getObjectFields(),
			objectField -> Validator.isNull(objectField.getRelationshipType()));

		List<Map<String, Object>> nonrelationshipObjectFieldsInfo =
			new ArrayList<>();

		for (ObjectField objectField : objectFields) {
			nonrelationshipObjectFieldsInfo.add(
				HashMapBuilder.<String, Object>put(
					"label",
					LocalizationUtil.getLocalizationMap(objectField.getLabel())
				).put(
					"name", objectField.getName()
				).build());
		}

		return nonrelationshipObjectFieldsInfo;
	}

	public ObjectDefinition getObjectDefinition() {
		HttpServletRequest httpServletRequest =
			objectRequestHelper.getRequest();

		return (ObjectDefinition)httpServletRequest.getAttribute(
			ObjectWebKeys.OBJECT_DEFINITION);
	}

	public List<ObjectField> getObjectFields() {
		HttpServletRequest httpServletRequest =
			objectRequestHelper.getRequest();

		return (List<ObjectField>)httpServletRequest.getAttribute(
			ObjectWebKeys.OBJECT_FIELDS);
	}

	public String getScope() {
		ObjectDefinition objectDefinition = getObjectDefinition();

		return ParamUtil.getString(
			objectRequestHelper.getRequest(), "scope",
			objectDefinition.getScope());
	}

	public List<KeyValuePair> getScopeKeyValuePairs(String scope) {
		List<KeyValuePair> keyValuePairs = new ArrayList<>();

		ObjectScopeProvider objectScopeProvider =
			_objectScopeProviderRegistry.getObjectScopeProvider(scope);

		for (String panelCategoryKey :
				objectScopeProvider.getRootPanelCategoryKeys()) {

			if (panelCategoryKey.equals(PanelCategoryKeys.COMMERCE)) {
				continue;
			}

			PanelCategory panelCategory =
				_panelCategoryRegistry.getPanelCategory(panelCategoryKey);

			List<PanelCategory> childPanelCategories =
				_panelCategoryRegistry.getChildPanelCategories(
					panelCategoryKey);

			for (PanelCategory childPanelCategory : childPanelCategories) {
				keyValuePairs.add(
					new KeyValuePair(
						childPanelCategory.getKey(),
						StringBundler.concat(
							panelCategory.getLabel(
								objectRequestHelper.getLocale()),
							" > ",
							childPanelCategory.getLabel(
								objectRequestHelper.getLocale()))));
			}
		}

		return keyValuePairs;
	}

	public boolean hasPublishObjectPermission() {
		PortletResourcePermission portletResourcePermission =
			objectDefinitionModelResourcePermission.
				getPortletResourcePermission();

		return portletResourcePermission.contains(
			objectRequestHelper.getPermissionChecker(), null,
			ObjectActionKeys.PUBLISH_OBJECT_DEFINITION);
	}

	private final ObjectDefinitionLocalService _objectDefinitionLocalService;
	private final ObjectRelationshipLocalService
		_objectRelationshipLocalService;
	private final ObjectScopeProviderRegistry _objectScopeProviderRegistry;
	private final PanelCategoryRegistry _panelCategoryRegistry;

}