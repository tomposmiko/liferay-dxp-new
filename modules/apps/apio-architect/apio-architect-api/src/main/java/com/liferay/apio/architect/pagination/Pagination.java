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

package com.liferay.apio.architect.pagination;

import aQute.bnd.annotation.ProviderType;

/**
 * Defines pagination for a collection endpoint. An instance of this class is
 * handed to resources that handle pagination parameters.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 * @see    Page
 */
@ProviderType
public interface Pagination {

	/**
	 * Returns the position of the requested page's last element.
	 *
	 * @return the position of the requested page's last element
	 */
	public int getEndPosition();

	/**
	 * Returns the selected number of items per page.
	 *
	 * @return the selected number of items per page
	 */
	public int getItemsPerPage();

	/**
	 * Returns the requested page's number.
	 *
	 * @return the requested page's number
	 */
	public int getPageNumber();

	/**
	 * Returns the position of the requested page's first element.
	 *
	 * @return the position of the requested page's first element
	 */
	public int getStartPosition();

}