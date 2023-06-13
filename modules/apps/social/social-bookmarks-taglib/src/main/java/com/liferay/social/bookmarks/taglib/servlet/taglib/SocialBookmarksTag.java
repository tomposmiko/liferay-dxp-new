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

package com.liferay.social.bookmarks.taglib.servlet.taglib;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.social.bookmarks.taglib.internal.servlet.ServletContextUtil;
import com.liferay.social.bookmarks.taglib.internal.util.SocialBookmarksRegistryUtil;
import com.liferay.taglib.util.IncludeTag;

import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * @author Brian Wing Shun Chan
 * @author Jorge Ferrer
 */
public class SocialBookmarksTag extends IncludeTag {

	@Override
	public int doEndTag() throws JspException {
		if ((_types != null) && (_types.length == 0)) {
			return EVAL_PAGE;
		}

		return super.doEndTag();
	}

	@Override
	public int doStartTag() throws JspException {
		if ((_types != null) && (_types.length == 0)) {
			return SKIP_BODY;
		}

		return super.doStartTag();
	}

	public void setClassName(String className) {
		_className = className;
	}

	public void setClassPK(long classPK) {
		_classPK = classPK;
	}

	public void setDisplayStyle(String displayStyle) {
		if (Validator.isNull(displayStyle)) {
			_displayStyle = "inline";
		}
		else {
			_displayStyle = displayStyle;
		}
	}

	public void setMaxInlineItems(int maxInlineItems) {
		_maxInlineItems = maxInlineItems;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		servletContext = ServletContextUtil.getServletContext();
	}

	public void setTarget(String target) {
		_target = target;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setTypes(String types) {
		if (types != null) {
			_types = StringUtil.split(types);
		}
	}

	public void setUrl(String url) {
		_url = url;
	}

	public void setUrlImpl(PortletURL urlImpl) {
		_urlImpl = urlImpl;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_className = null;
		_classPK = 0;
		_displayStyle = null;
		_maxInlineItems = 3;
		_target = null;
		_title = null;
		_types = null;
		_url = null;
		_urlImpl = null;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-social-bookmarks:bookmarks:className", _className);
		request.setAttribute(
			"liferay-social-bookmarks:bookmarks:classPK", _classPK);
		request.setAttribute(
			"liferay-social-bookmarks:bookmarks:displayStyle", _displayStyle);
		request.setAttribute(
			"liferay-social-bookmarks:bookmarks:maxInlineItems",
			_maxInlineItems);
		request.setAttribute(
			"liferay-social-bookmarks:bookmarks:target", _target);
		request.setAttribute(
			"liferay-social-bookmarks:bookmarks:title", _title);

		String[] types = _types;

		if (types == null) {
			List<String> allTypes =
				SocialBookmarksRegistryUtil.getSocialBookmarksTypes();

			types = allTypes.toArray(new String[0]);
		}

		request.setAttribute("liferay-social-bookmarks:bookmarks:types", types);

		if ((_url == null) && (_urlImpl != null)) {
			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			try {
				_url = PortalUtil.getCanonicalURL(
					_urlImpl.toString(), themeDisplay,
					themeDisplay.getLayout());
			}
			catch (PortalException pe) {
				_log.error("Unable to get canonical URL " + _urlImpl, pe);
			}
		}

		if (_url == null) {
			throw new IllegalArgumentException();
		}

		request.setAttribute("liferay-social-bookmarks:bookmarks:url", _url);
	}

	private static final String _PAGE = "/bookmarks/page.jsp";

	private static final Log _log = LogFactoryUtil.getLog(
		SocialBookmarksTag.class);

	private String _className;
	private long _classPK;
	private String _displayStyle;
	private int _maxInlineItems = 3;
	private String _target;
	private String _title;
	private String[] _types;
	private String _url;
	private PortletURL _urlImpl;

}