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

import {TreeView as ClayTreeView} from '@clayui/core';
import ClayIcon from '@clayui/icon';
import classnames from 'classnames';
import {navigate} from 'frontend-js-web';
import PropTypes from 'prop-types';
import React from 'react';

const ITEM_TYPES_SYMBOL = {
	article: 'document-text',
	folder: 'folder',
};
export default function NavigationPanel({items, selectedItemId}) {
	const handleClickItem = (event, item) => {
		event.stopPropagation();
		event.preventDefault();

		navigate(item.href);
	};

	return (
		<ClayTreeView
			defaultItems={items}
			defaultSelectedKeys={new Set([selectedItemId])}
			nestedKey="children"
			showExpanderOnHover={false}
		>
			{(item) => {
				return (
					<ClayTreeView.Item
						onClick={(event) => {
							handleClickItem(event, item);
						}}
					>
						<ClayTreeView.ItemStack
							className={classnames({
								'knowledge-base-navigation-item-active':
									item.id === selectedItemId,
							})}
						>
							<ClayIcon symbol={ITEM_TYPES_SYMBOL[item.type]} />

							{item.name}
						</ClayTreeView.ItemStack>

						<ClayTreeView.Group items={item.children}>
							{(item) => {
								return (
									<ClayTreeView.Item
										onClick={(event) => {
											handleClickItem(event, item);
										}}
									>
										<ClayIcon
											symbol={
												ITEM_TYPES_SYMBOL[item.type]
											}
										/>

										{item.name}
									</ClayTreeView.Item>
								);
							}}
						</ClayTreeView.Group>
					</ClayTreeView.Item>
				);
			}}
		</ClayTreeView>
	);
}

const itemShape = {
	href: PropTypes.string.isRequired,
	id: PropTypes.string.isRequired,
	name: PropTypes.string.isRequired,
	type: PropTypes.oneOf(Object.keys(ITEM_TYPES_SYMBOL)).isRequired,
};

itemShape.children = PropTypes.arrayOf(PropTypes.shape(itemShape));

NavigationPanel.propTypes = {
	items: PropTypes.arrayOf(PropTypes.shape(itemShape)),
	selectedItemId: PropTypes.string,
};
