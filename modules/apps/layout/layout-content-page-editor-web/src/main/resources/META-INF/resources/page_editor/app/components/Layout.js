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

import ClayAlert from '@clayui/alert';
import PropTypes from 'prop-types';
import React, {useEffect, useRef} from 'react';

import {
	LayoutDataPropTypes,
	getLayoutDataItemPropTypes,
} from '../../prop_types/index';
import {LAYOUT_DATA_ITEM_TYPES} from '../config/constants/layoutDataItemTypes';
import {config} from '../config/index';
import {useSelectItem} from '../contexts/ControlsContext';
import {useSelector} from '../contexts/StoreContext';
import {deepEqual} from '../utils/checkDeepEqual';
import useDropContainerId from '../utils/useDropContainerId';
import FragmentWithControls from './layout_data_items/FragmentWithControls';
import {
	CollectionItemWithControls,
	CollectionWithControls,
	ColumnWithControls,
	ContainerWithControls,
	DropZoneWithControls,
	FormWithControls,
	Root,
	RowWithControls,
} from './layout_data_items/index';

const LAYOUT_DATA_ITEMS = {
	[LAYOUT_DATA_ITEM_TYPES.collection]: CollectionWithControls,
	[LAYOUT_DATA_ITEM_TYPES.collectionItem]: CollectionItemWithControls,
	[LAYOUT_DATA_ITEM_TYPES.column]: ColumnWithControls,
	[LAYOUT_DATA_ITEM_TYPES.container]: ContainerWithControls,
	[LAYOUT_DATA_ITEM_TYPES.dropZone]: DropZoneWithControls,
	[LAYOUT_DATA_ITEM_TYPES.form]: FormWithControls,
	[LAYOUT_DATA_ITEM_TYPES.fragment]: FragmentWithControls,
	[LAYOUT_DATA_ITEM_TYPES.fragmentDropZone]: Root,
	[LAYOUT_DATA_ITEM_TYPES.root]: Root,
	[LAYOUT_DATA_ITEM_TYPES.row]: RowWithControls,
};

export default function Layout({mainItemId}) {
	const layoutData = useSelector((state) => state.layoutData);
	const layoutRef = useRef(null);
	const selectItem = useSelectItem();

	const mainItem = layoutData.items[mainItemId];

	const onClick = (event) => {
		if (event.target === event.currentTarget) {
			selectItem(null);
		}
	};

	useEffect(() => {
		const layout = layoutRef.current;

		const preventLinkClick = (event) => {
			const closestElement = event.target.closest('[href]');

			if (
				closestElement &&
				!closestElement.dataset.lfrPageEditorHrefEnabled
			) {
				event.preventDefault();
			}
		};

		if (layout) {
			layout.addEventListener('click', preventLinkClick);
		}

		return () => {
			if (layout) {
				layout.removeEventListener('click', preventLinkClick);
			}
		};
	}, [layoutRef]);

	const hasWarningMessages =
		config.isConversionDraft &&
		config.layoutConversionWarningMessages &&
		!!config.layoutConversionWarningMessages.length;

	return (
		<>
			{config.isConversionDraft && (
				<div className="page-editor__conversion-messages">
					<ClayAlert
						displayType="info"
						title={Liferay.Language.get(
							'page-conversion-description'
						)}
						variant="stripe"
					/>

					{hasWarningMessages && (
						<ClayAlert displayType="warning" variant="stripe">
							{config.layoutConversionWarningMessages.map(
								(message) => (
									<>
										{message}
										<br />
									</>
								)
							)}
						</ClayAlert>
					)}
				</div>
			)}

			{mainItem && (
				<>
					<div
						className="page-editor"
						id="page-editor"
						onClick={onClick}
						ref={layoutRef}
					>
						<LayoutDataItem
							item={mainItem}
							layoutData={layoutData}
						/>
					</div>

					<LayoutClassManager layoutRef={layoutRef} />
				</>
			)}
		</>
	);
}

Layout.propTypes = {
	mainItemId: PropTypes.string.isRequired,
};

class LayoutDataItem extends React.Component {
	static getDerivedStateFromError(error) {
		return {error};
	}

	static destructureItem(item, layoutData) {
		return {
			...item,
			children: item.children.map((child) =>
				LayoutDataItem.destructureItem(
					layoutData.items[child],
					layoutData
				)
			),
		};
	}

	constructor(props) {
		super(props);

		this.state = {
			error: null,
		};
	}

	shouldComponentUpdate(nextProps, nextState) {
		return (
			nextState.error ||
			!deepEqual(this.props.item, nextProps.item) ||
			!deepEqual(
				LayoutDataItem.destructureItem(
					this.props.item,
					this.props.layoutData
				),
				LayoutDataItem.destructureItem(
					nextProps.item,
					nextProps.layoutData
				)
			)
		);
	}

	render() {
		return this.state.error ? (
			<ClayAlert
				displayType="danger"
				title={Liferay.Language.get('error')}
			>
				{Liferay.Language.get(
					'an-unexpected-error-occurred-while-rendering-this-item'
				)}
			</ClayAlert>
		) : (
			<LayoutDataItemContent {...this.props} />
		);
	}
}

LayoutDataItem.propTypes = {
	item: getLayoutDataItemPropTypes().isRequired,
	layoutData: LayoutDataPropTypes.isRequired,
};

function LayoutDataItemContent({item, layoutData}) {
	const Component = LAYOUT_DATA_ITEMS[item.type];
	const componentRef = useRef(null);

	return (
		<>
			<Component item={item} layoutData={layoutData} ref={componentRef}>
				{item.children.map((childId) => {
					return (
						<LayoutDataItem
							item={layoutData.items[childId]}
							key={childId}
							layoutData={layoutData}
						/>
					);
				})}
			</Component>
		</>
	);
}

LayoutDataItemContent.propTypes = {
	item: getLayoutDataItemPropTypes().isRequired,
	layoutData: LayoutDataPropTypes.isRequired,
};

function LayoutClassManager({layoutRef}) {
	const dropContainerId = useDropContainerId();

	useEffect(() => {
		if (dropContainerId) {
			layoutRef.current?.classList.add('is-dragging');
		}
		else {
			layoutRef.current?.classList.remove('is-dragging');
		}
	}, [dropContainerId, layoutRef]);

	return null;
}
