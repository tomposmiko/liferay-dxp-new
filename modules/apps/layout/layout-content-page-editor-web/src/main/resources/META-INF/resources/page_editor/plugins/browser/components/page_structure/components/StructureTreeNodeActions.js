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
import {FocusScope} from '@clayui/shared';
import classNames from 'classnames';
import {openToast} from 'frontend-js-web';
import React, {useCallback, useMemo, useRef, useState} from 'react';
import {flushSync} from 'react-dom';

import SaveFragmentCompositionModal from '../../../../../app/components/SaveFragmentCompositionModal';
import hasDropZoneChild from '../../../../../app/components/layout_data_items/hasDropZoneChild';
import {FRAGMENT_ENTRY_TYPES} from '../../../../../app/config/constants/fragmentEntryTypes';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../../../app/config/constants/layoutDataItemTypes';
import {useSelectItem} from '../../../../../app/contexts/ControlsContext';
import {
	useDispatch,
	useSelector,
} from '../../../../../app/contexts/StoreContext';
import deleteItem from '../../../../../app/thunks/deleteItem';
import duplicateItem from '../../../../../app/thunks/duplicateItem';
import canBeDuplicated from '../../../../../app/utils/canBeDuplicated';
import canBeRemoved from '../../../../../app/utils/canBeRemoved';
import canBeRenamed from '../../../../../app/utils/canBeRenamed';
import canBeSaved from '../../../../../app/utils/canBeSaved';
import {
	FORM_ERROR_TYPES,
	getFormErrorDescription,
} from '../../../../../app/utils/getFormErrorDescription';
import updateItemStyle from '../../../../../app/utils/updateItemStyle';
import useHasRequiredChild from '../../../../../app/utils/useHasRequiredChild';

export default function StructureTreeNodeActions({
	item,
	setEditingNodeId,
	visible,
}) {
	const [active, setActive] = useState(false);

	const [openSaveModal, setOpenSaveModal] = useState(false);

	const alignElementRef = useRef();
	const dropdownRef = useRef();

	const updateActive = useCallback((nextActive) => {
		flushSync(() => {
			setActive(nextActive);
		});

		if (nextActive) {
			dropdownRef.current?.querySelector('button')?.focus();
		}
		else {
			alignElementRef.current?.focus();
		}
	}, []);

	return (
		<>
			<ClayButton
				aria-expanded={active}
				aria-haspopup="true"
				aria-label={Liferay.Language.get('options')}
				className={classNames(
					'ml-0 page-editor__page-structure__tree-node__actions-button',
					{
						'page-editor__page-structure__tree-node__actions-button--visible': visible,
					}
				)}
				displayType="unstyled"
				onClick={(event) => {
					event.stopPropagation();
					updateActive(!active);
				}}
				ref={alignElementRef}
				size="sm"
				title={Liferay.Language.get('options')}
			>
				<ClayIcon symbol="ellipsis-v" />
			</ClayButton>

			<ClayDropDown.Menu
				active={active}
				alignElementRef={alignElementRef}
				containerProps={{
					className: 'cadmin',
				}}
				onActiveChange={updateActive}
				ref={dropdownRef}
			>
				{active && (
					<ActionList
						item={item}
						setActive={updateActive}
						setEditingNodeId={setEditingNodeId}
						setOpenSaveModal={setOpenSaveModal}
					/>
				)}
			</ClayDropDown.Menu>

			{openSaveModal && (
				<SaveFragmentCompositionModal
					itemId={item.id}
					onCloseModal={() => setOpenSaveModal(false)}
				/>
			)}
		</>
	);
}

const ActionList = ({item, setActive, setEditingNodeId, setOpenSaveModal}) => {
	const dispatch = useDispatch();
	const hasRequiredChild = useHasRequiredChild(item.id);
	const selectItem = useSelectItem();
	const widgets = useSelector((state) => state.widgets);

	const {fragmentEntryLinks, layoutData, selectedViewportSize} = useSelector(
		(state) => state
	);

	const isInputFragment =
		item.type === LAYOUT_DATA_ITEM_TYPES.fragment &&
		fragmentEntryLinks[item.config.fragmentEntryLinkId]
			.fragmentEntryType === FRAGMENT_ENTRY_TYPES.input;

	const isHidden = item.config.styles.display === 'none';

	const dropdownItems = useMemo(() => {
		const items = [];

		if (
			item.type !== LAYOUT_DATA_ITEM_TYPES.dropZone &&
			!hasDropZoneChild(item, layoutData) &&
			!isInputFragment
		) {
			items.push({
				action: () => {
					updateItemStyle({
						dispatch,
						itemId: item.id,
						selectedViewportSize,
						styleName: 'display',
						styleValue: isHidden ? 'block' : 'none',
					});

					if (hasRequiredChild()) {
						const {message} = getFormErrorDescription({
							type: FORM_ERROR_TYPES.hiddenFragment,
						});

						openToast({
							message,
							type: 'warning',
						});
					}
				},
				icon: isHidden ? 'view' : 'hidden',
				label: isHidden
					? Liferay.Language.get('show-fragment')
					: Liferay.Language.get('hide-fragment'),
			});
		}

		if (canBeSaved(item, layoutData)) {
			items.push({
				action: () => setOpenSaveModal(true),
				icon: 'disk',
				label: Liferay.Language.get('save-composition'),
			});
		}

		if (items.length) {
			items.push({
				type: 'separator',
			});
		}

		if (canBeDuplicated(fragmentEntryLinks, item, layoutData, widgets)) {
			items.push({
				action: () =>
					dispatch(
						duplicateItem({
							itemId: item.id,
							selectItem,
						})
					),
				icon: 'copy',
				label: Liferay.Language.get('duplicate'),
			});
		}

		if (canBeRenamed(item)) {
			items.push({
				action: () => {
					setEditingNodeId(item.id);
				},
				label: Liferay.Language.get('rename'),
			});
		}

		items.push({
			type: 'separator',
		});

		if (canBeRemoved(item, layoutData)) {
			items.push({
				action: () =>
					dispatch(
						deleteItem({
							itemId: item.id,
							selectItem,
						})
					),
				icon: 'trash',
				label: Liferay.Language.get('delete'),
			});
		}

		return items;
	}, [
		dispatch,
		fragmentEntryLinks,
		hasRequiredChild,
		isInputFragment,
		item,
		layoutData,
		selectedViewportSize,
		selectItem,
		widgets,
		setOpenSaveModal,
		isHidden,
		setEditingNodeId,
	]);

	return (
		<FocusScope>
			<div>
				<ClayDropDown.ItemList>
					{dropdownItems.map((dropdownItem, index, array) =>
						dropdownItem.type === 'separator' ? (
							index !== array.length - 1 && (
								<ClayDropDown.Divider key={index} />
							)
						) : (
							<React.Fragment key={index}>
								<ClayDropDown.Item
									aria-label={Liferay.Language.get(
										dropdownItem.label
									)}
									onClick={() => {
										setActive(false);

										dropdownItem.action();
									}}
									symbolLeft={dropdownItem.icon}
								>
									<p className="d-inline-block m-0 ml-4">
										{dropdownItem.label}
									</p>
								</ClayDropDown.Item>
							</React.Fragment>
						)
					)}
				</ClayDropDown.ItemList>
			</div>
		</FocusScope>
	);
};
