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

package com.liferay.data.engine.taglib.servlet.taglib;

import com.liferay.data.engine.renderer.DataLayoutRendererContext;
import com.liferay.data.engine.taglib.servlet.taglib.base.BaseDataLayoutRendererTag;
import com.liferay.data.engine.taglib.servlet.taglib.util.DataLayoutTaglibUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.portlet.RenderResponse;

import javax.servlet.jsp.JspException;

/**
 * @author Jeyvison Nascimento
 */
public class DataLayoutRendererTag extends BaseDataLayoutRendererTag {

	@Override
	public int doStartTag() throws JspException {
		int result = super.doStartTag();

		setNamespacedAttribute(request, "content", _getContent());

		return result;
	}

	private String _getContent() {
		String content = StringPool.BLANK;

		try {
			DataLayoutRendererContext dataLayoutRendererContext =
				new DataLayoutRendererContext();

			dataLayoutRendererContext.setContainerId(getContainerId());

			if (getDataRecordId() != null) {
				dataLayoutRendererContext.setDataRecordValues(
					DataLayoutTaglibUtil.getDataRecordValues(
						getDataRecordId(), request));
			}
			else {
				dataLayoutRendererContext.setDataRecordValues(
					getDataRecordValues());
			}

			dataLayoutRendererContext.setHttpServletRequest(request);
			dataLayoutRendererContext.setHttpServletResponse(
				PortalUtil.getHttpServletResponse(
					(RenderResponse)request.getAttribute(
						JavaConstants.JAVAX_PORTLET_RESPONSE)));
			dataLayoutRendererContext.setPortletNamespace(getNamespace());

			content = DataLayoutTaglibUtil.renderDataLayout(
				getDataLayoutId(), dataLayoutRendererContext);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}
		}

		return content;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DataLayoutRendererTag.class);

}