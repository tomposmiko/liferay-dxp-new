/* eslint-disable no-undef */
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

const currentPath = Liferay.currentURL.split('/');
const mdfRequestId = +currentPath[currentPath.length - 1];

const getMDFRequestStatus = async () => {
	// eslint-disable-next-line @liferay/portal/no-global-fetch
	const statusResponse = await fetch(`/o/c/mdfrequests/${mdfRequestId}`, {
		headers: {
			'accept': 'application/json',
			'x-csrf-token': Liferay.authToken,
		},
	});

	if (statusResponse.ok) {
		const data = await statusResponse.json();

		fragmentElement.querySelector(
			'#mdfRequestStatusDisplay'
		).innerHTML = `Status: ${data.requestStatus}`;

		return;
	}
	Liferay.Util.openToast({
		message: 'An unexpected error occured.',
		type: 'danger',
	});
};

getMDFRequestStatus();
