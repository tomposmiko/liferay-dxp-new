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

import classNames from 'classnames';
import {InputHTMLAttributes} from 'react';

import {BaseWrapper} from '../Base';

type InputSelectProps = {
	className?: string;
	defaultOption?: boolean;
	disabled?: boolean;
	errors?: any;
	forceSelectOption?: boolean;
	id?: string;
	label?: string;
	name: string;
	options: {label: string; value: string | number}[];
	register?: any;
	registerOptions?: any;
	required?: boolean;
	type?: string;
} & InputHTMLAttributes<HTMLInputElement>;

const InputSelect: React.FC<InputSelectProps> = ({
	className,
	disabled = false,
	registerOptions,
	defaultOption = true,
	errors = {},
	defaultValue,
	label,
	name,
	register = () => {},
	id = name,
	options,
	forceSelectOption = false,
	required = false,
	...otherProps
}) => {
	return (
		<BaseWrapper
			disabled={disabled}
			error={errors[name]?.message}
			label={label}
			required={required}
		>
			<select
				className={classNames('form-control rounded-xs', className)}
				defaultValue={defaultValue}
				disabled={disabled}
				id={id}
				name={name}
				{...otherProps}
				{...register(name, {required, ...registerOptions})}
			>
				{defaultOption && <option value=""></option>}

				{options.map(({label, value}, index) => (
					<option
						key={index}
						selected={
							forceSelectOption
								? value === defaultValue
								: undefined
						}
						value={value}
					>
						{label}
					</option>
				))}
			</select>
		</BaseWrapper>
	);
};

export default InputSelect;
