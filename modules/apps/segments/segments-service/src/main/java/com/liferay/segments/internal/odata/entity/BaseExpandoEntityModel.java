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

package com.liferay.segments.internal.odata.entity;

import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoColumnTable;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.model.ExpandoTableTable;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.odata.entity.BooleanEntityField;
import com.liferay.portal.odata.entity.ComplexEntityField;
import com.liferay.portal.odata.entity.DateTimeEntityField;
import com.liferay.portal.odata.entity.DoubleEntityField;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.entity.IntegerEntityField;
import com.liferay.portal.odata.entity.StringEntityField;
import com.liferay.portal.search.expando.ExpandoBridgeIndexer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 */
public abstract class BaseExpandoEntityModel implements EntityModel {

	@Override
	public Map<String, EntityField> getEntityFieldsMap() {
		return _entityFieldsMap;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_entityFieldsMap = EntityModel.toEntityFieldsMap(getEntityFields());

		_entityFieldsMap.put(
			"customField", new LazyComplexEntityField("customField"));
	}

	@Deactivate
	protected void deactivate() {
		ServiceRegistration<?> serviceRegistration = _serviceRegistration;

		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

	protected abstract EntityField[] getEntityFields();

	protected abstract String getModelClassName();

	@Reference
	protected ClassNameLocalService classNameLocalService;

	@Reference
	protected EntityModelFieldMapper entityModelFieldMapper;

	@Reference
	protected ExpandoBridgeIndexer expandoBridgeIndexer;

	@Reference
	protected ExpandoColumnLocalService expandoColumnLocalService;

	@Reference
	protected ExpandoTableLocalService expandoTableLocalService;

	private Map<String, EntityField> _createEntityFieldsMap() {
		_serviceRegistration = _bundleContext.registerService(
			ModelListener.class, new ExpandoColumnModelListener(), null);

		Map<String, EntityField> entityFieldsMap = new HashMap<>();

		long classNameId = classNameLocalService.getClassNameId(
			getModelClassName());

		List<ExpandoColumn> expandoColumns = expandoColumnLocalService.dslQuery(
			DSLQueryFactoryUtil.select(
				ExpandoColumnTable.INSTANCE
			).from(
				ExpandoColumnTable.INSTANCE
			).where(
				ExpandoColumnTable.INSTANCE.tableId.in(
					DSLQueryFactoryUtil.select(
						ExpandoTableTable.INSTANCE.tableId
					).from(
						ExpandoTableTable.INSTANCE
					).where(
						ExpandoTableTable.INSTANCE.classNameId.eq(
							classNameId
						).and(
							ExpandoTableTable.INSTANCE.name.eq(
								ExpandoTableConstants.DEFAULT_TABLE_NAME)
						)
					))
			));

		for (ExpandoColumn expandoColumn : expandoColumns) {
			EntityField entityField = _getEntityField(expandoColumn);

			if (entityField != null) {
				entityFieldsMap.put(entityField.getName(), entityField);
			}
		}

		return entityFieldsMap;
	}

	private EntityField _getEntityField(ExpandoColumn expandoColumn) {
		UnicodeProperties unicodeProperties =
			expandoColumn.getTypeSettingsProperties();

		int indexType = GetterUtil.getInteger(
			unicodeProperties.get(ExpandoColumnConstants.INDEX_TYPE));

		if (indexType == ExpandoColumnConstants.INDEX_TYPE_NONE) {
			return null;
		}

		EntityField entityField = null;

		String encodedName =
			entityModelFieldMapper.getExpandoColumnEntityFieldName(
				expandoColumn);
		String encodedIndexedFieldName = expandoBridgeIndexer.encodeFieldName(
			expandoColumn);

		if (expandoColumn.getType() == ExpandoColumnConstants.BOOLEAN) {
			entityField = new BooleanEntityField(
				encodedName, locale -> encodedIndexedFieldName);
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.DATE) {
			entityField = new DateTimeEntityField(
				encodedName,
				locale -> Field.getSortableFieldName(encodedIndexedFieldName),
				locale -> encodedIndexedFieldName);
		}
		else if ((expandoColumn.getType() == ExpandoColumnConstants.DOUBLE) ||
				 (expandoColumn.getType() ==
					 ExpandoColumnConstants.DOUBLE_ARRAY) ||
				 (expandoColumn.getType() == ExpandoColumnConstants.FLOAT) ||
				 (expandoColumn.getType() ==
					 ExpandoColumnConstants.FLOAT_ARRAY)) {

			entityField = new DoubleEntityField(
				encodedName, locale -> encodedIndexedFieldName);
		}
		else if ((expandoColumn.getType() == ExpandoColumnConstants.INTEGER) ||
				 (expandoColumn.getType() ==
					 ExpandoColumnConstants.INTEGER_ARRAY) ||
				 (expandoColumn.getType() == ExpandoColumnConstants.LONG) ||
				 (expandoColumn.getType() ==
					 ExpandoColumnConstants.LONG_ARRAY) ||
				 (expandoColumn.getType() == ExpandoColumnConstants.SHORT) ||
				 (expandoColumn.getType() ==
					 ExpandoColumnConstants.SHORT_ARRAY)) {

			entityField = new IntegerEntityField(
				encodedName, locale -> encodedIndexedFieldName);
		}
		else if (expandoColumn.getType() ==
					ExpandoColumnConstants.STRING_LOCALIZED) {

			entityField = new StringEntityField(
				encodedName,
				locale -> Field.getLocalizedName(
					locale, encodedIndexedFieldName));
		}
		else {
			entityField = new StringEntityField(
				encodedName, locale -> encodedIndexedFieldName);
		}

		return entityField;
	}

	private BundleContext _bundleContext;
	private Map<String, EntityField> _entityFieldsMap;
	private final DCLSingleton<Map<String, EntityField>>
		_entityFieldsMapDCLSingleton = new DCLSingleton<>();
	private volatile ServiceRegistration<?> _serviceRegistration;

	private class ExpandoColumnModelListener
		extends BaseModelListener<ExpandoColumn> {

		@Override
		public void onAfterCreate(ExpandoColumn expandoColumn)
			throws ModelListenerException {

			try {
				long classNameId = classNameLocalService.getClassNameId(
					getModelClassName());
				ExpandoTable expandoTable = expandoTableLocalService.getTable(
					expandoColumn.getTableId());

				if ((classNameId != expandoTable.getClassNameId()) ||
					!ExpandoTableConstants.DEFAULT_TABLE_NAME.equals(
						expandoTable.getName())) {

					return;
				}

				EntityField entityField = _getEntityField(expandoColumn);

				if (entityField != null) {
					Map<String, EntityField> entityFieldsMap =
						_entityFieldsMapDCLSingleton.getSingleton(
							BaseExpandoEntityModel.this::
								_createEntityFieldsMap);

					entityFieldsMap.put(entityField.getName(), entityField);
				}
			}
			catch (PortalException portalException) {
				throw new ModelListenerException(portalException);
			}
		}

		@Override
		public void onAfterRemove(ExpandoColumn expandoColumn)
			throws ModelListenerException {

			if (expandoColumn == null) {
				return;
			}

			Map<String, EntityField> entityFieldsMap =
				_entityFieldsMapDCLSingleton.getSingleton(
					BaseExpandoEntityModel.this::_createEntityFieldsMap);

			entityFieldsMap.remove(
				entityModelFieldMapper.getExpandoColumnEntityFieldName(
					expandoColumn));
		}

		@Override
		public void onAfterUpdate(
				ExpandoColumn originalExpandoColumn,
				ExpandoColumn expandoColumn)
			throws ModelListenerException {

			if (expandoColumn == null) {
				return;
			}

			Map<String, EntityField> entityFieldsMap =
				_entityFieldsMapDCLSingleton.getSingleton(
					BaseExpandoEntityModel.this::_createEntityFieldsMap);

			entityFieldsMap.remove(
				entityModelFieldMapper.getExpandoColumnEntityFieldName(
					expandoColumn));

			onAfterCreate(expandoColumn);
		}

	}

	private class LazyComplexEntityField extends ComplexEntityField {

		@Override
		public Map<String, EntityField> getEntityFieldsMap() {
			return _entityFieldsMapDCLSingleton.getSingleton(
				BaseExpandoEntityModel.this::_createEntityFieldsMap);
		}

		private LazyComplexEntityField(String name) {
			super(name, Collections.emptyList());
		}

	}

}