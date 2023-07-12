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

import deleteItemAction from '../actions/deleteItem';
import deleteWidgets from '../actions/deleteWidgets';
import updatePageContents from '../actions/updatePageContents';
import {LAYOUT_DATA_ITEM_TYPES} from '../config/constants/layoutDataItemTypes';
import InfoItemService from '../services/InfoItemService';
import LayoutService from '../services/LayoutService';

export default function deleteItem({itemId, selectItem = () => {}, store}) {
	return (dispatch) => {
		const {fragmentEntryLinks, layoutData, segmentsExperienceId} = store;

		return markItemForDeletion({
			fragmentEntryLinks,
			itemId,
			layoutData,
			onNetworkStatus: dispatch,
			segmentsExperienceId,
		})
			.then(
				({
					deletedFragmentEntryLinkIds = [],
					portletIds = [],
					layoutData,
				}) => {
					const deletedWidgets = deletedFragmentEntryLinkIds
						.map(
							(fragmentEntryLinkId) =>
								store.fragmentEntryLinks[fragmentEntryLinkId]
						)
						.filter(
							(fragmentEntryLink) =>
								fragmentEntryLink.editableValues.portletId
						);

					if (deletedWidgets.length) {
						dispatch(deleteWidgets(deletedWidgets));
					}

					selectItem(null);

					dispatch(
						deleteItemAction({
							itemId,
							layoutData,
							portletIds,
						})
					);
				}
			)
			.then(() => {
				InfoItemService.getPageContents({
					onNetworkStatus: dispatch,
				}).then((pageContents) => {
					dispatch(
						updatePageContents({
							pageContents,
						})
					);
				});
			});
	};
}

function markItemForDeletion({
	fragmentEntryLinks,
	itemId,
	layoutData,
	onNetworkStatus: dispatch,
	segmentsExperienceId,
}) {
	const portletIds = findPortletIds(itemId, layoutData, fragmentEntryLinks);

	return LayoutService.markItemForDeletion({
		itemId,
		onNetworkStatus: dispatch,
		portletIds,
		segmentsExperienceId,
	}).then((response) => {
		return {...response, portletIds};
	});
}

function findPortletIds(itemId, layoutData, fragmentEntryLinks) {
	const item = layoutData.items[itemId];

	const {config = {}, children = []} = item;

	if (
		item.type === LAYOUT_DATA_ITEM_TYPES.fragment &&
		config.fragmentEntryLinkId
	) {
		const {editableValues = {}} = fragmentEntryLinks[
			config.fragmentEntryLinkId
		];

		if (editableValues.portletId && !editableValues.instanceId) {
			return [editableValues.portletId];
		}
	}

	const deletedWidgets = [];

	children.forEach((itemId) => {
		deletedWidgets.push(
			...findPortletIds(itemId, layoutData, fragmentEntryLinks)
		);
	});

	return deletedWidgets;
}
