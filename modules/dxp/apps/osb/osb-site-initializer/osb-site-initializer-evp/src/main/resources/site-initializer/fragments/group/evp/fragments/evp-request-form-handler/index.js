/* eslint-disable radix */
/* eslint-disable @liferay/portal/no-global-fetch */
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

const userId = parseInt(
	document.getElementById('user-id-container').textContent
);
const userInformation = [];

function main() {
	const requestType = document.querySelector('[name="requestType"]');

	handleDocumentClick(requestType);
}

document.addEventListener('click', main);

function updateValue(requestType) {
	const serviceForm = document.getElementsByClassName('.service-form');
	const grantForm = document.getElementsByClassName('.grant-form');
	const grantRequestType = document.querySelector(
		'[name="grantRequestType"]'
	);
	const grantAmount = document.querySelector('[name="grantAmount"]');
	const grantCombobox = document.querySelector(
		'.grant-combobox div div div div div input'
	);
	const managerEmailAddress = document.querySelector(
		'[name="managerEmailAddress"]'
	);
	const totalHoursRequested = document.querySelector(
		'[name="totalHoursRequested"]'
	);
	const startDate = document.querySelector('[name="startDate"]');
	const endDate = document.querySelector('[name="endDate"]');
	const labelName = document.querySelector(
		'.grant-combobox div div div div div label'
	);

	if (!labelName.innerText.includes('*')) {
		labelName.innerHTML =
			labelName.innerText +
			"<span style='color:#DA1414;margin-left:4px;font-size:15px;'>*</span>";
	}

	if (
		requestType.value === 'grant' &&
		grantForm[0].classList.contains('d-none')
	) {
		grantForm[0].classList.toggle('d-none');
		toggleGrantRequired(grantForm[0]);
		grantCombobox.setAttribute('required', true);

		if (!serviceForm[0].classList.contains('d-none')) {
			managerEmailAddress.value = '';
			totalHoursRequested.value = 0;
			startDate.value = '';
			endDate.value = '';

			serviceForm[0].classList.toggle('d-none');
			toggleServiceRequired(serviceForm[0]);
		}
	}
	if (
		requestType.value === 'service' &&
		serviceForm[0].classList.contains('d-none')
	) {
		serviceForm[0].classList.toggle('d-none');
		toggleServiceRequired(serviceForm[0]);
		grantCombobox.removeAttribute('required');

		if (!grantForm[0].classList.contains('d-none')) {
			grantRequestType.value = '';
			grantAmount.value = 0;

			grantForm[0].classList.toggle('d-none');
			toggleGrantRequired(grantForm[0]);
		}
	}
}

function toggleServiceRequired(service) {
	if (service.querySelector('[name="managerEmailAddress"]').required) {
		service.querySelector('[name="managerEmailAddress"]').required = false;
		service.querySelector('[name="totalHoursRequested"]').required = false;
		service.querySelector('[name="startDate"]').required = false;
		service.querySelector('[name="endDate"]').required = false;
	}
	else {
		service.querySelector('[name="managerEmailAddress"]').required = true;
		service.querySelector('[name="totalHoursRequested"]').required = true;
		service.querySelector('[name="startDate"]').required = true;
		service.querySelector('[name="endDate"]').required = true;
	}
}

function toggleGrantRequired(grant) {
	if (grant.querySelector('[name="grantAmount"]').required) {
		grant.querySelector('[name="grantAmount"]').required = false;
	}
	else {
		grant.querySelector('[name="grantAmount"]').required = true;
	}
}

function handleDocumentClick(requestType) {
	updateValue(requestType);
}

const getUser = async () => {
	const response = await fetch(
		`/o/headless-admin-user/v1.0/user-accounts/${userId}`,
		{
			headers: {
				'content-type': 'application/json',
				'x-csrf-token': Liferay.authToken,
			},
			method: 'GET',
		}
	);

	const data = await response.json();
	userInformation.push(data);
};

getUser();

const grantInput = document.querySelector('input[name="grantAmount"]');
const hoursInput = document.querySelector('input[name="totalHoursRequested"]');
const grantInputDiv = grantInput.parentNode;
const hoursInputDiv = hoursInput.parentNode;
const newParagraph = document.createElement('p');
const message = document.createTextNode('Text');
grantInputDiv.style.position = 'relative';
newParagraph.setAttribute('style', 'display:none');
newParagraph.setAttribute('style', 'color:#a90f0f');
newParagraph.setAttribute('class', 'error-msg');
newParagraph.appendChild(message);

const compareGrants = async () => {
	const grantInputValue = grantInput.value;

	await getUser();

	if (grantInputValue > userInformation[0].customFields[1].customValue.data) {
		grantInputDiv.appendChild(newParagraph);
		newParagraph.style.position = 'absolute';

		document.querySelector('.error-msg').innerText = 'No funds available.';
		document.querySelector('.error-msg').style.display = 'block';
		document.querySelector('button[type="submit"]').disabled = true;
	}
	else {
		document.querySelector('.error-msg').style.display = 'none';
		document.querySelector('button[type="submit"]').disabled = false;
	}
};

grantInput.addEventListener('change', compareGrants);

const compareHours = async () => {
	const hoursInputValue = hoursInput.value;

	await getUser();

	if (hoursInputValue > userInformation[0].customFields[0].customValue.data) {
		hoursInputDiv.appendChild(newParagraph);

		document.querySelector('.error-msg').innerText =
			'No service hours available.';

		document.querySelector('.error-msg').style.display = 'block';
		document.querySelector('button[type="submit"]').disabled = true;
	}
	else {
		document.querySelector('.error-msg').style.display = 'none';
		document.querySelector('button[type="submit"]').disabled = false;
	}
};

hoursInput.addEventListener('change', compareHours);
