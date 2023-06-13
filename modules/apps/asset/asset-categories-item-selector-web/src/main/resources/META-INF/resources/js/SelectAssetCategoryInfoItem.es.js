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
import ClayButton from '@clayui/button';
import ClayEmptyState from '@clayui/empty-state';
import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import {navigate} from 'frontend-js-web';
import React, {useState} from 'react';

import {AssetCategoryTree} from './AssetCategoryTree.es';

function SelectAssetCategory({
	addCategoryURL,
	inheritSelection,
	itemSelectedEventName,
	moveCategory,
	multiSelection,
	namespace,
	nodes,
	selectedCategoryIds,
}) {
	const [items, setItems] = useState(() => {
		if (nodes.length === 1 && nodes[0].vocabulary && nodes[0].id !== '0') {
			return nodes[0].children;
		}

		return nodes;
	});

	const [filterQuery, setFilterQuery] = useState('');
	const [selectedKeys, setSelectedKeys] = useState(
		new Set(selectedCategoryIds)
	);
	const [selectedItemsCount, setSelectedItemsCount] = useState(0);

	return (
		<div className="select-category">
			{moveCategory && (
				<ClayAlert displayType="info" variant="stripe">
					{Liferay.Language.get(
						'categories-can-only-be-moved-to-a-vocabulary-or-a-category-with-the-same-visibility'
					)}
				</ClayAlert>
			)}

			<form
				className="mb-0 px-1 py-3 select-category-filter"
				onSubmit={(event) => event.preventDefault()}
				role="search"
			>
				<ClayLayout.ContainerFluid className="d-flex">
					<div className="input-group">
						<div className="input-group-item">
							<input
								aria-label={Liferay.Language.get(
									'search-categories'
								)}
								className="form-control h-100 input-group-inset input-group-inset-after"
								onChange={(event) =>
									setFilterQuery(event.target.value)
								}
								placeholder={Liferay.Language.get('search')}
								type="text"
							/>

							<div className="input-group-inset-item input-group-inset-item-after pr-3">
								<ClayIcon symbol="search" />
							</div>
						</div>
					</div>

					{addCategoryURL && (
						<ClayButton
							className="btn-monospaced ml-3 nav-btn nav-btn-monospaced"
							displayType="primary"
							onClick={() => {
								navigate(addCategoryURL);
							}}
						>
							<ClayIcon symbol="plus" />
						</ClayButton>
					)}
				</ClayLayout.ContainerFluid>
			</form>

			{selectedItemsCount && multiSelection ? (
				<ClayLayout.Container
					className="align-items-center category-tree-count-feedback d-flex justify-content-between px-3"
					containerElement="section"
					fluid
				>
					<p className="m-0 text-2">
						{selectedItemsCount > 1
							? `${selectedItemsCount} ${Liferay.Language.get(
									'items-selected'
							  )}`
							: `${selectedItemsCount} ${Liferay.Language.get(
									'item-selected'
							  )}`}
					</p>

					<ClayButton
						className="text-3 text-dark text-weight-semi-bold"
						displayType="link"
						onClick={() => {
							setSelectedKeys(new Set([]));
						}}
					>
						{Liferay.Language.get('clear-all')}
					</ClayButton>
				</ClayLayout.Container>
			) : null}

			<form name={`${namespace}selectCategoryFm`}>
				<ClayLayout.ContainerFluid
					className="px-3"
					containerElement="fieldset"
				>
					<div
						className="category-tree mt-3"
						id={`${namespace}categoryContainer`}
					>
						{items.length ? (
							<AssetCategoryTree
								filterQuery={filterQuery}
								inheritSelection={inheritSelection}
								itemSelectedEventName={itemSelectedEventName}
								items={items}
								multiSelection={multiSelection}
								onItems={setItems}
								onSelectedItemsCount={setSelectedItemsCount}
								selectedKeys={selectedKeys}
								setSelectedKeys={setSelectedKeys}
							/>
						) : (
							<ClayEmptyState
								description={Liferay.Language.get(
									'no-categories-were-found'
								)}
								imgSrc={`${themeDisplay.getPathThemeImages()}/states/empty_state.gif`}
								title={null}
							/>
						)}
					</div>
				</ClayLayout.ContainerFluid>
			</form>
		</div>
	);
}

export default SelectAssetCategory;
