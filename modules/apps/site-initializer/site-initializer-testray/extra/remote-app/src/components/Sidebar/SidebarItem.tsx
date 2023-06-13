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

import ClayIcon from '@clayui/icon';
import classNames from 'classnames';
import {Link} from 'react-router-dom';

import i18n from '../../i18n';
import Tooltip from '../Tooltip';

type SidebarItemProps = {
	active?: boolean;
	className?: string;
	expanded?: boolean;
	icon: string;
	label: string;
	path?: string;
};

const SidebarItem: React.FC<SidebarItemProps> = ({
	active,
	className,
	expanded,
	icon,
	label,
	path,
}) => {
	const LinkComponent = path
		? Link
		: ({children, ...props}: any) => <div {...props}>{children}</div>;

	return (
		<Tooltip
			position="right"
			title={expanded ? undefined : i18n.translate(label)}
		>
			<LinkComponent
				className={classNames(
					'cursor-pointer testray-sidebar-item',
					{active},
					className
				)}
				to={path as string}
			>
				<ClayIcon
					color={active ? 'white' : undefined}
					fontSize={20}
					symbol={icon}
				/>

				<span
					className={classNames('ml-1 testray-sidebar-text', {
						'testray-sidebar-text-expanded': expanded,
					})}
				>
					{label}
				</span>
			</LinkComponent>
		</Tooltip>
	);
};

export default SidebarItem;
