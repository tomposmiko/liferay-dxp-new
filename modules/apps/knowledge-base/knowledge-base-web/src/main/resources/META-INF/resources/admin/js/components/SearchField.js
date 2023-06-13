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

import {ClayButtonWithIcon} from '@clayui/button';
import ClayEmptyState from '@clayui/empty-state';
import {ClayInput} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayLink from '@clayui/link';
import ClayList from '@clayui/list';
import {sub} from 'frontend-js-web';
import React, {useEffect, useState} from 'react';

const ITEM_TYPES_SYMBOL = {
	article: 'document-text',
	folder: 'folder',
};

const SEARCH_DELTA = 2;

const highlithKeywordInText = (text, keyword) => {
	const parts = text.split(new RegExp(`(${keyword})`, 'gi'));

	return (
		<span>
			{parts.map((part) =>
				part.toLowerCase() === keyword.toLowerCase() ? (
					<b>{part}</b>
				) : (
					part
				)
			)}
		</span>
	);
};

const SearchResult = ({filteredItems, keyword}) => {
	return filteredItems.length ? (
		<ClayList role="list">
			{filteredItems.map((item) => {
				return (
					<ClayList.ItemField expand key={item.id}>
						<ClayLink
							className="p-1"
							displayType="secondary"
							href={item.href}
						>
							<ClayIcon
								className="mr-2"
								symbol={ITEM_TYPES_SYMBOL[item.type]}
							/>

							{highlithKeywordInText(item.name, keyword)}
						</ClayLink>
					</ClayList.ItemField>
				);
			})}
		</ClayList>
	) : (
		<ClayEmptyState
			description={sub(
				Liferay.Language.get(
					'no-content-was-found-that-matched-keyword-x'
				),
				[keyword]
			)}
			imgSrc={`${themeDisplay.getPathThemeImages()}/states/search_state.gif`}
			small
			title={Liferay.Language.get('no-results-found')}
		/>
	);
};

export default function SearchField({handleSearchChange, items}) {
	const initialSearchInfo = {
		filteredItems: [],
		query: '',
	};

	const [searchInfo, setSearchInfo] = useState(initialSearchInfo);

	const [searchActive, setSearchActive] = useState(false);

	useEffect(() => {
		handleSearchChange({isSearchActive: searchActive});
	}, [handleSearchChange, searchActive]);

	const handleQueryChange = (event) => {
		const newQuery = event.target.value;
		let results = [];

		if (newQuery.length > SEARCH_DELTA) {
			results = items.filter((item) =>
				item.name.toLowerCase().includes(newQuery.toLowerCase())
			);
		}

		setSearchActive(newQuery.length > SEARCH_DELTA);
		setSearchInfo({
			filteredItems: results,
			query: newQuery,
		});
	};

	const handleSearchClick = () => {
		if (searchInfo.query) {
			setSearchInfo(initialSearchInfo);
			setSearchActive(false);
		}
	};

	const iconTitle = searchInfo.query
		? Liferay.Language.get('clear')
		: Liferay.Language.get('search');

	return (
		<>
			<ClayInput.Group small>
				<ClayInput.GroupItem>
					<ClayInput
						aria-label={Liferay.Language.get('search-for')}
						className="form-control input-group-inset input-group-inset-after"
						onChange={handleQueryChange}
						placeholder={Liferay.Language.get('search-for')}
						type="text"
						value={searchInfo.query}
					/>

					<ClayInput.GroupInsetItem after tag="span">
						<ClayButtonWithIcon
							aria-label={iconTitle}
							displayType="unstyled"
							onClick={handleSearchClick}
							small
							symbol={searchInfo.query ? 'times' : 'search'}
							title={iconTitle}
						/>
					</ClayInput.GroupInsetItem>
				</ClayInput.GroupItem>
			</ClayInput.Group>

			<hr className="separator" />

			{searchActive && (
				<SearchResult
					filteredItems={searchInfo.filteredItems}
					keyword={searchInfo.query}
				/>
			)}
		</>
	);
}
