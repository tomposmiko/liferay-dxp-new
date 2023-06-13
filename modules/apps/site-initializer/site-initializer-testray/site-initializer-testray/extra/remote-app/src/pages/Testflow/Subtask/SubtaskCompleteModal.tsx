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
import {useForm} from 'react-hook-form';

import Form from '../../../components/Form';
import Container from '../../../components/Layout/Container';
import Modal from '../../../components/Modal';
import {withVisibleContent} from '../../../hoc/withVisibleContent';
import {useFetch} from '../../../hooks/useFetch';
import {FormModalOptions} from '../../../hooks/useFormModal';
import i18n from '../../../i18n';
import yupSchema, {yupResolver} from '../../../schema/yup';
import {Liferay} from '../../../services/liferay';
import {
	TestraySubTask,
	TestraySubTaskIssue,
	liferayMessageBoardImpl,
	testraySubTaskImpl,
} from '../../../services/rest';
import {testraySubtaskIssuesImpl} from '../../../services/rest/TestraySubtaskIssues';
import {searchUtil} from '../../../util/search';
import {CaseResultStatuses} from '../../../util/statuses';

type SubtaskForm = typeof yupSchema.subtask.__outputType;

type SubTaskCompleteModalProps = {
	modal: FormModalOptions;
	revalidateSubtask: () => void;
	subtask: TestraySubTask;
};

const SubtaskCompleteModal: React.FC<SubTaskCompleteModalProps> = ({
	modal: {observer, onClose, onError, onSave},
	revalidateSubtask,
	subtask,
}) => {
	const {
		data: subTaskIssuesResponse,
		revalidate: revalidateSubtaskIssues,
	} = useFetch(testraySubtaskIssuesImpl.resource, {
		params: {
			filter: searchUtil.eq('subtaskId', subtask.id),
		},
		transformData: (response) =>
			testraySubtaskIssuesImpl.transformDataFromList(response),
	});

	const {data: mbMessage} = useFetch(
		liferayMessageBoardImpl.getMessagesIdURL(subtask.mbMessageId)
	);

	const subtaskIssues = subTaskIssuesResponse?.items || [];

	const issues = subtaskIssues
		.map((subtaskIssue: TestraySubTaskIssue) => subtaskIssue?.issue?.name)
		.join(', ');

	const {
		formState: {errors},
		handleSubmit,
		register,
		setValue,
	} = useForm<SubtaskForm>({
		defaultValues: {
			dueStatus: CaseResultStatuses.FAILED,
		},
		resolver: yupResolver(yupSchema.subtask),
	});

	const _onSubmit = async ({
		comment,
		dueStatus,
		issues = '',
	}: SubtaskForm) => {
		const _issues = issues
			.split(',')
			.map((name) => name.trim())
			.filter(Boolean);

		const commentSubtask = {
			comment,
			mbMessageId: subtask.mbMessageId,
			mbThreadId: subtask.mbThreadId,
			userId: Number(Liferay.ThemeDisplay.getUserId()),
		};

		try {
			await testraySubTaskImpl.complete(
				dueStatus as string,
				_issues,
				commentSubtask,
				subtask?.id
			);

			revalidateSubtask();

			revalidateSubtaskIssues();

			onSave();
		}
		catch (error) {
			onError(error);
		}
	};

	useEffect(() => {
		setValue('comment', mbMessage?.articleBody);
		setValue('issues', issues);
	}, [issues, mbMessage, setValue]);

	const inputProps = {
		errors,
		register,
	};

	return (
		<Modal
			last={
				<Form.Footer
					onClose={onClose}
					onSubmit={handleSubmit(_onSubmit)}
				/>
			}
			observer={observer}
			size="lg"
			title={i18n.sub('edit-x', 'status')}
			visible
		>
			<Container>
				<Form.Select
					className="container-fluid-max-md"
					defaultOption={false}
					label={i18n.translate('case-results-status')}
					name="dueStatus"
					options={[
						{
							label: i18n.translate('blocked'),
							value: CaseResultStatuses.BLOCKED,
						},
						{
							label: i18n.translate('failed'),
							value: CaseResultStatuses.FAILED,
						},
						{
							label: i18n.translate('passed'),
							value: CaseResultStatuses.PASSED,
						},
						{
							label: i18n.translate('test-fix'),
							value: CaseResultStatuses.TEST_FIX,
						},
					]}
					register={register}
				/>

				<Form.Input
					{...inputProps}
					className="container-fluid-max-md"
					label={i18n.translate('issues')}
					name="issues"
					register={register}
				/>

				<Form.Input
					{...inputProps}
					className="container-fluid-max-md"
					label={i18n.translate('comment')}
					name="comment"
					register={register}
					type="textarea"
				/>
			</Container>
		</Modal>
	);
};

export default withVisibleContent(SubtaskCompleteModal);
