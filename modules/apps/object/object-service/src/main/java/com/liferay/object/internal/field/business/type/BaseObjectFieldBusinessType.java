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

package com.liferay.object.internal.field.business.type;

import com.liferay.document.library.kernel.util.DLValidatorUtil;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectFieldSettingConstants;
import com.liferay.object.exception.ObjectFieldSettingNameException;
import com.liferay.object.exception.ObjectFieldSettingValueException;
import com.liferay.object.field.business.type.ObjectFieldBusinessType;
import com.liferay.object.field.render.ObjectFieldRenderingContext;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectFieldSetting;
import com.liferay.object.service.ObjectFieldSettingLocalService;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Carolina Barbosa
 */
public abstract class BaseObjectFieldBusinessType
	implements ObjectFieldBusinessType {

	@Override
	public Map<String, Object> getProperties(
		ObjectField objectField,
		ObjectFieldRenderingContext objectFieldRenderingContext) {

		return new HashMap<>(
			getObjectFieldSettingsValues(
				objectFieldSettingLocalService.
					getObjectFieldObjectFieldSettings(
						objectField.getObjectFieldId())));
	}

	protected Map<String, String> getObjectFieldSettingsValues(
		List<ObjectFieldSetting> objectFieldSettings) {

		Map<String, String> objectFieldSettingsValues = new HashMap<>();

		ListUtil.isNotEmptyForEach(
			objectFieldSettings,
			objectFieldSetting -> objectFieldSettingsValues.put(
				objectFieldSetting.getName(), objectFieldSetting.getValue()));

		return objectFieldSettingsValues;
	}

	protected void validateBooleanObjectFieldSetting(
			String objectFieldName, String objectFieldSettingName,
			Map<String, String> objectFieldSettingsValues)
		throws PortalException {

		String objectFieldSettingValue = objectFieldSettingsValues.get(
			objectFieldSettingName);

		if (Validator.isNotNull(objectFieldSettingValue) &&
			!StringUtil.equalsIgnoreCase(
				objectFieldSettingValue, StringPool.FALSE) &&
			!StringUtil.equalsIgnoreCase(
				objectFieldSettingValue, StringPool.TRUE)) {

			throw new ObjectFieldSettingValueException.InvalidValue(
				objectFieldName, objectFieldSettingName,
				objectFieldSettingValue);
		}
	}

	protected void validateNotAllowedObjectFieldSettingNames(
			Set<String> notAllowedObjectFieldSettingNames,
			String objectFieldName,
			Map<String, String> objectFieldSettingsValues)
		throws PortalException {

		for (String notAllowedObjectFieldSettingName :
				new HashSet<>(notAllowedObjectFieldSettingNames)) {

			if (!objectFieldSettingsValues.containsKey(
					notAllowedObjectFieldSettingName)) {

				notAllowedObjectFieldSettingNames.remove(
					notAllowedObjectFieldSettingName);
			}
		}

		if (!notAllowedObjectFieldSettingNames.isEmpty()) {
			throw new ObjectFieldSettingNameException.NotAllowedNames(
				objectFieldName, notAllowedObjectFieldSettingNames);
		}
	}

	protected void validateRelatedObjectFieldSettings(
			ObjectField objectField, String objectFieldSettingName1,
			String objectFieldSettingName2,
			Map<String, String> objectFieldSettingsValues)
		throws PortalException {

		validateBooleanObjectFieldSetting(
			objectField.getName(), objectFieldSettingName1,
			objectFieldSettingsValues);

		if (StringUtil.equalsIgnoreCase(
				objectFieldSettingsValues.get(objectFieldSettingName1),
				StringPool.TRUE)) {

			_validateObjectFieldSettingValue(
				objectField, objectFieldSettingName2,
				objectFieldSettingsValues);
		}
		else {
			validateNotAllowedObjectFieldSettingNames(
				SetUtil.fromArray(objectFieldSettingName2),
				objectField.getName(), objectFieldSettingsValues);
		}
	}

	@Reference
	protected JSONFactory jsonFactory;

	@Reference
	protected ObjectFieldSettingLocalService objectFieldSettingLocalService;

	private void _validateObjectFieldSettingValue(
			ObjectField objectField, String objectFieldSettingName,
			Map<String, String> objectFieldSettingsValues)
		throws PortalException {

		String objectFieldSettingValue = objectFieldSettingsValues.get(
			objectFieldSettingName);

		if (Validator.isNull(objectFieldSettingValue)) {
			throw new ObjectFieldSettingValueException.MissingRequiredValues(
				objectField.getName(),
				Collections.singleton(objectFieldSettingName));
		}

		if (Objects.equals(
				objectFieldSettingName,
				ObjectFieldSettingConstants.NAME_MAX_LENGTH)) {

			int maxLength = GetterUtil.getInteger(objectFieldSettingValue);

			int limitMaxLength = 280;

			if (objectField.compareBusinessType(
					ObjectFieldConstants.BUSINESS_TYPE_LONG_TEXT)) {

				limitMaxLength = 65000;
			}

			if ((maxLength < 1) || (maxLength > limitMaxLength)) {
				throw new ObjectFieldSettingValueException.InvalidValue(
					objectField.getName(),
					ObjectFieldSettingConstants.NAME_MAX_LENGTH,
					objectFieldSettingValue);
			}
		}
		else if (Objects.equals(
					objectFieldSettingName,
					ObjectFieldSettingConstants.NAME_STORAGE_DL_FOLDER_PATH)) {

			if (objectFieldSettingValue.length() > 255) {
				throw new ObjectFieldSettingValueException.
					MustBeLessThan256Characters();
			}

			for (String directoryName :
					StringUtil.split(
						objectFieldSettingValue, CharPool.FORWARD_SLASH)) {

				DLValidatorUtil.validateDirectoryName(directoryName);
			}
		}
	}

}