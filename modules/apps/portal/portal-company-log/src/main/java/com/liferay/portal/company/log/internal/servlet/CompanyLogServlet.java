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

package com.liferay.portal.company.log.internal.servlet;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.MimeTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.log4j.Log4JUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Hai Yu
 */
@Component(
	enabled = false,
	property = {
		"osgi.http.whiteboard.servlet.name=com.liferay.portal.company.log.internal.servlet.CompanyLogServlet",
		"osgi.http.whiteboard.servlet.pattern=/company-log/*",
		"servlet.init.httpMethods=GET"
	},
	service = Servlet.class
)
public class CompanyLogServlet extends HttpServlet {

	@Override
	protected void doGet(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		try {
			String path = HttpComponentsUtil.fixPath(
				httpServletRequest.getPathInfo());

			String[] pathArray = StringUtil.split(path, CharPool.SLASH);

			if (pathArray.length == 0) {
				_list(httpServletRequest, httpServletResponse);
			}
			else if (pathArray.length == 2) {
				_download(httpServletRequest, httpServletResponse, pathArray);
			}
		}
		catch (FileNotFoundException fileNotFoundException) {
			if (_log.isWarnEnabled()) {
				_log.warn(fileNotFoundException);
			}

			_portal.sendError(
				HttpServletResponse.SC_NOT_FOUND, fileNotFoundException,
				httpServletRequest, httpServletResponse);
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(exception);
			}

			_portal.sendError(
				exception, httpServletRequest, httpServletResponse);
		}
	}

	private void _download(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String[] pathArray)
		throws Exception {

		long companyId = GetterUtil.getLongStrict(pathArray[0]);

		_companyLocalService.getCompanyById(companyId);

		PermissionChecker permissionChecker = _getPermissionChecker(
			httpServletRequest);

		if (!permissionChecker.isCompanyAdmin(companyId)) {
			throw new PrincipalException.MustBeCompanyAdmin(
				permissionChecker.getUserId());
		}

		File companyLogDirectory = Log4JUtil.getCompanyLogDirectory(companyId);

		String fileName = pathArray[1];

		Path path = Paths.get(companyLogDirectory.getPath(), fileName);

		path = path.normalize();

		if (!path.startsWith(companyLogDirectory.getPath())) {
			throw new PrincipalException("Invalid path " + path);
		}

		File file = path.toFile();

		if (!file.exists()) {
			throw new FileNotFoundException(
				StringBundler.concat(
					"Unable to get file ", fileName, " for company ",
					companyId));
		}

		String startString = ParamUtil.getString(httpServletRequest, "start");
		String endString = ParamUtil.getString(httpServletRequest, "end");

		if (Validator.isNull(startString) && Validator.isNull(endString)) {
			ServletResponseUtil.sendFile(
				httpServletRequest, httpServletResponse, fileName,
				Files.newInputStream(file.toPath()), file.length(),
				_mimeTypes.getContentType(fileName),
				HttpHeaders.CONTENT_DISPOSITION_ATTACHMENT);
		}
		else {
			long start = 0;

			if (Validator.isNotNull(startString)) {
				start = GetterUtil.getLongStrict(startString);
			}

			long end = file.length();

			if (Validator.isNotNull(endString) &&
				(GetterUtil.getLongStrict(endString) < end)) {

				end = GetterUtil.getLongStrict(endString);
			}

			if ((start < 0) || (end < 0) || (start >= end)) {
				throw new IllegalArgumentException(
					"Start and end cannot be less than 0. Start cannot be " +
						"greater than or equal to end.");
			}

			if (start != 0) {
				--start;
			}

			try (FileChannel fileChannel = FileChannel.open(file.toPath())) {
				fileChannel.position(start);

				ServletResponseUtil.sendFile(
					httpServletRequest, httpServletResponse, fileName,
					Channels.newInputStream(fileChannel), end - start,
					_mimeTypes.getContentType(fileName),
					HttpHeaders.CONTENT_DISPOSITION_ATTACHMENT);
			}
		}
	}

	private PermissionChecker _getPermissionChecker(
			HttpServletRequest httpServletRequest)
		throws Exception {

		User user = _portal.getUser(httpServletRequest);

		if (user == null) {
			throw new PrincipalException.MustBeAuthenticated(0);
		}

		return _permissionCheckerFactory.create(user);
	}

	private void _list(
			Company company, JSONArray jsonArray,
			HttpServletRequest httpServletRequest)
		throws Exception {

		jsonArray.put(
			JSONUtil.put(
				"companyId", company.getCompanyId()
			).put(
				"companyLogs",
				() -> {
					File companyLogDirectory = Log4JUtil.getCompanyLogDirectory(
						company.getCompanyId());

					File[] files = companyLogDirectory.listFiles();

					Arrays.sort(files);

					return JSONUtil.toJSONArray(
						files,
						file -> JSONUtil.put(
							"fileName", file.getName()
						).put(
							"fileSize",
							_language.formatStorageSize(
								file.length(), httpServletRequest.getLocale())
						));
				}
			).put(
				"webId", company.getWebId()
			));
	}

	private void _list(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		httpServletResponse.setContentType(ContentTypes.APPLICATION_JSON);
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		PermissionChecker permissionChecker = _getPermissionChecker(
			httpServletRequest);

		if (permissionChecker.isOmniadmin()) {
			_companyLocalService.forEachCompany(
				company -> _list(company, jsonArray, httpServletRequest));
		}
		else if (permissionChecker.isCompanyAdmin()) {
			User user = permissionChecker.getUser();

			_list(
				_companyLocalService.getCompany(user.getCompanyId()), jsonArray,
				httpServletRequest);
		}
		else {
			throw new PrincipalException.MustBeCompanyAdmin(
				permissionChecker.getUserId());
		}

		ServletResponseUtil.write(httpServletResponse, jsonArray.toString());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CompanyLogServlet.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private MimeTypes _mimeTypes;

	@Reference
	private PermissionCheckerFactory _permissionCheckerFactory;

	@Reference
	private Portal _portal;

}