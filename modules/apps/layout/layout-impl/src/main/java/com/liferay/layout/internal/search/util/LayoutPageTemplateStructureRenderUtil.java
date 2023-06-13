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

package com.liferay.layout.internal.search.util;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.renderer.DefaultFragmentRendererContext;
import com.liferay.fragment.renderer.FragmentRendererController;
import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.util.structure.ContainerStyledLayoutStructureItem;
import com.liferay.layout.util.structure.FormStyledLayoutStructureItem;
import com.liferay.layout.util.structure.FragmentStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lourdes Fernández Besada
 */
public class LayoutPageTemplateStructureRenderUtil {

	public static String renderLayoutContent(
		FragmentRendererController fragmentRendererController,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		LayoutPageTemplateStructure layoutPageTemplateStructure, String mode,
		Locale locale, long segmentsExperienceId) {

		if (fragmentRendererController == null) {
			return StringPool.BLANK;
		}

		String data = layoutPageTemplateStructure.getData(segmentsExperienceId);

		if (Validator.isNull(data)) {
			return StringPool.BLANK;
		}

		LayoutStructure layoutStructure = LayoutStructure.of(data);

		LayoutStructureItem layoutStructureItem =
			layoutStructure.getLayoutStructureItem(
				layoutStructure.getMainItemId());

		StringBundler sb = new StringBundler();

		_renderLayoutStructure(
			layoutStructureItem.getChildrenItemIds(),
			fragmentRendererController, httpServletRequest, httpServletResponse,
			layoutStructure, locale, mode, sb);

		return sb.toString();
	}

	private static String _renderFragmentEntryLink(
		long fragmentEntryLinkId,
		FragmentRendererController fragmentRendererController,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, String mode, Locale locale) {

		FragmentEntryLink fragmentEntryLink =
			FragmentEntryLinkLocalServiceUtil.fetchFragmentEntryLink(
				fragmentEntryLinkId);

		if (fragmentEntryLink == null) {
			return StringPool.BLANK;
		}

		DefaultFragmentRendererContext fragmentRendererContext =
			new DefaultFragmentRendererContext(fragmentEntryLink);

		fragmentRendererContext.setLocale(locale);
		fragmentRendererContext.setMode(mode);

		return fragmentRendererController.render(
			fragmentRendererContext, httpServletRequest, httpServletResponse);
	}

	private static void _renderLayoutStructure(
		List<String> childrenItemIds,
		FragmentRendererController fragmentRendererController,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		LayoutStructure layoutStructure, Locale locale, String mode,
		StringBundler sb) {

		for (String childrenItemId : childrenItemIds) {
			LayoutStructureItem layoutStructureItem =
				layoutStructure.getLayoutStructureItem(childrenItemId);

			if (layoutStructureItem instanceof
					ContainerStyledLayoutStructureItem) {

				ContainerStyledLayoutStructureItem
					containerStyledLayoutStructureItem =
						(ContainerStyledLayoutStructureItem)layoutStructureItem;

				if (!containerStyledLayoutStructureItem.isIndexed()) {
					continue;
				}
			}
			else if (layoutStructureItem instanceof
						FormStyledLayoutStructureItem) {

				FormStyledLayoutStructureItem formStyledLayoutStructureItem =
					(FormStyledLayoutStructureItem)layoutStructureItem;

				if (!formStyledLayoutStructureItem.isIndexed()) {
					continue;
				}
			}
			else if (layoutStructureItem instanceof
						FragmentStyledLayoutStructureItem) {

				FragmentStyledLayoutStructureItem
					fragmentStyledLayoutStructureItem =
						(FragmentStyledLayoutStructureItem)layoutStructureItem;

				long fragmentEntryLinkId =
					fragmentStyledLayoutStructureItem.getFragmentEntryLinkId();

				if (fragmentStyledLayoutStructureItem.isIndexed() &&
					(fragmentEntryLinkId > 0)) {

					sb.append(
						_renderFragmentEntryLink(
							fragmentStyledLayoutStructureItem.
								getFragmentEntryLinkId(),
							fragmentRendererController, httpServletRequest,
							httpServletResponse, mode, locale));
				}

				continue;
			}

			_renderLayoutStructure(
				layoutStructureItem.getChildrenItemIds(),
				fragmentRendererController, httpServletRequest,
				httpServletResponse, layoutStructure, locale, mode, sb);
		}
	}

}