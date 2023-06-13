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

package com.liferay.dynamic.data.mapping.data.provider.instance.internal;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderException;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.kernel.workflow.WorkflowDefinitionManager;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Leonardo Barros
 */
public class WorkflowDefinitionsDataProviderTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		_workflowDefinitionsDataProvider =
			new WorkflowDefinitionsDataProvider();

		Mockito.when(
			_language.get(_locale, "no-workflow")
		).thenReturn(
			"No Workflow"
		);

		ReflectionTestUtil.setFieldValue(
			_workflowDefinitionsDataProvider, "_language", _language);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetSettings() {
		_workflowDefinitionsDataProvider.getSettings();
	}

	@Test
	public void testGetWorkflowDefinitions() throws Exception {
		DDMDataProviderRequest.Builder builder =
			DDMDataProviderRequest.Builder.newBuilder();

		DDMDataProviderRequest ddmDataProviderRequest = builder.withLocale(
			_locale
		).withCompanyId(
			1L
		).build();

		_workflowDefinitionsDataProvider.workflowDefinitionManager =
			_workflowDefinitionManager;

		WorkflowDefinition workflowDefinition1 = Mockito.mock(
			WorkflowDefinition.class);

		_setUpWorkflowDefinition(
			workflowDefinition1, "definition1", "Definition 1");

		WorkflowDefinition workflowDefinition2 = Mockito.mock(
			WorkflowDefinition.class);

		_setUpWorkflowDefinition(
			workflowDefinition2, "definition2", "Definition 2");

		Mockito.when(
			_workflowDefinitionManager.getActiveWorkflowDefinitions(
				1, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)
		).thenReturn(
			Arrays.asList(workflowDefinition1, workflowDefinition2)
		);

		DDMDataProviderResponse ddmDataProviderResponse =
			_workflowDefinitionsDataProvider.getData(ddmDataProviderRequest);

		Assert.assertTrue(ddmDataProviderResponse.hasOutput("Default-Output"));

		List<KeyValuePair> keyValuePairs = ddmDataProviderResponse.getOutput(
			"Default-Output", List.class);

		Assert.assertNotNull(keyValuePairs);

		List<KeyValuePair> expectedKeyValuePairs =
			new ArrayList<KeyValuePair>() {
				{
					add(new KeyValuePair("no-workflow", "No Workflow"));
					add(new KeyValuePair("definition1", "Definition 1"));
					add(new KeyValuePair("definition2", "Definition 2"));
				}
			};

		Assert.assertEquals(expectedKeyValuePairs, keyValuePairs);
	}

	@Test
	public void testNullWorkflowDefinitionManager() throws Exception {
		DDMDataProviderRequest.Builder builder =
			DDMDataProviderRequest.Builder.newBuilder();

		DDMDataProviderRequest ddmDataProviderRequest = builder.withLocale(
			_locale
		).build();

		DDMDataProviderResponse ddmDataProviderResponse =
			_workflowDefinitionsDataProvider.getData(ddmDataProviderRequest);

		Assert.assertTrue(ddmDataProviderResponse.hasOutput("Default-Output"));

		List<KeyValuePair> keyValuePairs = ddmDataProviderResponse.getOutput(
			"Default-Output", List.class);

		Assert.assertNotNull(keyValuePairs);

		List<KeyValuePair> expectedKeyValuePairs =
			new ArrayList<KeyValuePair>() {
				{
					add(new KeyValuePair("no-workflow", "No Workflow"));
				}
			};

		Assert.assertEquals(expectedKeyValuePairs, keyValuePairs);
	}

	@Test(expected = DDMDataProviderException.class)
	public void testThrowDDMDataProviderException() throws Exception {
		DDMDataProviderRequest.Builder builder =
			DDMDataProviderRequest.Builder.newBuilder();

		DDMDataProviderRequest ddmDataProviderRequest = builder.withLocale(
			_locale
		).withCompanyId(
			1L
		).build();

		_workflowDefinitionsDataProvider.workflowDefinitionManager =
			_workflowDefinitionManager;

		Mockito.when(
			_workflowDefinitionManager.getActiveWorkflowDefinitions(
				1, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)
		).thenThrow(
			WorkflowException.class
		);

		_workflowDefinitionsDataProvider.getData(ddmDataProviderRequest);
	}

	private void _setUpWorkflowDefinition(
		WorkflowDefinition workflowDefinition, String name, String title) {

		Mockito.when(
			workflowDefinition.getName()
		).thenReturn(
			name
		);

		Mockito.when(
			workflowDefinition.getTitle("en_US")
		).thenReturn(
			title
		);
	}

	private static final Language _language = Mockito.mock(Language.class);
	private static final Locale _locale = new Locale("en", "US");
	private static WorkflowDefinitionsDataProvider
		_workflowDefinitionsDataProvider = Mockito.mock(
			WorkflowDefinitionsDataProvider.class);

	private final WorkflowDefinitionManager _workflowDefinitionManager =
		Mockito.mock(WorkflowDefinitionManager.class);

}