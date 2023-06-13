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

package com.liferay.object.admin.rest.internal.dto.v1_0.util;

import com.liferay.object.admin.rest.dto.v1_0.ObjectField;
import com.liferay.object.admin.rest.dto.v1_0.ObjectFieldSetting;
import com.liferay.object.admin.rest.dto.v1_0.ObjectStateFlow;
import com.liferay.object.constants.ObjectFieldSettingConstants;
import com.liferay.object.field.setting.builder.ObjectFieldSettingBuilder;
import com.liferay.object.model.ObjectFieldSettingModel;
import com.liferay.object.model.ObjectFilter;
import com.liferay.object.service.ObjectFieldSettingLocalService;
import com.liferay.object.service.ObjectFilterLocalService;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Carolina Barbosa
 */
public class ObjectFieldSettingUtil {

	public static List<com.liferay.object.model.ObjectFieldSetting>
		toObjectFieldSettings(
			long listTypeDefinitionId, ObjectField objectField,
			ObjectFieldSettingLocalService objectFieldSettingLocalService,
			ObjectFilterLocalService objectFilterLocalService) {

		List<com.liferay.object.model.ObjectFieldSetting> objectFieldSettings =
			TransformUtil.transformToList(
				objectField.getObjectFieldSettings(),
				objectFieldSetting -> _toObjectFieldSetting(
					listTypeDefinitionId, objectFieldSetting,
					objectFieldSettingLocalService, objectFilterLocalService));

		List<String> objectFieldSettingNames = ListUtil.toList(
			objectFieldSettings, ObjectFieldSettingModel::getName);

		if (!FeatureFlagManagerUtil.isEnabled("LPS-163716") &&
			(objectFieldSettingNames.contains(
				ObjectFieldSettingConstants.NAME_DEFAULT_VALUE) ||
			 objectFieldSettingNames.contains(
				 ObjectFieldSettingConstants.NAME_DEFAULT_VALUE_TYPE))) {

			throw new UnsupportedOperationException();
		}

		if (objectFieldSettingNames.contains(
				ObjectFieldSettingConstants.NAME_DEFAULT_VALUE) ||
			Validator.isNull(objectField.getDefaultValue())) {

			return objectFieldSettings;
		}

		objectFieldSettings.add(
			new ObjectFieldSettingBuilder(
			).name(
				ObjectFieldSettingConstants.NAME_DEFAULT_VALUE
			).value(
				objectField.getDefaultValue()
			).build());
		objectFieldSettings.add(
			new ObjectFieldSettingBuilder(
			).name(
				ObjectFieldSettingConstants.NAME_DEFAULT_VALUE_TYPE
			).value(
				ObjectFieldSettingConstants.VALUE_INPUT_AS_VALUE
			).build());

		return objectFieldSettings;
	}

	private static com.liferay.object.model.ObjectFieldSetting
			_toObjectFieldSetting(
				long listTypeDefinitionId,
				ObjectFieldSetting objectFieldSetting,
				ObjectFieldSettingLocalService objectFieldSettingLocalService,
				ObjectFilterLocalService objectFilterLocalService)
		throws Exception {

		com.liferay.object.model.ObjectFieldSetting
			serviceBuilderObjectFieldSetting =
				objectFieldSettingLocalService.createObjectFieldSetting(0L);

		serviceBuilderObjectFieldSetting.setName(objectFieldSetting.getName());

		if (serviceBuilderObjectFieldSetting.compareName(
				ObjectFieldSettingConstants.NAME_STATE_FLOW)) {

			serviceBuilderObjectFieldSetting.setObjectStateFlow(
				ObjectStateFlowUtil.toObjectStateFlow(
					listTypeDefinitionId,
					ObjectMapperUtil.readValue(
						ObjectStateFlow.class, objectFieldSetting.getValue())));
		}

		serviceBuilderObjectFieldSetting.setValue(
			String.valueOf(objectFieldSetting.getValue()));

		if (serviceBuilderObjectFieldSetting.compareName(
				ObjectFieldSettingConstants.NAME_FILTERS)) {

			List<ObjectFilter> objectFilters = new ArrayList<>();

			List<Object> values = null;

			if (objectFieldSetting.getValue() instanceof JSONArray) {
				values = JSONUtil.toList(
					(JSONArray)objectFieldSetting.getValue(),
					jsonObject -> jsonObject.toMap());
			}
			else if (objectFieldSetting.getValue() instanceof Object[]) {
				values = ListUtil.fromArray(
					(Object[])objectFieldSetting.getValue());
			}
			else {
				values = (List<Object>)objectFieldSetting.getValue();
			}

			for (Object value : values) {
				Map<String, Object> valueMap = (Map<String, Object>)value;

				ObjectFilter objectFilter =
					objectFilterLocalService.createObjectFilter(0L);

				objectFilter.setFilterBy(
					String.valueOf(valueMap.get("filterBy")));
				objectFilter.setFilterType(
					String.valueOf(valueMap.get("filterType")));
				objectFilter.setJSON(
					String.valueOf(
						JSONFactoryUtil.createJSONObject(
							(Map)valueMap.get("json"))));

				objectFilters.add(objectFilter);
			}

			serviceBuilderObjectFieldSetting.setObjectFilters(objectFilters);
		}

		return serviceBuilderObjectFieldSetting;
	}

}