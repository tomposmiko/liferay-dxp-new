/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.web.internal.card.template.type;

import com.liferay.osb.faro.contacts.model.constants.ContactsCardTemplateConstants;
import com.liferay.osb.faro.web.internal.model.display.contacts.card.template.ContactsCardTemplateDisplay;
import com.liferay.osb.faro.web.internal.model.display.contacts.card.template.RecentActivitiesContactsCardTemplateDisplay;
import com.liferay.petra.string.StringPool;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Shinn Lok
 */
@Component(immediate = true, service = ContactsCardTemplateType.class)
public class RecentActivitiesContactsCardTemplateType
	extends BaseContactsCardTemplateType {

	@Override
	public String getDefaultName() {
		return _DEFAULT_NAME;
	}

	@Override
	public Map<String, Object> getDefaultSettings() {
		return _defaultSettings;
	}

	@Override
	public Class<? extends ContactsCardTemplateDisplay> getDisplayClass() {
		return RecentActivitiesContactsCardTemplateDisplay.class;
	}

	@Override
	public int getType() {
		return ContactsCardTemplateConstants.TYPE_RECENT_ACTIVITIES;
	}

	private static final String _DEFAULT_NAME = "Recent Activities";

	private static final Map<String, Object> _defaultSettings =
		new HashMap<String, Object>() {
			{
				put(
					"filterType",
					ContactsCardTemplateConstants.SETTINGS_FILTER_ALL);
				put("query", StringPool.BLANK);
			}
		};

}