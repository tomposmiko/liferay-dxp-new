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

package com.liferay.document.library.video.internal.servlet.filter;

import com.liferay.document.library.kernel.exception.NoSuchFileEntryException;
import com.liferay.document.library.kernel.exception.NoSuchFileVersionException;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.video.internal.constants.DLVideoPortletKeys;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.events.EventsProcessorUtil;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.constants.FriendlyURLResolverConstants;
import com.liferay.portal.kernel.repository.friendly.url.resolver.FileEntryFriendlyURLResolver;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	property = {
		"before-filter=Auto Login Filter", "dispatcher=FORWARD",
		"dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=DL Video Embed Filter", "url-pattern=/documents/*"
	},
	service = Filter.class
)
public class DLVideoEmbedFilter extends BasePortalFilter {

	@Override
	protected void processFilter(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws IOException, ServletException {

		boolean videoEmbed = ParamUtil.getBoolean(
			httpServletRequest, "videoEmbed");

		if (videoEmbed) {
			try {
				EventsProcessorUtil.process(
					PropsKeys.SERVLET_SERVICE_EVENTS_PRE,
					PropsValues.SERVLET_SERVICE_EVENTS_PRE, httpServletRequest,
					httpServletResponse);
			}
			catch (ActionException actionException) {
				if (_log.isDebugEnabled()) {
					_log.debug(actionException);
				}
			}

			httpServletResponse.sendRedirect(
				_getEmbedVideoURL(httpServletRequest));
		}
		else {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	private String _getEmbedVideoURL(HttpServletRequest httpServletRequest) {
		RequestBackedPortletURLFactory requestBackedPortletURLFactory =
			RequestBackedPortletURLFactoryUtil.create(httpServletRequest);

		PortletURL getEmbedVideoURL =
			requestBackedPortletURLFactory.createRenderURL(
				DLVideoPortletKeys.DL_VIDEO);

		try {
			getEmbedVideoURL.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (WindowStateException windowStateException) {
			if (_log.isDebugEnabled()) {
				_log.debug(windowStateException);
			}
		}

		getEmbedVideoURL.setParameter(
			"mvcRenderCommandName", "/document_library_video/embed_video");
		getEmbedVideoURL.setParameter(
			"fileVersionId", _getFileVersionId(httpServletRequest));

		return getEmbedVideoURL.toString();
	}

	private FileEntry _getFileEntry(
			HttpServletRequest httpServletRequest, List<String> pathParts)
		throws PortalException {

		if (_PATH_SEPARATOR_FILE_ENTRY.equals(pathParts.get(1))) {
			Optional<FileEntry> fileEntryOptional = _resolveFileEntry(
				httpServletRequest, pathParts);

			return fileEntryOptional.orElseThrow(
				() -> new NoSuchFileEntryException(
					"No file entry found for friendly URL " + pathParts));
		}

		long groupId = GetterUtil.getLong(pathParts.get(1));

		if (pathParts.size() == 5) {
			String uuid = pathParts.get(4);

			return _dlAppLocalService.getFileEntryByUuidAndGroupId(
				uuid, groupId);
		}

		long folderId = GetterUtil.getLong(pathParts.get(2));
		String fileName = HttpComponentsUtil.decodeURL(pathParts.get(3));

		return _dlAppLocalService.getFileEntryByFileName(
			groupId, folderId, fileName);
	}

	private String _getFileVersionId(HttpServletRequest httpServletRequest) {
		String requestURI = httpServletRequest.getRequestURI();

		List<String> pathParts = StringUtil.split(
			requestURI.substring(requestURI.indexOf("/documents")),
			CharPool.SLASH);

		if (pathParts.size() < 4) {
			return StringPool.BLANK;
		}

		try {
			FileEntry fileEntry = _getFileEntry(httpServletRequest, pathParts);

			String version = ParamUtil.getString(httpServletRequest, "version");

			FileVersion fileVersion = null;

			if (Validator.isNotNull(version)) {
				try {
					fileVersion = fileEntry.getFileVersion(version);
				}
				catch (NoSuchFileVersionException noSuchFileVersionException) {
					if (_log.isDebugEnabled()) {
						_log.debug(noSuchFileVersionException);
					}

					fileVersion = fileEntry.getFileVersion();
				}
			}
			else {
				fileVersion = fileEntry.getFileVersion();
			}

			return String.valueOf(fileVersion.getFileVersionId());
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}

		return StringPool.BLANK;
	}

	private Group _getGroup(long companyId, String name)
		throws PortalException {

		Group group = _groupLocalService.fetchFriendlyURLGroup(
			companyId, StringPool.SLASH + name);

		if (group != null) {
			return group;
		}

		User user = _userLocalService.getUserByScreenName(companyId, name);

		return user.getGroup();
	}

	private User _getUser(HttpServletRequest httpServletRequest)
		throws PortalException {

		User user = _portal.getUser(httpServletRequest);

		if (user != null) {
			return user;
		}

		HttpSession httpSession = httpServletRequest.getSession();

		String userIdString = (String)httpSession.getAttribute("j_username");
		String password = (String)httpSession.getAttribute("j_password");

		if ((userIdString != null) && (password != null)) {
			long userId = GetterUtil.getLong(userIdString);

			return _userLocalService.getUser(userId);
		}

		Company company = _companyLocalService.getCompany(
			_portal.getCompanyId(httpServletRequest));

		return company.getDefaultUser();
	}

	private Optional<FileEntry> _resolveFileEntry(
			HttpServletRequest httpServletRequest, List<String> pathParts)
		throws PortalException {

		if (_fileEntryFriendlyURLResolver == null) {
			return Optional.empty();
		}

		User user = _getUser(httpServletRequest);

		Group group = _getGroup(user.getCompanyId(), pathParts.get(2));

		return _fileEntryFriendlyURLResolver.resolveFriendlyURL(
			group.getGroupId(), pathParts.get(3));
	}

	private static final String _PATH_SEPARATOR_FILE_ENTRY =
		FriendlyURLResolverConstants.URL_SEPARATOR_Y_FILE_ENTRY;

	private static final Log _log = LogFactoryUtil.getLog(
		DLVideoEmbedFilter.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private FileEntryFriendlyURLResolver _fileEntryFriendlyURLResolver;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

}