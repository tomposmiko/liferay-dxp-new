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

package com.liferay.analytics.reports.web.internal.portlet.action;

import com.liferay.analytics.reports.web.internal.constants.AnalyticsReportsPortletKeys;
import com.liferay.analytics.reports.web.internal.data.provider.AnalyticsReportsDataProvider;
import com.liferay.analytics.reports.web.internal.info.display.contributor.util.LayoutDisplayPageProviderUtil;
import com.liferay.analytics.reports.web.internal.layout.seo.CanonicalURLProvider;
import com.liferay.analytics.reports.web.internal.model.HistoricalMetric;
import com.liferay.analytics.reports.web.internal.model.TimeSpan;
import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.layout.display.page.LayoutDisplayPageObjectProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProviderTracker;
import com.liferay.layout.seo.kernel.LayoutSEOLinkManager;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author David Arques
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + AnalyticsReportsPortletKeys.ANALYTICS_REPORTS,
		"mvc.command.name=/analytics_reports/get_historical_views"
	},
	service = MVCResourceCommand.class
)
public class GetHistoricalViewsMVCResourceCommand
	extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			resourceRequest);

		try {
			LayoutDisplayPageObjectProvider<Object>
				layoutDisplayPageObjectProvider =
					(LayoutDisplayPageObjectProvider<Object>)
						LayoutDisplayPageProviderUtil.
							getLayoutDisplayPageObjectProvider(
								httpServletRequest,
								_layoutDisplayPageProviderTracker, _portal);

			if (layoutDisplayPageObjectProvider == null) {
				JSONPortletResponseUtil.writeJSON(
					resourceRequest, resourceResponse,
					JSONUtil.put(
						"error",
						_language.get(
							httpServletRequest,
							"an-unexpected-error-occurred")));

				return;
			}

			AnalyticsReportsDataProvider analyticsReportsDataProvider =
				new AnalyticsReportsDataProvider(_http);

			String timeSpanKey = ParamUtil.getString(
				resourceRequest, "timeSpanKey", TimeSpan.defaultTimeSpanKey());

			TimeSpan timeSpan = TimeSpan.of(timeSpanKey);

			int timeSpanOffset = ParamUtil.getInteger(
				resourceRequest, "timeSpanOffset");

			CanonicalURLProvider canonicalURLProvider =
				new CanonicalURLProvider(
					httpServletRequest, _layoutSEOLinkManager, _portal);

			HistoricalMetric historicalMetric =
				analyticsReportsDataProvider.getHistoricalViewsHistoricalMetric(
					_portal.getCompanyId(resourceRequest),
					timeSpan.toTimeRange(timeSpanOffset),
					canonicalURLProvider.getCanonicalURL());

			jsonObject.put(
				"analyticsReportsHistoricalViews",
				historicalMetric.toJSONObject());
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			ThemeDisplay themeDisplay =
				(ThemeDisplay)resourceRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			jsonObject.put(
				"error",
				LanguageUtil.get(
					themeDisplay.getRequest(), "an-unexpected-error-occurred"));
		}

		JSONPortletResponseUtil.writeJSON(
			resourceRequest, resourceResponse, jsonObject);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		GetHistoricalViewsMVCResourceCommand.class);

	@Reference
	private AssetDisplayPageFriendlyURLProvider
		_assetDisplayPageFriendlyURLProvider;

	@Reference
	private Http _http;

	@Reference
	private Language _language;

	@Reference
	private LayoutDisplayPageProviderTracker _layoutDisplayPageProviderTracker;

	@Reference
	private LayoutSEOLinkManager _layoutSEOLinkManager;

	@Reference
	private Portal _portal;

}