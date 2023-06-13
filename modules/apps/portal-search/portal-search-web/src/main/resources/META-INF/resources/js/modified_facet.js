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

AUI.add(
	'liferay-search-modified-facet',
	(A) => {
		const DEFAULTS_FORM_VALIDATOR = A.config.FormValidator;

		const FacetUtil = Liferay.Search.FacetUtil;
		const Util = Liferay.Util;

		const ModifiedFacetFilter = function (config) {
			const instance = this;

			instance.form = config.form;
			instance.fromInputDatePicker = config.fromInputDatePicker;
			instance.fromInputName = config.fromInputName;
			instance.namespace = config.namespace;
			instance.searchCustomRangeButton = config.searchCustomRangeButton;
			instance.toInputDatePicker = config.toInputDatePicker;
			instance.toInputName = config.toInputName;

			instance.fromInput = A.one('#' + instance.fromInputName);
			instance.toInput = A.one('#' + instance.toInputName);

			instance._initializeFormValidator();

			if (instance.searchCustomRangeButton) {
				instance.searchCustomRangeButton.on(
					'click',
					A.bind(instance.filter, instance)
				);
			}

			if (instance.fromInput) {
				instance.fromInput.on('keydown', instance._onDateInputKeyDown);
			}

			if (instance.toInput) {
				instance.toInput.on('keydown', instance._onDateInputKeyDown);
			}
		};

		const ModifiedFacetFilterUtil = {
			clearSelections() {
				const param = this.getParameterName();
				const paramFrom = param + 'From';
				const paramTo = param + 'To';

				let parameterArray = document.location.search
					.substr(1)
					.split('&');

				parameterArray = FacetUtil.removeURLParameters(
					param,
					parameterArray
				);

				parameterArray = FacetUtil.removeURLParameters(
					paramFrom,
					parameterArray
				);

				parameterArray = FacetUtil.removeURLParameters(
					paramTo,
					parameterArray
				);

				this.submitSearch(parameterArray.join('&'));
			},

			getParameterName() {
				return 'modified';
			},

			submitSearch(parameterString) {
				document.location.search = parameterString;
			},

			/**
			 * Formats a date to 'YYYY-MM-DD' format.
			 * @param {Date} date The date to format.
			 * @returns {String} The date string.
			 */
			toLocaleDateStringFormatted(date) {
				const localDate = new Date(date);

				localDate.setMinutes(
					date.getMinutes() - date.getTimezoneOffset()
				);

				return localDate.toISOString().split('T')[0];
			},
		};

		A.mix(ModifiedFacetFilter.prototype, {
			_initializeFormValidator() {
				const instance = this;

				const dateRangeRuleName = instance.namespace + 'dateRange';

				A.mix(
					DEFAULTS_FORM_VALIDATOR.STRINGS,
					{
						[dateRangeRuleName]: Liferay.Language.get(
							'search-custom-range-invalid-date-range'
						),
					},
					true
				);

				A.mix(
					DEFAULTS_FORM_VALIDATOR.RULES,
					{
						[dateRangeRuleName]() {
							return A.Date.isGreaterOrEqual(
								instance.toInputDatePicker.getDate(),
								instance.fromInputDatePicker.getDate()
							);
						},
					},
					true
				);

				const customRangeValidator = new A.FormValidator({
					boundingBox: instance.form,
					fieldContainer: 'div',
					on: {
						errorField() {
							Util.toggleDisabled(
								instance.searchCustomRangeButton,
								true
							);
						},
						validField() {
							Util.toggleDisabled(
								instance.searchCustomRangeButton,
								false
							);
						},
					},
					rules: {
						[instance.fromInputName]: {
							[dateRangeRuleName]: true,
						},
						[instance.toInputName]: {
							[dateRangeRuleName]: true,
						},
					},
				});

				const onRangeSelectionChange = function () {
					customRangeValidator.validate();
				};

				if (instance.fromInputDatePicker) {
					instance.fromInputDatePicker.on(
						'selectionChange',
						onRangeSelectionChange
					);
				}

				if (instance.toInputDatePicker) {
					instance.toInputDatePicker.on(
						'selectionChange',
						onRangeSelectionChange
					);
				}
			},

			_onDateInputKeyDown(event) {
				if (!event.isKey('TAB')) {
					event.preventDefault();
				}
			},

			filter() {
				const instance = this;

				const fromDate = instance.fromInputDatePicker.getDate();

				const toDate = instance.toInputDatePicker.getDate();

				const modifiedFromParameter = ModifiedFacetFilterUtil.toLocaleDateStringFormatted(
					fromDate
				);

				const modifiedToParameter = ModifiedFacetFilterUtil.toLocaleDateStringFormatted(
					toDate
				);

				const param = ModifiedFacetFilterUtil.getParameterName();
				const paramFrom = param + 'From';
				const paramTo = param + 'To';

				let parameterArray = document.location.search
					.substr(1)
					.split('&');

				parameterArray = FacetUtil.removeURLParameters(
					param,
					parameterArray
				);

				parameterArray = FacetUtil.removeURLParameters(
					paramFrom,
					parameterArray
				);

				parameterArray = FacetUtil.removeURLParameters(
					paramTo,
					parameterArray
				);

				const startParameterNameElement = document.getElementById(
					instance.namespace + 'start-parameter-name'
				);

				if (startParameterNameElement) {
					parameterArray = FacetUtil.removeURLParameters(
						startParameterNameElement.value,
						parameterArray
					);
				}

				parameterArray = FacetUtil.addURLParameter(
					paramFrom,
					modifiedFromParameter,
					parameterArray
				);

				parameterArray = FacetUtil.addURLParameter(
					paramTo,
					modifiedToParameter,
					parameterArray
				);

				ModifiedFacetFilterUtil.submitSearch(parameterArray.join('&'));
			},
		});

		Liferay.namespace('Search').ModifiedFacetFilter = ModifiedFacetFilter;

		Liferay.namespace(
			'Search'
		).ModifiedFacetFilterUtil = ModifiedFacetFilterUtil;
	},
	'',
	{
		requires: ['aui-form-validator', 'liferay-search-facet-util'],
	}
);
