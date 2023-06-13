import {ClayPaginationBarWithBasicItems} from '@clayui/pagination-bar';
import {useEffect, useState} from 'react';

import accountLogo from '../../assets/icons/mainAppLogo.svg';
import {DashboardNavigation} from '../../components/DashboardNavigation/DashboardNavigation';
import {DashboardMemberTableRow} from '../../components/DashboardTable/DashboardMemberTableRow';
import {
	AppProps,
	DashboardTable,
} from '../../components/DashboardTable/DashboardTable';
import {PublishedAppsDashboardTableRow} from '../../components/DashboardTable/PublishedAppsDashboardTableRow';
import {MemberProfile} from '../../components/MemberProfile/MemberProfile';
import {
	getAccounts,
	getMyUserAccount,
	getProductSpecifications,
	getProducts,
	getUserAccounts,
} from '../../utils/api';
import {AccountDetailsPage} from '../AccountDetailsPage/AccountDetailsPage';
import {
	DashboardListItems,
	DashboardPage,
} from '../DashBoardPage/DashboardPage';

import {
	AccountBriefProps,
	MemberProps,
	ProductResponseProps,
	ProductSpecificationProps,
	UserAccountProps,
	initialDashboardNavigationItems,
} from './PublishedDashboardPageUtil';

import './PublishedAppsDashboardPage.scss';

declare let Liferay: {
	ThemeDisplay: {getLanguageId: () => string};
	authToken: string;
};

const appTableHeaders = [
	{
		iconSymbol: 'order-arrow',
		title: 'Name',
	},
	{
		title: 'Version',
	},
	{
		title: 'Type',
	},
	{
		title: 'Last Updated',
	},
	{
		title: 'Status',
	},
];

const memberTableHeaders = [
	{
		iconSymbol: 'order-arrow',
		title: 'Name',
	},
	{
		title: 'Email',
	},
	{
		title: 'Role',
	},
];

const initialAccountsState: Account[] = [
	{
		customFields: {CatalogId: 0},
		externalReferenceCode: '',
		id: 0,
		name: '',
	},
];

interface PublishedAppTable {
	items: AppProps[];
	pageSize: number;
	totalCount: number;
}

