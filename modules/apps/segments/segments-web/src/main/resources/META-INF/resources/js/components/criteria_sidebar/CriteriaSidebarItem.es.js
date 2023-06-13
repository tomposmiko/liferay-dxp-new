import ClayIcon from '../shared/ClayIcon.es';
import getCN from 'classnames';
import PropTypes from 'prop-types';
import React, {Component} from 'react';
import {DragSource as dragSource} from 'react-dnd';
import {DragTypes} from '../../utils/drag-types.es';
import {PROPERTY_TYPES} from '../../utils/constants.es';

const TYPE_ICON_MAP = {
	[PROPERTY_TYPES.BOOLEAN]: 'text',
	[PROPERTY_TYPES.COLLECTION]: 'table',
	[PROPERTY_TYPES.DATE]: 'date',
	[PROPERTY_TYPES.DATE_TIME]: 'date',
	[PROPERTY_TYPES.DOUBLE]: 'decimal',
	[PROPERTY_TYPES.ID]: 'diagram',
	[PROPERTY_TYPES.INTEGER]: 'integer',
	[PROPERTY_TYPES.STRING]: 'text'
};

/**
 * Passes the required values to the drop target.
 * This method must be called `beginDrag`.
 * @param {Object} props Component's current props
 * @returns {Object} The props to be passed to the drop target.
 */
function beginDrag({defaultValue, name, propertyKey, type}) {
	return {
		criterion: {
			defaultValue,
			propertyName: name,
			type
		},
		propertyKey
	};
}

class CriteriaSidebarItem extends Component {
	static propTypes = {
		className: PropTypes.string,
		connectDragSource: PropTypes.func,
		defaultValue: PropTypes.oneOfType(
			[
				PropTypes.string,
				PropTypes.number
			]
		),
		dragging: PropTypes.bool,
		label: PropTypes.string,
		name: PropTypes.string,
		propertyKey: PropTypes.string.isRequired,
		type: PropTypes.string
	};

	render() {
		const {
			className,
			connectDragSource,
			dragging,
			label,
			type
		} = this.props;

		const classes = getCN(
			'criteria-sidebar-item-root',
			{dragging},
			className,
		);

		return connectDragSource(
			<li className={classes}>
				<span className="inline-item">
					<ClayIcon iconName="drag" />
				</span>

				<span className="criteria-sidebar-item-type sticker sticker-light">
					<span className="inline-item">
						<ClayIcon iconName={TYPE_ICON_MAP[type] || 'text'} />
					</span>
				</span>

				{label}
			</li>
		);
	}
}

export default dragSource(
	DragTypes.PROPERTY,
	{
		beginDrag
	},
	(connect, monitor) => ({
		connectDragSource: connect.dragSource(),
		dragging: monitor.isDragging()
	})
)(CriteriaSidebarItem);