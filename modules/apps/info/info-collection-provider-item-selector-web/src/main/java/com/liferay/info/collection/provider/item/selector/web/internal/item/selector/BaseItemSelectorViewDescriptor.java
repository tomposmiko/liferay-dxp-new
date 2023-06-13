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

package com.liferay.info.collection.provider.item.selector.web.internal.item.selector;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItemListBuilder;
import com.liferay.info.collection.provider.InfoCollectionProvider;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.KeyValuePairComparator;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.Objects;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dante Wang
 */
public abstract class BaseItemSelectorViewDescriptor
	<T extends InfoCollectionProvider<?>>
		implements ItemSelectorViewDescriptor<T> {

	public BaseItemSelectorViewDescriptor(
		HttpServletRequest httpServletRequest, PortletURL portletURL,
		List<T> infoCollectionProviders) {

		this.httpServletRequest = httpServletRequest;
		this.portletURL = portletURL;
		this.infoCollectionProviders = infoCollectionProviders;

		themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public String[] getDisplayViews() {
		return new String[] {"icon"};
	}

	@Override
	public List<LabelItem> getFilterLabelItems() {
		return LabelItemListBuilder.add(
			() -> Validator.isNotNull(getSelectedItemType()),
			labelItem -> {
				labelItem.putData(
					"removeLabelURL",
					PortletURLBuilder.create(
						portletURL
					).setParameter(
						"itemType", (String)null
					).buildString());
				labelItem.setDismissible(true);

				String modelResource = ResourceActionsUtil.getModelResource(
					themeDisplay.getLocale(), getSelectedItemType());

				labelItem.setLabel(
					LanguageUtil.get(themeDisplay.getLocale(), "item-type") +
						": " + modelResource);
			}
		).build();
	}

	@Override
	public List<DropdownItem> getFilterNavigationDropdownItems() {
		return DropdownItemListBuilder.addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					_getFilterTypeDropdownItems());
				dropdownGroupItem.setLabel(
					LanguageUtil.get(
						themeDisplay.getLocale(), "filter-by-item-type"));
			}
		).build();
	}

	@Override
	public boolean isShowSearch() {
		return true;
	}

	protected String getSelectedItemType() {
		if (_selectedItemType != null) {
			return _selectedItemType;
		}

		_selectedItemType = ParamUtil.getString(httpServletRequest, "itemType");

		return _selectedItemType;
	}

	protected final HttpServletRequest httpServletRequest;
	protected final List<T> infoCollectionProviders;
	protected final PortletURL portletURL;
	protected final ThemeDisplay themeDisplay;

	private List<DropdownItem> _getFilterTypeDropdownItems() {
		List<KeyValuePair> keyValuePairs = TransformUtil.transform(
			infoCollectionProviders,
			infoCollectionProvider -> {
				String collectionItemClassName =
					infoCollectionProvider.getCollectionItemClassName();

				return new KeyValuePair(
					collectionItemClassName,
					ResourceActionsUtil.getModelResource(
						themeDisplay.getLocale(), collectionItemClassName));
			});

		ListUtil.distinct(
			keyValuePairs, new KeyValuePairComparator(false, true));

		return new DropdownItemList() {
			{
				for (KeyValuePair keyValuePair : keyValuePairs) {
					add(
						dropdownItem -> {
							if (Objects.equals(
									keyValuePair.getKey(),
									getSelectedItemType())) {

								dropdownItem.setActive(true);
							}

							dropdownItem.setHref(
								portletURL, "itemType", keyValuePair.getKey());
							dropdownItem.setLabel(keyValuePair.getValue());
						});
				}
			}
		};
	}

	private String _selectedItemType;

}