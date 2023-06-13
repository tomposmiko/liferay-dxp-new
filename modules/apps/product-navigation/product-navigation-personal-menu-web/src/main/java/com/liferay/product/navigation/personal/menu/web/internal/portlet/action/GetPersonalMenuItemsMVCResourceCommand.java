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

package com.liferay.product.navigation.personal.menu.web.internal.portlet.action;

import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceComparator;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.product.navigation.personal.menu.PersonalMenuEntry;
import com.liferay.product.navigation.personal.menu.constants.PersonalMenuPortletKeys;
import com.liferay.product.navigation.personal.menu.util.PersonalApplicationURLUtil;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeSet;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pei-Jung Lan
 */
@Component(
	property = {
		"javax.portlet.name=" + PersonalMenuPortletKeys.PERSONAL_MENU,
		"mvc.command.name=/product_navigation_personal_menu/get_personal_menu_items"
	},
	service = MVCResourceCommand.class
)
public class GetPersonalMenuItemsMVCResourceCommand
	extends BaseMVCResourceCommand {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openMultiValueMap(
			bundleContext, PersonalMenuEntry.class,
			"(product.navigation.personal.menu.group=*)",
			(serviceReference, emitter) -> emitter.emit(
				String.valueOf(
					serviceReference.getProperty(
						"product.navigation.personal.menu.group"))),
			Collections.reverseOrder(
				new PropertyServiceReferenceComparator<>(
					"product.navigation.personal.menu.entry.order")));
	}

	@Override
	protected void doServeResource(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		try {
			HttpServletResponse httpServletResponse =
				_portal.getHttpServletResponse(resourceResponse);

			httpServletResponse.setContentType(ContentTypes.APPLICATION_JSON);

			JSONArray jsonArray = _getPersonalMenuItemsJSONArray(
				resourceRequest);

			ServletResponseUtil.write(
				httpServletResponse, jsonArray.toString());
		}
		catch (Exception exception) {
			_log.error(exception);
		}
	}

	private JSONArray _getImpersonationItemsJSONArray(
			PortletRequest portletRequest, ThemeDisplay themeDisplay)
		throws PortalException {

		User realUser = themeDisplay.getRealUser();
		User user = themeDisplay.getUser();

		String realUserURL = HttpComponentsUtil.removeParameter(
			ParamUtil.getString(portletRequest, "currentURL"), "doAsUserId");

		String userProfileURL = HttpComponentsUtil.getPath(
			user.getDisplayURL(themeDisplay, false));

		if (realUserURL.startsWith(userProfileURL)) {
			realUserURL = StringUtil.replace(
				realUserURL, userProfileURL,
				HttpComponentsUtil.getPath(
					realUser.getDisplayURL(themeDisplay, false)));

			PersonalApplicationURLUtil.
				getOrAddEmbeddedPersonalApplicationLayout(
					realUser, realUser.getGroup(), false);
		}

		String userDashboardURL = HttpComponentsUtil.getPath(
			user.getDisplayURL(themeDisplay, true));

		if (realUserURL.startsWith(userDashboardURL)) {
			realUserURL = StringUtil.replace(
				realUserURL, userDashboardURL,
				HttpComponentsUtil.getPath(
					realUser.getDisplayURL(themeDisplay, true)));

			PersonalApplicationURLUtil.
				getOrAddEmbeddedPersonalApplicationLayout(
					realUser, realUser.getGroup(), true);
		}

		JSONArray jsonArray = JSONUtil.put(
			JSONUtil.put(
				"href", realUserURL
			).put(
				"label",
				_language.get(themeDisplay.getLocale(), "be-yourself-again")
			).put(
				"symbolRight", "change"
			));

		Locale realUserLocale = realUser.getLocale();
		Locale userLocale = user.getLocale();

		if (!realUserLocale.equals(userLocale)) {
			String changeLanguageLabel = null;
			String doAsUserLanguageId = null;

			Locale locale = themeDisplay.getLocale();

			if (Objects.equals(
					locale.getLanguage(), realUserLocale.getLanguage()) &&
				Objects.equals(
					locale.getCountry(), realUserLocale.getCountry())) {

				changeLanguageLabel = _language.format(
					realUserLocale, "use-x's-preferred-language-(x)",
					new String[] {
						_html.escape(user.getFullName()),
						userLocale.getDisplayLanguage(realUserLocale)
					},
					false);

				doAsUserLanguageId =
					userLocale.getLanguage() + "_" + userLocale.getCountry();
			}
			else {
				changeLanguageLabel = _language.format(
					realUserLocale, "use-your-preferred-language-(x)",
					realUserLocale.getDisplayLanguage(realUserLocale), false);

				doAsUserLanguageId = StringUtil.add(
					realUserLocale.getLanguage(), realUserLocale.getCountry(),
					StringPool.UNDERLINE);
			}

			jsonArray.put(
				JSONUtil.put(
					"href",
					HttpComponentsUtil.setParameter(
						ParamUtil.getString(portletRequest, "currentURL"),
						"doAsUserLanguageId", doAsUserLanguageId)
				).put(
					"label", changeLanguageLabel
				).put(
					"symbolRight", "globe"
				));
		}

		return jsonArray;
	}

	private JSONArray _getPersonalMenuEntriesJSONArray(
			PortletRequest portletRequest,
			List<PersonalMenuEntry> personalMenuEntries)
		throws PortalException {

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		for (PersonalMenuEntry personalMenuEntry : personalMenuEntries) {
			if (!personalMenuEntry.isShow(
					portletRequest, themeDisplay.getPermissionChecker())) {

				continue;
			}

			JSONObject jsonObject = JSONUtil.put(
				"active",
				personalMenuEntry.isActive(
					portletRequest,
					ParamUtil.getString(portletRequest, "portletId")));

			try {
				String href = personalMenuEntry.getPortletURL(
					_portal.getHttpServletRequest(portletRequest));

				if (href != null) {
					jsonObject.put("href", href);
				}
				else {
					jsonObject.put(
						"jsOnClickConfig",
						personalMenuEntry.getJSOnClickConfigJSONObject(
							_portal.getHttpServletRequest(portletRequest)));
				}
			}
			catch (PortalException portalException) {
				_log.error(portalException);
			}

			jsonObject.put(
				"label", personalMenuEntry.getLabel(themeDisplay.getLocale())
			).put(
				"symbolRight", personalMenuEntry.getIcon(portletRequest)
			);

			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

	private JSONArray _getPersonalMenuItemsJSONArray(
			PortletRequest portletRequest)
		throws PortalException {

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay.isImpersonated()) {
			jsonArray.put(
				JSONUtil.put(
					"items",
					_getImpersonationItemsJSONArray(
						portletRequest, themeDisplay)
				).put(
					"label",
					() -> {
						User user = themeDisplay.getUser();

						return StringUtil.appendParentheticalSuffix(
							user.getFullName(),
							_language.get(
								themeDisplay.getLocale(), "impersonated"));
					}
				).put(
					"type", "group"
				));
		}

		JSONObject dividerJSONObject = JSONUtil.put("type", "divider");

		for (String personalMenuGroup :
				new TreeSet<>(_serviceTrackerMap.keySet())) {

			JSONArray personalMenuEntriesJSONArray =
				_getPersonalMenuEntriesJSONArray(
					portletRequest,
					_serviceTrackerMap.getService(personalMenuGroup));

			if (personalMenuEntriesJSONArray.length() == 0) {
				continue;
			}

			if (jsonArray.length() > 0) {
				jsonArray.put(dividerJSONObject);
			}

			jsonArray.put(
				JSONUtil.put(
					"items", personalMenuEntriesJSONArray
				).put(
					"type", "group"
				));
		}

		if ((jsonArray.length() > 0) && !themeDisplay.isImpersonated()) {
			User user = themeDisplay.getUser();

			JSONObject jsonObject = (JSONObject)jsonArray.get(0);

			jsonObject.put("label", user.getFullName());
		}

		return jsonArray;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		GetPersonalMenuItemsMVCResourceCommand.class);

	@Reference
	private Html _html;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	private ServiceTrackerMap<String, List<PersonalMenuEntry>>
		_serviceTrackerMap;

}