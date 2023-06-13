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

package com.liferay.commerce.avalara.connector.engine;

import com.liferay.commerce.tax.model.CommerceTaxMethod;

/**
 * @author Riccardo Alberti
 */
public interface CommerceAvalaraConnectorEngine {

	public void addTaxCategories(long userId) throws Exception;

	public void deleteByAddressEntries(CommerceTaxMethod commerceTaxMethod);

	public void updateByAddressEntries(long groupId) throws Exception;

}