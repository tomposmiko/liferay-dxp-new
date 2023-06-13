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

package com.liferay.portal.tools.sample.sql.builder;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.test.rule.LogAssertionTestRule;
import com.liferay.portal.tools.ToolDependencies;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tina Tian
 */
public class SampleSQLBuilderTest {

	@ClassRule
	public static final LogAssertionTestRule logAssertionTestRule =
		LogAssertionTestRule.INSTANCE;

	@Test
	public void testFreemarkerTemplateContent() throws Exception {
		Class<?> clazz = getClass();

		URL url = clazz.getResource(
			"/com/liferay/portal/tools/sample/sql/builder/dependencies" +
				"/sample.ftl");

		String fileContent = new String(
			Files.readAllBytes(Paths.get(url.toURI())), StringPool.UTF8);

		Assert.assertTrue(
			"sample.ftl must end with " + _SAMPLE_FTL_END,
			fileContent.endsWith(_SAMPLE_FTL_END));
	}

	@Test
	public void testGenerateAndInsertSampleSQL() throws Exception {
		ToolDependencies.wireBasic();

		DBManagerUtil.setDB(DBType.HYPERSONIC, null);

		Properties properties = new Properties();

		File tempDir = new File(
			SystemProperties.get(SystemProperties.TMP_DIR),
			String.valueOf(System.currentTimeMillis()));

		_initProperties(properties);

		File tempPropertiesFile = File.createTempFile("test", ".properties");

		try (Writer writer = new FileWriter(tempPropertiesFile)) {
			properties.store(writer, null);

			System.setProperty(
				"sample-sql-properties", tempPropertiesFile.getAbsolutePath());
			System.setProperty("user.dir", tempDir.getAbsolutePath());

			new SampleSQLBuilder();

			_loadHypersonic(tempDir.getAbsolutePath());
		}
		finally {
			FileUtil.deltree(tempDir);
		}
	}

	private void _initProperties(Properties properties) {
		properties.put(
			BenchmarksPropsKeys.COMMERCE_LAYOUT_EXCLUDED_PORTLETS,
			StringPool.BLANK);
		properties.put(BenchmarksPropsKeys.DB_TYPE, "hypersonic");
		properties.put(BenchmarksPropsKeys.MAX_ASSET_CATEGORY_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_ASSET_ENTRY_TO_ASSET_CATEGORY_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_ASSET_ENTRY_TO_ASSET_TAG_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_ASSET_TAG_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_ASSET_VUCABULARY_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_ASSETPUBLISHER_PAGE_COUNT, "2");
		properties.put(BenchmarksPropsKeys.MAX_BLOGS_ENTRY_COMMENT_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_BLOGS_ENTRY_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_COMMERCE_ACCOUNT_ENTRY_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_COMMERCE_CATALOG_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_COMMERCE_GROUP_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_COMMERCE_INVENTORY_WAREHOUSE_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_COMMERCE_INVENTORY_WAREHOUSE_ITEM_QUANTITY,
			"1");
		properties.put(
			BenchmarksPropsKeys.MAX_COMMERCE_ORDER_STATUS_CANCELLED_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_COMMERCE_ORDER_STATUS_OPEN_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_COMMERCE_ORDER_STATUS_PENDING_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_COMMERCE_PRICE_LIST_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_COMMERCE_PRODUCT_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_COMMERCE_PRODUCT_DEFINITION_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_COMMERCE_PRODUCT_INSTANCE_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_COMMERCE_PRODUCT_OPTION_CATEGORY_COUNT,
			"1");
		properties.put(BenchmarksPropsKeys.MAX_COMPANY_COUNT, "2");
		properties.put(BenchmarksPropsKeys.MAX_COMPANY_USER_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_CONTENT_LAYOUT_COUNT, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_CP_DEFINITION_ATTACHMENT_TYPE_IMAGE_COUNT,
			"1");
		properties.put(
			BenchmarksPropsKeys.MAX_CP_DEFINITION_ATTACHMENT_TYPE_PDF_COUNT,
			"1");
		properties.put(
			BenchmarksPropsKeys.
				MAX_CP_DEFINITION_SPECIFICATION_OPTION_VALUE_COUNT,
			"1");
		properties.put(
			BenchmarksPropsKeys.MAX_CP_SPECIFICATION_OPTION_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_DDL_CUSTOM_FIELD_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_DDL_RECORD_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_DDL_RECORD_SET_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_DL_FILE_ENTRY_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_DL_FILE_ENTRY_SIZE, "1");
		properties.put(BenchmarksPropsKeys.MAX_DL_FOLDER_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_DL_FOLDER_DEPTH, "1");
		properties.put(BenchmarksPropsKeys.MAX_GROUP_COUNT, "2");
		properties.put(BenchmarksPropsKeys.MAX_JOURNAL_ARTICLE_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_JOURNAL_ARTICLE_PAGE_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_JOURNAL_ARTICLE_SIZE, "1");
		properties.put(
			BenchmarksPropsKeys.MAX_JOURNAL_ARTICLE_VERSION_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_MB_CATEGORY_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_MB_MESSAGE_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_MB_THREAD_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_SEGMENTS_ENTRY_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_USER_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_USER_TO_GROUP_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_WIKI_NODE_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_WIKI_PAGE_COMMENT_COUNT, "1");
		properties.put(BenchmarksPropsKeys.MAX_WIKI_PAGE_COUNT, "1");
		properties.put(BenchmarksPropsKeys.OPTIMIZE_BUFFER_SIZE, "8192");
		properties.put(
			BenchmarksPropsKeys.OUTPUT_CSV_FILE_NAMES,
			StringBundler.concat(
				"assetPublisher,blog,company,commerceInventoryWarehouseItem,",
				"commerceOrder,commerceProduct,cpDefinition,documentLibrary,",
				"dynamicDataList,fragment,layout,mbCategory,mbThread,",
				"repository,user,wiki"));
		properties.put(BenchmarksPropsKeys.OUTPUT_MERGE, "true");
		properties.put(
			BenchmarksPropsKeys.SCRIPT,
			"com/liferay/portal/tools/sample/sql/builder/dependencies" +
				"/sample.ftl");
		properties.put(BenchmarksPropsKeys.SEARCH_BAR_ENABLED, "true");
		properties.put(BenchmarksPropsKeys.VIRTUAL_HOST_NAME, "localhost");
	}

	private void _loadHypersonic(Connection connection, String fileName)
		throws Exception {

		DB db = DBManagerUtil.getDB();

		List<String> lines = Files.readAllLines(
			Paths.get(fileName), StandardCharsets.UTF_8);

		StringBundler sb = new StringBundler(lines.size() * 2);

		for (String line : lines) {
			if (line.isEmpty() || line.startsWith(StringPool.DOUBLE_SLASH)) {
				continue;
			}

			sb.append(line);
			sb.append(StringPool.NEW_LINE);
		}

		db.runSQLTemplateString(connection, sb.toString(), true);
	}

	private void _loadHypersonic(String outputDir) throws Exception {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:hsqldb:mem:testSampleSQLBuilderDB;shutdown=true", "sa",
				"")) {

			_loadHypersonic(connection, outputDir + "/sample-hypersonic.sql");

			try (Statement statement = connection.createStatement()) {
				statement.execute("SHUTDOWN COMPACT");
			}
		}
	}

	private static final String _SAMPLE_FTL_END =
		"<#include \"counters.ftl\">\n\nCOMMIT_TRANSACTION";

}