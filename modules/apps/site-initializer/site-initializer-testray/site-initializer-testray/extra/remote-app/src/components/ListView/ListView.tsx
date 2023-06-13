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

import {ClayPaginationBarWithBasicItems} from '@clayui/pagination-bar';
import {
	ReactNode,
	memo,
	useCallback,
	useContext,
	useEffect,
	useMemo,
} from 'react';
import {KeyedMutator} from 'swr';

import ListViewContextProvider, {
	AppActions,
	InitialState as ListViewContextState,
	ListViewContext,
	ListViewContextProviderProps,
	ListViewTypes,
} from '../../context/ListViewContext';
import {useFetch} from '../../hooks/useFetch';
import i18n from '../../i18n';
import {APIResponse} from '../../services/rest';
import {SortDirection} from '../../types';
import {PAGINATION} from '../../util/constants';
import {SearchBuilder} from '../../util/search';
import EmptyState from '../EmptyState';
import Loading from '../Loading';
import ManagementToolbar, {ManagementToolbarProps} from '../ManagementToolbar';
import Table, {TableProps} from '../Table';

type ChildrenOptions = {
	dispatch: React.Dispatch<AppActions>;
	listViewContext: ListViewContextState;
	mutate: KeyedMutator<any>;
};

export type ListViewProps<T = any> = {
	children?: (response: APIResponse, options: ChildrenOptions) => ReactNode;
	forceRefetch?: number;
	managementToolbarProps?: {
		visible?: boolean;
	} & Omit<
		ManagementToolbarProps,
		| 'actions'
		| 'tableProps'
		| 'totalItems'
		| 'onSelectAllRows'
		| 'rowSelectable'
	>;
	onContextChange?: (context: ListViewContextState) => void;
	pagination?: {
		displayTop?: boolean;
	};
	resource: string;
	tableProps: Omit<
		TableProps,
		'items' | 'mutate' | 'onSelectAllRows' | 'onSort'
	>;
	transformData?: (data: T) => APIResponse<T>;
	variables?: any;
};

const ListView: React.FC<ListViewProps> = ({
	children,
	forceRefetch,
	managementToolbarProps: {
		visible: managementToolbarVisible = true,
		...managementToolbarProps
	} = {},
	onContextChange,
	resource,
	tableProps,
	transformData,
	variables: _variables,
	pagination = {displayTop: true},
}) => {
	const [listViewContext, dispatch] = useContext(ListViewContext);

	const {
		columns: columnsContext,
		filters,
		selectedRows,
		sort,
	} = listViewContext;

	const getURLSearchParams = useCallback(
		() => ({
			filter:
				SearchBuilder.createFilter(
					filters.filter,
					_variables?.filter
				) || '',
			forceRefetch,
			page: listViewContext.page,
			pageSize: listViewContext.pageSize,
			sort: sort.key ? `${sort.key}:${sort.direction.toLowerCase()}` : '',
		}),
		[
			forceRefetch,
			_variables?.filter,
			filters.filter,
			listViewContext.page,
			listViewContext.pageSize,
			sort.direction,
			sort.key,
		]
	);

	const {data: response, error, loading, mutate} = useFetch(resource, {
		transformData,
		...getURLSearchParams(),
	});

	const {actions = {}, items = [], page, pageSize, totalCount = 0} =
		response || {};

	const columns = useMemo(
		() =>
			tableProps.columns.filter(({key}) => {
				const columns = columnsContext || {};

				if (columns[key] === undefined) {
					return true;
				}

				return columns[key];
			}),
		[columnsContext, tableProps.columns]
	);

	const onSelectRow = useCallback(
		(rowId: number | number[]) => {
			dispatch({
				payload: rowId,
				type: ListViewTypes.SET_CHECKED_ROW,
			});
		},
		[dispatch]
	);

	const onSort = useCallback(
		(key: string, direction: SortDirection) => {
			dispatch({
				payload: {direction, key},
				type: ListViewTypes.SET_SORT,
			});
		},
		[dispatch]
	);

	const contextString = JSON.stringify(listViewContext);

	const onSelectAllRows = useCallback(() => {
		onSelectRow(items.map(({id}) => id));
	}, [items, onSelectRow]);

	useEffect(() => {
		if (onContextChange) {
			onContextChange(JSON.parse(contextString));
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [contextString]);

	useEffect(() => {
		if (tableProps.rowSelectable) {
			dispatch({
				payload: items.every(({id}) => selectedRows.includes(id)),
				type: ListViewTypes.SET_CHECKED_ALL_ROWS,
			});
		}
	}, [items, tableProps, selectedRows, dispatch]);

	if (error) {
		return <span>{error.message}</span>;
	}

	if (loading) {
		return <Loading />;
	}

	const Pagination = (
		<ClayPaginationBarWithBasicItems
			activeDelta={pageSize}
			activePage={page}
			deltas={PAGINATION.delta.map((label) => ({label}))}
			disableEllipsis={totalCount > 100}
			ellipsisBuffer={PAGINATION.ellipsisBuffer}
			labels={{
				paginationResults: i18n.translate('showing-x-to-x-of-x'),
				perPageItems: i18n.translate('x-items'),
				selectPerPageItems: i18n.translate('x-items'),
			}}
			onDeltaChange={(delta) =>
				dispatch({payload: delta, type: ListViewTypes.SET_PAGE_SIZE})
			}
			onPageChange={(page) =>
				dispatch({payload: page, type: ListViewTypes.SET_PAGE})
			}
			totalItems={totalCount}
		/>
	);

	return (
		<>
			{managementToolbarVisible && (
				<ManagementToolbar
					{...managementToolbarProps}
					actions={actions}
					tableProps={tableProps}
					totalItems={items.length}
				/>
			)}

			{!items.length && <EmptyState />}

			{children &&
				children(response as APIResponse, {
					dispatch,
					listViewContext,
					mutate,
				})}

			{!!items.length && (
				<>
					{pagination?.displayTop && (
						<div className="mt-4">{Pagination}</div>
					)}

					<Table
						{...tableProps}
						allRowsChecked={listViewContext.checkAll}
						columns={columns}
						items={items}
						mutate={mutate}
						onSelectAllRows={onSelectAllRows}
						onSelectRow={onSelectRow}
						onSort={onSort}
						selectedRows={selectedRows}
						sort={sort}
					/>

					{Pagination}
				</>
			)}
		</>
	);
};

const ListViewMemoized = memo(ListView);

const ListViewWithContext: React.FC<
	ListViewProps & {
		initialContext?: ListViewContextProviderProps;
	}
> = ({initialContext, ...otherProps}) => (
	<ListViewContextProvider {...initialContext}>
		<ListViewMemoized {...otherProps} />
	</ListViewContextProvider>
);

export default ListViewWithContext;
