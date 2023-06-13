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

import com.liferay.asset.display.page.portlet.AssetDisplayPageEntryFormProcessor;
import com.liferay.knowledge.base.constants.KBArticleConstants;
import com.liferay.knowledge.base.constants.KBFolderConstants;
import com.liferay.knowledge.base.constants.KBPortletKeys;
import com.liferay.knowledge.base.exception.KBArticleExpirationDateException;
import com.liferay.knowledge.base.exception.KBArticleReviewDateException;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.KBArticleService;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.util.PropsValues;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	property = {
		"javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_ADMIN,
		"javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_ARTICLE,
		"javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_DISPLAY,
		"javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_SEARCH,
		"javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_SECTION,
		"mvc.command.name=/knowledge_base/update_kb_article"
	},
	service = AopService.class
)
@CTAware
public class UpdateKBArticleMVCActionCommand
	extends BaseMVCActionCommand implements AopService, MVCActionCommand {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws PortletException {

		return super.processAction(actionRequest, actionResponse);
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		long resourcePrimKey = ParamUtil.getLong(
			actionRequest, "resourcePrimKey");

		KBArticle kbArticle = null;

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			KBArticle.class.getName(), actionRequest);

		if (cmd.equals(Constants.REVERT)) {
			int version = ParamUtil.getInteger(
				actionRequest, "version", KBArticleConstants.DEFAULT_VERSION);

			kbArticle = _kbArticleService.revertKBArticle(
				resourcePrimKey, version, serviceContext);
		}

		if (!cmd.equals(Constants.ADD) && !cmd.equals(Constants.UPDATE)) {
			return;
		}

		String title = ParamUtil.getString(actionRequest, "title");
		String content = ParamUtil.getString(actionRequest, "content");
		String description = ParamUtil.getString(actionRequest, "description");
		String sourceURL = ParamUtil.getString(actionRequest, "sourceURL");

		Date expirationDate = null;
		Date reviewDate = null;

		if (FeatureFlagManagerUtil.isEnabled("LPS-165476")) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			User user = _userLocalService.getUser(themeDisplay.getUserId());

			expirationDate = _getExpirationDate(
				actionRequest, true, user.getTimeZone());
			reviewDate = _getReviewDate(
				actionRequest, true, user.getTimeZone());
		}

		String[] sections = actionRequest.getParameterValues("sections");
		String[] selectedFileNames = ParamUtil.getParameterValues(
			actionRequest, "selectedFileName");

		if (cmd.equals(Constants.ADD)) {
			long parentResourceClassNameId = ParamUtil.getLong(
				actionRequest, "parentResourceClassNameId",
				_portal.getClassNameId(KBFolderConstants.getClassName()));
			long parentResourcePrimKey = ParamUtil.getLong(
				actionRequest, "parentResourcePrimKey",
				KBFolderConstants.DEFAULT_PARENT_FOLDER_ID);
			String urlTitle = ParamUtil.getString(actionRequest, "urlTitle");

			kbArticle = _kbArticleService.addKBArticle(
				null, _portal.getPortletId(actionRequest),
				parentResourceClassNameId, parentResourcePrimKey, title,
				urlTitle, content, description, sections, sourceURL,
				expirationDate, reviewDate, selectedFileNames, serviceContext);
		}
		else if (cmd.equals(Constants.UPDATE)) {
			long[] removeFileEntryIds = ParamUtil.getLongValues(
				actionRequest, "removeFileEntryIds");

			kbArticle = _kbArticleService.updateKBArticle(
				resourcePrimKey, title, content, description, sections,
				sourceURL, expirationDate, reviewDate, selectedFileNames,
				removeFileEntryIds, serviceContext);
		}

		_assetDisplayPageEntryFormProcessor.process(
			KBArticle.class.getName(), kbArticle.getResourcePrimKey(),
			actionRequest);

		int workflowAction = ParamUtil.getInteger(
			actionRequest, "workflowAction");

		if (workflowAction == WorkflowConstants.ACTION_SAVE_DRAFT) {
			String editURL = _buildEditURL(
				actionRequest, actionResponse, kbArticle);

			actionRequest.setAttribute(WebKeys.REDIRECT, editURL);
		}
		else {
			String redirect = _portal.escapeRedirect(
				ParamUtil.getString(actionRequest, "redirect"));

			if (cmd.equals(Constants.ADD) && Validator.isNotNull(redirect)) {
				actionRequest.setAttribute(
					WebKeys.REDIRECT,
					_getContentRedirect(
						KBArticle.class, kbArticle.getResourcePrimKey(),
						redirect));
			}
		}
	}

	private String _buildEditURL(
			ActionRequest actionRequest, ActionResponse actionResponse,
			KBArticle kbArticle)
		throws Exception {

		if (Objects.equals(
				_portal.getPortletId(actionRequest),
				KBPortletKeys.KNOWLEDGE_BASE_ADMIN)) {

			return PortletURLBuilder.create(
				PortletURLFactoryUtil.create(
					actionRequest, KBPortletKeys.KNOWLEDGE_BASE_ADMIN,
					PortletRequest.RENDER_PHASE)
			).setMVCPath(
				"/admin/common/edit_kb_article.jsp"
			).setRedirect(
				_getRedirect(actionRequest)
			).setParameter(
				"resourcePrimKey", kbArticle.getResourcePrimKey()
			).setWindowState(
				actionRequest.getWindowState()
			).buildString();
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		String editURL = _portal.getLayoutFullURL(themeDisplay);

		editURL = HttpComponentsUtil.setParameter(
			editURL, "p_p_id", portletDisplay.getId());
		editURL = HttpComponentsUtil.setParameter(
			editURL, actionResponse.getNamespace() + "mvcPath",
			"/admin/common/edit_kb_article.jsp");
		editURL = HttpComponentsUtil.setParameter(
			editURL, actionResponse.getNamespace() + "redirect",
			_getRedirect(actionRequest));
		editURL = HttpComponentsUtil.setParameter(
			editURL, actionResponse.getNamespace() + "resourcePrimKey",
			kbArticle.getResourcePrimKey());
		editURL = HttpComponentsUtil.setParameter(
			editURL, actionResponse.getNamespace() + "status",
			WorkflowConstants.STATUS_ANY);

		return editURL;
	}

	private String _getContentRedirect(
		Class<?> clazz, long classPK, String redirect) {

		String portletId = HttpComponentsUtil.getParameter(
			redirect, "portletResource", false);

		String namespace = _portal.getPortletNamespace(portletId);

		if (Validator.isNotNull(portletId)) {
			redirect = HttpComponentsUtil.addParameter(
				redirect, namespace + "className", clazz.getName());
			redirect = HttpComponentsUtil.addParameter(
				redirect, namespace + "classPK", classPK);
		}

		return redirect;
	}

	private Date _getExpirationDate(
			ActionRequest actionRequest, boolean neverExpireDefaultValue,
			TimeZone timeZone)
		throws Exception {

		boolean neverExpire = ParamUtil.getBoolean(
			actionRequest, "neverExpire", neverExpireDefaultValue);

		if (!PropsValues.SCHEDULER_ENABLED || neverExpire) {
			return null;
		}

		int expirationDateMonth = ParamUtil.getInteger(
			actionRequest, "expirationDateMonth");
		int expirationDateDay = ParamUtil.getInteger(
			actionRequest, "expirationDateDay");
		int expirationDateYear = ParamUtil.getInteger(
			actionRequest, "expirationDateYear");
		int expirationDateHour = ParamUtil.getInteger(
			actionRequest, "expirationDateHour");
		int expirationDateMinute = ParamUtil.getInteger(
			actionRequest, "expirationDateMinute");
		int expirationDateAmPm = ParamUtil.getInteger(
			actionRequest, "expirationDateAmPm");

		if (expirationDateAmPm == Calendar.PM) {
			expirationDateHour += 12;
		}

		Date expirationDate = _portal.getDate(
			expirationDateMonth, expirationDateDay, expirationDateYear,
			expirationDateHour, expirationDateMinute, timeZone,
			KBArticleExpirationDateException.class);

		if ((expirationDate != null) && expirationDate.before(new Date())) {
			throw new KBArticleExpirationDateException(
				"Expiration date " + expirationDate + " is in the past");
		}

		return expirationDate;
	}

	private String _getRedirect(ActionRequest actionRequest) {
		String redirect = (String)actionRequest.getAttribute(WebKeys.REDIRECT);

		if (Validator.isBlank(redirect)) {
			redirect = ParamUtil.getString(actionRequest, "redirect");

			if (!Validator.isBlank(redirect)) {
				redirect = _portal.escapeRedirect(redirect);
			}
		}

		return redirect;
	}

	private Date _getReviewDate(
			ActionRequest actionRequest, boolean neverReviewDefaultValue,
			TimeZone timeZone)
		throws Exception {

		boolean neverReview = ParamUtil.getBoolean(
			actionRequest, "neverReview", neverReviewDefaultValue);

		if (!PropsValues.SCHEDULER_ENABLED || neverReview) {
			return null;
		}

		int reviewDateMonth = ParamUtil.getInteger(
			actionRequest, "reviewDateMonth");
		int reviewDateDay = ParamUtil.getInteger(
			actionRequest, "reviewDateDay");
		int reviewDateYear = ParamUtil.getInteger(
			actionRequest, "reviewDateYear");
		int reviewDateHour = ParamUtil.getInteger(
			actionRequest, "reviewDateHour");
		int reviewDateMinute = ParamUtil.getInteger(
			actionRequest, "reviewDateMinute");
		int reviewDateAmPm = ParamUtil.getInteger(
			actionRequest, "reviewDateAmPm");

		if (reviewDateAmPm == Calendar.PM) {
			reviewDateHour += 12;
		}

		return _portal.getDate(
			reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour,
			reviewDateMinute, timeZone, KBArticleReviewDateException.class);
	}

	@Reference
	private AssetDisplayPageEntryFormProcessor
		_assetDisplayPageEntryFormProcessor;

	@Reference
	private KBArticleService _kbArticleService;

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

}