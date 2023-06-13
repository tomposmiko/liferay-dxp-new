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

package com.liferay.portlet;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayEventResponse;
import com.liferay.portlet.internal.EventRequestImpl;
import com.liferay.portlet.internal.EventResponseImpl;

import javax.portlet.EventRequest;
import javax.portlet.PortletModeException;
import javax.portlet.WindowStateException;
import javax.portlet.filter.EventRequestWrapper;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 * @author Neil Griffin
 */
@ProviderType
public class EventResponseFactory {

	public static LiferayEventResponse create(
			EventRequest eventRequest, HttpServletResponse response, User user,
			Layout layout)
		throws PortletModeException, WindowStateException {

		while (eventRequest instanceof EventRequestWrapper) {
			eventRequest = ((EventRequestWrapper)eventRequest).getRequest();
		}

		EventResponseImpl eventResponseImpl = new EventResponseImpl();

		eventResponseImpl.init(
			(EventRequestImpl)eventRequest, response, user, layout);

		return eventResponseImpl;
	}

}