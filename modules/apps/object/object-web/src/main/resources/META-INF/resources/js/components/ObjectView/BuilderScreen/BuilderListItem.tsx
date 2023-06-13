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
import ClayList from '@clayui/list';
import {ClayTooltipProvider} from '@clayui/tooltip';
import classNames from 'classnames';
import React, {useContext, useRef} from 'react';
import {useDrag, useDrop} from 'react-dnd';

import ViewContext, {TYPES} from '../context';

import './BuilderListItem.scss';

interface Iprops {
	index: number;
	isDefaultSort?: boolean;
	label?: string;
	objectFieldName: string;
	onEditingObjectFieldName?: (objectFieldName: string) => void;
	onEditingSort?: (boolean: boolean) => void;
	onVisibleModal?: (boolean: boolean) => void;
	sortOrder?: string;
}

type TItemHover = {
	index: number;
	type: string;
};

type TDraggedOffset = {
	x: number;
	y: number;
} | null;

const BuilderListItem: React.FC<Iprops> = ({
	index,
	isDefaultSort,
	label,
	objectFieldName,
	onEditingObjectFieldName,
	onEditingSort,
	onVisibleModal,
	sortOrder,
}) => {
	const [
		{isFFObjectViewSortColumnConfigurationEnabled},
		dispatch,
	] = useContext(ViewContext);

	const ref = useRef<HTMLLIElement>(null);

	const [{isDragging}, dragRef] = useDrag({
		collect: (monitor) => ({
			isDragging: monitor.isDragging(),
		}),
		item: {
			index,
			type: 'FIELD',
		},
	});

	const [, dropRef] = useDrop({
		accept: 'FIELD',
		hover(item: TItemHover, monitor) {
			if (!ref.current) {
				return;
			}

			const draggedIndex = item.index;
			const targetIndex = index;

			if (draggedIndex === targetIndex) {
				return;
			}

			const targetSize = ref.current.getBoundingClientRect();
			const targetCenter = (targetSize.bottom - targetSize.top) / 2;

			const draggedOffset: TDraggedOffset = monitor.getClientOffset();

			if (!draggedOffset) {
				return;
			}

			const draggedTop = draggedOffset.y - targetSize.top;

			if (
				(draggedIndex < targetIndex && draggedTop < targetCenter) ||
				(draggedIndex > targetIndex && draggedTop > targetCenter)
			) {
				return;
			}

			dispatch({
				payload: {draggedIndex, targetIndex},
				type: isDefaultSort
					? TYPES.CHANGE_OBJECT_VIEW_SORT_COLUMN_ORDER
					: TYPES.CHANGE_OBJECT_VIEW_COLUMN_ORDER,
			});

			item.index = targetIndex;
		},
	});

	const handleDeleteColumn = (
		objectFieldName: string,
		isDefaultSort?: boolean
	) => {
		if (isDefaultSort) {
			dispatch({
				payload: {objectFieldName},
				type: TYPES.DELETE_OBJECT_VIEW_SORT_COLUMN,
			});
		}
		else {
			dispatch({
				payload: {objectFieldName},
				type: TYPES.DELETE_OBJECT_VIEW_COLUMN,
			});

			if (isFFObjectViewSortColumnConfigurationEnabled) {
				dispatch({
					payload: {objectFieldName},
					type: TYPES.DELETE_OBJECT_VIEW_SORT_COLUMN,
				});
			}
		}
	};

	dragRef(dropRef(ref));

	const handleEnableEditModal = (objectFieldName: string) => {
		onEditingObjectFieldName && onEditingObjectFieldName(objectFieldName);
		onEditingSort && onEditingSort(true);
		onVisibleModal && onVisibleModal(true);
	};

	return (
		<ClayList.Item
			className={classNames(
				'lfr-object__object-custom-view-builder-item',
				{
					'lfr-object__object-custom-view-builder-item--dragging': isDragging,
				}
			)}
			flex
			ref={ref}
		>
			<ClayList.ItemField>
				<ClayButtonWithIcon displayType={null} symbol="drag" />
			</ClayList.ItemField>

			<ClayList.ItemField expand>
				<ClayList.ItemTitle>{label}</ClayList.ItemTitle>
			</ClayList.ItemField>

			{isDefaultSort && (
				<ClayList.ItemField
					className="lfr-object__object-builder-list-item-sort-order"
					expand
				>
					<ClayList.ItemText>
						{sortOrder === 'asc'
							? Liferay.Language.get('ascending')
							: Liferay.Language.get('descending')}
					</ClayList.ItemText>
				</ClayList.ItemField>
			)}

			<ClayList.ItemField className="lfr-object__object-custom-view-builder-item-action-menu">
				{isDefaultSort && (
					<ClayTooltipProvider>
						<ClayList.QuickActionMenu.Item
							data-tooltip-align="bottom"
							onClick={() =>
								handleEnableEditModal(objectFieldName)
							}
							symbol="pencil"
							title={Liferay.Language.get('Edit')}
						/>
					</ClayTooltipProvider>
				)}

				<ClayTooltipProvider>
					<ClayList.QuickActionMenu.Item
						data-tooltip-align="bottom"
						onClick={() =>
							handleDeleteColumn(objectFieldName, isDefaultSort)
						}
						symbol="times"
						title={Liferay.Language.get('Delete')}
					/>
				</ClayTooltipProvider>
			</ClayList.ItemField>
		</ClayList.Item>
	);
};

export default BuilderListItem;
