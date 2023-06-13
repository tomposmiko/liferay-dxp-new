import {ADD_ROW, MOVE_ROW, REMOVE_ROW, UPDATE_ROW_COLUMNS, UPDATE_ROW_COLUMNS_NUMBER, UPDATE_ROW_CONFIG} from '../actions/actions.es';
import {add, addRow, remove, setIn, updateIn, updateWidgets} from '../utils/FragmentsEditorUpdateUtils.es';
import {containsFragmentEntryLinkId} from '../utils/LayoutDataList.es';
import {getDropRowPosition, getRowFragmentEntryLinkIds, getRowIndex} from '../utils/FragmentsEditorGetUtils.es';
import {MAX_COLUMNS} from '../utils/rowConstants';
import {removeFragmentEntryLinks, updatePageEditorLayoutData} from '../utils/FragmentsEditorFetchUtils.es';

/**
 * @param {object} state
 * @param {object} action
 * @param {[]} action.layoutColumns
 * @param {string} action.type
 * @return {object}
 * @review
 */
function addRowReducer(state, action) {
	let nextState = state;

	return new Promise(
		resolve => {
			if (action.type === ADD_ROW) {
				const position = getDropRowPosition(
					nextState.layoutData.structure,
					nextState.dropTargetItemId,
					nextState.dropTargetBorder
				);

				const nextData = addRow(
					action.layoutColumns,
					nextState.layoutData,
					position
				);

				updatePageEditorLayoutData(
					nextData,
					nextState.segmentsExperienceId
				).then(
					() => {
						nextState = setIn(nextState, ['layoutData'], nextData);

						resolve(nextState);
					}
				).catch(
					() => {
						resolve(nextState);
					}
				);
			}
			else {
				resolve(nextState);
			}
		}
	);
}

/**
 * @param {object} state
 * @param {object} action
 * @param {string} action.rowId
 * @param {string} action.targetBorder
 * @param {string} action.targetItemId
 * @param {object} action.type
 * @return {object}
 * @review
 */
function moveRowReducer(state, action) {
	let nextState = state;

	return new Promise(
		resolve => {
			if (action.type === MOVE_ROW) {
				const nextData = _moveRow(
					action.rowId,
					nextState.layoutData,
					action.targetItemId,
					action.targetBorder
				);

				updatePageEditorLayoutData(
					nextData,
					nextState.segmentsExperienceId
				).then(
					() => {
						nextState = setIn(nextState, ['layoutData'], nextData);

						resolve(nextState);
					}
				).catch(
					() => {
						resolve(nextState);
					}
				);
			}
			else {
				resolve(nextState);
			}
		}
	);
}

/**
 * @param {object} state
 * @param {object} action
 * @param {string} action.rowId
 * @param {string} action.type
 * @return {object}
 * @review
 */
function removeRowReducer(state, action) {
	let nextState = state;

	return new Promise(
		resolve => {
			if (action.type === REMOVE_ROW) {
				nextState = updateIn(
					nextState,
					['layoutData', 'structure'],
					structure => remove(
						structure,
						getRowIndex(structure, action.rowId)
					),
					[]
				);

				const fragmentEntryLinkIds = getRowFragmentEntryLinkIds(
					state.layoutData.structure[
						getRowIndex(state.layoutData.structure, action.rowId)
					]
				).filter(
					fragmentEntryLinkId => !containsFragmentEntryLinkId(
						nextState.layoutDataList,
						fragmentEntryLinkId,
						nextState.segmentsExperienceId || nextState.defaultSegmentsExperienceId
					)
				);

				fragmentEntryLinkIds.forEach(
					fragmentEntryLinkId => {
						nextState = updateWidgets(
							nextState,
							fragmentEntryLinkId
						);
					}
				);

				updatePageEditorLayoutData(
					nextState.layoutData,
					nextState.segmentsExperienceId
				).then(
					() => removeFragmentEntryLinks(
						nextState.layoutData,
						fragmentEntryLinkIds,
						nextState.segmentsExperienceId
					)
				).then(
					() => {
						resolve(nextState);
					}
				).catch(
					() => {
						resolve(state);
					}
				);
			}
			else {
				resolve(state);
			}
		}
	);
}

