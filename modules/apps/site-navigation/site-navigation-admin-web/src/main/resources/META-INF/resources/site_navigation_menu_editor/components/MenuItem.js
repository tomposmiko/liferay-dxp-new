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
import ClayCard from '@clayui/card';
import ClayIcon from '@clayui/icon';
import ClayLabel from '@clayui/label';
import ClayLayout from '@clayui/layout';
import classNames from 'classnames';
import {fetch, objectToFormData, openToast, sub} from 'frontend-js-web';
import PropTypes from 'prop-types';
import React, {useMemo} from 'react';

import {NESTING_MARGIN} from '../constants/nestingMargin';
import {SIDEBAR_PANEL_IDS} from '../constants/sidebarPanelIds';
import {useConstants} from '../contexts/ConstantsContext';
import {useItems, useSetItems} from '../contexts/ItemsContext';
import {useDragLayer, useSetDragLayer} from '../contexts/KeyboardDndContext';
import {
	useSelectedMenuItemId,
	useSetSelectedMenuItemId,
} from '../contexts/SelectedMenuItemIdContext';
import {useSetSidebarPanelId} from '../contexts/SidebarPanelIdContext';
import getFlatItems from '../utils/getFlatItems';
import getItemPath from '../utils/getItemPath';
import getOrder from '../utils/getOrder';
import {useDragItem, useDropTarget} from '../utils/useDragAndDrop';
import useKeyboardNavigation from '../utils/useKeyboardNavigation';
import {AddItemDropDown} from './AddItemDropdown';
import MenuItemOptions from './MenuItemOptions';

