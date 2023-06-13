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
import com.liferay.headless.foundation.dto.v1_0.Organization;
import com.liferay.headless.foundation.dto.v1_0.Role;
import com.liferay.headless.foundation.dto.v1_0.UserAccount;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
public abstract class BaseUserAccountResourceTestCase {

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
	public void testGetMyUserAccount() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetOrganizationUserAccountsPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetUserAccountsPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPostUserAccount() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testDeleteUserAccount() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetUserAccount() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPutUserAccount() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetWebSiteUserAccountsPage() throws Exception {
			Assert.assertTrue(true);
	}

	protected void assertResponseCode(int expectedResponseCode, Http.Response actualResponse) {
		Assert.assertEquals(expectedResponseCode, actualResponse.getResponseCode());
	}

	protected UserAccount invokeGetMyUserAccount(
				Long myUserAccountId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/my-user-accounts/{my-user-account-id}", myUserAccountId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), UserAccountImpl.class);
	}

	protected Http.Response invokeGetMyUserAccountResponse(
				Long myUserAccountId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/my-user-accounts/{my-user-account-id}", myUserAccountId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Page<UserAccount> invokeGetOrganizationUserAccountsPage(
				Long organizationId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/organizations/{organization-id}/user-accounts", organizationId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<UserAccountImpl>>() {});
	}

	protected Http.Response invokeGetOrganizationUserAccountsPageResponse(
				Long organizationId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/organizations/{organization-id}/user-accounts", organizationId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Page<UserAccount> invokeGetUserAccountsPage(
				String fullnamequery,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/user-accounts", fullnamequery));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<UserAccountImpl>>() {});
	}

	protected Http.Response invokeGetUserAccountsPageResponse(
				String fullnamequery,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/user-accounts", fullnamequery));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected UserAccount invokePostUserAccount(
				UserAccount userAccount)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/user-accounts", userAccount));

