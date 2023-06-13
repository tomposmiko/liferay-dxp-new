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

import ClayIcon from '@clayui/icon';
import ClayManagementToolbar from '@clayui/management-toolbar';
import {useNavigate, useParams} from 'react-router-dom';

import Button from '../../../components/Button';
import Container from '../../../components/Layout/Container';
import ListView, {ListViewProps} from '../../../components/ListView/ListView';
import {TableProps} from '../../../components/Table';
import {getRequirements} from '../../../graphql/queries';
import {FormModal} from '../../../hooks/useFormModal';
import i18n from '../../../i18n';
import {filters} from '../../../schema/filter';
import {searchUtil} from '../../../util/search';
import RequirementsModal from './RequirementModal';
import useRequirementActions from './useRequirementActions';

type RequirementListViewProps = {
	actions?: any[];
	formModal?: FormModal;
	projectId?: number | string;
	variables?: any;
} & {listViewProps?: Partial<ListViewProps>; tableProps?: Partial<TableProps>};

const RequirementListView: React.FC<RequirementListViewProps> = ({
	actions,
	formModal,
	listViewProps,
	tableProps,
	variables,
}) => {
	const navigate = useNavigate();

	return (
		<ListView
			forceRefetch={formModal?.forceRefetch}
			managementToolbarProps={{
				addButton: () => navigate('create'),
				buttons: (
					<>
						<Button displayType="secondary" symbol="redo" toolbar>
							{i18n.translate('import-jira-issues')}
						</Button>

						<ClayManagementToolbar.Item className="ml-2">
							<Button displayType="secondary" symbol="upload">
								{i18n.translate('upload-csv')}
							</Button>
						</ClayManagementToolbar.Item>
					</>
				),
				filterFields: filters.requirement as any,
				title: i18n.translate('requirements'),
			}}
			query={getRequirements}
			tableProps={{
				actions,
				columns: [
					{
						clickable: true,
						key: 'key',
						value: 'Key',
					},
					{
						key: 'linkTitle',
						render: (
							linkTitle: string,
							{linkURL}: {linkURL: string}
						) => (
							<a
								href={linkURL}
								rel="noopener noreferrer"
								target="_blank"
							>
								{linkTitle}

								<ClayIcon className="ml-2" symbol="shortcut" />
							</a>
						),
						value: 'Link',
					},
					{
						key: 'team',
						render: (_, {component}) => component?.team?.name,
						value: i18n.translate('team'),
					},
					{
						key: 'component',
						render: (component) => component?.name,
						value: i18n.translate('component'),
					},
					{
						key: 'components',
						value: i18n.translate('jira-components'),
					},
					{
						key: 'summary',
						size: 'md',
						value: i18n.translate('summary'),
					},
				],
				navigateTo: ({id}) => id?.toString(),
				...tableProps,
			}}
			transformData={(data) => data?.requirements}
			variables={variables}
			{...listViewProps}
		/>
	);
};

const Requirements = () => {
	const {actions, formModal} = useRequirementActions();
	const {projectId} = useParams();

	return (
		<>
			<Container>
				<RequirementListView
					actions={actions}
					formModal={formModal}
					variables={{
						filter: searchUtil.eq('projectId', projectId as string),
					}}
				/>
			</Container>

			<RequirementsModal
				modal={formModal.modal}
				projectId={Number(projectId)}
			/>
		</>
	);
};

export {RequirementListView};

export default Requirements;
