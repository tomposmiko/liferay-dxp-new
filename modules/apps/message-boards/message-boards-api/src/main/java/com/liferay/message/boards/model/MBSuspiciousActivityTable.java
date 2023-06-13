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

package com.liferay.message.boards.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;MBSuspiciousActivity&quot; database table.
 *
 * @author Brian Wing Shun Chan
 * @see MBSuspiciousActivity
 * @generated
 */
public class MBSuspiciousActivityTable
	extends BaseTable<MBSuspiciousActivityTable> {

	public static final MBSuspiciousActivityTable INSTANCE =
		new MBSuspiciousActivityTable();

	public final Column<MBSuspiciousActivityTable, Long> mvccVersion =
		createColumn(
			"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<MBSuspiciousActivityTable, Long> ctCollectionId =
		createColumn(
			"ctCollectionId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<MBSuspiciousActivityTable, String> uuid = createColumn(
		"uuid_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, Long> suspiciousActivityId =
		createColumn(
			"suspiciousActivityId", Long.class, Types.BIGINT,
			Column.FLAG_PRIMARY);
	public final Column<MBSuspiciousActivityTable, Long> groupId = createColumn(
		"groupId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, Long> companyId =
		createColumn(
			"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, String> userName =
		createColumn(
			"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, Date> createDate =
		createColumn(
			"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, Date> modifiedDate =
		createColumn(
			"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, Long> messageId =
		createColumn(
			"messageId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, Long> threadId =
		createColumn("threadId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, String> description =
		createColumn(
			"description", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, String> type = createColumn(
		"type_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<MBSuspiciousActivityTable, Boolean> validated =
		createColumn(
			"validated", Boolean.class, Types.BOOLEAN, Column.FLAG_DEFAULT);

	private MBSuspiciousActivityTable() {
		super("MBSuspiciousActivity", MBSuspiciousActivityTable::new);
	}

}