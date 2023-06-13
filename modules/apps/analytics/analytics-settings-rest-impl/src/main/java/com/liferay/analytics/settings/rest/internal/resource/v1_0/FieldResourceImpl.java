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

package com.liferay.analytics.settings.rest.internal.resource.v1_0;

import com.liferay.account.model.AccountEntry;
import com.liferay.analytics.settings.configuration.AnalyticsConfiguration;
import com.liferay.analytics.settings.rest.constants.FieldAccountConstants;
import com.liferay.analytics.settings.rest.constants.FieldOrderConstants;
import com.liferay.analytics.settings.rest.constants.FieldPeopleConstants;
import com.liferay.analytics.settings.rest.constants.FieldProductConstants;
import com.liferay.analytics.settings.rest.dto.v1_0.Field;
import com.liferay.analytics.settings.rest.manager.AnalyticsSettingsManager;
import com.liferay.analytics.settings.rest.resource.v1_0.FieldResource;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Riccardo Ferrari
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/field.properties",
	scope = ServiceScope.PROTOTYPE, service = FieldResource.class
)
public class FieldResourceImpl extends BaseFieldResourceImpl {

	@Override
	public Page<Field> getFieldsAccountsPage(
			String keyword, Pagination pagination, Sort[] sorts)
		throws Exception {

		AnalyticsConfiguration analyticsConfiguration =
			_analyticsSettingsManager.getAnalyticsConfiguration(
				contextCompany.getCompanyId());

		List<Field> fields = _getFields(
			FieldAccountConstants.FIELD_ACCOUNT_EXAMPLES,
			FieldAccountConstants.FIELD_ACCOUNT_NAMES,
			FieldAccountConstants.FIELD_ACCOUNT_REQUIRED_NAMES, "account",
			analyticsConfiguration.syncedAccountFieldNames(),
			FieldAccountConstants.FIELD_ACCOUNT_TYPES);

		fields.addAll(
			_getExpandoFields(
				AccountEntry.class.getName(), contextCompany.getCompanyId(),
				"account", analyticsConfiguration.syncedAccountFieldNames()));

		fields = _filter(fields, keyword);

		fields = _sort(fields, sorts);

		return Page.of(
			ListUtil.subList(
				fields, pagination.getStartPosition(),
				pagination.getEndPosition()),
			pagination, fields.size());
	}

	@Override
	public Page<Field> getFieldsOrdersPage(
			String keyword, Pagination pagination, Sort[] sorts)
		throws Exception {

		AnalyticsConfiguration analyticsConfiguration =
			_analyticsSettingsManager.getAnalyticsConfiguration(
				contextCompany.getCompanyId());

		List<Field> fields = _getFields(
			FieldOrderConstants.FIELD_ORDER_EXAMPLES,
			FieldOrderConstants.FIELD_ORDER_NAMES,
			FieldOrderConstants.FIELD_ORDER_REQUIRED_NAMES, "order",
			analyticsConfiguration.syncedOrderFieldNames(),
			FieldOrderConstants.FIELD_ORDER_TYPES);

		fields.addAll(
			_getFields(
				FieldOrderConstants.FIELD_ORDER_ITEM_EXAMPLES,
				FieldOrderConstants.FIELD_ORDER_ITEM_NAMES,
				FieldOrderConstants.FIELD_ORDER_ITEM_REQUIRED_NAMES,
				"order-item", analyticsConfiguration.syncedOrderFieldNames(),
				FieldOrderConstants.FIELD_ORDER_ITEM_TYPES));

		fields = _filter(fields, keyword);

		fields = _sort(fields, sorts);

		return Page.of(
			ListUtil.subList(
				fields, pagination.getStartPosition(),
				pagination.getEndPosition()),
			pagination, fields.size());
	}

