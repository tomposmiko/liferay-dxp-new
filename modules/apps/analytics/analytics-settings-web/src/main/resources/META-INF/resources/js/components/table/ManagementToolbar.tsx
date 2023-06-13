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

import ClayButton, {ClayButtonWithIcon} from '@clayui/button';
import {ClayDropDownWithItems} from '@clayui/drop-down';
import {ClayCheckbox, ClayInput} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayManagementToolbar, {
	ClayResultsBar,
} from '@clayui/management-toolbar';
import React, {useState} from 'react';

import {Events, useData, useDispatch} from './Context';
import {TColumn} from './types';
import {getOrderBy, getOrderBySymbol, getResultsLanguage} from './utils';

interface IManagementToolbarProps {
	columns: TColumn[];
	disabled: boolean;
	makeRequest: () => void;
}

const ManagementToolbar: React.FC<IManagementToolbarProps> = ({
	columns,
	disabled,
	makeRequest,
}) => {
	const {filter, globalChecked, keywords: storedKeywords, rows} = useData();
	const dispatch = useDispatch();

	const [keywords, setKeywords] = useState('');
	const [searchMobile, setSearchMobile] = useState(false);

	return (
		<>
			<ClayManagementToolbar>
				<ClayManagementToolbar.ItemList>
					<ClayManagementToolbar.Item>
						<ClayCheckbox
							checked={globalChecked}
							disabled={disabled}
							onChange={makeRequest}
						/>
					</ClayManagementToolbar.Item>

					<ClayDropDownWithItems
						items={columns
							.map(
								({
									expanded: _expanded,
									show: _show,
									...column
								}) => ({
									...column,
									onClick: () => {
										dispatch({
											payload: {
												value: column.value,
											},
											type: Events.ChangeFilter,
										});
									},
								})
							)
							.filter(({sortable = true}) => sortable)}
						trigger={
							<ClayButton
								className="nav-link"
								disabled={disabled}
								displayType="unstyled"
							>
								<span className="navbar-breakpoint-down-d-none">
									<span className="navbar-text-truncate">
										{Liferay.Language.get(
											'filter-and-order'
										)}
									</span>

									<ClayIcon
										className="inline-item inline-item-after"
										symbol="caret-bottom"
									/>
								</span>

								<span className="navbar-breakpoint-d-none">
									<ClayIcon symbol="filter" />
								</span>
							</ClayButton>
						}
					/>

					<ClayManagementToolbar.Item>
						<ClayButton
							className="nav-link nav-link-monospaced"
							disabled={disabled}
							displayType="unstyled"
							onClick={() => {
								dispatch({
									payload: {type: getOrderBy(filter)},
									type: Events.ChangeFilter,
								});
							}}
						>
							<ClayIcon symbol={getOrderBySymbol(filter)} />
						</ClayButton>
					</ClayManagementToolbar.Item>
				</ClayManagementToolbar.ItemList>

				<ClayManagementToolbar.Search
					onSubmit={(event) => {
						event.preventDefault();

						dispatch({
							payload: keywords,
							type: Events.ChangeKeywords,
						});
					}}
					showMobile={searchMobile}
				>
					<ClayInput.Group>
						<ClayInput.GroupItem>
							<ClayInput
								aria-label={Liferay.Language.get('search')}
								className="form-control input-group-inset input-group-inset-after"
								disabled={disabled}
								onChange={({target: {value}}) =>
									setKeywords(value)
								}
								placeholder={Liferay.Language.get('search')}
								type="text"
								value={keywords}
							/>

							<ClayInput.GroupInsetItem after tag="span">
								<ClayButtonWithIcon
									className="navbar-breakpoint-d-none"
									disabled={disabled}
									displayType="unstyled"
									onClick={() => setSearchMobile(false)}
									symbol="times"
								/>

								<ClayButtonWithIcon
									disabled={disabled}
									displayType="unstyled"
									symbol="search"
									type="submit"
								/>
							</ClayInput.GroupInsetItem>
						</ClayInput.GroupItem>
					</ClayInput.Group>
				</ClayManagementToolbar.Search>

				<ClayManagementToolbar.ItemList>
					<ClayManagementToolbar.Item className="navbar-breakpoint-d-none">
						<ClayButton
							className="nav-link nav-link-monospaced"
							disabled={disabled}
							displayType="unstyled"
							onClick={() => setSearchMobile(true)}
						>
							<ClayIcon symbol="search" />
						</ClayButton>
					</ClayManagementToolbar.Item>
				</ClayManagementToolbar.ItemList>
			</ClayManagementToolbar>

			{storedKeywords && (
				<ClayResultsBar>
					<ClayResultsBar.Item expand>
						<span className="component-text text-truncate-inline">
							<span className="text-truncate">
								<span>{getResultsLanguage(rows)}</span>

								<strong>{` "${storedKeywords}"`}</strong>
							</span>
						</span>
					</ClayResultsBar.Item>

					<ClayResultsBar.Item>
						<ClayButton
							className="component-link tbar-link"
							displayType="unstyled"
							onClick={() => {
								dispatch({
									payload: '',
									type: Events.ChangeKeywords,
								});

								setKeywords('');
							}}
						>
							{Liferay.Language.get('clear')}
						</ClayButton>
					</ClayResultsBar.Item>
				</ClayResultsBar>
			)}
		</>
	);
};

export default ManagementToolbar;
