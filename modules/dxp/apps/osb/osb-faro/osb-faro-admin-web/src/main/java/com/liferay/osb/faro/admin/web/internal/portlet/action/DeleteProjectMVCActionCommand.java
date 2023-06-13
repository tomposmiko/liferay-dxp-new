/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.admin.web.internal.portlet.action;

import com.liferay.osb.faro.admin.web.internal.constants.FaroAdminPortletKeys;
import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Marcos Martins
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + FaroAdminPortletKeys.FARO_ADMIN,
		"mvc.command.name=/faro_admin/delete_project"
	},
	service = MVCActionCommand.class
)
public class DeleteProjectMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long faroProjectId = ParamUtil.getLong(actionRequest, "faroProjectId");

		try {
			FaroProject faroProject = _faroProjectLocalService.getFaroProject(
				faroProjectId);

			Http.Options options = new Http.Options();

			options.setLocation(
				String.format(
					"http://localhost:8080/o/faro/main/project/%s",
					faroProject.getGroupId()));
			options.setDelete(true);
			options.setHeaders(getHeaders(actionRequest));

			_http.URLtoString(options);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			SessionErrors.add(actionRequest, exception.getClass());
		}
	}

	protected Map<String, String> getHeaders(ActionRequest actionRequest) {
		Map<String, String> headers = new HashMap<>();

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			actionRequest);

		Enumeration<String> enumeration = httpServletRequest.getHeaderNames();

		while (enumeration.hasMoreElements()) {
			String headerName = enumeration.nextElement();

			headers.put(headerName, httpServletRequest.getHeader(headerName));
		}

		return headers;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DeleteProjectMVCActionCommand.class);

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile ContactsEngineClient _contactsEngineClient;

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

	@Reference
	private Http _http;

	@Reference
	private Portal _portal;

}