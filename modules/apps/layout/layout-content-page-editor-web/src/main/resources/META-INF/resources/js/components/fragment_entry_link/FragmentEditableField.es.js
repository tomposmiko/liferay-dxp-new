import {Config} from 'metal-state';
import PortletBase from 'frontend-js-web/liferay/PortletBase.es';
import Soy from 'metal-soy';

import '../floating_toolbar/image_properties/FloatingToolbarImagePropertiesPanel.es';
import '../floating_toolbar/link/FloatingToolbarLinkPanel.es';
import '../floating_toolbar/mapping/FloatingToolbarMappingPanel.es';
import '../floating_toolbar/text_properties/FloatingToolbarTextPropertiesPanel.es';
import './FragmentEditableFieldTooltip.es';

import {CLEAR_FRAGMENT_EDITOR, DISABLE_FRAGMENT_EDITOR, ENABLE_FRAGMENT_EDITOR, OPEN_ASSET_TYPE_DIALOG, UPDATE_CONFIG_ATTRIBUTES, UPDATE_EDITABLE_VALUE, UPDATE_LAST_SAVE_DATE, UPDATE_SAVING_CHANGES_STATUS, UPDATE_TRANSLATION_STATUS} from '../../actions/actions.es';
import {EDITABLE_FIELD_CONFIG_KEYS, FLOATING_TOOLBAR_BUTTONS, FRAGMENTS_EDITOR_ITEM_TYPES} from '../../utils/constants';
import {prefixSegmentsExperienceId} from '../../utils/prefixSegmentsExperienceId.es';
import {getConnectedComponent} from '../../store/ConnectedComponent.es';
import {getItemPath, itemIsInPath} from '../../utils/FragmentsEditorGetUtils.es';
import {setIn} from '../../utils/FragmentsEditorUpdateUtils.es';
import {shouldUpdateOnChangeProperties, shouldUpdatePureComponent} from '../../utils/FragmentsEditorComponentUtils.es';
import FloatingToolbar from '../floating_toolbar/FloatingToolbar.es';
import FragmentProcessors from '../fragment_processors/FragmentProcessors.es';
import templates from './FragmentEditableField.soy';

const DEFAULT_LANGUAGE_ID_KEY = 'defaultValue';

/**
 * Delay to save changes of an editable field
 * @review
 * @type {!number}
 */
const SAVE_CHANGES_DELAY = 1500;

/**
 * FragmentEditableField
 */
class FragmentEditableField extends PortletBase {

	/**
	 * Checks if the given editable should be highlighted
	 * @param {string} activeItemId
	 * @param {string} activeItemType
	 * @param {string} hoveredItemId
	 * @param {string} hoveredItemType
	 * @param {object} structure
	 * @private
	 * @return {boolean}
	 * @review
	 */
	static _isHighlighted(
		activeItemId,
		activeItemType,
		fragmentEntryLinkId,
		hoveredItemId,
		hoveredItemType,
		structure
	) {
		const fragmentInActivePath = itemIsInPath(
			getItemPath(activeItemId, activeItemType, structure),
			fragmentEntryLinkId,
			FRAGMENTS_EDITOR_ITEM_TYPES.fragment
		) && activeItemType !== FRAGMENTS_EDITOR_ITEM_TYPES.editable;

		const fragmentInHoveredPath = itemIsInPath(
			getItemPath(hoveredItemId, hoveredItemType, structure),
			fragmentEntryLinkId,
			FRAGMENTS_EDITOR_ITEM_TYPES.fragment
		);

		return (fragmentInActivePath || fragmentInHoveredPath);
	}

	/**
	 * Checks if the given editable is mapped
	 * @param {object} editableValues
	 * @private
	 * @return {boolean}
	 * @review
	 */
	static _isMapped(editableValues) {
		return Boolean(
			editableValues.mappedField ||
			(
				editableValues.classNameId &&
				editableValues.classPK &&
				editableValues.fieldId
			)
		);
	}

	/**
	 * Checks if the given editable is mapped to an asset entry
	 * @param {object} editableValues
	 * @private
	 * @return {boolean}
	 * @review
	 */
	static _isMappedToAssetEntry(editableValues) {
		return Boolean(
			(
				editableValues.classNameId &&
				editableValues.classPK &&
				editableValues.fieldId
			)
		);
	}

