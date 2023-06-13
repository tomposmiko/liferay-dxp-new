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

/// <reference types="react" />

import {DateValue} from '../../../types/Date';
interface Props {
	disabled?: boolean;
	onChange: (payload: {type: string; value: DateValue}) => void;
	propertyLabel: string;
	propertyType: string;
	range?: boolean;
	value?: DateValue;
}
declare function DateTimeInput({
	disabled,
	onChange,
	propertyLabel,
	propertyType,
	range,
	value,
}: Props): JSX.Element;
export default DateTimeInput;
