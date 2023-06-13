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

import './Sidebar.scss';

import {ClayButtonWithIcon} from '@clayui/button';
import ClayLayout from '@clayui/layout';
import classNames from 'classnames';
import React, {useState} from 'react';

import SearchInput from '../search-input/SearchInput.es';

const Sidebar = React.forwardRef(({children, className}, ref) => {
	return (
		<div
			className={classNames(className, 'data-layout-builder-sidebar')}
			ref={ref}
		>
			<div className="sidebar sidebar-light">{children}</div>
		</div>
	);
});

const SidebarBody = ({children, className}) => {
	return (
		<div className={classNames(className, 'sidebar-body')}>{children}</div>
	);
};

const SidebarFooter = ({children}) => {
	return <div className="sidebar-footer">{children}</div>;
};

const SidebarHeader = ({children, className}) => {
	return (
		<div className={classNames(className, 'sidebar-header')}>
			{children}
		</div>
	);
};

const SidebarSearchInput = ({
	children,
	onSearch,
	searchText,
	setSearchClicked,
}) => (
	<ClayLayout.ContentRow className="sidebar-section">
		<ClayLayout.ContentCol expand>
			{onSearch && (
				<SearchInput
					onChange={(searchText) => onSearch(searchText)}
					searchText={searchText}
					setSearchClicked={setSearchClicked}
				/>
			)}
		</ClayLayout.ContentCol>

		{children}
	</ClayLayout.ContentRow>
);

const SidebarTabs = ({
	initialSelectedTab = 0,
	searchTerm,
	setKeywords = () => {},
	tabs,
}) => {
	const [selectedTab, setSelectedTab] = useState(initialSelectedTab);

	return (
		<>
			<SidebarTab
				onTabClick={(value) => {
					setSelectedTab(value);
					setKeywords('');
				}}
				selectedTab={selectedTab}
				tabs={tabs}
			/>

			<SidebarTabContent>
				{tabs[selectedTab].render({searchTerm})}
			</SidebarTabContent>
		</>
	);
};

const SidebarTab = ({onTabClick, selectedTab, tabs}) => {
	return (
		<nav className="component-tbar tbar">
			<ClayLayout.ContainerFluid>
				<ul className="nav nav-underline" role="tablist">
					{tabs.map(({label}, index) => (
						<li className="nav-item" key={index}>
							<button
								className={classNames(
									'btn btn-unstyled nav-link',
									{
										active: selectedTab === index,
									}
								)}
								data-senna-off
								onClick={(event) => {
									event.preventDefault();
									onTabClick(index);
								}}
								role="tab"
							>
								{label}
							</button>
						</li>
					))}
				</ul>
			</ClayLayout.ContainerFluid>
		</nav>
	);
};

const SidebarTabContent = ({children}) => {
	return (
		<div className="tab-content">
			<div className="active fade mt-3 show tab-pane" role="tabpanel">
				{children}
			</div>
		</div>
	);
};

const SidebarTitle = ({className, title}) => (
	<ClayLayout.ContentRow className={classNames('sidebar-section', className)}>
		<ClayLayout.ContentCol expand>
			<div className="component-title">
				<h2 className="text-truncate-inline">{title}</h2>
			</div>
		</ClayLayout.ContentCol>

		<ClayButtonWithIcon
			aria-label={Liferay.Language.get('close-builder-panel')}
			displayType="unstyled"
			onClick={() => {
				const builder = document.querySelector(
					'.ddm-form-builder--sidebar-open'
				);
				const sidebar = document.querySelector(
					'.multi-panel-sidebar-content-open'
				);

				builder.classList.remove('ddm-form-builder--sidebar-open');
				sidebar.classList.remove('multi-panel-sidebar-content-open');
			}}
			size="sm"
			symbol="times"
			tabIndex={0}
			title={Liferay.Language.get('close')}
		/>
	</ClayLayout.ContentRow>
);

const SidebarDescription = ({description}) => (
	<p>
		<span
			aria-label={description}
			className="de__sidebar-content-description"
		>
			{description}
		</span>
	</p>
);

Sidebar.Body = SidebarBody;
Sidebar.Description = SidebarDescription;
Sidebar.Footer = SidebarFooter;
Sidebar.Header = SidebarHeader;
Sidebar.SearchInput = SidebarSearchInput;
Sidebar.Tab = SidebarTab;
Sidebar.Tabs = SidebarTabs;
Sidebar.TabContent = SidebarTabContent;
Sidebar.Title = SidebarTitle;

export {
	SidebarBody,
	SidebarDescription,
	SidebarFooter,
	SidebarHeader,
	SidebarSearchInput,
	SidebarTab,
	SidebarTabs,
	SidebarTabContent,
};

export default Sidebar;
