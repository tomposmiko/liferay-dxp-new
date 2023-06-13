/* eslint-disable radix */
/* eslint-disable @liferay/portal/no-global-fetch */
/* eslint-disable eqeqeq */
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

const grantLimit = [];
const fundsLimit = [];
const serviceHoursLimit = [];
const requests = [];
const userId = parseInt(
	document.getElementById('user-id-container').textContent
);
const fundsRequestsByUserId = [];
const totalFundsRequestedById = [];
const serviceHoursRequestsByUserId = [];
const totalHoursRequestedByUserId = [];
const availableFunds = [];
const availableServiceHours = [];

const getGrantLimit = async () => {
	const response = await fetch(`/o/c/evpgrantlimits`, {
		headers: {
			'content-type': 'application/json',
			'x-csrf-token': Liferay.authToken,
		},
		method: 'GET',
	});
	const data = await response.json();
	grantLimit.push(data);
};

getGrantLimit();

const getFundsLimit = async () => {
	await getGrantLimit();

	fundsLimit.push(grantLimit[0].items[0].fundsLimit);
};

const getServiceHoursLimit = async () => {
	await getGrantLimit();

	serviceHoursLimit.push(grantLimit[0].items[0].serviceHoursLimit);
};

getFundsLimit();
getServiceHoursLimit();

const getRequests = async () => {
	const response = await fetch(`/o/c/evprequests`, {
		headers: {
			'content-type': 'application/json',
			'x-csrf-token': Liferay.authToken,
		},
		method: 'GET',
	});

	const data = await response.json();
	requests.push(data);
};

getRequests();

const getFundsRequestByUserId = async () => {
	await getRequests();

	const filteredRequests = requests[0].items.filter(
		(item) =>
			item.creator.id == userId && item.requestStatus.key !== 'rejected'
	);

	filteredRequests.map((item) =>
		fundsRequestsByUserId.push(item.grantAmount)
	);

	totalFundsRequestedById.push(
		fundsRequestsByUserId.reduce((total, quantity) => total + quantity)
	);
};

const getServiceHoursByUserId = async () => {
	await getRequests();

	const filteredRequests = requests[0].items.filter(
		(item) =>
			item.creator.id == userId && item.requestStatus.key !== 'rejected'
	);

	filteredRequests.map((item) =>
		serviceHoursRequestsByUserId.push(item.totalHoursRequested)
	);

	totalHoursRequestedByUserId.push(
		serviceHoursRequestsByUserId.reduce(
			(total, quantity) => total + quantity
		)
	);
};

getFundsRequestByUserId();
getServiceHoursByUserId();

const getAvailableFunds = async () => {
	await Promise.all([getFundsRequestByUserId(), getFundsLimit()]);

	if (fundsLimit[0] - totalFundsRequestedById[0] < 0) {
		availableFunds.push(0);
	}

	availableFunds.push(fundsLimit[0] - totalFundsRequestedById[0]);

	document.querySelector('#available-funds').innerHTML =
		' R$ ' + availableFunds[0];
};

getAvailableFunds();

const getAvailableHours = async () => {
	await Promise.all([getServiceHoursByUserId(), getServiceHoursLimit()]);

	if (serviceHoursLimit[0] - totalHoursRequestedByUserId[0] < 0) {
		availableServiceHours.push(0);
	}

	availableServiceHours.push(
		serviceHoursLimit[0] - totalHoursRequestedByUserId[0]
	);

	document.querySelector('#available-hours').innerHTML =
		availableServiceHours[0] + 'h';
};

getAvailableHours();

const updateUser = async () => {
	await Promise.all([getAvailableFunds(), getAvailableHours()]);

	const customFields = [
		{
			customValue: {
				data: availableServiceHours[0],
			},
			dataType: 'Integer',
			name: 'Service Hours Available',
		},
		{
			customValue: {
				data: availableFunds[0],
			},
			dataType: 'Integer',
			name: 'Funds Available',
		},
	];

	const response = await fetch(
		`/o/headless-admin-user/v1.0/user-accounts/${userId}`,
		{
			body: JSON.stringify({customFields}),
			headers: {
				'content-type': 'application/json',
				'x-csrf-token': Liferay.authToken,
			},
			method: 'PATCH',
		}
	);

	const data = await response.json();

	return data;
};

const updateAvailableValues = async () => {
	await Promise.all([getAvailableHours(), getAvailableFunds()]);

	updateUser();
};

updateAvailableValues();
