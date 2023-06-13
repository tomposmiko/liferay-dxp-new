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
import ClayEmptyState from '@clayui/empty-state';
import {ClayCheckbox} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import {getOpener, sub} from 'frontend-js-web';
import React, {useEffect, useMemo, useState} from 'react';

const nodeByName = (items, name) => {
	return items.reduce(function reducer(acc, item) {
		if (item.name?.toLowerCase().includes(name.toLowerCase())) {
			acc.push(item);
		}
		else if (item.children) {
			acc.concat(item.children.reduce(reducer, acc));
		}

		return acc;
	}, []);
};

function visit(nodes, callback) {
	nodes.forEach((node) => {
		callback(node);

		if (node.children) {
			visit(node.children, callback);
		}
	});
}

export function AssetCategoryTree({
	filterQuery,
	itemSelectedEventName,
	items,
	multiSelection,
	onItems,
	onSelectedItemsCount,
}) {
	const [selectedKeys, setSelectionChange] = useState(new Set());

	const filteredItems = useMemo(() => {
		if (!filterQuery) {
			return items;
		}

		return nodeByName(items, filterQuery);
	}, [items, filterQuery]);

	const itemsById = useMemo(() => {
		const flattenItems = {};

		visit(items, (item) => {
			flattenItems[item.id] = item;
		});

		return flattenItems;
	}, [items]);

	useEffect(() => {
		const selectedItems = [];

		selectedKeys.forEach((key) => {
			const item = itemsById[key];

			if (item.disabled) {
				return;
			}

			selectedItems.push({
				className: item.className,
				classNameId: item.classNameId,
				classPK: item.id,
				title: item.name,
			});
		});

		if (onSelectedItemsCount) {
			onSelectedItemsCount(selectedItems.length);
		}

		let data = selectedItems;

		if (!multiSelection) {
			data = selectedItems[0];
		}

		requestAnimationFrame(() => {
			if (data) {
				getOpener().Liferay.fire(itemSelectedEventName, {
					data,
				});
			}
		});
	}, [
		selectedKeys,
		itemsById,
		itemSelectedEventName,
		multiSelection,
		onSelectedItemsCount,
	]);

	const onClick = (event, item, selection, expand) => {
		event.preventDefault();

		if (item.disabled) {
			expand.toggle(item.id);

			return;
		}

		if (!multiSelection) {
			selection.toggle(item.id);

			return;
		}

		selection.toggle(item.id, {
			parentSelection: false,
			selectionMode: event.shiftKey ? 'multiple-recursive' : null,
		});
	};

	const onKeyDown = (event, item, selection) => {
		if (event.key === ' ' || event.key === 'Enter') {
			event.preventDefault();

			if (item.disabled) {
				return;
			}

			if (!multiSelection) {
				selection.toggle(item.id);

				return;
			}

			selection.toggle(item.id, {
				parentSelection: false,
				selectionMode: event.shiftKey ? 'multiple-recursive' : null,
			});
		}
	};

	return filteredItems.length ? (
		<>
			{multiSelection && (
				<p
					className="mb-4"
					dangerouslySetInnerHTML={{
						__html: sub(
							Liferay.Language.get(
								'press-x-to-select-or-deselect-a-parent-node-and-all-its-child-items'
							),
							'<kbd class="c-kbd c-kbd-light">⇧</kbd>'
						),
					}}
				/>
			)}

			<ClayTreeView
				items={filteredItems}
				onItemsChange={(items) => onItems(items)}
				onSelectionChange={(keys) => setSelectionChange(keys)}
				selectedKeys={selectedKeys}
				selectionMode={multiSelection ? 'multiple' : 'single'}
				showExpanderOnHover={false}
			>
				{(item, selection, expand) => (
					<ClayTreeView.Item>
						<ClayTreeView.ItemStack
							onClick={(event) =>
								onClick(event, item, selection, expand)
							}
							onKeyDown={(event) =>
								onKeyDown(event, item, selection)
							}
						>
							{multiSelection && !item.disabled && (
								<Checkbox
									checked={selection.has(item.id)}
									onChange={(event) => {
										selection.toggle(item.id, {
											parentSelection: false,
											selectionMode: event.nativeEvent
												.shiftKey
												? 'multiple-recursive'
												: null,
										});
									}}
									onClick={(event) => event.stopPropagation()}
									tabIndex="-1"
								/>
							)}

							<ClayIcon symbol={item.icon} />

							{item.name}
						</ClayTreeView.ItemStack>

						<ClayTreeView.Group items={item.children}>
							{(item) => (
								<ClayTreeView.Item
									onClick={(event) =>
										onClick(event, item, selection)
									}
									onKeyDown={(event) =>
										onKeyDown(event, item, selection)
									}
								>
									{multiSelection && !item.disabled && (
										<Checkbox
											checked={selection.has(item.id)}
											onChange={(event) => {
												selection.toggle(item.id, {
													parentSelection: false,
													selectionMode: event
														.nativeEvent.shiftKey
														? 'multiple-recursive'
														: null,
												});
											}}
											onClick={(event) =>
												event.stopPropagation()
											}
											tabIndex="-1"
										/>
									)}

									<ClayIcon symbol={item.icon} />

									{item.name}
								</ClayTreeView.Item>
							)}
						</ClayTreeView.Group>
					</ClayTreeView.Item>
				)}
			</ClayTreeView>
		</>
	) : (
		<ClayEmptyState
			description={Liferay.Language.get(
				'try-again-with-a-different-search'
			)}
			imgSrc={`${themeDisplay.getPathThemeImages()}/states/search_state.gif`}
			small
			title={Liferay.Language.get('no-results-found')}
		/>
	);
}

const Checkbox = (props) => <ClayCheckbox {...props} />;
