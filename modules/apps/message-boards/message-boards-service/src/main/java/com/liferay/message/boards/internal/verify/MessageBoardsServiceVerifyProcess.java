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

package com.liferay.message.boards.internal.verify;

import com.liferay.exportimport.kernel.staging.Staging;
import com.liferay.message.boards.constants.MBPortletKeys;
import com.liferay.message.boards.internal.verify.model.MBDiscussionVerifiableModel;
import com.liferay.message.boards.internal.verify.model.MBThreadFlagVerifiableModel;
import com.liferay.message.boards.internal.verify.model.MBThreadVerifiableModel;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.verify.VerifyAuditedModel;
import com.liferay.portal.verify.VerifyGroupedModel;
import com.liferay.portal.verify.VerifyProcess;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Zsolt Berentey
 */
@Component(
	immediate = true,
	property = "verify.process.name=com.liferay.message.boards.service",
	service = VerifyProcess.class
)
public class MessageBoardsServiceVerifyProcess extends VerifyProcess {

	@Override
	protected void doVerify() throws Exception {
		updateStagedPortletNames();
		verifyAuditedModels();
		verifyGroupedModels();
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	@Reference(
		target = "(&(release.bundle.symbolic.name=com.liferay.message.boards.service)(release.schema.version=1.1.0))",
		unbind = "-"
	)
	protected void setRelease(Release release) {
	}

	protected void updateStagedPortletNames() throws PortalException {
		ActionableDynamicQuery groupActionableDynamicQuery =
			_groupLocalService.getActionableDynamicQuery();

		groupActionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				Property siteProperty = PropertyFactoryUtil.forName("site");

				dynamicQuery.add(siteProperty.eq(Boolean.TRUE));
			});
		groupActionableDynamicQuery.setPerformActionMethod(
			(ActionableDynamicQuery.PerformActionMethod<Group>)group -> {
				UnicodeProperties typeSettingsProperties =
					group.getTypeSettingsProperties();

				if (typeSettingsProperties == null) {
					return;
				}

				String propertyKey = _staging.getStagedPortletId(
					MBPortletKeys.MESSAGE_BOARDS);

				String propertyValue = typeSettingsProperties.getProperty(
					propertyKey);

				if (Validator.isNull(propertyValue)) {
					return;
				}

				typeSettingsProperties.remove(propertyKey);

				propertyKey = _staging.getStagedPortletId(
					MBPortletKeys.MESSAGE_BOARDS_ADMIN);

				typeSettingsProperties.put(propertyKey, propertyValue);

				group.setTypeSettingsProperties(typeSettingsProperties);

				_groupLocalService.updateGroup(group);
			});

		groupActionableDynamicQuery.performActions();
	}

	protected void verifyAuditedModels() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			_verifyAuditedModel.verify(
				new MBDiscussionVerifiableModel(),
				new MBThreadVerifiableModel(),
				new MBThreadFlagVerifiableModel());
		}
	}

	protected void verifyGroupedModels() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			_verifyGroupedModel.verify(
				new MBDiscussionVerifiableModel(),
				new MBThreadFlagVerifiableModel());
		}
	}

	private GroupLocalService _groupLocalService;

	@Reference
	private Staging _staging;

	private final VerifyAuditedModel _verifyAuditedModel =
		new VerifyAuditedModel();
	private final VerifyGroupedModel _verifyGroupedModel =
		new VerifyGroupedModel();

}