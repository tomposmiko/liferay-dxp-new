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
import ClayCard from '@clayui/card';
import ClayIcon from '@clayui/icon';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import React from 'react';

import {FRAGMENTS_DISPLAY_STYLES} from '../../../app/config/constants/fragmentsDisplayStyles';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../app/config/constants/layoutDataItemTypes';
import {useDispatch, useSelector} from '../../../app/contexts/StoreContext';
import {useToggleWidgetHighlighted} from '../../../app/contexts/WidgetsContext';
import selectSegmentsExperienceId from '../../../app/selectors/selectSegmentsExperienceId';
import addFragment from '../../../app/thunks/addFragment';
import addItem from '../../../app/thunks/addItem';
import addWidget from '../../../app/thunks/addWidget';
import toggleFragmentHighlighted from '../../../app/thunks/toggleFragmentHighlighted';
import {useDragSymbol} from '../../../app/utils/drag-and-drop/useDragAndDrop';

const ITEM_PROPTYPES_SHAPE = PropTypes.shape({
	data: PropTypes.object.isRequired,
	highlighted: PropTypes.bool,
	icon: PropTypes.string.isRequired,
	label: PropTypes.string.isRequired,
	preview: PropTypes.string,
	type: PropTypes.string.isRequired,
});

export default function TabItem({displayStyle, item}) {
	const dispatch = useDispatch();
	const segmentsExperienceId = useSelector(selectSegmentsExperienceId);

	const toggleWidgetHighlighted = useToggleWidgetHighlighted();

	const onToggleHighlighted = () => {
		const nextValue = Boolean(!item.highlighted);

		if (item.data.portletId) {
			toggleWidgetHighlighted(item.data.portletId, nextValue);
		}
		else {
			dispatch(
				toggleFragmentHighlighted({
					fragmentEntryKey: item.itemId,
					highlighted: !item.highlighted,
				})
			);
		}
	};

	const {isDraggingSource, sourceRef} = useDragSymbol(
		{
			fragmentEntryType: item.data.type,
			icon: item.icon,
			label: item.label,
			type: item.type,
		},
		(parentId, position) => {
			let thunk;

			if (item.type === LAYOUT_DATA_ITEM_TYPES.fragment) {
				if (item.data.portletId) {
					thunk = addWidget;
				}
				else {
					thunk = addFragment;
				}
			}
			else {
				thunk = addItem;
			}

			dispatch(
				thunk({
					...item.data,
					itemType: item.type,
					parentItemId: parentId,
					position,
					store: {segmentsExperienceId},
				})
			);
		}
	);

	return displayStyle === FRAGMENTS_DISPLAY_STYLES.CARDS ? (
		<CardItem
			disabled={item.disabled || isDraggingSource}
			item={item}
			onToggleHighlighted={onToggleHighlighted}
			ref={sourceRef}
		/>
	) : (
		<ListItem
			disabled={item.disabled || isDraggingSource}
			item={item}
			onToggleHighlighted={onToggleHighlighted}
			ref={item.disabled ? null : sourceRef}
		/>
	);
}

TabItem.propTypes = {
	displayStyle: PropTypes.oneOf(Object.values(FRAGMENTS_DISPLAY_STYLES)),
	item: ITEM_PROPTYPES_SHAPE.isRequired,
};

const ListItem = React.forwardRef(
	({disabled, item, onToggleHighlighted}, ref) => {
		return (
			<li
				className={classNames(
					'align-items-center mb-1 d-flex page-editor__fragments-widgets__tab-list-item rounded',
					{
						disabled,
						'ml-3 page-editor__fragments-widgets__tab-portlet-item':
							item.data.portletItemId,
					}
				)}
				ref={ref}
			>
				<div className="align-items-center d-flex page-editor__fragments-widgets__tab-list-item-body">
					<ClayIcon className="mr-3" symbol={item.icon} />

					<div className="text-truncate title">{item.label}</div>
				</div>

				<HighlightButton
					item={item}
					onToggleHighlighted={onToggleHighlighted}
				/>
			</li>
		);
	}
);

ListItem.propTypes = {
	disabled: PropTypes.bool.isRequired,
	item: ITEM_PROPTYPES_SHAPE.isRequired,
	onToggleHighlighted: PropTypes.func.isRequired,
};

const CardItem = React.forwardRef(
	({disabled, item, onToggleHighlighted}, ref) => {
		return (
			<li
				className={classNames(
					'mb-2 page-editor__fragments-widgets__tab-card-item',
					{
						disabled,
					}
				)}
				ref={ref}
			>
				<ClayCard
					aria-label={item.label}
					displayType={item.preview ? 'image' : 'file'}
					selectable
				>
					<ClayCard.AspectRatio className="card-item-first">
						{item.preview ? (
							<img
								alt="thumbnail"
								className="aspect-ratio-item aspect-ratio-item-center-middle aspect-ratio-item-fluid"
								src={item.preview}
							/>
						) : (
							<div className="aspect-ratio-item aspect-ratio-item-center-middle aspect-ratio-item-fluid card-type-asset-icon">
								<ClayIcon symbol={item.icon} />
							</div>
						)}
					</ClayCard.AspectRatio>

					<ClayCard.Body className="align-items-center d-flex p-2 rounded-bottom">
						<ClayCard.Row>
							<div className="autofit-col autofit-col-expand">
								<section className="autofit-section">
									<ClayCard.Description
										displayType="title"
										title={item.label}
										truncate={false}
									>
										<span className="align-items-center d-flex justify-content-between">
											<span
												className="lfr-portal-tooltip text-truncate"
												data-tooltip-align="center"
											>
												{item.label}
											</span>

											<HighlightButton
												item={item}
												onToggleHighlighted={
													onToggleHighlighted
												}
											/>
										</span>
									</ClayCard.Description>
								</section>
							</div>
						</ClayCard.Row>
					</ClayCard.Body>
				</ClayCard>
			</li>
		);
	}
);

CardItem.propTypes = {
	disabled: PropTypes.bool.isRequired,
	item: ITEM_PROPTYPES_SHAPE.isRequired,
	onToggleHighlighted: PropTypes.func.isRequired,
};

const HighlightButton = ({item, onToggleHighlighted}) => {
	if (!Liferay.FeatureFlags['LPS-158737'] || item.data.portletItemId) {
		return null;
	}

	const {highlighted} = item;

	return (
		<ClayButtonWithIcon
			aria-label={
				highlighted
					? Liferay.Language.get('unmark-favorite')
					: Liferay.Language.get('mark-favorite')
			}
			borderless
			className={classNames(
				'page-editor__fragments-widgets__tab-highlight-button',
				{highlighted}
			)}
			displayType="secondary"
			onClick={onToggleHighlighted}
			symbol={highlighted ? 'star' : 'star-o'}
		/>
	);
};

HighlightButton.propTypes = {
	item: ITEM_PROPTYPES_SHAPE.isRequired,
	onToggleHighlighted: PropTypes.func.isRequired,
};
