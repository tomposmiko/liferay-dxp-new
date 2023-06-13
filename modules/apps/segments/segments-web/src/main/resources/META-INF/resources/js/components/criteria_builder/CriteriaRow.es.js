import BooleanInput from '../inputs/BooleanInput.es';
import ClayAlert from '../shared/ClayAlert';
import ClayButton from '../shared/ClayButton.es';
import ClayIcon from '../shared/ClayIcon.es';
import ClaySelect from '../shared/ClaySelect.es';
import CollectionInput from '../inputs/CollectionInput.es';
import DateInput from '../inputs/DateInput.es';
import DateTimeInput from '../inputs/DateTimeInput.es';
import DecimalInput from '../inputs/DecimalInput.es';
import getCN from 'classnames';
import IntegerInput from '../inputs/IntegerInput.es';
import React, {Component} from 'react';
import SelectEntityInput from '../inputs/SelectEntityInput.es';
import StringInput from '../inputs/StringInput.es';
import ThemeContext from '../../ThemeContext.es';
import {
	createNewGroup,
	dateToInternationalHuman,
	getSupportedOperatorsFromType,
	objectToFormData
} from '../../utils/utils.es';
import {DragSource as dragSource, DropTarget as dropTarget} from 'react-dnd';
import {DragTypes} from '../../utils/drag-types.es';
import {PROPERTY_TYPES} from '../../utils/constants.es';
import {PropTypes} from 'prop-types';

const acceptedDragTypes = [
	DragTypes.CRITERIA_ROW,
	DragTypes.PROPERTY
];

/**
 * Prevents rows from dropping onto itself and adding properties to not matching
 * contributors.
 * This method must be called `canDrop`.
 * @param {Object} props Component's current props.
 * @param {DropTargetMonitor} monitor
 * @returns {boolean} True if the target should accept the item.
 */
function canDrop(props, monitor) {
	const {
		groupId: destGroupId,
		index: destIndex,
		propertyKey: contributorPropertyKey
	} = props;

	const {
		groupId: startGroupId,
		index: startIndex,
		propertyKey: sidebarItemPropertyKey
	} = monitor.getItem();

	return (destGroupId !== startGroupId || destIndex !== startIndex) &&
		contributorPropertyKey === sidebarItemPropertyKey;
}

/**
 * Implements the behavior of what will occur when an item is dropped.
 * Items dropped on top of rows will create a new grouping.
 * This method must be called `drop`.
 * @param {Object} props Component's current props.
 * @param {DropTargetMonitor} monitor
 */
function drop(props, monitor) {
	const {
		criterion,
		groupId: destGroupId,
		index: destIndex,
		onChange,
		onMove,
		supportedOperators,
		supportedPropertyTypes
	} = props;

	const {
		criterion: droppedCriterion,
		groupId: startGroupId,
		index: startIndex
	} = monitor.getItem();

	const {
		defaultValue,
		displayValue,
		operatorName,
		propertyName,
		type,
		value
	} = droppedCriterion;

	const droppedCriterionValue = value || defaultValue;

	const operators = getSupportedOperatorsFromType(
		supportedOperators,
		supportedPropertyTypes,
		type
	);

	const newCriterion = {
		displayValue,
		operatorName: operatorName ?
			operatorName :
			operators[0].name,
		propertyName,
		value: droppedCriterionValue
	};

	const itemType = monitor.getItemType();

	const newGroup = createNewGroup([criterion, newCriterion]);

	if (itemType === DragTypes.PROPERTY) {
		onChange(newGroup);
	}
	else if (itemType === DragTypes.CRITERIA_ROW) {
		onMove(
			startGroupId,
			startIndex,
			destGroupId,
			destIndex,
			newGroup,
			true
		);
	}
}

/**
 * Passes the required values to the drop target.
 * This method must be called `beginDrag`.
 * @param {Object} props Component's current props
 * @returns {Object} The props to be passed to the drop target.
 */
function beginDrag({criterion, groupId, index, propertyKey}) {
	return {criterion, groupId, index, propertyKey};
}

class CriteriaRow extends Component {
	static contextType = ThemeContext;

	static propTypes = {
		canDrop: PropTypes.bool,
		connectDragPreview: PropTypes.func,
		connectDragSource: PropTypes.func,
		connectDropTarget: PropTypes.func,
		criterion: PropTypes.object,
		dragging: PropTypes.bool,
		editing: PropTypes.bool,
		entityName: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		hover: PropTypes.bool,
		index: PropTypes.number.isRequired,
		modelLabel: PropTypes.string,
		onAdd: PropTypes.func.isRequired,
		onChange: PropTypes.func.isRequired,
		onDelete: PropTypes.func.isRequired,
		onMove: PropTypes.func.isRequired,
		propertyKey: PropTypes.string.isRequired,
		supportedOperators: PropTypes.array,
		supportedProperties: PropTypes.array,
		supportedPropertyTypes: PropTypes.object
	};

