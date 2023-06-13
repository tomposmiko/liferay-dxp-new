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
import {ClayInput, ClaySelect} from '@clayui/form';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import React, {useContext, useEffect, useState} from 'react';
import {withRouter} from 'react-router-dom';

import {AppContext} from '../../AppContext.es';
import Alert from '../../components/Alert.es';
import Link from '../../components/Link.es';
import PaginatedList from '../../components/PaginatedList.es';
import useQueryParams from '../../hooks/useQueryParams.es';
import {getTags} from '../../utils/client.es';
import lang from '../../utils/lang.es';
import {
	dateToInternationalHuman,
	historyPushWithSlug,
	useDebounceCallback,
} from '../../utils/utils.es';

function getOrderByOptions() {
	return [
		{
			label: Liferay.Language.get('latest-created'),
			value: 'latest-created',
		},
		{
			label: Liferay.Language.get('number-of-usages'),
			value: 'number-of-usages',
		},
	];
}

export default withRouter(({history, location}) => {
	const context = useContext(AppContext);

	const [error, setError] = useState({});
	const [searchBoxValue, setSearchBoxValue] = useState('');
	const [loading, setLoading] = useState(true);
	const [orderBy, setOrderBy] = useState('number-of-usages');
	const [page, setPage] = useState(1);
	const [pageSize, setPageSize] = useState(20);
	const [search, setSearch] = useState('');
	const [tags, setTags] = useState([]);

	useEffect(() => {
		getTags(orderBy, page, pageSize, search, context.siteKey)
			.then(({data, loading}) => {
				setTags(data || []);
				setLoading(loading);
				setSearchBoxValue(search);
			})
			.catch((error) => {
				if (process.env.NODE_ENV === 'development') {
					console.error(error);
				}
				setLoading(false);
				setError({message: 'Loading Tags', title: 'Error'});
			});
	}, [orderBy, page, pageSize, search, context.siteKey]);

	const queryParams = useQueryParams(location);

	useEffect(() => {
		setPage(+queryParams.get('page') || 1);
	}, [queryParams]);

	useEffect(() => {
		setPageSize(+queryParams.get('pagesize') || 20);
	}, [queryParams]);

	useEffect(() => {
		setSearch(queryParams.get('search') || '');
	}, [queryParams]);

	const historyPushParser = historyPushWithSlug(history.push);

	function buildURL(search, page, pageSize) {
		let url = '/tags?';

		if (search) {
			url += `search=${search}&`;
		}

		url += `page=${page}&pagesize=${pageSize}`;

		return url;
	}

	const changePage = (page, pageSize) => {
		historyPushParser(buildURL(search, page, pageSize));
	};

	const orderByOptions = getOrderByOptions();

	const [debounceCallback] = useDebounceCallback((search) => {
		setLoading(true);
		historyPushParser(buildURL(search, 1, pageSize));
	}, 500);

	return (
		<>
			<div className="container">
				<div className="d-flex flex-row">
					<div className="d-flex flex-column flex-grow-1">
						<ClayInput.Group className="c-mt-3 justify-content-end">
							<ClayInput.GroupItem shrink>
								<label
									className="align-items-center d-inline-flex m-0 text-secondary"
									htmlFor="tagsOrderBy"
								>
									{Liferay.Language.get('order-by')}
								</label>
							</ClayInput.GroupItem>

							<ClayInput.GroupItem shrink>
								<ClaySelect
									className="bg-transparent border-0"
									disabled={loading}
									id="tagsOrderBy"
									onChange={(event) => {
										setLoading(true);
										setOrderBy(event.target.value);
									}}
									value={orderBy}
								>
									{orderByOptions.map((option) => (
										<ClaySelect.Option
											key={option.value}
											label={option.label}
											value={option.value}
										/>
									))}
								</ClaySelect>
							</ClayInput.GroupItem>
						</ClayInput.Group>
					</div>

					<div className="d-flex flex-column flex-grow-0">
						<ClayInput.Group className="c-mt-3">
							<ClayInput.GroupItem>
								<ClayInput
									className="bg-transparent form-control input-group-inset input-group-inset-after"
									disabled={
										!search &&
										tags &&
										tags.items &&
										!tags.items.length
									}
									onChange={(event) => {
										setSearchBoxValue(event.target.value);
										debounceCallback(event.target.value);
									}}
									placeholder={Liferay.Language.get('search')}
									type="text"
									value={searchBoxValue}
								/>

								<ClayInput.GroupInsetItem
									after
									className="bg-transparent"
									tag="span"
								>
									{loading && <ClayLoadingIndicator small />}
									{!loading &&
										((!!search && (
											<ClayButtonWithIcon
												displayType="unstyled"
												onClick={() => {
													historyPushParser(
														buildURL(
															'',
															1,
															pageSize
														)
													);
												}}
												symbol="times-circle"
												type="submit"
											/>
										)) || (
											<ClayButtonWithIcon
												displayType="unstyled"
												symbol="search"
												type="search"
											/>
										))}
								</ClayInput.GroupInsetItem>
							</ClayInput.GroupItem>
						</ClayInput.Group>
					</div>
				</div>

				<div className="c-mt-3 row">
					<PaginatedList
						activeDelta={pageSize}
						activePage={page}
						changeDelta={(pageSize) => changePage(page, pageSize)}
						changePage={(page) => changePage(page, pageSize)}
						data={tags}
						emptyState={
							<ClayEmptyState
								className="empty-state-icon"
								title={Liferay.Language.get(
									'there-are-no-results'
								)}
							/>
						}
						loading={loading}
					>
						{(tag) => (
							<div
								className="col-md-3 question-tags"
								key={tag.id}
							>
								<Link
									title={tag.name}
									to={`/questions/tag/${tag.name}`}
								>
									<div className="card card-interactive card-interactive-primary card-type-template template-card-horizontal">
										<div className="card-body">
											<div className="card-row">
												<div className="autofit-col autofit-col-expand">
													<div className="autofit-section">
														<div className="card-title">
															<span className="text-truncate">
																{tag.name}
															</span>
														</div>
														{orderBy ===
														'latest-created' ? (
															<div>
																{lang.sub(
																	Liferay.Language.get(
																		'created-on'
																	),
																	[
																		dateToInternationalHuman(
																			tag.dateCreated
																		),
																	]
																)}
															</div>
														) : (
															<div>
																{lang.sub(
																	Liferay.Language.get(
																		'used-x-times'
																	),
																	[
																		tag.keywordUsageCount,
																	]
																)}
															</div>
														)}
													</div>
												</div>
											</div>
										</div>
									</div>
								</Link>
							</div>
						)}
					</PaginatedList>

					<Alert info={error} />
				</div>
			</div>
		</>
	);
});