				options.setPost(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), UserAccountImpl.class);
	}

	protected Http.Response invokePostUserAccountResponse(
				UserAccount userAccount)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/user-accounts", userAccount));

				options.setPost(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected boolean invokeDeleteUserAccount(
				Long userAccountId)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setDelete(true);

			options.setLocation(_resourceURL + _toPath("/user-accounts/{user-account-id}", userAccountId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), Boolean.class);
	}

	protected Http.Response invokeDeleteUserAccountResponse(
				Long userAccountId)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setDelete(true);

			options.setLocation(_resourceURL + _toPath("/user-accounts/{user-account-id}", userAccountId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected UserAccount invokeGetUserAccount(
				Long userAccountId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/user-accounts/{user-account-id}", userAccountId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), UserAccountImpl.class);
	}

	protected Http.Response invokeGetUserAccountResponse(
				Long userAccountId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/user-accounts/{user-account-id}", userAccountId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected UserAccount invokePutUserAccount(
				Long userAccountId,UserAccount userAccount)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(userAccount), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/user-accounts/{user-account-id}", userAccountId));

				options.setPut(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), UserAccountImpl.class);
	}

	protected Http.Response invokePutUserAccountResponse(
				Long userAccountId,UserAccount userAccount)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(userAccount), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/user-accounts/{user-account-id}", userAccountId));

				options.setPut(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Page<UserAccount> invokeGetWebSiteUserAccountsPage(
				Long webSiteId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/web-sites/{web-site-id}/user-accounts", webSiteId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<UserAccountImpl>>() {});
	}

	protected Http.Response invokeGetWebSiteUserAccountsPageResponse(
				Long webSiteId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/web-sites/{web-site-id}/user-accounts", webSiteId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}

	protected void assertEquals(UserAccount userAccount1, UserAccount userAccount2) {
		Assert.assertTrue(userAccount1 + " does not equal " + userAccount2, equals(userAccount1, userAccount2));
	}

	protected void assertEquals(List<UserAccount> userAccounts1, List<UserAccount> userAccounts2) {
		Assert.assertEquals(userAccounts1.size(), userAccounts2.size());

		for (int i = 0; i < userAccounts1.size(); i++) {
			UserAccount userAccount1 = userAccounts1.get(i);
			UserAccount userAccount2 = userAccounts2.get(i);

			assertEquals(userAccount1, userAccount2);
	}
	}

	protected boolean equals(UserAccount userAccount1, UserAccount userAccount2) {
		if (userAccount1 == userAccount2) {
			return true;
	}

		return false;
	}

	protected UserAccount randomUserAccount() {
		return new UserAccountImpl() {
			{

						additionalName = RandomTestUtil.randomString();
						alternateName = RandomTestUtil.randomString();
						birthDate = RandomTestUtil.nextDate();
						dashboardURL = RandomTestUtil.randomString();
						email = RandomTestUtil.randomString();
						familyName = RandomTestUtil.randomString();
						givenName = RandomTestUtil.randomString();
						honorificPrefix = RandomTestUtil.randomString();
						honorificSuffix = RandomTestUtil.randomString();
						id = RandomTestUtil.randomLong();
						image = RandomTestUtil.randomString();
						jobTitle = RandomTestUtil.randomString();
						name = RandomTestUtil.randomString();
						profileURL = RandomTestUtil.randomString();
	}
		};
	}

	protected Group testGroup;

	protected static class UserAccountImpl implements UserAccount {

	public String getAdditionalName() {
				return additionalName;
	}

	public void setAdditionalName(String additionalName) {
				this.additionalName = additionalName;
	}

	@JsonIgnore
	public void setAdditionalName(
				UnsafeSupplier<String, Throwable> additionalNameUnsafeSupplier) {

				try {
					additionalName = additionalNameUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String additionalName;
	public String getAlternateName() {
				return alternateName;
	}

	public void setAlternateName(String alternateName) {
				this.alternateName = alternateName;
	}

	@JsonIgnore
	public void setAlternateName(
				UnsafeSupplier<String, Throwable> alternateNameUnsafeSupplier) {

				try {
					alternateName = alternateNameUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String alternateName;
	public Date getBirthDate() {
				return birthDate;
	}

	public void setBirthDate(Date birthDate) {
				this.birthDate = birthDate;
	}

	@JsonIgnore
	public void setBirthDate(
				UnsafeSupplier<Date, Throwable> birthDateUnsafeSupplier) {

				try {
					birthDate = birthDateUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Date birthDate;
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
	public String getDashboardURL() {
				return dashboardURL;
	}

	public void setDashboardURL(String dashboardURL) {
				this.dashboardURL = dashboardURL;
	}

	@JsonIgnore
	public void setDashboardURL(
				UnsafeSupplier<String, Throwable> dashboardURLUnsafeSupplier) {

				try {
					dashboardURL = dashboardURLUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String dashboardURL;
	public String getEmail() {
				return email;
	}

	public void setEmail(String email) {
				this.email = email;
	}

	@JsonIgnore
	public void setEmail(
				UnsafeSupplier<String, Throwable> emailUnsafeSupplier) {

				try {
					email = emailUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String email;
	public String getFamilyName() {
				return familyName;
	}

	public void setFamilyName(String familyName) {
				this.familyName = familyName;
	}

	@JsonIgnore
	public void setFamilyName(
				UnsafeSupplier<String, Throwable> familyNameUnsafeSupplier) {

				try {
					familyName = familyNameUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String familyName;
	public String getGivenName() {
				return givenName;
	}

	public void setGivenName(String givenName) {
				this.givenName = givenName;
	}

	@JsonIgnore
	public void setGivenName(
				UnsafeSupplier<String, Throwable> givenNameUnsafeSupplier) {

				try {
					givenName = givenNameUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String givenName;
	public String getHonorificPrefix() {
				return honorificPrefix;
	}

	public void setHonorificPrefix(String honorificPrefix) {
				this.honorificPrefix = honorificPrefix;
	}

	@JsonIgnore
	public void setHonorificPrefix(
				UnsafeSupplier<String, Throwable> honorificPrefixUnsafeSupplier) {

				try {
					honorificPrefix = honorificPrefixUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String honorificPrefix;
	public String getHonorificSuffix() {
				return honorificSuffix;
	}

	public void setHonorificSuffix(String honorificSuffix) {
				this.honorificSuffix = honorificSuffix;
	}

	@JsonIgnore
	public void setHonorificSuffix(
				UnsafeSupplier<String, Throwable> honorificSuffixUnsafeSupplier) {

				try {
					honorificSuffix = honorificSuffixUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String honorificSuffix;
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
	public String getImage() {
				return image;
	}

	public void setImage(String image) {
				this.image = image;
	}

	@JsonIgnore
	public void setImage(
				UnsafeSupplier<String, Throwable> imageUnsafeSupplier) {

				try {
					image = imageUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String image;
	public String getJobTitle() {
				return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
				this.jobTitle = jobTitle;
	}

	@JsonIgnore
	public void setJobTitle(
				UnsafeSupplier<String, Throwable> jobTitleUnsafeSupplier) {

				try {
					jobTitle = jobTitleUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String jobTitle;
	public Organization[] getMyOrganizations() {
				return myOrganizations;
	}

	public void setMyOrganizations(Organization[] myOrganizations) {
				this.myOrganizations = myOrganizations;
	}

	@JsonIgnore
	public void setMyOrganizations(
				UnsafeSupplier<Organization[], Throwable> myOrganizationsUnsafeSupplier) {

				try {
					myOrganizations = myOrganizationsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Organization[] myOrganizations;
	public Long[] getMyOrganizationsIds() {
				return myOrganizationsIds;
	}

	public void setMyOrganizationsIds(Long[] myOrganizationsIds) {
				this.myOrganizationsIds = myOrganizationsIds;
	}

	@JsonIgnore
	public void setMyOrganizationsIds(
				UnsafeSupplier<Long[], Throwable> myOrganizationsIdsUnsafeSupplier) {

				try {
					myOrganizationsIds = myOrganizationsIdsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long[] myOrganizationsIds;
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
	public String getProfileURL() {
				return profileURL;
	}

	public void setProfileURL(String profileURL) {
				this.profileURL = profileURL;
	}

	@JsonIgnore
	public void setProfileURL(
				UnsafeSupplier<String, Throwable> profileURLUnsafeSupplier) {

				try {
					profileURL = profileURLUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String profileURL;
	public Role[] getRoles() {
				return roles;
	}

	public void setRoles(Role[] roles) {
				this.roles = roles;
	}

	@JsonIgnore
	public void setRoles(
				UnsafeSupplier<Role[], Throwable> rolesUnsafeSupplier) {

				try {
					roles = rolesUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Role[] roles;
	public Long[] getRolesIds() {
				return rolesIds;
	}

	public void setRolesIds(Long[] rolesIds) {
				this.rolesIds = rolesIds;
	}

	@JsonIgnore
	public void setRolesIds(
				UnsafeSupplier<Long[], Throwable> rolesIdsUnsafeSupplier) {

				try {
					rolesIds = rolesIdsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long[] rolesIds;
	public String[] getTasksAssignedToMe() {
				return tasksAssignedToMe;
	}

	public void setTasksAssignedToMe(String[] tasksAssignedToMe) {
				this.tasksAssignedToMe = tasksAssignedToMe;
	}

	@JsonIgnore
	public void setTasksAssignedToMe(
				UnsafeSupplier<String[], Throwable> tasksAssignedToMeUnsafeSupplier) {

				try {
					tasksAssignedToMe = tasksAssignedToMeUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String[] tasksAssignedToMe;
	public String[] getTasksAssignedToMyRoles() {
				return tasksAssignedToMyRoles;
	}

	public void setTasksAssignedToMyRoles(String[] tasksAssignedToMyRoles) {
				this.tasksAssignedToMyRoles = tasksAssignedToMyRoles;
	}

	@JsonIgnore
	public void setTasksAssignedToMyRoles(
				UnsafeSupplier<String[], Throwable> tasksAssignedToMyRolesUnsafeSupplier) {

				try {
					tasksAssignedToMyRoles = tasksAssignedToMyRolesUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String[] tasksAssignedToMyRoles;

	public String toString() {
			StringBundler sb = new StringBundler();

			sb.append("{");

					sb.append("additionalName=");

				sb.append(additionalName);
					sb.append(", alternateName=");

				sb.append(alternateName);
					sb.append(", birthDate=");

				sb.append(birthDate);
					sb.append(", contactInformation=");

				sb.append(contactInformation);
					sb.append(", dashboardURL=");

				sb.append(dashboardURL);
					sb.append(", email=");

				sb.append(email);
					sb.append(", familyName=");

				sb.append(familyName);
					sb.append(", givenName=");

				sb.append(givenName);
					sb.append(", honorificPrefix=");

				sb.append(honorificPrefix);
					sb.append(", honorificSuffix=");

				sb.append(honorificSuffix);
					sb.append(", id=");

				sb.append(id);
					sb.append(", image=");

				sb.append(image);
					sb.append(", jobTitle=");

				sb.append(jobTitle);
					sb.append(", myOrganizations=");

				sb.append(myOrganizations);
					sb.append(", myOrganizationsIds=");

				sb.append(myOrganizationsIds);
					sb.append(", name=");

				sb.append(name);
					sb.append(", profileURL=");

				sb.append(profileURL);
					sb.append(", roles=");

				sb.append(roles);
					sb.append(", rolesIds=");

				sb.append(rolesIds);
					sb.append(", tasksAssignedToMe=");

				sb.append(tasksAssignedToMe);
					sb.append(", tasksAssignedToMyRoles=");

				sb.append(tasksAssignedToMyRoles);

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