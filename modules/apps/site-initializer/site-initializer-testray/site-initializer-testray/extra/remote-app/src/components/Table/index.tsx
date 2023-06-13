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

import {ClayCheckbox} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayTable from '@clayui/table';
import classNames from 'classnames';
import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {KeyedMutator} from 'swr';

import {Sort} from '../../context/ListViewContext';
import Permission from '../../core/Permission';
import useContextMenu from '../../hooks/useContextMenu';
import {APIResponse} from '../../services/rest';
import {Action, SortDirection, SortOption} from '../../types';
import ContextMenu from '../ContextMenu';

export type Column<T = any> = {
	clickable?: boolean;
	key: string;
	render?: (
		itemValue: any,
		item: T,
		mutate: KeyedMutator<APIResponse<T>>
	) => String | React.ReactNode;
	size?: 'sm' | 'md' | 'lg' | 'xl' | 'none';
	sorteable?: boolean;
	truncate?: boolean;
	value: string;
	width?: '50' | '75' | '100' | '200' | '250' | '300' | '350' | '400';
};

export type TableProps<T = any> = {
	actions?: Action[];
	allRowsChecked?: boolean;
	bodyVerticalAlignment?: 'bottom' | 'middle' | 'top';
	columns: Column<T>[];
	highlight?: (item: T) => boolean;
	items: T[];
	mutate: KeyedMutator<T>;
	navigateTo?: (item: T) => string;
	onClickRow?: (item: T) => void;
	onSelectAllRows: () => void;
	onSelectRow?: (row: any) => void;
	onSort: (columnTable: string, direction: SortDirection) => void;
	responsive?: boolean;
	rowSelectable?: boolean;
	rowWrap?: boolean;
	selectedRows?: number[];
	sort?: Sort;
};

const Table: React.FC<TableProps> = ({
	allRowsChecked = false,
	actions,
	columns,
	highlight,
	items,
	mutate,
	navigateTo,
	onClickRow,
	onSelectAllRows,
	onSelectRow,
	onSort,
	responsive,
	rowSelectable = false,
	rowWrap = false,
	selectedRows = [],
	sort,
	bodyVerticalAlignment = 'middle',
}) => {
	const [firstRowAction] = items;

	const filteredActions = actions
		? Permission.filterActions(actions, firstRowAction?.actions)
		: [];

	const displayActionColumn = !!filteredActions.length;

	const {
		contextMenuState,
		handleContext,
		setContextMenuState,
	} = useContextMenu(displayActionColumn);

	const [sorted, setSorted] = useState<SortDirection>(SortOption.ASC);

	const navigate = useNavigate();

	const changeSort = (key: string) => {
		onSort(key, sorted);
		setSorted(
			sorted === SortOption.DESC ? SortOption.ASC : SortOption.DESC
		);
	};

	const getSortSymbol = (key: string) => {
		if (!sort) {
			return '';
		}

		if (sort.key === key) {
			return sort.direction === SortOption.ASC
				? 'caret-top-l'
				: 'caret-bottom-l';
		}

		return 'caret-double-l';
	};

	return (
		<>
			<ClayTable
				borderless
				className="tr-table"
				hover
				responsive={responsive}
				tableVerticalAlignment={bodyVerticalAlignment}
			>
				<ClayTable.Head>
					<ClayTable.Row>
						{rowSelectable && (
							<ClayTable.Cell>
								<ClayCheckbox
									checked={allRowsChecked}
									onChange={() => {
										onSelectAllRows();
									}}
								/>
							</ClayTable.Cell>
						)}

						{columns.map((column, index) => (
							<ClayTable.Cell headingTitle key={index}>
								<div className="d-flex justify-content-between">
									<span
										className={classNames({
											'cursor-pointer': column.sorteable,
										})}
										onClick={() => changeSort(column.key)}
									>
										{column.value}
									</span>

									{column.sorteable && (
										<ClayIcon
											className="cursor-pointer"
											onClick={() =>
												changeSort(column.key)
											}
											symbol={getSortSymbol(column.key)}
										/>
									)}
								</div>
							</ClayTable.Cell>
						))}
					</ClayTable.Row>
				</ClayTable.Head>

				<ClayTable.Body>
					{items.map((item, rowIndex) => (
						<ClayTable.Row
							active={
								rowIndex === contextMenuState.rowIndex &&
								contextMenuState.visible
							}
							className={classNames('tr-table__row', {
								'text-nowrap': !rowWrap,
								'text-wrap': rowWrap,
								'tr-table__row--highligth':
									highlight && highlight(item),
							})}
							key={rowIndex}
							onContextMenu={(event) => {
								if (displayActionColumn) {
									handleContext({
										actions: filteredActions,
										event,
										item,
										rowIndex,
									});
								}
							}}
						>
							{rowSelectable && onSelectRow && (
								<ClayTable.Cell>
									<ClayCheckbox
										checked={selectedRows.includes(item.id)}
										onChange={() => onSelectRow(item.id)}
									/>
								</ClayTable.Cell>
							)}

							{columns.map((column, columnIndex) => (
								<ClayTable.Cell
									className={classNames('text-dark', {
										'cursor-pointer': column.clickable,
										[`table-cell-minw-${column.width}`]: column.width,
										'table-cell-expand':
											column.size === 'sm',
										'table-cell-expand-small':
											column.size === 'xl',
										'table-cell-expand-smaller':
											column.size === 'lg',
										'table-cell-expand-smallest':
											column.size === 'md',
									})}
									expanded={column.truncate}
									key={columnIndex}
									onClick={() => {
										if (column.clickable) {
											if (onClickRow) {
												onClickRow(item);
											}

											if (navigateTo) {
												navigate(navigateTo(item));
											}
										}
									}}
									truncate={column.truncate}
								>
									{column.render
										? column.render(
												item[column.key],
												{
													...item,
													rowIndex,
												},
												mutate
										  )
										: item[column.key]}
								</ClayTable.Cell>
							))}
						</ClayTable.Row>
					))}
				</ClayTable.Body>
			</ClayTable>

			{displayActionColumn && contextMenuState.visible && (
				<ContextMenu
					contextMenuState={contextMenuState}
					mutate={mutate}
					setContextMenuState={setContextMenuState}
				/>
			)}
		</>
	);
};

export default Table;
