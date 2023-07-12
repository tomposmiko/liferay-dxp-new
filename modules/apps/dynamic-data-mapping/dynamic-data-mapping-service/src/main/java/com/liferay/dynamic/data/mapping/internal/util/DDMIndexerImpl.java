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

package com.liferay.dynamic.data.mapping.internal.util;

import com.liferay.dynamic.data.mapping.configuration.DDMIndexerConfiguration;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.Field;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.dynamic.data.mapping.storage.constants.FieldConstants;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.dynamic.data.mapping.util.DDMFormValuesToFieldsConverter;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.FieldArray;
import com.liferay.portal.kernel.search.filter.QueryFilter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.NestedQuery;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.engine.SearchEngineInformation;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.sort.FieldSort;
import com.liferay.portal.search.sort.NestedSort;
import com.liferay.portal.search.sort.Sort;
import com.liferay.portal.search.sort.SortBuilderFactory;
import com.liferay.portal.search.sort.SortOrder;
import com.liferay.portal.search.sort.Sorts;

import java.io.Serializable;

import java.math.BigDecimal;

import java.text.Format;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alexander Chow
 */
@Component(
	configurationPid = "com.liferay.dynamic.data.mapping.configuration.DDMIndexerConfiguration",
	immediate = true, service = DDMIndexer.class
)
public class DDMIndexerImpl implements DDMIndexer {

	@Override
	public void addAttributes(
		Document document, DDMStructure ddmStructure,
		DDMFormValues ddmFormValues) {

		boolean legacyDDMIndexFieldsEnabled = isLegacyDDMIndexFieldsEnabled();

		FieldArray fieldArray = (FieldArray)document.getField(DDM_FIELD_ARRAY);

		if ((fieldArray == null) && !legacyDDMIndexFieldsEnabled) {
			fieldArray = new FieldArray(DDM_FIELD_ARRAY);

			document.add(fieldArray);
		}

		Set<Locale> locales = ddmFormValues.getAvailableLocales();

		Fields fields = toFields(ddmStructure, ddmFormValues);

		for (Field field : fields) {
			try {
				String indexType = ddmStructure.getFieldProperty(
					field.getName(), "indexType");

				if (Validator.isNull(indexType) || indexType.equals("none")) {
					continue;
				}

				String name = null;
				Serializable value = null;

				if (GetterUtil.getBoolean(
						ddmStructure.getFieldProperty(
							field.getName(), "localizable"))) {

					for (Locale locale : locales) {
						name = encodeName(
							ddmStructure.getStructureId(), field.getName(),
							locale, indexType);
						value = field.getValue(locale);

						if (legacyDDMIndexFieldsEnabled) {
							addToDocument(
								document, field, indexType, name, value);
						}
						else if (value != null) {
							fieldArray.addField(
								createField(
									ddmStructure.getDDMFormField(
										field.getName()),
									field, indexType, locale, name, value));
						}
					}
				}
				else {
					name = encodeName(
						ddmStructure.getStructureId(), field.getName(), null,
						indexType);
					value = field.getValue(ddmFormValues.getDefaultLocale());

					if (legacyDDMIndexFieldsEnabled) {
						addToDocument(document, field, indexType, name, value);
					}
					else if (value != null) {
						fieldArray.addField(
							createField(
								ddmStructure.getDDMFormField(field.getName()),
								field, indexType, null, name, value));
					}
				}
			}
			catch (Exception exception) {
				if (_log.isWarnEnabled()) {
					_log.warn(exception, exception);
				}
			}
		}
	}

