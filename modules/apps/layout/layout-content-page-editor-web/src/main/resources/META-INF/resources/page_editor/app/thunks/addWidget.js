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

import addFragmentEntryLink from '../actions/addFragmentEntryLink';
import WidgetService from '../services/WidgetService';

export default function addWidget({
	config,
	parentItemId,
	portletId,
	position,
	store
}) {
	return dispatch => {
		const {segmentsExperienceId} = store;

		WidgetService.addPortlet({
			config,
			onNetworkStatus: dispatch,
			parentItemId,
			portletId,
			position,
			segmentsExperienceId
		}).then(({fragmentEntryLink, layoutData}) => {
			// TODO: This is a temporary "hack"
			//       until the backend is consitent
			//       between both "metal+soy" and "react" versions
			fragmentEntryLink.content = {
				value: {
					content: fragmentEntryLink.content
				}
			};

			dispatch(
				addFragmentEntryLink({
					fragmentEntryLink,
					layoutData
				})
			);
		});
	};
}
