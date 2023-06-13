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

package com.liferay.segments.content.targeting.upgrade.internal.upgrade.v1_0_0;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeProcessFactory;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.segments.constants.SegmentsEntryConstants;
import com.liferay.segments.content.targeting.upgrade.internal.upgrade.v1_0_0.util.RuleConverter;
import com.liferay.segments.content.targeting.upgrade.internal.upgrade.v1_0_0.util.RuleConverterRegistry;
import com.liferay.segments.criteria.Criteria;
import com.liferay.segments.criteria.CriteriaSerializer;
import com.liferay.segments.service.SegmentsEntryLocalService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Locale;
import java.util.Map;

/**
 * @author Eduardo García
 */
public class ContentTargetingUpgradeProcess extends UpgradeProcess {

	public ContentTargetingUpgradeProcess(
		RuleConverterRegistry ruleConverterRegistry,
		SegmentsEntryLocalService segmentsEntryLocalService) {

		_ruleConverterRegistry = ruleConverterRegistry;
		_segmentsEntryLocalService = segmentsEntryLocalService;
	}

	@Override
	public void doUpgrade() throws Exception {
		if (!hasTable("CT_UserSegment")) {
			return;
		}

		_upgradeContentTargetingUserSegments();
		_deleteContentTargetingData();
	}

	@Override
	protected UpgradeStep[] getPostUpgradeSteps() {
		return new UpgradeStep[] {
			UpgradeProcessFactory.dropTables(
				"CT_AU_AnonymousUser", "CT_Analytics_AnalyticsEvent",
				"CT_Analytics_AnalyticsReferrer", "CT_AnonymousUserUserSegment",
				"CT_CCR_CampaignContent", "CT_CTA_CTAction",
				"CT_CTA_CTActionTotal", "CT_Campaign",
				"CT_Campaigns_UserSegments", "CT_ChannelInstance",
				"CT_ReportInstance", "CT_RuleInstance",
				"CT_ScorePoints_ScorePoint", "CT_Tactic",
				"CT_Tactics_UserSegments", "CT_TrackingActionInstance",
				"CT_USCR_UserSegmentContent", "CT_UserSegment",
				"CT_Visited_ContentVisited", "CT_Visited_PageVisited")
		};
	}

	private void _deleteContentTargetingData() throws Exception {
		runSQL(
			"delete from ClassName_ where value like '" + _CT_PACKAGE_NAME +
				"%'");

		runSQL(
			"delete from Release_ where servletContextName like '" +
				_CT_PACKAGE_NAME + "%'");

		runSQL(
			"delete from ResourceAction where name like '" + _CT_PACKAGE_NAME +
				"%'");

		runSQL(
			"delete from ResourcePermission where name like '" +
				_CT_PACKAGE_NAME + "%'");

		runSQL("delete from ServiceComponent where buildNamespace like 'CT%'");
	}

	private String _getCriteria(long userSegmentId) throws Exception {
		Criteria criteria = new Criteria();

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"select companyId, ruleKey, typeSettings from " +
					"CT_RuleInstance where userSegmentId = ?")) {

			preparedStatement.setLong(1, userSegmentId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					String ruleKey = resultSet.getString("ruleKey");

					RuleConverter ruleConverter =
						_ruleConverterRegistry.getRuleConverter(ruleKey);

					if (ruleConverter == null) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to perform automated update of rule " +
									ruleKey);
						}

						continue;
					}

					long companyId = resultSet.getLong("companyId");
					String typeSettings = resultSet.getString("typeSettings");

					ruleConverter.convert(companyId, criteria, typeSettings);
				}
			}
		}

		return CriteriaSerializer.serialize(criteria);
	}

	private void _upgradeContentTargetingUserSegments() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer();
			PreparedStatement preparedStatement1 = connection.prepareStatement(
				"select * from CT_UserSegment");
			ResultSet resultSet = preparedStatement1.executeQuery()) {

			ServiceContext serviceContext = new ServiceContext();

			while (resultSet.next()) {
				long userSegmentId = resultSet.getLong("userSegmentId");

				if (_log.isInfoEnabled()) {
					_log.info(
						"Upgrading Content Targeting User Segment " +
							userSegmentId);
				}

				String name = resultSet.getString("name");

				Map<Locale, String> nameMap =
					LocalizationUtil.getLocalizationMap(name);

				Map<Locale, String> descriptionMap =
					LocalizationUtil.getLocalizationMap(
						resultSet.getString("description"));

				serviceContext.setScopeGroupId(resultSet.getLong("groupId"));
				serviceContext.setUserId(
					PortalUtil.getValidUserId(
						resultSet.getLong("companyId"),
						resultSet.getLong("userId")));

				Locale defaultLocale = LocaleUtil.fromLanguageId(
					LocalizationUtil.getDefaultLanguageId(name));

				Locale currentDefaultLocale =
					LocaleThreadLocal.getSiteDefaultLocale();

				try {
					LocaleThreadLocal.setSiteDefaultLocale(defaultLocale);

					_segmentsEntryLocalService.addSegmentsEntry(
						"ct_" + userSegmentId, nameMap, descriptionMap, true,
						_getCriteria(userSegmentId),
						SegmentsEntryConstants.SOURCE_DEFAULT,
						User.class.getName(), serviceContext);
				}
				finally {
					LocaleThreadLocal.setSiteDefaultLocale(
						currentDefaultLocale);
				}
			}
		}
	}

	private static final String _CT_PACKAGE_NAME =
		"com.liferay.content.targeting";

	private static final Log _log = LogFactoryUtil.getLog(
		ContentTargetingUpgradeProcess.class);

	private final RuleConverterRegistry _ruleConverterRegistry;
	private final SegmentsEntryLocalService _segmentsEntryLocalService;

}