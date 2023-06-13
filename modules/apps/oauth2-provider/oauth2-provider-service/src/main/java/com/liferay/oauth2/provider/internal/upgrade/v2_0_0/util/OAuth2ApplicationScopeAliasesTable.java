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

package com.liferay.oauth2.provider.internal.upgrade.v2_0_0.util;

import java.sql.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * @author	  Brian Wing Shun Chan
 * @generated
 */
public class OAuth2ApplicationScopeAliasesTable {

	public static final String TABLE_NAME = "OAuth2ApplicationScopeAliases";

	public static final Object[][] TABLE_COLUMNS = {
		{"oA2AScopeAliasesId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"oAuth2ApplicationId", Types.BIGINT}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
new HashMap<String, Integer>();

static {
TABLE_COLUMNS_MAP.put("oA2AScopeAliasesId", Types.BIGINT);

TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);

TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);

TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);

TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);

TABLE_COLUMNS_MAP.put("oAuth2ApplicationId", Types.BIGINT);

}
	public static final String TABLE_SQL_CREATE =
"create table OAuth2ApplicationScopeAliases (oA2AScopeAliasesId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,oAuth2ApplicationId LONG)";

	public static final String TABLE_SQL_DROP =
"drop table OAuth2ApplicationScopeAliases";

	public static final String[] TABLE_SQL_ADD_INDEXES = {
		"create index IX_282ECE83 on OAuth2ApplicationScopeAliases (companyId)",
		"create index IX_2F9EBCBB on OAuth2ApplicationScopeAliases (oAuth2ApplicationId)"
	};

}