	/**
	 * @inheritDoc
	 * @review
	 */
	created() {
		this._handleBeforeNavigate = this._handleBeforeNavigate.bind(this);
		this._handleBeforeUnload = this._handleBeforeUnload.bind(this);
		this._handleEditableChanged = this._handleEditableChanged.bind(this);
		this._handleEditableDestroyed = this._handleEditableDestroyed.bind(this);
		this._handleFloatingToolbarButtonClicked = this._handleFloatingToolbarButtonClicked.bind(this);

		this._beforeNavigateHandler = Liferay.on(
			'beforeNavigate',
			this._handleBeforeNavigate
		);

		window.addEventListener('beforeunload', this._handleBeforeUnload);
	}

	/**
	 * @inheritDoc
	 * @review
	 */
	disposed() {
		clearTimeout(this._saveChangesTimeout);

		this._destroyProcessors();
		this._disposeFloatingToolbar();

		this._beforeNavigateHandler.detach();

		window.removeEventListener('beforeunload', this._handleBeforeUnload);
	}

	/**
	 * @inheritDoc
	 * @param {!object} state
	 * @returns {object}
	 */
	prepareStateForRender(state) {
		const defaultSegmentsExperienceId = prefixSegmentsExperienceId(this.defaultSegmentsExperienceId);
		const segmentsExperienceId = prefixSegmentsExperienceId(this.segmentsExperienceId);

		const segmentedValue = this.editableValues[segmentsExperienceId] ||
			this.editableValues[defaultSegmentsExperienceId] ||
			this.editableValues;

		const translatedValue = segmentedValue[this.languageId] ||
			segmentedValue[this.defaultLanguageId];

		const mapped = FragmentEditableField._isMapped(
			this.editableValues
		);

		let value = mapped ?
			(this._mappedFieldValue || this.editableValues.defaultValue) :
			(translatedValue || this.editableValues.defaultValue);

		const processor = FragmentProcessors[this.type] || FragmentProcessors.fallback;

		const content = Soy.toIncDom(
			processor.render(
				this.content,
				value,
				this.editableValues
			)
		);

		const highlighted = FragmentEditableField._isHighlighted(
			state.activeItemId,
			state.activeItemType,
			state.fragmentEntryLinkId,
			state.hoveredItemId,
			state.hoveredItemType,
			state.layoutData.structure
		);
		const itemId = this._getItemId();
		const translated = !mapped && Boolean(segmentedValue[this.languageId]);

		let nextState = state;

		nextState = setIn(nextState, ['_highlighted'], highlighted);
		nextState = setIn(nextState, ['_mapped'], mapped);
		nextState = setIn(nextState, ['_translated'], translated);
		nextState = setIn(nextState, ['content'], content);
		nextState = setIn(nextState, ['itemId'], itemId);
		nextState = setIn(nextState, ['itemTypes'], FRAGMENTS_EDITOR_ITEM_TYPES);

		return nextState;
	}

	/**
	 * @inheritDoc
	 * @review
	 */
	rendered() {
		if ((this._getItemId() === this.activeItemId) &&
			(this.activeItemType === FRAGMENTS_EDITOR_ITEM_TYPES.editable)) {

			this._createFloatingToolbar();
		}
		else {
			this._disposeFloatingToolbar();
		}
	}

	/**
	 * @inheritDoc
	 * @return {boolean}
	 * @review
	 */
	shouldUpdate(changes) {
		return this._editing ?
			shouldUpdateOnChangeProperties(changes, ['languageId', 'segmentsExperienceId']) :
			shouldUpdatePureComponent(changes);
	}

	/**
	 * Handle editableValues changed
	 * @inheritDoc
	 * @review
	 */
	syncEditableValues() {
		this._loadMappedFieldLabel();
		this._updateMappedFieldValue();
	}

	/**
	 * @inheritDoc
	 * @param {!Object} newVal
	 * @review
	 */
	syncFragmentEditorClear(newVal) {
		if (newVal === this._getItemId()) {
			this._handleEditableChanged('');

			this.store.dispatchAction(
				CLEAR_FRAGMENT_EDITOR,
				{
					itemId: ''
				}
			);
		}
	}

