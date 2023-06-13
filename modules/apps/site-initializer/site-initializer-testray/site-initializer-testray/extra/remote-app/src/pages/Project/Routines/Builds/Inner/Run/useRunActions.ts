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

import useFormModal from '../../../../../../hooks/useFormModal';
import useMutate from '../../../../../../hooks/useMutate';
import useRuns from '../../../../../../hooks/useRuns';
import i18n from '../../../../../../i18n';
import {TestrayRun, testrayRunImpl} from '../../../../../../services/rest';
import {Action} from '../../../../../../types';

const useRunActions = () => {
	const {removeItemFromList} = useMutate();
	const {setRunA, setRunB} = useRuns();
	const formModal = useFormModal();
	const modal = formModal.modal;

	const actions = [
		{
			action: (run) => modal.open(run),
			icon: 'display',
			name: i18n.translate('select-environment-factors'),
			permission: 'UPDATE',
		},
		{
			action: ({id}, mutate) =>
				testrayRunImpl
					.remove(id)
					.then(() => removeItemFromList(mutate, id))
					.then(modal.onSave)
					.catch(modal.onError),
			icon: 'trash',
			name: i18n.translate('delete'),
			permission: 'DELETE',
		},
		{
			action: ({id}) => setRunA(id),
			icon: 'select-from-list',
			name: i18n.translate('select-run-a'),
		},
		{
			action: ({id}) => setRunB(id),
			icon: 'select-from-list',
			name: i18n.translate('select-run-b'),
		},
	] as Action<TestrayRun>[];

	return {
		actions,
		formModal,
	};
};
export default useRunActions;
