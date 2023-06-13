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

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayForm, {ClayCheckbox} from '@clayui/form';
import React, {useState} from 'react';

import Form from '../../components/Form';
import Modal from '../../components/Modal';
import {FormModalOptions} from '../../hooks/useFormModal';
import i18n from '../../i18n';
import {
	testrayBuildRest,
	testrayProjectRest,
	testrayRoutineRest,
} from '../../services/rest';

type TestflowModalProps = {
	modal: FormModalOptions;
};

const TestflowForm = () => {
	const [form, setForm] = useState({
		testrayBuildId: 0,
		testrayProjectId: 0,
		testrayRoutineId: 0,
	});

	const onChange = (event: any) => {
		const {
			target: {checked, name, type, ...target},
		} = event;

		let {value} = target;

		if (type === 'checkbox') {
			value = checked;
		}

		setForm({
			...form,
			[name]: value,
		});
	};

	return (
		<>
			<Form.AutoComplete
				label="Project"
				onSearch={(keyword) => `contains(name, '${keyword}')`}
				resource="/projects"
				transformData={(response) =>
					testrayProjectRest.transformDataFromList(response)
				}
			/>

			<Form.AutoComplete
				label="Routine"
				onSearch={(keyword) => `contains(name, '${keyword}')`}
				resource="/routines"
				transformData={(response) =>
					testrayRoutineRest.transformDataFromList(response)
				}
			/>

			<Form.AutoComplete
				label="Build"
				onSearch={(keyword) => `contains(name, '${keyword}')`}
				resource={testrayBuildRest.resource}
				transformData={(response) =>
					testrayBuildRest.transformDataFromList(response)
				}
			/>
			<Form.Input label="Name" name="name" required />

			<div className="my-4">
				<ClayForm.Group>
					<label>{i18n.translate('case-type')}</label>

					<ClayCheckbox
						checked
						label={i18n.translate('automated-functional-test')}
						name="automatedFunctionalTest"
						onChange={onChange}
					/>

					<ClayCheckbox
						checked
						label={i18n.translate('automated-functional-test')}
						name="automatedFunctionalTest"
						onChange={onChange}
					/>

					<ClayCheckbox
						checked
						label={i18n.translate('automated-functional-test')}
						name="automatedFunctionalTest"
						onChange={onChange}
					/>
				</ClayForm.Group>
			</div>

			<h3>{i18n.translate('users')}</h3>
			<hr />

			<ClayButton.Group className="mb-4">
				<ClayButton displayType="secondary">
					{i18n.translate('assign-users')}
				</ClayButton>

				<ClayButton className="ml-3" displayType="secondary">
					{i18n.translate('assign-user-groups')}
				</ClayButton>
			</ClayButton.Group>

			<ClayAlert displayType="info">There are no linked users.</ClayAlert>
		</>
	);
};

const TestflowModal: React.FC<TestflowModalProps> = ({
	modal: {observer, onClose, visible},
}) => {
	const onSubmit = () => {};

	return (
		<Modal
			last={
				<Form.Footer
					isModal
					onClose={onClose}
					onSubmit={onSubmit}
					primaryButtonTitle="analyse"
				/>
			}
			observer={observer}
			size="lg"
			title={i18n.translate('new-task')}
			visible={visible}
		>
			<TestflowForm />
		</Modal>
	);
};

export default TestflowModal;
