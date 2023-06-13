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

package com.liferay.headless.foundation.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import com.liferay.headless.foundation.dto.v1_0.WebUrl;
import com.liferay.headless.foundation.resource.v1_0.WebUrlResource;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.InvocationTargetException;

import java.net.URL;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;

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
public abstract class BaseWebUrlResourceTestCase {

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

		_resourceURL = new URL(
			"http://localhost:8080/o/headless-foundation/v1.0");
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testGetOrganizationWebUrlsPage() throws Exception {
		Long organizationId =
			testGetOrganizationWebUrlsPage_getOrganizationId();
		Long irrelevantOrganizationId =
			testGetOrganizationWebUrlsPage_getIrrelevantOrganizationId();

		if ((irrelevantOrganizationId != null)) {
			WebUrl irrelevantWebUrl = testGetOrganizationWebUrlsPage_addWebUrl(
				irrelevantOrganizationId, randomIrrelevantWebUrl());

			Page<WebUrl> page = invokeGetOrganizationWebUrlsPage(
				irrelevantOrganizationId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantWebUrl), (List<WebUrl>)page.getItems());
			assertValid(page);
		}

		WebUrl webUrl1 = testGetOrganizationWebUrlsPage_addWebUrl(
			organizationId, randomWebUrl());

		WebUrl webUrl2 = testGetOrganizationWebUrlsPage_addWebUrl(
			organizationId, randomWebUrl());

		Page<WebUrl> page = invokeGetOrganizationWebUrlsPage(
			organizationId, Pagination.of(1, 2));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(webUrl1, webUrl2), (List<WebUrl>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetOrganizationWebUrlsPageWithPagination()
		throws Exception {

		Long organizationId =
			testGetOrganizationWebUrlsPage_getOrganizationId();

		WebUrl webUrl1 = testGetOrganizationWebUrlsPage_addWebUrl(
			organizationId, randomWebUrl());

		WebUrl webUrl2 = testGetOrganizationWebUrlsPage_addWebUrl(
			organizationId, randomWebUrl());

		WebUrl webUrl3 = testGetOrganizationWebUrlsPage_addWebUrl(
			organizationId, randomWebUrl());

		Page<WebUrl> page1 = invokeGetOrganizationWebUrlsPage(
			organizationId, Pagination.of(1, 2));

		List<WebUrl> webUrls1 = (List<WebUrl>)page1.getItems();

		Assert.assertEquals(webUrls1.toString(), 2, webUrls1.size());

		Page<WebUrl> page2 = invokeGetOrganizationWebUrlsPage(
			organizationId, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<WebUrl> webUrls2 = (List<WebUrl>)page2.getItems();

		Assert.assertEquals(webUrls2.toString(), 1, webUrls2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(webUrl1, webUrl2, webUrl3),
			new ArrayList<WebUrl>() {
				{
					addAll(webUrls1);
					addAll(webUrls2);
				}
			});
	}

	protected WebUrl testGetOrganizationWebUrlsPage_addWebUrl(
			Long organizationId, WebUrl webUrl)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetOrganizationWebUrlsPage_getOrganizationId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetOrganizationWebUrlsPage_getIrrelevantOrganizationId()
		throws Exception {

		return null;
	}

	protected Page<WebUrl> invokeGetOrganizationWebUrlsPage(
			Long organizationId, Pagination pagination)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/organizations/{organization-id}/web-urls",
					organizationId);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		return _outputObjectMapper.readValue(
			string,
			new TypeReference<Page<WebUrl>>() {
			});
	}

