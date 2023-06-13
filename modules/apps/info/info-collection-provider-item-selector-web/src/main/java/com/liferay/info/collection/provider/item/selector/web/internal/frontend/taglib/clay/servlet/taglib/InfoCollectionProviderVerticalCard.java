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

package com.liferay.info.collection.provider.item.selector.web.internal.frontend.taglib.clay.servlet.taglib;

import com.liferay.frontend.taglib.clay.servlet.taglib.BaseVerticalCard;
import com.liferay.info.collection.provider.FilteredInfoCollectionProvider;
import com.liferay.info.collection.provider.InfoCollectionProvider;
import com.liferay.info.collection.provider.SingleFormVariationInfoCollectionProvider;
import com.liferay.info.item.InfoItemFormVariation;
import com.liferay.info.item.InfoItemServiceTracker;
import com.liferay.info.item.provider.InfoItemFormVariationsProvider;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.RenderRequest;

/**
 * @author Eudaldo Alonso
 */
public class InfoCollectionProviderVerticalCard extends BaseVerticalCard {

	public InfoCollectionProviderVerticalCard(
		InfoCollectionProvider<?> infoCollectionProvider,
		InfoItemServiceTracker infoItemServiceTracker,
		RenderRequest renderRequest, RowChecker rowChecker) {

		super(null, renderRequest, rowChecker);

		_infoCollectionProvider = infoCollectionProvider;
		_infoItemServiceTracker = infoItemServiceTracker;
	}

	@Override
	public String getCssClass() {
		return "card-interactive card-interactive-secondary";
	}

	@Override
	public String getIcon() {
		return "list";
	}

	@Override
	public String getInputValue() {
		return null;
	}

	@Override
	public String getStickerIcon() {
		if (_infoCollectionProvider instanceof FilteredInfoCollectionProvider) {
			return "filter";
		}

		return super.getStickerIcon();
	}

	@Override
	public String getSubtitle() {
		String subtitle = _getSubtitle();

		if (Validator.isNull(subtitle)) {
			return _getTitle();
		}

		return _getTitle() + " - " + subtitle;
	}

	@Override
	public String getTitle() {
		return _infoCollectionProvider.getLabel(themeDisplay.getLocale());
	}

	@Override
	public Boolean isFlushHorizontal() {
		return true;
	}

	private String _getSubtitle() {
		String className = _infoCollectionProvider.getCollectionItemClassName();

		if (Validator.isNull(className) ||
			!(_infoCollectionProvider instanceof
				SingleFormVariationInfoCollectionProvider)) {

			return StringPool.BLANK;
		}

		InfoItemFormVariationsProvider<?> infoItemFormVariationsProvider =
			_infoItemServiceTracker.getFirstInfoItemService(
				InfoItemFormVariationsProvider.class, className);

		if (infoItemFormVariationsProvider == null) {
			return StringPool.BLANK;
		}

		SingleFormVariationInfoCollectionProvider<?>
			singleFormVariationInfoCollectionProvider =
				(SingleFormVariationInfoCollectionProvider<?>)
					_infoCollectionProvider;

		InfoItemFormVariation infoItemFormVariation =
			infoItemFormVariationsProvider.getInfoItemFormVariation(
				themeDisplay.getScopeGroupId(),
				singleFormVariationInfoCollectionProvider.
					getFormVariationKey());

		if (infoItemFormVariation == null) {
			return StringPool.BLANK;
		}

		return infoItemFormVariation.getLabel(themeDisplay.getLocale());
	}

	private String _getTitle() {
		String className = _infoCollectionProvider.getCollectionItemClassName();

		if (Validator.isNull(className)) {
			return StringPool.BLANK;
		}

		return ResourceActionsUtil.getModelResource(
			themeDisplay.getLocale(), className);
	}

	private final InfoCollectionProvider<?> _infoCollectionProvider;
	private final InfoItemServiceTracker _infoItemServiceTracker;

}