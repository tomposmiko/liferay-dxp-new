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

package com.liferay.product.navigation.taglib.display.context;

import com.liferay.product.navigation.control.menu.ProductNavigationControlMenuCategory;
import com.liferay.product.navigation.control.menu.ProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.constants.ProductNavigationControlMenuCategoryKeys;
import com.liferay.product.navigation.control.menu.util.ProductNavigationControlMenuCategoryRegistry;
import com.liferay.product.navigation.control.menu.util.ProductNavigationControlMenuEntryRegistry;
import com.liferay.product.navigation.taglib.internal.servlet.ServletContextUtil;
import com.liferay.taglib.servlet.PipingServletResponseFactory;
import com.liferay.taglib.ui.IconTag;

import java.io.Writer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Víctor Galán
 */
public class ProductNavigationControlMenuTagDisplayContext {

	public ProductNavigationControlMenuTagDisplayContext(
		HttpServletRequest httpServletRequest, PageContext pageContext) {

		_httpServletRequest = httpServletRequest;
		_pageContext = pageContext;
	}

	public Map<String, List<ProductNavigationControlMenuEntry>>
		getProductNavigationControlMenuEntriesMap() {

		if (_productNavigationControlMenuEntriesMap != null) {
			return _productNavigationControlMenuEntriesMap;
		}

		Map<String, List<ProductNavigationControlMenuEntry>>
			productNavigationControlMenuEntriesMap = new LinkedHashMap<>();

		ProductNavigationControlMenuCategoryRegistry
			productNavigationControlMenuCategoryRegistry =
				ServletContextUtil.
					getProductNavigationControlMenuCategoryRegistry();

		List<ProductNavigationControlMenuCategory>
			productNavigationControlMenuCategories =
				productNavigationControlMenuCategoryRegistry.
					getProductNavigationControlMenuCategories(
						ProductNavigationControlMenuCategoryKeys.ROOT);

		for (ProductNavigationControlMenuCategory
				productNavigationControlMenuCategory :
					productNavigationControlMenuCategories) {

			ProductNavigationControlMenuEntryRegistry
				productNavigationControlMenuEntryRegistry =
					ServletContextUtil.
						getProductNavigationControlMenuEntryRegistry();

			List<ProductNavigationControlMenuEntry>
				productNavigationControlMenuEntries =
					productNavigationControlMenuEntryRegistry.
						getProductNavigationControlMenuEntries(
							productNavigationControlMenuCategory,
							_httpServletRequest);

			productNavigationControlMenuEntriesMap.put(
				productNavigationControlMenuCategory.getKey(),
				productNavigationControlMenuEntries);
		}

		_productNavigationControlMenuEntriesMap =
			productNavigationControlMenuEntriesMap;

		return _productNavigationControlMenuEntriesMap;
	}

	public boolean hasControlMenuEntries() {
		Map<String, List<ProductNavigationControlMenuEntry>>
			productNavigationControlMenuEntriesMap =
				getProductNavigationControlMenuEntriesMap();

		for (Map.Entry<String, List<ProductNavigationControlMenuEntry>> entry :
				productNavigationControlMenuEntriesMap.entrySet()) {

			List<ProductNavigationControlMenuEntry>
				productNavigationControlMenuEntries = entry.getValue();

			if (!productNavigationControlMenuEntries.isEmpty()) {
				for (ProductNavigationControlMenuEntry
						productNavigationControlMenuEntry :
							productNavigationControlMenuEntries) {

					if (productNavigationControlMenuEntry.isRelevant(
							_httpServletRequest)) {

						return true;
					}
				}
			}
		}

		return false;
	}

