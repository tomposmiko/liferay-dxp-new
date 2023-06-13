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

package com.liferay.taglib.ui;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageConstants;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ServiceProxyFactory;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.util.IncludeTag;
import com.liferay.taglib.util.LexiconUtil;
import com.liferay.taglib.util.TagResourceBundleUtil;
import com.liferay.users.admin.kernel.file.uploads.UserFileUploadsSettings;

import java.util.ResourceBundle;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

/**
 * @author Eudaldo Alonso
 */
public class UserPortraitTag extends IncludeTag {

	public static String getUserPortraitHTML(
		User user, String cssClass, Supplier<String> userInitialsSupplier,
		Supplier<String> userPortraitURLSupplier) {

		StringBundler sb = new StringBundler(7);

		sb.append("<div class=\"");

		boolean imageDefaultUseInitials =
			_userFileUploadsSettings.isImageDefaultUseInitials();
		long userPortraitId = 0;

		if (user != null) {
			userPortraitId = user.getPortraitId();

			if (LanguageConstants.VALUE_IMAGE.equals(
					LanguageUtil.get(
						user.getLocale(),
						LanguageConstants.KEY_USER_DEFAULT_PORTRAIT,
						LanguageConstants.VALUE_INITIALS))) {

				imageDefaultUseInitials = false;
			}
		}

		if (imageDefaultUseInitials && (userPortraitId == 0)) {
			sb.append(LexiconUtil.getUserColorCssClass(user));
			sb.append(" ");
			sb.append(cssClass);
			sb.append(" user-icon user-icon-default\"><span>");
			sb.append(userInitialsSupplier.get());
			sb.append("</span></div>");
		}
		else {
			sb.append(cssClass);
			sb.append(" aspect-ratio-bg-cover user-icon\" ");
			sb.append("style=\"background-image:url(");
			sb.append(HtmlUtil.escape(userPortraitURLSupplier.get()));
			sb.append(")\"></div>");
		}

		return sb.toString();
	}

	@Override
	public int processEndTag() throws Exception {
		JspWriter jspWriter = pageContext.getOut();

		User user = getUser();

		String userPortraitHTML = getUserPortraitHTML(
			user, _cssClass, () -> getUserInitials(user),
			() -> getPortraitURL(user));

		jspWriter.write(userPortraitHTML);

		return EVAL_PAGE;
	}

	public void setCssClass(String cssClass) {
		_cssClass = cssClass;
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setImageCssClass(String imageCssClass) {
	}

	public void setUser(User user) {
		_user = user;
	}

	public void setUserId(long userId) {
		_user = UserLocalServiceUtil.fetchUser(userId);
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_cssClass = StringPool.BLANK;
		_user = null;
		_userName = StringPool.BLANK;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	protected String getPortraitURL(User user) {
		String portraitURL = null;

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (user != null) {
			try {
				portraitURL = user.getPortraitURL(themeDisplay);
			}
			catch (PortalException pe) {
				_log.error(pe, pe);
			}
		}
		else {
			portraitURL = UserConstants.getPortraitURL(
				themeDisplay.getPathImage(), true, 0, StringPool.BLANK);
		}

		return portraitURL;
	}

	protected User getUser() {
		return _user;
	}

	protected String getUserInitials(User user) {
		if (user != null) {
			return user.getInitials();
		}

		String userName = _userName;

		if (Validator.isNull(userName)) {
			ResourceBundle resourceBundle =
				TagResourceBundleUtil.getResourceBundle(pageContext);

			userName = LanguageUtil.get(resourceBundle, "user");
		}

		String[] userNames = StringUtil.split(userName, CharPool.SPACE);

		StringBuilder sb = new StringBuilder(2);

		for (int i = 0; (i < userNames.length) && (i < 2); i++) {
			if (!userNames[i].isEmpty()) {
				int codePoint = Character.toUpperCase(
					userNames[i].codePointAt(0));

				sb.append(Character.toChars(codePoint));
			}
		}

		return sb.toString();
	}

	@Override
	protected boolean isCleanUpSetAttributes() {
		return false;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
	}

	private static final String _PAGE =
		"/html/taglib/ui/user_portrait/page.jsp";

	private static final Log _log = LogFactoryUtil.getLog(
		UserPortraitTag.class);

	private static volatile UserFileUploadsSettings _userFileUploadsSettings =
		ServiceProxyFactory.newServiceTrackedInstance(
			UserFileUploadsSettings.class, UserPortraitTag.class,
			"_userFileUploadsSettings", false);

	private String _cssClass = StringPool.BLANK;
	private User _user;
	private String _userName = StringPool.BLANK;

}