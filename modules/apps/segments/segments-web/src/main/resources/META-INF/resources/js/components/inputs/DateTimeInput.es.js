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

import {format, isValid, parse, parseISO} from 'date-fns';
import propTypes from 'prop-types';
import React from 'react';

import {PROPERTY_TYPES} from '../../utils/constants.es';

const INPUT_DATE_FORMAT = 'yyyy-MM-dd';

class DateTimeInput extends React.Component {
	static propTypes = {
		disabled: propTypes.bool,
		onChange: propTypes.func.isRequired,
		propertyLabel: propTypes.string.isRequired,
		value: propTypes.string,
	};

	state = {};

	static getDerivedStateFromProps(props, state) {
		let returnVal = null;

		if (props.value !== state.initialValue) {
			returnVal = {
				initialValue: props.value,
				value: format(new Date(props.value), INPUT_DATE_FORMAT),
			};
		}

		return returnVal;
	}

	_handleDateChange = (event) => {
		const value = event.target.value;

		this.setState({value});
	};

	_handleDateBlur = (event) => {
		const dateObj = parseISO(event.target.value);

		if (isValid(dateObj)) {
			const date = format(dateObj, INPUT_DATE_FORMAT);

			this.setState(
				{
					value: date,
				},
				() => {
					this.props.onChange({
						type: PROPERTY_TYPES.DATE_TIME,
						value: parse(
							date,
							INPUT_DATE_FORMAT,
							new Date()
						).toISOString(),
					});
				}
			);
		}
		else {
			const resetDate = format(new Date(), INPUT_DATE_FORMAT);

			this.setState(
				{
					value: resetDate,
				},
				() => {
					this.props.onChange({
						type: PROPERTY_TYPES.DATE_TIME,
						value: parse(
							resetDate,
							INPUT_DATE_FORMAT,
							new Date()
						).toISOString(),
					});
				}
			);
		}
	};

	render() {
		const {value} = this.state;
		const {disabled, propertyLabel} = this.props;

		return (
			<div className="criterion-input date-input">
				<input
					aria-label={`${propertyLabel}: ${Liferay.Language.get(
						'select-date'
					)}`}
					className="form-control"
					data-testid="date-input"
					disabled={disabled}
					onBlur={this._handleDateBlur}
					onChange={this._handleDateChange}
					type="date"
					value={value}
				/>
			</div>
		);
	}
}

export default DateTimeInput;
