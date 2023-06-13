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

package com.liferay.commerce.bom.admin.web.internal.servlet.taglib.ui;

import com.liferay.commerce.bom.admin.web.internal.constants.CommerceBOMFolderScreenNavigationConstants;
import com.liferay.commerce.bom.model.CommerceBOMFolder;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationCategory;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;

import java.io.IOException;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false,
	property = {
		"screen.navigation.category.order:Integer=20",
		"screen.navigation.entry.order:Integer=10"
	},
	service = {ScreenNavigationCategory.class, ScreenNavigationEntry.class}
)
public class CommerceBOMFolderApplicationModelsScreenNavigationCategory
	implements ScreenNavigationCategory,
			   ScreenNavigationEntry<CommerceBOMFolder> {

	@Override
	public String getCategoryKey() {
		return CommerceBOMFolderScreenNavigationConstants.
			CATEGORY_KEY_APPLICATION_MODELS;
	}

	@Override
	public String getEntryKey() {
		return CommerceBOMFolderScreenNavigationConstants.
			ENTRY_KEY_APPLICATION_MODELS;
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(locale, "models");
	}

	@Override
	public String getScreenNavigationKey() {
		return CommerceBOMFolderScreenNavigationConstants.SCREEN_NAVIGATION_KEY;
	}

	@Override
	public boolean isVisible(User user, CommerceBOMFolder commerceBOMFolder) {
		if (commerceBOMFolder == null) {
			return false;
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		try {
			return _commerceBOMFolderModelResourcePermission.contains(
				permissionChecker, commerceBOMFolder, ActionKeys.UPDATE);
		}
		catch (PortalException portalException) {
			_log.error(portalException, portalException);
		}

		return false;
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		_jspRenderer.renderJSP(
			httpServletRequest, httpServletResponse, "/folder/models.jsp");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceBOMFolderApplicationModelsScreenNavigationCategory.class);

	@Reference(
		target = "(model.class.name=com.liferay.commerce.bom.model.CommerceBOMFolder)"
	)
	private ModelResourcePermission<CommerceBOMFolder>
		_commerceBOMFolderModelResourcePermission;

	@Reference
	private JSPRenderer _jspRenderer;

}