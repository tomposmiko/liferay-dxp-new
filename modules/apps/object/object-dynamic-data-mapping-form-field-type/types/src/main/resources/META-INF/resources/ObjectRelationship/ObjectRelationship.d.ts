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

import React from 'react';
export default function ObjectRelationship({
	apiURL,
	fieldName,
	inputName,
	labelKey,
	name,
	objectEntryId,
	onBlur,
	onChange,
	onFocus,
	parameterObjectFieldName,
	placeholder,
	readOnly,
	required,
	value,
	valueKey,
	...otherProps
}: IProps): JSX.Element;
interface IProps {
	apiURL: string;
	fieldName: string;
	inputName: string;
	labelKey?: string;
	name: string;
	objectEntryId: string;
	onBlur?: React.FocusEventHandler<HTMLInputElement>;
	onChange: (event: {
		target: {
			value: unknown;
		};
	}) => void;
	onFocus?: React.FocusEventHandler<HTMLInputElement>;
	parameterObjectFieldName?: string;
	placeholder?: string;
	readOnly?: boolean;
	required?: boolean;
	value?: string;
	valueKey?: string;
}
export {};
