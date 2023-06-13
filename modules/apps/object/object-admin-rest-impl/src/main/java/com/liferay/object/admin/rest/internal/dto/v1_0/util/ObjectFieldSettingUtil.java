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

import com.liferay.object.admin.rest.dto.v1_0.ObjectFieldSetting;
import com.liferay.object.admin.rest.dto.v1_0.ObjectStateFlow;
import com.liferay.object.admin.rest.dto.v1_0.util.ObjectStateFlowUtil;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectFieldSettingConstants;
import com.liferay.object.model.ObjectFilter;
import com.liferay.object.service.ObjectFieldSettingLocalService;
import com.liferay.object.service.ObjectFilterLocalService;
import com.liferay.object.service.ObjectStateFlowLocalServiceUtil;
import com.liferay.object.util.ObjectFilterUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Carolina Barbosa
 */
public class ObjectFieldSettingUtil {

	public static com.liferay.object.model.ObjectFieldSetting
			toObjectFieldSetting(
				String businessType, ObjectFieldSetting objectFieldSetting,
				ObjectFieldSettingLocalService objectFieldSettingLocalService,
				ObjectFilterLocalService objectFilterLocalService)
		throws PortalException {

		com.liferay.object.model.ObjectFieldSetting
			serviceBuilderObjectFieldSetting =
				objectFieldSettingLocalService.createObjectFieldSetting(0L);

		serviceBuilderObjectFieldSetting.setName(objectFieldSetting.getName());

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-164278")) &&
			Objects.equals(
				ObjectFieldSettingConstants.NAME_STATE_FLOW,
				objectFieldSetting.getName())) {

			serviceBuilderObjectFieldSetting.setObjectStateFlow(
				com.liferay.object.admin.rest.internal.dto.v1_0.util.
					ObjectStateFlowUtil.toObjectStateFlow(
						objectFieldSetting.getObjectFieldId(),
						ObjectMapperUtil.readValue(
							ObjectStateFlow.class,
							objectFieldSetting.getValue())));
		}

		serviceBuilderObjectFieldSetting.setValue(
			String.valueOf(objectFieldSetting.getValue()));

		if (Objects.equals(
				businessType, ObjectFieldConstants.BUSINESS_TYPE_AGGREGATION) &&
			Objects.equals(
				objectFieldSetting.getName(),
				ObjectFieldSettingConstants.NAME_FILTERS)) {

			List<ObjectFilter> objectFilters = new ArrayList<>();

			List<Object> values = null;

			if (objectFieldSetting.getValue() instanceof Object[]) {
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

	public static ObjectFieldSetting toObjectFieldSetting(
		String businessType,
		com.liferay.object.model.ObjectFieldSetting
			serviceBuilderObjectFieldSetting) {

		if (serviceBuilderObjectFieldSetting == null) {
			return null;
		}

		ObjectFieldSetting objectFieldSetting = new ObjectFieldSetting() {
			{
				name = serviceBuilderObjectFieldSetting.getName();
				value = serviceBuilderObjectFieldSetting.getValue();
			}
		};

		if (Objects.equals(
				businessType, ObjectFieldConstants.BUSINESS_TYPE_AGGREGATION) &&
			Objects.equals(
				objectFieldSetting.getName(),
				ObjectFieldSettingConstants.NAME_FILTERS)) {

			objectFieldSetting.setValue(
				ObjectFilterUtil.getObjectFiltersJSONArray(
					serviceBuilderObjectFieldSetting.getObjectFilters()));
		}
		else if (Objects.equals(
					ObjectFieldSettingConstants.NAME_STATE_FLOW,
					objectFieldSetting.getName())) {

			objectFieldSetting.setValue(
				ObjectStateFlowUtil.toObjectStateFlow(
					ObjectStateFlowLocalServiceUtil.fetchObjectStateFlow(
						GetterUtil.getLong(
							serviceBuilderObjectFieldSetting.getValue()))));
		}

		return objectFieldSetting;
	}

}