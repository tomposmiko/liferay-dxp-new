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

/* eslint-disable no-undef */
const findRequestIdUrl = (paramsUrl) => {
	const splitParamsUrl = paramsUrl.split('?');

	return splitParamsUrl[0];
};

const siteURL = Liferay.ThemeDisplay.getLayoutRelativeURL()
	.split('/')
	.slice(0, 3)
	.join('/');

const currentPath = Liferay.currentURL.split('/');
const mdfRequestId = findRequestIdUrl(currentPath.at(-1));

const updateStatusToApproved = fragmentElement.querySelector(
	'#status-approved'
);

const updateStatusToRequestMoreInfo = fragmentElement.querySelector(
	'#status-request'
);
const updateStatusToMarketingDirectorReview = fragmentElement.querySelector(
	'#status-marketing-director-review'
);

const updateStatusPendingMarketingReview = fragmentElement.querySelector(
	'#pending-marketing-review'
);
const updateStatusToReject = fragmentElement.querySelector('#status-reject');

const updateStatusToCanceled = fragmentElement.querySelector('#status-cancel');

const editButtonManager = fragmentElement.querySelector('#edit-button-manager');

const editButton = fragmentElement.querySelector('#edit-button-user');

if (updateStatusToApproved) {
	updateStatusToApproved.onclick = () =>
		Liferay.Util.openConfirmModal({
			message: 'Do you want to Approve this MDF?',
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					updateStatus('approved');
				}
			},
		});
}

if (updateStatusToRequestMoreInfo) {
	updateStatusToRequestMoreInfo.onclick = () =>
		Liferay.Util.openConfirmModal({
			message: 'Do you want to Request more info for this MDF?',
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					updateStatus('moreInfoRequested');
				}
			},
		});
}

if (updateStatusPendingMarketingReview) {
	updateStatusPendingMarketingReview.onclick = () =>
		Liferay.Util.openConfirmModal({
			message: 'Do you want to Pending Marketing Review for this MDF?',
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					updateStatus('pendingMarketingReview');
				}
			},
		});
}

if (updateStatusToMarketingDirectorReview) {
	updateStatusToMarketingDirectorReview.onclick = () =>
		Liferay.Util.openConfirmModal({
			message: 'Do you want Marketing Director Review in this MDF?',
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					updateStatus('marketingDirectorReview');
				}
			},
		});
}

if (updateStatusToReject) {
	updateStatusToReject.onclick = () =>
		Liferay.Util.openConfirmModal({
			message: 'Do you want to Reject this MDF?',
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					updateStatus('rejected');
				}
			},
		});
}

if (updateStatusToCanceled) {
	updateStatusToCanceled.onclick = () =>
		Liferay.Util.openConfirmModal({
			message: 'Do you want to Cancel this MDF?',
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					updateStatus('canceled');
				}
			},
		});
}
const statusResponse = async () => {
	// eslint-disable-next-line @liferay/portal/no-global-fetch
	const mdfRequestResponse = await fetch(
		`/o/c/mdfrequests/${mdfRequestId}?nestedFields=mdfReqToActs
	`,
		{
			headers: {
				'accept': 'application/json',
				'x-csrf-token': Liferay.authToken,
			},
		}
	);

	return mdfRequestResponse.json();
};

const getMDFRequestStatus = async () => {
	const mdfRequest = await statusResponse();
	if (mdfRequest) {
		fragmentElement.querySelector(
			'#mdf-request-status-display'
		).innerHTML = `Status: ${Liferay.Util.escape(
			mdfRequest.mdfRequestStatus.name
		)}`;

		updateButtons(mdfRequest.mdfRequestStatus.key);

		return;
	}

	Liferay.Util.openToast({
		message: 'An unexpected error occured.',
		type: 'danger',
	});
};

const updateStatus = async (status) => {
	// eslint-disable-next-line @liferay/portal/no-global-fetch
	const statusManagerResponse = await fetch(
		`/o/c/mdfrequests/${mdfRequestId}`,
		{
			body: `{"mdfRequestStatus": "${status}"}`,
			headers: {
				'content-type': 'application/json',
				'x-csrf-token': Liferay.authToken,
			},
			method: 'PUT',
		}
	);

	if (statusManagerResponse.ok) {
		if (status === 'approved') {
			await updateStatusActivities(status);
		}

		location.reload();

		return;
	}

	Liferay.Util.openToast({
		message: 'The MDF Request Status cannot be changed.',
		type: 'danger',
	});
};

