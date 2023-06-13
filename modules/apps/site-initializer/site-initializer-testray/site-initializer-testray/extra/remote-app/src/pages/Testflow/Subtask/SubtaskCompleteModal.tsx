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

import {useForm} from 'react-hook-form';
import {KeyedMutator} from 'swr';

import Form from '../../../components/Form';
import Container from '../../../components/Layout/Container';
import Modal from '../../../components/Modal';
import {withVisibleContent} from '../../../hoc/withVisibleContent';
import {FormModalOptions} from '../../../hooks/useFormModal';
import i18n from '../../../i18n';
import yupSchema, {yupResolver} from '../../../schema/yup';
import {TestraySubTask, testraySubTaskImpl} from '../../../services/rest';
import {CaseResultStatuses} from '../../../util/statuses';

type SubtaskForm = typeof yupSchema.subtask.__outputType;

type SubTaskCompleteModalProps = {
	modal: FormModalOptions;
	mutate?: KeyedMutator<any>;
	subtask: TestraySubTask;
};

const SubtaskCompleteModal: React.FC<SubTaskCompleteModalProps> = ({
	modal: {observer, onClose, onError, onSave},
	mutate,
	subtask,
}) => {
	const {register, watch} = useForm<SubtaskForm>({
		defaultValues: {dueStatus: CaseResultStatuses.FAILED},

		resolver: yupResolver(yupSchema.subtask),
	});

	const dueStatus = watch('dueStatus');

	const issue = watch('issue');

	const _onSubmit = () => {
		testraySubTaskImpl
			.complete(subtask.id, dueStatus as string)
			.then(mutate)
			.then(() => onSave())
			.catch(() => onError);
	};

	return (
		<Modal
			last={<Form.Footer onClose={onClose} onSubmit={_onSubmit} />}
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
						{label: 'Blocked', value: CaseResultStatuses.BLOCKED},
						{label: 'Failed', value: CaseResultStatuses.FAILED},
						{label: 'Passed', value: CaseResultStatuses.PASSED},
						{label: 'Test Fix', value: CaseResultStatuses.TEST_FIX},
					]}
					register={register}
				/>

				<Form.Input
					className="container-fluid-max-md"
					label={i18n.translate('issues')}
					name="issue"
					value={issue}
				/>

				<Form.Input
					className="container-fluid-max-md"
					label={i18n.translate('comment')}
					name="commentMBMessage"
					type="textarea"
				/>
			</Container>
		</Modal>
	);
};

export default withVisibleContent(SubtaskCompleteModal);
