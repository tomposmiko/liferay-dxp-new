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

import com.liferay.headless.foundation.dto.v1_0.PostalAddress;
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
public abstract class BasePostalAddressResourceTestCase {

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
	public void testGetGenericParentPostalAddressesPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetAddress() throws Exception {
			Assert.assertTrue(true);
	}

	protected void assertResponseCode(int expectedResponseCode, Http.Response actualResponse) {
		Assert.assertEquals(expectedResponseCode, actualResponse.getResponseCode());
	}

	protected Page<PostalAddress> invokeGetGenericParentPostalAddressesPage(
				Object genericParentId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/addresses", genericParentId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<PostalAddressImpl>>() {});
	}

	protected Http.Response invokeGetGenericParentPostalAddressesPageResponse(
				Object genericParentId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/addresses", genericParentId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected PostalAddress invokeGetAddress(
				Long addressId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/addresses/{address-id}", addressId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), PostalAddressImpl.class);
	}

	protected Http.Response invokeGetAddressResponse(
				Long addressId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/addresses/{address-id}", addressId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}

	protected void assertEquals(PostalAddress postalAddress1, PostalAddress postalAddress2) {
		Assert.assertTrue(postalAddress1 + " does not equal " + postalAddress2, equals(postalAddress1, postalAddress2));
	}

	protected void assertEquals(List<PostalAddress> postalAddresses1, List<PostalAddress> postalAddresses2) {
		Assert.assertEquals(postalAddresses1.size(), postalAddresses2.size());

		for (int i = 0; i < postalAddresses1.size(); i++) {
			PostalAddress postalAddress1 = postalAddresses1.get(i);
			PostalAddress postalAddress2 = postalAddresses2.get(i);

			assertEquals(postalAddress1, postalAddress2);
	}
	}

	protected boolean equals(PostalAddress postalAddress1, PostalAddress postalAddress2) {
		if (postalAddress1 == postalAddress2) {
			return true;
	}

		return false;
	}

	protected PostalAddress randomPostalAddress() {
		return new PostalAddressImpl() {
			{

						addressCountry = RandomTestUtil.randomString();
						addressLocality = RandomTestUtil.randomString();
						addressRegion = RandomTestUtil.randomString();
						addressType = RandomTestUtil.randomString();
						id = RandomTestUtil.randomLong();
						postalCode = RandomTestUtil.randomString();
						streetAddressLine1 = RandomTestUtil.randomString();
						streetAddressLine2 = RandomTestUtil.randomString();
						streetAddressLine3 = RandomTestUtil.randomString();
	}
		};
	}

	protected Group testGroup;

	protected static class PostalAddressImpl implements PostalAddress {

	public String getAddressCountry() {
				return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
				this.addressCountry = addressCountry;
	}

	@JsonIgnore
	public void setAddressCountry(
				UnsafeSupplier<String, Throwable> addressCountryUnsafeSupplier) {

				try {
					addressCountry = addressCountryUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String addressCountry;
	public String getAddressLocality() {
				return addressLocality;
	}

	public void setAddressLocality(String addressLocality) {
				this.addressLocality = addressLocality;
	}

	@JsonIgnore
	public void setAddressLocality(
				UnsafeSupplier<String, Throwable> addressLocalityUnsafeSupplier) {

				try {
					addressLocality = addressLocalityUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String addressLocality;
	public String getAddressRegion() {
				return addressRegion;
	}

	public void setAddressRegion(String addressRegion) {
				this.addressRegion = addressRegion;
	}

	@JsonIgnore
	public void setAddressRegion(
				UnsafeSupplier<String, Throwable> addressRegionUnsafeSupplier) {

				try {
					addressRegion = addressRegionUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String addressRegion;
	public String getAddressType() {
				return addressType;
	}

	public void setAddressType(String addressType) {
				this.addressType = addressType;
	}

	@JsonIgnore
	public void setAddressType(
				UnsafeSupplier<String, Throwable> addressTypeUnsafeSupplier) {

				try {
					addressType = addressTypeUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String addressType;
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
	public String getPostalCode() {
				return postalCode;
	}

	public void setPostalCode(String postalCode) {
				this.postalCode = postalCode;
	}

	@JsonIgnore
	public void setPostalCode(
				UnsafeSupplier<String, Throwable> postalCodeUnsafeSupplier) {

				try {
					postalCode = postalCodeUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String postalCode;
	public String getStreetAddressLine1() {
				return streetAddressLine1;
	}

	public void setStreetAddressLine1(String streetAddressLine1) {
				this.streetAddressLine1 = streetAddressLine1;
	}

	@JsonIgnore
	public void setStreetAddressLine1(
				UnsafeSupplier<String, Throwable> streetAddressLine1UnsafeSupplier) {

				try {
					streetAddressLine1 = streetAddressLine1UnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String streetAddressLine1;
	public String getStreetAddressLine2() {
				return streetAddressLine2;
	}

	public void setStreetAddressLine2(String streetAddressLine2) {
				this.streetAddressLine2 = streetAddressLine2;
	}

	@JsonIgnore
	public void setStreetAddressLine2(
				UnsafeSupplier<String, Throwable> streetAddressLine2UnsafeSupplier) {

				try {
					streetAddressLine2 = streetAddressLine2UnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String streetAddressLine2;
	public String getStreetAddressLine3() {
				return streetAddressLine3;
	}

	public void setStreetAddressLine3(String streetAddressLine3) {
				this.streetAddressLine3 = streetAddressLine3;
	}

	@JsonIgnore
	public void setStreetAddressLine3(
				UnsafeSupplier<String, Throwable> streetAddressLine3UnsafeSupplier) {

				try {
					streetAddressLine3 = streetAddressLine3UnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String streetAddressLine3;

	public String toString() {
			StringBundler sb = new StringBundler();

			sb.append("{");

					sb.append("addressCountry=");

				sb.append(addressCountry);
					sb.append(", addressLocality=");

				sb.append(addressLocality);
					sb.append(", addressRegion=");

				sb.append(addressRegion);
					sb.append(", addressType=");

				sb.append(addressType);
					sb.append(", id=");

				sb.append(id);
					sb.append(", postalCode=");

				sb.append(postalCode);
					sb.append(", streetAddressLine1=");

				sb.append(streetAddressLine1);
					sb.append(", streetAddressLine2=");

				sb.append(streetAddressLine2);
					sb.append(", streetAddressLine3=");

				sb.append(streetAddressLine3);

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