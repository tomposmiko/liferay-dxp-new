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

package com.liferay.object.web.internal.object.entries.frontend.data.set.view.table;

import com.liferay.frontend.data.set.view.table.BaseTableFDSView;
import com.liferay.frontend.data.set.view.table.DateFDSTableSchemaField;
import com.liferay.frontend.data.set.view.table.FDSTableSchema;
import com.liferay.frontend.data.set.view.table.FDSTableSchemaBuilder;
import com.liferay.frontend.data.set.view.table.FDSTableSchemaBuilderFactory;
import com.liferay.frontend.data.set.view.table.FDSTableSchemaField;
import com.liferay.frontend.data.set.view.table.StringFDSTableSchemaField;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.model.ObjectView;
import com.liferay.object.model.ObjectViewColumn;
import com.liferay.object.model.ObjectViewColumnModel;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.service.ObjectViewLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marco Leo
 * @author Brian Wing Shun Chan
 */
public class ObjectEntriesTableFDSView extends BaseTableFDSView {

	public ObjectEntriesTableFDSView(
		FDSTableSchemaBuilderFactory fdsTableSchemaBuilderFactory,
		ObjectDefinition objectDefinition,
		ObjectDefinitionLocalService objectDefinitionLocalService,
		ObjectFieldLocalService objectFieldLocalService,
		ObjectRelationshipLocalService objectRelationshipLocalService,
		ObjectViewLocalService objectViewLocalService) {

		_fdsTableSchemaBuilderFactory = fdsTableSchemaBuilderFactory;
		_objectDefinition = objectDefinition;
		_objectDefinitionLocalService = objectDefinitionLocalService;
		_objectFieldLocalService = objectFieldLocalService;
		_objectRelationshipLocalService = objectRelationshipLocalService;
		_objectViewLocalService = objectViewLocalService;
	}

	@Override
	public FDSTableSchema getFDSTableSchema(Locale locale) {
		FDSTableSchemaBuilder fdsTableSchemaBuilder =
			_fdsTableSchemaBuilderFactory.create();

		ObjectView defaultObjectView =
			_objectViewLocalService.fetchDefaultObjectView(
				_objectDefinition.getObjectDefinitionId());

		if (defaultObjectView == null) {
			_addAllObjectFields(fdsTableSchemaBuilder, locale);

			return fdsTableSchemaBuilder.build();
		}

		for (ObjectViewColumn objectViewColumn :
				ListUtil.sort(
					defaultObjectView.getObjectViewColumns(),
					Comparator.comparingInt(
						ObjectViewColumnModel::getPriority))) {

			ObjectField objectField = _objectFieldLocalService.fetchObjectField(
				_objectDefinition.getObjectDefinitionId(),
				objectViewColumn.getObjectFieldName());

			String label = _getLabel(
				objectViewColumn.getLabel(locale, false),
				objectField.getLabel(locale, false));

			if ((objectField == null) || objectField.isSystem()) {
				_addSystemObjectField(
					fdsTableSchemaBuilder, label,
					objectViewColumn.getObjectFieldName());
			}
			else {
				if (Validator.isNull(label)) {
					label = _getLabel(
						objectViewColumn.getLabel(
							objectViewColumn.getDefaultLanguageId()),
						objectField.getLabel(
							objectField.getDefaultLanguageId()));
				}

				_addCustomObjectField(
					fdsTableSchemaBuilder, label, objectField);
			}
		}

		return fdsTableSchemaBuilder.build();
	}

	private void _addAllObjectFields(
		FDSTableSchemaBuilder fdsTableSchemaBuilder, Locale locale) {

		Map<String, String> systemObjectFieldLabels = new HashMap<>();

		for (ObjectField systemObjectField :
				_objectFieldLocalService.getObjectFields(
					_objectDefinition.getObjectDefinitionId(), true)) {

			systemObjectFieldLabels.put(
				systemObjectField.getName(),
				systemObjectField.getLabel(locale, false));
		}

		if (_objectDefinition.isDefaultStorageType()) {
			_addSystemObjectField(
				fdsTableSchemaBuilder, systemObjectFieldLabels.get("id"), "id");
		}
		else {
			_addSystemObjectField(
				fdsTableSchemaBuilder,
				systemObjectFieldLabels.get("externalReferenceCode"),
				"externalReferenceCode");
		}

		for (ObjectField customObjectField :
				_objectFieldLocalService.getObjectFields(
					_objectDefinition.getObjectDefinitionId(), false)) {

			_addCustomObjectField(
				fdsTableSchemaBuilder, customObjectField.getLabel(locale, true),
				customObjectField);
		}

		_addSystemObjectField(
			fdsTableSchemaBuilder, systemObjectFieldLabels.get("status"),
			"status");
		_addSystemObjectField(
			fdsTableSchemaBuilder, systemObjectFieldLabels.get("creator"),
			"creator");
	}

