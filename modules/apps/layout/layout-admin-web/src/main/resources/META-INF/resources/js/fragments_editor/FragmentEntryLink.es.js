import Component from 'metal-component';
import Soy from 'metal-soy';
import {Config} from 'metal-state';
import {addClasses, removeClasses} from 'metal-dom';
import {isFunction, isObject, object} from 'metal';

import EditableImageFragmentProcessor from './fragment_processors/EditableImageFragmentProcessor.es';
import EditableTextFragmentProcessor from './fragment_processors/EditableTextFragmentProcessor.es';
import MappeableFragmentProcessor from './fragment_processors/MappeableFragmentProcessor.es';
import templates from './FragmentEntryLink.soy';

const ARROW_DOWN_KEYCODE = 40;

const ARROW_UP_KEYCODE = 38;

const EDITABLE_FRAGMENT_ENTRY_PROCESSOR = 'com.liferay.fragment.entry.processor.editable.EditableFragmentEntryProcessor';

const FRAGMENT_PROCESSORS = [
	EditableTextFragmentProcessor,
	EditableImageFragmentProcessor,
	MappeableFragmentProcessor
];

/**
 * FragmentEntryLink
 * @review
 */

class FragmentEntryLink extends Component {

	/**
	 * @inheritDoc
	 * @review
	 */

	created() {
		this._updateEditableStatus = this._updateEditableStatus.bind(this);
		this._updateEditableValues = this._updateEditableValues.bind(this);

		this._processors = FRAGMENT_PROCESSORS.map(
			Processor => new Processor(this)
		);
	}

	/**
	 * @inheritDoc
	 * @review
	 */

	disposed() {
		this._processors.forEach(
			processor => {
				processor.dispose();
			}
		);
	}

	/**
	 * @inheritDoc
	 * @review
	 */

	prepareStateForRender(state) {
		return Object.assign(
			{},
			state,
			{
				content: this.content ? Soy.toIncDom(this.content) : null
			}
		);
	}

	/**
	 * After each render, script tags need to be reapended to the DOM
	 * in order to trigger an execution (content changes do not trigger it).
	 * @inheritDoc
	 * @review
	 */

	rendered() {
		if (this.refs.content) {
			AUI().use(
				'aui-parse-content',
				A => {
					const content = A.one(this.refs.content);
					content.plug(A.Plugin.ParseContent);
					content.setContent(this.content);

					this._processors.forEach(
						processor => {
							processor.process();
						}
					);

					this._update(
						this.languageId,
						this.defaultLanguageId,
						[
							this._updateEditableValues,
							this._updateEditableStatus
						]
					);
				}
			);
		}
	}

	/**
	 * @inheritDoc
	 * @review
	 */

	shouldUpdate(changes) {
		return !!changes.content ||
				!!changes.showMapping ||
				!!changes.languageId;
	}

	/**
	 * Returns the given editable value
	 */

	getEditableValue(editableId) {
		return this.getEditableValues()[editableId] || {};
	}

	/**
	 * Returns the editable values property content
	 * @return {object}
	 * @review
	 */

	getEditableValues() {
		return this.editableValues[EDITABLE_FRAGMENT_ENTRY_PROCESSOR] || {};
	}

	/**
	 * Returns a new object with the updated editableId
	 * @param {string} editableId
	 * @param {object} content
	 */

	setEditableValue(editableId, content) {
		const editableValues = this.getEditableValues();

		const editableValue = this.getEditableValue(editableId);

		editableValues[editableId] = object.mixin({}, editableValue, content);

		this._update(
			this.languageId,
			this.defaultLanguageId,
			[this._updateEditableStatus]
		);

		return {
			[EDITABLE_FRAGMENT_ENTRY_PROCESSOR]: editableValues
		};
	}

	/**
	 * Emits a move event with the fragmentEntryLinkId and the direction.
	 * @param {!number} direction
	 * @private
	 */

	_emitMoveEvent(direction) {
		this.emit(
			'move',
			{
				direction,
				fragmentEntryLinkId: this.fragmentEntryLinkId
			}
		);
	}

	/**
	 * Handle fragment keyup event so it can emit when it
	 * should be moved or selected.
	 * @param {KeyboardEvent} event
	 * @private
	 * @review
	 */

	_handleFragmentKeyUp(event) {
		if (document.activeElement === this.refs.fragmentEntryLinkWrapper) {
			switch (event.which) {
			case ARROW_DOWN_KEYCODE:
				this._emitMoveEvent(FragmentEntryLink.MOVE_DIRECTIONS.DOWN);
				break;
			case ARROW_UP_KEYCODE:
				this._emitMoveEvent(FragmentEntryLink.MOVE_DIRECTIONS.UP);
				break;
			}
		}
	}

	/**
	 * Callback executed when the fragment move down button is clicked.
	 * It emits a 'moveDown' event with
	 * the FragmentEntryLink id.
	 * @private
	 * @review
	 */

	_handleFragmentMoveDownButtonClick() {
		this._emitMoveEvent(FragmentEntryLink.MOVE_DIRECTIONS.DOWN);
	}

	/**
	 * Callback executed when the fragment move up button is clicked.
	 * It emits a 'moveUp' event with
	 * the FragmentEntryLink id.
	 * @private
	 * @review
	 */

	_handleFragmentMoveUpButtonClick() {
		this._emitMoveEvent(FragmentEntryLink.MOVE_DIRECTIONS.UP);
	}

	/**
	 * Callback executed when the fragment remove button is clicked.
	 * It emits a 'remove' event with
	 * the FragmentEntryLink id.
	 * @private
	 */