	protected Http.Response invokeGetOrganizationWebUrlsPageResponse(
			Long organizationId, Pagination pagination)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/organizations/{organization-id}/web-urls",
					organizationId);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetUserAccountWebUrlsPage() throws Exception {
		Long userAccountId = testGetUserAccountWebUrlsPage_getUserAccountId();
		Long irrelevantUserAccountId =
			testGetUserAccountWebUrlsPage_getIrrelevantUserAccountId();

		if ((irrelevantUserAccountId != null)) {
			WebUrl irrelevantWebUrl = testGetUserAccountWebUrlsPage_addWebUrl(
				irrelevantUserAccountId, randomIrrelevantWebUrl());

			Page<WebUrl> page = invokeGetUserAccountWebUrlsPage(
				irrelevantUserAccountId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantWebUrl), (List<WebUrl>)page.getItems());
			assertValid(page);
		}

		WebUrl webUrl1 = testGetUserAccountWebUrlsPage_addWebUrl(
			userAccountId, randomWebUrl());

		WebUrl webUrl2 = testGetUserAccountWebUrlsPage_addWebUrl(
			userAccountId, randomWebUrl());

		Page<WebUrl> page = invokeGetUserAccountWebUrlsPage(
			userAccountId, Pagination.of(1, 2));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(webUrl1, webUrl2), (List<WebUrl>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetUserAccountWebUrlsPageWithPagination() throws Exception {
		Long userAccountId = testGetUserAccountWebUrlsPage_getUserAccountId();

		WebUrl webUrl1 = testGetUserAccountWebUrlsPage_addWebUrl(
			userAccountId, randomWebUrl());

		WebUrl webUrl2 = testGetUserAccountWebUrlsPage_addWebUrl(
			userAccountId, randomWebUrl());

		WebUrl webUrl3 = testGetUserAccountWebUrlsPage_addWebUrl(
			userAccountId, randomWebUrl());

		Page<WebUrl> page1 = invokeGetUserAccountWebUrlsPage(
			userAccountId, Pagination.of(1, 2));

		List<WebUrl> webUrls1 = (List<WebUrl>)page1.getItems();

		Assert.assertEquals(webUrls1.toString(), 2, webUrls1.size());

		Page<WebUrl> page2 = invokeGetUserAccountWebUrlsPage(
			userAccountId, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<WebUrl> webUrls2 = (List<WebUrl>)page2.getItems();

		Assert.assertEquals(webUrls2.toString(), 1, webUrls2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(webUrl1, webUrl2, webUrl3),
			new ArrayList<WebUrl>() {
				{
					addAll(webUrls1);
					addAll(webUrls2);
				}
			});
	}

	protected WebUrl testGetUserAccountWebUrlsPage_addWebUrl(
			Long userAccountId, WebUrl webUrl)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetUserAccountWebUrlsPage_getUserAccountId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetUserAccountWebUrlsPage_getIrrelevantUserAccountId()
		throws Exception {

		return null;
	}

	protected Page<WebUrl> invokeGetUserAccountWebUrlsPage(
			Long userAccountId, Pagination pagination)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/user-accounts/{user-account-id}/web-urls", userAccountId);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		return _outputObjectMapper.readValue(
			string,
			new TypeReference<Page<WebUrl>>() {
			});
	}

	protected Http.Response invokeGetUserAccountWebUrlsPageResponse(
			Long userAccountId, Pagination pagination)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/user-accounts/{user-account-id}/web-urls", userAccountId);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetWebUrl() throws Exception {
		WebUrl postWebUrl = testGetWebUrl_addWebUrl();

		WebUrl getWebUrl = invokeGetWebUrl(postWebUrl.getId());

		assertEquals(postWebUrl, getWebUrl);
		assertValid(getWebUrl);
	}

	protected WebUrl testGetWebUrl_addWebUrl() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected WebUrl invokeGetWebUrl(Long webUrlId) throws Exception {
		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL + _toPath("/web-urls/{web-url-id}", webUrlId);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(string, WebUrl.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokeGetWebUrlResponse(Long webUrlId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL + _toPath("/web-urls/{web-url-id}", webUrlId);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	protected void assertResponseCode(
		int expectedResponseCode, Http.Response actualResponse) {

		Assert.assertEquals(
			expectedResponseCode, actualResponse.getResponseCode());
	}

	protected void assertEquals(WebUrl webUrl1, WebUrl webUrl2) {
		Assert.assertTrue(
			webUrl1 + " does not equal " + webUrl2, equals(webUrl1, webUrl2));
	}

	protected void assertEquals(List<WebUrl> webUrls1, List<WebUrl> webUrls2) {
		Assert.assertEquals(webUrls1.size(), webUrls2.size());

		for (int i = 0; i < webUrls1.size(); i++) {
			WebUrl webUrl1 = webUrls1.get(i);
			WebUrl webUrl2 = webUrls2.get(i);

			assertEquals(webUrl1, webUrl2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<WebUrl> webUrls1, List<WebUrl> webUrls2) {

		Assert.assertEquals(webUrls1.size(), webUrls2.size());

		for (WebUrl webUrl1 : webUrls1) {
			boolean contains = false;

			for (WebUrl webUrl2 : webUrls2) {
				if (equals(webUrl1, webUrl2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				webUrls2 + " does not contain " + webUrl1, contains);
		}
	}

	protected void assertValid(WebUrl webUrl) {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertValid(Page<WebUrl> page) {
		boolean valid = false;

		Collection<WebUrl> webUrls = page.getItems();

		int size = webUrls.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected boolean equals(WebUrl webUrl1, WebUrl webUrl2) {
		if (webUrl1 == webUrl2) {
			return true;
		}

		return false;
	}

	protected Collection<EntityField> getEntityFields() throws Exception {
		if (!(_webUrlResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_webUrlResource;

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
		EntityField entityField, String operator, WebUrl webUrl) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("url")) {
			sb.append("'");
			sb.append(String.valueOf(webUrl.getUrl()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("urlType")) {
			sb.append("'");
			sb.append(String.valueOf(webUrl.getUrlType()));
			sb.append("'");

			return sb.toString();
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected WebUrl randomWebUrl() {
		return new WebUrl() {
			{
				id = RandomTestUtil.randomLong();
				url = RandomTestUtil.randomString();
				urlType = RandomTestUtil.randomString();
			}
		};
	}

	protected WebUrl randomIrrelevantWebUrl() {
		return randomWebUrl();
	}

	protected WebUrl randomPatchWebUrl() {
		return randomWebUrl();
	}

	protected Group irrelevantGroup;
	protected Group testGroup;

	protected static class Page<T> {

		public Collection<T> getItems() {
			return new ArrayList<>(items);
		}

		public long getLastPage() {
			return lastPage;
		}

		public long getPage() {
			return page;
		}

		public long getPageSize() {
			return pageSize;
		}

		public long getTotalCount() {
			return totalCount;
		}

		@JsonProperty
		protected Collection<T> items;

		@JsonProperty
		protected long lastPage;

		@JsonProperty
		protected long page;

		@JsonProperty
		protected long pageSize;

		@JsonProperty
		protected long totalCount;

	}

	private Http.Options _createHttpOptions() {
		Http.Options options = new Http.Options();

		options.addHeader("Accept", "application/json");

		String userNameAndPassword = "test@liferay.com:test";

		String encodedUserNameAndPassword = Base64.encode(
			userNameAndPassword.getBytes());

		options.addHeader(
			"Authorization", "Basic " + encodedUserNameAndPassword);

		options.addHeader("Content-Type", "application/json");

		return options;
	}

	private String _toPath(String template, Object... values) {
		if (ArrayUtil.isEmpty(values)) {
			return template;
		}

		for (int i = 0; i < values.length; i++) {
			template = template.replaceFirst(
				"\\{.*?\\}", String.valueOf(values[i]));
		}

		return template;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseWebUrlResourceTestCase.class);

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
	private final static ObjectMapper _inputObjectMapper = new ObjectMapper() {
		{
			setFilterProvider(
				new SimpleFilterProvider() {
					{
						addFilter(
							"Liferay.Vulcan",
							SimpleBeanPropertyFilter.serializeAll());
					}
				});
			setSerializationInclusion(JsonInclude.Include.NON_NULL);
		}
	};
	private final static ObjectMapper _outputObjectMapper = new ObjectMapper() {
		{
			setFilterProvider(
				new SimpleFilterProvider() {
					{
						addFilter(
							"Liferay.Vulcan",
							SimpleBeanPropertyFilter.serializeAll());
					}
				});
		}
	};

	@Inject
	private WebUrlResource _webUrlResource;

	private URL _resourceURL;

}