import Component from 'metal-component';
import position from 'metal-position';
import Soy from 'metal-soy';
import {Config} from 'metal-state';
import {Drag, DragDrop} from 'metal-drag-drop';

import '../floating_toolbar/FloatingToolbar.es';
import './FragmentEntryLinkListSection.es';
import getConnectedComponent from '../../store/ConnectedComponent.es';
import templates from './FragmentEntryLinkList.soy';
import {CLEAR_DROP_TARGET, MOVE_FRAGMENT_ENTRY_LINK, MOVE_SECTION, UPDATE_DROP_TARGET} from '../../actions/actions.es';
import {moveItem, setIn} from '../../utils/FragmentsEditorUpdateUtils.es';
import {FRAGMENTS_EDITOR_ITEM_BORDERS, FRAGMENTS_EDITOR_ITEM_TYPES} from '../../utils/constants';
import {getFragmentColumn, getTargetBorder} from '../../utils/FragmentsEditorGetUtils.es';
import {shouldUpdatePureComponent} from '../../utils/FragmentsEditorComponentUtils.es';

/**
 * FragmentEntryLinkList
 * @review
 */
class FragmentEntryLinkList extends Component {

	/**
	 * Adds drop target types to state
	 * @param {Object} _state
	 * @private
	 * @return {Object}
	 * @static
	 */
	static _addDropTargetItemTypesToState(_state) {
		return setIn(_state, ['dropTargetItemTypes'], FRAGMENTS_EDITOR_ITEM_TYPES);
	}

	/**
	 * Returns whether a drop is valid or not
	 * @param {Object} eventData
	 * @private
	 * @return {boolean}
	 * @static
	 */
	static _dropValid(eventData) {
		const sourceItemData = FragmentEntryLinkList._getItemData(
			eventData.source.dataset
		);
		const targetItemData = FragmentEntryLinkList._getItemData(
			eventData.target ? eventData.target.dataset : null
		);

		let dropValid = false;

		if (sourceItemData.itemType === FRAGMENTS_EDITOR_ITEM_TYPES.section) {
			dropValid = (
				(targetItemData.itemType === FRAGMENTS_EDITOR_ITEM_TYPES.section) &&
				(sourceItemData.itemId !== targetItemData.itemId)
			);
		}
		else if (sourceItemData.itemType === FRAGMENTS_EDITOR_ITEM_TYPES.fragment) {
			dropValid = (
				(targetItemData.itemType) &&
				(sourceItemData.itemId !== targetItemData.itemId)
			);
		}

		return dropValid;
	}

	/**
	 * Get id and type of an item from its dataset
	 * @param {!Object} itemDataset
	 * @private
	 * @return {Object}
	 * @static
	 */
	static _getItemData(itemDataset) {
		let itemId = null;
		let itemType = null;

		if (itemDataset) {
			if ('columnId' in itemDataset) {
				itemId = itemDataset.columnId;
				itemType = FRAGMENTS_EDITOR_ITEM_TYPES.column;
			}
			else if ('fragmentEntryLinkId' in itemDataset) {
				itemId = itemDataset.fragmentEntryLinkId;
				itemType = FRAGMENTS_EDITOR_ITEM_TYPES.fragment;
			}
			else if ('layoutSectionId' in itemDataset) {
				itemId = itemDataset.layoutSectionId;
				itemType = FRAGMENTS_EDITOR_ITEM_TYPES.section;
			}
			else if ('fragmentEmptyList' in itemDataset) {
				itemType = FRAGMENTS_EDITOR_ITEM_TYPES.fragmentList;
			}
		}

		return {
			itemId,
			itemType
		};
	}

	/**
	 * Checks wether a section is empty or not, sets empty parameter
	 * and returns a new state
	 * @param {Object} _state
	 * @private
	 * @return {Object}
	 * @static
	 */
	static _setEmptySections(_state) {
		return setIn(
			_state,
			[
				'layoutData',
				'structure'
			],
			_state.layoutData.structure.map(
				section => setIn(
					section,
					['empty'],
					section.columns.every(
						column => column.fragmentEntryLinkIds.length === 0
					)
				)
			)
		);
	}

	/**
	 * @inheritdoc
	 * @private
	 * @review
	 */
	attached() {
		this._initializeDragAndDrop();
	}

	/**
	 * @inheritdoc
	 * @private
	 * @review
	 */
	disposed() {
		this._dragDrop.dispose();
	}

	/**
	 * @inheritdoc
	 * @private
	 * @return {Object}
	 * @review
	 */
	prepareStateForRender(nextState) {
		let _state = FragmentEntryLinkList._addDropTargetItemTypesToState(nextState);

		_state = FragmentEntryLinkList._setEmptySections(_state);

		return _state;
	}

	/**
	 * @inheritdoc
	 * @private
	 * @review
	 */
	rendered() {
		if (
			(this.activeItemType === FRAGMENTS_EDITOR_ITEM_TYPES.fragmentList) &&
			this.element
		) {
			this.element.focus();
		}
	}

	/**
	 * @inheritdoc
	 * @return {boolean}
	 * @review
	 */
	shouldUpdate(changes) {
		return shouldUpdatePureComponent(changes);
	}

	/**
	 * Handle layoutData changed
	 * @inheritDoc
	 * @review
	 */
	syncLayoutData() {
		this._initializeDragAndDrop();
	}

