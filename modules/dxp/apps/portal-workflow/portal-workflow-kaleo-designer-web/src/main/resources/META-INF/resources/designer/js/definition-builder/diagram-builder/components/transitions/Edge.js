/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import PropTypes from 'prop-types';
import React, {useContext, useMemo} from 'react';
import {EdgeText, getBezierPath, useStoreState} from 'react-flow-renderer';

import {DefinitionBuilderContext} from '../../../DefinitionBuilderContext';
import {defaultLanguageId} from '../../../constants';
import {DiagramBuilderContext} from '../../DiagramBuilderContext';
import getBezierEdgeCenter from '../../util/getBezierEdgeCenter';
import MarkerEndDefinition, {markerEndId} from './MarkerEndDefinition';
import {getEdgeParams} from './utils';

function Edge(props) {
	const {
		data: {defaultEdge = true, label},
		id,
		source,
		style = {},
		target,
	} = props;

	const {elements, selectedLanguageId} = useContext(DefinitionBuilderContext);
	const {selectedItem, setSelectedItem} = useContext(DiagramBuilderContext);

	let labelProps = label;

	if (!labelProps || !labelProps[defaultLanguageId]) {
		labelProps = {
			[defaultLanguageId]: Liferay.Language.get('task'),
		};
	}

	let edgeLabel = labelProps[defaultLanguageId];

	if (selectedLanguageId && labelProps[selectedLanguageId]) {
		edgeLabel = labelProps[selectedLanguageId];
	}

	const nodes = useStoreState((state) => state.nodes);

	const sourceNode = useMemo(() => nodes.find((n) => n.id === source), [
		source,
		nodes,
	]);
	const targetNode = useMemo(() => nodes.find((n) => n.id === target), [
		target,
		nodes,
	]);

	const {sourcePos, sx, sy, targetPos, tx, ty} = getEdgeParams(
		sourceNode,
		targetNode
	);

	const hasCollidingNode = elements.filter(
		(element) => element.source === target && element.target === source
	).length;

	const collidedTransitionIndex = elements.findIndex(
		(element) => element.source === target && element.target === source
	);

	const currentTransitionIndex = elements.findIndex(
		(element) => element.id === id
	);

	let newSourceX = sx;
	let newTargetX = tx;

	if (hasCollidingNode) {
		newSourceX =
			currentTransitionIndex > collidedTransitionIndex
				? newSourceX + 40
				: newSourceX - 40;
		newTargetX =
			currentTransitionIndex > collidedTransitionIndex
				? newTargetX + 40
				: newTargetX - 40;
	}

	const drawn = getBezierPath({
		sourcePosition: sourcePos,
		sourceX: newSourceX,
		sourceY: sy,
		targetPosition: targetPos,
		targetX: newTargetX,
		targetY: ty,
	});

	// eslint-disable-next-line prefer-const
	let [edgeCenterX, edgeCenterY] = getBezierEdgeCenter({
		curvature: 0.25,
		sourcePosition: sourcePos,
		sourceX: newSourceX,
		sourceY: sy,
		targetPosition: targetPos,
		targetX: newTargetX,
		targetY: ty,
	});

	if (hasCollidingNode) {
		edgeCenterY =
			currentTransitionIndex > collidedTransitionIndex
				? edgeCenterY + 21
				: edgeCenterY - 21;
	}

	const [strokeColor, labelBg] =
		selectedItem?.id === id
			? ['#80ACFF', '#0B5FFF']
			: ['#A7A9BC', '#6B6C7E'];

	const edgeStyle = {
		...style,
		stroke: strokeColor,
		strokeDasharray: 0,
		strokeWidth: 2,
	};

	if (!defaultEdge) {
		edgeStyle.strokeDasharray = 5;
	}

	return (
		<g className="react-flow__connection">
			<MarkerEndDefinition color={strokeColor} />

			<path
				className="react-flow__edge-path"
				d={drawn}
				id={id}
				markerEnd={`url(#${markerEndId})`}
				style={edgeStyle}
			/>

			<EdgeText
				className="react-flow-__edge-text"
				label={edgeLabel?.toUpperCase()}
				labelBgBorderRadius="13px"
				labelBgPadding={[8, 4]}
				labelBgStyle={{
					fill: labelBg,
				}}
				labelShowBg={true}
				labelStyle={{fill: '#FFF', fontWeight: 600}}
				onClick={() => setSelectedItem(props)}
				x={edgeCenterX}
				y={edgeCenterY}
			/>
		</g>
	);
}

Edge.propTypes = {
	data: PropTypes.shape({
		defaultEdge: PropTypes.bool,
		label: PropTypes.object,
	}),
	id: PropTypes.string.isRequired,
	source: PropTypes.string,
	sourcePosition: PropTypes.string,
	sourceX: PropTypes.number,
	sourceY: PropTypes.number,
	style: PropTypes.object,
	target: PropTypes.string,
	targetPosition: PropTypes.string,
	targetX: PropTypes.number,
	targetY: PropTypes.number,
};

const edgeTypes = {
	transition: Edge,
};

export default edgeTypes;
