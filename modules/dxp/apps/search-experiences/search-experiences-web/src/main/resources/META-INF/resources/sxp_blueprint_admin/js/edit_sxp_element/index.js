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

import React, {useEffect, useState} from 'react';

import useClipboardJS from '../hooks/useClipboardJS';
import ErrorBoundary from '../shared/ErrorBoundary';
import ThemeContext from '../shared/ThemeContext';
import {COPY_BUTTON_CSS_CLASS} from '../utils/constants';
import fetchData from '../utils/fetch/fetch_data';
import formatLocaleWithUnderscores from '../utils/language/format_locale_with_underscores';
import renameKeys from '../utils/language/rename_keys';
import transformLocale from '../utils/language/transform_locale';
import {openInitialSuccessToast} from '../utils/toasts';
import EditSXPElementForm from './EditSXPElementForm';

/**
 * Gets the formatted description_i18n object from the sxp element response.
 * Also creates an object using the default locale and `description` field if
 * the description_i18n object is undefined. If the description_i18n object is
 * {}, it will return object with placeholder: {'en_US': ''}.
 *
 * The expected return format is: {'en_US': 'description'}
 * @param {object} sxpElementResponse The response object from the GET
 * 	sxp-elements
 * @param {string} defaultLocale The default locale
 * @returns {object}
 */
const getDescriptionI18n = (sxpElementResponse, defaultLocale) => {
	let descriptionObject = renameKeys(
		sxpElementResponse.description_i18n,
		transformLocale
	);

	if (Object.keys(descriptionObject).length === 0) {
		descriptionObject = {[defaultLocale]: ''};
	}

	return renameKeys(descriptionObject, formatLocaleWithUnderscores);
};

/**
 * See `getDescriptionI18n`.
 * @param {object} sxpElementResponse The response object from the GET
 * 	sxp-elements
 * @param {string} defaultLocale The default locale
 * @returns {object}
 */
const getTitleI18n = (sxpElementResponse) => {
	const titleObject = renameKeys(
		sxpElementResponse.title_i18n,
		transformLocale
	);

	return renameKeys(titleObject, formatLocaleWithUnderscores);
};

/**
 * Converts the GET sxp-elements response to match the format of the exported
 * sxp element file. This is so an exported element can be copy/pasted into the
 * element JSON editor.
 * https://github.com/liferay/liferay-portal/blob/e9255c529cbb97d494f8331ec4527b271bc412ae/modules/dxp/apps/search-experiences/search-experiences-rest-impl/src/main/java/com/liferay/search/experiences/rest/internal/resource/v1_0/SXPElementResourceImpl.java#L94-L108
 */
const transformToSXPElementExportFormat = (
	sxpElementResponse,
	defaultLocale
) => {
	return {
		description_i18n: getDescriptionI18n(sxpElementResponse, defaultLocale),
		elementDefinition: sxpElementResponse.elementDefinition,
		title_i18n: getTitleI18n(sxpElementResponse),
		type: sxpElementResponse.type,
	};
};

export default function ({
	defaultLocale,
	learnMessages,
	locale,
	namespace,
	redirectURL,
	sxpElementId,
}) {
	const [predefinedVariables, setPredefinedVariables] = useState(null);
	const [sxpElementResponse, setSXPElementResponse] = useState(null);

	useClipboardJS('.' + COPY_BUTTON_CSS_CLASS);

	useEffect(() => {
		openInitialSuccessToast();

		fetchData(
			`/o/search-experiences-rest/v1.0/sxp-elements/${sxpElementId}`
		)
			.then((responseContent) => setSXPElementResponse(responseContent))
			.catch(() => setSXPElementResponse({}));

		fetchData(
			'/o/search-experiences-rest/v1.0/sxp-parameter-contributor-definitions'
		)
			.then((responseContent) =>
				setPredefinedVariables(responseContent.items)
			)
			.catch(() => setPredefinedVariables([]));
	}, []); //eslint-disable-line

	if (!sxpElementResponse || !predefinedVariables) {
		return null;
	}

	return (
		<ThemeContext.Provider
			value={{
				availableLanguages: Liferay.Language.available,
				defaultLocale,
				learnMessages,
				locale,
				namespace,
				redirectURL,
			}}
		>
			<div className="edit-sxp-element-root">
				<ErrorBoundary>
					<EditSXPElementForm
						initialDescription={getDescriptionI18n(
							sxpElementResponse,
							defaultLocale
						)}
						initialElementJSONEditorValue={transformToSXPElementExportFormat(
							sxpElementResponse,
							defaultLocale
						)}
						initialTitle={getTitleI18n(sxpElementResponse)}
						predefinedVariables={predefinedVariables}
						readOnly={sxpElementResponse.readOnly}
						sxpElementId={sxpElementId}
						type={sxpElementResponse.type}
					/>
				</ErrorBoundary>
			</div>
		</ThemeContext.Provider>
	);
}
