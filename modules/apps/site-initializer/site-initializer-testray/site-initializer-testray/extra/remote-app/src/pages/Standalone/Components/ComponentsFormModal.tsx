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

import Form from '../../../components/Form';
import Modal from '../../../components/Modal';
import SearchBuilder from '../../../core/SearchBuilder';
import {withVisibleContent} from '../../../hoc/withVisibleContent';
import {useFetch} from '../../../hooks/useFetch';
import {FormModalOptions} from '../../../hooks/useFormModal';
import i18n from '../../../i18n';
import yupSchema, {yupResolver} from '../../../schema/yup';
import {
	APIResponse,
	TestrayTeam,
	testrayComponentImpl,
} from '../../../services/rest';

type ComponentForm = typeof yupSchema.component.__outputType;

type ComponentProps = {
	modal: FormModalOptions;
	projectId: number;
};

const ComponentFormModal: React.FC<ComponentProps> = ({
	modal: {modalState, observer, onClose, onError, onSave, onSubmit},
	projectId,
}) => {
	const {
		formState: {errors, isSubmitting},
		handleSubmit,
		register,
		watch,
	} = useForm<ComponentForm>({
		defaultValues: modalState
			? {
					id: modalState.id,
					name: modalState.name,
					teamId: modalState.team?.id,
			  }
			: {
					teamId: '',
			  },
		resolver: yupResolver(yupSchema.component),
	});

	const {data: teamsResponse} = useFetch<APIResponse<TestrayTeam>>(`/teams`, {
		params: {
			fields: 'id,name',
			filter: SearchBuilder.eq('projectId', projectId),
			pageSize: 100,
			sort: 'name:asc',
		},
	});

	const teamId = watch('teamId');
	const teams = teamsResponse?.items || [];

	const _onSubmit = (componentForm: ComponentForm) =>
		onSubmit(
			{
				...componentForm,
				projectId,
			},
			{
				create: (data) => testrayComponentImpl.create(data),
				update: (id, data) => testrayComponentImpl.update(id, data),
			}
		)
			.then(onSave)
			.catch(onError);

	return (
		<Modal
			last={
				<Form.Footer
					onClose={onClose}
					onSubmit={handleSubmit(_onSubmit)}
					primaryButtonProps={{loading: isSubmitting}}
				/>
			}
			observer={observer}
			size="lg"
			title={i18n.translate(
				modalState?.id ? 'edit-component' : 'new-component'
			)}
			visible
		>
			<Form.Input
				errors={errors}
				label={i18n.translate('name')}
				name="name"
				register={register}
				required
			/>

			<Form.Select
				errors={errors}
				label={i18n.translate('team')}
				name="teamId"
				options={teams.map(({id, name}) => ({label: name, value: id}))}
				register={register}
				value={teamId}
			/>
		</Modal>
	);
};

export default withVisibleContent(ComponentFormModal);
