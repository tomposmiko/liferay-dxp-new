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

package com.liferay.portal.verify;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;

/**
 * @author Alexander Chow
 */
public class VerifyProcessSuite extends VerifyProcess {

	@Override
	protected void doVerify() throws Exception {
		if (GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-157670"))) {
			verify(new VerifyPermission());
			verify(new VerifyGroup());
			verify(new VerifyRole());

			verify(new VerifyAuditedModel());
			verify(new VerifyResourceActions());
			verify(new VerifyResourcePermissions());
			verify(new VerifyUser());
		}
		else {
			verify(new VerifyGroup());

			verify(new VerifyResourcePermissions());
		}
	}

}