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
import {Dispatch} from 'react';
import {Link, useOutletContext, useParams} from 'react-router-dom';
import {KeyedMutator} from 'swr';

import Avatar from '../../components/Avatar';
import AssignToMe from '../../components/Avatar/AssigneToMe';
import Code from '../../components/Code';
import FloatingBox from '../../components/FloatingBox/index';
import Container from '../../components/Layout/Container';
import ListView from '../../components/ListView';
import Loading from '../../components/Loading';
import TaskbarProgress from '../../components/ProgressBar/TaskbarProgress';
import StatusBadge from '../../components/StatusBadge';
import {StatusBadgeType} from '../../components/StatusBadge/StatusBadge';
import QATable from '../../components/Table/QATable';
import {ListViewTypes} from '../../context/ListViewContext';
import useCaseResultGroupBy from '../../data/useCaseResultGroupBy';
import {useFetch} from '../../hooks/useFetch';
import useHeader from '../../hooks/useHeader';
import useMutate from '../../hooks/useMutate';
import i18n from '../../i18n';
import {filters} from '../../schema/filter';
import {Liferay} from '../../services/liferay';
import {
	APIResponse,
	PickList,
	TestraySubTask,
	TestrayTask,
	TestrayTaskUser,
	UserAccount,
	testrayTaskImpl,
	testrayTaskUsersImpl,
} from '../../services/rest';
import {testraySubTaskImpl} from '../../services/rest/TestraySubtask';
import {StatusesProgressScore, chartClassNames} from '../../util/constants';
import {getTimeFromNow} from '../../util/date';
import {SearchBuilder} from '../../util/search';
import {SubTaskStatuses} from '../../util/statuses';
import SubtaskCompleteModal from './Subtask/SubtaskCompleteModal';
import useSubtasksActions from './Subtask/useSubtasksActions';

type OutletContext = {
	mutateTask?: KeyedMutator<TestrayTask>;
	testrayTask: TestrayTask;
};

const ShortcutIcon = () => (
	<ClayIcon className="ml-2" fontSize={12} symbol="shortcut" />
);

