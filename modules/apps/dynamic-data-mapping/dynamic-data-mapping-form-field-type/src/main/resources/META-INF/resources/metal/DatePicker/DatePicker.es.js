import '../FieldBase/FieldBase.es';
import './DatePickerRegister.soy.js';
import 'clay-button';
import 'clay-icon';
import * as Helpers from './Helpers.es';
import Component from 'metal-component';
import dom from 'metal-dom';
import moment from 'moment';
import Soy from 'metal-soy';
import templates from './DatePicker.soy.js';
import vanillaTextMask from 'vanilla-text-mask';
import {Config} from 'metal-state';
import {EventHandler} from 'metal-events';

/**
 * Metal DatePicker component.
 * @extends Component
 */

class DatePicker extends Component {

	/**
	 * Returns the default value for the "dateFormat" state.
	 * It converts the format from "%d/%m/%Y" to "DD/MM/YYYY".
	 * @protected
	 */

	_dateFormatValueFn() {
		const dateFormat = this.getDateFormat();

		return dateFormat.split(this._dateDelimiter).map(
			item => {
				let currentFormat;

				if (item === '%Y') {
					currentFormat = 'YYYY';
				}
				else if (item === '%m') {
					currentFormat = 'MM';
				}
				else {
					currentFormat = 'DD';
				}

				return currentFormat;
			}
		).join(this._dateDelimiter);
	}

	/**
	 * Handles the click on element of the day
	 * @param {!Event} event
	 * @protected
	 */

	_handleDayClicked(event) {
		const ariaLabel = event.target.getAttribute('ariaLabel');
		const selectedDate = Helpers.formatDate(ariaLabel);

		if (selectedDate.getMonth() > this.currentMonth.getMonth()) {
			this._handleNextMonth();
		}
		else if (selectedDate.getMonth() < this.currentMonth.getMonth()) {
			this._handlePreviousMonth();
		}

		this._daySelected = ariaLabel;
		this.expanded = false;
		this.value = selectedDate;

		this._handleFieldEdited();
	}

	/**
	 * Handles the click of the document to hide the datepicker.
	 * @param {!Event} event
	 * @protected
	 */

	_handleDocClick(event) {
		if (this.element.contains(event.target)) {
			return;
		}
		this.expanded = false;
	}

	/**
	 * Handles the click on the dot button.
	 * @protected
	 */

	_handleDotClicked() {
		this._daySelected = this._getCurrentDate();
		this.currentMonth = Helpers.formatDate(this._daySelected);
		this.value = this.currentMonth;
		this._handleFieldEdited();
	}

	_getCurrentDate() {
		const today = new Date();

		const day = today.getDate();
		const month = today.getMonth();
		const year = today.getFullYear();

		return `${year} ${month} ${day}`;
	}

	/**
	 * Handles the blur on the input element.
	 * @param {!Event} event
	 * @protected
	 */

	_handleInputBlurred({target}) {
		if (!this.isEmptyValue(target.value)) {
			this.value = Helpers.formatDate(this._daySelected);
		}
	}

	/**
	 * Handles the click on the input element.
	 * @param {!Event} event
	 * @protected
	 */

	_handleInputFocused() {
		this.expanded = true;
	}

	/**
	 * Handles the change of the year and month of the header
	 * @param {!Event} event
	 * @protected
	 */

	_handleNavigateChange(event) {
		const {month, year} = event.target.form;

		this.currentMonth = new Date(year.value, month.value);
	}

	/**
	 * Handles the next month button
	 * @protected
	 */

	_handleNextMonth() {
		this.currentMonth = moment(this.currentMonth)
			.clone()
			.add(1, 'M')
			.toDate();
	}

	/**
	 * Handles the input change.
	 * @param {!Event} event
	 */

	_handleOnInput(event) {
		const {value} = event.target;
		const format = `${this.dateFormat}`;

		const date = moment(value, format);

		if (date.isValid() && date._i.length === 10) {
			this.currentMonth = date.toDate();
			this._daySelected = Helpers.setDateSelected(this.currentMonth);
		}

		this.value = value;

		if (!value) {
			this._daySelected = '';
		}

		this._handleFieldEdited();
	}

	/**
	 * Handles the previous month button
	 * @protected
	 */

	_handlePreviousMonth() {
		this.currentMonth = moment(this.currentMonth)
			.clone()
			.add(-1, 'M')
			.toDate();
	}

	/**
	 * Handles datepicker view
	 * @protected
	 */

	_handleToggle() {
		this.expanded = !this.expanded;
	}

	/**
	 * Set the current month formatted
	 * @param {!Date} value
	 * @protected
	 * @return {!Date}
	 */

	_setCurrentMonth(value) {
		const currentMonth = moment(value)
			.clone()
			.set('date', 1)
			.set('hour', 12)
			.toDate();

		return currentMonth;
	}

	/**
	 * @inheritdoc
	 */

