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

package com.liferay.layout.content.page.editor.web.internal.display.context;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.ClassType;
import com.liferay.asset.kernel.model.ClassTypeReader;
import com.liferay.fragment.renderer.FragmentRendererController;
import com.liferay.info.display.contributor.InfoDisplayContributor;
import com.liferay.layout.content.page.editor.sidebar.panel.ContentPageEditorSidebarPanel;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.template.soy.util.SoyContext;
import com.liferay.portal.template.soy.util.SoyContextFactoryUtil;

import java.util.List;

import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class ContentPageEditorLayoutPageTemplateDisplayContext
	extends ContentPageEditorDisplayContext {

	public ContentPageEditorLayoutPageTemplateDisplayContext(
		HttpServletRequest httpServletRequest, RenderResponse renderResponse,
		boolean pageIsDisplayPage, CommentManager commentManager,
		List<ContentPageEditorSidebarPanel> contentPageEditorSidebarPanels,
		FragmentRendererController fragmentRendererController) {

		super(
			httpServletRequest, renderResponse, commentManager,
			contentPageEditorSidebarPanels, fragmentRendererController);

		_pageIsDisplayPage = pageIsDisplayPage;
	}

	@Override
	public SoyContext getEditorSoyContext() throws Exception {
		if (_editorSoyContext != null) {
			return _editorSoyContext;
		}

		SoyContext soyContext = super.getEditorSoyContext();

		soyContext.put("lastSaveDate", StringPool.BLANK);

		if (_pageIsDisplayPage) {
			soyContext.put(
				"mappingFieldsURL",
				getFragmentEntryActionURL(
					"/content_layout/get_mapping_fields"));
		}

		soyContext.put(
			"publishURL",
			getFragmentEntryActionURL(
				"/content_layout/publish_layout_page_template_entry"));

		if (_pageIsDisplayPage) {
			soyContext.put("selectedMappingTypes", _getSelectedMappingTypes());
		}

		soyContext.put(
			"sidebarPanels", getSidebarPanelSoyContexts(_pageIsDisplayPage));

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry();

		if ((layoutPageTemplateEntry != null) &&
			(layoutPageTemplateEntry.getStatus() !=
				WorkflowConstants.STATUS_APPROVED)) {

			String statusLabel = WorkflowConstants.getStatusLabel(
				layoutPageTemplateEntry.getStatus());

			soyContext.put("status", LanguageUtil.get(request, statusLabel));
		}

		_editorSoyContext = soyContext;

		return _editorSoyContext;
	}

	@Override
	public SoyContext getFragmentsEditorToolbarSoyContext()
		throws PortalException {

		if (_fragmentsEditorToolbarSoyContext != null) {
			return _fragmentsEditorToolbarSoyContext;
		}

		_fragmentsEditorToolbarSoyContext =
			super.getFragmentsEditorToolbarSoyContext();

		return _fragmentsEditorToolbarSoyContext;
	}

	private LayoutPageTemplateEntry _getLayoutPageTemplateEntry()
		throws PortalException {

		if (_layoutPageTemplateEntry != null) {
			return _layoutPageTemplateEntry;
		}

		Layout draftLayout = themeDisplay.getLayout();

		Layout layout = LayoutLocalServiceUtil.fetchLayout(
			draftLayout.getClassPK());

		if (layout == null) {
			return null;
		}

		_layoutPageTemplateEntry =
			LayoutPageTemplateEntryLocalServiceUtil.
				fetchLayoutPageTemplateEntryByPlid(layout.getPlid());

		return _layoutPageTemplateEntry;
	}

	private String _getMappingSubtypeLabel() throws PortalException {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry();

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				layoutPageTemplateEntry.getClassName());

		if ((assetRendererFactory == null) ||
			!assetRendererFactory.isSupportsClassTypes()) {

			return null;
		}

		ClassTypeReader classTypeReader =
			assetRendererFactory.getClassTypeReader();

		ClassType classType = classTypeReader.getClassType(
			layoutPageTemplateEntry.getClassTypeId(), themeDisplay.getLocale());

		return classType.getName();
	}

	private String _getMappingTypeLabel() throws PortalException {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry();

		InfoDisplayContributor infoDisplayContributor =
			infoDisplayContributorTracker.getInfoDisplayContributor(
				layoutPageTemplateEntry.getClassName());

		if (infoDisplayContributor == null) {
			return null;
		}

		return infoDisplayContributor.getLabel(themeDisplay.getLocale());
	}

	private SoyContext _getSelectedMappingTypes() throws PortalException {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry();

		if ((layoutPageTemplateEntry == null) ||
			(layoutPageTemplateEntry.getClassNameId() <= 0)) {

			return SoyContextFactoryUtil.createSoyContext();
		}

		SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

		SoyContext typeSoyContext = SoyContextFactoryUtil.createSoyContext();

		typeSoyContext.put(
			"id", layoutPageTemplateEntry.getClassNameId()
		).put(
			"label", _getMappingTypeLabel()
		);

		soyContext.put("type", typeSoyContext);

		String subtypeLabel = _getMappingSubtypeLabel();

		if (Validator.isNotNull(subtypeLabel)) {
			SoyContext subtypeSoyContext =
				SoyContextFactoryUtil.createSoyContext();

			subtypeSoyContext.put(
				"id", layoutPageTemplateEntry.getClassTypeId()
			).put(
				"label", subtypeLabel
			);

			soyContext.put("subtype", subtypeSoyContext);
		}

		return soyContext;
	}

	private SoyContext _editorSoyContext;
	private SoyContext _fragmentsEditorToolbarSoyContext;
	private LayoutPageTemplateEntry _layoutPageTemplateEntry;
	private final boolean _pageIsDisplayPage;

}