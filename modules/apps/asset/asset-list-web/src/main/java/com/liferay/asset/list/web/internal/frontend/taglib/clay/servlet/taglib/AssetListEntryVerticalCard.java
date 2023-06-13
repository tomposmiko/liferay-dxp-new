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

package com.liferay.asset.list.web.internal.frontend.taglib.clay.servlet.taglib;

import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.asset.list.web.internal.display.context.AssetListDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.BaseVerticalCard;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.List;

import javax.portlet.RenderRequest;

/**
 * @author Jürgen Kappler
 */
public class AssetListEntryVerticalCard extends BaseVerticalCard {

	public AssetListEntryVerticalCard(
		AssetListDisplayContext assetListDisplayContext,
		AssetListEntry assetListEntry, RenderRequest renderRequest,
		RowChecker rowChecker) {

		super(assetListEntry, renderRequest, rowChecker);

		_assetListDisplayContext = assetListDisplayContext;
		_assetListEntry = assetListEntry;
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		return _assetListDisplayContext.getActionDropdownItems(_assetListEntry);
	}

	@Override
	public String getCssClass() {
		return "card-interactive card-interactive-secondary";
	}

	@Override
	public String getHref() {
		try {
			return _assetListDisplayContext.getEditURL(_assetListEntry);
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		return StringPool.BLANK;
	}

	@Override
	public String getIcon() {
		if (_assetListEntry.getType() == 0) {
			return "bolt";
		}

		return "list";
	}

	@Override
	public String getInputValue() {
		return null;
	}

	@Override
	public String getStickerIcon() {
		return StringPool.BLANK;
	}

	@Override
	public String getSubtitle() {
		return _assetListDisplayContext.getAssetEntrySubtypeLabel(
			_assetListEntry);
	}

	@Override
	public String getTitle() {
		return _assetListEntry.getTitle();
	}

	@Override
	public Boolean isFlushHorizontal() {
		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AssetListEntryVerticalCard.class);

	private final AssetListDisplayContext _assetListDisplayContext;
	private final AssetListEntry _assetListEntry;

}