import classNames from 'classnames';

import './DashboardNavigationList.scss';
import {AppProps} from '../DashboardTable/DashboardTable';
import {DashboardListItems} from './DashboardNavigation';
import {DashboardNavigationListItem} from './DashboardNavigationListItem';

interface DashboardNavigationListProps {
	dashboardNavigationItems: DashboardListItems[];
	navigationItemMock: DashboardListItems;
	navigationItemsMock: DashboardListItems[];
	onSelectAppChange: (value: AppProps) => void;
	setDashboardNavigationItems: (values: DashboardListItems[]) => void;
}

export function DashboardNavigationList({
	dashboardNavigationItems,
	navigationItemMock,
	navigationItemsMock,
	onSelectAppChange,
	setDashboardNavigationItems,
}: DashboardNavigationListProps) {
	const {itemIcon, itemName, itemSelected, itemTitle, items} =
		navigationItemMock;

	return (
		<>
			<div
				className={classNames('dashboard-navigation-body-list', {
					'dashboard-navigation-body-list-selected': itemSelected,
				})}
				onClick={() => {
					const newItems = navigationItemsMock.map(
						(navigationItem) => {
							if (navigationItem.itemName === itemName) {
								return {
									...navigationItem,
									itemSelected: true,
								};
							}

							return {
								...navigationItem,
								itemSelected: false,
							};
						}
					);

					setDashboardNavigationItems(newItems);
				}}
			>
				<img
					alt="Apps icon"
					className={classNames(
						'dashboard-navigation-body-list-icon',
						{
							'dashboard-navigation-body-list-icon-selected':
								itemSelected,
						}
					)}
					src={itemIcon}
				/>

				<span
					className={classNames(
						'dashboard-navigation-body-list-text',
						{
							'dashboard-navigation-body-list-text-selected':
								itemSelected,
						}
					)}
				>
					{itemTitle}
				</span>
			</div>

			{itemSelected &&
				items?.map((item) => (
					<DashboardNavigationListItem
						dashboardNavigationItems={dashboardNavigationItems}
						item={item}
						items={items}
						key={item.name}
						listName={itemName}
						onSelectAppChange={onSelectAppChange}
						setDashboardNavigationItems={
							setDashboardNavigationItems
						}
					/>
				))}
		</>
	);
}
