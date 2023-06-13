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

package com.liferay.osb.faro.web.internal.model.display.contacts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import com.liferay.osb.faro.engine.client.constants.ActivityConstants;
import com.liferay.osb.faro.engine.client.model.Activity;
import com.liferay.osb.faro.engine.client.model.ActivityGroup;
import com.liferay.osb.faro.web.internal.util.JSONUtil;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shinn Lok
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class ActivityGroupDisplay {

	public ActivityGroupDisplay() {
	}

	public ActivityGroupDisplay(ActivityGroup activityGroup) {
		Map<String, Object> embeddedResources =
			activityGroup.getEmbeddedResources();

		if (MapUtil.isNotEmpty(embeddedResources)) {
			List<Activity> activities = JSONUtil.convertValue(
				embeddedResources.get("activities"),
				new TypeReference<List<Activity>>() {
				});

			if (activities != null) {
				_activityDisplays = TransformUtil.transform(
					activities, ActivityDisplay::new);
			}

			Map<String, Integer> activitiesCount =
				(Map<String, Integer>)embeddedResources.get("activities-count");

			if (activitiesCount != null) {
				for (Map.Entry<String, Integer> entry :
						activitiesCount.entrySet()) {

					_activitiesCount.merge(
						ActivityConstants.getAction(entry.getKey()),
						entry.getValue(), Integer::sum);
				}
			}
		}

		_day = activityGroup.getDay();
		_endTime = activityGroup.getEndTime();
		_id = activityGroup.getId();
		_name = activityGroup.getName();
		_startTime = activityGroup.getStartTime();
	}

	@JsonIgnore
	public List<ActivityDisplay> getActivityDisplays() {
		return _activityDisplays;
	}

	private final Map<Integer, Integer> _activitiesCount = new HashMap<>();

	@JsonProperty("activities")
	private List<ActivityDisplay> _activityDisplays;

	private Date _day;
	private Date _endTime;
	private String _id;
	private String _name;
	private Date _startTime;

}