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

package com.liferay.layout.content.page.editor.web.internal.portlet.action;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.PortletRegistry;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.segments.constants.SegmentsExperienceConstants;
import com.liferay.segments.service.SegmentsExperienceService;
import com.liferay.segments.util.SegmentsExperiencePortletUtil;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author David Arques
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
		"mvc.command.name=/content_layout/delete_segments_experience"
	},
	service = {AopService.class, MVCActionCommand.class}
)
public class DeleteSegmentsExperienceMVCActionCommand
	extends BaseMVCActionCommand implements AopService, MVCActionCommand {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws PortletException {

		return super.processAction(actionRequest, actionResponse);
	}

	protected void deleteSegmentsExperience(ActionRequest actionRequest)
		throws PortalException {

		boolean deleteSegmentsExperience = ParamUtil.getBoolean(
			actionRequest, "deleteSegmentsExperience");

		long segmentsExperienceId = ParamUtil.getLong(
			actionRequest, "segmentsExperienceId",
			SegmentsExperienceConstants.ID_DEFAULT);

		if (deleteSegmentsExperience &&
			(segmentsExperienceId != SegmentsExperienceConstants.ID_DEFAULT)) {

			_segmentsExperienceService.deleteSegmentsExperience(
				segmentsExperienceId);
		}

		String fragmentEntryLinkIdsString = ParamUtil.getString(
			actionRequest, "fragmentEntryLinkIds");

		if (Validator.isNotNull(fragmentEntryLinkIdsString)) {
			long[] toFragmentEntryLinkIds = JSONUtil.toLongArray(
				JSONFactoryUtil.createJSONArray(fragmentEntryLinkIdsString));

			for (long fragmentEntryLinkId : toFragmentEntryLinkIds) {
				FragmentEntryLink fragmentEntryLink =
					_fragmentEntryLinkLocalService.getFragmentEntryLink(
						fragmentEntryLinkId);

				List<String> portletIds =
					_portletRegistry.getFragmentEntryLinkPortletIds(
						fragmentEntryLink);

				for (String portletId : portletIds) {
					String portletIdWithExperience =
						SegmentsExperiencePortletUtil.setSegmentsExperienceId(
							portletId, segmentsExperienceId);

					PortletPreferences jxPortletPreferences =
						_portletPreferencesLocalService.fetchPreferences(
							fragmentEntryLink.getCompanyId(),
							PortletKeys.PREFS_OWNER_ID_DEFAULT,
							PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
							fragmentEntryLink.getClassPK(),
							portletIdWithExperience);

					if (jxPortletPreferences != null) {
						_portletPreferencesLocalService.
							deletePortletPreferences(
								PortletKeys.PREFS_OWNER_ID_DEFAULT,
								PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
								fragmentEntryLink.getClassPK(),
								portletIdWithExperience);
					}
				}
			}

			if (deleteSegmentsExperience) {
				_fragmentEntryLinkLocalService.deleteFragmentEntryLinks(
					toFragmentEntryLinkIds);
			}
		}
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		try {
			deleteSegmentsExperience(actionRequest);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			jsonObject.put(
				"error",
				LanguageUtil.get(
					themeDisplay.getRequest(), "an-unexpected-error-occurred"));
		}

		hideDefaultSuccessMessage(actionRequest);

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DeleteSegmentsExperienceMVCActionCommand.class);

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private PortletPreferencesLocalService _portletPreferencesLocalService;

	@Reference
	private PortletRegistry _portletRegistry;

	@Reference
	private SegmentsExperienceService _segmentsExperienceService;

}