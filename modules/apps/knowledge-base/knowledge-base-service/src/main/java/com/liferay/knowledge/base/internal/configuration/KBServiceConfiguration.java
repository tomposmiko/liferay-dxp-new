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

package com.liferay.knowledge.base.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Alicia García
 */
@ExtendedObjectClassDefinition(
	category = "knowledge-base", generateUI = false,
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.knowledge.base.internal.configuration.KBServiceConfiguration",
	localization = "content/Language",
	name = "knowledge-base-service-configuration-name"
)
public interface KBServiceConfiguration {

	@Meta.AD(deflt = "15", name = "check-interval", required = false)
	public int checkInterval();

	@Meta.AD(
		deflt = "1",
		description = "expiration-date-notification-date-weeks-help",
		name = "expiration-date-notification-date-weeks", required = false
	)
	public int expirationDateNotificationDateWeeks();

}