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

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.headless.foundation.dto.v1_0.Email;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.EmailAddress;
import com.liferay.portal.kernel.model.ListType;
import com.liferay.portal.kernel.model.ListTypeConstants;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.EmailAddressLocalServiceUtil;
import com.liferay.portal.kernel.service.ListTypeServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author Javier Gamarra
 */
@RunWith(Arquillian.class)
public class EmailResourceTest extends BaseEmailResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_organization = OrganizationTestUtil.addOrganization();
		_user = UserTestUtil.addGroupAdminUser(testGroup);
	}

	@Override
	protected void assertValid(Email email) {
		boolean valid = false;

		if ((email.getEmail() != null) && (email.getId() != null) &&
			(email.getPrimary() != null)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	@Override
	protected boolean equals(Email email1, Email email2) {
		if (Objects.equals(email1.getEmail(), email2.getEmail()) &&
			Objects.equals(email1.getPrimary(), email2.getPrimary())) {

			return true;
		}

		return false;
	}

	@Override
	protected Email randomEmail() {
		return new Email() {
			{
				email = RandomTestUtil.randomString() + "@liferay.com";
				primary = RandomTestUtil.randomBoolean();
			}
		};
	}

	@Override
	protected Email testGetEmail_addEmail() throws Exception {
		Email email = randomEmail();

		return _addEmailAddress(
			email, Contact.class.getName(), _user.getContactId(),
			ListTypeConstants.CONTACT_EMAIL_ADDRESS);
	}

	@Override
	protected Email testGetOrganizationEmailsPage_addEmail(
			Long organizationId, Email email)
		throws Exception {

		return _addEmailAddress(
			email, _organization.getModelClassName(),
			_organization.getOrganizationId(),
			ListTypeConstants.ORGANIZATION_EMAIL_ADDRESS);
	}

	@Override
	protected Long testGetOrganizationEmailsPage_getOrganizationId()
		throws Exception {

		return _organization.getOrganizationId();
	}

	@Override
	protected Email testGetUserAccountEmailsPage_addEmail(
			Long userAccountId, Email email)
		throws Exception {

		return _addEmailAddress(
			email, Contact.class.getName(), _user.getContactId(),
			ListTypeConstants.CONTACT_EMAIL_ADDRESS);
	}

	@Override
	protected Long testGetUserAccountEmailsPage_getUserAccountId()
		throws Exception {

		return _user.getUserId();
	}

	private Email _addEmailAddress(
			Email email, String className, long classPK, String listTypeId)
		throws PortalException {

		return _toEmail(
			EmailAddressLocalServiceUtil.addEmailAddress(
				_user.getUserId(), className, classPK, email.getEmail(),
				_getListTypeId(listTypeId), email.getPrimary(),
				new ServiceContext()));
	}

	private long _getListTypeId(String listTypeId) {
		List<ListType> listTypes = ListTypeServiceUtil.getListTypes(listTypeId);

		ListType listType = listTypes.get(0);

		return listType.getListTypeId();
	}

	private Email _toEmail(EmailAddress emailAddress) {
		return new Email() {
			{
				email = emailAddress.getAddress();
				id = emailAddress.getEmailAddressId();
				primary = emailAddress.isPrimary();
			}
		};
	}

	private Organization _organization;
	private User _user;

}