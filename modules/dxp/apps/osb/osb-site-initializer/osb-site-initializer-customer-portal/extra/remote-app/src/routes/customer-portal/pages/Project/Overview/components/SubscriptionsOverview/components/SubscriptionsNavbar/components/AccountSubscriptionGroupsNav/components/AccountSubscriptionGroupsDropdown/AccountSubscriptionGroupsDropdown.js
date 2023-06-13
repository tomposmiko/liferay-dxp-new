/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import {Button} from '@clayui/core';
import DropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import {useState} from 'react';
import Skeleton from '../../../../../../../../../../../../../common/components/Skeleton';
import PopoverIcon from '../../../../../../../../../../../components/ActivationStatus/DXPCloud/components/PopoverIcon';
import {PRODUCT_TYPES} from '../../../../../../../../../../../utils/constants';
const AccountSubscriptionGroupsDropdown = ({
	accountSubscriptionGroups,
	disabled,
	loading,
	onSelect,
	selectedIndex,
}) => {
	const [active, setActive] = useState(false);
	const getDropdownItems = () =>
		accountSubscriptionGroups?.map((accountSubscriptionGroup, index) => (
			<DropDown.Item
				className="pr-6"
				disabled={index === selectedIndex || disabled}
				key={`${index}-${index}`}
				onClick={() => onSelect(index)}
				symbolRight={index === selectedIndex && 'check'}
			>
				{accountSubscriptionGroup.name === PRODUCT_TYPES.dxpCloud ? (
					<>
						{accountSubscriptionGroup.name}
						<PopoverIcon />
					</>
				) : (
					accountSubscriptionGroup.name
				)}
			</DropDown.Item>
		));

	return (
		<DropDown
			active={active}
			closeOnClickOutside
			menuWidth="shrink"
			onActiveChange={setActive}
			trigger={
				<Button
					borderless
					className="align-items-center d-flex px-2"
					data-testid="subscriptionDropDown"
					disabled={disabled}
					small
				>
					{loading ? (
						<Skeleton height={16} width={80} />
					) : accountSubscriptionGroups[selectedIndex]?.name ===
					  PRODUCT_TYPES.dxpCloud ? (
						<>
							{accountSubscriptionGroups[selectedIndex]?.name}
							<PopoverIcon />
						</>
					) : (
						accountSubscriptionGroups[selectedIndex]?.name
					)}

					<span className="inline-item inline-item-after">
						<ClayIcon symbol="caret-bottom" />
					</span>
				</Button>
			}
		>
			{getDropdownItems()}
		</DropDown>
	);
};
export default AccountSubscriptionGroupsDropdown;
