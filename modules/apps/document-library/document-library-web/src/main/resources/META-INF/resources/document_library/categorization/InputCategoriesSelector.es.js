import 'clay-multi-select';
import Component from 'metal-component';
import {Config} from 'metal-state';
import Soy from 'metal-soy';
import templates from './InputCategoriesSelector.soy';

class InputCategoriesSelector extends Component {

	/**
	 * @inheritDoc
	 */
	attached() {
		this.refs.categoriesSelector.on('buttonClicked', this._handleButtonClicked.bind(this));
	}

	/**
	 * Opens an itemSelectorDialog to choose the categories.
	 *
	 */
	_handleButtonClicked() {
		AUI().use(
			'liferay-item-selector-dialog',
			A => {
				let categories = this.categories;
				const selectedCategoriesIds = categories.map(category => category.value);

				const uri = A.Lang.sub(
					decodeURIComponent(this.selectCategoriesUrl),
					{
						selectedCategories: selectedCategoriesIds,
						singleSelect: !this.multiValued,
						vocabularyIds: this.vocabularyId
					}
				);

				const itemSelectorDialog = new A.LiferayItemSelectorDialog(
					{
						dialogClasses: this.dialogClasses,
						eventName: this.eventName,
						'strings.add': Liferay.Language.get('save'),
						title: Liferay.Language.get('select-categories'),
						url: uri
					}
				);

				itemSelectorDialog.on('selectedItemChange', this._onSelectedItemChange.bind(this));

				itemSelectorDialog.open();
			}
		);
	}

	/**
	 * Updates the input with the selected categories.
	 *
	 * @param  {Event} event
	 */
	_onSelectedItemChange(event) {
		let categories = this.categories;

		const data = event.newVal;

		if (data) {
			for (const key in data) {
				const existingCategory = categories.find(
					item => item.label === key
				);

				if (existingCategory && data[key].unchecked) {
					this.categories = categories.filter(
						item => item.label !== existingCategory.label
					);
				}

				if (!existingCategory) {
					categories.push(
						{
							label: data[key].value,
							value: parseFloat(data[key].categoryId)
						}
					);

					this.categories = categories;
				}
			}
		}
	}

}

/**
 * State definition.
 * @ignore
 * @static
 * @type {!Object}
 */
InputCategoriesSelector.STATE = {

	/**
	 * Categories of the selected fileEntries.
	 *
	 * @instance
	 * @memberof EditCategories
	 * @review
	 * @type {List<String>}
	 */
	categories: Config.array(),

	/**
	 * Classes to add to the categories selector dialog
	 * @type {String}
	 */
	dialogClasses: Config.string(),

	/**
	 * Name of the event that will be dispatched when the
	 * category selector dialog is closed
	 */
	eventName: Config.string().required,

	/**
	 * Wether vocabulary can have several
	 * categories or not.
	 *
	 * @type {Boolean}
	 */
	multiValued: Config.bool().value(true),

	/**
	 * Url to the categories selector page
	 * @type {String}
	 */
	selectCategoriesUrl: Config.string().required(),

	/**
	 * Path to images.
	 *
	 * @instance
	 * @memberof EditCategories
	 * @review
	 * @type {String}
	 */
	spritemap: Config.string().required(),

	/**
	 * Vocabulary Id
	 * @type {Number}
	 */
	vocabularyId: Config.number().required(),

	/**
	 * Vocabulary name
	 * @type {String}
	 */
	vocabularyName: Config.string().required()
};

// Register component

Soy.register(InputCategoriesSelector, templates);

export default InputCategoriesSelector;