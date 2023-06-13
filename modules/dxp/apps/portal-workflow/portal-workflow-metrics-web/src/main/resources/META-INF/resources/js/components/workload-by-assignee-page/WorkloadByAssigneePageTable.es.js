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

import React, {useCallback, useContext, useMemo} from 'react';

import filterConstants from '../../shared/components/filter/util/filterConstants.es';
import ListHeadItem from '../../shared/components/list/ListHeadItem.es';
import ChildLink from '../../shared/components/router/ChildLink.es';
import UserAvatar from '../../shared/components/user-avatar/UserAvatar.es';
import {AppContext} from '../AppContext.es';
import {processStatusConstants} from '../filter/ProcessStatusFilter.es';
import {slaStatusConstants} from '../filter/SLAStatusFilter.es';

const Item = ({
	assignee: {id, image, name},
	onTimeTaskCount,
	overdueTaskCount,
	processId,
	taskCount,
	taskNames,
}) => {
	const {defaultDelta} = useContext(AppContext);

	const getFiltersQuery = useCallback(
		(slaStatus) => ({
			[filterConstants.assignee.key]: [id],
			[filterConstants.processStatus.key]: [
				processStatusConstants.pending,
			],
			[filterConstants.processStep.key]: taskNames,
			[filterConstants.slaStatus.key]: [slaStatus],
		}),
		[id, taskNames]
	);

	const instancesListPath = useMemo(
		() => `/instance/${processId}/${defaultDelta}/1`,
		[defaultDelta, processId]
	);

	return (
		<tr>
			<td className="lfr-title-column table-cell-expand table-title">
				<UserAvatar className="mr-3" image={image} />

				<ChildLink
					className="workload-by-step-link"
					query={{filters: getFiltersQuery()}}
					to={instancesListPath}
				>
					<span>{name}</span>
				</ChildLink>
			</td>

			<td className="table-cell-minw-75 text-right">
				<ChildLink
					className="workload-by-step-link"
					query={{
						filters: getFiltersQuery(slaStatusConstants.overdue),
					}}
					to={instancesListPath}
				>
					{overdueTaskCount}
				</ChildLink>
			</td>

			<td className="table-cell-minw-75 text-right">
				<ChildLink
					className="workload-by-step-link"
					query={{
						filters: getFiltersQuery(slaStatusConstants.onTime),
					}}
					to={instancesListPath}
				>
					{onTimeTaskCount}
				</ChildLink>
			</td>

			<td className="table-cell-minw-75 text-right">
				<ChildLink
					className="workload-by-step-link"
					query={{filters: getFiltersQuery()}}
					to={instancesListPath}
				>
					{taskCount}
				</ChildLink>
			</td>
		</tr>
	);
};

const Table = ({items, processId, taskNames}) => {
	return (
		<div className="table-responsive workflow-process-dashboard">
			<table className="table table-heading-nowrap table-hover table-list">
				<thead>
					<tr>
						<th
							className="table-cell-expand table-head-title"
							style={{width: '62%'}}
						>
							{Liferay.Language.get('assignee-name')}
						</th>

						<th className="table-cell-minw-75 table-head-title text-right">
							<ListHeadItem
								iconColor="danger"
								iconName="exclamation-circle"
								name="overdueTaskCount"
								title={Liferay.Language.get('overdue')}
							/>
						</th>

						<th className="table-cell-minw-75 table-head-title text-right">
							<ListHeadItem
								iconColor="success"
								iconName="check-circle"
								name="onTimeTaskCount"
								title={Liferay.Language.get('on-time')}
							/>
						</th>

						<th className="table-cell-minw-75 table-head-title text-right">
							<ListHeadItem
								name="taskCount"
								title={Liferay.Language.get('total-pending')}
							/>
						</th>
					</tr>
				</thead>

				<tbody>
					{items.map((item, index) => (
						<Table.Item
							{...item}
							key={index}
							processId={processId}
							taskNames={taskNames}
						/>
					))}
				</tbody>
			</table>
		</div>
	);
};

Table.Item = Item;

export {Table};