/**
 * @param {object} state
 * @param {object} action
 * @param {[]} action.columns
 * @param {string} action.rowId
 * @param {string} action.type
 * @return {object}
 * @review
 */
const updateRowColumnsReducer = (state, action) => new Promise(
	resolve => {
		let nextState = state;

		if (action.type === UPDATE_ROW_COLUMNS) {
			const rowIndex = getRowIndex(
				nextState.layoutData.structure,
				action.rowId
			);

			if (rowIndex === -1) {
				resolve(nextState);
			}
			else {
				const nextData = setIn(
					nextState.layoutData,
					[
						'structure',
						rowIndex.toString(),
						'columns'
					],
					action.columns
				);

				updatePageEditorLayoutData(
					nextData,
					nextState.segmentsExperienceId
				).then(
					() => {
						nextState = setIn(
							nextState,
							['layoutData'],
							nextData
						);

						resolve(nextState);
					}
				).catch(
					() => {
						resolve(nextState);
					}
				);
			}
		}
		else {
			resolve(nextState);
		}
	}
);

/**
 * @param {!object} state
 * @param {object} action
 * @param {number} action.numberOfColumns
 * @param {string} action.rowId
 * @param {string} action.type
 * @return {object}
 * @review
 */
function updateRowColumnsNumberReducer(state, action) {
	let nextState = state;

	return new Promise(
		resolve => {
			if (action.type === UPDATE_ROW_COLUMNS_NUMBER) {

				let fragmentEntryLinkIdsToRemove = [];
				let nextData;

				const numberOfColumns = action.numberOfColumns;

				const columnsSize = Math.floor(MAX_COLUMNS / numberOfColumns);

				const rowIndex = getRowIndex(
					nextState.layoutData.structure,
					action.rowId
				);

				let columns = nextState.layoutData.structure[rowIndex].columns;

				if (numberOfColumns > columns.length) {
					nextData = _addColumns(
						nextState.layoutData,
						rowIndex,
						numberOfColumns,
						columnsSize
					);
				}
				else {
					let fragmentEntryLinkIdsToRemove = getRowFragmentEntryLinkIds(
						{
							columns: columns.slice(
								numberOfColumns - columns.length
							)
						}
					);

					fragmentEntryLinkIdsToRemove.forEach(
						fragmentEntryLinkId => {
							nextState = updateWidgets(
								nextState,
								fragmentEntryLinkId
							);
						}
					);

					nextData = _removeColumns(
						nextState.layoutData,
						rowIndex,
						numberOfColumns,
						columnsSize
					);
				}

				updatePageEditorLayoutData(
					nextData,
					nextState.segmentsExperienceId
				).then(
					() => removeFragmentEntryLinks(
						nextData,
						fragmentEntryLinkIdsToRemove,
						nextState.segmentsExperienceId
					)
				).then(
					() => {
						nextState = setIn(nextState, ['layoutData'], nextData);

						resolve(nextState);
					}
				).catch(
					() => {
						resolve(state);
					}
				);
			}
			else {
				resolve(state);
			}
		}
	);
}

/**
 * @param {object} state
 * @param {object} action
 * @param {object} action.config
 * @param {string} action.rowId
 * @param {string} action.type
 * @return {object}
 */
const updateRowConfigReducer = (state, action) => new Promise(
	resolve => {
		let nextState = state;

		if (action.type === UPDATE_ROW_CONFIG) {
			const rowIndex = getRowIndex(
				nextState.layoutData.structure,
				action.rowId
			);

			if (rowIndex === -1) {
				resolve(nextState);
			}
			else {
				Object.entries(action.config).forEach(
					entry => {
						const [key, value] = entry;

						const configPath = [
							'layoutData',
							'structure',
							rowIndex,
							'config',
							key
						];

						nextState = setIn(
							nextState,
							configPath,
							value
						);
					}
				);

				updatePageEditorLayoutData(
					nextState.layoutData,
					nextState.segmentsExperienceId
				).then(
					() => {
						resolve(nextState);
					}
				).catch(
					() => {
						resolve(state);
					}
				);
			}
		}
		else {
			resolve(nextState);
		}
	}
);