	_handleFragmentRemoveButtonClick() {
		this.emit(
			'remove',
			{
				fragmentEntryLinkId: this.fragmentEntryLinkId
			}
		);
	}

	/**
	 * Runs a set of update functions through the collection of editable values
	 * inside this fragment entry link.
	 * @param {string} languageId The current language id
	 * @param {string} defaultLanguageId The default language id
	 * @param {Array<Function>} updateFunctions The set of update functions to execute for each editable value
	 * @private
	 * @review
	 */

	_update(languageId, defaultLanguageId, updateFunctions) {
		const editableValues = this.getEditableValues() || {};

		Object.keys(editableValues).forEach(
			editableId => {
				const editableValue = editableValues[editableId];

				const defaultValue = editableValue[defaultLanguageId] || editableValue.defaultValue;
				const mappedField = editableValue.mappedField || '';
				const value = editableValue[languageId];

				updateFunctions.forEach(
					updateFunction => updateFunction(
						editableId,
						value,
						defaultValue,
						mappedField
					)
				);
			}
		);
	}

	/**
	 * Flags a DOM editable section as translated or untranslated compared to
	 * the stored default value for that same editable id.
	 * @param {string} editableId The editable id
	 * @param {string} value The value for the editable section
	 * @param {string} defaultValue
	 * @param {string} mappedField
	 * @private
	 * @review
	 */

	_updateEditableStatus(editableId, value, defaultValue, mappedField) {
		const element = this.element.querySelector(`lfr-editable[id="${editableId}"]`);

		if (element) {
			removeClasses(
				element,
				'mapped',
				'translated',
				'unmapped',
				'untranslated'
			);

			const mapped = !!mappedField;
			const translated = !mappedField && !!value;

			addClasses(element, mapped ? 'mapped' : 'unmapped');
			addClasses(element, translated ? 'translated' : 'untranslated');
		}
	}

	/**
	 * Looks through all available processors for associated editors with a given
	 * editable section to update them with a new value.
	 * @param {string} editableId The editable id
	 * @param {string} value The value for the editable section
	 * @param {string} defaultValue The default value for the editable section
	 * @private
	 * @review
	 */

	_updateEditableValues(editableId, value, defaultValue) {
		this._processors.forEach(
			processor => {
				const editor = processor.findEditor(editableId);

				if (editor) {
					editor.setData(
						value || defaultValue || editor.defaultValue
					);
				}
			}
		);
	}
}

/**
 * Directions where a fragment can be moved to
 * @review
 * @static
 * @type {!object}
 */

FragmentEntryLink.MOVE_DIRECTIONS = {
	DOWN: 1,
	UP: -1
};

/**
 * State definition.
 * @review
 * @static
 * @type {!Object}
 */

FragmentEntryLink.STATE = {

	/**
	 * Fragment content to be rendered
	 * @default ''
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {string}
	 */

	content: Config.any()
		.setter(
			content => {
				return !isFunction(content) && isObject(content) ? content.value.content : content;
			}
		)
		.value(''),

	/**
	 * Default configuration for AlloyEditor instances.
	 * @default {}
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {object}
	 */

	defaultEditorConfiguration: Config.object().value({}),

	/**
	 * Default language id.
	 * @default undefined
	 * @instance
	 * @memberOf FragmentsEditor
	 * @review
	 * @type {!string}
	 */

	defaultLanguageId: Config.string().required(),

	/**
	 * Editable values that should be used instead of the default ones
	 * inside editable fields.
	 * @default {}
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {!Object}
	 */

	editableValues: {},

	/**
	 * FragmentEntryLink id
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {!string}
	 */

	fragmentEntryLinkId: Config.string().required(),

	/**
	 * Image selector url
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {!string}
	 */

	imageSelectorURL: Config.string().required(),

	/**
	 * Currently selected language id.
	 * @default undefined
	 * @instance
	 * @memberOf FragmentsEditor
	 * @review
	 * @type {!string}
	 */

	languageId: Config.string().required(),

	/**
	 * Fragment name
	 * @default ''
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {string}
	 */

	name: Config.string().value(''),

	/**
	 * Selected mapping type label
	 * @default {}
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {{
	 *   subtype: {
	 *   	id: !string,
	 *   	label: !string
	 *   },
	 *   type: {
	 *   	id: !string,
	 *   	label: !string
	 *   }
	 * }}
	 */

	selectedMappingTypes: Config
		.shapeOf(
			{
				subtype: Config.shapeOf(
					{
						id: Config.string().required(),
						label: Config.string().required()
					}
				),
				type: Config.shapeOf(
					{
						id: Config.string().required(),
						label: Config.string().required()
					}
				)
			}
		)
		.value({}),

	/**
	 * Shows FragmentEntryLink control toolbar
	 * @default true
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {!bool}
	 */

	showControlBar: Config.bool().value(true),

	/**
	 * If true, asset mapping is enabled
	 * @default false
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {bool}
	 */

	showMapping: Config.bool().value(false),

	/**
	 * Portlet namespace needed for prefixing Alloy Editor instances
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {!string}
	 */

	portletNamespace: Config.string().required(),

	/**
	 * Fragment spritemap
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {!string}
	 */

	spritemap: Config.string().required(),

	/**
	 * Array of processors that will be applied to the fragments
	 * content whenever it is created.
	 * @default []
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @private
	 * @review
	 * @type {Array<object>}
	 */

	_processors: Config.arrayOf(Config.object())
		.internal()
		.value([])
};

Soy.register(FragmentEntryLink, templates);

export {FragmentEntryLink};
export default FragmentEntryLink;