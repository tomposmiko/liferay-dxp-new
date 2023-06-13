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

package com.liferay.analytics.reports.web.internal.item.action.provider;

import com.liferay.analytics.reports.info.action.provider.AnalyticsReportsContentDashboardItemActionProvider;
import com.liferay.analytics.reports.info.item.AnalyticsReportsInfoItemTracker;
import com.liferay.analytics.reports.web.internal.item.action.AnalyticsReportsContentDashboardItemAction;
import com.liferay.analytics.reports.web.internal.util.AnalyticsReportsUtil;
import com.liferay.asset.display.page.util.AssetDisplayPageUtil;
import com.liferay.content.dashboard.item.action.ContentDashboardItemAction;
import com.liferay.content.dashboard.item.action.exception.ContentDashboardItemActionException;
import com.liferay.info.item.InfoItemReference;
import com.liferay.layout.display.page.LayoutDisplayPageObjectProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProviderTracker;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoader;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author David Arques
 */
@Component(service = AnalyticsReportsContentDashboardItemActionProvider.class)
public class AnalyticsReportsContentDashboardItemActionProviderImpl
	implements AnalyticsReportsContentDashboardItemActionProvider {

	@Override
	public ContentDashboardItemAction getContentDashboardItemAction(
			String className, long classPK,
			HttpServletRequest httpServletRequest)
		throws ContentDashboardItemActionException {

		try {
			if (!isShowContentDashboardItemAction(
					className, classPK, httpServletRequest)) {

				return null;
			}

			LayoutDisplayPageObjectProvider<?> layoutDisplayPageObjectProvider =
				_getLayoutDisplayPageObjectProvider(className, classPK);

			return new AnalyticsReportsContentDashboardItemAction(
				_resourceBundleLoader,
				AnalyticsReportsUtil.getAnalyticsReportsPanelURL(
					layoutDisplayPageObjectProvider.getClassNameId(),
					layoutDisplayPageObjectProvider.getClassPK(),
					layoutDisplayPageObjectProvider.getGroupId(),
					httpServletRequest, _portal, _portletURLFactory));
		}
		catch (PortalException | WindowStateException exception) {
			throw new ContentDashboardItemActionException(exception);
		}
	}

	@Override
	public boolean isShowContentDashboardItemAction(
			String className, long classPK,
			HttpServletRequest httpServletRequest)
		throws PortalException {

		LayoutDisplayPageObjectProvider<?> layoutDisplayPageObjectProvider =
			_getLayoutDisplayPageObjectProvider(className, classPK);

		if ((layoutDisplayPageObjectProvider == null) ||
			(layoutDisplayPageObjectProvider.getDisplayObject() == null)) {

			return false;
		}

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			AssetDisplayPageUtil.getAssetDisplayPageLayoutPageTemplateEntry(
				layoutDisplayPageObjectProvider.getGroupId(),
				layoutDisplayPageObjectProvider.getClassNameId(),
				layoutDisplayPageObjectProvider.getClassPK(),
				layoutDisplayPageObjectProvider.getClassTypeId());

		if (layoutPageTemplateEntry == null) {
			return false;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (AnalyticsReportsUtil.isShowAnalyticsReportsPanel(
				_analyticsReportsInfoItemTracker, themeDisplay.getCompanyId(),
				httpServletRequest,
				_layoutLocalService.fetchLayout(
					layoutPageTemplateEntry.getPlid()),
				layoutDisplayPageObjectProvider,
				themeDisplay.getPermissionChecker(), _portal)) {

			return true;
		}

		return false;
	}

	private LayoutDisplayPageObjectProvider<?>
		_getLayoutDisplayPageObjectProvider(String className, long classPK) {

		LayoutDisplayPageProvider<?> layoutDisplayPageProvider =
			_layoutDisplayPageProviderTracker.
				getLayoutDisplayPageProviderByClassName(className);

		if (layoutDisplayPageProvider == null) {
			return null;
		}

		return layoutDisplayPageProvider.getLayoutDisplayPageObjectProvider(
			new InfoItemReference(className, classPK));
	}

	@Reference
	private AnalyticsReportsInfoItemTracker _analyticsReportsInfoItemTracker;

	@Reference
	private LayoutDisplayPageProviderTracker _layoutDisplayPageProviderTracker;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletURLFactory _portletURLFactory;

	@Reference(
		target = "(bundle.symbolic.name=com.liferay.analytics.reports.web)"
	)
	private ResourceBundleLoader _resourceBundleLoader;

}