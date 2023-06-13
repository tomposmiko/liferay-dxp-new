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

package com.liferay.content.dashboard.journal.internal.item.action;

import com.liferay.content.dashboard.item.action.ContentDashboardItemVersionAction;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Locale;

import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class ExpireJournalArticleContentDashboardItemVersionAction
	implements ContentDashboardItemVersionAction {

	public ExpireJournalArticleContentDashboardItemVersionAction(
		HttpServletRequest httpServletRequest, JournalArticle journalArticle,
		Language language,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

		_httpServletRequest = httpServletRequest;
		_journalArticle = journalArticle;
		_language = language;
		_requestBackedPortletURLFactory = requestBackedPortletURLFactory;
	}

	@Override
	public String getIcon() {
		return "time";
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "expire");
	}

	@Override
	public String getName() {
		return "expire";
	}

	@Override
	public String getURL() {
		LiferayPortletResponse liferayPortletResponse =
			PortalUtil.getLiferayPortletResponse(
				(PortletResponse)_httpServletRequest.getAttribute(
					JavaConstants.JAVAX_PORTLET_RESPONSE));

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		return PortletURLBuilder.create(
			_requestBackedPortletURLFactory.createActionURL(
				JournalPortletKeys.JOURNAL)
		).setActionName(
			"/journal/expire_articles"
		).setRedirect(
			portletURL
		).setBackURL(
			portletURL.toString()
		).setParameter(
			"articleId",
			_journalArticle.getArticleId() + _VERSION_SEPARATOR +
				_journalArticle.getVersion()
		).setParameter(
			"groupId", _journalArticle.getGroupId()
		).buildString();
	}

	private static final String _VERSION_SEPARATOR = "_version_";

	private final HttpServletRequest _httpServletRequest;
	private final JournalArticle _journalArticle;
	private final Language _language;
	private final RequestBackedPortletURLFactory
		_requestBackedPortletURLFactory;

}