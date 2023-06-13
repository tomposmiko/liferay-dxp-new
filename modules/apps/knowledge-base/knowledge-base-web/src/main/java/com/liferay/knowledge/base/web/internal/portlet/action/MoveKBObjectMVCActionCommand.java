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

package com.liferay.knowledge.base.web.internal.portlet.action;

import com.liferay.knowledge.base.constants.KBArticleConstants;
import com.liferay.knowledge.base.constants.KBFolderConstants;
import com.liferay.knowledge.base.constants.KBPortletKeys;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.model.KBFolder;
import com.liferay.knowledge.base.service.KBArticleService;
import com.liferay.knowledge.base.service.KBFolderService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_ADMIN,
		"javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_ARTICLE,
		"javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_DISPLAY,
		"javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_SEARCH,
		"javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_SECTION,
		"mvc.command.name=/knowledge_base/move_kb_object"
	},
	service = MVCActionCommand.class
)
public class MoveKBObjectMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		boolean dragAndDrop = ParamUtil.getBoolean(
			actionRequest, "dragAndDrop");

		try {
			long resourceClassNameId = ParamUtil.getLong(
				actionRequest, "resourceClassNameId");
			long resourcePrimKey = ParamUtil.getLong(
				actionRequest, "resourcePrimKey");
			long parentResourceClassNameId = ParamUtil.getLong(
				actionRequest, "parentResourceClassNameId",
				_portal.getClassNameId(KBFolderConstants.getClassName()));
			long parentResourcePrimKey = ParamUtil.getLong(
				actionRequest, "parentResourcePrimKey",
				KBFolderConstants.DEFAULT_PARENT_FOLDER_ID);

			long kbArticleClassNameId = _portal.getClassNameId(
				KBArticleConstants.getClassName());

			if (resourceClassNameId == kbArticleClassNameId) {
				if (!_isDragAndDrop(dragAndDrop)) {
					double priority = ParamUtil.getDouble(
						actionRequest, "priority");

					_kbArticleService.moveKBArticle(
						resourcePrimKey, parentResourceClassNameId,
						parentResourcePrimKey, priority);
				}
				else {
					KBArticle kbArticle = _kbArticleService.getLatestKBArticle(
						resourcePrimKey, WorkflowConstants.STATUS_ANY);

					if (kbArticle.getParentResourcePrimKey() !=
							parentResourcePrimKey) {

						_kbArticleService.moveKBArticle(
							resourcePrimKey, parentResourceClassNameId,
							parentResourcePrimKey, kbArticle.getPriority());
					}
				}
			}
			else {
				if (!_isDragAndDrop(dragAndDrop)) {
					_kbFolderService.moveKBFolder(
						resourcePrimKey, parentResourcePrimKey);
				}
				else {
					if (parentResourceClassNameId == kbArticleClassNameId) {
						_errorMessage(
							actionRequest, actionResponse,
							_language.get(
								_portal.getHttpServletRequest(actionRequest),
								"folders-cannot-be-moved-into-articles"));

						return;
					}

					KBFolder kbFolder = _kbFolderService.getKBFolder(
						resourcePrimKey);

					if (kbFolder.getParentKBFolderId() !=
							parentResourcePrimKey) {

						_kbFolderService.moveKBFolder(
							resourcePrimKey, parentResourcePrimKey);
					}
				}
			}

			if (_isDragAndDrop(dragAndDrop)) {
				JSONObject jsonObject = JSONUtil.put("success", Boolean.TRUE);

				JSONPortletResponseUtil.writeJSON(
					actionRequest, actionResponse, jsonObject);
			}
		}
		catch (PortalException portalException) {
			if (!_isDragAndDrop(dragAndDrop)) {
				throw portalException;
			}

			_errorMessage(
				actionRequest, actionResponse,
				_language.get(
					_portal.getHttpServletRequest(actionRequest),
					"your-request-failed-to-complete"));
		}
	}

	private void _errorMessage(
			ActionRequest actionRequest, ActionResponse actionResponse,
			String message)
		throws IOException {

		JSONObject jsonObject = JSONUtil.put("errorMessage", message);

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private boolean _isDragAndDrop(boolean dragAndDrop) {
		if (GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-156421"))) {
			return dragAndDrop;
		}

		return false;
	}

	@Reference
	private KBArticleService _kbArticleService;

	@Reference
	private KBFolderService _kbFolderService;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

}