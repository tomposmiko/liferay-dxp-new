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

import com.liferay.osb.faro.engine.client.model.Activity;
import com.liferay.osb.faro.engine.client.model.ActivityAggregation;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.engine.client.util.OrderByField;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.FaroController;
import com.liferay.osb.faro.web.internal.model.display.FaroResultsDisplay;
import com.liferay.osb.faro.web.internal.model.display.contacts.ActivityDisplay;
import com.liferay.osb.faro.web.internal.model.display.contacts.ActivityHistoryDisplay;
import com.liferay.osb.faro.web.internal.param.FaroParam;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.RoleConstants;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

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
@Component(
	immediate = true, service = {ActivityController.class, FaroController.class}
)
@Path("/{groupId}/activity")
@Produces(MediaType.APPLICATION_JSON)
public class ActivityController extends BaseFaroController {

	@GET
	@Path("/{id}")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public ActivityDisplay get(
			@PathParam("groupId") long groupId, @PathParam("id") String id)
		throws Exception {

		return new ActivityDisplay(
			contactsEngineClient.getActivity(
				faroProjectLocalService.getFaroProjectByGroupId(groupId), id));
	}

	@GET
	@Path("/history")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public ActivityHistoryDisplay getHistory(
			@PathParam("groupId") long groupId,
			@QueryParam("channelId") String channelId,
			@QueryParam("contactsEntityId") String contactsEntityId,
			@QueryParam("contactsEntityType") int contactsEntityType,
			@QueryParam("interval") String interval, @QueryParam("max") int max,
			@QueryParam("rangeEnd") String rangeEnd,
			@QueryParam("rangeStart") String rangeStart)
		throws Exception {

		Results<ActivityAggregation> results =
			contactsEngineClient.getActivityAggregations(
				faroProjectLocalService.getFaroProjectByGroupId(groupId),
				channelId, contactsEntityId,
				contactsHelper.getOwnerType(contactsEntityType), rangeEnd,
				rangeStart, interval, max * 2);

		List<ActivityAggregation> activityAggregations = results.getItems();

		return new ActivityHistoryDisplay(
			activityAggregations.subList(
				activityAggregations.size() / 2, activityAggregations.size()),
			activityAggregations.subList(0, activityAggregations.size() / 2));
	}

	@GET
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	@SuppressWarnings("unchecked")
	public FaroResultsDisplay search(
			@PathParam("groupId") long groupId,
			@QueryParam("contactsEntityId") String contactsEntityId,
			@QueryParam("contactsEntityType") int contactsEntityType,
			@QueryParam("query") String query,
			@DefaultValue(StringPool.BLANK) @QueryParam("startDate") FaroParam
				<Date> startDateFaroParam,
			@DefaultValue(StringPool.BLANK) @QueryParam("endDate") FaroParam
				<Date> endDateFaroParam,
			@QueryParam("action") int action, @QueryParam("cur") int cur,
			@QueryParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @QueryParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		Results<Activity> results = contactsEngineClient.getActivities(
			faroProjectLocalService.getFaroProjectByGroupId(groupId),
			contactsEntityId, contactsHelper.getOwnerType(contactsEntityType),
			null, query, startDateFaroParam.getValue(),
			endDateFaroParam.getValue(), action, cur, delta,
			orderByFieldsFaroParam.getValue());

		Function<Activity, ActivityDisplay> function = ActivityDisplay::new;

		return new FaroResultsDisplay(results, function);
	}

	@GET
	@Path("/asset")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay searchAssets(
			@PathParam("groupId") long groupId,
			@QueryParam("query") String query,
			@QueryParam("applicationId") String applicationId,
			@QueryParam("channelId") String channelId,
			@QueryParam("eventId") String eventId, @QueryParam("cur") int cur,
			@QueryParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @QueryParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		return new FaroResultsDisplay(
			contactsEngineClient.getActivityAssets(
				faroProjectLocalService.getFaroProjectByGroupId(groupId), query,
				applicationId, channelId, eventId, cur, delta,
				orderByFieldsFaroParam.getValue()));
	}

	@Path("/search")
	@POST
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay searchByForm(
			@PathParam("groupId") long groupId,
			@FormParam("contactsEntityId") String contactsEntityId,
			@QueryParam("contactsEntityType") int contactsEntityType,
			@FormParam("query") String query,
			@DefaultValue(StringPool.BLANK) @FormParam("startDate") FaroParam
				<Date> startDateFaroParam,
			@DefaultValue(StringPool.BLANK) @FormParam("endDate") FaroParam
				<Date> endDateFaroParam,
			@FormParam("action") int action, @QueryParam("cur") int cur,
			@FormParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @FormParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		return search(
			groupId, contactsEntityId, contactsEntityType, query,
			startDateFaroParam, endDateFaroParam, action, cur, delta,
			orderByFieldsFaroParam);
	}

	@GET
	@Path("/count")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public int searchCount(
			@PathParam("groupId") long groupId,
			@QueryParam("contactsEntityId") String contactsEntityId,
			@QueryParam("contactsEntityType") int contactsEntityType,
			@QueryParam("query") String query,
			@DefaultValue(StringPool.BLANK) @QueryParam("startDate") FaroParam
				<Date> startDateFaroParam,
			@DefaultValue(StringPool.BLANK) @QueryParam("endDate") FaroParam
				<Date> endDateFaroParam,
			@QueryParam("action") int action)
		throws Exception {

		Results<Activity> results = contactsEngineClient.getActivities(
			faroProjectLocalService.getFaroProjectByGroupId(groupId),
			contactsEntityId, contactsHelper.getOwnerType(contactsEntityType),
			null, null, startDateFaroParam.getValue(),
			endDateFaroParam.getValue(), action, 1, 1, null);

		return results.getTotal();
	}

}