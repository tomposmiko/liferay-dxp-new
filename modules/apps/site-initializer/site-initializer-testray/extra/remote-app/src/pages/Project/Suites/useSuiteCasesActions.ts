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

import {useNavigate, useParams} from 'react-router-dom';

import useFormModal from '../../../hooks/useFormModal';
import useMutate from '../../../hooks/useMutate';
import i18n from '../../../i18n';
import {TestraySuiteCase, deleteResource} from '../../../services/rest';
import {Action} from '../../../types';

const useSuiteCasesActions = ({isSmartSuite}: {isSmartSuite: boolean}) => {
	const {removeItemFromList} = useMutate();

	const {projectId} = useParams();
	const formModal = useFormModal();
	const navigate = useNavigate();

	const modal = formModal.modal;

	const actions: Action<TestraySuiteCase>[] = [
		{
			action: (suiteCase) =>
				navigate(
					`/project/${projectId}/cases/${suiteCase?.case?.id}/update`
				),
			icon: 'pencil',
			name: i18n.translate('edit'),
			permission: 'UPDATE',
		},
		{
			action: (suiteCase, mutate) =>
				deleteResource(`/suitescaseses/${suiteCase.id}`)
					?.then(() => removeItemFromList(mutate, suiteCase.id))
					.then(modal.onSuccess)
					.catch(modal.onError),
			disabled: isSmartSuite,
			icon: 'trash',
			name: i18n.translate('delete'),
			permission: 'DELETE',
		},
	];

	return {
		actions,
		formModal,
	};
};

export default useSuiteCasesActions;