	static defaultProps = {
		criterion: {},
		editing: true,
		supportedOperators: [],
		supportedProperties: [],
		supportedPropertyTypes: {}
	};

	componentDidMount() {
		const {
			criterion: {displayValue, propertyName, value},
			supportedProperties
		} = this.props;

		this._selectedProperty = this._getSelectedItem(
			supportedProperties,
			propertyName
		);

		if (this._selectedProperty.type === PROPERTY_TYPES.ID &&
			value &&
			!displayValue
		) {
			this._fetchEntityName();
		}
	}

	_fetchEntityName = () => {
		const {criterion, entityName, onChange} = this.props;

		const {propertyName, value} = criterion;

		const data = Liferay.Util.ns(
			this.context.namespace,
			{
				entityName,
				fieldName: propertyName,
				fieldValue: value
			}
		);

		fetch(
			this.context.requestFieldValueNameURL,
			{
				body: objectToFormData(data),
				method: 'POST'
			}
		)
			.then(
				response => response.text()
			)
			.then(
				displayValue => {
					onChange({...criterion, displayValue});
				}
			);
	}

	_getReadableCriteriaString = (
		{
			propertyLabel,
			operatorLabel,
			value,
			type,
			error
		}
	) => {
		const parsedValue = (type === PROPERTY_TYPES.DATE || type === PROPERTY_TYPES.DATE_TIME) ?
			dateToInternationalHuman(value) :
			value;

		return (
			<span>
				<b className="mr-1 text-dark">
					{propertyLabel}
				</b>
				<span className="operator mr-1">
					{operatorLabel}
				</span>
				<b>
					{parsedValue}
				</b>
			</span>
		);
	}

	/**
	 * Gets the selected item object with a `name` and `label` property for a
	 * selection input. If one isn't found, a new object is returned using the
	 * idSelected for name and label.
	 * @param {Array} list The list of objects to search through.
	 * @param {string} idSelected The name to match in each object in the list.
	 * @return {object} An object with a `name`, `label` and `type` property.
	 */
	_getSelectedItem = (list, idSelected) => {
		const selectedItem = list.find(item => item.name === idSelected);

		return selectedItem ?
			selectedItem :
			{
				label: idSelected,
				name: idSelected,
				notFound: true,
				type: PROPERTY_TYPES.STRING
			};
	}

	_handleDelete = event => {
		event.preventDefault();

		const {index, onDelete} = this.props;

		onDelete(index);
	}

	_handleDuplicate = event => {
		event.preventDefault();

		const {criterion, index, onAdd} = this.props;

		onAdd(index + 1, criterion);
	}

	_handleInputChange = propertyName => event => {
		const {criterion, onChange} = this.props;

		onChange(
			{
				...criterion,
				[propertyName]: event.target.value
			}
		);
	};

	/**
	 * Updates the criteria with a criterion value change. The param 'value'
	 * will only be an array when selecting multiple entities (see
	 * {@link SelectEntityInput.es.js}). And in the case of an array, a new
	 * group with multiple criterion rows will be created.
	 * @param {Array|object} value The properties or list of objects with
	 * properties to update.
	 */
	_handleTypedInputChange = value => {
		const {criterion, onChange} = this.props;

		if (Array.isArray(value)) {
			const items = value.map(
				item => ({
					...criterion,
					...item
				})
			);

			onChange(createNewGroup(items));
		}
		else {
			onChange(
				{
					...criterion,
					...value
				}
			);
		}
	}

	_renderValueInput = (selectedProperty, value, disabled) => {
		const inputComponentsMap = {
			[PROPERTY_TYPES.BOOLEAN]: BooleanInput,
			[PROPERTY_TYPES.COLLECTION]: CollectionInput,
			[PROPERTY_TYPES.DATE]: DateInput,
			[PROPERTY_TYPES.DATE_TIME]: DateTimeInput,
			[PROPERTY_TYPES.DOUBLE]: DecimalInput,
			[PROPERTY_TYPES.ID]: SelectEntityInput,
			[PROPERTY_TYPES.INTEGER]: IntegerInput,
			[PROPERTY_TYPES.STRING]: StringInput
		};

		const InputComponent = inputComponentsMap[selectedProperty.type] ||
			inputComponentsMap[PROPERTY_TYPES.STRING];

		return (
			<InputComponent
				disabled={disabled}
				displayValue={this.props.criterion.displayValue || ''}
				onChange={this._handleTypedInputChange}
				options={selectedProperty.options}
				selectEntity={selectedProperty.selectEntity}
				value={value}
			/>
		);
	}