	/**
	 * @inheritDoc
	 * @param {!Object} newVal
	 * @review
	 */
	syncFragmentEditorEnabled(newVal) {
		if (newVal === this._getItemId()) {
			this._enableEditor();

			this._disposeFloatingToolbar();
		}
	}

	/**
	 * Handle getAssetFieldValueURL changed
	 * @inheritDoc
	 * @review
	 */
	syncGetAssetFieldValueURL() {
		this._updateMappedFieldValue();
	}

	/**
	 * Creates a new instance of FloatingToolbar
	 * @private
	 * @review
	 */
	_createFloatingToolbar() {
		const processor = FragmentProcessors[this.type] || FragmentProcessors.fallback;

		const config = {
			anchorElement: this.element,
			buttons: processor.getFloatingToolbarButtons(this.editableValues),
			classes:
				this.editableValues.mappedField ||
				this.editableValues.fieldId ?
					'fragments-editor__floating-toolbar--mapped-field' :
					'',
			events: {
				buttonClicked: this._handleFloatingToolbarButtonClicked
			},
			item: {
				editableId: this.editableId,
				editableValues: this.editableValues,
				fragmentEntryLinkId: this.fragmentEntryLinkId,
				type: this.type
			},
			itemId: this._getItemId(),
			itemType: FRAGMENTS_EDITOR_ITEM_TYPES.editable,
			portalElement: document.body,
			store: this.store
		};

		if (this._floatingToolbar) {
			this._floatingToolbar.setState(config);
		}
		else {
			this._floatingToolbar = new FloatingToolbar(config);
		}
	}

	/**
	 * Call destroy method on all processors
	 * @private
	 * @review
	 */
	_destroyProcessors() {
		Object.values(FragmentProcessors).forEach(
			fragmentProcessor => fragmentProcessor.destroy()
		);
	}

	/**
	 * Disposes an existing instance of FloatingToolbar
	 * @private
	 * @review
	 */
	_disposeFloatingToolbar() {
		if (this._floatingToolbar) {
			this._floatingToolbar.dispose();

			this._floatingToolbar = null;
		}
	}

	/**
	 * Enables the corresponding editor
	 * @private
	 * @review
	 */
	_enableEditor() {
		const {init} = FragmentProcessors[this.type] ||
			FragmentProcessors.fallback;

		init(
			this.refs.editable,
			this.fragmentEntryLinkId,
			this.portletNamespace,
			this.processorsOptions,
			this._handleEditableChanged,
			this._handleEditableDestroyed
		);

		this._editing = true;
	}

	/**
	 * @private
	 * @return {string} Valid FragmentsEditor itemId for it's
	 * 	fragmentEntryLinkId and editableId
	 * @review
	 */
	_getItemId() {
		return `${this.fragmentEntryLinkId}-${this.editableId}`;
	}

	/**
	 * Handle beforeNavigate SPA event
	 * and destroy all existing processors.
	 * @private
	 * @review
	 */
	_handleBeforeNavigate(event) {
		if (this._unsavedChanges) {
			const msg = Liferay.Language.get('do-you-want-to-leave-this-site');

			if (!confirm(msg)) {
				event.originalEvent.preventDefault();
			}
		}
	}

	/**
	 * Handle beforeunload event and show confirmation dialog
	 * if there are unsaved changes
	 * @private
	 * @review
	 */
	_handleBeforeUnload(event) {
		const confirmationMessage = '';

		if (this._unsavedChanges) {
			event.returnValue = confirmationMessage;
		}

		return confirmationMessage;
	}

	/**
	 * Handle editable click event
	 * @private
	 * @review
	 */
	_handleEditableClick() {
		if (this._preventEditableClick) {
			this._preventEditableClick = false;
		}
		else {
			this._disposeFloatingToolbar();
			this._enableEditor();
		}
	}

	/**
	 * Handle editable focus event
	 * @private
	 * @review
	 */
	_handleEditableFocus() {
		this._preventEditableClick = true;
	}

