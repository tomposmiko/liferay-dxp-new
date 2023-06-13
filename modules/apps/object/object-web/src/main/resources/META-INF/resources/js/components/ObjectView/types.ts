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

import {TYPES} from './context';

export type TName = {
	[key: string]: string;
};

export type TWorkflowStatus = {
	label: string;
	value: string;
};

export type TObjectField = {
	businessType: string;
	checked: boolean;
	filtered?: boolean;
	hasFilter?: boolean;
	id: number;
	indexed: boolean;
	indexedAsKeyword: boolean;
	indexedLanguageId: string;
	label: TName;
	listTypeDefinitionId: boolean;
	name: string;
	required: boolean;
	type: string;
};

export type TObjectColumn = {
	defaultSort?: boolean;
	fieldLabel?: string;
	filterBy?: string;
	label: TName;
	objectFieldBusinessType?: string;
	objectFieldName: string;
	priority?: number;
	sortOrder?: string;
	type?: string;
	value?: string;
	valueList?: LabelValueObject[];
};

export type TObjectViewColumn = {
	defaultSort: boolean;
	fieldLabel: string;
	label: TName;
	objectFieldName: string;
	priority?: number;
};

export type TObjectViewSortColumn = {
	fieldLabel: string;
	label: TName;
	objectFieldName: string;
	priority?: number;
	sortOrder?: string;
};

export type TObjectViewFilterColumn = {
	definition: {[key: string]: string[]} | null;
	fieldLabel: string;
	filterBy: string;
	filterType: string | null;
	label: TName;
	objectFieldBusinessType?: string;
	objectFieldName: string;
	value?: string;
	valueList?: LabelValueObject[];
};

export type TObjectView = {
	defaultObjectView: boolean;
	name: TName;
	objectViewColumns: TObjectViewColumn[];
	objectViewFilterColumns: TObjectViewFilterColumn[];
	objectViewSortColumns: TObjectViewSortColumn[];
};

export type TState = {
	ffUseMetadataAsSystemFields: boolean;
	isViewOnly: boolean;
	objectFields: TObjectField[];
	objectView: TObjectView;
	objectViewId: string;
	workflowStatusJSONArray: TWorkflowStatus[];
};

export type TAction = {
	payload: {[key: string]: any};
	type: keyof typeof TYPES;
};
