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

package com.liferay.blogs.web.internal.portlet.action;

import com.liferay.blogs.configuration.BlogsGroupServiceOverriddenConfiguration;
import com.liferay.blogs.constants.BlogsConstants;
import com.liferay.blogs.service.BlogsEntryService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.rss.util.RSSUtil;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	immediate = true, property = "path=/blogs/rss", service = StrutsAction.class
)
public class RSSAction implements StrutsAction {

	@Override
	public String execute(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		if (!isRSSFeedsEnabled(httpServletRequest)) {
			_portal.sendRSSFeedsDisabledError(
				httpServletRequest, httpServletResponse);

			return null;
		}

		try {
			ServletResponseUtil.sendFile(
				httpServletRequest, httpServletResponse, null,
				getRSS(httpServletRequest), ContentTypes.TEXT_XML_UTF8);

			return null;
		}
		catch (Exception e) {
			_portal.sendError(e, httpServletRequest, httpServletResponse);

			return null;
		}
	}

	protected byte[] getRSS(HttpServletRequest httpServletRequest)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		long plid = ParamUtil.getLong(httpServletRequest, "plid");

		if (plid == LayoutConstants.DEFAULT_PLID) {
			plid = themeDisplay.getPlid();
		}

		long companyId = ParamUtil.getLong(httpServletRequest, "companyId");
		long groupId = ParamUtil.getLong(httpServletRequest, "groupId");
		long organizationId = ParamUtil.getLong(
			httpServletRequest, "organizationId");
		int status = WorkflowConstants.STATUS_APPROVED;
		int max = ParamUtil.getInteger(
			httpServletRequest, "max", SearchContainer.DEFAULT_DELTA);
		String type = ParamUtil.getString(
			httpServletRequest, "type", RSSUtil.FORMAT_DEFAULT);
		double version = ParamUtil.getDouble(
			httpServletRequest, "version", RSSUtil.VERSION_DEFAULT);
		String displayStyle = ParamUtil.getString(
			httpServletRequest, "displayStyle", RSSUtil.DISPLAY_STYLE_DEFAULT);

		String feedURL =
			themeDisplay.getPortalURL() + themeDisplay.getPathMain() +
				"/blogs/find_entry?";

		String entryURL = feedURL;

		String rss = StringPool.BLANK;

		if (companyId > 0) {
			feedURL = StringPool.BLANK;

			rss = _blogsEntryService.getCompanyEntriesRSS(
				companyId, new Date(), status, max, type, version, displayStyle,
				feedURL, entryURL, themeDisplay);
		}
		else if (groupId > 0) {
			feedURL += "p_l_id=" + plid;

			entryURL = feedURL;

			rss = _blogsEntryService.getGroupEntriesRSS(
				groupId, new Date(), status, max, type, version, displayStyle,
				feedURL, entryURL, themeDisplay);
		}
		else if (organizationId > 0) {
			feedURL = StringPool.BLANK;

			rss = _blogsEntryService.getOrganizationEntriesRSS(
				organizationId, new Date(), status, max, type, version,
				displayStyle, feedURL, entryURL, themeDisplay);
		}
		else if (layout != null) {
			groupId = themeDisplay.getScopeGroupId();

			feedURL = themeDisplay.getPathMain() + "/blogs/rss";

			entryURL = feedURL;

			rss = _blogsEntryService.getGroupEntriesRSS(
				groupId, new Date(), status, max, type, version, displayStyle,
				feedURL, entryURL, themeDisplay);
		}

		return rss.getBytes(StringPool.UTF8);
	}

	protected boolean isRSSFeedsEnabled(HttpServletRequest httpServletRequest)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		BlogsGroupServiceOverriddenConfiguration
			blogsGroupServiceOverriddenConfiguration =
				_configurationProvider.getConfiguration(
					BlogsGroupServiceOverriddenConfiguration.class,
					new GroupServiceSettingsLocator(
						themeDisplay.getSiteGroupId(),
						BlogsConstants.SERVICE_NAME));

		return blogsGroupServiceOverriddenConfiguration.enableRss();
	}

	@Reference
	private BlogsEntryService _blogsEntryService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Portal _portal;

}