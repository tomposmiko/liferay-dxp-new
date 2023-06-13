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

package com.liferay.notification.web.internal.portlet.action;

import com.liferay.notification.constants.NotificationPortletKeys;
import com.liferay.object.definition.notification.term.util.ObjectDefinitionNotificationTermUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Paulo Albuquerque
 */
@Component(
	property = {
		"javax.portlet.name=" + NotificationPortletKeys.NOTIFICATION_TEMPLATES,
		"mvc.command.name=/notification_templates/get_object_field_notification_template_terms"
	},
	service = MVCResourceCommand.class
)
public class GetObjectFieldNotificationTemplateTermsMVCResourceCommand
	extends BaseNotificationTemplateTermsMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		_objectDefinition = _objectDefinitionLocalService.fetchObjectDefinition(
			ParamUtil.getLong(resourceRequest, "objectDefinitionId"));

		if (_objectDefinition == null) {
			return;
		}

		super.doServeResource(resourceRequest, resourceResponse);
	}

	@Override
	protected Set<Map.Entry<String, String>> getTermNamesEntries() {
		Map<String, String> termNames = new LinkedHashMap<>();

		List<ObjectField> objectFields =
			_objectFieldLocalService.getObjectFields(
				_objectDefinition.getObjectDefinitionId());

		for (ObjectField objectField : objectFields) {
			if (StringUtil.equals(objectField.getName(), "creator") &&
				FeatureFlagManagerUtil.isEnabled("LPS-171625")) {

				_objectFieldNames.forEach(
					(termLabel, objectFieldName) -> termNames.put(
						termLabel, _getTermName(objectFieldName)));
			}
			else {
				termNames.put(
					objectField.getLabel(user.getLocale()),
					_getTermName(objectField.getName()));
			}
		}

		return termNames.entrySet();
	}

	private String _getTermName(String objectFieldName) {
		return ObjectDefinitionNotificationTermUtil.getObjectFieldTermName(
			_objectDefinition.getShortName(), objectFieldName);
	}

	private ObjectDefinition _objectDefinition;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectFieldLocalService _objectFieldLocalService;

	private final Map<String, String> _objectFieldNames = HashMapBuilder.put(
		"author-email-address", "AUTHOR_EMAIL_ADDRESS"
	).put(
		"author-first-name", "AUTHOR_FIRST_NAME"
	).put(
		"author-id", "AUTHOR_ID"
	).put(
		"author-last-name", "AUTHOR_LAST_NAME"
	).put(
		"author-middle-name", "AUTHOR_MIDDLE_NAME"
	).put(
		"author-prefix", "AUTHOR_PREFIX"
	).put(
		"author-suffix", "AUTHOR_SUFFIX"
	).build();

}