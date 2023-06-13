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

import {useEffect} from 'react';
import {useOutletContext, useParams} from 'react-router-dom';

import Avatar from '../../../components/Avatar';
import AssignToMe from '../../../components/Avatar/AssigneToMe';
import Code from '../../../components/Code';
import Container from '../../../components/Layout/Container';
import Loading from '../../../components/Loading';
import StatusBadge from '../../../components/StatusBadge';
import {StatusBadgeType} from '../../../components/StatusBadge/StatusBadge';
import QATable from '../../../components/Table/QATable';
import {useFetch} from '../../../hooks/useFetch';
import useHeader from '../../../hooks/useHeader';
import i18n from '../../../i18n';
import {APIResponse, TestraySubTask, TestrayTask} from '../../../services/rest';
import {testraySubTaskImpl} from '../../../services/rest/TestraySubtask';
import {getTimeFromNow} from '../../../util/date';
import {searchUtil} from '../../../util/search';
import SubtasksCaseResults from './SubtaskCaseResults';
import SubtaskHeaderActions from './SubtaskHeaderActions';

type OutletContext = {
	testrayTask: TestrayTask;
};

const Subtasks = () => {
	const {setHeading} = useHeader();
	const {subtaskId} = useParams();
	const {testrayTask} = useOutletContext<OutletContext>();

	const {data: testraySubtask, mutate: mutateSubtask} = useFetch<
		TestraySubTask
	>(testraySubTaskImpl.getResource(subtaskId as string), (response) =>
		testraySubTaskImpl.transformData(response)
	);

	const {data: testraySubtaskToMerged} = useFetch<
		APIResponse<TestraySubTask>
	>(
		`${testraySubTaskImpl.resource}&filter=${searchUtil.eq(
			'r_mergedToTestraySubtask_c_subtaskId',
			subtaskId as string
		)}&pageSize=100&fields=name`,
		(response) => testraySubTaskImpl.transformDataFromList(response)
	);

	const mergedSubtaskNames = (testraySubtaskToMerged?.items || [])
		.map(({name}) => name)
		.join(', ');

	useEffect(() => {
		if (testraySubtask) {
			setTimeout(() => {
				setHeading([
					{
						category: i18n.translate('task'),
						path: `/testflow/${testrayTask.id}`,
						title: testrayTask.name,
					},
					{
						category: i18n.translate('subtask'),
						title: testraySubtask.name,
					},
				]);
			});
		}
	}, [setHeading, testraySubtask, testrayTask]);

	if (!testraySubtask) {
		return <Loading />;
	}

	return (
		<>
			<SubtaskHeaderActions
				mutateSubtask={mutateSubtask}
				subtask={testraySubtask}
			/>

			<Container
				className="pb-6"
				title={i18n.translate('subtask-details')}
			>
				<div className="d-flex flex-wrap">
					<div className="col-4 col-lg-4 col-md-12">
						<QATable
							items={[
								{
									title: i18n.translate('status'),
									value: (
										<StatusBadge
											type={
												testraySubtask.dueStatus.key.toLowerCase() as StatusBadgeType
											}
										>
											{testraySubtask.dueStatus.name}
										</StatusBadge>
									),
								},
								{
									title: i18n.translate('assignee'),
									value: testraySubtask.user ? (
										<Avatar
											displayName
											name={`${testraySubtask.user?.givenName} ${testraySubtask?.user?.additionalName}`}
										/>
									) : (
										<AssignToMe
											onClick={() =>
												testraySubTaskImpl
													.assignToMe(testraySubtask)
													.then(mutateSubtask as any)
											}
										/>
									),
								},
								{
									title: i18n.translate('updated'),
									value: getTimeFromNow(
										testraySubtask?.dateModified
									),
								},
								{
									title: i18n.translate('issues'),
									value: '-',
								},
								{
									title: i18n.translate('comment'),
									value: 'None',
								},
							]}
						/>
					</div>

					<div className="col-8 col-lg-8 col-md-12 pb-5">
						<QATable
							items={[
								{
									title: i18n.translate('score'),
									value: `${testraySubtask?.score}`,
								},
								{
									title: i18n.translate('error'),
									value: <Code>{testraySubtask.errors}</Code>,
								},
								{
									title: i18n.translate('merged-with'),
									value: mergedSubtaskNames,
									visible: !!mergedSubtaskNames.length,
								},
							]}
						/>
					</div>
				</div>
			</Container>

			<Container className="mt-5" title={i18n.translate('tests')}>
				<SubtasksCaseResults />
			</Container>
		</>
	);
};

export default Subtasks;