	_handleFieldEdited() {
		let value = Helpers.formatDate(this._daySelected);

		if (this.isEmptyValue(this.value)) {
			value = '';
		}

		this.emit(
			'fieldEdited',
			{
				fieldInstance: this,
				value: this._setValue(value)
			}
		);
	}

	/**
	 * Sets the formatted date of the input.
	 * @param {!Date} value
	 * @protected
	 * @return {!String}
	 */

	_setValue(value) {
		let newValue;

		if (moment(value, this.dateFormat).isValid() && !isNaN(value)) {
			if (typeof (value) == 'string') {
				newValue = value;
			}
			else {
				const date = moment(value)
					.clone()
					.format(this.dateFormat);

				newValue = date;
			}
		}
		else {
			newValue = value;
		}

		return newValue;
	}

	/**
	 * @inheritDoc
	 */

	created() {
		this._eventHandler = new EventHandler();

		let newValue;

		if (this.value) {
			newValue = this.value;
		}
		else if (this.predefinedValue) {
			newValue = this.predefinedValue;
		}
		else {
			newValue = this.initialMonth;
		}

		const value = moment(
			newValue,
			this.dateFormat
		).toDate();

		this.currentMonth = this._setCurrentMonth(value);
		this._daySelected = Helpers.setDateSelected(value);
	}

	/**
	 * @inheritDoc
	 */

	detached() {
		this._eventHandler.removeAllListeners();
	}

	disposeInternal() {
		super.disposeInternal();

		if (this._vanillaTextMask) {
			this._vanillaTextMask.destroy();
		}
	}

	/**
	 * Returns date format based on user locale from Liefray.AUI.
	 * @return {!String}
	 */

	getDateFormat() {
		const dateFormat = Liferay.AUI.getDateFormat();

		this._dateDelimiter = '/';
		this._endDelimiter = false;

		if (dateFormat.indexOf('.') != -1) {
			this._dateDelimiter = '.';

			if (dateFormat.lastIndexOf('.') == dateFormat.length - 1) {
				this._endDelimiter = true;
			}
		}

		if (dateFormat.indexOf('-') != -1) {
			this._dateDelimiter = '-';
		}

		return dateFormat;
	}

	/**
	 * Returns the input mask for the vanillaTextMask plugin.
	 * @return {!String}
	 */

	getInputMask() {
		const dateFormat = this.getDateFormat();
		const inputMaskArray = [];

		dateFormat.split('').forEach(
			item => {
				if (item === this._dateDelimiter) {
					inputMaskArray.push(this._dateDelimiter);
				}
				else if (item === 'Y') {
					inputMaskArray.push(/\d/);
					inputMaskArray.push(/\d/);
					inputMaskArray.push(/\d/);
					inputMaskArray.push(/\d/);
				}
				else if (item === 'd' || item === 'm') {
					inputMaskArray.push(/\d/);
					inputMaskArray.push(/\d/);
				}
			}
		);

		return inputMaskArray;
	}

	/**
	 * Generates a range of +/- 5 years based on the current year.
	 * @return {!Array}
	 */

	getYears() {
		const currentYear = this._year;
		const years = [];

		for (let year = currentYear - 5; year < currentYear + 5; year++) {
			years.push(year);
		}

		return years;
	}

	/**
	 * Checks if a given string is empty according to the input mask
	 * @return {!Boolean}
	 */

	isEmptyValue(string) {
		const inputMask = this.getInputMask();

		return !inputMask.some(
			(validator, index) => {
				let hasValue = false;

				if (typeof validator !== 'string') {
					hasValue = string[index] !== '_';
				}

				return hasValue;
			}
		);
	}

	/**
	 * @inheritDoc
	 */

	prepareStateForRender(state) {
		return {
			...state,
			years: this.getYears()
		};
	}

	/**
	 * @inheritDoc
	 */

	syncCurrentMonth(value) {
		if (value) {
			this._weeks = Helpers.getWeekArray(value, this.firstDayOfWeek);
			this._month = value.getMonth();
			this._year = value.getFullYear();
		}
	}

	/**
	 * @inheritDoc
	 */

	syncExpanded() {
		if (this.expanded) {
			this._eventHandler.add(
				dom.on(document, 'click', this._handleDocClick.bind(this), true)
			);
		}
		else {
			this._eventHandler.removeAllListeners();
		}
	}

	/**
	 * @inheritDoc
	 */

	attached() {
		const {base} = this.refs;
		const {inputElement} = base.refs;

		this._vanillaTextMask = vanillaTextMask(
			{
				inputElement,
				mask: this.getInputMask(),
				placeholderChar: '_',
				showMask: true
			}
		);
	}

	/**
	 * @inheritDoc
	 */

	rendered() {
		if (this._vanillaTextMask) {
			const {textMaskInputElement} = this._vanillaTextMask;

			textMaskInputElement.update();
		}
	}
}

