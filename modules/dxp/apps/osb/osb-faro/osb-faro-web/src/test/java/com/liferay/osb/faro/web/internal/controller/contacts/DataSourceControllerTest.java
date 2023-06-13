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

package com.liferay.osb.faro.web.internal.controller.contacts;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.constants.FieldMappingConstants;
import com.liferay.osb.faro.engine.client.model.DataSourceField;
import com.liferay.osb.faro.engine.client.model.Field;
import com.liferay.osb.faro.engine.client.model.FieldMapping;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.osb.faro.web.internal.model.display.contacts.DataSourceMappingDisplay;
import com.liferay.osb.faro.web.internal.util.ContactsCSVHelper;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Inácio Nery
 */
public class DataSourceControllerTest {

	@Before
	public void setUp() throws Exception {
		Mockito.when(
			_contactsCSVHelper.getDataSourceFields(
				Mockito.eq(32783L), Mockito.isNull(), Mockito.eq(1),
				Mockito.eq(true))
		).thenReturn(
			Arrays.asList(
				new DataSourceField("City", Arrays.asList("Mountain View")),
				new DataSourceField("Country", Arrays.asList("United States")),
				new DataSourceField("Department", Arrays.asList("Legal")),
				new DataSourceField("Division", Arrays.asList("Operations")),
				new DataSourceField(
					"Email Address", Arrays.asList("jane.doe@liferay.com")),
				new DataSourceField(
					"Employee Name", Arrays.asList("Jane, Doe")),
				new DataSourceField(
					"Employment Status", Arrays.asList("Active")),
				new DataSourceField("Entity", Arrays.asList("Liferay")),
				new DataSourceField("Job", Arrays.asList("Sr Counsel")),
				new DataSourceField("Region", Arrays.asList("LATAM")),
				new DataSourceField(
					"Sub-Department", Arrays.asList("Not Applicable")),
				new DataSourceField(
					"Supervisor Email", Arrays.asList("john.doe@liferay.com")),
				new DataSourceField(
					"Supervisor Name", Arrays.asList("John, Doe")))
		);

		ReflectionTestUtils.setField(
			_dataSourceController, "_contactsCSVHelper", _contactsCSVHelper);

		Mockito.when(
			_contactsEngineClient.getFieldMappings(
				Mockito.eq(_faroProject),
				Mockito.eq(FieldMappingConstants.CONTEXT_DEMOGRAPHICS),
				Mockito.anyList(), Mockito.eq(1), Mockito.eq(10000),
				Mockito.isNull())
		).thenReturn(
			new Results<FieldMapping>(
				Arrays.asList(
					_createFieldMapping("city", "City"),
					_createFieldMapping("country", "Country"),
					_createFieldMapping("department", "Department"),
					_createFieldMapping("division", "Division"),
					_createFieldMapping(
						"employmentStatus", "Employment Status"),
					_createFieldMapping("liferayEntity", "Entity"),
					_createFieldMapping("region", "Region"),
					_createFieldMapping("subDepartment", "Sub-Department"),
					_createFieldMapping(
						"email", "emailAddress", "Employee Email"),
					_createFieldMapping("givenName", "firstName"),
					_createFieldMapping("jobTitle", "jobTitle", "Job")),
				11)
		);

		Mockito.when(
			_contactsEngineClient.getFieldMappings(
				Mockito.eq(_faroProject),
				Mockito.eq(FieldMappingConstants.CONTEXT_DEMOGRAPHICS),
				Mockito.isNull(), Mockito.isNull())
		).thenReturn(
			new Results<FieldMapping>()
		);

		Mockito.when(
			_contactsEngineClient.getFieldNamesList(
				Mockito.eq(_faroProject), Mockito.anyList(),
				Mockito.eq(FieldMappingConstants.OWNER_TYPE_INDIVIDUAL),
				Mockito.anyList())
		).thenReturn(
			Arrays.asList(
				Arrays.asList("city"), Arrays.asList("country"),
				Arrays.asList("department"),
				Arrays.asList("department", "subDepartment"),
				Arrays.asList("division"), Arrays.asList("email"),
				Arrays.asList("email"), Arrays.asList("employmentStatus"),
				Arrays.asList("familyName", "givenName"),
				Arrays.asList("familyName", "givenName"),
				Arrays.asList("jobTitle"), Arrays.asList("liferayEntity"),
				Arrays.asList("Region", "region"))
		);

		Mockito.when(
			_contactsEngineClient.getFieldsList(
				Mockito.eq(_faroProject),
				Mockito.eq(FieldMappingConstants.CONTEXT_DEMOGRAPHICS),
				Mockito.anyList(), Mockito.eq(1), Mockito.eq(10000),
				Mockito.isNull())
		).thenReturn(
			Arrays.asList(
				Arrays.asList(
					_createField("city", "City", "Mountain View"),
					_createField("country", "Country", "United States"),
					_createField("department", "Department", "Legal"),
					_createField("division", "Division", "Operations"),
					_createField(
						"email", "emailAddress", "jane.doe@liferay.com"),
					_createField(
						"employmentStatus", "Employment Status", "Active"),
					_createField("familyName", "lastName", "Doe"),
					_createField("givenName", "firstName", "John"),
					_createField("jobTitle", "jobTitle", "Sr Counsel"),
					_createField("liferayEntity", "Entity", "Liferay"),
					_createField("region", "Region", "LATAM"),
					_createField("region", "Region", "LATAM"),
					_createField(
						"subDepartment", "Sub-Department", "Not Applicable")))
		);

		ReflectionTestUtils.setField(
			_dataSourceController, "contactsEngineClient",
			_contactsEngineClient);

		Mockito.when(
			_faroProjectLocalService.getFaroProjectByGroupId(Mockito.anyLong())
		).thenReturn(
			_faroProject
		);

		ReflectionTestUtils.setField(
			_dataSourceController, "faroProjectLocalService",
			_faroProjectLocalService);
	}

	@Test
	public void testGetMappings() throws Exception {
		List<DataSourceMappingDisplay> mappings =
			_dataSourceController.getMappings(32719, null, 32783);

		Assert.assertEquals(mappings.toString(), 13, mappings.size());
	}

	private Field _createField(String name, String sourceName, String value) {
		return new Field() {
			{
				setContext("demographics");
				setDataSourceName("Liferay");
				setFieldType("Text");
				setName(name);
				setOwnerType("individual");
				setSourceName(sourceName);
				setValue(value);
			}
		};
	}

	private FieldMapping _createFieldMapping(
		String name, String... dataSourceFieldNames) {

		Map<String, String> dataSourceFieldNamesMap = new HashMap<>();

		for (int i = 0; i < dataSourceFieldNames.length; i++) {
			dataSourceFieldNamesMap.put("" + i, dataSourceFieldNames[i]);
		}

		return new FieldMapping() {
			{
				setContext("demographics");
				setDataSourceFieldNames(dataSourceFieldNamesMap);
				setDateModified(new Date());
				setDisplayName(name);
				setFieldName(name);
				setFieldType("Text");
				setOwnerType("individual");
			}
		};
	}

	private final ContactsCSVHelper _contactsCSVHelper = Mockito.mock(
		ContactsCSVHelper.class);
	private final ContactsEngineClient _contactsEngineClient = Mockito.mock(
		ContactsEngineClient.class);
	private final DataSourceController _dataSourceController =
		new DataSourceController();
	private final FaroProject _faroProject = Mockito.mock(FaroProject.class);
	private final FaroProjectLocalService _faroProjectLocalService =
		Mockito.mock(FaroProjectLocalService.class);

}