	@Override
	public Sort createDDMStructureFieldSort(
			DDMStructure ddmStructure, String fieldName, Locale locale,
			SortOrder sortOrder)
		throws PortalException {

		DDMFormField ddmFormField = ddmStructure.getDDMFormField(fieldName);

		if (GetterUtil.getBoolean(ddmFormField.getProperty("localizable"))) {
			if (locale == null) {
				throw new IllegalArgumentException(
					"Locale cannot be null if the dynamic data mapping form " +
						"field is localizable");
			}
		}
		else {
			locale = null;
		}

		StringBundler sb = new StringBundler(5);

		if (isLegacyDDMIndexFieldsEnabled()) {
			sb.append(
				encodeName(ddmStructure.getStructureId(), fieldName, locale));
		}
		else {
			sb.append(DDMIndexer.DDM_FIELD_ARRAY);
			sb.append(StringPool.PERIOD);

			String indexType = ddmStructure.getFieldProperty(
				fieldName, "indexType");

			sb.append(getValueFieldName(indexType, locale));
		}

		sb.append(StringPool.UNDERLINE);

		String ddmFormFieldType = ddmFormField.getType();

		if (Objects.equals(ddmFormFieldType, DDMFormFieldType.DECIMAL) ||
			Objects.equals(ddmFormFieldType, DDMFormFieldType.INTEGER) ||
			Objects.equals(ddmFormFieldType, DDMFormFieldType.NUMBER) ||
			Objects.equals(ddmFormFieldType, DDMFormFieldType.NUMERIC)) {

			sb.append("Number");
		}
		else {
			sb.append("String");
		}

		FieldSort fieldSort = sorts.field(
			com.liferay.portal.kernel.search.Field.getSortableFieldName(
				sb.toString()),
			sortOrder);

		if (isLegacyDDMIndexFieldsEnabled()) {
			return fieldSort;
		}

		NestedSort nestedSort = sorts.nested(DDMIndexer.DDM_FIELD_ARRAY);

		nestedSort.setFilterQuery(
			queries.term(
				StringBundler.concat(
					DDMIndexer.DDM_FIELD_ARRAY, StringPool.PERIOD,
					DDMIndexer.DDM_FIELD_NAME),
				encodeName(ddmStructure.getStructureId(), fieldName, locale)));

		fieldSort.setNestedSort(nestedSort);

		return fieldSort;
	}

	@Override
	public Sort createDDMStructureFieldSort(
			String ddmStructureFieldName, Locale locale, SortOrder sortOrder)
		throws PortalException {

		String[] ddmStructureFieldNameParts = StringUtil.split(
			ddmStructureFieldName, DDM_FIELD_SEPARATOR);

		long ddmStructureId = GetterUtil.getLong(ddmStructureFieldNameParts[2]);

		String fieldName = StringUtil.replaceLast(
			ddmStructureFieldNameParts[3],
			StringPool.UNDERLINE.concat(LocaleUtil.toLanguageId(locale)),
			StringPool.BLANK);

		DDMStructure ddmStructure = _ddmStructureLocalService.getStructure(
			ddmStructureId);

		return createDDMStructureFieldSort(
			ddmStructure, fieldName, locale, sortOrder);
	}

	public QueryFilter createFieldValueQueryFilter(
			DDMStructure ddmStructure, String fieldName, Locale locale,
			Serializable value)
		throws Exception {

		String indexType = ddmStructure.getFieldProperty(
			fieldName, "indexType");

		return createFieldValueQueryFilter(
			ddmStructure,
			encodeName(ddmStructure.getStructureId(), fieldName, locale), value,
			fieldName, indexType, locale);
	}

	@Override
	public QueryFilter createFieldValueQueryFilter(
			String ddmStructureFieldName, Serializable ddmStructureFieldValue,
			Locale locale)
		throws Exception {

		String[] ddmStructureFieldNameParts = StringUtil.split(
			ddmStructureFieldName, DDM_FIELD_SEPARATOR);

		DDMStructure ddmStructure = _ddmStructureLocalService.getStructure(
			GetterUtil.getLong(ddmStructureFieldNameParts[2]));

		String fieldName = StringUtil.replaceLast(
			ddmStructureFieldNameParts[3],
			StringPool.UNDERLINE.concat(LocaleUtil.toLanguageId(locale)),
			StringPool.BLANK);

		return createFieldValueQueryFilter(
			ddmStructure, ddmStructureFieldName, ddmStructureFieldValue,
			fieldName, ddmStructureFieldNameParts[1], locale);
	}

	@Override
	public String encodeName(long ddmStructureId, String fieldName) {
		return encodeName(ddmStructureId, fieldName, null);
	}

