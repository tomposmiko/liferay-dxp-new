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

package com.liferay.headless.commerce.admin.inventory.internal.graphql.servlet.v1_0;

import com.liferay.headless.commerce.admin.inventory.internal.graphql.mutation.v1_0.Mutation;
import com.liferay.headless.commerce.admin.inventory.internal.graphql.query.v1_0.Query;
import com.liferay.headless.commerce.admin.inventory.internal.resource.v1_0.ChannelResourceImpl;
import com.liferay.headless.commerce.admin.inventory.internal.resource.v1_0.OrderTypeResourceImpl;
import com.liferay.headless.commerce.admin.inventory.internal.resource.v1_0.ReplenishmentItemResourceImpl;
import com.liferay.headless.commerce.admin.inventory.internal.resource.v1_0.WarehouseChannelResourceImpl;
import com.liferay.headless.commerce.admin.inventory.internal.resource.v1_0.WarehouseItemResourceImpl;
import com.liferay.headless.commerce.admin.inventory.internal.resource.v1_0.WarehouseOrderTypeResourceImpl;
import com.liferay.headless.commerce.admin.inventory.internal.resource.v1_0.WarehouseResourceImpl;
import com.liferay.headless.commerce.admin.inventory.resource.v1_0.ChannelResource;
import com.liferay.headless.commerce.admin.inventory.resource.v1_0.OrderTypeResource;
import com.liferay.headless.commerce.admin.inventory.resource.v1_0.ReplenishmentItemResource;
import com.liferay.headless.commerce.admin.inventory.resource.v1_0.WarehouseChannelResource;
import com.liferay.headless.commerce.admin.inventory.resource.v1_0.WarehouseItemResource;
import com.liferay.headless.commerce.admin.inventory.resource.v1_0.WarehouseOrderTypeResource;
import com.liferay.headless.commerce.admin.inventory.resource.v1_0.WarehouseResource;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.vulcan.graphql.servlet.ServletData;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author Alessio Antonio Rendina
 * @generated
 */
@Component(service = ServletData.class)
@Generated("")
public class ServletDataImpl implements ServletData {

	@Activate
	public void activate(BundleContext bundleContext) {
		Mutation.setReplenishmentItemResourceComponentServiceObjects(
			_replenishmentItemResourceComponentServiceObjects);
		Mutation.setWarehouseResourceComponentServiceObjects(
			_warehouseResourceComponentServiceObjects);
		Mutation.setWarehouseChannelResourceComponentServiceObjects(
			_warehouseChannelResourceComponentServiceObjects);
		Mutation.setWarehouseItemResourceComponentServiceObjects(
			_warehouseItemResourceComponentServiceObjects);
		Mutation.setWarehouseOrderTypeResourceComponentServiceObjects(
			_warehouseOrderTypeResourceComponentServiceObjects);

		Query.setChannelResourceComponentServiceObjects(
			_channelResourceComponentServiceObjects);
		Query.setOrderTypeResourceComponentServiceObjects(
			_orderTypeResourceComponentServiceObjects);
		Query.setReplenishmentItemResourceComponentServiceObjects(
			_replenishmentItemResourceComponentServiceObjects);
		Query.setWarehouseResourceComponentServiceObjects(
			_warehouseResourceComponentServiceObjects);
		Query.setWarehouseChannelResourceComponentServiceObjects(
			_warehouseChannelResourceComponentServiceObjects);
		Query.setWarehouseItemResourceComponentServiceObjects(
			_warehouseItemResourceComponentServiceObjects);
		Query.setWarehouseOrderTypeResourceComponentServiceObjects(
			_warehouseOrderTypeResourceComponentServiceObjects);
	}

	public String getApplicationName() {
		return "Liferay.Headless.Commerce.Admin.Inventory";
	}

	@Override
	public Mutation getMutation() {
		return new Mutation();
	}

	@Override
	public String getPath() {
		return "/headless-commerce-admin-inventory-graphql/v1_0";
	}

	@Override
	public Query getQuery() {
		return new Query();
	}

	public ObjectValuePair<Class<?>, String> getResourceMethodObjectValuePair(
		String methodName, boolean mutation) {

		if (mutation) {
			return _resourceMethodObjectValuePairs.get(
				"mutation#" + methodName);
		}

		return _resourceMethodObjectValuePairs.get("query#" + methodName);
	}