const TestFlowTasks = () => {
	const {mutateTask, testrayTask} = useOutletContext<OutletContext>();
	const {updateItemFromList} = useMutate();
	const {taskId} = useParams();
	const {actions, completeModal} = useSubtasksActions();

	const {data: taskUserResponse} = useFetch<APIResponse<TestrayTaskUser>>(
		testrayTask?.id
			? `${testrayTaskImpl.getNestedObject(
					'taskToTasksUsers',
					testrayTask.id
			  )}?nestedFields=task,user`
			: null,
		(response) => testrayTaskUsersImpl.transformDataFromList(response)
	);

	const taskUsers: TestrayTaskUser[] = taskUserResponse?.items || [];

	const users = taskUsers
		.filter(({user}) => user)
		.map(({user}) => user as UserAccount);

	useHeader({useTabs: []});

	const {
		donut: {columns},
	} = useCaseResultGroupBy(testrayTask?.build?.id);

	if (!testrayTask) {
		return <Loading />;
	}

	const getFloatingBoxAlerts = (subtasks: TestraySubTask[]) => {
		const alerts = [];

		if (subtasks.length === 1) {
			alerts.push({
				text: i18n.translate(
					'please-select-at-least-two-subtasks-to-merge'
				),
			});
		}

		const subtasksWithDifferentAssignedUsers = subtasks
			.filter(
				({user}) =>
					user &&
					user.id.toString() !== Liferay.ThemeDisplay.getUserId()
			)
			.map(({name}) => ({
				header: name,
				text: i18n.sub(
					'subtask-x-must-be-assigned-to-you-to-be-user-id-a-merge',
					name
				),
			}));

		return [...alerts, ...subtasksWithDifferentAssignedUsers];
	};

	const onMergeSubtasks = async (
		subtasks: TestraySubTask[],
		mutate: KeyedMutator<any>,
		dispatch: Dispatch<any>
	) => {
		await testraySubTaskImpl.mergedToSubtask(subtasks);

		updateItemFromList(
			mutate,
			0,
			{},
			{
				revalidate: true,
			}
		);

		dispatch({
			payload: [],
			type: ListViewTypes.SET_CHECKED_ROW,
		});
	};

	const searchBuilder = new SearchBuilder({useURIEncode: false});

	const subTaskFilter = searchBuilder
		.eq('taskId', taskId as string)
		.and()
		.ne('dueStatus', SubTaskStatuses.MERGED)
		.build();

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
												testrayTask.dueStatus.key.toLowerCase() as StatusBadgeType
											}
										>
											{testrayTask.dueStatus.name}
										</StatusBadge>
									),
								},
								{
									title: i18n.translate('assigned-users'),
									value: (
										<Avatar.Group
											assignedUsers={users.map(
												({givenName}) => ({
													name: givenName,
													url:
														'https://picsum.photos/200',
												})
											)}
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
						items={[
							[StatusesProgressScore.SELF, 0],
							[
								StatusesProgressScore.OTHER,
								Number(testrayTask.subtaskScoreCompleted ?? 0),
							],
							[
								StatusesProgressScore.INCOMPLETE,
								Number(testrayTask.subtaskScoreIncomplete ?? 0),
							],
						]}
						legend
						taskbarClassNames={chartClassNames}
						totalCompleted={Number(
							testrayTask.subtaskScoreCompleted ?? 0
						)}
					/>
				</div>
			</Container>

			<Container className="mt-3">
				<ListView
					managementToolbarProps={{
						filterFields: filters.subtasks as any,
						title: i18n.translate('subtasks'),
					}}
					resource={testraySubTaskImpl.resource}
					tableProps={{
						actions,
						columns: [
							{
								clickable: true,
								key: 'name',
								sorteable: true,
								value: i18n.translate('name'),
							},
							{
								clickable: true,
								key: 'dueStatus',
								render: (dueStatus: PickList) => (
									<StatusBadge
										type={
											dueStatus?.key.toLowerCase() as StatusBadgeType
										}
									>
										{dueStatus?.name}
									</StatusBadge>
								),
								sorteable: true,
								value: i18n.translate('status'),
							},
							{
								clickable: true,
								key: 'score',
								sorteable: true,
								value: i18n.translate('score'),
							},
							{
								clickable: true,
								key: 'tests',
								value: i18n.translate('tests'),
							},
							{
								key: 'errors',
								render: (value) => <Code>{value}</Code>,
								size: 'xl',
								value: i18n.translate('errors'),
							},
							{
								key: 'user',
								render: (
									_: any,
									subtask: TestraySubTask,
									mutate
								) => {
									if (subtask.user) {
										return (
											<Avatar
												className="text-capitalize"
												displayName
												name={`${subtask?.user?.emailAddress
													.split('@')[0]
													.replace('.', ' ')}`}
												size="sm"
											/>
										);
									}

									return (
										<AssignToMe
											onClick={() =>
												testraySubTaskImpl
													.assignToMe(subtask)
													.then(() => {
														updateItemFromList(
															mutate,
															0,
															{},
															{
																revalidate: true,
															}
														);
													})
											}
										/>
									);
								},
								value: i18n.translate('assignee'),
							},
						],
						navigateTo: (subtask) => `subtasks/${subtask.id}`,
						rowSelectable: true,
						rowWrap: true,
					}}
					transformData={(response) =>
						testraySubTaskImpl.transformDataFromList(response)
					}
					variables={{
						filter: subTaskFilter,
					}}
				>
					{(
						{items},
						{dispatch, listViewContext: {selectedRows}, mutate}
					) => {
						const selectedSubtasks: TestraySubTask[] = selectedRows.map(
							(rowId) => items.find(({id}) => rowId === id)
						);

						const alerts = getFloatingBoxAlerts(selectedSubtasks);

						return (
							<FloatingBox
								alerts={alerts}
								clearList={() =>
									dispatch({
										payload: [],
										type: ListViewTypes.SET_CHECKED_ROW,
									})
								}
								isVisible={!!selectedRows.length}
								onSubmit={() =>
									onMergeSubtasks(
										selectedSubtasks,
										mutate,
										dispatch
									)
								}
								primaryButtonProps={{
									disabled: !!alerts.length,
									title: i18n.translate('merge-subtasks'),
								}}
								selectedCount={selectedRows.length}
								tooltipText={i18n.translate(
									'merge-selected-subtasks-into-the-highest-scoring-subtask'
								)}
							/>
						);
					}}
				</ListView>
			</Container>

			<SubtaskCompleteModal
				modal={completeModal}
				mutate={mutateTask}
				subtask={completeModal.modalState}
			/>
		</>
	);
};

export default TestFlowTasks;