export function MenuItem({item, onMenuItemRemoved}) {
	const setItems = useSetItems();
	const setSelectedMenuItemId = useSetSelectedMenuItemId();
	const setSidebarPanelId = useSetSidebarPanelId();
	const {
		editSiteNavigationMenuItemParentURL,
		portletNamespace,
	} = useConstants();

	const items = useItems();
	const {
		parentSiteNavigationMenuItemId,
		siteNavigationMenuItemId,
		title,
		type,
	} = item;
	const itemPath = getItemPath(siteNavigationMenuItemId, items);
	const selected = useSelectedMenuItemId() === siteNavigationMenuItemId;

	const order = useMemo(
		() =>
			items
				.filter(
					(siteNavigationMenuItem) =>
						siteNavigationMenuItem.parentSiteNavigationMenuItemId ===
						item.parentSiteNavigationMenuItemId
				)
				.findIndex(
					(siteNavigationMenuItem) =>
						siteNavigationMenuItem.siteNavigationMenuItemId ===
						item.siteNavigationMenuItemId
				),
		[items, item]
	);

	const updateMenuItemParent = (itemId, parentId, order) => {
		let computedOrder;

		if (Liferay.FeatureFlags['LPS-134527']) {
			computedOrder = order;
		}
		else {
			computedOrder = getOrder({
				items,
				parentSiteNavigationMenuItemId: parentId,
				siteNavigationMenuItemId: itemId,
			});
		}

		updateMenuItem({
			editSiteNavigationMenuItemParentURL,
			itemId,
			order: computedOrder,
			parentId,
			portletNamespace,
		})
			.then(({siteNavigationMenuItems}) => {
				const newItems = getFlatItems(siteNavigationMenuItems);

				setItems(newItems);
			})
			.catch(({error}) => {
				openToast({
					message: Liferay.Language.get(
						'an-unexpected-error-occurred'
					),
					type: 'danger',
				});

				if (process.env.NODE_ENV === 'development') {
					console.error(error);
				}
			});
	};

	const keyboardDragLayer = useDragLayer();
	const setKeyboardDragLayer = useSetDragLayer();
	const {handlerRef, isDragging} = useDragItem(item, updateMenuItemParent);

	const {isOver, isOverFirstItem, nestingLevel, targetRef} = useDropTarget(
		item
	);

	const isKeyboardDragging = useMemo(
		() =>
			keyboardDragLayer?.siteNavigationMenuItemId
				? getItemPath(siteNavigationMenuItemId, items).includes(
						keyboardDragLayer.siteNavigationMenuItemId
				  )
				: false,
		[
			items,
			keyboardDragLayer?.siteNavigationMenuItemId,
			siteNavigationMenuItemId,
		]
	);

	const itemStyle = {
		'--nesting-level': itemPath.length,
		'--nesting-margin': NESTING_MARGIN,
		'--over-nesting-level': nestingLevel,
	};

	const parentItemId =
		itemPath.length > 1 ? itemPath[itemPath.length - 2] : '0';

	const {
		element,
		isTarget,
		onBlur,
		onFocus,
		onKeyDown,
		setElement,
	} = useKeyboardNavigation();

	const onDragHandlerKeyDown = (event) => {
		if (!Liferay.FeatureFlags['LPS-134527']) {
			return;
		}

		if (event.key === 'Enter') {
			event.preventDefault();
			event.stopPropagation();

			if (isKeyboardDragging) {
				let nextOrder = keyboardDragLayer.order;

				if (
					parentSiteNavigationMenuItemId ===
						keyboardDragLayer.parentSiteNavigationMenuItemId &&
					keyboardDragLayer.order > order
				) {
					nextOrder -= 1;
				}

				updateMenuItem({
					editSiteNavigationMenuItemParentURL,
					itemId: keyboardDragLayer.siteNavigationMenuItemId,
					order: nextOrder,
					parentId: keyboardDragLayer.parentSiteNavigationMenuItemId,
					portletNamespace,
				}).then(({siteNavigationMenuItems}) => {
					setKeyboardDragLayer(items, null);
					setItems(getFlatItems(siteNavigationMenuItems));
				});
			}
			else {
				setKeyboardDragLayer(items, {
					eventKey: event.key,
					menuItemTitle: title,
					menuItemType: type,
					order,
					parentSiteNavigationMenuItemId:
						item.parentSiteNavigationMenuItemId,
					siteNavigationMenuItemId,
				});
			}
		}

		if (event.key === 'Escape') {
			setKeyboardDragLayer(items, null);
		}

		if (!isKeyboardDragging) {
			return;
		}

		event.stopPropagation();

		const eventKey = event.key;

		if (eventKey === 'ArrowDown' || eventKey === 'ArrowUp') {
			const getNextPosition =
				eventKey === 'ArrowDown' ? getDownPosition : getUpPosition;

			const findNextPosition = (previousResult) => {
				const nextResult = getNextPosition({
					items,
					order: previousResult.order,
					parentSiteNavigationMenuItemId:
						previousResult.parentSiteNavigationMenuItemId,
				});

				if (!nextResult) {
					return nextResult;
				}

				const resultPath = getItemPath(
					nextResult.parentSiteNavigationMenuItemId,
					items
				);

				if (resultPath.includes(siteNavigationMenuItemId)) {
					return findNextPosition(nextResult);
				}

				return nextResult;
			};

			const result = findNextPosition({
				order: keyboardDragLayer ? keyboardDragLayer.order : order,
				parentSiteNavigationMenuItemId: keyboardDragLayer
					? keyboardDragLayer.parentSiteNavigationMenuItemId
					: item.parentSiteNavigationMenuItemId,
			});

			if (!result) {
				return;
			}

			event.preventDefault();

			setKeyboardDragLayer(items, {
				eventKey,
				menuItemTitle: title,
				menuItemType: type,
				order: result.order,
				parentSiteNavigationMenuItemId:
					result.parentSiteNavigationMenuItemId,
				siteNavigationMenuItemId,
			});
		}
	};

	return (
		<>
			<div
				aria-description={
					item.icon
						? sub(
								Liferay.Language.get(
									'x-does-not-have-a-display-page-available'
								),
								`${title} (${type})`
						  )
						: null
				}
				aria-label={`${title} (${type})`}
				aria-level={itemPath.length}
				className={classNames(
					'focusable-menu-item site_navigation_menu_editor_MenuItem',
					{
						'active': selected,
						'dragging': isDragging || isKeyboardDragging,
						'is-over': isOver,
						'is-over-top': isOverFirstItem,
					}
				)}
				data-item-id={item.siteNavigationMenuItemId}
				data-nesting-level={nestingLevel}
				data-parent-item-id={parentItemId}
				onBlur={onBlur}
				onClick={(event) => {
					if (!isKeyboardDragging && event.nativeEvent.pointerType) {
						setSelectedMenuItemId(siteNavigationMenuItemId);
						setSidebarPanelId(SIDEBAR_PANEL_IDS.menuItemSettings);
					}
				}}
				onFocus={onFocus}
				onKeyDown={(event) => {
					if (
						(event.key === ' ' || event.key === 'Enter') &&
						!isKeyboardDragging &&
						event.target === element
					) {
						setSelectedMenuItemId(siteNavigationMenuItemId);
						setSidebarPanelId(SIDEBAR_PANEL_IDS.menuItemSettings);
					}

					onKeyDown(event);
				}}
				ref={(ref) => {
					targetRef(ref);
					setElement(ref);
				}}
				role="menuitem"
				style={itemStyle}
				tabIndex={isTarget ? '0' : '-1'}
			>
				<ClayCard className="mb-3">
					<ClayCard.Body className="px-0">
						<div ref={handlerRef}>
							<ClayCard.Row>
								<ClayLayout.ContentCol gutters>
									<ClayButtonWithIcon
										aria-label={sub(
											Liferay.Language.get('move-x'),
											`${title} (${type})`
										)}
										displayType="unstyled"
										monospaced={false}
										onBlur={() =>
											setKeyboardDragLayer(null)
										}
										onKeyDown={onDragHandlerKeyDown}
										size="sm"
										symbol="drag"
										tabIndex={
											isTarget &&
											Liferay.FeatureFlags['LPS-134527']
												? '0'
												: '-1'
										}
									/>
								</ClayLayout.ContentCol>

								<ClayLayout.ContentCol expand>
									<ClayCard.Description
										displayType="title"
										title={title}
									>
										{title}

										{item.icon && (
											<ClayIcon
												className="ml-2 text-warning"
												symbol={item.icon}
											/>
										)}
									</ClayCard.Description>

									<div className="d-flex">
										<ClayLabel
											className="mt-1"
											displayType="secondary"
										>
											{type}
										</ClayLabel>

										{item.dynamic && (
											<ClayLabel
												className="mt-1"
												displayType="info"
											>
												{Liferay.Language.get(
													'dynamic'
												)}
											</ClayLabel>
										)}
									</div>
								</ClayLayout.ContentCol>

								{Liferay.FeatureFlags['LPS-134527'] && (
									<div
										onClick={(event) =>
											event.stopPropagation()
										}
									>
										<AddItemDropDown
											className="position-absolute site_navigation_menu_editor_MenuItem-add-button-dropdown top-button"
											order={order}
											parentSiteNavigationMenuItemId={
												item.parentSiteNavigationMenuItemId
											}
											trigger={
												<ClayButtonWithIcon
													aria-label={sub(
														Liferay.Language.get(
															'add-item-before-x'
														),
														`${title} (${type})`
													)}
													className="site_navigation_menu_editor_MenuItem-add-button"
													displayType="primary"
													onClick={(event) => {
														event.preventDefault();
														event.stopPropagation();
													}}
													size="xs"
													symbol="plus"
													tabIndex={
														isTarget &&
														Liferay.FeatureFlags[
															'LPS-134527'
														]
															? '0'
															: '-1'
													}
													title={Liferay.Language.get(
														'add-item-at-the-same-level'
													)}
												/>
											}
										/>

										<AddItemDropDown
											className="bottom-button position-absolute site_navigation_menu_editor_MenuItem-add-button-dropdown"
											order={order + 1}
											parentSiteNavigationMenuItemId={
												item.parentSiteNavigationMenuItemId
											}
											trigger={
												<ClayButtonWithIcon
													aria-label={sub(
														Liferay.Language.get(
															'add-item-after-x'
														),
														`${title} (${type})`
													)}
													className="site_navigation_menu_editor_MenuItem-add-button"
													displayType="primary"
													onClick={(event) => {
														event.preventDefault();
														event.stopPropagation();
													}}
													size="xs"
													symbol="plus"
													tabIndex={
														isTarget &&
														Liferay.FeatureFlags[
															'LPS-134527'
														]
															? '0'
															: '-1'
													}
													title={Liferay.Language.get(
														'add-item-at-the-same-level'
													)}
												/>
											}
										/>
									</div>
								)}

								<ClayLayout.ContentCol
									gutters
									onClick={(event) => event.stopPropagation()}
								>
									<MenuItemOptions
										isTarget={isTarget}
										label={`${title} (${type})`}
										numberOfChildren={item.children.length}
										onMenuItemRemoved={onMenuItemRemoved}
										siteNavigationMenuItemId={
											siteNavigationMenuItemId
										}
									/>
								</ClayLayout.ContentCol>
							</ClayCard.Row>
						</div>
					</ClayCard.Body>
				</ClayCard>
			</div>
		</>
	);
}

