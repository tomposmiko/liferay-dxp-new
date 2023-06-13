import 'clay-icon';
import {Config} from 'metal-state';
import Component from 'metal-component';
import Soy from 'metal-soy';

import './AssetVocabularyCategoriesSelector.es';
import templates from './AssetCategoriesSelector.soy';

/**
 * AssetCategoriesSelector is a component wrapping the existing Clay's MultiSelect component
 * that offers the user a tag selection input
 */
class AssetCategoriesSelector extends Component {
	_handleInputFocus(event) {
		this.emit('inputFocus', event);
	}

	_handleSelectedItemsChange(event) {
		this.emit('selectedItemsChange', event);
	}
}

AssetCategoriesSelector.STATE = {

	/**
	 * Event name which fires when user selects a display page using item selector
	 * @default undefined
	 * @instance
	 * @memberof AssetCategoriesSelector
	 * @review
	 * @type {?string}
	 */

	eventName: Config.string(),

	/**
	 * The URL of a portlet to display the tags
	 * @default undefined
	 * @instance
	 * @memberof AssetCategoriesSelector
	 * @review
	 * @type {?string}
	 */

	portletURL: Config.string(),

	/**
	 * A comma separated version of the list of selected items
	 * @default undefined
	 * @instance
	 * @memberof AssetCategoriesSelector
	 * @review
	 * @type {?string}
	 */

	vocabularies: Config.array()
};

Soy.register(AssetCategoriesSelector, templates);

export {AssetCategoriesSelector};
export default AssetCategoriesSelector;