	/**
	 * Callback executed when the exiting editor is destroyed
	 * @private
	 * @review
	 */
	_handleEditableDestroyed() {
		this._editing = false;

		this.store.dispatchAction(
			DISABLE_FRAGMENT_EDITOR
		);
	}

	/**
	 * Callback executed when an editable value changes
	 * @param {string} newValue
	 * @private
	 */
	_handleEditableChanged(newValue) {
		this._unsavedChanges = true;

		clearTimeout(this._saveChangesTimeout);

		this._saveChangesTimeout = setTimeout(
			() => {
				this._saveChanges(newValue);
			},
			SAVE_CHANGES_DELAY
		);
	}

	/**
	 * Callback executed when an floating toolbar button is clicked
	 * @param {Event} event
	 * @param {Object} data
	 * @private
	 */
	_handleFloatingToolbarButtonClicked(event, data) {
		const {panelId, type} = data;

		if (type === 'editor') {
			this.store.dispatchAction(
				ENABLE_FRAGMENT_EDITOR,
				{
					itemId: this._getItemId()
				}
			);
		}
		else if (type === 'panel' &&
			panelId === FLOATING_TOOLBAR_BUTTONS.map.panelId &&
			this.mappingFieldsURL &&
			!this.selectedMappingTypes.type) {

			event.preventDefault();

			this.store.dispatchAction(OPEN_ASSET_TYPE_DIALOG);
		}
	}

	/**
	 * Load mapped field label
	 * @private
	 * @review
	 */
	_loadMappedFieldLabel() {
		let promise;
		let mappedFieldId;

		if (this.editableValues.mappedField &&
			this.selectedMappingTypes.type) {

			const data = {
				classNameId: this.selectedMappingTypes.type.id
			};

			if (this.selectedMappingTypes.subtype) {
				data.classTypeId = this.selectedMappingTypes.subtype.id;
			}

			mappedFieldId = this.editableValues.mappedField;
			promise = this.fetch(this.mappingFieldsURL, data);
		}
		else if (this.editableValues.classNameId &&
			this.editableValues.classPK &&
			this.editableValues.fieldId &&
			this.getAssetMappingFieldsURL) {

			mappedFieldId = this.editableValues.fieldId;
			promise = this.fetch(
				this.getAssetMappingFieldsURL,
				{
					classNameId: this.editableValues.classNameId,
					classPK: this.editableValues.classPK
				}
			);
		}

		if (promise) {
			promise
				.then(
					response => response.json()
				)
				.then(
					response => {
						const field = response.find(
							field => field.key === mappedFieldId
						);

						if (field) {
							this._mappedFieldLabel = field.label;
						}
					}
				);
		}
	}

	/**
	 * Saves editable value changes
	 * @param {string} newValue
	 */
	_saveChanges(newValue) {
		this._unsavedChanges = false;
		const editableValueSegmentsExperienceId = prefixSegmentsExperienceId(this.segmentsExperienceId) || prefixSegmentsExperienceId(this.defaultSegmentsExperienceId);

		this.store
			.dispatchAction(
				UPDATE_SAVING_CHANGES_STATUS,
				{
					savingChanges: true
				}
			)
			.dispatchAction(
				UPDATE_EDITABLE_VALUE,
				{
					editableId: this.editableId,
					editableValue: newValue,
					editableValueId: this.languageId || DEFAULT_LANGUAGE_ID_KEY,
					editableValueSegmentsExperienceId,
					fragmentEntryLinkId: this.fragmentEntryLinkId
				}
			)
			.dispatchAction(
				UPDATE_CONFIG_ATTRIBUTES,
				{
					config: {
						[EDITABLE_FIELD_CONFIG_KEYS.imageSource]: newValue
					},
					editableId: this.editableId,
					fragmentEntryLinkId: this.fragmentEntryLinkId
				}
			)
			.dispatchAction(
				UPDATE_TRANSLATION_STATUS,
				{
					segmentsExperienceId: this.segmentsExperienceId || this.defaultSegmentsExperienceId
				}
			)
			.dispatchAction(
				UPDATE_LAST_SAVE_DATE,
				{
					lastSaveDate: new Date()
				}
			)
			.dispatchAction(
				UPDATE_SAVING_CHANGES_STATUS,
				{
					savingChanges: false
				}
			);
	}

