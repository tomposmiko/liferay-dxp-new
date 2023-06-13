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

declare const API_URL: {
	FDS_ENTRIES: string;
	FDS_FIELDS: string;
	FDS_VIEWS: string;
};
declare const OBJECT_RELATIONSHIP: {
	readonly FDS_ENTRY_FDS_VIEW: 'fdsEntryFDSViewRelationship';
	readonly FDS_ENTRY_FDS_VIEW_ID: 'r_fdsEntryFDSViewRelationship_c_fdsEntryId';
	readonly FDS_VIEW_FDS_FIELD: 'fdsViewFDSFieldRelationship';
	readonly FDS_VIEW_FDS_FIELD_ID: 'r_fdsViewFDSFieldRelationship_c_fdsViewId';
};
declare const PAGINATION_PROPS: {
	pagination: {
		deltas: {
			label: number;
		}[];
		initialDelta: number;
	};
};
export {API_URL, OBJECT_RELATIONSHIP, PAGINATION_PROPS};
