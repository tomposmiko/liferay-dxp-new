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

import com.liferay.asset.list.service.AssetListEntryLocalService;
import com.liferay.exportimport.kernel.staging.Staging;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.frontend.token.definition.FrontendTokenDefinitionRegistry;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.search.InfoSearchClassMapperRegistry;
import com.liferay.item.selector.ItemSelector;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorWebKeys;
import com.liferay.layout.content.page.editor.sidebar.panel.ContentPageEditorSidebarPanel;
import com.liferay.layout.content.page.editor.web.internal.configuration.PageEditorConfiguration;
import com.liferay.layout.content.page.editor.web.internal.util.ContentManager;
import com.liferay.layout.content.page.editor.web.internal.util.FragmentCollectionManager;
import com.liferay.layout.content.page.editor.web.internal.util.FragmentEntryLinkManager;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureRelLocalService;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService;
import com.liferay.portal.kernel.service.permission.LayoutPermission;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.segments.configuration.provider.SegmentsConfigurationProvider;
import com.liferay.segments.manager.SegmentsExperienceManager;
import com.liferay.segments.service.SegmentsEntryService;
import com.liferay.segments.service.SegmentsExperienceLocalService;
import com.liferay.segments.service.SegmentsExperimentRelLocalService;
import com.liferay.staging.StagingGroupHelper;
import com.liferay.style.book.service.StyleBookEntryLocalService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	configurationPid = "com.liferay.layout.content.page.editor.web.internal.configuration.PageEditorConfiguration",
	service = ContentPageEditorDisplayContextProvider.class
)
public class ContentPageEditorDisplayContextProvider {

	public ContentPageEditorDisplayContext getContentPageEditorDisplayContext(
		HttpServletRequest httpServletRequest, RenderResponse renderResponse,
		PortletRequest portletRequest) {

		String className = (String)httpServletRequest.getAttribute(
			ContentPageEditorWebKeys.CLASS_NAME);

		if (Objects.equals(className, Layout.class.getName())) {
			return new ContentPageLayoutEditorDisplayContext(
				_assetListEntryLocalService,
				_getContentPageEditorSidebarPanels(), _contentManager,
				_fragmentCollectionManager, _fragmentEntryLinkManager,
				_fragmentEntryLinkLocalService, _fragmentEntryLocalService,
				_frontendTokenDefinitionRegistry, _groupLocalService,
				httpServletRequest, _infoItemServiceRegistry,
				_infoSearchClassMapperRegistry, _itemSelector, _jsonFactory,
				_language, _layoutLocalService, _layoutSetLocalService,
				_layoutPageTemplateEntryLocalService,
				_layoutPageTemplateEntryService,
				_layoutPageTemplateStructureLocalService,
				_layoutPageTemplateStructureRelLocalService, _layoutPermission,
				_pageEditorConfiguration, _portal, portletRequest,
				_portletURLFactory, renderResponse,
				_segmentsConfigurationProvider,
				new SegmentsExperienceManager(_segmentsExperienceLocalService),
				_segmentsExperienceLocalService,
				_segmentsExperimentRelLocalService, _segmentsEntryService,
				_staging, _stagingGroupHelper, _styleBookEntryLocalService,
				_userLocalService, _workflowDefinitionLinkLocalService);
		}

		long classPK = GetterUtil.getLong(
			httpServletRequest.getAttribute(ContentPageEditorWebKeys.CLASS_PK));

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.fetchLayoutPageTemplateEntry(
				classPK);

		boolean pageIsDisplayPage = false;

		if ((layoutPageTemplateEntry != null) &&
			(layoutPageTemplateEntry.getType() ==
				LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE)) {

			pageIsDisplayPage = true;
		}

		return new ContentPageEditorLayoutPageTemplateDisplayContext(
			_getContentPageEditorSidebarPanels(), _contentManager,
			_fragmentCollectionManager, _fragmentEntryLinkManager,
			_fragmentEntryLinkLocalService, _fragmentEntryLocalService,
			_frontendTokenDefinitionRegistry, httpServletRequest,
			_infoItemServiceRegistry, _infoSearchClassMapperRegistry,
			_itemSelector, _jsonFactory, _language, _layoutLocalService,
			_layoutSetLocalService, _layoutPageTemplateEntryLocalService,
			_layoutPageTemplateEntryService, _layoutPermission,
			_pageEditorConfiguration, pageIsDisplayPage, _portal,
			portletRequest, _portletURLFactory, renderResponse,
			_segmentsConfigurationProvider,
			new SegmentsExperienceManager(_segmentsExperienceLocalService),
			_segmentsExperienceLocalService, _segmentsExperimentRelLocalService,
			_staging, _stagingGroupHelper, _styleBookEntryLocalService,
			_userLocalService, _workflowDefinitionLinkLocalService);
	}

	@Activate
	@Modified
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_pageEditorConfiguration = ConfigurableUtil.createConfigurable(
			PageEditorConfiguration.class, properties);
		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext, ContentPageEditorSidebarPanel.class);
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerList.close();
	}

	private List<ContentPageEditorSidebarPanel>
		_getContentPageEditorSidebarPanels() {

		return _serviceTrackerList.toList();
	}

	@Reference
	private AssetListEntryLocalService _assetListEntryLocalService;

	@Reference
	private ContentManager _contentManager;

	@Reference
	private FragmentCollectionManager _fragmentCollectionManager;

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private FragmentEntryLinkManager _fragmentEntryLinkManager;

	@Reference
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@Reference
	private FrontendTokenDefinitionRegistry _frontendTokenDefinitionRegistry;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private InfoItemServiceRegistry _infoItemServiceRegistry;

	@Reference
	private InfoSearchClassMapperRegistry _infoSearchClassMapperRegistry;

	@Reference
	private ItemSelector _itemSelector;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private LayoutPageTemplateEntryService _layoutPageTemplateEntryService;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Reference
	private LayoutPageTemplateStructureRelLocalService
		_layoutPageTemplateStructureRelLocalService;

	@Reference
	private LayoutPermission _layoutPermission;

	@Reference
	private LayoutSetLocalService _layoutSetLocalService;

	private volatile PageEditorConfiguration _pageEditorConfiguration;

	@Reference
	private Portal _portal;

	@Reference
	private PortletURLFactory _portletURLFactory;

	@Reference
	private SegmentsConfigurationProvider _segmentsConfigurationProvider;

	@Reference
	private SegmentsEntryService _segmentsEntryService;

	@Reference
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

	@Reference
	private SegmentsExperimentRelLocalService
		_segmentsExperimentRelLocalService;

	private volatile ServiceTrackerList<ContentPageEditorSidebarPanel>
		_serviceTrackerList;

	@Reference
	private Staging _staging;

	@Reference
	private StagingGroupHelper _stagingGroupHelper;

	@Reference
	private StyleBookEntryLocalService _styleBookEntryLocalService;

	@Reference
	private UserLocalService _userLocalService;

	@Reference
	private WorkflowDefinitionLinkLocalService
		_workflowDefinitionLinkLocalService;

}