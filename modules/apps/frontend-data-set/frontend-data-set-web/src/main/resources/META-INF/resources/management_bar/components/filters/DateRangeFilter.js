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
import ClayForm from '@clayui/form';
import PropTypes from 'prop-types';
import React, {useState} from 'react';

import {
	formatDateObject,
	formatDateRangeObject,
	getDateFromDateString,
} from '../../../utils/dates';

const getIsoString = ({direction, entityFieldType, objectDate}) => {
	const timestamp = Date.UTC(
		objectDate.year,
		objectDate.month - 1,
		objectDate.day
	);

	const date = new Date(timestamp);

	if (direction === 'from') {
		date.setUTCHours(0, 0, 0, 0);
	}
	else {
		date.setUTCHours(23, 59, 59, 999);
	}

	const dateISOString = date.toISOString();

	if (entityFieldType === 'date') {
		return dateISOString.split('T')[0];
	}

	return dateISOString;
};

const getSelectedItemsLabel = ({selectedData}) => {
	return formatDateRangeObject(selectedData);
};

const getOdataString = ({entityFieldType, id, selectedData}) => {
	const {from, to} = selectedData;

	const fromIsoString =
		from &&
		getIsoString({direction: 'from', entityFieldType, objectDate: from});

	const toIsoString =
		to && getIsoString({direction: 'to', entityFieldType, objectDate: to});

	if (from && to) {
		return `${id} ge ${fromIsoString}) and (${id} le ${toIsoString}`;
	}
	if (from) {
		return `${id} ge ${fromIsoString}`;
	}
	if (to) {
		return `${id} le ${toIsoString}`;
	}
};

const DateRangeFilter = ({
	entityFieldType,
	id,
	max,
	min,
	placeholder,
	selectedData,
	setFilter,
}) => {
	const [fromValue, setFromValue] = useState(
		selectedData?.from && formatDateObject(selectedData.from)
	);
	const [toValue, setToValue] = useState(
		selectedData?.to && formatDateObject(selectedData.to)
	);

	let actionType = 'edit';

	if (selectedData && !fromValue && !toValue) {
		actionType = 'delete';
	}

	if (!selectedData) {
		actionType = 'add';
	}

	let submitDisabled = true;

	if (
		actionType === 'delete' ||
		((!selectedData || !selectedData.from) && fromValue) ||
		((!selectedData || !selectedData.to) && toValue) ||
		(selectedData &&
			selectedData.from &&
			fromValue !== formatDateObject(selectedData.from)) ||
		(selectedData &&
			selectedData.to &&
			toValue !== formatDateObject(selectedData.to))
	) {
		submitDisabled = false;
	}

	return (
		<>
			<ClayDropDown.Caption>
				<div className="form-group">
					<ClayForm.Group className="form-group-sm">
						<label htmlFor={`from-${id}`}>
							{Liferay.Language.get('from')}
						</label>

						<input
							className="form-control"
							id={`from-${id}`}
							max={toValue || (max && formatDateObject(max))}
							min={min && formatDateObject(min)}
							onChange={(event) =>
								setFromValue(event.target.value)
							}
							pattern="\d{4}-\d{2}-\d{2}"
							placeholder={placeholder || 'yyyy-mm-dd'}
							type="date"
							value={fromValue || ''}
						/>
					</ClayForm.Group>

					<ClayForm.Group className="form-group-sm mt-2">
						<label htmlFor={`to-${id}`}>
							{Liferay.Language.get('to')}
						</label>

						<input
							className="form-control"
							id={`to-${id}`}
							max={max && formatDateObject(max)}
							min={fromValue || (min && formatDateObject(min))}
							onChange={(event) => setToValue(event.target.value)}
							pattern="\d{4}-\d{2}-\d{2}"
							placeholder={placeholder || 'yyyy-mm-dd'}
							type="date"
							value={toValue || ''}
						/>
					</ClayForm.Group>
				</div>
			</ClayDropDown.Caption>
			<ClayDropDown.Divider />
			<ClayDropDown.Caption>
				<ClayButton
					disabled={submitDisabled}
					onClick={() => {
						if (actionType === 'delete') {
							setFilter({active: false, id});
						}
						else {
							const newSelectedData = {
								from: fromValue
									? getDateFromDateString(fromValue)
									: null,
								to: toValue
									? getDateFromDateString(toValue)
									: null,
							};

							setFilter({
								active: true,
								id,
								odataFilterString: getOdataString({
									entityFieldType,
									id,
									selectedData: newSelectedData,
								}),
								selectedData: newSelectedData,
								selectedItemsLabel: getSelectedItemsLabel({
									selectedData: newSelectedData,
								}),
							});
						}
					}}
					small
				>
					{actionType === 'add' && Liferay.Language.get('add-filter')}

					{actionType === 'edit' &&
						Liferay.Language.get('edit-filter')}

					{actionType === 'delete' &&
						Liferay.Language.get('delete-filter')}
				</ClayButton>
			</ClayDropDown.Caption>
		</>
	);
};

const dateShape = PropTypes.shape({
	day: PropTypes.number,
	month: PropTypes.number,
	year: PropTypes.number,
});

DateRangeFilter.propTypes = {
	id: PropTypes.string.isRequired,
	max: dateShape,
	min: dateShape,
	placeholder: PropTypes.string,
	selectedData: PropTypes.shape({
		from: dateShape,
		to: dateShape,
	}),
};

export {getSelectedItemsLabel, getOdataString};
export default DateRangeFilter;
