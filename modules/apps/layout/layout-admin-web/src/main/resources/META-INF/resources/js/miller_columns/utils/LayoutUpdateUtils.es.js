import {DROP_TARGET_TYPES} from './LayoutDragDrop.es';
import {
	getColumnActiveItem,
	getHomeItem,
	getItem,
	getItemColumn,
	getItemColumnIndex
} from './LayoutGetUtils.es';
import {setIn} from '../../utils/utils.es';

/**
 * Append an item to a column and returns a new array of columns
 * @param {object} sourceItem
 * @param {object[]} layoutColumns
 * @param {number} targetColumnIndex
 * @return {object[]}
 * @review
 */
function appendItemToColumn(sourceItem, layoutColumns, targetColumnIndex) {
	let nextLayoutColumns = layoutColumns;

	if (sourceItem) {
		nextLayoutColumns = removeItem(sourceItem.plid, nextLayoutColumns);

		const nextTargetColumn = [...nextLayoutColumns[targetColumnIndex]];

		nextTargetColumn.splice(nextTargetColumn.length, 0, sourceItem);

		nextLayoutColumns[targetColumnIndex] = nextTargetColumn;
	}

	return nextLayoutColumns;
}

/**
 * Removes following columns starting at position indicated
 * by startColumnIndex and returns a new array of columns
 * @param {object} layoutColumns
 * @param {number} startColumnIndex
 * @return {object}
 * @review
 */
function clearFollowingColumns(layoutColumns, startColumnIndex) {
	const nextLayoutColumns = layoutColumns.map(
		(layoutColumn) => [...layoutColumn]
	);

	for (let i = startColumnIndex + 1; i < nextLayoutColumns.length; i++) {
		nextLayoutColumns[i] = [];
	}

	return nextLayoutColumns;
}

/**
 * Clears path if an active item is moved to another column
 * @param {object} layoutColumns
 * @param {object} sourceItem
 * @param {number} sourceItemColumnIndex
 * @param {string} targetItemPlid
 * @return {object}
 * @review
 */
function clearPath(
	layoutColumns,
	sourceItem,
	sourceItemColumnIndex,
	targetId,
	targetType
) {
	let nextLayoutColumns = layoutColumns.map(
		(layoutColumn) => [...layoutColumn]
	);

	let targetColumnIndex = targetId;

	if (targetType === DROP_TARGET_TYPES.item) {
		targetColumnIndex = getItemColumnIndex(
			nextLayoutColumns,
			targetId
		);
	}

	if (
		sourceItem &&
		sourceItem.active &&
		(sourceItemColumnIndex !== targetColumnIndex)
	) {
		sourceItem.active = false;

		nextLayoutColumns = clearFollowingColumns(
			nextLayoutColumns,
			sourceItemColumnIndex
		);

		nextLayoutColumns = deleteEmptyColumns(nextLayoutColumns);
	}

	return nextLayoutColumns;
}

/**
 * Removes extra empty columns when there are more than three and returns
 * a new Array with the columns removed.
 * @param {object} layoutColumns
 * @private
 * @return {object}
 * @review
 */
function deleteEmptyColumns(layoutColumns) {
	const nextLayoutColumns = [...layoutColumns];

	for (let i = 3; (i < nextLayoutColumns.length) &&
		(nextLayoutColumns[i].length === 0); i++) {
		nextLayoutColumns.splice(i, 1);
	}

	return nextLayoutColumns;
}

/**
 * Insert an item inside another item's children
 * and returns a new array of columns
 * @param {object} layoutColumns
 * @param {boolean} pathUpdated
 * @param {object} sourceItem
 * @param {number} sourceItemColumnIndex
 * @param {object} targetItem
 * @return {object}
 * @review
 */
