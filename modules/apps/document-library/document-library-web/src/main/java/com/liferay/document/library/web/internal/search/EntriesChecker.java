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

package com.liferay.document.library.web.internal.search;

import com.liferay.document.library.kernel.model.DLFileShortcut;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.ResultRow;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergio González
 */
public class EntriesChecker extends RowChecker {

	public EntriesChecker(LiferayPortletResponse liferayPortletResponse) {
		super(liferayPortletResponse);

		_liferayPortletResponse = liferayPortletResponse;
	}

	@Override
	public String getAllRowsCheckBox() {
		return null;
	}

	@Override
	public String getAllRowsCheckBox(HttpServletRequest httpServletRequest) {
		return null;
	}

	@Override
	public Map<String, Object> getData(Object object) {
		Map<String, Object> data = super.getData(object);

		if (data == null) {
			return Collections.singletonMap("modelClassName", _getName(object));
		}

		return HashMapBuilder.create(
			data
		).put(
			"modelClassName", _getName(object)
		).build();
	}

	@Override
	public String getRowCheckBox(
		HttpServletRequest httpServletRequest, boolean checked,
		boolean disabled, String primaryKey) {

		Object result = _getModel(primaryKey);

		String name = _getName(result);

		if (name == null) {
			return StringPool.BLANK;
		}

		return _getRowCheckBox(
			httpServletRequest, checked, disabled,
			_liferayPortletResponse.getNamespace() + RowChecker.ROW_IDS + name,
			primaryKey, _getEntryRowIds(), "'#" + getAllRowIds() + "'",
			_liferayPortletResponse.getNamespace() + "toggleActionsButton();",
			getData(result));
	}

	@Override
	public String getRowCheckBox(
		HttpServletRequest httpServletRequest, ResultRow resultRow) {

		Object result = resultRow.getObject();

		return getRowCheckBox(
			httpServletRequest, isChecked(result), isDisabled(result),
			resultRow.getPrimaryKey());
	}

	@Override
	protected String getOnClick(
		String checkBoxRowIds, String checkBoxAllRowIds,
		String checkBoxPostOnClick) {

		return StringPool.BLANK;
	}

	private String _getEntryRowIds() {
		return StringBundler.concat(
			"['", _liferayPortletResponse.getNamespace(), RowChecker.ROW_IDS,
			_SIMPLE_NAME_FOLDER, "', '", _liferayPortletResponse.getNamespace(),
			RowChecker.ROW_IDS, _SIMPLE_NAME_DL_FILE_SHORTCUT, "', '",
			_liferayPortletResponse.getNamespace(), RowChecker.ROW_IDS,
			_SIMPLE_NAME_FILE_ENTRY, "']");
	}

	private Object _getModel(String primaryKey) {
		long entryId = GetterUtil.getLong(primaryKey);

		try {
			return DLAppServiceUtil.getFileEntry(entryId);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		try {
			return DLAppServiceUtil.getFileShortcut(entryId);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		try {
			return DLAppServiceUtil.getFolder(entryId);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return null;
	}

	private String _getName(Object result) {
		if (result instanceof FileEntry) {
			return _SIMPLE_NAME_FILE_ENTRY;
		}
		else if (result instanceof FileShortcut) {
			return _SIMPLE_NAME_DL_FILE_SHORTCUT;
		}
		else if (result instanceof Folder) {
			return _SIMPLE_NAME_FOLDER;
		}

		return null;
	}

	private String _getRowCheckBox(
		HttpServletRequest httpServletRequest, boolean checked,
		boolean disabled, String name, String value, String checkBoxRowIds,
		String checkBoxAllRowIds, String checkBoxPostOnClick,
		Map<String, Object> data) {

		StringBundler sb = new StringBundler(16);

		sb.append("<input ");

		if (checked) {
			sb.append("checked ");
		}

		sb.append("class=\"");
		sb.append(getCssClass());
		sb.append("\" ");

		if (disabled) {
			sb.append("disabled ");
		}

		sb.append("name=\"");
		sb.append(name);
		sb.append("\" title=\"");
		sb.append(LanguageUtil.get(httpServletRequest.getLocale(), "select"));
		sb.append("\" type=\"checkbox\" value=\"");
		sb.append(HtmlUtil.escapeAttribute(value));
		sb.append("\" ");

		if (Validator.isNotNull(getAllRowIds())) {
			sb.append(
				getOnClick(
					checkBoxRowIds, checkBoxAllRowIds, checkBoxPostOnClick));
		}

		sb.append(HtmlUtil.buildData(data));
		sb.append(">");

		return sb.toString();
	}

	private static final String _SIMPLE_NAME_DL_FILE_SHORTCUT =
		DLFileShortcut.class.getSimpleName();

	private static final String _SIMPLE_NAME_FILE_ENTRY =
		FileEntry.class.getSimpleName();

	private static final String _SIMPLE_NAME_FOLDER =
		Folder.class.getSimpleName();

	private static final Log _log = LogFactoryUtil.getLog(EntriesChecker.class);

	private final LiferayPortletResponse _liferayPortletResponse;

}