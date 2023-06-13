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

import React from 'react';
import {TColumn, TTableRequestParams} from '../table/types';
import {TProperty} from './Properties';
export declare type TRawItem = {
	channelName?: string;
	friendlyURL?: string;
	id: string;
	name: string;
	siteName: string;
};
interface ITabProps {
	columns: Array<keyof TRawItem>;
	description?: string;
	emptyStateTitle: string;
	enableCheckboxs?: boolean;
	header: TColumn[];
	initialIds: number[];
	noResultsTitle: string;
	onItemsChange: (ids: number[]) => void;
	property: TProperty;
	requestFn: (params: TTableRequestParams) => Promise<any>;
}
declare const Tab: React.FC<ITabProps>;
export default Tab;
