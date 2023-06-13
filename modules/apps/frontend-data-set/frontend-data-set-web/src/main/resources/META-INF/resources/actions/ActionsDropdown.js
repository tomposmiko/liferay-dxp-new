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

import ClayButton, {ClayButtonWithIcon} from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import ClayLink from '@clayui/link';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import {useIsMounted} from '@liferay/frontend-js-react-web';
import PropTypes from 'prop-types';
import React, {useContext} from 'react';

import FrontendDataSetContext from '../FrontendDataSetContext';
import {formatActionURL} from '../utils/index';
import {actionsBasePropTypes, isLink} from './Actions';

function DropdownItem({
	action,
	closeMenu,
	itemData,
	itemId,
	onClick,
	size,
	url,
}) {
	const {icon, label, target} = action;

	return (
		<ClayDropDown.Item
			href={isLink(target, null) ? url : null}
			onClick={(event) =>
				onClick({
					action,
					closeMenu,
					event,
					itemData,
					itemId,
					size,
				})
			}
		>
			{icon && (
				<span className="pr-2">
					<ClayIcon symbol={icon} />
				</span>
			)}

			{label}
		</ClayDropDown.Item>
	);
}

function ActionsDropdown({
	actions,
	handleAction,
	itemData,
	itemId,
	loading,
	menuActive,
	onClick,
	onMenuActiveChange,
	setLoading,
}) {
	const frontendDataSetContext = useContext(FrontendDataSetContext);

	const {
		inlineEditingSettings,
		loadData,
		openSidePanel,
		uniformActionsDisplay,
	} = frontendDataSetContext;

	const inlineEditingAvailable =
		inlineEditingSettings && itemData.actions?.update;
	const inlineEditingAlwaysOn =
		inlineEditingAvailable && inlineEditingSettings.alwaysOn;

	const isMounted = useIsMounted();

	const editModeActive = !!frontendDataSetContext.itemsChanges[itemId];
	const itemChanges =
		editModeActive &&
		Object.keys(frontendDataSetContext.itemsChanges[itemId]).length
			? frontendDataSetContext.itemsChanges[itemId]
			: null;

	const inlineEditingActions = (
		<div className="d-flex">
			<ClayButtonWithIcon
				className="mr-1"
				disabled={inlineEditingAlwaysOn && !itemChanges}
				displayType="secondary"
				onClick={() =>
					frontendDataSetContext.toggleItemInlineEdit(itemId)
				}
				small
				symbol="times-small"
			/>

			{loading ? (
				<ClayLoadingIndicator className="mb-2 mt-2" />
			) : (
				<ClayButtonWithIcon
					disabled={!itemChanges}
					monospaced
					onClick={() => {
						setLoading(true);
						frontendDataSetContext
							.applyItemInlineUpdates(itemId)
							.finally(() => {
								if (isMounted()) {
									setLoading(false);
								}
							});
					}}
					small
					symbol="check"
				/>
			)}
		</div>
	);

	if (!inlineEditingAlwaysOn && editModeActive) {
		return inlineEditingActions;
	}

	if (!actions.length) {
		return null;
	}

	if (
		!inlineEditingAlwaysOn &&
		!uniformActionsDisplay &&
		actions.length === 1
	) {
		const [action] = actions;
		const {data: actionData} = action;

		if (actionData?.id && !action?.href) {
			return null;
		}

		if (loading) {
			return <ClayLoadingIndicator className="mb-2 mt-2" />;
		}

		const content = action.icon ? (
			<ClayIcon symbol={action.icon} />
		) : (
			action.label
		);

		const onActionDropdownItemClick =
			frontendDataSetContext.onActionDropdownItemClick;

		const url = formatActionURL(action.href, itemData);

		const elementWithIconSpecialProps = action.icon
			? {
					ariaLabel: action.label,
					title: action.label,
			  }
			: {};

		return isLink(action.target, action.onClick) ? (
			<ClayLink
				{...elementWithIconSpecialProps}
				className="btn btn-secondary btn-sm"
				href={url}
				monospaced={Boolean(action.icon)}
				onClick={(event) => {
					if (onActionDropdownItemClick) {
						onActionDropdownItemClick({
							action,
							event,
							itemData,
							loadData,
							openSidePanel,
						});
					}
				}}
			>
				{content}
			</ClayLink>
		) : (
			<ClayLink
				{...elementWithIconSpecialProps}
				className="btn btn-secondary btn-sm"
				data-senna-off
				href="#"
				monospaced={Boolean(action.icon)}
				onClick={(event) => {
					if (onActionDropdownItemClick) {
						onActionDropdownItemClick({
							action,
							event,
							itemData,
							loadData,
							openSidePanel,
						});
					}

					handleAction(
						{
							errorMessage: actionData?.errorMessage,
							event,
							itemId,
							method: action.method ?? actionData?.method,
							setLoading,
							successMessage: actionData?.successMessage,
							url,
							...action,
						},
						frontendDataSetContext
					);
				}}
			>
				{content}
			</ClayLink>
		);
	}

	if (loading && !inlineEditingAlwaysOn) {
		return <ClayLoadingIndicator className="mb-2 mt-2" />;
	}

	const renderItems = (items) =>
		items.map(({items: nestedItems = [], separator, type, ...item}, i) => {
			if (type === 'group') {
				return (
					<ClayDropDown.Group {...item}>
						{separator && <ClayDropDown.Divider />}

						{renderItems(nestedItems)}
					</ClayDropDown.Group>
				);
			}

			return (
				<DropdownItem
					action={item}
					closeMenu={() => onMenuActiveChange(false)}
					handleAction={handleAction}
					itemData={itemData}
					itemId={itemId}
					key={i}
					onClick={onClick}
					setLoading={setLoading}
					url={item.href && formatActionURL(item.href, itemData)}
				/>
			);
		});

	return (
		<div className="d-flex justify-content-end">
			{inlineEditingAlwaysOn && inlineEditingActions}

			<ClayDropDown
				active={menuActive}
				onActiveChange={onMenuActiveChange}
				trigger={
					<ClayButton
						className="component-action dropdown-toggle ml-1"
						disabled={loading}
						displayType="unstyled"
					>
						<ClayIcon symbol="ellipsis-v" />

						<span className="sr-only">
							{Liferay.Language.get('actions')}
						</span>
					</ClayButton>
				}
			>
				<ClayDropDown.ItemList>
					{renderItems(actions)}
				</ClayDropDown.ItemList>
			</ClayDropDown>
		</div>
	);
}

ActionsDropdown.propTypes = {
	...actionsBasePropTypes,
	handleAction: PropTypes.func.isRequired,
	loading: PropTypes.bool.isRequired,
	onClick: PropTypes.func.isRequired,
	setLoading: PropTypes.func.isRequired,
};

export default ActionsDropdown;
