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

package com.liferay.osb.faro.contacts.demo.internal.data.creator;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.faro.model.FaroProject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class LiferayAssociationsDataCreator extends DataCreator {

	public LiferayAssociationsDataCreator(
		FaroProject faroProject, String dataSourceId,
		DataCreator... dataCreators) {

		super(null, faroProject, null, "associations");

		this.dataSourceId = dataSourceId;

		Collections.addAll(_dataCreators, dataCreators);
	}

	@Override
	protected void addData(List<Map<String, Object>> objects) {
		Http.Options options = new Http.Options();

		Map<String, String> headers = new HashMap<>();

		headers.put("Content-Type", ContentTypes.APPLICATION_JSON);
		headers.put("OSB-Asah-Data-Source-ID", dataSourceId);

		options.setHeaders(headers);

		options.setLocation(_OSB_ASAH_PUBLISHER_URL + "/dxp-entities");
		options.setPost(true);

		try {
			options.setBody(
				_objectMapper.writeValueAsString(objects),
				ContentTypes.APPLICATION_JSON, StandardCharsets.UTF_8.name());

			HttpUtil.URLtoString(options);
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> association = new HashMap<>();

		association.put("action", "addAssociation");

		Map<String, Object> objectMap = new HashMap<>();

		DataCreator dataCreator = _dataCreators.get(
			random.nextInt(_dataCreators.size()));

		Map<String, Object> associatedObject = dataCreator.getRandom();

		if (dataCreator instanceof LiferayOrganizationsDataCreator) {
			objectMap.put(
				"classPK",
				associatedObject.get(dataCreator.getClassPKFieldName()));
		}
		else {
			Map<String, Object> fields =
				(Map<String, Object>)associatedObject.get("fields");

			objectMap.put(
				"classPK", fields.get(dataCreator.getClassPKFieldName()));
		}

		Map<String, Object> liferayUser = (Map<String, Object>)params[0];

		objectMap.put("emailAddress", liferayUser.get("emailAddress"));
		objectMap.put("userId", liferayUser.get("userId"));

		association.put("objectJSONObject", objectMap);

		association.put("type", dataCreator.getClassName());

		return association;
	}

	protected String dataSourceId;

	private static final String _OSB_ASAH_PUBLISHER_URL = System.getenv(
		"OSB_ASAH_PUBLISHER_URL");

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayAssociationsDataCreator.class);

	private final List<DataCreator> _dataCreators = new ArrayList<>();
	private final ObjectMapper _objectMapper = new ObjectMapper();

}