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

package com.liferay.layout.util.structure;

import com.liferay.layout.util.constants.LayoutDataItemTypeConstants;
import com.liferay.petra.lang.HashUtil;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.Objects;

/**
 * @author Eudaldo Alonso
 */
public class FormStyledLayoutStructureItem extends StyledLayoutStructureItem {

	public static final int FORM_CONFIG_DISPLAY_PAGE_ITEM_TYPE = 1;

	public static final int FORM_CONFIG_OTHER_ITEM_TYPE = 2;

	public FormStyledLayoutStructureItem(String parentItemId) {
		super(parentItemId);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof FormStyledLayoutStructureItem)) {
			return false;
		}

		FormStyledLayoutStructureItem formStyledLayoutStructureItem =
			(FormStyledLayoutStructureItem)object;

		if (!Objects.equals(
				_classNameId, formStyledLayoutStructureItem._classNameId) ||
			!Objects.equals(
				_classTypeId, formStyledLayoutStructureItem._classTypeId) ||
			!Objects.equals(
				_formConfig, formStyledLayoutStructureItem._formConfig)) {

			return false;
		}

		return super.equals(object);
	}

	public String getAlign() {
		return _align;
	}

	public long getClassNameId() {
		return _classNameId;
	}

	public long getClassTypeId() {
		return _classTypeId;
	}

	public String getContentDisplay() {
		return _contentDisplay;
	}

	public String getDisplay() {
		return stylesJSONObject.getString("display");
	}

	public String getFlexWrap() {
		return _flexWrap;
	}

	public int getFormConfig() {
		return _formConfig;
	}

	@Override
	public JSONObject getItemConfigJSONObject() {
		JSONObject jsonObject = super.getItemConfigJSONObject();

		return jsonObject.put(
			"align", _align
		).put(
			"classNameId", _classNameId
		).put(
			"classTypeId", _classTypeId
		).put(
			"contentDisplay", _contentDisplay
		).put(
			"flexWrap", _flexWrap
		).put(
			"formConfig", _formConfig
		).put(
			"indexed", _indexed
		).put(
			"justify", _justify
		).put(
			"successMessage", _successMessageJSONObject
		).put(
			"widthType", _widthType
		);
	}

	@Override
	public String getItemType() {
		return LayoutDataItemTypeConstants.TYPE_FORM;
	}

	public String getJustify() {
		return _justify;
	}

	public JSONObject getSuccessMessageJSONObject() {
		return _successMessageJSONObject;
	}

	public String getWidthType() {
		return _widthType;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, getItemId());
	}

	public boolean isIndexed() {
		return _indexed;
	}

	public void setAlign(String align) {
		_align = align;
	}

	public void setClassNameId(long classNameId) {
		_classNameId = classNameId;
	}

	public void setClassTypeId(long classTypeId) {
		_classTypeId = classTypeId;
	}

	public void setContentDisplay(String contentDisplay) {
		_contentDisplay = contentDisplay;
	}

	public void setFlexWrap(String flexWrap) {
		_flexWrap = flexWrap;
	}

	public void setFormConfig(int formConfig) {
		_formConfig = formConfig;
	}

	public void setIndexed(boolean indexed) {
		_indexed = indexed;
	}

	public void setJustify(String justify) {
		_justify = justify;
	}

	public void setSuccessMessageJSONObject(
		JSONObject successMessageJSONObject) {

		_successMessageJSONObject = successMessageJSONObject;
	}

	public void setWidthType(String widthType) {
		_widthType = widthType;
	}

	@Override
	public void updateItemConfig(JSONObject itemConfigJSONObject) {
		super.updateItemConfig(itemConfigJSONObject);

		if (itemConfigJSONObject.has("align")) {
			setAlign(itemConfigJSONObject.getString("align"));
		}

		if (itemConfigJSONObject.has("classNameId")) {
			setClassNameId(itemConfigJSONObject.getLong("classNameId"));
		}

		if (itemConfigJSONObject.has("classTypeId")) {
			setClassTypeId(itemConfigJSONObject.getLong("classTypeId"));
		}

		if (itemConfigJSONObject.has("contentDisplay")) {
			setContentDisplay(itemConfigJSONObject.getString("contentDisplay"));
		}

		if (itemConfigJSONObject.has("flexWrap")) {
			setFlexWrap(itemConfigJSONObject.getString("flexWrap"));
		}

		if (itemConfigJSONObject.has("formConfig")) {
			setFormConfig(itemConfigJSONObject.getInt("formConfig"));
		}

		if (itemConfigJSONObject.has("justify")) {
			setJustify(itemConfigJSONObject.getString("justify"));
		}

		if (itemConfigJSONObject.has("indexed")) {
			setIndexed(itemConfigJSONObject.getBoolean("indexed"));
		}

		if (itemConfigJSONObject.has("successMessage")) {
			setSuccessMessageJSONObject(
				itemConfigJSONObject.getJSONObject("successMessage"));
		}

		if (itemConfigJSONObject.has("widthType")) {
			setWidthType(itemConfigJSONObject.getString("widthType"));
		}
	}

	private String _align = "";
	private long _classNameId;
	private long _classTypeId;
	private String _contentDisplay = "";
	private String _flexWrap = "";
	private int _formConfig;
	private boolean _indexed = true;
	private String _justify = "";
	private JSONObject _successMessageJSONObject;
	private String _widthType = "fluid";

}