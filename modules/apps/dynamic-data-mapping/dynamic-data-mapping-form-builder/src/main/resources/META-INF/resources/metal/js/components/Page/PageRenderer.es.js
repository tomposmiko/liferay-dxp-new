import 'clay-button';
import 'clay-dropdown';
import 'clay-modal';
import * as FormSupport from '../Form/FormSupport.es';
import Component from 'metal-component';
import core from 'metal';
import Position from 'metal-position';
import Soy from 'metal-soy';
import templates from './PageRenderer.soy.js';
import {Config} from 'metal-state';
import {dom} from 'metal-dom';
import {Drag} from 'metal-drag-drop';
import {pageStructure} from '../../util/config.es';
import {sub} from '../../util/strings.es';

class PageRenderer extends Component {
	static STATE = {

		/**
		 * @instance
		 * @memberof FormPage
		 * @type {?number}
		 */

		activePage: Config.number().value(0),

		/**
		 * @instance
		 * @memberof FormPage
		 * @type {?boolean}
		 */
		editable: Config.bool().value(false),

		/**
		 * @instance
		 * @memberof FormPage
		 * @type {?string}
		 */

		descriptionPlaceholder: Config.string().value(Liferay.Language.get('add-a-short-description-for-this-page')),

		/**
		 * @instance
		 * @memberof FormPage
		 * @type {?object}
		 */

		localizedTitle: Config.object().value(
			{
				en_US: ''
			}
		),

		/**
		 * @instance
		 * @memberof FormPage
		 * @type {?object}
		 */

		localizedDescription: Config.object().value(
			{
				en_US: ''
			}
		),

		/**
		 * @default []
		 * @instance
		 * @memberof FormRenderer
		 * @type {?array<object>}
		 */

		page: pageStructure.setter('_setPage'),

		/**
		 * @default 1
		 * @instance
		 * @memberof FormPage
		 * @type {?number}
		 */

		pageIndex: Config.number().value(0),

		/**
		 * @default undefined
		 * @instance
		 * @memberof FormRenderer
		 * @type {!string}
		 */

		spritemap: Config.string().required(),

		/**
		 * @instance
		 * @memberof FormPage
		 * @type {?string}
		 */

		titlePlaceholder: Config.string(),

		/**
		 * @default 1
		 * @instance
		 * @memberof FormPage
		 * @type {?number}
		 */

		total: Config.number().value(1)
	}

	willAttach() {
		this.titlePlaceholder = this._getTitlePlaceholder();
	}

	/**
	 * Remember to destroy the dragDrop instance when the component is disposed
	 */
	attached() {
		if (this.editable) {
			this._bindLayoutBuilder();
		}
	}

	disposed() {
		if (this._dragAndDrop) {
			this._dragAndDrop.dispose();
		}
	}

	prepareStateForRender(states) {
		return {
			...states,
			empty: this._isEmptyPage(states.page)
		};
	}

	willReceiveState(nextState) {
		this.titlePlaceholder = this._getTitlePlaceholder();

		if (nextState.page && this.editable) {
			this._dragAndDrop.setState(
				{
					targets: '.ddm-resize-drop'
				}
			);
		}

		return nextState;
	}

	_bindLayoutBuilder() {
		this._dragAndDrop = new Drag(
			{
				axis: 'x',
				sources: '.ddm-resize-handle',
				useShim: true
			}
		);

		this._dragAndDrop.on(Drag.Events.START, this._handleDragStartEvent.bind(this));
		this._dragAndDrop.on(Drag.Events.DRAG, this._handleDragEvent.bind(this));
	}

	_handleDragStartEvent() {
		this._lastResizeColumn = -1;
	}

	_handleDragEvent(event) {
		const columnNodes = Object.keys(this.refs)
			.filter(key => key.indexOf('resizeColumn') === 0)
			.map(key => this.refs[key]);
		const {source, x} = event;

		let distance = Infinity;
		let nearest;

		columnNodes.forEach(
			node => {
				const region = Position.getRegion(node);

				const currentDistance = Math.abs(x - region.left);

				if (currentDistance < distance) {
					distance = currentDistance;
					nearest = node;
				}
			}
		);

		if (nearest) {
			const column = Number(nearest.dataset.resizeColumn);
			const direction = source.classList.contains('ddm-resize-handle-left') ? 'left' : 'right';

			if (this._lastResizeColumn !== column) {
				this._lastResizeColumn = column;

				this.emit(
					'columnResized',
					{
						column,
						direction,
						source
					}
				);
			}
		}
	}

	/**
	 * @param {number} pageIndex
	 * @private
	 */

	_getTitlePlaceholder() {
		return sub(
			Liferay.Language.get('untitled-page-x-of-x'),
			[
				this.pageIndex + 1,
				this.total
			]
		);
	}

	/**
     * @param {!Event} event
     * @private
     */

	_handleDuplicateButtonClicked(event) {
		const index = FormSupport.getIndexes(
			dom.closest(event.target, '.col-ddm')
		);

		this.emit(
			'duplicateButtonClicked',
			{
				...index
			}
		);
	}

	/**
	 * @param {!Object} event
	 * @private
	 */

	_handleFieldBlurred(event) {
		this.emit('fieldBlurred', event);
	}

	/**
	 * @param {!Object} event
	 * @private
	 */

	_handleFieldEdited(event) {
		this.emit('fieldEdited', event);
	}

	/**
	 * @param {!Event} event
	 * @private
	 */

	_handleDeleteButtonClicked(event) {
		const index = FormSupport.getIndexes(
			dom.closest(event.target, '.col-ddm')
		);

		this.emit('deleteButtonClicked', index);
	}

	_handlePageDescriptionChanged(event) {
		const {page} = this;
		const {delegateTarget: {dataset, value}} = event;
		const pageIndex = parseInt(dataset.pageIndex, 10);

		this.emit(
			'updatePage',
			{
				page: {
					...page,
					description: value,
					localizedDescription: {
						...page.localizedDescription,
						[themeDisplay.getLanguageId()]: value
					}
				},
				pageIndex
			}
		);
	}

	/**
	 * @param {!Object} event
	 * @private
	 */

	_handlePageTitleChanged(event) {
		const {page} = this;
		const {delegateTarget: {dataset, value}} = event;
		const pageIndex = parseInt(dataset.pageIndex, 10);

		this.emit(
			'updatePage',
			{
				page: {
					...page,
					localizedTitle: {
						...page.localizedTitle,
						[themeDisplay.getLanguageId()]: value
					},
					title: value
				},
				pageIndex
			}
		);
	}

	/**
	 * @param {!Event} event
	 * @private
	 */

	_handleSelectFieldFocused({delegateTarget}) {
		const fieldNode = delegateTarget.parentElement.parentElement;
		const index = FormSupport.getIndexes(fieldNode);

		this.emit(
			'fieldClicked',
			{
				...index
			}
		);
	}

	_isEmptyPage({rows}) {
		let empty = false;

		if (!rows || !rows.length) {
			empty = true;
		}
		else {
			empty = !rows.some(
				({columns}) => {
					let hasFields = true;

					if (!columns) {
						hasFields = false;
					}
					else {
						hasFields = columns.some(column => column.fields.length);
					}
					return hasFields;
				}
			);
		}
		return empty;
	}

	_setPage(page) {
		if (core.isObject(page.description)) {
			page = {
				...page,
				description: page.description[themeDisplay.getLanguageId()]
			};
		}
		if (core.isObject(page.title)) {
			page = {
				...page,
				title: page.title[themeDisplay.getLanguageId()]
			};
		}

		return page;
	}
}

Soy.register(PageRenderer, templates);

export default PageRenderer;