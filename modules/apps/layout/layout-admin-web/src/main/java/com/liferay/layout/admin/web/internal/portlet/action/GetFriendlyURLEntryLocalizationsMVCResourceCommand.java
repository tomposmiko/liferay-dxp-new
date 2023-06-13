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

package com.liferay.layout.admin.web.internal.portlet.action;

import com.liferay.friendly.url.model.FriendlyURLEntryLocalization;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalService;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.admin.web.internal.util.comparator.FriendlyURLEntryLocalizationComparator;
import com.liferay.layout.friendly.url.LayoutFriendlyURLEntryHelper;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONSerializable;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + LayoutAdminPortletKeys.GROUP_PAGES,
		"mvc.command.name=/layout_admin/get_friendly_url_entry_localizations"
	},
	service = MVCResourceCommand.class
)
public class GetFriendlyURLEntryLocalizationsMVCResourceCommand
	extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (!themeDisplay.isSignedIn()) {
			throw new PrincipalException.MustBeAuthenticated(
				themeDisplay.getUserId());
		}

		JSONPortletResponseUtil.writeJSON(
			resourceRequest, resourceResponse,
			_getFriendlyURLEntryLocalizationsJSONObject(resourceRequest));
	}

	private JSONObject _getFriendlyURLEntryLocalizationsJSONObject(
			ResourceRequest resourceRequest)
		throws Exception {

		Layout layout = _layoutLocalService.getLayout(
			ParamUtil.getLong(resourceRequest, "plid"));

		JSONObject friendlyURLEntryLocalizationsJSONObject =
			JSONFactoryUtil.createJSONObject();

		for (String languageId : layout.getAvailableLanguageIds()) {
			List<FriendlyURLEntryLocalization> friendlyURLEntryLocalizations =
				_friendlyURLEntryLocalService.getFriendlyURLEntryLocalizations(
					layout.getGroupId(),
					_layoutFriendlyURLEntryHelper.getClassNameId(
						layout.isPrivateLayout()),
					layout.getPlid(), languageId, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, _friendlyURLEntryLocalizationComparator);

			String mainUrlTitle = layout.getFriendlyURL(
				LocaleUtil.fromLanguageId(languageId));

			friendlyURLEntryLocalizationsJSONObject.put(
				languageId,
				JSONUtil.put(
					"current",
					JSONUtil.put("urlTitle", _http.decodeURL(mainUrlTitle))
				).put(
					"history",
					_getJSONArray(
						ListUtil.filter(
							friendlyURLEntryLocalizations,
							friendlyURLEntryLocalization -> !Objects.equals(
								friendlyURLEntryLocalization.getUrlTitle(),
								mainUrlTitle)),
						this::_serializeFriendlyURLEntryLocalization)
				));
		}

		return friendlyURLEntryLocalizationsJSONObject;
	}

	private <T> JSONArray _getJSONArray(
		List<T> list, Function<T, JSONSerializable> serialize) {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		list.forEach(t -> jsonArray.put(serialize.apply(t)));

		return jsonArray;
	}

	private JSONObject _serializeFriendlyURLEntryLocalization(
		FriendlyURLEntryLocalization friendlyEntryLocalization) {

		if (friendlyEntryLocalization == null) {
			return null;
		}

		return JSONUtil.put(
			"friendlyURLEntryId",
			friendlyEntryLocalization.getFriendlyURLEntryId()
		).put(
			"friendlyURLEntryLocalizationId",
			Long.valueOf(
				friendlyEntryLocalization.getFriendlyURLEntryLocalizationId())
		).put(
			"urlTitle", _http.decodeURL(friendlyEntryLocalization.getUrlTitle())
		);
	}

	private final FriendlyURLEntryLocalizationComparator
		_friendlyURLEntryLocalizationComparator =
			new FriendlyURLEntryLocalizationComparator();

	@Reference
	private FriendlyURLEntryLocalService _friendlyURLEntryLocalService;

	@Reference
	private Http _http;

	@Reference
	private LayoutFriendlyURLEntryHelper _layoutFriendlyURLEntryHelper;

	@Reference
	private LayoutLocalService _layoutLocalService;

}