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

package com.liferay.object.action.executor;

import com.liferay.object.exception.ObjectActionExecutorKeyException;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.UnicodeProperties;

/**
 * @author Marco Leo
 * @author Brian Wing Shun Chan
 */
public interface ObjectActionExecutor {

	public void execute(
			long companyId, UnicodeProperties parametersUnicodeProperties,
			JSONObject payloadJSONObject, long userId)
		throws Exception;

	public String getKey();

	public default boolean isAllowedCompany(long companyId) {
		return true;
	}

	public default boolean isAllowedObjectDefinition(
		String objectDefinitionName) {

		return true;
	}

	public default void validate(long companyId, String objectDefinitionName)
		throws PortalException {

		if (!isAllowedCompany(companyId)) {
			throw new ObjectActionExecutorKeyException(
				StringBundler.concat(
					"The object action executor key ", getKey(),
					" is not allowed for company ", companyId));
		}

		if (!isAllowedObjectDefinition(objectDefinitionName)) {
			throw new ObjectActionExecutorKeyException(
				StringBundler.concat(
					"The object action executor key ", getKey(),
					" is not allowed for object definition ",
					objectDefinitionName));
		}
	}

}