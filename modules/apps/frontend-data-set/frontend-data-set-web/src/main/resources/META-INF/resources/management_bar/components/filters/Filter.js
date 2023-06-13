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

import ClayLoadingIndicator from '@clayui/loading-indicator';
import React, {useContext, useEffect, useState} from 'react';

import {getComponentByModuleURL} from '../../../utils/modules';
import ViewsContext from '../../../views/ViewsContext';
import {VIEWS_ACTION_TYPES} from '../../../views/viewsReducer';
import AutocompleteFilter, {
	getOdataString as getAutocompleteFilterOdataString,
	getSelectedItemsLabel as getAutocompleteFilterSelectedItemsLabel,
} from './AutocompleteFilter';
import DateRangeFilter, {
	getOdataString as getDateRangeFilterOdataString,
	getSelectedItemsLabel as getDateRangeFilterSelectedItemsLabel,
} from './DateRangeFilter';
import SelectionFilter, {
	getOdataString as getSelectionFilterOdataString,
	getSelectedItemsLabel as getSelectionFilterSelectedItemsLabel,
} from './SelectionFilter';

const FILTER_TYPE_COMPONENT = {
	autocomplete: AutocompleteFilter,
	dateRange: DateRangeFilter,
	selection: SelectionFilter,
};

const getFilterSelectedItemsLabel = (filter) => {
	switch (filter.type) {
		case 'autocomplete':
			return getAutocompleteFilterSelectedItemsLabel(filter);
		case 'dateRange':
			return getDateRangeFilterSelectedItemsLabel(filter);
		case 'selection':
			return getSelectionFilterSelectedItemsLabel(filter);
		default:
			return '';
	}
};

const getOdataFilterString = (filter) => {
	switch (filter.type) {
		case 'autocomplete':
			return getAutocompleteFilterOdataString(filter);
		case 'dateRange':
			return getDateRangeFilterOdataString(filter);
		case 'selection':
			return getSelectionFilterOdataString(filter);
		default:
			return '';
	}
};

const Filter = ({moduleURL, type, ...otherProps}) => {
	const [{filters}, viewsDispatch] = useContext(ViewsContext);

	const [Component, setComponent] = useState(() => {
		if (!moduleURL) {
			const Matched = FILTER_TYPE_COMPONENT[type];

			if (!Matched) {
				throw new Error(`Filter type '${type}' not found.`);
			}

			return Matched;
		}
		else {
			return null;
		}
	});

	useEffect(() => {
		if (moduleURL) {
			getComponentByModuleURL(moduleURL).then((FetchedComponent) =>
				setComponent(() => FetchedComponent)
			);
		}
	}, [moduleURL]);

	const setFilter = ({id, ...otherProps}) => {
		viewsDispatch({
			type: VIEWS_ACTION_TYPES.UPDATE_FILTERS,
			value: filters.map((filter) => ({
				...filter,
				...(filter.id === id ? {...otherProps} : {}),
			})),
		});
	};

	return Component ? (
		<div className="data-set-filter">
			<Component setFilter={setFilter} {...otherProps} />
		</div>
	) : (
		<ClayLoadingIndicator small />
	);
};

export {getFilterSelectedItemsLabel, getOdataFilterString};
export default Filter;
