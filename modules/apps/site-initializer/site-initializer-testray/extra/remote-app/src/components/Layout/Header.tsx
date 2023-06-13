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

import ClayTabs from '@clayui/tabs';
import classNames from 'classnames';
import {useContext} from 'react';
import {useNavigate} from 'react-router-dom';

import {HeaderContext} from '../../context/HeaderContext';
import DropDown from '../DropDown';

const Divider = () => <p className="mx-2 text-paragraph-lg">/</p>;

const Header = () => {
	const [{heading, tabs}] = useContext(HeaderContext);
	const navigate = useNavigate();

	return (
		<div className="d-flex flex-column header-container pt-4">
			<div className="d-flex">
				<div className="align-items-center d-flex justify-content-center mx-3">
					<DropDown data={[]}></DropDown>
				</div>

				<div className="d-flex flex-column">
					<div className="d-flex flex-wrap">
						{heading.map((header, index) => {
							const isClickable =
								header.path && index !== heading.length - 1;

							return (
								<span
									className={classNames(
										'd-flex flex-column header-item',
										{
											'cursor-pointer': isClickable,
										}
									)}
									key={index}
									onClick={() => {
										if (isClickable && header.path) {
											navigate(header.path);
										}
									}}
								>
									<small className="text-paragraph-xs text-secondary">
										{header.category}
									</small>

									<div className="d-flex flex-row">
										<p className="header-title text-paragraph-lg">
											{header.title}
										</p>

										{!!heading.length &&
											heading.length !== index + 1 && (
												<Divider />
											)}
									</div>
								</span>
							);
						})}
					</div>
				</div>
			</div>

			<ClayTabs className="header-container-tabs ml-3" modern>
				{tabs.map((tab, index) => (
					<ClayTabs.Item
						active={tab.active}
						innerProps={{
							'aria-controls': `tabpanel-${index}`,
						}}
						key={index}
						onClick={() => navigate(tab.path)}
					>
						{tab.title}
					</ClayTabs.Item>
				))}
			</ClayTabs>
		</div>
	);
};

export default Header;
