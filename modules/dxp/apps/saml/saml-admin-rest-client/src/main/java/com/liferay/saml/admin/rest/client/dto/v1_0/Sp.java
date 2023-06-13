/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.saml.admin.rest.client.dto.v1_0;

import com.liferay.saml.admin.rest.client.function.UnsafeSupplier;
import com.liferay.saml.admin.rest.client.serdes.v1_0.SpSerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Stian Sigvartsen
 * @generated
 */
@Generated("")
public class Sp implements Cloneable, Serializable {

	public static Sp toDTO(String json) {
		return SpSerDes.toDTO(json);
	}

	public Boolean getAllowShowingTheLoginPortlet() {
		return allowShowingTheLoginPortlet;
	}

	public void setAllowShowingTheLoginPortlet(
		Boolean allowShowingTheLoginPortlet) {

		this.allowShowingTheLoginPortlet = allowShowingTheLoginPortlet;
	}

	public void setAllowShowingTheLoginPortlet(
		UnsafeSupplier<Boolean, Exception>
			allowShowingTheLoginPortletUnsafeSupplier) {

		try {
			allowShowingTheLoginPortlet =
				allowShowingTheLoginPortletUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean allowShowingTheLoginPortlet;

	public Boolean getAssertionSignatureRequired() {
		return assertionSignatureRequired;
	}

	public void setAssertionSignatureRequired(
		Boolean assertionSignatureRequired) {

		this.assertionSignatureRequired = assertionSignatureRequired;
	}

	public void setAssertionSignatureRequired(
		UnsafeSupplier<Boolean, Exception>
			assertionSignatureRequiredUnsafeSupplier) {

		try {
			assertionSignatureRequired =
				assertionSignatureRequiredUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean assertionSignatureRequired;

	public Long getClockSkew() {
		return clockSkew;
	}

	public void setClockSkew(Long clockSkew) {
		this.clockSkew = clockSkew;
	}

	public void setClockSkew(
		UnsafeSupplier<Long, Exception> clockSkewUnsafeSupplier) {

		try {
			clockSkew = clockSkewUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long clockSkew;

	public String getKeyStoreEncryptionCredentialPassword() {
		return keyStoreEncryptionCredentialPassword;
	}

	public void setKeyStoreEncryptionCredentialPassword(
		String keyStoreEncryptionCredentialPassword) {

		this.keyStoreEncryptionCredentialPassword =
			keyStoreEncryptionCredentialPassword;
	}

	public void setKeyStoreEncryptionCredentialPassword(
		UnsafeSupplier<String, Exception>
			keyStoreEncryptionCredentialPasswordUnsafeSupplier) {

		try {
			keyStoreEncryptionCredentialPassword =
				keyStoreEncryptionCredentialPasswordUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String keyStoreEncryptionCredentialPassword;

	public Boolean getLdapImportEnabled() {
		return ldapImportEnabled;
	}

	public void setLdapImportEnabled(Boolean ldapImportEnabled) {
		this.ldapImportEnabled = ldapImportEnabled;
	}

	public void setLdapImportEnabled(
		UnsafeSupplier<Boolean, Exception> ldapImportEnabledUnsafeSupplier) {

		try {
			ldapImportEnabled = ldapImportEnabledUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean ldapImportEnabled;

	public Boolean getSignAuthnRequest() {
		return signAuthnRequest;
	}

	public void setSignAuthnRequest(Boolean signAuthnRequest) {
		this.signAuthnRequest = signAuthnRequest;
	}

	public void setSignAuthnRequest(
		UnsafeSupplier<Boolean, Exception> signAuthnRequestUnsafeSupplier) {

		try {
			signAuthnRequest = signAuthnRequestUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean signAuthnRequest;

	@Override
	public Sp clone() throws CloneNotSupportedException {
		return (Sp)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Sp)) {
			return false;
		}

		Sp sp = (Sp)object;

		return Objects.equals(toString(), sp.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return SpSerDes.toJSON(this);
	}

}