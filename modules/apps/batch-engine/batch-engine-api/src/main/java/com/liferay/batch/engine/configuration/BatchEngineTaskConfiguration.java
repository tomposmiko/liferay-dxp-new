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

package com.liferay.batch.engine.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Ivica Cardic
 */
@ExtendedObjectClassDefinition(category = "batch-engine")
@Meta.OCD(
	id = "com.liferay.batch.engine.configuration.BatchEngineTaskConfiguration",
	localization = "content/Language",
	name = "batch-engine-task-configuration-name"
)
public interface BatchEngineTaskConfiguration {

	@Meta.AD(
		deflt = "14",
		description = "completed-tasks-cleaner-scan-interval-description",
		name = "completed-tasks-cleaner-scan-interval", required = false
	)
	public int completedTasksCleanerScanInterval();

	@Meta.AD(
		deflt = "30", description = "orphanage-threshold-description",
		name = "orphanage-threshold", required = false
	)
	public int orphanageThreshold();

	@Meta.AD(
		deflt = "60", description = "orphan-scan-interval-description",
		name = "orphan-scan-interval", required = false
	)
	public int orphanScanInterval();

}