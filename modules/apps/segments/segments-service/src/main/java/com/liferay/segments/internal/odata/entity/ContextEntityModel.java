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

import com.liferay.portal.odata.entity.BooleanEntityField;
import com.liferay.portal.odata.entity.CollectionEntityField;
import com.liferay.portal.odata.entity.ComplexEntityField;
import com.liferay.portal.odata.entity.DateEntityField;
import com.liferay.portal.odata.entity.DateTimeEntityField;
import com.liferay.portal.odata.entity.DoubleEntityField;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.entity.StringEntityField;
import com.liferay.segments.context.Context;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides the entity data model for the Context.
 *
 * @author Eduardo García
 * @review
 */
public class ContextEntityModel implements EntityModel {

	public static final String NAME = "Context";

	public ContextEntityModel(List<EntityField> customEntityFields) {
		_entityFieldsMap = Stream.of(
			new BooleanEntityField(
				Context.SIGNED_IN, locale -> Context.SIGNED_IN),
			new CollectionEntityField(
				new StringEntityField(
					Context.COOKIES, locale -> Context.COOKIES)),
			new ComplexEntityField("customContext", customEntityFields),
			new DateEntityField(
				Context.LOCAL_DATE, locale -> Context.LOCAL_DATE,
				locale -> Context.LOCAL_DATE),
			new DateTimeEntityField(
				Context.LAST_SIGN_IN_DATE_TIME,
				locale -> Context.LAST_SIGN_IN_DATE_TIME,
				locale -> Context.LAST_SIGN_IN_DATE_TIME),
			new DoubleEntityField(
				Context.DEVICE_SCREEN_RESOLUTION_HEIGHT,
				locale -> Context.DEVICE_SCREEN_RESOLUTION_HEIGHT),
			new DoubleEntityField(
				Context.DEVICE_SCREEN_RESOLUTION_WIDTH,
				locale -> Context.DEVICE_SCREEN_RESOLUTION_WIDTH),
			new StringEntityField(Context.BROWSER, locale -> Context.BROWSER),
			new StringEntityField(
				Context.DEVICE_BRAND, locale -> Context.DEVICE_BRAND),
			new StringEntityField(
				Context.DEVICE_MODEL, locale -> Context.DEVICE_MODEL),
			new StringEntityField(
				Context.LANGUAGE_ID, locale -> Context.LANGUAGE_ID),
			new StringEntityField(
				Context.REFERRER_URL, locale -> Context.REFERRER_URL),
			new StringEntityField(Context.URL, locale -> Context.URL),
			new StringEntityField(
				Context.USER_AGENT, locale -> Context.USER_AGENT)
		).collect(
			Collectors.toMap(EntityField::getName, Function.identity())
		);
	}

	@Override
	public Map<String, EntityField> getEntityFieldsMap() {
		return _entityFieldsMap;
	}

	@Override
	public String getName() {
		return NAME;
	}

	private final Map<String, EntityField> _entityFieldsMap;

}