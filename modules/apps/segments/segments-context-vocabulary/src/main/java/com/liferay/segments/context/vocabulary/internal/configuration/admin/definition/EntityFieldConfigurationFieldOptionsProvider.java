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

package com.liferay.segments.context.vocabulary.internal.configuration.admin.definition;

import com.liferay.configuration.admin.definition.ConfigurationFieldOptionsProvider;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.CamelCaseUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Raymond Augé
 */
@Component(
	property = {
		"configuration.field.name=entityFieldName",
		"configuration.pid=com.liferay.segments.context.vocabulary.internal.configuration.SegmentsContextVocabularyCompanyConfiguration",
		"configuration.pid=com.liferay.segments.context.vocabulary.internal.configuration.SegmentsContextVocabularyConfiguration"
	},
	service = ConfigurationFieldOptionsProvider.class
)
public class EntityFieldConfigurationFieldOptionsProvider
	implements ConfigurationFieldOptionsProvider {

	@Override
	public List<Option> getOptions() {
		return _options;
	}

	@Activate
	@Modified
	protected void activate() {
		List<Option> options = new ArrayList<>();

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		for (EntityField entityField : entityFieldsMap.values()) {
			if (Objects.equals(
					entityField.getType(), EntityField.Type.STRING)) {

				options.add(_toOption(entityField.getName()));
			}
		}

		Collections.sort(options, Comparator.comparing(Option::getValue));

		_options = options;
	}

	@Deactivate
	protected void deactivate() {
		_options = Collections.emptyList();
	}

	private Option _toOption(String value) {
		return new Option() {

			@Override
			public String getLabel(Locale locale) {
				ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
					"content.Language", locale, getClass());

				return _language.get(
					resourceBundle,
					"field." + CamelCaseUtil.fromCamelCase(value));
			}

			@Override
			public String getValue() {
				return value;
			}

		};
	}

	@Reference(target = "(entity.model.name=Context)")
	private EntityModel _entityModel;

	@Reference
	private Language _language;

	private volatile List<Option> _options;

}