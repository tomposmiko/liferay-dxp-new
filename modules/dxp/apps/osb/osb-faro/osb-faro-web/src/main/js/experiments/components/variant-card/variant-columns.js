import Cell from 'shared/components/table/Cell';
import ImprovementCell from './ImprovementCell';
import React from 'react';
import UniqueVisitorCell from './UniqueVisitorsCell';
import VariantTitleCell from './VariantTitleCell';
import {
	getFormattedMedian,
	getFormattedMedianLabel,
	getFormattedProbabilityToWin,
	getVariantLabel
} from 'experiments/util/experiments';

export default ({
	bestVariant,
	metric,
	metricUnit,
	status,
	winnerDXPVariantId
}) => [
	{
		accessor: 'dxpVariantName',
		cellRenderer: ({data: {dxpVariantId, dxpVariantName}}) => (
			<VariantTitleCell
				id={dxpVariantId}
				label={getVariantLabel(
					status,
					bestVariant,
					winnerDXPVariantId,
					dxpVariantId
				)}
				title={`${dxpVariantName}`}
			/>
		),
		label: Liferay.Language.get('variants'),
		sortable: true
	},
	{
		accessor: 'median',
		cellRenderer: ({data: {median}}) => (
			<Cell className='text-right' title={false}>
				{`${getFormattedMedian(median, metric)}${metricUnit}`}
			</Cell>
		),
		className: 'text-right',
		label: getFormattedMedianLabel(metric),
		sortable: true
	},
	{
		accessor: 'confidenceInterval',
		cellRenderer: ({data: {confidenceInterval}}) => (
			<Cell className='text-right' title={false}>
				{`${getFormattedMedian(
					confidenceInterval[0],
					metric
				)}${metricUnit} - ${getFormattedMedian(
					confidenceInterval[1],
					metric
				)}${metricUnit}`}
			</Cell>
		),
		className: 'text-right',
		label: Liferay.Language.get('confidence-interval'),
		sortable: true
	},
	{
		accessor: 'improvement',
		cellRenderer: ({data: {improvement}}) => (
			<ImprovementCell className='text-right' improvement={improvement} />
		),
		className: 'text-right',
		label: Liferay.Language.get('improvement'),
		sortable: true
	},
	{
		accessor: 'probabilityToWin',
		cellRenderer: ({data: {probabilityToWin}}) => (
			<Cell className='text-right' title={false}>
				{`${getFormattedProbabilityToWin(probabilityToWin)}%`}
			</Cell>
		),
		className: 'text-right',
		label: Liferay.Language.get('probability-to-win'),
		sortable: true
	},
	{
		accessor: 'uniqueVisitors',
		cellRenderer: ({data: {trafficSplit, uniqueVisitors}}) => (
			<UniqueVisitorCell
				className='text-right'
				trafficSplit={trafficSplit}
				uniqueVisitors={uniqueVisitors}
			/>
		),
		className: 'text-right',
		label: Liferay.Language.get('unique-visitors'),
		sortable: true
	}
];
