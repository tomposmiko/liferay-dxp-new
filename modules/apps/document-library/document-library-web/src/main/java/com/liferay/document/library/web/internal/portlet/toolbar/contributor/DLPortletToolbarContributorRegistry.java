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

package com.liferay.document.library.web.internal.portlet.toolbar.contributor;

import com.liferay.document.library.portlet.toolbar.contributor.DLPortletToolbarContributor;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.servlet.taglib.ui.Menu;
import com.liferay.portal.kernel.servlet.taglib.ui.MenuItem;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true, service = DLPortletToolbarContributorRegistry.class
)
public class DLPortletToolbarContributorRegistry {

	public DLPortletToolbarContributor getDLPortletToolbarContributor() {
		return _dlPortletToolbarContributor;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_dlPortletToolbarContributor =
			new AggregateDLPortletToolbarContributor();
		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext, DLPortletToolbarContributor.class);
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerList.close();
	}

	private DLPortletToolbarContributor _dlPortletToolbarContributor;
	private ServiceTrackerList
		<DLPortletToolbarContributor, DLPortletToolbarContributor>
			_serviceTrackerList;

	private class AggregateDLPortletToolbarContributor
		implements DLPortletToolbarContributor {

		@Override
		public List<MenuItem> getPortletTitleAddDocumentMenuItems(
			Folder folder, ThemeDisplay themeDisplay,
			PortletRequest portletRequest) {

			List<MenuItem> menus = new ArrayList<>();

			_serviceTrackerList.forEach(
				dlPortletToolbarContributor -> menus.addAll(
					dlPortletToolbarContributor.
						getPortletTitleAddDocumentMenuItems(
							folder, themeDisplay, portletRequest)));

			return menus;
		}

		@Override
		public MenuItem getPortletTitleAddFolderMenuItem(
			ThemeDisplay themeDisplay, PortletRequest portletRequest,
			Folder folder) {

			for (DLPortletToolbarContributor dlPortletToolbarContributor :
					_serviceTrackerList) {

				MenuItem menuItem =
					dlPortletToolbarContributor.
						getPortletTitleAddFolderMenuItem(
							themeDisplay, portletRequest, folder);

				if (menuItem != null) {
					return menuItem;
				}
			}

			return null;
		}

		@Override
		public MenuItem getPortletTitleAddMultipleDocumentsMenuItem(
			ThemeDisplay themeDisplay, PortletRequest portletRequest,
			Folder folder) {

			for (DLPortletToolbarContributor dlPortletToolbarContributor :
					_serviceTrackerList) {

				MenuItem menuItem =
					dlPortletToolbarContributor.
						getPortletTitleAddMultipleDocumentsMenuItem(
							themeDisplay, portletRequest, folder);

				if (menuItem != null) {
					return menuItem;
				}
			}

			return null;
		}

		@Override
		public List<Menu> getPortletTitleMenus(
			PortletRequest portletRequest, PortletResponse portletResponse) {

			List<Menu> menus = new ArrayList<>();

			_serviceTrackerList.forEach(
				dlPortletToolbarContributor -> menus.addAll(
					dlPortletToolbarContributor.getPortletTitleMenus(
						portletRequest, portletResponse)));

			return menus;
		}

	}

}