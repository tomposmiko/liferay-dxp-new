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
import {ClayInput} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayManagementToolbar from '@clayui/management-toolbar';
import React, {useEffect, useState} from 'react';

import {slugToText} from '../../utils/utils.es';
import ManagementToolbarResults from './ManagementToolbarResults.es';

const ManagementToolbar = ({
	filterAndOrder,
	keywords,
	loading,
	maxNumberOfSearchResults,
	onClear,
	onSearch,
	plusButton,
	resultBar,
	search,
	subscribeButton,
	totalResults,
}) => {
	const [searchInput, setSearchInput] = useState('');
	const [searchMobile, setSearchMobile] = useState(false);

	const showResultsBar = !!(keywords || resultBar?.length);

	useEffect(() => {
		setSearchInput(slugToText(search));
	}, [search]);

	return (
		<div>
			<ClayManagementToolbar className="m-0 p-0">
				<ClayManagementToolbar.ItemList>
					<ClayManagementToolbar.Item>
						{subscribeButton}
					</ClayManagementToolbar.Item>

					<ClayManagementToolbar.Item>
						{filterAndOrder}
					</ClayManagementToolbar.Item>
				</ClayManagementToolbar.ItemList>

				<ClayManagementToolbar.Search
					onSubmit={(event) => {
						event.preventDefault();

						onSearch(searchInput);
					}}
					showMobile={searchMobile}
				>
					<ClayInput.Group>
						<ClayInput.GroupItem>
							<ClayInput
								aria-label={Liferay.Language.get('search')}
								className="form-control input-group-inset input-group-inset-after"
								disabled={loading}
								onChange={(event) =>
									setSearchInput(event.target.value)
								}
								placeholder={Liferay.Language.get('search')}
								type="text"
								value={searchInput}
							/>

							<ClayInput.GroupInsetItem after tag="span">
								<ClayButtonWithIcon
									className="navbar-breakpoint-d-none"
									displayType="unstyled"
									onClick={() => setSearchMobile(false)}
									symbol="times"
								/>

								<ClayButtonWithIcon
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
							displayType="unstyled"
							onClick={() => setSearchMobile(true)}
						>
							<ClayIcon symbol="search" />
						</ClayButton>
					</ClayManagementToolbar.Item>

					<ClayManagementToolbar.Item>
						{plusButton}
					</ClayManagementToolbar.Item>
				</ClayManagementToolbar.ItemList>
			</ClayManagementToolbar>

			{showResultsBar && (
				<ManagementToolbarResults
					keywords={keywords}
					maxNumberOfSearchResults={maxNumberOfSearchResults}
					onClear={onClear}
					resultBar={resultBar}
					totalResults={totalResults}
				/>
			)}
		</div>
	);
};

export default ManagementToolbar;
