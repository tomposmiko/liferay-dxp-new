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

package com.liferay.style.book.web.internal.portlet.action;

import com.liferay.frontend.token.definition.FrontendToken;
import com.liferay.frontend.token.definition.FrontendTokenDefinition;
import com.liferay.frontend.token.definition.FrontendTokenDefinitionRegistry;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.style.book.constants.StyleBookPortletKeys;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.zip.processor.StyleBookEntryZipProcessor;
import com.liferay.style.book.zip.processor.StyleBookEntryZipProcessorImportResultEntry;

import java.io.File;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	property = {
		"javax.portlet.name=" + StyleBookPortletKeys.STYLE_BOOK,
		"mvc.command.name=/style_book/import_style_book_entries"
	},
	service = MVCActionCommand.class
)
public class ImportStyleBookEntriesMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void addSuccessMessage(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		String successMessage = _language.get(
			_portal.getHttpServletRequest(actionRequest),
			"the-files-were-imported-correctly");

		SessionMessages.add(actionRequest, "requestProcessed", successMessage);
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		UploadPortletRequest uploadPortletRequest =
			_portal.getUploadPortletRequest(actionRequest);

		File file = uploadPortletRequest.getFile("file");

		boolean overwrite = ParamUtil.getBoolean(
			actionRequest, "overwrite", true);

		try {
			List<StyleBookEntryZipProcessorImportResultEntry>
				styleBookEntryZipProcessorImportResultEntries =
					_importStyleBookEntries(
						themeDisplay.getUserId(),
						themeDisplay.getScopeGroupId(), file, overwrite);

			if (ListUtil.isNotEmpty(
					styleBookEntryZipProcessorImportResultEntries)) {

				SessionMessages.add(
					actionRequest,
					"styleBookEntryZipProcessorImportResultEntries",
					styleBookEntryZipProcessorImportResultEntries);
			}

			SessionMessages.add(actionRequest, "success");

			LayoutSet layoutSet = _layoutSetLocalService.fetchLayoutSet(
				themeDisplay.getSiteGroupId(), false);

			Set<String> frontendTokenNames = new HashSet<>();

			FrontendTokenDefinition frontendTokenDefinition =
				_frontendTokenDefinitionRegistry.getFrontendTokenDefinition(
					layoutSet.getThemeId());

			if (frontendTokenDefinition != null) {
				Collection<FrontendToken> frontendTokens =
					frontendTokenDefinition.getFrontendTokens();

				for (FrontendToken frontendToken : frontendTokens) {
					frontendTokenNames.add(frontendToken.getName());
				}
			}

			for (StyleBookEntryZipProcessorImportResultEntry
					styleBookEntryZipProcessorImportResultEntry :
						styleBookEntryZipProcessorImportResultEntries) {

				StyleBookEntry styleBookEntry =
					styleBookEntryZipProcessorImportResultEntry.
						getStyleBookEntry();

				if ((styleBookEntryZipProcessorImportResultEntry.getStatus() !=
						StyleBookEntryZipProcessorImportResultEntry.Status.
							INVALID) &&
					(styleBookEntry != null) &&
					!_isValidFrontendTokenDefinition(
						frontendTokenNames, styleBookEntry)) {

					SessionMessages.add(
						actionRequest,
						"styleBookFrontendTokensValuesNotValidated");

					break;
				}
			}
		}
		catch (Exception exception) {
			SessionErrors.add(actionRequest, exception.getClass(), exception);
		}

		sendRedirect(actionRequest, actionResponse);
	}

	private List<StyleBookEntryZipProcessorImportResultEntry>
			_importStyleBookEntries(
				long userId, long groupId, File file, boolean overwrite)
		throws Exception {

		return _styleBookEntryZipProcessor.importStyleBookEntries(
			userId, groupId, file, overwrite);
	}

	private boolean _isValidFrontendTokenDefinition(
			Set<String> frontendTokenNames, StyleBookEntry styleBookEntry)
		throws JSONException {

		JSONObject frontendTokensValuesJSONObject =
			_jsonFactory.createJSONObject(
				styleBookEntry.getFrontendTokensValues());

		for (String key : frontendTokensValuesJSONObject.keySet()) {
			if (!frontendTokenNames.contains(key)) {
				return false;
			}
		}

		return true;
	}

	@Reference
	private FrontendTokenDefinitionRegistry _frontendTokenDefinitionRegistry;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private LayoutSetLocalService _layoutSetLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private StyleBookEntryZipProcessor _styleBookEntryZipProcessor;

}