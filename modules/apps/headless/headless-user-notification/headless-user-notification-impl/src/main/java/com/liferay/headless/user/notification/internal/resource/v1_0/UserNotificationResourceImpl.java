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

package com.liferay.headless.user.notification.internal.resource.v1_0;

import com.liferay.headless.user.notification.dto.v1_0.UserNotification;
import com.liferay.headless.user.notification.internal.dto.v1_0.UserNotificationDTOConverter;
import com.liferay.headless.user.notification.internal.odata.entity.v1_0.UserNotificationEntityModel;
import com.liferay.headless.user.notification.resource.v1_0.UserNotificationResource;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.UserNotificationEventService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.ActionUtil;
import com.liferay.portal.vulcan.util.SearchUtil;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MultivaluedMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Carlos Correa
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/user-notification.properties",
	scope = ServiceScope.PROTOTYPE, service = UserNotificationResource.class
)
public class UserNotificationResourceImpl
	extends BaseUserNotificationResourceImpl {

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap)
		throws Exception {

		return _entityModel;
	}

	@Override
	public Page<UserNotification> getMyUserNotificationsPage(
			String search, Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled("LPS-83384")) {
			throw new NotFoundException();
		}

		return _getPage(
			filter, pagination, search, sorts, contextUser.getUserId());
	}

	@Override
	public Page<UserNotification> getUserAccountUserNotificationsPage(
			Long userAccountId, String search, Filter filter,
			Pagination pagination, Sort[] sorts)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled("LPS-83384")) {
			throw new NotFoundException();
		}

		return _getPage(filter, pagination, search, sorts, userAccountId);
	}

	@Override
	public UserNotification getUserNotification(Long userNotificationId)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled("LPS-83384")) {
			throw new NotFoundException();
		}

		return _toUserNotification(
			_userNotificationEventService.getUserNotificationEvent(
				userNotificationId));
	}

	@Override
	public void putUserNotificationRead(Long userNotificationId)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled("LPS-83384")) {
			throw new NotFoundException();
		}

		UserNotificationEvent userNotificationEvent =
			_userNotificationEventService.getUserNotificationEvent(
				userNotificationId);

		_userNotificationEventService.updateUserNotificationEvent(
			userNotificationEvent.getUuid(),
			userNotificationEvent.getCompanyId(), true);
	}

	@Override
	public void putUserNotificationUnread(Long userNotificationId)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled("LPS-83384")) {
			throw new NotFoundException();
		}

		UserNotificationEvent userNotificationEvent =
			_userNotificationEventService.getUserNotificationEvent(
				userNotificationId);

		_userNotificationEventService.updateUserNotificationEvent(
			userNotificationEvent.getUuid(),
			userNotificationEvent.getCompanyId(), false);
	}

	private Page<UserNotification> _getPage(
			Filter filter, Pagination pagination, String search, Sort[] sorts,
			long userId)
		throws Exception {

		return SearchUtil.search(
			null,
			booleanQuery -> {
			},
			filter, UserNotificationEvent.class.getName(), search, pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			searchContext -> {
				searchContext.setAttribute(Field.USER_ID, userId);
				searchContext.setCompanyId(contextCompany.getCompanyId());
			},
			sorts,
			document -> _toUserNotification(
				_userNotificationEventService.getUserNotificationEvent(
					GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK)))));
	}

	private UserNotification _toUserNotification(
			UserNotificationEvent userNotificationEvent)
		throws Exception {

		return _userNotificationDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				contextAcceptLanguage.isAcceptAllLanguages(),
				HashMapBuilder.put(
					"get",
					ActionUtil.addAction(
						ActionKeys.VIEW, getClass(),
						userNotificationEvent.getUserId(),
						"getUserNotification", _userModelResourcePermission,
						userNotificationEvent.getUserNotificationEventId(),
						contextUriInfo)
				).put(
					"mark-read",
					ActionUtil.addAction(
						ActionKeys.UPDATE, getClass(),
						userNotificationEvent.getUserId(),
						"putUserNotificationRead", _userModelResourcePermission,
						userNotificationEvent.getUserNotificationEventId(),
						contextUriInfo)
				).put(
					"mark-unread",
					ActionUtil.addAction(
						ActionKeys.UPDATE, getClass(),
						userNotificationEvent.getUserId(),
						"putUserNotificationUnread",
						_userModelResourcePermission,
						userNotificationEvent.getUserNotificationEventId(),
						contextUriInfo)
				).build(),
				null, contextHttpServletRequest,
				userNotificationEvent.getUserNotificationEventId(),
				contextAcceptLanguage.getPreferredLocale(), contextUriInfo,
				contextUser),
			userNotificationEvent);
	}

	private static final EntityModel _entityModel =
		new UserNotificationEntityModel();

	@Reference(
		target = "(model.class.name=com.liferay.portal.kernel.model.User)"
	)
	private ModelResourcePermission<User> _userModelResourcePermission;

	@Reference
	private UserNotificationDTOConverter _userNotificationDTOConverter;

	@Reference
	private UserNotificationEventService _userNotificationEventService;

}