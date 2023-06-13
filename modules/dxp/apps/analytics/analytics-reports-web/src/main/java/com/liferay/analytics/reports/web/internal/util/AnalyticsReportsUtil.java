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

package com.liferay.analytics.reports.web.internal.util;

import com.liferay.analytics.reports.info.item.ClassNameClassPKInfoItemIdentifier;
import com.liferay.analytics.reports.web.internal.constants.AnalyticsReportsPortletKeys;
import com.liferay.analytics.settings.rest.manager.AnalyticsSettingsManager;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.InfoItemReference;
import com.liferay.portal.kernel.change.tracking.CTCollectionThreadLocal;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sarai Díaz
 */
public class AnalyticsReportsUtil {

	public static final String ANALYTICS_CLOUD_TRIAL_URL =
		"https://www.liferay.com/products/analytics-cloud/get-started";

	public static String getAnalyticsReportsPanelURL(
			InfoItemReference infoItemReference,
			HttpServletRequest httpServletRequest, Portal portal,
			PortletURLFactory portletURLFactory)
		throws WindowStateException {

		PortletURL portletURL = PortletURLBuilder.create(
			portletURLFactory.create(
				httpServletRequest,
				AnalyticsReportsPortletKeys.ANALYTICS_REPORTS,
				RenderRequest.RENDER_PHASE)
		).setMVCPath(
			"/analytics_reports_panel.jsp"
		).setRedirect(
			portal.getCurrentCompleteURL(httpServletRequest)
		).setParameter(
			"className", infoItemReference.getClassName()
		).buildPortletURL();

		if (infoItemReference.getInfoItemIdentifier() instanceof
				ClassNameClassPKInfoItemIdentifier) {

			ClassNameClassPKInfoItemIdentifier
				classNameClassPKInfoItemIdentifier =
					(ClassNameClassPKInfoItemIdentifier)
						infoItemReference.getInfoItemIdentifier();

			portletURL.setParameter(
				"classPK",
				String.valueOf(
					classNameClassPKInfoItemIdentifier.getClassPK()));
			portletURL.setParameter(
				"classTypeName",
				classNameClassPKInfoItemIdentifier.getClassName());
		}
		else if (infoItemReference.getInfoItemIdentifier() instanceof
					ClassPKInfoItemIdentifier) {

			ClassPKInfoItemIdentifier classPKInfoItemIdentifier =
				(ClassPKInfoItemIdentifier)
					infoItemReference.getInfoItemIdentifier();

			portletURL.setParameter(
				"classPK",
				String.valueOf(classPKInfoItemIdentifier.getClassPK()));
		}

		portletURL.setWindowState(LiferayWindowState.EXCLUSIVE);

		return portletURL.toString();
	}

	public static boolean isShowAnalyticsReportsPanel(
			AnalyticsSettingsManager analyticsSettingsManager, long companyId,
			HttpServletRequest httpServletRequest)
		throws Exception {

		if (!CTCollectionThreadLocal.isProductionMode()) {
			return false;
		}

		String layoutMode = ParamUtil.getString(
			httpServletRequest, "p_l_mode", Constants.VIEW);

		if (layoutMode.equals(Constants.EDIT)) {
			return false;
		}

		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(
				httpServletRequest);

		boolean hidePanel = GetterUtil.getBoolean(
			portalPreferences.getValue(
				AnalyticsReportsPortletKeys.ANALYTICS_REPORTS, "hide-panel"));

		if (!analyticsSettingsManager.isAnalyticsEnabled(companyId) &&
			hidePanel) {

			return false;
		}

		return true;
	}

}