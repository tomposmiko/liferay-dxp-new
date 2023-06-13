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

import com.liferay.osb.faro.engine.client.model.PageVisited;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.engine.client.util.OrderByField;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.FaroController;
import com.liferay.osb.faro.web.internal.model.display.FaroResultsDisplay;
import com.liferay.osb.faro.web.internal.param.FaroParam;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.RoleConstants;

import java.util.Date;
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
 * @author Matthew Kong
 */
@Component(service = {FaroController.class, PagesVisitedController.class})
@Path("/{groupId}/pages_visited")
@Produces(MediaType.APPLICATION_JSON)
public class PagesVisitedController extends BaseFaroController {

	@GET
	@Path("/{id}")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public PageVisited get(
			@PathParam("groupId") long groupId, @PathParam("id") String id)
		throws Exception {

		return contactsEngineClient.getPageVisited(
			faroProjectLocalService.getFaroProjectByGroupId(groupId), id);
	}

	@GET
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay search(
			@PathParam("groupId") long groupId,
			@QueryParam("contactsEntityId") String contactsEntityId,
			@QueryParam("contactsEntityType") int contactEntityType,
			@QueryParam("query") String query,
			@QueryParam("interestName") String interestName,
			@DefaultValue(StringPool.BLANK) @QueryParam("startDate") FaroParam
				<Date> startDateFaroParam,
			@DefaultValue(StringPool.BLANK) @QueryParam("endDate") FaroParam
				<Date> endDateFaroParam,
			@DefaultValue(StringPool.TRUE) @QueryParam("active") boolean active,
			@QueryParam("cur") int cur, @QueryParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @QueryParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		Results<PageVisited> results = contactsEngineClient.getPagesVisited(
			faroProjectLocalService.getFaroProjectByGroupId(groupId),
			contactsEntityId, contactsHelper.getOwnerType(contactEntityType),
			query, interestName, startDateFaroParam.getValue(),
			endDateFaroParam.getValue(), active, cur, delta,
			orderByFieldsFaroParam.getValue());

		return new FaroResultsDisplay(results);
	}

	@Path("/search")
	@POST
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay searchByForm(
			@PathParam("groupId") long groupId,
			@FormParam("contactsEntityId") String contactsEntityId,
			@FormParam("contactsEntityType") int contactsEntityType,
			@FormParam("query") String query,
			@FormParam("interestName") String interestName,
			@DefaultValue(StringPool.BLANK) @FormParam("startDate") FaroParam
				<Date> startDateFaroParam,
			@DefaultValue(StringPool.BLANK) @FormParam("endDate") FaroParam
				<Date> endDateFaroParam,
			@DefaultValue(StringPool.TRUE) @FormParam("active") boolean active,
			@FormParam("cur") int cur, @FormParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @FormParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		return search(
			groupId, contactsEntityId, contactsEntityType, query, interestName,
			startDateFaroParam, endDateFaroParam, active, cur, delta,
			orderByFieldsFaroParam);
	}

}