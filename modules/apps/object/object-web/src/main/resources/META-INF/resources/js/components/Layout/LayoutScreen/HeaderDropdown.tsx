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

import {ClayButtonWithIcon} from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import React, {FC, MouseEventHandler, useContext, useState} from 'react';

import LayoutContext from '../context';

const HeaderDropdown: FC<IHeaderDropdown> = ({
	addCategorization,
	deleteElement,
}) => {
	const [active, setActive] = useState<boolean>(false);
	const [
		{
			isViewOnly,
			objectLayout: {objectLayoutTabs},
		},
	] = useContext(LayoutContext);

	const handleOnClick = (handler: Function) => {
		handler();
		setActive(false);
	};
	const isThereFramework = (framework: string): boolean => {
		for (const tab of objectLayoutTabs) {
			if (tab.objectLayoutBoxes.some((box) => box.type === framework)) {
				return true;
			}
		}

		return false;
	};

	return (
		<ClayDropDown
			active={active}
			onActiveChange={setActive}
			trigger={
				<ClayButtonWithIcon
					displayType="unstyled"
					symbol="ellipsis-v"
				/>
			}
		>
			<ClayDropDown.ItemList>
				{addCategorization && (
					<ClayDropDown.Item
						disabled={isThereFramework('categorization')}
						onClick={() => handleOnClick(addCategorization)}
					>
						{Liferay.Language.get('add-categorization')}
					</ClayDropDown.Item>
				)}

				<ClayDropDown.Item
					disabled={isViewOnly}
					onClick={() => handleOnClick(deleteElement)}
				>
					{Liferay.Language.get('delete')}
				</ClayDropDown.Item>
			</ClayDropDown.ItemList>
		</ClayDropDown>
	);
};

interface IHeaderDropdown {
	addCategorization?: MouseEventHandler;
	deleteElement: MouseEventHandler;
}

export default HeaderDropdown;
