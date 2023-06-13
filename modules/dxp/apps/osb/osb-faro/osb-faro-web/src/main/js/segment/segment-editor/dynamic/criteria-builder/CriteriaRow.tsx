import AccountInput from '../inputs/AccountInput';
import autobind from 'autobind-decorator';
import BehaviorInput from '../inputs/BehaviorInput';
import BooleanInput from '../inputs/BooleanInput';
import CustomBooleanInput from '../inputs/CustomBooleanInput';
import CustomDateInput from '../inputs/CustomDateInput';
import CustomDateTimeInput from '../inputs/CustomDateTimeInput';
import CustomNumberInput from '../inputs/CustomNumberInput';
import DateInput from '../inputs/DateInput';
import DateTimeInput from '../inputs/DateTimeInput';
import DurationInput from '../inputs/DurationInput';
import EventInput from '../inputs/EventInput';
import Form from 'shared/components/form';
import GeolocationInput from '../inputs/GeolocationInput';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import IndividualSelectInput from '../inputs/IndividualSelectInput';
import InterestBooleanInput from '../inputs/InterestBooleanInput';
import NumberInput from '../inputs/NumberInput';
import OrganizationSelectInput from '../inputs/OrganizationSelectInput';
import OrganizationTextInput from '../inputs/OrganizationTextInput';
import React from 'react';
import RowActions from 'shared/components/RowActions';
import SessionInput from '../inputs/SessionInput';
import StringInput from '../inputs/StringInput';
import {
	AddProperty,
	withReferencedObjectsConsumer
} from '../context/referencedObjects';
import {ClaySelectWithOption} from '@clayui/select';
import {compose} from 'redux';
import {connect, ConnectedProps} from 'react-redux';
import {
	ConnectDragPreview,
	ConnectDragSource,
	ConnectDropTarget,
	DragSource as dragSource,
	DropTarget as dropTarget,
	DropTargetMonitor
} from 'react-dnd';
import {
	createNewGroup,
	findPropertyByCriterion,
	generateRowId,
	getSupportedOperatorsFromType,
	isOfKnownType,
	isValid
} from '../utils/utils';
import {Criterion, CriterionGroup, OnMove, Operator} from '../utils/types';
import {DragTypes} from '../utils/drag-types';
import {get} from 'lodash';
import {
	isKnown,
	isUnknown,
	PropertyTypes,
	RelationalOperators
} from '../utils/constants';
import {Map} from 'immutable';
import {Property} from 'shared/util/records';
import {RootState} from 'shared/store';

const acceptedDragTypes = [DragTypes.CriteriaRow, DragTypes.Property];

/**
 * Prevents rows from dropping onto itself and adding properties to not matching
 * contributors.
 * This method must be called `canDrop`.
 */
const canDrop = (
	{
		criteriaGroupId: destGroupId,
		index: destIndex
	}: {
		criteriaGroupId: string;
		index: number;
	},
	monitor: DropTargetMonitor
): boolean => {
	const {criteriaGroupId: startGroupId, index: startIndex} =
		monitor.getItem();

	return destGroupId !== startGroupId || destIndex !== startIndex;
};

/**
 * Implements the behavior of what will occur when an item is dropped.
 * Items dropped on top of rows will create a new grouping.
 * This method must be called `drop`.
 */
const drop = (
	{
		addProperty,
		criteriaGroupId: destGroupId,
		criterion,
		index: destIndex,
		onChange,
		onMove
	}: {
		addProperty: AddProperty;
		criteriaGroupId: string;
		criterion: Criterion;
		index: number;
		onChange: (newGroup: CriterionGroup) => void;
		onMove: OnMove;
	},
	monitor: DropTargetMonitor
): void => {
	const {
		criteriaGroupId: startGroupId,
		criterion: droppedCriterion,
		index: startIndex,
		property
	} = monitor.getItem();

	const {
		defaultValue,
		operatorName,
		propertyName,
		rowId,
		touched,
		type,
		valid,
		value
	} = droppedCriterion;

	if (property) {
		addProperty(property);
	}

	const droppedCriterionValue = isValid(value) ? value : defaultValue;

	const operators = getSupportedOperatorsFromType(type);

	const newCriterion = {
		operatorName: operatorName ? operatorName : operators[0].name,
		propertyName,
		rowId: rowId || generateRowId(),
		touched,
		valid,
		value: droppedCriterionValue
	} as Criterion;

	const itemType = monitor.getItemType();

	const newGroup = createNewGroup([criterion, newCriterion]);

	if (itemType === DragTypes.Property) {
		onChange(newGroup);
	} else if (itemType === DragTypes.CriteriaRow) {
		onMove(
			startGroupId,
			startIndex,
			destGroupId,
			destIndex,
			newGroup,
			true
		);
	}
};

