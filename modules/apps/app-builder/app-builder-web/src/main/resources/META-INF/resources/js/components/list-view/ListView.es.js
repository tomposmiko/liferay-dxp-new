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

import React, {useCallback, useContext, useEffect} from 'react';
import {withRouter} from 'react-router-dom';

import {AppContext} from '../../AppContext.es';
import useQuery from '../../hooks/useQuery.es';
import useResource from '../../hooks/useResource.es';
import {errorToast} from '../../utils/toast.es';
import ManagementToolbar from '../management-toolbar/ManagementToolbar.es';
import ManagementToolbarResultsBar, {
	getSelectedFilters,
} from '../management-toolbar/ManagementToolbarResultsBar.es';
import SearchContext, {reducer} from '../management-toolbar/SearchContext.es';
import TableWithPagination from '../table/TableWithPagination.es';

export default withRouter(
	({
		actions,
		addButton,
		children,
		columns,
		editMode,
		emptyState,
		endpoint,
		filters = [],
		history,
		noActionsMessage,
		queryParams,
		scope,
	}) => {
		const {defaultDelta = 20} = useContext(AppContext);
		const [query, setQuery] = useQuery(
			history,
			{
				filters: {},
				keywords: '',
				page: 1,
				pageSize: defaultDelta,
				sort: '',
				...queryParams,
			},
			scope
		);

		const dispatch = useCallback(
			(action) => setQuery(reducer(query, action)),
			[query, setQuery]
		);

		const params = {...query, ...query.filters};

		delete params.filters;

		const {error, isLoading, refetch, response} = useResource({
			endpoint,
			params,
		});

		let items = [];
		let totalCount = 0;
		let totalPages;

		if (response) {
			({items = [], totalCount, lastPage: totalPages} = response);
		}

		useEffect(() => {
			if (totalPages && Number(query.page) > totalPages) {
				dispatch({page: totalPages, type: 'CHANGE_PAGE'});
			}
		}, [dispatch, query.page, totalPages]);

		useEffect(() => {
			if (error) {
				errorToast();
			}
		}, [error]);

		let refetchOnActions;

		if (actions && actions.length > 0) {
			refetchOnActions = actions.map((action) => {
				if (!action.action) {
					return action;
				}

				return {
					...action,
					action: (item) => {
						action.action(item, refetch).then((isRefetch) => {
							if (!isRefetch) {
								return;
							}

							refetch();
						});
					},
				};
			});
		}

		const selectedFilters = getSelectedFilters(filters, query.filters);
		const isEmpty = totalCount === 0;
		const isFiltered = selectedFilters.length > 0;

		return (
			<SearchContext.Provider value={[query, dispatch]}>
				<ManagementToolbar
					addButton={addButton}
					columns={columns}
					disabled={!isFiltered && !query.keywords && isEmpty}
					filters={filters}
					totalCount={totalCount}
				/>

				<ManagementToolbarResultsBar
					filters={filters}
					isLoading={isLoading}
					totalCount={totalCount}
				/>

				<TableWithPagination
					actions={refetchOnActions}
					columns={columns}
					editMode={editMode}
					emptyState={emptyState}
					isEmpty={isEmpty}
					isFiltered={isFiltered}
					isLoading={isLoading}
					items={items.map((item, index) => children(item, index))}
					keywords={query.keywords}
					noActionsMessage={noActionsMessage}
					totalCount={totalCount}
				/>
			</SearchContext.Provider>
		);
	}
);
