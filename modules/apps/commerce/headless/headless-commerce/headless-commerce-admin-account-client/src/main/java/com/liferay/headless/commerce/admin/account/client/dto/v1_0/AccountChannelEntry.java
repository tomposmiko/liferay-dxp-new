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

package com.liferay.headless.commerce.admin.account.client.dto.v1_0;

import com.liferay.headless.commerce.admin.account.client.function.UnsafeSupplier;
import com.liferay.headless.commerce.admin.account.client.serdes.v1_0.AccountChannelEntrySerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
public class AccountChannelEntry implements Cloneable, Serializable {

	public static AccountChannelEntry toDTO(String json) {
		return AccountChannelEntrySerDes.toDTO(json);
	}

	public String getAccountExternalReferenceCode() {
		return accountExternalReferenceCode;
	}

	public void setAccountExternalReferenceCode(
		String accountExternalReferenceCode) {

		this.accountExternalReferenceCode = accountExternalReferenceCode;
	}

	public void setAccountExternalReferenceCode(
		UnsafeSupplier<String, Exception>
			accountExternalReferenceCodeUnsafeSupplier) {

		try {
			accountExternalReferenceCode =
				accountExternalReferenceCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String accountExternalReferenceCode;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public void setAccountId(
		UnsafeSupplier<Long, Exception> accountIdUnsafeSupplier) {

		try {
			accountId = accountIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long accountId;

	public String getChannelExternalReferenceCode() {
		return channelExternalReferenceCode;
	}

	public void setChannelExternalReferenceCode(
		String channelExternalReferenceCode) {

		this.channelExternalReferenceCode = channelExternalReferenceCode;
	}

	public void setChannelExternalReferenceCode(
		UnsafeSupplier<String, Exception>
			channelExternalReferenceCodeUnsafeSupplier) {

		try {
			channelExternalReferenceCode =
				channelExternalReferenceCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String channelExternalReferenceCode;

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public void setChannelId(
		UnsafeSupplier<Long, Exception> channelIdUnsafeSupplier) {

		try {
			channelId = channelIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long channelId;

	public String getClassExternalReferenceCode() {
		return classExternalReferenceCode;
	}

	public void setClassExternalReferenceCode(
		String classExternalReferenceCode) {

		this.classExternalReferenceCode = classExternalReferenceCode;
	}

	public void setClassExternalReferenceCode(
		UnsafeSupplier<String, Exception>
			classExternalReferenceCodeUnsafeSupplier) {

		try {
			classExternalReferenceCode =
				classExternalReferenceCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String classExternalReferenceCode;

	public Long getClassPK() {
		return classPK;
	}

	public void setClassPK(Long classPK) {
		this.classPK = classPK;
	}

	public void setClassPK(
		UnsafeSupplier<Long, Exception> classPKUnsafeSupplier) {

		try {
			classPK = classPKUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long classPK;

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

	public Boolean getOverrideEligibility() {
		return overrideEligibility;
	}

	public void setOverrideEligibility(Boolean overrideEligibility) {
		this.overrideEligibility = overrideEligibility;
	}

	public void setOverrideEligibility(
		UnsafeSupplier<Boolean, Exception> overrideEligibilityUnsafeSupplier) {

		try {
			overrideEligibility = overrideEligibilityUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean overrideEligibility;

	public Double getPriority() {
		return priority;
	}

	public void setPriority(Double priority) {
		this.priority = priority;
	}

	public void setPriority(
		UnsafeSupplier<Double, Exception> priorityUnsafeSupplier) {

		try {
			priority = priorityUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Double priority;

	@Override
	public AccountChannelEntry clone() throws CloneNotSupportedException {
		return (AccountChannelEntry)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AccountChannelEntry)) {
			return false;
		}

		AccountChannelEntry accountChannelEntry = (AccountChannelEntry)object;

		return Objects.equals(toString(), accountChannelEntry.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return AccountChannelEntrySerDes.toJSON(this);
	}

}