const updateStatusActivities = async (status) => {
	const mdfRequest = await statusResponse();

	await Promise.all(
		mdfRequest.mdfReqToActs.map((activity) => {
			if (activity.activityStatus.key === 'submitted') {
				// eslint-disable-next-line @liferay/portal/no-global-fetch
				return fetch(`/o/c/activities/${activity.id}`, {
					body: `{"activityStatus": "${status}"}`,
					headers: {
						'content-type': 'application/json',
						'x-csrf-token': Liferay.authToken,
					},
					method: 'PUT',
				});
			}
		})
	);
};

const updateButtons = (mdfRequestStatusKey) => {
	if (
		!editButtonManager &&
		(mdfRequestStatusKey === 'draft' ||
			mdfRequestStatusKey === 'moreInfoRequested')
	) {
		editButton.classList.toggle('d-flex');
	}

	if (mdfRequestStatusKey === 'pendingMarketingReview') {
		if (updateStatusToMarketingDirectorReview) {
			updateStatusToMarketingDirectorReview.classList.toggle('d-flex');
		}
		if (updateStatusToRequestMoreInfo) {
			updateStatusToRequestMoreInfo.classList.toggle('d-flex');
		}
		if (updateStatusToReject) {
			updateStatusToReject.classList.toggle('d-flex');
		}
		if (updateStatusToCanceled) {
			updateStatusToCanceled.classList.toggle('d-flex');
		}
		if (updateStatusToApproved) {
			updateStatusToApproved.classList.toggle('d-flex');
		}
	}

	if (mdfRequestStatusKey === 'approved') {
		if (updateStatusToCanceled) {
			updateStatusToCanceled.classList.toggle('d-flex');
		}
	}

	if (mdfRequestStatusKey === 'marketingDirectorReview') {
		if (updateStatusPendingMarketingReview) {
			updateStatusPendingMarketingReview.classList.toggle('d-flex');
		}
		if (updateStatusToApproved) {
			updateStatusToApproved.classList.toggle('d-flex');
		}
		if (updateStatusToRequestMoreInfo) {
			updateStatusToRequestMoreInfo.classList.toggle('d-flex');
		}
		if (updateStatusToReject) {
			updateStatusToReject.classList.toggle('d-flex');
		}
		if (updateStatusToCanceled) {
			updateStatusToCanceled.classList.toggle('d-flex');
		}
	}

	if (mdfRequestStatusKey === 'moreInfoRequested') {
		if (updateStatusToMarketingDirectorReview) {
			updateStatusToMarketingDirectorReview.classList.toggle('d-flex');
		}
		if (updateStatusToCanceled) {
			updateStatusToCanceled.classList.toggle('d-flex');
		}
	}

	if (mdfRequestStatusKey === 'expired') {
		if (updateStatusToMarketingDirectorReview) {
			updateStatusToMarketingDirectorReview.classList.toggle('d-flex');
		}
		if (updateStatusToCanceled) {
			updateStatusToCanceled.classList.toggle('d-flex');
		}
		if (updateStatusToRequestMoreInfo) {
			updateStatusToRequestMoreInfo.classList.toggle('d-flex');
		}
	}
	if (mdfRequestStatusKey === 'rejected') {
		if (updateStatusPendingMarketingReview) {
			updateStatusPendingMarketingReview.classList.toggle('d-flex');
		}
		if (updateStatusToMarketingDirectorReview) {
			updateStatusToMarketingDirectorReview.classList.toggle('d-flex');
		}
		if (updateStatusToRequestMoreInfo) {
			updateStatusToRequestMoreInfo.classList.toggle('d-flex');
		}
		if (updateStatusToCanceled) {
			updateStatusToCanceled.classList.toggle('d-flex');
		}
	}

	if (mdfRequestStatusKey === 'canceled') {
		if (updateStatusToApproved) {
			updateStatusToApproved.classList.toggle('d-flex');
		}
	}

	if (mdfRequestStatusKey === 'draft') {
		if (updateStatusPendingMarketingReview) {
			updateStatusPendingMarketingReview.classList.toggle('d-flex');
		}
		if (updateStatusToMarketingDirectorReview) {
			updateStatusToMarketingDirectorReview.classList.toggle('d-flex');
		}
		if (updateStatusToCanceled) {
			updateStatusToCanceled.classList.toggle('d-flex');
		}
	}

	if (editButton) {
		editButton.onclick = () =>
			Liferay.Util.navigate(
				`${siteURL}/marketing/mdf-requests/new/#/${mdfRequestId}`
			);
	}

	if (editButtonManager) {
		editButtonManager.onclick = () =>
			Liferay.Util.navigate(
				`${siteURL}/marketing/mdf-requests/new/#/${mdfRequestId}`
			);
	}
};

if (layoutMode !== 'edit') {
	getMDFRequestStatus();
}
