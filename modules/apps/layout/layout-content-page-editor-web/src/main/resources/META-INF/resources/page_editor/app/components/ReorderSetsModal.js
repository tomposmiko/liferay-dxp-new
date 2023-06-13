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

import ClayButton, {ClayButtonWithIcon} from '@clayui/button';
import ClayCard from '@clayui/card';
import {ClayDropDownWithItems} from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import ClayModal, {useModal} from '@clayui/modal';
import ClayTabs from '@clayui/tabs';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import React, {useCallback, useEffect, useMemo, useState} from 'react';
import {useDrag, useDrop} from 'react-dnd';
import {getEmptyImage} from 'react-dnd-html5-backend';

import updateSetsOrder from '../../app/thunks/updateSetsOrder';
import {useId} from '../../core/hooks/useId';
import {useDispatch, useSelector} from '../contexts/StoreContext';
import selectWidgetFragmentEntryLinks from '../selectors/selectWidgetFragmentEntryLinks';
import loadWidgets from '../thunks/loadWidgets';
import useTabsKeyboardNavigation from '../utils/useTabsKeyboardNavigation';

const FRAGMENTS_ID = 'fragments';
const WIDGETS_ID = 'widgets';

const ACCEPTING_ITEM_TYPE = 'acceptingItemType';

const HIGHLIGHTED_CATEGORY_ID = 'root--category-highlighted';

const HIGHLIGHTED_COLLECTION_ID = 'highlighted';

