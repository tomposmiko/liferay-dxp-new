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

package com.liferay.portal.upgrade.v7_1_x;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.upgrade.BaseUpgradeDBColumnSizeTestCase;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;

/**
 * @author Preston Crary
 */
public class UpgradeDB2Test extends BaseUpgradeDBColumnSizeTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		DB db = DBManagerUtil.getDB();

		Assume.assumeTrue(DBType.DB2.equals(db.getDBType()));
	}

	@Override
	protected String getCreateTestTableSQL() {
		return "create table TestTable (testTableId int not null primary " +
			"key, testValue varchar(750) null)";
	}

	@Override
	protected int getInitialSize() {
		return 750;
	}

	@Override
	protected String getTypeName() {
		return "varchar";
	}

	@Override
	protected UpgradeDB2 getUpgradeProcess() {
		return new UpgradeDB2();
	}

}