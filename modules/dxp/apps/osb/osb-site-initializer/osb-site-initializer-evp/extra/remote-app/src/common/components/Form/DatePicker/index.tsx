/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayDatePicker from '@clayui/date-picker';
import dayjs from 'dayjs';
import {InputHTMLAttributes, useEffect, useState} from 'react';
import {UseFormRegister} from 'react-hook-form';

import {generateReportsType} from '../../../../routes/reports/generateReport';
import BaseWrapper from '../Base/BaseWrapper';

type DatePickerTypes = {
	clearErrors?: any;
	errors?: any;
	id?: string;
	label?: string;
	name: string;
	register?: UseFormRegister<generateReportsType>;
	required?: boolean;
	setValue?: any;
	type?: string;
	value: string;
} & InputHTMLAttributes<HTMLInputElement>;

const DatePicker: React.FC<DatePickerTypes> = ({
	clearErrors = {},
	errors = {},
	label,
	name,
	id = name,
	required = false,
	setValue = () => {},
	value,
}) => {
	const [data, setData] = useState(value);

	useEffect(() => {
		clearErrors(name);
		setValue(name, data);
	}, [clearErrors, data, name, setValue]);

	return (
		<BaseWrapper
			error={errors[name]?.message}
			id={id}
			label={label}
			required={required}
		>
			<ClayDatePicker
				onChange={setData}
				placeholder="YYYY-MM-DD"
				value={data}
				years={{
					end: dayjs().year(),
					start: 1997,
				}}
			/>
		</BaseWrapper>
	);
};

export default DatePicker;
