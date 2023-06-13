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

import serviceFetch from './serviceFetch';

export default {
	/**
	 * Adds a Widget to the current layout
	 * @param {object} options
	 * @param {object} options.config Application config
	 * @param {string} options.portletId Portlet id of the Widget
	 * @param {string} options.parentItemId id of the parent where the portlet is going to be added
	 * @param {string} options.position position where the portlet is going to be added
	 * @param {string} options.portletId Portlet id of the Widget
	 * @param {string} options.segmentsExperienceId Current segmentsExperienceId
	 * @return {Promise<object>}
	 */
	addPortlet({
		config,
		onNetworkStatus,
		parentItemId,
		portletId,
		position,
		segmentsExperienceId
	}) {
		const {addPortletURL, classNameId, classPK} = config;

		return serviceFetch(
			config,
			addPortletURL,
			{
				body: {
					classNameId,
					classPK,
					parentItemId,
					portletId,
					position,
					segmentsExperienceId
				}
			},
			onNetworkStatus
		);
	}
};
