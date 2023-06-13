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

import ListView from '../../../components/ListView';
import StatusBadge from '../../../components/StatusBadge';
import {StatusBadgeType} from '../../../components/StatusBadge/StatusBadge';
import i18n from '../../../i18n';
import {TestraySubTaskCaseResult} from '../../../services/rest';
import {testraySubtaskCaseResultImpl} from '../../../services/rest/TestraySubtaskCaseResults';

const SubtasksCaseResults = () => {
	return (
		<ListView
			managementToolbarProps={{
				visible: false,
			}}
			resource={testraySubtaskCaseResultImpl.resource}
			tableProps={{
				columns: [
					{
						clickable: true,
						key: 'run',
						render: (
							_,
							testraySubTaskCaseResult: TestraySubTaskCaseResult
						) => testraySubTaskCaseResult?.caseResult?.run?.number,

						value: i18n.translate('run'),
					},
					{
						clickable: true,
						key: 'priority',
						render: (
							_,
							testraySubTaskCaseResult: TestraySubTaskCaseResult
						) =>
							testraySubTaskCaseResult.caseResult?.case?.priority,

						value: i18n.translate('priority'),
					},
					{
						clickable: true,
						key: 'component',
						render: (
							_,
							testraySubTaskCaseResult: TestraySubTaskCaseResult
						) =>
							testraySubTaskCaseResult.caseResult?.component?.team
								?.name,
						value: i18n.translate('team'),
					},
					{
						clickable: true,
						key: 'component',
						render: (
							_,
							testraySubTaskCaseResult: TestraySubTaskCaseResult
						) =>
							testraySubTaskCaseResult.caseResult?.component
								?.name,

						value: i18n.translate('component'),
					},

					{
						clickable: true,
						key: 'case',
						render: (
							_,
							testraySubTaskCaseResult: TestraySubTaskCaseResult
						) => testraySubTaskCaseResult.caseResult?.case?.name,

						size: 'md',
						value: i18n.translate('case'),
					},
					{key: 'issues', value: i18n.translate('issues')},

					{
						key: 'dueStatus',
						render: (
							_,
							testraySubTaskCaseResult: TestraySubTaskCaseResult
						) => (
							<StatusBadge
								type={
									testraySubTaskCaseResult.caseResult
										?.dueStatus.key as StatusBadgeType
								}
							>
								{
									testraySubTaskCaseResult.caseResult
										?.dueStatus.name
								}
							</StatusBadge>
						),

						value: i18n.translate('status'),
					},
				],
				navigateTo: ({caseResult}) =>
					`/project/${caseResult.build?.project.id}/routines/${caseResult.build?.routine.id}/build/${caseResult.build?.id}/case-result/${caseResult.id}`,
				rowSelectable: true,
				rowWrap: true,
			}}
			transformData={(response) =>
				testraySubtaskCaseResultImpl.transformDataFromList(response)
			}
		/>
	);
};
export default SubtasksCaseResults;
