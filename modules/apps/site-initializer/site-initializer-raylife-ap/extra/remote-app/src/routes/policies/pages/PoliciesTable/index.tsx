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

import ClayButton, {ClayButtonWithIcon} from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayForm, {ClayCheckbox, ClayInput} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayLabel from '@clayui/label';
import {ClayPaginationWithBasicItems} from '@clayui/pagination';
import ClayPaginationBar from '@clayui/pagination-bar';
import classNames from 'classnames';
import {useEffect, useState} from 'react';

import Header from '../../../../common/components/header';
import Table from '../../../../common/components/table';
import {
	Parameters,
	deletePolicyByExternalReferenceCode,
	getNotExpiredPolicies,
} from '../../../../common/services';
import {getPicklistByName} from '../../../../common/services/Picklists';
import {getProducts} from '../../../../common/services/Products';
import formatDate from '../../../../common/utils/dateFormatter';
import {redirectTo} from '../../../../common/utils/liferay';
import useDebounce from '../../../../hooks/useDebounce';

type Policy = {
	commission: number;
	endDate: string;
	externalReferenceCode: string;
	policyOwnerName: string;
	productName: string;
	startDate: string;
	termPremium: number;
};

type TableContent = {[keys: string]: string};

type TableItemType = {
	bold: boolean;
	clickable: boolean;
	key: string;
	type: string;
	value: string;
};

type TableRowContentType = {[keys: string]: string};

type itemsPolicies = {
	[keys: string]: string;
};

type itemsPolicyFilter = {
	policyStatus: {name: string};
	productName: string;
};

type itemsProducts = {
	[keys: string]: string;
};

type itemsPicklists = {
	[keys: string]: string;
};

const daysToExpirePolicyAlert = 15;

