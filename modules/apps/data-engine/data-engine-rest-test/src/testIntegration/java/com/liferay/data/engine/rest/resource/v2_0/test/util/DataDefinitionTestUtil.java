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

package com.liferay.data.engine.rest.resource.v2_0.test.util;

import com.liferay.data.engine.rest.client.dto.v2_0.DataDefinition;
import com.liferay.data.engine.rest.client.dto.v2_0.DataDefinitionField;
import com.liferay.data.engine.rest.client.resource.v2_0.DataDefinitionResource;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestHelper;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;

/**
 * @author Gabriel Albuquerque
 */
public class DataDefinitionTestUtil {

	public static DataDefinition addDataDefinition(
			DataDefinition dataDefinition, Long groupId)
		throws Exception {

		DataDefinitionResource.Builder builder =
			DataDefinitionResource.builder();

		DataDefinitionResource dataDefinitionResource = builder.authentication(
			"test@liferay.com", "test"
		).locale(
			LocaleUtil.getDefault()
		).build();

		return dataDefinitionResource.postSiteDataDefinitionByContentType(
			groupId, "app-builder", dataDefinition);
	}

	public static DataDefinition addDataDefinition(long groupId)
		throws Exception {

		return addDataDefinition(_randomDataDefinition(groupId), groupId);
	}

	public static DataDefinition addDataDefinitionWithDataLayout(long groupId)
		throws Exception {

		DataDefinition dataDefinition = DataDefinition.toDTO(
			read("data-definition-basic.json"));

		dataDefinition.setSiteId(groupId);

		DataDefinitionResource.Builder builder =
			DataDefinitionResource.builder();

		DataDefinitionResource dataDefinitionResource = builder.authentication(
			"test@liferay.com", "test"
		).locale(
			LocaleUtil.getDefault()
		).build();

		return dataDefinitionResource.postSiteDataDefinitionByContentType(
			groupId, "app-builder", dataDefinition);
	}

	public static DDMStructure addDDMStructure(Group group) throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(
					"com.liferay.app.builder.model.AppBuilderApp"),
				group);

		return ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(
				"com.liferay.app.builder.model.AppBuilderApp"),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			read("test-structured-content-structure.json"),
			StorageType.JSON.getValue());
	}

	public static String read(String fileName) throws Exception {
		Class<?> clazz = DataDefinitionTestUtil.class;

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/" + fileName);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			StringUtil.read(inputStream));

		return jsonObject.toString();
	}

	private static DataDefinition _randomDataDefinition(long groupId)
		throws Exception {

		DataDefinition dataDefinition = new DataDefinition() {
			{
				availableLanguageIds = new String[] {"en_US", "pt_BR"};
				dataDefinitionFields = new DataDefinitionField[] {
					new DataDefinitionField() {
						{
							description = HashMapBuilder.<String, Object>put(
								"en_US", RandomTestUtil.randomString()
							).build();
							fieldType = "text";
							label = HashMapBuilder.<String, Object>put(
								"en_US", RandomTestUtil.randomString()
							).put(
								"pt_BR", RandomTestUtil.randomString()
							).build();
							name = RandomTestUtil.randomString();
							tip = HashMapBuilder.<String, Object>put(
								"en_US", RandomTestUtil.randomString()
							).put(
								"pt_BR", RandomTestUtil.randomString()
							).build();
						}
					}
				};
				dataDefinitionKey = RandomTestUtil.randomString();
				defaultLanguageId = "en_US";
				siteId = groupId;
				userId = TestPropsValues.getUserId();
			}
		};

		dataDefinition.setDescription(
			HashMapBuilder.<String, Object>put(
				"en_US", RandomTestUtil.randomString()
			).build());
		dataDefinition.setName(
			HashMapBuilder.<String, Object>put(
				"en_US", RandomTestUtil.randomString()
			).build());

		return dataDefinition;
	}

}