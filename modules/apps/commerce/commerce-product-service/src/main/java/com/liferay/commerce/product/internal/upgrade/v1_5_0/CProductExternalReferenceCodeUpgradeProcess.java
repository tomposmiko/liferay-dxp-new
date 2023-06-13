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

package com.liferay.commerce.product.internal.upgrade.v1_5_0;

import com.liferay.commerce.product.model.impl.CPDefinitionImpl;
import com.liferay.commerce.product.model.impl.CProductImpl;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeProcessFactory;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * @author Alessio Antonio Rendina
 */
public class CProductExternalReferenceCodeUpgradeProcess
	extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		if (hasColumn(CProductImpl.TABLE_NAME, "externalReferenceCode")) {
			Class<CProductExternalReferenceCodeUpgradeProcess> clazz =
				CProductExternalReferenceCodeUpgradeProcess.class;

			String template = StringUtil.read(
				clazz.getResourceAsStream(
					"dependencies" +
						"/CProductExternalReferenceCodeUpgradeProcess.sql"));

			runSQLTemplateString(template, false);

			alterTableDropColumn(
				CPDefinitionImpl.TABLE_NAME, "externalReferenceCode");
		}
	}

	@Override
	protected UpgradeStep[] getPreUpgradeSteps() {
		return new UpgradeStep[] {
			UpgradeProcessFactory.addColumns(
				"CProduct", "externalReferenceCode VARCHAR(75)")
		};
	}

}