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

package com.liferay.osb.faro.web.internal.controller.asah;

import com.liferay.osb.faro.model.FaroChannel;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.service.FaroChannelLocalService;
import com.liferay.osb.faro.web.internal.annotations.TokenAuthentication;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.FaroController;
import com.liferay.osb.faro.web.internal.model.display.asah.FaroChannelDisplay;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Geyson Silva
 */
@Component(
	immediate = true, service = {ChannelController.class, FaroController.class}
)
@Path("/{lcpProjectId}/channel")
@Produces(MediaType.APPLICATION_JSON)
public class ChannelController extends BaseFaroController {

	@POST
	@TokenAuthentication
	public FaroChannelDisplay create(
			String body, @PathParam("lcpProjectId") String lcpProjectId)
		throws PortalException {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(body);

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByWeDeployKey(
				lcpProjectId + ".lfr.cloud");

		FaroChannel faroChannel = _faroChannelLocalService.addFaroChannel(
			getUserId(), jsonObject.getString("name"),
			jsonObject.getString("id"), faroProject.getGroupId());

		return new FaroChannelDisplay(faroChannel);
	}

	@Reference
	private FaroChannelLocalService _faroChannelLocalService;

}