	/**
	 * Callback that is executed when an item is being dragged.
	 * @param {Object} eventData
	 * @param {MouseEvent} eventData.originalEvent
	 * @private
	 * @review
	 */
	_handleDrag(eventData) {
		if (FragmentEntryLinkList._dropValid(eventData)) {
			const mouseY = eventData.originalEvent.clientY;
			const targetItem = eventData.target;
			const targetItemRegion = position.getRegion(targetItem);

			const dropTargetItemData = FragmentEntryLinkList._getItemData(
				targetItem.dataset
			);

			let targetBorder = FRAGMENTS_EDITOR_ITEM_BORDERS.bottom;

			if (
				Math.abs(mouseY - targetItemRegion.top) <=
				Math.abs(mouseY - targetItemRegion.bottom)
			) {
				targetBorder = FRAGMENTS_EDITOR_ITEM_BORDERS.top;
			}

			this.store.dispatchAction(
				UPDATE_DROP_TARGET,
				{
					dropTargetBorder: targetBorder,
					dropTargetItemId: dropTargetItemData.itemId,
					dropTargetItemType: dropTargetItemData.itemType
				}
			);
		}
	}

	/**
	 * Callback that is executed when we leave a drag target.
	 * @private
	 * @review
	 */
	_handleDragEnd() {
		this.store.dispatchAction(
			CLEAR_DROP_TARGET
		);
	}

	/**
	 * Callback that is executed when an item is dropped.
	 * @param {Object} data
	 * @param {MouseEvent} event
	 * @private
	 * @review
	 */
	_handleDrop(data, event) {
		event.preventDefault();

		if (FragmentEntryLinkList._dropValid(data)) {
			requestAnimationFrame(
				() => {
					this._initializeDragAndDrop();
				}
			);

			const itemData = FragmentEntryLinkList._getItemData(
				data.source.dataset
			);

			let moveItemAction = null;
			let moveItemPayload = null;

			if (itemData.itemType === FRAGMENTS_EDITOR_ITEM_TYPES.section) {
				moveItemAction = MOVE_SECTION;
				moveItemPayload = {
					sectionId: itemData.itemId,
					targetBorder: this.dropTargetBorder,
					targetItemId: this.dropTargetItemId
				};
			}
			else if (itemData.itemType === FRAGMENTS_EDITOR_ITEM_TYPES.fragment) {
				moveItemAction = MOVE_FRAGMENT_ENTRY_LINK;
				moveItemPayload = {
					fragmentEntryLinkId: itemData.itemId,
					targetBorder: this.dropTargetBorder,
					targetItemId: this.dropTargetItemId,
					targetItemType: this.dropTargetItemType
				};
			}

			moveItem(this.store, moveItemAction, moveItemPayload);
		}
	}

	/**
	 * @param {Event} event
	 * @private
	 * @review
	 */
	_handleFragmentMove(event) {
		const {fragmentEntryLinkId} = event;

		const column = getFragmentColumn(
			this.layoutData.structure,
			fragmentEntryLinkId
		);
		const fragmentIndex = column.fragmentEntryLinkIds.indexOf(
			fragmentEntryLinkId
		);
		const targetFragmentEntryLinkId = column.fragmentEntryLinkIds[
			fragmentIndex + event.direction
		];

		if (event.direction && targetFragmentEntryLinkId) {
			const moveItemPayload = {
				fragmentEntryLinkId,
				targetBorder: getTargetBorder(event.direction),
				targetItemId: targetFragmentEntryLinkId,
				targetItemType: FRAGMENTS_EDITOR_ITEM_TYPES.fragment
			};

			moveItem(this.store, MOVE_FRAGMENT_ENTRY_LINK, moveItemPayload);
		}
	}

	/**
	 * @private
	 * @review
	 */
	_initializeDragAndDrop() {
		if (this._dragDrop) {
			this._dragDrop.dispose();
		}

		this._dragDrop = new DragDrop(
			{
				autoScroll: true,
				dragPlaceholder: Drag.Placeholder.CLONE,
				handles: '.fragments-editor__drag-handler',
				sources: '.fragments-editor__drag-source--fragment, .fragments-editor__drag-source--layout',
				targets: '.fragments-editor__drop-target--fragment, .fragments-editor__drop-target--layout'
			}
		);

		this._dragDrop.on(
			DragDrop.Events.DRAG,
			this._handleDrag.bind(this)
		);

		this._dragDrop.on(
			DragDrop.Events.END,
			this._handleDrop.bind(this)
		);

		this._dragDrop.on(
			DragDrop.Events.TARGET_LEAVE,
			this._handleDragEnd.bind(this)
		);
	}

}

/**
 * State definition.
 * @review
 * @static
 * @type {!Object}
 */
FragmentEntryLinkList.STATE = {

	/**
	 * Internal DragDrop instance.
	 * @default null
	 * @instance
	 * @memberOf FragmentEntryLinkList
	 * @review
	 * @type {object|null}
	 */
	_dragDrop: Config.internal()
		.value(null)
};

const ConnectedFragmentEntryLinkList = getConnectedComponent(
	FragmentEntryLinkList,
	[
		'activeItemId',
		'activeItemType',
		'dropTargetBorder',
		'dropTargetItemId',
		'dropTargetItemType',
		'hoveredItemId',
		'hoveredItemType',
		'layoutData'
	]
);

Soy.register(ConnectedFragmentEntryLinkList, templates);

export {ConnectedFragmentEntryLinkList, FragmentEntryLinkList};
export default ConnectedFragmentEntryLinkList;