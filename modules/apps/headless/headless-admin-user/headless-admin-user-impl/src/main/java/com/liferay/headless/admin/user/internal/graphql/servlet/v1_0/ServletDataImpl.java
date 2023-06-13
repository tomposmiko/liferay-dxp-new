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

package com.liferay.headless.admin.user.internal.graphql.servlet.v1_0;

import com.liferay.headless.admin.user.internal.graphql.mutation.v1_0.Mutation;
import com.liferay.headless.admin.user.internal.graphql.query.v1_0.Query;
import com.liferay.headless.admin.user.internal.resource.v1_0.AccountResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.AccountRoleResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.EmailAddressResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.OrganizationResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.PhoneResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.PostalAddressResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.RoleResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.SegmentResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.SegmentUserResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.SiteResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.SubscriptionResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.UserAccountResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.UserGroupResourceImpl;
import com.liferay.headless.admin.user.internal.resource.v1_0.WebUrlResourceImpl;
import com.liferay.headless.admin.user.resource.v1_0.AccountResource;
import com.liferay.headless.admin.user.resource.v1_0.AccountRoleResource;
import com.liferay.headless.admin.user.resource.v1_0.EmailAddressResource;
import com.liferay.headless.admin.user.resource.v1_0.OrganizationResource;
import com.liferay.headless.admin.user.resource.v1_0.PhoneResource;
import com.liferay.headless.admin.user.resource.v1_0.PostalAddressResource;
import com.liferay.headless.admin.user.resource.v1_0.RoleResource;
import com.liferay.headless.admin.user.resource.v1_0.SegmentResource;
import com.liferay.headless.admin.user.resource.v1_0.SegmentUserResource;
import com.liferay.headless.admin.user.resource.v1_0.SiteResource;
import com.liferay.headless.admin.user.resource.v1_0.SubscriptionResource;
import com.liferay.headless.admin.user.resource.v1_0.UserAccountResource;
import com.liferay.headless.admin.user.resource.v1_0.UserGroupResource;
import com.liferay.headless.admin.user.resource.v1_0.WebUrlResource;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.vulcan.graphql.servlet.ServletData;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author Javier Gamarra
 * @generated
 */
@Component(service = ServletData.class)
@Generated("")
public class ServletDataImpl implements ServletData {

	@Activate
	public void activate(BundleContext bundleContext) {
		Mutation.setAccountResourceComponentServiceObjects(
			_accountResourceComponentServiceObjects);
		Mutation.setAccountRoleResourceComponentServiceObjects(
			_accountRoleResourceComponentServiceObjects);
		Mutation.setOrganizationResourceComponentServiceObjects(
			_organizationResourceComponentServiceObjects);
		Mutation.setRoleResourceComponentServiceObjects(
			_roleResourceComponentServiceObjects);
		Mutation.setSubscriptionResourceComponentServiceObjects(
			_subscriptionResourceComponentServiceObjects);
		Mutation.setUserAccountResourceComponentServiceObjects(
			_userAccountResourceComponentServiceObjects);
		Mutation.setUserGroupResourceComponentServiceObjects(
			_userGroupResourceComponentServiceObjects);

		Query.setAccountResourceComponentServiceObjects(
			_accountResourceComponentServiceObjects);
		Query.setAccountRoleResourceComponentServiceObjects(
			_accountRoleResourceComponentServiceObjects);
		Query.setEmailAddressResourceComponentServiceObjects(
			_emailAddressResourceComponentServiceObjects);
		Query.setOrganizationResourceComponentServiceObjects(
			_organizationResourceComponentServiceObjects);
		Query.setPhoneResourceComponentServiceObjects(
			_phoneResourceComponentServiceObjects);
		Query.setPostalAddressResourceComponentServiceObjects(
			_postalAddressResourceComponentServiceObjects);
		Query.setRoleResourceComponentServiceObjects(
			_roleResourceComponentServiceObjects);
		Query.setSegmentResourceComponentServiceObjects(
			_segmentResourceComponentServiceObjects);
		Query.setSegmentUserResourceComponentServiceObjects(
			_segmentUserResourceComponentServiceObjects);
		Query.setSiteResourceComponentServiceObjects(
			_siteResourceComponentServiceObjects);
		Query.setSubscriptionResourceComponentServiceObjects(
			_subscriptionResourceComponentServiceObjects);
		Query.setUserAccountResourceComponentServiceObjects(
			_userAccountResourceComponentServiceObjects);
		Query.setUserGroupResourceComponentServiceObjects(
			_userGroupResourceComponentServiceObjects);
		Query.setWebUrlResourceComponentServiceObjects(
			_webUrlResourceComponentServiceObjects);
	}

