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

import ClayIcon from '@clayui/icon';
import classNames from 'classnames';
import {PropTypes} from 'prop-types';
import React, {Fragment, useMemo} from 'react';
import {DragSource as dragSource} from 'react-dnd';

import {
	CONJUNCTIONS,
	SUPPORTED_OPERATORS,
	SUPPORTED_PROPERTY_TYPES,
} from '../../utils/constants';
import {DragTypes} from '../../utils/drag-types';
import {
	generateGroupId,
	getChildGroupIds,
	getSupportedOperatorsFromType,
	insertAtIndex,
	replaceAtIndex,
} from '../../utils/utils';
import Conjunction from './Conjunction.es';
import CriteriaRow from './CriteriaRow';
import DropZone from './DropZone';
import EmptyDropZone from './EmptyDropZone';

/**
 * Passes the required values to the drop target.
 * This method must be called `beginDrag`.
 * @param {Object} props Component's current props
 * @returns {Object} The props to be passed to the drop target.
 */
function beginDrag({criteria, index, parentGroupId}) {
	const childGroupIds = getChildGroupIds(criteria);

	return {
		childGroupIds,
		criterion: criteria,
		groupId: parentGroupId,
		index,
	};
}

/**
 * A function that decorates the passed in component with the drag source HOC.
 * This was separated out since this function needed to be called again for the
 * nested groups.
 * @param {React.Component} component The component to decorate.
 */
const withDragSource = dragSource(
	DragTypes.CRITERIA_GROUP,
	{
		beginDrag,
	},
	(connect, monitor) => ({
		connectDragPreview: connect.dragPreview(),
		connectDragSource: connect.dragSource(),
		dragging: monitor.isDragging(),
	})
);