	public void writeProductNavigationControlMenuEntries(Writer writer)
		throws Exception {

		Map<String, List<ProductNavigationControlMenuEntry>>
			productNavigationControlMenuEntriesMap =
				getProductNavigationControlMenuEntriesMap();

		List<ProductNavigationControlMenuEntry>
			productNavigationControlMenuEntries =
				productNavigationControlMenuEntriesMap.get(
					ProductNavigationControlMenuCategoryKeys.SITES);

		_writeProductNavigationControlMenuCategory(
			ProductNavigationControlMenuCategoryKeys.SITES,
			productNavigationControlMenuEntries, true, writer);

		productNavigationControlMenuEntries =
			productNavigationControlMenuEntriesMap.get(
				ProductNavigationControlMenuCategoryKeys.TOOLS);

		_writeProductNavigationControlMenuCategory(
			ProductNavigationControlMenuCategoryKeys.TOOLS,
			productNavigationControlMenuEntries, false, writer);

		for (Map.Entry<String, List<ProductNavigationControlMenuEntry>> entry :
				productNavigationControlMenuEntriesMap.entrySet()) {

			if (Objects.equals(
					entry.getKey(),
					ProductNavigationControlMenuCategoryKeys.SITES) ||
				Objects.equals(
					entry.getKey(),
					ProductNavigationControlMenuCategoryKeys.TOOLS)) {

				continue;
			}

			_writeProductNavigationControlMenuCategory(
				entry.getKey(), entry.getValue(), true, writer);
		}
	}

	private void _writeProductNavigationControlMenuCategory(
			String key,
			List<ProductNavigationControlMenuEntry>
				productNavigationControlMenuEntries,
			boolean useList, Writer writer)
		throws Exception {

		writer.append("<div class=\"control-menu-nav-category ");
		writer.append(key);
		writer.append("-control-group\">");

		if (useList) {
			writer.append("<ul ");
		}
		else {
			writer.append("<div ");
		}

		writer.append("class=\"control-menu-nav\">");

		for (ProductNavigationControlMenuEntry
				productNavigationControlMenuEntry :
					productNavigationControlMenuEntries) {

			_writeProductNavigationControlMenuEntry(
				productNavigationControlMenuEntry, useList, writer);
		}

		if (useList) {
			writer.append("</ul>");
		}
		else {
			writer.append("</div>");
		}

		writer.append("</div>");
	}

	private void _writeProductNavigationControlMenuEntry(
			ProductNavigationControlMenuEntry productNavigationControlMenuEntry,
			boolean useList, Writer writer)
		throws Exception {

		if (productNavigationControlMenuEntry.includeIcon(
				_httpServletRequest,
				PipingServletResponseFactory.createPipingServletResponse(
					_pageContext))) {

			return;
		}

		if (useList) {
			writer.append("<li ");
		}
		else {
			writer.append("<div ");
		}

		writer.append("class=\"control-menu-nav-item\">");

		IconTag iconTag = new IconTag();

		iconTag.setData(
			productNavigationControlMenuEntry.getData(_httpServletRequest));
		iconTag.setIcon(
			productNavigationControlMenuEntry.getIcon(_httpServletRequest));
		iconTag.setIconCssClass(
			productNavigationControlMenuEntry.getIconCssClass(
				_httpServletRequest));
		iconTag.setLabel(false);

		String linkCssClass = productNavigationControlMenuEntry.getLinkCssClass(
			_httpServletRequest);

		iconTag.setLinkCssClass("control-menu-icon " + linkCssClass);

		iconTag.setMessage(
			productNavigationControlMenuEntry.getLabel(
				_httpServletRequest.getLocale()));
		iconTag.setMethod("get");
		iconTag.setUrl(
			productNavigationControlMenuEntry.getURL(_httpServletRequest));

		writer.append(iconTag.doTagAsString(_pageContext));

		if (useList) {
			writer.append("</li>");
		}
		else {
			writer.append("</div>");
		}
	}

	private final HttpServletRequest _httpServletRequest;
	private final PageContext _pageContext;
	private Map<String, List<ProductNavigationControlMenuEntry>>
		_productNavigationControlMenuEntriesMap;

}