/**
 * Passes the required values to the drop target.
 * This method must be called `beginDrag`.
 * @param {Object} props Component's current props
 * @returns {Object} The props to be passed to the drop target.
 */
function beginDrag({criteriaGroupId, criterion, index}) {
	return {criteriaGroupId, criterion, index};
}

const connector = connect((store: RootState, {groupId}: {groupId: string}) => ({
	timeZoneId: store.getIn([
		'projects',
		groupId,
		'data',
		'timeZone',
		'timeZoneId'
	])
}));

type PropsFromRedux = ConnectedProps<typeof connector>;

interface ICriteriaRowProps extends PropsFromRedux {
	addProperty: AddProperty;
	canDrop: boolean;
	channelId: string;
	connectDragPreview: ConnectDragPreview;
	connectDragSource: ConnectDragSource;
	connectDropTarget: ConnectDropTarget;
	criteriaGroupId: string;
	criterion: Criterion;
	dragging?: boolean;
	groupId: string;
	id?: string;
	hover?: boolean;
	index: number;
	onAdd: (index: number, criterion: Criterion) => void;
	onChange: (criterion: Criterion | Criterion[]) => void;
	onDelete: (index: number) => void;
	onMove: OnMove;
	referencedProperties: Map<string, Map<string, Property>>;
	timeZoneId: string;
}

interface ICriteriaRowState {
	selectedProperty: Property;
	supportedOperators: Operator[];
}

class CriteriaRow extends React.Component<
	ICriteriaRowProps,
	ICriteriaRowState
