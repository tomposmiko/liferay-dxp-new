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

import updateItemConfig from '../../actions/updateItemConfig';
import LayoutService from '../../services/LayoutService';
import {setIn} from '../../utils/setIn';

function undoAction({action, store}) {
	const {config, itemId, pageContents} = action;
	const {layoutData} = store;

	const nextLayoutData = setIn(
		layoutData,
		['items', itemId, 'config'],
		config
	);

	return (dispatch) => {
		return LayoutService.updateLayoutData({
			layoutData: nextLayoutData,
			onNetworkStatus: dispatch,
			segmentsExperienceId: store.segmentsExperienceId,
		}).then(() => {
			dispatch(
				updateItemConfig({
					itemId,
					layoutData: nextLayoutData,
					overridePreviousConfig: true,
					pageContents,
				})
			);
		});
	};
}

function getDerivedStateForUndo({action, state}) {
	const {itemId, pageContents} = action;
	const {layoutData} = state;

	const item = layoutData.items[itemId];

	return {
		config: item.config,
		itemId,
		pageContents,
	};
}

export {undoAction, getDerivedStateForUndo};
