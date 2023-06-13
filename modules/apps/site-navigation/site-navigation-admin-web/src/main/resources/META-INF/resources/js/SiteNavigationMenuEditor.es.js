import {dom} from 'metal-dom';
import {Drag, DragDrop} from 'metal-drag-drop';
import State, {Config} from 'metal-state';

import SiteNavigationMenu from './SiteNavigationMenu.es';

import {
	MENU_ITEM_CLASSNAME,
	MENU_ITEM_CONTENT_CLASSNAME,
	MENU_ITEM_DRAG_ICON_CLASSNAME,
	SiteNavigationMenuItem
} from './SiteNavigationMenuItem.es';

const KEYS = {
	ARROW_DOWN: 'ArrowDown',
	ARROW_LEFT: 'ArrowLeft',
	ARROW_RIGHT: 'ArrowRight',
	ARROW_UP: 'ArrowUp',
	ENTER: 'Enter',
	SPACEBAR: ' '
};

/**
 *	Site navigation menu editor component.
 */

class SiteNavigationMenuEditor extends State {

	/**
	 * @inheritDoc
	 */

	constructor(config, ...args) {
		super(config, ...args);

		this.setState(config);

		this._dragDrop = new DragDrop(
			{
				dragPlaceholder: Drag.Placeholder.CLONE,
				handles: `.${MENU_ITEM_DRAG_ICON_CLASSNAME}`,
				sources: `.${MENU_ITEM_CLASSNAME}`,
				targets: `.${MENU_ITEM_CLASSNAME}`
			}
		);

		this._dragDrop.on(
			DragDrop.Events.DRAG,
			this._handleDragItem.bind(this)
		);

		this._dragDrop.on(
			Drag.Events.START,
			this._handleDragStart.bind(this)
		);

		this._dragDrop.on(
			DragDrop.Events.END,
			this._handleDropItem.bind(this)
		);

		this._itemClickHandler = dom.on(
			`.${MENU_ITEM_CONTENT_CLASSNAME}`,
			'click',
			this._handleItemClick.bind(this)
		);

		this._itemKeyUpHandler = dom.on(
			`.${MENU_ITEM_CLASSNAME}`,
			'keyup',
			this._handleItemKeyUp.bind(this)
		);
	}

	/**
	 * @inheritDoc
	 */

	dispose(...args) {
		if (this._dragDrop) {
			this._dragDrop.dispose();
			this._dragDrop = null;
		}

		if (this._itemClickHandler) {
			this._itemClickHandler.removeListener();
			this._itemClickHandler = null;
		}

		if (this._itemKeyUpHandler) {
			this._itemKeyUpHandler.removeListener();
			this._itemKeyUpHandler = null;
		}

		super.dispose(...args);
	}

	/**
	 * This is called when user drags the item across the container.
	 *
	 * @param {!object} data Drag event data
	 * @param {!Event} event Drag event
	 * @private
	 */

	_handleDragItem(data, event) {
		const placeholderMenuItem = data.placeholder;
		const sourceMenuItem = data.source;

		const nearestMenuItem = SiteNavigationMenu.getNearestMenuItem(
			sourceMenuItem,
			placeholderMenuItem
		);

		if (
			placeholderMenuItem && SiteNavigationMenuItem.isMenuItem(placeholderMenuItem) &&
			sourceMenuItem && SiteNavigationMenuItem.isMenuItem(sourceMenuItem) &&
			nearestMenuItem && SiteNavigationMenuItem.isMenuItem(nearestMenuItem)
		) {
			const nested = SiteNavigationMenu.shouldBeNested(
				placeholderMenuItem,
				nearestMenuItem
			);

			const over = SiteNavigationMenu.isOver(
				placeholderMenuItem,
				nearestMenuItem
			);

			if (!over && nested) {
				SiteNavigationMenu.insertAtPosition(
					nearestMenuItem,
					sourceMenuItem,
					0
				);
			}
			else {
				const nearestMenuItemParent = SiteNavigationMenuItem.getParent(
					nearestMenuItem
				);

				const nearestMenuItemIndex = SiteNavigationMenuItem.getChildren(nearestMenuItemParent).indexOf(nearestMenuItem) + (over ? 0 : 1);

				SiteNavigationMenu.insertAtPosition(
					nearestMenuItemParent,
					sourceMenuItem,
					nearestMenuItemIndex
				);
			}
		}
	}

	/**
	 * This is called when user starts to drag the item across the container.
	 *
	 * @param {!object} data Drag event data
	 * @param {!Event} event Drag event
	 * @private
	 */

	_handleDragStart(data, event) {
		const menuItem = event.target.getActiveDrag();

		SiteNavigationMenuItem.setDragging(menuItem, true);
	}

	/**
	 * This is called when user drops the item on the container.
	 *
	 * @param {!object} data Drop event data
	 * @param {!Event} event Drop event
	 * @private
	 */

