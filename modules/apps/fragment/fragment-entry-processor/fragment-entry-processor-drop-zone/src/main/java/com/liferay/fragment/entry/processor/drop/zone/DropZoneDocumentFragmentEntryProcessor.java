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

package com.liferay.fragment.entry.processor.drop.zone;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.DocumentFragmentEntryProcessor;
import com.liferay.fragment.processor.FragmentEntryProcessorContext;
import com.liferay.fragment.renderer.FragmentDropZoneRenderer;
import com.liferay.info.constants.InfoDisplayWebKeys;
import com.liferay.layout.constants.LayoutWebKeys;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.util.structure.FragmentDropZoneLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	property = "fragment.entry.processor.priority:Integer=6",
	service = DocumentFragmentEntryProcessor.class
)
public class DropZoneDocumentFragmentEntryProcessor
	implements DocumentFragmentEntryProcessor {

	@Override
	public void processFragmentEntryLinkHTML(
			FragmentEntryLink fragmentEntryLink, Document document,
			FragmentEntryProcessorContext fragmentEntryProcessorContext)
		throws PortalException {

		Elements elements = document.select("lfr-drop-zone");

		if (elements.size() <= 0) {
			return;
		}

		HttpServletRequest httpServletRequest =
			fragmentEntryProcessorContext.getHttpServletRequest();

		LayoutStructure layoutStructure = null;

		if (httpServletRequest != null) {
			layoutStructure = (LayoutStructure)httpServletRequest.getAttribute(
				LayoutWebKeys.LAYOUT_STRUCTURE);
		}

		if (layoutStructure == null) {
			LayoutPageTemplateStructure layoutPageTemplateStructure =
				_layoutPageTemplateStructureLocalService.
					fetchLayoutPageTemplateStructure(
						fragmentEntryLink.getGroupId(),
						fragmentEntryLink.getPlid());

			if (layoutPageTemplateStructure == null) {
				return;
			}

			layoutStructure = LayoutStructure.of(
				layoutPageTemplateStructure.getData(
					fragmentEntryLink.getSegmentsExperienceId()));
		}

		LayoutStructureItem layoutStructureItem =
			layoutStructure.getLayoutStructureItemByFragmentEntryLinkId(
				fragmentEntryLink.getFragmentEntryLinkId());

		if (layoutStructureItem == null) {
			return;
		}

		if (httpServletRequest != null) {
			httpServletRequest.setAttribute(
				InfoDisplayWebKeys.INFO_FORM,
				fragmentEntryProcessorContext.getInfoForm());
		}

		List<String> dropZoneItemIds = layoutStructureItem.getChildrenItemIds();

		if (fragmentEntryProcessorContext.isEditMode()) {
			boolean idsAvailable = true;

			for (Element element : elements) {
				String dropZoneId = element.attr("data-lfr-drop-zone-id");

				if (Validator.isBlank(dropZoneId)) {
					idsAvailable = false;

					break;
				}
			}

			if (!idsAvailable) {
				for (int i = 0;
					 (i < dropZoneItemIds.size()) && (i < elements.size());
					 i++) {

					Element element = elements.get(i);

					element.attr("uuid", dropZoneItemIds.get(i));
				}
			}
			else {
				Map<String, String> fragmentDropZoneIdsMap =
					new LinkedHashMap<>();

				List<String> noFragmentDropZoneItemIds = new LinkedList<>();

				for (String dropZoneItemId : dropZoneItemIds) {
					LayoutStructureItem childLayoutStructureItem =
						layoutStructure.getLayoutStructureItem(dropZoneItemId);

					if (!(childLayoutStructureItem instanceof
							FragmentDropZoneLayoutStructureItem)) {

						continue;
					}

					FragmentDropZoneLayoutStructureItem
						fragmentDropZoneLayoutStructureItem =
							(FragmentDropZoneLayoutStructureItem)
								childLayoutStructureItem;

					String fragmentDropZoneId =
						fragmentDropZoneLayoutStructureItem.
							getFragmentDropZoneId();

					if (Validator.isBlank(fragmentDropZoneId)) {
						noFragmentDropZoneItemIds.add(dropZoneItemId);
					}
					else {
						fragmentDropZoneIdsMap.put(
							fragmentDropZoneId, dropZoneItemId);
					}
				}

				for (int i = 0; i < elements.size(); i++) {
					Element element = elements.get(i);

					String dropZoneId = element.attr("data-lfr-drop-zone-id");

					if (fragmentDropZoneIdsMap.containsKey(dropZoneId)) {
						element.attr(
							"uuid", fragmentDropZoneIdsMap.get(dropZoneId));
					}
					else if (ListUtil.isNotEmpty(noFragmentDropZoneItemIds)) {
						element.attr(
							"uuid", noFragmentDropZoneItemIds.remove(0));
					}
				}
			}

			return;
		}

		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);

			String dropZoneHTML = _fragmentDropZoneRenderer.renderDropZone(
				fragmentEntryProcessorContext.getHttpServletRequest(),
				fragmentEntryProcessorContext.getHttpServletResponse(),
				dropZoneItemIds.get(i), fragmentEntryProcessorContext.getMode(),
				true);

			Element dropZoneElement = new Element("div");

			dropZoneElement.html(dropZoneHTML);

			element.replaceWith(dropZoneElement);
		}

		if (httpServletRequest != null) {
			httpServletRequest.removeAttribute(InfoDisplayWebKeys.INFO_FORM);
		}
	}

	@Reference
	private FragmentDropZoneRenderer _fragmentDropZoneRenderer;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

}