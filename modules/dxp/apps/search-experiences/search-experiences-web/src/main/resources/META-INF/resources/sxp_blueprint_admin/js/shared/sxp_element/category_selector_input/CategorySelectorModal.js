/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayButton from '@clayui/button';
import ClayEmptyState from '@clayui/empty-state';
import {ClayCheckbox, ClaySelect} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayModal, {useModal} from '@clayui/modal';
import getCN from 'classnames';
import React, {useState} from 'react';

import fetchData from '../../../utils/fetch/fetch_data';
import {FETCH_URLS} from './index';

/**
 * Checks whether the contents of two arrays are the same, by seeing if each object
 * has equal values for certain properties.
 * @param {Array} arr1 First array
 * @param {Array} arr2 Second array
 * @param {object} properties Properties to check within each object of array
 * @returns {boolean}
 */
const doArraysMatch = (arr1, arr2, properties = ['value']) => {
	const doItemsMatch = arr1.every((item1) =>
		arr2.some((item2) =>
			properties.every((property) => item1[property] === item2[property])
		)
	);

	return doItemsMatch && arr1.length === arr2.length;
};

/**
 * Returns info about the tree level. Since CategoryTreeViewGroup is being recursively
 * called, elements such as the urls, icons, selection checkbox will vary depending on
 * whether item is a category or vocabulary. Every item on the first level (depth = 0)
 * should be a vocabulary.
 * @param {number} depth Tree level in question
 * @returns {object}
 */
const getTreeLevelInfo = (depth) =>
	depth > 0
		? {
				getURL: FETCH_URLS.getSubCategories,
				icon: 'categories',
				showSelect: true,
		  }
		: {
				getURL: FETCH_URLS.getCategories,
				icon: 'vocabulary',
				showSelect: false,
		  };

/**
 * Returns a copy of the tree with properties added/updated to the object at index.
 * @param {Array} tree An array of categories
 * @param {number} index Position of object inside tree to update
 * @param {object} properties Properties to add/edit within object at index
 * @returns {Array}
 */
const getUpdatedTree = (tree, index, properties) => {
	const treeCopy = tree.slice();

	treeCopy.splice(index, 1, {...tree[index], ...properties});

	return treeCopy;
};

function TreeViewLink({
	depth,
	hasSubItems,
	id,
	isExpanded = false,
	isSelected,
	multiple,
	name,
	onClickExpand,
	onSelect,
}) {
	const {icon, showSelect} = getTreeLevelInfo(depth);

	return (
		<div
			aria-expanded={isExpanded}
			className={getCN('treeview-link', {collapsed: !isExpanded})}
			role="treeitem"
			style={{paddingLeft: 24 * depth + 'px'}}
		>
			<div className="c-inner" style={{marginLeft: -24 * depth + 'px'}}>
				<div className="autofit-row">
					<div
						className={getCN('autofit-col', {
							'no-subcategories': !hasSubItems,
						})}
					>
						{hasSubItems && (
							<ClayButton
								className="component-expander"
								displayType={null}
								monospaced
								onClick={onClickExpand}
							>
								<span className="c-inner">
									<ClayIcon symbol="angle-down" />

									<ClayIcon
										className="component-expanded-d-none"
										symbol="angle-right"
									/>
								</span>
							</ClayButton>
						)}
					</div>

					{multiple && showSelect && (
						<div className="autofit-col">
							<ClayCheckbox
								checked={isSelected}
								onChange={onSelect}
							/>
						</div>
					)}

					<div className="autofit-col">
						<span className="component-icon">
							<ClayIcon symbol={icon} />
						</span>
					</div>

					<div
						className="autofit-col autofit-col-expand"
						onClick={onClickExpand}
					>
						<span className="component-text">
							<span className="text-truncate-inline">
								<span className="text-truncate">
									{showSelect ? `${name} (ID: ${id})` : name}
								</span>
							</span>
						</span>
					</div>

					{!multiple && showSelect && (
						<div className="autofit-col">
							<ClayButton
								className="quick-action-item"
								displayType="secondary"
								onClick={onSelect}
							>
								{Liferay.Language.get('select')}
							</ClayButton>
						</div>
					)}
				</div>
			</div>
		</div>
	);
}

