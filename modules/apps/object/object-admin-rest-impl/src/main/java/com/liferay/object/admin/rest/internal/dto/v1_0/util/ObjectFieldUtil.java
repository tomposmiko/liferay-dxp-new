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

import com.liferay.list.type.model.ListTypeDefinition;
import com.liferay.list.type.model.ListTypeEntry;
import com.liferay.list.type.service.ListTypeDefinitionLocalService;
import com.liferay.list.type.service.ListTypeEntryLocalService;
import com.liferay.object.admin.rest.dto.v1_0.ObjectField;
import com.liferay.object.admin.rest.dto.v1_0.ObjectFieldSetting;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectFieldSettingConstants;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectFieldSettingLocalService;
import com.liferay.object.service.ObjectFilterLocalService;
import com.liferay.object.util.LocalizedMapUtil;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Collections;
import java.util.Objects;

/**
 * @author Gabriel Albuquerque
 */
public class ObjectFieldUtil {

	public static long addListTypeDefinition(
			long companyId,
			ListTypeDefinitionLocalService listTypeDefinitionLocalService,
			ListTypeEntryLocalService listTypeEntryLocalService,
			ObjectField objectField, long userId)
		throws Exception {

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-164278")) ||
			Validator.isNull(
				objectField.getListTypeDefinitionExternalReferenceCode())) {

			return 0;
		}

		ListTypeDefinition listTypeDefinition =
			listTypeDefinitionLocalService.
				fetchListTypeDefinitionByExternalReferenceCode(
					companyId,
					objectField.getListTypeDefinitionExternalReferenceCode());

		if (listTypeDefinition == null) {
			listTypeDefinition =
				listTypeDefinitionLocalService.addListTypeDefinition(
					objectField.getListTypeDefinitionExternalReferenceCode(),
					userId);
		}

		if (objectField.getObjectFieldSettings() != null) {
			_addListTypeEntries(
				listTypeDefinition, listTypeEntryLocalService, objectField,
				userId);
		}

