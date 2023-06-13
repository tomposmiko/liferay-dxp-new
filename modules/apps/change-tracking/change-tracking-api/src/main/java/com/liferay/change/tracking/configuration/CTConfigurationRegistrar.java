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

package com.liferay.change.tracking.configuration;

/**
 * @author Máté Thurzó
 */
public interface CTConfigurationRegistrar {

	/**
	 * Registers the change tracking configuration object as a component service
	 *
	 * @param ctConfiguration The change tracking configuration object to be
	 *        registered
	 */
	public void register(CTConfiguration<?, ?> ctConfiguration);

	/**
	 * Unregisters the change tracking configuration object as a component
	 * service
	 *
	 * @param ctConfiguration The change tracking configuration object to be
	 *        unregistered
	 */
	public void unregister(CTConfiguration<?, ?> ctConfiguration);

}