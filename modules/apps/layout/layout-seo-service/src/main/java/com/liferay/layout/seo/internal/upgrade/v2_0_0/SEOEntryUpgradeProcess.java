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

package com.liferay.layout.seo.internal.upgrade.v2_0_0;

import com.liferay.layout.seo.internal.upgrade.v2_0_0.util.LayoutSEOEntryTable;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Cristina González
 */
public class SEOEntryUpgradeProcess extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		alter(
			LayoutSEOEntryTable.class,
			new AlterColumnName("enabled", "canonicalURLEnabled BOOLEAN"),
			new AlterTableAddColumn("openGraphTitleEnabled", "BOOLEAN"),
			new AlterTableAddColumn("openGraphTitle", "STRING null"),
			new AlterTableAddColumn("openGraphDescriptionEnabled", "BOOLEAN"),
			new AlterTableAddColumn("openGraphDescription", "STRING null"),
			new AlterTableAddColumn("openGraphImageFileEntryId", "LONG"));
	}

}