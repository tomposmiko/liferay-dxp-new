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

import com.liferay.fragment.renderer.FragmentRendererController;
import com.liferay.layout.content.page.editor.sidebar.panel.ContentPageEditorSidebarPanel;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructureRel;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalServiceUtil;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureRelLocalServiceUtil;
import com.liferay.layout.page.template.util.LayoutDataConverter;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.template.soy.util.SoyContext;
import com.liferay.portal.template.soy.util.SoyContextFactoryUtil;
import com.liferay.segments.constants.SegmentsEntryConstants;
import com.liferay.segments.constants.SegmentsExperienceConstants;
import com.liferay.segments.constants.SegmentsExperimentConstants;
import com.liferay.segments.constants.SegmentsPortletKeys;
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.segments.model.SegmentsExperiment;
import com.liferay.segments.service.SegmentsEntryServiceUtil;
import com.liferay.segments.service.SegmentsExperienceLocalServiceUtil;
import com.liferay.segments.service.SegmentsExperienceServiceUtil;
import com.liferay.segments.service.SegmentsExperimentLocalServiceUtil;
import com.liferay.staging.StagingGroupHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class ContentPageLayoutEditorDisplayContext
	extends ContentPageEditorDisplayContext {

	public ContentPageLayoutEditorDisplayContext(
		HttpServletRequest httpServletRequest, RenderResponse renderResponse,
		CommentManager commentManager,
		List<ContentPageEditorSidebarPanel> contentPageEditorSidebarPanels,
		FragmentRendererController fragmentRendererController,
		PortletRequest portletRequest, StagingGroupHelper stagingGroupHelper) {

		super(
			httpServletRequest, renderResponse, commentManager,
			contentPageEditorSidebarPanels, fragmentRendererController,
			portletRequest);

		_stagingGroupHelper = stagingGroupHelper;
	}

	@Override
	public SoyContext getEditorSoyContext() throws Exception {
		if (_editorSoyContext != null) {
			return _editorSoyContext;
		}

		SoyContext soyContext = super.getEditorSoyContext();

		soyContext.put("sidebarPanels", getSidebarPanelSoyContexts(false));

		if (_isShowSegmentsExperiences()) {
			_populateSegmentsExperiencesSoyContext(soyContext);
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

		SoyContext soyContext = super.getFragmentsEditorToolbarSoyContext();

		if (_isShowSegmentsExperiences()) {
			_populateSegmentsExperiencesSoyContext(soyContext);
		}

		_fragmentsEditorToolbarSoyContext = soyContext;

		return _fragmentsEditorToolbarSoyContext;
	}

	@Override
	public boolean isSingleSegmentsExperienceMode() {
		long segmentsExperienceId = ParamUtil.getLong(
			PortalUtil.getOriginalServletRequest(httpServletRequest),
			"segmentsExperienceId", -1);

		if (segmentsExperienceId == -1) {
			return false;
		}

		return true;
	}

	@Override
	protected long getSegmentsExperienceId() {
		if (_segmentsExperienceId != null) {
			return _segmentsExperienceId;
		}

		_segmentsExperienceId = SegmentsExperienceConstants.ID_DEFAULT;

		long selectedSegmentsExperienceId = ParamUtil.getLong(
			PortalUtil.getOriginalServletRequest(httpServletRequest),
			"segmentsExperienceId", -1);

		if ((selectedSegmentsExperienceId != -1) &&
			(selectedSegmentsExperienceId !=
				SegmentsExperienceConstants.ID_DEFAULT)) {

			SegmentsExperience segmentsExperience =
				SegmentsExperienceLocalServiceUtil.fetchSegmentsExperience(
					selectedSegmentsExperienceId);

			if (segmentsExperience != null) {
				_segmentsExperienceId =
					segmentsExperience.getSegmentsExperienceId();
			}
		}

		return _segmentsExperienceId;
	}

	private SoyContext _getAvailableSegmentsEntriesSoyContext() {
		SoyContext availableSegmentsEntriesSoyContext =
			SoyContextFactoryUtil.createSoyContext();

		List<SegmentsEntry> segmentsEntries =
			SegmentsEntryServiceUtil.getSegmentsEntries(
				_getStagingAwareGroupId(SegmentsPortletKeys.SEGMENTS), true);

		for (SegmentsEntry segmentsEntry : segmentsEntries) {
			SoyContext segmentsEntrySoyContext =
				SoyContextFactoryUtil.createSoyContext();

			segmentsEntrySoyContext.put(
				"name", segmentsEntry.getName(themeDisplay.getLocale())
			).put(
				"segmentsEntryId",
				String.valueOf(segmentsEntry.getSegmentsEntryId())
			);

			availableSegmentsEntriesSoyContext.put(
				String.valueOf(segmentsEntry.getSegmentsEntryId()),
				segmentsEntrySoyContext);
		}

		SoyContext defaultSegmentsEntrySoyContext =
			SoyContextFactoryUtil.createSoyContext();

		defaultSegmentsEntrySoyContext.put(
			"name",
			SegmentsEntryConstants.getDefaultSegmentsEntryName(
				themeDisplay.getLocale())
		).put(
			"segmentsEntryId", SegmentsEntryConstants.ID_DEFAULT
		);

		availableSegmentsEntriesSoyContext.put(
			String.valueOf(SegmentsEntryConstants.ID_DEFAULT),
			defaultSegmentsEntrySoyContext);

		return availableSegmentsEntriesSoyContext;
	}

	private SoyContext _getAvailableSegmentsExperiencesSoyContext()
		throws PortalException {

		SoyContext availableSegmentsExperiencesSoyContext =
			SoyContextFactoryUtil.createSoyContext();

		Layout draftLayout = themeDisplay.getLayout();

		Layout layout = LayoutLocalServiceUtil.getLayout(
			draftLayout.getClassPK());

		String layoutFullURL = PortalUtil.getLayoutFullURL(
			layout, themeDisplay);

		List<SegmentsExperience> segmentsExperiences =
			SegmentsExperienceServiceUtil.getSegmentsExperiences(
				getGroupId(), PortalUtil.getClassNameId(Layout.class.getName()),
				themeDisplay.getPlid(), true);

		for (SegmentsExperience segmentsExperience : segmentsExperiences) {
			SoyContext segmentsExperienceSoyContext =
				SoyContextFactoryUtil.createSoyContext();

			segmentsExperienceSoyContext.put(
				"hasLockedSegmentsExperiment",
				segmentsExperience.hasSegmentsExperiment()
			).put(
				"name", segmentsExperience.getName(themeDisplay.getLocale())
			).put(
				"priority", segmentsExperience.getPriority()
			).put(
				"segmentsEntryId",
				String.valueOf(segmentsExperience.getSegmentsEntryId())
			).put(
				"segmentsExperienceId",
				String.valueOf(segmentsExperience.getSegmentsExperienceId())
			).put(
				"segmentsExperimentStatus",
				_getSegmentsExperimentStatusSoyContext(
					segmentsExperience.getSegmentsExperienceId())
			).put(
				"segmentsExperimentURL",
				_getSegmentsExperimentURL(
					layoutFullURL, segmentsExperience.getSegmentsExperienceId())
			);

			availableSegmentsExperiencesSoyContext.put(
				String.valueOf(segmentsExperience.getSegmentsExperienceId()),
				segmentsExperienceSoyContext);
		}

		SoyContext defaultSegmentsExperienceSoyContext =
			SoyContextFactoryUtil.createSoyContext();

		defaultSegmentsExperienceSoyContext.put(
			"hasLockedSegmentsExperiment",
			_hasDefaultSegmentsExperienceLockedSegmentsExperiment()
		).put(
			"name",
			SegmentsExperienceConstants.getDefaultSegmentsExperienceName(
				themeDisplay.getLocale())
		).put(
			"priority", SegmentsExperienceConstants.PRIORITY_DEFAULT
		).put(
			"segmentsEntryId", String.valueOf(SegmentsEntryConstants.ID_DEFAULT)
		).put(
			"segmentsExperienceId",
			String.valueOf(SegmentsExperienceConstants.ID_DEFAULT)
		).put(
			"segmentsExperimentStatus",
			_getSegmentsExperimentStatusSoyContext(
				SegmentsExperienceConstants.ID_DEFAULT)
		).put(
			"segmentsExperimentURL",
			_getSegmentsExperimentURL(
				layoutFullURL, SegmentsExperienceConstants.ID_DEFAULT)
		);

		availableSegmentsExperiencesSoyContext.put(
			String.valueOf(SegmentsExperienceConstants.ID_DEFAULT),
			defaultSegmentsExperienceSoyContext);

		return availableSegmentsExperiencesSoyContext;
	}

	private String _getEditSegmentsEntryURL() throws PortalException {
		if (_editSegmentsEntryURL != null) {
			return _editSegmentsEntryURL;
		}

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, SegmentsEntry.class.getName(),
			PortletProvider.Action.EDIT);

		if (portletURL == null) {
			_editSegmentsEntryURL = StringPool.BLANK;
		}
		else {
			portletURL.setParameter("redirect", themeDisplay.getURLCurrent());

			_editSegmentsEntryURL = portletURL.toString();
		}

		return _editSegmentsEntryURL;
	}

	private List<SoyContext> _getLayoutDataListSoyContext()
		throws PortalException {

		LayoutPageTemplateStructure layoutPageTemplateStructure =
			LayoutPageTemplateStructureLocalServiceUtil.
				fetchLayoutPageTemplateStructure(
					themeDisplay.getScopeGroupId(),
					PortalUtil.getClassNameId(Layout.class.getName()),
					themeDisplay.getPlid(), true);

		if (layoutPageTemplateStructure == null) {
			return Collections.emptyList();
		}

		List<SoyContext> soyContexts = new ArrayList<>();

		List<LayoutPageTemplateStructureRel> layoutPageTemplateStructureRels =
			LayoutPageTemplateStructureRelLocalServiceUtil.
				getLayoutPageTemplateStructureRels(
					layoutPageTemplateStructure.
						getLayoutPageTemplateStructureId());

		for (LayoutPageTemplateStructureRel layoutPageTemplateStructureRel :
				layoutPageTemplateStructureRels) {

			SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

			String layoutData = layoutPageTemplateStructureRel.getData();

			if (Objects.equals(
					contentPageEditorTypeConfiguration.type(), "react")) {

				layoutData = LayoutDataConverter.convert(layoutData);

				LayoutPageTemplateStructureLocalServiceUtil.
					updateLayoutPageTemplateStructure(
						themeDisplay.getScopeGroupId(),
						PortalUtil.getClassNameId(Layout.class.getName()),
						themeDisplay.getPlid(),
						layoutPageTemplateStructureRel.
							getSegmentsExperienceId(),
						layoutData);
			}

			soyContext.put(
				"layoutData", JSONFactoryUtil.createJSONObject(layoutData)
			).put(
				"segmentsExperienceId",
				layoutPageTemplateStructureRel.getSegmentsExperienceId()
			);

			soyContexts.add(soyContext);
		}

		return soyContexts;
	}

	private long _getSegmentsEntryId() {
		if (_segmentsEntryId != null) {
			return _segmentsEntryId;
		}

		_segmentsEntryId = ParamUtil.getLong(
			PortalUtil.getOriginalServletRequest(httpServletRequest),
			"segmentsEntryId");

		return _segmentsEntryId;
	}

	private Optional<SegmentsExperiment> _getSegmentsExperimentOptional(
			long segmentsExperienceId)
		throws PortalException {

		Layout draftLayout = themeDisplay.getLayout();

		Layout layout = LayoutLocalServiceUtil.getLayout(
			draftLayout.getClassPK());

		return Optional.ofNullable(
			SegmentsExperimentLocalServiceUtil.fetchSegmentsExperiment(
				segmentsExperienceId, PortalUtil.getClassNameId(Layout.class),
				layout.getPlid(),
				SegmentsExperimentConstants.Status.getExclusiveStatusValues()));
	}

	private SoyContext _getSegmentsExperimentStatusSoyContext(
			long segmentsExperienceId)
		throws PortalException {

		Optional<SegmentsExperiment> segmentsExperimentOptional =
			_getSegmentsExperimentOptional(segmentsExperienceId);

		if (!segmentsExperimentOptional.isPresent()) {
			return null;
		}

		SegmentsExperiment segmentsExperiment =
			segmentsExperimentOptional.get();

		SegmentsExperimentConstants.Status status =
			SegmentsExperimentConstants.Status.valueOf(
				segmentsExperiment.getStatus());

		SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

		return soyContext.put(
			"label",
			LanguageUtil.get(
				ResourceBundleUtil.getBundle(
					themeDisplay.getLocale(), getClass()),
				status.getLabel())
		).put(
			"value", status.getValue()
		);
	}

	private String _getSegmentsExperimentURL(
		String layoutFullURL, long segmentsExperienceId) {

		HttpUtil.addParameter(
			layoutFullURL, "p_l_back_url", themeDisplay.getURLCurrent());

		return layoutFullURL = HttpUtil.addParameter(
			layoutFullURL, "segmentsExperienceId", segmentsExperienceId);
	}

	private long _getStagingAwareGroupId(String portletId) {
		Long groupId = getGroupId();

		if (_stagingGroupHelper.isStagingGroup(groupId) &&
			!_stagingGroupHelper.isStagedPortlet(groupId, portletId)) {

			Group group = _stagingGroupHelper.fetchLiveGroup(groupId);

			if (group != null) {
				groupId = group.getGroupId();
			}
		}

		return groupId;
	}

	private boolean _hasDefaultSegmentsExperienceLockedSegmentsExperiment()
		throws PortalException {

		Optional<SegmentsExperiment> segmentsExperimentOptional =
			_getSegmentsExperimentOptional(
				SegmentsExperienceConstants.ID_DEFAULT);

		if (!segmentsExperimentOptional.isPresent()) {
			return false;
		}

		SegmentsExperiment segmentsExperiment =
			segmentsExperimentOptional.get();

		List<Integer> lockedStatusValuesList = ListUtil.fromArray(
			SegmentsExperimentConstants.Status.getLockedStatusValues());

		return lockedStatusValuesList.contains(segmentsExperiment.getStatus());
	}

	private boolean _hasEditSegmentsEntryPermission() throws PortalException {
		String editSegmentsEntryURL = _getEditSegmentsEntryURL();

		if (Validator.isNull(editSegmentsEntryURL)) {
			return false;
		}

		return true;
	}

	private Boolean _isLockedSegmentsExperience(long segmentsExperienceId)
		throws PortalException {

		if (_lockedSegmentsExperience != null) {
			return _lockedSegmentsExperience;
		}

		if (SegmentsExperienceConstants.ID_DEFAULT == segmentsExperienceId) {
			_lockedSegmentsExperience =
				_hasDefaultSegmentsExperienceLockedSegmentsExperiment();
		}
		else {
			SegmentsExperience segmentsExperience =
				SegmentsExperienceLocalServiceUtil.getSegmentsExperience(
					segmentsExperienceId);

			_lockedSegmentsExperience =
				segmentsExperience.hasSegmentsExperiment();
		}

		return _lockedSegmentsExperience;
	}

	private boolean _isShowSegmentsExperiences() throws PortalException {
		if (_showSegmentsExperiences != null) {
			return _showSegmentsExperiences;
		}

		Group group = GroupLocalServiceUtil.getGroup(getGroupId());

		if (!group.isLayoutSetPrototype() && !group.isUser()) {
			_showSegmentsExperiences = true;
		}
		else {
			_showSegmentsExperiences = false;
		}

		return _showSegmentsExperiences;
	}

	private void _populateSegmentsExperiencesSoyContext(SoyContext soyContext)
		throws PortalException {

		soyContext.put(
			"addSegmentsExperienceURL",
			getFragmentEntryActionURL("/content_layout/add_segments_experience")
		).put(
			"availableSegmentsEntries", _getAvailableSegmentsEntriesSoyContext()
		).put(
			"availableSegmentsExperiences",
			_getAvailableSegmentsExperiencesSoyContext()
		).put(
			"defaultSegmentsEntryId", SegmentsEntryConstants.ID_DEFAULT
		).put(
			"defaultSegmentsExperienceId",
			String.valueOf(SegmentsExperienceConstants.ID_DEFAULT)
		).put(
			"deleteSegmentsExperienceURL",
			getFragmentEntryActionURL(
				"/content_layout/delete_segments_experience")
		).put(
			"editSegmentsEntryURL", _getEditSegmentsEntryURL()
		).put(
			"hasEditSegmentsEntryPermission", _hasEditSegmentsEntryPermission()
		).put(
			"layoutDataList", _getLayoutDataListSoyContext()
		).put(
			"lockedSegmentsExperience",
			_isLockedSegmentsExperience(getSegmentsExperienceId())
		).put(
			"segmentsExperienceId", String.valueOf(getSegmentsExperienceId())
		).put(
			"segmentsExperimentStatus",
			_getSegmentsExperimentStatusSoyContext(getSegmentsExperienceId())
		).put(
			"selectedSegmentsEntryId", String.valueOf(_getSegmentsEntryId())
		).put(
			"singleSegmentsExperienceMode", isSingleSegmentsExperienceMode()
		);
	}

	private SoyContext _editorSoyContext;
	private String _editSegmentsEntryURL;
	private SoyContext _fragmentsEditorToolbarSoyContext;
	private Boolean _lockedSegmentsExperience;
	private Long _segmentsEntryId;
	private Long _segmentsExperienceId;
	private Boolean _showSegmentsExperiences;
	private final StagingGroupHelper _stagingGroupHelper;

}