	@Override
	public Page<Field> getFieldsPeoplePage(
			String keyword, Pagination pagination, Sort[] sorts)
		throws Exception {

		AnalyticsConfiguration analyticsConfiguration =
			_analyticsSettingsManager.getAnalyticsConfiguration(
				contextCompany.getCompanyId());

		List<Field> fields = _getFields(
			FieldPeopleConstants.FIELD_CONTACT_EXAMPLES,
			FieldPeopleConstants.FIELD_CONTACT_NAMES,
			FieldPeopleConstants.FIELD_CONTACT_REQUIRED_NAMES, "contact",
			analyticsConfiguration.syncedContactFieldNames(),
			FieldPeopleConstants.FIELD_CONTACT_TYPES);

		fields.addAll(
			_getFields(
				FieldPeopleConstants.FIELD_USER_EXAMPLES,
				FieldPeopleConstants.FIELD_USER_NAMES,
				FieldPeopleConstants.FIELD_USER_REQUIRED_NAMES, "user",
				analyticsConfiguration.syncedUserFieldNames(),
				FieldPeopleConstants.FIELD_USER_TYPES));

		fields.addAll(
			_getExpandoFields(
				User.class.getName(), contextCompany.getCompanyId(), "user",
				analyticsConfiguration.syncedUserFieldNames()));

		fields = _filter(fields, keyword);

		fields = _sort(fields, sorts);

		return Page.of(
			ListUtil.subList(
				fields, pagination.getStartPosition(),
				pagination.getEndPosition()),
			pagination, fields.size());
	}

	@Override
	public Page<Field> getFieldsProductsPage(
			String keyword, Pagination pagination, Sort[] sorts)
		throws Exception {

		AnalyticsConfiguration analyticsConfiguration =
			_analyticsSettingsManager.getAnalyticsConfiguration(
				contextCompany.getCompanyId());

		List<Field> fields = _getFields(
			FieldProductConstants.FIELD_CATEGORY_EXAMPLES,
			FieldProductConstants.FIELD_CATEGORY_NAMES,
			FieldProductConstants.FIELD_CATEGORY_REQUIRED_NAMES, "category",
			analyticsConfiguration.syncedCategoryFieldNames(),
			FieldProductConstants.FIELD_CATEGORY_TYPES);

		fields.addAll(
			_getFields(
				FieldProductConstants.FIELD_PRODUCT_EXAMPLES,
				FieldProductConstants.FIELD_PRODUCT_NAMES,
				FieldProductConstants.FIELD_PRODUCT_REQUIRED_NAMES, "product",
				analyticsConfiguration.syncedProductFieldNames(),
				FieldProductConstants.FIELD_PRODUCT_TYPES));
		fields.addAll(
			_getFields(
				FieldProductConstants.FIELD_PRODUCT_CHANNEL_EXAMPLES,
				FieldProductConstants.FIELD_PRODUCT_CHANNEL_NAMES,
				FieldProductConstants.FIELD_PRODUCT_CHANNEL_REQUIRED_NAMES,
				"product-channel",
				analyticsConfiguration.syncedProductChannelFieldNames(),
				FieldProductConstants.FIELD_PRODUCT_CHANNEL_TYPES));

		fields = _filter(fields, keyword);

		fields = _sort(fields, sorts);

		return Page.of(
			ListUtil.subList(
				fields, pagination.getStartPosition(),
				pagination.getEndPosition()),
			pagination, fields.size());
	}

	@Override
	public void patchFieldAccount(Field[] fields) throws Exception {
		AnalyticsConfiguration analyticsConfiguration =
			_analyticsSettingsManager.getAnalyticsConfiguration(
				contextCompany.getCompanyId());

		_analyticsSettingsManager.updateCompanyConfiguration(
			contextCompany.getCompanyId(),
			HashMapBuilder.<String, Object>put(
				"syncedAccountFieldNames",
				_updateSelectedFields(
					analyticsConfiguration.syncedAccountFieldNames(), fields,
					FieldAccountConstants.FIELD_ACCOUNT_REQUIRED_NAMES,
					"account",
					ArrayUtil.append(
						FieldAccountConstants.FIELD_ACCOUNT_NAMES,
						_getExpandoFieldNames(
							AccountEntry.class.getName(),
							contextCompany.getCompanyId())))
			).build());
	}

