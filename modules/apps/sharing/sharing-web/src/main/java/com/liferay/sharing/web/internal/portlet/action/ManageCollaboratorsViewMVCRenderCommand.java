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

package com.liferay.sharing.web.internal.portlet.action;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.sharing.model.SharingEntry;
import com.liferay.sharing.service.SharingEntryLocalService;
import com.liferay.sharing.web.internal.constants.SharingPortletKeys;
import com.liferay.sharing.web.internal.constants.SharingWebKeys;
import com.liferay.sharing.web.internal.display.SharingEntryPermissionDisplay;
import com.liferay.sharing.web.internal.display.SharingEntryPermissionDisplayAction;
import com.liferay.sharing.web.internal.util.SharingUtil;

import java.text.DateFormat;
import java.text.Format;

import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + SharingPortletKeys.MANAGE_COLLABORATORS,
		"mvc.command.name=/", "mvc.command.name=/sharing/manage_collaborators"
	},
	service = MVCRenderCommand.class
)
public class ManageCollaboratorsViewMVCRenderCommand
	implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		Template template = _getTemplate(renderRequest);

		template.put(
			"actionUrl", _getManageCollaboratorsActionURL(renderResponse));
		template.put(
			"collaborators", _getCollaboratorsJSONArray(renderRequest));
		template.put(
			"dialogId",
			ParamUtil.getString(
				renderRequest, SharingWebKeys.MANAGE_COLLABORATORS_DIALOG_ID));
		template.put("portletNamespace", renderResponse.getNamespace());
		template.put("spritemap", _getSpritemap(renderRequest));

		return "ManageCollaborators";
	}

	private JSONArray _getCollaboratorsJSONArray(RenderRequest renderRequest)
		throws PortletException {

		try {
			long classNameId = ParamUtil.getLong(renderRequest, "classNameId");
			long classPK = ParamUtil.getLong(renderRequest, "classPK");

			int sharingEntriesCount =
				_sharingEntryLocalService.getSharingEntriesCount(
					classNameId, classPK);

			if (sharingEntriesCount == 0) {
				return JSONFactoryUtil.createJSONArray();
			}

			ThemeDisplay themeDisplay =
				(ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			JSONArray collaboratorsJSONArray =
				JSONFactoryUtil.createJSONArray();

			List<SharingEntry> sharingEntries =
				_sharingEntryLocalService.getSharingEntries(
					classNameId, classPK);

			for (SharingEntry sharingEntry : sharingEntries) {
				User sharingEntryToUser = _userLocalService.fetchUser(
					sharingEntry.getToUserId());

				String portraitURL = StringPool.BLANK;

				if (sharingEntryToUser.getPortraitId() > 0) {
					portraitURL = sharingEntryToUser.getPortraitURL(
						themeDisplay);
				}

				JSONObject collaboratorJSONObject = JSONUtil.put(
					"portraitURL", portraitURL
				).put(
					"fullName", sharingEntryToUser.getFullName()
				).put(
					"sharingEntryId", sharingEntry.getSharingEntryId()
				);

				String expirationDateAsText = null;
				String expirationDateTooltip = null;

				Date expirationDate = sharingEntry.getExpirationDate();

				if (expirationDate != null) {
					DateFormat expirationDateFormat =
						DateFormatFactoryUtil.getSimpleDateFormat(
							"yyyy-MM-dd", themeDisplay.getLocale());

					expirationDateAsText = expirationDateFormat.format(
						expirationDate);

					Format expirationDateTooltipDateFormat =
						DateFormatFactoryUtil.getDate(themeDisplay.getLocale());

					expirationDateTooltip = LanguageUtil.format(
						ResourceBundleUtil.getBundle(
							themeDisplay.getLocale(), getClass()),
						"until-x",
						expirationDateTooltipDateFormat.format(expirationDate));
				}

				collaboratorJSONObject.put(
					"sharingEntryExpirationDate", expirationDateAsText
				).put(
					"sharingEntryExpirationDateTooltip", expirationDateTooltip
				).put(
					"sharingEntryPermissionDisplaySelectOptions",
					_getSharingEntryPermissionDisplaySelectOptions(
						sharingEntry, renderRequest)
				).put(
					"sharingEntryShareable", sharingEntry.isShareable()
				).put(
					"userId", Long.valueOf(sharingEntryToUser.getUserId())
				);

				collaboratorsJSONArray.put(collaboratorJSONObject);
			}

			return collaboratorsJSONArray;
		}
		catch (PortalException pe) {
			throw new PortletException(pe);
		}
	}

	private String _getManageCollaboratorsActionURL(
		RenderResponse renderResponse) {

		PortletURL manageCollaboratorURL = renderResponse.createActionURL();

		manageCollaboratorURL.setParameter(
			ActionRequest.ACTION_NAME, "/sharing/manage_collaborators");

		return manageCollaboratorURL.toString();
	}

	private JSONArray _getSharingEntryPermissionDisplaySelectOptions(
		SharingEntry sharingEntry, RenderRequest renderRequest) {

		long classNameId = ParamUtil.getLong(renderRequest, "classNameId");
		long classPK = ParamUtil.getLong(renderRequest, "classPK");

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		List<SharingEntryPermissionDisplay> sharingEntryPermissionDisplays =
			_sharingUtil.getSharingEntryPermissionDisplays(
				themeDisplay.getPermissionChecker(), classNameId, classPK,
				themeDisplay.getScopeGroupId(), themeDisplay.getLocale());

		JSONArray sharingEntryPermissionDisplaySelectOptionsJSONArray =
			JSONFactoryUtil.createJSONArray();

		for (SharingEntryPermissionDisplay sharingEntryPermissionDisplay :
				sharingEntryPermissionDisplays) {

			JSONObject sharingEntryPermissionDisplaySelectOptionJSONObject =
				JSONUtil.put(
					"label", sharingEntryPermissionDisplay.getPhrase());

			String currentSharingEntryPermissionDisplayActionKeyActionId =
				sharingEntryPermissionDisplay.
					getSharingEntryPermissionDisplayActionId();

			SharingEntryPermissionDisplayAction
				userSharingEntryPermissionDisplayActionKey =
					_sharingUtil.getSharingEntryPermissionDisplayActionKey(
						sharingEntry);

			sharingEntryPermissionDisplaySelectOptionJSONObject.put(
				"selected",
				currentSharingEntryPermissionDisplayActionKeyActionId.equals(
					userSharingEntryPermissionDisplayActionKey.getActionId())
			).put(
				"value",
				sharingEntryPermissionDisplay.
					getSharingEntryPermissionDisplayActionId()
			);

			sharingEntryPermissionDisplaySelectOptionsJSONArray.put(
				sharingEntryPermissionDisplaySelectOptionJSONObject);
		}

		return sharingEntryPermissionDisplaySelectOptionsJSONArray;
	}

	private String _getSpritemap(RenderRequest renderRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return themeDisplay.getPathThemeImages() + "/lexicon/icons.svg";
	}

	private Template _getTemplate(RenderRequest renderRequest) {
		return (Template)renderRequest.getAttribute(WebKeys.TEMPLATE);
	}

	@Reference
	private SharingEntryLocalService _sharingEntryLocalService;

	@Reference
	private SharingUtil _sharingUtil;

	@Reference
	private UserLocalService _userLocalService;

}