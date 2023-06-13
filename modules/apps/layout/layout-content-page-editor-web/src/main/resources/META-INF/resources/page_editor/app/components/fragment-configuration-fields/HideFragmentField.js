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
import ClayForm, {ClayCheckbox} from '@clayui/form';
import {openToast} from 'frontend-js-web';
import PropTypes from 'prop-types';
import React from 'react';

import useControlledState from '../../../core/hooks/useControlledState';
import {ConfigurationFieldPropTypes} from '../../../prop-types/index';
import {VIEWPORT_SIZES} from '../../config/constants/viewportSizes';
import {useSelectItem} from '../../contexts/ControlsContext';
import {useSelector} from '../../contexts/StoreContext';
import {
	FORM_ERROR_TYPES,
	getFormErrorDescription,
} from '../../utils/getFormErrorDescription';
import {getResponsiveConfig} from '../../utils/getResponsiveConfig';
import useHasRequiredChild from '../../utils/useHasRequiredChild';
import hasDropZoneChild from '../layout-data-items/hasDropZoneChild';

function getHiddenAncestorId(layoutData, item, selectedViewportSize) {
	const parent = layoutData.items[item.parentId];

	if (!parent) {
		return null;
	}

	const responsiveConfig = getResponsiveConfig(
		parent.config,
		selectedViewportSize
	);

	return responsiveConfig.styles.display === 'none'
		? parent.itemId
		: getHiddenAncestorId(layoutData, parent);
}

export function HideFragmentField({
	disabled,
	field,
	item,
	onValueSelect,
	title,
	value,
}) {
	const layoutData = useSelector((state) => state.layoutData);
	const selectedViewportSize = useSelector(
		(state) => state.selectedViewportSize
	);
	const hasRequiredChild = useHasRequiredChild(item.itemId);
	const selectItem = useSelectItem();

	const [nextValue, setNextValue] = useControlledState(value || false);

	const customValues = field.typeOptions?.customValues;

	const hiddenAncestorId = getHiddenAncestorId(
		layoutData,
		item,
		selectedViewportSize
	);

	return (
		<>
			<ClayForm.Group className="mb-0 mt-1">
				<div
					className="align-items-center d-flex justify-content-between page-editor__sidebar__fieldset__field-checkbox"
					data-tooltip-align="bottom"
					title={title}
				>
					<ClayCheckbox
						aria-label={field.label}
						checked={
							customValues
								? nextValue === customValues.checked
								: nextValue
						}
						containerProps={{className: 'mb-0'}}
						disabled={
							Boolean(hiddenAncestorId) ||
							hasDropZoneChild(item, layoutData) ||
							disabled
						}
						label={field.label}
						onChange={(event) => {
							let eventValue = event.target.checked;

							if (customValues) {
								eventValue = eventValue
									? customValues.checked
									: customValues.unchecked;
							}

							setNextValue(eventValue);
							onValueSelect(field.name, eventValue);

							if (eventValue && hasRequiredChild()) {
								const {message} = getFormErrorDescription({
									type: FORM_ERROR_TYPES.hiddenFragment,
								});

								openToast({
									message,
									type: 'warning',
								});
							}
						}}
					/>

					{field.responsive &&
						selectedViewportSize !== VIEWPORT_SIZES.desktop && (
							<ClayButtonWithIcon
								data-tooltip-align="bottom"
								displayType="secondary"
								onClick={() => {
									onValueSelect(field.name, null);
								}}
								small
								symbol="restore"
								title={Liferay.Language.get('restore-default')}
							/>
						)}
				</div>
			</ClayForm.Group>

			{hiddenAncestorId && (
				<>
					<p className="m-0 small text-secondary">
						{Liferay.Language.get(
							'this-configuration-is-inherited'
						)}
					</p>

					<ClayButton
						className="p-0 page-editor__hide-feedback-button text-left"
						displayType="link"
						onClick={() => selectItem(hiddenAncestorId)}
					>
						{Liferay.Language.get('go-to-parent-fragment-to-edit')}
					</ClayButton>
				</>
			)}
		</>
	);
}

HideFragmentField.propTypes = {
	disabled: PropTypes.bool,
	field: PropTypes.shape(ConfigurationFieldPropTypes).isRequired,
	onValueSelect: PropTypes.func.isRequired,
	value: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
};
