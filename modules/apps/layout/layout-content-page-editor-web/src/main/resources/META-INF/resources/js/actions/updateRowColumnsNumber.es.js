import {disableSavingChangesStatusAction, enableSavingChangesStatusAction, updateLastSaveDateAction} from './saveChanges.es';
import {getRowFragmentEntryLinkIds, getRowIndex} from '../utils/FragmentsEditorGetUtils.es';
import {MAX_COLUMNS} from '../utils/rowConstants';
import {removeFragmentEntryLinks, updatePageEditorLayoutData} from '../utils/FragmentsEditorFetchUtils.es';
import {setIn, updateIn} from '../utils/FragmentsEditorUpdateUtils.es';
import {UPDATE_ROW_COLUMNS_NUMBER_ERROR, UPDATE_ROW_COLUMNS_NUMBER_LOADING, UPDATE_ROW_COLUMNS_NUMBER_SUCCESS} from './actions.es';

/**
 * @param {number} numberOfColumns
 * @param {string} rowId
 * @return {function}
 * @review
 */
function updateRowColumnsNumberAction(numberOfColumns, rowId) {
	return function(dispatch, getState) {
		const state = getState();

		const columnsSize = Math.floor(MAX_COLUMNS / numberOfColumns);
		const rowIndex = getRowIndex(
			state.layoutData.structure,
			rowId
		);

		let columns = state.layoutData.structure[rowIndex].columns;
		let nextData;

		if (numberOfColumns > columns.length) {
			nextData = _addColumns(
				state.layoutData,
				rowIndex,
				numberOfColumns,
				columnsSize
			);
		}
		else {
			nextData = _removeColumns(
				state.layoutData,
				rowIndex,
				numberOfColumns,
				columnsSize
			);
		}

		let fragmentEntryLinkIdsToRemove = getRowFragmentEntryLinkIds(
			{
				columns: columns.slice(
					numberOfColumns - columns.length
				)
			}
		);

		dispatch(updateRowColumnsNumberLoadingAction());
		dispatch(enableSavingChangesStatusAction());

		updatePageEditorLayoutData(
			nextData,
			state.segmentsExperienceId
		).then(
			() => removeFragmentEntryLinks(
				nextData,
				fragmentEntryLinkIdsToRemove,
				state.segmentsExperienceId
			)
		).then(
			() => {
				dispatch(
					updateRowColumnsNumberSuccessAction(
						fragmentEntryLinkIdsToRemove,
						nextData
					)
				);

				dispatch(updateLastSaveDateAction());
				dispatch(disableSavingChangesStatusAction());
			}
		).catch(
			() => {
				dispatch(updateRowColumnsNumberErrorAction());
				dispatch(disableSavingChangesStatusAction());
			}
		);
	};
}

/**
 * @return {object}
 * @review
 */
function updateRowColumnsNumberErrorAction() {
	return {
		type: UPDATE_ROW_COLUMNS_NUMBER_ERROR
	};
}

/**
 * @return {object}
 * @review
 */
function updateRowColumnsNumberLoadingAction() {
	return {
		type: UPDATE_ROW_COLUMNS_NUMBER_LOADING
	};
}

/**
 * @param {Array} fragmentEntryLinkIdsToRemove
 * @param {object} layoutData
 * @return {object}
 * @review
 */
function updateRowColumnsNumberSuccessAction(
	fragmentEntryLinkIdsToRemove,
	layoutData
) {
	return {
		fragmentEntryLinkIdsToRemove,
		layoutData,
		type: UPDATE_ROW_COLUMNS_NUMBER_SUCCESS
	};
}

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
			const newColumns = columns.map(
				(column, index) => setIn(
					column,
					['size'],
					_getColumnSize(numberOfColumns, columnsSize, index)
				)
			);

			const numberOfNewColumns = numberOfColumns - columns.length;
			const numberOfOldColumns = columns.length;

			for (let i = 0; i < numberOfNewColumns; i++) {
				newColumns.push(
					{
						columnId: `${nextColumnId}`,
						fragmentEntryLinkIds: [],
						size: _getColumnSize(numberOfColumns, columnsSize, (i + numberOfOldColumns))
					}
				);

				nextColumnId += 1;
			}

			return newColumns;
		}
	);

	nextData = setIn(nextData, ['nextColumnId'], nextColumnId);

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
			let newColumns = columns.slice(0, numberOfColumns);

			newColumns = newColumns.map(
				(column, index) => setIn(
					column,
					['size'],
					_getColumnSize(numberOfColumns, columnsSize, index)
				)
			);

			return newColumns;
		}
	);

	return nextData;
}

export {
	updateRowColumnsNumberAction
};