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

package com.liferay.digital.signature.internal.model.field;

import com.liferay.digital.signature.model.field.FormulaDSField;

/**
 * @author Michael C. Han
 */
public class FormulaDSFieldImpl
	extends UserEntryDSFieldImpl<FormulaDSField> implements FormulaDSField {

	public FormulaDSFieldImpl(
		String documentKey, String fieldKey, Integer pageNumber) {

		super(documentKey, fieldKey, pageNumber);
	}

	@Override
	public String getFormula() {
		return _formula;
	}

	@Override
	public Boolean getRoundDecimalPlaces() {
		return _roundDecimalPlaces;
	}

	@Override
	public Boolean getSenderRequired() {
		return _senderRequired;
	}

	public void setFormula(String formula) {
		_formula = formula;
	}

	public void setRoundDecimalPlaces(Boolean roundDecimalPlaces) {
		_roundDecimalPlaces = roundDecimalPlaces;
	}

	public void setSenderRequired(Boolean senderRequired) {
		_senderRequired = senderRequired;
	}

	private String _formula;
	private Boolean _roundDecimalPlaces;
	private Boolean _senderRequired;

}