/**
 * State definition.
 * @static
 * @type {!Object}
 */

DatePicker.STATE = {

	/**
	 * The day selected by the user.
	 * @default Date Month
	 * @instance
	 * @memberof DatePicker
	 * @type {!Date}
	 */

	_daySelected: Config.any().internal(),

	/**
	 * The selected month.
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {!int}
	 */

	_month: Config.number().internal(),

	/**
	 * An array of the weeks and days list for the current month
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {!Array<Array>}
	 */

	_weeks: Config.array(Config.array()).internal(),

	/**
	 * The selected year.
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {!int}
	 */

	_year: Config.number().internal(),

	/**
	 * Aria label attribute for the button element.
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {?(string|undefined)}
	 */

	ariaLabel: Config.string(),

	/**
	 * Indicates the current month rendered on the screen.
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {!Date}
	 */

	currentMonth: Config.instanceOf(Date).internal(),

	/**
	 * Set the format of how the date will appear in the input element.
	 * See available: https://momentjs.com/docs/#/parsing/string-format/
	 * @default YYYY-MM-DD
	 * @instance
	 * @memberof DatePicker
	 * @type {?string}
	 */

	dateFormat: Config.string().valueFn('_dateFormatValueFn'),

	/**
	 * Set the initial value of the input.
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {?(Date|string|undefined)}
	 */

	predefinedValue: Config.oneOfType(
		[
			Config.instanceOf(Date),
			Config.string()
		]
	).setter('_setValue'),

	/**
	 * CSS classes to be applied to the element.
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {?(string|undefined)}
	 */

	elementClasses: Config.string(),

	/**
	 * Flag to indicate if date is expanded.
	 * @default false
	 * @instance
	 * @memberof DatePicker
	 * @type {?bool}
	 */

	expanded: Config.bool().internal().value(false),

	/**
	 * Set the first day of the week, starting from 0
	 * (Sunday) to 6 (Saturday).
	 * @default 0
	 * @instance
	 * @memberof DatePicker
	 * @type {?int}
	 */

	firstDayOfWeek: Config.oneOf([0, 1, 2, 3, 4, 5, 6]).value(0),

	/**
	 * Id to be applied to the element.
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {?(string|undefined)}
	 */

	id: Config.string(),

	/**
	 * The month to display in the calendar on the first render.
	 * @default Date
	 * @instance
	 * @memberof DatePicker
	 * @type {!Date}
	 */

	initialMonth: Config.instanceOf(Date).value(new Date()),

	/**
	 * The names of the months.
	 * @default January...
	 * @instance
	 * @memberof DatePicker
	 * @type {?array<String>}
	 */

	months: Config.array().value(
		[
			'January',
			'February',
			'March',
			'April',
			'May',
			'June',
			'July',
			'August',
			'September',
			'October',
			'November',
			'December'
		]
	),

	showLabel: Config.bool().value(true),

	/**
	 * Describe a brief tip to help users interact.
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {?(string|undefined)}
	 */

	placeholder: Config.string(),

	/**
	 * The path to the SVG spritemap file containing the icons.
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {!string}
	 */

	spritemap: Config.string().required(),

	/**
	 * Short names of days of the week to use in the header
	 * of the month. It should start from Sunday.
	 * @default S M T W T F S
	 * @instance
	 * @memberof DatePicker
	 * @type {!Array<String>}
	 */

	weekdaysShort: Config.array().value(['S', 'M', 'T', 'W', 'T', 'F', 'S']),

	/**
	 * List of years available for navigate that are added in the selector.
	 * @default undefined
	 * @instance
	 * @memberof DatePicker
	 * @type {!Array<String>}
	 */

	years: Config.array().value(
		[
			'2018',
			'2019',
			'2020',
			'2021',
			'2022',
			'2023',
			'2024'
		]
	),

	/** Teste */

	dataType: Config.string().value('string'),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	displayStyle: Config.string().value('singleline'),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	fieldName: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	label: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	name: Config.string().required(),

	/**
	 * @default false
	 * @instance
	 * @memberof Text
	 * @type {?(bool|undefined)}
	 */

	required: Config.bool().value(false),

	/**
	 * @default undefined
	 * @instance
	 * @memberof FieldBase
	 * @type {?(bool|undefined)}
	 */

	repeatable: Config.bool(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof FieldBase
	 * @type {?(bool|undefined)}
	 */
	readOnly: Config.bool().value(false),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	tip: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	type: Config.string().value('text'),

	/**
	 * @default undefined
	 * @instance
	 * @memberof FieldBase
	 * @type {?(string|undefined)}
	 */

	tooltip: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	value: Config.oneOfType(
		[
			Config.instanceOf(Date),
			Config.string()
		]
	).setter('_setValue').internal()

};

Soy.register(DatePicker, templates);

export {DatePicker};
export default DatePicker;