> {
	static defaultProps = {
		criterion: {}
	};

	constructor(props) {
		super(props);

		const selectedProperty = this.getSelectedProperty();

		const supportedOperators = selectedProperty
			? getSupportedOperatorsFromType(selectedProperty.type)
			: [];

		this.state = {
			selectedProperty,
			supportedOperators
		};
	}

	getSelectedOperator() {
		const {
			props: {
				criterion: {operatorName, value}
			},
			state: {supportedOperators}
		} = this;

		let operatorKey: Criterion['operatorName'] | 'is-known' | 'is-unknown' =
			operatorName;

		const valueNull = value === null;

		if (operatorName === RelationalOperators.EQ && valueNull) {
			operatorKey = isUnknown;
		} else if (operatorName === RelationalOperators.NE && valueNull) {
			operatorKey = isKnown;
		}

		const selectedOperator = supportedOperators.find(
			({key}) => key === operatorKey
		);

		return (
			selectedOperator || {
				key: operatorKey,
				label: operatorKey,
				name: operatorName
			}
		);
	}

	getSelectedProperty() {
		const {
			props: {criterion, referencedProperties}
		} = this;

		return findPropertyByCriterion(criterion, referencedProperties);
	}

	getValue(value, key) {
		if (isOfKnownType(key)) {
			return null;
		} else if (value === null) {
			return '';
		}

		return value;
	}

	@autobind
	handleDelete(event) {
		event.preventDefault();

		const {index, onDelete} = this.props;

		onDelete(index);
	}

	@autobind
	handleDuplicate(event) {
		event.preventDefault();

		const {criterion, index, onAdd} = this.props;

		onAdd(index + 1, {...criterion, rowId: generateRowId()});
	}

	@autobind
	handleOperatorChange(event) {
		const {value} = event.target;

		const {
			props: {criterion, onChange},
			state: {supportedOperators}
		} = this;

		const newVal = this.getValue(criterion.value, value);

		let params = {};

		if (isOfKnownType(value) || criterion.value === null) {
			params = {valid: isValid(newVal)};
		}

		onChange({
			...criterion,
			operatorName: supportedOperators.find(({key}) => key === value)
				.name,
			value: newVal,
			...params
		} as Criterion);
	}

	/**
	 * Updates the criteria with a criterion value change. The param 'value'
	 * will only be an array when selecting multiple entities (see
	 * {@link SelectEntityInput.js}). And in the case of an array, a new
	 * group with multiple criterion rows will be created.
	 * @param {Array|object} value The properties or list of objects with
	 * properties to update.
	 */
	@autobind
	handleTypedInputChange(value) {
		const {criterion, onChange} = this.props;

		if (Array.isArray(value)) {
			const items = value.map((item, i) => ({
				...criterion,
				...item,
				rowId: i === 0 ? criterion.rowId : generateRowId()
			}));

			onChange(items);
		} else {
			onChange({
				...criterion,
				...value
			});
		}
	}

	@autobind
	renderOperator() {
		const {supportedOperators} = this.state;

		const {key: selectedOperatorKey} = this.getSelectedOperator();

		const singleOption = supportedOperators.length === 1;

		return (
			<Form.GroupItem className='operator' label={singleOption} shrink>
				{singleOption ? (
					supportedOperators[0].label
				) : (
					<ClaySelectWithOption
						className='criterion-input operator-input'
						onChange={this.handleOperatorChange}
						options={supportedOperators.map(({key, label}) => ({
							label,
							value: key
						}))}
						value={selectedOperatorKey}
					/>
				)}
			</Form.GroupItem>
		);
	}

	renderValueInput() {
		const {
			props: {channelId, criterion, groupId, id, timeZoneId},
			state: {selectedProperty}
		} = this;

		const {label, options, type} = selectedProperty;

		const inputComponentsMap = {
			[PropertyTypes.Behavior]: BehaviorInput,
			[PropertyTypes.Boolean]: BooleanInput,
			[PropertyTypes.AccountNumber]: AccountInput,
			[PropertyTypes.AccountText]: AccountInput,
			[PropertyTypes.Date]: DateInput,
			[PropertyTypes.DateTime]: DateTimeInput,
			[PropertyTypes.Duration]: DurationInput,
			[PropertyTypes.Event]: EventInput,
			[PropertyTypes.Interest]: InterestBooleanInput,
			[PropertyTypes.Number]: NumberInput,
			[PropertyTypes.OrganizationBoolean]: CustomBooleanInput,
			[PropertyTypes.OrganizationNumber]: CustomNumberInput,
			[PropertyTypes.OrganizationSelectText]: OrganizationSelectInput,
			[PropertyTypes.OrganizationText]: OrganizationTextInput,
			[PropertyTypes.OrganizationDate]: CustomDateInput,
			[PropertyTypes.OrganizationDateTime]: CustomDateTimeInput,
			[PropertyTypes.SelectText]: IndividualSelectInput,
			[PropertyTypes.SessionDateTime]: CustomDateTimeInput,
			[PropertyTypes.SessionGeolocation]: GeolocationInput,
			[PropertyTypes.SessionNumber]: SessionInput,
			[PropertyTypes.SessionText]: SessionInput,
			[PropertyTypes.Text]: StringInput
		};

		const InputComponent: React.ElementType =
			inputComponentsMap[type || criterion.type] ||
			inputComponentsMap[PropertyTypes.Text];

		return (
			<InputComponent
				channelId={channelId}
				displayValue={label || ''}
				groupId={groupId}
				id={id}
				onChange={this.handleTypedInputChange}
				operatorRenderer={this.renderOperator}
				options={options}
				property={selectedProperty}
				timeZoneId={timeZoneId}
				touched={criterion.touched}
				valid={criterion.valid}
				value={criterion.value}
			/>
		);
	}

	render() {
		const {
			props: {
				canDrop,
				connectDragPreview,
				connectDragSource,
				connectDropTarget,
				dragging,
				hover
			},
			state: {selectedProperty}
		} = this;

		const classes = getCN('criterion-row-root', {
			'dnd-drag': dragging,
			'dnd-hover': hover && canDrop
		});

		return connectDropTarget(
			connectDragPreview(
				<div className={classes}>
					<div
						className={`color-stripe color--${get(
							selectedProperty,
							'propertyKey',
							'disabled'
						)}`}
					/>

					<div className='edit-container'>
						{connectDragSource(
							<div className='drag-icon'>
								<Icon symbol='drag' />
							</div>
						)}

						{selectedProperty ? (
							this.renderValueInput()
						) : (
							<div className='non-existent-property-message'>
								{Liferay.Language.get(
									'attribute-no-longer-exists'
								)}
							</div>
						)}

						<div className='actions'>
							<RowActions
								actions={[
									{
										label: Liferay.Language.get(
											'duplicate'
										),
										onClick: this.handleDuplicate
									},
									{
										label: Liferay.Language.get('delete'),
										onClick: this.handleDelete
									}
								]}
							/>
						</div>
					</div>
				</div>
			)
		);
	}
}

const CriteriaRowWithDrag = dragSource(
	DragTypes.CriteriaRow,
	{
		beginDrag
	},
	(connect, monitor) => ({
		connectDragPreview: connect.dragPreview(),
		connectDragSource: connect.dragSource(),
		dragging: monitor.isDragging()
	})
)(CriteriaRow);

export default compose<any>(
	connector,
	withReferencedObjectsConsumer,
	dropTarget(
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
	)
)(CriteriaRowWithDrag);
