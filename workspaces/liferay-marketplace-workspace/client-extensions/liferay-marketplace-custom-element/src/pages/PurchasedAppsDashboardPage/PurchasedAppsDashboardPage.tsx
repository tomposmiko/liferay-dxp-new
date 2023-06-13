import {ClayPaginationBarWithBasicItems} from '@clayui/pagination-bar';
import {useEffect, useState} from 'react';

import accountLogo from '../../assets/icons/mainAppLogo.svg';
import {DashboardNavigation} from '../../components/DashboardNavigation/DashboardNavigation';
import {
	AppProps,
	DashboardTable,
} from '../../components/DashboardTable/DashboardTable';
import {PurchasedAppsDashboardTableRow} from '../../components/DashboardTable/PurchasedAppsDashboardTableRow';
import {getCompanyId} from '../../liferay/constants';
import {
	getAccounts,
	getChannels,
	getOrders,
	getSKUCustomFieldExpandoValue,
} from '../../utils/api';
import {DashboardPage} from '../DashBoardPage/DashboardPage';

import './PurchasedAppsDashboardPage.scss';
import {
	initialAccountState,
	initialDashboardNavigationItems,
} from './PurchasedDashboardPageUtil';

export interface PurchasedAppProps {
	image: string;
	name: string;
	orderId: number;
	project?: string;
	provisioning: string;
	purchasedBy: string;
	purchasedDate: string;
	type: string;
	version: string;
}

interface PurchasedAppTable {
	items: PurchasedAppProps[];
	pageSize: number;
	totalCount: number;
}

const tableHeaders = [
	{
		title: 'Name',
	},
	{
		title: 'Purchased By',
	},
	{
		title: 'Type',
	},
	{
		title: 'Order ID',
	},
	{
		title: 'Provisioning',
	},
	{
		title: 'Installation',
	},
];

export function PurchasedAppsDashboardPage() {
	const [accounts, setAccounts] = useState<Account[]>(initialAccountState);
	const [selectedAccount, setSelectedAccount] = useState<Account>(
		accounts[0]
	);
	const [purchasedAppTable, setPurchasedAppTable] =
		useState<PurchasedAppTable>({items: [], pageSize: 7, totalCount: 1});
	const [page, setPage] = useState<number>(1);
	const [dashboardNavigationItems, setDashboardNavigationItems] = useState(
		initialDashboardNavigationItems
	);
	const [selectedApp, setSelectedApp] = useState<AppProps>();

	const messages = {
		description: 'Manage apps purchase from the Marketplace',
		emptyStateMessage: {
			description1:
				'Purchase and install new apps and they will show up here.',
			description2: 'Click on “Add Apps” to start.',
			title: 'No apps yet',
		},
		title: 'My Apps',
	};

	useEffect(() => {
		(async () => {
			const accountsResponse = await getAccounts();

			const accountsList = accountsResponse.items.map(
				(account: Account) => {
					return {
						externalReferenceCode: account.externalReferenceCode,
						id: account.id,
						name: account.name,
					} as Account;
				}
			);

			setAccounts(accountsList);
			setSelectedAccount(accountsList[0]);
		})();
	}, []);

	useEffect(() => {
		const makeFetch = async () => {
			const channels = await getChannels();

			const channel =
				channels.find(
					(channel) => channel.name === 'Marketplace Channel'
				) || channels[0];

			const placedOrders = await getOrders(
				selectedAccount?.id || 50307,
				channel.id,
				page,
				purchasedAppTable.pageSize
			);

			const newOrderItems = await Promise.all(
				placedOrders.items.map(async (order) => {
					const [placeOrderItem] = order.placedOrderItems;

					const date = new Date(order.createDate);
					const options: Intl.DateTimeFormatOptions = {
						day: 'numeric',
						month: 'short',
						year: 'numeric',
					};
					const formattedDate = date.toLocaleDateString(
						'en-US',
						options
					);

					const version = await getSKUCustomFieldExpandoValue({
						companyId: Number(getCompanyId()),
						customFieldName: 'version',
						skuId: placeOrderItem.skuId,
					});

					return {
						image: placeOrderItem.thumbnail,
						name: placeOrderItem.name,
						orderId: order.id,
						provisioning: order.orderStatusInfo.label_i18n,
						purchasedBy: order.author,
						purchasedDate: formattedDate,
						type: placeOrderItem.subscription
							? 'Subscription'
							: 'Perpetual',
						version: version ?? '',
					};
				})
			);

			setPurchasedAppTable((previousPurchasedAppTable) => {
				return {
					...previousPurchasedAppTable,
					items: newOrderItems,
					totalCount: placedOrders.totalCount,
				};
			});

			const accountsResponse = await getAccounts();

			const accountsList = accountsResponse.items.map(
				(account: Account) => {
					return {
						externalReferenceCode: account.externalReferenceCode,
						id: account.id,
						name: account.name,
					};
				}
			);

			setAccounts(accountsList);
		};
		makeFetch();
	}, [page, purchasedAppTable.pageSize, selectedAccount]);

	return (
		<div className="purchased-apps-dashboard-page-container">
			<DashboardNavigation
				accountAppsNumber="0"
				accountIcon={accountLogo}
				accounts={accounts}
				currentAccount={selectedAccount}
				dashboardNavigationItems={dashboardNavigationItems}
				onSelectAppChange={setSelectedApp}
				selectedApp={selectedApp}
				setDashboardNavigationItems={setDashboardNavigationItems}
				setSelectedAccount={setSelectedAccount}
			/>

			<DashboardPage
				buttonMessage="Add Apps"
				dashboardNavigationItems={dashboardNavigationItems}
				messages={messages}
			>
				<DashboardTable<PurchasedAppProps>
					emptyStateMessage={messages.emptyStateMessage}
					items={purchasedAppTable.items}
					tableHeaders={tableHeaders}
				>
					{(item) => (
						<PurchasedAppsDashboardTableRow
							item={item}
							key={item.name}
						/>
					)}
				</DashboardTable>

				{purchasedAppTable.items.length ? (
					<ClayPaginationBarWithBasicItems
						active={page}
						activeDelta={purchasedAppTable.pageSize}
						defaultActive={1}
						ellipsisBuffer={3}
						ellipsisProps={{'aria-label': 'More', 'title': 'More'}}
						onActiveChange={setPage}
						showDeltasDropDown={false}
						totalItems={purchasedAppTable?.totalCount}
					/>
				) : (
					<></>
				)}
			</DashboardPage>
		</div>
	);
}
