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

package com.liferay.commerce.inventory.service.persistence.impl;

import com.liferay.commerce.inventory.model.CommerceInventoryWarehouseRelTable;
import com.liferay.commerce.inventory.model.impl.CommerceInventoryWarehouseRelImpl;
import com.liferay.commerce.inventory.model.impl.CommerceInventoryWarehouseRelModelImpl;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.model.BaseModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;

/**
 * The arguments resolver class for retrieving value from CommerceInventoryWarehouseRel.
 *
 * @author Luca Pellizzon
 * @generated
 */
@Component(
	property = {
		"class.name=com.liferay.commerce.inventory.model.impl.CommerceInventoryWarehouseRelImpl",
		"table.name=CIWarehouseRel"
	},
	service = ArgumentsResolver.class
)
public class CommerceInventoryWarehouseRelModelArgumentsResolver
	implements ArgumentsResolver {

	@Override
	public Object[] getArguments(
		FinderPath finderPath, BaseModel<?> baseModel, boolean checkColumn,
		boolean original) {

		String[] columnNames = finderPath.getColumnNames();

		if ((columnNames == null) || (columnNames.length == 0)) {
			if (baseModel.isNew()) {
				return new Object[0];
			}

			return null;
		}

		CommerceInventoryWarehouseRelModelImpl
			commerceInventoryWarehouseRelModelImpl =
				(CommerceInventoryWarehouseRelModelImpl)baseModel;

		long columnBitmask =
			commerceInventoryWarehouseRelModelImpl.getColumnBitmask();

		if (!checkColumn || (columnBitmask == 0)) {
			return _getValue(
				commerceInventoryWarehouseRelModelImpl, columnNames, original);
		}

		Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
			finderPath);

		if (finderPathColumnBitmask == null) {
			finderPathColumnBitmask = 0L;

			for (String columnName : columnNames) {
				finderPathColumnBitmask |=
					commerceInventoryWarehouseRelModelImpl.getColumnBitmask(
						columnName);
			}

			if (finderPath.isBaseModelResult() &&
				(CommerceInventoryWarehouseRelPersistenceImpl.
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION ==
						finderPath.getCacheName())) {

				finderPathColumnBitmask |= _ORDER_BY_COLUMNS_BITMASK;
			}

			_finderPathColumnBitmasksCache.put(
				finderPath, finderPathColumnBitmask);
		}

		if ((columnBitmask & finderPathColumnBitmask) != 0) {
			return _getValue(
				commerceInventoryWarehouseRelModelImpl, columnNames, original);
		}

		return null;
	}

	@Override
	public String getClassName() {
		return CommerceInventoryWarehouseRelImpl.class.getName();
	}

	@Override
	public String getTableName() {
		return CommerceInventoryWarehouseRelTable.INSTANCE.getTableName();
	}

	private static Object[] _getValue(
		CommerceInventoryWarehouseRelModelImpl
			commerceInventoryWarehouseRelModelImpl,
		String[] columnNames, boolean original) {

		Object[] arguments = new Object[columnNames.length];

		for (int i = 0; i < arguments.length; i++) {
			String columnName = columnNames[i];

			if (original) {
				arguments[i] =
					commerceInventoryWarehouseRelModelImpl.
						getColumnOriginalValue(columnName);
			}
			else {
				arguments[i] =
					commerceInventoryWarehouseRelModelImpl.getColumnValue(
						columnName);
			}
		}

		return arguments;
	}

	private static final Map<FinderPath, Long> _finderPathColumnBitmasksCache =
		new ConcurrentHashMap<>();

	private static final long _ORDER_BY_COLUMNS_BITMASK;

	static {
		long orderByColumnsBitmask = 0;

		orderByColumnsBitmask |=
			CommerceInventoryWarehouseRelModelImpl.getColumnBitmask(
				"createDate");

		_ORDER_BY_COLUMNS_BITMASK = orderByColumnsBitmask;
	}

}