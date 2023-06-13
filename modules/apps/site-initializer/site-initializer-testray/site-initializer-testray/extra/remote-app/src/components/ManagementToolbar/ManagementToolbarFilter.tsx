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

import ClayButton from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import {useContext, useMemo, useState} from 'react';

import {ListViewContext, ListViewTypes} from '../../context/ListViewContext';
import useFormActions from '../../hooks/useFormActions';
import i18n from '../../i18n';
import {FilterSchema} from '../../schema/filter';
import {SearchBuilder} from '../../util/search';
import Form from '../Form';
import {RendererFields} from '../Form/Renderer';

type ManagementToolbarFilterProps = {
	filterSchema?: FilterSchema;
};

const ManagementToolbarFilter: React.FC<ManagementToolbarFilterProps> = ({
	filterSchema,
}) => {
	const fields = useMemo(() => filterSchema?.fields as RendererFields[], [
		filterSchema?.fields,
	]);

	const initialFilters = useMemo(() => {
		const initialValues: {[key: string]: string} = {};

		for (const field of fields) {
			initialValues[field.name] = '';
		}

		return initialValues;
	}, [fields]);

	const [, dispatch] = useContext(ListViewContext);
	const [filter, setFilter] = useState('');
	const [form, setForm] = useState(initialFilters);
	const formActions = useFormActions();

	const onChange = formActions.form.onChange({form, setForm});

	const onClear = () => {
		setForm(initialFilters);

		dispatch({
			payload: null,
			type: ListViewTypes.SET_CLEAR,
		});
	};

	const onApply = () => {
		const filterCleaned = SearchBuilder.removeEmptyFilter(form);

		const entries = Object.keys(filterCleaned).map((key) => ({
			label: fields?.find(({name}) => name === key)?.label,
			name: key,
			value: filterCleaned[key],
		}));

		dispatch({
			payload: {filters: {entries, filter: filterCleaned}},
			type: ListViewTypes.SET_UPDATE_FILTERS_AND_SORT,
		});
	};

	return (
		<ClayDropDown
			menuElementAttrs={{
				className: 'management-toolbar-filter-dropdown',
			}}
			menuWidth="sm"
			renderMenuOnClick
			trigger={
				<ClayButton className="nav-link" displayType="unstyled">
					<span className="navbar-breakpoint-down-d-none">
						<ClayIcon
							className="inline-item inline-item-after"
							symbol="filter"
						/>
					</span>

					<span className="navbar-breakpoint-d-none">
						<ClayIcon symbol="filter" />
					</span>
				</ClayButton>
			}
		>
			<div className="dropdown-header">
				<p className="font-weight-bold my-2">
					{i18n.translate('filter-results')}
				</p>

				<Form.Input
					name="search-filter"
					onChange={({target: {value}}) => setFilter(value)}
					placeholder={i18n.translate('search-filters')}
					value={filter}
				/>
			</div>

			<Form.Divider />

			<div className="popover-body">
				<Form.Renderer
					fields={fields}
					filter={filter}
					form={form}
					onChange={onChange}
				/>
			</div>

			<Form.Divider />

			<div className="popover-footer">
				<ClayButton onClick={onApply}>
					{i18n.translate('apply')}
				</ClayButton>

				<ClayButton
					className="ml-3"
					displayType="secondary"
					onClick={onClear}
				>
					{i18n.translate('clear')}
				</ClayButton>
			</div>
		</ClayDropDown>
	);
};

export default ManagementToolbarFilter;
