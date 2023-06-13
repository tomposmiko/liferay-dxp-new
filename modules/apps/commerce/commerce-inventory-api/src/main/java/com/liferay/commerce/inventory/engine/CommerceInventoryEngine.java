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

package com.liferay.commerce.inventory.engine;

import com.liferay.portal.kernel.exception.PortalException;

import java.util.Map;

/**
 * @author Luca Pellizzon
 * @author Alessio Antonio Rendina
 * @author Ivica Cardic
 */
public interface CommerceInventoryEngine {

	public void consumeQuantity(
			long userId, long commerceCatalogGroupId,
			long commerceInventoryWarehouseId, String sku, int quantity,
			long bookedQuantityId, Map<String, String> context)
		throws PortalException;

	public void decreaseStockQuantity(
			long userId, long commerceCatalogGroupId,
			long commerceInventoryWarehouseId, String sku, int quantity)
		throws PortalException;

	public String getAvailabilityStatus(
		long companyId, long commerceCatalogGroupId,
		long commerceChannelGroupId, int minStockQuantity, String sku);

	public int getStockQuantity(
			long companyId, long commerceCatalogGroupId,
			long commerceChannelGroupId, String sku)
		throws PortalException;

	public int getStockQuantity(
			long companyId, long commerceCatalogGroupId, String sku)
		throws PortalException;

	public boolean hasStockQuantity(
		long companyId, long commerceCatalogGroupId, String sku, int quantity);

	public void increaseStockQuantity(
			long userId, long commerceCatalogGroupId,
			long commerceInventoryWarehouseId, String sku, int quantity)
		throws PortalException;

}