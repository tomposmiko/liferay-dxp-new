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

package com.liferay.data.cleanup.internal.upgrade;

import com.liferay.change.tracking.model.CTEntryTable;
import com.liferay.change.tracking.store.model.CTSContent;
import com.liferay.change.tracking.store.model.CTSContentTable;
import com.liferay.change.tracking.store.service.CTSContentLocalService;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.Portal;

import java.util.List;

/**
 * @author David Truong
 */
public class UpgradePublishedCTSContentData extends UpgradeProcess {

	public UpgradePublishedCTSContentData(
		CTSContentLocalService ctsContentLocalService, Portal portal) {

		_ctsContentLocalService = ctsContentLocalService;
		_portal = portal;
	}

	@Override
	protected void doUpgrade() throws Exception {
		List<Long> ctsContentIds = _ctsContentLocalService.dslQuery(
			DSLQueryFactoryUtil.select(
				CTSContentTable.INSTANCE.ctsContentId
			).from(
				CTSContentTable.INSTANCE
			).where(
				CTSContentTable.INSTANCE.ctsContentId.notIn(
					DSLQueryFactoryUtil.select(
						CTEntryTable.INSTANCE.modelClassPK
					).from(
						CTEntryTable.INSTANCE
					).where(
						CTEntryTable.INSTANCE.modelClassNameId.eq(
							_portal.getClassNameId(CTSContent.class.getName()))
					))
			));

		for (long ctsContentId : ctsContentIds) {
			_ctsContentLocalService.deleteCTSContent(ctsContentId);
		}
	}

	private final CTSContentLocalService _ctsContentLocalService;
	private final Portal _portal;

}