MenuItem.propTypes = {
	item: PropTypes.shape({
		children: PropTypes.array.isRequired,
		siteNavigationMenuItemId: PropTypes.string.isRequired,
		title: PropTypes.string.isRequired,
		type: PropTypes.string.isRequired,
	}),
};

function updateMenuItem({
	editSiteNavigationMenuItemParentURL,
	itemId,
	order,
	parentId,
	portletNamespace,
}) {
	return fetch(editSiteNavigationMenuItemParentURL, {
		body: objectToFormData({
			[`${portletNamespace}siteNavigationMenuItemId`]: itemId,
			[`${portletNamespace}parentSiteNavigationMenuItemId`]: parentId,
			[`${portletNamespace}order`]: order,
		}),
		method: 'POST',
	})
		.then((response) => response.json())
		.catch(({error}) => {
			openToast({
				message: Liferay.Language.get('an-unexpected-error-occurred'),
				type: 'danger',
			});

			if (process.env.NODE_ENV === 'development') {
				console.error(error);
			}
		});
}

export function getDownPosition({
	items,
	order,
	parentSiteNavigationMenuItemId,
}) {
	const flatItems = getFlatItems(items);

	const parentItem = flatItems.find(
		(item) =>
			item.siteNavigationMenuItemId === parentSiteNavigationMenuItemId
	);

	const siblingsItems = flatItems.filter(
		(item) =>
			item.parentSiteNavigationMenuItemId ===
			parentSiteNavigationMenuItemId
	);

	const siblingItem = siblingsItems[order];

	// If there aren't any sibling, the menu is placed as the sibling of the parent.

	if (!siblingItem) {

		// If there aren't any sibling and the parentSiteNavigationMenuItemId is 0,
		// there is no movement possible.

		if (!parentItem) {
			return null;
		}

		const parentSiblings = flatItems.filter(
			(item) =>
				item.parentSiteNavigationMenuItemId ===
				parentItem.parentSiteNavigationMenuItemId
		);

		const parentOrder = parentSiblings.findIndex(
			(item) =>
				item.siteNavigationMenuItemId ===
				parentItem.siteNavigationMenuItemId
		);

		return {
			order: parentOrder + 1,
			parentSiteNavigationMenuItemId:
				parentItem.parentSiteNavigationMenuItemId,
		};
	}

	// If there aren't any sibling, the menu is placed as its child.

	return {
		order: 0,
		parentSiteNavigationMenuItemId: siblingItem.siteNavigationMenuItemId,
	};
}

