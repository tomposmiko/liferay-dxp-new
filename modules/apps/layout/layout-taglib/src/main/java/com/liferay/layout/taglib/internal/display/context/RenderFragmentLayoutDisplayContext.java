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

package com.liferay.layout.taglib.internal.display.context;

import com.liferay.asset.display.page.constants.AssetDisplayPageWebKeys;
import com.liferay.info.constants.InfoDisplayWebKeys;
import com.liferay.info.display.contributor.InfoDisplayContributor;
import com.liferay.info.display.contributor.InfoDisplayContributorTracker;
import com.liferay.info.display.contributor.InfoDisplayObjectProvider;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rubén Pulido
 */
public class RenderFragmentLayoutDisplayContext {

	public RenderFragmentLayoutDisplayContext(
		HttpServletRequest httpServletRequest) {

		_httpServletRequest = httpServletRequest;

		_infoDisplayContributorTracker =
			(InfoDisplayContributorTracker)httpServletRequest.getAttribute(
				InfoDisplayWebKeys.INFO_DISPLAY_CONTRIBUTOR_TRACKER);
	}

	public String getBackgroundImage(JSONObject rowConfigJSONObject)
		throws PortalException {

		if (rowConfigJSONObject == null) {
			return StringPool.BLANK;
		}

		JSONObject backgroundImageJSONObject =
			rowConfigJSONObject.getJSONObject("backgroundImage");

		if (backgroundImageJSONObject == null) {
			return rowConfigJSONObject.getString(
				"backgroundImage", StringPool.BLANK);
		}

		String mappedField = backgroundImageJSONObject.getString("mappedField");

		if (Validator.isNotNull(mappedField)) {
			InfoDisplayObjectProvider infoDisplayObjectProvider =
				(InfoDisplayObjectProvider)_httpServletRequest.getAttribute(
					AssetDisplayPageWebKeys.INFO_DISPLAY_OBJECT_PROVIDER);

			if ((_infoDisplayContributorTracker != null) &&
				(infoDisplayObjectProvider != null)) {

				InfoDisplayContributor infoDisplayContributor =
					_infoDisplayContributorTracker.getInfoDisplayContributor(
						PortalUtil.getClassName(
							infoDisplayObjectProvider.getClassNameId()));

				if (infoDisplayContributor != null) {
					Object object =
						infoDisplayContributor.getInfoDisplayFieldValue(
							infoDisplayObjectProvider.getDisplayObject(),
							mappedField, LocaleUtil.getDefault());

					if (object instanceof JSONObject) {
						JSONObject fieldValueJSONObject = (JSONObject)object;

						return fieldValueJSONObject.getString(
							"url", StringPool.BLANK);
					}
				}
			}
		}

		String fieldId = backgroundImageJSONObject.getString("fieldId");

		if (Validator.isNotNull(fieldId)) {
			long classNameId = backgroundImageJSONObject.getLong("classNameId");
			long classPK = backgroundImageJSONObject.getLong("classPK");

			if ((classNameId != 0L) && (classPK != 0L)) {
				InfoDisplayContributor infoDisplayContributor =
					_infoDisplayContributorTracker.getInfoDisplayContributor(
						PortalUtil.getClassName(classNameId));

				if (infoDisplayContributor != null) {
					InfoDisplayObjectProvider infoDisplayObjectProvider =
						infoDisplayContributor.getInfoDisplayObjectProvider(
							classPK);

					if (infoDisplayObjectProvider != null) {
						Object object =
							infoDisplayContributor.getInfoDisplayFieldValue(
								infoDisplayObjectProvider.getDisplayObject(),
								fieldId, LocaleUtil.getDefault());

						if (object instanceof JSONObject) {
							JSONObject fieldValueJSONObject =
								(JSONObject)object;

							return fieldValueJSONObject.getString(
								"url", StringPool.BLANK);
						}
					}
				}
			}
		}

		String backgroundImageURL = backgroundImageJSONObject.getString("url");

		if (Validator.isNotNull(backgroundImageURL)) {
			return backgroundImageURL;
		}

		return StringPool.BLANK;
	}

	private final HttpServletRequest _httpServletRequest;
	private final InfoDisplayContributorTracker _infoDisplayContributorTracker;

}