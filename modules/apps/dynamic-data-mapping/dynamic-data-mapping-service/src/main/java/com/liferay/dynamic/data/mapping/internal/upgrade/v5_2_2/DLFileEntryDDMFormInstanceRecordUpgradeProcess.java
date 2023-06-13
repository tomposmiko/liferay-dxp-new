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

package com.liferay.dynamic.data.mapping.internal.upgrade.v5_2_2;

import com.liferay.dynamic.data.mapping.constants.DDMFormConstants;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Roberto Díaz
 */
public class DLFileEntryDDMFormInstanceRecordUpgradeProcess
	extends UpgradeProcess {

	public DLFileEntryDDMFormInstanceRecordUpgradeProcess(
		ClassNameLocalService classNameLocalService) {

		_classNameLocalService = classNameLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		try (PreparedStatement preparedStatement1 = connection.prepareStatement(
				StringBundler.concat(
					"select DDMFieldAttribute.largeAttributeValue, ",
					"DDMFieldAttribute.smallAttributeValue, ",
					"DDMFormInstanceRecord.formInstanceRecordId, ",
					"DDMFormInstanceRecord.companyId from DDMFieldAttribute ",
					"join DDMField on DDMFieldAttribute.fieldId = ",
					"DDMField.fieldId join DDMFormInstanceRecord on ",
					"DDMFieldAttribute.storageId = ",
					"DDMFormInstanceRecord.storageId where DDMField.fieldType ",
					"like ? and DDMFieldAttribute.attributeName like ?"));
			PreparedStatement preparedStatement2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					StringBundler.concat(
						"update DLFileEntry set classNameId = ?, classPK = ? ",
						"where fileEntryId = ? and userId = (select userId ",
						"from User_ where companyId = ? and screenName like ",
						"?)"))) {

			preparedStatement1.setString(
				1, DDMFormFieldTypeConstants.DOCUMENT_LIBRARY);
			preparedStatement1.setString(2, "fileEntryId");

			try (ResultSet resultSet = preparedStatement1.executeQuery()) {
				while (resultSet.next()) {
					String attributeValue = StringUtil.unquote(
						resultSet.getString("largeAttributeValue"));

					if ((attributeValue == null) || attributeValue.isEmpty()) {
						attributeValue = StringUtil.unquote(
							resultSet.getString("smallAttributeValue"));
					}

					long fileEntryId = GetterUtil.getLong(attributeValue);

					if (fileEntryId == 0) {
						continue;
					}

					preparedStatement2.setLong(
						1,
						_classNameLocalService.getClassNameId(
							DDMFormInstanceRecord.class.getName()));
					preparedStatement2.setLong(
						2, resultSet.getLong("formInstanceRecordId"));
					preparedStatement2.setLong(3, fileEntryId);
					preparedStatement2.setLong(
						4, resultSet.getLong("companyId"));
					preparedStatement2.setString(
						5, DDMFormConstants.DDM_FORM_DEFAULT_USER_SCREEN_NAME);

					preparedStatement2.addBatch();
				}
			}

			preparedStatement2.executeBatch();
		}
	}

	private final ClassNameLocalService _classNameLocalService;

}