	_handleDropItem(data, event) {
		event.preventDefault();

		const menuItem = data.source;
		const menuItemId = SiteNavigationMenuItem.getId(menuItem);

		const menuItemIndex = SiteNavigationMenuItem
			.getSiblings(menuItem)
			.indexOf(menuItem);

		const menuItemParentId = SiteNavigationMenuItem.getId(
			SiteNavigationMenuItem.getParent(menuItem)
		);

		this._updateParentAndOrder(
			{
				dragOrder: menuItemIndex,
				parentId: menuItemParentId,
				siteNavigationMenuItemId: menuItemId
			}
		);

		SiteNavigationMenuItem.setDragging(menuItem, false);
	}

	/**
	 * This is called when user clicks on menu item.
	 *
	 * @param {!Event} event Click event data
	 * @private
	 */

	_handleItemClick(event) {
		SiteNavigationMenuItem.setSelected(
			SiteNavigationMenuItem.getFromContentElement(
				event.delegateTarget
			),
			true
		);
	}

	/**
	 * This is called when user presses a key on menu item.
	 *
	 * @param {!Event} event KeyUp event data
	 * @private
	 */

	_handleItemKeyUp(event) {
		event.stopPropagation();

		const menuItem = event.delegateTarget;
		const menuItemIndex = SiteNavigationMenuItem
			.getSiblings(menuItem)
			.indexOf(menuItem);

		const menuItemParent = SiteNavigationMenuItem.getParent(menuItem);

		let layoutModified = false;

		if ((event.key === KEYS.ENTER) || (event.key === KEYS.SPACEBAR)) {
			menuItem.click();
		}
		else if (event.key === KEYS.ARROW_LEFT) {
			const menuItemParentIndex = SiteNavigationMenuItem
				.getSiblings(menuItemParent)
				.indexOf(menuItemParent);

			const menuItemGrandParent = SiteNavigationMenuItem.getParent(
				menuItemParent
			);

			if (menuItemParentIndex !== -1) {
				SiteNavigationMenu.insertAtPosition(
					menuItemGrandParent,
					menuItem,
					menuItemParentIndex + 1
				);
			}

			layoutModified = true;
		}
		else if (event.key === KEYS.ARROW_UP && menuItemIndex > 0) {
			SiteNavigationMenu.insertAtPosition(
				menuItemParent,
				menuItem,
				menuItemIndex - 1
			);

			layoutModified = true;
		}
		else if (event.key === KEYS.ARROW_RIGHT && menuItemIndex > 0) {
			const previousSibling = SiteNavigationMenuItem
				.getSiblings(menuItem)[menuItemIndex - 1];

			SiteNavigationMenu.insertAtPosition(
				previousSibling,
				menuItem,
				Infinity
			);

			layoutModified = true;
		}
		else if (event.key === KEYS.ARROW_DOWN) {
			SiteNavigationMenu.insertAtPosition(
				menuItemParent,
				menuItem,
				menuItemIndex + 2
			);

			layoutModified = true;
		}

		if (layoutModified) {
			const siteNavigationMenuItemId = SiteNavigationMenuItem.getId(
				menuItem
			);

			this._updateParentAndOrder(
				{
					dragOrder: SiteNavigationMenuItem
						.getSiblings(menuItem)
						.indexOf(menuItem),

					parentId: SiteNavigationMenuItem.getId(
						SiteNavigationMenuItem.getParent(menuItem)
					),

					siteNavigationMenuItemId
				}
			);

			requestAnimationFrame(
				() => {
					const modifiedMenuItem = SiteNavigationMenuItem.getFromId(
						siteNavigationMenuItemId
					);

					modifiedMenuItem.focus();
				}
			);
		}
	}

	/**
	 * Send layout information to the server and returns a promise
	 * that resolves after finishing.
	 * @param {{
	 *   dragOrder: !string,
	 *   parentId: !string,
	 *   siteNavigationMenuItemId: !string
	 * }} data
	 * @private
	 * @return {Promise}
	 */

	_updateParentAndOrder(data) {
		const formData = new FormData();

		formData.append(
			`${this.namespace}siteNavigationMenuItemId`,
			data.siteNavigationMenuItemId
		);

		formData.append(
			`${this.namespace}parentSiteNavigationMenuItemId`,
			data.parentId
		);

		formData.append(
			`${this.namespace}order`,
			data.dragOrder
		);

		return fetch(
			this.editSiteNavigationMenuItemParentURL,
			{
				body: formData,
				credentials: 'include',
				method: 'POST'
			}
		);
	}
}

/**
 * State definition.
 * @type {!Object}
 * @static
 */

SiteNavigationMenuEditor.STATE = {

	/**
	 * URL for edit site navigation menu item parent action.
	 *
	 * @default undefined
	 * @instance
	 * @memberOf SiteNavigationMenuEditor
	 * @type {!string}
	 */

	editSiteNavigationMenuItemParentURL: Config.string().required(),

	/**
	 * Portlet namespace to use in edit action.
	 *
	 * @default undefined
	 * @instance
	 * @memberOf SiteNavigationMenuEditor
	 * @type {!string}
	 */

	namespace: Config.string().required(),

	/**
	 * Internal DragDrop instance.
	 *
	 * @default null
	 * @instance
	 * @memberOf SiteNavigationMenuEditor
	 * @type {State}
	 */

	_dragDrop: Config.internal().value(null)
};

export {SiteNavigationMenuEditor};
export default SiteNavigationMenuEditor;