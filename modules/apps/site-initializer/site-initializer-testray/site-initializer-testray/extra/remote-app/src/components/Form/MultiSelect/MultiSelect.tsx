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

import Form from '..';
import {InputHTMLAttributes} from 'react';
import ReactSelect, {PropsValue} from 'react-select';

type Option = {label: string; value: string};

type MultiSelectProps = {
	label?: string;
	options: Option[];
} & InputHTMLAttributes<HTMLSelectElement>;

const MultiSelect: React.FC<MultiSelectProps> = ({
	disabled,
	label,
	name = '',
	onChange,
	options,
	value,
}) => (
	<Form.BaseWrapper label={label}>
		<ReactSelect
			classNamePrefix="testray-multi-select"
			closeMenuOnSelect={false}
			isDisabled={disabled}
			isMulti
			name={name}
			onChange={(value) => {
				if (onChange) {
					onChange({target: {name, value}} as any);
				}
			}}
			options={options}
			value={value as PropsValue<unknown>}
		/>
	</Form.BaseWrapper>
);

export default MultiSelect;
