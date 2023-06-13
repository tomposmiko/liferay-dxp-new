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

package com.liferay.osb.faro.web.internal.controller.api;

import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.web.internal.context.GroupInfo;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.util.JSONUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.net.URI;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marcellus Tavares
 */
@Component(service = RecommendationController.class)
@Path("/recommendations")
@Produces(MediaType.APPLICATION_JSON)
public class RecommendationController extends BaseFaroController {

	@GET
	@Path("{any:.*}")
	public Map<?, ?> get(@Context GroupInfo groupInfo, @Context UriInfo uriInfo)
		throws Exception {

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(
				groupInfo.getGroupId());

		return contactsEngineClient.get(
			faroProject, _createHeaders(uriInfo.getBaseUri()),
			"/api/" + uriInfo.getPath(), uriInfo.getQueryParameters(),
			Map.class);
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{any:.*}")
	@POST
	public Map<?, ?> post(
			@Context GroupInfo groupInfo, String requestBody,
			@Context UriInfo uriInfo)
		throws Exception {

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(
				groupInfo.getGroupId());

		return contactsEngineClient.post(
			faroProject, _createHeaders(uriInfo.getBaseUri()),
			"/api/" + uriInfo.getPath(), uriInfo.getQueryParameters(),
			JSONUtil.readValue(requestBody, Map.class), Map.class);
	}

	private Map<String, String> _createHeaders(URI baseURI) {
		return HashMapBuilder.put(
			"X-Forwarded-Host", baseURI.getHost()
		).put(
			"X-Forwarded-Port", String.valueOf(baseURI.getPort())
		).put(
			"X-Forwarded-Proto", baseURI.getScheme()
		).build();
	}

}