	@Override
	public String encodeName(
		long ddmStructureId, String fieldName, Locale locale) {

		String indexType = StringPool.BLANK;
		boolean localizable = true;

		if (ddmStructureId > 0) {
			DDMStructure ddmStructure =
				_ddmStructureLocalService.fetchDDMStructure(ddmStructureId);

			if (ddmStructure != null) {
				try {
					indexType = ddmStructure.getFieldProperty(
						fieldName, "indexType");
					localizable = GetterUtil.getBoolean(
						ddmStructure.getFieldProperty(
							fieldName, "localizable"));
				}
				catch (PortalException portalException) {
					throw new IllegalArgumentException(
						StringBundler.concat(
							"Unable to obtain index tpe for field ", fieldName,
							" and DDM structure ID ", ddmStructureId),
						portalException);
				}
			}
		}

		if (localizable) {
			return encodeName(ddmStructureId, fieldName, locale, indexType);
		}

		return encodeName(ddmStructureId, fieldName, null, indexType);
	}

	@Override
	public String extractIndexableAttributes(
		DDMStructure ddmStructure, DDMFormValues ddmFormValues, Locale locale) {

		StringBundler sb = new StringBundler();

		_extractIndexableAttributes(
			ddmFormValues.getDDMFormFieldValues(), ddmStructure,
			ddmFormValues.getDefaultLocale(), locale, sb);

		if (sb.index() > 0) {
			sb.setIndex(sb.index() - 1);
		}

		return sb.toString();
	}

	@Override
	public String getValueFieldName(String indexType) {
		return getValueFieldName(indexType, null);
	}

	@Override
	public String getValueFieldName(String indexType, Locale locale) {
		String valueFieldName = DDM_VALUE_FIELD_NAME_PREFIX;

		if (indexType != null) {
			valueFieldName = valueFieldName.concat(
				StringUtil.upperCaseFirstLetter(indexType));
		}

		if (locale != null) {
			valueFieldName = StringBundler.concat(
				valueFieldName, StringPool.UNDERLINE,
				LocaleUtil.toLanguageId(locale));
		}

		return valueFieldName;
	}

	@Override
	public boolean isLegacyDDMIndexFieldsEnabled() {
		if (Objects.equals(searchEngineInformation.getVendorString(), "Solr")) {
			return true;
		}

		return _ddmIndexerConfiguration.enableLegacyDDMIndexFields();
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_ddmIndexerConfiguration = ConfigurableUtil.createConfigurable(
			DDMIndexerConfiguration.class, properties);
	}

	protected void addFieldValueRequiredTerm(
		BooleanQuery booleanQuery, String ddmStructureFieldName,
		String ddmStructureFieldValue, String indexType, Locale locale,
		boolean localizable) {

		if (isLegacyDDMIndexFieldsEnabled()) {
			booleanQuery.addRequiredTerm(
				ddmStructureFieldName,
				StringPool.QUOTE + ddmStructureFieldValue + StringPool.QUOTE);

			return;
		}

		booleanQuery.addRequiredTerm(
			StringBundler.concat(
				DDM_FIELD_ARRAY, StringPool.PERIOD, DDM_FIELD_NAME),
			ddmStructureFieldName);

		if (!localizable) {
			locale = null;
		}

		booleanQuery.addRequiredTerm(
			StringBundler.concat(
				DDM_FIELD_ARRAY, StringPool.PERIOD,
				getValueFieldName(indexType, locale)),
			StringPool.QUOTE + ddmStructureFieldValue + StringPool.QUOTE);
	}

	protected void addToDocument(
			Document document, Field field, String indexType, String name,
			Serializable value)
		throws PortalException {

		addToDocument(document, field, indexType, name, value, value);
	}