function moveItemInside(
	layoutColumns,
	pathUpdated,
	sourceItem,
	sourceItemColumnIndex,
	targetItem
) {
	let nextLayoutColumns = removeItem(sourceItem.plid, layoutColumns);

	const targetColumn = getItemColumn(
		nextLayoutColumns,
		targetItem.plid
	);

	const targetColumnIndex = getItemColumnIndex(
		nextLayoutColumns,
		targetItem.plid
	);

	if (targetItem.active) {
		const nextColumn = nextLayoutColumns[targetColumnIndex + 1];

		if (nextColumn) {
			nextLayoutColumns = setIn(
				nextLayoutColumns,
				[
					targetColumnIndex + 1,
					nextColumn.length
				],
				sourceItem
			);
		}
		else {
			nextLayoutColumns = setIn(
				nextLayoutColumns,
				[targetColumnIndex + 1],
				[]
			);

			nextLayoutColumns = setIn(
				nextLayoutColumns,
				[
					targetColumnIndex + 1,
					0
				],
				sourceItem
			);
		}
	}

	if (sourceItem.active && !pathUpdated) {
		nextLayoutColumns = clearFollowingColumns(
			nextLayoutColumns,
			sourceItemColumnIndex
		);

		nextLayoutColumns = deleteEmptyColumns(nextLayoutColumns);
	}

	return setIn(
		nextLayoutColumns,
		[
			targetColumnIndex,
			targetColumn.indexOf(targetItem),
			'hasChild'
		],
		true
	);
}

/**
 * Removes an item, if any, from a column and returns a new array of columns
 * @param {string} itemPlid
 * @param {object[]} layoutColumns
 * @param {string} targetItemPlid
 * @return {object[]} new column array
 * @review
 */
function removeItem(itemPlid, layoutColumns) {
	const item = getItem(layoutColumns, itemPlid);
	let nextLayoutColumns = layoutColumns;

	if (item) {
		nextLayoutColumns = [...layoutColumns];

		const itemColumn = getItemColumn(nextLayoutColumns, itemPlid);

		if (itemColumn) {
			const itemIndex = itemColumn.findIndex(
				(_item) => _item.plid === itemPlid
			);
			const nextItemColumn = [...itemColumn];
			const nextItemColumnIndex = getItemColumnIndex(
				nextLayoutColumns,
				itemPlid
			);

			nextItemColumn.splice(itemIndex, 1);
			nextLayoutColumns[nextItemColumnIndex] = nextItemColumn;
		}
	}

	return nextLayoutColumns;
}

/**
 * Set the first page as Home page
 * @param {!Array} layoutColumns
 * @param {string} currentHomeItemPlid
 * @return {object|null}
 * @review
 */
function setHomePage(layoutColumns) {
	let nextLayoutColumns = layoutColumns;

	const firstItem = nextLayoutColumns[1][0];

	if (!firstItem.homePage) {
		nextLayoutColumns = setIn(nextLayoutColumns, [1, 0, 'homePage'], true);

		const currentHomeItem = getHomeItem(layoutColumns);

		if (currentHomeItem) {
			const currentHomeItemColumn = getItemColumn(
				nextLayoutColumns,
				currentHomeItem.plid
			);
			const currentHomeItemColumnIndex = getItemColumnIndex(
				nextLayoutColumns,
				currentHomeItem.plid
			);
			const currentHomeItemIndex = currentHomeItemColumn.indexOf(
				currentHomeItem
			);

			nextLayoutColumns = setIn(
				nextLayoutColumns,
				[
					currentHomeItemColumnIndex,
					currentHomeItemIndex,
					'homePage'
				],
				false
			);
		}
	}

	return nextLayoutColumns;
}

/**
 * Set the item with the given plid as the active item of its column
 * and returns a new layoutColumns
 * @param {object} layoutColumns
 * @param {string} itemPlid
 * @return {object}
 * @review
 */
function setActiveItem(layoutColumns, itemPlid) {
	const columnIndex = getItemColumnIndex(layoutColumns, itemPlid);

	const column = layoutColumns[columnIndex];
	const currentActiveItemIndex = column.indexOf(
		getColumnActiveItem(layoutColumns, columnIndex)
	);
	const newActiveItemIndex = column.indexOf(
		getItem(layoutColumns, itemPlid)
	);

	let nextLayoutColumns = setIn(
		layoutColumns,
		[
			columnIndex,
			currentActiveItemIndex,
			'active'
		],
		false
	);

	nextLayoutColumns = setIn(
		nextLayoutColumns,
		[
			columnIndex,
			newActiveItemIndex,
			'active'
		],
		true
	);

	return nextLayoutColumns;
}

export {
	appendItemToColumn,
	clearFollowingColumns,
	clearPath,
	deleteEmptyColumns,
	moveItemInside,
	removeItem,
	setActiveItem,
	setHomePage
};