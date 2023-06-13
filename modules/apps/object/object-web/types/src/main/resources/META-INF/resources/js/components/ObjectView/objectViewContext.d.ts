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
import {
	TName,
	TObjectView,
	TObjectViewSortColumn,
	TState,
	TWorkflowStatus,
} from './types';
interface IViewContextProps extends Array<TState | Function> {
	0: typeof initialState;
	1: React.Dispatch<React.ReducerAction<React.Reducer<TState, TAction>>>;
}
export declare enum TYPES {
	ADD_OBJECT_FIELDS = 'ADD_OBJECT_FIELDS',
	ADD_OBJECT_VIEW = 'ADD_OBJECT_VIEW',
	ADD_OBJECT_CUSTOM_VIEW_FIELD = 'ADD_OBJECT_CUSTOM_VIEW_FIELD',
	ADD_OBJECT_VIEW_COLUMN = 'ADD_OBJECT_VIEW_COLUMN',
	ADD_OBJECT_VIEW_SORT_COLUMN = 'ADD_OBJECT_VIEW_SORT_COLUMN',
	ADD_OBJECT_VIEW_FILTER_COLUMN = 'ADD_OBJECT_VIEW_FILTER_COLUMN',
	CHANGE_OBJECT_VIEW_NAME = 'CHANGE_OBJECT_VIEW_NAME',
	CHANGE_OBJECT_VIEW_COLUMN_ORDER = 'CHANGE_OBJECT_VIEW_COLUMN_ORDER',
	CHANGE_OBJECT_VIEW_SORT_COLUMN_ORDER = 'CHANGE_OBJECT_VIEW_SORT_COLUMN_ORDER',
	DELETE_OBJECT_VIEW_COLUMN = 'DELETE_OBJECT_VIEW_COLUMN',
	DELETE_OBJECT_VIEW_SORT_COLUMN = 'DELETE_OBJECT_VIEW_SORT_COLUMN',
	DELETE_OBJECT_VIEW_FILTER_COLUMN = 'DELETE_OBJECT_VIEW_FILTER_COLUMN',
	DELETE_OBJECT_CUSTOM_VIEW_FIELD = 'DELETE_OBJECT_CUSTOM_VIEW_FIELD',
	EDIT_OBJECT_VIEW_COLUMN_LABEL = 'EDIT_OBJECT_VIEW_COLUMN_LABEL',
	EDIT_OBJECT_VIEW_FILTER_COLUMN = 'EDIT_OBJECT_VIEW_FILTER_COLUMN',
	EDIT_OBJECT_VIEW_SORT_COLUMN_SORT_ORDER = 'EDIT_OBJECT_VIEW_SORT_COLUMN_SORT_ORDER',
	SET_OBJECT_VIEW_AS_DEFAULT = 'SET_OBJECT_VIEW_AS_DEFAULT',
}
declare const initialState: TState;
declare type TSortOptions = {
	label: string;
	value: string;
};
export declare type TAction =
	| {
			payload: {
				objectFields: ObjectField[];
				objectView: TObjectView;
			};
			type: TYPES.ADD_OBJECT_VIEW;
	  }
	| {
			payload: {
				selectedObjectFields: ObjectField[];
			};
			type: TYPES.ADD_OBJECT_VIEW_COLUMN;
	  }
	| {
			payload: {
				filterType?: string;
				objectFieldName: string;
				valueList?: IItem[];
			};
			type: TYPES.ADD_OBJECT_VIEW_FILTER_COLUMN;
	  }
	| {
			payload: {
				objectFieldName: string;
				objectFields: ObjectField[];
				objectViewSortColumns?: TObjectViewSortColumn[];
				selectedObjetSort: TSortOptions;
			};
			type: TYPES.ADD_OBJECT_VIEW_SORT_COLUMN;
	  }
	| {
			payload: {
				newName: string;
			};
			type: TYPES.CHANGE_OBJECT_VIEW_NAME;
	  }
	| {
			payload: {
				draggedIndex: number;
				targetIndex: number;
			};
			type: TYPES.CHANGE_OBJECT_VIEW_COLUMN_ORDER;
	  }
	| {
			payload: {
				draggedIndex: number;
				targetIndex: number;
			};
			type: TYPES.CHANGE_OBJECT_VIEW_SORT_COLUMN_ORDER;
	  }
	| {
			payload: {
				objectFieldName?: string;
			};
			type: TYPES.DELETE_OBJECT_VIEW_COLUMN;
	  }
	| {
			payload: {
				objectFieldName?: string;
			};
			type: TYPES.DELETE_OBJECT_VIEW_FILTER_COLUMN;
	  }
	| {
			payload: {
				objectFieldName?: string;
			};
			type: TYPES.DELETE_OBJECT_VIEW_SORT_COLUMN;
	  }
	| {
			payload: {
				editingObjectFieldName: string;
				translations: TName;
			};
			type: TYPES.EDIT_OBJECT_VIEW_COLUMN_LABEL;
	  }
	| {
			payload: {
				filterType?: string;
				objectFieldName?: string;
				valueList?: IItem[];
			};
			type: TYPES.EDIT_OBJECT_VIEW_FILTER_COLUMN;
	  }
	| {
			payload: {
				editingObjectFieldName: string;
				selectedObjectSort: string;
			};
			type: TYPES.EDIT_OBJECT_VIEW_SORT_COLUMN_SORT_ORDER;
	  }
	| {
			payload: {
				checked: boolean;
			};
			type: TYPES.SET_OBJECT_VIEW_AS_DEFAULT;
	  };
interface IViewContextProviderProps extends React.HTMLAttributes<HTMLElement> {
	value: {
		filterOperators: TFilterOperators;
		isViewOnly: boolean;
		objectViewId: string;
		workflowStatusJSONArray: TWorkflowStatus[];
	};
}
export declare function ViewContextProvider({
	children,
	value,
}: IViewContextProviderProps): JSX.Element;
export declare function useViewContext(): IViewContextProps;
export {};