	private static final Map<String, ObjectValuePair<Class<?>, String>>
		_resourceMethodObjectValuePairs =
			new HashMap<String, ObjectValuePair<Class<?>, String>>() {
				{
					put(
						"mutation#deleteReplenishmentItemByExternalReferenceCode",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"deleteReplenishmentItemByExternalReferenceCode"));
					put(
						"mutation#patchReplenishmentItemByExternalReferenceCode",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"patchReplenishmentItemByExternalReferenceCode"));
					put(
						"mutation#deleteReplenishmentItem",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"deleteReplenishmentItem"));
					put(
						"mutation#deleteReplenishmentItemBatch",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"deleteReplenishmentItemBatch"));
					put(
						"mutation#patchReplenishmentItem",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"patchReplenishmentItem"));
					put(
						"mutation#createReplenishmentItemsPageExportBatch",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"postReplenishmentItemsPageExportBatch"));
					put(
						"mutation#createReplenishmentItem",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"postReplenishmentItem"));
					put(
						"mutation#createReplenishmentItemBatch",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"postReplenishmentItemBatch"));
					put(
						"mutation#createWarehousesPageExportBatch",
						new ObjectValuePair<>(
							WarehouseResourceImpl.class,
							"postWarehousesPageExportBatch"));
					put(
						"mutation#createWarehouse",
						new ObjectValuePair<>(
							WarehouseResourceImpl.class, "postWarehouse"));
					put(
						"mutation#createWarehouseBatch",
						new ObjectValuePair<>(
							WarehouseResourceImpl.class, "postWarehouseBatch"));
					put(
						"mutation#deleteWarehouseByExternalReferenceCode",
						new ObjectValuePair<>(
							WarehouseResourceImpl.class,
							"deleteWarehouseByExternalReferenceCode"));
					put(
						"mutation#patchWarehouseByExternalReferenceCode",
						new ObjectValuePair<>(
							WarehouseResourceImpl.class,
							"patchWarehouseByExternalReferenceCode"));
					put(
						"mutation#deleteWarehouseId",
						new ObjectValuePair<>(
							WarehouseResourceImpl.class, "deleteWarehouseId"));
					put(
						"mutation#patchWarehouseId",
						new ObjectValuePair<>(
							WarehouseResourceImpl.class, "patchWarehouseId"));
					put(
						"mutation#deleteWarehouseChannel",
						new ObjectValuePair<>(
							WarehouseChannelResourceImpl.class,
							"deleteWarehouseChannel"));
					put(
						"mutation#deleteWarehouseChannelBatch",
						new ObjectValuePair<>(
							WarehouseChannelResourceImpl.class,
							"deleteWarehouseChannelBatch"));
					put(
						"mutation#createWarehouseByExternalReferenceCodeWarehouseChannel",
						new ObjectValuePair<>(
							WarehouseChannelResourceImpl.class,
							"postWarehouseByExternalReferenceCodeWarehouseChannel"));
					put(
						"mutation#createWarehouseIdWarehouseChannel",
						new ObjectValuePair<>(
							WarehouseChannelResourceImpl.class,
							"postWarehouseIdWarehouseChannel"));
					put(
						"mutation#createWarehouseIdWarehouseChannelBatch",
						new ObjectValuePair<>(
							WarehouseChannelResourceImpl.class,
							"postWarehouseIdWarehouseChannelBatch"));
					put(
						"mutation#deleteWarehouseItemByExternalReferenceCode",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"deleteWarehouseItemByExternalReferenceCode"));
					put(
						"mutation#patchWarehouseItemByExternalReferenceCode",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"patchWarehouseItemByExternalReferenceCode"));
					put(
						"mutation#createWarehouseItemByExternalReferenceCode",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"postWarehouseItemByExternalReferenceCode"));
					put(
						"mutation#deleteWarehouseItem",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"deleteWarehouseItem"));
					put(
						"mutation#deleteWarehouseItemBatch",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"deleteWarehouseItemBatch"));
					put(
						"mutation#patchWarehouseItem",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"patchWarehouseItem"));
					put(
						"mutation#createWarehouseByExternalReferenceCodeWarehouseItem",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"postWarehouseByExternalReferenceCodeWarehouseItem"));
					put(
						"mutation#createWarehouseIdWarehouseItem",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"postWarehouseIdWarehouseItem"));
					put(
						"mutation#createWarehouseIdWarehouseItemBatch",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"postWarehouseIdWarehouseItemBatch"));
					put(
						"mutation#deleteWarehouseOrderType",
						new ObjectValuePair<>(
							WarehouseOrderTypeResourceImpl.class,
							"deleteWarehouseOrderType"));
					put(
						"mutation#deleteWarehouseOrderTypeBatch",
						new ObjectValuePair<>(
							WarehouseOrderTypeResourceImpl.class,
							"deleteWarehouseOrderTypeBatch"));
					put(
						"mutation#createWarehouseByExternalReferenceCodeWarehouseOrderType",
						new ObjectValuePair<>(
							WarehouseOrderTypeResourceImpl.class,
							"postWarehouseByExternalReferenceCodeWarehouseOrderType"));
					put(
						"mutation#createWarehouseIdWarehouseOrderType",
						new ObjectValuePair<>(
							WarehouseOrderTypeResourceImpl.class,
							"postWarehouseIdWarehouseOrderType"));
					put(
						"mutation#createWarehouseIdWarehouseOrderTypeBatch",
						new ObjectValuePair<>(
							WarehouseOrderTypeResourceImpl.class,
							"postWarehouseIdWarehouseOrderTypeBatch"));

					put(
						"query#warehouseChannelChannel",
						new ObjectValuePair<>(
							ChannelResourceImpl.class,
							"getWarehouseChannelChannel"));
					put(
						"query#warehouseOrderTypeOrderType",
						new ObjectValuePair<>(
							OrderTypeResourceImpl.class,
							"getWarehouseOrderTypeOrderType"));
					put(
						"query#replenishmentItemByExternalReferenceCode",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"getReplenishmentItemByExternalReferenceCode"));
					put(
						"query#replenishmentItem",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"getReplenishmentItem"));
					put(
						"query#replenishmentItems",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"getReplenishmentItemsPage"));
					put(
						"query#warehouseIdReplenishmentItems",
						new ObjectValuePair<>(
							ReplenishmentItemResourceImpl.class,
							"getWarehouseIdReplenishmentItemsPage"));
					put(
						"query#warehouses",
						new ObjectValuePair<>(
							WarehouseResourceImpl.class, "getWarehousesPage"));
					put(
						"query#warehouseByExternalReferenceCode",
						new ObjectValuePair<>(
							WarehouseResourceImpl.class,
							"getWarehouseByExternalReferenceCode"));
					put(
						"query#warehouseId",
						new ObjectValuePair<>(
							WarehouseResourceImpl.class, "getWarehouseId"));
					put(
						"query#warehouseByExternalReferenceCodeWarehouseChannels",
						new ObjectValuePair<>(
							WarehouseChannelResourceImpl.class,
							"getWarehouseByExternalReferenceCodeWarehouseChannelsPage"));
					put(
						"query#warehouseIdWarehouseChannels",
						new ObjectValuePair<>(
							WarehouseChannelResourceImpl.class,
							"getWarehouseIdWarehouseChannelsPage"));
					put(
						"query#warehouseItemByExternalReferenceCode",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"getWarehouseItemByExternalReferenceCode"));
					put(
						"query#warehouseItemsUpdated",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"getWarehouseItemsUpdatedPage"));
					put(
						"query#warehouseItem",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"getWarehouseItem"));
					put(
						"query#warehouseByExternalReferenceCodeWarehouseItems",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"getWarehouseByExternalReferenceCodeWarehouseItemsPage"));
					put(
						"query#warehouseIdWarehouseItems",
						new ObjectValuePair<>(
							WarehouseItemResourceImpl.class,
							"getWarehouseIdWarehouseItemsPage"));
					put(
						"query#warehouseByExternalReferenceCodeWarehouseOrderTypes",
						new ObjectValuePair<>(
							WarehouseOrderTypeResourceImpl.class,
							"getWarehouseByExternalReferenceCodeWarehouseOrderTypesPage"));
					put(
						"query#warehouseIdWarehouseOrderTypes",
						new ObjectValuePair<>(
							WarehouseOrderTypeResourceImpl.class,
							"getWarehouseIdWarehouseOrderTypesPage"));
				}
			};

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<ReplenishmentItemResource>
		_replenishmentItemResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<WarehouseResource>
		_warehouseResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<WarehouseChannelResource>
		_warehouseChannelResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<WarehouseItemResource>
		_warehouseItemResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<WarehouseOrderTypeResource>
		_warehouseOrderTypeResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<ChannelResource>
		_channelResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<OrderTypeResource>
		_orderTypeResourceComponentServiceObjects;

}