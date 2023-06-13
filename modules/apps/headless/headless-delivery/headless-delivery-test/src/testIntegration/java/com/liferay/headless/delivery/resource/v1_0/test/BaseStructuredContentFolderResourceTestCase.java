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

package com.liferay.headless.delivery.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.delivery.client.dto.v1_0.StructuredContentFolder;
import com.liferay.headless.delivery.client.http.HttpInvoker;
import com.liferay.headless.delivery.client.pagination.Page;
import com.liferay.headless.delivery.client.pagination.Pagination;
import com.liferay.headless.delivery.client.resource.v1_0.StructuredContentFolderResource;
import com.liferay.headless.delivery.client.serdes.v1_0.StructuredContentFolderSerDes;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.InvocationTargetException;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.time.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseStructuredContentFolderResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();
		testLocale = LocaleUtil.getDefault();

		testCompany = CompanyLocalServiceUtil.getCompany(
			testGroup.getCompanyId());

		_structuredContentFolderResource.setContextCompany(testCompany);
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testClientSerDesToDTO() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				enable(SerializationFeature.INDENT_OUTPUT);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		StructuredContentFolder structuredContentFolder1 =
			randomStructuredContentFolder();

		String json = objectMapper.writeValueAsString(structuredContentFolder1);

		StructuredContentFolder structuredContentFolder2 =
			StructuredContentFolderSerDes.toDTO(json);

		Assert.assertTrue(
			equals(structuredContentFolder1, structuredContentFolder2));
	}

	@Test
	public void testClientSerDesToJSON() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		StructuredContentFolder structuredContentFolder =
			randomStructuredContentFolder();

		String json1 = objectMapper.writeValueAsString(structuredContentFolder);
		String json2 = StructuredContentFolderSerDes.toJSON(
			structuredContentFolder);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		StructuredContentFolder structuredContentFolder =
			randomStructuredContentFolder();

		structuredContentFolder.setDescription(regex);
		structuredContentFolder.setName(regex);

		String json = StructuredContentFolderSerDes.toJSON(
			structuredContentFolder);

		Assert.assertFalse(json.contains(regex));

		structuredContentFolder = StructuredContentFolderSerDes.toDTO(json);

		Assert.assertEquals(regex, structuredContentFolder.getDescription());
		Assert.assertEquals(regex, structuredContentFolder.getName());
	}

	@Test
	public void testGetSiteStructuredContentFoldersPage() throws Exception {
		Long siteId = testGetSiteStructuredContentFoldersPage_getSiteId();
		Long irrelevantSiteId =
			testGetSiteStructuredContentFoldersPage_getIrrelevantSiteId();

		if ((irrelevantSiteId != null)) {
			StructuredContentFolder irrelevantStructuredContentFolder =
				testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
					irrelevantSiteId,
					randomIrrelevantStructuredContentFolder());

			Page<StructuredContentFolder> page =
				StructuredContentFolderResource.
					getSiteStructuredContentFoldersPage(
						irrelevantSiteId, null, null, null, Pagination.of(1, 2),
						null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantStructuredContentFolder),
				(List<StructuredContentFolder>)page.getItems());
			assertValid(page);
		}

		StructuredContentFolder structuredContentFolder1 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, randomStructuredContentFolder());

		StructuredContentFolder structuredContentFolder2 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, randomStructuredContentFolder());

		Page<StructuredContentFolder> page =
			StructuredContentFolderResource.getSiteStructuredContentFoldersPage(
				siteId, null, null, null, Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(structuredContentFolder1, structuredContentFolder2),
			(List<StructuredContentFolder>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetSiteStructuredContentFoldersPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteStructuredContentFoldersPage_getSiteId();

		StructuredContentFolder structuredContentFolder1 =
			randomStructuredContentFolder();

		structuredContentFolder1 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, structuredContentFolder1);

		for (EntityField entityField : entityFields) {
			Page<StructuredContentFolder> page =
				StructuredContentFolderResource.
					getSiteStructuredContentFoldersPage(
						siteId, null, null,
						getFilterString(
							entityField, "between", structuredContentFolder1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(structuredContentFolder1),
				(List<StructuredContentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetSiteStructuredContentFoldersPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteStructuredContentFoldersPage_getSiteId();

		StructuredContentFolder structuredContentFolder1 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, randomStructuredContentFolder());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		StructuredContentFolder structuredContentFolder2 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, randomStructuredContentFolder());

		for (EntityField entityField : entityFields) {
			Page<StructuredContentFolder> page =
				StructuredContentFolderResource.
					getSiteStructuredContentFoldersPage(
						siteId, null, null,
						getFilterString(
							entityField, "eq", structuredContentFolder1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(structuredContentFolder1),
				(List<StructuredContentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetSiteStructuredContentFoldersPageWithPagination()
		throws Exception {

		Long siteId = testGetSiteStructuredContentFoldersPage_getSiteId();

		StructuredContentFolder structuredContentFolder1 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, randomStructuredContentFolder());

		StructuredContentFolder structuredContentFolder2 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, randomStructuredContentFolder());

		StructuredContentFolder structuredContentFolder3 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, randomStructuredContentFolder());

		Page<StructuredContentFolder> page1 =
			StructuredContentFolderResource.getSiteStructuredContentFoldersPage(
				siteId, null, null, null, Pagination.of(1, 2), null);

		List<StructuredContentFolder> structuredContentFolders1 =
			(List<StructuredContentFolder>)page1.getItems();

		Assert.assertEquals(
			structuredContentFolders1.toString(), 2,
			structuredContentFolders1.size());

		Page<StructuredContentFolder> page2 =
			StructuredContentFolderResource.getSiteStructuredContentFoldersPage(
				siteId, null, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<StructuredContentFolder> structuredContentFolders2 =
			(List<StructuredContentFolder>)page2.getItems();

		Assert.assertEquals(
			structuredContentFolders2.toString(), 1,
			structuredContentFolders2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(
				structuredContentFolder1, structuredContentFolder2,
				structuredContentFolder3),
			new ArrayList<StructuredContentFolder>() {
				{
					addAll(structuredContentFolders1);
					addAll(structuredContentFolders2);
				}
			});
	}

	@Test
	public void testGetSiteStructuredContentFoldersPageWithSortDateTime()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteStructuredContentFoldersPage_getSiteId();

		StructuredContentFolder structuredContentFolder1 =
			randomStructuredContentFolder();
		StructuredContentFolder structuredContentFolder2 =
			randomStructuredContentFolder();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				structuredContentFolder1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		structuredContentFolder1 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, structuredContentFolder1);

		structuredContentFolder2 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, structuredContentFolder2);

		for (EntityField entityField : entityFields) {
			Page<StructuredContentFolder> ascPage =
				StructuredContentFolderResource.
					getSiteStructuredContentFoldersPage(
						siteId, null, null, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(
					structuredContentFolder1, structuredContentFolder2),
				(List<StructuredContentFolder>)ascPage.getItems());

			Page<StructuredContentFolder> descPage =
				StructuredContentFolderResource.
					getSiteStructuredContentFoldersPage(
						siteId, null, null, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(
					structuredContentFolder2, structuredContentFolder1),
				(List<StructuredContentFolder>)descPage.getItems());
		}
	}

	@Test
	public void testGetSiteStructuredContentFoldersPageWithSortString()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteStructuredContentFoldersPage_getSiteId();

		StructuredContentFolder structuredContentFolder1 =
			randomStructuredContentFolder();
		StructuredContentFolder structuredContentFolder2 =
			randomStructuredContentFolder();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				structuredContentFolder1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(
				structuredContentFolder2, entityField.getName(), "Bbb");
		}

		structuredContentFolder1 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, structuredContentFolder1);

		structuredContentFolder2 =
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				siteId, structuredContentFolder2);

		for (EntityField entityField : entityFields) {
			Page<StructuredContentFolder> ascPage =
				StructuredContentFolderResource.
					getSiteStructuredContentFoldersPage(
						siteId, null, null, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(
					structuredContentFolder1, structuredContentFolder2),
				(List<StructuredContentFolder>)ascPage.getItems());

			Page<StructuredContentFolder> descPage =
				StructuredContentFolderResource.
					getSiteStructuredContentFoldersPage(
						siteId, null, null, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(
					structuredContentFolder2, structuredContentFolder1),
				(List<StructuredContentFolder>)descPage.getItems());
		}
	}

	protected StructuredContentFolder
			testGetSiteStructuredContentFoldersPage_addStructuredContentFolder(
				Long siteId, StructuredContentFolder structuredContentFolder)
		throws Exception {

		return StructuredContentFolderResource.postSiteStructuredContentFolder(
			siteId, structuredContentFolder);
	}

	protected Long testGetSiteStructuredContentFoldersPage_getSiteId()
		throws Exception {

		return testGroup.getGroupId();
	}

	protected Long testGetSiteStructuredContentFoldersPage_getIrrelevantSiteId()
		throws Exception {

		return irrelevantGroup.getGroupId();
	}

	@Test
	public void testPostSiteStructuredContentFolder() throws Exception {
		StructuredContentFolder randomStructuredContentFolder =
			randomStructuredContentFolder();

		StructuredContentFolder postStructuredContentFolder =
			testPostSiteStructuredContentFolder_addStructuredContentFolder(
				randomStructuredContentFolder);

		assertEquals(
			randomStructuredContentFolder, postStructuredContentFolder);
		assertValid(postStructuredContentFolder);
	}

	protected StructuredContentFolder
			testPostSiteStructuredContentFolder_addStructuredContentFolder(
				StructuredContentFolder structuredContentFolder)
		throws Exception {

		return StructuredContentFolderResource.postSiteStructuredContentFolder(
			testGetSiteStructuredContentFoldersPage_getSiteId(),
			structuredContentFolder);
	}

	@Test
	public void testGetStructuredContentFolderStructuredContentFoldersPage()
		throws Exception {

		Long parentStructuredContentFolderId =
			testGetStructuredContentFolderStructuredContentFoldersPage_getParentStructuredContentFolderId();
		Long irrelevantParentStructuredContentFolderId =
			testGetStructuredContentFolderStructuredContentFoldersPage_getIrrelevantParentStructuredContentFolderId();

		if ((irrelevantParentStructuredContentFolderId != null)) {
			StructuredContentFolder irrelevantStructuredContentFolder =
				testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
					irrelevantParentStructuredContentFolderId,
					randomIrrelevantStructuredContentFolder());

			Page<StructuredContentFolder> page =
				StructuredContentFolderResource.
					getStructuredContentFolderStructuredContentFoldersPage(
						irrelevantParentStructuredContentFolderId, null, null,
						Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantStructuredContentFolder),
				(List<StructuredContentFolder>)page.getItems());
			assertValid(page);
		}

		StructuredContentFolder structuredContentFolder1 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId,
				randomStructuredContentFolder());

		StructuredContentFolder structuredContentFolder2 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId,
				randomStructuredContentFolder());

		Page<StructuredContentFolder> page =
			StructuredContentFolderResource.
				getStructuredContentFolderStructuredContentFoldersPage(
					parentStructuredContentFolderId, null, null,
					Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(structuredContentFolder1, structuredContentFolder2),
			(List<StructuredContentFolder>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetStructuredContentFolderStructuredContentFoldersPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentStructuredContentFolderId =
			testGetStructuredContentFolderStructuredContentFoldersPage_getParentStructuredContentFolderId();

		StructuredContentFolder structuredContentFolder1 =
			randomStructuredContentFolder();

		structuredContentFolder1 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId, structuredContentFolder1);

		for (EntityField entityField : entityFields) {
			Page<StructuredContentFolder> page =
				StructuredContentFolderResource.
					getStructuredContentFolderStructuredContentFoldersPage(
						parentStructuredContentFolderId, null,
						getFilterString(
							entityField, "between", structuredContentFolder1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(structuredContentFolder1),
				(List<StructuredContentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetStructuredContentFolderStructuredContentFoldersPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentStructuredContentFolderId =
			testGetStructuredContentFolderStructuredContentFoldersPage_getParentStructuredContentFolderId();

		StructuredContentFolder structuredContentFolder1 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId,
				randomStructuredContentFolder());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		StructuredContentFolder structuredContentFolder2 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId,
				randomStructuredContentFolder());

		for (EntityField entityField : entityFields) {
			Page<StructuredContentFolder> page =
				StructuredContentFolderResource.
					getStructuredContentFolderStructuredContentFoldersPage(
						parentStructuredContentFolderId, null,
						getFilterString(
							entityField, "eq", structuredContentFolder1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(structuredContentFolder1),
				(List<StructuredContentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetStructuredContentFolderStructuredContentFoldersPageWithPagination()
		throws Exception {

		Long parentStructuredContentFolderId =
			testGetStructuredContentFolderStructuredContentFoldersPage_getParentStructuredContentFolderId();

		StructuredContentFolder structuredContentFolder1 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId,
				randomStructuredContentFolder());

		StructuredContentFolder structuredContentFolder2 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId,
				randomStructuredContentFolder());

		StructuredContentFolder structuredContentFolder3 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId,
				randomStructuredContentFolder());

		Page<StructuredContentFolder> page1 =
			StructuredContentFolderResource.
				getStructuredContentFolderStructuredContentFoldersPage(
					parentStructuredContentFolderId, null, null,
					Pagination.of(1, 2), null);

		List<StructuredContentFolder> structuredContentFolders1 =
			(List<StructuredContentFolder>)page1.getItems();

		Assert.assertEquals(
			structuredContentFolders1.toString(), 2,
			structuredContentFolders1.size());

		Page<StructuredContentFolder> page2 =
			StructuredContentFolderResource.
				getStructuredContentFolderStructuredContentFoldersPage(
					parentStructuredContentFolderId, null, null,
					Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<StructuredContentFolder> structuredContentFolders2 =
			(List<StructuredContentFolder>)page2.getItems();

		Assert.assertEquals(
			structuredContentFolders2.toString(), 1,
			structuredContentFolders2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(
				structuredContentFolder1, structuredContentFolder2,
				structuredContentFolder3),
			new ArrayList<StructuredContentFolder>() {
				{
					addAll(structuredContentFolders1);
					addAll(structuredContentFolders2);
				}
			});
	}

	@Test
	public void testGetStructuredContentFolderStructuredContentFoldersPageWithSortDateTime()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentStructuredContentFolderId =
			testGetStructuredContentFolderStructuredContentFoldersPage_getParentStructuredContentFolderId();

		StructuredContentFolder structuredContentFolder1 =
			randomStructuredContentFolder();
		StructuredContentFolder structuredContentFolder2 =
			randomStructuredContentFolder();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				structuredContentFolder1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		structuredContentFolder1 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId, structuredContentFolder1);

		structuredContentFolder2 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId, structuredContentFolder2);

		for (EntityField entityField : entityFields) {
			Page<StructuredContentFolder> ascPage =
				StructuredContentFolderResource.
					getStructuredContentFolderStructuredContentFoldersPage(
						parentStructuredContentFolderId, null, null,
						Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(
					structuredContentFolder1, structuredContentFolder2),
				(List<StructuredContentFolder>)ascPage.getItems());

			Page<StructuredContentFolder> descPage =
				StructuredContentFolderResource.
					getStructuredContentFolderStructuredContentFoldersPage(
						parentStructuredContentFolderId, null, null,
						Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(
					structuredContentFolder2, structuredContentFolder1),
				(List<StructuredContentFolder>)descPage.getItems());
		}
	}

	@Test
	public void testGetStructuredContentFolderStructuredContentFoldersPageWithSortString()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentStructuredContentFolderId =
			testGetStructuredContentFolderStructuredContentFoldersPage_getParentStructuredContentFolderId();

		StructuredContentFolder structuredContentFolder1 =
			randomStructuredContentFolder();
		StructuredContentFolder structuredContentFolder2 =
			randomStructuredContentFolder();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				structuredContentFolder1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(
				structuredContentFolder2, entityField.getName(), "Bbb");
		}

		structuredContentFolder1 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId, structuredContentFolder1);

		structuredContentFolder2 =
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				parentStructuredContentFolderId, structuredContentFolder2);

		for (EntityField entityField : entityFields) {
			Page<StructuredContentFolder> ascPage =
				StructuredContentFolderResource.
					getStructuredContentFolderStructuredContentFoldersPage(
						parentStructuredContentFolderId, null, null,
						Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(
					structuredContentFolder1, structuredContentFolder2),
				(List<StructuredContentFolder>)ascPage.getItems());

			Page<StructuredContentFolder> descPage =
				StructuredContentFolderResource.
					getStructuredContentFolderStructuredContentFoldersPage(
						parentStructuredContentFolderId, null, null,
						Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(
					structuredContentFolder2, structuredContentFolder1),
				(List<StructuredContentFolder>)descPage.getItems());
		}
	}

	protected StructuredContentFolder
			testGetStructuredContentFolderStructuredContentFoldersPage_addStructuredContentFolder(
				Long parentStructuredContentFolderId,
				StructuredContentFolder structuredContentFolder)
		throws Exception {

		return StructuredContentFolderResource.
			postStructuredContentFolderStructuredContentFolder(
				parentStructuredContentFolderId, structuredContentFolder);
	}

	protected Long
			testGetStructuredContentFolderStructuredContentFoldersPage_getParentStructuredContentFolderId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetStructuredContentFolderStructuredContentFoldersPage_getIrrelevantParentStructuredContentFolderId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostStructuredContentFolderStructuredContentFolder()
		throws Exception {

		StructuredContentFolder randomStructuredContentFolder =
			randomStructuredContentFolder();

		StructuredContentFolder postStructuredContentFolder =
			testPostStructuredContentFolderStructuredContentFolder_addStructuredContentFolder(
				randomStructuredContentFolder);

		assertEquals(
			randomStructuredContentFolder, postStructuredContentFolder);
		assertValid(postStructuredContentFolder);
	}

	protected StructuredContentFolder
			testPostStructuredContentFolderStructuredContentFolder_addStructuredContentFolder(
				StructuredContentFolder structuredContentFolder)
		throws Exception {

		return StructuredContentFolderResource.
			postStructuredContentFolderStructuredContentFolder(
				testGetStructuredContentFolderStructuredContentFoldersPage_getParentStructuredContentFolderId(),
				structuredContentFolder);
	}

	@Test
	public void testDeleteStructuredContentFolder() throws Exception {
		StructuredContentFolder structuredContentFolder =
			testDeleteStructuredContentFolder_addStructuredContentFolder();

		assertHttpResponseStatusCode(
			204,
			StructuredContentFolderResource.
				deleteStructuredContentFolderHttpResponse(
					structuredContentFolder.getId()));

		assertHttpResponseStatusCode(
			404,
			StructuredContentFolderResource.
				getStructuredContentFolderHttpResponse(
					structuredContentFolder.getId()));

		assertHttpResponseStatusCode(
			404,
			StructuredContentFolderResource.
				getStructuredContentFolderHttpResponse(0L));
	}

	protected StructuredContentFolder
			testDeleteStructuredContentFolder_addStructuredContentFolder()
		throws Exception {

		return StructuredContentFolderResource.postSiteStructuredContentFolder(
			testGroup.getGroupId(), randomStructuredContentFolder());
	}

	@Test
	public void testGetStructuredContentFolder() throws Exception {
		StructuredContentFolder postStructuredContentFolder =
			testGetStructuredContentFolder_addStructuredContentFolder();

		StructuredContentFolder getStructuredContentFolder =
			StructuredContentFolderResource.getStructuredContentFolder(
				postStructuredContentFolder.getId());

		assertEquals(postStructuredContentFolder, getStructuredContentFolder);
		assertValid(getStructuredContentFolder);
	}

	protected StructuredContentFolder
			testGetStructuredContentFolder_addStructuredContentFolder()
		throws Exception {

		return StructuredContentFolderResource.postSiteStructuredContentFolder(
			testGroup.getGroupId(), randomStructuredContentFolder());
	}

	@Test
	public void testPatchStructuredContentFolder() throws Exception {
		StructuredContentFolder postStructuredContentFolder =
			testPatchStructuredContentFolder_addStructuredContentFolder();

		StructuredContentFolder randomPatchStructuredContentFolder =
			randomPatchStructuredContentFolder();

		StructuredContentFolder patchStructuredContentFolder =
			StructuredContentFolderResource.patchStructuredContentFolder(
				postStructuredContentFolder.getId(),
				randomPatchStructuredContentFolder);

		StructuredContentFolder expectedPatchStructuredContentFolder =
			(StructuredContentFolder)BeanUtils.cloneBean(
				postStructuredContentFolder);

		_beanUtilsBean.copyProperties(
			expectedPatchStructuredContentFolder,
			randomPatchStructuredContentFolder);

		StructuredContentFolder getStructuredContentFolder =
			StructuredContentFolderResource.getStructuredContentFolder(
				patchStructuredContentFolder.getId());

		assertEquals(
			expectedPatchStructuredContentFolder, getStructuredContentFolder);
		assertValid(getStructuredContentFolder);
	}

	protected StructuredContentFolder
			testPatchStructuredContentFolder_addStructuredContentFolder()
		throws Exception {

		return StructuredContentFolderResource.postSiteStructuredContentFolder(
			testGroup.getGroupId(), randomStructuredContentFolder());
	}

	@Test
	public void testPutStructuredContentFolder() throws Exception {
		StructuredContentFolder postStructuredContentFolder =
			testPutStructuredContentFolder_addStructuredContentFolder();

		StructuredContentFolder randomStructuredContentFolder =
			randomStructuredContentFolder();

		StructuredContentFolder putStructuredContentFolder =
			StructuredContentFolderResource.putStructuredContentFolder(
				postStructuredContentFolder.getId(),
				randomStructuredContentFolder);

		assertEquals(randomStructuredContentFolder, putStructuredContentFolder);
		assertValid(putStructuredContentFolder);

		StructuredContentFolder getStructuredContentFolder =
			StructuredContentFolderResource.getStructuredContentFolder(
				putStructuredContentFolder.getId());

		assertEquals(randomStructuredContentFolder, getStructuredContentFolder);
		assertValid(getStructuredContentFolder);
	}

	protected StructuredContentFolder
			testPutStructuredContentFolder_addStructuredContentFolder()
		throws Exception {

		return StructuredContentFolderResource.postSiteStructuredContentFolder(
			testGroup.getGroupId(), randomStructuredContentFolder());
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		StructuredContentFolder structuredContentFolder1,
		StructuredContentFolder structuredContentFolder2) {

		Assert.assertTrue(
			structuredContentFolder1 + " does not equal " +
				structuredContentFolder2,
			equals(structuredContentFolder1, structuredContentFolder2));
	}

	protected void assertEquals(
		List<StructuredContentFolder> structuredContentFolders1,
		List<StructuredContentFolder> structuredContentFolders2) {

		Assert.assertEquals(
			structuredContentFolders1.size(), structuredContentFolders2.size());

		for (int i = 0; i < structuredContentFolders1.size(); i++) {
			StructuredContentFolder structuredContentFolder1 =
				structuredContentFolders1.get(i);
			StructuredContentFolder structuredContentFolder2 =
				structuredContentFolders2.get(i);

			assertEquals(structuredContentFolder1, structuredContentFolder2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<StructuredContentFolder> structuredContentFolders1,
		List<StructuredContentFolder> structuredContentFolders2) {

		Assert.assertEquals(
			structuredContentFolders1.size(), structuredContentFolders2.size());

		for (StructuredContentFolder structuredContentFolder1 :
				structuredContentFolders1) {

			boolean contains = false;

			for (StructuredContentFolder structuredContentFolder2 :
					structuredContentFolders2) {

				if (equals(
						structuredContentFolder1, structuredContentFolder2)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				structuredContentFolders2 + " does not contain " +
					structuredContentFolder1,
				contains);
		}
	}

	protected void assertValid(
		StructuredContentFolder structuredContentFolder) {

		boolean valid = true;

		if (structuredContentFolder.getDateCreated() == null) {
			valid = false;
		}

		if (structuredContentFolder.getDateModified() == null) {
			valid = false;
		}

		if (structuredContentFolder.getId() == null) {
			valid = false;
		}

		if (!Objects.equals(
				structuredContentFolder.getSiteId(), testGroup.getGroupId())) {

			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (structuredContentFolder.getCreator() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (structuredContentFolder.getCustomFields() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (structuredContentFolder.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (structuredContentFolder.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfStructuredContentFolders",
					additionalAssertFieldName)) {

				if (structuredContentFolder.
						getNumberOfStructuredContentFolders() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfStructuredContents", additionalAssertFieldName)) {

				if (structuredContentFolder.getNumberOfStructuredContents() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("viewableBy", additionalAssertFieldName)) {
				if (structuredContentFolder.getViewableBy() == null) {
					valid = false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Page<StructuredContentFolder> page) {
		boolean valid = false;

		Collection<StructuredContentFolder> structuredContentFolders =
			page.getItems();

		int size = structuredContentFolders.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected boolean equals(
		StructuredContentFolder structuredContentFolder1,
		StructuredContentFolder structuredContentFolder2) {

		if (structuredContentFolder1 == structuredContentFolder2) {
			return true;
		}

		if (!Objects.equals(
				structuredContentFolder1.getSiteId(),
				structuredContentFolder2.getSiteId())) {

			return false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						structuredContentFolder1.getCreator(),
						structuredContentFolder2.getCreator())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						structuredContentFolder1.getCustomFields(),
						structuredContentFolder2.getCustomFields())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						structuredContentFolder1.getDateCreated(),
						structuredContentFolder2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						structuredContentFolder1.getDateModified(),
						structuredContentFolder2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						structuredContentFolder1.getDescription(),
						structuredContentFolder2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						structuredContentFolder1.getId(),
						structuredContentFolder2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						structuredContentFolder1.getName(),
						structuredContentFolder2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfStructuredContentFolders",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						structuredContentFolder1.
							getNumberOfStructuredContentFolders(),
						structuredContentFolder2.
							getNumberOfStructuredContentFolders())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfStructuredContents", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						structuredContentFolder1.
							getNumberOfStructuredContents(),
						structuredContentFolder2.
							getNumberOfStructuredContents())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("viewableBy", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						structuredContentFolder1.getViewableBy(),
						structuredContentFolder2.getViewableBy())) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected Collection<EntityField> getEntityFields() throws Exception {
		if (!(_structuredContentFolderResource instanceof
				EntityModelResource)) {

			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_structuredContentFolderResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		Collection<EntityField> entityFields = getEntityFields();

		Stream<EntityField> stream = entityFields.stream();

		return stream.filter(
			entityField -> Objects.equals(entityField.getType(), type)
		).collect(
			Collectors.toList()
		);
	}

	protected String getFilterString(
		EntityField entityField, String operator,
		StructuredContentFolder structuredContentFolder) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("creator")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("customFields")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("dateCreated")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							structuredContentFolder.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							structuredContentFolder.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(
					_dateFormat.format(
						structuredContentFolder.getDateCreated()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("dateModified")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							structuredContentFolder.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							structuredContentFolder.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(
					_dateFormat.format(
						structuredContentFolder.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(structuredContentFolder.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(structuredContentFolder.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("numberOfStructuredContentFolders")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("numberOfStructuredContents")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("siteId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("viewableBy")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected StructuredContentFolder randomStructuredContentFolder()
		throws Exception {

		return new StructuredContentFolder() {
			{
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				description = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
				name = RandomTestUtil.randomString();
				siteId = testGroup.getGroupId();
			}
		};
	}

	protected StructuredContentFolder randomIrrelevantStructuredContentFolder()
		throws Exception {

		StructuredContentFolder randomIrrelevantStructuredContentFolder =
			randomStructuredContentFolder();

		randomIrrelevantStructuredContentFolder.setSiteId(
			irrelevantGroup.getGroupId());

		return randomIrrelevantStructuredContentFolder;
	}

	protected StructuredContentFolder randomPatchStructuredContentFolder()
		throws Exception {

		return randomStructuredContentFolder();
	}

	protected Group irrelevantGroup;
	protected Company testCompany;
	protected Group testGroup;
	protected Locale testLocale;
	protected String testUserNameAndPassword = "test@liferay.com:test";

	private static final Log _log = LogFactoryUtil.getLog(
		BaseStructuredContentFolderResourceTestCase.class);

	private static BeanUtilsBean _beanUtilsBean = new BeanUtilsBean() {

		@Override
		public void copyProperty(Object bean, String name, Object value)
			throws IllegalAccessException, InvocationTargetException {

			if (value != null) {
				super.copyProperty(bean, name, value);
			}
		}

	};
	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.headless.delivery.resource.v1_0.
			StructuredContentFolderResource _structuredContentFolderResource;

}