		return listTypeDefinition.getListTypeDefinitionId();
	}

	public static String getDBType(String dbType, String type) {
		if (Validator.isNull(dbType) && Validator.isNotNull(type)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"The type property is deprecated. Use the DBType " +
						"property instead.");
			}

			return type;
		}

		return dbType;
	}

	public static long getListTypeDefinitionId(
		long companyId,
		ListTypeDefinitionLocalService listTypeDefinitionLocalService,
		ObjectField objectField) {

		if (!StringUtil.equals(
				objectField.getBusinessTypeAsString(),
				ObjectFieldConstants.BUSINESS_TYPE_MULTISELECT_PICKLIST) &&
			!StringUtil.equals(
				objectField.getBusinessTypeAsString(),
				ObjectFieldConstants.BUSINESS_TYPE_PICKLIST)) {

			return 0;
		}

		long listTypeDefinitionId = GetterUtil.getLong(
			objectField.getListTypeDefinitionId());

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-164278")) ||
			(listTypeDefinitionId != 0) ||
			Validator.isNull(
				objectField.getListTypeDefinitionExternalReferenceCode())) {

			return listTypeDefinitionId;
		}

		ListTypeDefinition listTypeDefinition =
			listTypeDefinitionLocalService.
				fetchListTypeDefinitionByExternalReferenceCode(
					companyId,
					objectField.getListTypeDefinitionExternalReferenceCode());

		if (listTypeDefinition == null) {
			return 0;
		}

		return listTypeDefinition.getListTypeDefinitionId();
	}

	public static com.liferay.object.model.ObjectField toObjectField(
		ListTypeDefinitionLocalService listTypeDefinitionLocalService,
		ObjectField objectField,
		ObjectFieldLocalService objectFieldLocalService,
		ObjectFieldSettingLocalService objectFieldSettingLocalService,
		ObjectFilterLocalService objectFilterLocalService) {

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-164948")) &&
			Objects.equals(
				objectField.getBusinessTypeAsString(),
				ObjectFieldConstants.BUSINESS_TYPE_FORMULA)) {

			throw new UnsupportedOperationException();
		}

		com.liferay.object.model.ObjectField serviceBuilderObjectField =
			objectFieldLocalService.createObjectField(0L);

		serviceBuilderObjectField.setExternalReferenceCode(
			objectField.getExternalReferenceCode());

		long listTypeDefinitionId = getListTypeDefinitionId(
			serviceBuilderObjectField.getCompanyId(),
			listTypeDefinitionLocalService, objectField);

		serviceBuilderObjectField.setListTypeDefinitionId(listTypeDefinitionId);

		serviceBuilderObjectField.setBusinessType(
			objectField.getBusinessTypeAsString());
		serviceBuilderObjectField.setDBType(
			getDBType(
				objectField.getDBTypeAsString(),
				objectField.getTypeAsString()));

		if (Validator.isNotNull(objectField.getDefaultValue())) {
			serviceBuilderObjectField.setDefaultValue(
				objectField.getDefaultValue());
		}

		serviceBuilderObjectField.setIndexed(
			GetterUtil.getBoolean(objectField.getIndexed()));
		serviceBuilderObjectField.setIndexedAsKeyword(
			GetterUtil.getBoolean(objectField.getIndexedAsKeyword()));
		serviceBuilderObjectField.setIndexedLanguageId(
			objectField.getIndexedLanguageId());
		serviceBuilderObjectField.setLabelMap(
			LocalizedMapUtil.getLocalizedMap(objectField.getLabel()));
		serviceBuilderObjectField.setName(objectField.getName());
		serviceBuilderObjectField.setObjectFieldSettings(
			TransformUtil.transformToList(
				objectField.getObjectFieldSettings(),
				objectFieldSetting ->
					ObjectFieldSettingUtil.toObjectFieldSetting(
						objectField.getBusinessTypeAsString(),
						listTypeDefinitionId, objectFieldSetting,
						objectFieldSettingLocalService,
						objectFilterLocalService)));
		serviceBuilderObjectField.setRequired(
			GetterUtil.getBoolean(objectField.getRequired()));

		if (Validator.isNotNull(objectField.getState())) {
			serviceBuilderObjectField.setState(objectField.getState());
		}

		serviceBuilderObjectField.setSystem(
			GetterUtil.getBoolean(objectField.getSystem()));

		return serviceBuilderObjectField;
	}

	private static void _addListTypeEntries(
			ListTypeDefinition listTypeDefinition,
			ListTypeEntryLocalService listTypeEntryLocalService,
			ObjectField objectField, long userId)
		throws Exception {

		for (ObjectFieldSetting objectFieldSetting :
				objectField.getObjectFieldSettings()) {

			if (!StringUtil.equals(
					objectFieldSetting.getName(),
					ObjectFieldSettingConstants.NAME_STATE_FLOW)) {

				continue;
			}

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				JSONFactoryUtil.looseSerializeDeep(
					objectFieldSetting.getValue()));

			JSONArray objectStatesJSONArray = jsonObject.getJSONArray(
				"objectStates");

			for (int i = 0; i < objectStatesJSONArray.length(); i++) {
				JSONObject objectStateJSONObject =
					objectStatesJSONArray.getJSONObject(i);

				String key = objectStateJSONObject.getString("key");

				ListTypeEntry listTypeEntry =
					listTypeEntryLocalService.fetchListTypeEntry(
						listTypeDefinition.getListTypeDefinitionId(), key);

				if (listTypeEntry != null) {
					continue;
				}

				listTypeEntryLocalService.addListTypeEntry(
					userId, listTypeDefinition.getListTypeDefinitionId(), key,
					Collections.singletonMap(LocaleUtil.getDefault(), key));
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectFieldUtil.class);

}