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

import {ClayButtonWithIcon} from '@clayui/button';
import React, {useMemo, useState} from 'react';

import {ReorderSetsModal} from '../../../app/components/ReorderSetsModal';
import {FRAGMENTS_DISPLAY_STYLES} from '../../../app/config/constants/fragmentsDisplayStyles';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../app/config/constants/layoutDataItemTypes';
import {config} from '../../../app/config/index';
import {useSelector} from '../../../app/contexts/StoreContext';
import SearchForm from '../../../common/components/SearchForm';
import SidebarPanelHeader from '../../../common/components/SidebarPanelHeader';
import {useSessionState} from '../../../core/hooks/useSessionState';
import SearchResultsPanel from './SearchResultsPanel';
import TabsPanel from './TabsPanel';

export const COLLECTION_IDS = {
	fragments: 'fragments',
	widgets: 'widgets',
};

const collectionFilter = (collections, searchValue) => {
	const searchValueLowerCase = searchValue.toLowerCase();

	const itemFilter = (item) =>
		item.label.toLowerCase().indexOf(searchValueLowerCase) !== -1;

	const hasChildren = (collection) => {
		if (collection.children?.length) {
			return true;
		}

		return collection.collections?.some(hasChildren) ?? false;
	};

	return collections
		.reduce((acc, collection) => {
			if (itemFilter(collection)) {
				return [...acc, collection];
			}
			else {
				const updateCollection = {
					...collection,
					children: collection.children?.filter(
						(item) =>
							itemFilter(item) ||
							item.portletItems?.some(itemFilter)
					),
					...(collection.collections?.length && {
						collections: collectionFilter(
							collection.collections,
							searchValueLowerCase
						),
					}),
				};

				return [...acc, updateCollection];
			}
		}, [])
		.filter(hasChildren);
};

const normalizeWidget = (widget) => {
	return {
		data: {
			instanceable: widget.instanceable,
			portletId: widget.portletId,
			portletItemId: widget.portletItemId || null,
			used: widget.used,
		},
		disabled: !widget.instanceable && widget.used,
		highlighted: widget.highlighted,
		icon: widget.instanceable ? 'square-hole-multi' : 'square-hole',
		itemId: widget.portletId,
		label: widget.title,
		portletItems: widget.portletItems?.length
			? widget.portletItems.map(normalizeWidget)
			: null,
		preview: '',
		type: LAYOUT_DATA_ITEM_TYPES.fragment,
	};
};

const normalizeCollection = (collection) => {
	const normalizedElement = {
		children: collection.portlets.map(normalizeWidget),
		collectionId: collection.path,
		label: collection.title,
	};

	if (collection.categories?.length) {
		normalizedElement.collections = collection.categories.map(
			normalizeCollection
		);
	}

	return normalizedElement;
};

const normalizeFragmentEntry = (fragmentEntry) => ({
	data: {
		fragmentEntryKey: fragmentEntry.fragmentEntryKey,
		groupId: fragmentEntry.groupId,
		type: fragmentEntry.type,
	},
	highlighted: fragmentEntry.highlighted,
	icon: fragmentEntry.icon,
	itemId: fragmentEntry.fragmentEntryKey,
	label: fragmentEntry.name,
	preview: fragmentEntry.imagePreviewURL,
	type: fragmentEntry.itemType || LAYOUT_DATA_ITEM_TYPES.fragment,
});

export default function FragmentsSidebar() {
	const fragments = useSelector((state) => state.fragments);
	const widgets = useSelector((state) => state.widgets);

	const [
		activeTabId,
		setActiveTabId,
	] = useSessionState(
		`${config.portletNamespace}_fragments-sidebar_active-tab-id`,
		COLLECTION_IDS.fragments,
		{persistEnabled: Liferay.FeatureFlags['LPS-153452']}
	);

	const [displayStyle, setDisplayStyle] = useSessionState(
		'FRAGMENTS_DISPLAY_STYLE_KEY',
		FRAGMENTS_DISPLAY_STYLES.LIST
	);

	const [searchValue, setSearchValue] = useState('');

	const [showReorderModal, setShowReorderModal] = useState(false);

	const tabs = useMemo(
		() => [
			{
				collections: fragments.map((collection) => ({
					children: collection.fragmentEntries.map((fragmentEntry) =>
						normalizeFragmentEntry(fragmentEntry)
					),
					collectionId: collection.fragmentCollectionId,
					label: collection.name,
				})),
				id: COLLECTION_IDS.fragments,
				label: Liferay.Language.get('fragments'),
			},
			{
				collections: widgets
					? widgets.map((collection) =>
							normalizeCollection(collection)
					  )
					: [],
				id: COLLECTION_IDS.widgets,
				label: Liferay.Language.get('widgets'),
			},
		],
		[fragments, widgets]
	);

	const filteredTabs = useMemo(
		() =>
			searchValue
				? tabs
						.map((tab) => ({
							...tab,
							collections: collectionFilter(
								tab.collections,
								searchValue
							),
						}))
						.filter((item) => item.collections.length)
				: tabs,
		[tabs, searchValue]
	);

	const displayStyleButtonDisabled =
		searchValue || activeTabId === COLLECTION_IDS.widgets;

	return (
		<>
			<SidebarPanelHeader>
				{Liferay.Language.get('fragments-and-widgets')}
			</SidebarPanelHeader>

			<div className="d-flex flex-column page-editor__sidebar__fragments-widgets-panel">
				<div className="align-items-center d-flex flex-shrink-0 justify-content-between mb-3 px-3">
					<SearchForm
						className="flex-grow-1 mb-0"
						onChange={setSearchValue}
					/>

					{Liferay.FeatureFlags['LPS-158737'] && (
						<ClayButtonWithIcon
							borderless
							className="lfr-portal-tooltip ml-2 mt-0"
							data-tooltip-align="bottom-right"
							displayType="secondary"
							onClick={() => setShowReorderModal(true)}
							small
							symbol="order-arrow"
							title={Liferay.Language.get('reorder-sets')}
						/>
					)}

					<ClayButtonWithIcon
						borderless
						className="lfr-portal-tooltip ml-2 mt-0"
						data-tooltip-align="bottom-right"
						disabled={displayStyleButtonDisabled}
						displayType="secondary"
						onClick={() => {
							setDisplayStyle(
								displayStyle === FRAGMENTS_DISPLAY_STYLES.LIST
									? FRAGMENTS_DISPLAY_STYLES.CARDS
									: FRAGMENTS_DISPLAY_STYLES.LIST
							);
						}}
						small
						symbol={
							displayStyleButtonDisabled ||
							displayStyle === FRAGMENTS_DISPLAY_STYLES.LIST
								? 'cards2'
								: 'list'
						}
						title={Liferay.Util.sub(
							Liferay.Language.get('switch-to-x-view'),
							displayStyle === FRAGMENTS_DISPLAY_STYLES.LIST
								? Liferay.Language.get('card')
								: Liferay.Language.get('list')
						)}
					/>
				</div>

				{searchValue ? (
					<SearchResultsPanel filteredTabs={filteredTabs} />
				) : (
					<TabsPanel
						activeTabId={activeTabId}
						displayStyle={displayStyle}
						setActiveTabId={setActiveTabId}
						tabs={tabs}
					/>
				)}
			</div>

			{showReorderModal && (
				<ReorderSetsModal
					onCloseModal={() => setShowReorderModal(false)}
				/>
			)}
		</>
	);
}
