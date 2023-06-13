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

package com.liferay.trash.web.internal.portlet.configuration.icon;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.TrashedModel;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.configuration.icon.BaseJSPPortletConfigurationIcon;
import com.liferay.portal.kernel.portlet.configuration.icon.PortletConfigurationIcon;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.trash.TrashHelper;
import com.liferay.trash.constants.TrashPortletKeys;
import com.liferay.trash.model.TrashEntry;
import com.liferay.trash.web.internal.display.context.TrashDisplayContext;

import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	property = {
		"javax.portlet.name=" + TrashPortletKeys.TRASH, "path=/view_content.jsp"
	},
	service = PortletConfigurationIcon.class
)
public class RestoreTrashPortletConfigurationIcon
	extends BaseJSPPortletConfigurationIcon {

	@Override
	public Map<String, Object> getContext(PortletRequest portletRequest) {
		return HashMapBuilder.<String, Object>put(
			"action", getNamespace(portletRequest) + "restoreTrash"
		).put(
			"globalAction", true
		).put(
			"restoreTrashURL", _getRestoreTrashURL(portletRequest)
		).build();
	}

	@Override
	public String getIconCssClass() {
		return "restore";
	}

	@Override
	public String getJspPath() {
		return "/configuration/icon/restore_trash.jsp";
	}

	@Override
	public String getMessage(PortletRequest portletRequest) {
		return _language.get(getLocale(portletRequest), "restore");
	}

	@Override
	public double getWeight() {
		return 100.0;
	}

	@Override
	public boolean isShow(PortletRequest portletRequest) {
		TrashDisplayContext trashDisplayContext = new TrashDisplayContext(
			_portal.getHttpServletRequest(portletRequest), null, null);

		TrashHandler trashHandler = trashDisplayContext.getTrashHandler();

		if ((trashHandler == null) || trashHandler.isContainerModel()) {
			return false;
		}

		TrashEntry trashEntry = trashDisplayContext.getTrashEntry();

		if (trashEntry != null) {
			TrashedModel trashedModel = trashHandler.getTrashedModel(
				trashEntry.getClassPK());

			try {
				if (!trashHandler.isMovable(trashEntry.getClassPK()) ||
					!trashHandler.isRestorable(trashEntry.getClassPK()) ||
					!_trashHelper.isInTrashContainer(trashedModel)) {

					return false;
				}
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}

				return false;
			}
		}

		return true;
	}

	@Override
	protected ServletContext getServletContext() {
		return _servletContext;
	}

	private String _getRestoreTrashURL(PortletRequest portletRequest) {
		try {
			PortletResponse portletResponse =
				(PortletResponse)portletRequest.getAttribute(
					JavaConstants.JAVAX_PORTLET_RESPONSE);

			TrashDisplayContext trashDisplayContext = new TrashDisplayContext(
				_portal.getHttpServletRequest(portletRequest),
				_portal.getLiferayPortletRequest(portletRequest),
				_portal.getLiferayPortletResponse(portletResponse));

			TrashHandler trashHandler = trashDisplayContext.getTrashHandler();

			return PortletURLBuilder.create(
				_portal.getControlPanelPortletURL(
					portletRequest, TrashPortletKeys.TRASH,
					PortletRequest.RENDER_PHASE)
			).setMVCPath(
				"/view_container_model.jsp"
			).setRedirect(
				trashDisplayContext.getViewContentRedirectURL()
			).setParameter(
				"classNameId", trashDisplayContext.getClassNameId()
			).setParameter(
				"classPK", trashDisplayContext.getClassPK()
			).setParameter(
				"containerModelClassNameId",
				_portal.getClassNameId(
					trashHandler.getContainerModelClassName(
						trashDisplayContext.getClassPK()))
			).setWindowState(
				LiferayWindowState.POP_UP
			).buildString();
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RestoreTrashPortletConfigurationIcon.class);

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference(target = "(osgi.web.symbolicname=com.liferay.trash.web)")
	private ServletContext _servletContext;

	@Reference
	private TrashHelper _trashHelper;

}