	private void _addCustomObjectField(
		FDSTableSchemaBuilder fdsTableSchemaBuilder, String label,
		ObjectField objectField) {

		if (objectField.isSystem()) {
			return;
		}

		if (Validator.isNull(objectField.getRelationshipType())) {
			_addFDSTableSchemaField(
				objectField.getBusinessType(), null, objectField.getDBType(),
				fdsTableSchemaBuilder,
				_getFieldName(
					objectField.getBusinessType(), objectField.getName()),
				label, false, objectField.isIndexed());
		}
		else if (Objects.equals(
					objectField.getRelationshipType(),
					ObjectRelationshipConstants.TYPE_ONE_TO_MANY)) {

			ObjectRelationship objectRelationship =
				_objectRelationshipLocalService.
					fetchObjectRelationshipByObjectFieldId2(
						objectField.getObjectFieldId());

			ObjectDefinition objectDefinition =
				_objectDefinitionLocalService.fetchObjectDefinition(
					objectRelationship.getObjectDefinitionId1());

			ObjectField titleObjectField =
				_objectFieldLocalService.fetchObjectField(
					objectDefinition.getTitleObjectFieldId());

			if (titleObjectField == null) {
				_addFDSTableSchemaField(
					objectField.getBusinessType(), null,
					objectField.getDBType(), fdsTableSchemaBuilder,
					objectField.getName(), label, false, false);
			}
			else {
				_addFDSTableSchemaField(
					titleObjectField.getBusinessType(),
					_getContentRenderer(titleObjectField.getName()),
					titleObjectField.getDBType(), fdsTableSchemaBuilder,
					_getFieldName(
						titleObjectField.getBusinessType(),
						StringBundler.concat(
							StringUtil.replaceLast(
								objectField.getName(), "Id", ""),
							StringPool.PERIOD, titleObjectField.getName())),
					label, false, false);
			}
		}
	}

	private void _addFDSTableSchemaField(
		String businessType, String contentRenderer, String dbType,
		FDSTableSchemaBuilder fdsTableSchemaBuilder, String fieldName,
		String label, boolean localizeLabel, boolean sortable) {

		FDSTableSchemaField fdsTableSchemaField = new FDSTableSchemaField();

		fdsTableSchemaField.setLocalizeLabel(localizeLabel);

		if (Objects.equals(
				businessType, ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT) ||
			Objects.equals(dbType, ObjectFieldConstants.DB_TYPE_CLOB) ||
			Objects.equals(dbType, ObjectFieldConstants.DB_TYPE_STRING)) {

			StringFDSTableSchemaField stringFDSTableSchemaField =
				new StringFDSTableSchemaField();

			if (Objects.equals(
					businessType,
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT)) {

				stringFDSTableSchemaField.setContentRenderer("link");
			}
			else if (Objects.equals(
						businessType,
						ObjectFieldConstants.
							BUSINESS_TYPE_MULTISELECT_PICKLIST)) {

				stringFDSTableSchemaField.setContentRenderer(
					"multiselectPicklistDataRenderer");
			}

			stringFDSTableSchemaField.setFieldName(fieldName);
			stringFDSTableSchemaField.setLabel(label);
			stringFDSTableSchemaField.setLocalizeLabel(localizeLabel);
			stringFDSTableSchemaField.setTruncate(true);

			fdsTableSchemaBuilder.add(stringFDSTableSchemaField);

			fdsTableSchemaField = stringFDSTableSchemaField;
		}
		else if (Objects.equals(dbType, ObjectFieldConstants.DB_TYPE_DATE)) {
			DateFDSTableSchemaField dateFDSTableSchemaField =
				new DateFDSTableSchemaField();

			dateFDSTableSchemaField.setFieldName(fieldName);
			dateFDSTableSchemaField.setFormat("short");
			dateFDSTableSchemaField.setLabel(label);
			dateFDSTableSchemaField.setLocalizeLabel(localizeLabel);

			fdsTableSchemaBuilder.add(dateFDSTableSchemaField);

			fdsTableSchemaField = dateFDSTableSchemaField;
		}
		else {
			fdsTableSchemaField.setFieldName(fieldName);
			fdsTableSchemaField.setLabel(label);

			fdsTableSchemaBuilder.add(fdsTableSchemaField);

			if (Objects.equals(dbType, ObjectFieldConstants.DB_TYPE_BOOLEAN)) {
				fdsTableSchemaField.setContentRenderer("boolean");
			}
		}

		if (Validator.isNotNull(contentRenderer)) {
			fdsTableSchemaField.setContentRenderer(contentRenderer);
		}

		if (!Objects.equals(
				businessType, ObjectFieldConstants.BUSINESS_TYPE_AGGREGATION) &&
			!Objects.equals(
				businessType, ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT) &&
			!Objects.equals(
				businessType, ObjectFieldConstants.BUSINESS_TYPE_RICH_TEXT) &&
			!Objects.equals(dbType, ObjectFieldConstants.DB_TYPE_BLOB) &&
			sortable) {

			fdsTableSchemaField.setSortable(true);
		}

		fdsTableSchemaBuilder.add(fdsTableSchemaField);
	}

