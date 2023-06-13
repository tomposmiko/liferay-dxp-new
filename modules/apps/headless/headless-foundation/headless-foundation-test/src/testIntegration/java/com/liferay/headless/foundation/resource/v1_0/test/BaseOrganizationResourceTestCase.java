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

import com.liferay.headless.foundation.dto.v1_0.ContactInformation;
import com.liferay.headless.foundation.dto.v1_0.Location;
import com.liferay.headless.foundation.dto.v1_0.Organization;
import com.liferay.headless.foundation.dto.v1_0.Services;
import com.liferay.headless.foundation.dto.v1_0.UserAccount;
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
public abstract class BaseOrganizationResourceTestCase {

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
	public void testGetMyUserAccountOrganizationsPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetOrganizationsPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetOrganization() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetOrganizationOrganizationsPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetUserAccountOrganizationsPage() throws Exception {
			Assert.assertTrue(true);
	}

	protected void assertResponseCode(int expectedResponseCode, Http.Response actualResponse) {
		Assert.assertEquals(expectedResponseCode, actualResponse.getResponseCode());
	}

	protected Page<Organization> invokeGetMyUserAccountOrganizationsPage(
				Long myUserAccountId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/my-user-accounts/{my-user-account-id}/organizations", myUserAccountId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<OrganizationImpl>>() {});
	}

	protected Http.Response invokeGetMyUserAccountOrganizationsPageResponse(
				Long myUserAccountId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/my-user-accounts/{my-user-account-id}/organizations", myUserAccountId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Page<Organization> invokeGetOrganizationsPage(
				Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/organizations", pagination));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<OrganizationImpl>>() {});
	}

	protected Http.Response invokeGetOrganizationsPageResponse(
				Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/organizations", pagination));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Organization invokeGetOrganization(
				Long organizationId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/organizations/{organization-id}", organizationId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), OrganizationImpl.class);
	}

	protected Http.Response invokeGetOrganizationResponse(
				Long organizationId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/organizations/{organization-id}", organizationId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Page<Organization> invokeGetOrganizationOrganizationsPage(
				Long organizationId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/organizations/{organization-id}/organizations", organizationId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<OrganizationImpl>>() {});
	}

	protected Http.Response invokeGetOrganizationOrganizationsPageResponse(
				Long organizationId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/organizations/{organization-id}/organizations", organizationId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Page<Organization> invokeGetUserAccountOrganizationsPage(
				Long userAccountId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/user-accounts/{user-account-id}/organizations", userAccountId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<OrganizationImpl>>() {});
	}

