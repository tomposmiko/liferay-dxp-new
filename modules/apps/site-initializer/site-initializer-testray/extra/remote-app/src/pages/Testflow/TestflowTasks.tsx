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
import {useEffect} from 'react';
import {Link, useParams} from 'react-router-dom';

import Avatar from '../../components/Avatar';
import Code from '../../components/Code';
import Container from '../../components/Layout/Container';
import ListView from '../../components/ListView';
import Loading from '../../components/Loading';
import TaskbarProgress from '../../components/ProgressBar/TaskbarProgress';
import StatusBadge from '../../components/StatusBadge';
import QATable from '../../components/Table/QATable';
import useCaseResultGroupBy from '../../data/useCaseResultGroupBy';
import {useFetch} from '../../hooks/useFetch';
import useHeader from '../../hooks/useHeader';
import i18n from '../../i18n';
import {getTaskQuery, getTaskTransformData} from '../../services/rest';
import {
	SUBTASK_STATUS,
	StatusesProgressScore,
	chartClassNames,
} from '../../util/constants';
import {getTimeFromNow} from '../../util/date';
import {assigned} from '../../util/mock';

export const progressScoreItems = [
	[StatusesProgressScore.SELF, 7000],
	[StatusesProgressScore.OTHER, 8967],
	[StatusesProgressScore.INCOMPLETE, 1000],
];
function getTotalCompletedScore(scores: [string, number][]) {
	let totalCompleted = 0;

	for (const [scoreName, score] of scores) {
		if (scoreName !== StatusesProgressScore.INCOMPLETE) {
			totalCompleted += score;
		}
	}

	return totalCompleted;
}

const ShortcutIcon = () => (
	<ClayIcon className="ml-2" fontSize={12} symbol="shortcut" />
);

const TestFlowTasks = () => {
	const {testrayTaskId} = useParams();

	const {data: testrayTask, loading} = useFetch(
		getTaskQuery(testrayTaskId),
		getTaskTransformData
	);

	const {
		donut: {columns},
	} = useCaseResultGroupBy(testrayTask?.build?.id);

	const {setHeading} = useHeader({timeout: 50, useTabs: []});

	useEffect(() => {
		if (testrayTask) {
			setHeading([
				{
					category: i18n.translate('tasks'),
					title: testrayTask.name,
				},
			]);
		}
	}, [setHeading, testrayTask]);

	if (loading || !testrayTask) {
		return <Loading />;
	}

	return (
		<>
			<Container collapsable title={i18n.translate('task-details')}>
				<div className="d-flex flex-wrap">
					<div className="col-4 col-lg-4 col-md-12 p-0">
						<QATable
							items={[
								{
									title: i18n.translate('status'),
									value: (
										<StatusBadge
											type={
												(SUBTASK_STATUS as any)[
													testrayTask.dueStatus
												]?.color
											}
										>
											{
												(SUBTASK_STATUS as any)[
													testrayTask.dueStatus
												]?.label
											}
										</StatusBadge>
									),
								},
								{
									title: i18n.translate('assigned-users'),
									value: (
										<Avatar.Group
											assignedUsers={assigned}
											groupSize={3}
										/>
									),
								},
								{
									title: i18n.translate('created'),
									value: getTimeFromNow(
										testrayTask.dateCreated
									),
								},
							]}
						/>
					</div>

					<div className="col-8 col-lg-8 col-md-12 mb-3 p-0">
						<QATable
							items={[
								{
									title: i18n.translate('project-name'),
									value: (
										<Link
											className="text-dark"
											to={`/project/${testrayTask.build?.project?.id}/routines`}
										>
											{testrayTask.build?.project?.name}

											<ShortcutIcon />
										</Link>
									),
								},
								{
									title: i18n.translate('routine-name'),
									value: (
										<Link
											className="text-dark"
											to={`/project/${testrayTask.build?.project?.id}/routines/${testrayTask.build?.routine?.id}`}
										>
											{testrayTask.build?.routine?.name}

											<ShortcutIcon />
										</Link>
									),
								},
								{
									title: i18n.translate('build-name'),
									value: (
										<Link
											className="text-dark"
											to={`/project/${testrayTask.build?.project?.id}/routines/${testrayTask.build?.routine?.id}/build/${testrayTask.build?.id}`}
										>
											{testrayTask.build?.name}

											<ShortcutIcon />
										</Link>
									),
								},
							]}
						/>

						<div className="pb-4">
							<TaskbarProgress
								displayTotalCompleted={false}
								items={columns as any}
								legend
								taskbarClassNames={chartClassNames}
							/>
						</div>
					</div>
				</div>
			</Container>

			<Container
				className="mt-3"
				collapsable
				title={i18n.translate('progress-score')}
			>
				<div className="pb-5">
					<TaskbarProgress
						displayTotalCompleted
						items={progressScoreItems as any}
						legend
						taskbarClassNames={chartClassNames}
						totalCompleted={getTotalCompletedScore(
							progressScoreItems as any
						)}
					/>
				</div>
			</Container>

			<Container className="mt-3">
				<ListView
					managementToolbarProps={{title: i18n.translate('subtasks')}}
					resource="/subtasks"
					tableProps={{
						columns: [
							{
								clickable: true,
								key: 'name',
								value: i18n.translate('name'),
							},
							{
								clickable: true,
								key: 'dueStatus',
								render: (status) => (
									<StatusBadge
										type={
											(SUBTASK_STATUS as any)[status]
												?.color
										}
									>
										{(SUBTASK_STATUS as any)[status]?.label}
									</StatusBadge>
								),

								value: i18n.translate('status'),
							},
							{
								clickable: true,
								key: 'score',
								value: i18n.translate('score'),
							},
							{
								clickable: true,
								key: 'tests',
								value: i18n.translate('tests'),
							},
							{
								clickable: true,
								key: 'error',
								render: (value) => <Code>{value}</Code>,
								size: 'xl',
								value: i18n.translate('errors'),
							},
							{
								clickable: true,
								key: 'assignee',
								render: (assignee: any) =>
									assignee && (
										<Avatar
											displayName
											name={assignee[0]?.name}
											url={assignee[0]?.url}
										/>
									),
								size: 'sm',
								value: i18n.translate('assignee'),
							},
						],
						navigateTo: () => '/testflow/subtasks',
					}}
				/>
			</Container>
		</>
	);
};

export default TestFlowTasks;