	protected void addToDocument(
			Document document, Field field, String indexType, String name,
			Serializable sortableValue, Serializable value)
		throws PortalException {

		if (value == null) {
		}
		else if (value instanceof BigDecimal) {
			document.addNumberSortable(name, (BigDecimal)value);
		}
		else if (value instanceof BigDecimal[]) {
			document.addNumberSortable(name, (BigDecimal[])value);
		}
		else if (value instanceof Boolean) {
			document.addKeywordSortable(name, (Boolean)value);
		}
		else if (value instanceof Boolean[]) {
			document.addKeywordSortable(name, (Boolean[])value);
		}
		else if (value instanceof Date) {
			document.addDateSortable(name, (Date)value);
		}
		else if (value instanceof Date[]) {
			document.addDateSortable(name, (Date[])value);
		}
		else if (value instanceof Double) {
			document.addNumberSortable(name, (Double)value);
		}
		else if (value instanceof Double[]) {
			document.addNumberSortable(name, (Double[])value);
		}
		else if (value instanceof Integer) {
			document.addNumberSortable(name, (Integer)value);
		}
		else if (value instanceof Integer[]) {
			document.addNumberSortable(name, (Integer[])value);
		}
		else if (value instanceof Long) {
			document.addNumberSortable(name, (Long)value);
		}
		else if (value instanceof Long[]) {
			document.addNumberSortable(name, (Long[])value);
		}
		else if (value instanceof Float) {
			document.addNumberSortable(name, (Float)value);
		}
		else if (value instanceof Float[]) {
			document.addNumberSortable(name, (Float[])value);
		}
		else if (value instanceof Number[]) {
			Number[] numbers = (Number[])value;

			Double[] doubles = new Double[numbers.length];

			for (int i = 0; i < numbers.length; i++) {
				doubles[i] = numbers[i].doubleValue();
			}

			document.addNumberSortable(name, doubles);
		}
		else if (value instanceof Object[]) {
			String[] valuesString = ArrayUtil.toStringArray((Object[])value);

			if (indexType.equals("keyword")) {
				document.addKeywordSortable(name, valuesString);
			}
			else {
				document.addTextSortable(name, valuesString);
			}
		}
		else {
			String sortableValueString = StringUtil.toLowerCase(
				String.valueOf(sortableValue));
			String valueString = String.valueOf(value);

			String type = field.getType();

			if (type.equals(DDMFormFieldType.GEOLOCATION)) {
				JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
					valueString);

				double latitude = jsonObject.getDouble("latitude", 0);
				double longitude = jsonObject.getDouble("longitude", 0);

				document.addGeoLocation(
					name.concat("_geolocation"), latitude, longitude);
			}
			else if (type.equals(DDMImpl.TYPE_SELECT)) {
				document.addKeyword(
					_getSortableFieldName(name),
					StringUtil.merge(
						ArrayUtil.toStringArray(
							JSONFactoryUtil.createJSONArray(
								sortableValueString)),
						StringPool.SPACE));

				document.addKeyword(
					name,
					ArrayUtil.toStringArray(
						JSONFactoryUtil.createJSONArray(valueString)));
			}
			else {
				if (type.equals(DDMImpl.TYPE_DDM_TEXT_HTML)) {
					valueString = HtmlUtil.extractText(valueString);
					sortableValueString = HtmlUtil.extractText(
						sortableValueString);
				}

				_createSortableTextField(document, name, sortableValueString);

				if (indexType.equals("keyword")) {
					document.addKeyword(name, valueString);
				}
				else {
					document.addText(name, valueString);
				}
			}
		}
	}

	protected com.liferay.portal.kernel.search.Field createField(
			DDMFormField ddmFormField, Field ddmStructureField,
			String indexType, Locale locale, String name, Serializable value)
		throws PortalException {

		Document document = new DocumentImpl();

		String valueFieldName = getValueFieldName(indexType, locale);

		addToDocument(
			document, ddmStructureField, indexType, valueFieldName,
			_getSortableValue(ddmFormField, locale, value), value);

		Map<String, com.liferay.portal.kernel.search.Field> fields =
			document.getFields();

		com.liferay.portal.kernel.search.Field ddmField =
			new com.liferay.portal.kernel.search.Field("");

		ddmField.addField(
			new com.liferay.portal.kernel.search.Field(DDM_FIELD_NAME, name));

		ddmField.addField(
			new com.liferay.portal.kernel.search.Field(
				DDM_VALUE_FIELD_NAME, valueFieldName));

		for (com.liferay.portal.kernel.search.Field field : fields.values()) {
			ddmField.addField(field);
		}

		return ddmField;
	}

	protected QueryFilter createFieldValueQueryFilter(
			DDMStructure ddmStructure, String ddmStructureFieldName,
			Serializable ddmStructureFieldValue, String fieldName,
			String indexType, Locale locale)
		throws Exception {

		BooleanQuery booleanQuery = new BooleanQueryImpl();

		boolean localizable = false;

		if (ddmStructure.hasField(fieldName)) {
			ddmStructureFieldValue = _ddm.getIndexedFieldValue(
				ddmStructureFieldValue, ddmStructure.getFieldType(fieldName));

			localizable = GetterUtil.getBoolean(
				ddmStructure.getFieldProperty(fieldName, "localizable"));
		}

		if (ddmStructureFieldValue instanceof String[]) {
			String[] ddmStructureFieldValueArray =
				(String[])ddmStructureFieldValue;

			for (String ddmStructureFieldValueString :
					ddmStructureFieldValueArray) {

				addFieldValueRequiredTerm(
					booleanQuery, ddmStructureFieldName,
					ddmStructureFieldValueString, indexType, locale,
					localizable);
			}
		}
		else {
			addFieldValueRequiredTerm(
				booleanQuery, ddmStructureFieldName,
				String.valueOf(ddmStructureFieldValue), indexType, locale,
				localizable);
		}

		if (isLegacyDDMIndexFieldsEnabled()) {
			return new QueryFilter(booleanQuery);
		}

		return new QueryFilter(new NestedQuery(DDM_FIELD_ARRAY, booleanQuery));
	}

	protected String encodeName(
		long ddmStructureId, String fieldName, Locale locale,
		String indexType) {

		StringBundler sb = new StringBundler(8);

		sb.append(DDM_FIELD_PREFIX);

		if (Validator.isNotNull(indexType)) {
			sb.append(indexType);
			sb.append(DDM_FIELD_SEPARATOR);
		}

		sb.append(ddmStructureId);
		sb.append(DDM_FIELD_SEPARATOR);
		sb.append(fieldName);

		if (locale != null) {
			sb.append(StringPool.UNDERLINE);
			sb.append(LocaleUtil.toLanguageId(locale));
		}

		return sb.toString();
	}

	@Reference(unbind = "-")
	protected void setDDM(DDM ddm) {
		_ddm = ddm;
	}

	@Reference(unbind = "-")
	protected void setDDMFormValuesToFieldsConverter(
		DDMFormValuesToFieldsConverter ddmFormValuesToFieldsConverter) {

		_ddmFormValuesToFieldsConverter = ddmFormValuesToFieldsConverter;
	}

	@Reference(unbind = "-")
	protected void setDDMStructureLocalService(
		DDMStructureLocalService ddmStructureLocalService) {

		_ddmStructureLocalService = ddmStructureLocalService;
	}

	protected Fields toFields(
		DDMStructure ddmStructure, DDMFormValues ddmFormValues) {

		try {
			return _ddmFormValuesToFieldsConverter.convert(
				ddmStructure, ddmFormValues);
		}
		catch (PortalException portalException) {
			_log.error(
				"Unable to convert DDMFormValues to Fields", portalException);
		}

		return new Fields();
	}

	@Reference
	protected Queries queries;

	@Reference
	protected SearchEngineInformation searchEngineInformation;

	@Reference
	protected SortBuilderFactory sortBuilderFactory;

	@Reference
	protected Sorts sorts;

	private void _addFieldValue(
			StringBundler sb, String type, String valueString)
		throws Exception {

		if (type.equals(DDMImpl.TYPE_DDM_DOCUMENTLIBRARY) ||
			type.equals(DDMImpl.TYPE_DDM_JOURNAL_ARTICLE) ||
			type.equals(DDMImpl.TYPE_DDM_LINK_TO_PAGE)) {

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				valueString);

			if ((jsonObject != null) && jsonObject.has("title")) {
				sb.append(jsonObject.getString("title"));
			}
		}
		else if (type.equals(DDMImpl.TYPE_DDM_IMAGE)) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				valueString);

			if (jsonObject == null) {
				return;
			}

			if (jsonObject.has("description") &&
				Validator.isNotNull(jsonObject.getString("description"))) {

				sb.append(jsonObject.getString("description"));
			}
			else if (jsonObject.has("alt") &&
					 Validator.isNotNull(jsonObject.getString("alt"))) {

				sb.append(jsonObject.getString("alt"));
			}
			else if (jsonObject.has("title")) {
				sb.append(jsonObject.getString("title"));
			}
		}
		else if (type.equals(DDMImpl.TYPE_DDM_TEXT_HTML)) {
			sb.append(HtmlUtil.extractText(valueString));
		}
		else if (type.equals(DDMImpl.TYPE_SELECT)) {
			JSONArray jsonArray = JSONFactoryUtil.createJSONArray(valueString);

			sb.append(ArrayUtil.toStringArray(jsonArray));
		}
		else {
			sb.append(valueString);
		}
	}

	private void _createSortableTextField(
		Document document, String name, String sortableValueString) {

		if (Validator.isNull(sortableValueString)) {
			return;
		}

		if (sortableValueString.length() >
				_SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH) {

			sortableValueString = sortableValueString.substring(
				0, _SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH);
		}

		document.addKeyword(_getSortableFieldName(name), sortableValueString);
	}

	private void _extractIndexableAttribute(
			DDMFormField ddmFormField, Locale defaultLocale, String indexType,
			Locale locale, StringBundler sb, Value value)
		throws Exception {

		if (Validator.isNull(indexType) || indexType.equals("none") ||
			(value == null)) {

			return;
		}

		Serializable serializable = FieldConstants.getSerializable(
			defaultLocale, locale, ddmFormField.getDataType(),
			value.getString(locale));

		if ((serializable == null) ||
			Validator.isBlank(String.valueOf(serializable))) {

			return;
		}

		if (serializable instanceof Boolean || serializable instanceof Number) {
			sb.append(serializable);
		}
		else if (serializable instanceof Date) {
			Format dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(
				PropsUtil.get(PropsKeys.INDEX_DATE_FORMAT_PATTERN));

			sb.append(dateFormat.format(serializable));
		}
		else {
			_addFieldValue(
				sb, ddmFormField.getType(),
				_getSortableValue(ddmFormField, locale, serializable));
		}

		sb.append(StringPool.SPACE);
	}

	private void _extractIndexableAttributes(
		List<DDMFormFieldValue> ddmFormFieldValues, DDMStructure ddmStructure,
		Locale defaultLocale, Locale locale, StringBundler sb) {

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			DDMFormField ddmFormField = ddmFormFieldValue.getDDMFormField();

			try {
				Locale ddmFormFieldLocale = locale;

				if (!ddmFormField.isLocalizable()) {
					ddmFormFieldLocale = LocaleUtil.ROOT;
				}

				_extractIndexableAttribute(
					ddmFormField, defaultLocale,
					ddmStructure.getFieldProperty(
						ddmFormField.getName(), "indexType"),
					ddmFormFieldLocale, sb, ddmFormFieldValue.getValue());
			}
			catch (Exception exception) {
				if (_log.isWarnEnabled()) {
					_log.warn(exception);
				}
			}

			if (ListUtil.isNotEmpty(
					ddmFormFieldValue.getNestedDDMFormFieldValues())) {

				_extractIndexableAttributes(
					ddmFormFieldValue.getNestedDDMFormFieldValues(),
					ddmStructure, defaultLocale, locale, sb);
			}
		}
	}

	private String _getSortableFieldName(String name) {
		return com.liferay.portal.kernel.search.Field.getSortableFieldName(
			name + "_String");
	}

	private String _getSortableValue(
		DDMFormField ddmFormField, Locale locale, Serializable value) {

		if (value == null) {
			return null;
		}

		String sortableValue = String.valueOf(value);

		DDMFormFieldOptions ddmFormFieldOptions =
			(DDMFormFieldOptions)ddmFormField.getProperty("options");

		Map<String, LocalizedValue> map = ddmFormFieldOptions.getOptions();

		for (Map.Entry<String, LocalizedValue> entry : map.entrySet()) {
			LocalizedValue localizedValue = entry.getValue();

			sortableValue = StringUtil.replace(
				sortableValue, entry.getKey(),
				localizedValue.getString(locale));
		}

		return sortableValue;
	}

	private static final int _SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH =
		GetterUtil.getInteger(
			PropsUtil.get(
				PropsKeys.INDEX_SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH));

	private static final Log _log = LogFactoryUtil.getLog(DDMIndexerImpl.class);

	private DDM _ddm;
	private DDMFormValuesToFieldsConverter _ddmFormValuesToFieldsConverter;
	private volatile DDMIndexerConfiguration _ddmIndexerConfiguration;
	private DDMStructureLocalService _ddmStructureLocalService;

}