	protected Http.Response invokeGetUserAccountOrganizationsPageResponse(
				Long userAccountId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/user-accounts/{user-account-id}/organizations", userAccountId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}

	protected void assertEquals(Organization organization1, Organization organization2) {
		Assert.assertTrue(organization1 + " does not equal " + organization2, equals(organization1, organization2));
	}

	protected void assertEquals(List<Organization> organizations1, List<Organization> organizations2) {
		Assert.assertEquals(organizations1.size(), organizations2.size());

		for (int i = 0; i < organizations1.size(); i++) {
			Organization organization1 = organizations1.get(i);
			Organization organization2 = organizations2.get(i);

			assertEquals(organization1, organization2);
	}
	}

	protected boolean equals(Organization organization1, Organization organization2) {
		if (organization1 == organization2) {
			return true;
	}

		return false;
	}

	protected Organization randomOrganization() {
		return new OrganizationImpl() {
			{

						comment = RandomTestUtil.randomString();
						id = RandomTestUtil.randomLong();
						logo = RandomTestUtil.randomString();
						name = RandomTestUtil.randomString();
						parentOrganizationId = RandomTestUtil.randomLong();
	}
		};
	}

	protected Group testGroup;

	protected static class OrganizationImpl implements Organization {

	public String getComment() {
				return comment;
	}

	public void setComment(String comment) {
				this.comment = comment;
	}

	@JsonIgnore
	public void setComment(
				UnsafeSupplier<String, Throwable> commentUnsafeSupplier) {

				try {
					comment = commentUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String comment;
	public ContactInformation getContactInformation() {
				return contactInformation;
	}

	public void setContactInformation(ContactInformation contactInformation) {
				this.contactInformation = contactInformation;
	}

	@JsonIgnore
	public void setContactInformation(
				UnsafeSupplier<ContactInformation, Throwable> contactInformationUnsafeSupplier) {

				try {
					contactInformation = contactInformationUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected ContactInformation contactInformation;
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
	public Location getLocation() {
				return location;
	}

	public void setLocation(Location location) {
				this.location = location;
	}

	@JsonIgnore
	public void setLocation(
				UnsafeSupplier<Location, Throwable> locationUnsafeSupplier) {

				try {
					location = locationUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Location location;
	public String getLogo() {
				return logo;
	}

	public void setLogo(String logo) {
				this.logo = logo;
	}

	@JsonIgnore
	public void setLogo(
				UnsafeSupplier<String, Throwable> logoUnsafeSupplier) {

				try {
					logo = logoUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String logo;
	public UserAccount[] getMembers() {
				return members;
	}

	public void setMembers(UserAccount[] members) {
				this.members = members;
	}

	@JsonIgnore
	public void setMembers(
				UnsafeSupplier<UserAccount[], Throwable> membersUnsafeSupplier) {

				try {
					members = membersUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected UserAccount[] members;
	public Long[] getMembersIds() {
				return membersIds;
	}

	public void setMembersIds(Long[] membersIds) {
				this.membersIds = membersIds;
	}

	@JsonIgnore
	public void setMembersIds(
				UnsafeSupplier<Long[], Throwable> membersIdsUnsafeSupplier) {

				try {
					membersIds = membersIdsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long[] membersIds;
	public String getName() {
				return name;
	}

	public void setName(String name) {
				this.name = name;
	}

	@JsonIgnore
	public void setName(
				UnsafeSupplier<String, Throwable> nameUnsafeSupplier) {

				try {
					name = nameUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String name;
	public Organization getParentOrganization() {
				return parentOrganization;
	}

	public void setParentOrganization(Organization parentOrganization) {
				this.parentOrganization = parentOrganization;
	}

	@JsonIgnore
	public void setParentOrganization(
				UnsafeSupplier<Organization, Throwable> parentOrganizationUnsafeSupplier) {

				try {
					parentOrganization = parentOrganizationUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Organization parentOrganization;
	public Long getParentOrganizationId() {
				return parentOrganizationId;
	}

	public void setParentOrganizationId(Long parentOrganizationId) {
				this.parentOrganizationId = parentOrganizationId;
	}

	@JsonIgnore
	public void setParentOrganizationId(
				UnsafeSupplier<Long, Throwable> parentOrganizationIdUnsafeSupplier) {

				try {
					parentOrganizationId = parentOrganizationIdUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long parentOrganizationId;
	public Services[] getServices() {
				return services;
	}

	public void setServices(Services[] services) {
				this.services = services;
	}

	@JsonIgnore
	public void setServices(
				UnsafeSupplier<Services[], Throwable> servicesUnsafeSupplier) {

				try {
					services = servicesUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Services[] services;
	public Organization[] getSubOrganization() {
				return subOrganization;
	}

	public void setSubOrganization(Organization[] subOrganization) {
				this.subOrganization = subOrganization;
	}

	@JsonIgnore
	public void setSubOrganization(
				UnsafeSupplier<Organization[], Throwable> subOrganizationUnsafeSupplier) {

				try {
					subOrganization = subOrganizationUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Organization[] subOrganization;
	public Long[] getSubOrganizationIds() {
				return subOrganizationIds;
	}

	public void setSubOrganizationIds(Long[] subOrganizationIds) {
				this.subOrganizationIds = subOrganizationIds;
	}

	@JsonIgnore
	public void setSubOrganizationIds(
				UnsafeSupplier<Long[], Throwable> subOrganizationIdsUnsafeSupplier) {

				try {
					subOrganizationIds = subOrganizationIdsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long[] subOrganizationIds;

	public String toString() {
			StringBundler sb = new StringBundler();

			sb.append("{");

					sb.append("comment=");

				sb.append(comment);
					sb.append(", contactInformation=");

				sb.append(contactInformation);
					sb.append(", id=");

				sb.append(id);
					sb.append(", location=");

				sb.append(location);
					sb.append(", logo=");

				sb.append(logo);
					sb.append(", members=");

				sb.append(members);
					sb.append(", membersIds=");

				sb.append(membersIds);
					sb.append(", name=");

				sb.append(name);
					sb.append(", parentOrganization=");

				sb.append(parentOrganization);
					sb.append(", parentOrganizationId=");

				sb.append(parentOrganizationId);
					sb.append(", services=");

				sb.append(services);
					sb.append(", subOrganization=");

				sb.append(subOrganization);
					sb.append(", subOrganizationIds=");

				sb.append(subOrganizationIds);

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