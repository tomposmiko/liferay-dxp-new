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

package com.liferay.client.extension.type.internal.validator;

import com.liferay.client.extension.constants.ClientExtensionEntryConstants;
import com.liferay.client.extension.exception.ClientExtensionEntryTypeSettingsException;
import com.liferay.client.extension.type.CETGlobalCSS;
import com.liferay.client.extension.type.internal.CETGlobalCSSImpl;
import com.liferay.client.extension.type.validator.CETValidator;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Iván Zaera Avellón
 */
@Component(
	property = "type=" + ClientExtensionEntryConstants.TYPE_GLOBAL_CSS,
	service = CETValidator.class
)
public class CETGlobalCSSValidator implements CETValidator {

	@Override
	public void validate(
			UnicodeProperties newTypeSettingsUnicodeProperties,
			UnicodeProperties oldTypeSettingsUnicodeProperties)
		throws PortalException {

		CETGlobalCSS newCETGlobalCSSImpl = new CETGlobalCSSImpl(
			newTypeSettingsUnicodeProperties);

		if (!Validator.isUrl(newCETGlobalCSSImpl.getURL())) {
			throw new ClientExtensionEntryTypeSettingsException(
				"please-enter-a-valid-url");
		}
	}

}