import Card from 'shared/components/Card';
import Label from 'shared/components/Label';
import moment from 'moment';
import React from 'react';
import RecommendationJobRunsQuery from '../queries/RecommendationJobRunsQuery';
import Table from 'shared/components/table';
import {applyTimeZone} from 'shared/util/date';
import {compose} from 'redux';
import {
	createOrderIOMap,
	getSortFromOrderIOMap,
	ID
} from 'shared/util/pagination';
import {getFormattedTitle} from 'shared/components/NoResultsDisplay';
import {getMapResultToProps} from 'shared/hoc/mappers/metrics';
import {graphql} from '@apollo/react-hoc';
import {
	JOB_RUN_FREQUENCIES_LABEL_MAP,
	JOB_RUN_STATUSES_DISPLAY_MAP,
	JOB_RUN_STATUSES_LABEL_MAP
} from '../utils/utils';
import {
	JobRunFrequencies,
	JobRunStatuses,
	OrderByDirections
} from 'shared/util/constants';
import {OrderedMap} from 'immutable';
import {OrderParams} from 'shared/util/records';
import {Router} from 'shared/types';
import {sub} from 'shared/util/lang';
import {withEmpty} from 'cerebro-shared/hocs/utils';
import {
	withError,
	withLoading,
	withPaginationBar,
	withStatefulPagination
} from 'shared/hoc';

const DATE_FORMAT = 'MMM DD, YYYY';

const getContextItemCount =
	(contextItemKey: string) =>
	(context: {key: string; value: any}[]): string => {
		const contextItem = context.find(({key}) => key === contextItemKey);

		if (contextItem) {
			return Number(contextItem.value).toLocaleString();
		}

		return '0';
	};

interface IOutputVersionsCardProps {
	nextRunDate: string;
	router: Router;
	runFrequency: JobRunFrequencies;
	timeZoneId: string;
}

const withData = () =>
	graphql(RecommendationJobRunsQuery, {
		options: ({
			delta,
			jobId,
			orderIOMap,
			page
		}: {
			delta: number;
			jobId: string;
			orderIOMap: OrderedMap<string, OrderParams>;
			page: number;
		}) => ({
			fetchPolicy: 'no-cache',
			variables: {
				jobId,
				size: delta,
				sort: getSortFromOrderIOMap(orderIOMap),
				start: (page - 1) * delta
			}
		}),
		props: getMapResultToProps(({jobRuns: {jobRuns, total}}) => ({
			items: jobRuns,
			total
		}))
	});

const TableWithData = compose(
	withData(),
	withPaginationBar({defaultDelta: 5}),
	withLoading({alignCenter: true, page: false}),
	withError({page: false}),
	withEmpty({
		emptyTitle: getFormattedTitle(
			Liferay.Language.get('output-versions').toLowerCase()
		)
	})
)(Table);

const OutputVersionsListWithData = withStatefulPagination(
	TableWithData,
	{
		initialDelta: 5,
		initialOrderIOMap: createOrderIOMap(ID, OrderByDirections.Descending)
	},
	false
);

const OutputVersionsCard: React.FC<IOutputVersionsCardProps> = ({
	nextRunDate,
	router,
	runFrequency,
	timeZoneId
}) => {
	const {
		params: {jobId}
	} = router;

	return (
		<Card className='output-versions-card-root'>
			<Card.Header className='d-flex justify-content-between'>
				<Card.Title>
					{Liferay.Language.get('output-versions')}
				</Card.Title>

				<div className='training-frequency'>
					{Liferay.Language.get('training-frequency')}

					<b>{JOB_RUN_FREQUENCIES_LABEL_MAP[runFrequency]}</b>

					{!!nextRunDate && (
						<b>{`(${sub(Liferay.Language.get('next-x'), [
							moment(nextRunDate).fromNow()
						])})`}</b>
					)}
				</div>
			</Card.Header>

			<Card.Body>
				<OutputVersionsListWithData
					columns={[
						{
							accessor: 'completedDate',
							className: 'table-cell-expand',
							dataFormatter: val =>
								applyTimeZone(val, timeZoneId).calendar(null, {
									lastDay: DATE_FORMAT,
									lastWeek: DATE_FORMAT,
									nextDay: DATE_FORMAT,
									nextWeek: DATE_FORMAT,
									sameDay: `[${Liferay.Language.get(
										'today'
									)}]`,
									sameElse: DATE_FORMAT
								}),
							label: Liferay.Language.get('training-date'),
							sortable: false,
							title: true
						},
						{
							accessor: 'context',
							className: 'table-column-text-end',
							dataFormatter: getContextItemCount(
								'userItemInteractionsDatasetCount'
							),
							label: Liferay.Language.get('events'),
							sortable: false
						},
						{
							accessor: 'context',
							className: 'table-column-text-end',
							dataFormatter:
								getContextItemCount('itemsDatasetCount'),
							label: Liferay.Language.get('items'),
							sortable: false
						},
						{
							accessor: 'status',
							cellRenderer: ({
								className,
								data: {status}
							}: {
								className: string;
								data: {status: JobRunStatuses};
							}) => (
								<td className={className}>
									<Label
										className='status'
										display={
											JOB_RUN_STATUSES_DISPLAY_MAP[status]
										}
										size='lg'
										uppercase
									>
										{JOB_RUN_STATUSES_LABEL_MAP[status]}
									</Label>
								</td>
							),
							label: Liferay.Language.get('status'),
							sortable: false
						}
					]}
					jobId={jobId}
					router={router}
					showDeltaDropdown={false}
				/>
			</Card.Body>
		</Card>
	);
};

export default OutputVersionsCard;
