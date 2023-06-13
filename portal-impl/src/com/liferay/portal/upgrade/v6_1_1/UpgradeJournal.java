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

package com.liferay.portal.upgrade.v6_1_1;

import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.util.PropsValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Objects;

/**
 * @author Brian Wing Shun Chan
 */
public class UpgradeJournal extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		try (PreparedStatement preparedStatement1 = connection.prepareStatement(
				"select id_, content from JournalArticle");
			ResultSet resultSet = preparedStatement1.executeQuery();
			PreparedStatement preparedStatement2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update JournalArticle set content = ? where id_ = ?")) {

			while (resultSet.next()) {
				long id = resultSet.getLong("id_");
				String content = resultSet.getString("content");

				String upgradedContent = _upgradeContent(id, content);

				if (!Objects.equals(content, upgradedContent)) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							"Fixing invalid content in journal article " + id);
					}

					preparedStatement2.setString(1, upgradedContent);
					preparedStatement2.setLong(2, id);

					preparedStatement2.addBatch();
				}
			}

			preparedStatement2.executeBatch();
		}
	}

	private String _upgradeContent(long id, String content) throws Exception {

		// LPS-23332 and LPS-26009

		if (!content.contains("<dynamic-content") &&
			!content.contains("</dynamic-content>")) {

			return content;
		}

		content = HtmlUtil.unescape(content);

		content = StringUtil.replace(
			content, new String[] {"<dynamic-content>", "</dynamic-content>"},
			new String[] {
				"<dynamic-content><![CDATA[", "]]></dynamic-content>"
			});

		content = StringUtil.replace(
			content,
			new String[] {
				"<dynamic-content><![CDATA[<![CDATA[",
				"]]>]]></dynamic-content>"
			},
			new String[] {
				"<dynamic-content><![CDATA[", "]]></dynamic-content>"
			});

		content = content.replaceAll(
			"<dynamic-content id=\"([0-9]+)\">",
			"<dynamic-content id=\"$1\"><![CDATA[");

		content = content.replaceAll(
			"<dynamic-content id=\"([0-9]+)\"><!\\[CDATA\\[<!\\[CDATA\\[",
			"<dynamic-content id=\"$1\"><![CDATA[");

		for (String languageId : PropsValues.LOCALES) {
			content = StringUtil.replace(
				content, "<dynamic-content language-id=\"" + languageId + "\">",
				"<dynamic-content language-id=\"" + languageId +
					"\"><![CDATA[");

			content = StringUtil.replace(
				content,
				"<dynamic-content language-id=\"" + languageId +
					"\"><![CDATA[<![CDATA[",
				"<dynamic-content language-id=\"" + languageId +
					"\"><![CDATA[");
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Upgraded dynamic content: " + content);
		}

		try {
			SAXReaderUtil.read(content);
		}
		catch (Exception exception) {
			_log.error(
				StringBundler.concat(
					"Journal article ", String.valueOf(id),
					" has invalid content due to: ", exception.getMessage()));
		}

		return content;
	}

	private static final Log _log = LogFactoryUtil.getLog(UpgradeJournal.class);

}