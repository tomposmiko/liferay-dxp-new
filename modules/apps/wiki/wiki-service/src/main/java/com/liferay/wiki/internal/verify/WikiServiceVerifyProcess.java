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

package com.liferay.wiki.internal.verify;

import com.liferay.exportimport.kernel.staging.Staging;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.verify.VerifyProcess;
import com.liferay.wiki.constants.WikiPortletKeys;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(service = VerifyProcess.class)
public class WikiServiceVerifyProcess extends VerifyProcess {

	@Override
	protected void doVerify() throws Exception {
		if (!FeatureFlagManagerUtil.isEnabled("LPS-157670")) {
			return;
		}

		updateStagedPortletNames();
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
				UnicodeProperties typeSettingsUnicodeProperties =
					group.getTypeSettingsProperties();

				if (typeSettingsUnicodeProperties == null) {
					return;
				}

				String propertyKey = _staging.getStagedPortletId(
					WikiPortletKeys.WIKI);

				String propertyValue =
					typeSettingsUnicodeProperties.getProperty(propertyKey);

				if (Validator.isNull(propertyValue)) {
					return;
				}

				typeSettingsUnicodeProperties.remove(propertyKey);

				propertyKey = _staging.getStagedPortletId(
					WikiPortletKeys.WIKI_ADMIN);

				typeSettingsUnicodeProperties.put(propertyKey, propertyValue);

				group.setTypeSettingsProperties(typeSettingsUnicodeProperties);

				_groupLocalService.updateGroup(group);
			});

		groupActionableDynamicQuery.performActions();
	}

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference(
		target = "(&(release.bundle.symbolic.name=com.liferay.wiki.service)(release.schema.version>=2.3.0))"
	)
	private Release _release;

	@Reference
	private Staging _staging;

}