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

import ClayTabs from '@clayui/tabs';
import {
	API,
	SidePanelContent,
	invalidateRequired,
	openToast,
	saveAndReload,
} from '@liferay/object-js-components-web';
import React, {useContext, useEffect, useState} from 'react';

import BasicInfoScreen from './BasicInfoScreen/BasicInfoScreen';
import {DefaultSortScreen} from './DefaultSortScreen/DefaultSortScreen';
import {FilterScreen} from './FilterScreen/FilterScreen';
import ViewBuilderScreen from './ViewBuilderScreen/ViewBuilderScreen';
import ViewContext, {TYPES, ViewContextProvider} from './context';
import {TObjectView, TWorkflowStatus} from './types';

const TABS = [
	{
		Component: BasicInfoScreen,
		label: Liferay.Language.get('basic-info'),
	},
	{
		Component: ViewBuilderScreen,
		label: Liferay.Language.get('view-builder'),
	},
	{
		Component: DefaultSortScreen,
		label: Liferay.Language.get('default-sort'),
	},
	{
		Component: FilterScreen,
		label: Liferay.Language.get('filters'),
	},
];

const CustomView: React.FC<React.HTMLAttributes<HTMLElement>> = () => {
	const [{isViewOnly, objectView, objectViewId}, dispatch] = useContext(
		ViewContext
	);

	const [activeIndex, setActiveIndex] = useState<number>(0);
	const [loading, setLoading] = useState<boolean>(true);

	useEffect(() => {
		const makeFetch = async () => {
			const {
				defaultObjectView,
				name,
				objectDefinitionId,
				objectViewColumns,
				objectViewFilterColumns,
				objectViewSortColumns,
			} = await API.fetchJSON<TObjectView>(
				`/o/object-admin/v1.0/object-views/${objectViewId}`
			);

			const objectFields = await API.getObjectFields(objectDefinitionId);

			const objectView = {
				defaultObjectView,
				name,
				objectDefinitionId,
				objectViewColumns,
				objectViewFilterColumns,
				objectViewSortColumns,
			};

			dispatch({
				payload: {
					objectView,
				},
				type: TYPES.ADD_OBJECT_VIEW,
			});

			dispatch({
				payload: {
					objectFields,
					objectView,
				},
				type: TYPES.ADD_OBJECT_FIELDS,
			});

			setLoading(false);
		};

		makeFetch();
	}, [objectViewId, dispatch]);

	const removeUnnecessaryPropertiesFromObjectView = (
		objectView: TObjectView
	) => {
		const {
			objectViewColumns,
			objectViewFilterColumns,
			objectViewSortColumns,
		} = objectView;

		const newObjectViewColumns = objectViewColumns.map((viewColumn) => {
			return {
				label: viewColumn.label,
				objectFieldName: viewColumn.objectFieldName,
				priority: viewColumn.priority,
			};
		});

		const newObjectViewFilterColumns = objectViewFilterColumns.map(
			(filterColumn) => {
				return {
					filterType: filterColumn.filterType,
					json: JSON.stringify(filterColumn.definition),
					objectFieldName: filterColumn.objectFieldName,
				};
			}
		);

		const newObjectViewSortColumns = objectViewSortColumns.map(
			(sortColumn) => {
				return {
					objectFieldName: sortColumn.objectFieldName,
					priority: sortColumn.priority,
					sortOrder: sortColumn.sortOrder,
				};
			}
		);

		const newObjectView = {
			...objectView,
			objectViewColumns: newObjectViewColumns,
			objectViewFilterColumns: newObjectViewFilterColumns,
			objectViewSortColumns: newObjectViewSortColumns,
		};

		return newObjectView;
	};

	const handleSaveObjectView = async () => {
		const newObjectView = removeUnnecessaryPropertiesFromObjectView(
			objectView
		);

		const {objectViewColumns} = newObjectView;

		if (
			invalidateRequired(
				objectView.name[Liferay.ThemeDisplay.getDefaultLanguageId()]
			)
		) {
			openToast({
				message: Liferay.Language.get('a-name-is-required'),
				type: 'danger',
			});

			return;
		}

		if (!objectView.defaultObjectView || objectViewColumns.length !== 0) {
			try {
				await API.save(
					`/o/object-admin/v1.0/object-views/${objectViewId}`,
					newObjectView
				);
				saveAndReload();

				openToast({
					message: Liferay.Language.get(
						'modifications-saved-successfully'
					),
				});
			}
			catch (error) {
				openToast({
					message: (error as Error).message,
					type: 'danger',
				});
			}
		}
		else {
			openToast({
				message: Liferay.Language.get(
					'default-view-must-have-at-least-one-column'
				),
				type: 'danger',
			});
		}
	};

	return (
		<SidePanelContent
			onSave={handleSaveObjectView}
			readOnly={isViewOnly || loading}
			title={Liferay.Language.get('custom-view')}
		>
			<ClayTabs className="side-panel-iframe__tabs">
				{TABS.map(({label}, index) => (
					<ClayTabs.Item
						active={activeIndex === index}
						key={index}
						onClick={() => setActiveIndex(index)}
					>
						{label}
					</ClayTabs.Item>
				))}
			</ClayTabs>

			<ClayTabs.Content activeIndex={activeIndex} fade>
				{TABS.map(({Component}, index) => (
					<ClayTabs.TabPane key={index}>
						{!loading && <Component />}
					</ClayTabs.TabPane>
				))}
			</ClayTabs.Content>
		</SidePanelContent>
	);
};
interface ICustomViewWrapperProps extends React.HTMLAttributes<HTMLElement> {
	filterOperators: TFilterOperators;
	isViewOnly: boolean;
	objectViewId: string;
	workflowStatusJSONArray: TWorkflowStatus[];
}

const CustomViewWrapper: React.FC<ICustomViewWrapperProps> = ({
	filterOperators,
	isViewOnly,
	objectViewId,
	workflowStatusJSONArray,
}) => {
	return (
		<ViewContextProvider
			value={{
				filterOperators,
				isViewOnly,
				objectViewId,
				workflowStatusJSONArray,
			}}
		>
			<CustomView />
		</ViewContextProvider>
	);
};

export default CustomViewWrapper;
