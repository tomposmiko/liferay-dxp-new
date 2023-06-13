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

import ListView from '../../../components/ListView';
import SearchBuilder from '../../../core/SearchBuilder';
import i18n from '../../../i18n';
import {testrayProductVersionImpl} from '../../../services/rest';
import TeamFormModal from './ProductVersionFormModal';
import useTeamActions from './useProductVersionActions';

type ProductVersionModalProps = {
	projectId: number;
};

const ProductVersionModal: React.FC<ProductVersionModalProps> = ({
	projectId,
}) => {
	const {actions, formModal} = useTeamActions();

	return (
		<>
			<ListView
				forceRefetch={formModal.forceRefetch}
				managementToolbarProps={{
					addButton: () => formModal.modal.open(),
				}}
				resource={testrayProductVersionImpl.resource}
				tableProps={{
					actions,
					columns: [
						{
							key: 'name',
							value: i18n.translate('name'),
						},
					],
				}}
				transformData={(response) =>
					testrayProductVersionImpl.transformDataFromList(response)
				}
				variables={{filter: SearchBuilder.eq('projectId', projectId)}}
			/>

			<TeamFormModal modal={formModal.modal} projectId={projectId} />
		</>
	);
};

export default ProductVersionModal;
