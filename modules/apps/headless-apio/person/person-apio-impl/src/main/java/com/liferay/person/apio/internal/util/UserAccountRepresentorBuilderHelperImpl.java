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

package com.liferay.person.apio.internal.util;

import com.liferay.address.apio.architect.identifier.AddressIdentifier;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.email.apio.architect.identifier.EmailIdentifier;
import com.liferay.person.apio.internal.model.UserWrapper;
import com.liferay.phone.apio.architect.identifier.PhoneIdentifier;
import com.liferay.portal.apio.identifier.ClassNameClassPK;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.ContactModel;
import com.liferay.portal.kernel.model.ListType;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ListTypeService;
import com.liferay.role.apio.identifier.RoleIdentifier;
import com.liferay.web.url.apio.architect.identifier.WebUrlIdentifier;

import java.util.Date;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo Perez
 */
@Component(
	immediate = true, service = UserAccountRepresentorBuilderHelper.class
)
public class UserAccountRepresentorBuilderHelperImpl
	implements UserAccountRepresentorBuilderHelper {

	@Override
	public Representor.FirstStep<UserWrapper> buildUserWrapperFirstStep(
		Representor.Builder<UserWrapper, Long> builder) {

		return builder.types(
			"Liferay:UserAccount", "Person"
		).identifier(
			User::getUserId
		).addDate(
			"birthDate", UserAccountRepresentorBuilderHelperImpl::_getBirthday
		).addLocalizedStringByLocale(
			"honorificPrefix", _getContactField(Contact::getPrefixId)
		).addLocalizedStringByLocale(
			"honorificSuffix", _getContactField(Contact::getSuffixId)
		).addNested(
			"contactInformation", Function.identity(),
			contactBuilder -> contactBuilder.types(
				"ContactInformation"
			).addRelatedCollection(
				"address", AddressIdentifier.class,
				this::_createClassNameAndClassPK
			).addRelatedCollection(
				"email", EmailIdentifier.class, this::_createClassNameAndClassPK
			).addRelatedCollection(
				"telephone", PhoneIdentifier.class,
				this::_createClassNameAndClassPK
			).addRelatedCollection(
				"webUrl", WebUrlIdentifier.class,
				this::_createClassNameAndClassPK
			).addString(
				"facebook",
				userWrapper -> _getContactInfo(
					userWrapper, ContactModel::getFacebookSn)
			).addString(
				"jabber",
				userWrapper -> _getContactInfo(
					userWrapper, ContactModel::getJabberSn)
			).addString(
				"skype",
				userWrapper -> _getContactInfo(
					userWrapper, ContactModel::getSkypeSn)
			).addString(
				"sms",
				userWrapper -> _getContactInfo(
					userWrapper, ContactModel::getSmsSn)
			).addString(
				"twitter",
				userWrapper -> _getContactInfo(
					userWrapper, ContactModel::getTwitterSn)
			).build()
		).addRelatedCollection(
			"roles", RoleIdentifier.class
		).addRelativeURL(
			"image", UserWrapper::getPortraitURL
		).addString(
			"additionalName", User::getMiddleName
		).addString(
			"alternateName", User::getScreenName
		).addString(
			"dashboardURL", UserWrapper::getDashboardURL
		).addString(
			"email", User::getEmailAddress
		).addString(
			"familyName", User::getLastName
		).addString(
			"gender", UserAccountRepresentorBuilderHelperImpl::_getGender
		).addString(
			"givenName", User::getFirstName
		).addString(
			"jobTitle", User::getJobTitle
		).addString(
			"name", User::getFullName
		).addString(
			"profileURL", UserWrapper::getProfileURL
		);
	}

	private static Date _getBirthday(User user) {
		return Try.fromFallible(
			user::getBirthday
		).orElse(
			null
		);
	}

	private static String _getGender(User user) {
		return Try.fromFallible(
			user::isMale
		).map(
			male -> male ? "male" : "female"
		).orElse(
			null
		);
	}

	private ClassNameClassPK _createClassNameAndClassPK(
		UserWrapper userWrapper) {

		return ClassNameClassPK.create(
			User.class.getName(), userWrapper.getUserId());
	}

	private BiFunction<UserWrapper, Locale, String> _getContactField(
		Function<Contact, Long> function) {

		return (user, locale) -> Try.fromFallible(
			user::getContact
		).map(
			function::apply
		).map(
			_listTypeService::getListType
		).map(
			ListType::getName
		).map(
			name -> LanguageUtil.get(locale, name)
		).orElse(
			null
		);
	}

	private String _getContactInfo(
		UserWrapper userWrapper, Function<Contact, String> function) {

		return Try.fromFallible(
			() -> function.apply(userWrapper.getContact())
		).orElse(
			null
		);
	}

	@Reference
	private ListTypeService _listTypeService;

}