const PoliciesTable = () => {
	const [policies, setPolicies] = useState<TableContent[]>([]);
	const [totalCount, setTotalCount] = useState<number>(0);
	const [pageSize, setPageSize] = useState<number>(5);
	const [totalPages, setTotalPages] = useState<number>(0);
	const [page, setPage] = useState<number>(1);
	const [firstPaginationLabel, setFirstPaginationLabel] = useState<number>(1);
	const [secondPaginationLabel, setSecondPaginationLabel] = useState<number>(
		1
	);
	const [hasRedLine, setHasRedLine] = useState<boolean>(false);

	const [searchInput, setSearchInput] = useState('');

	const [activeFilter, setActiveFilter] = useState(true);
	const [productFilterItems, setProductFilterItems] = useState<string[]>([]);
	const [statusFilterItems, setStatusFilterItems] = useState<string[]>([]);
	const [filterProductCheck, setFilterProductCheck] = useState<string[]>([]);
	const [filterStatusCheck, setFilterStatusCheck] = useState<string[]>([]);
	const [filterCheckedLabel, setFilterCheckedLabel] = useState<string[]>([]);
	const [checkedStateProduct, setCheckedStateProduct] = useState<boolean[]>(
		[]
	);
	const [checkedStateStatus, setCheckedStateStatus] = useState<boolean[]>([]);

	const filterSearch = `contains(policyOwnerName,'${searchInput}') or contains(policyOwnerEmail, '${searchInput}') or contains(externalReferenceCode, '${searchInput}')`;

	const filterProduct = !filterStatusCheck.length
		? `productName in (${filterProductCheck})`
		: `policyStatus in (${filterStatusCheck}) and productName in (${filterProductCheck})`;

	const filterStatus = !filterProductCheck.length
		? `policyStatus in (${filterStatusCheck})`
		: `policyStatus in (${filterStatusCheck}) and productName in (${filterProductCheck})`;

	const generateParameters = (filtered?: string) => {
		const parameters: Parameters =
			filtered === undefined
				? {page: '0', pageSize: '0', sort: 'endDate:asc'}
				: {
						filter: filtered,
						page: '0',
						pageSize: '0',
						sort: 'endDate:asc',
				  };

		return parameters;
	};

	const [parameters, setParameters] = useState<Parameters>(
		generateParameters()
	);

	const parameterDebounce = useDebounce(parameters, 200);

	parameters.pageSize = pageSize.toString();
	parameters.page = page.toString();

	const handleChangeSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
		setSearchInput(event.target.value);
	};

	const conditionalFilters = () => {
		setPage(1);

		if (
			searchInput &&
			!filterStatusCheck.length &&
			!filterProductCheck.length
		) {
			return setParameters(generateParameters(filterSearch));
		}

		if (searchInput && filterStatusCheck.length) {
			return setParameters(
				generateParameters(filterSearch + ' and ' + filterStatus)
			);
		}

		if (searchInput && filterProductCheck.length) {
			return setParameters(
				generateParameters(filterSearch + ' and ' + filterProduct)
			);
		}

		if (
			!searchInput &&
			(filterStatusCheck.length || filterProductCheck.length)
		) {
			if (filterStatusCheck.length) {
				return setParameters(generateParameters(filterStatus));
			}
			if (filterProductCheck.length) {
				return setParameters(generateParameters(filterProduct));
			}
		}
		if (
			!searchInput &&
			!filterStatusCheck.length &&
			!filterProductCheck.length
		) {
			return setParameters(generateParameters());
		}
	};

	const handleClick = () => {
		conditionalFilters();
	};
	const filterClick = () => {
		setActiveFilter(!activeFilter);
	};

	const itemsCreate = (listItem: string[], checkedItem: boolean[] | []) => {
		const itemsFilters = listItem.map(
			(statusName: string, index: number) => {
				const item: Array<{checked: boolean; item: string}> = [];
				item.push({
					checked: checkedItem[index],
					item: statusName,
				});

				return item;
			}
		);

		return itemsFilters;
	};

	const itemProducts = itemsCreate(productFilterItems, checkedStateProduct);
	const itemStatus = itemsCreate(statusFilterItems, checkedStateStatus);

	useEffect(() => {
		setCheckedStateProduct(
			new Array(productFilterItems.length).fill(false)
		);
		setCheckedStateStatus(new Array(statusFilterItems.length).fill(false));
	}, [productFilterItems.length, statusFilterItems.length]);

	useEffect(() => {
		conditionalFilters();

		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [filterProductCheck, filterStatusCheck, filterCheckedLabel]);

	const handleKeyDown = (event: React.KeyboardEvent<HTMLElement>) => {
		if (event.key === 'Enter') {
			handleClick();
		}
	};
	const checkItemProduct = (productCheck: string) => {
		setFilterCheckedLabel((old: string[]) => [...old, productCheck]);

		if (!filterProductCheck.includes(productCheck)) {
			setFilterProductCheck((old: string[]) => [
				...old,
				`'${productCheck}'`,
			]);
		}

		return filterProductCheck;
	};

	const uncheckItemProduct = (productName: string) => {
		setFilterCheckedLabel(
			filterCheckedLabel.filter((currentProductName: string) => {
				return productName !== currentProductName;
			})
		);

		setFilterProductCheck(
			filterProductCheck.filter((currentProductName: string) => {
				return currentProductName !== `'${productName}'`;
			})
		);
	};

	const handleProductCheck = (checkedIndex: number, productName: string) => {
		const updatedCheckedState = checkedStateProduct.map(
			(checked: boolean, index: number) => {
				if (index === checkedIndex) {
					return !checked;
				}

				return checked;
			}
		);

		setCheckedStateProduct(updatedCheckedState);

		if (!checkedStateProduct[checkedIndex]) {
			return checkItemProduct(productName);
		}

		return uncheckItemProduct(productName);
	};

	const checkItemStatus = (statusName: string) => {
		setFilterCheckedLabel((old: string[]) => [...old, statusName]);
		if (!filterStatusCheck.includes(statusName)) {
			setFilterStatusCheck((old: string[]) => [
				...old,
				`'${statusName.toLowerCase()}'`,
			]);
		}

		return filterStatusCheck;
	};

	const uncheckItemStatus = (statusName: string) => {
		setFilterCheckedLabel(
			filterCheckedLabel.filter((currentStatusName: string) => {
				return statusName !== currentStatusName;
			})
		);

		setFilterStatusCheck(
			filterStatusCheck.filter((currentStatusName: string) => {
				return currentStatusName !== `'${statusName?.toLowerCase()}'`;
			})
		);
	};

	const handleStatusCheck = (checkedIndex: number, statusName: string) => {
		const updatedCheckedState = checkedStateStatus.map(
			(checked: boolean, index: number) => {
				if (index === checkedIndex) {
					return !checked;
				}

				return checked;
			}
		);

		setCheckedStateStatus(updatedCheckedState);

		if (!checkedStateStatus[checkedIndex]) {
			return checkItemStatus(statusName);
		}

		return uncheckItemStatus(statusName);
	};

	const onClickLabel = (currentFilterName: string) => {
		if (statusFilterItems.includes(currentFilterName)) {
			setFilterStatusCheck(
				filterStatusCheck.filter((statusName: string) => {
					return (
						statusName !== `'${currentFilterName?.toLowerCase()}'`
					);
				})
			);
		}
		else {
			setFilterProductCheck(
				filterProductCheck.filter((productName: string) => {
					return productName !== `'${currentFilterName}'`;
				})
			);
		}

		setFilterCheckedLabel(
			filterCheckedLabel.filter((filterNameActived: string) => {
				return filterNameActived !== currentFilterName;
			})
		);

		const updatedCheckedStateProduct = checkedStateProduct.map(
			(checked: boolean, index: number) => {
				const productName = itemProducts[index]?.[0]?.item;

				if (currentFilterName === productName) {
					return !checked;
				}

				return checked;
			}
		);

		setCheckedStateProduct(updatedCheckedStateProduct);

		const updatedCheckedStateStatus = checkedStateStatus.map(
			(checked: boolean, index: number) => {
				const statusName = itemStatus[index]?.[0]?.item;

				if (currentFilterName === statusName) {
					return !checked;
				}

				return checked;
			}
		);

		setCheckedStateStatus(updatedCheckedStateStatus);
	};

	const HEADERS = [
		{
			bold: false,
			key: 'renewalDue',
			redColor: hasRedLine,
			type: 'status',
			value: 'Renewal Due',
		},
		{
			key: 'productName',
			value: 'Product',
		},
		{
			bold: true,
			clickable: true,
			key: 'externalReferenceCode',
			type: 'link',
			value: 'Policy Number',
		},
		{
			greyColor: true,
			key: 'policyOwnerName',
			value: 'Name',
		},
		{
			greyColor: true,
			key: 'monthlyPremium',
			type: 'status',
			value: 'Monthly Premium',
		},
		{
			greyColor: true,
			key: 'policyPeriod',
			value: 'Policy Period',
		},
		{
			greyColor: true,
			key: 'commission',
			type: 'status',
			value: 'Commission',
		},
	];

	const handleDeletePolicy = (externalReferenceCode: string) => {
		deletePolicyByExternalReferenceCode(externalReferenceCode);

		const filteredPolicies = policies.filter(
			(policy) => !(policy.key === externalReferenceCode)
		);

		setPolicies(filteredPolicies);
		setTotalCount(totalCount - 1);
	};

	const handleEditPolicy = (externalReferenceCode: string) => {
		alert(`Edit ${externalReferenceCode} Action`);
	};

	useEffect(() => {
		getNotExpiredPolicies(generateParameters()).then((results) => {
			const allItems: itemsPolicies[] = [];
			results?.data?.items?.forEach(
				({policyStatus: {name}, productName}: itemsPolicyFilter) => {
					allItems.push({
						name,
						productName,
					});
				}
			);
		});

		getProducts().then((results) => {
			const productsResult = results?.data?.items;

			const products = productsResult.map((product: itemsProducts) => {
				return product.name;
			});

			setProductFilterItems(products);
		});

		getPicklistByName('PolicyStatus').then((results) => {
			const policyStatusResult = results?.data?.listTypeEntries;

			const policyStatuses = policyStatusResult.map(
				(policyStatusPicklist: itemsPicklists) => {
					return policyStatusPicklist.name;
				}
			);

			setStatusFilterItems(policyStatuses);
		});

		return;

		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	useEffect(() => {
		getNotExpiredPolicies(parameterDebounce).then((results) => {
			const policiesList: TableContent[] = [];
			results?.data?.items?.forEach(
				({
					commission,
					endDate,
					externalReferenceCode,
					policyOwnerName,
					productName,
					startDate,
					termPremium,
				}: Policy) => {
					const policyEndDate = Date.parse(endDate);

					const currentDate: Date = new Date();

					const differenceOfDays =
						Number(policyEndDate) - Number(currentDate);

					const renewalDue =
						Math.floor(differenceOfDays / (1000 * 60 * 60 * 24)) +
						1;

					setHasRedLine(
						renewalDue >= 0 && renewalDue < daysToExpirePolicyAlert
					);

					const renewalDueDisplayRules = () => {
						if (renewalDue < 0) {
							return 'Expired';
						}
						if (renewalDue === 0) {
							return 'Due Today';
						}
						else {
							return renewalDue;
						}
					};

					const policyPeriod = `${formatDate(
						new Date(startDate),
						true
					)} - ${formatDate(new Date(endDate), true)}`;

					const monthlyPremium = termPremium / 12;

					policiesList.push({
						commission: `$${commission.toFixed(2)}`,
						externalReferenceCode,
						isExpiring: (renewalDue < 0).toString(),
						isRedLine: (
							renewalDue >= 0 &&
							renewalDue < daysToExpirePolicyAlert
						).toString(),
						key: externalReferenceCode,
						monthlyPremium: `$${monthlyPremium.toFixed(2)}`,
						policyOwnerName,
						policyPeriod: `${policyPeriod}`,
						productName,
						renewalDue: `${renewalDueDisplayRules()}`,
						renewalDueCalculation: `${
							renewalDue >= 0 ? renewalDue : null
						}`,
					});

					policiesList.sort((firstPolicy, secondPolicy) =>
						Number(firstPolicy.renewalDueCalculation) >
						Number(secondPolicy.renewalDueCalculation)
							? 1
							: Number(secondPolicy.renewalDueCalculation) >
							  Number(firstPolicy.renewalDueCalculation)
							? -1
							: 0
					);
				}
			);
			setPolicies(policiesList);

			const totalCount = results?.data?.totalCount;
			setTotalCount(totalCount);

			const totalPages = Math.ceil(totalCount / pageSize);
			setTotalPages(totalPages);

			const firstPaginationLabel = (page - 1) * pageSize + 1;
			setFirstPaginationLabel(firstPaginationLabel);

			const secondPaginationLabel =
				totalCount > page * pageSize ? page * pageSize : totalCount;
			setSecondPaginationLabel(secondPaginationLabel);
		});
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [pageSize, page, searchInput, filterCheckedLabel, parameterDebounce]);

	const title = `Policies (${totalCount})`;

	const handleRedirectToGmail = (email: string) => {
		window.location.href = `mailto:${email}`;
	};

	const handleRedirectToDetailsPages = (
		externalReferenceCode: string,
		entity: string
	) => {
		redirectTo(`${entity}?externalReferenceCode=${externalReferenceCode}`);
	};

	const onClickRules = (
		item: TableItemType,
		rowContent: TableRowContentType
	) => {
		if (item.clickable && item.key === 'email') {
			handleRedirectToGmail(rowContent[item.key]);
		}

		if (
			rowContent['productName'] === 'Auto' &&
			item.key === 'externalReferenceCode'
		) {
			handleRedirectToDetailsPages(
				rowContent['externalReferenceCode'],
				'policy-details'
			);
		}
	};

	return (
		<div className="ray-dashboard-recent-policies">
			<div className="d-flex justify-content-between responsive-search">
				<Header className="mb-5 pt-3 px-4" title={title} />

				<ClayForm.Group className="flex-row mt-3 px-3">
					<ClayInput.Group className="justify-content-between">
						<ClayInput.GroupItem prepend>
							<ClayInput
								onChange={handleChangeSearch}
								onKeyDown={handleKeyDown}
								placeholder="Search for..."
								type="text"
							/>
						</ClayInput.GroupItem>

						<ClayInput.GroupItem append shrink>
							<ClayButton
								displayType="secondary"
								onClick={handleClick}
								type="submit"
							>
								<ClayIcon symbol="search" />
							</ClayButton>
						</ClayInput.GroupItem>

						<ClayButtonWithIcon
							displayType="secondary"
							onClick={filterClick}
							symbol="filter"
							type="submit"
						></ClayButtonWithIcon>
					</ClayInput.Group>
				</ClayForm.Group>
			</div>

			<div
				className={classNames('mb-2 mr-3 ', {
					'd-flex': !activeFilter,
					'd-none': activeFilter,
				})}
			>
				<div className="d-flex justify-content-center ml-4 responsive-filter">
					<div className="d-flex">
						<ClayDropDown
							className='"d-flex mr-3"'
							trigger={
								<ClayButton
									className="btn-sm hover-button-dropdown mr-3 shadow-none text-neutral-8"
									displayType="secondary"
								>
									Product
									<ClayIcon symbol="caret-double-l" />
								</ClayButton>
							}
						>
							<ClayDropDown.ItemList className="border-neutral-6 ml-3 mt-2">
								{productFilterItems.map(
									(
										productName: string,
										checkedIndex: number
									) => (
										<ClayCheckbox
											checked={
												checkedStateProduct[
													checkedIndex
												]
											}
											key={checkedIndex}
											label={
												itemProducts[checkedIndex]?.[0]
													?.item
											}
											onChange={() =>
												handleProductCheck(
													checkedIndex,
													productName
												)
											}
										/>
									)
								)}
							</ClayDropDown.ItemList>
						</ClayDropDown>

						<ClayDropDown
							className="mr-3"
							trigger={
								<ClayButton
									className="btn-sm hover-button-dropdown mr-2 shadow-none text-neutral-8"
									displayType="secondary"
								>
									Status
									<ClayIcon symbol="caret-double-l" />
								</ClayButton>
							}
						>
							<ClayDropDown.ItemList className="border-neutral-6 ml-3 mt-2">
								{statusFilterItems.map(
									(
										statusName: string,
										checkedIndex: number
									) => (
										<ClayCheckbox
											checked={
												checkedStateStatus[checkedIndex]
											}
											key={checkedIndex}
											label={
												itemStatus[checkedIndex]?.[0]
													?.item
											}
											onChange={() => {
												handleStatusCheck(
													checkedIndex,
													statusName
												);
											}}
										/>
									)
								)}
							</ClayDropDown.ItemList>
						</ClayDropDown>
					</div>

					<div
						className={classNames(
							'responsive-label flex-wrap w-100 align-items-center',
							{
								'd-flex': filterCheckedLabel.length,
								'd-none': !filterCheckedLabel.length,
							}
						)}
					>
						{filterCheckedLabel.map(
							(currentFilterName: string, index: number) => (
								<ClayLabel
									className="align-items-center d-flex justify-content-center label label-primary mr-2"
									displayType="unstyled"
									key={index}
								>
									<div className="align-items-center d-flex justify-content-center ml-1">
										{`${currentFilterName}`}

										<ClayIcon
											className="cursor-pointer d-flex m-2"
											onClick={() => {
												onClickLabel(currentFilterName);
											}}
											symbol="times"
										/>
									</div>
								</ClayLabel>
							)
						)}

						<div>
							<ClayButton
								className="btn-sm hover-button shadow-none text-neutral-9"
								onClick={() => {
									const updatedCheckedProduct = checkedStateProduct.fill(
										false
									);

									const updatedCheckedStatus = checkedStateStatus.fill(
										false
									);

									setCheckedStateProduct(
										updatedCheckedProduct
									);
									setCheckedStateStatus(updatedCheckedStatus);
									setFilterCheckedLabel([]);
									setFilterProductCheck([]);
									setFilterStatusCheck([]);
									setParameters(generateParameters());
								}}
							>
								<ClayIcon
									className="mr-1"
									symbol="times-circle"
								/>
								Clear
							</ClayButton>
						</div>
					</div>
				</div>
			</div>

			<Table
				actions={[
					{
						action: handleEditPolicy,
						value: 'Edit',
					},
					{
						action: handleDeletePolicy,
						value: 'Delete',
					},
				]}
				data={policies}
				headers={HEADERS}
				onClickRules={onClickRules}
			/>

			<div className="d-flex justify-content-between mt-3">
				<ClayPaginationBar>
					<ClayPaginationBar.DropDown
						items={[
							{
								label: '5',
								onClick: () => {
									setPageSize(5);
									setPage(1);
								},
							},
							{
								label: '10',
								onClick: () => {
									setPageSize(10);
									setPage(1);
								},
							},
							{
								label: '20',
								onClick: () => {
									setPageSize(20);
									setPage(1);
								},
							},
							{
								label: '30',
								onClick: () => {
									setPageSize(30);
									setPage(1);
								},
							},
							{
								href: '#3',
								label: '50',
								onClick: () => {
									setPageSize(50);
									setPage(1);
								},
							},
							{
								label: '75',
								onClick: () => {
									setPageSize(75);
									setPage(1);
								},
							},
						]}
						trigger={
							<ClayButton displayType="unstyled">
								{pageSize}
								&nbsp;Entries
								<ClayIcon symbol="caret-double-l" />
							</ClayButton>
						}
					/>

					<ClayPaginationBar.Results>
						Showing {firstPaginationLabel}
						&nbsp;to&nbsp;
						{secondPaginationLabel} of {totalCount} entries.
					</ClayPaginationBar.Results>
				</ClayPaginationBar>

				<ClayPaginationWithBasicItems
					activePage={page}
					ellipsisBuffer={2}
					onPageChange={(page: number) => setPage(page)}
					totalPages={totalPages}
				/>
			</div>
		</div>
	);
};

export default PoliciesTable;
