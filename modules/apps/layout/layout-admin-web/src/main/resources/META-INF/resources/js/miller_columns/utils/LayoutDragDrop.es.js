import {Drag, DragDrop} from 'metal-drag-drop';
import position from 'metal-position';
import State, {Config} from 'metal-state';

/**
 * Positions where elements can be dragged to
 * @review
 */

const DRAG_POSITIONS = {
	bottom: 'layout-column-item-drag-bottom',
	inside: 'layout-column-item-drag-inside',
	top: 'layout-column-item-drag-top'
};

/**
 * Possible drop target types
 * @review
 */

const DROP_TARGET_TYPES = {
	column: 'layout-column',
	item: 'layout-column-item'
};

/**
 * LayoutDragDrop
 */
class LayoutDragDrop extends State {

	/**
	 * @inheritDoc
	 * @review
	 */
	constructor(config, ...args) {
		super(config, ...args);

		this._initializeDragAndDrop();
	}

	/**
	 * @inheritDoc
	 * @review
	 */
	dispose() {
		this._dragDrop.dispose();
	}

	/**
	 * Adds a target to the list
	 * @param {!HTMLElement} target
	 * @review
	 */
	addTarget(target) {
		this._dragDrop.addTarget(target);
	}

	/**
	 * Callback that is executed when an item is being dragged.
	 * @param {!object} data
	 * @param {!MouseEvent} data.originalEvent
	 * @param {!HTMLElement} data.target
	 * @private
	 * @review
	 */
	_handleDrag(data) {
		const targetItem = data.target;

		if (targetItem) {
			const mouseY = data.originalEvent.clientY;
			const placeholderItemRegion = position.getRegion(data.placeholder);
			const sourceItemPlid = data.source.dataset.layoutColumnItemPlid;
			const targetItemRegion = position.getRegion(targetItem);

			let targetId = null;
			let targetType = null;

			if (targetItem.dataset.layoutColumnIndex) {
				targetId = targetItem.dataset.layoutColumnIndex;
				targetId = targetId === '0' ? null : targetId;
				targetType = DROP_TARGET_TYPES.column;
			}
			else if (targetItem.dataset.layoutColumnItemPlid) {
				targetId = targetItem.dataset.layoutColumnItemPlid;
				targetType = DROP_TARGET_TYPES.item;

				if (placeholderItemRegion.top > targetItemRegion.top &&
					placeholderItemRegion.bottom < targetItemRegion.bottom) {
					this._draggingItemPosition = DRAG_POSITIONS.inside;
				}
				else if (Math.abs(mouseY - targetItemRegion.top) <=
					Math.abs(mouseY - targetItemRegion.bottom)) {
					this._draggingItemPosition = DRAG_POSITIONS.top;
				}
				else {
					this._draggingItemPosition = DRAG_POSITIONS.bottom;
				}
			}

			this.emit(
				'dragLayoutColumnItem',
				{
					position: this._draggingItemPosition,
					sourceItemPlid,
					targetId,
					targetType
				}
			);
		}
	}

	/**
	 * Callback that is executed when a target is leaved.
	 * @private
	 * @review
	 */
	_handleDragEnd() {
		this.emit('leaveLayoutColumnItem');
	}

	/**
	 * Callback that is executed when a target starts being dragged.
	 * @param {object} data
	 * @param {MouseEvent} event
	 * @private
	 * @review
	 */
	_handleDragStart(data, event) {
		const sourceItemPlid = event.target.getActiveDrag().dataset.layoutColumnItemPlid;

		this.emit(
			'startMovingLayoutColumnItem',
			{
				sourceItemPlid
			}
		);
	}

	/**
	 * Callback that is executed when an item is dropped.
	 * @param {!object} data
	 * @param {!HTMLElement} data.source
	 * @param {HTMLElement} data.target
	 * @param {!MouseEvent} event
	 * @private
	 * @review
	 */
	_handleDrop(data, event) {
		event.preventDefault();

		const sourceItemPlid = data.source.dataset.layoutColumnItemPlid;
		let targetId = null;
		let targetType = null;

		if (data.target) {
			if (data.target.dataset.layoutColumnIndex) {
				targetId = data.target.dataset.layoutColumnIndex;
				targetId = targetId === '0' ? null : targetId;
				targetType = DROP_TARGET_TYPES.column;
			}
			else if (data.target.dataset.layoutColumnItemPlid) {
				targetId = data.target.dataset.layoutColumnItemPlid;
				targetType = DROP_TARGET_TYPES.item;
			}
		}

		this.emit(
			'dropLayoutColumnItem',
			{
				sourceItemPlid,
				targetId,
				targetType
			}
		);
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
				handles: '.layout-drag-handler',
				scrollContainers: '.layout-column, .layout-columns',
				sources: '.layout-drag-item',
				targets: '.layout-drop-target-item'
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
			Drag.Events.START,
			this._handleDragStart.bind(this)
		);

		this._dragDrop.on(
			DragDrop.Events.TARGET_LEAVE,
			this._handleDragEnd.bind(this)
		);
	}

}

/**
 * State definition.
 * @type {!Object}
 * @static
 */

LayoutDragDrop.STATE = {

	/**
	 * Internal DragDrop instance.
	 * @default null
	 * @instance
	 * @memberOf LayoutDragDrop
	 * @review
	 * @type {object|null}
	 */

	_dragDrop: Config.internal().value(null),

	/**
	 * Position of the dragging card
	 * @default undefined
	 * @instance
	 * @memberOf LayoutDragDrop
	 * @review
	 * @type {!string}
	 */

	_draggingItemPosition: Config.internal().string()
};

export {
	DRAG_POSITIONS,
	DROP_TARGET_TYPES,
	LayoutDragDrop
};
export default LayoutDragDrop;