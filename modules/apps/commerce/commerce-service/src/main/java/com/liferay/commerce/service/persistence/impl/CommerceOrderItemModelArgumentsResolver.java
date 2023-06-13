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

package com.liferay.commerce.service.persistence.impl;

import com.liferay.commerce.model.CommerceOrderItemTable;
import com.liferay.commerce.model.impl.CommerceOrderItemImpl;
import com.liferay.commerce.model.impl.CommerceOrderItemModelImpl;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.model.BaseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;

/**
 * The arguments resolver class for retrieving value from CommerceOrderItem.
 *
 * @author Alessio Antonio Rendina
 * @generated
 */
@Component(
	property = {
		"class.name=com.liferay.commerce.model.impl.CommerceOrderItemImpl",
		"table.name=CommerceOrderItem"
	},
	service = ArgumentsResolver.class
)
public class CommerceOrderItemModelArgumentsResolver
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

		CommerceOrderItemModelImpl commerceOrderItemModelImpl =
			(CommerceOrderItemModelImpl)baseModel;

		if (!checkColumn ||
			_hasModifiedColumns(commerceOrderItemModelImpl, columnNames) ||
			_hasModifiedColumns(
				commerceOrderItemModelImpl, _ORDER_BY_COLUMNS)) {

			return _getValue(commerceOrderItemModelImpl, columnNames, original);
		}

		return null;
	}

	@Override
	public String getClassName() {
		return CommerceOrderItemImpl.class.getName();
	}

	@Override
	public String getTableName() {
		return CommerceOrderItemTable.INSTANCE.getTableName();
	}

	private static Object[] _getValue(
		CommerceOrderItemModelImpl commerceOrderItemModelImpl,
		String[] columnNames, boolean original) {

		Object[] arguments = new Object[columnNames.length];

		for (int i = 0; i < arguments.length; i++) {
			String columnName = columnNames[i];

			if (original) {
				arguments[i] =
					commerceOrderItemModelImpl.getColumnOriginalValue(
						columnName);
			}
			else {
				arguments[i] = commerceOrderItemModelImpl.getColumnValue(
					columnName);
			}
		}

		return arguments;
	}

	private static boolean _hasModifiedColumns(
		CommerceOrderItemModelImpl commerceOrderItemModelImpl,
		String[] columnNames) {

		if (columnNames.length == 0) {
			return false;
		}

		for (String columnName : columnNames) {
			if (!Objects.equals(
					commerceOrderItemModelImpl.getColumnOriginalValue(
						columnName),
					commerceOrderItemModelImpl.getColumnValue(columnName))) {

				return true;
			}
		}

		return false;
	}

	private static final String[] _ORDER_BY_COLUMNS;

	static {
		List<String> orderByColumns = new ArrayList<String>();

		orderByColumns.add("createDate");

		_ORDER_BY_COLUMNS = orderByColumns.toArray(new String[0]);
	}

}