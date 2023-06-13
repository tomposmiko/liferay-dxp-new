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

import {gql} from '@apollo/client';

export type TestraySuite = {
	dateCreated: string;
	dateModified: string;
	description: string;
	name: string;
	testraySuiteId: number;
	type: string;
};

const testraySuiteFragment = gql`
	fragment TestraySuiteFragment on C_TestraySuite {
		dateCreated
		dateModified
		description
		name
		testraySuiteId
		type
	}
`;

export const getTestraySuite = gql`
	${testraySuiteFragment}

	query getTestraySuite($testraySuiteId: Long!) {
		c {
			testraySuite(testraySuiteId: $testraySuiteId) {
				...TestraySuiteFragment
			}
		}
	}
`;

export const getTestraySuites = gql`
	${testraySuiteFragment}

	query getTestraySuites(
		$filter: String
		$page: Int = 1
		$pageSize: Int = 20
	) {
		c {
			testraySuites(filter: $filter, page: $page, pageSize: $pageSize) {
				items {
					...TestraySuiteFragment
				}
				lastPage
				page
				pageSize
				totalCount
			}
		}
	}
`;
