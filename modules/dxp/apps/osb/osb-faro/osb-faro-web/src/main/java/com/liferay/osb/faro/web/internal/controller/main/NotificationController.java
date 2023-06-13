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

package com.liferay.osb.faro.web.internal.controller.main;

import com.liferay.osb.faro.constants.FaroNotificationConstants;
import com.liferay.osb.faro.engine.client.CerebroEngineClient;
import com.liferay.osb.faro.model.FaroNotification;
import com.liferay.osb.faro.service.FaroNotificationLocalService;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.FaroController;
import com.liferay.osb.faro.web.internal.model.display.contacts.NotificationDisplay;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Geyson Silva
 */
@Component(service = {FaroController.class, NotificationController.class})
@Path("/{groupId}/notification")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationController extends BaseFaroController {

	@DELETE
	@Path("/{id}")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public void deleteNotification(
			@PathParam("groupId") long groupId, @PathParam("id") long id)
		throws PortalException {

		_faroNotificationLocalService.deleteFaroNotification(id);
	}

	@GET
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public List<NotificationDisplay> getNotifications(
			@PathParam("groupId") long groupId, @QueryParam("type") String type)
		throws Exception {

		if (StringUtil.equals(FaroNotificationConstants.TYPE_ALERT, type)) {
			boolean customEventsLimitReached =
				_cerebroEngineClient.isCustomEventsLimitReached(
					faroProjectLocalService.getFaroProjectByGroupId(groupId));

			long faroNotificationsLast30DaysCount =
				_faroNotificationLocalService.
					getFaroNotificationsLast30DaysCount(
						groupId,
						FaroNotificationConstants.SUBTYPE_BLOCKED_EVENTS_LIMIT,
						type, getUserId());

			if (customEventsLimitReached &&
				(faroNotificationsLast30DaysCount == 0)) {

				_faroNotificationLocalService.addFaroNotification(
					getUserId(), groupId, getUserId(),
					FaroNotificationConstants.SCOPE_USER, type,
					FaroNotificationConstants.SUBTYPE_BLOCKED_EVENTS_LIMIT);
			}
			else if (!customEventsLimitReached &&
					 (faroNotificationsLast30DaysCount > 0)) {

				_faroNotificationLocalService.deleteFaroNotifications(
					groupId, type,
					FaroNotificationConstants.SUBTYPE_BLOCKED_EVENTS_LIMIT,
					getUserId());
			}
		}

		return TransformUtil.transform(
			_faroNotificationLocalService.findFaroNotificationsLast30Days(
				groupId, type, getUserId()),
			NotificationDisplay::new);
	}

	@Path("/{id}/read")
	@POST
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public void readNotification(
			@PathParam("groupId") long groupId, @PathParam("id") long id)
		throws PortalException {

		FaroNotification faroNotification =
			_faroNotificationLocalService.getFaroNotification(id);

		faroNotification.setRead(true);

		_faroNotificationLocalService.updateFaroNotification(faroNotification);
	}

	@Reference
	private CerebroEngineClient _cerebroEngineClient;

	@Reference
	private FaroNotificationLocalService _faroNotificationLocalService;

}