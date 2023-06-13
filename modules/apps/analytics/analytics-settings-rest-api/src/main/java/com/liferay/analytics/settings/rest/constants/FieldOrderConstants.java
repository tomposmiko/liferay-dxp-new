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

package com.liferay.analytics.settings.rest.constants;

/**
 * @author Riccardo Ferrari
 */
public class FieldOrderConstants {

	public static final String[] FIELD_ORDER_EXAMPLES = {
		"30130", "30130", "2017-07-21", "USD", "key1=value1, key2=value2, ...",
		"AB-34098-789-N", "30130", "2017-08-21", "2017-07-21",
		"[item, item,...]", "0", "AB-34098-789-N", "30130", "paypal", "0", "0",
		"113", "12345"
	};

	public static final String[] FIELD_ORDER_ITEM_EXAMPLES = {
		"12345", "2017-07-21", "key1=value1, key2=value2, ...",
		"AB-34098-789-N", "200", "30130", "2017-07-21", "Hand Saw", "", "30128",
		"30128", "2", "12341234", "True", "pc", "101", "12345"
	};

	public static final String[] FIELD_ORDER_ITEM_NAMES = {
		"cpDefinitionId", "createDate", "customFields", "externalReferenceCode",
		"finalPrice", "id", "modifiedDate", "name", "options", "orderId",
		"parentOrderItemId", "quantity", "sku", "subscription", "unitOfMeasure",
		"unitPrice", "userId"
	};

	public static final String[] FIELD_ORDER_ITEM_REQUIRED_NAMES = {
		"cpDefinitionId", "createDate", "customFields", "externalReferenceCode",
		"finalPrice", "id", "modifiedDate", "name", "options", "orderId",
		"parentOrderItemId", "quantity", "sku", "subscription", "unitOfMeasure",
		"unitPrice", "userId"
	};

	public static final String[] FIELD_ORDER_ITEM_TYPES = {
		"Integer", "String", "Object", "String", "Number", "Integer", "String",
		"Object", "String", "Integer", "Integer", "Integer", "String",
		"Boolean", "String", "Number", "Integer"
	};

	public static final String[] FIELD_ORDER_NAMES = {
		"accountId", "channelId", "createDate", "currencyCode", "customFields",
		"externalReferenceCode", "id", "modifiedDate", "orderDate",
		"orderItems", "orderStatus", "orderTypeExternalReferenceCode",
		"orderTypeId", "paymentMethod", "paymentStatus", "status", "total",
		"userId"
	};

	public static final String[] FIELD_ORDER_REQUIRED_NAMES = {
		"accountId", "channelId", "createDate", "currencyCode", "customFields",
		"externalReferenceCode", "id", "modifiedDate", "orderDate",
		"orderItems", "orderStatus", "orderTypeExternalReferenceCode",
		"orderTypeId", "paymentMethod", "paymentStatus", "status", "total",
		"userId"
	};

	public static final String[] FIELD_ORDER_TYPES = {
		"Integer", "Integer", "String", "String", "Object", "String", "Integer",
		"String", "String", "Array", "Integer", "String", "Integer", "String",
		"Integer", "Integer", "BigDecimal", "Integer"
	};

}