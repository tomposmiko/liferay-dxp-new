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

import ClayButton from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import ClayLabel from '@clayui/label';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import React, {useState} from 'react';

import useSelector from '../../../store/hooks/useSelector.es';
import {
	EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
	FRAGMENTS_EDITOR_ITEM_TYPES
} from '../../../utils/constants';

const getEditableValues = (itemId, fragmentEntryLinks) => {
	const [fragmentEntryLinkId, ...editableNameSplit] = itemId.split('-');

	const editableName = editableNameSplit.join('-');

	const fragmentEntryLink = fragmentEntryLinks[fragmentEntryLinkId];

	if (fragmentEntryLink) {
		return (
			fragmentEntryLink.editableValues[EDITABLE_FRAGMENT_ENTRY_PROCESSOR][
				editableName
			] || {}
		);
	}

	return {};
};

const PageContent = props => {
	const [active, setActive] = useState(false);
	const {classNameId, classPK} = props;
	const {editURL, permissionsURL, viewUsagesURL} = props.actions;
	const itemId = `${classNameId}-${classPK}`;

	const isPageContentHovered = useSelector(state => {
		const {fragmentEntryLinks, hoveredItemId, hoveredItemType} = state;

		if (hoveredItemType === FRAGMENTS_EDITOR_ITEM_TYPES.editable) {
			const editableValues = getEditableValues(
				hoveredItemId,
				fragmentEntryLinks
			);

			return (
				editableValues.classNameId === classNameId &&
				editableValues.classPK === classPK
			);
		} else if (hoveredItemType === FRAGMENTS_EDITOR_ITEM_TYPES.mappedItem) {
			return itemId === state.hoveredItemId;
		}
	});

	const openWindow = (uri, title) => {
		Liferay.Util.openWindow({
			dialog: {
				destroyOnHide: true,
				modal: true
			},
			dialogIframe: {
				bodyCssClass: 'dialog-with-footer'
			},
			title,
			uri
		});
	};

	const className = classNames({
		'fragments-editor__page-content': true,
		'fragments-editor__page-content--mapped-item-hovered': isPageContentHovered
	});

	return (
		<li
			className={className}
			data-fragments-editor-item-id={itemId}
			data-fragments-editor-item-type={
				FRAGMENTS_EDITOR_ITEM_TYPES.mappedItem
			}
		>
			<div className="d-flex pl-3 pr-2 py-3">
				<div className="autofit-col autofit-col-expand">
					<strong className="list-group-title truncate-text">
						{props.title}
					</strong>

					<span className="small text-secondary">{props.name}</span>

					<span className="small text-secondary">
						{props.usagesCount === 1
							? Liferay.Language.get('used-in-1-page')
							: Liferay.Util.sub(
									Liferay.Language.get('used-in-x-pages'),
									props.usagesCount
							  )}
					</span>

					<div>
						{props.status.hasApprovedVersion && (
							<ClayLabel displayType="success">
								{Liferay.Language.get('approved')}
							</ClayLabel>
						)}
						<ClayLabel displayType={props.status.style}>
							{props.status.label}
						</ClayLabel>
					</div>
				</div>

				<ClayDropDown
					active={active}
					onActiveChange={setActive}
					trigger={
						<ClayButton
							className="btn-monospaced btn-sm text-secondary"
							displayType="unstyled"
						>
							<ClayIcon symbol="ellipsis-v" />
						</ClayButton>
					}
				>
					<ClayDropDown.ItemList>
						{editURL && (
							<ClayDropDown.Item href={editURL} key="editURL">
								{Liferay.Language.get('edit')}
							</ClayDropDown.Item>
						)}

						{permissionsURL && (
							<ClayDropDown.Item
								key="permissionsURL"
								onClick={() =>
									openWindow(
										permissionsURL,
										Liferay.Language.get('permissions')
									)
								}
							>
								{Liferay.Language.get('permissions')}
							</ClayDropDown.Item>
						)}

						{viewUsagesURL && (
							<ClayDropDown.Item
								key="viewUsagesURL"
								onClick={() =>
									openWindow(
										viewUsagesURL,
										Liferay.Language.get('view-usages')
									)
								}
							>
								{Liferay.Language.get('view-usages')}
							</ClayDropDown.Item>
						)}
					</ClayDropDown.ItemList>
				</ClayDropDown>
			</div>
		</li>
	);
};

PageContent.propTypes = {
	actions: PropTypes.object,
	name: PropTypes.string.isRequired,
	status: PropTypes.shape({
		hasApprovedVersion: PropTypes.bool,
		label: PropTypes.string,
		style: PropTypes.string
	}),
	title: PropTypes.string.isRequired,
	usagesCount: PropTypes.number.isRequired
};

export {PageContent};
export default PageContent;