/**
 * Returns a new layoutData with the given columns inserted in the specified
 * row with the specified size and resizes the rest of columns to the
 * same size.
 *
 * @param {object} layoutData
 * @param {number} rowIndex
 * @param {number} numberOfColumns
 * @param {number} columnsSize
 * @return {object}
 */
function _addColumns(layoutData, rowIndex, numberOfColumns, columnsSize) {
	let nextColumnId = layoutData.nextColumnId || 0;

	let nextData = updateIn(
		layoutData,
		['structure', rowIndex, 'columns'],
		columns => {
			columns.forEach(
				(column, index) => {
					column.size = _getColumnSize(numberOfColumns, columnsSize, index);
				}
			);

			const numberOfNewColumns = numberOfColumns - columns.length;
			const numberOfOldColumns = columns.length;

			for (let i = 0; i < numberOfNewColumns; i++) {
				columns.push(
					{
						columnId: `${nextColumnId}`,
						fragmentEntryLinkIds: [],
						size: _getColumnSize(numberOfColumns, columnsSize, (i + numberOfOldColumns))
					}
				);

				nextColumnId += 1;
			}

			return columns;
		}
	);

	nextData = setIn(layoutData, ['nextColumnId'], nextColumnId);

	return nextData;
}

/**
 * Returns the new size of a column based on the total number of columns of a
 * grid, the general size and its position in the grid.
 *
 * @param {number} numberOfColumns
 * @param {number} columnsSize
 * @param {number} columnIndex
 * @return {string}
 */
function _getColumnSize(numberOfColumns, columnsSize, columnIndex) {
	let newColumnSize = columnsSize;

	const middleColumnPosition = Math.ceil(numberOfColumns / 2) - 1;

	if (middleColumnPosition === columnIndex) {
		newColumnSize = MAX_COLUMNS - ((numberOfColumns - 1) * columnsSize);
	}

	return newColumnSize.toString();
}

/**
 * Returns a new layoutData without the columns out of range in the specified
 * row and resizes the rest of columns to the specified size.
 *
 * @param {object} layoutData
 * @param {number} rowIndex
 * @param {number} numberOfColumns
 * @param {number} columnsSize
 * @return {object}
 */
function _removeColumns(layoutData, rowIndex, numberOfColumns, columnsSize) {
	let nextData = updateIn(
		layoutData,
		['structure', rowIndex, 'columns'],
		columns => {
			columns = columns.slice(0, numberOfColumns);

			columns.forEach(
				(column, index) => {
					column.size = _getColumnSize(numberOfColumns, columnsSize, index);
				}
			);

			return columns;
		}
	);

	return nextData;
}

/**
 * Returns a new layoutData with the given row moved to the position
 * calculated with targetItemId and targetItemBorder
 * @param {string} rowId
 * @param {object} layoutData
 * @param {string} targetItemId
 * @param {string} targetItemBorder
 * @private
 * @return {object}
 * @review
 */
function _moveRow(rowId, layoutData, targetItemId, targetItemBorder) {
	const index = getRowIndex(layoutData.structure, rowId);
	const row = layoutData.structure[index];

	let nextStructure = remove(layoutData.structure, index);

	const position = getDropRowPosition(
		nextStructure,
		targetItemId,
		targetItemBorder
	);

	nextStructure = add(nextStructure, row, position);

	return setIn(layoutData, ['structure'], nextStructure);
}

export {
	addRowReducer,
	moveRowReducer,
	removeRowReducer,
	updateRowColumnsReducer,
	updateRowColumnsNumberReducer,
	updateRowConfigReducer
};