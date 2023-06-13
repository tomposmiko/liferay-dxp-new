import Component from 'metal-component';
import Soy from 'metal-soy';
import {Config} from 'metal-state';

import './FragmentEntryLinkContent.es';
import templates from './FragmentEntryLink.soy';
import {REMOVE_FRAGMENT_ENTRY_LINK} from '../../actions/actions.es';
import {getConnectedComponent} from '../../store/ConnectedComponent.es';
import {getItemMoveDirection, getItemPath, itemIsInPath} from '../../utils/FragmentsEditorGetUtils.es';
import {FRAGMENTS_EDITOR_ITEM_TYPES} from '../../utils/constants';
import {removeItem, setIn} from '../../utils/FragmentsEditorUpdateUtils.es';
import {shouldUpdatePureComponent} from '../../utils/FragmentsEditorComponentUtils.es';

/**
 * FragmentEntryLink
 * @review
 */
class FragmentEntryLink extends Component {

	/**
	 * @inheritdoc
	 * @param {object} state
	 * @return {object}
	 * @review
	 */
	prepareStateForRender(state) {
		const hoveredPath = getItemPath(
			state.hoveredItemId,
			state.hoveredItemType,
			state.layoutData.structure
		);

		const fragmentEntryLinkInHoveredPath = itemIsInPath(
			hoveredPath,
			state.fragmentEntryLinkId,
			FRAGMENTS_EDITOR_ITEM_TYPES.fragment
		);

		const nextState = setIn(
			state,
			['_fragmentsEditorItemTypes'],
			FRAGMENTS_EDITOR_ITEM_TYPES
		);

		return setIn(
			nextState,
			['_hovered'],
			fragmentEntryLinkInHoveredPath
		);
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
	 * Handle fragment keyup event so it can emit when it
	 * should be moved or selected.
	 * @param {KeyboardEvent} event
	 * @private
	 * @review
	 */
	_handleFragmentKeyUp(event) {
		event.stopPropagation();

		const direction = getItemMoveDirection(event.which);

		this.emit(
			'moveFragment',
			{
				direction,
				fragmentEntryLinkId: this.fragmentEntryLinkId
			}
		);
	}

	/**
	 * Callback executed when the fragment remove button is clicked.
	 * @param {Object} event
	 * @private
	 */
	_handleFragmentRemoveButtonClick(event) {
		event.stopPropagation();

		removeItem(
			this.store,
			REMOVE_FRAGMENT_ENTRY_LINK,
			{
				fragmentEntryLinkId: this.fragmentEntryLinkId
			}
		);
	}

}

/**
 * State definition.
 * @review
 * @static
 * @type {!Object}
 */
FragmentEntryLink.STATE = {

	/**
	 * FragmentEntryLink id
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {!string}
	 */
	fragmentEntryLinkId: Config.string()
		.required(),

	/**
	 * Fragment name
	 * @default ''
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {string}
	 */
	name: Config.string()
		.value(''),

	/**
	 * Shows FragmentEntryLink control toolbar
	 * @default true
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {!bool}
	 */
	showControlBar: Config.bool()
		.value(true),

	/**
	 * CSS class to modify style
	 * @default undefined
	 * @instance
	 * @memberOf FragmentEntryLink
	 * @review
	 * @type {!string}
	 */
	styleModifier: Config.string()
};

const ConnectedFragmentEntryLink = getConnectedComponent(
	FragmentEntryLink,
	[
		'activeItemId',
		'activeItemType',
		'defaultLanguageId',
		'dropTargetItemId',
		'dropTargetItemType',
		'dropTargetBorder',
		'hoveredItemId',
		'hoveredItemType',
		'imageSelectorURL',
		'languageId',
		'layoutData',
		'portletNamespace',
		'selectedMappingTypes',
		'spritemap'
	]
);

Soy.register(ConnectedFragmentEntryLink, templates);

export {ConnectedFragmentEntryLink, FragmentEntryLink};

export default ConnectedFragmentEntryLink;