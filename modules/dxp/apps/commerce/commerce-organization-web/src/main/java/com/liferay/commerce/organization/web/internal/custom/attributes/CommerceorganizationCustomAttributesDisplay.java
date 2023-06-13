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

package com.liferay.commerce.organization.web.internal.custom.attributes;

import com.liferay.commerce.organization.constants.CommerceOrganizationPortletKeys;
import com.liferay.expando.kernel.model.BaseCustomAttributesDisplay;
import com.liferay.expando.kernel.model.CustomAttributesDisplay;
import com.liferay.portal.kernel.model.Organization;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false, immediate = true,
	property = "javax.portlet.name=" + CommerceOrganizationPortletKeys.COMMERCE_ORGANIZATION,
	service = CustomAttributesDisplay.class
)
public class CommerceorganizationCustomAttributesDisplay
	extends BaseCustomAttributesDisplay {

	@Override
	public String getClassName() {
		return Organization.class.getName();
	}

	@Override
	public String getIconCssClass() {
		return "tag";
	}

}