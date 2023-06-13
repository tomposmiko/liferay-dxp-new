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

import {useParams} from 'react-router-dom';

import Avatar from '../../../../components/Avatar';
import AssignToMe from '../../../../components/Avatar/AssigneToMe';
import Code from '../../../../components/Code';
import Container from '../../../../components/Layout/Container';
import ListView from '../../../../components/ListView/ListView';
import StatusBadge from '../../../../components/StatusBadge';
import {TestrayCaseResult, getCaseResults} from '../../../../graphql/queries';
import useAssignCaseResult from '../../../../hooks/useAssignCaseResult';
import i18n from '../../../../i18n';
import {filters} from '../../../../schema/filter';
import {getStatusLabel} from '../../../../util/constants';
import {searchUtil} from '../../../../util/search';

const Build = () => {
	const {buildId} = useParams();
	const {onAssignToMe} = useAssignCaseResult();

	return (
		<Container className="mt-4">
			<ListView
				managementToolbarProps={{
					filterFields: filters.build.results as any,
					title: i18n.translate('tests'),
				}}
				query={getCaseResults}
				tableProps={{
					columns: [
						{
							clickable: true,
							key: 'priority',
							render: (
								_: any,
								{case: testrayCase}: TestrayCaseResult
							) => testrayCase?.priority,
							value: i18n.translate('priority'),
						},
						{
							key: 'component',
							render: (
								_: any,
								{case: testrayCase}: TestrayCaseResult
							) => testrayCase?.component?.name,
							value: i18n.translate('component'),
						},
						{
							clickable: true,
							key: 'name',
							render: (
								_: any,
								{case: testrayCase}: TestrayCaseResult
							) => testrayCase?.name,
							value: i18n.translate('case'),
						},
						{
							key: 'run',
							render: () => '01',
							value: i18n.translate('run'),
						},
						{
							key: 'user',
							render: (_: any, caseResult: TestrayCaseResult) =>
								caseResult?.user ? (
									<Avatar
										displayName
										name={caseResult.user.givenName}
									/>
								) : (
									<AssignToMe
										onClick={() => onAssignToMe(caseResult)}
									/>
								),
							value: i18n.translate('assignee'),
						},
						{
							key: 'dueStatus',
							render: (dueStatus: any) => (
								<StatusBadge type={getStatusLabel(dueStatus)}>
									{getStatusLabel(dueStatus)}
								</StatusBadge>
							),
							value: i18n.translate('status'),
						},
						{
							key: 'issues',
							value: i18n.translate('issues'),
						},
						{
							key: 'errors',
							render: (errors: string) =>
								errors && <Code>{errors}</Code>,
							value: i18n.translate('errors'),
						},
					],
					navigateTo: ({id}) => `case-result/${id}`,
				}}
				transformData={(data) => data?.caseResults}
				variables={{
					filter: searchUtil.eq('buildId', buildId as string),
				}}
			/>
		</Container>
	);
};

export default Build;
