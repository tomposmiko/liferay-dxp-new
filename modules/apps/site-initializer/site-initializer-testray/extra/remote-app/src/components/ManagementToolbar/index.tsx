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

import ClayManagementToolbar from '@clayui/management-toolbar';
import {ReactNode, useContext} from 'react';

import {ListViewContext, ListViewTypes} from '../../context/ListViewContext';
import i18n from '../../i18n';
import {RendererFields} from '../Form/Renderer';
import {TableProps} from '../Table';
import ManagementToolbarLeft from './ManagementToolbarLeft';
import ManagementToolbarResultsBar from './ManagementToolbarResultsBar';
import ManagementToolbarRight, {IItem} from './ManagementToolbarRight';

export type ManagementToolbarProps = {
	addButton?: () => void;
	buttons?: ReactNode;
	display?: {
		columns?: boolean;
	};
	filterFields?: RendererFields[];
	tableProps: Omit<TableProps, 'items' | 'onSelectAllRows' | 'onSort'>;
	title?: string;
	totalItems: number;
};

const ManagementToolbar: React.FC<ManagementToolbarProps> = ({
	addButton,
	buttons,
	display,
	filterFields,
	tableProps,
	title,
	totalItems,
}) => {
	const [{columns: contextColumns, filters}, dispatch] = useContext(
		ListViewContext
	);

	const disabled = totalItems === 0;

	const columns = [
		{
			items: tableProps.columns.map((column) => ({
				checked: (contextColumns || {})[column.key] ?? true,
				label: column.value,
				onChange: (value: boolean) => {
					dispatch({
						payload: {
							columns: {
								...contextColumns,
								[column.key]: value,
							},
						},
						type: ListViewTypes.SET_COLUMNS,
					});
				},
				type: 'checkbox',
			})),
			label: i18n.translate('columns'),
			type: 'group',
		},
	];

	return (
		<>
			<ClayManagementToolbar>
				<ManagementToolbarLeft title={title} />

				<ManagementToolbarRight
					addButton={addButton}
					buttons={buttons}
					columns={columns as IItem[]}
					disabled={disabled}
					display={display}
					filterFields={filterFields}
				/>
			</ClayManagementToolbar>

			{!!filters.entries?.length && (
				<ManagementToolbarResultsBar totalItems={totalItems} />
			)}
		</>
	);
};

export default ManagementToolbar;
