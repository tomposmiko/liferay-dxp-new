/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.contacts.service.persistence.impl;

import com.liferay.osb.faro.contacts.model.ContactsCardTemplateTable;
import com.liferay.osb.faro.contacts.model.impl.ContactsCardTemplateImpl;
import com.liferay.osb.faro.contacts.model.impl.ContactsCardTemplateModelImpl;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The arguments resolver class for retrieving value from ContactsCardTemplate.
 *
 * @author Shinn Lok
 * @generated
 */
@OSGiBeanProperties(
	property = {
		"class.name=com.liferay.osb.faro.contacts.model.impl.ContactsCardTemplateImpl",
		"table.name=OSBFaro_ContactsCardTemplate"
	},
	service = ArgumentsResolver.class
)
public class ContactsCardTemplateModelArgumentsResolver
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

		ContactsCardTemplateModelImpl contactsCardTemplateModelImpl =
			(ContactsCardTemplateModelImpl)baseModel;

		long columnBitmask = contactsCardTemplateModelImpl.getColumnBitmask();

		if (!checkColumn || (columnBitmask == 0)) {
			return _getValue(
				contactsCardTemplateModelImpl, columnNames, original);
		}

		Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
			finderPath);

		if (finderPathColumnBitmask == null) {
			finderPathColumnBitmask = 0L;

			for (String columnName : columnNames) {
				finderPathColumnBitmask |=
					contactsCardTemplateModelImpl.getColumnBitmask(columnName);
			}

			_finderPathColumnBitmasksCache.put(
				finderPath, finderPathColumnBitmask);
		}

		if ((columnBitmask & finderPathColumnBitmask) != 0) {
			return _getValue(
				contactsCardTemplateModelImpl, columnNames, original);
		}

		return null;
	}

	@Override
	public String getClassName() {
		return ContactsCardTemplateImpl.class.getName();
	}

	@Override
	public String getTableName() {
		return ContactsCardTemplateTable.INSTANCE.getTableName();
	}

	private static Object[] _getValue(
		ContactsCardTemplateModelImpl contactsCardTemplateModelImpl,
		String[] columnNames, boolean original) {

		Object[] arguments = new Object[columnNames.length];

		for (int i = 0; i < arguments.length; i++) {
			String columnName = columnNames[i];

			if (original) {
				arguments[i] =
					contactsCardTemplateModelImpl.getColumnOriginalValue(
						columnName);
			}
			else {
				arguments[i] = contactsCardTemplateModelImpl.getColumnValue(
					columnName);
			}
		}

		return arguments;
	}

	private static final Map<FinderPath, Long> _finderPathColumnBitmasksCache =
		new ConcurrentHashMap<>();

}