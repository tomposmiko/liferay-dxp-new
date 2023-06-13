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

package com.liferay.sharing.web.internal.portlet.action;

import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceComparator;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderConstants;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.sharing.display.context.util.SharingMenuItemFactory;
import com.liferay.sharing.exception.NoSuchEntryException;
import com.liferay.sharing.filter.SharedWithMeFilterItem;
import com.liferay.sharing.interpreter.SharingEntryInterpreter;
import com.liferay.sharing.interpreter.SharingEntryInterpreterProvider;
import com.liferay.sharing.model.SharingEntry;
import com.liferay.sharing.renderer.SharingEntryViewRenderer;
import com.liferay.sharing.service.SharingEntryLocalService;
import com.liferay.sharing.web.internal.constants.SharingPortletKeys;
import com.liferay.sharing.web.internal.display.context.SharedWithMeViewDisplayContext;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + SharingPortletKeys.SHARED_WITH_ME,
		"mvc.command.name=/",
		"mvc.command.name=/shared_with_me/select_asset_type",
		"mvc.command.name=/shared_with_me/view",
		"mvc.command.name=/shared_with_me/view_sharing_entry"
	},
	service = MVCRenderCommand.class
)
public class SharedWithMeViewMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		String mvcRenderCommandName = ParamUtil.getString(
			renderRequest, "mvcRenderCommandName");

		if (Objects.equals(
				mvcRenderCommandName, "/shared_with_me/view_sharing_entry")) {

			ThemeDisplay themeDisplay =
				(ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			try {
				SharingEntry sharingEntry = _getSharingEntry(
					renderRequest, themeDisplay);

				if (sharingEntry.getToUserId() != themeDisplay.getUserId()) {
					throw new PrincipalException(
						StringBundler.concat(
							"User ", themeDisplay.getUserId(),
							" does not have permission to view sharing entry ",
							sharingEntry.getSharingEntryId()));
				}

				SharingEntryInterpreter sharingEntryInterpreter =
					_sharingEntryInterpreterProvider.getSharingEntryInterpreter(
						sharingEntry);

				if (sharingEntryInterpreter == null) {
					throw new PortletException(
						"Sharing entry interpreter is null for class name ID " +
							sharingEntry.getClassNameId());
				}

				SharingEntryViewRenderer sharingEntryViewRenderer =
					sharingEntryInterpreter.getSharingEntryViewRenderer();

				sharingEntryViewRenderer.render(
					sharingEntry, _portal.getHttpServletRequest(renderRequest),
					_portal.getHttpServletResponse(renderResponse));

				return MVCRenderConstants.MVC_PATH_VALUE_SKIP_DISPATCH;
			}
			catch (PortalException pe) {
				SessionErrors.add(renderRequest, pe.getClass());

				return "/shared_with_me/error.jsp";
			}
			catch (IOException ioe) {
				throw new PortletException(ioe);
			}
		}

		List<SharedWithMeFilterItem> sharedWithMeFilterItems =
			new ArrayList<>();

		_serviceTrackerList.forEach(sharedWithMeFilterItems::add);

		LiferayPortletRequest liferayPortletRequest =
			_portal.getLiferayPortletRequest(renderRequest);
		LiferayPortletResponse liferayPortletResponse =
			_portal.getLiferayPortletResponse(renderResponse);

		HttpServletRequest request = _portal.getHttpServletRequest(
			renderRequest);

		ResourceBundle resourceBundle =
			_resourceBundleLoader.loadResourceBundle(
				_portal.getLocale(request));

		SharedWithMeViewDisplayContext sharedWithMeViewDisplayContext =
			new SharedWithMeViewDisplayContext(
				liferayPortletRequest, liferayPortletResponse, request,
				resourceBundle, _sharingEntryLocalService,
				_sharingEntryInterpreterProvider::getSharingEntryInterpreter,
				sharedWithMeFilterItems, _sharingMenuItemFactory);

		renderRequest.setAttribute(
			SharedWithMeViewDisplayContext.class.getName(),
			sharedWithMeViewDisplayContext);

		if (Objects.equals(
				mvcRenderCommandName, "/shared_with_me/select_asset_type")) {

			return "/shared_with_me/select_asset_type.jsp";
		}

		return "/shared_with_me/view.jsp";
	}

	@Activate
	protected void activate(final BundleContext bundleContext) {
		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext, SharedWithMeFilterItem.class,
			Collections.reverseOrder(
				new PropertyServiceReferenceComparator<>(
					"navigation.item.order")));
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerList.close();
	}

	private SharingEntry _getSharingEntry(
			RenderRequest renderRequest, ThemeDisplay themeDisplay)
		throws PortalException {

		try {
			long sharingEntryId = ParamUtil.getLong(
				renderRequest, "sharingEntryId");

			return _sharingEntryLocalService.getSharingEntry(sharingEntryId);
		}
		catch (NoSuchEntryException nsee) {
			long classNameId = ParamUtil.getLong(renderRequest, "classNameId");
			long classPK = ParamUtil.getLong(renderRequest, "classPK");

			List<SharingEntry> sharingEntries =
				_sharingEntryLocalService.getSharingEntries(
					themeDisplay.getUserId(), classNameId, classPK);

			if (sharingEntries.isEmpty()) {
				throw nsee;
			}

			return sharingEntries.get(0);
		}
	}

	@Reference
	private Portal _portal;

	@Reference(target = "(bundle.symbolic.name=com.liferay.sharing.web)")
	private ResourceBundleLoader _resourceBundleLoader;

	private ServiceTrackerList<SharedWithMeFilterItem, SharedWithMeFilterItem>
		_serviceTrackerList;

	@Reference
	private SharingEntryInterpreterProvider _sharingEntryInterpreterProvider;

	@Reference
	private SharingEntryLocalService _sharingEntryLocalService;

	@Reference
	private SharingMenuItemFactory _sharingMenuItemFactory;

}