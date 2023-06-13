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
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;

/**
 * @author Eudaldo Alonso
 */
public class FragmentDropZoneLayoutStructureItem extends LayoutStructureItem {

	public FragmentDropZoneLayoutStructureItem(String parentItemId) {
		super(parentItemId);
	}

	public String getFragmentDropZoneId() {
		return _fragmentDropZoneId;
	}

	@Override
	public JSONObject getItemConfigJSONObject() {
		return JSONUtil.put("fragmentDropZoneId", _fragmentDropZoneId);
	}

	@Override
	public String getItemType() {
		return LayoutDataItemTypeConstants.TYPE_FRAGMENT_DROP_ZONE;
	}

	public void setFragmentDropZoneId(String fragmentDropZoneId) {
		_fragmentDropZoneId = fragmentDropZoneId;
	}

	@Override
	public void updateItemConfig(JSONObject itemConfigJSONObject) {
		if (itemConfigJSONObject.has("fragmentDropZoneId")) {
			setFragmentDropZoneId(
				itemConfigJSONObject.getString("fragmentDropZoneId"));
		}
	}

	private String _fragmentDropZoneId;

}