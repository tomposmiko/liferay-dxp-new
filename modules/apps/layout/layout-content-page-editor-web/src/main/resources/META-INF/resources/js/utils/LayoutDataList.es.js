import {getFragmentRowIndex} from './FragmentsEditorGetUtils.es';

/**
 * Tells if a fragmentEntryLink is referenced in any (but the current one)
 * LayoutData inside LayoutDataList
 * @param {Array<{ segmentsExperienceId: string, layoutData: {structure: Array} }>} LayoutDataList
 * @param {string} fragmentEntryLinkId
 * @param {string} [skipSegmentsExperienceId] - allows to skip searching in layoutData by segmentsExperienceId
 * @returns {boolean}
 */
function containsFragmentEntryLinkId(
	LayoutDataList,
	fragmentEntryLinkId,
	skipSegmentsExperienceId
) {
	return LayoutDataList
		.filter(
			function _avoidCurrentExperienceLayoutDataItem(LayoutDataItem) {
				return LayoutDataItem.segmentsExperienceId !== skipSegmentsExperienceId;
			}
		).some(
			function _getFragmentRowIndexWrapper(LayoutDataItem) {
				const index = getFragmentRowIndex(
					LayoutDataItem.layoutData.structure,
					fragmentEntryLinkId
				);
				return index !== -1;
			}
		);
}

/**
 * Utility to get a layoutData object
 *
 * @returns {object}
 */
function getEmptyLayoutData() {
	return {
		nextColumnId: 0,
		nextRowId: 0,
		structure: []
	};
}

export {
	containsFragmentEntryLinkId,
	getEmptyLayoutData
};