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

import {useMovementTarget} from '../contexts/KeyboardMovementContext';
import {useSelectorRef} from '../contexts/StoreContext';
import getDropContainerId from './drag_and_drop/getDropContainerId';
import {useDropTargetData} from './drag_and_drop/useDragAndDrop';

export default function useDropContainerId() {
	const {item: dropItem, position: dropPosition} = useDropTargetData();
	const {
		itemId: keyboardMovementItemId,
		position: keyboardMovementPosition,
	} = useMovementTarget();

	const layoutDataRef = useSelectorRef((state) => state.layoutData);
	const keyboardMovementItem =
		layoutDataRef.current.items[keyboardMovementItemId];

	if (!dropItem && !keyboardMovementItem) {
		return null;
	}

	const dropContainerId = getDropContainerId(
		layoutDataRef.current,
		dropItem || keyboardMovementItem,
		dropPosition || keyboardMovementPosition
	);

	return dropContainerId;
}
