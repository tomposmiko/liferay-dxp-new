import 'clay-button';
import 'clay-dropdown';
import 'clay-modal';
import {Config} from 'metal-state';
import {dom} from 'metal-dom';
import {pageStructure} from '../../util/config.es';
import {setLocalizedValue} from '../../util/i18n.es';
import {sub} from '../../util/strings.es';
import Component from 'metal-component';
import core from 'metal';
import FormSupport from '../Form/FormSupport.es';
import Soy from 'metal-soy';
import templates from './PageRenderer.soy.js';

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

		pageId: Config.number().value(0),

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

	prepareStateForRender(states) {
		return {
			...states,
			empty: this._isEmptyPage(states.page)
		};
	}

	willReceiveState() {
		this.titlePlaceholder = this._getTitlePlaceholder();
	}

	/**
	 * @param {Object} event
	 * @param {String} pageProperty
	 * @private
	 */

	_changePageForm({delegateTarget}, pageProperty) {
		const {value} = delegateTarget;

		const languageId = themeDisplay.getLanguageId();
		const page = {...this.page};

		setLocalizedValue(page, languageId, pageProperty, value);

		return page;
	}

	/**
	 * @param {number} pageId
	 * @private
	 */

	_getTitlePlaceholder() {
		return sub(
			Liferay.Language.get('untitled-page-x-of-x'),
			[
				this.pageId + 1,
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
	 * @param {!Object} data
	 * @private
	 */

	_handleFieldEdited(event) {
		this.emit('fieldEdited', event);
	}

	/**
	 * @param {!Event} event
	 * @private
	 */

	_handleModal(event) {
		event.stopPropagation();
		const index = FormSupport.getIndexes(
			dom.closest(event.target, '.col-ddm')
		);

		this.emit('deleteFieldClicked', index);
	}

	/**
	 * @param {!Event} event
	 * @private
	 */

	_handleOnClickResize() {}

	/**
	 * @param {!Object} event
	 * @private
	 */

	_handlePageDescriptionChanged(event) {
		const page = this._changePageForm(event, 'description');
		const {delegateTarget: {dataset}} = event;
		let {pageId} = dataset;

		pageId = parseInt(pageId, 10);

		this.emit(
			'updatePage',
			{
				page,
				pageId
			}
		);
	}

	/**
	 * @param {!Object} event
	 * @private
	 */

	_handlePageTitleChanged(event) {
		const page = this._changePageForm(event, 'title');
		const {delegateTarget: {dataset}} = event;
		let {pageId} = dataset;

		pageId = parseInt(pageId, 10);

		this.emit(
			'updatePage',
			{
				page,
				pageId
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