	public String getApplicationName() {
		return "Liferay.Headless.Admin.User";
	}

	@Override
	public Mutation getMutation() {
		return new Mutation();
	}

	@Override
	public String getPath() {
		return "/headless-admin-user-graphql/v1_0";
	}

	@Override
	public Query getQuery() {
		return new Query();
	}

	public ObjectValuePair<Class<?>, String> getResourceMethodObjectValuePair(
		String methodName, boolean mutation) {

		if (mutation) {
			return _resourceMethodObjectValuePairs.get(
				"mutation#" + methodName);
		}

		return _resourceMethodObjectValuePairs.get("query#" + methodName);
	}

	private static final Map<String, ObjectValuePair<Class<?>, String>>
		_resourceMethodObjectValuePairs =
			new HashMap<String, ObjectValuePair<Class<?>, String>>() {
				{
					put(
						"mutation#createAccount",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "postAccount"));
					put(
						"mutation#createAccountBatch",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "postAccountBatch"));
					put(
						"mutation#deleteAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"deleteAccountByExternalReferenceCode"));
					put(
						"mutation#patchAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"patchAccountByExternalReferenceCode"));
					put(
						"mutation#updateAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"putAccountByExternalReferenceCode"));
					put(
						"mutation#deleteAccount",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "deleteAccount"));
					put(
						"mutation#deleteAccountBatch",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "deleteAccountBatch"));
					put(
						"mutation#patchAccount",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "patchAccount"));
					put(
						"mutation#updateAccount",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "putAccount"));
					put(
						"mutation#updateAccountBatch",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "putAccountBatch"));
					put(
						"mutation#patchOrganizationMoveAccounts",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"patchOrganizationMoveAccounts"));
					put(
						"mutation#patchOrganizationMoveAccountsByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"patchOrganizationMoveAccountsByExternalReferenceCode"));
					put(
						"mutation#deleteOrganizationAccounts",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"deleteOrganizationAccounts"));
					put(
						"mutation#createOrganizationAccounts",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"postOrganizationAccounts"));
					put(
						"mutation#deleteOrganizationAccountsByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"deleteOrganizationAccountsByExternalReferenceCode"));
					put(
						"mutation#createOrganizationAccountsByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"postOrganizationAccountsByExternalReferenceCode"));
					put(
						"mutation#deleteAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"deleteAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCode"));
					put(
						"mutation#createAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"postAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCode"));
					put(
						"mutation#createAccountAccountRoleByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"postAccountAccountRoleByExternalReferenceCode"));
					put(
						"mutation#deleteAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"deleteAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress"));
					put(
						"mutation#createAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"postAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress"));
					put(
						"mutation#createAccountAccountRole",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"postAccountAccountRole"));
					put(
						"mutation#createAccountAccountRoleBatch",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"postAccountAccountRoleBatch"));
					put(
						"mutation#deleteAccountAccountRoleUserAccountAssociation",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"deleteAccountAccountRoleUserAccountAssociation"));
					put(
						"mutation#createAccountAccountRoleUserAccountAssociation",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"postAccountAccountRoleUserAccountAssociation"));
					put(
						"mutation#deleteAccountByExternalReferenceCodeOrganization",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"deleteAccountByExternalReferenceCodeOrganization"));
					put(
						"mutation#createAccountByExternalReferenceCodeOrganization",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"postAccountByExternalReferenceCodeOrganization"));
					put(
						"mutation#deleteAccountOrganization",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"deleteAccountOrganization"));
					put(
						"mutation#createAccountOrganization",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"postAccountOrganization"));
					put(
						"mutation#createOrganization",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"postOrganization"));
					put(
						"mutation#createOrganizationBatch",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"postOrganizationBatch"));
					put(
						"mutation#deleteOrganizationByExternalReferenceCode",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"deleteOrganizationByExternalReferenceCode"));
					put(
						"mutation#patchOrganizationByExternalReferenceCode",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"patchOrganizationByExternalReferenceCode"));
					put(
						"mutation#updateOrganizationByExternalReferenceCode",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"putOrganizationByExternalReferenceCode"));
					put(
						"mutation#deleteOrganization",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"deleteOrganization"));
					put(
						"mutation#deleteOrganizationBatch",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"deleteOrganizationBatch"));
					put(
						"mutation#patchOrganization",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"patchOrganization"));
					put(
						"mutation#updateOrganization",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class, "putOrganization"));
					put(
						"mutation#updateOrganizationBatch",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"putOrganizationBatch"));
					put(
						"mutation#deleteUserAccountsByEmailAddress",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"deleteUserAccountsByEmailAddress"));
					put(
						"mutation#createUserAccountsByEmailAddress",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"postUserAccountsByEmailAddress"));
					put(
						"mutation#deleteUserAccountByEmailAddress",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"deleteUserAccountByEmailAddress"));
					put(
						"mutation#createUserAccountByEmailAddress",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"postUserAccountByEmailAddress"));
					put(
						"mutation#deleteRoleUserAccountAssociation",
						new ObjectValuePair<>(
							RoleResourceImpl.class,
							"deleteRoleUserAccountAssociation"));
					put(
						"mutation#createRoleUserAccountAssociation",
						new ObjectValuePair<>(
							RoleResourceImpl.class,
							"postRoleUserAccountAssociation"));
					put(
						"mutation#deleteOrganizationRoleUserAccountAssociation",
						new ObjectValuePair<>(
							RoleResourceImpl.class,
							"deleteOrganizationRoleUserAccountAssociation"));
					put(
						"mutation#createOrganizationRoleUserAccountAssociation",
						new ObjectValuePair<>(
							RoleResourceImpl.class,
							"postOrganizationRoleUserAccountAssociation"));
					put(
						"mutation#deleteSiteRoleUserAccountAssociation",
						new ObjectValuePair<>(
							RoleResourceImpl.class,
							"deleteSiteRoleUserAccountAssociation"));
					put(
						"mutation#createSiteRoleUserAccountAssociation",
						new ObjectValuePair<>(
							RoleResourceImpl.class,
							"postSiteRoleUserAccountAssociation"));
					put(
						"mutation#deleteMyUserAccountSubscription",
						new ObjectValuePair<>(
							SubscriptionResourceImpl.class,
							"deleteMyUserAccountSubscription"));
					put(
						"mutation#deleteAccountByExternalReferenceCodeUserAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"deleteAccountByExternalReferenceCodeUserAccountByExternalReferenceCode"));
					put(
						"mutation#createAccountByExternalReferenceCodeUserAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"postAccountByExternalReferenceCodeUserAccountByExternalReferenceCode"));
					put(
						"mutation#createAccountUserAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"postAccountUserAccountByExternalReferenceCode"));
					put(
						"mutation#deleteAccountUserAccountsByExternalReferenceCodeByEmailAddress",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"deleteAccountUserAccountsByExternalReferenceCodeByEmailAddress"));
					put(
						"mutation#createAccountUserAccountsByExternalReferenceCodeByEmailAddress",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"postAccountUserAccountsByExternalReferenceCodeByEmailAddress"));
					put(
						"mutation#deleteAccountUserAccountByExternalReferenceCodeByEmailAddress",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"deleteAccountUserAccountByExternalReferenceCodeByEmailAddress"));
					put(
						"mutation#createAccountUserAccountByExternalReferenceCodeByEmailAddress",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"postAccountUserAccountByExternalReferenceCodeByEmailAddress"));
					put(
						"mutation#createAccountUserAccount",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"postAccountUserAccount"));
					put(
						"mutation#createAccountUserAccountBatch",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"postAccountUserAccountBatch"));
					put(
						"mutation#deleteAccountUserAccountsByEmailAddress",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"deleteAccountUserAccountsByEmailAddress"));
					put(
						"mutation#createAccountUserAccountsByEmailAddress",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"postAccountUserAccountsByEmailAddress"));
					put(
						"mutation#deleteAccountUserAccountByEmailAddress",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"deleteAccountUserAccountByEmailAddress"));
					put(
						"mutation#createAccountUserAccountByEmailAddress",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"postAccountUserAccountByEmailAddress"));
					put(
						"mutation#createUserAccount",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class, "postUserAccount"));
					put(
						"mutation#createUserAccountBatch",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"postUserAccountBatch"));
					put(
						"mutation#deleteUserAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"deleteUserAccountByExternalReferenceCode"));
					put(
						"mutation#updateUserAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"putUserAccountByExternalReferenceCode"));
					put(
						"mutation#deleteUserAccount",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"deleteUserAccount"));
					put(
						"mutation#deleteUserAccountBatch",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"deleteUserAccountBatch"));
					put(
						"mutation#patchUserAccount",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class, "patchUserAccount"));
					put(
						"mutation#updateUserAccount",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class, "putUserAccount"));
					put(
						"mutation#updateUserAccountBatch",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"putUserAccountBatch"));
					put(
						"mutation#createUserGroup",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class, "postUserGroup"));
					put(
						"mutation#createUserGroupBatch",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class, "postUserGroupBatch"));
					put(
						"mutation#deleteUserGroupByExternalReferenceCode",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class,
							"deleteUserGroupByExternalReferenceCode"));
					put(
						"mutation#patchUserGroupByExternalReferenceCode",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class,
							"patchUserGroupByExternalReferenceCode"));
					put(
						"mutation#updateUserGroupByExternalReferenceCode",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class,
							"putUserGroupByExternalReferenceCode"));
					put(
						"mutation#deleteUserGroup",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class, "deleteUserGroup"));
					put(
						"mutation#deleteUserGroupBatch",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class,
							"deleteUserGroupBatch"));
					put(
						"mutation#patchUserGroup",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class, "patchUserGroup"));
					put(
						"mutation#updateUserGroup",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class, "putUserGroup"));
					put(
						"mutation#updateUserGroupBatch",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class, "putUserGroupBatch"));
					put(
						"mutation#deleteUserGroupUsers",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class,
							"deleteUserGroupUsers"));
					put(
						"mutation#createUserGroupUsers",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class, "postUserGroupUsers"));

					put(
						"query#accounts",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "getAccountsPage"));
					put(
						"query#accountByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"getAccountByExternalReferenceCode"));
					put(
						"query#account",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "getAccount"));
					put(
						"query#organizationAccounts",
						new ObjectValuePair<>(
							AccountResourceImpl.class,
							"getOrganizationAccountsPage"));
					put(
						"query#accountByExternalReferenceCodeUserAccountByExternalReferenceCodeAccountRoles",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"getAccountByExternalReferenceCodeUserAccountByExternalReferenceCodeAccountRolesPage"));
					put(
						"query#accountAccountRolesByExternalReferenceCode",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"getAccountAccountRolesByExternalReferenceCodePage"));
					put(
						"query#accountByExternalReferenceCodeUserAccountByEmailAddressAccountRoles",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"getAccountByExternalReferenceCodeUserAccountByEmailAddressAccountRolesPage"));
					put(
						"query#accountAccountRoles",
						new ObjectValuePair<>(
							AccountRoleResourceImpl.class,
							"getAccountAccountRolesPage"));
					put(
						"query#emailAddress",
						new ObjectValuePair<>(
							EmailAddressResourceImpl.class, "getEmailAddress"));
					put(
						"query#organizationEmailAddresses",
						new ObjectValuePair<>(
							EmailAddressResourceImpl.class,
							"getOrganizationEmailAddressesPage"));
					put(
						"query#userAccountEmailAddresses",
						new ObjectValuePair<>(
							EmailAddressResourceImpl.class,
							"getUserAccountEmailAddressesPage"));
					put(
						"query#accountByExternalReferenceCodeOrganizations",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"getAccountByExternalReferenceCodeOrganizationsPage"));
					put(
						"query#accountOrganizations",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"getAccountOrganizationsPage"));
					put(
						"query#organizations",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"getOrganizationsPage"));
					put(
						"query#organizationByExternalReferenceCode",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"getOrganizationByExternalReferenceCode"));
					put(
						"query#organization",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class, "getOrganization"));
					put(
						"query#organizationChildOrganizations",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"getOrganizationChildOrganizationsPage"));
					put(
						"query#organizationOrganizations",
						new ObjectValuePair<>(
							OrganizationResourceImpl.class,
							"getOrganizationOrganizationsPage"));
					put(
						"query#organizationPhones",
						new ObjectValuePair<>(
							PhoneResourceImpl.class,
							"getOrganizationPhonesPage"));
					put(
						"query#phone",
						new ObjectValuePair<>(
							PhoneResourceImpl.class, "getPhone"));
					put(
						"query#userAccountPhones",
						new ObjectValuePair<>(
							PhoneResourceImpl.class,
							"getUserAccountPhonesPage"));
					put(
						"query#accountPostalAddresses",
						new ObjectValuePair<>(
							PostalAddressResourceImpl.class,
							"getAccountPostalAddressesPage"));
					put(
						"query#organizationPostalAddresses",
						new ObjectValuePair<>(
							PostalAddressResourceImpl.class,
							"getOrganizationPostalAddressesPage"));
					put(
						"query#postalAddress",
						new ObjectValuePair<>(
							PostalAddressResourceImpl.class,
							"getPostalAddress"));
					put(
						"query#userAccountPostalAddresses",
						new ObjectValuePair<>(
							PostalAddressResourceImpl.class,
							"getUserAccountPostalAddressesPage"));
					put(
						"query#roles",
						new ObjectValuePair<>(
							RoleResourceImpl.class, "getRolesPage"));
					put(
						"query#role",
						new ObjectValuePair<>(
							RoleResourceImpl.class, "getRole"));
					put(
						"query#segments",
						new ObjectValuePair<>(
							SegmentResourceImpl.class, "getSiteSegmentsPage"));
					put(
						"query#userAccountSegments",
						new ObjectValuePair<>(
							SegmentResourceImpl.class,
							"getSiteUserAccountSegmentsPage"));
					put(
						"query#segmentUserAccounts",
						new ObjectValuePair<>(
							SegmentUserResourceImpl.class,
							"getSegmentUserAccountsPage"));
					put(
						"query#myUserAccountSites",
						new ObjectValuePair<>(
							SiteResourceImpl.class,
							"getMyUserAccountSitesPage"));
					put(
						"query#byFriendlyUrlPath",
						new ObjectValuePair<>(
							SiteResourceImpl.class,
							"getSiteByFriendlyUrlPath"));
					put(
						"query#site",
						new ObjectValuePair<>(
							SiteResourceImpl.class, "getSite"));
					put(
						"query#myUserAccountSubscriptions",
						new ObjectValuePair<>(
							SubscriptionResourceImpl.class,
							"getMyUserAccountSubscriptionsPage"));
					put(
						"query#myUserAccountSubscription",
						new ObjectValuePair<>(
							SubscriptionResourceImpl.class,
							"getMyUserAccountSubscription"));
					put(
						"query#accountUserAccountsByExternalReferenceCode",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"getAccountUserAccountsByExternalReferenceCodePage"));
					put(
						"query#accountUserAccounts",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"getAccountUserAccountsPage"));
					put(
						"query#myUserAccount",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class, "getMyUserAccount"));
					put(
						"query#organizationUserAccounts",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"getOrganizationUserAccountsPage"));
					put(
						"query#siteUserAccounts",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"getSiteUserAccountsPage"));
					put(
						"query#userAccounts",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"getUserAccountsPage"));
					put(
						"query#userAccountByExternalReferenceCode",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class,
							"getUserAccountByExternalReferenceCode"));
					put(
						"query#userAccount",
						new ObjectValuePair<>(
							UserAccountResourceImpl.class, "getUserAccount"));
					put(
						"query#userUserGroups",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class, "getUserUserGroups"));
					put(
						"query#userGroups",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class, "getUserGroupsPage"));
					put(
						"query#userGroupByExternalReferenceCode",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class,
							"getUserGroupByExternalReferenceCode"));
					put(
						"query#userGroup",
						new ObjectValuePair<>(
							UserGroupResourceImpl.class, "getUserGroup"));
					put(
						"query#organizationWebUrls",
						new ObjectValuePair<>(
							WebUrlResourceImpl.class,
							"getOrganizationWebUrlsPage"));
					put(
						"query#userAccountWebUrls",
						new ObjectValuePair<>(
							WebUrlResourceImpl.class,
							"getUserAccountWebUrlsPage"));
					put(
						"query#webUrl",
						new ObjectValuePair<>(
							WebUrlResourceImpl.class, "getWebUrl"));
				}
			};

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<AccountResource>
		_accountResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<AccountRoleResource>
		_accountRoleResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<OrganizationResource>
		_organizationResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<RoleResource>
		_roleResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SubscriptionResource>
		_subscriptionResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<UserAccountResource>
		_userAccountResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<UserGroupResource>
		_userGroupResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<EmailAddressResource>
		_emailAddressResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<PhoneResource>
		_phoneResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<PostalAddressResource>
		_postalAddressResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SegmentResource>
		_segmentResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SegmentUserResource>
		_segmentUserResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SiteResource>
		_siteResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<WebUrlResource>
		_webUrlResourceComponentServiceObjects;

}