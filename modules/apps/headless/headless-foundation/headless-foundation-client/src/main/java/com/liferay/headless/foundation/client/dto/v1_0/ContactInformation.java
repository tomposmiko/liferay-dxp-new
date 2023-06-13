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

package com.liferay.headless.foundation.client.dto.v1_0;

import com.liferay.headless.foundation.client.function.UnsafeSupplier;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class ContactInformation {

	public Email[] getEmails() {
		return emails;
	}

	public void setEmails(Email[] emails) {
		this.emails = emails;
	}

	public void setEmails(
		UnsafeSupplier<Email[], Exception> emailsUnsafeSupplier) {

		try {
			emails = emailsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Email[] emails;

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public void setFacebook(
		UnsafeSupplier<String, Exception> facebookUnsafeSupplier) {

		try {
			facebook = facebookUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String facebook;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(UnsafeSupplier<Long, Exception> idUnsafeSupplier) {
		try {
			id = idUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long id;

	public String getJabber() {
		return jabber;
	}

	public void setJabber(String jabber) {
		this.jabber = jabber;
	}

	public void setJabber(
		UnsafeSupplier<String, Exception> jabberUnsafeSupplier) {

		try {
			jabber = jabberUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String jabber;

	public PostalAddress[] getPostalAddresses() {
		return postalAddresses;
	}

	public void setPostalAddresses(PostalAddress[] postalAddresses) {
		this.postalAddresses = postalAddresses;
	}

	public void setPostalAddresses(
		UnsafeSupplier<PostalAddress[], Exception>
			postalAddressesUnsafeSupplier) {

		try {
			postalAddresses = postalAddressesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected PostalAddress[] postalAddresses;

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public void setSkype(
		UnsafeSupplier<String, Exception> skypeUnsafeSupplier) {

		try {
			skype = skypeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String skype;

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public void setSms(UnsafeSupplier<String, Exception> smsUnsafeSupplier) {
		try {
			sms = smsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String sms;

	public Phone[] getTelephones() {
		return telephones;
	}

	public void setTelephones(Phone[] telephones) {
		this.telephones = telephones;
	}

	public void setTelephones(
		UnsafeSupplier<Phone[], Exception> telephonesUnsafeSupplier) {

		try {
			telephones = telephonesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Phone[] telephones;

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public void setTwitter(
		UnsafeSupplier<String, Exception> twitterUnsafeSupplier) {

		try {
			twitter = twitterUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String twitter;

	public WebUrl[] getWebUrls() {
		return webUrls;
	}

	public void setWebUrls(WebUrl[] webUrls) {
		this.webUrls = webUrls;
	}

	public void setWebUrls(
		UnsafeSupplier<WebUrl[], Exception> webUrlsUnsafeSupplier) {

		try {
			webUrls = webUrlsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected WebUrl[] webUrls;

}