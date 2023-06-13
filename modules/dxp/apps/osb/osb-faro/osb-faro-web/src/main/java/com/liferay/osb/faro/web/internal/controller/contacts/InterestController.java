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

package com.liferay.osb.faro.web.internal.controller.contacts;

import com.liferay.osb.faro.engine.client.util.OrderByField;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.FaroController;
import com.liferay.osb.faro.web.internal.model.display.FaroResultsDisplay;
import com.liferay.osb.faro.web.internal.param.FaroParam;
import com.liferay.osb.faro.web.internal.util.InterestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.RoleConstants;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;

/**
 * @author Shinn Lok
 */
@Component(service = {FaroController.class, InterestController.class})
@Path("/{groupId}/interest")
@Produces(MediaType.APPLICATION_JSON)
public class InterestController extends BaseFaroController {

	@GET
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay search(
			@PathParam("groupId") long groupId,
			@QueryParam("contactsEntityId") String contactsEntityId,
			@QueryParam("query") String query,
			@QueryParam("interval") String interval, @QueryParam("max") int max,
			@QueryParam("cur") int cur, @QueryParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @QueryParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		return InterestUtil.getInterests(
			faroProjectLocalService.getFaroProjectByGroupId(groupId),
			contactsEntityId, query, cur, delta,
			orderByFieldsFaroParam.getValue(), contactsEngineClient);
	}

	@Path("/search")
	@POST
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay searchByForm(
			@PathParam("groupId") long groupId,
			@FormParam("contactsEntityId") String contactsEntityId,
			@FormParam("query") String query,
			@FormParam("interval") String interval, @FormParam("max") int max,
			@FormParam("cur") int cur, @FormParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @FormParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		return search(
			groupId, contactsEntityId, query, interval, max, cur, delta,
			orderByFieldsFaroParam);
	}

	@GET
	@Path("/keywords")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay searchKeywords(
			@PathParam("groupId") long groupId,
			@QueryParam("query") String query, @QueryParam("cur") int cur,
			@QueryParam("delta") int delta)
		throws Exception {

		return new FaroResultsDisplay(
			contactsEngineClient.getInterestKeywords(
				faroProjectLocalService.getFaroProjectByGroupId(groupId), query,
				cur, delta));
	}

}