export function ReorderSetsModal({onCloseModal}) {
	const {observer, onClose} = useModal({
		onClose: onCloseModal,
	});

	const dispatch = useDispatch();

	const widgetFragmentEntryLinks = useSelector(
		selectWidgetFragmentEntryLinks
	);

	const [lists, setLists] = useState({
		[FRAGMENTS_ID]: null,
		[WIDGETS_ID]: null,
	});

	const updateLists = useCallback(
		(listId, newItems) =>
			setLists({...lists, [listId]: newItems.map(({id}) => id)}),
		[lists, setLists]
	);

	return (
		<ClayModal
			className="page-editor__reorder-set-modal"
			containerProps={{className: 'cadmin'}}
			observer={observer}
		>
			<ClayModal.Header>
				{Liferay.Language.get('reorder-sets')}
			</ClayModal.Header>

			<ClayModal.Body className="p-0">
				<p className="m-0 p-3 text-secondary">
					{Liferay.Language.get(
						'fragments-and-widgets-sets-can-be-ordered-to-give-you-easy-access-to-the-ones-you-use-the-most'
					)}
				</p>

				<Tabs updateLists={updateLists} />
			</ClayModal.Body>

			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton displayType="secondary" onClick={onClose}>
							{Liferay.Language.get('cancel')}
						</ClayButton>

						<ClayButton
							displayType="primary"
							onClick={() => {
								const orderedFragments = lists[FRAGMENTS_ID];
								const orderedWidgets = lists[WIDGETS_ID];

								if (!orderedFragments && !orderedWidgets) {
									return;
								}

								dispatch(
									updateSetsOrder({
										fragments: orderedFragments,
										widgetFragmentEntryLinks,
										widgets: orderedWidgets,
									})
								);

								onClose();
							}}
						>
							{Liferay.Language.get('save')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</ClayModal>
	);
}

ReorderSetsModal.propTypes = {
	onCloseModal: PropTypes.func.isRequired,
};

function Tabs({updateLists}) {
	const namespace = useId();

	const getTabId = (id) => `${namespace}tab${id}`;
	const getTabPanelId = (tabId) => `${namespace}tabPanel${tabId}`;

	const [activeTabId, setActiveTabId] = useState(FRAGMENTS_ID);

	const dispatch = useDispatch();
	const widgetFragmentEntryLinks = useSelector(
		selectWidgetFragmentEntryLinks
	);

	const fragments = useSelector((state) =>
		state.fragments
			.filter(
				({fragmentCollectionId}) =>
					fragmentCollectionId !== HIGHLIGHTED_COLLECTION_ID
			)
			.map(({fragmentCollectionId, name}) => ({
				id: fragmentCollectionId,
				name,
			}))
	);

	const widgets = useSelector((state) =>
		state.widgets
			? state.widgets
					.filter(({path}) => path !== HIGHLIGHTED_CATEGORY_ID)
					.map(({path, title}) => ({
						id: path,
						name: title,
					}))
			: null
	);

	const tabs = useMemo(
		() => [
			{
				id: FRAGMENTS_ID,
				items: fragments,
				label: Liferay.Language.get('fragments'),
			},
			{
				id: WIDGETS_ID,
				items: widgets,
				label: Liferay.Language.get('widgets'),
			},
		],
		[fragments, widgets]
	);

	const {onTabItemKeyDown, tabItemRef} = useTabsKeyboardNavigation({
		numberOfPanels: tabs.length,
		selectPanel: (panelIndex) => {
			const nextPanel = tabs[panelIndex];

			setActiveTabId(nextPanel.id);
		},
	});

	useEffect(() => {
		if (activeTabId === WIDGETS_ID && !widgets) {
			dispatch(
				loadWidgets({
					fragmentEntryLinks: widgetFragmentEntryLinks,
				})
			);
		}
	}, [activeTabId, dispatch, widgetFragmentEntryLinks, widgets]);

	return (
		<>
			<ClayTabs className="px-3">
				{tabs.map(({id, label}, index) => (
					<ClayTabs.Item
						active={activeTabId === id}
						innerProps={{
							'aria-controls': getTabPanelId(id),
							'id': getTabId(id),
						}}
						key={id}
						onClick={() => setActiveTabId(id)}
						onKeyDown={(event) => onTabItemKeyDown(event, index)}
						ref={(ref) => tabItemRef(ref, index)}
					>
						{label}
					</ClayTabs.Item>
				))}
			</ClayTabs>

			<ClayTabs.Content
				activeIndex={tabs.findIndex((tab) => tab.id === activeTabId)}
				fade
			>
				{tabs.map(({id, items}) => (
					<ClayTabs.TabPane
						aria-labelledby={getTabId(id)}
						id={getTabPanelId(id)}
						key={id}
					>
						{items ? (
							<Items
								items={items}
								listId={id}
								updateLists={updateLists}
							/>
						) : (
							<ClayLoadingIndicator small />
						)}
					</ClayTabs.TabPane>
				))}
			</ClayTabs.Content>
		</>
	);
}

Tabs.propTypes = {
	updateLists: PropTypes.func.isRequired,
};

function Items({items: initialItems, listId, updateLists}) {
	const [items, setItems] = useState(initialItems);

	const onChangeItemPosition = (itemId, newPosition) => {
		const itemIndex = items.findIndex(({id}) => id === itemId);
		const item = items[itemIndex];

		const nextItems = [...items];

		nextItems.splice(itemIndex, 1);
		nextItems.splice(newPosition, 0, item);

		setItems(nextItems);
		updateLists(listId, nextItems);
	};

	return (
		<div className="p-4">
			{items.map((item, index) => (
				<CardItem
					index={index}
					item={item}
					key={item.id}
					numberOfItems={items.length}
					onChangeItemPosition={onChangeItemPosition}
				/>
			))}
		</div>
	);
}

Items.propTypes = {
	items: PropTypes.array,
	listId: PropTypes.string.isRequired,
	updateLists: PropTypes.func.isRequired,
};

function CardItem({index, item, numberOfItems, onChangeItemPosition}) {
	const {name} = item;

	const {handlerRef, isDragging} = useDragItem(item);
	const {targetRef} = useDropTarget(item.id, index, onChangeItemPosition);

	return (
		<div ref={targetRef}>
			<div ref={handlerRef}>
				<ClayCard
					className={classNames('mb-3', {dragging: isDragging})}
				>
					<ClayCard.Body className="px-0">
						<ClayCard.Row className="align-items-center">
							<ClayLayout.ContentCol gutters>
								<ClayIcon
									className="text-secondary"
									symbol="drag"
								/>
							</ClayLayout.ContentCol>

							<ClayLayout.ContentCol expand>
								<ClayCard.Description
									className="text-uppercase"
									displayType="title"
									title={name}
								>
									{name}
								</ClayCard.Description>
							</ClayLayout.ContentCol>

							<ClayLayout.ContentCol gutters>
								<ReorderDropdown
									index={index}
									item={item}
									numberOfItems={numberOfItems}
									onChangeItemPosition={onChangeItemPosition}
								/>
							</ClayLayout.ContentCol>
						</ClayCard.Row>
					</ClayCard.Body>
				</ClayCard>
			</div>
		</div>
	);
}

CardItem.propTypes = {
	index: PropTypes.number.isRequired,
	item: PropTypes.object.isRequired,
	numberOfItems: PropTypes.number.isRequired,
	onChangeItemPosition: PropTypes.func.isRequired,
};

function ReorderDropdown({index, item, numberOfItems, onChangeItemPosition}) {
	const items = [
		{
			disabled: index === 0,
			label: Liferay.Language.get('move-up'),
			onClick: () => onChangeItemPosition(item.id, index - 1),
			symbolLeft: 'angle-up',
		},
		{
			disabled: index === numberOfItems - 1,
			label: Liferay.Language.get('move-down'),
			onClick: () => onChangeItemPosition(item.id, index + 1),
			symbolLeft: 'angle-down',
		},
	];

	return (
		<ClayDropDownWithItems
			items={items}
			trigger={
				<ClayButtonWithIcon
					className="text-secondary"
					displayType="unstyled"
					small
					symbol="ellipsis-v"
				/>
			}
		/>
	);
}

ReorderDropdown.propTypes = {
	index: PropTypes.number.isRequired,
	item: PropTypes.object.isRequired,
	numberOfItems: PropTypes.number.isRequired,
	onChangeItemPosition: PropTypes.func.isRequired,
};

function useDragItem(item) {
	const [{isDragging}, handlerRef, previewRef] = useDrag({
		begin() {},
		collect: (monitor) => ({
			isDragging: !!monitor.isDragging(),
		}),
		item: {...item, type: ACCEPTING_ITEM_TYPE},
	});

	useEffect(() => {
		previewRef(getEmptyImage(), {captureDraggingState: true});
	}, [previewRef]);

	return {
		handlerRef,
		isDragging,
	};
}

export function useDropTarget(itemId, itemIndex, onChangeItemPosition) {
	const [, targetRef] = useDrop({
		accept: ACCEPTING_ITEM_TYPE,
		canDrop(source, monitor) {
			return monitor.isOver();
		},
		hover(source, monitor) {
			if (monitor.canDrop(source, monitor)) {
				if (source.id === itemId) {
					return;
				}

				onChangeItemPosition(source.id, itemIndex);
			}
		},
	});

	return {
		targetRef,
	};
}
