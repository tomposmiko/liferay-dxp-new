import Component from 'metal-component';
import Soy from 'metal-soy';
import getConnectedComponent from '../../store/ConnectedComponent.es';
import templates from './SegmentSelector.soy';
import {CHANGE_SEGMENT_ID} from '../../actions/actions.es';

/**
 * SegmentSelector
 */
class SegmentSelector extends Component {

	/**
	 * @param {object} event
	 * @private
	 * @review
	 */
	_handleSegmentChange(event) {
		const {value} = event.target;

		this.store.dispatchAction(
			CHANGE_SEGMENT_ID,
			{
				segmentId: value
			}
		);
	}

}

const ConnectedSegmentSelector = getConnectedComponent(
	SegmentSelector,
	[
		'classPK',
		'portletNamespace',
		'segmentId',
		'segments'
	]
);

Soy.register(ConnectedSegmentSelector, templates);

export {ConnectedSegmentSelector};
export default ConnectedSegmentSelector;