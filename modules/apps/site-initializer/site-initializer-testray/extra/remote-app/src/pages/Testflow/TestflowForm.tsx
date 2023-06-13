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

import {useQuery} from '@apollo/client';
import ClayButton from '@clayui/button';
import {useState} from 'react';

import Form from '../../components/Form';
import Container from '../../components/Layout/Container';
import {
	CTypePagination,
	TestrayCaseType,
	getCaseTypes,
} from '../../graphql/queries';
import useFormModal from '../../hooks/useFormModal';
import i18n from '../../i18n';
import {searchUtil} from '../../util/search';
import {UserListView} from '../Manage/User';
import TestflowAssignUserModal from './modal';

const TestflowForm = () => {
	const [users, setUsers] = useState([]);
	const [modalType, setModalType] = useState('assign-users');

	const {data} = useQuery<CTypePagination<'caseTypes', TestrayCaseType>>(
		getCaseTypes
	);

	const caseTypes = data?.c.caseTypes?.items || [];

	const {modal} = useFormModal({
		onSave: setUsers,
	});

	const onOpenModal = (option: 'assign-users' | 'assign-user-groups') => {
		setModalType(option);

		modal.open();
	};

	return (
		<Container>
			<Form.Input label={i18n.translate('name')} name="name" required />

			<Form.Clay.Group>
				<label className="mb-2">{i18n.translate('case-type')}</label>

				{caseTypes.slice(0, 10).map((caseType, index) => (
					<Form.Checkbox key={index} label={caseType.name} />
				))}
			</Form.Clay.Group>

			<h3>{i18n.translate('users')}</h3>

			<Form.Divider />

			<Form.Clay.Group>
				<ClayButton
					displayType="secondary"
					onClick={() => onOpenModal('assign-users')}
				>
					{i18n.translate('assign-users')}
				</ClayButton>

				<ClayButton
					className="ml-2"
					displayType="secondary"
					onClick={() => onOpenModal('assign-user-groups')}
				>
					{i18n.translate('assign-user-groups')}
				</ClayButton>
			</Form.Clay.Group>

			{!!users.length && (
				<UserListView
					listViewProps={{
						managementToolbarProps: {visible: false},
						variables: {filter: searchUtil.in('id', users)},
					}}
				/>
			)}

			<Form.Footer onClose={() => null} onSubmit={() => null} />

			<TestflowAssignUserModal modal={modal} type={modalType as any} />
		</Container>
	);
};

export default TestflowForm;
