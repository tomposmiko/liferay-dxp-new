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

package com.liferay.notification.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Clob;
import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;NotificationTemplate&quot; database table.
 *
 * @author Gabriel Albuquerque
 * @see NotificationTemplate
 * @generated
 */
public class NotificationTemplateTable
	extends BaseTable<NotificationTemplateTable> {

	public static final NotificationTemplateTable INSTANCE =
		new NotificationTemplateTable();

	public final Column<NotificationTemplateTable, Long> mvccVersion =
		createColumn(
			"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<NotificationTemplateTable, String> uuid = createColumn(
		"uuid_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, Long>
		notificationTemplateId = createColumn(
			"notificationTemplateId", Long.class, Types.BIGINT,
			Column.FLAG_PRIMARY);
	public final Column<NotificationTemplateTable, Long> companyId =
		createColumn(
			"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, String> userName =
		createColumn(
			"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, Date> createDate =
		createColumn(
			"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, Date> modifiedDate =
		createColumn(
			"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, String> bcc = createColumn(
		"bcc", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, Clob> body = createColumn(
		"body", Clob.class, Types.CLOB, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, String> cc = createColumn(
		"cc", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, String> description =
		createColumn(
			"description", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, String> from = createColumn(
		"from_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, String> fromName =
		createColumn(
			"fromName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, String> name = createColumn(
		"name", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, String> subject =
		createColumn(
			"subject", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<NotificationTemplateTable, String> to = createColumn(
		"to_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);

	private NotificationTemplateTable() {
		super("NotificationTemplate", NotificationTemplateTable::new);
	}

}