	@Override
	public void patchFieldOrder(Field[] fields) throws Exception {
		AnalyticsConfiguration analyticsConfiguration =
			_analyticsSettingsManager.getAnalyticsConfiguration(
				contextCompany.getCompanyId());

		_analyticsSettingsManager.updateCompanyConfiguration(
			contextCompany.getCompanyId(),
			HashMapBuilder.<String, Object>put(
				"syncedOrderFieldNames",
				_updateSelectedFields(
					analyticsConfiguration.syncedOrderFieldNames(), fields,
					FieldOrderConstants.FIELD_ORDER_REQUIRED_NAMES, "order",
					FieldOrderConstants.FIELD_ORDER_NAMES)
			).put(
				"syncedOrderItemFieldNames",
				_updateSelectedFields(
					analyticsConfiguration.syncedOrderItemFieldNames(), fields,
					FieldOrderConstants.FIELD_ORDER_ITEM_REQUIRED_NAMES,
					"order-item", FieldOrderConstants.FIELD_ORDER_ITEM_NAMES)
			).build());
	}

	@Override
	public void patchFieldPeople(Field[] fields) throws Exception {
		AnalyticsConfiguration analyticsConfiguration =
			_analyticsSettingsManager.getAnalyticsConfiguration(
				contextCompany.getCompanyId());

		_analyticsSettingsManager.updateCompanyConfiguration(
			contextCompany.getCompanyId(),
			HashMapBuilder.<String, Object>put(
				"syncedContactFieldNames",
				_updateSelectedFields(
					analyticsConfiguration.syncedContactFieldNames(), fields,
					FieldPeopleConstants.FIELD_CONTACT_REQUIRED_NAMES,
					"contact", FieldPeopleConstants.FIELD_CONTACT_NAMES)
			).put(
				"syncedUserFieldNames",
				_updateSelectedFields(
					analyticsConfiguration.syncedUserFieldNames(), fields,
					FieldPeopleConstants.FIELD_USER_REQUIRED_NAMES, "user",
					ArrayUtil.append(
						FieldPeopleConstants.FIELD_USER_NAMES,
						_getExpandoFieldNames(
							User.class.getName(),
							contextCompany.getCompanyId())))
			).build());
	}

	@Override
	public void patchFieldProduct(Field[] fields) throws Exception {
		AnalyticsConfiguration analyticsConfiguration =
			_analyticsSettingsManager.getAnalyticsConfiguration(
				contextCompany.getCompanyId());

		_analyticsSettingsManager.updateCompanyConfiguration(
			contextCompany.getCompanyId(),
			HashMapBuilder.<String, Object>put(
				"syncedCategoryFieldNames",
				_updateSelectedFields(
					analyticsConfiguration.syncedCategoryFieldNames(), fields,
					FieldProductConstants.FIELD_CATEGORY_REQUIRED_NAMES,
					"category", FieldProductConstants.FIELD_CATEGORY_NAMES)
			).put(
				"syncedProductChannelFieldNames",
				_updateSelectedFields(
					analyticsConfiguration.syncedProductChannelFieldNames(),
					fields,
					FieldProductConstants.FIELD_PRODUCT_CHANNEL_REQUIRED_NAMES,
					"product-channel",
					FieldProductConstants.FIELD_PRODUCT_CHANNEL_NAMES)
			).put(
				"syncedProductFieldNames",
				_updateSelectedFields(
					analyticsConfiguration.syncedProductFieldNames(), fields,
					FieldProductConstants.FIELD_PRODUCT_REQUIRED_NAMES,
					"product", FieldProductConstants.FIELD_PRODUCT_NAMES)
			).build());
	}

	private List<Field> _filter(List<Field> fields, String keywords) {
		if (keywords == null) {
			return fields;
		}

		return ListUtil.filter(
			fields,
			field -> {
				String name = field.getName();

				return name.matches("(?i).*" + keywords + ".*");
			});
	}

	private String _getDataType(int type) {
		if ((type == ExpandoColumnConstants.BOOLEAN) ||
			(type == ExpandoColumnConstants.BOOLEAN_ARRAY)) {

			return "Boolean";
		}
		else if ((type == ExpandoColumnConstants.DATE) ||
				 (type == ExpandoColumnConstants.DATE_ARRAY)) {

			return "Date";
		}
		else if ((type == ExpandoColumnConstants.DOUBLE) ||
				 (type == ExpandoColumnConstants.DOUBLE_ARRAY) ||
				 (type == ExpandoColumnConstants.FLOAT) ||
				 (type == ExpandoColumnConstants.FLOAT_ARRAY)) {

			return "Decimal";
		}
		else if ((type == ExpandoColumnConstants.INTEGER) ||
				 (type == ExpandoColumnConstants.INTEGER_ARRAY)) {

			return "Integer";
		}
		else if ((type == ExpandoColumnConstants.LONG) ||
				 (type == ExpandoColumnConstants.LONG_ARRAY)) {

			return "Long";
		}
		else if ((type == ExpandoColumnConstants.NUMBER) ||
				 (type == ExpandoColumnConstants.NUMBER_ARRAY) ||
				 (type == ExpandoColumnConstants.SHORT) ||
				 (type == ExpandoColumnConstants.SHORT_ARRAY)) {

			return "Number";
		}

		return "String";
	}