export function getUpPosition({items, order, parentSiteNavigationMenuItemId}) {

	// The first menu cannot be moved upwards

	if (order === 0 && parentSiteNavigationMenuItemId === '0') {
		return null;
	}

	const flatItems = getFlatItems(items);

	const parentItem = flatItems.find(
		(item) =>
			item.siteNavigationMenuItemId === parentSiteNavigationMenuItemId
	);

	const siblingsItems = flatItems.filter(
		(item) =>
			item.parentSiteNavigationMenuItemId ===
			parentSiteNavigationMenuItemId
	);

	// When the menu is the first child, the menu is placed as the sibling of the parent.

	if (order === 0) {
		const parentSiblings = flatItems.filter(
			(item) =>
				item.parentSiteNavigationMenuItemId ===
				parentItem.parentSiteNavigationMenuItemId
		);

		const parentOrder = parentSiblings.findIndex(
			(item) =>
				item.siteNavigationMenuItemId ===
				parentItem.siteNavigationMenuItemId
		);

		return {
			order: parentOrder,
			parentSiteNavigationMenuItemId:
				parentItem.parentSiteNavigationMenuItemId,
		};
	}

	const siblingItem = siblingsItems[order - 1];

	const siblingItemChildren = flatItems.filter(
		(item) =>
			item.parentSiteNavigationMenuItemId ===
			siblingItem.siteNavigationMenuItemId
	);

	return {
		order: siblingItemChildren.length,
		parentSiteNavigationMenuItemId: siblingItem.siteNavigationMenuItemId,
	};
}