export function PublishedAppsDashboardPage() {
	const [accounts, setAccounts] = useState<Account[]>(initialAccountsState);
	const [apps, setApps] = useState<AppProps[]>(Array<AppProps>());
	const [selectedApp, setSelectedApp] = useState<AppProps>();
	const [dashboardNavigationItems, setDashboardNavigationItems] = useState(
		initialDashboardNavigationItems
	);
	const [page, setPage] = useState(1);
	const [publishedAppTable, setPublishedAppTable] =
		useState<PublishedAppTable>({items: [], pageSize: 7, totalCount: 1});
	const [selectedNavigationItem, setSelectedNavigationItem] =
		useState('Apps');
	const [members, setMembers] = useState<MemberProps[]>(Array<MemberProps>());
	const [selectedMember, setSelectedMember] = useState<MemberProps>();
	const [selectedAccount, setSelectedAccount] = useState<Account>({
		customFields: {CatalogId: 0},
		externalReferenceCode: '',
		id: 0,
		name: '',
	});

	const appMessages = {
		description: 'Manage and publish apps on the Marketplace',
		emptyStateMessage: {
			description1: 'Publish apps and they will show up here.',
			description2: 'Click on “New App” to start.',
			title: 'No apps yet',
		},
		title: 'Apps',
	};

	const memberMessages = {
		description:
			'Manage users in your development team and invite new ones',
		emptyStateMessage: {
			description1: 'Create new members and they will show up here.',
			description2: 'Click on “New Member” to start.',
			title: 'No members yet',
		},
		title: 'Members',
	};

	const formatDate = (date: string) => {
		const locale = Liferay.ThemeDisplay.getLanguageId().replace('_', '-');

		const dateOptions: Intl.DateTimeFormatOptions = {
			day: 'numeric',
			month: 'short',
			year: 'numeric',
		};

		const formattedDate = new Intl.DateTimeFormat(
			locale,
			dateOptions
		).format(new Date(date));

		return formattedDate;
	};

	function getAppListProductSpecifications(productIds: number[]) {
		const appListProductSpecifications: Promise<ProductSpecificationProps>[] =
			[];

		productIds.forEach((productId) => {
			appListProductSpecifications.push(
				getProductSpecifications({appProductId: productId})
			);
		});

		return Promise.all(appListProductSpecifications);
	}

	function getAppListProductIds(products: {items: AppProps[]}) {
		const productIds: number[] = [];

		products.items.map((product: AppProps) => {
			productIds.push(product.productId);
		});

		return productIds;
	}

	function getProductTypeFromSpecifications(
		specifications: ProductSpecificationProps
	) {
		let productType = 'no type';

		specifications.items.forEach((specification: Specification) => {
			if (specification.specificationKey === 'type') {
				productType = specification.value.en_US;

				if (productType === 'cloud') {
					productType = 'Cloud';
				}
				else if (productType === 'osgi') {
					productType = 'OSGI';
				}
			}
		});

		return productType;
	}

	function getProductVersionFromSpecifications(
		specifications: ProductSpecificationProps
	) {
		let productVersion = '0';

		specifications.items.forEach((specification: Specification) => {
			if (specification.specificationKey === 'version') {
				productVersion = specification.value.en_US;
			}
		});

		return productVersion;
	}

	function getRolesList(accountBriefs: AccountBrief[]) {
		const rolesList: string[] = [];

		const accountBrief = accountBriefs.find(
			(accountBrief) => accountBrief.name === selectedAccount.name
		);

		accountBrief?.roleBriefs.forEach((role) => {
			rolesList.push(role.name);
		});

		return rolesList.join(', ');
	}

	useEffect(() => {
		(async () => {
			const accountsResponse = await getAccounts();

			const accountsList = accountsResponse.items.map(
				(account: Account) => {
					return {
						customFields: account.customFields,
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
		(async () => {
			const accountCatalogId = selectedAccount.customFields?.CatalogId;

			if (accountCatalogId) {
				if (accountCatalogId !== 0) {
					const appList = await getProducts();

					const appListProductIds: number[] =
						getAppListProductIds(appList);

					const appListProductSpecifications =
						await getAppListProductSpecifications(
							appListProductIds
						);

					const newAppList: AppProps[] = [];

					appList.items.forEach(
						(product: ProductResponseProps, index: number) => {
							if (product.catalogId === accountCatalogId) {
								newAppList.push({
									catalogId: product.catalogId,
									externalReferenceCode:
										product.externalReferenceCode,
									lastUpdatedBy: product.lastUpdatedBy,
									name: product.name.en_US,
									productId: product.productId,
									status: product.workflowStatusInfo.label.replace(
										/(^\w|\s\w)/g,
										(m: string) => m.toUpperCase()
									),
									thumbnail: product.thumbnail,
									type: getProductTypeFromSpecifications(
										appListProductSpecifications[index]
									),
									updatedDate: formatDate(
										product.modifiedDate
									),
									version:
										getProductVersionFromSpecifications(
											appListProductSpecifications[index]
										),
								});
							}
						}
					);

					setApps(newAppList);

					setPublishedAppTable({
						items: newAppList.slice(
							publishedAppTable.pageSize * (page - 1),
							publishedAppTable.pageSize * (page - 1) +
								publishedAppTable.pageSize
						),
						pageSize: publishedAppTable.pageSize,
						totalCount: newAppList.length,
					});
				}
			}
		})();
	}, [page, publishedAppTable.pageSize, selectedAccount]);

	useEffect(() => {
		(() => {
			const currentAppNavigationItem = dashboardNavigationItems.find(
				(navigationItem) => navigationItem.itemName === 'apps'
			) as DashboardListItems;

			const newAppNavigationItem = {
				...currentAppNavigationItem,
				items: apps.slice(0, 4),
			};

			setDashboardNavigationItems([
				newAppNavigationItem,
				...dashboardNavigationItems.filter(
					(navigationItem) => navigationItem.itemName !== 'apps'
				),
			]);
		})();
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [apps]);

	useEffect(() => {
		const clickedNavigationItem =
			dashboardNavigationItems.find(
				(dashboardNavigationItem) =>
					dashboardNavigationItem.itemSelected
			) || dashboardNavigationItems[0];

		setSelectedNavigationItem(clickedNavigationItem?.itemTitle as string);
	}, [dashboardNavigationItems]);

	useEffect(() => {
		(async () => {
			if (selectedNavigationItem === 'Members') {
				const currentUserAccountResponse = await getMyUserAccount();

				const currentUserAccount = {
					accountBriefs: currentUserAccountResponse.accountBriefs,
					isCustomerAccount: false,
					isPublisherAccount: false,
				};

				const currentUserAccountRoleBriefs =
					currentUserAccount.accountBriefs.find(
						(accountBrief: {name: string}) =>
							accountBrief.name === selectedAccount.name
					).roleBriefs;

				const customerRoles = [
					'Account Administrator',
					'Project Installer',
					'Account Buyer',
					'Account Member',
				];

				const publisherRoles = [
					'Owner',
					'Account Administrator',
					'App Editor',
					'Sales Manager',
				];

				customerRoles.forEach((customerRole) => {
					if (
						currentUserAccountRoleBriefs.find(
							(role: {name: string}) => role.name === customerRole
						)
					) {
						currentUserAccount.isCustomerAccount = true;
					}
				});

				publisherRoles.forEach((publisherRole) => {
					if (
						currentUserAccountRoleBriefs.find(
							(role: {name: string}) =>
								role.name === publisherRole
						)
					) {
						currentUserAccount.isPublisherAccount = true;
					}
				});

				const accountsListResponse = await getUserAccounts();

				const membersList = accountsListResponse.items.map(
					(member: UserAccountProps) => {
						return {
							accountBriefs: member.accountBriefs,
							dateCreated: member.dateCreated,
							email: member.emailAddress,
							image: member.image,
							isCustomerAccount: false,
							isPublisherAccount: false,
							lastLoginDate: member.lastLoginDate,
							name: member.name,
							role: getRolesList(member.accountBriefs),
							userId: member.id,
						} as MemberProps;
					}
				);

				membersList.forEach((member: MemberProps) => {
					const rolesList = member.role.split(', ');

					customerRoles.forEach((customerRole) => {
						if (rolesList.find((role) => role === customerRole)) {
							member.isCustomerAccount = true;
						}
					});

					publisherRoles.forEach((publisherRole) => {
						if (rolesList.find((role) => role === publisherRole)) {
							member.isPublisherAccount = true;
						}
					});
				});

				let filteredMembersList: MemberProps[] = [];

				filteredMembersList = membersList.filter(
					(member: MemberProps) => {
						if (
							member.accountBriefs.find(
								(accountBrief: AccountBriefProps) =>
									accountBrief.externalReferenceCode ===
									selectedAccount.externalReferenceCode
							) &&
							member.isPublisherAccount
						) {
							return true;
						}

						return false;
					}
				);

				setMembers(filteredMembersList);
			}
		})();
	}, [selectedNavigationItem, selectedAccount]);

	return (
		<div className="published-apps-dashboard-page-container">
			<DashboardNavigation
				accountAppsNumber={apps.length.toString()}
				accountIcon={accountLogo}
				accounts={accounts}
				currentAccount={selectedAccount}
				dashboardNavigationItems={dashboardNavigationItems}
				onSelectAppChange={setSelectedApp}
				selectedApp={selectedApp}
				setDashboardNavigationItems={setDashboardNavigationItems}
				setSelectedAccount={setSelectedAccount}
			/>

			{selectedNavigationItem === 'Apps' && (
				<DashboardPage
					buttonMessage="+ New App"
					dashboardNavigationItems={dashboardNavigationItems}
					messages={appMessages}
				>
					<DashboardTable<AppProps>
						emptyStateMessage={appMessages.emptyStateMessage}
						items={publishedAppTable.items}
						tableHeaders={appTableHeaders}
					>
						{(item) => (
							<PublishedAppsDashboardTableRow
								item={item}
								key={item.name}
							/>
						)}
					</DashboardTable>

					{publishedAppTable.items.length ? (
						<ClayPaginationBarWithBasicItems
							active={page}
							activeDelta={publishedAppTable.pageSize}
							defaultActive={1}
							ellipsisBuffer={3}
							ellipsisProps={{
								'aria-label': 'More',
								'title': 'More',
							}}
							onActiveChange={setPage}
							showDeltasDropDown={false}
							totalItems={publishedAppTable.totalCount}
						/>
					) : (
						<></>
					)}
				</DashboardPage>
			)}

			{selectedNavigationItem === 'Members' && (
				<DashboardPage
					dashboardNavigationItems={dashboardNavigationItems}
					messages={memberMessages}
				>
					{selectedMember ? (
						<MemberProfile
							member={selectedMember}
							setSelectedMember={setSelectedMember}
						></MemberProfile>
					) : (
						<DashboardTable<MemberProps>
							emptyStateMessage={memberMessages.emptyStateMessage}
							items={members}
							tableHeaders={memberTableHeaders}
						>
							{(item) => (
								<DashboardMemberTableRow
									item={item}
									key={item.name}
									onSelectedMemberChange={setSelectedMember}
								/>
							)}
						</DashboardTable>
					)}
				</DashboardPage>
			)}

			{selectedNavigationItem === 'Account' && <AccountDetailsPage />}
		</div>
	);
}