	/**
	 * Updates mapped field value
	 * @private
	 * @review
	 */
	_updateMappedFieldValue() {
		if (this.getAssetFieldValueURL &&
			FragmentEditableField._isMappedToAssetEntry(this.editableValues)) {

			this.fetch(
				this.getAssetFieldValueURL,
				{
					classNameId: this.editableValues.classNameId,
					classPK: this.editableValues.classPK,
					fieldId: this.editableValues.fieldId
				}
			)
				.then(
					response => response.json()
				)
				.then(
					response => {
						this._mappedFieldValue = response.fieldValue;
					}
				);
		}
	}

}

/**
 * State definition.
 * @review
 * @static
 * @type {!Object}
 */
FragmentEditableField.STATE = {

	/**
	 * Editable content to be rendered
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEditableField
	 * @review
	 * @type {!string}
	 */
	content: Config
		.string()
		.required(),

	/**
	 * Editable ID
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEditableField
	 * @review
	 * @type {!string}
	 */
	editableId: Config
		.string()
		.required(),

	/**
	 * Editable values
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEditableField
	 * @review
	 * @type {!object}
	 */
	editableValues: Config
		.object()
		.required(),

	/**
	 * FragmentEntryLink id
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEditableField
	 * @review
	 * @type {!string}
	 */
	fragmentEntryLinkId: Config
		.string()
		.required(),

	/**
	 * Set of options that are sent to the processors.
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEditableField
	 * @review
	 * @type {!object}
	 */
	processorsOptions: Config
		.object()
		.required(),

	/**
	 * Editable type
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEditableField
	 * @review
	 * @type {!string}
	 */
	type: Config.oneOf(['html', 'image', 'link', 'rich-text', 'text']).required(),

	/**
	 * Internal FloatingToolbar instance.
	 * @default null
	 * @instance
	 * @memberOf FragmentEditableField
	 * @review
	 * @type {object|null}
	 */
	_floatingToolbar: Config
		.internal()
		.value(null),

	/**
	 * Flag indicating if the editable editor is active.
	 * @default false
	 * @instance
	 * @memberOf FragmentEditableField
	 * @private
	 * @review
	 * @type {boolean}
	 */
	_editing: Config
		.internal()
		.bool()
		.value(false),

	/**
	 * Translated label of the mapped field
	 * @instance
	 * @memberOf FragmentEditableField
	 * @private
	 * @review
	 * @type {string}
	 */
	_mappedFieldLabel: Config
		.internal()
		.string(),

	/**
	 * Mapped asset field value
	 * @instance
	 * @memberOf FragmentEditableField
	 * @private
	 * @review
	 * @type {string}
	 */
	_mappedFieldValue: Config
		.internal()
		.string(),

	/**
	 * Prevent editable click effect
	 * @instance
	 * @memberOf FragmentEditableField
	 * @private
	 * @review
	 * @type {boolean}
	 */
	_preventEditableClick: Config
		.bool()
		.value(),

	/**
	 * Id of the timeout to save changes
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEditableField
	 * @review
	 * @type {number}
	 */

	_saveChangesTimeout: Config.number().internal(),

	/**
	 * Flag indicating if there are unsaved changes
	 * @default false
	 * @instance
	 * @memberOf FragmentEditableField
	 * @private
	 * @review
	 * @type {boolean}
	 */
	_unsavedChanges: Config
		.internal()
		.bool()
		.value(false)
};

const ConnectedFragmentEditableField = getConnectedComponent(
	FragmentEditableField,
	[
		'activeItemId',
		'activeItemType',
		'defaultLanguageId',
		'defaultSegmentsExperienceId',
		'fragmentEditorClear',
		'fragmentEditorEnabled',
		'getAssetFieldValueURL',
		'getAssetMappingFieldsURL',
		'hoveredItemId',
		'hoveredItemType',
		'languageId',
		'layoutData',
		'mappingFieldsURL',
		'portletNamespace',
		'segmentsExperienceId',
		'selectedMappingTypes'
	]
);

Soy.register(ConnectedFragmentEditableField, templates);

export {ConnectedFragmentEditableField, FragmentEditableField};
export default ConnectedFragmentEditableField;