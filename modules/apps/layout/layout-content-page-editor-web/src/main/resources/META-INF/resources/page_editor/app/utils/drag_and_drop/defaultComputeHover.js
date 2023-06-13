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

import {LAYOUT_DATA_ITEM_TYPES} from '../../config/constants/layoutDataItemTypes';
import {collectionIsMapped} from '../collectionIsMapped';
import {formIsMapped} from '../formIsMapped';
import isItemContainerFlex from '../isItemContainerFlex';
import isItemEmpty from '../isItemEmpty';
import checkAllowedChild from './checkAllowedChild';
import {DRAG_DROP_TARGET_TYPE} from './constants/dragDropTargetType';
import {ORIENTATIONS} from './constants/orientations';
import {TARGET_POSITIONS} from './constants/targetPositions';
import getDropTargetPosition from './getDropTargetPosition';
import getTargetData from './getTargetData';
import getTargetPositions from './getTargetPositions';
import itemIsAncestor from './itemIsAncestor';

const ELEVATION_BORDER_SIZE = 15;
const MAXIMUM_ELEVATION_STEPS = 3;
const ORIENTATION_BORDER_SIZE = 80;

export default function defaultComputeHover({
	dispatch,
	layoutDataRef,
	monitor,
	siblingItem = null,
	sourceItem,
	targetItem,
	targetRefs,
}) {

	// Not dragging over direct child
	// We do not want to alter state here,
	// as dnd generate extra hover events when
	// items are being dragged over nested children

	if (!monitor.isOver({shallow: true})) {
		return;
	}

	// Apparently valid drag, calculate position and
	// nesting validation

	const orientation = getOrientation(
		siblingItem || targetItem,
		monitor,
		targetRefs,
		layoutDataRef
	);

	const [
		targetPositionWithMiddle,
		targetPositionWithoutMiddle,
		elevationDepth,
	] = getItemPosition(
		siblingItem || targetItem,
		monitor,
		targetRefs,
		orientation
	);

	// Drop inside target

	const validDropInsideTarget = (() => {
		const targetIsColumn =
			targetItem.type === LAYOUT_DATA_ITEM_TYPES.column;
		const targetIsCollectionNotMapped =
			targetItem.type === LAYOUT_DATA_ITEM_TYPES.collection &&
			!collectionIsMapped(targetItem);
		const targetIsContainerFlex = isItemContainerFlex(targetItem);
		const targetIsFragment =
			targetItem.type === LAYOUT_DATA_ITEM_TYPES.fragment;
		const targetIsFormNotMapped =
			targetItem.type === LAYOUT_DATA_ITEM_TYPES.form &&
			!formIsMapped(targetItem);
		const targetIsEmpty = isItemEmpty(
			layoutDataRef.current.items[targetItem.itemId],
			layoutDataRef.current
		);

		return (
			targetPositionWithMiddle === TARGET_POSITIONS.MIDDLE &&
			(targetIsEmpty ||
				targetIsColumn ||
				targetIsContainerFlex ||
				targetIsCollectionNotMapped ||
				targetIsFormNotMapped) &&
			!targetIsFragment
		);
	})();

	if (
		!siblingItem &&
		validDropInsideTarget &&
		!itemIsAncestor(sourceItem, targetItem, layoutDataRef)
	) {
		return dispatch({
			dropItem: sourceItem,
			dropTargetItem: targetItem,
			droppable: checkAllowedChild(sourceItem, targetItem, layoutDataRef),
			elevate: null,
			targetPositionWithMiddle,
			targetPositionWithoutMiddle,
			type: DRAG_DROP_TARGET_TYPE.INSIDE,
		});
	}

	// Valid elevation:
	// - sourceItem should be child of dropTargetItem
	// - sourceItem should be sibling of siblingItem
	// - siblingItem should have flex parent for horizontal elevation
	//   and no-flex parent for vertical elevation
	// - sourceItem should not be ancestor of siblingItem

	if (
		siblingItem &&
		!shouldBeIgnoredInElevation(parent) &&
		validElevation(siblingItem, orientation, layoutDataRef) &&
		!itemIsAncestor(sourceItem, siblingItem, layoutDataRef)
	) {
		return dispatch({
			dropItem: sourceItem,
			dropTargetItem: siblingItem,
			droppable: checkAllowedChild(sourceItem, targetItem, layoutDataRef),
			elevate: true,
			targetPositionWithMiddle,
			targetPositionWithoutMiddle,
			type: DRAG_DROP_TARGET_TYPE.ELEVATE,
		});
	}

	// Try to elevate to some valid ancestor
	// Using dropTargetItem parent as target and dropTargetItem as sibling
	// It will try elevate multiple levels if elevationDepth is enough and
	// there are valid ancestors

	if (elevationDepth) {
		const getElevatedTargetItem = (sibling, maximumDepth) => {
			let parent = layoutDataRef.current.items[sibling.parentId];

			if (parent) {
				parent = {
					...parent,
					collectionItemIndex: sibling.collectionItemIndex,
					parentToControlsId: sibling.parentToControlsId,
					toControlsId:
						parent.type === LAYOUT_DATA_ITEM_TYPES.collection
							? sibling.parentToControlsId
							: sibling.toControlsId,
				};
			}

			if (parent) {
				const [siblingPositionWithMiddle] = getItemPosition(
					sibling,
					monitor,
					targetRefs,
					orientation
				);

				const [parentPositionWithMiddle] = getItemPosition(
					parent,
					monitor,
					targetRefs,
					orientation
				);

				if (
					(siblingPositionWithMiddle === targetPositionWithMiddle ||
						parentPositionWithMiddle ===
							targetPositionWithMiddle) &&
					!shouldBeIgnoredInElevation(parent)
				) {
					if (maximumDepth > 1) {
						const [
							grandParent,
							parentSibling,
						] = getElevatedTargetItem(parent, maximumDepth - 1);

						if (grandParent) {
							return [grandParent, parentSibling];
						}
					}

					return [parent, sibling];
				}
				else {
					return getElevatedTargetItem(parent, maximumDepth);
				}
			}

			return [null, null];
		};

		const [elevatedTargetItem, siblingItem] = getElevatedTargetItem(
			targetItem,
			elevationDepth
		);

		if (elevatedTargetItem && elevatedTargetItem !== targetItem) {
			return defaultComputeHover({
				dispatch,
				layoutDataRef,
				monitor,
				siblingItem,
				sourceItem,
				targetItem: elevatedTargetItem,
				targetRefs,
			});
		}
	}
}

