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
import com.liferay.osb.faro.web.internal.model.display.contacts.card.template.SegmentDistributionContactsCardTemplateDisplay;
import com.liferay.petra.string.StringPool;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Shinn Lok
 */
@Component(immediate = true, service = ContactsCardTemplateType.class)
public class SegmentDistributionContactsCardTemplateType
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
		return SegmentDistributionContactsCardTemplateDisplay.class;
	}

	@Override
	public int getType() {
		return ContactsCardTemplateConstants.TYPE_SEGMENT_DISTRIBUTION;
	}

	private static final String _DEFAULT_NAME = "Segment Distribution";

	private static final Map<String, Object> _defaultSettings =
		new HashMap<String, Object>() {
			{
				put("binSize", -1);
				put("fieldMappingFieldName", StringPool.BLANK);
				put(
					"graphType",
					ContactsCardTemplateConstants.SETTINGS_GRAPH_TYPE_BAR);
				put("max", -1);
				put("numberOfBins", 6);
			}
		};

}