	private String[] _getExpandoFieldNames(String className, long companyId) {
		ExpandoTable expandoTable = _expandoTableLocalService.fetchTable(
			companyId, _portal.getClassNameId(className),
			ExpandoTableConstants.DEFAULT_TABLE_NAME);

		if (expandoTable == null) {
			return new String[0];
		}

		return transformToArray(
			_expandoColumnLocalService.getColumns(expandoTable.getTableId()),
			expandoColumn -> expandoColumn.getName(), String.class);
	}

	private List<Field> _getExpandoFields(
		String className, long companyId, String source, String[] syncedNames) {

		ExpandoTable expandoTable = _expandoTableLocalService.fetchTable(
			companyId, _portal.getClassNameId(className),
			ExpandoTableConstants.DEFAULT_TABLE_NAME);

		if (expandoTable == null) {
			return Collections.emptyList();
		}

		return transform(
			_expandoColumnLocalService.getColumns(expandoTable.getTableId()),
			expandoColumn -> {
				Field field = new Field();

				field.setName(expandoColumn.getName());
				field.setRequired(false);
				field.setSelected(
					ArrayUtil.contains(syncedNames, expandoColumn.getName()));
				field.setSource(source);
				field.setType(_getDataType(expandoColumn.getType()));

				return field;
			});
	}

	private List<Field> _getFields(
		String[] examples, String[] names, String[] requiredNames,
		String source, String[] syncedNames, String[] types) {

		List<Field> fields = new ArrayList<>();

		for (int i = 0; i < names.length; i++) {
			Field field = new Field();

			field.setExample(examples[i]);
			field.setName(names[i]);
			field.setRequired(ArrayUtil.contains(requiredNames, names[i]));
			field.setSelected(
				ArrayUtil.contains(syncedNames, names[i]) ||
				field.getRequired());
			field.setSource(source);
			field.setType(types[i]);

			fields.add(field);
		}

		return fields;
	}

	private List<Field> _sort(List<Field> fields, Sort[] sorts) {
		if (ArrayUtil.isEmpty(sorts)) {
			return fields;
		}

		Comparator<Field> fieldComparator = null;

		for (Sort sort : sorts) {
			if (!Objects.equals(sort.getFieldName(), "name") &&
				!Objects.equals(sort.getFieldName(), "type")) {

				if (_log.isWarnEnabled()) {
					_log.warn(
						"Skipping unsupported sort field: " +
							sort.getFieldName());
				}

				continue;
			}

			if (Objects.equals(sort.getFieldName(), "name")) {
				fieldComparator = Comparator.comparing(Field::getName);
			}
			else {
				fieldComparator = Comparator.comparing(Field::getType);
			}

			if (sort.isReverse()) {
				fieldComparator = fieldComparator.reversed();
			}
		}

		if (fieldComparator != null) {
			fields.sort(fieldComparator);
		}

		return fields;
	}

	private String[] _updateSelectedFields(
		String[] configurationFieldNames, Field[] fields,
		String[] requiredFieldNames, String source,
		String[] validateFieldNames) {

		Set<String> selectedFieldNames = new HashSet<>(
			Arrays.asList(configurationFieldNames));

		for (Field field : fields) {
			if (Objects.equals(source, field.getSource())) {
				if (!field.getSelected()) {
					selectedFieldNames.remove(field.getName());
				}
				else {
					if (ArrayUtil.contains(
							validateFieldNames, field.getName())) {

						selectedFieldNames.add(field.getName());
					}
				}
			}
		}

		Collections.addAll(selectedFieldNames, requiredFieldNames);

		return selectedFieldNames.toArray(new String[0]);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FieldResourceImpl.class);

	@Reference
	private AnalyticsSettingsManager _analyticsSettingsManager;

	@Reference
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	@Reference
	private Portal _portal;

}