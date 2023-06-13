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
import {Link} from 'react-router-dom';

import {useSidebarTask} from '../../../hooks/useSidebarTask';
import i18n from '../../../i18n';
import {TestraySubTask} from '../../../services/rest';
import {StatusesProgressScore, chartClassNames} from '../../../util/constants';
import TaskbarProgress from '../../ProgressBar/TaskbarProgress';
import Tooltip from '../../Tooltip';

type TaskBadgeProps = {
	className?: string;
	count: number;
};

type TaskSidebarProps = {
	expanded: boolean;
};

type SubtaskCardProps = {
	subtask: {
		id: number;
		name: string;
		score: number;
	};
	taskId: number;
};

const TaskBadge: React.FC<TaskBadgeProps> = ({className, count}) => (
	<span
		className={classNames(
			'align-items-center d-flex justify-content-center quantity-task-badge',
			className
		)}
	>
		{count}
	</span>
);

const SubtaskCard: React.FC<SubtaskCardProps> = ({subtask, taskId}) => (
	<Link
		className="align-items-center d-flex justify-content-between subtask-sidebar text-nowrap"
		to={`testflow/${taskId}/subtasks/${subtask?.id}`}
	>
		<div className="col-4">
			<span>{subtask.name}</span>
		</div>

		<div className="col-8 d-flex justify-content-end overflow-hidden">
			<span className="ellipsis-text mr-2">
				{i18n.translate('score')}
			</span>

			<span className="ellipsis-text">{subtask.score}</span>
		</div>
	</Link>
);

const TaskSidebar: React.FC<TaskSidebarProps> = ({expanded}) => {
	const {tasks} = useSidebarTask();

	const sidebarVisibility = {
		'task-sidebar-expanded': expanded,
		'task-sidebar-hidden': !expanded,
	};

	if (!tasks.length) {
		return null;
	}

	return (
		<div
			className={classNames('task-sidebar', {
				'overflow-hidden': !expanded,
			})}
		>
			<div
				className={classNames('d-flex sticky-top task-sidebar-title', {
					'task-sidebar-title-expanded': expanded,
					'task-sidebar-title-hidden': !expanded,
				})}
			>
				<Tooltip position="right" title={i18n.translate('tasks')}>
					<div
						className={classNames(
							'd-flex flex-row justify-content-between',
							sidebarVisibility
						)}
					>
						<span>{i18n.translate('tasks')}</span>

						<TaskBadge className="c-p-3 h6" count={tasks.length} />
					</div>

					<div
						className={classNames('notification', {
							'notification-hide': expanded,
							'notification-show': !expanded,
						})}
					>
						<ClayIcon fontSize={20} symbol="blogs" />

						{!!tasks.length && (
							<span className="task-sidebar-notification">
								{tasks.length}
							</span>
						)}
					</div>
				</Tooltip>
			</div>

			<ul className="list-unstyled">
				{tasks.map((task, index) => (
					<li
						className={classNames('mb-6 mt-4', sidebarVisibility)}
						key={index}
					>
						<Tooltip position="right" title={task?.name}>
							<Link
								className={classNames(
									'sidebar-link d-flex mb-2',
									sidebarVisibility
								)}
								to={`testflow/${task?.id}`}
							>
								<TaskBadge
									className="mr-2 p-2"
									count={task?.subTasks?.length as number}
								/>

								<div className="ellipsis-text">
									{task?.name}
								</div>
							</Link>
						</Tooltip>

						<Tooltip position="right" title={task?.build?.name}>
							<div className="ellipsis-text">
								<Link
									to={`/project/${task?.build?.project?.id}/routines/${task?.build?.routine?.id}/build/${task?.build?.id}`}
								>
									{task?.build?.name}
								</Link>
							</div>
						</Tooltip>

						<div
							className="mb-3"
							style={{height: '20px', overflow: 'hidden'}}
						>
							<TaskbarProgress
								displayTotalCompleted
								items={[
									[StatusesProgressScore.SELF, 0],
									[
										StatusesProgressScore.OTHER,
										Number(
											task?.subtaskScoreCompleted ?? 0
										),
									],
									[
										StatusesProgressScore.INCOMPLETE,
										Number(
											task?.subtaskScoreIncomplete ?? 0
										),
									],
								]}
								taskbarClassNames={chartClassNames}
							/>
						</div>

						{task?.subTasks?.map(
							(subtask: TestraySubTask, index) => (
								<SubtaskCard
									key={index}
									subtask={{
										id: subtask?.id,
										name: subtask?.name,
										score: subtask.score,
									}}
									taskId={task.id as number}
								/>
							)
						)}
					</li>
				))}
			</ul>
		</div>
	);
};
export default TaskSidebar;
