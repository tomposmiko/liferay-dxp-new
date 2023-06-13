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

import React, {useRef, useState} from 'react';

import {useItems} from '../contexts/ItemsContext';
import KeyboardMovementText from './KeyboardMovementText';
import {MenuItem} from './MenuItem';

export function Menu() {
	const [movementText, setMovementText] = useState('');
	const [isMovementEnabled, setIsMovementEnabled] = useState(false);

	const items = useItems();
	const menuRef = useRef();

	const onMenuItemRemoved = (itemIndex) => {
		const items = menuRef.current?.querySelectorAll('.focusable-menu-item');

		if (!items) {
			return;
		}

		const index = Math.min(itemIndex, items.length - 1);

		items[index]?.focus();
	};

	return (
		<div
			aria-orientation="vertical"
			className="container ml-lg-auto ml-sm-0 p-3 pt-4"
			ref={menuRef}
			role="menubar"
		>
			<KeyboardMovementText
				setText={setMovementText}
				text={movementText}
			/>

			{items.map((item, index) => (
				<MenuItem
					isMovementEnabled={isMovementEnabled}
					item={item}
					key={item.siteNavigationMenuItemId}
					onMenuItemRemoved={() => onMenuItemRemoved(index)}
					setIsMovementEnabled={setIsMovementEnabled}
					setMovementText={setMovementText}
				/>
			))}
		</div>
	);
}
