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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.template.soy.util.SoyContext;
import com.liferay.portal.template.soy.util.SoyContextFactoryUtil;
import com.liferay.segments.constants.SegmentsConstants;
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.segments.service.SegmentsEntryServiceUtil;
import com.liferay.segments.service.SegmentsExperienceLocalServiceUtil;
import com.liferay.segments.service.SegmentsExperienceServiceUtil;

import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class ContentPageLayoutEditorDisplayContext
	extends ContentPageEditorDisplayContext {

	public ContentPageLayoutEditorDisplayContext(
		HttpServletRequest request, RenderResponse renderResponse,
		String className, long classPK) {

		super(request, renderResponse, className, classPK);
	}

	@Override
	public SoyContext getEditorSoyContext() throws Exception {
		if (_editorSoyContext != null) {
			return _editorSoyContext;
		}

		SoyContext soyContext = super.getEditorSoyContext();

		soyContext.put(
			"availableSegmentsEntries", _getAvailableSegmentsEntriesSoyContext()
		).put(
			"availableSegmentsExperiences",
			_getAvailableSegmentsExperiencesSoyContext()
		).put(
			"defaultSegmentsEntryId",
			SegmentsConstants.SEGMENTS_ENTRY_ID_DEFAULT
		).put(
			"defaultSegmentsExperienceId", _getDefaultSegmentsExperienceId()
		).put(
			"sidebarPanels", getSidebarPanelSoyContexts(false)
		);

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

		soyContext.put(
			"availableSegmentsEntries", _getAvailableSegmentsEntriesSoyContext()
		).put(
			"availableSegmentsExperiences",
			_getAvailableSegmentsExperiencesSoyContext()
		).put(
			"defaultSegmentsEntryId",
			SegmentsConstants.SEGMENTS_ENTRY_ID_DEFAULT
		).put(
			"defaultSegmentsExperienceId", _getDefaultSegmentsExperienceId()
		);

		_fragmentsEditorToolbarSoyContext = soyContext;

		return _fragmentsEditorToolbarSoyContext;
	}

	@Override
	protected long[] getSegmentsExperienceIds() {
		return new long[] {
			GetterUtil.getLong(_getDefaultSegmentsExperienceId())
		};
	}

	private SoyContext _getAvailableSegmentsEntriesSoyContext() {
		SoyContext availableSegmentsEntriesSoyContext =
			SoyContextFactoryUtil.createSoyContext();

		List<SegmentsEntry> segmentsEntries =
			SegmentsEntryServiceUtil.getSegmentsEntries(getGroupId(), true);

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

		SoyContext segmentsEntrySoyContext =
			SoyContextFactoryUtil.createSoyContext();

		segmentsEntrySoyContext.put(
			"name",
			() -> {
				ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
					"content.Language", themeDisplay.getLocale(),
					SegmentsConstants.class);

				return LanguageUtil.get(resourceBundle, "default-segment-name");
			}
		).put(
			"segmentsEntryId", SegmentsConstants.SEGMENTS_ENTRY_ID_DEFAULT
		);

		availableSegmentsEntriesSoyContext.put(
			String.valueOf(SegmentsConstants.SEGMENTS_ENTRY_ID_DEFAULT),
			segmentsEntrySoyContext);

		return availableSegmentsEntriesSoyContext;
	}

	private SoyContext _getAvailableSegmentsExperiencesSoyContext() {
		SoyContext availableSegmentsEntriesSoyContext =
			SoyContextFactoryUtil.createSoyContext();

		List<SegmentsExperience> segmentsExperiences =
			SegmentsExperienceServiceUtil.getSegmentsExperiences(
				getGroupId(), classNameId, classPK, true);

		for (SegmentsExperience segmentsExperience : segmentsExperiences) {
			SoyContext segmentsExperienceSoyContext =
				SoyContextFactoryUtil.createSoyContext();

			segmentsExperienceSoyContext.put(
				"name", segmentsExperience.getName(themeDisplay.getLocale())
			).put(
				"priority", segmentsExperience.getPriority()
			).put(
				"segmentsEntryId",
				String.valueOf(segmentsExperience.getSegmentsEntryId())
			).put(
				"segmentsExperienceId",
				String.valueOf(segmentsExperience.getSegmentsExperienceId())
			);

			availableSegmentsEntriesSoyContext.put(
				String.valueOf(segmentsExperience.getSegmentsExperienceId()),
				segmentsExperienceSoyContext);
		}

		return availableSegmentsEntriesSoyContext;
	}

	private String _getDefaultSegmentsExperienceId() {
		if (_defaultSegmentsExperienceId != null) {
			return _defaultSegmentsExperienceId;
		}

		_defaultSegmentsExperienceId = StringPool.BLANK;

		try {
			SegmentsExperience defaultSegmentsExperience =
				SegmentsExperienceLocalServiceUtil.getDefaultSegmentsExperience(
					getGroupId(), classNameId, classPK);

			_defaultSegmentsExperienceId = String.valueOf(
				defaultSegmentsExperience.getSegmentsExperienceId());
		}
		catch (PortalException pe) {
			_log.error("Unable to get default segments experience", pe);
		}

		return _defaultSegmentsExperienceId;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ContentPageLayoutEditorDisplayContext.class);

	private String _defaultSegmentsExperienceId;
	private SoyContext _editorSoyContext;
	private SoyContext _fragmentsEditorToolbarSoyContext;

}