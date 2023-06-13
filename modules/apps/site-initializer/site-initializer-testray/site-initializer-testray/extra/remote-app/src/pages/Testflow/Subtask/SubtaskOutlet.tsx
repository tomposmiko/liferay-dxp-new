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
import {Outlet, useOutletContext, useParams} from 'react-router-dom';

import {useFetch} from '../../../hooks/useFetch';
import useHeader from '../../../hooks/useHeader';
import i18n from '../../../i18n';
import {
	APIResponse,
	TestraySubTask,
	TestrayTask,
	liferayMessageBoardImpl,
	testraySubTaskImpl,
} from '../../../services/rest';
import {testraySubtaskIssuesImpl} from '../../../services/rest/TestraySubtaskIssues';
import {SearchBuilder} from '../../../util/search';

type OutletContext = {
	data: {
		testrayTask: TestrayTask;
	};
	revalidate: {
		revalidateTaskUser: () => void;
	};
};

const SubtaskOutlet = () => {
	const {setHeading} = useHeader();
	const {subtaskId} = useParams();
	const {
		data: {testrayTask},
	} = useOutletContext<OutletContext>();

	const {
		data: testraySubtask,
		mutate: mutateSubtask,
		revalidate: revalidateSubtask,
	} = useFetch<TestraySubTask>(
		testraySubTaskImpl.getResource(subtaskId as string),
		{
			transformData: (response) =>
				testraySubTaskImpl.transformData(response),
		}
	);

	const {data: testraySubtaskToMerged} = useFetch<
		APIResponse<TestraySubTask>
	>(testraySubTaskImpl.resource, {
		params: {
			fields: 'name',
			filter: SearchBuilder.eq(
				'r_mergedToTestraySubtask_c_subtaskId',
				subtaskId as string
			),
			pageSize: 100,
		},
		transformData: (response) =>
			testraySubTaskImpl.transformDataFromList(response),
	});

	const {data, mutate: mutateSubtaskIssues} = useFetch(
		testraySubtaskIssuesImpl.resource,
		{
			params: {
				filter: SearchBuilder.eq('subtaskId', subtaskId as string),
			},
			transformData: (response) =>
				testraySubtaskIssuesImpl.transformDataFromList(response),
		}
	);

	const {data: mbMessage} = useFetch(
		testraySubtask?.mbMessageId
			? liferayMessageBoardImpl.getMessagesIdURL(
					testraySubtask.mbMessageId
			  )
			: null
	);

	const {data: testraySubtaskToSplit} = useFetch<APIResponse<TestraySubTask>>(
		testraySubTaskImpl.resource,
		{
			params: {
				fields: 'name',
				filter: SearchBuilder.eq(
					'r_splitFromTestraySubtask_c_subtaskId',
					subtaskId as string
				),
				pageSize: 100,
			},
			transformData: (response) =>
				testraySubTaskImpl.transformDataFromList(response),
		}
	);

	const subtaskIssues = data?.items || [];

	const mergedSubtaskNames = (testraySubtaskToMerged?.items || [])
		.map(({name}) => name)
		.join(', ');

	const splitSubtaskNames = (testraySubtaskToSplit?.items || [])
		.map(({name}) => name)
		.join(', ');

	useEffect(() => {
		if (testraySubtask) {
			setTimeout(() => {
				setHeading([
					{
						category: i18n.translate('task'),
						path: `/testflow/${testrayTask?.id}`,
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

	return (
		<Outlet
			context={{
				data: {
					mbMessage,
					mergedSubtaskNames,
					splitSubtaskNames,
					subtaskIssues,
					testraySubtask,
					testrayTask,
				},
				mutate: {
					mutateSubtask,
					mutateSubtaskIssues,
				},
				revalidate: {
					revalidateSubtask,
				},
			}}
		/>
	);
};

export default SubtaskOutlet;
