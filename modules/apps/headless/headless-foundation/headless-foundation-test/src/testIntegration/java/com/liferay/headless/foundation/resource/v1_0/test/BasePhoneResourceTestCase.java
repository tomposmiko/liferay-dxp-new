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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.headless.foundation.dto.v1_0.Phone;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BasePhoneResourceTestCase {

	@Before
	public void setUp() throws Exception {
		testGroup = GroupTestUtil.addGroup();

		_resourceURL = new URL(
			"http://localhost:8080/o/headless-foundation/v1.0");
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testGetGenericParentPhonesPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetPhone() throws Exception {
			Assert.assertTrue(true);
	}

	protected void assertResponseCode(int expectedResponseCode, Http.Response actualResponse) {
		Assert.assertEquals(expectedResponseCode, actualResponse.getResponseCode());
	}

	protected Page<Phone> invokeGetGenericParentPhonesPage(
				Object genericParentId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/phones", genericParentId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<PhoneImpl>>() {});
	}

	protected Http.Response invokeGetGenericParentPhonesPageResponse(
				Object genericParentId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/phones", genericParentId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Phone invokeGetPhone(
				Long phoneId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/phones/{phone-id}", phoneId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), PhoneImpl.class);
	}

	protected Http.Response invokeGetPhoneResponse(
				Long phoneId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/phones/{phone-id}", phoneId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}

	protected void assertEquals(Phone phone1, Phone phone2) {
		Assert.assertTrue(phone1 + " does not equal " + phone2, equals(phone1, phone2));
	}

	protected void assertEquals(List<Phone> phones1, List<Phone> phones2) {
		Assert.assertEquals(phones1.size(), phones2.size());

		for (int i = 0; i < phones1.size(); i++) {
			Phone phone1 = phones1.get(i);
			Phone phone2 = phones2.get(i);

			assertEquals(phone1, phone2);
	}
	}

	protected boolean equals(Phone phone1, Phone phone2) {
		if (phone1 == phone2) {
			return true;
	}

		return false;
	}

	protected Phone randomPhone() {
		return new PhoneImpl() {
			{

						extension = RandomTestUtil.randomString();
						id = RandomTestUtil.randomLong();
						phoneNumber = RandomTestUtil.randomString();
						phoneType = RandomTestUtil.randomString();
	}
		};
	}

	protected Group testGroup;

	protected static class PhoneImpl implements Phone {

	public String getExtension() {
				return extension;
	}

	public void setExtension(String extension) {
				this.extension = extension;
	}

	@JsonIgnore
	public void setExtension(
				UnsafeSupplier<String, Throwable> extensionUnsafeSupplier) {

				try {
					extension = extensionUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String extension;
	public Long getId() {
				return id;
	}

	public void setId(Long id) {
				this.id = id;
	}

	@JsonIgnore
	public void setId(
				UnsafeSupplier<Long, Throwable> idUnsafeSupplier) {

				try {
					id = idUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long id;
	public String getPhoneNumber() {
				return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
				this.phoneNumber = phoneNumber;
	}

	@JsonIgnore
	public void setPhoneNumber(
				UnsafeSupplier<String, Throwable> phoneNumberUnsafeSupplier) {

				try {
					phoneNumber = phoneNumberUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String phoneNumber;
	public String getPhoneType() {
				return phoneType;
	}

	public void setPhoneType(String phoneType) {
				this.phoneType = phoneType;
	}

	@JsonIgnore
	public void setPhoneType(
				UnsafeSupplier<String, Throwable> phoneTypeUnsafeSupplier) {

				try {
					phoneType = phoneTypeUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String phoneType;

	public String toString() {
			StringBundler sb = new StringBundler();

			sb.append("{");

					sb.append("extension=");

				sb.append(extension);
					sb.append(", id=");

				sb.append(id);
					sb.append(", phoneNumber=");

				sb.append(phoneNumber);
					sb.append(", phoneType=");

				sb.append(phoneType);

			sb.append("}");

			return sb.toString();
	}

	}

	protected static class Page<T> {

	public Collection<T> getItems() {
			return new ArrayList<>(items);
	}

	public int getItemsPerPage() {
			return itemsPerPage;
	}

	public int getLastPageNumber() {
			return lastPageNumber;
	}

	public int getPageNumber() {
			return pageNumber;
	}

	public int getTotalCount() {
			return totalCount;
	}

	@JsonProperty
	protected Collection<T> items;

	@JsonProperty
	protected int itemsPerPage;

	@JsonProperty
	protected int lastPageNumber;

	@JsonProperty
	protected int pageNumber;

	@JsonProperty
	protected int totalCount;

	}

	private Http.Options _createHttpOptions() {
		Http.Options options = new Http.Options();

		options.addHeader("Accept", "application/json");

		String userNameAndPassword = "test@liferay.com:test";

		String encodedUserNameAndPassword = Base64.encode(userNameAndPassword.getBytes());

		options.addHeader("Authorization", "Basic " + encodedUserNameAndPassword);

		options.addHeader("Content-Type", "application/json");

		return options;
	}

	private String _toPath(String template, Object value) {
		return template.replaceFirst("\\{.*\\}", String.valueOf(value));
	}

	private final static ObjectMapper _inputObjectMapper = new ObjectMapper() {
		{
			setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}
	};
	private final static ObjectMapper _outputObjectMapper = new ObjectMapper();

	private URL _resourceURL;

}