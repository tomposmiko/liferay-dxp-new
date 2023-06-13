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

import '@testing-library/jest-dom/extend-expect';
import {cleanup, fireEvent, render} from '@testing-library/react';
import React from 'react';

import LocalizedInput from '../../../../src/main/resources/META-INF/resources/js/components/title_editor/LocalizedInput.es';

const LOCALIZED_DROPDOWN_BUTTON = 'localized-dropdown-button';

const LOCALIZED_MAIN_INPUT_ID = 'localized-main-input';

const PRE_EXISTING_VALUE = 'Testing default value';

const PRE_EXISTING_VALUE_ALT = 'Testing alternative default value';

describe('LocalizedInput', () => {
	describe('with minimal configuration', () => {
		afterEach(cleanup);

		it('renders', () => {
			const {asFragment} = setUpComponent();
			expect(asFragment()).toMatchSnapshot();
		});

		it('has main input with empty value', () => {
			const {getByTestId} = setUpComponent();
			const mainInput = getByTestId(LOCALIZED_MAIN_INPUT_ID);

			expect(mainInput.value).toBeFalsy();
		});

		function setUpComponent() {
			const {asFragment, getByTestId} = render(
				<LocalizedInput
					availableLanguages={{
						en_US: '',
						es_ES: '',
					}}
					initialLanguageId="en_US"
				/>
			);

			return {asFragment, getByTestId};
		}
	});

	describe('rendering with pre-existing value', () => {
		afterEach(cleanup);

		it('renders', () => {
			const {asFragment} = setUpComponent();
			expect(asFragment()).toMatchSnapshot();
		});

		it('has main input with value', () => {
			const {getByTestId} = setUpComponent();
			const mainInput = getByTestId(LOCALIZED_MAIN_INPUT_ID);
			expect(mainInput.value).toBe(PRE_EXISTING_VALUE);
		});

		it('has dropdown with options', () => {
			const {
				asFragment,
				getAllByText,
				getByTestId,
				getByText,
			} = setUpComponent();
			const dropdownButton = getByTestId(LOCALIZED_DROPDOWN_BUTTON);
			fireEvent.click(dropdownButton);

			expect(
				getByText('translated').closest('span.taglib-text-icon')
					.textContent
			).toBe('es-EStranslated');

			expect(
				getByText('default').closest('span.taglib-text-icon')
					.textContent
			).toBe('en-USdefault');

			expect(getAllByText('not-translated').length).toBe(11);

			expect(asFragment()).toMatchSnapshot();
		});

		it('switches main input value on language selection', () => {
			const {getByTestId, getByText} = setUpComponent();
			const dropdownButton = getByTestId(LOCALIZED_DROPDOWN_BUTTON);
			const mainInput = getByTestId(LOCALIZED_MAIN_INPUT_ID);

			expect(mainInput.value).toBe(PRE_EXISTING_VALUE);

			fireEvent.click(dropdownButton);

			const spanishSelector = getByText('es-ES');
			fireEvent.click(spanishSelector);

			expect(mainInput.value).toBe(PRE_EXISTING_VALUE_ALT);
		});

		function setUpComponent() {
			const testHelpers = render(
				<LocalizedInput
					availableLanguages={{
						ar_SA: 'Arabic (Saudi Arabia)',
						ca_ES: 'Catalan (Spain)',
						de_DE: 'German (Germany)',
						en_US: 'English (United States)',
						es_ES: 'Spanish (Spain)',
						fi_FI: 'Finnish (Finland)',
						fr_FR: 'French (France)',
						hu_HU: 'Hungarian (Hungary)',
						ja_JP: 'Japanese (Japan)',
						nl_NL: 'Dutch (Netherlands)',
						pt_BR: 'Portuguese (Brazil)',
						sv_SE: 'Swedish (Sweden)',
						zh_CN: 'Chinese (China)',
					}}
					defaultLang="en_US"
					initialLanguageId="en_US"
					initialValues={{
						en_US: PRE_EXISTING_VALUE,
						es_ES: PRE_EXISTING_VALUE_ALT,
					}}
				/>
			);

			return testHelpers;
		}
	});
});
