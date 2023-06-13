/**
 * @type {object}
 */
let _store;

/**
 * @param {object} body
 * @param {string} portletNamespace
 * @param {FormData} [_formData=new FormData()]
 * @return {FormData}
 * @review
 */
function _getFormData(body, portletNamespace, _formData = new FormData()) {
	Object.entries(body).forEach(
		([key, value]) => {
			_formData.append(`${portletNamespace}${key}`, value);
		}
	);

	return _formData;
}

/**
 * @param {string} url
 * @param {object} [body={}]
 * @private
 * @return {Promise<Response>}
 * @review
 */
function _fetch(url, body = {}) {
	return fetch(
		url,
		{
			body: _getFormData(body, _store.getState().portletNamespace),
			credentials: 'include',
			method: 'POST'
		}
	);
}

/**
 * @param {string} segmentsExperienceId
 * @param {Array<string>} [fragmentEntryLinkIds=[]]
 * @return {Promise<Response>}
 */
function removeExperience(segmentsExperienceId, fragmentEntryLinkIds = []) {
	const state = _store.getState();

	const body = {
		segmentsExperienceId
	};

	if (fragmentEntryLinkIds && fragmentEntryLinkIds.length) {
		body.fragmentEntryLinkIds = JSON.stringify(fragmentEntryLinkIds);
	}

	return _fetch(state.deleteSegmentsExperienceURL, body);
}

/**
 * @param {{}} layoutData
 * @param {string[]} fragmentEntryLinkIds
 * @param {string} segmentsExperienceId
 * @return {Promise<Response[]>}
 */
function removeFragmentEntryLinks(
	layoutData,
	fragmentEntryLinkIds,
	segmentsExperienceId
) {
	const state = _store.getState();

	return Promise.all(
		fragmentEntryLinkIds.map(
			fragmentEntryLinkId => _fetch(
				state.deleteFragmentEntryLinkURL,
				{
					classNameId: state.classNameId,
					classPK: state.classPK,
					data: JSON.stringify(layoutData),
					fragmentEntryLinkId,
					segmentsExperienceId
				}
			)
		)
	);
}

/**
 * Sets the store
 * @param {object} store
 */
function setStore(store) {
	_store = store;
}

/**
 * @param {string} fragmentEntryLinkId
 * @param {object} editableValues
 * @return {Promise<Response>}
 */
function updateEditableValues(fragmentEntryLinkId, editableValues) {
	const state = _store.getState();

	return _fetch(
		state.editFragmentEntryLinkURL,
		{
			editableValues: JSON.stringify(editableValues),
			fragmentEntryLinkId
		}
	);
}

/**
 * @param {Object} layoutData
 * @param {string} segmentsExperienceId
 * @return {Promise<Response>}
 */
function updatePageEditorLayoutData(layoutData, segmentsExperienceId) {
	const state = _store.getState();

	return _fetch(
		state.updateLayoutPageTemplateDataURL,
		{
			classNameId: state.classNameId,
			classPK: state.classPK,
			data: JSON.stringify(layoutData),
			segmentsExperienceId
		}
	);
}

export {
	removeExperience,
	removeFragmentEntryLinks,
	setStore,
	updateEditableValues,
	updatePageEditorLayoutData
};