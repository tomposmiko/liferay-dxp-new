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

import {ClayTooltipProvider} from '@clayui/tooltip';
import classNames from 'classnames';
import {useRef, useState} from 'react';
import {Link, useLocation} from 'react-router-dom';
import {STORAGE_KEYS} from '~/core/Storage';
import {CONSENT_TYPE} from '~/util/enum';

import useStorage from '../../hooks/useStorage';
import i18n from '../../i18n';
import {TestrayIcon, TestrayIconBrand} from '../../images';
import CompareRunsPopover from '../CompareRunsPopover';
import TestrayIcons from '../Icons/TestrayIcon';
import Tooltip from '../Tooltip';
import SidebarFooter from './SidebarFooter';
import SidebarItem from './SidebarItem';
import TaskSidebar from './TasksSidebar';

const Sidebar = () => {
	const {pathname} = useLocation();
	const [expanded, setExpanded] = useStorage(STORAGE_KEYS.SIDEBAR, {
		consentType: CONSENT_TYPE.PERSONALIZATION,
		initialValue: true,
		storageType: 'persisted',
	});
	const [visible, setVisible] = useState(false);

	const CompareRunsContent = (
		<div className={classNames('cursor-pointer testray-sidebar-item')}>
			<TestrayIcons
				className="testray-icon"
				fill="#8b8db2"
				size={35}
				symbol="drop"
			/>

			<span
				className={classNames('ml-1 testray-sidebar-text', {
					'testray-sidebar-text-expanded': expanded,
				})}
			>
				{i18n.sub('compare-x', 'runs')}
			</span>
		</div>
	);

	const ref = useRef<HTMLDivElement>(null);

	const sidebarItems = [
		{
			icon: 'polls',
			label: i18n.translate('results'),
			path: '/',
		},
		{
			icon: 'merge',
			label: i18n.translate('testflow'),
			path: '/testflow',
		},
		{
			className: 'mt-3',
			element: (
				<div
					className={classNames({
						'testray-sidebar-item-expand': expanded,
						'testray-sidebar-item-normal': !expanded,
					})}
					onClick={() => setVisible((show) => !show)}
					ref={ref}
				>
					<Tooltip
						position="right"
						title={
							expanded
								? undefined
								: i18n.translate('compare-runs')
						}
					>
						{CompareRunsContent}
					</Tooltip>
				</div>
			),
		},
	];

	return (
		<ClayTooltipProvider>
			<div
				className={classNames(
					'testray-sidebar d-flex flex-column justify-content-between',
					{
						'testray-sidebar-expanded': expanded,
					}
				)}
			>
				<div className="testray-sidebar-content">
					<div>
						<Link
							className="d-flex flex-center testray-sidebar-title"
							to="/"
						>
							<TestrayIcon className="testray-logo" />

							<TestrayIconBrand
								className={classNames('testray-brand-logo', {
									'testray-brand-logo-expand': expanded,
								})}
							/>
						</Link>

						{sidebarItems.map(
							(
								{className, element, icon, label, path},
								index
							) => {
								const [, ...items] = sidebarItems;

								if (path) {
									const someItemIsActive = items.some(
										(item) =>
											item.path
												? pathname.includes(item.path)
												: false
									);

									return (
										<SidebarItem
											active={
												index === 0
													? !someItemIsActive
													: pathname.includes(path)
											}
											className={className}
											expanded={expanded}
											icon={icon}
											key={index}
											label={label}
											path={path}
										/>
									);
								}

								return (
									<div className={className} key={index}>
										{element}
									</div>
								);
							}
						)}

						<CompareRunsPopover
							expanded={expanded}
							setVisible={setVisible}
							triggedRef={ref}
							visible={visible}
						/>

						<div className="pb-5 pt-3">
							<div className="divider divider-full" />
						</div>
					</div>

					<TaskSidebar expanded={expanded} />
				</div>

				<div className="pb-1">
					<SidebarFooter
						expanded={expanded}
						onClick={() => setExpanded(!expanded)}
					/>
				</div>
			</div>
		</ClayTooltipProvider>
	);
};

export default Sidebar;