function TreeViewGroup({
	depth,
	isSelected,
	items,
	multiple,
	onChangeItems,
	onSelect,
}) {
	const {getURL} = getTreeLevelInfo(depth);

	const _handleExpand = (item, index) => {
		if (!item.children && item.numberOfTaxonomyCategories > 0) {
			fetchData(getURL(item.id)).then((responseContent) => {
				onChangeItems(
					getUpdatedTree(items, index, {
						children: responseContent.items.map(
							({id, name, numberOfTaxonomyCategories}) => ({
								id,
								name,
								numberOfTaxonomyCategories,
							})
						),
						expand: true,
					})
				);
			});
		}
		else {
			onChangeItems(
				getUpdatedTree(items, index, {
					expand: !items[index].expand,
				})
			);
		}
	};

	const _handleChangeChildren = (index) => (children) => {
		onChangeItems(getUpdatedTree(items, index, {children}));
	};

	return (
		<ul className="treeview-group" role="group">
			{items.map((item, index) => (
				<li className="treeview-item" key={item.id} role="none">
					<TreeViewLink
						depth={depth}
						hasSubItems={item.numberOfTaxonomyCategories > 0}
						id={item.id}
						isExpanded={item.expand}
						isSelected={isSelected(item)}
						multiple={multiple}
						name={item.name}
						onClickExpand={() => _handleExpand(item, index)}
						onSelect={() => onSelect(item)}
					/>

					{item.children && (
						<div
							className={getCN('collapse', {
								show: item.expand,
							})}
						>
							<TreeViewGroup
								depth={depth + 1}
								isSelected={isSelected}
								items={item.children}
								multiple={multiple}
								onChangeItems={_handleChangeChildren(index)}
								onSelect={onSelect}
							/>
						</div>
					)}
				</li>
			))}
		</ul>
	);
}

function CategorySelectorModal({
	tree = [],
	multiple,
	onChangeValue,
	onChangeTree,
	onClose,
	observer,
	value,
}) {
	const [currentSite, setCurrentSite] = useState({id: ''});
	const [selected, setSelected] = useState(multiple ? value : []);

	const _handleChangeCurrentSite = (event) => {
		const currentSiteIndex = tree.findIndex(
			(site) => site.id === event.target.value
		);

		setCurrentSite({id: event.target.value, index: currentSiteIndex});
	};

	const _handleChangeSiteChildren = (children) => {
		onChangeTree(
			getUpdatedTree(tree, currentSite.index, {
				children,
			})
		);
	};

	const _handleSave = () => {
		onChangeValue(selected);

		onClose();
	};

	const _handleSelect = (item) => {
		if (multiple) {
			if (_isItemSelected(item)) {
				setSelected(
					selected.filter(
						(selectedItem) => selectedItem.value !== item.id
					)
				);
			}
			else {
				setSelected([
					...selected,
					{label: `${item.name} (ID: ${item.id})`, value: item.id},
				]);
			}
		}
		else {
			onChangeValue({
				label: `${item.name} (ID: ${item.id})`,
				value: item.id,
			});

			onClose();
		}
	};

	const _isItemSelected = ({id}) =>
		selected.some((item) => item.value === id);

	return (
		<ClayModal
			className="sxp-category-selector-modal-root"
			observer={observer}
			size="md"
		>
			<ClayModal.Header>
				{Liferay.Language.get('select-asset-category')}
			</ClayModal.Header>

			<ClayModal.Body>
				{tree.length ? (
					<>
						<label htmlFor="selectSite">
							{Liferay.Language.get('select-site')}
						</label>

						<ClaySelect
							aria-label={Liferay.Language.get('select-site')}
							id="selectSite"
							onChange={_handleChangeCurrentSite}
							value={currentSite.id}
						>
							<option hidden></option>

							{tree.map((site) => (
								<ClaySelect.Option
									key={site.id}
									label={site.descriptiveName || site.name}
									value={site.id}
								/>
							))}
						</ClaySelect>
					</>
				) : (
					<ClayEmptyState
						description={Liferay.Language.get(
							'an-error-has-occurred-and-we-were-unable-to-load-the-results'
						)}
						imgSrc="/o/admin-theme/images/states/empty_state.gif"
						title={Liferay.Language.get('no-items-were-found')}
					/>
				)}

				{currentSite.id && (
					<div className="selector-modal-tree">
						{!tree[currentSite.index].children?.length ? (
							<span className="component-text text-secondary">
								{Liferay.Language.get('no-items-were-found')}
							</span>
						) : (
							<ul
								className="treeview treeview-light treeview-nested"
								role="tree"
							>
								<TreeViewGroup
									depth={0}
									isSelected={_isItemSelected}
									items={
										tree[currentSite.index].children || []
									}
									multiple={multiple}
									onChangeItems={_handleChangeSiteChildren}
									onSelect={_handleSelect}
								/>
							</ul>
						)}
					</div>
				)}
			</ClayModal.Body>

			{multiple && (
				<ClayModal.Footer
					last={
						<ClayButton.Group spaced>
							<ClayButton
								displayType="secondary"
								onClick={onClose}
							>
								{Liferay.Language.get('cancel')}
							</ClayButton>

							<ClayButton
								disabled={doArraysMatch(value, selected)}
								onClick={_handleSave}
							>
								{Liferay.Language.get('save')}
							</ClayButton>
						</ClayButton.Group>
					}
				/>
			)}
		</ClayModal>
	);
}

export default function ({
	children,
	multiple,
	onChangeTree,
	onChangeValue,
	tree,
	value,
}) {
	const {observer, onOpenChange, open} = useModal();

	return (
		<>
			<div onClick={() => onOpenChange(true)}>{children}</div>

			{open && (
				<CategorySelectorModal
					multiple={multiple}
					observer={observer}
					onChangeTree={onChangeTree}
					onChangeValue={onChangeValue}
					onClose={() => onOpenChange(false)}
					tree={tree}
					value={value}
				/>
			)}
		</>
	);
}