function getOrientation(item, monitor, targetRefs, layoutDataRef) {
	if (
		!item.parentId ||
		!isItemContainerFlex(layoutDataRef.current.items[item.parentId])
	) {
		return ORIENTATIONS.vertical;
	}

	const targetRef = targetRefs.get(item.toControlsId(item.itemId));
	const targetRect = targetRef.current.getBoundingClientRect();
	const hoverMiddle = targetRect.left + targetRect.width / 2;
	const clientOffsetX = monitor.getClientOffset().x;

	const targetPosition =
		clientOffsetX < hoverMiddle
			? TARGET_POSITIONS.LEFT
			: TARGET_POSITIONS.RIGHT;

	const distanceFromBorder =
		targetPosition === TARGET_POSITIONS.LEFT
			? clientOffsetX - targetRect.left
			: targetRect.right - clientOffsetX;

	return distanceFromBorder < ORIENTATION_BORDER_SIZE
		? ORIENTATIONS.horizontal
		: ORIENTATIONS.vertical;
}

function getItemPosition(item, monitor, targetRefs, orientation) {
	const targetRef = targetRefs.get(item.toControlsId(item.itemId));

	if (!targetRef || !targetRef.current) {
		return [null, null, 0];
	}

	const clientOffset =
		orientation === ORIENTATIONS.horizontal
			? monitor.getClientOffset().x
			: monitor.getClientOffset().y;

	const targetRect = targetRef.current.getBoundingClientRect();
	const targetPositions = getTargetPositions(orientation);
	const targetData = getTargetData(targetRect, orientation);

	const elevationStepSize = Math.min(
		targetData.length / (2 * (MAXIMUM_ELEVATION_STEPS + 1)),
		ELEVATION_BORDER_SIZE
	);

	const totalElevationBorderSize =
		elevationStepSize * MAXIMUM_ELEVATION_STEPS;

	const [
		targetPositionWithMiddle,
		targetPositionWithoutMiddle,
	] = getDropTargetPosition(
		clientOffset,
		totalElevationBorderSize,
		targetPositions,
		targetData
	);

	let elevationDepth = 0;

	if (targetPositionWithMiddle !== TARGET_POSITIONS.MIDDLE) {
		const distanceFromBorder =
			targetPositionWithMiddle === targetPositions.start
				? clientOffset - targetData.start
				: targetData.end - clientOffset;

		elevationDepth =
			MAXIMUM_ELEVATION_STEPS -
			Math.floor(
				(distanceFromBorder / totalElevationBorderSize) *
					MAXIMUM_ELEVATION_STEPS
			);
	}

	return [
		targetPositionWithMiddle,
		targetPositionWithoutMiddle,
		elevationDepth,
	];
}

function shouldBeIgnoredInElevation(item) {

	// Dropping inside a collection or inside a row is illegal
	// but in those cases we don't want to inform the user about it,
	// we just want to ignore those cases and try to elevate in the direct parent.
	// This is why this case is handled separately in the checkAllowedChild function

	return (
		item.type === LAYOUT_DATA_ITEM_TYPES.collection ||
		item.type === LAYOUT_DATA_ITEM_TYPES.row
	);
}

function validElevation(siblingItem, orientation, layoutDataRef) {
	const targetItemParent = layoutDataRef.current.items[siblingItem.parentId];

	return orientation === ORIENTATIONS.horizontal
		? isItemContainerFlex(targetItemParent)
		: !isItemContainerFlex(targetItemParent);
}
