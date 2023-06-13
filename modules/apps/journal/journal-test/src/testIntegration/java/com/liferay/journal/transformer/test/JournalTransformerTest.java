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

package com.liferay.journal.transformer.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.data.engine.rest.dto.v2_0.DataDefinition;
import com.liferay.data.engine.rest.resource.v2_0.DataDefinitionResource;
import com.liferay.data.engine.rest.test.util.DataDefinitionTestUtil;
import com.liferay.dynamic.data.mapping.util.DDMFormValuesToFieldsConverter;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.journal.util.JournalConverter;
import com.liferay.journal.util.JournalHelper;
import com.liferay.journal.util.JournalTransformerListenerRegistry;
import com.liferay.layout.display.page.LayoutDisplayPageProviderRegistry;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.templateparser.TransformerListener;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marcellus Tavares
 */
@RunWith(Arquillian.class)
public class JournalTransformerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_transformMethod = JournalTestUtil.getJournalUtilTransformMethod();

		_group = GroupTestUtil.addGroup();

		DataDefinition dataDefinition =
			DataDefinitionTestUtil.addDataDefinition(
				"journal", _dataDefinitionResourceFactory, _group.getGroupId(),
				_read("data_definition.json"), TestPropsValues.getUser());

		_journalArticle = JournalTestUtil.addArticleWithXMLContent(
			_group.getGroupId(), _read("journal_content.xml"),
			dataDefinition.getDataDefinitionKey(), null);
	}

	@Test
	public void testFTLTransformation() throws Exception {
		Assert.assertEquals(
			"Joe Bloggs - print",
			_transformMethod.invoke(
				null, _journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.US),
				_layoutDisplayPageProviderRegistry, null, false,
				"${name.getData()} - ${viewMode}", null, Constants.PRINT));
	}

	@Test
	public void testLocaleTransformerListener() throws Exception {
		Assert.assertEquals(
			"Joe Bloggs",
			_transformMethod.invoke(
				null, _journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.US),
				_layoutDisplayPageProviderRegistry, null, false,
				"${name.getData()}", null, Constants.VIEW));

		Assert.assertEquals(
			"Joao da Silva",
			_transformMethod.invoke(
				null, _journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.BRAZIL),
				_layoutDisplayPageProviderRegistry, null, false,
				"${name.getData()}", null, Constants.VIEW));

		Assert.assertEquals(
			"Joe Bloggs",
			_transformMethod.invoke(
				null, _journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.FRENCH),
				_layoutDisplayPageProviderRegistry, null, false,
				"${name.getData()}", null, Constants.VIEW));
	}

	@Test
	public void testLocaleTransformerListenerNestedFieldWithNoTranslation()
		throws Exception {

		Assert.assertEquals(
			"2022-11-26",
			_transformMethod.invoke(
				null, _journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.US),
				_layoutDisplayPageProviderRegistry, null, false,
				"${birthday.getData()}", null, Constants.VIEW));

		Assert.assertEquals(
			"2022-11-26",
			_transformMethod.invoke(
				null, _journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.BRAZIL),
				_layoutDisplayPageProviderRegistry, null, false,
				"${birthday.getData()}", null, Constants.VIEW));
	}

	@Test
	public void testRegexTransformerListener() throws Exception {
		initRegexTransformerListener();

		Assert.assertEquals(
			"Hello Joe Bloggs, Welcome to production.sample.com.",
			_transformMethod.invoke(
				null, _journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.US),
				_layoutDisplayPageProviderRegistry, null, false,
				"Hello ${name.getData()}, Welcome to beta.sample.com.", null,
				Constants.VIEW));
	}

	@Test
	public void testTokensTransformerListener() throws Exception {
		Assert.assertEquals(
			String.valueOf(TestPropsValues.getCompanyId()),
			_transformMethod.invoke(
				null, _journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.US),
				_layoutDisplayPageProviderRegistry, null, false, "@company_id@",
				null, Constants.VIEW));

		Assert.assertEquals(
			String.valueOf(TestPropsValues.getCompanyId()),
			_transformMethod.invoke(
				null, _journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.US),
				_layoutDisplayPageProviderRegistry, null, false,
				"@@company_id@@", null, Constants.VIEW));
	}

	@Test
	public void testTransformSelectDDMFormFieldType() throws Exception {
		DataDefinition dataDefinition =
			DataDefinitionTestUtil.addDataDefinition(
				"journal", _dataDefinitionResourceFactory, _group.getGroupId(),
				_read(
					"data_definition_with_select_field_single_selection.json"),
				TestPropsValues.getUser());

		JournalArticle journalArticle =
			JournalTestUtil.addArticleWithXMLContent(
				_group.getGroupId(),
				_read("journal_content_with_select_field_single_selection.xml"),
				dataDefinition.getDataDefinitionKey(), null);

		Assert.assertEquals(
			"Option71814087",
			_transformMethod.invoke(
				null, journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.US),
				_layoutDisplayPageProviderRegistry, null, false,
				"${Radio80408512.getData()}", null, Constants.VIEW));

		dataDefinition = DataDefinitionTestUtil.addDataDefinition(
			"journal", _dataDefinitionResourceFactory, _group.getGroupId(),
			_read("data_definition_with_select_field_multiple_selection.json"),
			TestPropsValues.getUser());

		journalArticle = JournalTestUtil.addArticleWithXMLContent(
			_group.getGroupId(),
			_read("journal_content_with_select_field_multiple_selection.xml"),
			dataDefinition.getDataDefinitionKey(), null);

		Assert.assertEquals(
			JSONUtil.putAll(
				"Option81316201", "Option25867365"
			).toString(),
			_transformMethod.invoke(
				null, journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.US),
				_layoutDisplayPageProviderRegistry, null, false,
				"${CheckboxMultiple94681127.getData()}", null, Constants.VIEW));
	}

	@Test
	public void testViewCounterTransformerListener() throws Exception {
		Assert.assertEquals(
			StringBundler.concat(
				"<script type=\"text/javascript\">",
				"Liferay.Service('/assetentry/increment-view-counter',",
				"{userId:0, className:'",
				"com.liferay.journal.model.JournalArticle', classPK:",
				_journalArticle.getResourcePrimKey(), "});</script>"),
			_transformMethod.invoke(
				null, _journalArticle, null, _journalHelper,
				LocaleUtil.toLanguageId(LocaleUtil.US),
				_layoutDisplayPageProviderRegistry, null, false,
				"@view_counter@", null, Constants.VIEW));
	}

	protected void initRegexTransformerListener() {
		TransformerListener transformerListener =
			_journalTransformerListenerRegistry.getTransformerListener(
				"com.liferay.journal.internal.transformer." +
					"RegexTransformerListener");

		CacheRegistryUtil.setActive(true);

		List<Pattern> patterns = new ArrayList<>();
		List<String> replacements = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			String regex = TestPropsUtil.get(
				"journal.transformer.regex.pattern." + i);
			String replacement = TestPropsUtil.get(
				"journal.transformer.regex.replacement." + i);

			if (Validator.isNull(regex) || Validator.isNull(replacement)) {
				break;
			}

			patterns.add(Pattern.compile(regex));
			replacements.add(replacement);
		}

		ReflectionTestUtil.setFieldValue(
			transformerListener, "_patterns", patterns);
		ReflectionTestUtil.setFieldValue(
			transformerListener, "_replacements", replacements);
	}

	private String _read(String fileName) throws Exception {
		return new String(
			FileUtil.getBytes(getClass(), "dependencies/" + fileName));
	}

	@Inject
	private DataDefinitionResource.Factory _dataDefinitionResourceFactory;

	@Inject
	private DDMFormValuesToFieldsConverter _ddmFormValuesToFieldsConverter;

	@DeleteAfterTestRun
	private Group _group;

	private JournalArticle _journalArticle;

	@Inject
	private JournalConverter _journalConverter;

	@Inject
	private JournalHelper _journalHelper;

	@Inject
	private JournalTransformerListenerRegistry
		_journalTransformerListenerRegistry;

	@Inject
	private Language _language;

	@Inject
	private LayoutDisplayPageProviderRegistry
		_layoutDisplayPageProviderRegistry;

	@Inject
	private Portal _portal;

	private Method _transformMethod;

}