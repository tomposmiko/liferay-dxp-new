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

import {Align} from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import classNames from 'classnames';
import {useContext} from 'react';

import {AccountContext} from '../../context/AccountContext';
import i18n from '../../i18n';
import {ForwardIcon} from '../../images';
import {Liferay} from '../../services/liferay';
import Avatar from '../Avatar';
import DropDown from '../DropDown';
import Tooltip from '../Tooltip';
import useSidebarActions from './useSidebarActions';

type SidebarProps = {
	expanded: boolean;
	onClick: () => void;
};

const SidebarFooter: React.FC<SidebarProps> = ({expanded, onClick}) => {
	const [{myUserAccount}] = useContext(AccountContext);
	const MANAGE_DROPDOWN = useSidebarActions();

	const loggedUserName = myUserAccount
		? `${myUserAccount?.givenName} ${myUserAccount?.familyName}`
		: Liferay.ThemeDisplay.getUserName();

	return (
		<div className="cursor-pointer testray-sidebar-footer">
			<div className="d-flex justify-content-end">
				<Tooltip
					position="right"
					title={expanded ? undefined : i18n.translate('expand')}
				>
					<div onClick={onClick}>
						<ForwardIcon
							className={classNames('forward-icon ', {
								'forward-icon-expanded': expanded,
							})}
						/>
					</div>
				</Tooltip>
			</div>

			<DropDown
				items={MANAGE_DROPDOWN}
				position={Align.RightBottom}
				trigger={
					<div>
						<Tooltip
							position="right"
							title={
								expanded ? undefined : i18n.translate('manage')
							}
						>
							<div className="testray-sidebar-item">
								<ClayIcon fontSize={16} symbol="cog" />

								<span
									className={classNames(
										'ml-1 testray-sidebar-text',
										{
											'testray-sidebar-text-expanded': expanded,
										}
									)}
								>
									{i18n.translate('manage')}
								</span>
							</div>
						</Tooltip>
					</div>
				}
			/>

			<div className="divider divider-full" />

			<DropDown
				items={[
					{
						items: [
							{
								icon: 'user',
								label: i18n.translate('manage-account'),
								path: '/manage/user/me',
							},
							{
								icon: 'logout',
								label: i18n.translate('sign-out'),
								path: `${window.location.origin}/c/portal/logout`,
							},
						],
						title: '',
					},
				]}
				position={Align.RightBottom}
				trigger={
					<div>
						<Tooltip
							position="right"
							title={expanded ? undefined : loggedUserName}
						>
							<div className="testray-avatar-dropdown testray-sidebar-item">
								<Avatar
									displayName
									expanded={expanded}
									name={loggedUserName}
									url={myUserAccount?.image}
								/>
							</div>
						</Tooltip>
					</div>
				}
			/>
		</div>
	);
};

export default SidebarFooter;