	private void _addSystemObjectField(
		FDSTableSchemaBuilder fdsTableSchemaBuilder, String fieldLabel,
		String fieldName) {

		if (Objects.equals(fieldName, "createDate")) {
			_addFDSTableSchemaField(
				null, null, "Date", fdsTableSchemaBuilder, "dateCreated",
				_getLabel(fieldLabel, "create-date"), true, true);
		}
		else if (Objects.equals(fieldName, "creator")) {
			_addFDSTableSchemaField(
				null, null, null, fdsTableSchemaBuilder, fieldName + ".name",
				_getLabel(fieldLabel, "author"), true, true);
		}
		else if (Objects.equals(fieldName, "externalReferenceCode")) {
			_addFDSTableSchemaField(
				null, "actionLink", null, fdsTableSchemaBuilder, fieldName,
				_getLabel(fieldLabel, "external-reference-code"), true, true);
		}
		else if (Objects.equals(fieldName, "id")) {
			_addFDSTableSchemaField(
				null, "actionLink", null, fdsTableSchemaBuilder, "id",
				_getLabel(fieldLabel, "id"), true, true);
		}
		else if (Objects.equals(fieldName, "modifiedDate")) {
			_addFDSTableSchemaField(
				null, null, "Date", fdsTableSchemaBuilder, "dateModified",
				_getLabel(fieldLabel, "modified-date"), true, true);
		}
		else if (Objects.equals(fieldName, "status")) {
			_addFDSTableSchemaField(
				null, "status", null, fdsTableSchemaBuilder, fieldName,
				_getLabel(fieldLabel, "status"), true, true);
		}
	}

	private String _getContentRenderer(String fieldName) {
		if (Objects.equals(fieldName, "status")) {
			return "status";
		}

		return null;
	}

	private String _getFieldName(String businessType, String fieldName) {
		if (fieldName.contains(".creator")) {
			return StringUtil.replaceLast(fieldName, "creator", "creator.name");
		}

		if (fieldName.contains(".createDate")) {
			return StringUtil.replaceLast(
				fieldName, "createDate", "dateCreated");
		}

		if (fieldName.contains(".modifiedDate")) {
			return StringUtil.replace(
				fieldName, "modifiedDate", "dateModified");
		}

		if (Objects.equals(
				businessType, ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT)) {

			return fieldName + ".link";
		}

		if (Objects.equals(
				businessType, ObjectFieldConstants.BUSINESS_TYPE_PICKLIST)) {

			return fieldName + ".name";
		}

		if (Objects.equals(
				businessType, ObjectFieldConstants.BUSINESS_TYPE_RICH_TEXT)) {

			return fieldName + "RawText";
		}

		return fieldName;
	}

	private String _getLabel(String label, String defaultLabel) {
		if (Validator.isNotNull(label)) {
			return label;
		}

		return defaultLabel;
	}

	private final FDSTableSchemaBuilderFactory _fdsTableSchemaBuilderFactory;
	private final ObjectDefinition _objectDefinition;
	private final ObjectDefinitionLocalService _objectDefinitionLocalService;
	private final ObjectFieldLocalService _objectFieldLocalService;
	private final ObjectRelationshipLocalService
		_objectRelationshipLocalService;
	private final ObjectViewLocalService _objectViewLocalService;

}