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

package com.liferay.change.tracking.web.internal.portlet.action;

import com.liferay.change.tracking.constants.CTConstants;
import com.liferay.change.tracking.web.internal.constants.CTPortletKeys;
import com.liferay.change.tracking.web.internal.display.BasePersistenceRegistry;
import com.liferay.change.tracking.web.internal.display.CTDisplayRendererRegistry;
import com.liferay.portal.change.tracking.sql.CTSQLModeThreadLocal;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Samuel Trong Tran
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CTPortletKeys.PUBLICATIONS,
		"mvc.command.name=/change_tracking/render_ct_entry"
	},
	service = MVCResourceCommand.class
)
public class RenderCTEntryMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		_renderCTEntry(resourceRequest, resourceResponse);
	}

	private <T extends BaseModel<T>> void _renderCTEntry(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		long modelClassNameId = ParamUtil.getLong(
			resourceRequest, "modelClassNameId");
		long modelClassPK = ParamUtil.getLong(resourceRequest, "modelClassPK");

		T model = _ctDisplayRendererRegistry.fetchCTModel(
			modelClassNameId, modelClassPK);

		if (model == null) {
			model = _basePersistenceRegistry.fetchBaseModel(
				modelClassNameId, modelClassPK);
		}

		_ctDisplayRendererRegistry.renderCTEntry(
			_portal.getHttpServletRequest(resourceRequest),
			_portal.getHttpServletResponse(resourceResponse),
			CTConstants.CT_COLLECTION_ID_PRODUCTION,
			CTSQLModeThreadLocal.CTSQLMode.DEFAULT, 0, model, modelClassNameId,
			null);
	}

	@Reference
	private BasePersistenceRegistry _basePersistenceRegistry;

	@Reference
	private CTDisplayRendererRegistry _ctDisplayRendererRegistry;

	@Reference
	private Portal _portal;

}