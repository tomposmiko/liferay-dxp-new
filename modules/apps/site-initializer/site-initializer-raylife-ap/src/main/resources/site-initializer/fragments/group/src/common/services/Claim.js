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

import {axios} from './liferay/api';

const DeliveryAPI = 'o/c/raylifeclaims';

export function getTotalClaims() {
	return axios.get(`${DeliveryAPI}/`);
}

export function getActiveClaims() {
	return axios.get(`${DeliveryAPI}/?filter=claimStatus ne 'declined'`);
}

export function getClaimsStatus(status) {
	return axios.get(`${DeliveryAPI}/?filter=claimStatus eq '${status}'`);
}

export function getClaimsByPeriodSettled(
	currentYear,
	currentMonth,
	currentDay,
	periodYear,
	periodMonth,
	periodDay
) {
	return axios.get(
		`${DeliveryAPI}/?fields=claimCreateDate,claimStatus,r_policyToClaims_c_raylifePolicyERC&pageSize=200&filter=claimStatus ne 'declined' and claimCreateDate le ${currentYear}-${currentMonth}-${currentDay} and claimCreateDate ge ${periodYear}-${periodMonth}-${periodDay}`
	);
}

export function getClaimsByPeriod(
	currentYear,
	currentMonth,
	currentDay,
	periodYear,
	periodMonth,
	periodDay
) {
	return axios.get(
		`${DeliveryAPI}/?fields=claimStatus,claimCreateDate,claimAmount&pageSize=200&filter=claimCreateDate ge ${currentYear}-${currentMonth}-${currentDay} and claimCreateDate le ${periodYear}-${periodMonth}-${periodDay}`
	);
}

export function getClaims() {
	return axios.get(
		`${DeliveryAPI}/?fields=claimStatus,claimCreateDate,claimAmount,r_policyToClaims_c_raylifePolicyERC&pageSize=200`
	);
}

export function getSettledClaims(
	currentYear,
	currentMonth,
	currentDay,
	periodYear,
	periodMonth,
	periodDay
) {
	return axios.get(
		`${DeliveryAPI}/?fields=claimCreateDate,claimStatus,r_policyToClaims_c_raylifePolicyERC&pageSize=200&filter=claimStatus eq 'settled' and claimCreateDate le ${currentYear}-${currentMonth}-${currentDay} and claimCreateDate ge ${periodYear}-${periodMonth}-${periodDay}`
	);
}
