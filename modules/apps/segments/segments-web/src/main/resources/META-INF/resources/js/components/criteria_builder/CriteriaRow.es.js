/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import ClayAlert from '@clayui/alert';
import getCN from 'classnames';
import {fetch} from 'frontend-js-web';
import {PropTypes} from 'prop-types';
import React, {Component} from 'react';
import {DragSource as dragSource, DropTarget as dropTarget} from 'react-dnd';

import ThemeContext from '../../ThemeContext.es';
import {
	PROPERTY_TYPES,
	SUPPORTED_OPERATORS,
	SUPPORTED_PROPERTY_TYPES,
} from '../../utils/constants';
import {DragTypes} from '../../utils/drag-types';
import {
	createNewGroup,
	getSupportedOperatorsFromType,
	objectToFormData,
} from '../../utils/utils';
import CriteriaRowEditable from './CriteriaRowEditable.es';
import CriteriaRowReadable from './CriteriaRowReadable.es';

const acceptedDragTypes = [DragTypes.CRITERIA_ROW, DragTypes.PROPERTY];

const DISPLAY_VALUE_NOT_FOUND_ERROR = 'displayValue not found';

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
		propertyKey: contributorPropertyKey,
	} = props;

	const {
		groupId: startGroupId,
		index: startIndex,
		propertyKey: sidebarItemPropertyKey,
	} = monitor.getItem();

	return (
		(destGroupId !== startGroupId || destIndex !== startIndex) &&
		contributorPropertyKey === sidebarItemPropertyKey
	);
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
	} = props;

	const {
		criterion: droppedCriterion,
		groupId: startGroupId,
		index: startIndex,
	} = monitor.getItem();

	const {
		defaultValue,
		displayValue,
		operatorName,
		propertyName,
		type,
		value,
	} = droppedCriterion;

	const droppedCriterionValue = value || defaultValue;

	const operators = getSupportedOperatorsFromType(
		SUPPORTED_OPERATORS,
		SUPPORTED_PROPERTY_TYPES,
		type
	);

	const newCriterion = {
		displayValue,
		operatorName: operatorName ? operatorName : operators[0].name,
		propertyName,
		value: droppedCriterionValue,
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
		renderEmptyValuesErrors: PropTypes.bool,
		supportedProperties: PropTypes.array,
	};

	static defaultProps = {
		criterion: {},
		editing: true,
		renderEmptyValuesErrors: false,
		supportedProperties: [],
	};

	componentDidMount() {
		const {
			criterion: {displayValue, propertyName, value},
			supportedProperties,
		} = this.props;

		this._selectedProperty = this._getSelectedItem(
			supportedProperties,
			propertyName
		);

		if (
			this._selectedProperty.type === PROPERTY_TYPES.ID &&
			value &&
			!displayValue
		) {
			this._fetchEntityName();
		}
	}

	_fetchEntityName = () => {
		const {criterion, entityName, onChange} = this.props;

		const {propertyName, value} = criterion;

		const data = Liferay.Util.ns(this.context.namespace, {
			entityName,
			fieldName: propertyName,
			fieldValue: value,
		});

		fetch(this.context.requestFieldValueNameURL, {
			body: objectToFormData(data),
			method: 'POST',
		})
			.then((response) => response.json())
			.then(({fieldValueName: displayValue}) => {
				if (displayValue === undefined) {
					throw new Error(DISPLAY_VALUE_NOT_FOUND_ERROR);
				}

				onChange({...criterion, displayValue, unknownEntity: false});
			})
			.catch((error) => {
				if (error && error.message === DISPLAY_VALUE_NOT_FOUND_ERROR) {
					onChange({
						...criterion,
						displayValue: value,
						unknownEntity: true,
					});
				}
				else {
					onChange({...criterion, displayValue: value});
				}
			});
	};

	/**
	 * Gets the selected item object with a `name` and `label` property for a
	 * selection input. If one isn't found, a new object is returned using the
	 * idSelected for name and label.
	 * @param {Array} list The list of objects to search through.
	 * @param {string} idSelected The name to match in each object in the list.
	 * @return {object} An object with a `name`, `label` and `type` property.
	 */
	_getSelectedItem = (list, idSelected) => {
		const selectedItem = list.find((item) => item.name === idSelected);

		return selectedItem
			? selectedItem
			: {
					label: idSelected,
					name: idSelected,
					notFound: true,
					type: PROPERTY_TYPES.STRING,
			  };
	};

	_renderErrorMessages({errorOnProperty, unknownEntityError}) {
		const {editing} = this.props;
		const errors = [];
		if (errorOnProperty) {
			const message = editing
				? Liferay.Language.get('criteria-error-message-edit')
				: Liferay.Language.get('criteria-error-message-view');

			errors.push({
				message,
			});
		}

		if (unknownEntityError) {
			const message = editing
				? Liferay.Language.get('unknown-element-message-edit')
				: Liferay.Language.get('unknown-element-message-view');

			errors.push({
				message,
			});
		}

		return errors.map((error, index) => {
			return (
				<ClayAlert
					className="bg-transparent border-0 mt-1 p-1"
					displayType="danger"
					key={index}
					title={Liferay.Language.get('error')}
				>
					{error.message}
				</ClayAlert>
			);
		});
	}

	_renderWarningMessages() {
		const {editing} = this.props;
		const warnings = [];
		const message = editing
			? Liferay.Language.get('criteria-warning-message-edit')
			: Liferay.Language.get('criteria-warning-message-view');

		warnings.push({
			message,
		});

		return warnings.map((warning, index) => {
			return (
				<ClayAlert
					className="bg-transparent border-0 mt-1 p-1"
					displayType="warning"
					key={index}
					title={Liferay.Language.get('warning')}
				>
					{warning.message}
				</ClayAlert>
			);
		});
	}

	render() {
		const {
			canDrop,
			connectDragPreview,
			connectDragSource,
			connectDropTarget,
			criterion,
			dragging,
			editing,
			hover,
			index,
			onAdd,
			onChange,
			onDelete,
			renderEmptyValuesErrors,
			supportedProperties,
		} = this.props;

		const {unknownEntity} = criterion;

		const selectedOperator = this._getSelectedItem(
			SUPPORTED_OPERATORS,
			criterion.operatorName
		);

		const selectedProperty = this._getSelectedItem(
			supportedProperties,
			criterion.propertyName
		);

		const value = criterion ? criterion.value : '';
		const errorOnProperty = selectedProperty.notFound;
		const error = errorOnProperty || unknownEntity;
		const warningOnProperty =
			selectedProperty.options === undefined
				? false
				: !selectedProperty.options?.length
				? false
				: selectedProperty.options.find((option) => {
						return (
							option.value === value &&
							option.disabled === undefined
						);
				  });
		const warning = !(warningOnProperty || warningOnProperty === false);

		if (
			selectedProperty.options !== undefined &&
			!!selectedProperty.options?.length &&
			selectedProperty.options.find((option) => {
				return option.value === value;
			}) === undefined &&
			warning
		) {
			selectedProperty.options.unshift({
				disabled: true,
				label: value,
				value,
			});
		}

		const classes = getCN('criterion-row-root', {
			'criterion-row-root-error': error,
			'criterion-row-root-warning': warning,
			'dnd-drag': dragging,
			'dnd-hover': hover && canDrop,
		});

		return (
			<>
				{connectDropTarget(
					connectDragPreview(
						<div className={classes}>
							{editing ? (
								<CriteriaRowEditable
									connectDragSource={connectDragSource}
									criterion={criterion}
									error={error}
									index={index}
									onAdd={onAdd}
									onChange={onChange}
									onDelete={onDelete}
									renderEmptyValuesErrors={
										renderEmptyValuesErrors
									}
									selectedOperator={selectedOperator}
									selectedProperty={selectedProperty}
								/>
							) : (
								<CriteriaRowReadable
									criterion={criterion}
									selectedOperator={selectedOperator}
									selectedProperty={selectedProperty}
								/>
							)}
						</div>
					)
				)}
				{error &&
					this._renderErrorMessages({
						errorOnProperty,
						unknownEntityError: unknownEntity,
					})}
				{warning && this._renderWarningMessages()}
				{!value && renderEmptyValuesErrors && (
					<ClayAlert
						className="pr-6 text-right"
						displayType="danger"
						title={Liferay.Language.get(
							'a-value-needs-to-be-added-or-selected-in-the-blank-field'
						)}
						variant="feedback"
					/>
				)}
			</>
		);
	}
}

const CriteriaRowWithDrag = dragSource(
	DragTypes.CRITERIA_ROW,
	{
		beginDrag,
	},
	(connect, monitor) => ({
		connectDragPreview: connect.dragPreview(),
		connectDragSource: connect.dragSource(),
		dragging: monitor.isDragging(),
	})
)(CriteriaRow);

export default dropTarget(
	acceptedDragTypes,
	{
		canDrop,
		drop,
	},
	(connect, monitor) => ({
		canDrop: monitor.canDrop(),
		connectDropTarget: connect.dropTarget(),
		hover: monitor.isOver(),
	})
)(CriteriaRowWithDrag);
