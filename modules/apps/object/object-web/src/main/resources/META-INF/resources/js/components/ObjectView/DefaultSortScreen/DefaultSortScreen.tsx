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

import ClayAlert from '@clayui/alert';
import {useModal} from '@clayui/modal';
import {BuilderScreen} from '@liferay/object-js-components-web';
import React, {useContext, useEffect, useState} from 'react';

import {ModalAddDefaultSortColumn} from '../ModalAddDefaultSortColumn/ModalAddDefaultSortColumn';
import ViewContext, {TYPES} from '../context';

export function DefaultSortScreen() {
	const [
		{
			objectView: {objectViewSortColumns},
		},
		dispatch,
	] = useContext(ViewContext);

	const [visibleModal, setVisibleModal] = useState(false);
	const [isEditingSort, setIsEditingSort] = useState(false);
	const [editingObjectFieldName, setEditingObjectFieldName] = useState('');

	const {observer, onClose} = useModal({
		onClose: () => setVisibleModal(false),
	});

	useEffect(() => {
		visibleModal === false && setIsEditingSort(false);
	}, [visibleModal]);

	const handleChangeColumnOrder = (
		draggedIndex: number,
		targetIndex: number
	) => {
		dispatch({
			payload: {draggedIndex, targetIndex},
			type: TYPES.CHANGE_OBJECT_VIEW_SORT_COLUMN_ORDER,
		});
	};

	const handleDeleteColumn = (objectFieldName: string) => {
		dispatch({
			payload: {objectFieldName},
			type: TYPES.DELETE_OBJECT_VIEW_SORT_COLUMN,
		});
	};

	return (
		<>
			<ClayAlert
				className="lfr-objects__side-panel-content-container"
				displayType="info"
				title={`${Liferay.Language.get('info')}:`}
			>
				{Liferay.Language.get(
					'the-hierarchy-of-the-default-sorting-will-be-defined-by-the-vertical-order-of-the-fields'
				)}
			</ClayAlert>

			<BuilderScreen
				defaultSort
				emptyState={{
					buttonText: Liferay.Language.get('new-default-sort'),
					description: Liferay.Language.get(
						'start-creating-a-sort-to-display-specific-data'
					),
					title: Liferay.Language.get(
						'no-default-sort-was-created-yet'
					),
				}}
				firstColumnHeader={Liferay.Language.get('name')}
				hasDragAndDrop
				objectColumns={objectViewSortColumns ?? []}
				onChangeColumnOrder={handleChangeColumnOrder}
				onDeleteColumn={handleDeleteColumn}
				onEditing={setIsEditingSort}
				onEditingObjectFieldName={setEditingObjectFieldName}
				onVisibleEditModal={setVisibleModal}
				openModal={() => setVisibleModal(true)}
				secondColumnHeader={Liferay.Language.get('sorting')}
				title={Liferay.Language.get('default-sort')}
			/>

			{visibleModal && (
				<ModalAddDefaultSortColumn
					editingObjectFieldName={editingObjectFieldName}
					header={
						isEditingSort
							? Liferay.Language.get('edit-default-sort')
							: Liferay.Language.get('new-default-sort')
					}
					isEditingSort={isEditingSort}
					observer={observer}
					onClose={onClose}
				/>
			)}
		</>
	);
}