	_renderErrorMessage() {
		const {editing} = this.props;
		const message = editing ?
			Liferay.Language.get('criteria-error-message-edit') :
			Liferay.Language.get('criteria-error-message-view');

		return (
			<ClayAlert
				className="bg-transparent p-1 mt-1 border-0"
				message={message}
				title={Liferay.Language.get('error')}
				type="danger"
			/>
		);
	}

	_renderEditContainer(
		{
			error,
			propertyLabel,
			selectedOperator,
			selectedProperty,
			value
		}
	) {
		const {connectDragSource, supportedOperators, supportedPropertyTypes} = this.props;

		const propertyType = selectedProperty ? selectedProperty.type : '';

		const filteredSupportedOperators = getSupportedOperatorsFromType(
			supportedOperators,
			supportedPropertyTypes,
			propertyType
		);

		const disabledInput = !!error;

		return (
			<div className="edit-container">
				{connectDragSource(
					<div className="drag-icon">
						<ClayIcon iconName="drag" />
					</div>
				)}

				<span className="criterion-string">
					<b>{propertyLabel}</b>
				</span>

				<ClaySelect
					className="criterion-input operator-input form-control"
					disabled={disabledInput}
					onChange={this._handleInputChange(
						'operatorName'
					)}
					options={filteredSupportedOperators.map(
						({label, name}) => ({
							label,
							value: name
						})
					)}
					selected={selectedOperator && selectedOperator.name}
				/>

				{this._renderValueInput(selectedProperty, value, disabledInput)}

				{error ?
					<ClayButton
						label={Liferay.Language.get('delete')}
						onClick={this._handleDelete}
						style="outline-danger"
					/> :
					<React.Fragment>
						<ClayButton
							borderless
							iconName="paste"
							monospaced
							onClick={this._handleDuplicate}
						/>

						<ClayButton
							borderless
							iconName="times-circle"
							monospaced
							onClick={this._handleDelete}
						/>
					</React.Fragment>
				}
			</div>
		);
	}

	render() {
		const {
			canDrop,
			connectDragPreview,
			connectDropTarget,
			criterion,
			dragging,
			editing,
			hover,
			supportedOperators,
			supportedProperties
		} = this.props;

		const selectedOperator = this._getSelectedItem(
			supportedOperators,
			criterion.operatorName
		);

		const selectedProperty = this._getSelectedItem(
			supportedProperties,
			criterion.propertyName
		);

		const errorOnProperty = selectedProperty.notFound;
		const operatorLabel = selectedOperator ? selectedOperator.label : '';
		const propertyLabel = selectedProperty ? selectedProperty.label : '';

		const value = criterion ? criterion.value : '';

		const classes = getCN(
			'criterion-row-root',
			{
				'criterion-row-root-error': errorOnProperty,
				'dnd-drag': dragging,
				'dnd-hover': hover && canDrop
			}
		);

		return (
			<React.Fragment>
				{
					connectDropTarget(
						connectDragPreview(
							<div
								className={classes}
							>
								{editing ? this._renderEditContainer(
									{
										error: errorOnProperty,
										propertyLabel,
										selectedOperator,
										selectedProperty,
										value
									}
								) : (
									<span className="criterion-string">
										{this._getReadableCriteriaString(
											{
												error: errorOnProperty,
												operatorLabel,
												propertyLabel,
												type: selectedProperty.type,
												value: criterion.displayValue || value
											}
										)}
									</span>
								)}
							</div>
						)
					)
				}
				{
					errorOnProperty &&
					this._renderErrorMessage()
				}
			</React.Fragment>
		);
	}
}

const CriteriaRowWithDrag = dragSource(
	DragTypes.CRITERIA_ROW,
	{
		beginDrag
	},
	(connect, monitor) => ({
		connectDragPreview: connect.dragPreview(),
		connectDragSource: connect.dragSource(),
		dragging: monitor.isDragging()
	})
)(CriteriaRow);

export default dropTarget(
	acceptedDragTypes,
	{
		canDrop,
		drop
	},
	(connect, monitor) => ({
		canDrop: monitor.canDrop(),
		connectDropTarget: connect.dropTarget(),
		hover: monitor.isOver()
	})
)(CriteriaRowWithDrag);