function CriteriaGroup({
	connectDragPreview,
	connectDragSource,
	criteria,
	dragging,
	editing,
	emptyContributors,
	entityName,
	groupId,
	modelLabel,
	onChange,
	onMove,
	propertyKey,
	renderEmptyValuesErrors = false,
	root = false,
	supportedProperties,
}) {
	const NestedCriteriaGroupWithDrag = useMemo(() => {
		return withDragSource(CriteriaGroup);
	}, []);

	const _handleConjunctionSelect = (conjunctionName) => {
		onChange({
			...criteria,
			conjunctionName,
		});
	};

	/**
	 * Adds a new criterion in a group at the specified index. If the criteria
	 * was previously empty and is being added to the root group, a new group
	 * will be added as well.
	 * @param {number} index The position the criterion will be inserted in.
	 * @param {object} criterion The criterion that will be added.
	 * @memberof CriteriaGroup
	 */
	const _handleCriterionAdd = (index, criterion) => {
		const {
			defaultValue = '',
			operatorName,
			propertyName,
			type,
			value,
		} = criterion;

		const criterionValue = value || defaultValue;

		const operators = getSupportedOperatorsFromType(
			SUPPORTED_OPERATORS,
			SUPPORTED_PROPERTY_TYPES,
			type
		);

		const newCriterion = {
			operatorName: operatorName ? operatorName : operators[0].name,
			propertyName,
			type,
			value: criterionValue,
		};

		if (root && !criteria) {
			onChange({
				conjunctionName: CONJUNCTIONS.AND,
				groupId: generateGroupId(),
				items: [newCriterion],
			});
		}
		else {
			onChange({
				...criteria,
				items: insertAtIndex(newCriterion, criteria.items, index),
			});
		}
	};

	const _handleCriterionChange = (index) => (newCriterion) => {
		onChange({
			...criteria,
			items: replaceAtIndex(newCriterion, criteria.items, index),
		});
	};

	const _handleCriterionDelete = (index) => {
		onChange({
			...criteria,
			items: criteria.items.filter((fItem, fIndex) => fIndex !== index),
		});
	};

	const _isCriteriaEmpty = () => {
		return criteria ? !criteria.items.length : true;
	};

	const _renderConjunction = (index) => {
		return (
			<>
				<DropZone
					dropIndex={index}
					groupId={groupId}
					onCriterionAdd={_handleCriterionAdd}
					onMove={onMove}
					propertyKey={propertyKey}
				/>

				<Conjunction
					conjunctionName={criteria.conjunctionName}
					editing={editing}
					onSelect={_handleConjunctionSelect}
				/>

				<DropZone
					before
					dropIndex={index}
					groupId={groupId}
					onCriterionAdd={_handleCriterionAdd}
					onMove={onMove}
					propertyKey={propertyKey}
				/>
			</>
		);
	};

	const _renderCriterion = (criterion, index) => {
		return (
			<div
				className={classNames('criterion position-relative', {
					'criterion-group': criterion.items,
				})}
			>
				{criterion.items ? (
					<NestedCriteriaGroupWithDrag
						criteria={criterion}
						editing={editing}
						entityName={entityName}
						groupId={criterion.groupId}
						index={index}
						modelLabel={modelLabel}
						onChange={_handleCriterionChange(index)}
						onMove={onMove}
						parentGroupId={groupId}
						propertyKey={propertyKey}
						renderEmptyValuesErrors={renderEmptyValuesErrors}
						supportedProperties={supportedProperties}
					/>
				) : (
					<CriteriaRow
						criterion={criterion}
						editing={editing}
						entityName={entityName}
						groupId={groupId}
						index={index}
						modelLabel={modelLabel}
						onAdd={_handleCriterionAdd}
						onChange={_handleCriterionChange(index)}
						onDelete={_handleCriterionDelete}
						onMove={onMove}
						propertyKey={propertyKey}
						renderEmptyValuesErrors={renderEmptyValuesErrors}
						root={root}
						supportedProperties={supportedProperties}
					/>
				)}

				<DropZone
					dropIndex={index + 1}
					groupId={groupId}
					onCriterionAdd={_handleCriterionAdd}
					onMove={onMove}
					propertyKey={propertyKey}
				/>
			</div>
		);
	};

	const singleRow = criteria && criteria.items && criteria.items.length === 1;

	return connectDragPreview(
		<div
			className={classNames(
				{
					'criteria-group-root w-100': criteria,
					'dnd-drag': dragging,
				},
				`color--${propertyKey} criteria-group-item${
					root ? '-root' : ''
				}`
			)}
		>
			{_isCriteriaEmpty() ? (
				<EmptyDropZone
					emptyContributors={emptyContributors}
					onCriterionAdd={_handleCriterionAdd}
					propertyKey={propertyKey}
				/>
			) : (
				<>
					<DropZone
						before
						dropIndex={0}
						groupId={groupId}
						onCriterionAdd={_handleCriterionAdd}
						onMove={onMove}
						propertyKey={propertyKey}
					/>

					{editing &&
						singleRow &&
						!root &&
						connectDragSource(
							<div className="align-items-center d-flex drag-icon h-100 position-absolute top-0">
								<ClayIcon symbol="drag" />
							</div>
						)}

					{criteria.items &&
						criteria.items.map((criterion, index) => {
							return (
								<Fragment key={index}>
									{index !== 0 && _renderConjunction(index)}

									{_renderCriterion(criterion, index)}
								</Fragment>
							);
						})}
				</>
			)}
		</div>
	);
}

CriteriaGroup.propTypes = {
	connectDragPreview: PropTypes.func,
	connectDragSource: PropTypes.func,
	criteria: PropTypes.object,
	dragging: PropTypes.bool,
	editing: PropTypes.bool,
	emptyContributors: PropTypes.bool,
	entityName: PropTypes.string,
	groupId: PropTypes.string,
	index: PropTypes.number,
	modelLabel: PropTypes.string,
	onChange: PropTypes.func,
	onMove: PropTypes.func,
	parentGroupId: PropTypes.string,
	propertyKey: PropTypes.string.isRequired,
	renderEmptyValuesErrors: PropTypes.bool,
	root: PropTypes.bool,
	supportedProperties: PropTypes.array,
};

export default withDragSource(CriteriaGroup);
