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
import './Card.scss';
export declare function Card({
	children,
	className,
	title,
	tooltip,
	viewMode,
	...otherProps
}: IProps): JSX.Element;
interface IProps extends React.HTMLAttributes<HTMLDivElement> {
	title: string;
	tooltip?: ITooltip | null;
	viewMode?: 'inline' | 'no-children' | 'no-padding';
}
interface ITooltip {
	content: string;
	symbol: string;
}
export {};
