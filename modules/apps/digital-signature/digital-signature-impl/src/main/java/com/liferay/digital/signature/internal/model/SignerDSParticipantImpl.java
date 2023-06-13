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

package com.liferay.digital.signature.internal.model;

import com.liferay.digital.signature.model.DSParticipantRole;
import com.liferay.digital.signature.model.DSParticipantVisitor;
import com.liferay.digital.signature.model.DSSignatureInfo;
import com.liferay.digital.signature.model.SignerDSParticipant;
import com.liferay.digital.signature.model.field.DSField;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public class SignerDSParticipantImpl
	extends BaseDSParticipantImpl implements SignerDSParticipant {

	public SignerDSParticipantImpl(
		String emailAddress, String name, int routingOrder) {

		super(emailAddress, name, routingOrder);

		setDSParticipantRole(DSParticipantRole.SIGNER);
	}

	public void addDSFields(Collection<DSField<?>> dsfields) {
		_dsFields.addAll(dsfields);
	}

	@Override
	public Boolean getAutoNavigation() {
		return _autoNavigation;
	}

	@Override
	public Boolean getDefaultParticipant() {
		return _defaultParticipant;
	}

	@Override
	public Collection<DSField<?>> getDSFields() {
		return Collections.unmodifiableCollection(_dsFields);
	}

	@Override
	public DSSignatureInfo getDSSignatureInfo() {
		return _dsSignatureInfo;
	}

	@Override
	public Boolean getSignInEachLocation() {
		return _signInEachLocation;
	}

	public void setAutoNavigation(Boolean autoNavigation) {
		_autoNavigation = autoNavigation;
	}

	public void setDefaultParticipant(Boolean defaultParticipant) {
		_defaultParticipant = defaultParticipant;
	}

	public void setDSSignatureInfo(DSSignatureInfo dsSignatureInfo) {
		_dsSignatureInfo = dsSignatureInfo;
	}

	public void setSignInEachLocation(Boolean signInEachLocation) {
		_signInEachLocation = signInEachLocation;
	}

	@Override
	public <T> T translate(DSParticipantVisitor<T> dsParticipantVisitor) {
		return dsParticipantVisitor.visit(this);
	}

	private Boolean _autoNavigation;
	private Boolean _defaultParticipant;
	private Set<DSField<?>> _dsFields = new HashSet<>();
	private DSSignatureInfo _dsSignatureInfo;
	private Boolean _signInEachLocation;

}